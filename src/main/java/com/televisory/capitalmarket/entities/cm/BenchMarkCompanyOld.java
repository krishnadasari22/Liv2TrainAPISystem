package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fp_v2_fp_basic_prices")
public class BenchMarkCompanyOld {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "p_id")
	private Integer id;
	
	
	@Column(name="p_comp_name")
	private String companyName;
	
	@Column(name = "p_fsym_id")
	private String companyId;
	
	@Column(name = "p_price")
	private Double latestPrice;
	
	@Column(name = "p_currency")
	private String currency;
	
	@Column(name = "p_prev_price")
	private Double prevPrice;
	
	@Column(name = "p_change")
	private Double perChange;
	
	@Column(name = "p_date")
	private Date date;

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

	public Double getLatestPrice() {
		return latestPrice;
	}

	public void setLatestPrice(Double latestPrice) {
		this.latestPrice = latestPrice;
	}

	public Double getPrevPrice() {
		return prevPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setPrevPrice(Double prevPrice) {
		this.prevPrice = prevPrice;
	}

	public Double getPerChange() {
		return perChange;
	}

	public void setPerChange(Double perChange) {
		this.perChange = perChange;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	

}
