package com.pcompany.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ref_v2_fe_item")
public class SegmentFeItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "fe_item", nullable = false)
	private String feItem;
	
	@Column(name = "currind")
	private int currInd;

	@Column(name = "splitind")
	private String splitInd;

	@Column(name = "unitfactor")
	private double unitFactor;

	@Column(name = "factcode")
	private String factCode;

	@Column(name = "hasquarter")
	private int hasQuarter;

	@Column(name = "hasfiscalyear")
	private int hasFiscalYear;

	@Column(name = "hasactual")
	private int hasActual;

	@Column(name = "hasactualonly")
	private int hasActualOnly;
	
	@Column(name = "name")
	private String name;

	@Column(name = "fe_ind")
	private String feInd;
	
	@Column(name = "units")
	private String units;

	public String getFeItem() {
		return feItem;
	}

	public void setFeItem(String feItem) {
		this.feItem = feItem;
	}

	public int getCurrInd() {
		return currInd;
	}

	public void setCurrInd(int currInd) {
		this.currInd = currInd;
	}

	public String getSplitInd() {
		return splitInd;
	}

	public void setSplitInd(String splitInd) {
		this.splitInd = splitInd;
	}

	public double getUnitFactor() {
		return unitFactor;
	}

	public void setUnitFactor(double unitFactor) {
		this.unitFactor = unitFactor;
	}

	public String getFactCode() {
		return factCode;
	}

	public void setFactCode(String factCode) {
		this.factCode = factCode;
	}

	public int getHasQuarter() {
		return hasQuarter;
	}

	public void setHasQuarter(int hasQuarter) {
		this.hasQuarter = hasQuarter;
	}

	public int getHasFiscalYear() {
		return hasFiscalYear;
	}

	public void setHasFiscalYear(int hasFiscalYear) {
		this.hasFiscalYear = hasFiscalYear;
	}

	public int getHasActual() {
		return hasActual;
	}

	public void setHasActual(int hasActual) {
		this.hasActual = hasActual;
	}

	public int getHasActualOnly() {
		return hasActualOnly;
	}

	public void setHasActualOnly(int hasActualOnly) {
		this.hasActualOnly = hasActualOnly;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFeInd() {
		return feInd;
	}

	public void setFeInd(String feInd) {
		this.feInd = feInd;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SegmentFeItem [feItem=" + feItem + ", currInd=" + currInd
				+ ", splitInd=" + splitInd + ", unitFactor=" + unitFactor
				+ ", factCode=" + factCode + ", hasQuarter=" + hasQuarter
				+ ", hasFiscalYear=" + hasFiscalYear + ", hasActual="
				+ hasActual + ", hasActualOnly=" + hasActualOnly + ", name="
				+ name + ", feInd=" + feInd + ", units=" + units + "]";
	}
	
}
