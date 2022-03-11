package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ma_v1_ma_deal_terms")
public class MNADealMetaData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "deal_id")
	private Integer dealId;
	
	@Column(name = "company")
	private String company;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "acquirer")
	private String acquirer;
	
	@Column(name = "target")
	private String target;
	
	@Column(name = "seller")
	private String seller;
			
	@Column(name = "announce_date")
	private Date announceDate;
	
	@Column(name = "close_date")
	private Date closeDate;
	
	@Column(name = "status")
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
