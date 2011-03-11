package com.sunnybrook;

import java.sql.ResultSet;
import java.sql.SQLException;

public class superorder extends workorder{
	private String laborcode;
	
	public superorder(ResultSet _RS) throws SQLException {
		super(_RS);
		laborcode = _RS.getString("laborcode");
	}

	public superorder(String wonum,String laborcode){
		super(wonum);
		this.laborcode=laborcode;
	}
	
	public String getLaborCode(){
		return laborcode;
	}
	
	public void setLaborCode(String code){
		laborcode=code;
	}
}
