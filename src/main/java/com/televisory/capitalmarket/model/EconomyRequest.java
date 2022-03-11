package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;

import io.swagger.annotations.ApiModelProperty;

public class EconomyRequest {

	@ApiModelProperty(notes = "Type of Economy Data { economyIndicator| economyCountry}", required =true)
	private String type;
	
	@ApiModelProperty(notes = "ID of Indicator/Country {IndicatorName | CountryName}", required =true)
	private String id;
	
	@ApiModelProperty(notes = "Additional Filters on dataType", required =false)
	private List<String> filterList;
	
	@ApiModelProperty(notes = "Required Currency", required =false)
	private String currency;
	
	@ApiModelProperty(notes = "periodicity of the data {DAILY | WEEKLY | MONTHLY | QUARTERLY | YEARLY}", required =true)
	private String periodicity;
	@ApiModelProperty(notes = "Extracted economy data list" , readOnly =true)
	private List<IndicatorHistoricalDataDTO> economyData;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<String> filterList) {
		this.filterList = filterList;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public List<IndicatorHistoricalDataDTO> getEconomyData() {
		return economyData;
	}

	public void setEconomyData(List<IndicatorHistoricalDataDTO> economyData) {
		this.economyData = economyData;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "EconomyRequest [type=" + type + ", id=" + id + ", filterList=" + filterList + ", currency=" + currency
				+ ", periodicity=" + periodicity + ", economyData=" + economyData + "]";
	}
}
