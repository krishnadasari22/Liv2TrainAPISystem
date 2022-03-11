package com.televisory.bond.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="cds_balance_model")
public class CDSBalanceModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="field_name")
	private String  fieldName;

	@Column(name = "description")
	private String description;
	
	@Column(name = "csv_field_name")
	private String csvFieldName;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "currency_flag")
	private Integer currencyFlag;
	
	@Column(name = "metric_display_order")
	private Integer metricDisplayOrder;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "table_display_name")
	private String tableDisplayName;
	
	@Column(name = "table_display_order")
	private String tableDisplayOrder;
	
	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
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
		return "CDSBalanceModel [id=" + id + ", fieldName=" + fieldName + ", description=" + description
				+ ", csvFieldName=" + csvFieldName + ", unit=" + unit + ", currencyFlag=" + currencyFlag
				+ ", metricDisplayOrder=" + metricDisplayOrder + ", tableName=" + tableName + ", tableDisplayName="
				+ tableDisplayName + ", tableDisplayOrder=" + tableDisplayOrder + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}

}
