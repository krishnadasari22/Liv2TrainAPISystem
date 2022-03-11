package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dcs_v2_dcs_details")
public class DebtCapitalStructureDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="factset_entity_id")
	private String entityId;
	
	@Column(name = "instrument_type")
	private String instrumentType;
	
	@Column(name = "instrument_description")
	private String instrumentDescription;
	
	@Column(name = "issue_date")
	private String issueDate;
	
	@Column(name = "maturity_date")
	private String maturityDate;
	
	@Column(name = "maturity_year")
	private String maturityYear;
	
	@Column(name = "coupon_type")
	private String couponType;
	
	@Column(name = "coupon_index")
	private String couponIndex;
	
	@Column(name = "min_coupon_rate")
	private Double couponRateMin;
	
	@Column(name = "max_coupon_rate")
	private Double couponRateMax;
	
	@Column(name = "coupon_spread")
	private String couponSpread;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "issuance_currency")
	private String issueCurrency;
	
	@Column(name = "asset_desc")
	private String assetDesc;
	
	@Column(name = "collateral_type")
	private String collateralType;
	
	@Column(name = "issue_amount")
	private Double issueAmount;
	
	@Column(name = "os_amount")
	private Double outstandingAmount;
	
	@Column(name = "avail_amount")
	private Double availAmount;

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

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getInstrumentDescription() {
		return instrumentDescription;
	}

	public void setInstrumentDescription(String instrumentDescription) {
		this.instrumentDescription = instrumentDescription;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponIndex() {
		return couponIndex;
	}

	public void setCouponIndex(String couponIndex) {
		this.couponIndex = couponIndex;
	}

	public Double getCouponRateMin() {
		return couponRateMin;
	}

	public void setCouponRateMin(Double couponRateMin) {
		this.couponRateMin = couponRateMin;
	}

	public Double getCouponRateMax() {
		return couponRateMax;
	}

	public void setCouponRateMax(Double couponRateMax) {
		this.couponRateMax = couponRateMax;
	}

	public String getCouponSpread() {
		return couponSpread;
	}

	public void setCouponSpread(String couponSpread) {
		this.couponSpread = couponSpread;
	}

	public Double getIssueAmount() {
		return issueAmount;
	}

	public void setIssueAmount(Double issueAmount) {
		this.issueAmount = issueAmount;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	
	public String getAssetDesc() {
		return assetDesc;
	}

	public void setAssetDesc(String assetDesc) {
		this.assetDesc = assetDesc;
	}

	public String getCollateralType() {
		return collateralType;
	}

	public void setCollateralType(String collateralType) {
		this.collateralType = collateralType;
	}

	public Double getAvailAmount() {
		return availAmount;
	}

	public void setAvailAmount(Double availAmount) {
		this.availAmount = availAmount;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIssueCurrency() {
		return issueCurrency;
	}

	public void setIssueCurrency(String issueCurrency) {
		this.issueCurrency = issueCurrency;
	}
	
	public String getMaturityYear() {
		return maturityYear;
	}

	public void setMaturityYear(String maturityYear) {
		this.maturityYear = maturityYear;
	}

	@Override
	public String toString() {
		return "DebtCapitalStructureDetails [id=" + id + ", entityId=" + entityId + ", instrumentType=" + instrumentType
				+ ", instrumentDescription=" + instrumentDescription + ", issueDate=" + issueDate + ", maturityDate="
				+ maturityDate + ", maturityYear=" + maturityYear + ", couponType=" + couponType + ", couponIndex="
				+ couponIndex + ", couponRateMin=" + couponRateMin + ", couponRateMax=" + couponRateMax
				+ ", couponSpread=" + couponSpread + ", currency=" + currency + ", issueCurrency=" + issueCurrency
				+ ", assetDesc=" + assetDesc + ", collateralType=" + collateralType + ", issueAmount=" + issueAmount
				+ ", outstandingAmount=" + outstandingAmount + ", availAmount=" + availAmount + "]";
	}
}
