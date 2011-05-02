package com.sunnybrook;
import java.util.Comparator;

public class CraftorderComparator implements Comparator <craftorder> {
	private String mCompareBy = "";
	
	public CraftorderComparator(String _Compareby) {
		mCompareBy = _Compareby;
	}
	
	public int compare(craftorder arg0, craftorder arg1) {
		
		if(mCompareBy.equals("wonum")) {
			return(arg0.getOrderId().compareToIgnoreCase(arg1.getOrderId()));
		}
		if(mCompareBy.equals("status")) {
			return(arg0.getStatus().compareToIgnoreCase(arg1.getStatus()));
		}
		if(mCompareBy.equals("location")) {
			return(arg0.getLocation().compareToIgnoreCase(arg1.getLocation()));
		}
		if(mCompareBy.equals("laborcode")) {
			return(arg0.getLaborCode().compareToIgnoreCase(arg1.getLaborCode()));
		}
		if(mCompareBy.equals("laborname")) {
			return(arg0.getLaborName().compareToIgnoreCase(arg1.getLaborName()));
		}
		if(mCompareBy.equals("wopriority")) {
			return(arg0.getWopriority() - arg1.getWopriority());
		}
		if(mCompareBy.equals("reportdate")) {
			if(arg0.getReportdate()==null)
				return -1;
			else if (arg1.getReportdate() == null)
				return 1;
			else 
				return (arg0.getReportdate().compareTo(arg1.getReportdate()));
		}
		return 0;
	}

}