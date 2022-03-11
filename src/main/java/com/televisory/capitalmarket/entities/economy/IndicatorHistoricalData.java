package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "indicator_data_historical")
public class IndicatorHistoricalData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "indicator_datahis_id", nullable = false, unique = true)
	private Integer indicatorHistId;

	@Column(name = "country")
	private String country;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "country_iso_code_3")
	private String countryIsoCode3;
	
	@Column(name = "country_id")
	private Integer countryId;
	
	/*@Column(name = "tel_category")
	private String telCategory;*/
	
	@Column(name = "datetime")
	private Date period;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "close")
	private Double data;
	
	@Column(name = "frequency")
	private String periodType;
	
	@Column(name = "historical_data_symbol")
	private String histDataSymbol;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "is_active")
	private int isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
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
	
	

	/*public String getTelCategory() {
		return telCategory;
	}

	public void setTelCategory(String telCategory) {
		this.telCategory = telCategory;
	}*/
	
	

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
		return "IndicatorHistoricalData [indicatorHistId=" + indicatorHistId + ", country=" + country + ", category="
				+ category + ", countryIsoCode3=" + countryIsoCode3 + ", countryId=" + countryId + ", period=" + period
				+ ", unit=" + unit + ", data=" + data + ", periodType=" + periodType + ", histDataSymbol="
				+ histDataSymbol + ", lastUpdate=" + lastUpdate + ", isActive=" + isActive + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}

}
