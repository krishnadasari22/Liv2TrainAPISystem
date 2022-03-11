package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PEVCFundingDTO implements Serializable {

	private static final long serialVersionUID = -3570600745437163027L;
	
    private Integer id;
	private String entityId;
	private String companyName;
	private String countryCode;
	private String countryName;
	private String industryCode;
	private String industryName;
	private String latestRound;
	private Date latestRoundDate;
	private String financingType;
	private Double totalFundingAmount;
	private Double fxTotalFundingAmount;
	private String currency;
	private String unit;
	private long count;
	private String companyId;
	private String entityType;
	
	List<PEVCFundingInvestmentDTO> investmentList;

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

	public List<PEVCFundingInvestmentDTO> getInvestmentList() {
		return investmentList;
	}

	public void setInvestmentList(List<PEVCFundingInvestmentDTO> investmentList) {
		this.investmentList = investmentList;
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
