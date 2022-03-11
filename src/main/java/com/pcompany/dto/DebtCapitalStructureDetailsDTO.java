package com.pcompany.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebtCapitalStructureDetailsDTO implements Serializable {

	private static final long serialVersionUID = 998044300839560179L;
	
	private Integer id;
	
	private String entityId;
	
	private String instrumentType;
	
	private String instrumentDescription;
	
	private String issueDate;
	
	private String maturityDate;
	
	private String maturityYear;
	
	private String currency;
	
	private String issueCurrency;
	
	private String couponType;
	
	private String couponIndex;
	
	private Double couponRateMin;

	private Double couponRateMax;
	
	private String couponSpread;
	
	private String assetDesc;
	
	private String collateralType;
	
	private Double issueAmount;
	
	private Double outstandingAmount;
	
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
		if(maturityDate!=null && maturityDate!=""){
			int len = maturityDate.split("-").length;
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat format2 = new SimpleDateFormat("MMM-yyyy");
			DateFormat format3 = new SimpleDateFormat("yyyy-MM");
			DateFormat format4 = new SimpleDateFormat("dd-MMM-yyyy");
			//System.out.println(" maturityDate "+maturityDate);
			//System.out.println(" len "+len);
			if(len==3){
				Date mDate = null;
				try {
					mDate = format.parse(maturityDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String period = format4.format(mDate);
				this.maturityDate = period;
			}else if(len==2){
				Date mDate = null;
				try {
					mDate = format3.parse(maturityDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String period = format2.format(mDate);
				this.maturityDate = period;
			}else{
				this.maturityDate = maturityDate;
			}
		}else{
			this.maturityDate = maturityDate;
		}
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
		return "DebtCapitalStructureDetailsDTO [id=" + id + ", entityId=" + entityId + ", instrumentType="
				+ instrumentType + ", instrumentDescription=" + instrumentDescription + ", issueDate=" + issueDate
				+ ", maturityDate=" + maturityDate + ", maturityYear=" + maturityYear + ", currency=" + currency
				+ ", issueCurrency=" + issueCurrency + ", couponType=" + couponType + ", couponIndex=" + couponIndex
				+ ", couponRateMin=" + couponRateMin + ", couponRateMax=" + couponRateMax + ", couponSpread="
				+ couponSpread + ", assetDesc=" + assetDesc + ", collateralType=" + collateralType + ", issueAmount="
				+ issueAmount + ", outstandingAmount=" + outstandingAmount + ", availAmount=" + availAmount + "]";
	}
}
