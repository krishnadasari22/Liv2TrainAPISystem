package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

public class CompanyLatestFilingInfoDTO implements Serializable {

	private static final long serialVersionUID = 516323861576898345L;
	
	private String periodType;

	private Date period;
	
	private String currency;
	
	private String reportingCurrency;
	
	private String companyId;
	
	private Character statusCode;

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getReportingCurrency() {
		return reportingCurrency;
	}

	public void setReportingCurrency(String reportingCurrency) {
		this.reportingCurrency = reportingCurrency;
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Character getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Character statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "CompanyLatestFilingInfoDTO [periodType=" + periodType + ", period=" + period + ", currency=" + currency
				+ ", reportingCurrency=" + reportingCurrency + ", companyId=" + companyId + "]";
	}

}
