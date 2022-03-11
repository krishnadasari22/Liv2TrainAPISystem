package com.televisory.capitalmarket.model;

public class CommodityDataDownload {
	
	String commodityName;
	String periodicity;
	
	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	@Override
	public String toString() {
		return "CommodityDataDownload [commodityName=" + commodityName
				+ ", periodicity=" + periodicity + "]";
	}

}
