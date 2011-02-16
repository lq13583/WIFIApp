package com.sunnybrook;

public class superorder extends workorder{
	private String laborcode;

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
