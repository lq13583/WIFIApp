package com.sunnybrook;

import java.util.List;
import java.util.TimerTask;

public class SyncDataTask extends TimerTask {
	public static boolean mEnabled = true; 
	private static int mCounts = 0; 
	private static int mPeriod = 0;
	private static boolean is_running = false;
	private sysconfig myConfig = WIFIApp.myConfig;
	private int maxPeriod = myConfig.getUpdate_int_max()/WIFIApp.myConfig.getUpdate_int();

	@Override
	public void run() {
		if(myConfig.getLabor_code().equals("")) return;
		if(!mEnabled) return;
		localDB myLocalDB = WIFIApp.localdb;
		remoteDB myRemoteDB;

//Task is delayed until mCounts count down to 0		
		if (mCounts>0){
			mCounts--;
			return;
		}

//Task is still running
		if(is_running) return; 

//Set the task running flag
		is_running=true;

//Open remote DB connection
		myRemoteDB = new remoteDB(myConfig.getJdbc_url());
		
		if (!myRemoteDB.isConnected()) {
//Increase the waiting period if no network connection.		
			if (mPeriod < maxPeriod) {
				mPeriod++;
			}		
			mCounts += mPeriod;
		}
		else {
//Reset waiting period to 0;			
			mPeriod = 0;
			if(pushData(myLocalDB,myRemoteDB)){
				if(pullData(myLocalDB,myRemoteDB)) {
					if(cleanData(myLocalDB,myRemoteDB)) {
						
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
		List<ownorder> mOwnOrderList = localdb.getOwnOrderList(myConfig.getLabor_code(), "wonum");
		for(int i=0;i<mOwnOrderList.size();i++ ) {
			if(!remotedb.saveOwnOrder(mOwnOrderList.get(i))) return false;
		}
		SysLog.AppendLog("Debug", "pushData", "Push own orders end.");
		return true;
	}
	
	private boolean pullData(localDB localdb, remoteDB remotedb) {
		SysLog.AppendLog("Debug", "pullData", "Start pulling data......");
		localdb.clearExistedFlag("wo_labor");
		if (!pullOwnOrders(localdb,remotedb)) return false;
		if (myConfig.is_super())
			if(!pullSuperOrders(localdb, remotedb)) return false;
		if (!myConfig.getCraft_list().isEmpty())
			if(!pullCraftOrders(localdb, remotedb)) return false;
/*
		if(myConfig.isUpdate_key())
			if(!pullKeys(localdb,remotedb)) return false;
*/
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
		SysLog.AppendLog("Debug", "pullOwnOrder", "Start pulling remote own workorders......");
		List<ownorder> mList = remotedb.getOwnOrders(myConfig.getLabor_code());
		SysLog.AppendLog("Debug", "pullOwnOrder","Start writing local own workorders......");
		for(int i=0;i<mList.size();i++){
			localdb.saveOwnOrder(mList.get(i),myConfig.getLabor_code());
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

/*	
	private boolean pullKeys(localDB localdb, remoteDB remotedb) {
		return true;
	}
*/	

}