package com.pcompany.model;

import java.io.Serializable;
import java.util.List;

import com.pcompany.dto.MNATopDealChartDTO;
import com.pcompany.dto.MNATopDealMakerDTO;
import com.pcompany.dto.MNATopDealMakerTotalDTO;


public class MNATopDealMakerResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long totalCount;
	
	private Integer totalDeals;

	private Double totalValue;

	private Double avgValue;

	private Double maxValue;

	private String currency;

	private String unit;
	
	private MNATopDealMakerTotalDTO topDealTotal;
	
	List<MNATopDealMakerDTO> topDealList;
	
	List<MNATopDealChartDTO> topDealChartData;
	
	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalDeals() {
		return totalDeals;
	}

	public void setTotalDeals(Integer totalDeals) {
		this.totalDeals = totalDeals;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public MNATopDealMakerTotalDTO getTopDealTotal() {
		return topDealTotal;
	}

	public void setTopDealTotal(MNATopDealMakerTotalDTO topDealTotal) {
		this.topDealTotal = topDealTotal;
	}

	public List<MNATopDealMakerDTO> getTopDealList() {
		return topDealList;
	}

	public void setTopDealList(List<MNATopDealMakerDTO> topDealList) {
		this.topDealList = topDealList;
	}

	public List<MNATopDealChartDTO> getTopDealChartData() {
		return topDealChartData;
	}

	public void setTopDealChartData(List<MNATopDealChartDTO> topDealChartData) {
		this.topDealChartData = topDealChartData;
	}
	

}
