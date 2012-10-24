package com.sunnybrook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity implements OnClickListener{
	private sysconfig mSysconfig;
	private WIFIApp mParent;

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		mParent = (WIFIApp) getParent();
		mSysconfig = mParent.myConfig;
   		setContentView(R.layout.ui_auth);
       	Button btn = (Button) findViewById(R.id.btnAuth);
       	btn.setOnClickListener(this);
    }

    private void initValues() {
    	
    	EditText mEditText = (EditText) findViewById(R.id.labor_code);
    	mEditText.setText(mSysconfig.getLabor_code());
    	mEditText = (EditText) findViewById(R.id.labor_name);
    	mEditText.setText(mSysconfig.getLabor_name());
    	mEditText = (EditText) findViewById(R.id.jdbc_url);
    	mEditText.setText(mSysconfig.getJdbc_url());
    	mEditText = (EditText) findViewById(R.id.ssid);
    	mEditText.setText(mSysconfig.getSsid());
    	mEditText = (EditText) findViewById(R.id.net_key);
    	mEditText.setText(mSysconfig.getNetwork_key());
    	mEditText = (EditText) findViewById(R.id.crafts);
    	mEditText.setText(mSysconfig.getCrafts());
    	mEditText = (EditText) findViewById(R.id.daily_start_time);
    	mEditText.setText(mSysconfig.getDaily_start_time());

    	mEditText = (EditText) findViewById(R.id.update_int);
    	mEditText.setText(Long.toString(mSysconfig.getUpdate_int()));
    	mEditText = (EditText) findViewById(R.id.update_int_max);
    	mEditText.setText(Long.toString(mSysconfig.getUpdate_int_max()));
    	mEditText = (EditText) findViewById(R.id.font_size);
    	mEditText.setText(Integer.toString(mSysconfig.getFont_size()));
    	mEditText = (EditText) findViewById(R.id.desc_font_size);
    	mEditText.setText(Integer.toString(mSysconfig.getDesc_font_size()));
    	mEditText = (EditText) findViewById(R.id.list_font_size);
    	mEditText.setText(Integer.toString(mSysconfig.getList_font_size()));
    	mEditText = (EditText) findViewById(R.id.outstanding_days);
    	mEditText.setText(Long.toString(mSysconfig.getOutstanding_days()));
    	mEditText = (EditText) findViewById(R.id.daily_update_min);
    	mEditText.setText(Integer.toString(mSysconfig.getDaily_update_min()));

    	CheckBox mCheckBox = (CheckBox) findViewById(R.id.is_super);
    	mCheckBox.setChecked(mSysconfig.is_super());
    	mCheckBox = (CheckBox) findViewById(R.id.update_key);
    	mCheckBox.setChecked(mSysconfig.isUpdate_key());
    	mCheckBox = (CheckBox) findViewById(R.id.debug_mode);
    	mCheckBox.setChecked(mSysconfig.isDebug_mode());
    	mCheckBox = (CheckBox) findViewById(R.id.daily_bypass);
    	mCheckBox.setChecked(mSysconfig.isDaily_bypass());
    	
    }    
    
    private void setEvents() {
    	Button btn = (Button) findViewById(R.id.btnSave);
    	btn.setOnClickListener(this);
    	btn = (Button) findViewById(R.id.btnReset);
    	btn.setOnClickListener(this);
    	btn = (Button) findViewById(R.id.btnBackup);
    	btn.setOnClickListener(this);
    }
    
   	public void onClick(View v) {
   		switch (v.getId()) {
   			case R.id.btnSave:
   		   		saveSettings();
   		   		showMessage("Save Ok!");
   		   		break;
   			case R.id.btnReset:
   				initValues();
   				showMessage("Reset Ok!");
   				break;
   			case R.id.btnBackup:
   				backupDB();
   				break;
   			case R.id.btnAuth:
   				EditText mEditText = (EditText) findViewById(R.id.password);
   				if(mEditText.getText().toString().equals("Maximo")){
   					setContentView(R.layout.settingsactivity);
   					initValues();
   					setEvents();
   				}
   				else
   					showMessage("Invalid password!");
   				break;
   		}
    };
    
    private void saveSettings() {
    	EditText mEditText = (EditText) findViewById(R.id.labor_code);
    	mSysconfig.setLabor_code(mEditText.getText().toString());
    	mEditText = (EditText) findViewById(R.id.labor_name);
    	mSysconfig.setLabor_name(mEditText.getText().toString());
    	mEditText = (EditText) findViewById(R.id.jdbc_url);
    	mSysconfig.setJdbc_url(mEditText.getText().toString());
    	mEditText = (EditText) findViewById(R.id.ssid);
    	mSysconfig.setSsid(mEditText.getText().toString());
    	mEditText = (EditText) findViewById(R.id.net_key);
    	mSysconfig.setNetwork_key(mEditText.getText().toString());
    	mEditText = (EditText) findViewById(R.id.crafts);
    	mSysconfig.setCrafts(mEditText.getText().toString());
    	mEditText = (EditText) findViewById(R.id.daily_start_time);
    	mSysconfig.setDaily_start_time(mEditText.getText().toString());
 
    	mEditText = (EditText) findViewById(R.id.update_int);
    	mSysconfig.setUpdate_int(Long.parseLong(mEditText.getText().toString()));
    	mEditText = (EditText) findViewById(R.id.update_int_max);
    	mSysconfig.setUpdate_int_max(Long.parseLong(mEditText.getText().toString()));
    	mEditText = (EditText) findViewById(R.id.font_size);
    	mSysconfig.setFont_size(Integer.parseInt(mEditText.getText().toString()));
    	mEditText = (EditText) findViewById(R.id.desc_font_size);
    	mSysconfig.setDesc_font_size(Integer.parseInt(mEditText.getText().toString()));
    	mEditText = (EditText) findViewById(R.id.list_font_size);
    	mSysconfig.setList_font_size(Integer.parseInt(mEditText.getText().toString()));
    	mEditText = (EditText) findViewById(R.id.outstanding_days);
    	mSysconfig.setOutstanding_days(Integer.parseInt(mEditText.getText().toString()));
    	mEditText = (EditText) findViewById(R.id.daily_update_min);
    	mSysconfig.setDaily_update_min(Integer.parseInt(mEditText.getText().toString()));

    	CheckBox mCheckBox = (CheckBox) findViewById(R.id.is_super);
    	mSysconfig.set_super(mCheckBox.isChecked());
    	mCheckBox = (CheckBox) findViewById(R.id.update_key);
    	mSysconfig.setUpdate_key(mCheckBox.isChecked());
    	mCheckBox = (CheckBox) findViewById(R.id.debug_mode);
    	mSysconfig.setDebug_mode(mCheckBox.isChecked());
    	mCheckBox = (CheckBox) findViewById(R.id.daily_bypass);
    	mSysconfig.setDaily_bypass(mCheckBox.isChecked());

		mSysconfig.saveAllToDB(mParent.localdb);
    }
    
    private void showMessage(String txtMsg) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
   		builder.setTitle(R.string.app_name)
   			   .setCancelable(false)
   			   .setMessage(txtMsg)
   			   .setPositiveButton("Close", new DialogInterface.OnClickListener() {
   				   public void onClick(DialogInterface dlg, int sumthin) {
   					   dlg.cancel();
   				   }
   			   })
			   .show();
    }
    
    private void backupDB() {
    	try {
    		File currentDB = this.getDatabasePath("maximo.sqlite");
    		File extPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    		if(!extPath.exists()) extPath.mkdirs();
    		File backupDB = new File(extPath,"maximo.sqlite");
    		
    		if (currentDB.exists()) {
    			FileInputStream fsSrc = new FileInputStream(currentDB);
    			FileOutputStream fsDst = new FileOutputStream(backupDB);
    			FileChannel src = fsSrc.getChannel();
    			FileChannel dst = fsDst.getChannel();
    			dst.transferFrom(src, 0, src.size());
    			src.close();
    			dst.close();
    			fsSrc.close();
    			fsDst.close();
    			showMessage("Finish Backup to:" + backupDB.getAbsolutePath());
    		}
    	}catch(Exception e) {
    		showMessage(e.toString());
    	}
    }
}
