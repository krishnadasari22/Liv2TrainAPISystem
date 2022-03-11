package com.televisory.capitalmarket.dto.economy;

import java.io.Serializable;

import org.dozer.Mapping;

public class CurrencyMappingDTO implements Serializable {
	

	private static final long serialVersionUID = -4611359426220388735L;

	@Mapping(value="id")
	private int currencyMappingId;
	
	@Mapping(value="country.id")
	private int countryId;
	
	@Mapping(value="country.countryName")
	private String countryName;
	
	private String currencyCode;
	
	@Mapping(value="currency.id")
	private int currencyId;

	public int getCurrencyMappingId() {
		return currencyMappingId;
	}

	public void setCurrencyMappingId(int currencyMappingId) {
		this.currencyMappingId = currencyMappingId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public String toString() {
		return "CurrencyMappingDTO [currencyMappingId=" + currencyMappingId + ", countryId=" + countryId
				+ ", countryName=" + countryName + ", currencyCode=" + currencyCode + ", currencyId=" + currencyId
				+ "]";
	}
}
