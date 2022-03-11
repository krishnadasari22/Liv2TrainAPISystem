package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author Hitesh
 *
 */
@Entity
@Table(name = "pc_company_list")
public class PCCompany {

	@Column(name = "id")
	private Integer rowNo;

	@Column(name = "company_id")
	private String id;

	@Id
	@Column(name = "entity_id")
	private String factSetEntityId;

	@Column(name = "name")
	private String name;

	@Column(name = "proper_name")
	private String properName;

	@Column(name = "description")
	private String description;


	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "domicile_country_code", referencedColumnName = "country_iso_code_3")
	private String country;*/
	@Column(name = "domicile_country_code")
	private String domicileCountryCode;
	
	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tics_industry_code")
	private TicsIndustry ticsIndustry;*/
	
	@Column(name = "tics_industry_code")
	private String ticsIndustryCode;
	
	@Column(name = "entity_type")
	private String entityType;

	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "security_id")
	private String securityId;
	
	@Column(name = "security_type")
	private String securityType;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiesBy;

	@Column(name = "industry_type")
	private String ff_industry;

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiesBy() {
		return lastModifiesBy;
	}

	public void setLastModifiesBy(String lastModifiesBy) {
		this.lastModifiesBy = lastModifiesBy;
	}


	/*
	 * public String getDomicileCountryCode() { return domicileCountryCode; }
	 * 
	 * public void setDomicileCountryCode(String domicileCountryCode) {
	 * this.domicileCountryCode = domicileCountryCode; }
	 */

	public String getDomicileCountryCode() {
		return domicileCountryCode;
	}

	public void setDomicileCountryCode(String domicileCountryCode) {
		this.domicileCountryCode = domicileCountryCode;
	}

	public String getProperName() {
		return properName;
	}

	public void setProperName(String properName) {
		this.properName = properName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	/*
	 * public String getTicsIndustryCode() { return ticsIndustryCode; }
	 * 
	 * public void setTicsIndustryCode(String ticsIndustryCode) {
	 * this.ticsIndustryCode = ticsIndustryCode; }
	 */

	public String getSecurityType() {
		return securityType;
	}

	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public String getFf_industry() {
		return ff_industry;
	}

	public void setFf_industry(String ff_industry) {
		this.ff_industry = ff_industry;
	}

	public String getFactSetEntityId() {
		return factSetEntityId;
	}

	public void setFactSetEntityId(String factSetEntityId) {
		this.factSetEntityId = factSetEntityId;
	}
	
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@Override
	public String toString() {
		return "CMCompany [rowNo=" + rowNo + ", id=" + id + ", securityId="
				+ securityId + ", factSetEntityId=" + factSetEntityId
				+ ", name=" + name + ", properName=" + properName
				+ ", description=" + description + ", domicileCountryCode=" + domicileCountryCode + ", ticsIndustryCode="
				+ ticsIndustryCode + ", entityType=" + entityType + ", isActive="
				+ isActive + ", securityType="
				+ securityType + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiesBy=" + lastModifiesBy + ", ff_industry="
				+ ff_industry + "]";
	}
	
}
