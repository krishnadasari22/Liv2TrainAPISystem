package com.pcompany.dto;

import java.io.Serializable;

public class MNATopDealMakerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer rank;
	
    private String countryCode;
	
	private String country;
	
	private String industryCode;
	
	private String industryName;
	
	private String sectorCode;
	
	private String sectorName;

	private String name;

	private String companyId;
	
	private String entityId;

	private Integer totalDeals;

	private Double totalValue;

	private Double avgValue;

	private Double maxValue;

	private String dealCurrency;

	private String currency;

	private String unit;

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Integer getTotalDeals() {
		return totalDeals;
	}

	public void setTotalDeals(Integer totalDeals) {
		this.totalDeals = totalDeals;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public String getDealCurrency() {
		return dealCurrency;
	}

	public void setDealCurrency(String dealCurrency) {
		this.dealCurrency = dealCurrency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "MNATopDealMakerDTO [rank=" + rank + ", name=" + name
				+ ", companyId=" + companyId + ", totalDeals=" + totalDeals
				+ ", totalValue=" + totalValue + ", avgValue=" + avgValue
				+ ", maxValue=" + maxValue + ", dealCurrency=" + dealCurrency
				+ ", currency=" + currency + ", unit=" + unit + "]";
	}
	
	

}
