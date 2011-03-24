package com.sunnybrook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class craftorder extends workorder{
	private static final long serialVersionUID = 3L;
	private String laborcode;
	private String laborname;
	private String craft;

	public craftorder(ResultSet _RS) throws SQLException {
		super(_RS);
		laborcode = _RS.getString("laborcode");
		craft = _RS.getString("craft");
		laborname = _RS.getString("laborname");
	}

	public craftorder(HashMap<String,String> _HashMap) {
		super(_HashMap);
		laborcode = _HashMap.get("laborcode");
		laborname = _HashMap.get("laborname");
		craft = _HashMap.get("craft");
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

	public void setLaborName(String laborname) {
		this.laborname = laborname;
	}

	public String getLaborName() {
		return laborname;
	}
	
}
