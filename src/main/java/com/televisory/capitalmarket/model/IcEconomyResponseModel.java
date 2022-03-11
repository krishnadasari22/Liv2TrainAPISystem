package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.IndicatorDataDTO_old;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.entities.economy.ExchangeRatesComparison;

public class IcEconomyResponseModel {

	private String countryIsoCode;
	private String dataType;
	private List<IndicatorHistoricalDataDTO> indicatorsDataList;
	private List<List<ExchangeRatesComparison>> exchangeRate;
	
	public String getCountryIsoCode() {
		return countryIsoCode;
	}
	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}
	public List<IndicatorHistoricalDataDTO> getIndicatorsDataList() {
		return indicatorsDataList;
	}
	public void setIndicatorsDataList(List<IndicatorHistoricalDataDTO> indicatorsDataList) {
		this.indicatorsDataList = indicatorsDataList;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public List<List<ExchangeRatesComparison>> getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(List<List<ExchangeRatesComparison>> exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	@Override
	public String toString() {
		return "IcEconomyResponseModel [countryIsoCode=" + countryIsoCode + ", dataType=" + dataType + ", indicatorsDataList="
				+ indicatorsDataList + ", exchangeRate=" + exchangeRate + "]";
	}
}
