package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "country_indicators")
public class CountryIndicators {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "country_indicators_id", nullable = false, unique = true)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="indicator_name_id",referencedColumnName = "indicator_name_id")
	private IndicatorName indicatorsName;
	
	@ManyToOne
	@JoinColumn(name="country_id")
	private CountryList country;
	
	@Column(name="period_type")
	private String periodType;
	
	@Column(name="tenure")
	private String tenure;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="unit")
	private String unit;
	
	@Column(name = "is_active")
	private int isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_at")
	private Date lastModifiedAt;

	@Column(name = "modified_by")
	private String lastModifiedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IndicatorName getIndicatorsName() {
		return indicatorsName;
	}

	public void setIndicatorsName(IndicatorName indicatorsName) {
		this.indicatorsName = indicatorsName;
	}

	

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
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

	public CountryList getCountry() {
		return country;
	}

	public void setCountry(CountryList country) {
		this.country = country;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "CountryIndicators [id=" + id + ", indicatorsName=" + indicatorsName + ", country=" + country
				+ ", periodType=" + periodType + ", currency=" + currency + ", unit=" + unit + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiedBy=" + lastModifiedBy + "]";
	}

	
}
