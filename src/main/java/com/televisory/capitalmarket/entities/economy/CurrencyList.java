package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author vinay
 *
 */
@Entity
@Table(name = "currency_list")
public class CurrencyList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "currency_id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "iso_code")
	private String isoCode;

	@Column(name = "currency_name", unique = true)
	private String currencyName;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
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
		return "CurrencyList [id=" + id + ", isoCode=" + isoCode + ", currencyName=" + currencyName + ", isActive="
				+ isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastModifiedAt="
				+ lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
}
