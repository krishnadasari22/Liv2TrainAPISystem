package com.televisory.capitalmarket.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.ScreenerCompanyFinancialDTO;
import com.televisory.capitalmarket.dto.ScreenerStockPriceDTO;
import com.televisory.capitalmarket.entities.factset.FxConversionRate;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.service.CMFinancialDataService;
import com.televisory.capitalmarket.service.ScreenerService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value="screener")
@Api(value = "Screener", description = "Rest API for Screener", tags = "CM Screener API")
public class ScreenerController {
	@Autowired
	ScreenerService screenerService;
	
	@Autowired
	CMFinancialDataService cmFinancialDataService;
	
	Logger _log = Logger.getLogger(ScreenerController.class);
	
	@GetMapping("/company")
	public ResponseEntity<List<ScreenerCompanyFinancialDTO>> getCompany(@RequestParam(name="ticsIndustryCode")String ticsIndustryCode
			,@RequestParam(value="countryiso",required=false) String countryIso
			) {
		_log.info("screener company list for ticsIndustryCode: "+ ticsIndustryCode + " :::: " + countryIso);
		List<ScreenerCompanyFinancialDTO> sectors = null;
		
		
		try {
				sectors = screenerService.getCompaniesForScreener(ticsIndustryCode,countryIso);
			
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(sectors, HttpStatus.OK);
	}
	
	@GetMapping("/companies/financial")
	public ResponseEntity<List<ScreenerCompanyFinancialDTO>> getCompanyFinancial(@RequestParam(name="subIndustryCode") String subIndustryCode
			,@RequestParam(value="currency",required=false) String currency
			,@RequestParam(value="fieldName") String fieldName
			,@RequestParam(value="domicileCountryCode",required=false) String domicileCountryCode
			){
		
		List<ScreenerCompanyFinancialDTO> financialData = null;
		try {
			_log.info(currency);
			if(currency==null) {
				currency= "USD";
			}
			financialData = screenerService.getCompanyFinancial(subIndustryCode,currency,fieldName,domicileCountryCode);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ScreenerCompanyFinancialDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("/companies/ratio")
	public ResponseEntity<List<CompanyFinancialDTO>> getCompanyFinancialRatio(@RequestParam(name="ticsIndustryCode") String ticsIndustryCode
			,@RequestParam(value="currency",required=false) String currency
			,@RequestParam(value="ratioName") String ratioName
			,@RequestParam(value="domicileCountryCode",required=false) String domicileCountryCode
			){
		
		List<CompanyFinancialDTO> financialData = null;
		try {
			_log.info(currency);
			if(currency==null) {
				currency= "USD";
			}
			financialData = screenerService.getCompanyFinancialRatio(ticsIndustryCode,currency,ratioName,domicileCountryCode);	
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
	
	
	
	@GetMapping("/companies/stockprice")
	public ResponseEntity<List<ScreenerStockPriceDTO>> getCompanyStockPrice(@RequestParam(name="ticsIndustryCode") String ticsIndustryCode
			,@RequestParam(value="countryisocode",required=false) String countryIsoCode
			,@RequestParam(value="currency",required=false) String currency){
		List<ScreenerStockPriceDTO> latestSharePrice = null;
		
		 List<String> countryIsoList = null;
         
         if(countryIsoCode!=null){
                 countryIsoList = Arrays.asList(countryIsoCode.split(","));
        }

		
		try {
			if(currency != null && !currency.isEmpty()){
				latestSharePrice = screenerService.getCompanyLatestStockPrice(ticsIndustryCode,currency,countryIsoList);	
			}else{
				latestSharePrice = screenerService.getCompanyLatestStockPrice(ticsIndustryCode,"USD",countryIsoList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<ScreenerStockPriceDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<ScreenerStockPriceDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<ScreenerStockPriceDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ScreenerStockPriceDTO>>(latestSharePrice, HttpStatus.OK);
	}
	
	/*@GetMapping("/companies/stockprice")
	public ResponseEntity<List<StockPriceDTO>> getCompanyStockPrice(@RequestParam(name="ticsIndustryCode") String ticsIndustryCode
			,@RequestParam(value="currency",required=false) String currency){
		List<StockPriceDTO> latestSharePrice = null;
		try {
			if(currency != null && !currency.isEmpty()){
				latestSharePrice = screenerService.getCompanyLatestStockPrice(ticsIndustryCode,currency);	
			}else{
				latestSharePrice = screenerService.getCompanyLatestStockPrice(ticsIndustryCode,"USD");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<StockPriceDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<StockPriceDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<StockPriceDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<StockPriceDTO>>(latestSharePrice, HttpStatus.OK);
	}*/
	
	@GetMapping("/companies/totalStock")
	public ResponseEntity<List<FFBasicCfDTO>> getCompanyTotalStock(@RequestParam(name="ticsIndustryCode") String ticsIndustryCode){
		List<FFBasicCfDTO> totalStock = null;
		try {
			totalStock = screenerService.getCompaniesTotalStock(ticsIndustryCode);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<FFBasicCfDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<FFBasicCfDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<FFBasicCfDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<FFBasicCfDTO>>(totalStock, HttpStatus.OK);
	}
	@GetMapping("/currency/conversionrate")
	public ResponseEntity<List<FxConversionRate>> getCurrencyConversionRate(@RequestParam(name="sourceCurrency") String sourceCurrency
			,@RequestParam(value="destinationCurrency") String destinationCurrency
			,@RequestParam(value="periodList") String[] periodList
			){
		
		List<FxConversionRate> fxRate = null;
		try {
			_log.info(sourceCurrency);
			if(sourceCurrency==null) {
				sourceCurrency= "USD";
			}
			fxRate = screenerService.getCurrencyConversionRate(sourceCurrency,destinationCurrency,periodList);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<FxConversionRate>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<FxConversionRate>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<FxConversionRate>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<FxConversionRate>>(fxRate, HttpStatus.OK);
	}
	
}
