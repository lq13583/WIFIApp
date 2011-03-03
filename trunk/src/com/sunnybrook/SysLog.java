package com.sunnybrook;

public class SysLog {
	public static localDB localdb;
	public static boolean debug_mode = false;

	public static void AppendLog(String logLevel, String logAct, String logMsg ){
		if (logLevel.equals("INFO") || debug_mode)
			localdb.appendSyslog(logAct,logMsg);
	}
	
	public static void ClearLog(){
		localdb.clearSyslog();
	}
	
	
}
