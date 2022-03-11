package com.televisory.user;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

public class UserModelDTO  implements Serializable{

		private static final long serialVersionUID = -4955640308771834681L;

		private Integer id;

		private String periodType;

		private Date period;

		private Date applicablePeriod;
		
		@Mapping("depthLevel")
		private String depthLevel;
		
		@Mapping("mandatory")
		private Integer mandatory;
		
		private String displayOrder;
		
		private String financialType;
		
		private String fieldName;
		
		private String section;
		
		private String shortName;
		
		@Mapping("itemName")
		private String itemName;
		
		private String displayName;

		private Double data;
		
		@Mapping("unit")
		private String unit;
		
		private String currency;
				
		private Integer isActive;

		private Date createdAt;

		private String creadtedBy;

		private Date lastmodifiedAt;

		private String lastModifiedBy;
		
		private int keyParameter;

		private int keyParameterOrder;
		
		private Integer icFlag;
		
		private Integer screenerFlag;
		
		private Integer watchListFlag;
		
		public Date getPeriod() {
			return period;
		}

		public void setPeriod(Date period) {
			this.period = period;
		}

		public int getKeyParameter() {
			return keyParameter;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public void setKeyParameter(int keyParameter) {
			this.keyParameter = keyParameter;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getPeriodType() {
			return periodType;
		}

		public void setPeriodType(String periodType) {
			this.periodType = periodType;
		}

		public Date getApplicablePeriod() {
			return applicablePeriod;
		}

		public void setApplicablePeriod(Date period) {
			this.applicablePeriod = period;
		}
		

		public String getDepthLevel() {
			return depthLevel;
		}

		public void setDepthLevel(String depthLevel) {	
			if(depthLevel.contains("L")){
				depthLevel=depthLevel.substring(1);
			}
			this.depthLevel = depthLevel;
		}

		public Integer getMandatory() {
			return mandatory;
		}

		public void setMandatory(Integer mandatory) {
			this.mandatory = mandatory;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public String getSection() {
			return section;
		}

		public void setSection(String section) {
			this.section = section;
		}

		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		
		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public Double getData() {
			return data;
		}

		public void setData(Double data) {
			this.data = data;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
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

		public String getCreadtedBy() {
			return creadtedBy;
		}

		public void setCreadtedBy(String creadtedBy) {
			this.creadtedBy = creadtedBy;
		}

		public Date getLastmodifiedAt() {
			return lastmodifiedAt;
		}

		public void setLastmodifiedAt(Date lastmodifiedAt) {
			this.lastmodifiedAt = lastmodifiedAt;
		}

		public String getLastModifiedBy() {
			return lastModifiedBy;
		}

		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}

		public String getDisplayOrder() {
			return displayOrder;
		}

		public void setDisplayOrder(String displayOrder) {
			this.displayOrder = displayOrder;
		}
		
		public String getFinancialType() {
			return financialType;
		}

		public void setFinancialType(String financialType) {
			this.financialType = financialType;
		}

		public int getKeyParameterOrder() {
			return keyParameterOrder;
		}

		public void setKeyParameterOrder(int keyParameterOrder) {
			this.keyParameterOrder = keyParameterOrder;
		}

		public Integer getIcFlag() {
			return icFlag;
		}

		public void setIcFlag(Integer icFlag) {
			this.icFlag = icFlag;
		}

		public Integer getScreenerFlag() {
			return screenerFlag;
		}

		public void setScreenerFlag(Integer screenerFlag) {
			this.screenerFlag = screenerFlag;
		}

		public Integer getWatchListFlag() {
			return watchListFlag;
		}

		public void setWatchListFlag(Integer watchListFlag) {
			this.watchListFlag = watchListFlag;
		}

		@Override
		public String toString() {
			return "UserModelDTO [id=" + id + ", periodType=" + periodType + ", period=" + applicablePeriod + ", depthLevel="
					+ depthLevel + ", mandatory=" + mandatory + ", displayOrder=" + displayOrder + ", financialType="
					+ financialType + ", fieldName=" + fieldName + ", section=" + section + ", shortName=" + shortName
					+ ", itemName=" + itemName + ", displayName=" + displayName + ", data=" + data + ", unit=" + unit
					+ ", currency=" + currency + ", isActive=" + isActive + ", createdAt=" + createdAt + ", creadtedBy="
					+ creadtedBy + ", lastmodifiedAt=" + lastmodifiedAt + ", lastModifiedBy=" + lastModifiedBy
					+ ", keyParameter=" + keyParameter + ", keyParameterOrder=" + keyParameterOrder + ", icFlag="
					+ icFlag + ", screenerFlag=" + screenerFlag + ", watchListFlag=" + watchListFlag + "]";
		}

}
