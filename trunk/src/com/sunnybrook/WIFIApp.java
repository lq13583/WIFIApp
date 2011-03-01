package com.sunnybrook;

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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        localdb = new localDB(this);
        myConfig = new sysconfig(localdb);
  
        Resources res = getResources();	// Resource object to get Drawables
        
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab

        Intent intent;  // Reusable Intent for each tab
        
        intent = new Intent().setClass(this, SettingsActivity.class);
        
        spec = tabHost.newTabSpec("settings").setIndicator("Settings",
        				res.getDrawable(R.drawable.ic_tab_settings))
        			.setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, OwnordersActivity.class);
        spec = tabHost.newTabSpec("ownorders").setIndicator("Ownorders",
        				res.getDrawable(R.drawable.ic_tab_ownorders))
        			.setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);        

        myTimer = new Timer();
        myTimer.schedule(new SyncDataTask(), 0, myConfig.getUpdate_int());
//        find_and_modify_button();
        
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
