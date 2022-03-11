package com.pcompany.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Null;

@Entity
@Table(name = "ff_v3_ff_segbus_af")
public class SegmentOperational implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name = "fsym_id")
	private String fsymId;
	
	@Column(name = "fe_item")
	private String feItem;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "fe_fp_end")
	private Date feFpEndte;

	@Column(name = "currency")
	private String currency;
	
	@Column(name = "adjdate")
	private Date adjdate;
	
	@Column(name = "report_date")
	private Date reportDate;
	
	@Column(name = "publication_date")
	private Date publicationDate;

	@Column(name = "actual_value")
	private Double actualValue;

	@Column(name = "actual_flag_code")
	private Double actualFlagCode	;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
		return "SegmentOperational [id=" + id + ", fsymId=" + fsymId
				+ ", feItem=" + feItem + ", name=" + name + ", feFpEndte="
				+ feFpEndte + ", currency=" + currency + ", adjdate=" + adjdate
				+ ", reportDate=" + reportDate + ", publicationDate="
				+ publicationDate + ", actualValue=" + actualValue
				+ ", actualFlagCode=" + actualFlagCode + "]";
	}

}
