package com.televisory.capitalmarket.entities.cm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GlobalSearchCompany {

	@Column(name = "id")
	private Integer rowNo;

	
	@Column(name = "company_id")
	private String id;

	@Column(name = "security_id")
	private String securityId;

	@Id
	@Column(name = "factset_entity_id")
	private String factSetEntityId;

	@Column(name = "name")
	private String name;

	@Column(name = "proper_name")
	private String properName;

	@Column(name = "description")
	private String description;

	@Column(name = "company_ticker")
	private String companyTicker;

	@Column(name = "domicile_country_code")
	private String domicileCountryCode;

	@Column(name = "tics_industry_code")
	private String ticsIndustryCode;
	
	@Column(name = "tics_industry_name")
	private String ticsIndustryName;

	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "security_type")
	private String securityType;

	@Column(name = "factset_industry")
	private String ff_industry;
	
	@Column(name = "entity_type")
	private String entityType;
	
	@Column(name = "exchange_code")
	private String exchangeCode;

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getFactSetEntityId() {
		return factSetEntityId;
	}

	public void setFactSetEntityId(String factSetEntityId) {
		this.factSetEntityId = factSetEntityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperName() {
		return properName;
	}

	public void setProperName(String properName) {
		this.properName = properName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompanyTicker() {
		return companyTicker;
	}

	public void setCompanyTicker(String companyTicker) {
		this.companyTicker = companyTicker;
	}

	public String getDomicileCountryCode() {
		return domicileCountryCode;
	}

	public void setDomicileCountryCode(String domicileCountryCode) {
		this.domicileCountryCode = domicileCountryCode;
	}

	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public String getTicsIndustryName() {
		return ticsIndustryName;
	}

	public void setTicsIndustryName(String ticsIndustryName) {
		this.ticsIndustryName = ticsIndustryName;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public String getFf_industry() {
		return ff_industry;
	}

	public void setFf_industry(String ff_industry) {
		this.ff_industry = ff_industry;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	@Override
	public String toString() {
		return "GlobalSearchCompany [rowNo=" + rowNo + ", id=" + id
				+ ", securityId=" + securityId + ", factSetEntityId="
				+ factSetEntityId + ", name=" + name + ", properName="
				+ properName + ", description=" + description
				+ ", companyTicker=" + companyTicker + ", domicileCountryCode="
				+ domicileCountryCode + ", ticsIndustryCode="
				+ ticsIndustryCode + ", ticsIndustryName=" + ticsIndustryName
				+ ", isActive=" + isActive + ", securityType=" + securityType
				+ ", ff_industry=" + ff_industry + ", entityType=" + entityType
				+ ", exchangeCode=" + exchangeCode + "]";
	}

	
}
