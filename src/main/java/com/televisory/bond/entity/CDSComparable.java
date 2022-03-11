package com.televisory.bond.entity;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cds_data_latest")
public class CDSComparable {

	@Id
	@Column(name = "cds_identifier")
	private String identifier;
	
	@Column(name = "business_date_time")
	private Date businessDateTime;
	
	@Column(name = "par_spread_mid")
	private Double parSpreadMid;
	
	@Column(name = "quote_spread_mid")
	private Double quoteSpreadMid;
	
	@Column(name = "up_front_mid")
	private Double upFrontMid;
	
	@Column(name = "tenor")
	private Double tenor;
	
	@Column(name = "region")
	private String region;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "restructuring_type")
	private String restructuringType;
	
	@Column(name = "seniority")
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
		return "CDSComparable [identifier=" + identifier + ", businessDateTime=" + businessDateTime + ", parSpreadMid="
				+ parSpreadMid + ", quoteSpreadMid=" + quoteSpreadMid + ", upFrontMid=" + upFrontMid + ", tenor="
				+ tenor + ", region=" + region + ", currency=" + currency + ", entityName=" + entityName
				+ ", restructuringType=" + restructuringType + ", seniority=" + seniority + "]";
	}
	
	
}
