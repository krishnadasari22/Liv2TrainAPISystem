package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pe_v1_pe_securities")
public class PEVCInvestmentDetails {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "pevc_security_id")
	private String pevcSecurityId;
	
	@Column(name = "security_name")
	private String securityName;	
	
	@Column(name = "category_name_desc")
	private String round;	
	
	@Column(name = "factset_investor_entity_id")
	private String factsetInvestorEntityId;
	
	@Column(name="entity_proper_name")
	private String entityProperName;
	
	@Column(name="pct_held")
	private String pctHeld;
	
	@Column(name="termination_date")
	private Date terminationDate;
	
	@Column(name="entity_type")
	private String entityType;
	
	@Column(name="fundName")
	private String fundName;
	
	@Column(name="status")
	private String status;
	
	@Column(name="inception_date")
	private Date inceptionDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPevcSecurityId() {
		return pevcSecurityId;
	}

	public void setPevcSecurityId(String pevcSecurityId) {
		this.pevcSecurityId = pevcSecurityId;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getFactsetInvestorEntityId() {
		return factsetInvestorEntityId;
	}

	public void setFactsetInvestorEntityId(String factsetInvestorEntityId) {
		this.factsetInvestorEntityId = factsetInvestorEntityId;
	}

	public String getEntityProperName() {
		return entityProperName;
	}

	public void setEntityProperName(String entityProperName) {
		this.entityProperName = entityProperName;
	}

	public String getPctHeld() {
		return pctHeld;
	}

	public void setPctHeld(String pctHeld) {
		this.pctHeld = pctHeld;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Date inceptionDate) {
		this.inceptionDate = inceptionDate;
	}
	
	
	


	

}
