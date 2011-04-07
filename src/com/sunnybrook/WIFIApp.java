package com.sunnybrook;

import java.util.Timer;
import java.util.TimerTask;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TabHost;
import android.widget.TextView;

public class WIFIApp extends TabActivity{
	public static sysconfig myConfig;
	public static localDB localdb;
//	private TextView mStatusBar;
    private static Timer myTimer = new Timer();

	private TimerTask mySyncDataTask;
	public static MyDateFormat myDateFormat = new MyDateFormat();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        
        TextView mStatusBar = (TextView) findViewById(R.id.txtStatus);
        mStatusBar.setText("Ready");
        if(mySyncDataTask == null) {
        	mySyncDataTask = new SyncDataTask(mHandler);
        	myTimer.schedule(mySyncDataTask, 1000, myConfig.getUpdate_int());
        }
    }

    Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		switch(msg.arg1)
    		{
    			case 0:
    		        TextView mStatusBar = (TextView) findViewById(R.id.txtStatus);
    				mStatusBar.setText((String) msg.obj);
    				break;
    			default:
    				break;
    		}
    	}
    };
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	SysLog.AppendLog("Info", "WIFIApp", "Application Closed.");
    }
    
}
