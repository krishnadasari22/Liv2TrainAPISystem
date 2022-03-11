package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "indicator_data_forecast")
public class IndicatorDataForecast {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "indicator_data_forecast_id", nullable = false, unique = true)
	private Integer indicator_data_forecast_id;
	
	@Column(name="historical_data_symbol")
	private String historical_data_symbol;
	
	@Column(name="category")
	private String category;
	
	@Column(name=" 	forecast_value_1q")
	private Double forecastQ1;
	
	@Column(name=" 	forecast_value_2q")
	private Double forecastQ2;
	
	@Column(name=" 	forecast_value_3q")
	private Double forecastQ3;
	
	@Column(name=" 	forecast_value_4q")
	private Double forecastQ4;
	
	@Column(name="year_end")
	private Double forecastY1;
	
	@Column(name="year_end2")
	private Double forecastY2;
	
	@Column(name="year_end3")
	private Double forecastY3;
	
	@Column(name="frequency")
	private String periodType;
	
	@Column(name="1q_date")
	private Date q1Date;
	
	@Column(name="2q_date")
	private Date q2Date;
	
	@Column(name="3q_date")
	private Date q3Date;
	
	@Column(name="4q_date")
	private Date q4Date;
	

	public Integer getIndicator_data_forecast_id() {
		return indicator_data_forecast_id;
	}

	public void setIndicator_data_forecast_id(Integer indicator_data_forecast_id) {
		this.indicator_data_forecast_id = indicator_data_forecast_id;
	}

	public String getHistorical_data_symbol() {
		return historical_data_symbol;
	}

	public void setHistorical_data_symbol(String historical_data_symbol) {
		this.historical_data_symbol = historical_data_symbol;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Double getForecastY1() {
		return forecastY1;
	}

	public void setForecastY1(Double forecastY1) {
		this.forecastY1 = forecastY1;
	}

	public Double getForecastY2() {
		return forecastY2;
	}

	public void setForecastY2(Double forecastY2) {
		this.forecastY2 = forecastY2;
	}

	public Double getForecastY3() {
		return forecastY3;
	}

	public void setForecastY3(Double forecastY3) {
		this.forecastY3 = forecastY3;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	
	public Date getQ1Date() {
		return q1Date;
	}

	public void setQ1Date(Date q1Date) {
		this.q1Date = q1Date;
	}

	public Date getQ2Date() {
		return q2Date;
	}

	public void setQ2Date(Date q2Date) {
		this.q2Date = q2Date;
	}

	public Date getQ3Date() {
		return q3Date;
	}

	public void setQ3Date(Date q3Date) {
		this.q3Date = q3Date;
	}

	public Date getQ4Date() {
		return q4Date;
	}

	public void setQ4Date(Date q4Date) {
		this.q4Date = q4Date;
	}

	@Override
	public String toString() {
		return "IndicatorDataForecast [indicator_data_forecast_id=" + indicator_data_forecast_id
				+ ", historical_data_symbol=" + historical_data_symbol + ", category=" + category + ", forecastQ1="
				+ forecastQ1 + ", forecastQ2=" + forecastQ2 + ", forecastQ3=" + forecastQ3 + ", forecastQ4="
				+ forecastQ4 + ", forecastY1=" + forecastY1 + ", forecastY2=" + forecastY2 + ", forecastY3="
				+ forecastY3 + ", periodType=" + periodType + "]";
	}

	
}
