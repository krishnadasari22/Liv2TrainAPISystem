package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

public class EconomyFxDTO implements Cloneable,Serializable {

	private static final long serialVersionUID = -8081817908722958182L;
	private int id;
	@Mapping("indicatorName")
	private String indicatorName;
	private String unit;
	@Mapping("currencyObject.currencyName")
	private String currency;
	private String targetCurrencyName;
	private String periodType;
	private Date period;
	@Mapping("country.countryName")
	private String countryName;
	@Mapping("country.id")
	private Integer countryId;
	private Double data;
	private int isActive;
	private Date createdAt;
	private String createdBy;
	private Date lastModifiedAt;
	private String lastModifiedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getTargetCurrencyName() {
		return targetCurrencyName;
	}

	public void setTargetCurrencyName(String targetCurrencyName) {
		this.targetCurrencyName = targetCurrencyName;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
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

	@Override
	public String toString() {
		return "EconomyDTO [id=" + id + ", indicatorName=" + indicatorName + ", unit=" + unit + ", currency=" + currency
				+ ", periodType=" + periodType + ", period=" + period + ", countryName=" + countryName + ", countryId="
				+ countryId + ", data=" + data + ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
