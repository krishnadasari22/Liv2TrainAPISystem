package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_ind_guid_af")

public class AnalystConsensusFinancial {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name = "fe_item")
	private String feItem;

	@Column(name = "management_date")
	private Date management_date;
	
	@Column(name = "analyst_date")
	private Date analyst_date;
	
	@Column(name = "analyst_value")
	private String analyst_value;
	
	@Column(name = "management_value")
	private String management_value;
	
	@Column(name = "fe_item_desc")
	private String description;

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

	public Date getManagement_date() {
		return management_date;
	}

	public void setManagement_date(Date management_date) {
		this.management_date = management_date;
	}

	public Date getAnalyst_date() {
		return analyst_date;
	}

	public void setAnalyst_date(Date analyst_date) {
		this.analyst_date = analyst_date;
	}

	public String getAnalyst_value() {
		return analyst_value;
	}

	public void setAnalyst_value(String analyst_value) {
		this.analyst_value = analyst_value;
	}

	public String getManagement_value() {
		return management_value;
	}

	public void setManagement_value(String management_value) {
		this.management_value = management_value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "AnalystConsensusFinancial [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem
				+ ", management_date=" + management_date + ", analyst_date=" + analyst_date + ", analyst_value="
				+ analyst_value + ", management_value=" + management_value + ", description=" + description + "]";
	}

	
	
	
	

}
