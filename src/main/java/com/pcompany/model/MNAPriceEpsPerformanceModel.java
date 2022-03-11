package com.pcompany.model;

import java.util.Date;
import java.util.List;

import com.privatecompany.dto.CompanyEpsDTO;
import com.privatecompany.dto.DealHistoryDTO;
import com.televisory.capitalmarket.dto.CompanyStockPriceResponseDTO;

public class MNAPriceEpsPerformanceModel {

	private String companyId;
	private String entityId;
	private Date startDate;
	private Date endDate;
	private String epsPeriodicity;
	private String currency;
	private List<CompanyStockPriceResponseDTO> stockPriceList;
	private List<CompanyEpsDTO> epsList;
	private List<DealHistoryDTO> dealHistoryList;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public List<CompanyEpsDTO> getEpsList() {
		return epsList;
	}

	public void setEpsList(List<CompanyEpsDTO> epsList) {
		this.epsList = epsList;
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

	public String getEpsPeriodicity() {
		return epsPeriodicity;
	}

	public void setEpsPeriodicity(String epsPeriodicity) {
		this.epsPeriodicity = epsPeriodicity;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<CompanyStockPriceResponseDTO> getStockPriceList() {
		return stockPriceList;
	}

	public void setStockPriceList(List<CompanyStockPriceResponseDTO> stockPriceList) {
		this.stockPriceList = stockPriceList;
	}

	public List<DealHistoryDTO> getDealHistoryList() {
		return dealHistoryList;
	}

	public void setDealHistoryList(List<DealHistoryDTO> dealHistoryList) {
		this.dealHistoryList = dealHistoryList;
	}

	@Override
	public String toString() {
		return "MNAPriceEpsPerformanceModel [companyId=" + companyId + ", entityId=" + entityId + ", startDate="
				+ startDate + ", endDate=" + endDate + ", epsPeriodicity=" + epsPeriodicity + ", currency=" + currency
				+ ", stockPriceList=" + stockPriceList + ", epsList=" + epsList + ", dealHistoryList=" + dealHistoryList
				+ "]";
	}

}
