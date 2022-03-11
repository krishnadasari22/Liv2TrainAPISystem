package com.televisory.capitalmarket.dto.economy;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommodityLatestDataDTO {
	
	private Integer telCommodityId;
	private Integer marketDataId;
	private String name;
	private String ticker;
	private String country;
	private String type;
	private String telMarketGroup;
	private String teleDescription;
	private String displayOrder;
	private String symbol;
	private Date date;
	private Double last;
	private Double dailyChange;
	private Double dailyPercentualChange;
	private Double weeklyChange;
	private Double weeklyPercentualChange;
	private Double monthlyChange;
	private Double monthlyPercentualChange;
	private Double yearlyChange;
	private Double yearlyPercentualChange;
	private Double ytdChange;
	private Double ytdPercentualChange;
	private Double forecastQ1;
	private Double forecastQ2;
	private Double forecastQ3;
	private Double forecastQ4;
	private Double forecastQ1PerChange;
	private Double forecastQ2PerChange;
	private Double forecastQ3PerChange;
	private Double forecastQ4PerChange;
	private Double yesterday;
	private Double lastWeek;
	private Double lastMonth;
	private Double lastYear;
	private Double startYear;
	private String unit;
	private Double close;
	private Date closeDate;
	private String frequency;
	private Date lastUpdate;
	
	@JsonIgnore
	private Integer isActive;
	@JsonIgnore
	private Date createdAt;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Date lastModifiedAt;
	@JsonIgnore
	private String lastModifiedBy;

	public Integer getTelCommodityId() {
		return telCommodityId;
	}

	public void setTelCommodityId(Integer telCommodityId) {
		this.telCommodityId = telCommodityId;
	}

	public Integer getMarketDataId() {
		return marketDataId;
	}

	public void setMarketDataId(Integer marketDataId) {
		this.marketDataId = marketDataId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTelMarketGroup() {
		return telMarketGroup;
	}

	public void setTelMarketGroup(String telMarketGroup) {
		this.telMarketGroup = telMarketGroup;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getLast() {
		return last;
	}

	public void setLast(Double last) {
		this.last = last;
	}

	public Double getDailyChange() {
		return dailyChange;
	}

	public void setDailyChange(Double dailyChange) {
		this.dailyChange = dailyChange;
	}

	public Double getDailyPercentualChange() {
		return dailyPercentualChange;
	}

	public void setDailyPercentualChange(Double dailyPercentualChange) {
		this.dailyPercentualChange = dailyPercentualChange;
	}

	public Double getWeeklyChange() {
		return weeklyChange;
	}

	public void setWeeklyChange(Double weeklyChange) {
		this.weeklyChange = weeklyChange;
	}

	public Double getWeeklyPercentualChange() {
		return weeklyPercentualChange;
	}

	public void setWeeklyPercentualChange(Double weeklyPercentualChange) {
		this.weeklyPercentualChange = weeklyPercentualChange;
	}

	public Double getMonthlyChange() {
		return monthlyChange;
	}

	public void setMonthlyChange(Double monthlyChange) {
		this.monthlyChange = monthlyChange;
	}

	public Double getMonthlyPercentualChange() {
		return monthlyPercentualChange;
	}

	public void setMonthlyPercentualChange(Double monthlyPercentualChange) {
		this.monthlyPercentualChange = monthlyPercentualChange;
	}

	public Double getYearlyChange() {
		return yearlyChange;
	}

	public void setYearlyChange(Double yearlyChange) {
		this.yearlyChange = yearlyChange;
	}

	public Double getYearlyPercentualChange() {
		return yearlyPercentualChange;
	}

	public void setYearlyPercentualChange(Double yearlyPercentualChange) {
		this.yearlyPercentualChange = yearlyPercentualChange;
	}

	public Double getYtdChange() {
		return ytdChange;
	}

	public void setYtdChange(Double ytdChange) {
		this.ytdChange = ytdChange;
	}

	public Double getYtdPercentualChange() {
		return ytdPercentualChange;
	}

	public void setYtdPercentualChange(Double ytdPercentualChange) {
		this.ytdPercentualChange = ytdPercentualChange;
	}
	
	public Double getForecastQ1() {
		return forecastQ1;
	}

	public void setForecastQ1(Double forecastQ1) {
		this.forecastQ1 = forecastQ1;
	}

	public Double getForecastQ2() {
		return forecastQ2;
	}

	public void setForecastQ2(Double forecastQ2) {
		this.forecastQ2 = forecastQ2;
	}

	public Double getForecastQ3() {
		return forecastQ3;
	}

	public void setForecastQ3(Double forecastQ3) {
		this.forecastQ3 = forecastQ3;
	}

	public Double getForecastQ4() {
		return forecastQ4;
	}

	public void setForecastQ4(Double forecastQ4) {
		this.forecastQ4 = forecastQ4;
	}

	public Double getForecastQ1PerChange() {
		return forecastQ1PerChange;
	}

	public void setForecastQ1PerChange(Double forecastQ1PerChange) {
		this.forecastQ1PerChange = forecastQ1PerChange;
	}

	public Double getForecastQ2PerChange() {
		return forecastQ2PerChange;
	}

	public void setForecastQ2PerChange(Double forecastQ2PerChange) {
		this.forecastQ2PerChange = forecastQ2PerChange;
	}

	public Double getForecastQ3PerChange() {
		return forecastQ3PerChange;
	}

	public void setForecastQ3PerChange(Double forecastQ3PerChange) {
		this.forecastQ3PerChange = forecastQ3PerChange;
	}

	public Double getForecastQ4PerChange() {
		return forecastQ4PerChange;
	}

	public void setForecastQ4PerChange(Double forecastQ4PerChange) {
		this.forecastQ4PerChange = forecastQ4PerChange;
	}

	public Double getYesterday() {
		return yesterday;
	}

	public void setYesterday(Double yesterday) {
		this.yesterday = yesterday;
	}

	public Double getLastWeek() {
		return lastWeek;
	}

	public void setLastWeek(Double lastWeek) {
		this.lastWeek = lastWeek;
	}

	public Double getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(Double lastMonth) {
		this.lastMonth = lastMonth;
	}

	public Double getLastYear() {
		return lastYear;
	}

	public void setLastYear(Double lastYear) {
		this.lastYear = lastYear;
	}

	public Double getStartYear() {
		return startYear;
	}

	public void setStartYear(Double startYear) {
		this.startYear = startYear;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	public String getTeleDescription() {
		return teleDescription;
	}

	public void setTeleDescription(String teleDescription) {
		this.teleDescription = teleDescription;
	}

	@Override
	public String toString() {
		return "CommodityLatestDataDTO [telCommodityId=" + telCommodityId + ", marketDataId=" + marketDataId + ", name="
				+ name + ", ticker=" + ticker + ", country=" + country + ", type=" + type + ", telMarketGroup="
				+ telMarketGroup + ", teleDescription=" + teleDescription + ", displayOrder=" + displayOrder
				+ ", symbol=" + symbol + ", date=" + date + ", last=" + last + ", dailyChange=" + dailyChange
				+ ", dailyPercentualChange=" + dailyPercentualChange + ", weeklyChange=" + weeklyChange
				+ ", weeklyPercentualChange=" + weeklyPercentualChange + ", monthlyChange=" + monthlyChange
				+ ", monthlyPercentualChange=" + monthlyPercentualChange + ", yearlyChange=" + yearlyChange
				+ ", yearlyPercentualChange=" + yearlyPercentualChange + ", ytdChange=" + ytdChange
				+ ", ytdPercentualChange=" + ytdPercentualChange + ", forecastQ1=" + forecastQ1 + ", forecastQ2="
				+ forecastQ2 + ", forecastQ3=" + forecastQ3 + ", forecastQ4=" + forecastQ4 + ", forecastQ1PerChange="
				+ forecastQ1PerChange + ", forecastQ2PerChange=" + forecastQ2PerChange + ", forecastQ3PerChange="
				+ forecastQ3PerChange + ", forecastQ4PerChange=" + forecastQ4PerChange + ", yesterday=" + yesterday
				+ ", lastWeek=" + lastWeek + ", lastMonth=" + lastMonth + ", lastYear=" + lastYear + ", startYear="
				+ startYear + ", unit=" + unit + ", close=" + close + ", closeDate=" + closeDate + ", frequency="
				+ frequency + ", lastUpdate=" + lastUpdate + ", isActive=" + isActive + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}

}
