package com.pcompany.service;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.privatecompany.dao.MNAdaoA;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;

@Service
public class MNAServiceA {
	
	@Autowired
	MNAdaoA mnAdaoA;

	public List<CompanyDTO> getMnaCompanies(String searchCriteria) {

		List<CompanyDTO> allCompany = mnAdaoA.getMnaCompanies(searchCriteria);
		
		List<CompanyDTO>companyDTOs=null;

		if(null != allCompany && allCompany.size() > 0) {


			//remove duplicate companies based on company name
			companyDTOs = allCompany.stream()
					.collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyDTO<String>::getName))),
							ArrayList::new));
		} else {
			companyDTOs = allCompany;
		}
	return companyDTOs;
	}

	public List<CountryListDTO> getMnaCountries() {
		return mnAdaoA.getMnaCountries();
	}

	public List<IndustryFinancialDataDTO> getMnaIndustries(String countryCode) {
		return mnAdaoA.getMnaIndustries(countryCode);
	}
}
