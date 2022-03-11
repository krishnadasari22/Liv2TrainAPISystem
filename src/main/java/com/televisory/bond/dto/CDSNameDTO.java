package com.televisory.bond.dto;

import org.dozer.Mapping;

public class CDSNameDTO {

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

	private String identifier;
	
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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return "CDSNameDTO [entityName=" + entityName + ", currency=" + currency + ", tenor=" + tenor + ", seniority="
				+ seniority + ", restructuringType=" + restructuringType + ", identifier=" + identifier + "]";
	}

}
