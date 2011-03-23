package com.sunnybrook;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class MyDateFormat extends SimpleDateFormat{

	private static final long serialVersionUID = 5L;
	
	public MyDateFormat(){
		super("yyyy-MM-dd HH:mm:ss");
	}

	public String myFormat(Date _date) {
		return (_date==null?"null":format(_date));
	}
}
