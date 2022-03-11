package com.televisory.bond.dto;

import java.util.Date;

import javax.persistence.Column;

public class CDSBalanceModelDTO {

	private Integer id;
	private String  fieldName;
	private String description;
	private String csvFieldName;
	private String unit;
	private Integer currencyFlag;
	private Integer metricDisplayOrder;
	private String tableName;
	private String tableDisplayName;
	private String tableDisplayOrder;
	private Date createdAt;
	private String createdBy;
	private Date lastModifiedAt;
	private String lastModifiedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public Integer getCurrencyFlag() {
		return currencyFlag;
	}

	public void setCurrencyFlag(Integer currencyFlag) {
		this.currencyFlag = currencyFlag;
	}

	public Integer getMetricDisplayOrder() {
		return metricDisplayOrder;
	}

	public void setMetricDisplayOrder(Integer metricDisplayOrder) {
		this.metricDisplayOrder = metricDisplayOrder;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDisplayName() {
		return tableDisplayName;
	}

	public void setTableDisplayName(String tableDisplayName) {
		this.tableDisplayName = tableDisplayName;
	}

	public String getTableDisplayOrder() {
		return tableDisplayOrder;
	}

	public void setTableDisplayOrder(String tableDisplayOrder) {
		this.tableDisplayOrder = tableDisplayOrder;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getCsvFieldName() {
		return csvFieldName;
	}

	public void setCsvFieldName(String csvFieldName) {
		this.csvFieldName = csvFieldName;
	}

	@Override
	public String toString() {
		return "CDSBalanceModelDTO [id=" + id + ", fieldName=" + fieldName + ", description=" + description
				+ ", csvFieldName=" + csvFieldName + ", unit=" + unit + ", currencyFlag=" + currencyFlag
				+ ", metricDisplayOrder=" + metricDisplayOrder + ", tableName=" + tableName + ", tableDisplayName="
				+ tableDisplayName + ", tableDisplayOrder=" + tableDisplayOrder + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}

}
