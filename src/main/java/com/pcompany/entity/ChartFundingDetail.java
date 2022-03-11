package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sym_v1_sym_entity")
public class ChartFundingDetail {

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "valuation_fx")
	private Double valuation;

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

	@Override
	public String toString() {
		return "ChartFundingDetails [code=" + code + ", name=" + name + ", valuation=" + valuation + "]";
	}

}
