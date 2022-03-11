package com.privatecompany.dto;

import java.util.Date;

public class PEVCInvestmentDetailsDTO {
	
	private String pevcSecurityId;
	private String securityName;
	private String round;	
	private String factsetInvestorEntityId;
	private String entityProperName;
	private String pctHeld;
	private Date terminationDate;
	private String entityType;
	private String fundName;
	private String status;
	private Date inceptionDate;
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
