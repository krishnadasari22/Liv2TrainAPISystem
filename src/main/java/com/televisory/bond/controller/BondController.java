package com.televisory.bond.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.bond.Model.FIResponseModel;
import com.televisory.bond.aspect.TrackExecutionTime;
import com.televisory.bond.dto.FIComparableDTO;
import com.televisory.bond.dto.FINameDTO;
import com.televisory.bond.service.FiService;
import com.televisory.bond.service.IBondService;
import com.televisory.bond.utils.BondStaticParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "bond", description = "Rest API for Capital Market", tags = "Bond/CDS API")
public class BondController{

	public static Logger _log = Logger.getLogger(BondController.class);

	@Autowired
	IBondService bondService;
	
	@Autowired
	FiService fiService;

	@TrackExecutionTime
	@GetMapping(value = "getBond")
	@ApiOperation(value = "Test any bond service")
	public ResponseEntity<String> getBondInitialParams() {
		try{
			System.out.println("In the bond Dummy ");
		}catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("No Data Found")){
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<String>(headers,HttpStatus.NO_CONTENT);
			}

			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@TrackExecutionTime
	@ApiOperation(value = "get the filtered Value for the global filter")
	@RequestMapping(value = "/getAdvanceSearch", method = RequestMethod.POST, consumes = { "application/json" })
	public 	<T> ResponseEntity<List<T>> advanceSearch(@RequestBody Map<String,String> filters, HttpServletRequest request) {
		
		String requestType = request.getParameter("requesttype");
		List<T> cdsData = new ArrayList<>();
		try{
			cdsData = bondService.getAdvanceSearch(filters , requestType);
		}catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("No Data Found")){
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<List<T>>(headers,HttpStatus.NO_CONTENT);
			}

			if(e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<List<T>>( HttpStatus.BAD_REQUEST);
			}
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<List<T>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<List<T>>(cdsData,HttpStatus.OK);
	}

	@TrackExecutionTime
	@ApiOperation(value = "get the projection Value for the given parameter[Used to get the initial params]")
	@RequestMapping(value = "/getProjectedValues", method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<Map<String, Object>> getProjectedValues(@RequestBody Map<String,String> filters , HttpServletRequest request) {
		String requestType = request.getParameter("requesttype");
		_log.info(requestType);
		Map<String, Object> initialParams = new HashMap<>();
		try{
			List<String> myList = new ArrayList<String>(filters.keySet());
			initialParams = bondService.getProjectedValues(myList,requestType);
		}catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("No Data Found")){
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<Map<String, Object>>(headers,HttpStatus.NO_CONTENT);
			}
			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
			}
			_log.error("SobondServicemething went wrong.Try  after some time: "+ e.getMessage());
			return new ResponseEntity<Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<Map<String, Object>>(initialParams,HttpStatus.OK);
	}


	@TrackExecutionTime
	@ApiOperation(value = "get the projection Value for the given parameter[Used to get the initial params]")
	@RequestMapping(value = "/getCurrentValues", method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<Map<String, Object>> getCurrentValues(@RequestBody Map<String,String> filters) {
		Map<String, Object> initialParams = new HashMap<>();
		try{
			List<String> myList = new ArrayList<String>();
			myList.add("currency");
			myList.add("entity_name");
			initialParams = bondService.getCurrentValues(filters,BondStaticParams.CDS);
		}catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("No Data Found")){
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<Map<String, Object>>(headers,HttpStatus.NO_CONTENT);
			}
			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
			}
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			return new ResponseEntity<Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<Map<String, Object>>(initialParams,HttpStatus.OK);
	}
	
	
	@GetMapping("/fi/currencyList")
	public ResponseEntity<List<String>> getFiCurrencyList(@RequestParam(value="categeory",required=false) String categeory) {
			
		List<String> currencyList = null;
		try {
			if(categeory!=null){
				categeory = java.net.URLDecoder.decode(categeory, StandardCharsets.UTF_8.name());
			}
			currencyList=fiService.getFiCurrencyList(categeory);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(currencyList, HttpStatus.OK);
	}
	
	@GetMapping("/fi/categeory")
	public ResponseEntity<List<String>> getFiCategeoryList() {
			
		List<String> currencyList = null;
		try {
			
			currencyList=fiService.getFiCategeoryList();
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(currencyList, HttpStatus.OK);
	}
	
	
	@GetMapping("/fi/fiNameList")
	public ResponseEntity<List<FINameDTO>> getFiNameList(
			@RequestParam(name="categeory",required=false) String categeory,
			@RequestParam(name="currency",required=false) String currency,
			@RequestParam(name="searchCriteria",required=false) String searchCriteria) {
		
		List<FINameDTO> nameList;
		
		try {
			if(categeory!=null){
				categeory = java.net.URLDecoder.decode(categeory, StandardCharsets.UTF_8.name());
			}
			nameList= fiService.getFiNameList(categeory, currency, searchCriteria);
			
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<FINameDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<FINameDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<FINameDTO>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/fi/fiData")
	public ResponseEntity<FIResponseModel> getFiData(
			@RequestParam(name="identifier",required=false) String identifier,
			@RequestParam(name="fieldName",required=false) String fieldName) {
		
		FIResponseModel cdsData;
		
		if(identifier == null || identifier.equals("")) {
			identifier = fiService.getDefaultFIIdentifier();
		}
		
		if(fieldName == null || fieldName.equals("")) {
			fieldName = BondStaticParams.FIELD_NAME;
		}
		
		try {
			cdsData= fiService.getFiData(identifier, fieldName);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<FIResponseModel>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<FIResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<FIResponseModel>(cdsData, HttpStatus.OK);
	}
	
	
	@GetMapping("/fi/comparable")
	public ResponseEntity<List<FIComparableDTO>> getComparable(
			@RequestParam(name="identifier",required=true) String identifier,
			@RequestParam(name="includeIdentifier",required=false) List<String> includeIdentifier,
			@RequestParam(name="excludeIdentifier",required=false) List<String> excludeIdentifier) {
		
		List<FIComparableDTO> cdsData;
		_log.info("getting FI comparable for identifier: "+ identifier +", includeIdentifier: "+includeIdentifier +", excludeIdentifier: "+ excludeIdentifier);
		try {
			cdsData= fiService.getComparable(identifier, includeIdentifier, excludeIdentifier);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<FIComparableDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<FIComparableDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<FIComparableDTO>>(cdsData, HttpStatus.OK);
	}
	
}
