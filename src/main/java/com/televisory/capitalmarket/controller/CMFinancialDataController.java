package com.televisory.capitalmarket.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcompany.model.BenchmarkCompanyResponse;
import com.televisory.capitalmarket.dto.BenchMarkCompanyDTO;
import com.televisory.capitalmarket.dto.BenchMarkCompanyOldDTO;
import com.televisory.capitalmarket.dto.BenchmarkCompanyNewDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.service.CMFinancialDataService;
import com.televisory.capitalmarket.service.InteractiveComparisonService;
import com.televisory.capitalmarket.service.RatioService;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;

@RestController
@Api(value = "EquityFactSet", description = "Rest API for FactSet", tags = "CM Equity Financial API")
public class CMFinancialDataController {

	Logger _log = Logger.getLogger(CMFinancialDataController.class);
	
	@Autowired
	CMFinancialDataService cmFinancialDataService;
	
	@Autowired
	RatioService ratioService;

	@Autowired
	InteractiveComparisonService icService;
	
	@GetMapping("/companies/{companyId}/financial/{financialType}")
	public ResponseEntity<List<CompanyFinancialMINDTO>> getCompanyFinancial(@PathVariable(name="companyId") String companyId
			,@PathVariable("financialType") String financialType,
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency, @RequestParam(value="restated",required=false) Boolean restated,@RequestParam(value="entityType",required=false) String entityType){
		_log.info("get Company Financial for company Id: "+ companyId +", financialType: "+ financialType +", periodType: "+ periodType +", startDate: "+ startDate +", endDate: "+ endDate +", currency: "+ currency +", restated: "+ restated);
		List<CompanyFinancialMINDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			if(entityType== null || entityType.equalsIgnoreCase(CMStatic.ENTITY_TYPE_PUBLIC)) {
				financialData = cmFinancialDataService.getCompanyFinancial(companyId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency, restated);
				if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
					financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency));
				} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
					financialData = ratioService.getCompanyRatio(companyId, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency);
				}
			}else {
				financialData = cmFinancialDataService.getPrivateCompanyFinancial(companyId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency, restated);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyFinancialMINDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<CompanyFinancialMINDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyFinancialMINDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<CompanyFinancialMINDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("/companies/{companyId}/latestFilingInfo")
	public ResponseEntity<List<CompanyLatestFilingInfoDTO>> getLatestFilingInfo(
			@PathVariable(name="companyId") String companyId){
		_log.info("get Latest Filing Info for company id: "+ companyId);
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = null;
		try {
			companyLatestFilingInfoDTOs = cmFinancialDataService.getCompanyLatestFilingInfo(companyId);
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
	
	
	@GetMapping("/companies/relativeperformance")
	public ResponseEntity<List<Map<String,Object>>> getCompanyRelativePerformance(@RequestParam(value = "companyId",required=false) String companyId,
			@RequestParam(value="indexId",required=false) String indexId,@RequestParam(value="currency",required=false) String currency) {
		_log.info("Getting Relative performance for company: "+ companyId +" Index -"+indexId);
		List<Map<String,Object>> relativePerformanceObject = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			relativePerformanceObject=cmFinancialDataService.getCompanyRelativePerformance(companyId,indexId,currency);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<Map<String,Object>>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<Map<String,Object>>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyRelativePerformance");
		return new ResponseEntity<List<Map<String,Object>>>(relativePerformanceObject, HttpStatus.OK);
	}
	
	
	@GetMapping("/companies/{companyId}/benchmarkcompanies")
	public ResponseEntity<List<BenchMarkCompanyOldDTO>> getbenchMarkCompaniesByCompanyId(@PathVariable(value = "companyId",required=false) String companyId,
			@RequestParam(value="resultLimit") Integer resultLimit,@RequestParam(value="currency",required=false) String currency) {
		
		_log.info("Getting benchMark companies for company: "+ companyId);
		
		List<BenchMarkCompanyOldDTO> benchMarkCompanies = null;
		
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			benchMarkCompanies=cmFinancialDataService.getbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<BenchMarkCompanyOldDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<BenchMarkCompanyOldDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getbenchMarkCompaniesByCompanyId");
		return new ResponseEntity<List<BenchMarkCompanyOldDTO>>(benchMarkCompanies, HttpStatus.OK);
	}
	
	@GetMapping("/companies/{companyId}/editbenchmarkcompanies")
	public ResponseEntity<List<BenchMarkCompanyDTO>> editbenchMarkCompaniesByCompanyId(@PathVariable(value = "companyId",required=false) String companyId,
			@RequestParam(value="resultLimit") Integer resultLimit,@RequestParam(value="currency",required=false) String currency, @RequestParam(value="benchmarkCompanyAdd", required=false) String benchmarkCompanyAdd,@RequestParam(value="benchmarkCompanyRemove" ,required=false) String benchmarkCompanyRemove
			,@RequestParam(value="periodType") String periodType) {
		
		_log.info("Getting benchMark companies for company: "+ companyId);
		
		List<BenchMarkCompanyDTO> benchMarkCompanies = null;
		
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			if(periodType !=null) {
				periodType = periodType.toUpperCase();
			}
			benchMarkCompanies=cmFinancialDataService.editbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency,benchmarkCompanyAdd, benchmarkCompanyRemove,periodType);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<BenchMarkCompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<BenchMarkCompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getbenchMarkCompaniesByCompanyId");
		return new ResponseEntity<List<BenchMarkCompanyDTO>>(benchMarkCompanies, HttpStatus.OK);
	}

	@GetMapping("/companies/{companyId}/editbenchmarkcompaniesnew")
	public ResponseEntity<List<BenchmarkCompanyNewDTO>> editbenchMarkCompaniesByEnityId(@PathVariable(value = "companyId",required=false) String companyId,
			@RequestParam(value="resultLimit") Integer resultLimit,@RequestParam(value="currency",required=false) String currency, @RequestParam(value="benchmarkCompanyAdd", required=false) String benchmarkCompanyAdd,@RequestParam(value="benchmarkCompanyRemove" ,required=false) String benchmarkCompanyRemove
			,@RequestParam(value="periodType") String periodType, String entityType) {
		
		_log.info("Getting benchMark companies for company: "+ companyId+" entityType:"+entityType);
		
		List<BenchmarkCompanyNewDTO> benchMarkCompanies = null;
		
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			if(periodType !=null) {
				periodType = periodType.toUpperCase();
			}
			benchMarkCompanies=cmFinancialDataService.editbenchMarkCompaniesByEntityId(companyId,resultLimit,currency,benchmarkCompanyAdd, benchmarkCompanyRemove,periodType, entityType);
		
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<BenchmarkCompanyNewDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<BenchmarkCompanyNewDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getbenchMarkCompaniesByCompanyId");
		return new ResponseEntity<List<BenchmarkCompanyNewDTO>>(benchMarkCompanies, HttpStatus.OK);
	}
	
	@GetMapping("/companies/{companyId}/new/editbenchmarkcompanies")
	public ResponseEntity<BenchmarkCompanyResponse> getEditbenchMarkCompaniesByEnityId(@PathVariable(value = "companyId",required=false) String companyId,
			@RequestParam(value="resultLimit") Integer resultLimit,@RequestParam(value="currency",required=false) String currency, @RequestParam(value="benchmarkCompanyAdd", required=false) String benchmarkCompanyAdd,@RequestParam(value="benchmarkCompanyRemove" ,required=false) String benchmarkCompanyRemove
			,@RequestParam(value="periodType") String periodType, String entityType) {
		
		_log.info("Getting benchMark companies for company: "+ companyId+" entityType:"+entityType);
		
		BenchmarkCompanyResponse response = null;		
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			if(periodType !=null) {
				periodType = periodType.toUpperCase();
			}
			List<BenchmarkCompanyNewDTO> benchMarkCompanies = null;
			benchMarkCompanies=cmFinancialDataService.editbenchMarkCompaniesByEntityId(companyId,resultLimit,currency,benchmarkCompanyAdd, benchmarkCompanyRemove,periodType, entityType);
			response = new BenchmarkCompanyResponse();
			response.setData(benchMarkCompanies);
		} catch (Exception e) {
			_log.error("failed in getEditbenchMarkCompaniesByEnityId",e);
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<BenchmarkCompanyResponse>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause()!=null && e.getCause().getCause()!=null 
					&& e.getCause().getCause().getMessage().contains("NO REVENUE IS AVAILABLE")){
				response = new BenchmarkCompanyResponse();
				response.setMessage("Revenue Not Available to Create Revenue Comps");
				response.setStatus(HttpStatus.NO_CONTENT);
				return new ResponseEntity<BenchmarkCompanyResponse>(response, HttpStatus.OK);
			}
			return new ResponseEntity<BenchmarkCompanyResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getbenchMarkCompaniesByCompanyId");
		return new ResponseEntity<BenchmarkCompanyResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/companies/{companyId}/industry/ratios/{fieldName}")
	public ResponseEntity<List<CompanyFinancialDTO>> getFactSetRatio(@PathVariable(name="companyId") String companyId,
			@PathVariable(name="fieldName") String ratioFieldName,
			@RequestParam("periodType") String periodType,
			@RequestParam(value="currency", required=false) String currency){
		List<CompanyFinancialDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			
			//financialData = ratioService.getCompanyRatio(companyId, ratioFieldName, periodType, currency, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyFinancialDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<CompanyFinancialDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyFinancialDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyFinancialDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("financial/parameter/{ff_industry}")
	public ResponseEntity<List<BalanceModelDTO>> getFinancialParameter(
			@PathVariable(name="ff_industry") String ffIndustry,
			@RequestParam(value="distinctParams", required=false, defaultValue="false") Boolean distinctParams,
			@RequestParam(value="watchlistFlag", required=false, defaultValue="false") Boolean watchlistFlag,
			@RequestParam(value="icFlag", required=false, defaultValue="false") Boolean icFlag,
			@RequestParam(value="screenerFlag", required=false, defaultValue="false") Boolean screenerFlag){
		List<BalanceModelDTO> ratioData = null;
		_log.info("requert fianancial params for: "+ ffIndustry +", distinct: "+ distinctParams+", watchlistFlag: "+watchlistFlag+", icFlag: "+icFlag+", screenerFlag: "+screenerFlag);
		try {
			
			if(distinctParams) {
				ratioData = icService.getDistinctFinancialParameter(ffIndustry.trim(),watchlistFlag,icFlag,screenerFlag);
			} else {
				ratioData = icService.getFinancialParameter(ffIndustry.trim(),watchlistFlag,icFlag,screenerFlag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<BalanceModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<BalanceModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<BalanceModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("returning total "+ ratioData.size() +" fianancial params for: "+ ffIndustry +", distinct: "+ distinctParams);
		return new ResponseEntity<List<BalanceModelDTO>>(ratioData, HttpStatus.OK);
	}
	
/*	@GetMapping("/financial/periodicity")
	public ResponseEntity<List<String>> getFinancialPeriodicity() {
		
		List<String> periodicityList = new ArrayList<>();
		
		try {
			periodicityList.add(CMStatic.PERIODICITY_QUARTERLY);
			periodicityList.add(CMStatic.PERIODICITY_YEARLY);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<String>>(periodicityList, HttpStatus.OK);
	}*/
	@GetMapping("/financial/{companyId}/periodicity")
	public ResponseEntity<List<String>> getFinancialPeriodicity(@PathVariable(value="companyId", required=true) String companyId) {
		
		List<String>periodicityList=new ArrayList<>();
		try {
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = cmFinancialDataService.getCompanyLatestFilingInfo(companyId);
		if(null!=companyLatestFilingInfoDTOs) {
			for (CompanyLatestFilingInfoDTO companyLatestFilingInfoDTO : companyLatestFilingInfoDTOs) {
				periodicityList.add(companyLatestFilingInfoDTO.getPeriodType());
			}
		}
		}catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<String>>(periodicityList, HttpStatus.OK);
	}
	
	@GetMapping("/companies/{companyId}/benchmarkLatestFilingInfo")
	public ResponseEntity<List<CompanyLatestFilingInfoDTO>> getBenchmarkLatestFilingInfo(
			@PathVariable(name="companyId") String companyId){
		_log.info("get benchmark latest periodicity for company id: "+ companyId);
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = null;
		try {
			companyLatestFilingInfoDTOs = cmFinancialDataService.getBenchmarkLatestFilingInfo(companyId);
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
		_log.info("out getBenchmarkLatestFilingInfo");
		return new ResponseEntity<List<CompanyLatestFilingInfoDTO>>(companyLatestFilingInfoDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/companies/{companyId}/benchmarkPeriods")
	public ResponseEntity<List<CompanyLatestFilingInfoDTO>> getBenchmarkPeriods(@PathVariable(name="companyId") String companyId){
		_log.info("get benchmark quarterly or semianuually periods for company id: "+ companyId);
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = null;
		try {
			companyLatestFilingInfoDTOs = cmFinancialDataService.getBenchmarkPeriods(companyId);
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
		_log.info("out getBenchmarkPeriods");
		return new ResponseEntity<List<CompanyLatestFilingInfoDTO>>(companyLatestFilingInfoDTOs, HttpStatus.OK);
	}
	
	
}
