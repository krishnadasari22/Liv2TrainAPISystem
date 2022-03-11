package com.televisory.capitalmarket.model;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author vinay
 *
 */
public class IcStockRequestModel {

	private String fieldType;
	
	private List<String> fieldName;
	
	private String periodicity;
	
	private String currency;
	
	private Date startDate;
	
	private Date endDate;
	
	private List<String> companyList;

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public List<String> getFieldName() {
		return fieldName;
	}

	public void setFieldName(List<String> fieldName) {
		this.fieldName = fieldName;
	}

	public List<String> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<String> companyList) {
		this.companyList = companyList;
	}
	
	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "IcStockRequestModel [fieldType=" + fieldType + ", fieldName=" + fieldName + ", periodicity="
				+ periodicity + ", currency=" + currency + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", companyList=" + companyList + "]";
	}
}
