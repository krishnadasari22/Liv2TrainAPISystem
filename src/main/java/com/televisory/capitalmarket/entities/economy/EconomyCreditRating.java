package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.televisory.capitalmarket.util.CMStatic;


@Entity
@Table(name="credit_rating")
public class EconomyCreditRating {

	private static final String indicatorName = CMStatic.ECONOMY_CREDIT_RATING;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "credit_rating_id", nullable = false, unique = true)
	private int id;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "period_type")
	private String periodType;
	
	@Column(name = "period")
	private Date period;
	
	@OneToOne
	@JoinColumn(name = "country_id")
	private CountryList country;
	
	@Column(name = "data")
	private String data;
	
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

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	 
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	
	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public CountryList getCountry() {
		return country;
	}

	public void setCountry(CountryList country) {
		this.country = country;
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

	
	public String getData() {
		return data;
	}

	public void setData(String data) {
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

	public static String getIndicatorname() {
		return indicatorName;
	}
}
