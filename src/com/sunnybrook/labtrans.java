package com.sunnybrook;

import java.util.Date;

public class labtrans {
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
	
	public int getTransId(){
		return transid;
	}
	
	public int getLabTransId() {
		return labtransid;
	}
	
	public Date getTransDate() {
		return transdate;
	}
	
	public String getLaborCode() {
		return laborcode;
	}
	
	public String getRefWo() {
		return refwo;
	}
	
	public String getEnterBy(){
		return enterby;
	}
	
	public Date getEnterDate() {
		return enterdate;
	}
	
	public Date getStartDate() {
		return startdate;
	}
	
	public float getRegularHrs() {
		return regularhrs;
	}
	
	public String getLocation() {
		return location;
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
