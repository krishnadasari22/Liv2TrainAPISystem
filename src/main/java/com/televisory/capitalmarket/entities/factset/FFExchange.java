package com.televisory.capitalmarket.entities.factset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ref_v2_fref_sec_exchange_map")
public class FFExchange {

	@Id
	@Column(name="fref_exchange_code")
	private String exchangeCode;
	
	@Column(name="fref_exchange_desc")
	private String exchangeDescription;
	
	@Column(name="fref_exchange_location_code")
	private String exchangeLocation;

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getExchangeDescription() {
		return exchangeDescription;
	}

	public void setExchangeDescription(String exchangeDescription) {
		this.exchangeDescription = exchangeDescription;
	}

	public String getExchangeLocation() {
		return exchangeLocation;
	}

	public void setExchangeLocation(String exchangeLocation) {
		this.exchangeLocation = exchangeLocation;
	}

}
