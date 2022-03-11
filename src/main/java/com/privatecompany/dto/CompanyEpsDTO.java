package com.privatecompany.dto;

import java.util.Date;

public class CompanyEpsDTO {

	private String companyId;
	private String currency;
	private Date date;
	private Double epsData;
	private String acquirer;
	private String target;
	private Double transactionValue;

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

	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Double getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionValue(Double transactionValue) {
		this.transactionValue = transactionValue;
	}

}
