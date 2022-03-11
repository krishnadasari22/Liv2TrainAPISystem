package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "market_data_historical")
public class CommodityHistoricalData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "market_data_hisid", nullable = false, unique = true)
	private Integer marketDataHisIid;

	@Column(name = "symbol")
	private String symbol;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "date")
	private Date period;

	@Column(name = "open")
	private Double open;
	
	@Column(name = "high")
	private Double high;
	
	@Column(name = "low")
	private Double low;
	
	@Column(name = "close")
	private Double close;
	
	@Column(name = "daily_change")
	private Double dailyChange;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
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

/*	@Override
	public String toString() {
		return "CommodityHistoricalData [marketDataHisIid=" + marketDataHisIid + ", symbol=" + symbol + ", type=" + type
				+ ", name=" + name + ", unit=" + unit + ", period=" + period + ", open=" + open + ", high=" + high
				+ ", low=" + low + ", close=" + close + ", lastUpdate=" + lastUpdate + ", isActive=" + isActive
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt
				+ ", lastModifiedBy=" + lastModifiedBy + "]";
	}*/
	
	

	public Double getDailyChange() {
		return dailyChange;
	}

	public void setDailyChange(Double dailyChange) {
		this.dailyChange = dailyChange;
	}

	@Override
	public String toString() {
		return "CommodityHistoricalData [marketDataHisIid=" + marketDataHisIid + ", symbol=" + symbol + ", type=" + type
				+ ", name=" + name + ", unit=" + unit + ", period=" + period + ", open=" + open + ", high=" + high
				+ ", low=" + low + ", close=" + close + ", dailyChange=" + dailyChange + ", lastUpdate=" + lastUpdate
				+ ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
	
	

	

}
