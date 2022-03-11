package com.televisory.capitalmarket.entities.industry;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "country_list")
@Proxy(lazy=false)
public class CountryListMIN {
	
		
	@Column(name = "country_id", nullable = false, unique = true)
	private int id;

	@Column(name = "country_iso_code_2")
	private String countryIsoCode2;

	@Id
	@Column(name = "country_iso_code_3")
	private String countryIsoCode3;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "is_active")
	private int isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	@Column(name = "region_id")
	private Integer regionId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountryIsoCode2() {
		return countryIsoCode2;
	}

	public void setCountryIsoCode2(String countryIsoCode2) {
		this.countryIsoCode2 = countryIsoCode2;
	}

	public String getCountryIsoCode3() {
		return countryIsoCode3;
	}

	public void setCountryIsoCode3(String countryIsoCode3) {
		this.countryIsoCode3 = countryIsoCode3;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	@Override
	public String toString() {
		return "CountryList [id=" + id + ", countryIsoCode2=" + countryIsoCode2 + ", countryIsoCode3="
				+ countryIsoCode3 + ", countryName=" + countryName + ", isActive=" + isActive + ", createdAt="
				+ createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiedBy=" + lastModifiedBy + ", regionId=" + regionId + "]";
	}

	
	
		
}
