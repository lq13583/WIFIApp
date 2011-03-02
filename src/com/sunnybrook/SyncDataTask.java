package com.sunnybrook;

import java.util.List;
import java.util.TimerTask;

public class SyncDataTask extends TimerTask {
	private static int mCounts = 0; 
	private static int mPeriod = 0;
	private static boolean is_running = false;
	private sysconfig myConfig = WIFIApp.myConfig;
	private int maxPeriod = myConfig.getUpdate_int_max()/WIFIApp.myConfig.getUpdate_int();

	@Override
	public void run() {
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
					
				}
			}
		}


//Clear the task running flag
		is_running = false;
		return;
	}

	private boolean pushData(localDB localdb, remoteDB remotedb){
		return true;
	}
	
	private boolean pullOwnOrders(localDB localdb, remoteDB remotedb) {
		List<ownorder> mList = remotedb.getOwnOrders(myConfig.getLabor_code());
		return true;
	}
	
	private boolean pullSuperOrders(localDB localdb, remoteDB remotedb) {
		return true;
	}
	
	private boolean pullCraftOrders(localDB localdb, remoteDB remotedb) {
		return true;
	}
	
	private boolean pullKeys(localDB localdb, remoteDB remotedb) {
		return true;
	}
	
	private boolean pullData(localDB localdb, remoteDB remotedb) {
		if (!pullOwnOrders(localdb,remotedb)) return false;
		if (myConfig.is_super())
			if(!pullSuperOrders(localdb, remotedb)) return false;
		if (!myConfig.getCraft_list().isEmpty())
			if(!pullCraftOrders(localdb, remotedb)) return false;
		if(myConfig.isUpdate_key())
			if(!pullKeys(localdb,remotedb)) return false;
		return true;
	}
}