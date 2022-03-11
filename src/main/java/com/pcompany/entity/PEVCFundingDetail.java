package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ma_v1_ma_deal_info")
public class PEVCFundingDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "factset_entity_id")
	private String entityId;
	
	@Column(name = "entity_proper_name")
	private String companyName;
	
	@Column(name = "country_iso_code_3")
	private String countryCode;
	
	@Column(name = "country_name")
	private String countryName;
	
	@Column(name="tics_industry_code")
	private String industryCode;
	
	@Column(name="tics_industry_name")
	private String industryName;
	
	@Column(name="category_name_desc")
	private String latestRound;
	
	@Column(name="inception_date")
	private Date latestRoundDate;
	
	@Column(name="portco_fin_type")
	private String financingType;
	
	@Column(name="valuation")
	private Double totalFundingAmount;
	
	@Column(name="valuation_cal")
	private Double fxTotalFundingAmount;

	@Column(name = "target_currency")
	private String currency;

	@Column(name = "unit")
	private String unit;
	
	@Column(name = "count")
	private long count;
	
	@Column(name = "company_id")
	private String companyId;
	
	@Column(name = "entity_type")
	private String entityType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public String getLatestRound() {
		return latestRound;
	}

	public void setLatestRound(String latestRound) {
		this.latestRound = latestRound;
	}

	public Date getLatestRoundDate() {
		return latestRoundDate;
	}

	public void setLatestRoundDate(Date latestRoundDate) {
		this.latestRoundDate = latestRoundDate;
	}

	public String getFinancingType() {
		return financingType;
	}

	public void setFinancingType(String financingType) {
		this.financingType = financingType;
	}

	public Double getTotalFundingAmount() {
		return totalFundingAmount;
	}

	public void setTotalFundingAmount(Double totalFundingAmount) {
		this.totalFundingAmount = totalFundingAmount;
	}

	public Double getFxTotalFundingAmount() {
		return fxTotalFundingAmount;
	}

	public void setFxTotalFundingAmount(Double fxTotalFundingAmount) {
		this.fxTotalFundingAmount = fxTotalFundingAmount;
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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	
	
}
