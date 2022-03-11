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
public class SegmentBusiness implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name = "fsym_id")
	private String fsymId;
	
	@Column(name = "date")
	private Date date;

	@Column(name = "ff_segment_type")
	private String ffSegmentType;

	@Column(name = "ff_segment_num")
	private Integer ffSegmentNum;

	@Column(name = "adjdate")
	private Date adjdate;

	@Column(name = "currency")
	private String currency;

	@Column(name = "label")
	private String label;

	@Column(name = "sales")
	private Double sales;

	@Column(name = "opinc")
	private Double opinc;
	
	@Column(name = "assets")
	private Double assets;

	@Column(name = "capex")
	private Double capex;
	
	@Column(name = "dep")
	private Double dep;
	
	@Column(name = "ff_sic_code")
	private String ffSicCode;
	
	@Column(name = "unit")
	private String unit;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFfSegmentType() {
		return ffSegmentType;
	}

	public void setFfSegmentType(String ffSegmentType) {
		this.ffSegmentType = ffSegmentType;
	}

	public Integer getFfSegmentNum() {
		return ffSegmentNum;
	}

	public void setFfSegmentNum(Integer ffSegmentNum) {
		this.ffSegmentNum = ffSegmentNum;
	}

	public Date getAdjdate() {
		return adjdate;
	}

	public void setAdjdate(Date adjdate) {
		this.adjdate = adjdate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public Double getOpinc() {
		return opinc;
	}

	public void setOpinc(Double opinc) {
		this.opinc = opinc;
	}

	public Double getAssets() {
		return assets;
	}

	public void setAssets(Double assets) {
		this.assets = assets;
	}

	public Double getCapex() {
		return capex;
	}

	public void setCapex(Double capex) {
		this.capex = capex;
	}

	public Double getDep() {
		return dep;
	}

	public void setDep(Double dep) {
		this.dep = dep;
	}

	public String getFfSicCode() {
		return ffSicCode;
	}

	public void setFfSicCode(String ffSicCode) {
		this.ffSicCode = ffSicCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "SegmentBusiness [id=" + id + ", fsymId=" + fsymId + ", date="
				+ date + ", ffSegmentType=" + ffSegmentType + ", ffSegmentNum="
				+ ffSegmentNum + ", adjdate=" + adjdate + ", currency="
				+ currency + ", label=" + label + ", sales=" + sales
				+ ", opinc=" + opinc + ", assets=" + assets + ", capex="
				+ capex + ", dep=" + dep + ", ffSicCode=" + ffSicCode
				+ ", unit=" + unit + "]";
	}

}
