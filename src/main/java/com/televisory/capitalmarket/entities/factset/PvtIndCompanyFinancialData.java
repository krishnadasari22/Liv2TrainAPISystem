package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//to store private company financial data
@Entity
public class PvtIndCompanyFinancialData {
		@Id
		@Column(name = "id")
		private Integer id;
		
		@Column(name = "cin")
		private String companyId;

		@Column(name = "type", insertable = false)
		private String financialType;

		private static final Integer mandatory = 0;

		@Column(name = "display_order")
		private String displayOrder;

		@Column(name = "display_level")
		private String depthLevel;

		@Column(name = "field_name")
		private String fieldName;

		@Column(name = "section")
		private String section;
		
		@Column(name = "short_name")
		private String shortName;
		
		@Column(name = "description")
		private String itemName;
		
		@Column(name = "display_name")
		private String displayName;

		@Column(name = "date", insertable = false)
		@Temporal(TemporalType.DATE)
		private Date period;

		@Column(name = "currency", insertable = false)
		private String currency;
		
		@Column(name = "unit", insertable = false)
		private String unit;

		@Column(name = "data", insertable = false)
		private Double data;
		
		@Column(name = "key_parameter", insertable = false)
		private Integer keyParameter;
		
		@Column(name = "key_parameter_order", insertable = false)
		private Integer keyParameterOrder;
		
		@Column(name = "ic_flag", insertable = false)
		private Integer icFlag;
		
		@Column(name = "screener_flag", insertable = false)
		private Integer screenerFlag;
		
		@Column(name = "watchlist_flag", insertable = false)
		private Integer watchlistFlag;

		public String getFinancialType() {
			return financialType;
		}

		public void setFinancialType(String financialType) {
			this.financialType = financialType;
		}

		public String getDisplayOrder() {
			return displayOrder;
		}

		public void setDisplayOrder(String displayOrder) {
			this.displayOrder = displayOrder;
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
		
		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public Date getPeriod() {
			return period;
		}

		public void setPeriod(Date period) {
			this.period = period;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public Double getData() {
			return data;
		}

		public void setData(Double data) {
			this.data = data;
		}

		public Integer getKeyParameter() {
			return keyParameter;
		}

		public void setKeyParameter(Integer keyParameter) {
			this.keyParameter = keyParameter;
		}

		public Integer getKeyParameterOrder() {
			return keyParameterOrder;
		}

		public void setKeyParameterOrder(Integer keyParameterOrder) {
			this.keyParameterOrder = keyParameterOrder;
		}

		public String getDepthLevel() {
			return depthLevel;
		}

		public void setDepthLevel(String depthLevel) {

			this.depthLevel = depthLevel;
		}

		public static Integer getMandatory() {
			return mandatory;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
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

		public Integer getWatchlistFlag() {
			return watchlistFlag;
		}

		public void setWatchlistFlag(Integer watchlistFlag) {
			this.watchlistFlag = watchlistFlag;
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		@Override
		public String toString() {
			return "PrivateCompanyFinancialData [id=" + id + ", entityId=" + companyId + ", financialType="
					+ financialType + ", displayOrder=" + displayOrder + ", depthLevel=" + depthLevel + ", fieldName="
					+ fieldName + ", section=" + section + ", shortName=" + shortName + ", itemName=" + itemName
					+ ", displayName=" + displayName + ", period=" + period + ", currency=" + currency + ", unit="
					+ unit + ", data=" + data + ", keyParameter=" + keyParameter + ", keyParameterOrder="
					+ keyParameterOrder + ", icFlag=" + icFlag + ", screenerFlag=" + screenerFlag + ", watchlistFlag="
					+ watchlistFlag + "]";
		}

		
}
