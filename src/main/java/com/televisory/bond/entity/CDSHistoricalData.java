package com.televisory.bond.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="cds_data")
public class CDSHistoricalData {
	
	@EmbeddedId
	CDSDataDetails cdsDataDetails;
	
	
	@Column(name = "field_name",insertable=false,updatable=false)
	private String fieldName;
	
	@Column(name = "data",insertable=false,updatable=false)
	private Double data;

	public CDSDataDetails getCdsDataDetails() {
		return cdsDataDetails;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setCdsDataDetails(CDSDataDetails cdsDataDetails) {
		this.cdsDataDetails = cdsDataDetails;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CDSHistoricalData [cdsDataDetails=" + cdsDataDetails + ", fieldName=" + fieldName + ", data=" + data
				+ "]";
	}
}
