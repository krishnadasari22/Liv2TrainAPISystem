package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dcs_v2_dcs_maturity_schedule")
public class DebtCapitalMaturity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "debt_maturity_id")
	private Integer debtMaturityId;
	
	@Column(name="factset_entity_id")
	private String entityId;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "report_date")
	private Date reportDate;
	
	@Column(name = "fiscal_year_start")
	private Integer yearStart;
	
	@Column(name = "fiscal_year_end")
	private Integer yearEnd;
	
	@Column(name = "fiscal_year_code")
	private Integer yearCode;
	
	@Column(name = "debt_maturity_amount")
	private Double maturityAmount;
	
	@Column(name = "capital_lease_flag")
	private Integer capitalLeaseFlag;
	
	@Column(name = "interest_flag")
	private Integer interestFlag;
	
	@Column(name = "discount_premium_flag")
	private Integer discountPremiumFlag;
	
	@Column(name = "other_obligations_flag")
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
