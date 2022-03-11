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
public class OwnershipDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="fsym_id")
	private String companyId;

	@Column(name="as_on")
	private Date period;

	@Column(name="entity_name")
	private String entityName;

	@Column(name="total_share")
	private Double totalShares;

	@Column(name="pct_os")
	private Double percentShare;

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

	public Double getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(Double totalShares) {
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

