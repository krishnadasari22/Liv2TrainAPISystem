package com.pcompany.model;

import java.util.Date;

public class OwnershipModel {
	
	int id;
	String companyId;
	Date period;
	Date asOnDate;
	Double total;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	public Date getAsOnDate() {
		return asOnDate;
	}
	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "OwnershipModel [id=" + id + ", companyId=" + companyId + ", period=" + period + ", asOnDate=" + asOnDate
				+ ", total=" + total + "]";
	}
}
