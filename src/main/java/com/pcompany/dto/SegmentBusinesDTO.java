package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

public class SegmentBusinesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String fsymId;
	private Date date;
	private String ffSegmentType;
	private Integer ffSegmentNum;
	private Date adjdate;
	private String currency;
	private String label;
	private Double sales;
	private Double opinc;
	private Double assets;
	private Double capex;
	private Double dep;
	private String ffSicCode;
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
		return "SegmentBusinesDTO [id=" + id + ", fsymId=" + fsymId + ", date="
				+ date + ", ffSegmentType=" + ffSegmentType + ", ffSegmentNum="
				+ ffSegmentNum + ", adjdate=" + adjdate + ", currency="
				+ currency + ", label=" + label + ", sales=" + sales
				+ ", opinc=" + opinc + ", assets=" + assets + ", capex="
				+ capex + ", dep=" + dep + ", ffSicCode=" + ffSicCode
				+ ", unit=" + unit + "]";
	}
	
	
}
