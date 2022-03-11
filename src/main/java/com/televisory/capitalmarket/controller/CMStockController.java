package com.televisory.capitalmarket.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.televisory.capitalmarket.dto.CompanyStockPriceResponseDTO;
import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.model.BetaData;
import com.televisory.capitalmarket.service.CMStockService;
import com.televisory.capitalmarket.service.CapitalMarketService;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

import io.swagger.annotations.Api;
	
@RestController
@Api(value = "Equity", description = "Rest API for Equity", tags = "CM Equity Stock API")
public class CMStockController {
	
	Logger _log = Logger.getLogger(CMStockController.class);
	
	@Autowired
	CMStockService cmStockService;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Autowired
	CapitalMarketService capitalMarketService;
		
	@GetMapping("/companies/{companyId}/stockprice")
	public ResponseEntity<List<CompanyStockPriceResponseDTO>> getFactSetCompanyStockPrice(@PathVariable(name="companyId") String companyId,
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
			@RequestParam(value="currency", required=false) String currency){
		
		_log.info("in getFactSetCompanyStockPrice for company: "+ companyId);
		
		List<StockPriceDTO> stockPriceData = null;
		List<CompanyStockPriceResponseDTO> stockPriceResponseDTO =null; 
		
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
		
			stockPriceData = cmStockService.getFactSetCompanyStockPrice(companyId,periodType,startDate,endDate,currency);	
			
			stockPriceResponseDTO=DozerHelper.map(dozerBeanMapper, stockPriceData, CompanyStockPriceResponseDTO.class);
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyStockPriceResponseDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyStockPriceResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<CompanyStockPriceResponseDTO>>(stockPriceResponseDTO, HttpStatus.OK);
	}
		
	@GetMapping("/companies/{companyId}/beta")
	public ResponseEntity<BetaData> getCompanyBetaData(@PathVariable(value = "companyId") String companyId,
			@RequestParam(value="periodicity",required=false) String periodicity, 
			@RequestParam(value="indexId") String indexId,
			@RequestParam(value="currency", required=false) String currency) {
		
		List<BetaData> betaData = null;
		
		periodicity = periodicity != null ? periodicity : CMStatic.PERIODICITY_DAILY;
		
		List<String> indexList=new ArrayList<>();
		indexList.add(indexId);
		
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			betaData = cmStockService.getCompanyBeta(companyId, periodicity, indexList, currency);
		} catch (RuntimeException re) {
			if (re.getLocalizedMessage() != null && re.getLocalizedMessage().equals(HttpStatus.NO_CONTENT.toString())) {
				_log.error(re.getMessage());
				return new ResponseEntity<BetaData>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<BetaData>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			if (e.getLocalizedMessage() != null && e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<BetaData>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<BetaData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<BetaData>(betaData.get(0), HttpStatus.OK);
	}
	
	@GetMapping("/indexes")
	public ResponseEntity<List<IndexDTO>> getIndexList(@RequestParam(value="exchangeCode") String exchangeCode) {	
		
		_log.info("Getting index list for company : "+exchangeCode);
		
		List<IndexDTO> indexList = null;
		
		try {
			indexList = capitalMarketService.getIndexList(exchangeCode);
		} catch (Exception e) {
		
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<IndexDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IndexDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndexDTO>>(indexList, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/companies/{companyId}/indexes")
	public ResponseEntity<List<IndexDTO>> getCompanyIndexList(@PathVariable(value="companyId") String companyId) {	
		
		_log.info("Getting index list for company : "+companyId);
		
		List<IndexDTO> indexList = null;
		try {
			indexList = capitalMarketService.getCompanyIndexList(companyId);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<IndexDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IndexDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Getting index list for company completed : "+companyId);
		return new ResponseEntity<List<IndexDTO>>(indexList, HttpStatus.OK);
	}
	
	@GetMapping("/indexes/{indexId}")
	public ResponseEntity<IndexDTO> getIndex(@PathVariable(name="indexId") Integer indexId) {
		
		_log.info("Getting Index date for:"+indexId);
	
		IndexDTO indexes = null;
		try {
			indexes = capitalMarketService.getIndex(indexId);	
		} catch (Exception e) {
			_log.error(e.getMessage());

			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<IndexDTO>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<IndexDTO>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<IndexDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<IndexDTO>(indexes, HttpStatus.OK);
	}
	
	@GetMapping("/indexes/{indexId}/stockprice")
	public ResponseEntity<List<StockPriceDTO>> getIndexStockPriceData(@PathVariable(value = "indexId") Integer indexId,
			@RequestParam(value="periodType") String periodType,@RequestParam(value="startDate",required=false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
				@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
		
		_log.info("Getting Index Stock Price Data for index Id "+indexId+" periodType "+periodType+" Start Date "+startDate+" End Date "+endDate);
		List<StockPriceDTO> indexStockPriceData = null;
		try {
			indexStockPriceData = cmStockService.getIndexStockPrice(indexId, startDate, endDate,periodType);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<StockPriceDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<StockPriceDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<StockPriceDTO>>(indexStockPriceData, HttpStatus.OK);
	}
	

	@GetMapping("/stockprice/periodicity")
	public ResponseEntity<List<String>> getStockPeriodicity() {
		_log.info("Getting Stock Price Periodicity");
		List<String> periodicityList = new ArrayList<String>();
		periodicityList.add(CMStatic.PERIODICITY_DAILY);
		periodicityList.add(CMStatic.PERIODICITY_WEEKLY);
		return new ResponseEntity<List<String>>(periodicityList, HttpStatus.OK);
	}
	
	@GetMapping("/beta/periodicity")
	public ResponseEntity<List<String>> getBetaPeriodicity(@RequestParam(value="companyId",required=false) Integer companyId) {
		_log.info("Getting bse beta periodicity");
		List<String> periodicityList = new ArrayList<String>();
		periodicityList.add(CMStatic.PERIODICITY_DAILY);
		periodicityList.add(CMStatic.PERIODICITY_WEEKLY);
		return new ResponseEntity<List<String>>(periodicityList, HttpStatus.OK);
	}

}
