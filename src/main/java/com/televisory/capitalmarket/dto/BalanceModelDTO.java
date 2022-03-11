package com.televisory.capitalmarket.dto;

import java.io.Serializable;

import javax.persistence.Column;

public class BalanceModelDTO implements Serializable{

	private static final long serialVersionUID = 402174590798741804L;

	private Integer id;

	private String financialType;
	
	private String displayOrder;

	private String displayLevel;

	private String fieldName;

	private String description;
	
	private String shortName;
	
	private String factsetIndustry;
	
	private Integer displayFlag;
	
	private Integer currencyFlag;
	
	private String unit;
	
	private Integer fxAod;
	/********************/
	
	private Integer keyParameter;
	
	private Integer keyParameterOrder;
	
	private String section;
	
	private Integer isActive;
	
	private Integer icFlag;
	
	private Integer screenerFlag;
	
	private Integer watchlistFlag;
	
	//Used to map from ratio Balance model
	private String financialField;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFinancialType() {
		return financialType;
	}

	public void setFinancialType(String financialType) {
		this.financialType = financialType;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDisplayLevel() {
		return displayLevel;
	}

	public void setDisplayLevel(String displayLevel) {
		this.displayLevel = displayLevel;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFactsetIndustry() {
		return factsetIndustry;
	}

	public void setFactsetIndustry(String factsetIndustry) {
		this.factsetIndustry = factsetIndustry;
	}

	public Integer getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(Integer displayFlag) {
		this.displayFlag = displayFlag;
	}

	public Integer getCurrencyFlag() {
		return currencyFlag;
	}

	public void setCurrencyFlag(Integer currencyFlag) {
		this.currencyFlag = currencyFlag;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getFxAod() {
		return fxAod;
	}

	public void setFxAod(Integer fxAod) {
		this.fxAod = fxAod;
	}

	/***********************************/
	

	public Integer getKeyParameter() {
		return keyParameter;
	}

	public void setKeyParameter(Integer keyParameter) {
		this.keyParameter = keyParameter;
	}

	public Integer getKeyParameterOrder() {
		return keyParameterOrder;
	}

	public void setKeyParameterOrder(Integer keyParameterOrder) {
		this.keyParameterOrder = keyParameterOrder;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	public String getFinancialField() {
		return financialField;
	}

	public void setFinancialField(String financialField) {
		this.financialField = financialField;
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

	public Integer getWatchlistFlag() {
		return watchlistFlag;
	}

	public void setWatchlistFlag(Integer watchlistFlag) {
		this.watchlistFlag = watchlistFlag;
	}

	@Override
	public String toString() {
		return "BalanceModelDTO [id=" + id + ", financialType=" + financialType + ", displayOrder=" + displayOrder
				+ ", displayLevel=" + displayLevel + ", fieldName=" + fieldName + ", description=" + description
				+ ", shortName=" + shortName + ", factsetIndustry=" + factsetIndustry + ", displayFlag=" + displayFlag
				+ ", currencyFlag=" + currencyFlag + ", unit=" + unit + ", fxAod=" + fxAod + ", keyParameter="
				+ keyParameter + ", keyParameterOrder=" + keyParameterOrder + ", section=" + section + ", isActive="
				+ isActive + ", icFlag=" + icFlag + ", screenerFlag=" + screenerFlag + ", watchlistFlag="
				+ watchlistFlag + ", financialField=" + financialField + "]";
	}

	

	
	
}
