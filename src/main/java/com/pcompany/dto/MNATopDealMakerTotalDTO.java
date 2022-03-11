package com.pcompany.dto;

import java.io.Serializable;

public class MNATopDealMakerTotalDTO implements Serializable{
	
	private static final long serialVersionUID = -4666006862111351931L;

	private Integer totalRows;

	private Integer totalDeals;

	private Double totalValue;

	private Double avgValue;

	private Double maxValue;

	private String dealCurrency;

	private String currency;

	private String unit;

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getTotalDeals() {
		return totalDeals;
	}

	public void setTotalDeals(Integer totalDeals) {
		this.totalDeals = totalDeals;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public String getDealCurrency() {
		return dealCurrency;
	}

	public void setDealCurrency(String dealCurrency) {
		this.dealCurrency = dealCurrency;
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
		return "MNATopDealMakerTotalDTO [totalRows=" + totalRows
				+ ", totalDeals=" + totalDeals + ", totalValue=" + totalValue
				+ ", avgValue=" + avgValue + ", maxValue=" + maxValue + "]";
	}

}
