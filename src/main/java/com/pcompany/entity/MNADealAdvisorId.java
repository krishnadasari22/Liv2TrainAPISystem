package com.pcompany.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MNADealAdvisorId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "deal_id")
	private Integer dealId;

	@Column(name = "advisor_name")
	private String advisorName;

	@Column(name = "advisor_role")
	private String advisorRole;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "client_role")
	private String clientRole;

	public MNADealAdvisorId() {
		super();
	}

	public MNADealAdvisorId(Integer dealId, String advisorName, String advisorRole, String clientName, String clientRole) {
		super();
		
		this.dealId = dealId;
		this.advisorName = advisorName; 
		this.advisorRole = advisorRole; 
		this.clientName = clientName; 
		this.clientRole = clientRole;
	}

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public String getAdvisorName() {
		return advisorName;
	}

	public void setAdvisorName(String advisorName) {
		this.advisorName = advisorName;
	}

	public String getAdvisorRole() {
		return advisorRole;
	}

	public void setAdvisorRole(String advisorRole) {
		this.advisorRole = advisorRole;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientRole() {
		return clientRole;
	}

	public void setClientRole(String clientRole) {
		this.clientRole = clientRole;
	}

	@Override
	public String toString() {
		return "MNADealAdvisorId [dealId=" + dealId + ", advisorName=" + advisorName + ", advisorRole=" + advisorRole
				+ ", clientName=" + clientName + ", clientRole=" + clientRole + "]";
	}

}
