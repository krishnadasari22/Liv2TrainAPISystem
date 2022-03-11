package com.televisory.bond.controller;

import io.swagger.annotations.Api;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.bond.Model.CDSResponseModel;
import com.televisory.bond.dto.CDSBalanceModelDTO;
import com.televisory.bond.dto.CDSComparableDTO;
import com.televisory.bond.dto.CDSNameDTO;
import com.televisory.bond.service.IBondService;

@Controller
@RequestMapping(value="cds")
@Api(value = "bond", description = "Rest API for Capital Market", tags = "Bond/CDS API")
public class CdsController {
	
	public static Logger _log = Logger.getLogger(CdsController.class);

	@Autowired
	IBondService cdsService;
	
	@GetMapping("/currencyList")
	public ResponseEntity<List<String>> getCdsCurrencyList(@RequestParam(value="sector",required=false) String sector) {
			
		List<String> currencyList = null;
		try {
			if(sector!="" && sector!=null)
				sector = java.net.URLDecoder.decode(sector, StandardCharsets.UTF_8.name());
			
			currencyList=cdsService.getCdsCurrencyList(sector);
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage() != null && e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(currencyList, HttpStatus.OK);
	}
	
	
	@GetMapping("/cdsNameList")
	public ResponseEntity<List<CDSNameDTO>> getCdsNameList(
			@RequestParam(name="sector",required=false) String sector,
			@RequestParam(name="currency",required=false) String currency,
			@RequestParam(name="searchCriteria",required=false) String searchCriteria) {
		
		List<CDSNameDTO> nameList;
		
		try {
			if(searchCriteria!="" && searchCriteria!=null)
				searchCriteria = java.net.URLDecoder.decode(searchCriteria, StandardCharsets.UTF_8.name());
			if(sector!="" && sector!=null)
				sector = java.net.URLDecoder.decode(sector, StandardCharsets.UTF_8.name());
			
			nameList= cdsService.getCdsNameList(sector, currency, searchCriteria);
			
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CDSNameDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CDSNameDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<CDSNameDTO>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/cdsData")
	public ResponseEntity<CDSResponseModel> getCDSData(
			@RequestParam(name="identifier",required=false) String identifier,
			@RequestParam(name="fieldName",required=false) String fieldName) {
		
		CDSResponseModel cdsData;
		
		if(identifier == null || identifier.equals("")) {
			identifier = cdsService.getDefaultCDSIdentifier();
		}
		
		try {
			cdsData= cdsService.getCDSData(identifier, fieldName);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<CDSResponseModel>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<CDSResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CDSResponseModel>(cdsData, HttpStatus.OK);
	}
	
	@GetMapping("/comparable")
	public ResponseEntity<List<CDSComparableDTO>> getComparable(
			@RequestParam(name="identifier",required=true) String identifier,
			@RequestParam(name="includeIdentifier",required=false) List<String> includeIdentifier,
			@RequestParam(name="excludeIdentifier",required=false) List<String> excludeIdentifier) {
		
		List<CDSComparableDTO> cdsData;
		_log.info("getting CDS comparable for identifier: "+ identifier +", includeIdentifier: "+includeIdentifier +", excludeIdentifier: "+ excludeIdentifier);
		try {
			_log.info(includeIdentifier);
			/*if(includeIdentifier==null){
				includeIdentifier = new ArrayList<String>();
				includeIdentifier.addAll(CDSMapping.DEFAULT_CDS_COMPARABLE);
			}*/
			_log.info(includeIdentifier);
			cdsData= cdsService.getComparable(identifier, includeIdentifier, excludeIdentifier);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CDSComparableDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CDSComparableDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<CDSComparableDTO>>(cdsData, HttpStatus.OK);
	}
	
	/* @GetMapping("/sectorList")
	public ResponseEntity<List<String>> getCdsSectorList() {
			
		List<String> countryList = null;
		try {
			
			countryList=cdsService.getCdsSectorList();
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(countryList, HttpStatus.OK);
	}*/
	
	@GetMapping("/metricList")
	public ResponseEntity<List<CDSBalanceModelDTO>> getCdsMetricList() {
		
		List<CDSBalanceModelDTO> metricList=null;
		
		try {
			metricList=cdsService.getCdsMetricList();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CDSBalanceModelDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CDSBalanceModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<CDSBalanceModelDTO>>(metricList, HttpStatus.OK);
	}
	
	/*@GetMapping("/latest-data")
	public ResponseEntity<CDSDataLatestDTO> getLatestDateData(CdsRequestModel cdsRequestModel) {
		
		CDSDataLatestDTO latestData;
		try {
			
			latestData= cdsService.getLatestDateData(cdsRequestModel);
			
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<CDSDataLatestDTO>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<CDSDataLatestDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CDSDataLatestDTO>(latestData, HttpStatus.OK);
	}

	@GetMapping("/historical-data")
	public ResponseEntity<List<CDSHistoricalDataDTO>> getHistoricalData(CdsRequestModel cdsRequestModel) {
		
		List<CDSHistoricalDataDTO> historicalData;
		try {
			
			historicalData= cdsService.getHistoricalData(cdsRequestModel);
			
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CDSHistoricalDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CDSHistoricalDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<CDSHistoricalDataDTO>>(historicalData, HttpStatus.OK);
	}*/
}
