package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author vinay
 *
 */
public class IndexDTO implements Serializable {
	
	private static final long serialVersionUID = -7641867234975147849L;
	private int id;
	private String name;
	private int isActive;
	private Date createdAt;
	private String createdBy;
	private Date lastModifiedAt;
	private String lastModifiedBy;
	//private List<CompanyDTO> indexCompanies;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	/*public List<CompanyDTO> getIndexCompanies() {
		return indexCompanies;
	}
	public void setIndexCompanies(List<CompanyDTO> indexCompanies) {
		this.indexCompanies = indexCompanies;
	}*/
	@Override
	public String toString() {
		return "IndexDTO [id=" + id + ", name=" + name + ", isActive=" + isActive + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}
	
	
	
}
