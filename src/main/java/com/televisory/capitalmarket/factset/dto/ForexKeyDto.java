package com.televisory.capitalmarket.factset.dto;

import java.util.Date;

public class ForexKeyDto {

	private String isoCurrency;
	private Date date;
	
	public String getIsoCurrency() {
		return isoCurrency;
	}
	
	public void setIsoCurrency(String isoCurrency) {
		this.isoCurrency = isoCurrency;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

}
