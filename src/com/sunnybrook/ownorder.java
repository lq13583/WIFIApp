package com.sunnybrook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ownorder extends workorder{
	private static final long serialVersionUID = 4L;
	private List<labtrans> translist;
	private String mycomments;
	private String readstatus;
	private boolean needsupdate;
	private String delayreason;
	private String rfdcomments;
	private Date edcompletion;

	public ownorder(ResultSet mRS) throws SQLException {
		super(mRS);
		needsupdate = false;
	}

	public ownorder(HashMap<String,String> _HashMap) {
		super(_HashMap);
		MyDateFormat myDateFormat = new MyDateFormat();
		readstatus = _HashMap.get("readstatus");
		mycomments = _HashMap.get("mycomments")==null?"":_HashMap.get("mycomments");
		needsupdate = _HashMap.get("upexisted")!=null;
		if(needsupdate) {
			delayreason = _HashMap.get("Reason_For_Delay")==null?"":_HashMap.get("Reason_For_Delay");
			rfdcomments = _HashMap.get("RFD_Comments")==null?"":_HashMap.get("RFD_Comments");
			try {
				edcompletion = myDateFormat.myParse(_HashMap.get("ED_Completion"));
			} catch (ParseException e) {
				SysLog.appendLog("ERROR", "ownorder", e.getMessage());
			}
		}
	}

	public ownorder(String wonum){
		super(wonum);
	}
	
	public String getMyComments(){
		return mycomments;
	}
	
	public void setMyComments(String comments) {
		mycomments = comments;
	}
	
	public String getReadStatus() {
		return readstatus;
	}
	
	public void setReadStatus(String status) {
		readstatus = status;
	}

	public List<labtrans> getTranslist() {
		return translist;
	}
	
	public void addLabTrans(labtrans _labtrans) {
		if(translist == null) translist = new ArrayList<labtrans>();
		translist.add(_labtrans);
	}

	public void setNeedsupdate(boolean _needsupdate) {
		this.needsupdate = _needsupdate;
	}

	public boolean isNeedsupdate() {
		return needsupdate;
	}

	public void setDelayreason(String _delayreason) {
		this.delayreason = _delayreason;
	}

	public String getDelayreason() {
		return delayreason;
	}

	public void setRfdcomments(String _rfdcomments) {
		this.rfdcomments = _rfdcomments;
	}

	public String getRfdcomments() {
		return rfdcomments;
	}

	public void setEdcompletion(Date _edcompletion) {
		this.edcompletion = _edcompletion;
	}

	public Date getEdcompletion() {
		return edcompletion;
	}
	
	public float getTotalHrs() {
		float mTotal = 0;
		if(translist != null) {
			Iterator<labtrans> mIt = translist.iterator();
			while(mIt.hasNext()) {
				labtrans mLabtrans = (labtrans) mIt.next();
				mTotal += mLabtrans.getRegularHrs();
			}
		}
		return mTotal;
	}
}
