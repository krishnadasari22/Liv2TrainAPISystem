package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_advanced_guid_af")
public class AnalystManagementGuide {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name = "fe_item")
	private String feItem;

	@Column(name = "fe_fp_end")
	private Date startDate;

	@Column(name = "guidance_type")
	private String guidanceType;
	
	@Column(name = "guidance_value")
	private Double guidanceValue;
	
	@Column(name = "fe_item_desc")
	private String description;
	
	private String currency; 
	
	private String unit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public String getGuidanceType() {
		return guidanceType;
	}

	public void setGuidanceType(String guidanceType) {
		this.guidanceType = guidanceType;
	}

	public Double getGuidanceValue() {
		return guidanceValue;
	}

	public void setGuidanceValue(Double guidanceValue) {
		this.guidanceValue = guidanceValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "AnalystManagementGuide [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem + ", startDate="
				+ startDate + ", guidanceType=" + guidanceType + ", guidanceValue=" + guidanceValue + ", description="
				+ description + ", currency=" + currency + ", unit=" + unit + "]";
	}	

}
