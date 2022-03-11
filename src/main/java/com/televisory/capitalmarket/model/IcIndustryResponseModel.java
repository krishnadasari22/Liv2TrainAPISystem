package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;

/**
 * 
 * @author vinay
 *
 */
public class IcIndustryResponseModel {

	private Integer countryId;
	private String ticsIndustryCode;
	private List<String> fieldNames;
	private List<IndustryFinancialDataDTO> industryData;

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public List<IndustryFinancialDataDTO> getIndustryData() {
		return industryData;
	}

	public void setIndustryData(List<IndustryFinancialDataDTO> industryData) {
		this.industryData = industryData;
	}

	@Override
	public String toString() {
		return "IcIndustryResponse [countryId=" + countryId + ", ticsIndustryCode=" + ticsIndustryCode + ", fieldNames=" + fieldNames
				+ ", industryData=" + industryData + "]";
	}
}
