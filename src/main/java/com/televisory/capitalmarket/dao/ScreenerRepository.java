package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;

import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.ScreenerCompanyFinancialDTO;
import com.televisory.capitalmarket.dto.ScreenerStockPriceDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.entities.factset.FxConversionRate;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.model.ScreenerFinancialRespModel;

public interface ScreenerRepository {
	List<ScreenerCompanyFinancialDTO> getCompanyYearly(String ticsIndustryCode, String countryIsoList);
	List<ScreenerCompanyFinancialDTO> getCompanyFinancial(String subIndustryCode,String currency,String fieldName,String domicileCountryCode);
	List<ScreenerCompanyFinancialDTO> getCompanyFinancialRatio(String subIndustryCode,String currency,String fieldName,String domicileCountryCode);
	List<ScreenerStockPriceDTO> getCompaniesLatestStockPrice(String ticsIndustryCode,String currency, List<String> countryIsoList);
	List<FFBasicCfDTO> getCompaniesTotalStock(String ticsIndustryCode);
	Double getCurrencyConversionRate(String sourceCurrency,String destinationCurrency,String periodList);
}
