package com.pcompany.dto;

import java.util.Date;


public class OwnershipEmployeeJobDTO {
	
	private Integer id;
	
	private String company;
	
	private String title;
	
	private String period;
	
	private Integer experience;
	
	private String name;
	
	private String noOfShares;
	
	private String asOnDate;
	
	private Double percentHoldings;
	
	private String terminationDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNoOfShares() {
		return noOfShares;
	}

	public void setNoOfShares(String noOfShares) {
		this.noOfShares = noOfShares;
	}

	public String getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(String asOnDate) {
		this.asOnDate = asOnDate;
	}
	
	public Double getPercentHoldings() {
		return percentHoldings;
	}

	public void setPercentHoldings(Double percentHoldings) {
		this.percentHoldings = percentHoldings;
	}

	public String getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}

	@Override
	public String toString() {
		return "OwnershipEmployeeJobDTO [id=" + id + ", company=" + company
				+ ", title=" + title + ", period=" + period + ", experience="
				+ experience + ", name=" + name + ", noOfShares=" + noOfShares
				+ ", asOnDate=" + asOnDate + ", percentHoldings="
				+ percentHoldings + ", terminationDate=" + terminationDate
				+ "]";
	}
}
