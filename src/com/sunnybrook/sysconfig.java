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
	private int list_font_size;
	private long update_int;
	private long update_int_max;
	private String jdbc_url;
	private String ssid;
	private String network_key;
	private boolean debug_mode;
	private int outstanding_days;
	private String order_own;
	private String order_craft;
	private String order_super;
	private String order_update;

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
    	mRet = localdb.getSysConfig("update_int");
    	update_int = (mRet == null)?300000:Long.parseLong(mRet);
    	mRet = localdb.getSysConfig("update_int_max");
    	update_int_max = (mRet == null)?600000:Long.parseLong(mRet);
    	mRet = localdb.getSysConfig("font_size");
    	font_size = (mRet == null)?25:Integer.parseInt(mRet);
    	mRet = localdb.getSysConfig("desc_font_size");
    	desc_font_size = (mRet == null)?25:Integer.parseInt(mRet);
    	mRet = localdb.getSysConfig("list_font_size");
    	list_font_size = (mRet == null)?25:Integer.parseInt(mRet);
        jdbc_url = localdb.getSysConfig("jdbc_url");
        ssid = localdb.getSysConfig("ssid");
        network_key = localdb.getSysConfig("network_key");
        craft_list = new ArrayList<String>();
		setCrafts(localdb.getSysConfig("crafts"));
    	outstanding_days = Integer.parseInt(localdb.getSysConfig("outstanding_days"));
    	order_own = localdb.getSysConfig("order_own");
    	if(order_own == null) order_own = "wonum";
    	order_craft = localdb.getSysConfig("order_craft");
    	if(order_craft == null) order_craft = "wonum";
    	order_super = localdb.getSysConfig("order_super");
    	if(order_super == null) order_super = "wonum";
    	order_update = localdb.getSysConfig("order_update");
    	if(order_update == null) order_update = "wonum";
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
		localdb.saveSysConfig("list_font_size", Integer.toString(list_font_size));
		localdb.saveSysConfig("jdbc_url", jdbc_url);
		localdb.saveSysConfig("ssid", ssid);
		localdb.saveSysConfig("network_key", network_key);
		localdb.saveSysConfig("crafts", getCrafts());
		localdb.saveSysConfig("outstanding_days", Integer.toString(outstanding_days));
	}
	
	public String getCrafts() {
		String mCrafts = "";
		Iterator<String> itr = craft_list.iterator();
		while (itr.hasNext()) {
			mCrafts += itr.next() + ";";
		}
		return mCrafts;
	}
	
	public void setCrafts(String _Crafts) {
		String mCrafts=_Crafts.replace("\n","");
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
		localdb.saveSysConfig("list_font_size", Integer.toString(list_font_size));
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

	public void setList_font_size(int font_size) {
		this.list_font_size = font_size;
	}
	public int getList_font_size() {
		return list_font_size;
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

	public void setOutstanding_days(int outstanding_days) {
		this.outstanding_days = outstanding_days;
	}

	public int getOutstanding_days() {
		return outstanding_days;
	}

	public String getOrder_own() {
		return order_own;
	}

	public void setOrder_own(String order_own) {
		this.order_own = order_own;
	}

	public void saveOrderOwnToDB(localDB localdb){
		localdb.saveSysConfig("order_own",order_own );
	}

	public String getOrder_craft() {
		return order_craft;
	}

	public void setOrder_craft(String order_craft) {
		this.order_craft = order_craft;
	}

	public void saveOrderCraftToDB(localDB localdb){
		localdb.saveSysConfig("order_craft",order_craft );
	}

	public String getOrder_super() {
		return order_super;
	}

	public void setOrder_super(String order_super) {
		this.order_super = order_super;
	}

	public void saveOrderSuperToDB(localDB localdb){
		localdb.saveSysConfig("order_super",order_super );
	}
	
	public String getOrder_update() {
		return order_update;
	}

	public void setOrder_update(String order_update) {
		this.order_update = order_update;
	}

	public void saveOrderUpdateToDB(localDB localdb){
		localdb.saveSysConfig("order_update",order_update );
	}
	
}
