package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;

import io.swagger.annotations.ApiModelProperty;

public class CommodityRequest {


	@ApiModelProperty(notes = "Commodity List whose data to be extracted", required =false)
	private List<String> commodityList;

	@ApiModelProperty(notes = "Commodity List whose data to be extracted", required =false)
	private List<CommodityParams> commodityParams;

	@ApiModelProperty(notes = "Required Currency", required =false)
	private String currency;

	@ApiModelProperty(notes = "Extracted commodity data list" , readOnly =true)
	private List<CommodityHistoricalDataDTO> commodityData;

	@ApiModelProperty(notes = "periodicity of the data {DAILY | WEEKLY | MONTHLY | QUARTERLY | YEARLY}", required =false)
	private String periodicity;

	@ApiModelProperty(notes = "Request Type", required =false)
	private String type;

	public List<String> getCommodityList() {
		return commodityList;
	}

	public void setCommodityList(List<String> commodityList) {
		this.commodityList = commodityList;
	}

	public List<CommodityParams> getCommodityParams() {
		return commodityParams;
	}

	public void setCommodityParams(List<CommodityParams> commodityParams) {
		this.commodityParams = commodityParams;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<CommodityHistoricalDataDTO> getCommodityData() {
		return commodityData;
	}

	public void setCommodityData(List<CommodityHistoricalDataDTO> commodityData) {
		this.commodityData = commodityData;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CommodityRequest [commodityList=" + commodityList
				+ ", commodityParams=" + commodityParams + ", currency="
				+ currency + ", commodityData=" + commodityData
				+ ", periodicity=" + periodicity + ", type=" + type + "]";
	}




}
