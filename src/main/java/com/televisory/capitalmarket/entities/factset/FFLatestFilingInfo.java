package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ff_v3_ff_basic_af")
public class FFLatestFilingInfo {

	@Id
	@Column(name="period_type")
	private String periodType;

	@Column(name="date")
	private Date period;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="ff_curn_doc")
	private String reportingCurrency;
	
	@Column(name="fsym_id")
	private String companyId;
	
	
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

	@Override
	public String toString() {
		return "FFLatestFilingInfo [periodType=" + periodType + ", period=" + period + ", currency=" + currency
				+ ", reportingCurrency=" + reportingCurrency + ", companyId=" + companyId + "]";
	}
}
