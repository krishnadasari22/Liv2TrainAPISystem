package com.televisory.capitalmarket.model;

public class ReportShareholding {
	
	private String companyName;
	private Double data;
	private String period;
	private String text;
	
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Double getData() {
		return data;
	}
	public void setData(Double data) {
		this.data = data;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "ReportShareholding [companyName=" + companyName + ", data=" + data + ", period=" + period + ", text="
				+ text + "]";
	}
	
	
	
}
