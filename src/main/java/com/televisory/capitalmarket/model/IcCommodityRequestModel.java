package com.televisory.capitalmarket.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;

/**
 * 
 * @author vinay
 *
 */
public class IcCommodityRequestModel {
	
	private Date startDate;
	private Date endDate;
	
	private String currency;
	
	private List<String> commoditySymbolList;
	
	private Map<String,List<CommodityHistoricalDataDTO>> commodityDataMap;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<String> getCommoditySymbolList() {
		return commoditySymbolList;
	}

	public void setCommoditySymbolList(List<String> commoditySymbolList) {
		this.commoditySymbolList = commoditySymbolList;
	}

	public Map<String, List<CommodityHistoricalDataDTO>> getCommodityDataMap() {
		return commodityDataMap;
	}

	public void setCommodityDataMap(Map<String, List<CommodityHistoricalDataDTO>> commodityDataMap) {
		this.commodityDataMap = commodityDataMap;
	}

	@Override
	public String toString() {
		return "IcCommodityRequestModel [startDate=" + startDate + ", endDate=" + endDate + ", currency=" + currency
				+ ", commoditySymbolList=" + commoditySymbolList + ", commodityDataMap=" + commodityDataMap + "]";
	}

}
