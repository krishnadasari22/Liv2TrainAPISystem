package com.televisory.capitalmarket.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ReportIndustryMonitor {

	private String ticsSectorCode;
	private String ticsIndustryCode;
	private Integer countryId;
	private Date period;
	private String periodType;
	private String industryType;
	
	private List<ReportIndustryMetaData> industryMetaData;
	private LinkedHashMap<String, Object> fundamentalMonitors ;
	private LinkedHashMap<String, Object> valuationMonitors ;
	private LinkedHashMap<String, Object> economyMonitors ;
	private LinkedHashMap<String, List<ReportIndustryToppersData>> industryToppersList;
	private List<ReportMonitorData> regionalMonitors;
	private List<ReportMonitorData> growthMonitorsQoQ;
	private List<ReportMonitorData> growthMonitorsYoY;
	private LinkedHashMap<String,  List<ReportQoqYoyData>> fundamentalQoqYoy ;
	private LinkedHashMap<String,  List<ReportQoqYoyData>> valuationQoqYoy ;
	private LinkedHashMap<String,  List<ReportQoqYoyData>> economyQoqYoy ;
	
	private LinkedHashMap<String, Integer> fundamentalQoqYoySlot ;
	private LinkedHashMap<String, Integer> valuationQoqYoySlot ;
	private LinkedHashMap<String, Integer> economyQoqYoySlot ;
	
	private String regionalMonitorsDoubleStarMsg;
	private String regionalMonitorsTripleStarMsg;
	private String growthMonitorsQoQDoubleStarMsg;
	private String growthMonitorsQoQTripleStarMsg;
	private String growthMonitorsYoYDoubleStarMsg;
	private String growthMonitorsYoYTripleStarMsg;
	
	private String currency;
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public ReportIndustryMonitor() {
		super();
	}
	
	public ReportIndustryMonitor(IndustryMonitorRequest industryMonitorRequest) {
		this.ticsSectorCode = industryMonitorRequest.getTicsSectorCode();
		this.ticsIndustryCode = industryMonitorRequest.getTicsIndustryCode();
		this.countryId = industryMonitorRequest.getCountryId();
		this.period = industryMonitorRequest.getPeriod();
		this.periodType = industryMonitorRequest.getPeriodType();
		this.currency = industryMonitorRequest.getCurrency();
	}
	
	public LinkedHashMap<String, Integer> getFundamentalQoqYoySlot() {
		return fundamentalQoqYoySlot;
	}
	public void setFundamentalQoqYoySlot(
			LinkedHashMap<String, Integer> fundamentalQoqYoySlot) {
		this.fundamentalQoqYoySlot = fundamentalQoqYoySlot;
	}
	public LinkedHashMap<String, Integer> getValuationQoqYoySlot() {
		return valuationQoqYoySlot;
	}
	public void setValuationQoqYoySlot(
			LinkedHashMap<String, Integer> valuationQoqYoySlot) {
		this.valuationQoqYoySlot = valuationQoqYoySlot;
	}
	public LinkedHashMap<String, Integer> getEconomyQoqYoySlot() {
		return economyQoqYoySlot;
	}
	public void setEconomyQoqYoySlot(
			LinkedHashMap<String, Integer> economyQoqYoySlot) {
		this.economyQoqYoySlot = economyQoqYoySlot;
	}
	public String getTicsSectorCode() {
		return ticsSectorCode;
	}
	public void setTicsSectorCode(String ticsSectorCode) {
		this.ticsSectorCode = ticsSectorCode;
	}
	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}
	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public String getIndustryType() {
		return industryType;
	}
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	public List<ReportIndustryMetaData> getIndustryMetaData() {
		return industryMetaData;
	}
	public void setIndustryMetaData(List<ReportIndustryMetaData> industryMetaData) {
		this.industryMetaData = industryMetaData;
	}
	public LinkedHashMap<String, Object> getFundamentalMonitors() {
		return fundamentalMonitors;
	}
	public void setFundamentalMonitors(
			LinkedHashMap<String, Object> fundamentalMonitors) {
		this.fundamentalMonitors = fundamentalMonitors;
	}
	public LinkedHashMap<String, Object> getValuationMonitors() {
		return valuationMonitors;
	}
	public void setValuationMonitors(LinkedHashMap<String, Object> valuationMonitors) {
		this.valuationMonitors = valuationMonitors;
	}
	public LinkedHashMap<String, Object> getEconomyMonitors() {
		return economyMonitors;
	}
	public void setEconomyMonitors(LinkedHashMap<String, Object> economyMonitors) {
		this.economyMonitors = economyMonitors;
	}
	public LinkedHashMap<String, List<ReportIndustryToppersData>> getIndustryToppersList() {
		return industryToppersList;
	}
	public void setIndustryToppersList(
			LinkedHashMap<String, List<ReportIndustryToppersData>> industryToppersList) {
		this.industryToppersList = industryToppersList;
	}
	public List<ReportMonitorData> getRegionalMonitors() {
		return regionalMonitors;
	}
	public void setRegionalMonitors(List<ReportMonitorData> regionalMonitors) {
		this.regionalMonitors = regionalMonitors;
	}
	public List<ReportMonitorData> getGrowthMonitorsQoQ() {
		return growthMonitorsQoQ;
	}
	public void setGrowthMonitorsQoQ(List<ReportMonitorData> growthMonitorsQoQ) {
		this.growthMonitorsQoQ = growthMonitorsQoQ;
	}
	public List<ReportMonitorData> getGrowthMonitorsYoY() {
		return growthMonitorsYoY;
	}
	public void setGrowthMonitorsYoY(List<ReportMonitorData> growthMonitorsYoY) {
		this.growthMonitorsYoY = growthMonitorsYoY;
	}
	public LinkedHashMap<String, List<ReportQoqYoyData>> getFundamentalQoqYoy() {
		return fundamentalQoqYoy;
	}
	public void setFundamentalQoqYoy(
			LinkedHashMap<String, List<ReportQoqYoyData>> fundamentalQoqYoy) {
		this.fundamentalQoqYoy = fundamentalQoqYoy;
	}
	public LinkedHashMap<String, List<ReportQoqYoyData>> getValuationQoqYoy() {
		return valuationQoqYoy;
	}
	public void setValuationQoqYoy(
			LinkedHashMap<String, List<ReportQoqYoyData>> valuationQoqYoy) {
		this.valuationQoqYoy = valuationQoqYoy;
	}
	public LinkedHashMap<String, List<ReportQoqYoyData>> getEconomyQoqYoy() {
		return economyQoqYoy;
	}
	public void setEconomyQoqYoy(
			LinkedHashMap<String, List<ReportQoqYoyData>> economyQoqYoy) {
		this.economyQoqYoy = economyQoqYoy;
	}

	public String getRegionalMonitorsDoubleStarMsg() {
		return regionalMonitorsDoubleStarMsg;
	}

	public void setRegionalMonitorsDoubleStarMsg(
			String regionalMonitorsDoubleStarMsg) {
		this.regionalMonitorsDoubleStarMsg = regionalMonitorsDoubleStarMsg;
	}

	public String getRegionalMonitorsTripleStarMsg() {
		return regionalMonitorsTripleStarMsg;
	}

	public void setRegionalMonitorsTripleStarMsg(
			String regionalMonitorsTripleStarMsg) {
		this.regionalMonitorsTripleStarMsg = regionalMonitorsTripleStarMsg;
	}

	public String getGrowthMonitorsQoQDoubleStarMsg() {
		return growthMonitorsQoQDoubleStarMsg;
	}

	public void setGrowthMonitorsQoQDoubleStarMsg(
			String growthMonitorsQoQDoubleStarMsg) {
		this.growthMonitorsQoQDoubleStarMsg = growthMonitorsQoQDoubleStarMsg;
	}

	public String getGrowthMonitorsQoQTripleStarMsg() {
		return growthMonitorsQoQTripleStarMsg;
	}

	public void setGrowthMonitorsQoQTripleStarMsg(
			String growthMonitorsQoQTripleStarMsg) {
		this.growthMonitorsQoQTripleStarMsg = growthMonitorsQoQTripleStarMsg;
	}

	public String getGrowthMonitorsYoYDoubleStarMsg() {
		return growthMonitorsYoYDoubleStarMsg;
	}

	public void setGrowthMonitorsYoYDoubleStarMsg(
			String growthMonitorsYoYDoubleStarMsg) {
		this.growthMonitorsYoYDoubleStarMsg = growthMonitorsYoYDoubleStarMsg;
	}

	public String getGrowthMonitorsYoYTripleStarMsg() {
		return growthMonitorsYoYTripleStarMsg;
	}

	public void setGrowthMonitorsYoYTripleStarMsg(
			String growthMonitorsYoYTripleStarMsg) {
		this.growthMonitorsYoYTripleStarMsg = growthMonitorsYoYTripleStarMsg;
	}

	@Override
	public String toString() {
		return "ReportIndustryMonitor [ticsSectorCode=" + ticsSectorCode
				+ ", ticsIndustryCode=" + ticsIndustryCode + ", countryId="
				+ countryId + ", period=" + period + ", periodType="
				+ periodType + ", industryType=" + industryType
				+ ", industryMetaData=" + industryMetaData
				+ ", fundamentalMonitors=" + fundamentalMonitors
				+ ", valuationMonitors=" + valuationMonitors
				+ ", economyMonitors=" + economyMonitors
				+ ", industryToppersList=" + industryToppersList
				+ ", regionalMonitors=" + regionalMonitors
				+ ", growthMonitorsQoQ=" + growthMonitorsQoQ
				+ ", growthMonitorsYoY=" + growthMonitorsYoY
				+ ", fundamentalQoqYoy=" + fundamentalQoqYoy
				+ ", valuationQoqYoy=" + valuationQoqYoy + ", economyQoqYoy="
				+ economyQoqYoy + ", fundamentalQoqYoySlot="
				+ fundamentalQoqYoySlot + ", valuationQoqYoySlot="
				+ valuationQoqYoySlot + ", economyQoqYoySlot="
				+ economyQoqYoySlot + ", currency=" + currency + "]";
	}
	
}
