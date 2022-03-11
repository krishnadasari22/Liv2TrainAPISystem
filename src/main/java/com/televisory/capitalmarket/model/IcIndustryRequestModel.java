package com.televisory.capitalmarket.model;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author vinay
 *
 */
public class IcIndustryRequestModel {

	private List<Integer> countryId;
	private List<String> ticsIndustryCodes;
	private List<String> fieldNames;
	private String periodicity;
	private String currency;
	private Date startDate;
	private Date endDate;
	private String type;

	public List<Integer> getCountryId() {
		return countryId;
	}

	public void setCountryId(List<Integer> countryId) {
		this.countryId = countryId;
	}

	public List<String> getTicsIndustryCodes() {
		return ticsIndustryCodes;
	}

	public void setTicsIndustryCodes(List<String> ticsIndustryCodes) {
		this.ticsIndustryCodes = ticsIndustryCodes;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "IcIndustryRequestModel [countryId=" + countryId
				+ ", ticsIndustryCodes=" + ticsIndustryCodes + ", fieldNames="
				+ fieldNames + ", periodicity=" + periodicity + ", currency="
				+ currency + ", startDate=" + startDate + ", endDate="
				+ endDate + ", type=" + type + "]";
	}
}
