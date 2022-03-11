package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CompanyFinancialDTO implements Serializable{

	private static final long serialVersionUID = -6071814171657968910L;

	private Integer id;

	private String periodType;

	private Date period;
	
	private Date applicablePeriod;
	
	//@Mapping("depthLevel")
	private String depthLevel;
	
	@JsonIgnore
	@Mapping("mandatory")
	private Integer mandatory;
	
	private String displayOrder;
	
	private String financialType;
	
	private String fieldName;
	
	private String section;
	
	private String shortName;
	
	private String itemName;

	private String displayName;
	
	private Double data;
	
	@Mapping("unit")
	private String unit;
	
	private String currency;

	/*private Object companyId;*/
	@Mapping("company.id")
	private String companyId;
	
	@Mapping("company.name")
	private String companyName;

	@Mapping("company.country.countryId")
	private String countryId;
	
	@Mapping("company.country.countryName")
	private String countryName;

	@JsonIgnore
	private Integer isActive;

	@JsonIgnore
	private Date createdAt;

	@JsonIgnore
	private String creadtedBy;

	@JsonIgnore
	private Date lastmodifiedAt;

	@JsonIgnore
	private String lastModifiedBy;
	
	private Integer keyParameter;

	private Integer keyParameterOrder;
	
	private Integer icFlag;
	
	//@JsonIgnore
	private Integer screenerFlag;
	
	@JsonIgnore
	private Integer watchListFlag;

	public Integer getKeyParameter() {
		return keyParameter;
	}

	public void setKeyParameter(Integer keyParameter) {
		this.keyParameter = keyParameter;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public Date getApplicablePeriod() {
		return applicablePeriod;
	}

	public void setApplicablePeriod(Date applicablePeriod) {
		this.applicablePeriod = applicablePeriod;
	}
	
	public String getDepthLevel() {
		return depthLevel;
	}

	public void setDepthLevel(String depthLevel) {	
		if(depthLevel.contains("L")){
			depthLevel=depthLevel.substring(1);
		}
		this.depthLevel = depthLevel;
	}

	public Integer getMandatory() {
		return mandatory;
	}

	public void setMandatory(Integer mandatory) {
		this.mandatory = mandatory;
	}
	
	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public String getFinancialType() {
		return financialType;
	}

	public void setFinancialType(String financialType) {
		this.financialType = financialType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getItemName() {
		return itemName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public String getUnit() {
		return unit;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreadtedBy() {
		return creadtedBy;
	}

	public void setCreadtedBy(String creadtedBy) {
		this.creadtedBy = creadtedBy;
	}

	public Date getLastmodifiedAt() {
		return lastmodifiedAt;
	}

	public void setLastmodifiedAt(Date lastmodifiedAt) {
		this.lastmodifiedAt = lastmodifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	

	public Integer getKeyParameterOrder() {
		return keyParameterOrder;
	}

	public void setKeyParameterOrder(Integer keyParameterOrder) {
		this.keyParameterOrder = keyParameterOrder;
	}
	
	public Integer getIcFlag() {
		return icFlag;
	}

	public void setIcFlag(Integer icFlag) {
		this.icFlag = icFlag;
	}

	public Integer getScreenerFlag() {
		return screenerFlag;
	}

	public void setScreenerFlag(Integer screenerFlag) {
		this.screenerFlag = screenerFlag;
	}

	public Integer getWatchListFlag() {
		return watchListFlag;
	}

	public void setWatchListFlag(Integer watchListFlag) {
		this.watchListFlag = watchListFlag;
	}

	@Override
	public String toString() {
		return "CompanyFinancialDTO [id=" + id + ", periodType=" + periodType + ", period=" + period
				+ ", applicablePeriod=" + applicablePeriod + ", depthLevel=" + depthLevel + ", mandatory=" + mandatory
				+ ", displayOrder=" + displayOrder + ", financialType=" + financialType + ", fieldName=" + fieldName
				+ ", section=" + section + ", shortName=" + shortName + ", itemName=" + itemName + ", displayName="
				+ displayName + ", data=" + data + ", unit=" + unit + ", currency=" + currency + ", companyId="
				+ companyId + ", companyName=" + companyName + ", countryId=" + countryId + ", countryName="
				+ countryName + ", isActive=" + isActive + ", createdAt=" + createdAt + ", creadtedBy=" + creadtedBy
				+ ", lastmodifiedAt=" + lastmodifiedAt + ", lastModifiedBy=" + lastModifiedBy + ", keyParameter="
				+ keyParameter + ", keyParameterOrder=" + keyParameterOrder + ", icFlag=" + icFlag + ", screenerFlag="
				+ screenerFlag + ", watchListFlag=" + watchListFlag + "]";
	}

		
}
