package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "indicator_data_latest")
public class IndicatorLatestData {
	
	
	@Column(name = "indicator_data_id")
	private Integer indicatorDataId;
	
	@Column(name = "country")
	private String countryName;
	
	/*@Column(name = "countryiso_code_3")
	private String countryIsoCode3;
	
	@Column(name = "country_id")
	private Integer countryId;*/
	
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
	
	@Column(name = "forecast_value_1q")
	private Double forecastQ1;
	
	@Column(name = "forecast_value_2q")
	private Double forecastQ2;
	
	@Column(name = "forecast_value_3q")
	private Double forecastQ3;
	
	@Column(name = "forecast_value_4q")
	private Double forecastQ4;
	
	@Column(name = "year_end")
	private Double forecastY1;
	
	@Column(name = "year_end2")
	private Double forecastY2;
	
	@Column(name = "year_end3")
	private Double forecastY3;
	

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
	
	public Double getForecastQ1() {
		return forecastQ1;
	}

	public void setForecastQ1(Double forecastQ1) {
		this.forecastQ1 = forecastQ1;
	}

	public Double getForecastQ2() {
		return forecastQ2;
	}

	public void setForecastQ2(Double forecastQ2) {
		this.forecastQ2 = forecastQ2;
	}

	public Double getForecastQ3() {
		return forecastQ3;
	}

	public void setForecastQ3(Double forecastQ3) {
		this.forecastQ3 = forecastQ3;
	}

	public Double getForecastQ4() {
		return forecastQ4;
	}

	public void setForecastQ4(Double forecastQ4) {
		this.forecastQ4 = forecastQ4;
	}

	public Double getForecastY1() {
		return forecastY1;
	}

	public void setForecastY1(Double forecastY1) {
		this.forecastY1 = forecastY1;
	}

	public Double getForecastY2() {
		return forecastY2;
	}

	public void setForecastY2(Double forecastY2) {
		this.forecastY2 = forecastY2;
	}

	public Double getForecastY3() {
		return forecastY3;
	}

	public void setForecastY3(Double forecastY3) {
		this.forecastY3 = forecastY3;
	}

	@Override
	public String toString() {
		return "IndicatorLatestData [indicatorDataId=" + indicatorDataId + ", countryName=" + countryName
				+ ", categoryGroup=" + categoryGroup + ", category=" + category + ", displayOrder=" + displayOrder
				+ ", telCategory=" + telCategory + ", telCategoryGroup=" + telCategoryGroup + ", telCategeoryParentId="
				+ telCategeoryParentId + ", telIndicatorId=" + telIndicatorId + ", yoyChangeFlag=" + yoyChangeFlag
				+ ", title=" + title + ", period=" + period + ", data=" + data + ", unit=" + unit + ", yoyChange="
				+ yoyChange + ", periodType=" + periodType + ", historicalDataSymbol=" + historicalDataSymbol
				+ ", showDefault=" + showDefault + ", forecastQ1=" + forecastQ1 + ", forecastQ2=" + forecastQ2
				+ ", forecastQ3=" + forecastQ3 + ", forecastQ4=" + forecastQ4 + ", forecastY1=" + forecastY1
				+ ", forecastY2=" + forecastY2 + ", forecastY3=" + forecastY3 + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiedBy=" + lastModifiedBy + "]";
	}

	
}
