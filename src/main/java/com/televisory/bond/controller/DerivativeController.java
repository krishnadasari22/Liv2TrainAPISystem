package com.televisory.bond.controller;

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

import com.televisory.bond.Model.DerivativeResponseModel;
import com.televisory.bond.dto.DerFuCoparableDTO;
import com.televisory.bond.dto.DerOpCoparableDTO;
import com.televisory.bond.dto.DerivativeNameDTO;
import com.televisory.bond.entity.DerivativeIdentifierWithProp;
import com.televisory.bond.service.DerivativeService;
import com.televisory.bond.utils.BondStaticParams;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value="derivative")
@Api(value = "bond", description = "Rest API for Capital Market", tags = "Bond/CDS API")
public class DerivativeController {
	
	public static Logger _log = Logger.getLogger(DerivativeController.class);
	
	@Autowired
	DerivativeService derivativeService;
	
	@GetMapping("/derivative-name")
	public ResponseEntity<List<DerivativeNameDTO>> getDerivativeNameList(@RequestParam(value="searchCriteria",required=false) String searchCriteria) {
			
		List<DerivativeNameDTO> nameList = null;
		try {
			
			if(searchCriteria!=null){
				searchCriteria = java.net.URLDecoder.decode(searchCriteria, StandardCharsets.UTF_8.name());
			}
			
			nameList=derivativeService.getDerivativeNameList(searchCriteria);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<DerivativeNameDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<DerivativeNameDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<DerivativeNameDTO>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/category")
	public ResponseEntity<List<String>> getDerivativeCategoryList(@RequestParam(value="derivativeName",required=false) String derivativeName) {
			
		List<String> nameList = null;
		try {
			
			if(derivativeName!=null){
				derivativeName = java.net.URLDecoder.decode(derivativeName, StandardCharsets.UTF_8.name());
			}
			
			nameList=derivativeService.getDerivativeCategoryList(derivativeName);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/expiryDate")
	public ResponseEntity<List<DerivativeIdentifierWithProp>> getDerivativeExpiryDateList(@RequestParam(value="derivativeName",required=false) String derivativeName,
			@RequestParam(value="category",required=false) String category) {
			
		List<DerivativeIdentifierWithProp> nameList = null;
		try {
			
			if(derivativeName!=null){
				derivativeName = java.net.URLDecoder.decode(derivativeName, StandardCharsets.UTF_8.name());
			}
			
			nameList=derivativeService.getDerivativeExpiryDateList(derivativeName,category);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<DerivativeIdentifierWithProp>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<DerivativeIdentifierWithProp>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<DerivativeIdentifierWithProp>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/derivativeData")
	public ResponseEntity<DerivativeResponseModel> getDerivativeData(
			@RequestParam(value="identifier",required=false) String identifier,
			@RequestParam(name="fieldName",required=false) String fieldName) {
			
		DerivativeResponseModel derivativeResponseModel = null;
		
		if(identifier == null || identifier.equals("")) {
			identifier = derivativeService.getDefaultIdentifier();
		}
		
		if(fieldName == null || fieldName.equals("")) {
			fieldName = BondStaticParams.ETD_DEFAULT_FIELD_NAME;
		}
		
		try {
			derivativeResponseModel=derivativeService.getDerivativeData(identifier, fieldName);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<DerivativeResponseModel>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<DerivativeResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<DerivativeResponseModel>(derivativeResponseModel, HttpStatus.OK);
	}
	
	@GetMapping("/derivativeOptionType")
	public ResponseEntity<List<String>> getDerivativeOptionType(@RequestParam(value="derivativeName",required=false) String derivativeName,
			@RequestParam(value="category",required=false) String category,
			@RequestParam(value="expiryDate",required=false) String expiryDate){
			//@RequestParam(value="expiryDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date ) {
			
		List<String> nameList = null;
		try {
			
			if(derivativeName!=null){
				derivativeName = java.net.URLDecoder.decode(derivativeName, StandardCharsets.UTF_8.name());
			}
			
			nameList=derivativeService.getDerivativeOptionType(derivativeName,category,expiryDate);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<String>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/strikePrice")
	public ResponseEntity<List<DerivativeIdentifierWithProp>> getDerivativeStrikePrice(@RequestParam(value="derivativeName",required=false) String derivativeName,
			@RequestParam(value="category",required=false) String category,@RequestParam(value="expiryDate",required=false) String expiryDate,
			@RequestParam(value="optionType",required=false) String optionType) {
			
		List<DerivativeIdentifierWithProp> nameList = null;
		try {
			
			if(derivativeName!=null){
				derivativeName = java.net.URLDecoder.decode(derivativeName, StandardCharsets.UTF_8.name());
			}
			
			nameList=derivativeService.getDerivativeStrikeList(derivativeName, category, expiryDate, optionType);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<DerivativeIdentifierWithProp>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<DerivativeIdentifierWithProp>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<DerivativeIdentifierWithProp>>(nameList, HttpStatus.OK);
	}
	
	@GetMapping("/futureComparable")
	public ResponseEntity<List<DerFuCoparableDTO>> getFutureComparable(
			@RequestParam(name="identifier",required=true) String identifier) {
		
		List<DerFuCoparableDTO> derFuCoparableDTOs;
		_log.info("getting comparable for Future identifier: "+identifier );
		try {
			derFuCoparableDTOs= derivativeService.getFuComparable(identifier);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<DerFuCoparableDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<DerFuCoparableDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<DerFuCoparableDTO>>(derFuCoparableDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/optionComparable")
	public ResponseEntity<List<DerOpCoparableDTO>> getOptionComparable(
			@RequestParam(name="identifier",required=true) String identifier) {
		
		List<DerOpCoparableDTO> derOpCoparableDTOs;
		_log.info("getting comparable for option identifier: "+identifier );
		try {
			derOpCoparableDTOs= derivativeService.getOpComparable(identifier);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<DerOpCoparableDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<DerOpCoparableDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<DerOpCoparableDTO>>(derOpCoparableDTOs, HttpStatus.OK);
	}
}
