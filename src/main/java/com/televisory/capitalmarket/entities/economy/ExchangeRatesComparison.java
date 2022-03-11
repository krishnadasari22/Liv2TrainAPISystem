package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRatesComparison {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "exchange_rate_id", nullable = false, unique = true)
	private int id;
	
	@Column(name="period_type")
	private String periodType;
	
	@Column(name="period")
	private Date period;
	
	@Column(name="value")
	private Double data;
	
	@Column(name="sourceCurrencyCode")
	private String sourceCurrencyCode;
	
	@Column(name="sourceCurrencyName")
	private String sourceCurrencyName;
	
	@Column(name="targetCurrencyCode")
	private String targetCurrencyCode;

	@Column(name="targetCurrencyName")
	private String targetCurrencyName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
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

	public String getSourceCurrencyCode() {
		return sourceCurrencyCode;
	}

	public void setSourceCurrencyCode(String sourceCurrencyCode) {
		this.sourceCurrencyCode = sourceCurrencyCode;
	}

	public String getSourceCurrencyName() {
		return sourceCurrencyName;
	}

	public void setSourceCurrencyName(String sourceCurrencyName) {
		this.sourceCurrencyName = sourceCurrencyName;
	}

	public String getTargetCurrencyCode() {
		return targetCurrencyCode;
	}

	public void setTargetCurrencyCode(String targetCurrencyCode) {
		this.targetCurrencyCode = targetCurrencyCode;
	}

	public String getTargetCurrencyName() {
		return targetCurrencyName;
	}

	public void setTargetCurrencyName(String targetCurrencyName) {
		this.targetCurrencyName = targetCurrencyName;
	}

	@Override
	public String toString() {
		return "ExchangeRatesComparison [id=" + id + ", periodType=" + periodType + ", period=" + period + ", data="
				+ data + ", sourceCurrencyCode=" + sourceCurrencyCode + ", sourceCurrencyName=" + sourceCurrencyName
				+ ", targetCurrencyCode=" + targetCurrencyCode + ", targetCurrencyName=" + targetCurrencyName + "]";
	}
	
	
}
