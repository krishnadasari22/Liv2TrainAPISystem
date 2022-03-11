package com.televisory.capitalmarket.factset.dto;

import org.dozer.Mapping;

public class FactSetExchangeDTO {
	
	@Mapping("exchangeDescription")
	private String name;
	
	@Mapping("exchangeCode")
	private String shortName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
