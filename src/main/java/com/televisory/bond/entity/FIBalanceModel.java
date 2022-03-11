package com.televisory.bond.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="fi_balance_model")
public class FIBalanceModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="field_name")
	private String  fieldName;

	@Column(name = "description")
	private String description;
	
	@Column(name = "section")
	private String section;
	
	@Column(name = "display_name")
	private String displayName;
	
	@Column(name = "display_order")
	private String displayOrder;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "currency_flag")
	private Integer currencyFlag;
	
	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	@Column(name="chart_history_flag")
	private int chartHistoryFlag;

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
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
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
	
	public int getChartHistoryFlag() {
		return chartHistoryFlag;
	}

	public void setChartHistoryFlag(int chartHistoryFlag) {
		this.chartHistoryFlag = chartHistoryFlag;
	}

	@Override
	public String toString() {
		return "FIBalanceModel [id=" + id + ", fieldName=" + fieldName + ", description=" + description + ", section="
				+ section + ", displayName=" + displayName + ", displayOrder=" + displayOrder + ", unit=" + unit
				+ ", currencyFlag=" + currencyFlag + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
}
