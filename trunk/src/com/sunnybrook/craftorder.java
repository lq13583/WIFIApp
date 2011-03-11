package com.sunnybrook;

import java.sql.ResultSet;
import java.sql.SQLException;

public class craftorder extends workorder{
	private String laborcode;
	private String craft;

	public craftorder(ResultSet _RS) throws SQLException {
		super(_RS);
		laborcode = _RS.getString("laborcode");
		craft = _RS.getString("craft");
	}
	
	public craftorder(String wonum,String laborcode,String craft) {
		super(wonum);
		this.laborcode = laborcode;
		this.craft = craft;
	}
	
	public void setLaborCode(String code) {
		laborcode = code;
	}
	
	public String getLaborCode(){
		return laborcode;
	}
	
	public void setCraft(String craft){
		this.craft = craft;
	}

	public String getCraft() {
		return craft;
	}
	
}
