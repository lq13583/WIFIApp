package com.sunnybrook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class sysconfig {
	private String labor_code;
	private String labor_name;
	private List<String> craft_list;
	private boolean is_super;
	private boolean update_key;
	private int font_size;
	private int desc_font_size;
	private long update_int;
	private long update_int_max;
	private String jdbc_url;
	private String ssid;
	private String network_key;
	private boolean debug_mode;

	public sysconfig(localDB localdb){
		String mRet;
    	labor_code = localdb.getSysConfig("labor_code");
    	labor_name = localdb.getSysConfig("labor_name");
    	mRet = localdb.getSysConfig("is_super");
    	is_super = (mRet == null)?false:mRet.equals("yes");
    	mRet = localdb.getSysConfig("update_key");
    	update_key = (mRet == null)?false:mRet.equals("yes");
    	mRet = localdb.getSysConfig("debug_mode");
    	debug_mode = (mRet == null)?false:mRet.equals("yes");
    	update_int = Long.parseLong(localdb.getSysConfig("update_int"));
    	update_int_max = Long.parseLong(localdb.getSysConfig("update_int_max"));
    	font_size = Integer.parseInt(localdb.getSysConfig("font_size"));
    	desc_font_size = Integer.parseInt(localdb.getSysConfig("desc_font_size"));
        jdbc_url = localdb.getSysConfig("jdbc_url");
        ssid = localdb.getSysConfig("ssid");
        network_key = localdb.getSysConfig("network_key");
        craft_list = new ArrayList<String>();
		setCrafts(localdb.getSysConfig("crafts"));
	}
	
	public void saveAllToDB(localDB localdb){
		localdb.saveSysConfig("labor_code", labor_code);
		localdb.saveSysConfig("labor_name", labor_name);
		localdb.saveSysConfig("is_super", is_super?"yes":"no");
		localdb.saveSysConfig("update_key",update_key?"yes":"no");
		localdb.saveSysConfig("debug_mode",debug_mode?"yes":"no");
		localdb.saveSysConfig("update_int",Long.toString(update_int) );
		localdb.saveSysConfig("update_int_max",Long.toString(update_int_max));
		localdb.saveSysConfig("font_size", Integer.toString(font_size));
		localdb.saveSysConfig("desc_font_size", Integer.toString(desc_font_size));
		localdb.saveSysConfig("jdbc_url", jdbc_url);
		localdb.saveSysConfig("ssid", ssid);
		localdb.saveSysConfig("network_key", network_key);
		localdb.saveSysConfig("crafts", getCrafts());
	}
	
	public String getCrafts() {
		String mCrafts = "";
		Iterator<String> itr = craft_list.iterator();
		while (itr.hasNext()) {
			mCrafts += itr.next() + ";";
		}
		return mCrafts;
	}
	
	public void setCrafts(String mCrafts) {
		StringTokenizer st = new StringTokenizer(mCrafts,";");
		craft_list.removeAll(craft_list);
		while(st.hasMoreTokens()){
			String craft = st.nextToken();
			craft_list.add(craft);
		}
	}
	
	public void saveFontToDB(localDB localdb){
		localdb.saveSysConfig("font_size", Integer.toString(font_size));
		localdb.saveSysConfig("desc_font_size", Integer.toString(desc_font_size));
	}
	
	public void saveUpdateIntToDB(localDB localdb){
		localdb.saveSysConfig("update_int",Long.toString(update_int) );
		localdb.saveSysConfig("update_int_max",Long.toString(update_int_max));
	}
	
	public void setLabor_code(String labor_code) {
		this.labor_code = labor_code;
	}
	public String getLabor_code() {
		return labor_code;
	}
	public void setLabor_name(String labor_name) {
		this.labor_name = labor_name;
	}
	public String getLabor_name() {
		return labor_name;
	}

	public List<String> getCraft_list() {
		return craft_list;
	}
	public void set_super(boolean is_super) {
		this.is_super = is_super;
	}
	public boolean is_super() {
		return is_super;
	}
	public void setUpdate_key(boolean update_key) {
		this.update_key = update_key;
	}
	public boolean isUpdate_key() {
		return update_key;
	}
	public void setFont_size(int font_size) {
		this.font_size = font_size;
	}
	public int getFont_size() {
		return font_size;
	}
	public void setDesc_font_size(int desc_font_size) {
		this.desc_font_size = desc_font_size;
	}
	public int getDesc_font_size() {
		return desc_font_size;
	}
	public void setJdbc_url(String jdbc_url) {
		this.jdbc_url = jdbc_url;
	}
	public String getJdbc_url() {
		return jdbc_url;
	}

	public void setUpdate_int(long update_int) {
		this.update_int = update_int;
	}

	public long getUpdate_int() {
		return update_int;
	}

	public void setUpdate_int_max(long max_update_int) {
		this.update_int_max = max_update_int;
	}

	public long getUpdate_int_max() {
		return update_int_max;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setNetwork_key(String networkkey) {
		this.network_key = networkkey;
	}

	public String getNetwork_key() {
		return network_key;
	}

	public void setDebug_mode(boolean debug_mode) {
		this.debug_mode = debug_mode;
	}

	public boolean isDebug_mode() {
		return debug_mode;
	}

}
