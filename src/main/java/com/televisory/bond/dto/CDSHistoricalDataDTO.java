package com.televisory.bond.dto;

import java.util.Date;

import org.dozer.Mapping;

public class CDSHistoricalDataDTO {
	
	@Mapping("cdsDataDetails.entityName")
	private String entityName;
	
	@Mapping("cdsDataDetails.currency")
	private String currency;
	
	
	@Mapping("cdsDataDetails.tenor")
	private Double tenor;
	
	@Mapping("cdsDataDetails.seniority")
	private String seniority;

	@Mapping("cdsDataDetails.restructuringType")
	private String restructuringType;
	
	@Mapping("cdsDataDetails.businessDateTime")
	private Date period;
	
	private String fieldName;
	
	private Double data;

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

	public String getSeniority() {
		return seniority;
	}

	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}

	public String getRestructuringType() {
		return restructuringType;
	}

	public void setRestructuringType(String restructuringType) {
		this.restructuringType = restructuringType;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CDSHistoricalDataDTO [entityName=" + entityName + ", currency=" + currency + ", tenor=" + tenor
				+ ", seniority=" + seniority + ", restructuringType=" + restructuringType + ", period=" + period
				+ ", fieldName=" + fieldName + ", data=" + data + "]";
	}
}
