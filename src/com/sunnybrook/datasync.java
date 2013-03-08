package com.sunnybrook;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

public class datasync extends Thread{

	private sysconfig myConfig;
	private Context mContext;
	private Handler mHandler;
	private WifiManager mWifi;
	private localDB myLocalDB;
	private remoteDB myRemoteDB;
	private static boolean is_running = false;
	private int mNewOrders = 0;
	private String errMsg;

	private String TAG = datasync.class.getSimpleName();
	
	private void updateStatus(String _msg) {
		errMsg = _msg;
		updateStatus(Consts.DATASYNC_RUNNING,_msg);
	}
	
	private void updateStatus(int _arg1, String _msg) {
		Message msg = mHandler.obtainMessage(Consts.MSG_DATASYNC_STATUS);
		msg.arg1 = _arg1;
		msg.arg2 = mNewOrders;
		msg.obj = _msg;
		mHandler.sendMessage(msg);
		SysLog.appendLog("INFO",TAG,_msg);
	}
	
	public datasync(Handler _handler, localDB _localdb, WifiManager _wifi) {
		super();
		mHandler = _handler;
		mWifi = _wifi;
		myLocalDB = _localdb;
		myConfig = new sysconfig(myLocalDB);
	}
	
	public datasync(Handler _handler, Context _context) {
		super();
		mHandler = _handler;
		mContext = _context;
		myLocalDB = new localDB(_context);
		mWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		myConfig = new sysconfig(myLocalDB);
	}
	
	public boolean is_running() {
		return is_running;
	}

	public void run() {
		myConfig.refresh(myLocalDB);
		if(myConfig.getLabor_code().equals(""))
			errMsg = "Labor code is not set!";
		else if(is_running)	//Another datasync thread is still running.
			errMsg = "Data Sync is running!";
		else {
			is_running=true;	//Set the task running flag
			SysLog.appendLog("INFO", TAG, "Data Sync Start.");
			if(checkWifiStatus()) {
				if(checkRemoteDB()) {
					if(pushData(myLocalDB,myRemoteDB)){
						if(pullData(myLocalDB,myRemoteDB)) {
							if(cleanData(myLocalDB,myRemoteDB)) {
								errMsg = "SyncData Successed.";
							}
						}
					}
					if(myRemoteDB.isConnected()) myRemoteDB.close();
				}
			}
			else
				SysLog.appendLog("INFO", TAG, "No WIFI.");
			SysLog.appendLog("INFO", TAG, "Data Sync End.");
			is_running=false;
		}
		updateStatus(Consts.DATASYNC_FINISHED,errMsg);
	}
	
	private boolean checkWifiStatus() {
		boolean is_Emulator="generic".equals(android.os.Build.BRAND); 
		updateStatus("Checking WIFI connection......");
		WifiInfo mWifiInfo = mWifi.getConnectionInfo();

		if(is_Emulator) return true;
		
		if(!mWifi.isWifiEnabled()) {
			mWifi.setWifiEnabled(true);
			errMsg = "WIFI Connection is enabled.";
			return false;
		}
		if((mWifiInfo.getSSID() == null) || !mWifiInfo.getSSID().replace("\"", "").equals(myConfig.getSsid())) {
			List<WifiConfiguration> mWifiConfList = mWifi.getConfiguredNetworks();
			WifiConfiguration mWifiConfig = null;
			int mNetId = 0;
			for(int i = 0;i<mWifiConfList.size();i++) 
				if(mWifiConfList.get(i).SSID.equals("\"" + myConfig.getSsid() + "\"")) {
					mWifiConfig = mWifiConfList.get(i);
					mNetId = mWifiConfig.networkId;
					mWifiConfig.status = WifiConfiguration.Status.ENABLED;
					updateStatus("WIFI Configuration is found.");
					break;
				}
				if(mWifiConfig == null){
					mWifiConfig = new WifiConfiguration();
					mWifiConfig.SSID = "\"" + myConfig.getSsid() + "\"";
					mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
					mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
					mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
					mWifiConfig.wepKeys[0] = myConfig.getNetwork_key();
					mWifiConfig.wepTxKeyIndex = 0;
					mWifiConfig.priority = 1;
					mWifiConfig.status = WifiConfiguration.Status.ENABLED;
					mNetId = mWifi.addNetwork(mWifiConfig);
					if(mNetId >= 0) {
						mWifiConfig.networkId = mNetId;
						updateStatus("WIFI Configuration is added.");
					}
					else {
						errMsg = "Faild to add WIFI Configuration!";
						return false;
					}
				}
				mWifi.enableNetwork(mNetId, true);
				errMsg = "WIFI Configuration is enabled.";
				return false;
		}
	
		if(mWifiInfo.getIpAddress()==0) {
			errMsg = "Waiting for WIFI connection......";
			return false;
		}
		return true;
	}
	
