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
			DriverManager.setLoginTimeout(120);
			conn = DriverManager.getConnection(connStr);
		} catch (ClassNotFoundException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
	}

	public void close() {
		try {
			if(conn!= null && !conn.isClosed()) conn.close();
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
	}
	
/*
	protected void finalize(){
		try {
			if(conn!= null && !conn.isClosed()) conn.close();
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
	}
*/

	public boolean isConnected(){
		if (conn==null) return false;
		try {
			return(!conn.isClosed());
		} catch (SQLException e) {
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
			return false;
		}
	}
	
	public List<ownorder> getOwnOrders(String labor_code, int _days) {
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
				if(!isPending(mRs.getString("wonum"))) {
					ownorder mOwnorder = new ownorder(mRs);
					mOwnorder.setReadStatus("UR");
					mOwnorder.setNeedsupdate(isUpdating(mRs.getString("wonum"),_days));
					orderList.add(mOwnorder);
				}
			}
			mRs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
		return orderList;
	}

	private boolean isPending(String _wonum) {
		boolean mReturn = false;
		String sql = "Select * from [Facilities Services].dbo.WO_Projects " 
                   + " where WorkOrder_No = ? and status = 'Quote Finalized'";
		PreparedStatement mPs = null;
		ResultSet mRs = null;
		try {
			mPs = conn.prepareStatement(sql);
			mPs.setString(1,_wonum);
			mRs = mPs.executeQuery();
			if (mRs.next())	mReturn = true;
			mRs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
		return mReturn;
	}
	
	private boolean isUpdating(String _wonum, int _days) {
		boolean mReturn = false;
		String sql = "Select * from [WRM_Sunnybrook].dbo.ServiceCall "
				   + " where WorkOrder_No = ? and isnull(ED_Completion,DateAdd(day,?,Ticket_DateTime)) < getdate()";
		PreparedStatement mPs = null;
		ResultSet mRs = null;
		try {
			mPs = conn.prepareStatement(sql);
			mPs.setString(1,_wonum);
			mPs.setInt(2,_days);
			mRs = mPs.executeQuery();
			if (mRs.next()){
				mReturn = true;
			}
			mRs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SysLog.AppendLog("Info", "remoteDB", e.getMessage());
		}
		
		return mReturn;
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
				   + "labor.laborcode,labor.name laborname "
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
			mCrafts += "'" + itr.next().toUpperCase() + "',";
		}
		mCrafts = mCrafts.substring(0,mCrafts.length()-1);
		String sql = "select wo.wonum,wo.status,wo.statusdate,wo.description,wo.location,"
				   + "wo.changeby,wo.changedate,wo.wopriority,wo.wo2,wo.wo3,des.ldtext comments,"
				   + "wo.reportedby,wo.reportdate,wo.phone,loc.description locdesc,wo.empcomments remp," 
				   + "khname,ktitle,kdept,kcamp,kcost,kcod1,kcodr1,kcod2,kcodr2,kcod3,kcodr3,kcod4,kcodr4," 
				   + "kcor1,kcorr1,kcor2,kcorr2,kcor3,kcorr3,kcor4,kcorr4,kcom,kq1,kq2,kq3,kq4," 
				   + "labor.laborcode,labor.craft,labor.name laborname"
				   + " from workorder wo"
				   + " left join longdescription des on des.ldkey = wo.ldkey"
				   + " left join locations loc on loc.location = wo.location and loc.siteid = wo.siteid"
				   + " join assignment ass on ass.wonum = wo.wonum "
				   + " join labor on labor.laborcode = ass.craft and upper(labor.craft) in ( " + mCrafts + ") "
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
	
	public boolean saveOwnOrder(ownorder _order, localDB _localdb){
		List<labtrans> mLabTransList = _order.getTranslist();
		if(mLabTransList!= null) {
			for(int i=0; i<mLabTransList.size();i++) {
					if(!saveLabTrans(mLabTransList.get(i),_localdb)){
						SysLog.AppendLog("Info", "remoteDB-saveOwnOrder", "Save labtrans record error!");
						return false;
					}
			}
		}

/* Upload myComments if it is not null */
		if(_order.getMyComments() != null) {
			PreparedStatement mPs = null;
			String sql = "update workorder set empcomments = SUBSTRING(ISNULL([empcomments],'') + ?,1,100) where wonum = ?;";
			try {
				mPs = conn.prepareStatement(sql);
				mPs.setString(1,_order.getMyComments());
				mPs.setString(2,_order.getOrderId());
				mPs.executeUpdate();
			} catch (SQLException e) {
				SysLog.AppendLog("Info", "remoteDB-saveOwnOrder", e.getMessage());
				return false;
			}
//Clear local comments when it is uploaded.			
			_order.setMyComments("");
			_localdb.updateMyComment(_order);
		}

/* Update Outstanding order status */
		if(_order.isNeedsupdate()) {
			if(_order.getEdcompletion()!=null) {
				try {
					PreparedStatement mPs = null;
					String sql = "update WRM_Sunnybrook.dbo.ServiceCall set Reason_For_Delay = ?, RFD_Comments = ?, ED_Completion = ? where WorkOrder_No = ?;";
					mPs = conn.prepareStatement(sql);
					mPs.setString(1,_order.getDelayreason());
					mPs.setString(2,_order.getRfdcomments());
					mPs.setDate(3, Date2SQL(_order.getEdcompletion()));
					mPs.setString(4,_order.getOrderId());
					mPs.executeUpdate();
					
					sql = "Insert into WRM_Sunnybrook.dbo.TReason_For_Delay (TicketId,Updated_On,WorkOrder_No,Reason_For_Delay,RFD_Comments,ED_Completion) "
						+ "Select TicketId,getDate(),WorkOrder_No,Reason_For_Delay,RFD_Comments,ED_Completion from WRM_Sunnybrook.dbo.ServiceCall where WorkOrder_No = ?;";
					mPs = conn.prepareStatement(sql);
					mPs.setString(1,_order.getOrderId());
					mPs.executeUpdate();
					
				} catch (SQLException e) {
					SysLog.AppendLog("Info", "remoteDB-saveOutstandingOrder", e.getMessage());
					return false;
				}
			}
		}
		
/* Update workorder status if it is not INPRG */
		if(!_order.getStatus().equals("INPRG")) {
			PreparedStatement mPs = null;
			if(_order.getStatus().equals("COMP")) {
/* Update actstart and actfinish if the order is completed */
				String sql = "update workorder set status = ?, actstart = ?, actfinish = ?  where wonum = ?;";
				try {
					mPs = conn.prepareStatement(sql);
					mPs.setString(1,_order.getStatus());
					mPs.setDate(2, Date2SQL(_order.getActstart()));
					mPs.setDate(3,Date2SQL(_order.getActfinish()));
					mPs.setString(4,_order.getOrderId());
					mPs.executeUpdate();
				} catch (SQLException e) {
					SysLog.AppendLog("Info", "remoteDB-saveOwnOrder", e.getMessage());
					return false;
				}
			}
			else {
				String sql = "update workorder set status = ? where wonum = ? and status = ?;";
				try {
					mPs = conn.prepareStatement(sql);
					mPs.setString(1,_order.getStatus());
					mPs.setString(2,_order.getOrderId());
					mPs.setString(3,"INPRG");
					mPs.executeUpdate();
				} catch (SQLException e) {
					SysLog.AppendLog("Info", "remoteDB-saveOwnOrder", e.getMessage());
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean saveLabTrans(labtrans _labtrans, localDB _localdb) {
		String sql;
		if(_labtrans.getLabTransId()==0) {
			if(_labtrans.getRegularHrs()==0) return true;
			int mLabTransId = 0;
			PreparedStatement mPs = null;
			ResultSet mRs = null;
			sql="select max(labtransid) from labtrans;";
	 		try {
				mPs = conn.prepareStatement(sql);
				mRs = mPs.executeQuery();
				if (mRs.next())
					mLabTransId = mRs.getInt(1);
	 			mRs.close();
 			} catch (SQLException e) {
				SysLog.AppendLog("Info", "remoteDB(saveLabTrans)", e.getMessage());
				return false;
			}
			mLabTransId++;
			sql = "insert into labtrans " 
                + "(transdate,laborcode,regularhrs,enterby,enterdate,"
                + "startdate,finishdate,transtype,location,orgid," 
                + "siteid,refwo,labtransid,linecost,rollup,othrs," 
                + "otscale,payrate,outside,genapprservreceipt,enteredastask) " 
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				mPs = conn.prepareStatement(sql);
				mPs.setDate(1,Date2SQL(_labtrans.getTransDate()));
				mPs.setString(2,_labtrans.getLaborCode());
				mPs.setFloat(3, _labtrans.getRegularHrs());
				mPs.setString(4,_labtrans.getEnterBy());
				mPs.setDate(5, Date2SQL(_labtrans.getEnterDate()));
				mPs.setDate(6, Date2SQL(_labtrans.getStartDate()));
				mPs.setDate(7, Date2SQL(_labtrans.getStartDate()));
				mPs.setString(8,"WORK");
				mPs.setString(9,_labtrans.getLocation());
				mPs.setString(10, "MAXORG");
				mPs.setString(11, "MAXSITE");
				mPs.setString(12,_labtrans.getRefWo());
				mPs.setInt(13, mLabTransId);
				mPs.setInt(14,0);
				mPs.setString(15,"N");
				mPs.setInt(16,0);
				mPs.setFloat(17,(float) 1.5);
				mPs.setInt(18,0);
				mPs.setString(19,"N");
				mPs.setString(20,"Y");
				mPs.setString(21,"N");
				mPs.executeUpdate();
				_labtrans.setLabTransId(mLabTransId);
				if(!_localdb.updateLabTransId(_labtrans)) return false;
			} catch (SQLException e) {
				SysLog.AppendLog("Info", "remoteDB(saveLabTrans)", e.getMessage());
				return false;
			}
		}
		else {
			sql = "update labtrans set laborcode = ? , regularhrs = ?, " 
				+  " finishdate = ?, startdate = ? " 
				+ " where labtransid = ?; ";
			try {
				PreparedStatement mPs = conn.prepareStatement(sql);
				mPs.setString(1,_labtrans.getLaborCode());
				mPs.setFloat(2, _labtrans.getRegularHrs());
				mPs.setDate(3, Date2SQL(_labtrans.getStartDate()));
				mPs.setDate(4, Date2SQL(_labtrans.getStartDate()));
				mPs.setInt(5,_labtrans.getLabTransId());
				mPs.executeUpdate();
			} catch (SQLException e) {
				SysLog.AppendLog("Info", "remoteDB(saveLabTrans)", e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	private java.sql.Date Date2SQL(java.util.Date _date) {
		return new java.sql.Date(_date.getTime());
	}
}
