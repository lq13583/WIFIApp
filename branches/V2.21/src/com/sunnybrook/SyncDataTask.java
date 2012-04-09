package com.sunnybrook;

import java.util.TimerTask;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class SyncDataTask extends TimerTask {
	public static boolean mEnabled = true; 
	private sysconfig myConfig;
	private Handler mHandler;
	private WifiManager mWifi;
	private localDB myLocalDB;
	private datasync mDataSync;
	
	public SyncDataTask(Handler _handler,sysconfig _config, localDB _localdb, WifiManager _wifi) {
		super();
		mHandler = _handler;
		myConfig = _config;
		mWifi = _wifi;
		myLocalDB = _localdb;
	}
	
	public boolean isRunning() {
		return mDataSync.is_running();
	}

	@Override
	public void run() {
		if(!mEnabled) return;	//SyncData task is disabled.
		if(myConfig.getLabor_code().equals("")) return;	//Initial setup is not done.
		mDataSync = new datasync(mHandler,myConfig,myLocalDB,mWifi);
		if(!mDataSync.is_running()) mDataSync.start();
	}
}