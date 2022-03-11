package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "own_v5_own_stakes_detail")
public class ShareholdingOwnershipDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name="factset_entity_id")
	private String entityId;

	@Column(name="entity_name")
	private String companyName;

	@Column(name="as_on_date")
	private Date asOnDate;
	
	@Column(name="report_date")
	private Date reportDate;

	@Column(name="total_shares")
	private Double totalShares;

	@Column(name="pct_os")
	private Double percent;

	@Column(name="entity_type")
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

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Override
	public String toString() {
		return "ShareholdingOwnershipDetails [id=" + id + ", companyId=" + companyId + ", entityId=" + entityId
				+ ", companyName=" + companyName + ", asOnDate=" + asOnDate + ", reportDate=" + reportDate
				+ ", totalShares=" + totalShares + ", percent=" + percent + ", entityType=" + entityType + "]";
	}




}
