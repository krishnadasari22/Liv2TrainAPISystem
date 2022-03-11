package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="credit_rating_latest")
public class EconomyCreditRatingLatest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "credit_rating_id", nullable = false, unique = true)
	private Integer creditRatingId;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "agency")
	private String agency;
	
	@Column(name = "rating")
	private String rating;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "outlook")
	private String outlook;
	
	@Column(name = "is_active	")
	private Integer isActive;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "last_modified_at")
	private Date lastModifiedAt;
	
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	public Integer getCreditRatingId() {
		return creditRatingId;
	}
	public void setCreditRatingId(Integer creditRatingId) {
		this.creditRatingId = creditRatingId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getOutlook() {
		return outlook;
	}
	public void setOutlook(String outlook) {
		this.outlook = outlook;
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
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public String toString() {
		return "EconomyCreditRatingLatest [creditRatingId=" + creditRatingId + ", date=" + date + ", country=" + country
				+ ", agency=" + agency + ", rating=" + rating + ", unit=" + unit + ", outlook=" + outlook
				+ ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
}
