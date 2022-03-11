package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author hitesh
 *	In this entity "percentChange" is extra from the FFStockPrice entity
 */

@Entity
@Table(name = "fp_v2_fp_basic_prices")
public class FFStockPriceLatest {
	
	@Id
	@Column(name="id")
	private Integer id;

	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name="p_date")
	private Date date;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="p_price")
	private Double close;
	
	@Column(name="p_price_open")
	private Double open;
	
	@Column(name="p_price_high")
	private Double high;
	
	@Column(name="p_price_low")
	private Double low;
	
	@Column(name="p_volume")
	private Double volume;
	
	@Column(name="percent_change")
	private Double percentChange;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
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

	@Override
	public String toString() {
		return "FFStockPriceLatest [id=" + id + ", companyId=" + companyId + ", date=" + date + ", currency=" + currency
				+ ", close=" + close + ", open=" + open + ", high=" + high + ", low=" + low + ", volume=" + volume
				+ ", percentChange=" + percentChange + "]";
	}
	
}
