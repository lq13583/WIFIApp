package com.sunnybrook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
			mystatement.close();
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
		mStatement.close();
	}
	
	public workorder getWorkOrder(String wonum) {
		workorder mWorkOrder = null;
		String mReturn;
		String sql="select * from workorder where wonum=?";
		SQLiteStatement mStatement = db.compileStatement(sql);
		mStatement.bindString(1,wonum);
		mStatement.execute();
		
		return mWorkOrder;
	}
	
	public void saveWorkOrder(workorder mWorkOrder){
		String sql;
		SQLiteStatement mStatement;
		long mReturn = 0;
		try {
			sql  = "select count(*) from workorder where wonum = ?"; 
			mStatement = db.compileStatement(sql);
			mStatement.bindString(1,mWorkOrder.getOrderId());
			mReturn = mStatement.simpleQueryForLong();
			mStatement.close();
		}
		catch (SQLiteDoneException ex){
			mReturn = 0;
		}
		if (mReturn == 0) {
			sql = "insert into workorder(status,statusdate,description,location,locationdesc,changedby,changedate,wopriority,"
				+ "wo2,wo3,comments,reportedby,reportdate,phone,actstart,actfinish,empcomments,khname,ktitle,kdept,kcamp,"
				+ "kcost,kcod1,kcodr1,kcod2,kcodr2,kcod3,kcodr3,kcod4,kcodr4,kcor1,kcorr1,kcor2,kcorr2,kcor3,kcorr3,kcor4,"
				+ "kcorr4,kcom,kq1,kq2,kq3,kq4,wonum) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		else {
			sql = "update workorder set status = ?, statusdate = ?, description = ?, location = ?, "
				+ "locationdesc = ?, changedby = ?, changedate = ?, wopriority = ?,  wo2 = ?, wo3 = ?, "
				+ "comments = ? , reportedby = ?, reportdate = ?, phone = ?, actstart = ?, actfinish = ?,"
				+ "empcomments = ?, khname = ?, ktitle = ?, kdept = ?, kcamp = ?, kcost = ?, kcod1 = ?,"
				+ "kcodr1 = ?, kcod2 = ?, kcodr2 = ?, kcod3 = ?, kcodr3 = ?, kcod4 = ?, kcodr4 = ?,"
				+ "kcor1 = ?, kcorr1 = ?, kcor2 = ?, kcorr2 = ?, kcor3 = ?, kcorr3 = ?, kcor4 = ?,"
				+ "kcorr4 = ?, kcom = ?, kq1 = ?, kq2 = ?, kq3 = ?, kq4 = ? "
				+ "where wonum = ?";
		}
		try {
			mStatement = db.compileStatement(sql);
			mStatement.bindString(1,mWorkOrder.getStatus());
			mStatement.bindString(2,mWorkOrder.getStatusdate().toLocaleString());
			mStatement.bindString(3,mWorkOrder.getDescription());
			mStatement.bindString(4,mWorkOrder.getLocation());
			mStatement.bindString(5,mWorkOrder.getLocationdesc());
			mStatement.bindString(6,mWorkOrder.getChangedby());
			mStatement.bindString(7,mWorkOrder.getChangedate().toLocaleString());
			mStatement.bindLong(8,mWorkOrder.getWopriority());
			mStatement.bindString(9,mWorkOrder.getWo2());
			mStatement.bindString(10,mWorkOrder.getWo3());
			mStatement.bindString(11,mWorkOrder.getComments());
			mStatement.bindString(12,mWorkOrder.getReportedby());
			mStatement.bindString(13,mWorkOrder.getReportdate().toLocaleString());
			mStatement.bindString(14,mWorkOrder.getPhone());
			mStatement.bindString(15,mWorkOrder.getActstart().toLocaleString());
			mStatement.bindString(16,mWorkOrder.getActfinish().toLocaleString());
			mStatement.bindString(17,mWorkOrder.getEmpcomments());
			mStatement.bindString(18,mWorkOrder.getKhname());
			mStatement.bindString(19,mWorkOrder.getKtitle());
			mStatement.bindString(20,mWorkOrder.getKdept());
			mStatement.bindString(21,mWorkOrder.getKcamp());
			mStatement.bindString(22,mWorkOrder.getKcost());
			mStatement.bindString(23,mWorkOrder.getKcod1());
			mStatement.bindString(24,mWorkOrder.getKcodr1());
			mStatement.bindString(25,mWorkOrder.getKcod2());
			mStatement.bindString(26,mWorkOrder.getKcodr2());
			mStatement.bindString(27,mWorkOrder.getKcod3());
			mStatement.bindString(28,mWorkOrder.getKcodr3());
			mStatement.bindString(29,mWorkOrder.getKcod4());
			mStatement.bindString(30,mWorkOrder.getKcodr4());
			mStatement.bindString(31,mWorkOrder.getKcor1());
			mStatement.bindString(32,mWorkOrder.getKcorr1());
			mStatement.bindString(33,mWorkOrder.getKcor2());
			mStatement.bindString(34,mWorkOrder.getKcorr2());
			mStatement.bindString(35,mWorkOrder.getKcor3());
			mStatement.bindString(36,mWorkOrder.getKcorr3());
			mStatement.bindString(37,mWorkOrder.getKcor4());
			mStatement.bindString(38,mWorkOrder.getKcorr4());
			mStatement.bindString(39,mWorkOrder.getKcom());
			mStatement.bindString(40,mWorkOrder.getKq1());
			mStatement.bindString(41,mWorkOrder.getKq2());
			mStatement.bindString(42,mWorkOrder.getKq3());
			mStatement.bindString(43,mWorkOrder.getKq4());
			mStatement.bindString(44,mWorkOrder.getOrderId());

			mStatement.execute();
			mStatement.close();
		}
		catch (SQLiteDoneException ex){

		}
	}

	public void saveOwnOrder(ownorder mOwnorder){
		String sql;
		long mReturn = 0;
		try {
			sql  = "select count(*) from workorder where wonum = ?"; 
			SQLiteStatement mStatement = db.compileStatement(sql);
			mStatement.bindString(1,mOwnorder.getOrderId());
			mReturn = mStatement.simpleQueryForLong();
		}
		catch (SQLiteDoneException ex){
			mReturn = 0;
		}
		
		if (mReturn == 0) {
			sql = "insert into workorder;";
		}
		else {
			sql = "update workorder ";
		}
			
	}
	
	public void appendSyslog(String logAct,String logMsg ){
		try {
			SQLiteStatement mStatement = db.compileStatement("insert into syslog (logact,logdesc,logtime) values(?,?,?);");
			mStatement.bindString(1,logAct);
			mStatement.bindString(2, logMsg);
			mStatement.bindString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mStatement.execute();
			mStatement.close();
		}
		catch (SQLException ex){
		}
	}
	
	public void clearSyslog(){
		db.execSQL("delete from syslog;");
	}
	
	public List<loginfo> getLogList(){
		List<loginfo> mList = new ArrayList<loginfo>();
		Cursor mCur = db.query("syslog", null, null, null, null, null, "logid desc");
		if(mCur.moveToFirst())
		do {
			mList.add(new loginfo(mCur.getInt(0),mCur.getString(1),mCur.getString(2),mCur.getString(3)));
		} while (mCur.moveToNext());
		mCur.close();
		return mList;
	}
	
	public Cursor getCursor(String mSql,String[] mSelectArgs) {
		Cursor mCur;
		mCur = db.rawQuery(mSql,mSelectArgs);
		return mCur;
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
				put("is_super","no");
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
				put("ssid","SW_Access");
				put("network_key","df4cfcd9a06d46ce3f4d6ccb8e");
				put("debug_mode","no");
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

/* Create Table syslog */
			sql = "CREATE  TABLE syslog (logid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "logtime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,logact VARCHAR(10) NOT NULL,logdesc VARCHAR(200));";
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
			mystatement.close();
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		}
	}
}
