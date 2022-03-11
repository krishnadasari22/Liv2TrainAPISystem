package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;

public class IcDataDownloadResponse {
	private List<IcStockResponseModel> icStockResponseData;
	private List<IcStockResponseModel> icCompanyResponseData;
	private List<IcEconomyResponseModel> icEconomyResponseData;
	private List<IndustryFinancialDataDTO> icIndustryResponseData;
	private IcCommodityRequestModel icCommodityResponseData;
	private List<CompanyDTO> icCompanyData;
	private List<IcEconomyResponseModel> icForexResponseData;
	
	public List<IcStockResponseModel> getIcStockResponseData() {
		return icStockResponseData;
	}
	public void setIcStockResponseData(
			List<IcStockResponseModel> icStockResponseData) {
		this.icStockResponseData = icStockResponseData;
	}
	public List<IcEconomyResponseModel> getIcEconomyResponseData() {
		return icEconomyResponseData;
	}
	public void setIcEconomyResponseData(
			List<IcEconomyResponseModel> icEconomyResponseData) {
		this.icEconomyResponseData = icEconomyResponseData;
	}
	public List<IndustryFinancialDataDTO> getIcIndustryResponseData() {
		return icIndustryResponseData;
	}
	public void setIcIndustryResponseData(
			List<IndustryFinancialDataDTO> icIndustryResponseData) {
		this.icIndustryResponseData = icIndustryResponseData;
	}
	public List<IcStockResponseModel> getIcCompanyResponseData() {
		return icCompanyResponseData;
	}
	public void setIcCompanyResponseData(
			List<IcStockResponseModel> icCompanyResponseData) {
		this.icCompanyResponseData = icCompanyResponseData;
	}
	public IcCommodityRequestModel getIcCommodityResponseData() {
		return icCommodityResponseData;
	}
	public void setIcCommodityResponseData(
			IcCommodityRequestModel icCommodityResponseData) {
		this.icCommodityResponseData = icCommodityResponseData;
	}
	public List<CompanyDTO> getIcCompanyData() {
		return icCompanyData;
	}
	public void setIcCompanyData(List<CompanyDTO> icCompanyData) {
		this.icCompanyData = icCompanyData;
	}
	public List<IcEconomyResponseModel> getIcForexResponseData() {
		return icForexResponseData;
	}
	public void setIcForexResponseData(
			List<IcEconomyResponseModel> icForexResponseData) {
		this.icForexResponseData = icForexResponseData;
	}
	@Override
	public String toString() {
		return "IcDataDownloadResponse [icStockResponseData="
				+ icStockResponseData + ", icCompanyResponseData="
				+ icCompanyResponseData + ", icEconomyResponseData="
				+ icEconomyResponseData + ", icIndustryResponseData="
				+ icIndustryResponseData + ", icCommodityResponseData="
				+ icCommodityResponseData + ", icCompanyData=" + icCompanyData
				+ ", icForexResponseData=" + icForexResponseData + "]";
	}
}
