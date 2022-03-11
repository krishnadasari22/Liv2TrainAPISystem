package com.televisory.capitalmarket.entities.economy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "televisory_indicator_reporting_frequency")
public class TelevisoryIndicatorReportingFrequency implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tid", nullable = false, unique = true)
	private int id;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "category")
	private String category;

	@Column(name = "frequency")
	private String frequency;

	@Column(name = "reported_frequency")
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
		return "TelevisoryIndicatorReportingFrequency [id=" + id
				+ ", countryName=" + countryName + ", category=" + category
				+ ", frequency=" + frequency + ", reportedFrequency="
				+ reportedFrequency + "]";
	}

	


}
