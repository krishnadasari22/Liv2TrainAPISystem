package com.pcompany.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_ind_act_af")
public class SegmentIndAct implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "fsym_id", nullable = false)
	private String fsymId;
	
	@Column(name = "fe_item")
	private String feItem;

	@Column(name = "fe_fp_end")
	private Date feFpEnd;

	@Column(name = "currency")
	private String currency;

	@Column(name = "adjdate")
	private Date adjdate;

	@Column(name = "report_date")
	private Date reportDate;

	@Column(name = "publication_date")
	private double publicationDate;

	@Column(name = "actual_value")
	private double actualValue;
	
	@Column(name = "actual_flag_code")
	private double actualFlagCode;

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

	public Date getFeFpEnd() {
		return feFpEnd;
	}

	public void setFeFpEnd(Date feFpEnd) {
		this.feFpEnd = feFpEnd;
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

	public double getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(double publicationDate) {
		this.publicationDate = publicationDate;
	}

	public double getActualValue() {
		return actualValue;
	}

	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}

	public double getActualFlagCode() {
		return actualFlagCode;
	}

	public void setActualFlagCode(double actualFlagCode) {
		this.actualFlagCode = actualFlagCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SegmentIndAct [fsymId=" + fsymId + ", feItem=" + feItem
				+ ", feFpEnd=" + feFpEnd + ", currency=" + currency
				+ ", adjdate=" + adjdate + ", reportDate=" + reportDate
				+ ", publicationDate=" + publicationDate + ", actualValue="
				+ actualValue + ", actualFlagCode=" + actualFlagCode + "]";
	}
	
}
