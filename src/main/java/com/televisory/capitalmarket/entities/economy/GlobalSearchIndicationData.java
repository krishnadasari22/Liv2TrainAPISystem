package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "indicator_data_latest")
public class GlobalSearchIndicationData {
	
	
	@Column(name = "indicator_data_id")
	private Integer indicatorDataId;
	
	@Column(name = "country")
	private String countryName;
	
	@Column(name = "country_iso_code_3")
	private String countryIsoCode3;
	
	@Column(name = "category_group")
	private String categoryGroup;
	
	@Column(name = "category")
	private String category;
	
	@Column(name="display_order")
	private String displayOrder;
	
	@Column(name = "tel_category")
	private String telCategory;
	
	@Column(name = "tel_category_group")
	private String telCategoryGroup;
	
	@Column(name = "tel_category_parent_id")
	private Integer telCategeoryParentId;
	
	@Id
	@Column(name = "tel_indicator_id")
	private Integer telIndicatorId;
	
	@Column(name = "yoy_change_flag")
	private Integer yoyChangeFlag;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "latest_value_date")
	private Date period;
	
	@Column(name = "latest_value")
	private Double data;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "yoy_change")
	private Double yoyChange;
	
	@Column(name = "frequency")
	private String periodType;
	
	@Column(name = "historical_data_symbol")
	private String historicalDataSymbol;
	
	@Column(name = "show_default")
	private Integer showDefault;
	
	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	public Integer getIndicatorDataId() {
		return indicatorDataId;
	}

	public void setIndicatorDataId(Integer indicatorDataId) {
		this.indicatorDataId = indicatorDataId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCategoryGroup() {
		return categoryGroup;
	}

	public void setCategoryGroup(String categoryGroup) {
		this.categoryGroup = categoryGroup;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTelCategory() {
		return telCategory;
	}

	public void setTelCategory(String telCategory) {
		this.telCategory = telCategory;
	}
	
	public Integer getTelIndicatorId() {
		return telIndicatorId;
	}

	public void setTelIndicatorId(Integer telIndicatorId) {
		this.telIndicatorId = telIndicatorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
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

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getYoyChange() {
		return yoyChange;
	}

	public void setYoyChange(Double yoyChange) {
		this.yoyChange = yoyChange;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getHistoricalDataSymbol() {
		return historicalDataSymbol;
	}

	public void setHistoricalDataSymbol(String historicalDataSymbol) {
		this.historicalDataSymbol = historicalDataSymbol;
	}
	
	public Integer getShowDefault() {
		return showDefault;
	}

	public void setShowDefault(Integer showDefault) {
		this.showDefault = showDefault;
	}
	
	public Integer getYoyChangeFlag() {
		return yoyChangeFlag;
	}

	public void setYoyChangeFlag(Integer yoyChangeFlag) {
		this.yoyChangeFlag = yoyChangeFlag;
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

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	public String getTelCategoryGroup() {
		return telCategoryGroup;
	}

	public void setTelCategoryGroup(String telCategoryGroup) {
		this.telCategoryGroup = telCategoryGroup;
	}

	public Integer getTelCategeoryParentId() {
		return telCategeoryParentId;
	}

	public void setTelCategeoryParentId(Integer telCategeoryParentId) {
		this.telCategeoryParentId = telCategeoryParentId;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getCountryIsoCode3() {
		return countryIsoCode3;
	}

	public void setCountryIsoCode3(String countryIsoCode3) {
		this.countryIsoCode3 = countryIsoCode3;
	}

	@Override
	public String toString() {
		return "GlobalSearchIndicationData [indicatorDataId=" + indicatorDataId
				+ ", countryName=" + countryName + ", countryIsoCode3="
				+ countryIsoCode3 + ", categoryGroup=" + categoryGroup
				+ ", category=" + category + ", displayOrder=" + displayOrder
				+ ", telCategory=" + telCategory + ", telCategoryGroup="
				+ telCategoryGroup + ", telCategeoryParentId="
				+ telCategeoryParentId + ", telIndicatorId=" + telIndicatorId
				+ ", yoyChangeFlag=" + yoyChangeFlag + ", title=" + title
				+ ", period=" + period + ", data=" + data + ", unit=" + unit
				+ ", yoyChange=" + yoyChange + ", periodType=" + periodType
				+ ", historicalDataSymbol=" + historicalDataSymbol
				+ ", showDefault=" + showDefault + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}
}
