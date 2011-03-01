package com.sunnybrook;

import java.util.List;

public class ownorder extends workorder{
	private List<labtrans> translist;
	private String mycomments;
	private String readstatus;
	
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
	
}
