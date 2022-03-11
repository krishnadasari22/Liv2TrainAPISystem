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
public class AnalystTotalRec {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name = "fe_item")
	private String feItem;
		
	@Column(name = "cons_date")
	private Date startDate;

	@Column(name = "fe_item_desc")
	private String description;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "total", insertable=false)
	private Integer total;

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

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "AnalystTotalRec [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem + ", startDate="
				+ startDate + ", description=" + description + ", currency=" + currency + ", total=" + total + "]";
	}

	
}
