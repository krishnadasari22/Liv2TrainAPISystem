package com.televisory.capitalmarket.dto.economy;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IndicatorHistoricalDataDTO {

	private Integer indicatorHistId;
	private String country;	
	private String countryIsoCode3;
	private Integer countryId;
	private String category;
	private Date period;
	private Double data;
	private String periodType;
	private String histDataSymbol;
	private Date lastUpdate;
	private String unit;
	
	//this property is used in amcharts in case of forecast data
	private Integer dashLength;
	
	@JsonIgnore
	private int isActive;
	@JsonIgnore
	private Date createdAt;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Date lastModifiedAt;
	@JsonIgnore
	private String lastModifiedBy;

	public Integer getIndicatorHistId() {
		return indicatorHistId;
	}

	public void setIndicatorHistId(Integer indicatorHistId) {
		this.indicatorHistId = indicatorHistId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getPeriod() {
		return period;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getHistDataSymbol() {
		return histDataSymbol;
	}

	public void setHistDataSymbol(String histDataSymbol) {
		this.histDataSymbol = histDataSymbol;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Integer getDashLength() {
		return dashLength;
	}

	public void setDashLength(Integer dashLength) {
		this.dashLength = dashLength;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
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

	@Override
	public String toString() {
		return "IndicatorHistoricalDataDTO [indicatorHistId=" + indicatorHistId + ", country=" + country
				+ ", countryIsoCode3=" + countryIsoCode3 + ", countryId=" + countryId + ", category=" + category
				+ ", period=" + period + ", data=" + data + ", periodType=" + periodType + ", histDataSymbol="
				+ histDataSymbol + ", lastUpdate=" + lastUpdate + ", unit=" + unit + ", dashLength=" + dashLength
				+ ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}

}
