package com.televisory.capitalmarket.model;

import java.util.List;

/**
 * 
 * @author varun
 *
 */
public class RatioCompanyData {
	private String companyName;
	private String companyId;
	private String countryId;
	private String countryName;
	private List<String> periods;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public List<String> getPeriods() {
		return periods;
	}
	public void setPeriods(List<String> periods) {
		this.periods = periods;
	}
	@Override
	public String toString() {
		return "RatioCompanyData [companyName=" + companyName + ", companyId=" + companyId + ", countryId=" + countryId
				+ ", countryName=" + countryName + ", periods=" + periods + "]";
	}
}
