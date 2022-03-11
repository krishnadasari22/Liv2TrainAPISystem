package com.pcompany.dto;

import org.dozer.Mapping;

public class MNADealAdvisorDTO {

	@Mapping("dealAdvisorId.dealId")
	private Integer dealId;

	@Mapping("dealAdvisorId.advisorName")
	private String advisorName;

	@Mapping("dealAdvisorId.advisorRole")
	private String advisorRole;

	@Mapping("dealAdvisorId.clientName")
	private String clientName;

	@Mapping("dealAdvisorId.clientRole")
	private String clientRole;
	
	private String dealStatus;
	
	private Double fee;
	
	private String currency;
	
	private String comment;

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

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "MNADealAdvisorDTO [dealId=" + dealId + ", advisorName=" + advisorName + ", advisorRole=" + advisorRole
				+ ", clientName=" + clientName + ", clientRole=" + clientRole + ", dealStatus=" + dealStatus + ", fee="
				+ fee + ", currency=" + currency + ", comment=" + comment + "]";
	}
	
}
