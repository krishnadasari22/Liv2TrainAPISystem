package com.pcompany.entity;

import java.io.DataOutput;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_basic_conh_rec")
public class AnalystCoverage {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id")
		private Integer id;
		
		@Column(name="fsym_id")
		private String companyId;
		
		@Column(name = "fe_item")
		private String feItem;
		
		@Column(name = "buy")
		private Integer buy;
		
		@Column(name = "overweight")
		private Integer overWeight;
		
		@Column(name = "neutral")
		private Integer neutral;
		
		@Column(name = "underweight")
		private String underweight;
		
		@Column(name = "sell")
		private String sell;
		
		@Column(name = "cons_date")
		private Date startDate;

		@Column(name = "fe_item_desc")
		private String description;
		
		@Column(name = "currency")
		private String currency;
		
		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getFeItem() {
			return feItem;
		}

		public void setFeItem(String feItem) {
			this.feItem = feItem;
		}

		public Integer getBuy() {
			return buy;
		}

		public void setBuy(Integer buy) {
			this.buy = buy;
		}

		

		public Integer getNeutral() {
			return neutral;
		}

		public void setNeutral(Integer neutral) {
			this.neutral = neutral;
		}

		public String getUnderweight() {
			return underweight;
		}

		public void setUnderweight(String underweight) {
			this.underweight = underweight;
		}

		public String getSell() {
			return sell;
		}

		public void setSell(String sell) {
			this.sell = sell;
		}

		public Integer getOverWeight() {
			return overWeight;
		}

		public void setOverWeight(Integer overWeight) {
			this.overWeight = overWeight;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		@Override
		public String toString() {
			return "AnalystCoverage [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem + ", buy=" + buy
					+ ", overWeight=" + overWeight + ", neutral=" + neutral + ", underweight=" + underweight + ", sell="
					+ sell + ", startDate=" + startDate + ", description=" + description + ", currency=" + currency
					+ "]";
		}

		

		

		

		
		
		

}
