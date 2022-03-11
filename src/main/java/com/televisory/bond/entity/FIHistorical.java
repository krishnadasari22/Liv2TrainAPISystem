package com.televisory.bond.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fi_data")
public class FIHistorical {
	
	
	@Column(name = "isin_number")
	private String isinNumber;
	
	@Column(name = "description	")
	private String description;
	
	@Column(name = "ticker	")
	private String ticker;
	
	@Id
	@Column(name = "as_of_date	")
	private Date asOfDate;
	
	@Column(name = "field_name",insertable=false,updatable=false)
	private String fieldName;
	
	@Column(name = "data",insertable=false,updatable=false)
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
	public Date getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
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
		return "FIHistorical [isinNumber=" + isinNumber + ", description=" + description + ", ticker=" + ticker
				+ ", asOfDate=" + asOfDate + ", fieldName=" + fieldName + ", data=" + data + "]";
	}
}
