package com.televisory.capitalmarket.model;

import java.util.List;

/**
 * 
 * @author vinay
 *
 */
public class ReportIndustryMetaData {

	private String industryName;
	private String industryDescription;
	private String countryCoverage;
	private Integer companyCoverage;
	private Double totMarketCap;
	private Double totRevenue;
	private String currency;
	private String unit;
	
	private List<ReportPerformanceData> quartileDataByRev;
	private List<ReportPerformanceData> quartileDataByMcap;
	
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getIndustryDescription() {
		return industryDescription;
	}
	public void setIndustryDescription(String industryDescription) {
		this.industryDescription = industryDescription;
	}
	
	public Double getTotMarketCap() {
		return totMarketCap;
	}
	public void setTotMarketCap(Double totMarketCap) {
		this.totMarketCap = totMarketCap;
	}
	public Double getTotRevenue() {
		return totRevenue;
	}
	public void setTotRevenue(Double totRevenue) {
		this.totRevenue = totRevenue;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCountryCoverage() {
		return countryCoverage;
	}
	public void setCountryCoverage(String countryCoverage) {
		this.countryCoverage = countryCoverage;
	}
	public Integer getCompanyCoverage() {
		return companyCoverage;
	}
	public void setCompanyCoverage(Integer companyCoverage) {
		this.companyCoverage = companyCoverage;
	}
	public List<ReportPerformanceData> getQuartileDataByRev() {
		return quartileDataByRev;
	}
	public void setQuartileDataByRev(List<ReportPerformanceData> quartileDataByRev) {
		this.quartileDataByRev = quartileDataByRev;
	}
	public List<ReportPerformanceData> getQuartileDataByMcap() {
		return quartileDataByMcap;
	}
	public void setQuartileDataByMcap(List<ReportPerformanceData> quartileDataByMcap) {
		this.quartileDataByMcap = quartileDataByMcap;
	}
	@Override
	public String toString() {
		return "ReportIndustryMetaData [industryName=" + industryName + ", industryDescription=" + industryDescription
				+ ", countryCoverage=" + countryCoverage + ", companyCoverage=" + companyCoverage + ", totMarketCap="
				+ totMarketCap + ", totRevenue=" + totRevenue + ", currency=" + currency + ", unit=" + unit
				+ ", quartileDataByRev=" + quartileDataByRev + ", quartileDataByMcap=" + quartileDataByMcap + "]";
	}
	
	
	
	
}
