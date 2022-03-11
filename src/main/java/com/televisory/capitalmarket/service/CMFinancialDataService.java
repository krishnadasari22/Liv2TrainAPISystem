package com.televisory.capitalmarket.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CMFinancialDataRepository;
import com.televisory.capitalmarket.dto.BenchMarkCompanyDTO;
import com.televisory.capitalmarket.dto.BenchMarkCompanyOldDTO;
import com.televisory.capitalmarket.dto.BenchmarkCompanyNewDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class CMFinancialDataService {
	

	Logger _log = Logger.getLogger(CMFinancialDataService.class);
	
	@Autowired
	CMFinancialDataRepository factSetRepository;
	
	@Autowired
	CorporateAnnouncementService corpAnnService;
	
	@Autowired
	@Lazy
	CapitalMarketService capitalMarketService;
	
	//extract latest annual financial for company metadata 
	public CompanyFinancialDTO getCompanyLatestBasicBSFinancial(String companyId, String fieldName, String currencyCode) {
		return factSetRepository.getCompanyLatestBasicBSFinancial(companyId, fieldName, currencyCode);
	}
	
	public CompanyFinancialDTO getCompanyLatestBasicDerivedAnnualFinancial(String companyId, String fieldName, String currencyCode) {
		return factSetRepository.getCompanyLatestBasicDerivedAnnualFinancial(companyId, fieldName, currencyCode);
	}
	
	public CompanyFinancialDTO getCompanyLatestAdvanceAnnualFinancial(String companyId, String fieldName, String currencyCode) {
		return factSetRepository.getCompanyLatestAdvanceAnnualFinancial(companyId, fieldName, currencyCode);
	}
	/**
	 * Method to get the company financial data (BS, CF , IS)
	 * @param companyId
	 * @param financialType
	 * @param periodType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SQLException 
	 * 
	 */
	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0")
	public  List<CompanyFinancialMINDTO> getCompanyFinancial(String companyId, String financialType, String periodType, Date startDate, Date endDate,String currency, Boolean restated) throws Exception {
		
		if(financialType!=null 
				&& (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE) || financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE))) {
			financialType = CMStatic.PUBLIC_FINANCIAL_ALL;
		}
		List<CompanyFinancialMINDTO> companyFinancialDto = factSetRepository.getCompanyFinancial(companyId, financialType, periodType, startDate, endDate,currency,restated);
		
		return companyFinancialDto;
		
	}
	
	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0")
	public  List<CompanyFinancialMINDTO> getPrivateCompanyFinancial(String companyId, String financialType, String periodType, Date startDate, Date endDate,String currency, Boolean restated) throws Exception {
		
		if(financialType!=null 
				&& (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE) || financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE))) {
			financialType = CMStatic.PRIVATE_FINANCIAL_ALL;
		}
		List<CompanyFinancialMINDTO> companyFinancialDto = factSetRepository.getPrivateCompanyFinancial(companyId, financialType, periodType, startDate, endDate,currency,restated);
		
		return companyFinancialDto;
		
	}
	
	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0",key="{#root.methodName,#p0}")
	public  List<CompanyLatestFilingInfoDTO> getCompanyLatestFilingInfo(String companyId) throws Exception {
		
		
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = factSetRepository.getCompanyLatestFilingInfo(companyId);
		
		return companyLatestFilingInfoDTOs;
		
	}
	/**
	 * Method to get the company financial data (BS, CF , IS)
	 * @param companyId
	 * @param financialType
	 * @param fieldName
	 * @param periodType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SQLException 
	 */
	public  List<CompanyFinancialMINDTO> getCompanyFinancial(String companyId, List<String> fieldName, String periodType, Date startDate, Date endDate,String currency) throws Exception {
			
		List<CompanyFinancialMINDTO> companyFinancialDto = factSetRepository.getCompanyFinancial(companyId, fieldName, periodType, startDate, endDate,currency);
		
		return companyFinancialDto;
	}

	
	/**
	 * Method to get relative performance
	 * @param companyId
	 * @param indexId
	 * @param currency 
	 * @return
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<Map<String,Object>> getCompanyRelativePerformance(String companyId, String indexId, String currency) {
		return factSetRepository.getCompanyRelativePerformance(companyId,indexId,currency);	
	}



	/**
	 * Method to get benchmark comapnies by company id
	 * @param companyId
	 * @param resultLimit
	 * @return
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<BenchMarkCompanyOldDTO> getbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency) {
		return factSetRepository.getbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency);
	}
	
	/**
	 * Method to get benchmark comapnies by company id
	 * @param companyId
	 * @param resultLimit
	 * @return
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<BenchMarkCompanyOldDTO> getbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove, String period) {
		return factSetRepository.getbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency);
	}
	
	
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<BenchMarkCompanyDTO> editbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove, String periodType) {
		return factSetRepository.editbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency, benchmarkCompanyAdd, benchmarkCompanyRemove, periodType);
	}
	
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<BenchmarkCompanyNewDTO> editbenchMarkCompaniesByEntityId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove, String periodType, String entityType) {
		return factSetRepository.editbenchMarkCompaniesByEntityId(companyId,resultLimit,currency, benchmarkCompanyAdd, benchmarkCompanyRemove, periodType, entityType);
	}
	
	
	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0")
	public  List<String> getCompanyFinancialPeriodicity(String companyId) throws Exception {
		
	
		//List<CompanyFinancialMINDTO> companyFinancialDto = factSetRepository.getPrivateCompanyFinancial(companyId, financialType, periodType, startDate, endDate,currency,restated);
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = null;
		companyLatestFilingInfoDTOs = getCompanyLatestFilingInfo(companyId);
		_log.info(companyLatestFilingInfoDTOs);
		List<String> periodicityList = new ArrayList<>();
		if(companyLatestFilingInfoDTOs!=null) {
			for(int i=0;i <companyLatestFilingInfoDTOs.size();i++) {
				periodicityList.add(companyLatestFilingInfoDTOs.get(i).getPeriodType());
			}
		}
		return periodicityList;
		
	}

	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0",key="{#root.methodName,#p0}")
	public List<CompanyLatestFilingInfoDTO> getBenchmarkLatestFilingInfo(
			String companyId) {
		return factSetRepository.getBenchmarkLatestFilingInfo(companyId);
	}

	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0",key="{#root.methodName,#p0}")
	public List<CompanyLatestFilingInfoDTO> getBenchmarkPeriods(String companyId) {
		return factSetRepository.getBenchmarkPeriods(companyId);
	}
}
