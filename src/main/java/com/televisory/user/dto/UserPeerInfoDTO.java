package com.televisory.user.dto;

import java.io.Serializable;
import java.util.Date;

public class UserPeerInfoDTO implements Serializable{
	
	private Integer id;
	
	private String userId;
	
	private Date applicableDate;
	
	private String peerCompanyId;
	
	private String peerSet;
	
	private Integer isActive;
	
	private Date createdAt;

	private String createdBy;

	private Date lastModifiedAt;

	private String lastModifiesBy;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getApplicableDate() {
		return applicableDate;
	}

	public void setApplicableDate(Date applicableDate) {
		this.applicableDate = applicableDate;
	}

	public String getPeerCompanyId() {
		return peerCompanyId;
	}

	public void setPeerCompanyId(String peerCompanyId) {
		this.peerCompanyId = peerCompanyId;
	}

	public String getPeerSet() {
		return peerSet;
	}

	public void setPeerSet(String peerSet) {
		this.peerSet = peerSet;
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

	@Override
	public String toString() {
		return "UserPeerInfoDTO [id=" + id + ", userId=" + userId + ", applicableDate=" + applicableDate
				+ ", peerCompanyId=" + peerCompanyId + ", peerSet=" + peerSet + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiesBy=" + lastModifiesBy + "]";
	}
}
