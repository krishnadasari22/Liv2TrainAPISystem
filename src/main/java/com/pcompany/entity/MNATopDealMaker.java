package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ma_v1_ma_deal_info")
public class MNATopDealMaker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rank")
	private Integer rank;
	
	@Column(name="country_code")
	private String countryCode;
	
	@Column(name="country")
	private String country;
	
	@Column(name="industry_code")
	private String industryCode;
	
	@Column(name="industry_name")
	private String industryName;
	
	@Column(name="sector_code")
	private String sectorCode;
	
	@Column(name="sector_name")
	private String sectorName;

	@Column(name = "name")
	private String name;

	@Column(name = "company_id")
	private String companyId;
	
	@Column(name = "factset_entity_id")
	private String entityId;

	@Column(name = "total_deals")
	private Integer totalDeals;

	@Column(name = "transact_tot_value")
	private Double totalValue;

	@Column(name = "transact_avg_value")
	private Double avgValue;

	@Column(name = "transact_max_value")
	private Double maxValue;

	@Column(name = "deal_currency")
	private String dealCurrency;

	@Column(name = "currency")
	private String currency;

	@Column(name = "unit")
	private String unit;

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MNATopDealMaker other = (MNATopDealMaker) obj;
		if (rank == null) {
			if (other.rank != null)
				return false;
		} else if (!rank.equals(other.rank))
			return false;
		return true;
	}
	
	

}
