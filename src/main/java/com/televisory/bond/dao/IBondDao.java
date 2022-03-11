package com.televisory.bond.dao;

import java.util.List;
import java.util.Map;

import com.televisory.bond.dto.CDSBalanceModelDTO;
import com.televisory.bond.dto.CDSComparableDTO;
import com.televisory.bond.dto.CDSDataDTO;
import com.televisory.bond.dto.CDSDataLatestDTO;
import com.televisory.bond.dto.CDSHistoricalDataDTO;
import com.televisory.bond.dto.CDSNameDTO;

public interface IBondDao {
	
	public String getDefaultCDSIdentifier();
	
	public Map<String, Object> getProjectedValues(List<String> vals);

	Map<String, Object> getProjectedValues(String vals, String moduleName);

	public Map<String, Object> getCurrentValues(Map<String, String> vals, String moduleName);

	Map<String, Object> getProjectedValuesNon(String vals, String moduleName);

	public List<CDSBalanceModelDTO> getCdsMetricList();

	public List<String> getCdsCountryList();

	public List<String> getCdsCurrencyList(String sector);

	public CDSDataLatestDTO getLatestDateData(String identifier) throws Exception;

	public List<CDSNameDTO> getCdsNameList(String sector, String currency, String searchCriteria);
	
	public List<CDSNameDTO> getCdsNameListByCurrency(String searchCriteria,String currency);
	
	public List<CDSNameDTO> getCdsNameListByCountryCurrency(String searchCriteria,String country,String currency);
	
	public List<CDSNameDTO> getCdsNameListByCountry(String searchCriteria,String country);

	public List<CDSHistoricalDataDTO> getHistoricalData(String identifier, String fieldName);

	public List<String> getCdsSectorList();

	//public CDSDataDTO getLatestDateData() throws Exception;

	//List<CDSDataLatest> getAdvanceBondSearch(Map<String, String> filters, String requestType);
	
	<T> List<T> getAdvanceBondSearchGen(Map<String, String> filters, String requestType);

	List<CDSComparableDTO> getComparable(String identifier, List<String> includeIdentifier, List<String> excludeIdentifier) throws Exception;



}
