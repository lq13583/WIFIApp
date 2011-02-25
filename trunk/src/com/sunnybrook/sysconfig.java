package com.sunnybrook;

import java.util.List;

public class sysconfig {
	private String labor_code;
	private String labor_name;
	private List<String> craft_list;
	private boolean is_super;
	private boolean update_key;
	private int font_size;
	private int desc_font_size;
	private int update_int;
	private int update_int_max;
	private String jdbc_url;

	public sysconfig(localDB localdb){
    	labor_code = localdb.getSysConfig("labor_code");
    	labor_name = localdb.getSysConfig("labor_name");
    	is_super = localdb.getSysConfig("is_super").equals("yes");
    	update_key = localdb.getSysConfig("update_key").equals("yes");
    	update_int = Integer.parseInt(localdb.getSysConfig("update_int"));
    	update_int_max = Integer.parseInt(localdb.getSysConfig("update_int_max"));
    	font_size = Integer.parseInt(localdb.getSysConfig("font_size"));
    	desc_font_size = Integer.parseInt(localdb.getSysConfig("desc_font_size"));
        jdbc_url = localdb.getSysConfig("jdbc_url");
	}
	
	public void saveAllToDB(localDB localdb){
		localdb.saveSysConfig("labor_code", labor_code);
		localdb.saveSysConfig("labor_name", labor_name);
		localdb.saveSysConfig("is_super", is_super?"yes":"no");
		localdb.saveSysConfig("update_key",update_key?"yes":"no");
		localdb.saveSysConfig("update_int",Integer.toString(update_int) );
		localdb.saveSysConfig("update_int_max",Integer.toString(update_int_max));
		localdb.saveSysConfig("font_size", Integer.toString(font_size));
		localdb.saveSysConfig("desc_font_size", Integer.toString(desc_font_size));
		localdb.saveSysConfig("jdbc_url", jdbc_url);
	}
	
	public void saveFontToDB(localDB localdb){
		localdb.saveSysConfig("font_size", Integer.toString(font_size));
		localdb.saveSysConfig("desc_font_size", Integer.toString(desc_font_size));
	}
	
	public void saveUpdateIntToDB(localDB localdb){
		localdb.saveSysConfig("update_int",Integer.toString(update_int) );
		localdb.saveSysConfig("update_int_max",Integer.toString(update_int_max));
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

	public void setCraft_list(List<String> craft_list) {
		this.craft_list = craft_list;
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

	public void setUpdate_int(int update_int) {
		this.update_int = update_int;
	}

	public int getUpdate_int() {
		return update_int;
	}

	public void setUpdate_int_max(int max_update_int) {
		this.update_int_max = max_update_int;
	}

	public int getUpdate_int_max() {
		return update_int_max;
	}

}
