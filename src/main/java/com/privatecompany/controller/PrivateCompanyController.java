package com.privatecompany.controller;

import io.swagger.annotations.Api;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.privatecompany.dto.PrivateCompanyDTO;
import com.privatecompany.service.PrivateCompanyService;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;

@RestController
@Api(value = "Equity", description = "Rest API for Private Company", tags = "Private Company API")
public class PrivateCompanyController {
	 	
	Logger _log = Logger.getLogger(PrivateCompanyController.class);
	
	@Autowired
	PrivateCompanyService privateCompanyService;
	
	/*@SuppressWarnings("rawtypes")
	@GetMapping("/companies")
	public ResponseEntity<List<CompanyDTO>> getCMCompanies(
			@RequestParam(name="searchCriteria",required=false) String searchCriteria,
			@RequestParam(name="countryId",required=false) Integer countryIds, 
			@RequestParam(name="encodedFlag",required=false) Integer encodedFlag,
			@RequestParam(name="companyId",required=false) String companyIds,
			@RequestParam(name="resultCount",required=false) Integer resultCount,
			@RequestParam(name="excludeDuplicateFlag",required=false, defaultValue="false") Boolean excludeDuplicateFlag,
			@RequestParam(value="userCountryList",required=false) List<String> userCountryList) {
		_log.info("Extracting company list for countryId:"+ countryIds +", companyId: "+ companyIds +", excludeDuplicateFlag: "+ excludeDuplicateFlag +", searchCriteria:"+ searchCriteria);
		List<CompanyDTO> companies = null;
		try {
			if(encodedFlag != null && searchCriteria != null && encodedFlag.equals(1)) {
				searchCriteria = java.net.URLDecoder.decode(searchCriteria, StandardCharsets.UTF_8.name());
				_log.info("Extracting companies like: "+ searchCriteria);
			}
			companies = privateCompanyService.getPrivateCompanies(searchCriteria,resultCount, countryIds, companyIds, excludeDuplicateFlag,userCountryList);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCMCompanies");
		return new ResponseEntity<List<CompanyDTO>>(companies, HttpStatus.OK);
	}*/
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/basicinfo")
	public ResponseEntity<PrivateCompanyDTO> getBasicInfo(@RequestParam(name="entityId") String entityId){
		_log.info("get Basic Info: "+entityId);
		PrivateCompanyDTO privateCompany = null;
		try {
			
			privateCompany = privateCompanyService.getBasicInfo(entityId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<PrivateCompanyDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<PrivateCompanyDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<PrivateCompanyDTO>(privateCompany, HttpStatus.OK);
	}
	
	@GetMapping("/privatecompanies/{entityId}/latestFilingInfo")
	public ResponseEntity<List<CompanyLatestFilingInfoDTO>> getLatestFilingInfo(
			@PathVariable(name="entityId") String entityId){
		_log.info("get Latest Filing Info for entityId id: "+ entityId);
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = null;
		try {
			companyLatestFilingInfoDTOs = privateCompanyService.getCompanyLatestFilingInfo(entityId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyLatestFilingInfoDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<CompanyLatestFilingInfoDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyLatestFilingInfoDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<CompanyLatestFilingInfoDTO>>(companyLatestFilingInfoDTOs, HttpStatus.OK);
	}

}
