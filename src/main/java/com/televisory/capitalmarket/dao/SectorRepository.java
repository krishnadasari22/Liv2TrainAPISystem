package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;

import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.entities.cm.IndustryMetaData;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.entities.cm.TicsSector;
import com.televisory.capitalmarket.entities.industry.CountryList;

public interface SectorRepository {
	List<CountryList> findAllIndustryCountries(String ticsSectorCodeList, String ticsIndustryCodeList, String periodType, Date startDate, Date endDate, List<String> userCountyList);
	
	/*List<DELETE_Sector> getSectorPeriod();*/
	
	List<IndustryFinancialDataDTO> getIDSectorDataYearly(String ticsSectorCode, List<String> userCountyList, String params, Date startDate, Date endDate, String currencyCode, Integer month);
	List<IndustryFinancialDataDTO> getIDSectorDataQuarterly(String ticsSectorCode, List<String> userCountyList,String params, Date startDate, Date endDate, String currencyCode);

	List<IndustryFinancialDataDTO> getIDIndustryDataYearly(String ticsSectorCode, String ticsIndustryCode, String countryId, String params, Date startDate, Date endDate, String currencyCode, Integer month);
	List<IndustryFinancialDataDTO> getIDIndustryDataQuarterly(String ticsSectorCode, String ticsIndustryCode, String countryId, String params, Date startDate, Date endDate, String currencyCode);
	
	List<IndustryFinancialDataDTO> getIDCompanyDataYearly(String ticsIndustryCode, String companyId, String countryId, String params, Date startDate, Date endDate, String currencyCode, Integer month, Integer companyFlag);
	List<IndustryFinancialDataDTO> getIDCompanyDataQuarterly(String ticsIndustryCode,  String companyId, String countryId, String params, Date startDate, Date endDate, String currencyCode, Integer companyFlag);
	
	List<TicsSector> getAllSectors();
	
	List<TicsIndustry> getTicsIndustryByTicsSectorCode(String ticsSectorCode);
	
	List<TicsIndustry> getIndustryByTicsIndustryCode(String ticsIndustryCode);
	
	//List<IndustryFinancialDataDTO> getIndustryCountryByField(String fieldName,String periodType,Date startDate, Date endDate,String ticsSectorCode,String ticsIndustryCode,String countryCode,Integer recCount);

	List<TicsIndustry> getTicsIndustryByTicsSectorCode();

	List<TicsIndustry> getTicsIndustryByFactsetIndustry(String factsetIndustry);

	List<TicsIndustry> getTicsIndustryByTicsSectorCode(String ticsSectorCode, String factsetIndustry);

	List<BalanceModelDTO> getIndustryFinancialParameter(String industryType);

	List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, Integer watchlistFlag);

	//List<IndustryFinancialDataDTO> getIndustryCompanyData(String fieldName, String periodType, Date startDate, Date endDate, String ticsSectorCode, String ticsIndustryCode, String countryCode);
	
	List<TicsIndustry> getIndustryByIdList(List<String> customIndustryIds);
	
	String getCountryDomocileCode(String countryId);
	
	List<IndustryMetaData>getPeriodByTicsIndustryNCountryCode(String ticsIndustryCode,String countryCode,String ticsSectorCode);

	List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, String type);

	List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, String type,
			Integer watchlistFlag);

	List<TicsIndustry> getTicsIndustryByCountryCode(String countryList);

	List<TicsIndustry> getTicsIndustryBySearchParam(String searchParam,Integer resultCount);

	@SuppressWarnings("rawtypes")
	CompanyDTO getDefaultCountryForIndustry(List<String> countryCode);

	List<BalanceModelDTO> getIndustryFinancialParameterForIC(String industryType, Integer icFlag);

	List<BalanceModelDTO> getIndustryFinancialModel(String industryType,List<String> fields);

	List<TicsSector> getSectorBySectorCode(String ticsSectorCode);

	List<TicsIndustry> getTicsIndustryByTicsIndustryCode(String ticsIndustryCode);
}
