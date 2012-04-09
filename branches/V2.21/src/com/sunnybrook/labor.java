package com.sunnybrook;

public class labor {
	private String laborcode;
	private String laborname;
	
	public labor(String code, String name){
		laborcode = code;
		laborname = name;
	}
	
	public String getName() {
		return laborname;
	}
	
	public String getCode() {
		return laborcode;
	}
}
