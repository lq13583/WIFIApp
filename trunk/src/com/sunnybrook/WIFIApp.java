package com.sunnybrook;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class WIFIApp extends TabActivity{
	public static sysconfig myConfig;
	public static localDB localdb;
	public static Timer myTimer;
	public static SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        localdb = new localDB(this);
        myConfig = new sysconfig(localdb);
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

        myTimer = new Timer();
        myTimer.schedule(new SyncDataTask(), 0, myConfig.getUpdate_int());
//        find_and_modify_button();
        
    }
    
    public void finalize() {
    	SysLog.AppendLog("Info", "WIFIApp", "Application Closed.");
    }
    
/*
    private void find_and_modify_button() {
    	Button button = (Button) findViewById(R.id.Button_Test);
    	button.setOnClickListener(button_test_listener); 
    }    

    
    private OnClickListener button_test_listener = new OnClickListener() {
    	public void onClick(View v) {	
    		SyncData();
    	}
    };
*/

}
