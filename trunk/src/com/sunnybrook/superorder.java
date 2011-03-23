package com.sunnybrook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class superorder extends workorder{

	private static final long serialVersionUID = 2L;
	private String laborcode;
	private String laborname;
	
	public superorder(ResultSet _RS) throws SQLException {
		super(_RS);
		laborcode = _RS.getString("laborcode");
		laborname = _RS.getString("laborname");
	}

	public superorder(HashMap<String,String> _HashMap) {
		super(_HashMap);
		laborcode = _HashMap.get("laborcode");
		laborname = _HashMap.get("laborname");
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

	public void setLaborName(String laborname) {
		this.laborname = laborname;
	}

	public String getLaborName() {
		return laborname;
	}

}
