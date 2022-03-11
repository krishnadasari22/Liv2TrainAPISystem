package com.televisory.capitalmarket.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.entities.economy.CountryList;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

@Service
@Repository
@Transactional
public class IMService {

	static private Logger _log = Logger.getLogger(IMService.class);

	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;

	@Autowired
	@Qualifier(value="teSessionFactory")
	private SessionFactory teSessionFactory;

	@Autowired
	EconomyService economyService;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	public List<LocalDate> getPeriod(String type, Boolean endOfTheMonth, LocalDate currentPeriod, Integer duration,Boolean advance) {

		List<String> typeList = new ArrayList<>();
		typeList.add("YEARLY");
		typeList.add("QUARTERLY");
		if(type==null || !typeList.contains(type.toUpperCase())){
			type = "YEARLY";
		}

		if(endOfTheMonth==null){
			endOfTheMonth = false;
		}

		if(advance==null){
			advance = false;
		}

		//	LocalDate currentPeriod = LocalDate.fromDateFields(period);
		if (duration == null) {
			duration = 5;
		}

		List<LocalDate> dateList = new ArrayList<LocalDate>();
		for (int i = 0; i < duration; i++) {
			LocalDate tempPeriod = null;
			if (endOfTheMonth) {
				if ("YEARLY".equalsIgnoreCase(type)) {
					if(advance){
						tempPeriod = currentPeriod.plusYears(i).dayOfMonth().withMaximumValue();
					}else{
						tempPeriod = currentPeriod.minusYears(i).dayOfMonth().withMaximumValue();
					}
				}
				if ("QUARTERLY".equalsIgnoreCase(type)) {
					if(advance){
						tempPeriod = currentPeriod.plusMonths(i * 3).dayOfMonth().withMaximumValue();
					}else{
						tempPeriod = currentPeriod.minusMonths(i * 3).dayOfMonth().withMaximumValue();
					}
				}
			} else {
				if ("YEARLY".equalsIgnoreCase(type)) {
					if(advance){
						tempPeriod = currentPeriod.plusYears(i);
					}else{
						tempPeriod = currentPeriod.minusYears(i);
					}

				}
				if ("QUARTERLY".equalsIgnoreCase(type)) {
					if(advance){
						tempPeriod = currentPeriod.plusMonths(i * 3);
					}else{
						tempPeriod = currentPeriod.minusMonths(i * 3);
					}

				}
			}
			dateList.add(tempPeriod);
		}
		return dateList;
	}
	
	public List<CountryListDTO> getTECountry(String countryIsoCode){
		//String querryString = "SELECT `country`, month(`latest_value_date`) as m FROM `indicator_data_latest` WHERE `country` = :country and `frequency` = :frequency GROUP by month(`latest_value_date`) order by m desc limit 1";
		String querryString = "SELECT * FROM `country_list` where country_iso_code_3 = :country_iso_code_3";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(querryString).addEntity(CountryList.class);;
		query.setParameter("country_iso_code_3", countryIsoCode);
		
		@SuppressWarnings("unchecked")
		List<CountryList> countryList = query.list();
		_log.info(countryList);
		
		for (CountryList countryList2 : countryList) {
			_log.info(countryList2);
		}
		
		List<CountryListDTO> countryListDTO = null;
		countryListDTO = DozerHelper.map(dozerBeanMapper, countryList, CountryListDTO.class);	
		_log.info(countryListDTO);
		return countryListDTO;
	}

	public List<Object> getBasePeriodMonth(String country_iso_code_3, String frequency){
		//String querryString = "SELECT `country`, month(`latest_value_date`) as m FROM `indicator_data_latest` WHERE `country` = :country and `frequency` = :frequency GROUP by month(`latest_value_date`) order by m desc limit 1";
		
		//String querryString = "SELECT `country`, month(`latest_value_date`) as m, frequency FROM `indicator_data_latest` WHERE `country_iso_code_3` = :country_iso_code_3 and category=:category GROUP by month(`latest_value_date`) order by m desc limit 1";
		
		String querryString = "SELECT idl.country, month(`latest_value_date`) as m, frequency FROM indicator_data_latest idl join country_list cl on cl.country_name = idl.country WHERE cl.`country_iso_code_3` = :country_iso_code_3 and category=:category GROUP by month(`latest_value_date`) order by m desc limit 1";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(querryString);
		//query.setParameter("country", countryName);
		
		query.setParameter("country_iso_code_3", country_iso_code_3);
		//query.setParameter("frequency", frequency);
		query.setParameter("category", CMStatic.GDP);
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.list();
		
		String frequencyOfData = "";
		List<Object> x = new ArrayList<>();
		x.add(frequencyOfData);
		if(list!=null && !list.isEmpty()){
			x = new ArrayList<>();
			Object[] obj = list.get(0);
			int month = Integer.parseInt(obj[1].toString());
			frequencyOfData = (String)obj[2].toString();
			x.add(month);
			x.add(frequencyOfData);
			return x;
		}
		
		return null;
	}

	public LocalDate getStartingDate(LocalDate period,String type){
		_log.info(period + " Freq ::: " + type);
		LocalDate startDate = null;
		if("yearly".equalsIgnoreCase(type)){
			startDate = period.minusYears(1).dayOfMonth().withMaximumValue().plusDays(1);
		}

		if("half-yearly".equalsIgnoreCase(type)){
			startDate = period.minusMonths(6).dayOfMonth().withMaximumValue().plusDays(1);
		}


		if("quarterly".equalsIgnoreCase(type)){
			startDate = period.minusMonths(3).dayOfMonth().withMaximumValue().plusDays(1);
		}

		if("monthly".equalsIgnoreCase(type)){
			startDate = period.minusMonths(1).dayOfMonth().withMaximumValue().plusDays(1);
		}

		if("weekly".equalsIgnoreCase(type)){
			startDate = period.minusDays(6);
		}

		_log.info("Start date is ::: " + startDate);

		return startDate;
	}


