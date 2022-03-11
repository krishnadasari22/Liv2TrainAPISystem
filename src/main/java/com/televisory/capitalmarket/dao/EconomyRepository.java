package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;

import com.televisory.capitalmarket.dto.CountryIndicatorDTO;
import com.televisory.capitalmarket.dto.CurrencyListDTO;
import com.televisory.capitalmarket.dto.IndicatorDataDTO_old;
import com.televisory.capitalmarket.dto.IndicatorTypeDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.CurrencyMappingDTO;
import com.televisory.capitalmarket.dto.economy.EconomyCreditRatingLatestDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorDataForecastDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.dto.economy.TelevisoryIndicatorReportingFrequencyDTO;
import com.televisory.capitalmarket.entities.economy.ExchangeRatesComparison;
import com.televisory.capitalmarket.entities.economy.IndicatorData_old;
import com.televisory.capitalmarket.entities.economy.IndicatorType;
import com.televisory.capitalmarket.model.PeriodicityEconomyRequest;

public interface EconomyRepository {
	
	List<CountryListDTO> findAllEconomyCountries();
	List<IndicatorData_old> getEconomyIndicatorData(Integer countryId,Integer indicatorId);
	List<IndicatorData_old> getEconomyIndicatorData(Integer countryId,Integer indicatorId, Date startDate, Date endDate);
	List<IndicatorType> getAllIndicatorType();
	List<CountryIndicatorDTO> getCountryIndicatorPeriodicity(Integer countryId);
	List<CountryIndicatorDTO> getCountryIndicatorPeriodicityList(List<String> countryId);
	IndicatorTypeDTO getIndicatorData(Integer countryId, Integer indicatorId, String periodType);
	List<ExchangeRatesComparison> getLatestExchangeRate(String currencyCode);
	List<ExchangeRatesComparison> getDummyExchangeRate(String currencyCode);
	List<IndicatorDataDTO_old> getMajorEconomyData(Integer indicatorId, String periodType,Integer exceptCountryId);
	//List<IndicatorData> getPrevIndicatorData(int indicatorId, int countryId, String dataType, int indicatorTypeId, String minusPeriod, String plusPeriod);
	List<IndicatorDataDTO_old> getPrevIndicatorData(int countryIndicatorId, String minusPeriod, String plusPeriod);
	String getCountryName(Integer countryId);
	String getIndicatorName(Integer indicatorId);
	IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, Date startDate, Date endDate);
	List<IndicatorDataDTO_old> getIndicatorDataByIndicator(Integer countryId, Integer indicatorId, String periodType);
	List<IndicatorDataDTO_old> getIndicatorDataByIndicator(Integer countryId, Integer indicatorId, String periodType, Date startDate, Date endDate);
	List<IndicatorLatestDataDTO> findAllIndicators();
	List<CountryListDTO> findEconomyCountriesByIndicator(String indicatorName);
	List<IndicatorLatestDataDTO> findIndicatorsByCountry(List<String> countryIsoCodeList);
	List<CountryIndicatorDTO> getIndicatorPeriodicity(Integer indicatorId, Integer countryId);
	List<CountryIndicatorDTO> getIndicatorPeriodicityList(Integer indicatorId, List<String> countryId);
	List<CountryListDTO> findEconomyCountriesByMultipleIndicator(List<Integer> indicatorList);
	List<IndicatorDTO> findIndicatorsByMultipleCountry(List<Integer> countryList);
	List<String> getPeriodicity(PeriodicityEconomyRequest periodicityRequest);
	IndicatorTypeDTO getIndicatorData(Integer countryId, Integer indicatorTypeId, String periodType, String currency);
	IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, String currency,Date startDate, Date endDate);
	List<IndicatorData_old> getEconomyIndicatorData(List<String> countryList, List<String> indicatorList, Date startDate,Date startDate2);
	String getCountryIsoCode(Integer countryId);
	//IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, String currency);
	List<IndicatorDataDTO_old> getEconomyRequestIndicatorData(Integer countryId, Integer indicatorId, String currency, String periodicity, Date startDate, Date endDate);
	List<CountryListDTO> findCountriesBySearchCriteria(String searchCriteria,Integer resultCount);
	List<CurrencyListDTO> getAllCurrency();
	List<CurrencyListDTO> getAllCurrency(String searchCriteria);
	CurrencyMappingDTO getCountryCurrency(String country) throws Exception;
	List<ExchangeRatesComparison> getFactSetExchangeRate(String sourceCurrencyCode, String targetCurrencyCode, Date startDate, Date endDate);
	List<IndicatorDTO> findIndicators(List<Integer> countryList);
	List<CountryListDTO> getCMCountriesByIdList(List<String> customCountryIds);
	Double getFxData(String periodType, Date fxDate, String currencyCode);
	List<IndicatorLatestDataDTO> getEconomyIndicatorLatestData(String countryIsoCode, Date startDate, Date endDate);
	List<IndicatorLatestDataDTO> getEconomyIndicatorLatestDataByCurrency(String countryName, String currency,
			Date startDate, Date endDate);
	List<IndicatorHistoricalDataDTO> getDataByIndicatorAndCountry(List<String> countryIsoCodeList, List<String> indicatorNameList, String periodType,Date startDate,Date endDate);
	List<IndicatorHistoricalDataDTO> getDataByIndicatorAndCountry(List<String> countryIsoCodeList, List<String> indicatorNameList,
			String periodType, String currency, Date startDate, Date endDate);
	List<NewsDTO> getNews(String countryIsoCode);
	CurrencyMappingDTO getCountryCurrencyByIsoCode(String countryIsoCode) throws Exception;
	CountryListDTO getCountryByIsoCode(String countryIsoCode);
	List<String> getIndicatorPeriodIcity(List<String> indicatorName,List<String> countryList);
	List<EconomyCreditRatingLatestDTO> getEconomyCreditRatingByCountry(String countryIsoCode);
	CountryListDTO getCMCountryByIsoCodes(String countryIsoCode);
	List<IndicatorLatestDataDTO> findAllIndicators(String searchParam);
	Double getDailyFxData(String periodType, Date fxDate, String currencyCode,
			String countryCurrency);
	List<CountryListDTO> findCountriesBySubscribedCountry(List<String> userCountryList);
	CountryListDTO getDefaultCountryForEconomy(List<String> countryCode);
	List<CountryListDTO> findEconomyCountriesByIndicatorAndSubscribtion(String indicatorName, List<String> userCountryList);
	Double getFxData(String periodType, Date fxDate, String currencyCode,
			String countryCurrency);
	List<IndicatorDataForecastDTO> getDefaultCountryForEconomy(String historicalDataSymbol);
	List<TelevisoryIndicatorReportingFrequencyDTO> getReportedFrequencyBasedOnCountryAndCategory(
			String countryName, String countryIsoCode, String category);
	
}