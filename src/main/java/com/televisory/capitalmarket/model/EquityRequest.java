package com.televisory.capitalmarket.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;

import io.swagger.annotations.ApiModelProperty;

public class EquityRequest {

	@ApiModelProperty(notes = "Name of exchange", required =true)
	private String exchange;

	@ApiModelProperty(notes = "Entity Type {private | public}", required =true)
	private String entityType;
	
	@ApiModelProperty(notes = "Type of Data {company | index}", required =true)
	private String type;
	
	@ApiModelProperty(notes = "Id of company or Index", required =true)
	private Object code;
	
	@ApiModelProperty(notes = "Name of company or Index", required =true)
	private String name;
	
	@ApiModelProperty(notes = "Data type [stockPrice | balanceSheet | pnl | etc...]", required =true)
	private String dataType;
	
	@ApiModelProperty(notes = "Additional Filters on dataType", required =false)
	private List<String> filterList;

	@ApiModelProperty(notes = "periodicity of the data {DAILY | WEEKLY | MONTHLY | QUARTERLY | YEARLY}", required =true)
	private String periodicity;
	
	@ApiModelProperty(notes = "Extracted stock price data list" , readOnly =true)
	private List<StockPriceDTO> stockPriceDTOs;
	
	@ApiModelProperty(notes = "Extracted financial data list" , readOnly =true)
	private List<CompanyFinancialMINDTO> companyFinancialDTOs;
	
	@ApiModelProperty(notes = "Extracted Beta data" , readOnly =true)
	private List<BetaData> betaDatas;
	
	@ApiModelProperty(notes = "Required Currency", required =false)
	private String currency;
	
	@ApiModelProperty(notes = "Restated or non-restated numbers", required =false)
	private Boolean restated;
	
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Object getCode() {
		return code;
	}
	public void setCode(Object code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = null;
		try {
			this.name = URLDecoder.decode(name, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public List<String> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<String> filterList) {
		this.filterList = filterList;
	}
	
	public String getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public List<StockPriceDTO> getStockPriceDTOs() {
		return stockPriceDTOs;
	}
	public void setStockPriceDTOs(List<StockPriceDTO> stockPriceDTOs) {
		this.stockPriceDTOs = stockPriceDTOs;
	}
	public List<CompanyFinancialMINDTO> getCompanyFinancialDTOs() {
		return companyFinancialDTOs;
	}
	public void setCompanyFinancialDTOs(List<CompanyFinancialMINDTO> companyFinancialDTOs) {
		this.companyFinancialDTOs = companyFinancialDTOs;
	}
	public List<BetaData> getBetaDatas() {
		return betaDatas;
	}
	public void setBetaDatas(List<BetaData> betaDatas) {
		this.betaDatas = betaDatas;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Boolean getRestated() {
		return restated;
	}
	public void setRestated(Boolean restated) {
		this.restated = restated;
	}
	@Override
	public String toString() {
		return "EquityRequest [exchange=" + exchange + ", type=" + type + ", code=" + code + ", name=" + name
				+ ", dataType=" + dataType + ", filterList=" + filterList + ", periodicity=" + periodicity
				+ ", stockPriceDTOs=" + stockPriceDTOs + ", companyFinancialDTOs=" + companyFinancialDTOs
				+ ", betaDatas=" + betaDatas + ", currency=" + currency + ", restated=" + restated + "]";
	}
	
}
