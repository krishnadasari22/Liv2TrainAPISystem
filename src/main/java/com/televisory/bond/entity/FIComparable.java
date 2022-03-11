package com.televisory.bond.entity;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fi_data_latest")
public class FIComparable {

	@Id
	@Column(name = "isin_number")
	private String isinNumber;

	@Column(name = "as_of_date")
	private Date asOfDate;

	@Column(name = "maturity_date")
	private Date maturityDate;

	@Column(name = "yield_to_maturity")
	private Double yieldToMaturity;

	@Column(name = "price")
	private Double price;

	@Column(name = "modified_duration")
	private Double modifiedDuration;

	@Column(name = "description")
	private String description;

	@Column(name = "industry_lvl_1_desc")
	private String industryLvl1Desc;
	
	@Column(name = "maturity_type")
	private String maturityType;
	
	public String getIsinNumber() {
		return isinNumber;
	}

	public void setIsinNumber(String isinNumber) {
		this.isinNumber = isinNumber;
	}

	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Double getYieldToMaturity() {
		return yieldToMaturity;
	}

	public void setYieldToMaturity(Double yieldToMaturity) {
		this.yieldToMaturity = yieldToMaturity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getModifiedDuration() {
		return modifiedDuration;
	}

	public void setModifiedDuration(Double modifiedDuration) {
		this.modifiedDuration = modifiedDuration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIndustryLvl1Desc() {
		return industryLvl1Desc;
	}

	public void setIndustryLvl1Desc(String industryLvl1Desc) {
		this.industryLvl1Desc = industryLvl1Desc;
	}

	public String getMaturityType() {
		return maturityType;
	}

	public void setMaturityType(String maturityType) {
		this.maturityType = maturityType;
	}

	@Override
	public String toString() {
		return "FIComparable [isinNumber=" + isinNumber + ", asOfDate=" + asOfDate + ", maturityDate=" + maturityDate
				+ ", yieldToMaturity=" + yieldToMaturity + ", price=" + price + ", modifiedDuration=" + modifiedDuration
				+ ", description=" + description + ", industryLvl1Desc=" + industryLvl1Desc + ", maturityType="
				+ maturityType + "]";
	}	
	
}
