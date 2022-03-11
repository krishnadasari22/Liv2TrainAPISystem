package com.televisory.capitalmarket.entities.cm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "industry_balance_model")
public class IndustryBalanceModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "financial_type")
	private String financialType;

	@Column(name = "display_order")
	private String displayOrder;

	@Column(name = "display_level")
	private String displayLevel;

	@Column(name = "field_name")
	private String fieldName;

	@Column(name = "description")
	private String description;
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "factset_industry")
	private String factsetIndustry;
	
	@Column(name = "display_flag")
	private Integer displayFlag;
	
	@Column(name = "watchlist_flag")
	private Integer watchlistFlag;
	
	@Column(name = "currency_flag")
	private Integer currencyFlag;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "ic_flag", insertable = false)
	private Integer icFlag;
	
	@Column(name = "screener_flag", insertable = false)
	private Integer screenerFlag;
	
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
	
	public Integer getIcFlag() {
		return icFlag;
	}

	public void setIcFlag(Integer icFlag) {
		this.icFlag = icFlag;
	}

	public Integer getWatchlistFlag() {
		return watchlistFlag;
	}

	public void setWatchlistFlag(Integer watchlistFlag) {
		this.watchlistFlag = watchlistFlag;
	}

	public Integer getScreenerFlag() {
		return screenerFlag;
	}

	public void setScreenerFlag(Integer screenerFlag) {
		this.screenerFlag = screenerFlag;
	}

	@Override
	public String toString() {
		return "IndustryBalanceModel [id=" + id + ", financialType=" + financialType + ", displayOrder=" + displayOrder
				+ ", displayLevel=" + displayLevel + ", fieldName=" + fieldName + ", description=" + description
				+ ", shortName=" + shortName + ", factsetIndustry=" + factsetIndustry + ", displayFlag=" + displayFlag
				+ ", watchlistFlag=" + watchlistFlag + ", currencyFlag=" + currencyFlag + ", unit=" + unit + ", icFlag="
				+ icFlag + ", screenerFlag=" + screenerFlag + "]";
	}

	
	
}
