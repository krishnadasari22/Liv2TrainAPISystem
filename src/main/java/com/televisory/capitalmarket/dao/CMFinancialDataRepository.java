package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.televisory.capitalmarket.dto.BenchMarkCompanyDTO;
import com.televisory.capitalmarket.dto.BenchMarkCompanyOldDTO;
import com.televisory.capitalmarket.dto.BenchmarkCompanyNewDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;

public interface CMFinancialDataRepository {

	//List<CompanyDTO> getFactSetExchangeCompanies(String exchangeName);

	List<Map<String, Object>> getCompanyRelativePerformance(String companyId, String indexId,String currency);

	//List<CompanyFinancialDTO> getCompanykeyFinancials(String companyId, String keyFinancials,Date startDate,Date endDate);
	public CompanyFinancialDTO getCompanyLatestBasicBSFinancial(String companyId, String fieldName, String currencyCode);
	public CompanyFinancialDTO getCompanyLatestAdvanceAnnualFinancial(String companyId, String fieldName, String currencyCode);
	public CompanyFinancialDTO getCompanyLatestBasicDerivedAnnualFinancial(String companyId, String fieldName, String currencyCode);
	
	List<CompanyFinancialMINDTO> getCompanyFinancial(String companyId, String financialType, String periodType, Date startDate, Date endDate, String currency, Boolean restated) throws Exception;
	List<CompanyFinancialMINDTO> getCompanyFinancial(String companyId, List<String> fieldName, String periodType, Date startDate, Date endDate,String currency);
	
	List<CompanyFinancialMINDTO> getPrivateCompanyFinancial(String companyId, String financialType, String periodType, Date startDate, Date endDate, String currency, Boolean restated) throws Exception;

	List<BenchMarkCompanyOldDTO> getbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency);
	List<BenchMarkCompanyDTO> editbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove,String period);
	List<BenchmarkCompanyNewDTO> editbenchMarkCompaniesByEntityId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove,String period, String entityType);
	
	List<CompanyLatestFilingInfoDTO> getCompanyLatestFilingInfo(String companyId);

	List<BenchMarkCompanyDTO> getbenchMarkCompaniesByCompanyId(String companyId, Integer resultLimit, String currency,String benchmarkCompanyAdd, String benchmarkCompanyRemove,String period);

	List<CompanyLatestFilingInfoDTO> getBenchmarkLatestFilingInfo(
			String companyId);

	List<CompanyLatestFilingInfoDTO> getBenchmarkPeriods(String companyId);

}
