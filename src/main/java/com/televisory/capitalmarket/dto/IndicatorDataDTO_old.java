package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;


public class IndicatorDataDTO_old implements Serializable {

	private static final long serialVersionUID = 1303981375250098350L;

	@Mapping("countryIndicator.indicatorsName.id")
	private int id;
	
	@Mapping("countryIndicator.id")
	private int countryIndicatorId;
	
	@Mapping("countryIndicator.indicatorsName.indicatorsName")
	private String name;
	
	@Mapping("countryIndicator.indicatorsName.shortName")
	private String shortName;
	
	@Mapping("countryIndicator.indicatorsName.dataType")
	private String dataType;
	
	@Mapping("countryIndicator.currency")
	private String currency;
	
	@Mapping("countryIndicator.unit")
	private String unit;
	
	@Mapping("countryIndicator.indicatorsName.chartFlag")
	private int chartFlag;
	
	private Double data;
	
	private Date period;
	
	@Mapping("countryIndicator.periodType")
	private String periodType;
	
	@Mapping("countryIndicator.tenure")
	private String tenure;
	
	@Mapping("countryIndicator.country.id")
	private int countryId;
	
	@Mapping("countryIndicator.country.countryName")
	private String countryName;
	
	private Double percentageChange;
	
	private String rating;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public int getCountryIndicatorId() {
		return countryIndicatorId;
	}

	public void setCountryIndicatorId(int countryIndicatorId) {
		this.countryIndicatorId = countryIndicatorId;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public Double getPercentageChange() {
		return percentageChange;
	}

	public void setPercentageChange(Double percentageChange) {
		this.percentageChange = percentageChange;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public int getChartFlag() {
		return chartFlag;
	}

	public void setChartFlag(int chartFlag) {
		this.chartFlag = chartFlag;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	@Override
	public String toString() {
		return "IndicatorDataDTO [id=" + id + ", countryIndicatorId=" + countryIndicatorId + ", name=" + name
				+ ", shortName=" + shortName + ", dataType=" + dataType + ", currency=" + currency + ", unit=" + unit
				+ ", chartFlag=" + chartFlag + ", data=" + data + ", period=" + period + ", periodType=" + periodType
				+ ", countryId=" + countryId + ", countryName=" + countryName + ", percentageChange=" + percentageChange
				+ ", rating=" + rating + "]";
	}
}
