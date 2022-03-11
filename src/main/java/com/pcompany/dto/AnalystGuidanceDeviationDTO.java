package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class AnalystGuidanceDeviationDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String feItem;
	
	private Date closestGuidanceDate;
	
	private Date startDate;
	
	private Date guidanceDate;
	
	private String lowValue;
	
	private String highValue;
	
	private String feItemCode;
	
	private String tableName;
	
	private String actualValue;
	
	private String description;

	private String unit;
	
	private String currency;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFeItem() {
		return feItem;
	}

	public void setFeItem(String feItem) {
		this.feItem = feItem;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getGuidanceDate() {
		return guidanceDate;
	}

	public void setGuidanceDate(Date guidanceDate) {
		this.guidanceDate = guidanceDate;
	}

	public String getLowValue() {
		return lowValue;
	}

	public void setLowValue(String lowValue) {
		this.lowValue = lowValue;
	}

	public String getHighValue() {
		return highValue;
	}

	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}

	public String getFeItemCode() {
		return feItemCode;
	}

	public void setFeItemCode(String feItemCode) {
		this.feItemCode = feItemCode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Date getClosestGuidanceDate() {
		return closestGuidanceDate;
	}

	public void setClosestGuidanceDate(Date closestGuidanceDate) {
		this.closestGuidanceDate = closestGuidanceDate;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "AnalystGuidanceDeviationDTO [id=" + id + ", feItem=" + feItem + ", closestGuidanceDate="
				+ closestGuidanceDate + ", startDate=" + startDate + ", guidanceDate=" + guidanceDate + ", lowValue="
				+ lowValue + ", highValue=" + highValue + ", feItemCode=" + feItemCode + ", tableName=" + tableName
				+ ", actualValue=" + actualValue + ", description=" + description + ", unit=" + unit + ", currency="
				+ currency + "]";
	}

}
