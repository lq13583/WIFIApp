package com.sunnybrook;

import java.util.List;
import java.util.TimerTask;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

public class SyncDataTask extends TimerTask {
	public static boolean mEnabled = true; 
	private static int mCounts = 0; 
	private static int mPeriod = 1;
	private static boolean is_running = false;
	private sysconfig myConfig;
	private long maxPeriod;
	private Handler mHandler;
	private WifiManager mWifi;
	private localDB myLocalDB;
	
	public SyncDataTask(Handler _handler,sysconfig _config, localDB _localdb, WifiManager _wifi) {
		super();
		mHandler = _handler;
		myConfig = _config;
		mWifi = _wifi;
		myLocalDB = _localdb;
		maxPeriod = myConfig.getUpdate_int_max()/myConfig.getUpdate_int();
	}
	
	public boolean isRunning() {
		return is_running;
	}
	@Override
	public void run() {
		
		if(myConfig.getLabor_code().equals("")) return;	//Initial setup is not done.

		if(!mEnabled) return;	//SyncData task is disabled.

		if(is_running) return;	//Another SyncData task is still running 

//Task is delayed until mCounts count down to 0
		if (mCounts>0){
			mCounts--;
			return;
		}
		
		if(!checkWifiStatus()) return;
		
		//Set the task running flag
		is_running=true;


//Open remote DB connection

		updateStatus("Connecting to RemoteDB......");
		remoteDB myRemoteDB = new remoteDB(myConfig.getJdbc_url());

		if (!myRemoteDB.isConnected()) {	//Connect to remote DB failed.
			updateStatus("Failed to connect Remote DB!!!!");
//Increase the waiting period if no network connection.		
			if (mPeriod < maxPeriod) {
				mPeriod++;
			}		
			mCounts += mPeriod;
		}
		else {
//Reset waiting period to 1;
			updateStatus("Remote DB is connected.");
			mPeriod = 1;
			if(pushData(myLocalDB,myRemoteDB)){
				if(pullData(myLocalDB,myRemoteDB)) {
					if(cleanData(myLocalDB,myRemoteDB)) {
						updateStatus("SyncData Successed.");
					}
				}
			}
		}


//Clear the task running flag
//		myRemoteDB.finalize();
		is_running = false;
		return;
	}

	private boolean pushData(localDB localdb, remoteDB remotedb){
		SysLog.AppendLog("Debug", "pushData", "Start pushing own orders ......");
		updateStatus("Pushing local data ......");
		List<ownorder> mOwnOrderList = localdb.getOwnOrderList(myConfig.getLabor_code(), "wonum");
		for(int i=0;i<mOwnOrderList.size();i++ ) {
			if(!remotedb.saveOwnOrder(mOwnOrderList.get(i),localdb)) return false;
		}
		updateStatus("Pushing local data finished.");
		SysLog.AppendLog("Debug", "pushData", "Push own orders end.");
		return true;
	}
	
	private boolean pullData(localDB localdb, remoteDB remotedb) {
		SysLog.AppendLog("Debug", "pullData", "Start pulling data......");
		updateStatus("Pulling ownorders......");
		localdb.clearExistedFlag("wo_labor");
		if (!pullOwnOrders(localdb,remotedb)) return false;
		updateStatus("Pull own orders finished.");
		if (myConfig.is_super()){
			updateStatus("Pulling superorders ......");
			if(!pullSuperOrders(localdb, remotedb)) return false;
			updateStatus("Pull superorders finished.");
		}
		if (!myConfig.getCraft_list().isEmpty()){
			updateStatus("Pulling craftorders......");
			if(!pullCraftOrders(localdb, remotedb)) return false;
			updateStatus("Pull craftorders finished.");
		}

		SysLog.AppendLog("Debug", "pullData", "End pulling data.");
		return true;
	}
	
	private boolean cleanData(localDB _localdb,remoteDB _remotedb) {
		SysLog.AppendLog("Debug", "cleanData", "Start cleaning up data ......");
		if(!_localdb.cleanData()) return false;
		SysLog.AppendLog("Debug", "cleanData", "End cleaning up data ......");
		return true;
	}

	private boolean pullOwnOrders(localDB localdb, remoteDB remotedb) {
		String mNewOrders = "";
		SysLog.AppendLog("Debug", "pullOwnOrder", "Start pulling remote own workorders......");
		List<ownorder> mList = remotedb.getOwnOrders(myConfig.getLabor_code());
		SysLog.AppendLog("Debug", "pullOwnOrder","Start writing local own workorders......");
		for(int i=0;i<mList.size();i++){
			if(localdb.saveOwnOrder(mList.get(i),myConfig.getLabor_code()))
				mNewOrders = mNewOrders + mList.get(i).getOrderId() + ",";
		}
		if(mNewOrders.length()>0) {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = 1;
			msg.obj = mNewOrders;
			mHandler.sendMessage(msg);		
		}
		SysLog.AppendLog("Debug","pullOwnOrder", "End pull own workorders.");
		return true;
	}
	
	private boolean pullSuperOrders(localDB localdb, remoteDB remotedb) {
		SysLog.AppendLog("Debug", "pullSuperOrder", "Start pulling remote super workorders......");
		List<superorder> mList = remotedb.getSuperOrders(myConfig.getLabor_code());
		SysLog.AppendLog("Debug", "pullSuperOrder", "Start writing local super workorders......");
		for(int i=0;i<mList.size();i++){
			localdb.saveSuperOrder(mList.get(i));
		}
		SysLog.AppendLog("Debug", "pullSuperOrder", "End pull super workorders.");
		return true;
	}
	
	private boolean pullCraftOrders(localDB localdb, remoteDB remotedb) {
		SysLog.AppendLog("Debug", "pullCraftOrder", "Start pulling remote craft workorders......");
		List<craftorder> mList = remotedb.getCraftOrders(myConfig.getCraft_list());
		SysLog.AppendLog("Debug", "pullCraftOrder", "Start writing local craft workorders......");
		for(int i=0;i<mList.size();i++){
			localdb.saveCraftOrder(mList.get(i));
		}
		SysLog.AppendLog("Debug", "pullCraftOrder", "End pull craft workorders.");
		return true;
	}

	private void updateStatus(String _msg) {
		Message msg = mHandler.obtainMessage();
		msg.arg1 = 0;
		msg.obj = _msg;
		mHandler.sendMessage(msg);		
	}

	private boolean checkWifiStatus() {
		if(!mWifi.isWifiEnabled()) {
			mWifi.setWifiEnabled(true);
			updateStatus("WIFI Connection is enabled.");
			return false;
		}

		WifiInfo mWifiInfo = mWifi.getConnectionInfo();
		if((mWifiInfo.getSSID() == null) || !mWifiInfo.getSSID().equals(myConfig.getSsid())) {
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
					updateStatus("Faild to add WIFI Configuration!");
					return false;
				}
			}
			mWifi.enableNetwork(mNetId, true);
			updateStatus("WIFI Configuration is enabled.");
			return false;
		}
		if(mWifiInfo.getIpAddress()==0) {
			updateStatus("Waiting for WIFI connection......");
			return false;
		}
		return true;
	}

}