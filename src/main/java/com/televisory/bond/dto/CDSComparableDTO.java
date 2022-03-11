package com.televisory.bond.dto;


import java.sql.Date;

import javax.persistence.Column;

public class CDSComparableDTO {

	private String identifier;
	
	private Date businessDateTime;
	
	private Double parSpreadMid;
	
	private Double quoteSpreadMid;
	
	private Double upFrontMid;
	
	private Double tenor;
	
	private String region;
	
	private String currency;
	
	private String entityName;

	private String restructuringType;
	
	private String seniority;
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getBusinessDateTime() {
		return businessDateTime;
	}

	public void setBusinessDateTime(Date businessDateTime) {
		this.businessDateTime = businessDateTime;
	}

	public Double getParSpreadMid() {
		return parSpreadMid;
	}

	public void setParSpreadMid(Double parSpreadMid) {
		this.parSpreadMid = parSpreadMid;
	}

	public Double getQuoteSpreadMid() {
		return quoteSpreadMid;
	}

	public void setQuoteSpreadMid(Double quoteSpreadMid) {
		this.quoteSpreadMid = quoteSpreadMid;
	}

	public Double getUpFrontMid() {
		return upFrontMid;
	}

	public void setUpFrontMid(Double upFrontMid) {
		this.upFrontMid = upFrontMid;
	}

	public Double getTenor() {
		return tenor;
	}

	public void setTenor(Double tenor) {
		this.tenor = tenor;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getRestructuringType() {
		return restructuringType;
	}

	public void setRestructuringType(String restructuringType) {
		this.restructuringType = restructuringType;
	}

	public String getSeniority() {
		return seniority;
	}

	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}

	@Override
	public String toString() {
		return "CDSComparableDTO [identifier=" + identifier + ", businessDateTime=" + businessDateTime
				+ ", parSpreadMid=" + parSpreadMid + ", quoteSpreadMid=" + quoteSpreadMid + ", upFrontMid=" + upFrontMid
				+ ", tenor=" + tenor + ", region=" + region + ", currency=" + currency + ", entityName=" + entityName
				+ ", restructuringType=" + restructuringType + ", seniority=" + seniority + "]";
	}
	
}
