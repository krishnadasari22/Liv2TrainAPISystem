
package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AnalystCoverageChartDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String companyId;
	
	private String feItem;
	
	private Integer buy;
	
	private Integer overWeight;
	
	private Integer neutral;
	
	private String underweight;
	
	private String sell;
	
	private Double feMean;

	private Double feMedian;
	
	private Double feLow;
	
	private Double feHigh;
	
	private Double feStdDev;
	
	private String guidanceType;
	
	private Double guidanceValue;
	
	private Date startDate;

	private String description;
	
	private Date management_date;
	
	private Date analyst_date;
	
	private String analyst_value;
	
	private String management_value;
	
	private Integer upgrade;
	
	private Integer downgrade;
	
	private Integer nochange;
	
	private Integer netmovement; 
	
	private Integer total; 
	
	private String currency; 
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getOverWeight() {
		return overWeight;
	}

	public void setOverWeight(Integer overWeight) {
		this.overWeight = overWeight;
	}

	public Integer getNeutral() {
		return neutral;
	}

	public void setNeutral(Integer neutral) {
		this.neutral = neutral;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	
	public Double getFeMean() {
		return feMean;
	}

	public void setFeMean(Double feMean) {
		this.feMean = feMean;
	}

	public Double getFeMedian() {
		return feMedian;
	}

	public void setFeMedian(Double feMedian) {
		this.feMedian = feMedian;
	}

	public Double getFeLow() {
		return feLow;
	}

	public void setFeLow(Double feLow) {
		this.feLow = feLow;
	}

	public Double getFeHigh() {
		return feHigh;
	}

	public void setFeHigh(Double feHigh) {
		this.feHigh = feHigh;
	}

	public Double getFeStdDev() {
		return feStdDev;
	}

	public void setFeStdDev(Double feStdDev) {
		this.feStdDev = feStdDev;
	}

	public String getGuidanceType() {
		return guidanceType;
	}

	public void setGuidanceType(String guidanceType) {
		this.guidanceType = guidanceType;
	}

	public Double getGuidanceValue() {
		return guidanceValue;
	}

	public void setGuidanceValue(Double guidanceValue) {
		this.guidanceValue = guidanceValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public Date getManagement_date() {
		return management_date;
	}

	public void setManagement_date(Date management_date) {
		this.management_date = management_date;
	}

	public Date getAnalyst_date() {
		return analyst_date;
	}

	public void setAnalyst_date(Date analyst_date) {
		this.analyst_date = analyst_date;
	}

	public String getAnalyst_value() {
		return analyst_value;
	}

	public void setAnalyst_value(String analyst_value) {
		this.analyst_value = analyst_value;
	}

	public String getManagement_value() {
		return management_value;
	}

	public void setManagement_value(String management_value) {
		this.management_value = management_value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(Integer upgrade) {
		this.upgrade = upgrade;
	}

	public Integer getDowngrade() {
		return downgrade;
	}

	public void setDowngrade(Integer downgrade) {
		this.downgrade = downgrade;
	}

	public Integer getNoChange() {
		return nochange;
	}

	public void setNoChange(Integer noChange) {
		this.nochange = noChange;
	}
	
	public Integer getNetmovement() {
		return netmovement;
	}

	public void setNetmovement(Integer netmovement) {
		this.netmovement = netmovement;
	}

	public Integer getNochange() {
		return nochange;
	}

	public void setNochange(Integer nochange) {
		this.nochange = nochange;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "AnalystCoverageChartDTO [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem + ", buy=" + buy
				+ ", overWeight=" + overWeight + ", neutral=" + neutral + ", underweight=" + underweight + ", sell="
				+ sell + ", feMean=" + feMean + ", feMedian=" + feMedian + ", feLow=" + feLow + ", feHigh=" + feHigh
				+ ", feStdDev=" + feStdDev + ", guidanceType=" + guidanceType + ", guidanceValue=" + guidanceValue
				+ ", startDate=" + startDate + ", description=" + description + ", management_date=" + management_date
				+ ", analyst_date=" + analyst_date + ", analyst_value=" + analyst_value + ", management_value="
				+ management_value + ", upgrade=" + upgrade + ", downgrade=" + downgrade + ", nochange=" + nochange
				+ ", netmovement=" + netmovement + ", total=" + total + ", currency=" + currency + "]";
	}





	

	

	

	

	
	
	
	

}
