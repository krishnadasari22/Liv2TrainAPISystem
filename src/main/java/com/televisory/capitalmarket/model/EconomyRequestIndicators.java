package com.televisory.capitalmarket.model;

public class EconomyRequestIndicators {
	
	private String indicatorName;
	private String periodicity;
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public String getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	
	@Override
	public String toString() {
		return "EconomyRequestIndicators [indicatorId=" + indicatorName + ", periodicity=" + periodicity + "]";
	}
}
