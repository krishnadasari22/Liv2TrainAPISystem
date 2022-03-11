package com.televisory.capitalmarket.model;


public class ReportIndustryToppersData {
	
	private String categoryName;
	private Integer companyCount;
	private String unit;
	private String currency;
	private Double data;
	private Integer qoqTrend;
	private String itemName;
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getCompanyCount() {
		return companyCount;
	}
	public void setCompanyCount(Integer companyCount) {
		this.companyCount = companyCount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getData() {
		return data;
	}
	public void setData(Double data) {
		this.data = data;
	}
	public Integer getQoqTrend() {
		return qoqTrend;
	}
	public void setQoqTrend(Integer qoqTrend) {
		this.qoqTrend = qoqTrend;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@Override
	public String toString() {
		return "ReportIndustryToppersData [categoryName=" + categoryName + ", companyCount=" + companyCount + ", unit="
				+ unit + ", currency=" + currency + ", data=" + data + ", qoqTrend=" + qoqTrend + ", itemName="
				+ itemName + "]";
	} 
	
	
		
	
	
}
