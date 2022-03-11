package com.televisory.bond.service;

import java.util.List;

import com.televisory.bond.Model.DerivativeResponseModel;
import com.televisory.bond.dto.DerFuCoparableDTO;
import com.televisory.bond.dto.DerOpCoparableDTO;
import com.televisory.bond.dto.DerivativeNameDTO;
import com.televisory.bond.entity.DerivativeIdentifierWithProp;

public interface DerivativeService {

	List<DerivativeNameDTO> getDerivativeNameList(String searchCriteria);
	
	List<String> getDerivativeCategoryList(String derivativeName);
	
	DerivativeResponseModel getDerivativeData(String identifier, String fieldName);

	List<DerivativeIdentifierWithProp> getDerivativeExpiryDateList(String derivativeName, String category);

	List<String> getDerivativeOptionType(String derivativeName, String category, String expiryDate);

	List<DerivativeIdentifierWithProp> getDerivativeStrikeList(String derivativeName, String category,
			String expiryDate, String optionType);

	List<DerFuCoparableDTO> getFuComparable(String identifier);

	List<DerOpCoparableDTO> getOpComparable(String identifier);

	String getDefaultIdentifier();

}
