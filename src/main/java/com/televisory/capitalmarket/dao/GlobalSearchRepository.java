package com.televisory.capitalmarket.dao;

import java.util.List;

import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.GlobalSearchIndustryDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.entities.cm.GlobalSearchCompany;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;


public interface GlobalSearchRepository {
	List<GlobalSearchCompany> getCMExchangeCompanies(String searchCriteria,Integer resultCount , Integer countryId);

	List<GlobalSearchIndustryDTO> getTicsIndustryBySearchParam(String searchParam,Integer resultCount);

	List<IndicatorLatestDataDTO> findAllIndicators(String searchParam,Integer resultCount);

	List<CommodityLatestDataDTO> getCommodities(String searchParam,Integer resultCount);

	List<CountryListDTO> findCountriesBySearchCriteria(String searchCriteria,Integer resultCount);

	List<CountryListDTO> findAllEconomyCountries();

	List<GlobalSearchCompany> getCMExchangeCompaniesWithSubscribedCountries(String searchCriteria, Integer resultCount, Integer countryId,List<String> userCountryList);

	List<CountryListDTO> findCountriesBySearchCriteriaAndSubscription(String searchCriteria, Integer resultCount,List<String> userCountryList);

	List<GlobalSearchIndustryDTO> getTicsIndustryBySearchParamAndSubscription(String keyword, Integer resultCount, List<String> userCountryList);

	List<IndicatorLatestDataDTO> findAllIndicatorsBySubscription(String keyword, Integer resultCount, List<String> userCountryList);
}
