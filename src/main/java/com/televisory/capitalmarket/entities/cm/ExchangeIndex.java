package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "index_list")
public class ExchangeIndex {

	@Id
	@Column(name="index_id")
	private Integer id;
	
	@Column(name="factset_benchmark_id")
	private String factSetBenchmarkId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="primary_flag")
	private Integer primaryFlag;
	
	@Column(name="exchange_code")
	private String exchangeCode;
	
	@Column(name="is_active")
	private Integer isActive;
	
	@Column(name="created_at")
	private Date createdAt;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="last_modified_at")
	private Date lastModifiedAt;
	
	@Column(name="last_modified_by")
	private String lastModifiedBy;

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFactSetBenchmarkId() {
		return factSetBenchmarkId;
	}

	public void setFactSetBenchmarkId(String factSetBenchmarkId) {
		this.factSetBenchmarkId = factSetBenchmarkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrimaryFlag() {
		return primaryFlag;
	}

	public void setPrimaryFlag(Integer primaryFlag) {
		this.primaryFlag = primaryFlag;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
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

}
