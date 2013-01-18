package com.sunnybrook;

public class Consts {

//	Constants for msg.what 
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;	
    public static final int MSG_DATASYNC_STATUS = 3;
    public static final int MSG_LOADDATA_STATUS = 4;
    public static final int MSG_DATASYNC_ACT = 5;
    
//	Constants for data sync status as msg.arg1    
	public static final int DATASYNC_RUNNING = 10;
	public static final int DATASYNC_FINISHED = 11;

	public static final String MSG_STRING = "MSG_STRING";
}