	public LinkedHashMap<String, List<IndicatorHistoricalDataDTO>> getDataForAPeriodBasedOnFrequency(String coountryIsoCode, String frequency, LocalDate period, String category , String currency, String requestedDataFormat){
		_log.info("Getting the data based on the frequency " +  coountryIsoCode +" :: " + frequency + " :: " + period + " :: " + category);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(frequency==null){
			_log.info("No Frequency.......taking default as yearly");
			frequency = CMStatic.YEARLY_FREQUENCY;
		}

		LinkedHashMap<String, List<IndicatorHistoricalDataDTO>> myMap = new LinkedHashMap<>();

		Date startDate = getStartingDate(period, requestedDataFormat).toDate();
		Date endDate = period.toDate();
		List<String> countryList = new ArrayList<String>();
		countryList.add(coountryIsoCode);
		List<String> categoryList = new ArrayList<String>();
		categoryList.add(category);
		List<IndicatorHistoricalDataDTO> data = null;
		if(currency==null){
			 data = economyService.getDataByIndicatorAndCountry(countryList, categoryList, frequency ,startDate, endDate);
		}else{
			 data = economyService.getDataByIndicatorAndCountry(countryList, categoryList, frequency, currency ,startDate, endDate);
		}
		
		Collections.reverse(data);
		myMap.put(sdf.format(endDate), data);
		return myMap;
	}


	public String getIndicatorFrequencyByCountry(String country_iso_code_3, String category){
		//String querryString = "SELECT frequency FROM `indicator_data_latest` WHERE `country` = :countryName AND `category` = :category ORDER BY `latest_value_date` DESC limit 1 ";
		String querryString = "SELECT frequency FROM `indicator_data_latest` idl join country_list cl on cl.country_name = idl.country WHERE cl.`country_iso_code_3` = :country_iso_code_3 AND `category` = :category ORDER BY `latest_value_date` DESC limit 1 ";

		Query query = teSessionFactory.getCurrentSession().createSQLQuery(querryString);
		//query.setParameter("countryName", countryName);
		query.setParameter("country_iso_code_3", country_iso_code_3);
		query.setParameter("category", category);
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		if(list!=null && !list.isEmpty()){
			String frequency =	list.get(0).toString();
			return frequency;
		}
		return null;
	}



	public boolean canDataBeUse(String requestedFreq , String dataFreq){
		if(requestedFreq.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
			if(dataFreq.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
				return false;
			}
		}
		return true;
	}

	public Double mapValueToPeriod(HashMap<String, List<IndicatorHistoricalDataDTO>> data, String category){
		Double dataForIndicator = null;
		for (String key : data.keySet()) {
			List<IndicatorHistoricalDataDTO> dataList = data.get(key);
			if(!category.equals("Exports")){
				if(dataList!=null && !dataList.isEmpty()){
					IndicatorHistoricalDataDTO indicatorHistoricalDataDTO = dataList.get(0);
					dataForIndicator =  indicatorHistoricalDataDTO.getData();
				}
			}else{
				_log.info("The request is for the exports ....aaa");
			}
		}
		return dataForIndicator;
	}


	public 	LinkedHashMap<String, Object> getEconomicalDataForIM(Integer countryId,String periodType,List<LocalDate> dateList, String category,String currency , String requestedDataFormat){
		try {
			List<String> countryList = new ArrayList<>();
			if(countryId!=null){
				countryList.add(""+countryId);
				List<CountryListDTO> countryListTemp = economyService.getCMCountriesByIdList(countryList);
				if(countryListTemp!=null && !countryListTemp.isEmpty()){
					String countryIsoCode = countryListTemp.get(0).getCountryIsoCode3();
					//String countryName = 	countryListTemp.get(0).getCountryName();	
					String frequency = getIndicatorFrequencyByCountry(countryIsoCode, category);
					LinkedHashMap<String, Object> mappedData = new LinkedHashMap<>();
					for (LocalDate localDate2 : dateList) {
						LinkedHashMap<String, List<IndicatorHistoricalDataDTO>> data = getDataForAPeriodBasedOnFrequency(countryIsoCode, frequency, localDate2, category, currency,requestedDataFormat);
						mappedData.putAll(data);
					}
					
					mappedData.put(CMStatic.FREQUENCY, frequency);
					return mappedData;

				}else{
					return null;
				}
			}
		} catch (Exception e) {
			_log.error(e.getMessage(),e);
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println("In Main");

		IMService imService = new IMService();
		List<LocalDate> list = imService.getPeriod("Quarterly", true, new LocalDate(), 6,true);
		_log.info(list);
		imService.getStartingDate(new LocalDate("2020-06-30"), "yearly");

	}




	public String convertToMillionOrBillion(List<Double> value){
		String unit = null;
		int millionCal = 0;
		int billionCal = 0;
		if(value !=null){
			for (Double double1 : value) {
				if(double1!=null ){
					if(double1 >= 1000000){
						millionCal ++;
					}else if(double1 >= 1000000000){
						billionCal++;
					}
				}
			}
		}

		if(millionCal==0 && billionCal==0){
			return null;
		}else if(billionCal==0){
			unit = "Million";
		}else if(billionCal > 0){
			unit = "Billion";
		}else{
			unit = null;
		}
		return unit;
	}


}
