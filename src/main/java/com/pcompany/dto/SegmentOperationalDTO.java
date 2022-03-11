package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class SegmentOperationalDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*private Integer id;*/
	
	private String fsymId;
	
	private String feItem;
	
	private String name;
	
	private Date feFpEndte;

	private String currency;
	
	private Date adjdate;
	
	private Date reportDate;
	
	private Date publicationDate;

	private Double actualValue;

	private Double actualFlagCode	;

	public String getFsymId() {
		return fsymId;
	}

	public void setFsymId(String fsymId) {
		this.fsymId = fsymId;
	}

	public String getFeItem() {
		return feItem;
	}

	public void setFeItem(String feItem) {
		this.feItem = feItem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFeFpEndte() {
		return feFpEndte;
	}

	public void setFeFpEndte(Date feFpEndte) {
		this.feFpEndte = feFpEndte;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getAdjdate() {
		return adjdate;
	}

	public void setAdjdate(Date adjdate) {
		this.adjdate = adjdate;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Double getActualValue() {
		return actualValue;
	}

	public void setActualValue(Double actualValue) {
		this.actualValue = actualValue;
	}

	public Double getActualFlagCode() {
		return actualFlagCode;
	}

	public void setActualFlagCode(Double actualFlagCode) {
		this.actualFlagCode = actualFlagCode;
	}

	@Override
	public String toString() {
		return "SegmentOperationalDTO [fsymId=" + fsymId + ", feItem=" + feItem
				+ ", name=" + name + ", feFpEndte=" + feFpEndte + ", currency="
				+ currency + ", adjdate=" + adjdate + ", reportDate="
				+ reportDate + ", publicationDate=" + publicationDate
				+ ", actualValue=" + actualValue + ", actualFlagCode="
				+ actualFlagCode + "]";
	}

}
