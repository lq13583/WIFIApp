package com.sunnybrook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MyDateFormat extends SimpleDateFormat{

	private static final long serialVersionUID = 5L;
	private static String fullPattern = "yyyy-MM-dd HH:mm:ss";
	private static String datePattern = "yyyy-MM-dd";
	private static String timePattern = "HH:mm:ss";
	public MyDateFormat(){
		super(fullPattern);
	}
	
	public MyDateFormat(String _format) {
		super(_format);
	}

	public String myFormat(Date _date) {
		applyPattern(fullPattern);
		return (_date==null?"null":format(_date));
	}
	
	public Date myParse(String _string) throws ParseException {
		applyPattern(fullPattern);
		return(_string==null?null:parse(_string));
	}
	
	public String myDateFormat(Date _date) {
		applyPattern(datePattern);
		return (_date==null?"null":format(_date));
	}
	
	public String myTimeFormat(Date _date) {
		applyPattern(timePattern);
		return (_date==null?"null":format(_date));
	}
}
