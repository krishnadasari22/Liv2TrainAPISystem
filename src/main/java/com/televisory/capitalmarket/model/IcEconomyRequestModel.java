package com.televisory.capitalmarket.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class IcEconomyRequestModel {

	private String fieldType;
	
	private List<String> countryList;
	
	private List<String> currencyList;
	
	private List<EconomyRequestIndicators> indicators;
	
	private Date startDate;
	
	private Date endDate;
	
	private String targetCurrency;
	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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

	public List<String> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<String> countryList) {
		this.countryList = countryList;
	}
	
	public List<EconomyRequestIndicators> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<EconomyRequestIndicators> indicators) {
		this.indicators = indicators;
	}
	
	public List<String> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<String> currencyList) {
		this.currencyList = currencyList;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	@Override
	public String toString() {
		return "IcEconomyRequestModel [fieldType=" + fieldType + ", countryList=" + countryList + ", currencyList="
				+ currencyList + ", indicators=" + indicators + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", targetCurrency=" + targetCurrency + "]";
	}
}
