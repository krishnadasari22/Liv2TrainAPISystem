package com.pcompany.model;

import java.util.List;

import com.televisory.capitalmarket.model.ReportMonitorData;

public class RegionalGrowthMonitorsData {
	
	private List<ReportMonitorData> regionalMonitors;
	
	private String doubleStarMsg;
	
	private String tripleStarMsg;

	public RegionalGrowthMonitorsData() {
		
	}

	public RegionalGrowthMonitorsData(List<ReportMonitorData> monitorList,
			String doubleStarMsg, String tripleStarMsg) {
		this.regionalMonitors = monitorList;
		this.doubleStarMsg = doubleStarMsg;
		this.tripleStarMsg = tripleStarMsg;
	}

	public List<ReportMonitorData> getRegionalMonitors() {
		return regionalMonitors;
	}

	public void setRegionalMonitors(List<ReportMonitorData> regionalMonitors) {
		this.regionalMonitors = regionalMonitors;
	}

	public String getDoubleStarMsg() {
		return doubleStarMsg;
	}

	public void setDoubleStarMsg(String doubleStarMsg) {
		this.doubleStarMsg = doubleStarMsg;
	}

	public String getTripleStarMsg() {
		return tripleStarMsg;
	}

	public void setTripleStarMsg(String tripleStarMsg) {
		this.tripleStarMsg = tripleStarMsg;
	}

}
