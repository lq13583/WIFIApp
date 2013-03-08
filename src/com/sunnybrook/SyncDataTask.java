package com.sunnybrook;

import java.util.TimerTask;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class SyncDataTask extends TimerTask {
	private static final String TAG = SyncDataTask.class.getSimpleName();
	public static boolean mEnabled = true; 
	private sysconfig myConfig;
	private Handler mHandler;
	private localDB myLocalDB;
	private datasync mDataSync;
	private Context mContext;
	
	public SyncDataTask(Handler _handler, Context _context) {
		super();
		mHandler = _handler;
		mContext = _context;
		myLocalDB = new localDB(mContext);
		myConfig = new sysconfig(myLocalDB);
	}
	
	public String getErrMsg() {
		return mDataSync.getErrMsg();
	}
	
	public boolean isRunning() {
		return ((mDataSync != null) && mDataSync.is_running());
	}

	@Override
	public void run() {
		myConfig.refresh(myLocalDB);
		if(!mEnabled) {
			SysLog.appendLog("INFO",TAG,"SyncData task is disabled.");
			return;	//SyncData task is disabled.
		}
		if(myConfig.getLabor_code().equals("")) {
			SysLog.appendLog("INFO", TAG, "Labor code is not set.");
			return;	//Initial setup is not done.
		}
		if(mDataSync==null) mDataSync = new datasync(mHandler,mContext);
		if(!mDataSync.is_running()) mDataSync.run();
	}
	
	@Override
	public boolean cancel() {
		mDataSync.interrupt();
		return super.cancel();
	}
}