	private boolean checkRemoteDB(){
		updateStatus("Connecting to RemoteDB......");
		myRemoteDB = new remoteDB(myConfig.getJdbc_url());

		if (!myRemoteDB.isConnected()) {	//Connect to remote DB failed.
			errMsg = "Failed to connect Remote DB!!!!";
			return false;
		}
		else {
			updateStatus("Remote DB is connected.");
			return true;
		}
	}
	
	private boolean pushData(localDB localdb, remoteDB remotedb){
		SysLog.appendLog("DEBUG", TAG +":pushData", "Start pushing own orders ......");
		updateStatus("Pushing local data ......");
		List<ownorder> mOwnOrderList = localdb.getOwnOrderList(myConfig.getLabor_code(), "wonum");
		for(int i=0;i<mOwnOrderList.size();i++ ) {
			if(!remotedb.saveOwnOrder(mOwnOrderList.get(i),localdb)){
				errMsg="Failed to push workorder!";
				return false;
			}
		}
		updateStatus("Pushing local data finished.");
		SysLog.appendLog("DEBUG", TAG +":pushData", "Push own orders end.");
		return true;
	}
	
	private boolean pullData(localDB localdb, remoteDB remotedb) {
		SysLog.appendLog("DEBUG", TAG +":pullData", "Start pulling data......");
		updateStatus("Pulling ownorders......");
		localdb.clearExistedFlag("wo_labor");
		localdb.clearExistedFlag("wo_update");
		if (!pullOwnOrders(localdb,remotedb)) {
			errMsg="Failed to pull own orders!";
			return false;
		}
		updateStatus("Pull own orders finished.");
		if (myConfig.is_super()){
			updateStatus("Pulling superorders ......");
			if(!pullSuperOrders(localdb, remotedb)){
				errMsg = "Failed to pull super orders!";
				return false;
			}
			updateStatus("Pull superorders finished.");
		}
		if (!myConfig.getCraft_list().isEmpty()){
			updateStatus("Pulling craftorders......");
			if(!pullCraftOrders(localdb, remotedb)) {
				errMsg = "Failed to pull craft orders!";
				return false;
			}
			updateStatus("Pull craftorders finished.");
		}

		SysLog.appendLog("DEBUG", TAG +":pullData", "End pulling data.");
		return true;
	}
	
	private boolean cleanData(localDB _localdb,remoteDB _remotedb) {
		updateStatus("Cleaning up data ......");
		SysLog.appendLog("DEBUG", TAG + ":cleanData", "Start cleaning up data ......");
		if(!_localdb.cleanData()) {
			errMsg = "Failed to clean up data!";
			return false;
		}
		SysLog.appendLog("DEBUG", TAG + ":cleanData", "End cleaning up data.");
		updateStatus("Cleaning up data finished.");
		return true;
	}

	private boolean pullOwnOrders(localDB localdb, remoteDB remotedb) {
		SysLog.appendLog("DEBUG", TAG +":pullOwnOrder", "Start pulling remote own workorders......");
		List<ownorder> mList = remotedb.getOwnOrders(myConfig.getLabor_code(),myConfig.getOutstanding_days());
		SysLog.appendLog("DEBUG", TAG +":pullOwnOrder","Start writing local own workorders......");
		mNewOrders = 0;
		for(int i=0;i<mList.size();i++){
			if( (i % 10) == 0) {
				updateStatus(String.format("Pulling ownorders(%d/%d)......",i,mList.size()));
			}
			if(localdb.saveOwnOrder(mList.get(i),myConfig.getLabor_code()))
				mNewOrders ++;
		}
		SysLog.appendLog("DEBUG",TAG + ":pullOwnOrder", "End pull own workorders.");
		return true;
	}
	
	private boolean pullSuperOrders(localDB localdb, remoteDB remotedb) {
		SysLog.appendLog("DEBUG", TAG + ":pullSuperOrder", "Start pulling remote super workorders......");
		List<superorder> mList = remotedb.getSuperOrders(myConfig.getLabor_code());
		SysLog.appendLog("DEBUG", TAG + ":pullSuperOrder", "Start writing local super workorders......");
		for(int i=0;i<mList.size();i++){
			localdb.saveSuperOrder(mList.get(i));
		}
		SysLog.appendLog("DEBUG", TAG +":pullSuperOrder", "End pull super workorders.");
		return true;
	}
	
	private boolean pullCraftOrders(localDB localdb, remoteDB remotedb) {
		SysLog.appendLog("DEBUG", TAG + ":pullCraftOrder", "Start pulling remote craft workorders......");
		List<craftorder> mList = remotedb.getCraftOrders(myConfig.getCraft_list());
		SysLog.appendLog("DEBUG", TAG +":pullCraftOrder", "Start writing local craft workorders......");
		for(craftorder tmpOrder:mList){
			if(!tmpOrder.getLaborCode().equals(myConfig.getLabor_code()))	
				localdb.saveCraftOrder(tmpOrder);
		}
		SysLog.appendLog("DEBUG", TAG + ":pullCraftOrder", "End pull craft workorders.");
		return true;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
}
