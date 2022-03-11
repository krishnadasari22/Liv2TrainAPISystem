package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

public class ShareholdingOwnershipDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private String companyId;
	
	private String entityId;

	private String companyName;

	private Date asOnDate;
	
	private Date reportDate;

	private Double totalShares;

	private Double percent;

	private String entityType;
	
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Double getTotalShares() {
		return totalShares;
	}
	
	public void setTotalShares(Double totalShares) {
		this.totalShares = totalShares;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Override
	public String toString() {
		return "ShareholdingOwnershipDetailsDTO [id=" + id + ", companyId=" + companyId + ", entityId=" + entityId
				+ ", companyName=" + companyName + ", asOnDate=" + asOnDate + ", reportDate=" + reportDate
				+ ", totalShares=" + totalShares + ", percent=" + percent + ", entityType=" + entityType + "]";
	}

}