package com.televisory.capitalmarket.model;

import java.util.List;

public class IcDataDownloadRequest {
	private IcStockRequestModel 	stockRequest;
	private List<IcStockRequestModel> 	companyRequest;
	private IcEconomyRequestModel 	economyRequest;
	private IcIndustryRequestModel 	industryRequest;
	private IcCommodityRequestModel 	commodityRequest;
	private IcEconomyRequestModel 	forexRequest;
	
	public IcStockRequestModel getStockRequest() {
		return stockRequest;
	}
	public void setStockRequest(IcStockRequestModel stockRequest) {
		this.stockRequest = stockRequest;
	}
	public List<IcStockRequestModel> getCompanyRequest() {
		return companyRequest;
	}
	public void setCompanyRequest(List<IcStockRequestModel> companyRequest) {
		this.companyRequest = companyRequest;
	}
	public IcIndustryRequestModel getIndustryRequest() {
		return industryRequest;
	}
	public void setIndustryRequest(IcIndustryRequestModel industryRequest) {
		this.industryRequest = industryRequest;
	}
	public IcEconomyRequestModel getEconomyRequest() {
		return economyRequest;
	}
	public void setEconomyRequest(IcEconomyRequestModel economyRequest) {
		this.economyRequest = economyRequest;
	}
	public IcCommodityRequestModel getCommodityRequest() {
		return commodityRequest;
	}
	public void setCommodityRequest(IcCommodityRequestModel commodityRequest) {
		this.commodityRequest = commodityRequest;
	}
	public IcEconomyRequestModel getForexRequest() {
		return forexRequest;
	}
	public void setForexRequest(IcEconomyRequestModel forexRequest) {
		this.forexRequest = forexRequest;
	}
	@Override
	public String toString() {
		return "IcDataDownloadRequest [stockRequest=" + stockRequest
				+ ", companyRequest=" + companyRequest + ", economyRequest="
				+ economyRequest + ", industryRequest=" + industryRequest
				+ ", commodityRequest=" + commodityRequest + ", forexRequest="
				+ forexRequest + "]";
	}
}
