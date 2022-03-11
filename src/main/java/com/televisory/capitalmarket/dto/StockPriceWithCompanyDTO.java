package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import org.dozer.Mapping;

public class StockPriceWithCompanyDTO implements Serializable {
	
	private static final long serialVersionUID = -8367667410111267936L;
	private int id;
	private Date date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private String unit;
	@Mapping("company")
	private CompanyDTO company;
	
	@Mapping("percentageChange")
	private Double percentageChange;

	@Mapping("changeValue")
	private Double changeValue;
	
	@Mapping("percentageMonthlyChange")
	private Double percentageMonthlyChange;

	@Mapping("monthlyChangeValue")
	private Double monthlyChangeValue;
	
	@Mapping("ytd")
	private Double ytd;

	@Mapping("perYtd")
	private Double perYtd;
	
	@Mapping("percentageYearlyChange")
	private Double percentageYearlyChange;

	@Mapping("yearlyChangeValue")
	private Double yearlyChangeValue;
	
	private String currency;
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

	public Double getPercentageChange() {
		return percentageChange;
	}

	public void setPercentageChange(Double percentageChange) {
		this.percentageChange = percentageChange;
	}

	public Double getPercentageMonthlyChange() {
		return percentageMonthlyChange;
	}

	public void setPercentageMonthlyChange(Double percentageMonthlyChange) {
		this.percentageMonthlyChange = percentageMonthlyChange;
	}

	public Double getMonthlyChangeValue() {
		return monthlyChangeValue;
	}

	public void setMonthlyChangeValue(Double monthlyChangeValue) {
		this.monthlyChangeValue = monthlyChangeValue;
	}

	public Double getYtd() {
		return ytd;
	}

	public void setYtd(Double ytd) {
		this.ytd = ytd;
	}

	public Double getPerYtd() {
		return perYtd;
	}

	public void setPerYtd(Double perYtd) {
		this.perYtd = perYtd;
	}

	
	public Double getPercentageYearlyChange() {
		return percentageYearlyChange;
	}

	public void setPercentageYearlyChange(Double percentageYearlyChange) {
		this.percentageYearlyChange = percentageYearlyChange;
	}

	public Double getYearlyChangeValue() {
		return yearlyChangeValue;
	}

	public void setYearlyChangeValue(Double yearlyChangeValue) {
		this.yearlyChangeValue = yearlyChangeValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(Double changeValue) {
		this.changeValue = changeValue;
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

	public CompanyDTO getCompany() {
		return company;
	}

	public void setCompany(CompanyDTO company) {
		this.company = company;
	}
}
