package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

public class BenchMarkCompanyOldDTO implements Serializable {

	private static final long serialVersionUID = -7359224336597264487L;

	private Integer id;
	
	private String companyId;
	
	private String companyName;
	
	private Double latestPrice;
	
	private String currency;
	
	private Double prevPrice;
	
	private Double perChange;
	
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

	public void setPrevPrice(Double prevPrice) {
		this.prevPrice = prevPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
