package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pe_v1_pe_securities")
public class PEVCInvstmentHeader {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name="entity_proper_name")
	private String entityProperName;
	
	@Column(name="security_name")
	private String securityName;
	
	@Column(name="category_name_desc")
	private String categoryNameDesc;
	
	@Column(name="issue_type")
	private String issueType;
	
	@Column(name="portco_fin_type")
	private String portcoFinType;
	
	@Column(name="inception_date")
	private Date inceptionDate;
	
	@Column(name="iso_currency")
	private String isoCurrency;
	
	@Column(name="valuation")
	private String valuation;
	
	@Column(name="valuation_cal")
	private String valuationCal;
	
	@Column(name="target_currency")
	private String targetCurrency;
	
	@Column(name="investment_currency")
	private String investmentCurrency;
	
	@Column(name="country_iso_code_3")
	private String countryIsoCode3;
	
	@Column(name="tics_industry_name")
	private String ticsIndustryName;
	
	@Column(name="country_name")
	private String countryName;
	
	@Column(name="tics_industry_code")
	private String ticsIndustryCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntityProperName() {
		return entityProperName;
	}

	public void setEntityProperName(String entityProperName) {
		this.entityProperName = entityProperName;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public String getCategoryNameDesc() {
		return categoryNameDesc;
	}

	public void setCategoryNameDesc(String categoryNameDesc) {
		this.categoryNameDesc = categoryNameDesc;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getPortcoFinType() {
		return portcoFinType;
	}

	public void setPortcoFinType(String portcoFinType) {
		this.portcoFinType = portcoFinType;
	}

	public Date getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Date inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

	public String getIsoCurrency() {
		return isoCurrency;
	}

	public void setIsoCurrency(String isoCurrency) {
		this.isoCurrency = isoCurrency;
	}

	public String getValuation() {
		return valuation;
	}

	public void setValuation(String valuation) {
		this.valuation = valuation;
	}

	public String getValuationCal() {
		return valuationCal;
	}

	public void setValuationCal(String valuationCal) {
		this.valuationCal = valuationCal;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public String getInvestmentCurrency() {
		return investmentCurrency;
	}

	public void setInvestmentCurrency(String investmentCurrency) {
		this.investmentCurrency = investmentCurrency;
	}
   
	public String getCountryIsoCode3() {
		return countryIsoCode3;
	}

	public void setCountryIsoCode3(String countryIsoCode3) {
		this.countryIsoCode3 = countryIsoCode3;
	}

	public String getTicsIndustryName() {
		return ticsIndustryName;
	}

	public void setTicsIndustryName(String ticsIndustryName) {
		this.ticsIndustryName = ticsIndustryName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}
	
	
	
}
