package com.televisory.capitalmarket.dto.economy;

import java.util.Date;

public class EconomyCreditRatingLatestDTO {

	private Integer creditRatingId;

	private Date date;

	private String country;

	private String agency;

	private String rating;

	private String outlook;
	
	private String unit;

	private Integer isActive;

	private Date createdAt;

	private String createdBy;

	private Date lastModifiedAt;

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
		return "EconomyCreditRatingLatestDTO [creditRatingId=" + creditRatingId + ", date=" + date + ", country="
				+ country + ", agency=" + agency + ", rating=" + rating + ", outlook=" + outlook + ", unit=" + unit
				+ ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}

}
