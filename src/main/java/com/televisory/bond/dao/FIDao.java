package com.televisory.bond.dao;

import java.util.List;

import com.televisory.bond.dto.FIBalanceModelDTO;
import com.televisory.bond.dto.FIComparableDTO;
import com.televisory.bond.dto.FIHistoricalDTO;
import com.televisory.bond.dto.FILatestDataDTO;
import com.televisory.bond.dto.FINameDTO;

public interface FIDao {

	List<String> getFiCurrencyList(String categeory);

	String getDefaultFIIdentifier();

	List<String> getFiCategeoryList();

	List<FIBalanceModelDTO> getFIMetricList();

	FILatestDataDTO getLatestDateData(String identifier) throws Exception;

	List<FIHistoricalDTO> getHistoricalData(String identifier, String fieldName);

	List<FINameDTO> getFiNameList(String categeory, String currency, String searchCriteria);

	List<FIComparableDTO> getComparable(String identifier, List<String> includeIdentifier,
			List<String> excludeIdentifier) throws Exception;

}
