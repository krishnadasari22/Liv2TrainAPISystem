package com.televisory.capitalmarket.dto;

import java.io.Serializable;

import org.dozer.Mapping;

public class CountryIndicatorDTO implements Serializable {
	
	private static final long serialVersionUID = 3027290930139354501L;

	@Mapping("id")
	private int countryIndicatorId;

	@Mapping("indicatorsName.id")
	private int indicatorId;
	
	
	@Mapping("indicatorsName.indicatorsName")
	private String name;
	
	@Mapping("indicatorsName.shortName")
	private String shortName;
	
	private String periodType;
	@Mapping("country.id")
	private int countryId;
	@Mapping("country.countryName")
	private String countryName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public int getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}
	
	public int getCountryIndicatorId() {
		return countryIndicatorId;
	}
	public void setCountryIndicatorId(int countryIndicatorId) {
		this.countryIndicatorId = countryIndicatorId;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Override
	public String toString() {
		return "CountryIndicatorDTO [countryIndicatorId=" + countryIndicatorId + ", indicatorId=" + indicatorId
				+ ", name=" + name + ", shortName=" + shortName + ", periodType=" + periodType + ", countryId="
				+ countryId + ", countryName=" + countryName + "]";
	}
}
