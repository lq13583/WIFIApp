package com.sunnybrook;
import java.util.Comparator;

public class OwnorderComparator implements Comparator <ownorder> {
	private String mCompareBy = "";
	
	public OwnorderComparator(String _Compareby) {
		mCompareBy = _Compareby;
	}
	
	public int compare(ownorder arg0, ownorder arg1) {
		
		if(mCompareBy.equals("wonum")) {
			return(arg0.getOrderId().compareToIgnoreCase(arg1.getOrderId()));
		}
		if(mCompareBy.equals("status")) {
			return(arg0.getStatus().compareToIgnoreCase(arg1.getStatus()));
		}
		if(mCompareBy.equals("readstatus")) {
			return(arg0.getReadStatus().compareToIgnoreCase(arg1.getReadStatus()));
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