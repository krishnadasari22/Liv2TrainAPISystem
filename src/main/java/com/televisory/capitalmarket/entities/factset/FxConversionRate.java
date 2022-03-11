package com.televisory.capitalmarket.entities.factset;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FxConversionRate implements Serializable {
	
	private static final long serialVersionUID = 1030671457664888535L;
	private String period;
	private String sourceCurrency;
	private String destinationCurrency;
	private Double rate;
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getSourceCurrency() {
		return sourceCurrency;
	}
	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}
	public String getDestinationCurrency() {
		return destinationCurrency;
	}
	public void setDestinationCurrency(String destinationCurrency) {
		this.destinationCurrency = destinationCurrency;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	
	

}
