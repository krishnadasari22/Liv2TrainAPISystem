package com.pcompany.dto;

import java.util.Date;

public class MNADealMetaDataDTO {

	private Integer dealId;
	
	private String company;
	
	private String role;
	
	private String acquirer;
	
	private String target;
	
	private String seller;
			
	private Date announceDate;
	
	private Date closeDate;
	
	private String status;

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public Date getAnnounceDate() {
		return announceDate;
	}

	public void setAnnounceDate(Date announceDate) {
		this.announceDate = announceDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MNADealTerm [dealId=" + dealId + ", company=" + company + ", role=" + role + ", acquirer=" + acquirer
				+ ", target=" + target + ", seller=" + seller + ", announceDate=" + announceDate + ", closeDate="
				+ closeDate + ", status=" + status + "]";
	}
	
}
