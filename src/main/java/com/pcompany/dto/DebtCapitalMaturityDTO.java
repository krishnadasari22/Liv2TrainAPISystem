package com.pcompany.dto;

import java.util.Date;

public class DebtCapitalMaturityDTO {
	
	private Integer debtMaturityId;
	
	private String entityId;
	
	private String currency;
	
	private Date reportDate;
	
	private Integer yearStart;
	
	private Integer yearEnd;
	
	private Integer yearCode;
	
	private Double maturityAmount;
	
	private Integer capitalLeaseFlag;
	
	private Integer interestFlag;
	
	private Integer discountPremiumFlag;
	
	private Integer otherObligationsFlag;

	public Integer getDebtMaturityId() {
		return debtMaturityId;
	}

	public void setDebtMaturityId(Integer debtMaturityId) {
		this.debtMaturityId = debtMaturityId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getYearStart() {
		return yearStart;
	}

	public void setYearStart(Integer yearStart) {
		this.yearStart = yearStart;
	}

	public Integer getYearEnd() {
		return yearEnd;
	}

	public void setYearEnd(Integer yearEnd) {
		this.yearEnd = yearEnd;
	}

	public Integer getYearCode() {
		return yearCode;
	}

	public void setYearCode(Integer yearCode) {
		this.yearCode = yearCode;
	}

	public Double getMaturityAmount() {
		return maturityAmount;
	}

	public void setMaturityAmount(Double maturityAmount) {
		this.maturityAmount = maturityAmount;
	}

	public Integer getCapitalLeaseFlag() {
		return capitalLeaseFlag;
	}

	public void setCapitalLeaseFlag(Integer capitalLeaseFlag) {
		this.capitalLeaseFlag = capitalLeaseFlag;
	}

	public Integer getInterestFlag() {
		return interestFlag;
	}

	public void setInterestFlag(Integer interestFlag) {
		this.interestFlag = interestFlag;
	}

	public Integer getDiscountPremiumFlag() {
		return discountPremiumFlag;
	}

	public void setDiscountPremiumFlag(Integer discountPremiumFlag) {
		this.discountPremiumFlag = discountPremiumFlag;
	}

	public Integer getOtherObligationsFlag() {
		return otherObligationsFlag;
	}

	public void setOtherObligationsFlag(Integer otherObligationsFlag) {
		this.otherObligationsFlag = otherObligationsFlag;
	}

	@Override
	public String toString() {
		return "DebtCapitalMaturity [debtMaturityId=" + debtMaturityId
				+ ", entityId=" + entityId + ", currency=" + currency
				+ ", reportDate=" + reportDate + ", yearStart=" + yearStart
				+ ", yearEnd=" + yearEnd + ", yearCode=" + yearCode
				+ ", maturityAmount=" + maturityAmount + ", capitalLeaseFlag="
				+ capitalLeaseFlag + ", interestFlag=" + interestFlag
				+ ", discountPremiumFlag=" + discountPremiumFlag
				+ ", otherObligationsFlag=" + otherObligationsFlag + "]";
	}
}
