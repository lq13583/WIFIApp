package com.sunnybrook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.ContentValues;
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

/*	
	public workorder getWorkOrder(String wonum) {
		workorder mWorkOrder = null;
		String sql="select * from workorder where wonum=?";
		SQLiteStatement mStatement = db.compileStatement(sql);
		mStatement.bindString(1,wonum);
		mStatement.execute();
		
		return mWorkOrder;
	}
*/
	public void saveLabTrans(labtrans _labtrans) {
		if(_labtrans.getTransId()==0 && _labtrans.getRegularHrs()==0) return;
		MyDateFormat mDateFormat = WIFIApp.myDateFormat;
		String mTable = "labtrans";
		String mWhereArgs = "transid=?";
		String mWhereVals[] = new String[]{Integer.toString(_labtrans.getTransId())};
		Cursor mCur = db.query(mTable, null,mWhereArgs, mWhereVals, null, null, null);
		if(mCur.getCount() == 0) {
			ContentValues mValues = new ContentValues();
			if(_labtrans.getEnterBy()!= null) mValues.put("enterby", _labtrans.getEnterBy());
			if(_labtrans.getEnterDate()!=null) mValues.put("enterdate", mDateFormat.format(_labtrans.getEnterDate()));
			if(_labtrans.getLaborCode()!=null) mValues.put("laborcode", _labtrans.getLaborCode());
			if(_labtrans.getLocation()!=null) mValues.put("location",_labtrans.getLocation());
			if(_labtrans.getRefWo()!=null) mValues.put("refwo", _labtrans.getRefWo());
			mValues.put("regularhrs", Float.toString(_labtrans.getRegularHrs()));
			if(_labtrans.getStartDate()!=null) mValues.put("startdate", mDateFormat.format(_labtrans.getStartDate()));
			if(_labtrans.getTransDate()!=null) mValues.put("transdate", mDateFormat.format(_labtrans.getTransDate()));
			try {
				db.insertOrThrow(mTable,null,mValues);
			} catch(SQLException ex) {
				SysLog.AppendLog("Info", "localDB", ex.getMessage());
			}
		}
		mCur.close();
		ContentValues mValues = new ContentValues();
		if(_labtrans.getEnterBy()!= null) mValues.put("enterby", _labtrans.getEnterBy());
		if(_labtrans.getEnterDate()!=null) mValues.put("enterdate", mDateFormat.format(_labtrans.getEnterDate()));
		if(_labtrans.getLaborCode()!=null) mValues.put("laborcode", _labtrans.getLaborCode());
		if(_labtrans.getLocation()!=null) mValues.put("location",_labtrans.getLocation());
		if(_labtrans.getRefWo()!=null) mValues.put("refwo", _labtrans.getRefWo());
		mValues.put("regularhrs", Float.toString(_labtrans.getRegularHrs()));
		if(_labtrans.getStartDate()!=null) mValues.put("startdate", mDateFormat.format(_labtrans.getStartDate()));
		if(_labtrans.getTransDate()!=null) mValues.put("transdate", mDateFormat.format(_labtrans.getTransDate()));
		try {
			db.update(mTable, mValues, mWhereArgs, mWhereVals);
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}
	
	public boolean updateLabTransId(labtrans _labtrans) {
		String mTable = "labtrans";
		String mWhereArgs = "transid=?";
		String mWhereVals[] = new String[]{Integer.toString(_labtrans.getTransId())};
		ContentValues mValues = new ContentValues();
		mValues.put("labtransid", Integer.toString(_labtrans.getLabTransId()));
		try {
			db.update(mTable, mValues, mWhereArgs, mWhereVals);
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB-updateLabTransId", ex.getMessage());
			return false;
		}
		return true;
	}
	
	public void saveWorkOrder(workorder mWorkOrder){

		if (mWorkOrder.getOrderId().equals("00000")) return;

		String mTable = "workorder";
		String mWhereArgs = "wonum=?";
		String mWhereVals[] = new String[] {mWorkOrder.getOrderId()};
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Cursor mCur = db.query(mTable,null, mWhereArgs, mWhereVals,null, null, null);
		if(mCur.getCount() == 0) {
			ContentValues mValues = new ContentValues();
			mValues.put("wonum", mWorkOrder.getOrderId());
			mValues.put("status",mWorkOrder.getStatus());
			mValues.put("statusdate",df.format(mWorkOrder.getStatusdate()));
			try {
				db.insertOrThrow(mTable,null,mValues);
			} catch(SQLException ex) {
				SysLog.AppendLog("Info", "localDB", ex.getMessage());
			}
		}
		mCur.close();
		ContentValues mValues = new ContentValues();
		if (mWorkOrder.getStatus()!= null) mValues.put("status", mWorkOrder.getStatus());
		if (mWorkOrder.getStatusdate()!= null) mValues.put("statusdate",df.format(mWorkOrder.getStatusdate()));
		if (mWorkOrder.getDescription()!= null) mValues.put("description", mWorkOrder.getDescription());
		if (mWorkOrder.getLocation()!= null) mValues.put("location", mWorkOrder.getLocation());
		if (mWorkOrder.getLocationdesc()!= null) mValues.put("locationdesc", mWorkOrder.getLocationdesc());
		if (mWorkOrder.getChangeby()!= null) mValues.put("changeby", mWorkOrder.getChangeby());
		if (mWorkOrder.getChangedate()!= null) mValues.put("changedate", df.format(mWorkOrder.getChangedate()));
		mValues.put("wopriority", mWorkOrder.getWopriority());
		if (mWorkOrder.getWo2()!= null) mValues.put("wo2", mWorkOrder.getWo2());
		if (mWorkOrder.getWo3()!= null) mValues.put("wo3", mWorkOrder.getWo3());
		if (mWorkOrder.getComments()!= null) mValues.put("comments", mWorkOrder.getComments());
		if (mWorkOrder.getReportedby()!= null) mValues.put("reportedby", mWorkOrder.getReportedby());
		if (mWorkOrder.getReportdate().toLocaleString()!= null) mValues.put("reportdate", df.format(mWorkOrder.getReportdate()));
		if (mWorkOrder.getPhone()!= null) mValues.put("phone", mWorkOrder.getPhone());
		if (mWorkOrder.getActstart()!= null) mValues.put("actstart", df.format(mWorkOrder.getActstart()));
		if (mWorkOrder.getActfinish()!= null) mValues.put("actfinish", df.format(mWorkOrder.getActfinish()));
		if (mWorkOrder.getEmpcomments()!= null) mValues.put("empcomments", mWorkOrder.getEmpcomments());
		if (mWorkOrder.getKhname()!= null) mValues.put("khname", mWorkOrder.getKhname());
		if (mWorkOrder.getKtitle()!= null) mValues.put("ktitle", mWorkOrder.getKtitle());
		if (mWorkOrder.getKdept()!= null) mValues.put("kdept", mWorkOrder.getKdept());
		if (mWorkOrder.getKcamp()!= null) mValues.put("kcamp", mWorkOrder.getKcamp());
		if (mWorkOrder.getKcost()!= null) mValues.put("kcost", mWorkOrder.getKcost());
		if (mWorkOrder.getKcod1()!= null) mValues.put("kcod1", mWorkOrder.getKcod1());
		if (mWorkOrder.getKcodr1()!= null) mValues.put("kcodr1", mWorkOrder.getKcodr1());
		if (mWorkOrder.getKcod2()!= null) mValues.put("kcod2", mWorkOrder.getKcod2());
		if (mWorkOrder.getKcodr2()!= null) mValues.put("kcodr2", mWorkOrder.getKcodr2());
		if (mWorkOrder.getKcod3()!= null) mValues.put("kcod3", mWorkOrder.getKcod3());
		if (mWorkOrder.getKcodr3()!= null) mValues.put("kcodr3", mWorkOrder.getKcodr3());
		if (mWorkOrder.getKcod4()!= null) mValues.put("kcod4", mWorkOrder.getKcod4());
		if (mWorkOrder.getKcodr4()!= null) mValues.put("kcodr4", mWorkOrder.getKcodr4());
		if (mWorkOrder.getKcor1()!= null) mValues.put("kcor1", mWorkOrder.getKcor1());
		if (mWorkOrder.getKcorr1()!= null) mValues.put("kcorr1", mWorkOrder.getKcorr1());
		if (mWorkOrder.getKcor2()!= null) mValues.put("kcor2", mWorkOrder.getKcor2());
		if (mWorkOrder.getKcorr2()!= null) mValues.put("kcorr2", mWorkOrder.getKcorr2());
		if (mWorkOrder.getKcor3()!= null) mValues.put("kcor3", mWorkOrder.getKcor3());
		if (mWorkOrder.getKcorr3()!= null) mValues.put("kcorr3", mWorkOrder.getKcorr3());
		if (mWorkOrder.getKcor4()!= null) mValues.put("kcor4", mWorkOrder.getKcor4());
		if (mWorkOrder.getKcorr4()!= null) mValues.put("kcorr4", mWorkOrder.getKcorr4());
		if (mWorkOrder.getKcom()!= null) mValues.put("kcom", mWorkOrder.getKcom());
		if (mWorkOrder.getKq1()!= null) mValues.put("kq1", mWorkOrder.getKq1());
		if (mWorkOrder.getKq2()!= null) mValues.put("kq2", mWorkOrder.getKq2());
		if (mWorkOrder.getKq3()!= null) mValues.put("kq3", mWorkOrder.getKq3());
		if (mWorkOrder.getKq4()!= null) mValues.put("kq4", mWorkOrder.getKq4());
		try {
			db.update(mTable, mValues, mWhereArgs, mWhereVals);
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}

	public void saveOwnOrder(ownorder mOwnOrder,String _laborcode){
		if(mOwnOrder.getOrderId().equals("00000")) return;
		
		String mTable = "wo_labor";
		String mWhereArgs = "wonum=? and laborcode=? and labortype=?";
		String mWhereVals[] = new String[] {mOwnOrder.getOrderId(),_laborcode,"O"};
		
		saveWorkOrder(mOwnOrder);

		Cursor mCur = db.query(mTable,null, mWhereArgs, mWhereVals,null, null, null);
		if(mCur.getCount() == 0) {
			ContentValues mValues = new ContentValues();
			mValues.put("wonum", mOwnOrder.getOrderId());
			mValues.put("laborcode",_laborcode);
			mValues.put("labortype", "O");
			mValues.put("readstatus",mOwnOrder.getReadStatus());
			try {
				db.insertOrThrow(mTable,null,mValues);
			} catch(SQLException ex) {
				SysLog.AppendLog("Info", "localDB", ex.getMessage());
			}
		}
		mCur.close();
		ContentValues mValues = new ContentValues();
//		if (mOwnOrder.getReadStatus()!= null) mValues.put("readstatus", mOwnOrder.getReadStatus());
		if (mOwnOrder.getMyComments()!= null) mValues.put("empcomments",mOwnOrder.getMyComments());
		mValues.put("existed", "true");
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
		
		/* To Do -- Save labtrans */
	}
	
	public void updateStatus(workorder _order) {
		String mTable = "workorder";
		String mWhereArgs = "wonum=?";
		String mWhereVals[] = new String[] {_order.getOrderId()};
		ContentValues mValues = new ContentValues();
		if (_order.getStatus()!= null){
			mValues.put("status", _order.getStatus());
			if(_order.getStatus().equals("COMP")) {
				if(_order.getActstart()==null) mValues.put("actstart", WIFIApp.myDateFormat.myFormat(new Date()));
				if(_order.getActfinish()==null) mValues.put("actfinish", WIFIApp.myDateFormat.myFormat(new Date()));
			}
		}
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}
	
	public void updateActstart(workorder _order) {
		String mTable = "workorder";
		String mWhereArgs = "wonum=?";
		String mWhereVals[] = new String[] {_order.getOrderId()};
		ContentValues mValues = new ContentValues();
		if (_order.getActstart()!= null) mValues.put("actstart", WIFIApp.myDateFormat.myFormat(_order.getActstart()));
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}
	
	public void updateActfinish(workorder _order) {
		String mTable = "workorder";
		String mWhereArgs = "wonum=?";
		String mWhereVals[] = new String[] {_order.getOrderId()};
		ContentValues mValues = new ContentValues();
		if (_order.getActfinish()!= null) mValues.put("actfinish", WIFIApp.myDateFormat.myFormat(_order.getActfinish()));
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}
	
	public void updateReadStatus(ownorder _order) {
		String mTable = "wo_labor";
		String mWhereArgs = "wonum=?";
		String mWhereVals[] = new String[] {_order.getOrderId()};
		ContentValues mValues = new ContentValues();
		if (_order.getReadStatus()!= null) mValues.put("readstatus", _order.getReadStatus());
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}

	public void updateLabTransList(ownorder _order) {
		if(_order.getTranslist() == null) return;
		List<labtrans> mItems = _order.getTranslist();
		for(int i=0;i<mItems.size();i++) {
			labtrans mItem = mItems.get(i);
			mItem.setEnterBy(WIFIApp.myConfig.getLabor_code());
			mItem.setEnterDate(new Date());
			mItem.setLocation(_order.getLocation());
			mItem.setRefWo(_order.getOrderId());
			mItem.setTransDate(new Date());
			saveLabTrans(mItem);
		}
	}
	
	public void updateMyComment(ownorder _order) {
		String mTable = "wo_labor";
		String mWhereArgs = "wonum=?";
		String mWhereVals[] = new String[] {_order.getOrderId()};
		ContentValues mValues = new ContentValues();
		if (_order.getMyComments()!= null) mValues.put("empcomments", _order.getMyComments());
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}

	public void saveSuperOrder(superorder _Order){
		if(_Order.getOrderId().equals("00000")) return;
		
		String mTable = "wo_labor";
		String mWhereArgs = "wonum=? and laborcode=? and labortype=?";
		String mWhereVals[] = new String[] {_Order.getOrderId(),_Order.getLaborCode(),"S"};
		
		saveWorkOrder(_Order);

		Cursor mCur = db.query(mTable,null, mWhereArgs, mWhereVals,null, null, null);
		if(mCur.getCount() == 0) {
			ContentValues mValues = new ContentValues();
			mValues.put("wonum", _Order.getOrderId());
			mValues.put("laborcode",_Order.getLaborCode());
			mValues.put("laborname", _Order.getLaborName());
			mValues.put("labortype", "S");
			try {
				db.insertOrThrow(mTable,null,mValues);
			} catch(SQLException ex) {
				SysLog.AppendLog("Info", "localDB", ex.getMessage());
			}
		}
		mCur.close();
		ContentValues mValues = new ContentValues();
		mValues.put("existed", "true");
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}

	public void saveCraftOrder(craftorder _Order){
		if(_Order.getOrderId().equals("00000")) return;
		
		String mTable = "wo_labor";
		String mWhereArgs = "wonum=? and laborcode=? and labortype=?";
		String mWhereVals[] = new String[] {_Order.getOrderId(),_Order.getLaborCode(),"C"};
		
		saveWorkOrder(_Order);

		Cursor mCur = db.query(mTable,null, mWhereArgs, mWhereVals,null, null, null);
		if(mCur.getCount() == 0) {
			ContentValues mValues = new ContentValues();
			mValues.put("wonum", _Order.getOrderId());
			mValues.put("laborcode",_Order.getLaborCode());
			mValues.put("laborname", _Order.getLaborName());
			mValues.put("labortype", "C");
			mValues.put("craft", _Order.getCraft());
			try {
				db.insertOrThrow(mTable,null,mValues);
			} catch(SQLException ex) {
				SysLog.AppendLog("Info", "localDB", ex.getMessage());
			}
		}
		mCur.close();
		ContentValues mValues = new ContentValues();
		mValues.put("existed", "true");
		try {
			db.update(mTable, mValues, mWhereArgs,mWhereVals );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}

	public void clearExistedFlag(String _table) {
		ContentValues mValues = new ContentValues();
		mValues.put("existed", "false");
		try {
			db.update(_table, mValues, null, null );
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
	}
	
	public boolean cleanData() {
		String mTable = "wo_labor";
		String mWhereArgs = "existed = ?";
		String mWhereVals[] = new String[] {"false"};
		try {
			db.delete(mTable,mWhereArgs,mWhereVals);
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
			return false;
		}
		String sql = "delete from workorder where not exists (select * from wo_labor where wo_labor.wonum=workorder.wonum);";
		db.execSQL(sql);
		sql = "delete from labtrans where not exists (select * from workorder where workorder.wonum=labtrans.refwo);";
		db.execSQL(sql);
		return true;
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

	public List<labor> getLaborList() {
		List<labor> mList = new ArrayList<labor>();
		String sql = "SELECT distinct laborcode, laborname FROM wo_labor WHERE labortype = 'S' order by laborcode;";
		Cursor mCur = db.rawQuery(sql, null);
		if (mCur.moveToFirst())
			do {
				mList.add(new labor(mCur.getString(mCur.getColumnIndex("laborcode")),mCur.getString(mCur.getColumnIndex("laborname"))));
			} while (mCur.moveToNext());
		mCur.close();
		return mList;
	}
	
	public List<superorder> getSuperOrderList(String _laborcode, String _orderby) {
		List<superorder> mList = new ArrayList<superorder>();
		String mWhereVals[] = new String[] {"S",_laborcode};
		String mOrderby = " order by wo." + _orderby;
		String sql = "SELECT wo.*,wl.laborcode,wl.laborname FROM workorder wo " 
				   + " join wo_labor wl on wo.wonum=wl.wonum and wl.labortype=? and wl.laborcode=?"
				   + mOrderby + ";";
		Cursor mCur = db.rawQuery(sql, mWhereVals);
		if (mCur.moveToFirst())
			do {
				mList.add(new superorder(Cursor2HashMap(mCur)));
			} while (mCur.moveToNext());
		mCur.close();
		return mList;
	}
	
	public List<ownorder> getOwnOrderList(String _laborcode, String _orderby) {
		List<ownorder> mList = new ArrayList<ownorder>();
		String mWhereVals[] = new String[] {"O",_laborcode};
		String mOrderby = " order by " + _orderby;
		String sql = "SELECT wo.*,wl.readstatus, wl.empcomments mycomments FROM workorder wo " 
				   + " JOIN wo_labor wl on wo.wonum=wl.wonum and wl.labortype=? and wl.laborcode=?"
				   + mOrderby + ";";
		Cursor mCur = db.rawQuery(sql, mWhereVals);
		if (mCur.moveToFirst())
			do {
				ownorder mOwnorder = new ownorder(Cursor2HashMap(mCur));
				String mTableTrans = "labtrans";
				String mWhereArgs = "refwo=?";
				String mWhereValsTrans[] = new String[] {mOwnorder.getOrderId()};
				
				Cursor mCurTrans = db.query(mTableTrans,null, mWhereArgs, mWhereValsTrans,null, null, "transid");
				if(mCurTrans.moveToFirst())
					do {
						mOwnorder.addLabTrans(new labtrans(Cursor2HashMap(mCurTrans)));
					} while (mCurTrans.moveToNext());
				mCurTrans.close();	
				mList.add(mOwnorder);
			} while (mCur.moveToNext());
		mCur.close();
		return mList;
	}

	public ownorder Craft2Own(craftorder _order, String _laborcode) {
		ownorder mReturn = null;
		String mTable = "wo_labor";
		String mWhereArgs = "wonum=? and laborcode=? and craft=? and labortype=?";
		String mWhereVals[] = new String[] {_order.getOrderId(),_order.getLaborCode(),_order.getCraft(),"C"}; 
		ContentValues mValues = new ContentValues();
		mValues.put("labortype", "O");
		mValues.put("laborcode", _laborcode);
		mValues.put("readstatus", "UR");
		try {
			db.update(mTable, mValues, mWhereArgs, mWhereVals);
		} catch(SQLException ex) {
			SysLog.AppendLog("Info", "localDB", ex.getMessage());
		}
		String mSelectVals[] = new String[] {"O",_laborcode,_order.getOrderId()};
		String sql = "SELECT wo.*,wl.readstatus, wl.empcomments mycomments FROM workorder wo " 
				   + " JOIN wo_labor wl on wo.wonum=wl.wonum and wl.labortype=? and wl.laborcode=? and wo.wonum=?;";
		Cursor mCur = db.rawQuery(sql, mSelectVals);
		if (mCur.moveToFirst()){
				mReturn = new ownorder(Cursor2HashMap(mCur));
		}
		return mReturn;	
	}
	
	public List<craftorder> getCraftOrderList(String _craft, String _orderby) {
		List<craftorder> mList = new ArrayList<craftorder>();
		String mWhereVals[] = new String[] {"C",_craft};
		String mOrderby = " order by " + _orderby;
		String sql = "SELECT wo.*,wl.laborcode,wl.laborname,wl.craft FROM workorder wo " 
				   + " join wo_labor wl on wo.wonum=wl.wonum and wl.labortype=? and wl.craft=?"
				   + mOrderby + ";";
		Cursor mCur = db.rawQuery(sql, mWhereVals);
		if (mCur.moveToFirst())
			do {
				mList.add(new craftorder(Cursor2HashMap(mCur)));
			} while (mCur.moveToNext());
		mCur.close();
		return mList;
	}
	
/*	
	public Cursor getCursor(String mSql,String[] mSelectArgs) {
		Cursor mCur;
		mCur = db.rawQuery(mSql,mSelectArgs);
		return mCur;
	}
*/
	
	public void finalize(){
		if(db.isOpen()) db.close();
	}
	
	private HashMap<String,String> Cursor2HashMap(Cursor _Cur) {
		HashMap<String,String> mHashMap = new HashMap<String, String>() ;
		for(int i = 0; i< _Cur.getColumnCount(); i++) {
			mHashMap.put(_Cur.getColumnName(i), _Cur.getString(i));
		}
		return mHashMap;
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
				+ "kcor4 varchar(10),kcorr4 varchar(10),kcom text,kq1 varchar(3),kq2 varchar(3),kq3 varchar(3),kq4 varchar(3),"
				+ "existed bool default true);";
			db.execSQL(sql);
			
/* Create table wo_labor */
			sql = "CREATE TABLE wo_labor (wonum varchar(10) not null,laborcode varchar(8) not null,labortype varchar(1) not null,"
				+ "laborname varchar(40),empcomments varchar(100),readstatus varchar(2),craft varchar(8),existed bool default true,"
				+ "Primary Key (wonum, laborcode,labortype));";
			db.execSQL(sql);
			
/* Create indexes on table wo_labor */
			sql = "CREATE  INDEX idx_1 ON wo_labor (laborcode ASC, labortype ASC);";
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
