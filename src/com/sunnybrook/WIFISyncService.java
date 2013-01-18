package com.sunnybrook;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class WIFISyncService extends Service {
    
    private static final String TAG = WIFISyncService.class.getSimpleName();
	
	private Timer timer;
	
	private localDB localdb;
	
	private SyncDataTask syncDataTask;
	private static ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
	
	class IncomingHandler extends Handler {
		@Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Consts.MSG_REGISTER_CLIENT:
                mClients.add(msg.replyTo);
                break;
            case Consts.MSG_UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;            
            case Consts.MSG_DATASYNC_STATUS:
            	sendMessageToUI(msg);
            	break;
            case Consts.MSG_DATASYNC_ACT:
            	if(!syncDataTask.isRunning()) syncDataTask.run();
           	default:
                super.handleMessage(msg);
            }
        }		
	}
	
	private Handler mHandler = new IncomingHandler();
	final Messenger mMessenger =  new Messenger(mHandler);
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mMessenger.getBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		android.os.Debug.waitForDebugger();
		localdb = new localDB(this);
		sysconfig myConfig = new sysconfig(localdb);
		SysLog.setDebugMode(myConfig.isDebug_mode());
		SysLog.appendLog("INFO", TAG, "Service creating");
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

	private static void sendMessageToUI(Message _msg) {
        for (int i=mClients.size()-1; i>=0; i--) {
            try {
            	Message msg = Message.obtain(null,_msg.what,_msg.arg1,_msg.arg2);
            	Bundle bundle = new Bundle();
            	bundle.putString(Consts.MSG_STRING, (String) _msg.obj);
            	msg.setData(bundle);
                mClients.get(i).send(msg);
            } catch (RemoteException e) {
                // The client is dead. Remove it from the list; we are going through the list from back to front so this is safe to do inside the loop.
                mClients.remove(i);
            }
        }
    }
	
}
