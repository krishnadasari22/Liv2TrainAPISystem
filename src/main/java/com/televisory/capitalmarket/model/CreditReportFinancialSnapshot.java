package com.televisory.capitalmarket.model;

public class CreditReportFinancialSnapshot {

	String metric;
	String companyData;
	String companyPreviousPeriodData;
	String companyPercentageChange;
	String industryData;
	String industryPreviousPeriodData;
	String industryPercentageChange;

	public CreditReportFinancialSnapshot(String metric,	String companyData,	String companyPreviousPeriodData,String companyPercentageChange,String industryData,String industryPreviousPeriodData,String industryPercentageChange) {
		this.metric = metric;	
		this.companyData = companyData;
		this.companyPreviousPeriodData = companyPreviousPeriodData;
		this.companyPercentageChange = companyPercentageChange;
		this.industryData = industryData;
		this.industryPreviousPeriodData = industryPreviousPeriodData;
		this.industryPercentageChange = industryPercentageChange;
	}

	public String getMetric() {
		return metric;
	}



	public void setMetric(String metric) {
		this.metric = metric;
	}



	public String getCompanyData() {
		return companyData;
	}



	public void setCompanyData(String companyData) {
		this.companyData = companyData;
	}



	public String getCompanyPreviousPeriodData() {
		return companyPreviousPeriodData;
	}



	public void setCompanyPreviousPeriodData(String companyPreviousPeriodData) {
		this.companyPreviousPeriodData = companyPreviousPeriodData;
	}



	public String getCompanyPercentageChange() {
		return companyPercentageChange;
	}



	public void setCompanyPercentageChange(String companyPercentageChange) {
		this.companyPercentageChange = companyPercentageChange;
	}



	public String getIndustryData() {
		return industryData;
	}



	public void setIndustryData(String industryData) {
		this.industryData = industryData;
	}



	public String getIndustryPreviousPeriodData() {
		return industryPreviousPeriodData;
	}



	public void setIndustryPreviousPeriodData(String industryPreviousPeriodData) {
		this.industryPreviousPeriodData = industryPreviousPeriodData;
	}



	public String getIndustryPercentageChange() {
		return industryPercentageChange;
	}



	public void setIndustryPercentageChange(String industryPercentageChange) {
		this.industryPercentageChange = industryPercentageChange;
	}



	@Override
	public String toString() {
		return "CreditReportFinancialSnapshot [metric=" + metric
				+ ", companyData=" + companyData
				+ ", companyPreviousPeriodData=" + companyPreviousPeriodData
				+ ", companyPercentageChange=" + companyPercentageChange
				+ ", industryData=" + industryData
				+ ", industryPreviousPeriodData=" + industryPreviousPeriodData
				+ ", industryPercentageChange=" + industryPercentageChange
				+ "]";
	}


	


}
