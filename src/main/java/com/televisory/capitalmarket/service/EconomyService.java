package com.televisory.capitalmarket.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.EconomyRepository;
import com.televisory.capitalmarket.dto.CountryIndicatorDTO;
import com.televisory.capitalmarket.dto.CurrencyListDTO;
import com.televisory.capitalmarket.dto.IndicatorDataDTO_old;
import com.televisory.capitalmarket.dto.IndicatorTypeDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.CurrencyMappingDTO;
import com.televisory.capitalmarket.dto.economy.EconomyCreditRatingLatestDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorDataForecastDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.entities.economy.ExchangeRatesComparison;
import com.televisory.capitalmarket.entities.economy.IndicatorData_old;
import com.televisory.capitalmarket.entities.economy.IndicatorType;
import com.televisory.capitalmarket.model.PeriodicityEconomyRequest;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;
import com.televisory.capitalmarket.util.DozerHelper;

@Service
public class EconomyService {

	Logger _log = Logger.getLogger(EconomyService.class);

	@Autowired
	EconomyRepository economyRepository;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Autowired
	DateUtil dateUtil;
	
	@Autowired
	CommonService commonService;
	
	public List<CountryListDTO> findCountries(String indicatorName,String searchCriteria,Integer resultCount, List<String> userCountryList){
	
		if(indicatorName!=null) {
			if(userCountryList!=null && userCountryList.size()>0){
				return economyRepository.findEconomyCountriesByIndicatorAndSubscribtion(indicatorName,userCountryList);
			}else{
				return economyRepository.findEconomyCountriesByIndicator(indicatorName);
			}
		}else if(searchCriteria!=null && !searchCriteria.isEmpty()){
			return economyRepository.findCountriesBySearchCriteria(searchCriteria,resultCount);
		}else if(userCountryList!=null && userCountryList.size()>0){
			return economyRepository.findCountriesBySubscribedCountry(userCountryList);
		}else{
			return economyRepository.findAllEconomyCountries();
		}
	
	}

