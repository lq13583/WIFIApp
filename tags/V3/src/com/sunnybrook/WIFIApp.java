package com.sunnybrook;

import java.util.Timer;

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
	public sysconfig myConfig;
	public localDB localdb;
    private Timer myTimer = new Timer();
	private SyncDataTask mySyncDataTask;
	private TextView mStatusBar;
	public  MyDateFormat myDateFormat = new MyDateFormat();
	public  WifiManager mWifi;
	public  boolean mRefreshOwnOrder = false;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private String mAppVer;
	private MainApp mApp;
	private TabHost mTabHost;
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
//    				refreshOwnOrder();
    				break;
    			case 2:
    				mySyncDataTask.run();
    				break;
    			default:
    				break;
    		}
    	}
    };

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification();
        setContentView(R.layout.main);
        if (localdb==null) localdb = new localDB(this);
        mApp = (MainApp)getApplication();
        mApp.setMyLocalDB(localdb);
        if (myConfig==null) myConfig = new sysconfig(localdb);
        mApp.setMySysConfig(myConfig);
        SysLog.localdb = localdb;
        SysLog.debug_mode = myConfig.isDebug_mode();
        SysLog.AppendLog("Info", "WIFIApp", "Application Launched.");
        
        Resources res = getResources();	// Resource object to get Drawables
        
        TabHost mTabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab

        Intent intent;  // Reusable Intent for each tab
        
        
        intent = new Intent().setClass(this, OwnordersActivity.class);
        spec = mTabHost.newTabSpec("ownorders").setIndicator("Ownorders",
        				res.getDrawable(R.drawable.ic_tab_ownorders))
        			.setContent(intent);
        mTabHost.addTab(spec);
        if(myConfig.is_super()) {
            intent = new Intent().setClass(this, SuperordersActivity.class);
        	spec = mTabHost.newTabSpec("superorder").setIndicator("Superorders",
        			  		res.getDrawable(R.drawable.ic_tab_superorders))
        			  	.setContent(intent);
        	mTabHost.addTab(spec);
        }
        if(myConfig.getCraft_list().size() > 0) {
            intent = new Intent().setClass(this, CraftordersActivity.class);
        	spec = mTabHost.newTabSpec("craftorder").setIndicator("Craftorders",
        			  		res.getDrawable(R.drawable.ic_tab_craftorders))
        			  	.setContent(intent);
        	mTabHost.addTab(spec);
        }

        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = mTabHost.newTabSpec("settings").setIndicator("Settings",
        				res.getDrawable(R.drawable.ic_tab_settings))
        			.setContent(intent);
        mTabHost.addTab(spec);
        
        intent = new Intent().setClass(this, SyslogActivity.class);
        spec = mTabHost.newTabSpec("syslog").setIndicator("Syslog",
				res.getDrawable(R.drawable.ic_tab_syslog))
			.setContent(intent);
        mTabHost.addTab(spec);
        
        mTabHost.setCurrentTab(0);        
        mStatusBar = (TextView) findViewById(R.id.txtStatus);
        updateStatus("Ready");
        if(mySyncDataTask == null) {
        	mySyncDataTask = new SyncDataTask(mHandler, myConfig, localdb, mWifi);
        	myTimer.schedule(mySyncDataTask, 1000, myConfig.getUpdate_int());
        }
        try {
			mAppVer = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
		} catch (NameNotFoundException e) {
			mAppVer = "0";
		}
        setTitle("WIFIApp V" + mAppVer + " (" + myConfig.getLabor_code() + " - " + myConfig.getLabor_name() + ")");

	}

    private void updateStatus(String _msg) {
    	String mStatusMsg;
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
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
    	while (mTemp != 0) {
    		mReturn = mReturn + Integer.toString(mTemp & 0xff) + ".";
    		mTemp >>>= 8;
    	}
    	return mReturn;
    }
    
    public void startSyncData() {
    	if(mySyncDataTask != null){
    		if(mySyncDataTask.isRunning()) return;
    		mySyncDataTask.cancel();
    		myTimer.cancel();
    		myTimer = new Timer();
        	mySyncDataTask = new SyncDataTask(mHandler, myConfig, localdb, mWifi);
        	myTimer.schedule(mySyncDataTask, 0, myConfig.getUpdate_int());
    	}
    }
    
    private void refreshOwnOrder() {
    	if(mTabHost.getCurrentTabTag().equals("ownorders")) {
    		OwnordersActivity mActivity = (OwnordersActivity) getLocalActivityManager().getActivity("ownorders"); 
    		mActivity.refreshOrderList();
    	}
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	SysLog.AppendLog("Info", "WIFIApp", "Application Closed.");
    }
    
}
