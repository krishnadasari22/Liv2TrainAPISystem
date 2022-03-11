package com.televisory.capitalmarket.model;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class representing of download request payload")
public class DownloadRequest {

	@ApiModelProperty(notes = "Data Start Date", required =true)
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	
	@ApiModelProperty(notes = "Data End Date", required =true)
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	
	private List<EquityRequest> equity;
	private List<EconomyRequest> economy;
	
	private CommodityRequest commodity;

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

	public List<EquityRequest> getEquity() {
		return equity;
	}

	public void setEquity(List<EquityRequest> equity) {
		this.equity = equity;
	}

	public List<EconomyRequest> getEconomy() {
		return economy;
	}

	public void setEconomy(List<EconomyRequest> economy) {
		this.economy = economy;
	}

	public CommodityRequest getCommodity() {
		return commodity;
	}

	public void setCommodity(CommodityRequest commodity) {
		this.commodity = commodity;
	}

	@Override
	public String toString() {
		return "DownloadRequest [startDate=" + startDate + ", endDate="
				+ endDate + ", equity=" + equity + ", economy=" + economy
				+ ", commodity=" + commodity + "]";
	}
	
	

	
}