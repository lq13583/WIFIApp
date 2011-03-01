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

//Task is still running
		if(is_running) return; 

//Task is delayed until mCounts count down to 0		
		if (mCounts>0){
			mCounts--;
			return;
		}
//Set the task running flag
		is_running=true;

		myRemoteDB = new remoteDB(myJDBC_URL);
		
		if (myRemoteDB.isConnected()) {
			mPeriod = 0;
		}
		else {
//Increase the waiting period if no network connection.		
			if (mPeriod < maxPeriod) {
				mPeriod++;
			}		
			mCounts += mPeriod;

//Clear the task running flag
			is_running = false;
		}
	}

}