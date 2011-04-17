package com.sunnybrook;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TabHost;
import android.widget.TextView;

public class WIFIApp extends TabActivity{
	public static sysconfig myConfig;
	public static localDB localdb;
    private static Timer myTimer = new Timer();
	private TimerTask mySyncDataTask;
	private TextView mStatusBar;
	public static MyDateFormat myDateFormat = new MyDateFormat();
	public static WifiManager mWifi;
	public static boolean mRefreshOwnOrder = false;
	private static NotificationManager mNotificationManager;
	private Notification mNotification;
	private String mAppVer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification();
        setContentView(R.layout.main);
        if (localdb==null) localdb = new localDB(this);
        
        if (myConfig==null) myConfig = new sysconfig(localdb);
        SysLog.localdb = localdb;
        SysLog.debug_mode = myConfig.isDebug_mode();
        SysLog.AppendLog("Info", "WIFIApp", "Application Launched.");
        
        Resources res = getResources();	// Resource object to get Drawables
        
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab

        Intent intent;  // Reusable Intent for each tab
        
        
        intent = new Intent().setClass(this, OwnordersActivity.class);
        spec = tabHost.newTabSpec("ownorders").setIndicator("Ownorders",
        				res.getDrawable(R.drawable.ic_tab_ownorders))
        			.setContent(intent);
        tabHost.addTab(spec);
        if(myConfig.is_super()) {
            intent = new Intent().setClass(this, SuperordersActivity.class);
        	spec = tabHost.newTabSpec("superorder").setIndicator("Superorders",
        			  		res.getDrawable(R.drawable.ic_tab_superorders))
        			  	.setContent(intent);
        	tabHost.addTab(spec);
        }
        if(myConfig.getCraft_list().size() > 0) {
            intent = new Intent().setClass(this, CraftordersActivity.class);
        	spec = tabHost.newTabSpec("craftorder").setIndicator("Craftorders",
        			  		res.getDrawable(R.drawable.ic_tab_craftorders))
        			  	.setContent(intent);
        	tabHost.addTab(spec);
        }

        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = tabHost.newTabSpec("settings").setIndicator("Settings",
        				res.getDrawable(R.drawable.ic_tab_settings))
        			.setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SyslogActivity.class);
        spec = tabHost.newTabSpec("syslog").setIndicator("Syslog",
				res.getDrawable(R.drawable.ic_tab_syslog))
			.setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);        
        mStatusBar = (TextView) findViewById(R.id.txtStatus);
        updateStatus("Ready");
        if(mySyncDataTask == null) {
        	mySyncDataTask = new SyncDataTask(mHandler);
        	myTimer.schedule(mySyncDataTask, 1000, myConfig.getUpdate_int());
        }
        try {
			mAppVer = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
		} catch (NameNotFoundException e) {
			mAppVer = "0";
		}
        setTitle("WIFIApp V" + mAppVer + " (" + myConfig.getLabor_code() + " - " + myConfig.getLabor_name() + ")");
    }

    Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		switch(msg.arg1)
    		{
    			case 0:
    				updateStatus((String) msg.obj);
    				break;
    			case 1:
    				notifyNewOrders((String) msg.obj);
    				mRefreshOwnOrder=true;
    				break;
    			default:
    				break;
    		}
    	}
    };
    
    private void updateStatus(String _msg) {
    	String mStatusMsg;
		WifiInfo mWifiInfo = mWifi.getConnectionInfo();
		if(mWifiInfo.getIpAddress()==0) {
			mStatusMsg = "Wifi:Disconnected.";
		}
		else {
			mStatusMsg = "Wifi:" + getStringIp(mWifiInfo.getIpAddress());
		}
		mStatusMsg = mStatusMsg + " Sync:" + _msg;
		mStatusBar.setText(mStatusMsg);
    }
    
    private void notifyNewOrders(String _msg) {
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "My notification";
    	CharSequence contentText = _msg;
    	Intent notificationIntent = new Intent(this, WIFIApp.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    	mNotification.defaults = Notification.DEFAULT_ALL;
    	mNotification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	mNotificationManager.notify(1,mNotification);
    }
    
    private String getStringIp(int _ip) {
    	String mReturn = "";
    	int mTemp = _ip;
    	while (mTemp >0) {
    		mReturn = mReturn + Integer.toString(mTemp % 256) + ".";
    		mTemp = mTemp / 256;
    	}
    	return mReturn;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	SysLog.AppendLog("Info", "WIFIApp", "Application Closed.");
    }
    
}
