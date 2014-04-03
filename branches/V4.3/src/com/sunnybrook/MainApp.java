package com.sunnybrook;

import android.app.Application;

public class MainApp extends Application{
	private sysconfig mySysConfig;
	private localDB myLocalDB;

	public void setMySysConfig(sysconfig _SysConfig) {
		this.mySysConfig = _SysConfig;
	}
	public sysconfig getMySysConfig() {
		return mySysConfig;
	}
	public void setMyLocalDB(localDB _LocalDB) {
		this.myLocalDB = _LocalDB;
	}
	public localDB getMyLocalDB() {
		return myLocalDB;
	}
	
}
