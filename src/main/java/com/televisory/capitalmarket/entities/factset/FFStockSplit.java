package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fp_v2_fp_basic_splits")
public class FFStockSplit {

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name="p_split_date")
	private Date date;
	
	@Column(name="p_split_factor")
	private Double factor;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}

	@Override
	public String toString() {
		return "FactsetStockSplit [id=" + id + ", companyId=" + companyId + ", date=" + date + ", factor=" + factor
				+ "]";
	}
}
