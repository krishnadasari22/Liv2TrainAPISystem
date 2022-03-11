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

/**
 * 
 * @author vinay
 *
 */
@Entity
@Table(name = "fx")
public class EconomyFX {
	
	private int id;
	private static final String indicatorName = CMStatic.ECONOMY_FX;
	private String unit;
	private String currency;
	private CurrencyList currencyObject;
	private String periodType;
	private Date period;
	private CountryList country;
	private Double data;
	private int isActive;
	private Date createdAt;
	private String createdBy;
	private Date lastModifiedAt;	
	private String lastModifiedBy;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "fx_id", nullable = false, unique = true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="unit")
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name="period_type")
	public String getPeriodType() {
		return periodType;
	}
	
	@Column(name="currency")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@OneToOne
	@JoinColumn(name="currency_id")
	public CurrencyList getCurrencyObject() {
		return currencyObject;
	}
	public void setCurrencyObject(CurrencyList currencyObject) {
		this.currencyObject = currencyObject;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	
	@OneToOne
	@JoinColumn(name="country_id")
	public CountryList getCountry() {
		return country;
	}
	public void setCountry(CountryList country) {
		this.country = country;
	}
	
	@Column(name="is_active")
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	@Column(name="period")
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	
	@Column(name="data")
	public Double getData() {
		return data;
	}
	public void setData(Double data) {
		this.data = data;
	}
	
	@Column(name="created_at")
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Column(name="created_by")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="last_modified_at")
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
	
	@Column(name="last_modified_by")
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
