package com.televisory.capitalmarket.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.entities.cm.IndustryMetaData;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.entities.cm.TicsSector;
import com.televisory.capitalmarket.entities.industry.CountryList;
import com.televisory.capitalmarket.service.SectorService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value="industry")
@Api(value = "Industry", description = "Rest API for Industry", tags = "CM Industry API")


public class IndustryController {

	@Autowired
	SectorService industryService;
	
	Logger _log = Logger.getLogger(IndustryController.class);
	
	@GetMapping("/countries")
	public ResponseEntity<List<CountryList>> getEconomyCountries(
			@RequestParam(value="ticsSectorCode", required=false) String ticsSectorCodeList, 
			@RequestParam(value="ticsIndustryCode", required=false) String ticsIndustryCodeList,
			@RequestParam(value="periodType",required=false) String periodType,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
			@RequestParam(value="userCountryIdList",required=false) String userCountryIdList) {
		_log.info("getting countries for ticsSectorCode: "+ ticsSectorCodeList +", ticsIndustryCode: "+ ticsIndustryCodeList +", periodType: "+ periodType +", startDate: "+ startDate +", endDate: "+ endDate +", userCountryIdList: "+ userCountryIdList);
		List<CountryList> countryList = null;
		List<String> userCountyList = null;
		if(userCountryIdList!=null){
			userCountyList = Arrays.asList(userCountryIdList.split("\\s*,\\s*"));
		}
		try {
			countryList = industryService.findAllIndustryCountries(ticsSectorCodeList, ticsIndustryCodeList, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,userCountyList);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<CountryList>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CountryList>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CountryList>>(countryList, HttpStatus.OK);
	} 
	
/*	@GetMapping("/sectors/period")
	public ResponseEntity<List<Sector>> getSectorPeriod() {
		List<Sector> sectorsPeriod = null;
		try {
			sectorsPeriod = industryService.getSectorPeriod();
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<Sector>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<Sector>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Sector>>(sectorsPeriod, HttpStatus.OK);
	}*/
	
	
	@GetMapping("/sectors")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getSectors(
			@RequestParam(value="countryId",required=false) String countryId,
			@RequestParam("periodType") String periodType,
			@RequestParam(value="fieldNames",required=false) String fieldNames,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
			@RequestParam(value="month",required=false) Integer month) { 

		_log.info("Extracting All Sector data country: "+ countryId  +", preiod type: "+ periodType +", fieldNames: "+ fieldNames +", currency: "+ currency +", startDate: "+ startDate +", endDate: "+ endDate +", month: "+ month );
		List<IndustryFinancialDataDTO> sectors = null;
		List<String> userCountyList = null;
		
		if(countryId!=null){
			userCountyList = Arrays.asList(countryId.split("\\s*,\\s*"));
		}
		
		try {
			sectors = industryService.getIDSectorData(periodType, null, userCountyList, fieldNames, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency, month);
			
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(sectors, HttpStatus.OK);
	}
	
	@GetMapping("/sectors/{ticsSectorCode}")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getSectorsByTicsSectorCode(
			@PathVariable(value="ticsSectorCode") String ticsSectorCode,
			@RequestParam(value="countryId",required=false) String countryId, 
			@RequestParam("periodType") String periodType,
			@RequestParam(value="fieldNames",required=false) String fieldNames,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
			@RequestParam(value="month",required=false) Integer month) {

		_log.info("Extracting Sector data for tics sector code: "+ ticsSectorCode +", country: "+ countryId  +", preiod type: "+ periodType +", fieldNames: "+ fieldNames +", currency: "+ currency +", startDate: "+ startDate +", endDate: "+ endDate +", month: "+ month );
		List<IndustryFinancialDataDTO> sectors = null;
		List<String> userCountyList = null;
		if(countryId!=null){
			userCountyList = new ArrayList<>(Arrays.asList(countryId.split(",")));
		}
		
		try {
			sectors = industryService.getIDSectorData(periodType, ticsSectorCode, userCountyList, fieldNames, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency, month);
			
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(sectors, HttpStatus.OK);
	}
	
	@GetMapping("/ticsindustry")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getTicsIndustry(
			@RequestParam(value="ticsSectorCode") String ticsSectorCode,
			@RequestParam(value="countryId",required=false) String countryId, 
			@RequestParam("periodType") String periodType,
			@RequestParam(value="fieldNames",required=false) String fieldNames,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
			@RequestParam(value="month",required=false) Integer month){
		
		_log.info("Extracting All TicsIndustry data for tics sector code: "+ ticsSectorCode +", country: "+ countryId  +", preiod type: "+ periodType +", fieldNames: "+ fieldNames +", currency: "+ currency +", startDate: "+ startDate +", endDate: "+ endDate +", month: "+ month );
		List<IndustryFinancialDataDTO> sectors = null;
			
		try {
			sectors = industryService.getIDIndustryData(periodType, ticsSectorCode, null, countryId, fieldNames, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency, month);
			
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(sectors, HttpStatus.OK);
	}
	
	@GetMapping("/ticsindustry/{ticsIndustryCode}")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getTicsIndustryByTicsIndustryCode(
			@PathVariable(value="ticsIndustryCode") String ticsIndustryCode ,
			@RequestParam(value="ticsSectorCode") String ticsSectorCode,
			@RequestParam(value="countryId",required=false) String countryId, 
			@RequestParam("periodType") String periodType,
			@RequestParam(value="fieldNames",required=false) String fieldNames,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
			@RequestParam(value="month",required=false) Integer month) {
		
		_log.info("Extracting TicsIndustry data for tics sector code: "+ ticsSectorCode +", ticsIndustryCode: "+ ticsIndustryCode +", country: "+ countryId  +", preiod type: "+ periodType +", fieldNames: "+ fieldNames +", currency: "+ currency +", startDate: "+ startDate +", endDate: "+ endDate +", month: "+ month );
		List<IndustryFinancialDataDTO> sectors = null;
			
		try {
			sectors = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, countryId, fieldNames, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency, month);
			
			
			
		}catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(sectors, HttpStatus.OK);
	}
	
	@GetMapping("/company")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getCompany(
			@RequestParam(value="ticsIndustryCode") String ticsIndustryCode,
			@RequestParam(value="companyId", required=false) String companyId,
			@RequestParam(value="countryId",required=false) String countryId, 
			@RequestParam("periodType") String periodType,
			@RequestParam(value="fieldNames",required=false) String fieldNames,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
			@RequestParam(value="month",required=false) Integer month) {
		
		_log.info("Extracting Industry-Company data for ticsIndustryCode: "+ ticsIndustryCode +", country: "+ countryId  +", preiod type: "+ periodType +", fieldNames: "+ fieldNames +", currency: "+ currency +", startDate: "+ startDate +", endDate: "+ endDate +", month: "+ month );
		
		_log.info(" countryId: "+ countryId +", ticsIndustryCode: "+ticsIndustryCode);
		List<IndustryFinancialDataDTO> sectors = null;
		
		
		try {
				sectors = industryService.getIDCompanyData(periodType, ticsIndustryCode, companyId, countryId, fieldNames, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency, month,null);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(sectors, HttpStatus.OK);
	}
	
	
	@GetMapping("/sectorList")
	public ResponseEntity<List<TicsSector>> getAllSectors(@RequestParam(value="ticsSectorCode",required=false) String ticsSectorCode) {
		List<TicsSector> sectorList = null;
		try {
			if(null==ticsSectorCode) {
			sectorList = industryService.getAllSectors();
			}
			else {
				sectorList=industryService.getSectorBySectorCode(ticsSectorCode);
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<TicsSector>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<TicsSector>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<TicsSector>>(sectorList, HttpStatus.OK);
	}
	
	
	@GetMapping("/ticsIndustryList")
	public ResponseEntity<List<TicsIndustry>> getTicsIndustryByTicsSectorCode(@RequestParam(value="ticsSectorCode",required=false) String ticsSectorCode,
									@RequestParam(value="factset_industry",required=false) String factsetIndustry,
									@RequestParam(value="countryList",required=false) String countryList,
									@RequestParam(value="resultCount",required=false) Integer resultCount,
									@RequestParam(value="searchParam",required=false) String searchParam,
	                                @RequestParam(value="ticsIndustryCode",required=false) String ticsIndustryCode){
		
		_log.info("Extracting TicsIndustry data for tics sector code: "+ ticsSectorCode +" factset Industry : "+factsetIndustry+" country : "+countryList);
		
		List<TicsIndustry> ticsIndustry = null;
		
		try {
			if(ticsIndustryCode==null) {
			if(ticsSectorCode != null && factsetIndustry==null) {
				ticsIndustry = industryService.getTicsIndustryByTicsSectorCode(ticsSectorCode);
			}else if(ticsSectorCode == null && factsetIndustry!=null){
				ticsIndustry = industryService.getTicsIndustryByFactsetIndustry(factsetIndustry);
			}else if(ticsSectorCode != null && factsetIndustry!=null){
				ticsIndustry = industryService.getTicsIndustryByTicsSectorCode(ticsSectorCode,factsetIndustry);
			}else if(countryList!=null){
				ticsIndustry = industryService.getTicsIndustryByCountryCode(countryList);
			}else if(searchParam!=null){
				_log.info(" searchParam "+searchParam);
				ticsIndustry = industryService.getTicsIndustryBySearchParam(searchParam,resultCount);
			}else{
				ticsIndustry = industryService.getTicsIndustryByTicsSectorCode();
			}
			}
			else {
				ticsIndustry = industryService.getTicsIndustryByTicsIndustryCode(ticsIndustryCode);
			}
			
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<TicsIndustry>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<TicsIndustry>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<TicsIndustry>>(ticsIndustry, HttpStatus.OK);
	}
	
	
	@GetMapping("/{ff_industry_type}/financial/parameter")
	public ResponseEntity<List<BalanceModelDTO>> getIcomFinancialParameter(@PathVariable(name="ff_industry_type") String industryType,
			@RequestParam(value="financialType",required=false) String financialType,
			@RequestParam(value="watchlistFlag",required=false) Integer watchlistFlag,
			@RequestParam(value="icFlag",required=false) Integer icFlag) {
		
		List<BalanceModelDTO> financialParameters = null;
		
		try {	
			
			 if(icFlag != null ){
					financialParameters = industryService.getIndustryFinancialParameterForIC(industryType,icFlag);
				}else if(watchlistFlag == null || watchlistFlag == 0){
					financialParameters = industryService.getIndustryFinancialParameter(industryType, financialType);
				}else{
					financialParameters = industryService.getIndustryFinancialParameter(industryType, financialType, watchlistFlag);
				}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<BalanceModelDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<BalanceModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<BalanceModelDTO>>(financialParameters, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/ticsIndustryList/metadata")
	public ResponseEntity<List<TicsIndustry>> getCMCompaniesByIdList(@RequestParam(value="industryIdList") String industryIdList) {

		List<TicsIndustry> ticsIndustry = null;

		try {
			List<String> customIndustryIds = Arrays.asList(industryIdList.split("\\s*,\\s*"));
			ticsIndustry = industryService.getIndustryByIdList(customIndustryIds);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<TicsIndustry>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<TicsIndustry>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<TicsIndustry>>(ticsIndustry, HttpStatus.OK);
	}	
	
	@GetMapping("/period")
	public ResponseEntity<List<IndustryMetaData>> getPeriod(@RequestParam(value="ticsIndustryCode",required=false) String ticsIndustryCode,
									@RequestParam(value="countryId",required=false) String countryId,@RequestParam(value="ticsSectorCode",required=false) String ticsSectorCode) {
		
		_log.info("Extracting period for ticsIndustryCode: "+ ticsIndustryCode +" countryCode : "+countryId + "ticsSectorCode: "+ticsSectorCode);
		List<IndustryMetaData> industryMetaData = null;
		String countryCode = null;
		if(countryId !=null && !countryId.equals("") && !countryId.equals("0") ) {
			countryCode = industryService.getCountryDomocileCode(countryId);
			_log.info(countryCode);
		}
		
		try {
			/*if(countryCode!=null) {*/
				industryMetaData = industryService.getPeriodByTicsIndustryNCountryCode(ticsIndustryCode,countryCode,ticsSectorCode);
				//_log.info(industryMetaData);
			/*}else{
				industryMetaData = industryService.getPeriodByTicsIndustryCode(ticsIndustryCode);
			}*/
			
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndustryMetaData>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndustryMetaData>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryMetaData>>(industryMetaData, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/defaultcountry")
	public ResponseEntity<CompanyDTO> getDefaultCountryForIndustry(@RequestParam(value="countryCode") List<String> countryCode) {

		CompanyDTO company = null;
		try {
			_log.info(" countryCode "+countryCode);
			company = industryService.getDefaultCountryForIndustry(countryCode);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<CompanyDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<CompanyDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CompanyDTO>(company, HttpStatus.OK);
	}
}
