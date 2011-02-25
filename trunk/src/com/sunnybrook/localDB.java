package com.sunnybrook;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.database.sqlite.*;

public class localDB{

	private SQLiteDatabase db;

	public localDB(Context context){
		OpenHelper openHelper = new OpenHelper(context);
		db = openHelper.getWritableDatabase();
	}
	
	public String getSysConfig(String name){
		String value = null;
		String sql = "select conf_value from sysconfig where conf_name= ? ;";
		try {
			SQLiteStatement mystatement =db.compileStatement(sql);
			mystatement.bindString(1,name);
			value = mystatement.simpleQueryForString();
		}
		catch (SQLiteDoneException ex){
			value = null;
		}
		return value;
	}

	public void saveSysConfig(String name,String value){
		String sql;
		SQLiteStatement mStatement;
		if (getSysConfig(name) == null){
			sql = "Insert Into sysconfig values(?,?);";
			mStatement = db.compileStatement(sql);
			mStatement.bindString(1,name);
			mStatement.bindString(2,value);
		}
		else {
			sql = "update sysconfig set conf_value = ? where conf_name= ? ;";
			mStatement = db.compileStatement(sql);
			mStatement.bindString(1,value);
			mStatement.bindString(2,name);
		}
		mStatement.execute();
	}
	
	public void finalize(){
		if(db.isOpen()) db.close();
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		private static final int DATABASE_VERSION = 3;
		private static final String DB_PATH="maximo.sqlite";
		private static final HashMap<String,String> configMap = new HashMap<String,String>(){
			private static final long serialVersionUID = 1L;
			{
				put("labor_code","");
				put("labor_name","");
				put("is_super","yes");
				put("update_key","yes");
				put("update_int", "60000");
				put("update_int_max","600000");
				put("font_size","9");
				put("desc_font_size","12");
				put("crafts","");
/* My Dev SQL Server
				put("jdbc_url","jdbc:jtds:sqlserver://172.16.145.35:1433/maximo;user=sa;password=maximo;");
*/
				put("jdbc_url","jdbc:jtds:sqlserver://142.76.72.125:1433/maximo;user=sa;password=maximo;");
			}
		};

		public OpenHelper(Context context){
			super(context,DB_PATH, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
			String sql;

/* Create table workorder */
			sql = "CREATE TABLE workorder(wonum varchar(10) NOT NULL PRIMARY KEY,status varchar(8) NOT NULL,"
				+ "statusdate datetime NOT NULL,description varchar(50),location varchar(15),locationdesc varchar(80),"
				+ "changeby varchar(18),changedate datetime,wopriority int,wo2 varchar(20),wo3 varchar(20),comments text,"
				+ "reportedby varchar(18),reportdate datetime,phone varchar(20),actstart datetime,actfinish datetime,"
				+ "empcomments varchar(100),khname varchar(40),ktitle varchar(40),kdept varchar(40),kcamp varchar(40),"
				+ "kcost varchar(40),kcod1 varchar(10),kcodr1 varchar(10),kcod2 varchar(10),kcodr2 varchar(10),"
				+ "kcod3 varchar(10),kcodr3 varchar(10),kcod4 varchar(10),kcodr4 varchar(10),kcor1 varchar(10),"
				+ "kcorr1 varchar(10),kcor2 varchar(10),kcorr2 varchar(10),kcor3 varchar(10),kcorr3 varchar(10),"
				+ "kcor4 varchar(10),kcorr4 varchar(10),kcom text,kq1 varchar(3),kq2 varchar(3),kq3 varchar(3),kq4 varchar(3));";
			db.execSQL(sql);
			
/* Create table wo_labor */
			sql = "CREATE TABLE wo_labor (wonum varchar(10) not null,laborcode varchar(8) not null,labortype varchar(1) not null,"
				+ "laborname varchar(40),empcomments varchar(100),readstatus varchar(2),craft varchar(8),"
				+ "Primary Key (wonum, laborcode,labortype));";
			db.execSQL(sql);
			
/* Create Table labtrans */
			sql = "CREATE TABLE labtrans (transid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,labtransid INTEGER NOT NULL DEFAULT 0,"
				+ "transdate DATETIME NOT NULL,laborcode VARCHAR(8) NOT NULL,refwo VARCHAR(8) NOT NULL,enterby VARCHAR(18),"
				+ "enterdate DATETIME NOT NULL,startdate DATETIME NOT NULL,regularhrs FLOAT NOT NULL DEFAULT 0,location VARCHAR(15));";
			db.execSQL(sql);
			
/* Create Table sysconfig */		
			sql = "CREATE TABLE sysconfig (conf_name VARCHAR(50) NOT NULL PRIMARY KEY,conf_value VARCHAR(100) NOT NULL);";
			db.execSQL(sql);
			Iterator<String> iterator = configMap.keySet().iterator();
			sql = "Insert Into sysconfig values(?,?);";
			SQLiteStatement mystatement =db.compileStatement(sql);
			while(iterator.hasNext()){
				String conf_name = iterator.next();
				String conf_value = configMap.get(conf_name);
				mystatement.bindString(1,conf_name);
				mystatement.bindString(2,conf_value);
				mystatement.execute();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		}
	}
}
