package com.televisory.capitalmarket.dto;

import java.io.Serializable;

public class CurrencyListDTO implements Serializable {
	
	private static final long serialVersionUID = -7178929596776363322L;
	private Integer id;

	private String isoCode;

	private String currencyName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Override
	public String toString() {
		return "CurrencyListDTO [id=" + id + ", isoCode=" + isoCode + ", currencyName=" + currencyName + "]";
	}
}
