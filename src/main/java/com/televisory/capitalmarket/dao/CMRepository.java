package com.televisory.capitalmarket.dao;

import java.util.List;

import com.privatecompany.dto.AdvanceSearchCompanyDTO;
import com.privatecompany.dto.EntityStructureDTO;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CMCDTO;
import com.televisory.capitalmarket.dto.CMExchangeDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.ObjectPropertiesDTO;
import com.televisory.capitalmarket.dto.PCCompanyDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.factset.dto.FFEntityProfileDTO;

public interface CMRepository {
	
	ObjectPropertiesDTO getObjectProperty(String name);
	
	List<CountryListDTO> getCountryList();
	
	CountryListDTO getCountry(String countryCode);

	List<CountryListDTO> getCountryList(List<String> countryCodeList);
	
	List<IndexDTO> getIndexList(String exchangeCode);

	IndexDTO getIndex(Integer indexId) throws Exception;
	
	List<IndexDTO> getIndex(List<Integer> indexId) throws Exception;

	String getCompanyTicker(String companyId) throws Exception;

	CompanyDTO getCMCompaniesById(String companyId);
	
	List<CompanyDTO> getCMCompaniesByIdList(List<String> companyIdList);

	List<CMExchangeDTO> getExchangeList();
	
	List<CMExchangeDTO> getExchangeList(List<String> userCountryList);

	List<CompanyDTO> getExchangeCompanyList(String exchangeCode, String searchCriteria);

	List<IndexDTO> getCompanyIndexList(String companyId);

	//CompanyDetailsDTO getCMCompleteCompanyDetailsById(String companyId);

	List<BalanceModelDTO> getRatioBalanceModel(String industry, Boolean watchlistFlag, Boolean icFlag,Boolean screenerFlag);
	
	BalanceModelDTO getFinancialParams(String industry, String fieldName);
	
	List<BalanceModelDTO> getFinancialParams(String industry, List<String> fieldNames);
	
	String getItemNameByFieldName(String fieldName) throws Exception;

	CompanyDTO getDefaultCompanyForCountry();
	
	CompanyDTO getDefaultCompanyForCountry(String primaryCountryCode);
	
	CompanyDTO getDefaultCompanyForCountry(String primaryCountryCode, List<String> subscribedCountyList);

	List<CompanyDTO> getCMCompaniesByEntityId(String entityId);
	List<CMCDTO> getCMCompaniesUsedTriggerByEntityId(String entityId);

	List<CompanyDTO> getCMExchangeCompaniesByUserSubscription(String searchCriteria, Integer resultCount,List<String> userCountryList);
	
	public Double getFxData(String periodType, String date, String sourceCurrency , String targetCurrency);
	
	String getMetricNameFromMetricCode(String metricCode);

	List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchAllEntityAllIndustry(
			String entitySelection, String industrySelection,
			String countrySelection, String currencySelection, String searchKeyWord);

	List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchAllEntity(
			String entitySelection, String industrySelection,
			String countrySelection, String currencySelection, String searchKeyWord);

	List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchPublicEntityAllIndustry(
			String entitySelection, String industrySelection,
			String countrySelection, String currencySelection, String searchKeyWord);

	List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchPublicEntity(
			String entitySelection, String industrySelection,
			String countrySelection, String currencySelection, String searchKeyWord);

	/*
	 * List<AdvanceSearchCompanyDTO>
	 * getAdvancedCompanySearchPrivateEntityAllIndustry( String entitySelection,
	 * String industrySelection, String countrySelection, String currencySelection,
	 * String searchKeyWord);
	 */

	List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchPrivateEntity(
			String entitySelection, String industrySelection,
			String countrySelection, String currencySelection, String searchKeyWord);

	List<EntityStructureDTO> getEntityStructure(String entityId);
	
	PCCompanyDTO getPCCompaniesById(String entityId);

	List<FFEntityProfileDTO> getEntityProfile(String entityId);
	
	List<CompanyDTO> getCMExchangeCompanies(String searchCriteria,
			Integer resultCount, Integer countryId, String entityType);
}
