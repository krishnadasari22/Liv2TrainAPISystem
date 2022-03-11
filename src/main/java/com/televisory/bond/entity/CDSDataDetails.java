package com.televisory.bond.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CDSDataDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "currency")
	private String currency;

	@Column(name = "tenor")
	private Double tenor;

	@Column(name = "restructuring_type")
	private String restructuringType;

	@Column(name = "seniority")
	private String seniority;

	@Column(name = "business_date_time")
	private Date businessDateTime;

	public CDSDataDetails() {
		super();
	}

	public CDSDataDetails(String entityName, String currency, Double tenor, String restructuringType, String seniority,
			Date businessDateTime) {
		super();
		this.entityName = entityName;
		this.currency = currency;
		this.tenor = tenor;
		this.restructuringType = restructuringType;
		this.seniority = seniority;
		this.businessDateTime = businessDateTime;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getTenor() {
		return tenor;
	}

	public void setTenor(Double tenor) {
		this.tenor = tenor;
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

	public Date getBusinessDateTime() {
		return businessDateTime;
	}

	public void setBusinessDateTime(Date businessDateTime) {
		this.businessDateTime = businessDateTime;
	}

	@Override
	public String toString() {
		return "CDSDataDetails [entityName=" + entityName + ", currency=" + currency + ", tenor=" + tenor
				+ ", restructuringType=" + restructuringType + ", seniority=" + seniority + ", businessDateTime="
				+ businessDateTime + "]";
	}

}
