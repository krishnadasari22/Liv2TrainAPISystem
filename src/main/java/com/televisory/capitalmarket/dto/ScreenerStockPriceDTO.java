package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

public class ScreenerStockPriceDTO <T> implements Serializable{

	private static final long serialVersionUID = 3596375349101451405L;
	private T id;
	private Date period;
	private Date priceDate;
	private Double open;
	private Double high;
	private Double low;
	private Double data;
	private String unit;
	private Double volume;
	private Double percentChange;
	private String currency;
	private Date createdAt;
	private String createdBy;
	private Date lastModifiedAt;
	private String lastModifiedBy;
	private String companyId;
	private String companyName;
	private String countryId;
	private String countryName;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
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

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getPercentChange() {
		return percentChange;
	}

	public void setPercentChange(Double percentChange) {
		this.percentChange = percentChange;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Date getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	@Override
	public String toString() {
		return "ScreenerStockPriceDTO [id=" + id + ", period=" + period + ", priceDate=" + priceDate + ", open=" + open
				+ ", high=" + high + ", low=" + low + ", data=" + data + ", unit=" + unit + ", volume=" + volume
				+ ", percentChange=" + percentChange + ", currency=" + currency + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + ", companyId=" + companyId + ", companyName=" + companyName + ", countryId="
				+ countryId + ", countryName=" + countryName + "]";
	}

	
	
	

}
