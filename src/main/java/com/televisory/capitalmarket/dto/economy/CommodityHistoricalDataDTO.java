package com.televisory.capitalmarket.dto.economy;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class CommodityHistoricalDataDTO {


	private Integer marketDataHisIid;
	
	private String symbol;
	
	@JsonIgnore
	private String type;
	
	private String name;
	
	
	private String unit;
	private Date period;
	
	@JsonIgnore
	private Double open;
	
	@JsonIgnore
	private Double high;
	
	@JsonIgnore
	private Double low;
	
	private Double close;
	
	@JsonIgnore
	private Date lastUpdate;
	
	private Double dailyChange;
	
	@JsonIgnore
	private int isActive;
	@JsonIgnore
	private Date createdAt;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Date lastModifiedAt;
	@JsonIgnore
	private String lastModifiedBy;

	public Integer getMarketDataHisIid() {
		return marketDataHisIid;
	}

	public void setMarketDataHisIid(Integer marketDataHisIid) {
		this.marketDataHisIid = marketDataHisIid;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	

	public Double getDailyChange() {
		return dailyChange;
	}

	public void setDailyChange(Double dailyChange) {
		this.dailyChange = dailyChange;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
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
		return "CommodityHistoricalDataDTO [marketDataHisIid=" + marketDataHisIid + ", symbol=" + symbol + ", type="
				+ type + ", name=" + name + ", unit=" + unit + ", period=" + period + ", open=" + open + ", high="
				+ high + ", low=" + low + ", close=" + close + ", lastUpdate=" + lastUpdate + ", dailyChange="
				+ dailyChange + ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
	
	

}
