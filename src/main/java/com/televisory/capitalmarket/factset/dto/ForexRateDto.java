package com.televisory.capitalmarket.factset.dto;

public class ForexRateDto {

	private double exchRatePerUsd;
	
	private ForexKeyDto forexKey;

	public double getExchRatePerUsd() {
		return exchRatePerUsd;
	}
	
	public void setExchRatePerUsd(double exchRatePerUsd) {
		this.exchRatePerUsd = exchRatePerUsd;
	}
	
	public ForexKeyDto getForexKey() {
		return forexKey;
	}
	
	public void setForexKey(ForexKeyDto forexKey) {
		this.forexKey = forexKey;
	}
	
}
