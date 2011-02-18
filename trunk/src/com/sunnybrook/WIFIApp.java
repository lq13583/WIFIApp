package com.sunnybrook;

//import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TabHost;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;

public class WIFIApp extends TabActivity{
//	private sysconfig myconfig;

//	private localDB localdb;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        localdb = new localDB(this);
//        myconfig = new sysconfig(localdb);
  
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
/*    
    private void SyncData(){
    	remoteDB myDB = new remoteDB(myconfig.getJdbc_url());
    	String conn_status = "Not Connected!";
    	if(myDB.isConnected())
    		conn_status = "Connected";
        else
        	conn_status = "Not Connected!!!";
		new AlertDialog.Builder(this)
			.setTitle("MessageDemo")
			.setMessage(conn_status)
			.setNeutralButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				// do nothing – it will close on its own
				}
			})
			.show();
 
    }
*/
    
}
