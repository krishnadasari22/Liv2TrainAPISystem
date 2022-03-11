package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.StockPriceDTO;

public class ReportCompanyProfile {
	
	//company meta data class
	private List<CompanyMetaData> companyDetails;
	private List<ReportRatiosData> profitabilityRatios ;
	private List<ReportRatiosData> liquidityRatios ;
	private List<ReportRatiosData> leverageRatios ;
	private List<ReportRatiosData> returnRatios ;
	private List<ReportRatiosData> valuationRatios ;
	private List<ReportRatiosData> financialData;
	private List<ReportPeerFinancialData> peerFinancialData;
	private List<ReportHistPerformance> histPerformance;
	private List<ReportHistPerformance> peerHistPerformance;
	private List<ReportHistPerformance> riskRatios;
	private List<ReportPerformanceData> qtPriPerformance ;
	private List<ReportPerformanceData> qtSecPerformance ;
	private List<ReportPerformanceData> yrPriPerformance ;
	private List<ReportPerformanceData> yrSecPerformance ;
	private List<ReportShareholding> shareholdingRatios;
	private List<StockPriceDTO> stockData; 
	private String peerPeriod;
	private String peerPeriodType;
	private Integer templateType;
	private Boolean peerFlag;
	private Boolean nmFlag;
	
	
	/*public List<Date> getPeriodList() {
		return periodList;
	}
	public void setPeriodList(List<Date> periodList) {
		this.periodList = periodList;
	}*/
	
	
	public List<ReportRatiosData> getLiquidityRatios() {
		return liquidityRatios;
	}
	public Boolean getNmFlag() {
		return nmFlag;
	}
	public void setNmFlag(Boolean nmFlag) {
		this.nmFlag = nmFlag;
	}
	public void setLiquidityRatios(List<ReportRatiosData> liquidityRatios) {
		this.liquidityRatios = liquidityRatios;
	}
	public List<ReportRatiosData> getLeverageRatios() {
		return leverageRatios;
	}
	public void setLeverageRatios(List<ReportRatiosData> leverageRatios) {
		this.leverageRatios = leverageRatios;
	}
	public List<ReportRatiosData> getReturnRatios() {
		return returnRatios;
	}
	public void setReturnRatios(List<ReportRatiosData> returnRatios) {
		this.returnRatios = returnRatios;
	}
	public List<ReportRatiosData> getValuationRatios() {
		return valuationRatios;
	}
	public void setValuationRatios(List<ReportRatiosData> valuationRatios) {
		this.valuationRatios = valuationRatios;
	}
	
	public List<ReportPeerFinancialData> getPeerFinancialData() {
		return peerFinancialData;
	}
	public void setPeerFinancialData(List<ReportPeerFinancialData> peerFinancialData) {
		this.peerFinancialData = peerFinancialData;
	}
	public List<ReportRatiosData> getFinancialData() {
		return financialData;
	}
	public void setFinancialData(List<ReportRatiosData> financialData) {
		this.financialData = financialData;
	}
	public List<ReportRatiosData> getProfitabilityRatios() {
		return profitabilityRatios;
	}
	public void setProfitabilityRatios(List<ReportRatiosData> profitabilityRatios) {
		this.profitabilityRatios = profitabilityRatios;
	}
	public List<CompanyMetaData> getCompanyDetails() {
		return companyDetails;
	}
	public void setCompanyDetails(List<CompanyMetaData> companyDetails) {
		this.companyDetails = companyDetails;
	}
	public List<ReportHistPerformance> getHistPerformance() {
		return histPerformance;
	}
	public void setHistPerformance(List<ReportHistPerformance> histPerformance) {
		this.histPerformance = histPerformance;
	}
	public List<ReportHistPerformance> getPeerHistPerformance() {
		return peerHistPerformance;
	}
	public void setPeerHistPerformance(List<ReportHistPerformance> peerHistPerformance) {
		this.peerHistPerformance = peerHistPerformance;
	}
	public List<StockPriceDTO> getStockData() {
		return stockData;
	}
	public void setStockData(List<StockPriceDTO> stockData) {
		this.stockData = stockData;
	}
	
	public List<ReportPerformanceData> getQtPriPerformance() {
		return qtPriPerformance;
	}
	public void setQtPriPerformance(List<ReportPerformanceData> qtPriPerformance) {
		this.qtPriPerformance = qtPriPerformance;
	}
	public List<ReportPerformanceData> getQtSecPerformance() {
		return qtSecPerformance;
	}
	public void setQtSecPerformance(List<ReportPerformanceData> qtSecPerformance) {
		this.qtSecPerformance = qtSecPerformance;
	}
	public List<ReportPerformanceData> getYrPriPerformance() {
		return yrPriPerformance;
	}
	public void setYrPriPerformance(List<ReportPerformanceData> yrPriPerformance) {
		this.yrPriPerformance = yrPriPerformance;
	}
	public List<ReportPerformanceData> getYrSecPerformance() {
		return yrSecPerformance;
	}
	public void setYrSecPerformance(List<ReportPerformanceData> yrSecPerformance) {
		this.yrSecPerformance = yrSecPerformance;
	}
	
	public List<ReportHistPerformance> getRiskRatios() {
		return riskRatios;
	}
	public void setRiskRatios(List<ReportHistPerformance> riskRatios) {
		this.riskRatios = riskRatios;
	}
	
	public String getPeerPeriod() {
		return peerPeriod;
	}
	public void setPeerPeriod(String peerPeriod) {
		this.peerPeriod = peerPeriod;
	}
	public List<ReportShareholding> getShareholdingRatios() {
		return shareholdingRatios;
	}
	public void setShareholdingRatios(List<ReportShareholding> shareholdingRatios) {
		this.shareholdingRatios = shareholdingRatios;
	}
	@Override
	public String toString() {
		return "ReportCompanyProfile [companyDetails=" + companyDetails + ", profitabilityRatios=" + profitabilityRatios
				+ ", liquidityRatios=" + liquidityRatios + ", leverageRatios=" + leverageRatios + ", returnRatios="
				+ returnRatios + ", valuationRatios=" + valuationRatios + ", financialData=" + financialData
				+ ", peerFinancialData=" + peerFinancialData + ", histPerformance=" + histPerformance
				+ ", peerHistPerformance=" + peerHistPerformance + ", riskRatios=" + riskRatios + ", qtPriPerformance="
				+ qtPriPerformance + ", qtSecPerformance=" + qtSecPerformance + ", yrPriPerformance=" + yrPriPerformance
				+ ", yrSecPerformance=" + yrSecPerformance + ", shareholdingRatios=" + shareholdingRatios
				+ ", stockData=" + stockData + ", peerPeriod=" + peerPeriod + "]";
	}
	public String getPeerPeriodType() {
		return peerPeriodType;
	}
	public void setPeerPeriodType(String peerPeriodType) {
		this.peerPeriodType = peerPeriodType;
	}
	public Integer getTemplateType() {
		return templateType;
	}
	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}
	public Boolean getPeerFlag() {
		return peerFlag;
	}
	public void setPeerFlag(Boolean peerFlag) {
		this.peerFlag = peerFlag;
	}
	
	
	
}