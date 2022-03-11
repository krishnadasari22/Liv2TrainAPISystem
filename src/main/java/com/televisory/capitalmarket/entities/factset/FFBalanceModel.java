package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "balance_model")
public class FFBalanceModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "display_order")
	private String displayOrder;

	@Column(name = "display_level")
	private String displayLevel;

	@Column(name = "type")
	private String financialType;

	@Column(name = "factset_industry")
	private String factsetIndustry;

	@Column(name = "field_name")
	private String fieldName;

	@Column(name = "section")
	private String section;

	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "description")
	private String description;

	@Column(name = "currency_flag")
	private Integer currencyFlag;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "fx_aod")
	private Integer fxAod;
	
	@Column(name = "key_parameter")
	private Integer keyParameter;
	
	@Column(name = "key_parameter_order")
	private Integer keyParameterOrder;
	
	@Column(name = "financial_field")
	private String financialField;
	
	@Column(name = "ic_flag", insertable = false)
	private Integer icFlag;
	
	@Column(name = "screener_flag", insertable = false)
	private Integer screenerFlag;
	
	@Column(name = "watchlist_flag", insertable = false)
	private Integer watchlistFlag;

	@Column(name = "display_flag")
	private Integer displayFlag;
	
	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiesBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFinancialType() {
		return financialType;
	}

	public void setFinancialType(String financialType) {
		this.financialType = financialType;
	}

	public String getFactsetIndustry() {
		return factsetIndustry;
	}

	public void setFactsetIndustry(String factsetIndustry) {
		this.factsetIndustry = factsetIndustry;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(Integer displayFlag) {
		this.displayFlag = displayFlag;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiesBy() {
		return lastModifiesBy;
	}

	public void setLastModifiesBy(String lastModifiesBy) {
		this.lastModifiesBy = lastModifiesBy;
	}

	@Override
	public String toString() {
		return "FFBalanceModel [id=" + id + ", displayOrder=" + displayOrder + ", displayLevel=" + displayLevel
				+ ", financialType=" + financialType + ", factsetIndustry=" + factsetIndustry + ", fieldName="
				+ fieldName + ", section=" + section + ", shortName=" + shortName + ", description=" + description
				+ ", currencyFlag=" + currencyFlag + ", unit=" + unit + ", fxAod=" + fxAod + ", keyParameter="
				+ keyParameter + ", keyParameterOrder=" + keyParameterOrder + ", financialField=" + financialField
				+ ", icFlag=" + icFlag + ", screenerFlag=" + screenerFlag + ", watchlistFlag=" + watchlistFlag
				+ ", displayFlag=" + displayFlag + ", isActive=" + isActive + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiesBy="
				+ lastModifiesBy + "]";
	}
}
