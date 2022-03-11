package com.televisory.capitalmarket.model;

import java.util.List;

public class PeriodicityEconomyRequest <T> {
	public List<T> indicatorList;
	public List<T> countryList;
	public List<T> getIndicatorList() {
		return indicatorList;
	}
	public void setIndicatorList(List<T> indicatorList) {
		this.indicatorList = indicatorList;
	}
	public List<T> getCountryList() {
		return countryList;
	}
	public void setCountryList(List<T> countryList) {
		this.countryList = countryList;
	}
	@Override
	public String toString() {
		return "PeriodicityEconomyRequest [indicatorList=" + indicatorList + ", countryList=" + countryList + "]";
	}
	
	
}
