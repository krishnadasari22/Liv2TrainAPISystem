package com.televisory.capitalmarket.controller;

import java.text.ParseException;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.dto.CurrencyListDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.EconomyCreditRatingLatestDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorDataForecastDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.entities.economy.ExchangeRatesComparison;
import com.televisory.capitalmarket.model.PeriodicityEconomyRequest;
import com.televisory.capitalmarket.service.EconomyService;
import com.televisory.capitalmarket.service.IMService;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value="economy")
@Api(value = "Equity", description = "Rest API for Economy", tags = "CM Economy API")
public class EconomyController {

	Logger _log = Logger.getLogger(EconomyController.class);

	@Autowired
	EconomyService economyService ;

	@Autowired
	IMService imService;

	@GetMapping("/countries")
	public ResponseEntity<List<CountryListDTO>> getEconomyCountries(@RequestParam(value="indicatorName",required=false) String indicatorName,
			@RequestParam(name="searchCriteria",required=false) String searchCriteria,
			@RequestParam(name="resultCount",required=false) Integer resultCount,
			@RequestParam(value = "userCountryList",required=false) List<String> userCountryList) {

		_log.info("getting countries ::: " + userCountryList + indicatorName + searchCriteria+resultCount);

		List<CountryListDTO> countryList = null;

		try {
			countryList = economyService.findCountries(indicatorName, searchCriteria,resultCount,userCountryList);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<CountryListDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CountryListDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CountryListDTO>>(countryList, HttpStatus.OK);
	}


	@GetMapping("/news")
	public ResponseEntity<List<NewsDTO>> getEconomyNewsByCountry(@RequestParam(value="countryIsoCode",required=true) String countryIsoCode) {

		List<NewsDTO> newsList = null;

		try {
			newsList = economyService.getNews(countryIsoCode);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<NewsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<NewsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<NewsDTO>>(newsList, HttpStatus.OK);
	}

	@GetMapping("/credit-rating")
	public ResponseEntity<List<EconomyCreditRatingLatestDTO>> getEconomyCreditRatingByCountry(@RequestParam(value="countryIsoCode",required=true) String countryIsoCode) {

		List<EconomyCreditRatingLatestDTO> creditRatingList = null;

		try {
			creditRatingList = economyService.getEconomyCreditRatingByCountry(countryIsoCode);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<EconomyCreditRatingLatestDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<EconomyCreditRatingLatestDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<EconomyCreditRatingLatestDTO>>(creditRatingList, HttpStatus.OK);
	}


	@GetMapping("/countries/{CountryIsoCode}/latest-data")
	public ResponseEntity<List<IndicatorLatestDataDTO>> getLatestIndicatorData(@PathVariable(value = "CountryIsoCode",required=true) String CountryIsoCode,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
			@RequestParam(value="periodType",required=false) String periodType,
			@RequestParam(value="currency",required=false) String currency) {

		List<IndicatorLatestDataDTO> indicatorTypeDTO = null;
		try {
			indicatorTypeDTO = economyService.getEconomyIndicatorLatestData(CountryIsoCode,currency,startDate,endDate);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndicatorLatestDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndicatorLatestDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndicatorLatestDataDTO>>(indicatorTypeDTO, HttpStatus.OK);

	}

	@GetMapping("/countries/indicators/historical-data")
	public ResponseEntity<List<IndicatorHistoricalDataDTO>> getDataByIndicatorAndCountry(@RequestParam(value = "countryIsoCodeList",required=true) List<String> countryIsoCode,
			@RequestParam(value = "indicatorNameList") List<String> indicatorName,@RequestParam("periodType") String periodType,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
			@RequestParam(value="currency",required=false) String currency) {

		List<IndicatorHistoricalDataDTO> indicatorTypeDTO = null;

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

		try {
			if(currency!=null && !currency.isEmpty()) {
				indicatorTypeDTO = economyService.getDataByIndicatorAndCountry(countryIsoCode,indicatorName,periodType,currency,startDate,endDate);
			}else {
				indicatorTypeDTO = economyService.getDataByIndicatorAndCountry(countryIsoCode,indicatorName,periodType,startDate,endDate);
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndicatorHistoricalDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndicatorHistoricalDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndicatorHistoricalDataDTO>>(indicatorTypeDTO, HttpStatus.OK);
	}


	@GetMapping("/countries/indicators/historical-data-with-forecast")
	public ResponseEntity<List<IndicatorHistoricalDataDTO>> getDataByIndicatorAndCountryWithForecast(@RequestParam(value = "countryIsoCode",required=true) String countryIsoCode,
			@RequestParam(value = "indicatorName") String indicatorName,@RequestParam("periodType") String periodType,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
			@RequestParam(value="currency",required=false) String currency) {

		List<IndicatorHistoricalDataDTO> indicatorTypeDTO = null;
		
		List<IndicatorDataForecastDTO> indicatorForecastData =null;
		
		List<String> indicatorList =new ArrayList<String>(2);
		List<String> countryIsoCodeList =new ArrayList<String>(2);
		
		indicatorList.add(indicatorName);
		countryIsoCodeList.add(countryIsoCode);
		
		

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
			
			LocalDate now = LocalDate.now(); 
			LocalDate lastDay = now.with(lastDayOfYear()); 
			
			endDate=Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

		try {
			
			if(currency!=null && !currency.isEmpty()) {
				indicatorTypeDTO = economyService.getDataByIndicatorAndCountry(countryIsoCodeList,indicatorList,periodType,currency,startDate,endDate);
			}else {
				indicatorTypeDTO = economyService.getDataByIndicatorAndCountry(countryIsoCodeList,indicatorList,periodType,startDate,endDate);
			}
			
			if(indicatorTypeDTO.size()>0){
				
				indicatorForecastData = economyService.getForecastDataByIndicatorAndCountry(indicatorTypeDTO.get(0).getHistDataSymbol());
	
				
				if(indicatorForecastData.size()>0){
					
					IndicatorDataForecastDTO forecastData = indicatorForecastData.get(0);
					
					if(indicatorForecastData.get(0).getPeriodType().toLowerCase().equals("yearly")){
						
						LocalDate currentDate = indicatorTypeDTO.get(indicatorTypeDTO.size()-1)
								.getPeriod().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						
						_log.info(currentDate.getYear());
											
						String year = Integer.toString(currentDate.getYear()+1);
						String year2 = Integer.toString(currentDate.getYear()+2);
						String year3 = Integer.toString(currentDate.getYear()+3);
						
						IndicatorHistoricalDataDTO indicatorHistoricalData =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData.setData(forecastData.getForecastY1());
						indicatorHistoricalData.setPeriod(new SimpleDateFormat("yyyy-mm-dd").parse(year+"-12-31"));
						indicatorHistoricalData.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData.setDashLength(8);
						
						indicatorHistoricalData.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData);
						
						IndicatorHistoricalDataDTO indicatorHistoricalData2 =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData2.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData2.setData(forecastData.getForecastY2());
						indicatorHistoricalData2.setPeriod(new SimpleDateFormat("yyyy-mm-dd").parse(year2+"-12-31"));
						indicatorHistoricalData2.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData2.setDashLength(8);
						
						indicatorHistoricalData2.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData2);
						
						IndicatorHistoricalDataDTO indicatorHistoricalData3 =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData3.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData3.setData(forecastData.getForecastY3());
						indicatorHistoricalData3.setPeriod(new SimpleDateFormat("yyyy-mm-dd").parse(year3+"-12-31"));
						indicatorHistoricalData3.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData3.setDashLength(8);
						
						indicatorHistoricalData3.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData3);
						
					}else{
						
						IndicatorHistoricalDataDTO indicatorHistoricalData =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData.setData(forecastData.getForecastQ1());
						indicatorHistoricalData.setPeriod(forecastData.getQ1Date());
						indicatorHistoricalData.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData.setDashLength(8);
						
						indicatorHistoricalData.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData);
						
						IndicatorHistoricalDataDTO indicatorHistoricalData1 =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData1.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData1.setData(forecastData.getForecastQ2());
						indicatorHistoricalData1.setPeriod(forecastData.getQ2Date());
						indicatorHistoricalData1.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData1.setDashLength(8);
						
						indicatorHistoricalData1.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData1);
						
						IndicatorHistoricalDataDTO indicatorHistoricalData2 =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData2.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData2.setData(forecastData.getForecastQ3());
						indicatorHistoricalData2.setPeriod(forecastData.getQ3Date());
						indicatorHistoricalData2.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData2.setDashLength(8);
						
						indicatorHistoricalData2.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData2);
						
						IndicatorHistoricalDataDTO indicatorHistoricalData3 =new IndicatorHistoricalDataDTO();
						
						indicatorHistoricalData3.setHistDataSymbol(indicatorTypeDTO.get(0).getHistDataSymbol());
						indicatorHistoricalData3.setData(forecastData.getForecastQ4());
						indicatorHistoricalData3.setPeriod(forecastData.getQ4Date());
						indicatorHistoricalData3.setPeriodType(forecastData.getPeriodType());
						indicatorHistoricalData3.setDashLength(8);
						
						indicatorHistoricalData3.setCategory(forecastData.getCategory());
						
						indicatorTypeDTO.add(indicatorHistoricalData3);
						
					}
				}
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndicatorHistoricalDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndicatorHistoricalDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndicatorHistoricalDataDTO>>(indicatorTypeDTO, HttpStatus.OK);
	}

	

	@GetMapping("/countries/{countryId}/latestfx")
	public ResponseEntity<List<ExchangeRatesComparison>> getExchangeRate(@PathVariable(value = "countryId") Integer countryId) {
		List<ExchangeRatesComparison> exchangeRateDTO = null;
		try {
			exchangeRateDTO = economyService.getExchangeRate(countryId);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<ExchangeRatesComparison>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<ExchangeRatesComparison>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ExchangeRatesComparison>>(exchangeRateDTO, HttpStatus.OK);
	}



	@GetMapping("/indicators")
	public ResponseEntity<List<IndicatorLatestDataDTO>> getIndicators(@RequestParam(value="countryIsoCodeList",required=false) List<String> countryIsoCodeList,
			@RequestParam(value="searchParam",required=false) String searchParam,
			@RequestParam(value="resultCount",required=false) Integer resultCount) {

		List<IndicatorLatestDataDTO> indicatorNames = null;

		_log.info("getting the indicator List");

		try {
			if(countryIsoCodeList!=null) {
				_log.info(" countryIsoCodeList "+countryIsoCodeList);;
				indicatorNames = economyService.findIndicatorsByCountry(countryIsoCodeList);
			}else {
				indicatorNames = economyService.findAllIndicators(searchParam,resultCount);
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<IndicatorLatestDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<IndicatorLatestDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndicatorLatestDataDTO>>(indicatorNames, HttpStatus.OK);
	}


	@GetMapping("/indicator/periodicity")
	public ResponseEntity<List<String>> getIndicatorPeriodIcity(@RequestParam(value="indicatorName",required=true) List<String> indicatorName
			,@RequestParam(value="countryName",required=true) List<String> countryNameList) {

		List<String> indicatorPeriodicityList = null;
		try{
			_log.info("getting the indicator periodicity List");

			indicatorPeriodicityList = economyService.getIndicatorPeriodIcity(indicatorName,countryNameList);

		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<String>>(indicatorPeriodicityList, HttpStatus.OK);
	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/country/periodicity", method = RequestMethod.POST)
	public ResponseEntity<List<String>> getPeriodicity(@RequestBody PeriodicityEconomyRequest periodicityRequest) {
		List<String> indicatorsPeriodicity = null;
		try {
			_log.info("periodicityRequest "+periodicityRequest);
			indicatorsPeriodicity = economyService.getPeriodicity(periodicityRequest);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<String>>(indicatorsPeriodicity, HttpStatus.OK);
	}

	@GetMapping("/currency")
	public ResponseEntity<List<CurrencyListDTO>> getCurrency(@RequestParam(name="searchCriteria",required=false) String searchCriteria) {
		_log.info("getting currency list");
		List<CurrencyListDTO> currencyList = null;
		try {
			currencyList = economyService.getAllCurrency(searchCriteria);
		} catch (Exception e) {
			_log.error(e.getMessage());
			if (e.getLocalizedMessage().contains("401")) {
				return new ResponseEntity<List<CurrencyListDTO>>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<List<CurrencyListDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("getting currency list finished");
		return new ResponseEntity<List<CurrencyListDTO>>(currencyList, HttpStatus.OK);
	}

	@GetMapping("/countries/metadata/")
	public ResponseEntity<List<CountryListDTO>> getCMCountriesByIdList(@RequestParam(value="countryIdList") String countryIdList) {

		List<CountryListDTO> company = null;

		try {
			List<String> customCountryIds = Arrays.asList(countryIdList.split("\\s*,\\s*"));
			company = economyService.getCMCountriesByIdList(customCountryIds);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CountryListDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CountryListDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CountryListDTO>>(company, HttpStatus.OK);
	}

	@GetMapping("/countries/{countryIsoCode}/")
	public ResponseEntity<CountryListDTO> getCMCountryByIsoCode(@PathVariable(value="countryIsoCode") String countryIsoCode) {

		CountryListDTO countryObject = null;

		try {
			countryObject = economyService.getCMCountryByIsoCode(countryIsoCode);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<CountryListDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<CountryListDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CountryListDTO>(countryObject, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/defaultcountry")
	public ResponseEntity<CountryListDTO> getDefaultCountryForEconomy(@RequestParam(value="countryCode") List<String> countryCode) {

		CountryListDTO countryList = null;
		try {
			_log.info(" countryCode "+countryCode);
			countryList = economyService.getDefaultCountryForEconomy(countryCode);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<CountryListDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<CountryListDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CountryListDTO>(countryList, HttpStatus.OK);
	}



	/*@GetMapping("/indicator/{indicatorName}/country/{countryId}/")
	public ResponseEntity<CountryListDTO> getLatestIndicatorForCountry(@PathVariable(value="indicatorName") String category, @PathVariable(value="countryId") Integer countryId) {
		_log.info("Getting the latest value of the indicator for the country");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		_log.info(category+" ::: " + countryId);
		String requestPeriod = "2020-03-31";
		CountryListDTO countryObject = null;
		
		try {
			LinkedHashMap<String, List<IndicatorHistoricalDataDTO>> data = imService.getEconomicalDataForIM(countryId, "quarterly", null, category, "USD");
			_log.info(data);	

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<CountryListDTO>(countryObject, HttpStatus.OK);
	}
*/



}


