package com.televisory.capitalmarket.dto;

import java.io.Serializable;

import org.dozer.Mapping;

public class GlobalSearchIndustryDTO implements Cloneable,Serializable{
	private static final long serialVersionUID = -8081817908722958182L;
	
	private Integer globalSearchIndustryId;
	private Integer industryId;
	private String ticsIndustryName;
	private String ticsIndustryCode;
	private String ticsIndustryDesc;
	private String factsetIndustry;
	@Mapping("ticsSector.sectorId")
	private Integer sectorId;
	@Mapping("ticsSector.ticsSectorCode")
	private String ticsSectorCode;
	@Mapping("ticsSector.ticsSectorName")
	private String ticsSectorName;
	@Mapping("ticsSector.ticsSectorDesc")
	private String ticsSectorDesc;
	private String domicileCountryCode;
	private String countryName;
	private Integer countryId;

	public Integer getGlobalSearchIndustryId() {
		return globalSearchIndustryId;
	}

	public void setGlobalSearchIndustryId(Integer globalSearchIndustryId) {
		this.globalSearchIndustryId = globalSearchIndustryId;
	}

	public Integer getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}

	public String getTicsIndustryName() {
		return ticsIndustryName;
	}

	public void setTicsIndustryName(String ticsIndustryName) {
		this.ticsIndustryName = ticsIndustryName;
	}

	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public String getTicsIndustryDesc() {
		return ticsIndustryDesc;
	}

	public void setTicsIndustryDesc(String ticsIndustryDesc) {
		this.ticsIndustryDesc = ticsIndustryDesc;
	}

	public String getFactsetIndustry() {
		return factsetIndustry;
	}

	public void setFactsetIndustry(String factsetIndustry) {
		this.factsetIndustry = factsetIndustry;
	}

	public Integer getSectorId() {
		return sectorId;
	}

	public void setSectorId(Integer sectorId) {
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

	public String getTicsSectorDesc() {
		return ticsSectorDesc;
	}

	public void setTicsSectorDesc(String ticsSectorDesc) {
		this.ticsSectorDesc = ticsSectorDesc;
	}

	public String getDomicileCountryCode() {
		return domicileCountryCode;
	}

	public void setDomicileCountryCode(String domicileCountryCode) {
		this.domicileCountryCode = domicileCountryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "GlobalSearchIndustryDTO [globalSearchIndustryId="
				+ globalSearchIndustryId + ", industryId=" + industryId
				+ ", ticsIndustryName=" + ticsIndustryName
				+ ", ticsIndustryCode=" + ticsIndustryCode
				+ ", ticsIndustryDesc=" + ticsIndustryDesc
				+ ", factsetIndustry=" + factsetIndustry + ", sectorId="
				+ sectorId + ", ticsSectorCode=" + ticsSectorCode
				+ ", ticsSectorName=" + ticsSectorName + ", ticsSectorDesc="
				+ ticsSectorDesc + ", domicileCountryCode="
				+ domicileCountryCode + ", countryName=" + countryName
				+ ", countryId=" + countryId + "]";
	}
}
