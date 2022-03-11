package com.televisory.capitalmarket.model;

import java.util.Date;

//To Map the request in controller for generating Industry Monitor
public class IndustryMonitorRequest {

	private String ticsSectorCode;
	private String ticsIndustryCode;
	private Integer countryId;
	private Date period;
	private String periodType;
	private String currency;

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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		return "IndustryMonitorRequest [ticsSectorCode=" + ticsSectorCode
				+ ", ticsIndustryCode=" + ticsIndustryCode + ", countryId="
				+ countryId + ", period=" + period + ", periodType="
				+ periodType + ", currency=" + currency + "]";
	}


}
