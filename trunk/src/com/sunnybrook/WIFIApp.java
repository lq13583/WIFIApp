package com.sunnybrook;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class WIFIApp extends TabActivity implements OnTabChangeListener{
	public sysconfig myConfig;
	public localDB localdb;

	private TextView mStatusBar,mCountsUpdates,mCountsOutstanding;
	public  MyDateFormat myDateFormat = new MyDateFormat();
	public  WifiManager mWifi;
	public  boolean mRefreshOwnOrder = false;
	private String mAppVer;
	private MainApp mApp;
	private boolean mIsBound=false;
	public Messenger mSyncService = null;
	private String TAG = WIFIApp.class.getSimpleName();
	
	class IncomingHandler extends Handler{
		Bundle data;
		String dataString;
    	public void handleMessage(Message msg) {
    		switch(msg.what)
    		{
    		case Consts.MSG_DATASYNC_STATUS:
    			switch(msg.arg1)
    			{
    			case Consts.DATASYNC_RUNNING:
    				data = msg.getData();
    				dataString = data.getString(Consts.MSG_STRING);
    				updateStatus(dataString);
    				break;
    			case Consts.DATASYNC_FINISHED:
    				data = msg.getData();
    				dataString = data.getString(Consts.MSG_STRING);
    				updateStatus(String.format("%s %d new orders.", dataString, msg.arg2));
    				break;
    			default: break;
    			}
    			break;
   			default:
    			break;
    		}
    	}
    };

    final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName clasName, IBinder service) {
			// TODO Auto-generated method stub
		     mSyncService = new Messenger(service);
	         try {
	             Message msg = Message.obtain(null, Consts.MSG_REGISTER_CLIENT);
	             msg.replyTo = mMessenger;
	             mSyncService.send(msg);
	         } catch (RemoteException e) {
	                // In this case the service has crashed before we could even do anything with it
	         }
	         mIsBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mSyncService = null;
			mIsBound = false;
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        setContentView(R.layout.main);
        if (localdb==null) localdb = new localDB(this);
        mApp = (MainApp)getApplication();
        mApp.setMyLocalDB(localdb);
        if (myConfig==null) myConfig = new sysconfig(localdb);
        mApp.setMySysConfig(myConfig);

        SysLog.setDebugMode(myConfig.isDebug_mode());
        SysLog.appendLog("INFO", TAG, "Application Launched.");
        
        Resources res = getResources();	// Resource object to get Drawables
        
        TabHost mTabHost = getTabHost();  // The activity TabHost
        mTabHost.setOnTabChangedListener(this);
        
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab

        Intent intent;  // Reusable Intent for each tab
        
        
        intent = new Intent().setClass(this, OwnordersActivity.class);
        spec = mTabHost.newTabSpec("ownorders").setIndicator("WO's",
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
        intent = new Intent().setClass(this, UpdateordersActivity.class);
        spec = mTabHost.newTabSpec("updateorders").setIndicator("Updateorders",
        				res.getDrawable(R.drawable.ic_tab_ownorders))
        			.setContent(intent);
        mTabHost.addTab(spec);

        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = mTabHost.newTabSpec("settings").setIndicator("Settings",
        				res.getDrawable(R.drawable.ic_tab_settings))
        			.setContent(intent);
        mTabHost.addTab(spec);

        mTabHost.setCurrentTab(0);        
        mStatusBar = (TextView) findViewById(R.id.txtStatus);
        updateStatus("Ready");
        mCountsUpdates = (TextView) findViewById(R.id.txtUpdateCnt);
        mCountsOutstanding = (TextView) findViewById(R.id.txtOutstandingCnt);

        try {
			mAppVer = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
		} catch (NameNotFoundException e) {
			mAppVer = "0";
		}
        setTitle("WIFIApp V" + mAppVer + " (" + myConfig.getLabor_code() + " - " + myConfig.getLabor_name() + ")");

        updateCountsUpdates();
        updateCountsOutstanding();
        
        if(localdb.isDailyUpdateRequired()) {
        	mTabHost.setCurrentTabByTag("updateorders");
        	int curTab = mTabHost.getCurrentTab();
        	for(int i=0; i<mTabHost.getTabWidget().getChildCount() - 1; i++)
        		if(i!=curTab) mTabHost.getTabWidget().getChildAt(i).setEnabled(false);
        }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mIsBound = bindService(new Intent(this, WIFISyncService.class),mConnection,Context.BIND_AUTO_CREATE);
	}

	public void updateCountsUpdates() {
		Long lCounts = localdb.getUpdatesCnt(myConfig.getLabor_code());
		String strText = Long.toString(lCounts) + " " + getString(R.string.cnt_updates);
		mCountsUpdates.setText(strText);
	}
	
	public void updateCountsOutstanding() {
		Long lCounts = localdb.getOwnOrderCnt(myConfig.getLabor_code());
		String strText = Long.toString(lCounts) + " " + getString(R.string.cnt_outstanding);
		mCountsOutstanding.setText(strText);
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

    private String getStringIp(int _ip) {
    	String mReturn = "";
    	int mTemp = _ip;
    	while (mTemp != 0) {
    		mReturn = mReturn + Integer.toString(mTemp & 0xff) + ".";
    		mTemp >>>= 8;
    	}
    	return mReturn;
    }
    
    @Override
    public void onDestroy() {
    	SysLog.appendLog("INFO", TAG, "Application Closed.");
    	super.onDestroy();
    }
    
    @Override
    public void onStop() {
    	SysLog.appendLog("INFO", TAG, "Application Stopped.");
    	super.onStop();
    	if(mIsBound) {
            try {
            	Message msg = Message.obtain(null,Consts.MSG_UNREGISTER_CLIENT);
            	msg.replyTo = mMessenger;
				mSyncService.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    		unbindService(mConnection);
    		mIsBound = false;
    	}
    }

	@Override
	public void onTabChanged(String _TabId) {
		if(_TabId.equals("updateorders")) {
			UpdateordersActivity mActivity=(UpdateordersActivity) getLocalActivityManager().getActivity(_TabId);
			if (mActivity!=null) 
				mActivity.refreshOrderList();
			
		}
		else if(_TabId.equals("ownorders")) {
			OwnordersActivity mActivity=(OwnordersActivity) getLocalActivityManager().getActivity(_TabId);
			if (mActivity!=null) 
				mActivity.refreshOrderList();
			
		}
		else if(_TabId.equals("craftorder")) {
			CraftordersActivity mActivity=(CraftordersActivity) getLocalActivityManager().getActivity(_TabId);
			if (mActivity!=null)
				mActivity.refreshOrderList();
		}
		else if(_TabId.equals("superorder")) {
			SuperordersActivity mActivity=(SuperordersActivity) getLocalActivityManager().getActivity(_TabId);
			if (mActivity!=null)
				mActivity.refreshOrderList();
		}
	}
}
