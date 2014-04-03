package com.sunnybrook;

import java.util.TimerTask;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class SyncDataTask extends TimerTask {
	private static final String TAG = SyncDataTask.class.getSimpleName();
	public static boolean mEnabled = true; 
	private sysconfig myConfig;
	private Handler mHandler;
	private WifiManager mWifi;
	private localDB myLocalDB;
	private datasync mDataSync;
	
	public SyncDataTask(Handler _handler, localDB _localdb, WifiManager _wifi) {
		super();
		mHandler = _handler;
		mWifi = _wifi;
		myLocalDB = _localdb;
		myConfig = new sysconfig(myLocalDB);
	}
	
	public boolean isRunning() {
		return mDataSync.is_running();
	}

	@Override
	public void run() {
		if(!mEnabled) {
			SysLog.appendLog("INFO",TAG,"SyncData task is disabled.");
			return;	//SyncData task is disabled.
		}
		if(myConfig.getLabor_code().equals("")) {
			SysLog.appendLog("INFO", TAG, "Labor code is not set.");
			return;	//Initial setup is not done.
		}
		mDataSync = new datasync(mHandler,myLocalDB,mWifi);
		if(!mDataSync.is_running()) mDataSync.start();
	}
}