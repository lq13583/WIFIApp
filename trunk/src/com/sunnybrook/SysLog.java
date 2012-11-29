package com.sunnybrook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class SysLog {

	private static boolean debug_mode = false;

	public static void appendLog(String logLevel, String logAct, String logMsg ){
		if(logLevel.equals("INFO"))
			Log.i(logAct,logMsg);
		else if(logLevel.equals("DEBUG"))
			Log.d(logAct,logMsg);
		else if(logLevel.equals("ERROR"))
			Log.e(logAct,logMsg);
		else if(logLevel.equals("WARN"))
			Log.w(logAct,logMsg);
		else
			Log.v(logAct,logMsg);
		
		if (debug_mode)
			appendFileLog(logLevel,logAct,logMsg);
	}
	
	private static void appendFileLog(String _logLevel, String _logAct, String _logMsg) {
		String msgString = (new MyDateFormat()).myFormat(new Date()) 
				 + " " + _logAct
				 + " [" + _logLevel + "]"
				 + " " + _logMsg + "\n";
		try {
			File extPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			if(!extPath.exists()) extPath.mkdirs();
			File sysLogFile = new File(extPath,"WiFiApp.log");
/*
			if(!sysLogFile.exists())
					sysLogFile.createNewFile();
*/
			FileWriter fw = new FileWriter(sysLogFile,true);
			fw.append(msgString);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// 	TODO Auto-generated catch block
				Log.e("SysLog",e.toString());
		}
	}
	
	public static void setDebugMode(boolean _mode) {
		debug_mode = _mode;
	}
	
}
