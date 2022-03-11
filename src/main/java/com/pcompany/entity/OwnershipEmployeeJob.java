package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ff_v3_ff_od_jobs")
public class OwnershipEmployeeJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="company")
	private String company;
	
	@Column(name="title")
	private String title;
	
	@Column(name = "period")
	private String period;
	
	@Column(name="experience")
	private Integer experience;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "no_of_shares")
	private String noOfShares;
	
	@Column(name = "as_on_date")
	private String asOnDate;
	
	@Column(name = "percent_holdings")
	private Double percentHoldings;
	
	@Column(name = "termination_date")
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
		return "OwnershipEmployeeJob [id=" + id + ", company=" + company
				+ ", title=" + title + ", period=" + period + ", experience="
				+ experience + ", name=" + name + ", noOfShares=" + noOfShares
				+ ", asOnDate=" + asOnDate + ", percentHoldings="
				+ percentHoldings + ", terminationDate=" + terminationDate
				+ "]";
	}
}
