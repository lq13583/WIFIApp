package com.sunnybrook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class remoteDB {
	private String connStr;
	private Connection conn;
	
	public remoteDB(String connectionString ){
		connStr = connectionString;
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			conn = DriverManager.getConnection(connStr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void finalize(){
		try {
			if(!conn.isClosed()) conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected(){
		try {
			return(!conn.isClosed());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public workorder getWorkOrder(String wonum) {
		workorder mWorkOrder = null;
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
			if(mRs.next()) {
				mWorkOrder = new workorder(mRs.getString("wonum"));
				mWorkOrder.setStatus(mRs.getString("status"));
				mWorkOrder.setStatusdate(mRs.getDate("statusdate"));
				mWorkOrder.setDescription(mRs.getString("description"));
				mWorkOrder.setLocation(mRs.getString("location"));
				mWorkOrder.setChangedby(mRs.getString("changeby"));
				mWorkOrder.setChangedate(mRs.getDate("changedate"));
				mWorkOrder.setWopriority(mRs.getInt("wopriority"));
				mWorkOrder.setWo2(mRs.getString("wo2"));
				mWorkOrder.setWo3(mRs.getString("wo3"));
				mWorkOrder.setComments(mRs.getString("comments"));
				mWorkOrder.setReportedby(mRs.getString("reportedby"));
				mWorkOrder.setReportdate(mRs.getDate("reportdate"));
				mWorkOrder.setPhone(mRs.getString("phone"));
				mWorkOrder.setLocationdesc(mRs.getString("locdesc"));
				mWorkOrder.setEmpcomments(mRs.getString("remp"));
				mWorkOrder.setKhname(mRs.getString("khname"));
				mWorkOrder.setKtitle(mRs.getString("ktitle"));
				mWorkOrder.setKdept(mRs.getString("kdept"));
				mWorkOrder.setKcamp(mRs.getString("kcamp"));
				mWorkOrder.setKcost(mRs.getString("kcost"));
				mWorkOrder.setKcod1(mRs.getString("kcod1"));
				mWorkOrder.setKcodr1(mRs.getString("kcodr1"));
				mWorkOrder.setKcod2(mRs.getString("kcod2"));
				mWorkOrder.setKcodr2(mRs.getString("kcodr2"));
				mWorkOrder.setKcod3(mRs.getString("kcod3"));
				mWorkOrder.setKcodr3(mRs.getString("kcodr3"));
				mWorkOrder.setKcod4(mRs.getString("kcod4"));
				mWorkOrder.setKcodr4(mRs.getString("kcodr4"));
				mWorkOrder.setKcor1(mRs.getString("kcor1"));
				mWorkOrder.setKcorr1(mRs.getString("kcorr1"));
				mWorkOrder.setKcor2(mRs.getString("kcor2"));
				mWorkOrder.setKcorr2(mRs.getString("kcorr2"));
				mWorkOrder.setKcor3(mRs.getString("kcor3"));
				mWorkOrder.setKcorr3(mRs.getString("kcorr3"));
				mWorkOrder.setKcor4(mRs.getString("kcor4"));
				mWorkOrder.setKcorr4(mRs.getString("kcorr4"));
				mWorkOrder.setKcom(mRs.getString("kcom"));
				mWorkOrder.setKq1(mRs.getString("kq1"));
				mWorkOrder.setKq2(mRs.getString("kq2"));
				mWorkOrder.setKq3(mRs.getString("kq3"));
				mWorkOrder.setKq4(mRs.getString("kq4"));
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mWorkOrder;
	}
	
	public List<ownorder> getOwnOrders(String labor_code) {
		List<ownorder> orderList = new ArrayList<ownorder>();
		PreparedStatement mPs = null;
		ResultSet mRs = null;
		String sql = "select wo.wonum from workorder wo"
				   + " join assignment ass on ass.wonum = wo.wonum and ass.craft= ? "
				   + " where wo.siteid = 'MAXSITE' and wo.status in ('INPRG','WMATL') and (wo.worktype like 'C-%' or wo.worktype is null)"
				   + " order by wo.wonum";
		try {
			mPs = conn.prepareStatement(sql);
			mPs.setString(1,labor_code);
			mRs = mPs.executeQuery();
			while (mRs.next()) {
				ownorder mOwnorder = (ownorder) getWorkOrder(mRs.getString("wonum"));
				mOwnorder.setReadStatus("UR");
				orderList.add(mOwnorder);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return orderList;
	}
}
