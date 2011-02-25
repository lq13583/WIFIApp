package com.sunnybrook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
    	setButtonEvents();
    }
    
    private void setButtonEvents() {
    	Button btn = (Button) findViewById(R.id.btnSave);
    	btn.setOnClickListener(btn_save_onclick_listener);
    }
    
    private OnClickListener btn_save_onclick_listener = new OnClickListener() {
    	public void onClick(View v) {
    		saveSettings();
    		showMessage("OK");
       	}
    };
    
    private void saveSettings() {
    	EditText mEditText = (EditText) findViewById(R.id.labor_code);
    	mSysconfig.setLabor_code(mEditText.getText().toString());
    	showMessage(mSysconfig.getLabor_code());

//		mSysconfig.saveAllToDB(mLocalDB);
    }
    private void showMessage(String txtMsg) {
   		new AlertDialog.Builder(this) 
		.setTitle(R.string.app_name)
		.setMessage(txtMsg)
		.setNeutralButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				// do nothing – it will close on its own
			}
		})
		.show();
    }
    
    private void showSettings(sysconfig mSysconfig) {
    	
    	EditText mEditText = (EditText) findViewById(R.id.labor_code);
    	mEditText.setText(mSysconfig.getLabor_code());
    	mEditText = (EditText) findViewById(R.id.labor_name);
    	mEditText.setText(mSysconfig.getLabor_name());
    	mEditText = (EditText) findViewById(R.id.jdbc_url);
    	mEditText.setText(mSysconfig.getJdbc_url());

    	mEditText = (EditText) findViewById(R.id.update_int);
    	mEditText.setText(Integer.toString(mSysconfig.getUpdate_int()));
    	mEditText = (EditText) findViewById(R.id.update_int_max);
    	mEditText.setText(Integer.toString(mSysconfig.getUpdate_int_max()));
    	mEditText = (EditText) findViewById(R.id.font_size);
    	mEditText.setText(Integer.toString(mSysconfig.getFont_size()));
    	mEditText = (EditText) findViewById(R.id.desc_font_size);
    	mEditText.setText(Integer.toString(mSysconfig.getDesc_font_size()));

    	CheckBox mCheckBox = (CheckBox) findViewById(R.id.is_super);
    	mCheckBox.setChecked(mSysconfig.is_super());
    	mCheckBox = (CheckBox) findViewById(R.id.update_key);
    	mCheckBox.setChecked(mSysconfig.isUpdate_key());
    	
    }
}
