package com.sunnybrook;

import java.util.TimerTask;

public class SyncDataTask extends TimerTask {
	private int mCounts = 0; 
	private int mPeriod = 0;
	private int maxPeriod = WIFIApp.myConfig.getUpdate_int_max()/WIFIApp.myConfig.getUpdate_int();
	private boolean is_running = false;
	private localDB myLocalDB = WIFIApp.localdb;
	private String myJDBC_URL = WIFIApp.myConfig.getJdbc_url();
	private remoteDB myRemoteDB;

	@Override
	public void run() {

//Task is delayed until mCounts count down to 0		
		if (mCounts>0){
			mCounts--;
			return;
		}

//Task is still running
		if(is_running) return; 

//Set the task running flag
		is_running=true;

		myRemoteDB = new remoteDB(myJDBC_URL);
		
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
	
	private boolean pullData(localDB localdb, remoteDB remotedb) {
		return true;
	}
}