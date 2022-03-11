package com.televisory.capitalmarket.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.service.CMFinancialDataService;
import com.televisory.capitalmarket.service.MobileAppResponseService;
import com.televisory.capitalmarket.service.RatioService;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Mobile", description = "Rest API for Mobile app", tags = "CM API for Mobile App")

public class MobileAppController {

	Logger _log = Logger.getLogger(MobileAppController.class);
	
	@Autowired
	MobileAppResponseService mobileAppResponseService;
	
	@Autowired
	CMFinancialDataService cmFinancialDataService;
	
	@Autowired
	RatioService ratioService;
	
	@GetMapping("/mobile/companies/{companyId}/editbenchmarkcompanies")
	public ResponseEntity<String> editbenchMarkCompaniesByCompanyId(@PathVariable(value = "companyId",required=false) String companyId,
			@RequestParam(value="resultLimit") Integer resultLimit,@RequestParam(value="currency",required=false) String currency, @RequestParam(value="benchmarkCompanyAdd", required=false) String benchmarkCompanyAdd,@RequestParam(value="benchmarkCompanyRemove" ,required=false) String benchmarkCompanyRemove
			,@RequestParam(value="periodType") String periodType) {
		
		_log.info("Getting benchMark companies for company: "+ companyId);
		
		JSONObject responeJson = null;
		try {
			
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			if(periodType !=null) {
				periodType = periodType.toUpperCase();
			}
			
			responeJson=mobileAppResponseService.editbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency,benchmarkCompanyAdd, benchmarkCompanyRemove,periodType);
		
		} catch (Exception e) {e.getStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
			
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		/*responeJson.addProperty("data", benchMarkCompanies.toString());
		responeJson.addProperty("msg", "");*/
		return new ResponseEntity<String>(responeJson.toString(), HttpStatus.OK);
	}
	
	
	@GetMapping("mobile/companies/{companyId}/financial/{financialType}")
	public ResponseEntity<String> getCompanyFinancial(@PathVariable(name="companyId") String companyId
			,@PathVariable("financialType") String financialType,
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency, @RequestParam(value="restated",required=false) Boolean restated){
		
		_log.info("get Company Financial for company Id: "+ companyId +", financialType: "+ financialType +", periodType: "+ periodType +", startDate: "+ startDate +", endDate: "+ endDate +", currency: "+ currency +", restated: "+ restated);
		
		List<CompanyFinancialMINDTO> financialData = null;
		
		JSONObject financialDataResponse = null;
		
		try {
			
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			
			if(periodType!=null){
				if(periodType.equalsIgnoreCase("half-yearly")) {
					periodType = CMStatic.PERIODICITY_HALF_YEARLY;
				}else{
					periodType = periodType.toUpperCase();
				}	
			}
			
			financialData = cmFinancialDataService.getCompanyFinancial(companyId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency, restated);
			
			if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
				financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency));
			} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
				financialData = ratioService.getCompanyRatio(companyId, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null, currency);
			}
			
			financialDataResponse = mobileAppResponseService.getCompanyFinancial(financialData);
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<String>(financialDataResponse.toString(), HttpStatus.OK);
	}
}
