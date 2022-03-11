package com.televisory.bond.service;

import java.util.List;

import com.televisory.bond.Model.FIResponseModel;
import com.televisory.bond.dto.FIComparableDTO;
import com.televisory.bond.dto.FINameDTO;

public interface FiService {

	List<String> getFiCurrencyList(String categeory);

	List<FINameDTO> getFiNameList(String categeory, String currency, String searchCriteria);

	String getDefaultFIIdentifier();

	FIResponseModel getFiData(String identifier, String fieldName);

	List<String> getFiCategeoryList();

	List<FIComparableDTO> getComparable(String identifier, List<String> includeIdentifier,
			List<String> excludeIdentifier) throws Exception;

}
