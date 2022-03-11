package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.GlobalSearchIndustryDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.entities.cm.GlobalSearchCompany;

/**
 * @author vinay
 */
public class GlobalSearchResponseModel {
	
	private String searchKeyWord;
	
	@SuppressWarnings("rawtypes")
	private List<GlobalSearchCompany> company;
	
	private List<CountryListDTO> country;
	
	private List<GlobalSearchIndustryDTO> industry;
	
	private List<IndicatorLatestDataDTO> indicator;
	
	private List<CommodityLatestDataDTO> commodity;

	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public List<GlobalSearchCompany> getCompany() {
		return company;
	}

	public void setCompany(List<GlobalSearchCompany> company) {
		this.company = company;
	}

	public List<CountryListDTO> getCountry() {
		return country;
	}

	public void setCountry(List<CountryListDTO> country) {
		this.country = country;
	}
	
	public List<GlobalSearchIndustryDTO> getIndustry() {
		return industry;
	}

	public void setIndustry(List<GlobalSearchIndustryDTO> industry) {
		this.industry = industry;
	}
	
	public List<IndicatorLatestDataDTO> getIndicator() {
		return indicator;
	}

	public void setIndicator(List<IndicatorLatestDataDTO> indicator) {
		this.indicator = indicator;
	}
	
	public List<CommodityLatestDataDTO> getCommodity() {
		return commodity;
	}

	public void setCommodity(List<CommodityLatestDataDTO> commodity) {
		this.commodity = commodity;
	}

	@Override
	public String toString() {
		return "GlobalSearchResponseModel [searchKeyWord=" + searchKeyWord
				+ ", company=" + company + ", country=" + country
				+ ", industry=" + industry + ", indicator=" + indicator
				+ ", commodity=" + commodity + "]";
	}
}
