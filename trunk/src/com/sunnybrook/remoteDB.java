package com.sunnybrook;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class remoteDB {
	private String connStr;
	private Connection conn;
	
	public remoteDB(String connectionString ){
		connStr = connectionString;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			DriverManager.setLoginTimeout(60);
			conn = DriverManager.getConnection(connStr);
		} catch (ClassNotFoundException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
	}

	protected void finalize(){
		try {
			if(!conn.isClosed()) conn.close();
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
	}

	public boolean isConnected(){
		if (conn==null) return false;
		try {
			return(!conn.isClosed());
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
			return false;
		}
	}
/*	
	public ResultSet getWorkOrderRS(String wonum) {
		PreparedStatement mPs = null;
		ResultSet mRs = null;
		String sql = "select wo.wonum,wo.status,wo.statusdate,wo.description,wo.location,"
				   + "wo.changeby,wo.changedate,wo.wopriority,wo.wo2,wo.wo3,des.ldtext comments,"
				   + "wo.reportedby,wo.reportdate,wo.phone,loc.description locdesc,wo.empcomments remp," 
				   + "khname,ktitle,kdept,kcamp,kcost,kcod1,kcodr1,kcod2,kcodr2,kcod3,kcodr3,kcod4,kcodr4," 
                   + "kcor1,kcorr1,kcor2,kcorr2,kcor3,kcorr3,kcor4,kcorr4,kcom,kq1,kq2,kq3,kq4" 
				   + " from workorder wo"
				   + " left join longdescription des on des.ldkey = wo.ldkey"
				   + " left join locations loc on loc.location = wo.location and loc.siteid = wo.siteid"
				   + " where wo.wonum = ?";
		try {
			mPs = conn.prepareStatement(sql);
			mPs.setString(1,wonum);
			mRs = mPs.executeQuery();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mRs;
	}
*/
	
	public List<ownorder> getOwnOrders(String labor_code) {
		List<ownorder> orderList = new ArrayList<ownorder>();
		PreparedStatement mPs = null;
		ResultSet mRs = null;
		String sql = "select wo.wonum,wo.status,wo.statusdate,wo.description,wo.location,"
				   + "wo.changeby,wo.changedate,wo.wopriority,wo.wo2,wo.wo3,des.ldtext comments,"
				   + "wo.reportedby,wo.reportdate,wo.phone,loc.description locdesc,wo.empcomments remp," 
				   + "khname,ktitle,kdept,kcamp,kcost,kcod1,kcodr1,kcod2,kcodr2,kcod3,kcodr3,kcod4,kcodr4," 
				   + "kcor1,kcorr1,kcor2,kcorr2,kcor3,kcorr3,kcor4,kcorr4,kcom,kq1,kq2,kq3,kq4" 
				   + " from workorder wo"
				   + " left join longdescription des on des.ldkey = wo.ldkey"
				   + " left join locations loc on loc.location = wo.location and loc.siteid = wo.siteid"
				   + " join assignment ass on ass.wonum = wo.wonum and ass.craft= ? "
				   + " where wo.siteid = 'MAXSITE' and wo.status in ('INPRG','WMATL') and (wo.worktype like 'C-%' or wo.worktype is null)"
				   + " order by wo.wonum";
		try {
			mPs = conn.prepareStatement(sql);
			mPs.setString(1,labor_code);
			mRs = mPs.executeQuery();
			while (mRs.next()) {
				ownorder mOwnorder = new ownorder(mRs);
				mOwnorder.setReadStatus("UR");
				orderList.add(mOwnorder);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
		return orderList;
	}
	
	public List<superorder> getSuperOrders(String _laborcode) {
		List<superorder> orderList = new ArrayList<superorder>();
		PreparedStatement mPs = null;
		ResultSet mRs = null;
		String sql = "select wo.wonum,wo.status,wo.statusdate,wo.description,wo.location,"
				   + "wo.changeby,wo.changedate,wo.wopriority,wo.wo2,wo.wo3,des.ldtext comments,"
				   + "wo.reportedby,wo.reportdate,wo.phone,loc.description locdesc,wo.empcomments remp," 
				   + "khname,ktitle,kdept,kcamp,kcost,kcod1,kcodr1,kcod2,kcodr2,kcod3,kcodr3,kcod4,kcodr4," 
				   + "kcor1,kcorr1,kcor2,kcorr2,kcor3,kcorr3,kcor4,kcorr4,kcom,kq1,kq2,kq3,kq4,"
				   + "labor.laborcode "
				   + " from workorder wo"
				   + " left join longdescription des on des.ldkey = wo.ldkey"
				   + " left join locations loc on loc.location = wo.location and loc.siteid = wo.siteid"
				   + " join assignment ass on ass.wonum = wo.wonum "
				   + " join labor on labor.laborcode = ass.craft and labor.supervisor = ? "
				   + " where wo.siteid = 'MAXSITE' and wo.status in ('INPRG','WMATL') and (wo.worktype like 'C-%' or wo.worktype is null)"
				   + " order by wo.wonum";
		try {
			mPs = conn.prepareStatement(sql);
			mPs.setString(1,_laborcode);
			mRs = mPs.executeQuery();
			while (mRs.next()) {
				superorder mOrder = new superorder(mRs);
				orderList.add(mOrder);
			}
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
		
		
		return orderList;
	}
	
	public List<craftorder> getCraftOrders(List<String> _craftlist) {
		List<craftorder> orderList = new ArrayList<craftorder>();
		PreparedStatement mPs = null;
		ResultSet mRs = null;

		String mCrafts = "";
		Iterator<String> itr = _craftlist.iterator();
		while (itr.hasNext()) {
			mCrafts += "'" + itr.next() + "',";
		}
		mCrafts = mCrafts.substring(0,mCrafts.length()-1);
		String sql = "select wo.wonum,wo.status,wo.statusdate,wo.description,wo.location,"
				   + "wo.changeby,wo.changedate,wo.wopriority,wo.wo2,wo.wo3,des.ldtext comments,"
				   + "wo.reportedby,wo.reportdate,wo.phone,loc.description locdesc,wo.empcomments remp," 
				   + "khname,ktitle,kdept,kcamp,kcost,kcod1,kcodr1,kcod2,kcodr2,kcod3,kcodr3,kcod4,kcodr4," 
				   + "kcor1,kcorr1,kcor2,kcorr2,kcor3,kcorr3,kcor4,kcorr4,kcom,kq1,kq2,kq3,kq4," 
				   + "labor.laborcode,labor.craft"
				   + " from workorder wo"
				   + " left join longdescription des on des.ldkey = wo.ldkey"
				   + " left join locations loc on loc.location = wo.location and loc.siteid = wo.siteid"
				   + " join assignment ass on ass.wonum = wo.wonum "
				   + " join labor on labor.laborcode = ass.craft and labor.craft in ( " + mCrafts + ") "
				   + " where wo.siteid = 'MAXSITE' and wo.status in ('INPRG','WMATL') and (wo.worktype like 'C-%' or wo.worktype is null)"
				   + " order by wo.wonum";
 
 		try {
			mPs = conn.prepareStatement(sql);
			mRs = mPs.executeQuery();
			while (mRs.next()) {
				craftorder mOrder = new craftorder(mRs);
				orderList.add(mOrder);
			}
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
		
		return orderList;
	}

}
