package com.privatecompany.dao;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.PEVCFundingInvestmentDTO;
import com.pcompany.dto.PEVCInvestmentHeaderDTO;
import com.pcompany.entity.PEVCInvstmentHeader;
import com.privatecompany.dto.PEVCInvestmentDetailsDTO;
import com.televisory.capitalmarket.dto.AdvancedSearchFundingAmoutDto;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.FinancialTypeDto;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.PevcIssueTypeDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;

public interface PeVcDaoA {

	List<CountryListDTO> getPeVcCountries();

	List<IndustryFinancialDataDTO> getPeVcIndustries(String countryCode, Date startDate,Date endDate);

	List<CompanyDTO> getPeVcCompanies(String searchCriteria, String countryCode, String industry, Date startDate,
			Date endDate);

	List<PEVCInvestmentDetailsDTO> getPeVcFundingInvestments(String entityId, String category);

	PEVCInvstmentHeader getPeVcInvestmentHeader(String entityId, String category, Date startDate, Date endDate,
			String financialType,String currencyCode);

	List<PevcIssueTypeDTO> getPeVcIssueType(String coun,String industry,String fintype);

	List<CountryListDTO> getPeVcAdvancedSearchCountries(String ticsIndustry, String financialType, String issueType);

	List<IndustryFinancialDataDTO> getPeVcAdvancedSearchIndustries(String countryCode, String financialType,
			String issueType);

	List<FinancialTypeDto> getPeVcAdvancedSearchFinancialType(String countryCode, String ticsIndustry, String issueType);

	List<PevcIssueTypeDTO> getPeVcAdvancedSearchIssueType(String countryCode, String ticsIndustry,
			String financialType);

	AdvancedSearchFundingAmoutDto getPeVcfundingAmout(String countryCode, String ticsIndustry,
			String financialType, String issueType, String entityId,String currency, Date sDate, Date eDate);

}
