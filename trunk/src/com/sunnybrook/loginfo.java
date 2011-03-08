package com.sunnybrook;

public class loginfo {
	private int logid;
	private String logtime;
	private String logact;
	private String logdesc;
	
	public loginfo(int _logid, String _logtime, String _logact, String _logdesc) {
		logid = _logid;
		logtime = _logtime;
		logact = _logact;
		logdesc = _logdesc;
	}

	public void setLogid(int logid) {
		this.logid = logid;
	}
	public int getLogid() {
		return logid;
	}
	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
	public String getLogtime() {
		return logtime;
	}
	public void setLogact(String logact) {
		this.logact = logact;
	}
	public String getLogact() {
		return logact;
	}
	public void setLogdesc(String logdesc) {
		this.logdesc = logdesc;
	}
	public String getLogdesc() {
		return logdesc;
	}
	
}
