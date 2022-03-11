package com.televisory.capitalmarket.dto.economy;

import java.util.Date;

import javax.persistence.Column;

import org.dozer.Mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author vinay
 *
 */
public class IndicatorLatestDataDTO {

	private Integer indicatorDataId;
	private String countryName;
	private String countryIsoCode3;
	private Integer countryId;
	private String categoryGroup;
	private String category;
	private String telCategory;
	private String telCategoryGroup;
	private Integer telCategeoryParentId;
	private Integer telIndicatorId;
	private String title;
	private Date period;
	private Double data;
	private String displayOrder;
	private String unit;
	private Double yoyChange;
	private Integer showDefault;
	private String periodType;
	private Integer yoyChangeFlag;
	private Double forecastQ1;
	private Double forecastQ2;
	private Double forecastQ3;
	private Double forecastQ4;
	private Double forecastY1;
	private Double forecastY2;
	private Double forecastY3;
	
	@JsonIgnore
	private String historicalDataSymbol;
	
	@JsonIgnore
	private Integer isActive;
	
	@JsonIgnore
	private Date createdAt;
	
	@JsonIgnore
	private String createdBy;
	
	@JsonIgnore
	private Date lastModifiedAt;
	
	@JsonIgnore
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

	public Integer getShowDefault() {
		return showDefault;
	}

	public void setShowDefault(Integer showDefault) {
		this.showDefault = showDefault;
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

	public String getCountryIsoCode3() {
		return countryIsoCode3;
	}

	public void setCountryIsoCode3(String countryIsoCode3) {
		this.countryIsoCode3 = countryIsoCode3;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public Integer getYoyChangeFlag() {
		return yoyChangeFlag;
	}

	public void setYoyChangeFlag(Integer yoyChangeFlag) {
		this.yoyChangeFlag = yoyChangeFlag;
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
		return "IndicatorLatestDataDTO [indicatorDataId=" + indicatorDataId + ", countryName=" + countryName
				+ ", countryIsoCode3=" + countryIsoCode3 + ", countryId=" + countryId + ", categoryGroup="
				+ categoryGroup + ", category=" + category + ", telCategory=" + telCategory + ", telCategoryGroup="
				+ telCategoryGroup + ", telCategeoryParentId=" + telCategeoryParentId + ", telIndicatorId="
				+ telIndicatorId + ", title=" + title + ", period=" + period + ", data=" + data + ", displayOrder="
				+ displayOrder + ", unit=" + unit + ", yoyChange=" + yoyChange + ", showDefault=" + showDefault
				+ ", periodType=" + periodType + ", yoyChangeFlag=" + yoyChangeFlag + ", forecastQ1=" + forecastQ1
				+ ", forecastQ2=" + forecastQ2 + ", forecastQ3=" + forecastQ3 + ", forecastQ4=" + forecastQ4
				+ ", forecastY1=" + forecastY1 + ", forecastY2=" + forecastY2 + ", forecastY3=" + forecastY3
				+ ", historicalDataSymbol=" + historicalDataSymbol + ", isActive=" + isActive + ", createdAt="
				+ createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}
}
