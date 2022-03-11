package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

public class BenchmarkCompanyNewDTO implements Serializable{
	private static final long serialVersionUID = -7359224336597264487L;

	private Integer id;
	
	private String periodType;
	
	private Date date;

	private String entityId;
	
	private String companyId;
	
	private String companyName;
	
	private String currency;

	private String fieldName;

	private Double data;

	private String shortName;
	
	private String itemName;
	
	private String unit;
	
	private int dataFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(int dataFlag) {
		this.dataFlag = dataFlag;
	}

	@Override
	public String toString() {
		return "BenchmarkCompanyNewDTO [id=" + id + ", periodType=" + periodType + ", date=" + date + ", entityId="
				+ entityId + ", companyId=" + companyId + ", companyName=" + companyName + ", currency=" + currency
				+ ", fieldName=" + fieldName + ", data=" + data + ", shortName=" + shortName + ", itemName=" + itemName
				+ ", unit=" + unit + ", dataFlag=" + dataFlag + "]";
	}

	

	


}
