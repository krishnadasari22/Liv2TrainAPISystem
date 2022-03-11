package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_basic_conh_rec")
public class AnalystGuidanceDeviation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	/*@Column(name = "closest_guidance_date")
	private Date closestGuidanceDate;*/
	
	@Column(name = "fe_fp_end")
	private Date startDate;
	
	@Column(name = "guidance_date")
	private Date guidanceDate;
	
	@Column(name = "fe_item")
	private String feItem;
	
	@Column(name = "high_value")
	private String highValue;
	
	@Column(name = "low_value")
	private String lowValue;
	
	@Column(name = "field_name")
	private String feItemCode;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "fe_item_desc")
	private String description;

	@Column(name = "unit")
	private String unit;
	
	@Column(name = "currency")
	private String currency;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFeItem() {
		return feItem;
	}

	public void setFeItem(String feItem) {
		this.feItem = feItem;
	}

	public String getHighValue() {
		return highValue;
	}

	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}
	
	public String getLowValue() {
		return lowValue;
	}

	public void setLowValue(String lowValue) {
		this.lowValue = lowValue;
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

	/*public Date getClosestGuidanceDate() {
		return closestGuidanceDate;
	}

	public void setClosestGuidanceDate(Date closestGuidanceDate) {
		this.closestGuidanceDate = closestGuidanceDate;
	}*/

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
		return "AnalystGuidanceDeviation [id=" + id + ", startDate=" + startDate + ", guidanceDate=" + guidanceDate
				+ ", feItem=" + feItem + ", highValue=" + highValue + ", lowValue=" + lowValue + ", feItemCode="
				+ feItemCode + ", tableName=" + tableName + ", description=" + description + ", unit=" + unit
				+ ", currency=" + currency + "]";
	}
	
}
