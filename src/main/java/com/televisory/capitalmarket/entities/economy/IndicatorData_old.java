package com.televisory.capitalmarket.entities.economy;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "indicator_data")
public class IndicatorData_old {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "indicators_data_id", nullable = false, unique = true)
	private int id;
	
	@Column(name="period")
	private Date period;
	
	@ManyToOne
	@JoinColumn(name="country_indicator_id")
	private CountryIndicators countryIndicator;
	
	@Column(name="value")
	private Double data;
	
	@Column(name="rating")
	private String rating;
	
	@Column(name = "is_active")
	private int isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_at")
	private Date lastModifiedAt;

	@Column(name = "modified_by")
	private String lastModifiedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
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

	public CountryIndicators getCountryIndicator() {
		return countryIndicator;
	}

	public void setCountryIndicator(CountryIndicators countryIndicator) {
		this.countryIndicator = countryIndicator;
	}
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "IndicatorData [id=" + id + ", period=" + period + ", countryIndicator=" + countryIndicator + ", data="
				+ data + ", rating=" + rating + ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}

}
