package com.televisory.bond.dto;

import java.util.Date;

import org.dozer.Mapping;

public class FIHistoricalDTO {
	
	private String isinNumber;
	
	private String description;
	
	private String ticker;
	
	@Mapping("asOfDate")
	private Date period;
	
	private String fieldName;
	
	private Double data;

	public String getIsinNumber() {
		return isinNumber;
	}

	public void setIsinNumber(String isinNumber) {
		this.isinNumber = isinNumber;
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

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "FIHistoricalDTO [isinNumber=" + isinNumber + ", description=" + description + ", ticker=" + ticker
				+ ", period=" + period + ", fieldName=" + fieldName + ", data=" + data + "]";
	}
}
