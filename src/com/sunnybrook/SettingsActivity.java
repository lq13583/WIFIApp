package com.sunnybrook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class SettingsActivity extends Activity{
	private sysconfig mSysconfig;
	private localDB mLocalDB;
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.settingsactivity);
    	mLocalDB = new localDB(this);
    	mSysconfig = new sysconfig(mLocalDB);
    	showSettings(mSysconfig);
    }
    
    private void showSettings(sysconfig mSysconfig) {
    	EditText mEditText= (EditText) findViewById(R.id.jdbc_url);
    	mEditText.setText(mSysconfig.getJdbc_url());
    }
}
