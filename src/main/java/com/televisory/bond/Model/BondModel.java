package com.televisory.bond.Model;

import java.util.HashMap;


// Using the builder design pattern in here
public class BondModel {

	HashMap<String, String> advanceSearchFilters;
	
	HashMap<String, Object> chartData;
	
	HashMap<String, Object> comparable;
	
	HashMap<String, Object> summary;
	
	HashMap<String, Object> yield;
	
	HashMap<String, Object> duration;
	
	public BondModel() {
	}

	public BondModel setAdvanceSearch(HashMap<String, String> advanceSearchFilters){
		this.advanceSearchFilters = advanceSearchFilters;
		return this;
	}
	
	public BondModel setChartData(HashMap<String, Object> chartData){
		this.chartData = chartData;
		return this;
	}
	
	public BondModel setYield(HashMap<String, Object> summary){
		this.summary = summary;
		return this;
	}
	
	public BondModel setDuration(HashMap<String, Object> duration){
		this.duration = duration;
		return this;
	}
	
	public BondModel setComparable(HashMap<String, Object> comparable){
		this.comparable = comparable;
		return this;
	}

	public HashMap<String, String> getAdvanceSearchFilters() {
		return advanceSearchFilters;
	}

	public void setAdvanceSearchFilters(HashMap<String, String> advanceSearchFilters) {
		this.advanceSearchFilters = advanceSearchFilters;
	}

	public HashMap<String, Object> getSummary() {
		return summary;
	}

	public void setSummary(HashMap<String, Object> summary) {
		this.summary = summary;
	}

	public HashMap<String, Object> getChartData() {
		return chartData;
	}

	public HashMap<String, Object> getComparable() {
		return comparable;
	}

	public HashMap<String, Object> getYield() {
		return yield;
	}

	public HashMap<String, Object> getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return "BondModel [advanceSearchFilters=" + advanceSearchFilters
				+ ", chartData=" + chartData + ", comparable=" + comparable
				+ ", summary=" + summary + ", yield=" + yield + ", duration="
				+ duration + "]";
	}
	
}
