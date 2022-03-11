package com.pcompany.model;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.ChartFundingDetailDTO;
import com.pcompany.dto.ChartIndustryFundingDetailDTO;
import com.pcompany.dto.TopFundedCompaniesDTO;

public class PEVCSummaryModel {

	private String countryIsoCode;
	private String currency;
	private Integer sectorCode;
	private Date startDate;
	private Date endDate;

	List<ChartFundingDetailDTO> byCountryFundingDetail;

	List<ChartIndustryFundingDetailDTO> byIndustryFundingDetail;

	List<TopFundedCompaniesDTO> topFundedCompanies;

	public List<ChartFundingDetailDTO> getByCountryFundingDetail() {
		return byCountryFundingDetail;
	}

	public void setByCountryFundingDetail(List<ChartFundingDetailDTO> byCountryFundingDetail) {
		this.byCountryFundingDetail = byCountryFundingDetail;
	}

	public List<ChartIndustryFundingDetailDTO> getByIndustryFundingDetail() {
		return byIndustryFundingDetail;
	}

	public void setByIndustryFundingDetail(List<ChartIndustryFundingDetailDTO> byIndustryFundingDetail) {
		this.byIndustryFundingDetail = byIndustryFundingDetail;
	}

	public List<TopFundedCompaniesDTO> getTopFundedCompanies() {
		return topFundedCompanies;
	}

	public void setTopFundedCompanies(List<TopFundedCompaniesDTO> topFundedCompanies) {
		this.topFundedCompanies = topFundedCompanies;
	}

	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}

	public Integer getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(Integer sectorCode) {
		this.sectorCode = sectorCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "PEVCSummaryModel [countryIsoCode=" + countryIsoCode + ", currency=" + currency + ", sectorCode="
				+ sectorCode + ", startDate=" + startDate + ", endDate=" + endDate + ", byCountryFundingDetail="
				+ byCountryFundingDetail + ", byIndustryFundingDetail=" + byIndustryFundingDetail
				+ ", topFundedCompanies=" + topFundedCompanies + "]";
	}

}
