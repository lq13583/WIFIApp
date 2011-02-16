package com.sunnybrook;

public class craftorder extends workorder{
	private String laborcode;
	private String craft;
	
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
