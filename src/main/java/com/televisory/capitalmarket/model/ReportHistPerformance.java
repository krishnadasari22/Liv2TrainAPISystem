package com.televisory.capitalmarket.model;

public class ReportHistPerformance {
	
	private String companyName;
	private Double oneYearData;
	private Double threeYearData;
	private Double fiveYearData;
	private Double twoYearData;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public Double getOneYearData() {
		return oneYearData;
	}
	public void setOneYearData(Double oneYearData) {
		this.oneYearData = oneYearData;
	}
	public Double getThreeYearData() {
		return threeYearData;
	}
	public void setThreeYearData(Double threeYearData) {
		this.threeYearData = threeYearData;
	}
	public Double getFiveYearData() {
		return fiveYearData;
	}
	public void setFiveYearData(Double fiveYearData) {
		this.fiveYearData = fiveYearData;
	}
	
	public Double getTwoYearData() {
		return twoYearData;
	}
	public void setTwoYearData(Double twoYearData) {
		this.twoYearData = twoYearData;
	}
	@Override
	public String toString() {
		return "ReportHistPerformance [companyName=" + companyName + ", oneYearData=" + oneYearData + ", threeYearData="
				+ threeYearData + ", fiveYearData=" + fiveYearData + ", twoYearData=" + twoYearData + "]";
	}	
	
	
}
