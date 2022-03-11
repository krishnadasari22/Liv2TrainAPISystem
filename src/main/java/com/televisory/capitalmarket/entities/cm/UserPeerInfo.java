package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "user_peer_info_af")
public class UserPeerInfo {
	
	@Id
	@Column(name="id")
	private Integer id;

	@Column(name = "user_Id")
	private String userId;
	
	@Column(name = "applicable_date")
	private Date applicableDate;
	
	@Column(name = "peer_company_id")
	private String peerCompanyId;
	
	@Column(name = "peer_set")
	private String peerSet;
	
	@Column(name = "is_active")
	private Integer isActive;
	
	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
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
		return "UserPeerInfo [id=" + id + ", userId=" + userId + ", applicableDate=" + applicableDate
				+ ", peerCompanyId=" + peerCompanyId + ", peerSet=" + peerSet + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiesBy=" + lastModifiesBy + "]";
	}
}
