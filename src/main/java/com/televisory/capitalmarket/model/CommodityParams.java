package com.televisory.capitalmarket.model;

import java.util.List;

public class CommodityParams {
	String symbol;
	String ticker_name;
	List<String> commodityCheckedParams;
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTicker_name() {
		return ticker_name;
	}

	public void setTicker_name(String ticker_name) {
		this.ticker_name = ticker_name;
	}

	public List<String> getCommodityCheckedParams() {
		return commodityCheckedParams;
	}

	public void setCommodityCheckedParams(List<String> commodityCheckedParams) {
		this.commodityCheckedParams = commodityCheckedParams;
	}

	@Override
	public String toString() {
		return "CommodityParams [symbol=" + symbol + ", ticker_name="
				+ ticker_name + ", commodityCheckedParams="
				+ commodityCheckedParams + "]";
	}
	
}
