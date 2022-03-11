package com.televisory.bond.dto;

import java.util.Date;

import org.dozer.Mapping;

public class FINameDTO {
	
	String isin_number;
	private String description;
	String ticker;
	Date maturity_date;
	
	@Mapping("iso_currency_code")
	private String currency;
	
	@Mapping("industry_lvl_1_desc")
	private String categeory;

	public String getIsin_number() {
		return isin_number;
	}

	public void setIsin_number(String isin_number) {
		this.isin_number = isin_number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public Date getMaturity_date() {
		return maturity_date;
	}

	public void setMaturity_date(Date maturity_date) {
		this.maturity_date = maturity_date;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCategeory() {
		return categeory;
	}

	public void setCategeory(String categeory) {
		this.categeory = categeory;
	}

	@Override
	public String toString() {
		return "FINameDTO [isin_number=" + isin_number + ", description=" + description + ", ticker=" + ticker
				+ ", maturity_date=" + maturity_date + ", currency=" + currency + ", categeory=" + categeory + "]";
	}

}
