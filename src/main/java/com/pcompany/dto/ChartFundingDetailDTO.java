package com.pcompany.dto;

public class ChartFundingDetailDTO {

	private String code;

	private String name;

	private Double valuation;

	private Double percent;

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

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return "ChartFundingDetailDTO [code=" + code + ", name=" + name + ", valuation=" + valuation + ", percent="
				+ percent + "]";
	}

}
