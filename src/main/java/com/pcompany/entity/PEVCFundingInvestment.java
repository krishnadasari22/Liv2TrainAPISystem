package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pe_v1_pe_securities")
public class PEVCFundingInvestment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "factset_portco_entity_id")
	private String entityId;
	
	@Column(name = "entity_proper_name")
	private String companyName;	
	
	@Column(name = "security_name")
	private String securityName;	
	
	@Column(name = "category_name_desc")
	private String round;	
	
	@Column(name = "issue_type")
	private String issueType;
	
	@Column(name = "portco_fin_type")
	private String financingType;
	
	@Column(name = "inception_date")
	private Date roundDate;	
	
	@Column(name = "iso_currency")
	private String investmentCurrency;
	
	@Column(name = "valuation")
	private Double investmentAmount; 
	
	@Column(name = "valuation_cal")
	private Double fxInvestmentAmount; 
	
	@Column(name = "target_currency")
	private String currency;
	
	@Column(name = "unit")
	private String unit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getFinancingType() {
		return financingType;
	}

	public void setFinancingType(String financingType) {
		this.financingType = financingType;
	}

	public Date getRoundDate() {
		return roundDate;
	}

	public void setRoundDate(Date roundDate) {
		this.roundDate = roundDate;
	}

	public String getInvestmentCurrency() {
		return investmentCurrency;
	}

	public void setInvestmentCurrency(String investmentCurrency) {
		this.investmentCurrency = investmentCurrency;
	}

	public Double getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(Double investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	public Double getFxInvestmentAmount() {
		return fxInvestmentAmount;
	}

	public void setFxInvestmentAmount(Double fxInvestmentAmount) {
		this.fxInvestmentAmount = fxInvestmentAmount;
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

}
