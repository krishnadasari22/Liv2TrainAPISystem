package com.televisory.bond.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="etd_data")
public class DerivativeHistoricalData {
	
	@Id
	@Column(name = "period")
	private Date period;
	
	@Column(name = "symbol")
	private String identifier;
	
	@Column(name = "field_name",insertable=false,updatable=false)
	private String fieldName;
	
	@Column(name = "data",insertable=false,updatable=false)
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
