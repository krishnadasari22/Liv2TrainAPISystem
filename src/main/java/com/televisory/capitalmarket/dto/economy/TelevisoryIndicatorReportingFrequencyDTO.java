package com.televisory.capitalmarket.dto.economy;

import java.io.Serializable;

public class TelevisoryIndicatorReportingFrequencyDTO implements Serializable{

	private static final long serialVersionUID = 7697833646884621204L;

	private int id;
	private String countryName;
	private String category;
	private String frequency;
	private String reportedFrequency;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCountryName() {
		return countryName;
	}


	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getFrequency() {
		return frequency;
	}


	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}


	public String getReportedFrequency() {
		return reportedFrequency;
	}


	public void setReportedFrequency(String reportedFrequency) {
		this.reportedFrequency = reportedFrequency;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "TelevisoryIndicatorReportingFrequencyDTO [id=" + id
				+ ", countryName=" + countryName + ", category=" + category
				+ ", frequency=" + frequency + ", reportedFrequency="
				+ reportedFrequency + "]";
	}
	


}
