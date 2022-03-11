package com.televisory.capitalmarket.dao;

import java.util.List;

import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.model.IcStockRequestModel;

/**
 * 
 * @author vinay
 *
 */
public interface InteractiveComparisonRepository {

	List<CompanyDTO> getIComCompanies(String countryIdList);

	List<CompanyDTO> getIComCompanies(String searchCriteria, String countryCode, String ffIndustry);

	List<CompanyDTO> getIComCompanies();

	List<CompanyDTO> getIComCompaniesByCountryIndustry(String countryCode, String ffIndustry);

	List<BalanceModelDTO> getFinancialParameter(String industryType,Boolean watchlistFlag, Boolean icFlag,Boolean screenerFlag);

	List<BalanceModelDTO> getDistinctFinancialParameter(String industryType,Boolean watchlistFlag, Boolean icFlag,Boolean screenerFlag);

	List<BalanceModelDTO> getFinancialParams(String industryType, String fieldName);

	List<CompanyDTO> getIComCompanies(String searchCriteria,
			String countryIdList);
}
