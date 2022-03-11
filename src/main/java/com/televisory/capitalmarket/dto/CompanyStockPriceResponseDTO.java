package com.televisory.capitalmarket.dto;

import java.util.Date;

public class CompanyStockPriceResponseDTO {

	private String companyId;
	private Date date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private String currency;
	private String volume;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	@Override
	public String toString() {
		return "CompanyStockPriceResponseDTO [companyId=" + companyId + ", date=" + date + ", open=" + open + ", high="
				+ high + ", low=" + low + ", close=" + close + ", currency=" + currency + ", volume=" + volume + "]";
	}
}
