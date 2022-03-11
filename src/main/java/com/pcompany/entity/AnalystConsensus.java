package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_advanced_conh_af")
public class AnalystConsensus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name = "fe_item")
	private String feItem;

	@Column(name = "mean")
	private Double feMean;

	@Column(name = "median")
	private Double feMedian;
	
	@Column(name = "low")
	private Double feLow;
	
	@Column(name = "high")
	private Double feHigh;
	
	@Column(name = "standard_deviation")
	private Double feStdDev;
	
	@Column(name = "fe_fp_end")
	private Date startDate;
	
	@Column(name = "fe_item_desc")
	private String description;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "unit")
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

	public Double getFeMean() {
		return feMean;
	}

	public void setFeMean(Double feMean) {
		this.feMean = feMean;
	}

	public Double getFeMedian() {
		return feMedian;
	}

	public void setFeMedian(Double feMedian) {
		this.feMedian = feMedian;
	}

	public Double getFeLow() {
		return feLow;
	}

	public void setFeLow(Double feLow) {
		this.feLow = feLow;
	}

	public Double getFeHigh() {
		return feHigh;
	}

	public void setFeHigh(Double feHigh) {
		this.feHigh = feHigh;
	}

	public Double getFeStdDev() {
		return feStdDev;
	}

	public void setFeStdDev(Double feStdDev) {
		this.feStdDev = feStdDev;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "AnalystConsensus [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem + ", feMean=" + feMean
				+ ", feMedian=" + feMedian + ", feLow=" + feLow + ", feHigh=" + feHigh + ", feStdDev=" + feStdDev
				+ ", startDate=" + startDate + ", description=" + description + ", currency=" + currency + ", unit="
				+ unit + "]";
	}

	

	

	

	
}
