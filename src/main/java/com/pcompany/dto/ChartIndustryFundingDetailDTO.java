package com.pcompany.dto;

public class ChartIndustryFundingDetailDTO {

	private String code;

	private String name;

	private Double valuation;

	private String sector;

	private String sectorCode;

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

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return "ChartIndustryFundingDetailDTO [code=" + code + ", name=" + name + ", valuation=" + valuation
				+ ", sector=" + sector + ", sectorCode=" + sectorCode + ", percent=" + percent + "]";
	}

}
