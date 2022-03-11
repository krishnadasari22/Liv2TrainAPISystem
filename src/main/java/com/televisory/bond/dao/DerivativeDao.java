package com.televisory.bond.dao;

import java.util.List;

import com.televisory.bond.dto.DerFuCoparableDTO;
import com.televisory.bond.dto.DerOpCoparableDTO;
import com.televisory.bond.dto.DerivativeBalanceModelDTO;
import com.televisory.bond.dto.DerivativeHistoricalDataDTO;
import com.televisory.bond.dto.DerivativeLatestDTO;
import com.televisory.bond.dto.DerivativeNameDTO;
import com.televisory.bond.entity.DerivativeIdentifierWithProp;

public interface DerivativeDao {

	List<DerivativeNameDTO> getDerivativeNameList(String searchCriteria);

	List<String> getDerivativeCategoryList(String derivativeName);

	List<DerivativeIdentifierWithProp> getDerivativeExpiryDateList(String derivativeName, String category);

	List<String> getDerivativeOptionType(String derivativeName, String category, String expiryDate);

	List<DerivativeIdentifierWithProp> getDerivativeStrikeList(String derivativeName, String category,
			String expiryDate, String optionType);

	List<DerFuCoparableDTO> getFuComparable(String identifier);

	List<DerOpCoparableDTO> getOpComparable(String identifier);

	String getDefaultIdentifier();
	
	List<DerivativeBalanceModelDTO> getDerivativeMetricList();

	DerivativeLatestDTO getLatestDateData(String identifier) throws Exception;

	List<DerivativeHistoricalDataDTO> getHistoricalData(String identifier, String fieldName);

}
