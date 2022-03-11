package com.televisory.capitalmarket.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.service.CommodityService;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value="commodity")
@Api(value = "Commodity", description = "Rest API for Commodity", tags = "CM Commodity API")
public class CommodityController {
	
	Logger _log = Logger.getLogger(CommodityController.class);

	@Autowired
	CommodityService commodityService ;
	
	@GetMapping("/list")
	public ResponseEntity<List<CommodityLatestDataDTO>> commodities() {
		List<CommodityLatestDataDTO> commodityDTO = null;
		_log.info("getting the commodity List");
		try {
			commodityDTO = commodityService.getCommodities();
		} catch (Exception e) {
			
			_log.error("problem occured in extracting commodity list. "+ e);
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<CommodityLatestDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CommodityLatestDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CommodityLatestDataDTO>>(commodityDTO, HttpStatus.OK);
	}
	
	@GetMapping("/latest-data")
	public ResponseEntity<List<CommodityLatestDataDTO>> getLatestData(
			@RequestParam(value="currency",required=false) String currency) {
		
		List<CommodityLatestDataDTO> commodityDTO = null;
		try {
			commodityDTO = commodityService.getCommodityLatestData(currency);
		} catch (Exception e) {
			
			_log.error("problem occured in extracting latest commodity data. "+ e);
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<CommodityLatestDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CommodityLatestDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CommodityLatestDataDTO>>(commodityDTO, HttpStatus.OK);
	
	}
	
	@GetMapping("/historical-data")
	public ResponseEntity<List<CommodityHistoricalDataDTO>> getHistoricalData(
			@RequestParam(value = "symbolList") List<String> symbolList,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
			@RequestParam(value="currency",required=false) String currency) {
		
		List<CommodityHistoricalDataDTO> commodityHistoricalDTO = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = null;
		
		if(startDate==null){
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate=date;
		}
		
		if(endDate==null){
			endDate=new Date();
		}
		
		_log.info(startDate+" - "+endDate);
		
		try {
			commodityHistoricalDTO = commodityService.getCommodityHistoricalData(symbolList, currency, startDate, endDate);
			
		} catch (Exception e) {
			_log.error("problem occured in extracting historical commodity data for symbol: "+ symbolList +". "+ e);
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<CommodityHistoricalDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CommodityHistoricalDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CommodityHistoricalDataDTO>>(commodityHistoricalDTO, HttpStatus.OK);
	}
	
	@GetMapping("/news")
	public ResponseEntity<List<NewsDTO>> getCommodityNews() {
		
		List<NewsDTO> newsList = null;
		
		try {
			newsList = commodityService.getNews();
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<NewsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<NewsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<NewsDTO>>(newsList, HttpStatus.OK);
	}

}


