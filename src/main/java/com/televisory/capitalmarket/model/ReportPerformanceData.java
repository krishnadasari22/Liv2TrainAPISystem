package com.televisory.capitalmarket.model;


public class ReportPerformanceData {
	
	private String series;
	private Double data;
	private String period; 
	
	public ReportPerformanceData() {
	}
	public ReportPerformanceData(String series, Double data, String period) {
		this.series = series;
		this.data = data;
		this.period = period;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public Double getData() {
		return data;
	}
	public void setData(Double data) {
		this.data = data;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	@Override
	public String toString() {
		return "ReportPerformanceData [series=" + series + ", data=" + data + ", period=" + period + "]";
	}
		
	
	
}
