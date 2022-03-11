package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

public class IndustryFinancialDataDTO implements Serializable {

	private static final long serialVersionUID = -4290287017439732582L;

	private Integer id;

	@Mapping("ticsSector.sectorId")
	private int sectorId;

	@Mapping("ticsSector.ticsSectorCode")
	private String ticsSectorCode;

	@Mapping("ticsSector.ticsSectorName")
	private String ticsSectorName;

	@Mapping("company.id")
	private String companyCode;

	@Mapping("company.name")
	private String companyName;

	@Mapping("ticsIndustry.ticsIndustryCode")
	private String ticsIndustryCode;

	@Mapping("ticsIndustry.ticsIndustryName")
	private String ticsIndustryName;

	private String displayOrder;

	private String depthLevel;

	private String fieldName;

	private String shortName;

	private String itemName;

	private String displayName;

	private Date period;

	private Date applicablePeriod;

	private String displayFlag;

	private Double data;

	private String currency;

	private String unit;

	private String periodType;

	private Integer companyCount;
	
	private String finType;

	@Mapping("domicileCountry.id")
	private Integer countryId;

	@Mapping("domicileCountry.countryIsoCode3")
	private String countryCode;

	@Mapping("domicileCountry.countryName")
	private String countryName;

	private Integer noDataFlag;

	private Integer companyActiveFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSectorId() {
		return sectorId;
	}

	public void setSectorId(int sectorId) {
		this.sectorId = sectorId;
	}

	public String getTicsSectorCode() {
		return ticsSectorCode;
	}

	public void setTicsSectorCode(String ticsSectorCode) {
		this.ticsSectorCode = ticsSectorCode;
	}

	public String getTicsSectorName() {
		return ticsSectorName;
	}

	public void setTicsSectorName(String ticsSectorName) {
		this.ticsSectorName = ticsSectorName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDepthLevel() {
		return depthLevel;
	}

	public void setDepthLevel(String depthLevel) {
		this.depthLevel = depthLevel;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Integer getCompanyCount() {
		return companyCount;
	}

	public void setCompanyCount(Integer companyCount) {
		this.companyCount = companyCount;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getNoDataFlag() {
		return noDataFlag;
	}

	public void setNoDataFlag(Integer noDataFlag) {
		this.noDataFlag = noDataFlag;
	}

	public Integer getCompanyActiveFlag() {
		return companyActiveFlag;
	}

	public void setCompanyActiveFlag(Integer companyActiveFlag) {
		this.companyActiveFlag = companyActiveFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public String getFinType() {
		return finType;
	}

	public void setFinType(String finType) {
		this.finType = finType;
	}

	@Override
	public String toString() {
		return "IndustryFinancialDataDTO [id=" + id + ", sectorId=" + sectorId
				+ ", ticsSectorCode=" + ticsSectorCode + ", ticsSectorName="
				+ ticsSectorName + ", companyCode=" + companyCode
				+ ", companyName=" + companyName + ", ticsIndustryCode="
				+ ticsIndustryCode + ", ticsIndustryName=" + ticsIndustryName
				+ ", displayOrder=" + displayOrder + ", depthLevel="
				+ depthLevel + ", fieldName=" + fieldName + ", shortName="
				+ shortName + ", itemName=" + itemName + ", displayName="
				+ displayName + ", period=" + period + ", applicablePeriod="
				+ applicablePeriod + ", displayFlag=" + displayFlag + ", data="
				+ data + ", currency=" + currency + ", unit=" + unit
				+ ", periodType=" + periodType + ", companyCount="
				+ companyCount + ", countryId=" + countryId + ", countryCode="
				+ countryCode + ", countryName=" + countryName
				+ ", noDataFlag=" + noDataFlag + ", companyActiveFlag="
				+ companyActiveFlag + "]";
	}

}
