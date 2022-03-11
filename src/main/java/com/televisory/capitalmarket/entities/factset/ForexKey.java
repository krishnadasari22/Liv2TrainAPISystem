package com.televisory.capitalmarket.entities.factset;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class ForexKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "iso_currency")
	private String isoCurrency;
	
	@Column(name = "date")
	private Date date;

	public ForexKey() {
	
	}
	
	public ForexKey(String isoCurrency, Date date) {
		this.isoCurrency = isoCurrency;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getIsoCurrency() {
		return isoCurrency;
	}
	
	public void setIsoCurrency(String isoCurrency) {
		this.isoCurrency = isoCurrency;
	}

}
