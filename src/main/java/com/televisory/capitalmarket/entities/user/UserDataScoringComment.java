package com.televisory.capitalmarket.entities.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author navankur
 *
 */
@Entity
@Table(name = "user_data_scoring_comment_af")
public class UserDataScoringComment {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "applicable_date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date applicablePeriod;

	@Column(name = "field_name")
	private String fieldName;

	@Column(name = "comment")
	private String comment;

	@Column(name = "created_at", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date createdAt;


	@Column(name = "last_modified_at", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date lastModifiedAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_by")
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
