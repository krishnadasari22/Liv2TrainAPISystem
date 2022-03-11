package com.televisory.bond.dto;


import java.sql.Date;

public class FIComparableDTO {

	private String isinNumber;

	private Date asOfDate;

	private Date maturityDate;

	private Double price;
	
	private Double yieldToMaturity;

	private Double modifiedDuration;

	private String description;

	private String industryLvl1Desc;
	
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
		return "FIComparableDTO [isinNumber=" + isinNumber + ", asOfDate=" + asOfDate + ", maturityDate=" + maturityDate
				+ ", price=" + price + ", yieldToMaturity=" + yieldToMaturity + ", modifiedDuration=" + modifiedDuration
				+ ", description=" + description + ", industryLvl1Desc=" + industryLvl1Desc + ", maturityType="
				+ maturityType + "]";
	}	
	
}
