package com.televisory.capitalmarket.dto.economy;

import java.io.Serializable;
import java.util.Date;

public class CountryListDTO implements Serializable {
	
	private static final long serialVersionUID = -6493216144614594301L;
	private int id;
	private String countryIsoCode2;
	private String countryIsoCode3;
	private String countryName;
	private int isActive;
	private Date createdAt;
	private String createdBy;
	private Date lastModifiedAt;	
	private String lastModifiedBy;

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
	@Override
	public String toString() {
		return "CountryListDTO [id=" + id + ", countryIsoCode2="
				+ countryIsoCode2 + ", countryIsoCode3=" + countryIsoCode3
				+ ", countryName=" + countryName + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}
	
	

}
