package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class OwnershipDetailsDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private String companyId;

	private Date period;

	private String entityName;
	
	private String totalShares;
	
	private Double percentShare;

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

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(String totalShares) {
		this.totalShares = totalShares;
	}

	public Double getPercentShare() {
		return percentShare;
	}

	public void setPercentShare(Double percentShare) {
		this.percentShare = percentShare;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@Override
	public String toString() {
		return "OwnershipDetails [id=" + id + ", companyId=" + companyId
				+ ", period=" + period + ", entityName=" + entityName
				+ ", totalShares=" + totalShares + ", percentShare="
				+ percentShare + ", entityType=" + entityType + "]";
	}
	

}
