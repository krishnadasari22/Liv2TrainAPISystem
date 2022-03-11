package com.televisory.capitalmarket.entities.cm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "tics_sector")
@Proxy(lazy=false)
public class TicsSector implements Serializable {
	
	private static final long serialVersionUID = 5358726982933201465L;

	@Column(name="sector_id")
	private Integer sectorId;
	
	@Id
	@Column(name="tics_sector_code")
	private String ticsSectorCode;
	
	@Column(name="tics_sector_name")
	private String ticsSectorName;
	
	@Column(name="tics_sector_desc")
	private String ticsSectorDesc;
	
	@Column(name="is_active")
	private Integer isActive;
	
	@Column(name="created_at")
	private Date CreatedAt;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="last_modified_at")
	private Date lastModifiedAt;
	
	@Column(name="last_modified_by")
	private String lastModifiedBy;
	
	public Integer getSectorId() {
		return sectorId;
	}

	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}

	public String getTicsSectorCode() {
		return ticsSectorCode;
	}

	public void setTicsSectorCode(String ticsSectorCode) {
		this.ticsSectorCode = ticsSectorCode;
	}

	public String getTicsSectorName() {
		return ticsSectorName;
	}

	public void setTicsSectorName(String ticsSectorName) {
		this.ticsSectorName = ticsSectorName;
	}

	public String getTicsSectorDesc() {
		return ticsSectorDesc;
	}

	public void setTicsSectorDesc(String ticsSectorDesc) {
		this.ticsSectorDesc = ticsSectorDesc;
	}
	
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
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
		return "TicsSector [sectorId=" + sectorId + ", ticsSectorCode=" + ticsSectorCode + ", ticsSectorName="
				+ ticsSectorName + ", ticsSectorDesc=" + ticsSectorDesc + ", isActive=" + isActive + ", CreatedAt="
				+ CreatedAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}
	
}
