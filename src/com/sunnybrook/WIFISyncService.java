package com.sunnybrook;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class WIFISyncService extends Service {

	private static final String TAG = WIFISyncService.class.getSimpleName();
	
	private Timer timer;
	
	private localDB localdb;
	
	private SyncDataTask syncDataTask;

/*
	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			SysLog.appendLog("INFO",TAG, "Timer task doing work");
			sysconfig myConfig = new sysconfig(localdb);
			WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			new datasync(mHandler,myConfig,localdb,mWifi).start();
			
		}
	};
*/
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SysLog.appendLog("INFO", TAG, "Service creating");
		localdb = new localDB(this);
		sysconfig myConfig = new sysconfig(localdb);
		WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		syncDataTask = new SyncDataTask(mHandler,localdb,mWifi);
		
		timer = new Timer("DataSyncTimer");
		timer.schedule(syncDataTask, 1000L, myConfig.getUpdate_int());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SysLog.appendLog("INFO", TAG, "Service destroying");

//		localdb.finalize();
		localdb = null;
		
		timer.cancel();
		timer = null;
	}
	
	final Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		//ToDo: adding codes to handle the message.
    	}
    };
	
}
