package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "market_data_latest")
public class CommodityLatestData {
	
	@Id
	@Column(name = "tel_commodity_id")
	private Integer telCommodityId;

	@Column(name = "market_data_id")
	private Integer marketDataId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "ticker")
	private String ticker;
	
	@Column(name = "country")
	private String country;
	
	@Column(name="type")
	private String type;
	
	@Column(name = "tel_market_group")
	private String telMarketGroup;
	
	@Column(name = "definition")
	private String teleDescription;
	
	@Column(name = "display_order")
	private String displayOrder;
	
	@Column(name = "symbol")
	private String symbol;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "last")
	private Double last;
	
	@Column(name = "daily_change")
	private Double dailyChange;

	@Column(name = "daily_percentual_change")
	private Double dailyPercentualChange;

	@Column(name = "weekly_change")
	private Double weeklyChange;

	@Column(name = "weekly_percentual_change")
	private Double weeklyPercentualChange;

	@Column(name = "monthly_change")
	private Double monthlyChange;

	@Column(name = "monthly_percentual_change")
	private Double monthlyPercentualChange;

	@Column(name = "yearly_change")
	private Double yearlyChange;

	@Column(name = "yearly_percentual_change")
	private Double yearlyPercentualChange;

	@Column(name = "ytd_change")
	private Double ytdChange;

	@Column(name = "ytd_percentual_change")
	private Double ytdPercentualChange;
	
	@Column(name = "forecast1")
	private Double forecastQ1;
	
	@Column(name = "forecast2")
	private Double forecastQ2;
	
	@Column(name = "forecast3")
	private Double forecastQ3;
	
	@Column(name = "forecast4")
	private Double forecastQ4;
	
	@Column(name = "PerChangeQ1")
	private Double forecastQ1PerChange;
	
	@Column(name = "PerChangeQ2")
	private Double forecastQ2PerChange;
	
	@Column(name = "PerChangeQ3")
	private Double forecastQ3PerChange;
	
	@Column(name = "PerChangeQ4")
	private Double forecastQ4PerChange;

	@Column(name = "yesterday")
	private Double yesterday;

	@Column(name = "last_week")
	private Double lastWeek;

	@Column(name = "last_month")
	private Double lastMonth;

	@Column(name = "last_year")
	private Double lastYear;

	@Column(name = "start_year")
	private Double startYear;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "close")
	private Double close;
	
	@Column(name = "close_date")
	private Date closeDate;
	
	@Column(name = "frequency")
	private String frequency;
							
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "is_active")
	private Integer isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_modified_at")
	private Date lastModifiedAt;

	@Column(name = "last_modified_by")
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
		return "CommodityLatestData [telCommodityId=" + telCommodityId + ", marketDataId=" + marketDataId + ", name="
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
