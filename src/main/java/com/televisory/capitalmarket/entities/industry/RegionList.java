package com.televisory.capitalmarket.entities.industry;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;


@Entity
@Table(name = "region_list")
@Proxy(lazy=false)
public class RegionList {

		@Id
		@Column(name = "region_id", nullable = false, unique = true)
		private int regionId;

		@Column(name = "region_name")
		private String regionName;

		@Column(name = "region_code")
		private String regionCode;

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

		public int getRegionId() {
			return regionId;
		}

		public void setRegionId(int regionId) {
			this.regionId = regionId;
		}

		public String getRegionName() {
			return regionName;
		}

		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}

		public String getRegionCode() {
			return regionCode;
		}

		public void setRegionCode(String regionCode) {
			this.regionCode = regionCode;
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
			return "RegionList [regionId=" + regionId + ", regionName=" + regionName + ", regionCode=" + regionCode
					+ ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
					+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
		}

}
