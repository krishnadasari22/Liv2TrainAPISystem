package com.televisory.bond.service;

import java.util.List;
import java.util.Map;

import com.televisory.bond.Model.CDSResponseModel;
import com.televisory.bond.dto.CDSBalanceModelDTO;
import com.televisory.bond.dto.CDSComparableDTO;
import com.televisory.bond.dto.CDSDataLatestDTO;
import com.televisory.bond.dto.CDSHistoricalDataDTO;
import com.televisory.bond.dto.CDSNameDTO;

public interface IBondService {
	
	String getDefaultCDSIdentifier();

	Map<String, Object> getProjectedValues(List<String> vals, String moduleName);

	Map<String, Object> getCurrentValues(Map<String, String> filters, String cds);

	List<CDSBalanceModelDTO> getCdsMetricList();

	List<String> getCdsCountryList();

	List<String> getCdsCurrencyList(String sector);

	CDSDataLatestDTO getLatestDateData(String identifier) throws Exception;

	List<CDSNameDTO> getCdsNameList(String sector, String currency, String searchCriteria);

	List<CDSHistoricalDataDTO> getHistoricalData(String identifier, String fieldName);

	List<String> getCdsSectorList();

	CDSResponseModel getCDSData(String identifier, String fieldName);

	<T> List<T> getAdvanceSearch(Map<String, String> filters, String requestType);

	List<CDSComparableDTO> getComparable(String identifier, List<String> includeIdentifier, List<String> excludeIdentifier) throws Exception;


}
