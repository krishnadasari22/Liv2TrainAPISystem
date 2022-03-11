package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.factset.dto.FFStockSplitDTO;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.factset.dto.FFStockSpinoffDTO;

public interface CMStockRepository {

	StockPriceDTO getCompanyLatestStockPrice(String companyId, String currencyCode) throws Exception;
	
	StockPriceDTO getCompanyStockPrice(String companyId, String date, String currencyCode) throws Exception;
	
	List<StockPriceDTO> getFactSetCompanyStockPrice(String companyId, String periodType, Date startDate, Date endDate,String currencyCode);
	
	StockPriceDTO getStockPriceHighest(String companyId, String currencyCode);

	StockPriceDTO getStockPriceHighest(String companyId, Integer days, String currencyCode);
	
	StockPriceDTO getStockPriceLowest(String companyId, String currencyCode);
	
	StockPriceDTO getStockPriceLowest(String companyId, Integer days, String currencyCode);
	
	List<FFStockSplitDTO> getSplitStockInfo(String companyId);

	List<FFStockSpinoffDTO> getSpinoffStockInfo(String companyId);

	List<StockPriceDTO> getIndexStockPrice(Integer indexId, Date startDate, Date endDate, String periodType);
	
	List<Map<String, Object>> getCompanyTotalSharesByPeriod(String companyId,String period);

	FFBasicCfDTO getFFBasicCfDTO(String companyId);

	
}
