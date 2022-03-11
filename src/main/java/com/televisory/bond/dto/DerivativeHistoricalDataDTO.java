package com.televisory.bond.dto;

import java.util.Date;

public class DerivativeHistoricalDataDTO {
	
	private Date period;
	
	private String identifier;
	
	private String fieldName;
	
	private Double data;

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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
		return "DerivativeHistoricalData [period=" + period + ", identifier=" + identifier + ", fieldName=" + fieldName
				+ ", data=" + data + "]";
	}
}
