package com.pcompany.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcompany.service.MNAServiceA;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Company MNA", description = "Rest API for merger and acquisition", tags = "Company MNA")
public class MNAControllerA {

	Logger _log = Logger.getLogger(MNAControllerA.class);
	
	
	@Autowired
	MNAServiceA mNAServiceA;
	
	
	@GetMapping("/mna/companies")
	public ResponseEntity<List<CompanyDTO>> getMnaCompanies(
			@RequestParam(name="searchCriteria",required=false) String searchCriteria) {
		
		_log.info("Extracting M&A company list for searchCriteria:"+ searchCriteria);
		List<CompanyDTO> companies = null;
		try {
			if(null!=searchCriteria) {
				searchCriteria = java.net.URLDecoder.decode(searchCriteria, StandardCharsets.UTF_8.name());
				_log.info("Extracting companies like: '"+ searchCriteria +"'");
			}
			companies=mNAServiceA.getMnaCompanies(searchCriteria);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<List<CompanyDTO>>(companies, HttpStatus.OK);
	}
	
	@GetMapping("/mna/countries")
	public ResponseEntity<List<CountryListDTO>> getMnaCountries() {
     _log.info("getting man countries");
		  List<CountryListDTO> countryList = null;
    try {
			countryList=mNAServiceA.getMnaCountries();
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CountryListDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CountryListDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CountryListDTO>>(countryList, HttpStatus.OK);
	}
	
	@GetMapping("/mna/ticsindustry")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getMnaIndustries(@RequestParam(value="countryCode",required=false)String countryCode) {
     _log.info("getting man countries");
		  List<IndustryFinancialDataDTO>industryFinancialDataDTOs = null;
    try {
    	industryFinancialDataDTOs=mNAServiceA.getMnaIndustries(countryCode);
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(industryFinancialDataDTOs, HttpStatus.OK);
	}
}
