package com.televisory.capitalmarket.factset.dto;

import java.util.Date;

public class FFStockSpinoffDTO {

	private Integer id;
	private String companyId;

	private Date date;
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
		return "CorpAnnounceAdjustStockPriceDTO [id=" + id + ", companyId="
				+ companyId + ", date=" + date + ", factor=" + factor + "]";
	}
	
	

}
