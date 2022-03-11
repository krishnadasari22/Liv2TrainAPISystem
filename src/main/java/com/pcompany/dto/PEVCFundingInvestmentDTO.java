package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class PEVCFundingInvestmentDTO implements Serializable {

	private static final long serialVersionUID = 6054108707018212985L;
	
    private String id;
	private String entityId;
	private String companyName;	
	private String securityName;	
	private String round;	
	private String issueType;
	private String financingType;
	private Date roundDate;	
	private String investmentCurrency;
	private Double investmentAmount; 
	private Double fxInvestmentAmount; 
	private String currency;
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
