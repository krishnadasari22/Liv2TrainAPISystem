package com.televisory.capitalmarket.model;

public class ReportPeerFinancialData {

	private String itemName;
	private String companyName1;
	private String companyName2;
	private String companyName3;
	private String companyName4;
	private Double companyData1;
	private Double companyData2;
	private Double companyData3;
	private Double companyData4;
	private String unit;
	private String currency;
	
	
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getCompanyData1() {
		return companyData1;
	}
	public void setCompanyData1(Double companyData1) {
		this.companyData1 = companyData1;
	}
	public Double getCompanyData2() {
		return companyData2;
	}
	public void setCompanyData2(Double companyData2) {
		this.companyData2 = companyData2;
	}
	public Double getCompanyData3() {
		return companyData3;
	}
	public void setCompanyData3(Double companyData3) {
		this.companyData3 = companyData3;
	}
	public String getCompanyName1() {
		return companyName1;
	}
	public void setCompanyName1(String companyName1) {
		this.companyName1 = companyName1;
	}
	public String getCompanyName2() {
		return companyName2;
	}
	public void setCompanyName2(String companyName2) {
		this.companyName2 = companyName2;
	}
	public String getCompanyName3() {
		return companyName3;
	}
	public void setCompanyName3(String companyName3) {
		this.companyName3 = companyName3;
	}
		
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getCompanyName4() {
		return companyName4;
	}
	public void setCompanyName4(String companyName4) {
		this.companyName4 = companyName4;
	}
	public Double getCompanyData4() {
		return companyData4;
	}
	public void setCompanyData4(Double companyData4) {
		this.companyData4 = companyData4;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		return "ReportPeerFinancialData [itemName=" + itemName + ", companyName1=" + companyName1 + ", companyName2="
				+ companyName2 + ", companyName3=" + companyName3 + ", companyName4=" + companyName4 + ", companyData1="
				+ companyData1 + ", companyData2=" + companyData2 + ", companyData3=" + companyData3 + ", companyData4="
				+ companyData4 + ", unit=" + unit + ", currency=" + currency + "]";
	}
	
	
	
}
