package com.sunnybrook;

import java.util.Comparator;

public class WorkorderComparator implements Comparator <workorder>{
	private String mCompareBy = "";
	
	public WorkorderComparator(String _Compareby) {
		mCompareBy = _Compareby;
	}
	
	@Override
	public int compare(workorder arg0, workorder arg1) {
		if(mCompareBy.equals("wonum")) {
			return(arg0.getOrderId().compareToIgnoreCase(arg1.getOrderId()));
		}
		if(mCompareBy.equals("status")) {
			return(arg0.getStatus().compareToIgnoreCase(arg1.getStatus()));
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
