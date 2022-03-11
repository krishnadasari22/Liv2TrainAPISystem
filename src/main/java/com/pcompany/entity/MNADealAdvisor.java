package com.pcompany.entity;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ma_v1_ma_profession")
public class MNADealAdvisor {

	@EmbeddedId
	MNADealAdvisorId dealAdvisorId;
	
	@Column(name = "deal_status")
	private String dealStatus;
	
	@Column(name = "fee")
	private Double fee;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "comment")
	private String comment;

	public MNADealAdvisorId getDealAdvisorId() {
		return dealAdvisorId;
	}

	public void setDealAdvisorId(MNADealAdvisorId dealAdvisorId) {
		this.dealAdvisorId = dealAdvisorId;
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
		return "MNADealAdvisor [dealAdvisorId=" + dealAdvisorId + ", dealStatus=" + dealStatus + ", fee=" + fee
				+ ", currency=" + currency + ", comment=" + comment + "]";
	}

}