	public List<IndicatorTypeDTO> getEconomyIndicatorData(Integer countryId, Date startDate, Date endDate) {
		
		List<IndicatorTypeDTO> indicatorTypeDTO = new ArrayList<IndicatorTypeDTO>(); 
		List<IndicatorType> indicatorType = economyRepository.getAllIndicatorType();
		
		for(int i=0;i<indicatorType.size();i++) {
			IndicatorTypeDTO indicatrTypeDTO =  new IndicatorTypeDTO();
			List<IndicatorData_old> indicatorData = null;
			if(startDate!=null && endDate!=null) {
				indicatorData =	economyRepository.getEconomyIndicatorData(countryId,indicatorType.get(i).getId(),startDate,endDate);
			}else {
				indicatorData =	economyRepository.getEconomyIndicatorData(countryId,indicatorType.get(i).getId());
			}
			List<IndicatorDataDTO_old> indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);
			if(indicatorData!=null && indicatorData.size()>0) {
				indicatrTypeDTO.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
				indicatrTypeDTO.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
				indicatrTypeDTO.setIndicatorData(indicatorDataDTO);
			}else {
				continue;
			}
			if(indicatorDataDTO!=null && indicatorDataDTO.size()>0) {
				for(int j=0;j<indicatorDataDTO.size();j++) {
					if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
						indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_THOUSAND);
						indicatorDataDTO.get(j).setUnit(null);
					}else if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
						indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_MILLION);
						indicatorDataDTO.get(j).setUnit(null);
					}else if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
						indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_BILLION);
						indicatorDataDTO.get(j).setUnit(null);
					}
					String prevYear = dateUtil.prevYear(indicatorDataDTO.get(j).getPeriod());
					LocalDate localDate = LocalDate.parse(prevYear);
					String minusPeriod = localDate.minusDays(10).toString();
					String plusPeriod = localDate.plusDays(10).toString();
					List<IndicatorDataDTO_old> prevIndicatorData = economyRepository.getPrevIndicatorData(indicatorDataDTO.get(j).getCountryIndicatorId(),minusPeriod,plusPeriod);
					if(prevIndicatorData!=null && prevIndicatorData.size()>0) {
						IndicatorDataDTO_old prevData = prevIndicatorData.get(0);
						if(prevData.getData()!=null && prevData.getUnit()!=null && prevData.getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
							prevData.setData(prevData.getData()*CMStatic.UNIT_FACTOR_THOUSAND);
							prevData.setUnit(null);
						}else if(prevData.getData()!=null && prevData.getUnit()!=null && prevData.getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
							prevData.setData(prevData.getData()*CMStatic.UNIT_FACTOR_MILLION);
							prevData.setUnit(null);
						}else if(prevData.getData()!=null && prevData.getUnit()!=null && prevData.getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
							prevData.setData(prevData.getData()*CMStatic.UNIT_FACTOR_BILLION);
							prevData.setUnit(null);
						}
						Double prevVal = prevData.getData();
						Double percentageChange = calculatePercentageChange(indicatorDataDTO.get(j).getData(),prevVal,indicatorDataDTO.get(j).getUnit());
						indicatorDataDTO.get(j).setPercentageChange(percentageChange);
					}
				}
			}
			if(indicatrTypeDTO!=null)
				indicatorTypeDTO.add(indicatrTypeDTO);
		}
		return  indicatorTypeDTO;
	}

	private Double calculatePercentageChange(Double currentData, Double previousData, String unit) {
		if(currentData==null) {
			currentData = 0.0;
		}
		if(previousData==null || previousData==0.0) {
			return null;
		} else {
			Double res = null;
			
			if(unit != null && unit.equals("%"))
				res = (currentData-previousData)*100;
			else
				res =	commonService.percentageChange(currentData, previousData);
				//res = ((currentData/previousData)-1)*100;
			return res;
		}
	}

	public IndicatorTypeDTO getEconomyIndicatorData(Integer countryId, Integer indicatorTypeId, Date startDate, Date endDate) {
		List<IndicatorData_old> indicatorData = null;
		if(startDate!=null && endDate!=null) {
			indicatorData =	economyRepository.getEconomyIndicatorData(countryId,indicatorTypeId,startDate,endDate);
		}else {
			indicatorData =	economyRepository.getEconomyIndicatorData(countryId,indicatorTypeId);
		}
		_log.info("indicatorData "+indicatorData);
		List<IndicatorDataDTO_old> indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);
		_log.info("countryId "+countryId+" indicatorId "+indicatorTypeId);
		if(indicatorDataDTO!=null && indicatorDataDTO.size()>0) {
			for(int j=0;j<indicatorDataDTO.size();j++) {
				//if(indicatorDataDTO.get(j).getDataType().equalsIgnoreCase("ABSOLUTE")) {
					String prevYear = dateUtil.prevYear(indicatorDataDTO.get(j).getPeriod());
					LocalDate localDate = LocalDate.parse(prevYear);
					String minusPeriod = localDate.minusDays(10).toString();
					String plusPeriod = localDate.plusDays(10).toString();
					_log.info("Calculating percentage for indicator "+indicatorDataDTO.get(j).getId()+" minusPeriod "+minusPeriod+" plusPeriod "+plusPeriod);
					List<IndicatorDataDTO_old> prevIndicatorData = economyRepository.getPrevIndicatorData(indicatorDataDTO.get(j).getCountryIndicatorId(),minusPeriod,plusPeriod);
					
					if(prevIndicatorData!=null && prevIndicatorData.size()>0) {
						Double prevVal = prevIndicatorData.get(0).getData();
						_log.info(" Prev Val "+prevVal);
						Double percentageChange = calculatePercentageChange(indicatorDataDTO.get(j).getData(),prevVal, indicatorDataDTO.get(j).getUnit());
						indicatorDataDTO.get(j).setPercentageChange(percentageChange);
					}
				//}
			}
		}
		
		IndicatorTypeDTO indicatorType =  new IndicatorTypeDTO();
		if(indicatorData!=null && indicatorData.size()>0) {
			indicatorType.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
			indicatorType.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
			indicatorType.setIndicatorData(indicatorDataDTO);
		}else {
			return null;
		} 
		return indicatorType;
	}

	public List<CountryIndicatorDTO> getCountryIndicatorPeriodicity(Integer countryId) {
		return economyRepository.getCountryIndicatorPeriodicity(countryId);
	}
	public List<CountryIndicatorDTO> getCountryIndicatorPeriodicityList(List<String> countryId) {
		return economyRepository.getCountryIndicatorPeriodicityList(countryId);
	}
	
	public IndicatorTypeDTO getIndicatorData(Integer countryId, Integer indicatorId, String periodType) {
		return economyRepository.getIndicatorData(countryId,indicatorId,periodType);
	}

	public List<ExchangeRatesComparison> getExchangeRate(Integer countryId) throws Exception {
		
		String currencyCode = getCountryCurrency(countryId.toString()).getCurrencyCode();
		List<ExchangeRatesComparison> exchangeRateDto = economyRepository.getLatestExchangeRate(currencyCode);
		
		if(exchangeRateDto == null || exchangeRateDto.isEmpty())
			exchangeRateDto = economyRepository.getDummyExchangeRate(currencyCode);
		
		exchangeRateDto.sort(Comparator.comparing(ExchangeRatesComparison::getTargetCurrencyCode));
		
		return exchangeRateDto;
	}

	public List<IndicatorDataDTO_old> getMajorEconomyData(Integer indicatorTypeId, String periodType, Integer exceptCountryId) {
		
		List<IndicatorDataDTO_old> indicatorDataDTO =  economyRepository.getMajorEconomyData(indicatorTypeId,periodType,exceptCountryId);
		if(indicatorDataDTO!=null && indicatorDataDTO.size()>0) {
			
			for(int j=0;j<indicatorDataDTO.size();j++) {
				if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
					indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_THOUSAND);
					indicatorDataDTO.get(j).setUnit(null);
				}else if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
					indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_MILLION);
					indicatorDataDTO.get(j).setUnit(null);
				}else if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
					indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_BILLION);
					indicatorDataDTO.get(j).setUnit(null);
				}
				String prevYear = dateUtil.prevYear(indicatorDataDTO.get(j).getPeriod());
				LocalDate localDate = LocalDate.parse(prevYear);
				String minusPeriod = localDate.minusDays(10).toString();
				String plusPeriod = localDate.plusDays(10).toString();
				_log.info("Calculating percentage for indicator "+indicatorDataDTO.get(j).getId()+" minusPeriod "+minusPeriod+" plusPeriod "+plusPeriod);
				List<IndicatorDataDTO_old> prevIndicatorData = economyRepository.getPrevIndicatorData(indicatorDataDTO.get(j).getCountryIndicatorId(),minusPeriod,plusPeriod);
				if(prevIndicatorData!=null && prevIndicatorData.size()>0) {
					IndicatorDataDTO_old prevData = prevIndicatorData.get(0);
					if(prevData.getData()!=null && prevData.getUnit()!=null && prevData.getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
						prevData.setData(prevData.getData()*CMStatic.UNIT_FACTOR_THOUSAND);
						prevData.setUnit(null);
					}else if(prevData.getData()!=null && prevData.getUnit()!=null && prevData.getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
						prevData.setData(prevData.getData()*CMStatic.UNIT_FACTOR_MILLION);
						prevData.setUnit(null);
					}else if(prevData.getData()!=null && prevData.getUnit()!=null && prevData.getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
						prevData.setData(prevData.getData()*CMStatic.UNIT_FACTOR_BILLION);
						prevData.setUnit(null);
					}
					Double prevVal = prevData.getData();
					_log.info(" Prev Val "+prevVal);
					_log.info(" indicatorDataDTO.get(j).getData() "+indicatorDataDTO.get(j).getData()+" indicatorDataDTO.get(j).getUnit() "+indicatorDataDTO.get(j).getUnit());
					Double percentageChange = calculatePercentageChange(indicatorDataDTO.get(j).getData(),prevVal, indicatorDataDTO.get(j).getUnit());
					indicatorDataDTO.get(j).setPercentageChange(percentageChange);
				}
			}
		}
		
		//return economyRepository.getMajorEconomyData(indicatorId,periodType,exceptCountryId);
		return indicatorDataDTO;
	}
	
	public String getCountryName(Integer countryId) {
		return economyRepository.getCountryName(countryId);
	}
	
	public String getCountryIsoCode(Integer countryId) {
		return economyRepository.getCountryIsoCode(countryId);
	}
	
	public String getIndicatorName(Integer indicatorId) {
		return economyRepository.getIndicatorName(indicatorId);
	}
	
	public CountryListDTO getCountryByIsoCode(String countryIsoCode) {
		return economyRepository.getCountryByIsoCode(countryIsoCode);
	}



	public IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, Date startDate, Date endDate) {
		IndicatorTypeDTO indicatorType = economyRepository.getIndicatorDataByCountryIndicator(countryIndicatorId,startDate,endDate);
		List<IndicatorDataDTO_old> indicatorDataDTO = indicatorType.getIndicatorData();
		if(indicatorDataDTO!=null && indicatorDataDTO.size()>0) {
			for(int j=0;j<indicatorDataDTO.size();j++) {
				if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
					indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_THOUSAND);
					indicatorDataDTO.get(j).setUnit(null);
				}else if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
					indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_MILLION);
					indicatorDataDTO.get(j).setUnit(null);
				}else if(indicatorDataDTO.get(j).getData()!=null && indicatorDataDTO.get(j).getUnit()!=null && indicatorDataDTO.get(j).getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
					indicatorDataDTO.get(j).setData(indicatorDataDTO.get(j).getData()*CMStatic.UNIT_FACTOR_BILLION);
					indicatorDataDTO.get(j).setUnit(null);
				}
			}
		}
		return indicatorType;
	}


	public List<IndicatorDataDTO_old> getIndicatorDataByIndicator(Integer countryId, Integer indicatorId, String periodType, Date startDate, Date endDate) {
		if(startDate!=null && endDate!=null) {
			return economyRepository.getIndicatorDataByIndicator(countryId,indicatorId,periodType,startDate,endDate);
		}else {
			return economyRepository.getIndicatorDataByIndicator(countryId,indicatorId,periodType);
		}
	}


	public List<IndicatorLatestDataDTO> findAllIndicators(String searchParam,Integer resultCount) {
		
		if(searchParam !=null){
			return economyRepository.findAllIndicators(searchParam);
		}else{
			return economyRepository.findAllIndicators();
		}
		
		
	}


	/*public List<CountryListDTO> findEconomyCountriesByIndicator(Integer indicatorId) {
		return economyRepository.findEconomyCountriesByIndicator(indicatorId);
	}*/


	public List<IndicatorLatestDataDTO> findIndicatorsByCountry(List<String> countryIsoCodeList) {
		return economyRepository.findIndicatorsByCountry(countryIsoCodeList);
	}


	public List<CountryIndicatorDTO> getIndicatorPeriodicity(Integer indicatorId, Integer countryId) {
		return economyRepository.getIndicatorPeriodicity(indicatorId,countryId);
	}
	
	public List<CountryIndicatorDTO> getIndicatorPeriodicityList(Integer indicatorId, List<String> countryId) {
		return economyRepository.getIndicatorPeriodicityList(indicatorId,countryId);
	}


	public List<CountryListDTO> findEconomyCountriesByMultipleIndicator(List<Integer> indicatorList) {
		return economyRepository.findEconomyCountriesByMultipleIndicator(indicatorList);
	}


	public List<IndicatorDTO> findIndicatorsByMultipleCountry(List<Integer> countryList) {
		return economyRepository.findIndicatorsByMultipleCountry(countryList);
	}


	public List<String> getPeriodicity(PeriodicityEconomyRequest periodicityRequest) {
		return economyRepository.getPeriodicity(periodicityRequest);
	}


	public IndicatorTypeDTO getIndicatorData(Integer countryId, Integer indicatorTypeId, String periodType,String currency) {
		return economyRepository.getIndicatorData(countryId,indicatorTypeId,periodType,currency);
	}


	public IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, String currency, Date startDate, Date endDate) {
		IndicatorTypeDTO indicatorType = economyRepository.getIndicatorDataByCountryIndicator(countryIndicatorId,currency,startDate,endDate);
		indicatorType.getIndicatorData().forEach(data -> 
		{
			if(data.getCurrency()!=null && !data.getCurrency().equals("")) {
				data.setCurrency(currency);
			}
			if(data.getData()!=null && data.getUnit()!=null && data.getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
				data.setData(data.getData()*CMStatic.UNIT_FACTOR_THOUSAND);
				data.setUnit(null);
			}else if(data.getData()!=null && data.getUnit()!=null && data.getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
				data.setData(data.getData()*CMStatic.UNIT_FACTOR_MILLION);
				data.setUnit(null);
			}else if(data.getData()!=null && data.getUnit()!=null && data.getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
				data.setData(data.getData()*CMStatic.UNIT_FACTOR_BILLION);
				data.setUnit(null);
			}
		});
		return indicatorType;
	}
	
	/*public IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, String currency) {
		IndicatorTypeDTO indicatorType = economyRepository.getIndicatorDataByCountryIndicator(countryIndicatorId);
		indicatorType.getIndicatorData().forEach(data -> data.setCurrency(currency));
		return indicatorType;
	}*/
	
	public List<IndicatorDataDTO_old> getEconomyRequestIndicatorData(Integer indicatorId, Integer countryISoCode, String currency, String periodicity, Date startDate, Date endDate) {
		
		//List<IndicatorDataDTO_old> indicatorData = economyRepository.getEconomyRequestIndicatorData(countryId, indicatorId, currency, periodicity, startDate, endDate);
		
		//_log.info("data:: "+indicatorData);
/*		
		
		if(indicatorData!=null) {
			indicatorData.forEach(data -> {
				if(data.getCurrency()!=null && !data.getCurrency().equals("")) {
					data.setCurrency(currency);
				}
				if(data.getUnit()!=null && data.getData()!=null && data.getUnit().equalsIgnoreCase(CMStatic.UNIT_THOUSAND)) {
					data.setData(data.getData()*CMStatic.UNIT_FACTOR_THOUSAND);
					data.setUnit(null);
				}else if(data.getUnit()!=null && data.getData()!=null && data.getUnit().equalsIgnoreCase(CMStatic.UNIT_MILLION)) {
					data.setData(data.getData()*CMStatic.UNIT_FACTOR_MILLION);
					data.setUnit(null);
				}else if(data.getUnit()!=null && data.getData()!=null && data.getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION)) {
					data.setData(data.getData()*CMStatic.UNIT_FACTOR_BILLION);
					data.setUnit(null);
				}
			});
		}else {
			indicatorData = new ArrayList<IndicatorDataDTO_old>();
		}*/
		return null;
	}

	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<CurrencyListDTO> getAllCurrency(String searchCriteria) {
		if(searchCriteria!=null && !searchCriteria.isEmpty()){
			return economyRepository.getAllCurrency(searchCriteria);
		}else {
			return economyRepository.getAllCurrency();
		}
	}
	
	//Remove this later if not used
	public CurrencyMappingDTO getCountryCurrency(String countryId) throws Exception {
		return economyRepository.getCountryCurrency(countryId);
	}
	
	public CurrencyMappingDTO getCountryCurrencyByIsoCode(String countryIsoCode) throws Exception {
		return economyRepository.getCountryCurrencyByIsoCode(countryIsoCode);
	}

	public List<ExchangeRatesComparison> getFactSetExchangeRate(String sourceCurrencyCode, String targetCurrencyCode, Date startDate, Date endDate) {
		return economyRepository.getFactSetExchangeRate(sourceCurrencyCode,targetCurrencyCode,startDate,endDate);
	}


	public List<IndicatorDTO> findIndicators(List<Integer> countryList) {
		return economyRepository.findIndicators(countryList);
	}
	
	/**
	 * 
	 * @param companyIdList
	 * @return
	 */
	public List<CountryListDTO> getCMCountriesByIdList(List<String> customCountryIds) {

		List<CountryListDTO> countryListDTO=null;
			
		countryListDTO =economyRepository.getCMCountriesByIdList(customCountryIds);		
		return countryListDTO;
	}
	
	public Double getFxData(String periodType, Date fxDate, String currencyCode) {
		return economyRepository.getFxData(periodType, fxDate, currencyCode);
	}
	public Double getFxData(String periodType, Date fxDate, String currencyCode, String countryCurrency) {
		return economyRepository.getFxData(periodType,fxDate,currencyCode,countryCurrency);
	}
	
	public Double getDailyFxData(String periodType, Date fxDate, String currencyCode, String countryCurrency) {
		return economyRepository.getDailyFxData(periodType, fxDate, currencyCode, countryCurrency);
	}


	public List<IndicatorLatestDataDTO> getEconomyIndicatorLatestData(String countryIsoCode,String currency, Date startDate,
			Date endDate) {
		
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
		
		if(currency!=null){
			return economyRepository.getEconomyIndicatorLatestDataByCurrency(countryIsoCode,currency, startDate, endDate);
		}else{
			return economyRepository.getEconomyIndicatorLatestData(countryIsoCode, startDate, endDate);
		}
	}

	public List<IndicatorHistoricalDataDTO> getDataByIndicatorAndCountry(List<String> countryIsoCodeList, List<String> indicatorNameList, String periodType,Date startDate
			,Date endDate) {	
		return economyRepository.getDataByIndicatorAndCountry(countryIsoCodeList,indicatorNameList,periodType,startDate,endDate);
	}


	public List<IndicatorHistoricalDataDTO> getDataByIndicatorAndCountry(List<String> countryIsoCodeList, List<String> indicatorNameList, String periodType,
			String currency,Date startDate,Date endDate) {
		return economyRepository.getDataByIndicatorAndCountry(countryIsoCodeList,indicatorNameList,periodType,currency,startDate,endDate);
	}


	public List<NewsDTO> getNews(String countryIsoCode) {
		return economyRepository.getNews(countryIsoCode);
	}

	public List<String> getIndicatorPeriodIcity(List<String> indicatorName,List<String> countryNameList) {
		return economyRepository.getIndicatorPeriodIcity(indicatorName,countryNameList);
	}

	public List<EconomyCreditRatingLatestDTO> getEconomyCreditRatingByCountry(String countryIsoCode) {
		return  economyRepository.getEconomyCreditRatingByCountry(countryIsoCode);
	}

	public CountryListDTO getCMCountryByIsoCode(String countryIsoCode) {
		return  economyRepository.getCMCountryByIsoCodes(countryIsoCode);
	}

	public CountryListDTO getDefaultCountryForEconomy(List<String> countryCode) {
		return economyRepository.getDefaultCountryForEconomy(countryCode);
	}

	public List<IndicatorDataForecastDTO> getForecastDataByIndicatorAndCountry(String historicalDataSymbol) {
		return economyRepository.getDefaultCountryForEconomy(historicalDataSymbol);
	}
}
