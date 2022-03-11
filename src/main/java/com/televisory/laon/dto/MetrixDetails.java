package com.televisory.laon.dto;

import java.util.List;

import com.televisory.capitalmarket.model.ReportPerformanceData;

public class MetrixDetails {
	
	String metrixName;
	
	/////// the list 0 is user , list 1 is comp1 , list 2 is comp 2 , list 3 is peer
	List<List<ReportPerformanceData>> data;

	public String getMetrixName() {
		return metrixName;
	}

	public void setMetrixName(String metrixName) {
		this.metrixName = metrixName;
	}

	public List<List<ReportPerformanceData>> getData() {
		return data;
	}

	public void setData(List<List<ReportPerformanceData>> data) {
		this.data = data;
	}
	

}
