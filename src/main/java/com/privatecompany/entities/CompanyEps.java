package com.privatecompany.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ff_v3_ff_basic_ltm")
public class CompanyEps {

	@Column(name = "fsym_id")
	private String companyId;

	@Column(name = "currency")
	private String currency;

	@Id
	@Column(name = "date")
	private Date date;

	@Column(name = "ff_eps_basic")
	private Double epsData;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getEpsData() {
		return epsData;
	}

	public void setEpsData(Double epsData) {
		this.epsData = epsData;
	}

	@Override
	public String toString() {
		return "CompanyEps [companyId=" + companyId + ", currency=" + currency + ", date=" + date + ", epsData="
				+ epsData + "]";
	}

}
