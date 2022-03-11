package com.privatecompany.dao;

import java.util.List;

import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;

public interface MNAdaoA {

	List<CompanyDTO> getMnaCompanies(String searchCriteria);

	List<CountryListDTO> getMnaCountries();

	List<IndustryFinancialDataDTO> getMnaIndustries(String countryId);

}
