package com.televisory.user.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author navankur
 *
 */
public class UserDataScoringCommentDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String userId;

	private Date date;

	private Date applicablePeriod;

	private String fieldName;

	private String comment;
	
	@JsonIgnore
	private Date createdAt;
	
	@JsonIgnore
	private Date lastModifiedAt;
	
	@JsonIgnore
	private String createdBy;
	
	@JsonIgnore
	private String lastModifiedBy;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getApplicablePeriod() {
		return applicablePeriod;
	}

	public void setApplicablePeriod(Date applicablePeriod) {
		this.applicablePeriod = applicablePeriod;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public String toString() {
		return "UserDataScoringComment [id=" + id + ", userId=" + userId
				+ ", date=" + date + ", applicablePeriod=" + applicablePeriod
				+ ", fieldName=" + fieldName + ", comment=" + comment
				+ ", createdAt=" + createdAt + ", lastModifiedAt="
				+ lastModifiedAt + ", createdBy=" + createdBy
				+ ", lastModifiedBy=" + lastModifiedBy + "]";
	}



}
