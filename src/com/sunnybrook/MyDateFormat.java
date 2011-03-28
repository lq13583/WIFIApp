package com.sunnybrook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MyDateFormat extends SimpleDateFormat{

	private static final long serialVersionUID = 5L;
	
	public MyDateFormat(){
		super("yyyy-MM-dd HH:mm:ss");
	}
	
	public MyDateFormat(String _format) {
		super(_format);
	}

	public String myFormat(Date _date) {
		return (_date==null?"null":format(_date));
	}
	
	public Date myParse(String _string) throws ParseException {
		return(_string==null?null:parse(_string));
	}
}
