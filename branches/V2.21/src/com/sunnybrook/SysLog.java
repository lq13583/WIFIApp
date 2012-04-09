package com.sunnybrook;

import java.util.List;

public class SysLog {
	public static localDB localdb;
	public static boolean debug_mode = false;

	public static void AppendLog(String logLevel, String logAct, String logMsg ){
		if (logLevel.equals("Info") || debug_mode)
			localdb.appendSyslog(logAct,"[" + logLevel + "]:" + logMsg);
	}
	
	public static void ClearLog(){
		localdb.clearSyslog();
	}
	
	public static List<loginfo> getLogList() {
		return localdb.getLogList();
	}
	
}
