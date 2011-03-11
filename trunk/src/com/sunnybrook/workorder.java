package com.sunnybrook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class workorder {
	private String wonum;
	private String status;
	private Date statusdate;
	private String description;
	private String location;
	private String locationdesc;
	private String changeby;
	private Date changedate;
	private int wopriority;
	private String wo2;
	private String wo3;
	private String comments;
	private String reportedby;
	private Date reportdate;
	private String phone;
	private Date actstart;
	private Date actfinish;
	private String empcomments;
	private String khname;
	private String ktitle;
	private String kdept;
	private String kcamp;
	private String kcost;
	private String kcod1;
	private String kcodr1;
	private String kcod2;
	private String kcodr2;
	private String kcod3;
	private String kcodr3;
	private String kcod4;
	private String kcodr4;
	private String kcor1;
	private String kcorr1;
	private String kcor2;
	private String kcorr2;
	private String kcor3;
	private String kcorr3;
	private String kcor4;
	private String kcorr4;
	private String kcom;
	private String kq1;
	private String kq2;
	private String kq3;
	private String kq4;

	public workorder(ResultSet mRs) throws SQLException {
		wonum = mRs.getString("wonum");
		status = mRs.getString("status");
		statusdate = mRs.getTimestamp("statusdate");
		description = mRs.getString("description");
		location = mRs.getString("location");
		changeby = mRs.getString("changeby");
		changedate = mRs.getTimestamp("changedate");
		wopriority = mRs.getInt("wopriority");
		wo2 = mRs.getString("wo2");
		wo3 = mRs.getString("wo3");
		comments = mRs.getString("comments");
		reportedby = mRs.getString("reportedby");
		setReportdate(mRs.getTimestamp("reportdate"));
		setPhone(mRs.getString("phone"));
		setLocationdesc(mRs.getString("locdesc"));
		setEmpcomments(mRs.getString("remp"));
		setKhname(mRs.getString("khname"));
		setKtitle(mRs.getString("ktitle"));
		setKdept(mRs.getString("kdept"));
		setKcamp(mRs.getString("kcamp"));
		setKcost(mRs.getString("kcost"));
		setKcod1(mRs.getString("kcod1"));
		setKcodr1(mRs.getString("kcodr1"));
		setKcod2(mRs.getString("kcod2"));
		setKcodr2(mRs.getString("kcodr2"));
		setKcod3(mRs.getString("kcod3"));
		setKcodr3(mRs.getString("kcodr3"));
		setKcod4(mRs.getString("kcod4"));
		setKcodr4(mRs.getString("kcodr4"));
		setKcor1(mRs.getString("kcor1"));
		setKcorr1(mRs.getString("kcorr1"));
		setKcor2(mRs.getString("kcor2"));
		setKcorr2(mRs.getString("kcorr2"));
		setKcor3(mRs.getString("kcor3"));
		setKcorr3(mRs.getString("kcorr3"));
		setKcor4(mRs.getString("kcor4"));
		setKcorr4(mRs.getString("kcorr4"));
		setKcom(mRs.getString("kcom"));
		setKq1(mRs.getString("kq1"));
		setKq2(mRs.getString("kq2"));
		setKq3(mRs.getString("kq3"));
		setKq4(mRs.getString("kq4"));
	}
	
	public workorder(String wonum){
		this.wonum=wonum;
	}
	
	public String getOrderId(){
		return wonum;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatusdate(Date statusdate) {
		this.statusdate = statusdate;
	}

	public Date getStatusdate() {
		return statusdate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocationdesc(String locationdesc) {
		this.locationdesc = locationdesc;
	}

	public String getLocationdesc() {
		return locationdesc;
	}

	public void setChangeby(String changeby) {
		this.changeby = changeby;
	}

	public String getChangeby() {
		return changeby;
	}

	public void setChangedate(Date changedate) {
		this.changedate = changedate;
	}

	public Date getChangedate() {
		return changedate;
	}

	public void setWopriority(int wopriority) {
		this.wopriority = wopriority;
	}

	public int getWopriority() {
		return wopriority;
	}

	public void setWo2(String wo2) {
		this.wo2 = wo2;
	}

	public String getWo2() {
		return wo2;
	}

	public void setWo3(String wo3) {
		this.wo3 = wo3;
	}

	public String getWo3() {
		return wo3;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

	public void setReportedby(String reportedby) {
		this.reportedby = reportedby;
	}

	public String getReportedby() {
		return reportedby;
	}

	public void setReportdate(Date reportdate) {
		this.reportdate = reportdate;
	}

	public Date getReportdate() {
		return reportdate;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setActstart(Date actstart) {
		this.actstart = actstart;
	}

	public Date getActstart() {
		return actstart;
	}

	public void setActfinish(Date actfinish) {
		this.actfinish = actfinish;
	}

	public Date getActfinish() {
		return actfinish;
	}

	public void setEmpcomments(String empcomments) {
		this.empcomments = empcomments;
	}

	public String getEmpcomments() {
		return empcomments;
	}

	public void setKhname(String khname) {
		this.khname = khname;
	}

	public String getKhname() {
		return khname;
	}

	public void setKtitle(String ktitle) {
		this.ktitle = ktitle;
	}

	public String getKtitle() {
		return ktitle;
	}

	public void setKdept(String kdept) {
		this.kdept = kdept;
	}

	public String getKdept() {
		return kdept;
	}

	public void setKcamp(String kcamp) {
		this.kcamp = kcamp;
	}

	public String getKcamp() {
		return kcamp;
	}

	public void setKcost(String kcost) {
		this.kcost = kcost;
	}

	public String getKcost() {
		return kcost;
	}

	public void setKcod1(String kcod1) {
		this.kcod1 = kcod1;
	}

	public String getKcod1() {
		return kcod1;
	}

	public void setKcodr1(String kcodr1) {
		this.kcodr1 = kcodr1;
	}

	public String getKcodr1() {
		return kcodr1;
	}

	public void setKcod2(String kcod2) {
		this.kcod2 = kcod2;
	}

	public String getKcod2() {
		return kcod2;
	}

	public void setKcodr2(String kcodr2) {
		this.kcodr2 = kcodr2;
	}

	public String getKcodr2() {
		return kcodr2;
	}

	public void setKcod3(String kcod3) {
		this.kcod3 = kcod3;
	}

	public String getKcod3() {
		return kcod3;
	}

	public void setKcodr3(String kcodr3) {
		this.kcodr3 = kcodr3;
	}

	public String getKcodr3() {
		return kcodr3;
	}

	public void setKcod4(String kcod4) {
		this.kcod4 = kcod4;
	}

	public String getKcod4() {
		return kcod4;
	}

	public void setKcodr4(String kcodr4) {
		this.kcodr4 = kcodr4;
	}

	public String getKcodr4() {
		return kcodr4;
	}

	public void setKcor1(String kcor1) {
		this.kcor1 = kcor1;
	}

	public String getKcor1() {
		return kcor1;
	}

	public void setKcorr1(String kcorr1) {
		this.kcorr1 = kcorr1;
	}

	public String getKcorr1() {
		return kcorr1;
	}

	public void setKcor2(String kcor2) {
		this.kcor2 = kcor2;
	}

	public String getKcor2() {
		return kcor2;
	}

	public void setKcorr2(String kcorr2) {
		this.kcorr2 = kcorr2;
	}

	public String getKcorr2() {
		return kcorr2;
	}

	public void setKcor3(String kcor3) {
		this.kcor3 = kcor3;
	}

	public String getKcor3() {
		return kcor3;
	}

	public void setKcorr3(String kcorr3) {
		this.kcorr3 = kcorr3;
	}

	public String getKcorr3() {
		return kcorr3;
	}

	public void setKcor4(String kcor4) {
		this.kcor4 = kcor4;
	}

	public String getKcor4() {
		return kcor4;
	}

	public void setKcorr4(String kcorr4) {
		this.kcorr4 = kcorr4;
	}

	public String getKcorr4() {
		return kcorr4;
	}

	public void setKcom(String kcom) {
		this.kcom = kcom;
	}

	public String getKcom() {
		return kcom;
	}

	public void setKq1(String kq1) {
		this.kq1 = kq1;
	}

	public String getKq1() {
		return kq1;
	}

	public void setKq2(String kq2) {
		this.kq2 = kq2;
	}

	public String getKq2() {
		return kq2;
	}

	public void setKq3(String kq3) {
		this.kq3 = kq3;
	}

	public String getKq3() {
		return kq3;
	}

	public void setKq4(String kq4) {
		this.kq4 = kq4;
	}

	public String getKq4() {
		return kq4;
	}

}
