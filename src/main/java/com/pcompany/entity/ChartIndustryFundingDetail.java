package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sym_v1_sym_entity")
public class ChartIndustryFundingDetail {

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "valuation_fx")
	private Double valuation;

	@Column(name = "sector")
	private String sector;

	@Column(name = "sector_code")
	private String sectorCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValuation() {
		return valuation;
	}

	public void setValuation(Double valuation) {
		this.valuation = valuation;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}

	@Override
	public String toString() {
		return "ChartIndustryFundingDetail [code=" + code + ", name=" + name + ", valuation=" + valuation + ", sector="
				+ sector + ", sectorCode=" + sectorCode + "]";
	}

}
