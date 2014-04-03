package com.sunnybrook;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

public class labtrans implements Serializable{

	private static final long serialVersionUID = 6087761531722409998L;
	private int transid;
	private int labtransid;
	private Date transdate;
	private String laborcode;
	private String refwo;
	private String enterby;
	private Date enterdate;
	private Date startdate;
	private float regularhrs;
	private String location;
	
	public labtrans(String _laborcode,Date _startdate, float _hrs, String _refwo, String _location ) {
		laborcode = _laborcode;
		startdate = _startdate;
		regularhrs = _hrs;
		refwo = _refwo;
		location = _location;
		transid = 0;
		labtransid = 0;
	}

	public labtrans(HashMap<String,String> _HashMap) {
		transid = Integer.parseInt(_HashMap.get("transid"));
		labtransid = Integer.parseInt(_HashMap.get("labtransid"));
		MyDateFormat myDateFormat = new MyDateFormat();
		try {
			transdate = myDateFormat.myParse(_HashMap.get("transdate"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		laborcode = _HashMap.get("laborcode");
		refwo = _HashMap.get("refwo");
		enterby = _HashMap.get("enterby");
		try {
			enterdate = myDateFormat.myParse(_HashMap.get("enterdate"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		try {
			startdate = myDateFormat.myParse(_HashMap.get("startdate"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		regularhrs = Float.parseFloat(_HashMap.get("regularhrs"));
		location = _HashMap.get("location");
	}
	
	public int getTransId(){
		return transid;
	}
	
	public void setTransId(int _transid) {
		transid = _transid;
	}
	
	public int getLabTransId() {
		return labtransid;
	}
	
	public void setLabTransId(int _labtransid) {
		labtransid = _labtransid;
	}
	
	public Date getTransDate() {
		return transdate;
	}
	
	public void setTransDate(Date _transdate) {
		transdate = _transdate;
	}
	
	public String getLaborCode() {
		return laborcode;
	}
	
	public void setLaborCode(String _laborcode) {
		laborcode = _laborcode;
	}
	
	public String getRefWo() {
		return refwo;
	}
	
	public void setRefWo(String _refwo) {
		refwo = _refwo;
	}

	public String getEnterBy(){
		return enterby;
	}
	
	public void setEnterBy(String _enterby) {
		enterby = _enterby;
	}

	public Date getEnterDate() {
		return enterdate;
	}
	
	public void setEnterDate(Date _enterdate) {
		enterdate = _enterdate;
	}
	
	public Date getStartDate() {
		return startdate;
	}

	public void setStartDate(Date _startdate) {
		startdate = _startdate;
	}
	
	public float getRegularHrs() {
		return regularhrs;
	}
	
	public void setRegularHrs(float _hrs) {
		regularhrs = _hrs;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String _location) {
		location = _location;
	}

/* Save data into local SQLite DB. */	
	public boolean saveLocal(){
		return true;
	}
	
/* Save data into remote MSSQL DB. */
	public boolean SaveRemote(){
		return true;
	}
}
