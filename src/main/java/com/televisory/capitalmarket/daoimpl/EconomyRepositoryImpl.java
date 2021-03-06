package com.televisory.capitalmarket.daoimpl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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
import com.televisory.capitalmarket.dto.economy.TelevisoryIndicatorReportingFrequencyDTO;
import com.televisory.capitalmarket.entities.economy.CountryIndicators;
import com.televisory.capitalmarket.entities.economy.CountryList;
import com.televisory.capitalmarket.entities.economy.CurrencyList;
import com.televisory.capitalmarket.entities.economy.CurrencyMapping;
import com.televisory.capitalmarket.entities.economy.EconomyCreditRatingLatest;
import com.televisory.capitalmarket.entities.economy.ExchangeRatesComparison;
import com.televisory.capitalmarket.entities.economy.IndicatorDataForecast;
import com.televisory.capitalmarket.entities.economy.IndicatorData_old;
import com.televisory.capitalmarket.entities.economy.IndicatorHistoricalData;
import com.televisory.capitalmarket.entities.economy.IndicatorLatestData;
import com.televisory.capitalmarket.entities.economy.IndicatorName;
import com.televisory.capitalmarket.entities.economy.IndicatorType;
import com.televisory.capitalmarket.entities.economy.News;
import com.televisory.capitalmarket.entities.economy.TelevisoryIndicatorReportingFrequency;
import com.televisory.capitalmarket.model.PeriodicityEconomyRequest;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;


@Repository
@Transactional
public class EconomyRepositoryImpl implements EconomyRepository{
	
	Logger _log = Logger.getLogger(EconomyRepositoryImpl.class);
	
	/*@Autowired
	@Qualifier(value="macroSessionFactory")
	private SessionFactory sessionFactory;*/
	
	@Autowired
	@Qualifier(value="imfDataSessionFactory")
	private SessionFactory imfDataFactory;
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="teSessionFactory")
	private SessionFactory teSessionFactory;
	
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<CountryListDTO> findAllEconomyCountries() {
		
		String baseQuery = "SELECT cl.* FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` "
				+ "GROUP BY cl.`country_name`";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		List<CountryListDTO> eountryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return eountryListDTO;
	}
	
	@Override
	public CountryListDTO getDefaultCountryForEconomy(List<String> countryCode) {
		String baseQuery = " SELECT cl.*,count(*) as data_count FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` "+
				" where cl.country_iso_code_3 IN(:countryCodes) GROUP BY cl.`country_name` order by data_count desc, country_name ASC ";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameterList("countryCodes", countryCode);
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		List<CountryListDTO> eountryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return eountryListDTO.get(0);
	}

	@Override
	public List<IndicatorData_old> getEconomyIndicatorData(Integer countryId,Integer indicatorId) {
		_log.info("extracting indicator data for country "+countryId+" and indicator "+indicatorId);

		String baseQuery =  "SELECT indicators_data_id , value , period ,rating ,country_indicator_id,rating, is_active,created_at, created_by,modified_at, modified_by from (select * from (SELECT idt.indicators_data_id,rating,ci.period_type,iname.indicator_name_id , idt.value , idt.period  period ,idt.country_indicator_id, idt.is_active,idt.created_at, idt.created_by,idt.modified_at, idt.modified_by FROM imf_data.indicators_data idt " + 
				"	inner join imf_data.country_indicators  ci ON ci.country_indicators_id=idt.country_indicator_id " + 
				"	inner join imf_data.indicator_name iname ON iname.indicator_name_id=ci.indicator_name_id " + 
				"	where ci.country_id=:countryId and " + 
				"		iname.indicator_type_id=:indicatorId " + 
				"	order by idt.period desc)x group by indicator_name_id,period_type)y;";
		
		
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorId", indicatorId);
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			/*_log.info("indicatorData "+indicatorData);
			List<IndicatorDataDTO> indicatorDataDTO=null;
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO.class);	
			_log.info("indicatorDataDTO "+indicatorDataDTO);
			if(indicatorData!=null && indicatorData.size()>0) {
				if(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getDataType().equalsIgnoreCase("ABSOLUTE")) {
					
				}
				indicatorType.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
				indicatorType.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
				indicatorType.setIndicatorData(indicatorDataDTO);
			}else {
				return null;
			}*/
			return indicatorData;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	@Override
	public List<IndicatorData_old> getEconomyIndicatorData(Integer countryId,Integer indicatorId,Date startDate,Date endDate) {
		_log.info("extracting indicator data for country "+countryId+" and indicator "+indicatorId+" and startDate "+startDate+" and endDate "+endDate);
		String baseQuery =  "SELECT indicators_data_id , value , period ,rating ,country_indicator_id, is_active,created_at, created_by,modified_at, modified_by from (select * from (SELECT idt.indicators_data_id,rating,ci.period_type,iname.indicator_name_id , idt.value , idt.period  period ,idt.country_indicator_id, idt.is_active,idt.created_at, idt.created_by,idt.modified_at, idt.modified_by FROM imf_data.indicators_data idt " + 
				"	inner join imf_data.country_indicators  ci ON ci.country_indicators_id=idt.country_indicator_id " + 
				"	inner join imf_data.indicator_name iname ON iname.indicator_name_id=ci.indicator_name_id " + 
				"	where ci.country_id=:countryId and " + 
				"		iname.indicator_type_id=:indicatorId and " + 
				" idt.period BETWEEN :startDate AND :endDate "+
				"	order by idt.period desc)x group by indicator_name_id,period_type)y;";
	_log.info(baseQuery);
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorId", indicatorId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			return indicatorData;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	
	}

	@Override
	public List<IndicatorType> getAllIndicatorType() {
		String baseQuery =  "SELECT * FROM indicator_type where is_active=1";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorType.class);
		List<IndicatorType> indicatorTypes = (List<IndicatorType>) query.list();
		
		return indicatorTypes;
	}

	@Override
	public List<CountryIndicatorDTO> getCountryIndicatorPeriodicity(Integer countryId) {
		//String baseQuery =  "SELECT * FROM imf_data.country_indicators where indicator_name_id IN( select distinct indicator_name_id from imf_data.indicator_name where is_active=1 and chart_flag=1) and country_id=:countryId and is_active=1";
		String baseQuery =  "SELECT * FROM imf_data.country_indicators where indicator_name_id IN( select distinct indicator_name_id from imf_data.indicator_name where is_active=1 and chart_flag=1) and country_id=:countryId and is_active=1 and country_indicators_id in (select distinct country_indicator_id from indicators_data where period > curdate()-interval 7 year)";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryIndicators.class);
		query.setParameter("countryId", countryId);
		List<CountryIndicators> indicatorPeriodicity = (List<CountryIndicators>) query.list();
		//_log.info(" indicatorPeriodicity "+indicatorPeriodicity);
		
		
		List<CountryIndicatorDTO> countryIndicatorDTO = DozerHelper.map(dozerBeanMapper, indicatorPeriodicity, CountryIndicatorDTO.class);
		return countryIndicatorDTO;
	}
	
	@Override
	public List<CountryIndicatorDTO> getCountryIndicatorPeriodicityList(List<String> countryId) {
		//String baseQuery =  "SELECT * FROM imf_data.country_indicators where indicator_name_id IN( select distinct indicator_name_id from imf_data.indicator_name where is_active=1 and chart_flag=1) and country_id=:countryId and is_active=1";
		String baseQuery =  "SELECT * FROM imf_data.country_indicators where indicator_name_id IN( select distinct indicator_name_id from imf_data.indicator_name where is_active=1 and chart_flag=1) and country_id in (:countryId) and is_active=1 and country_indicators_id in (select distinct country_indicator_id from indicators_data where period > curdate()-interval 7 year)";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryIndicators.class);
		query.setParameterList("countryId", countryId);
		List<CountryIndicators> indicatorPeriodicity = (List<CountryIndicators>) query.list();
		//_log.info(" indicatorPeriodicity "+indicatorPeriodicity);
		
		
		List<CountryIndicatorDTO> countryIndicatorDTO = DozerHelper.map(dozerBeanMapper, indicatorPeriodicity, CountryIndicatorDTO.class);
		return countryIndicatorDTO;
	}
	

	@Override
	public IndicatorTypeDTO getIndicatorData(Integer countryId,Integer indicatorId,String periodType) {
		IndicatorTypeDTO indicatorType = new IndicatorTypeDTO();
		_log.info("extracting indicator data for country "+countryId+" and indicator "+indicatorId+" and periodType "+periodType);
		String baseQuery =  "SELECT idt.indicators_data_id,idt.rating,iname.indicator_name_id, idt.value, idt.period period, idt.country_indicator_id, idt.is_active, " + 
				"                       idt.created_at, idt.created_by, idt.modified_at, idt.modified_by " + 
				"                FROM   imf_data.indicators_data idt " + 
				"                       INNER JOIN imf_data.country_indicators ci ON " + 
				"                       ci.country_indicators_id = idt.country_indicator_id " + 
				"                       INNER JOIN imf_data.indicator_name iname ON " + 
				"                       iname.indicator_name_id = ci.indicator_name_id " + 
				"                WHERE  ci.country_id = :countryId AND iname.indicator_type_id = :indicatorId AND ci.period_type=:periodType AND " + 
				"                       idt.is_active=1 AND " + 
				"                       ci.is_active=1 AND " + 
				"                       iname.is_active=1 " + 
				"                ORDER  BY idt.period DESC ; ";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorId", indicatorId);
		query.setParameter("periodType", periodType);
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			//_log.info("indicatorData "+indicatorData);
			List<IndicatorDataDTO_old> indicatorDataDTO=null;
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			//_log.info("indicatorDataDTO "+indicatorDataDTO);
			//List<IndicatorTypeDTO> indicatorType = new ArrayList<IndicatorTypeDTO>();
			if(indicatorData!=null && indicatorData.size()>0) {
				indicatorType.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
				indicatorType.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
				indicatorType.setIndicatorData(indicatorDataDTO);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return indicatorType;
	}

	@Override
	public List<ExchangeRatesComparison> getLatestExchangeRate(String currencyCode) {
		
		Query query = factSetSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_factset_latest_exchange_rate(:currencyCode)")
				.addEntity(ExchangeRatesComparison.class);
		
		query.setParameter("currencyCode", currencyCode);
		
		List<ExchangeRatesComparison> exchangeRates = null;
		try {
			exchangeRates = (List<ExchangeRatesComparison>) query.list();
			//_log.info("exchangeRates "+exchangeRates);
			return exchangeRates;
		}catch(Exception e) {
			_log.error("preoblem occured in getting latest exchange for "+ currencyCode );
			e.printStackTrace();
			throw e;
		}		
	}
	
	@Override
	public List<ExchangeRatesComparison> getDummyExchangeRate(String currencyCode) {
		String baseQuery = "select `currency_id` as exchange_rate_id, '"+currencyCode+"' as sourceCurrencyCode, "
				+ "`iso_code` as targetCurrencyCode, 'DAILY' as period_type, null as period, "
				+ "null as value, null as sourceCurrencyName, currency_name as targetCurrencyName "
				+ "from currency_list where `is_active` = 1 group by `currency_id`";
		
		Query query = imfDataFactory.getCurrentSession()
				.createSQLQuery(baseQuery)
				.addEntity(ExchangeRatesComparison.class);
		//query.setParameter("currencyCode", currencyCode);
		
		List<ExchangeRatesComparison> exchangeRates = null;
		try {
			exchangeRates = (List<ExchangeRatesComparison>) query.list();
			return exchangeRates;
		}catch(Exception e) {
			_log.error("preoblem occured in getting latest exchange for "+ currencyCode );
			e.printStackTrace();
			throw e;
		}		
	}
	

	@Override
	public List<IndicatorDataDTO_old> getMajorEconomyData(Integer indicatorId, String periodType,Integer exceptCountryId) {
		//String baseQuery = "select * from indicators_data group by country_indicator_id limit 5";
		
		/*String baseQuery = "select " + 
				"indicators_data_id,country_indicator_id,period,value,is_active,created_at,created_by,modified_at,modified_by " + 
				"from (select * from (SELECT iidata.*, cii.country_id  FROM " + 
				"`indicators_data` iidata " + 
				"join `country_indicators` cii on iidata.`country_indicator_id` = " + 
				"cii.`country_indicators_id` " + 
				"and cii.`indicator_name_id` = :indicatorId and cii.`period_type` = :periodType and " + 
				"cii.country_id in " + 
				"" + 
				"(select * from (SELECT ci.country_id FROM `indicators_data` idata " + 
				"join `country_indicators` ci on idata.`country_indicator_id` = " + 
				"ci.`country_indicators_id` " + 
				"and idata.`period` > now() - INTERVAL 3 YEAR and ci.`indicator_name_id` " + 
				"= 17 and ci.`period_type` = 'YEARLY' and ci.`country_id` != :exceptCountryId" + 
				"group by ci.country_id order by value desc limit 5)x)" + 
				"" + 
				"order by iidata.period desc)y group by y.country_id, y.period order by " + 
				"y.period desc)z group by z.country_id";*/
		
		String baseQuery = "select * from( " + 
				"select * from indicators_data " + 
				"where country_indicator_id in(select country_indicators_id from (SELECT country_indicators_id, country_id FROM " + 
				"imf_data.country_indicators where country_id in(25,37, 44, 75, 84, 103,109,112,232,237) " + 
				"and period_type = :periodType AND indicator_name_id = :indicatorId group by country_id, period_type, indicator_name_id)x where x.country_id != :exceptCountryId) " + 
				"and period between date_sub(current_date, interval 5 year) and now() " + 
				"order by country_indicator_id, period desc)y " + 
				"group by country_indicator_id;";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("indicatorId", indicatorId);
		query.setParameter("periodType", periodType);
		query.setParameter("exceptCountryId", exceptCountryId);
		List<IndicatorDataDTO_old> indicatorDataDTO=null;
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			_log.info("indicatorData "+indicatorData.size());
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return indicatorDataDTO;
	}

	@Override
	public String getCountryName(Integer countryId) {
		String countryName="";
		String baseQuery = "select * FROM country_list WHERE country_id = :countryId AND is_active=1 ";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameter("countryId", countryId);
		
		try {
			List<CountryList> countryList = (List<CountryList>) query.list();
			_log.info("CountryList "+countryList);
			if(countryList!=null && countryList.size()!=0) {
				countryName =  countryList.get(0).getCountryName();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return countryName;
	}

	@Override
	public String getIndicatorName(Integer indicatorId) {
		String indicatorName="";
		String baseQuery = "select * FROM indicator_name WHERE indicator_name_id = :indicatorId AND is_active=1 ";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorName.class);
		query.setParameter("indicatorId", indicatorId);
		
		try {
			List<IndicatorName> indicatorNames = (List<IndicatorName>) query.list();
			_log.info("indicatorNames "+indicatorNames);
			if(indicatorNames!=null && indicatorNames.size()!=0) {
				indicatorName =  indicatorNames.get(0).getIndicatorsName();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return indicatorName;
	}

	/*@Override
	public List<IndicatorData> getPrevIndicatorData(int indicatorId, int countryId,String dataType,int indicatorTypeId,String minusPeriod, String plusPeriod) {
		String baseQuery =  "SELECT indicators_data_id , value ,rating, period  ,country_indicator_id, is_active,created_at, created_by,modified_at, modified_by from (select * from (SELECT idt.indicators_data_id,rating,ci.period_type,iname.indicator_name_id , idt.value , idt.period  period ,idt.country_indicator_id, idt.is_active,idt.created_at, idt.created_by,idt.modified_at, idt.modified_by FROM imf_data.indicators_data idt " + 
				"	inner join imf_data.country_indicators  ci ON ci.country_indicators_id=idt.country_indicator_id " + 
				"	inner join imf_data.indicator_name iname ON iname.indicator_name_id=ci.indicator_name_id " + 
				"	where ci.country_id=:countryId and " + 
				"		iname.indicator_type_id=:indicatorTypeId " + 
				"	and	iname.indicator_name_id=:indicatorId " +
				"	and	ci.period_type=:dataType " +
				" and idt.period BETWEEN :minusPeriod AND :plusPeriod "+
				"	order by idt.period desc)x group by indicator_name_id,period_type)y;";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData.class);
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorTypeId", indicatorTypeId);
		query.setParameter("indicatorId", indicatorId);
		query.setParameter("minusPeriod", minusPeriod);
		query.setParameter("dataType", dataType);
		query.setParameter("plusPeriod", plusPeriod);
		try {
			List<IndicatorData> indicatorData = (List<IndicatorData>) query.list();
			return indicatorData;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	@Override
	public List<IndicatorDataDTO_old> getPrevIndicatorData(int countryIndicatorId, String minusPeriod, String plusPeriod) {
		String baseQuery =  "select * from indicators_data where country_indicator_id = :countryIndicatorId and period between :minusPeriod and :plusPeriod order by period desc limit 1";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryIndicatorId", countryIndicatorId);
		query.setParameter("minusPeriod", minusPeriod);
		query.setParameter("plusPeriod", plusPeriod);
		/*try {
			List<IndicatorData> indicatorData = (List<IndicatorData>) query.list();
			return indicatorData;
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		
		
		List<IndicatorDataDTO_old> indicatorDataDTO=null;
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			//_log.info("indicatorData "+indicatorData.size());
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			//_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			return indicatorDataDTO;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, Date startDate, Date endDate) {
		IndicatorTypeDTO indicatorType = new IndicatorTypeDTO();
		_log.info("extracting indicator data for countryIndicator: "+countryIndicatorId);
		//String baseQuery =  "SELECT * from indicators_data where country_indicator_id =:countryIndicatorId ";
		
		String baseQuery =  "SELECT * from indicators_data where country_indicator_id =:countryIndicatorId and period >=DATE_ADD((select max(period) "+
				 " from indicators_data where country_indicator_id =:countryIndicatorId) , INTERVAL - 4 YEAR)";
		
		if(startDate!=null) {
			baseQuery+= "AND period > :startDate ";
		}
		if(endDate!=null) {
			baseQuery+= "AND period < :endDate  ";
		}
		baseQuery+= " order by period asc";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryIndicatorId", countryIndicatorId);
		if(startDate!=null) {
			query.setParameter("startDate", startDate);
		}
		if(endDate!=null) {
			query.setParameter("endDate", endDate);
		}
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			//_log.info("indicatorData "+indicatorData);
			List<IndicatorDataDTO_old> indicatorDataDTO=null;
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			//_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			//List<IndicatorTypeDTO> indicatorType = new ArrayList<IndicatorTypeDTO>();
			if(indicatorData!=null && indicatorData.size()>0) {
				indicatorType.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
				indicatorType.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
				indicatorType.setIndicatorData(indicatorDataDTO);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return indicatorType;
	}

	@Override
	public List<IndicatorDataDTO_old> getIndicatorDataByIndicator(Integer countryId, Integer indicatorId,String period_type) {
		String baseQuery = "select * from indicators_data idata  "
				+ "inner join country_indicators ci on ci.country_indicators_id = idata.country_indicator_id  "
				+ "where ci.indicator_name_id=:indicatorId and ci.country_id=:countryId "
				+ " and ci.is_active=1 and idata.is_active=1";
		
		if(period_type!=null && !period_type.isEmpty()) {
			baseQuery+=" and ci.period_type=:periodType ";
		}
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorId", indicatorId);
		if(period_type!=null && !period_type.isEmpty()) 
			query.setParameter("periodType", period_type);
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			List<IndicatorDataDTO_old> indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			return indicatorDataDTO;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<IndicatorDataDTO_old> getIndicatorDataByIndicator(Integer countryId, Integer indicatorId,String period_type,Date startDate,Date endDate) {
		String baseQuery = "select * from indicators_data idata  "
				+ "inner join country_indicators ci on ci.country_indicators_id = idata.country_indicator_id  "
				+ "where ci.indicator_name_id=:indicatorId and ci.country_id=:countryId and "
				+ " idata.period between :startDate and :endDate  and ci.is_active=1 and idata.is_active=1 ";
		if(period_type!=null && !period_type.isEmpty()) {
			baseQuery+=" and ci.period_type=:periodType ";
		}
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorId", indicatorId);
		if(period_type!=null && !period_type.isEmpty()) 
			query.setParameter("periodType", period_type);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			List<IndicatorDataDTO_old> indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			return indicatorDataDTO;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<IndicatorLatestDataDTO> findAllIndicators() {
		
		_log.info("Getting the indicator list");
		
		String baseQuery = "SELECT i.*,@rn \\:= @rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS "
				+ "tel_category_parent_id,NULL as yoy_change_flag , NULL as forecast_value_1q, NULL as forecast_value_2q, NULL as forecast_value_3q, "
				+ "NULL as forecast_value_4q, NULL As year_end, NULL As year_end2, NULL As year_end3 FROM trading_economic.`indicator_data_latest` i, (select @rn\\:=0) j WHERE is_active = 1 group by category,frequency";
		
		
		/*String baseQuery ="SELECT hl.*,cl.country_id,cl.country_iso_code_3,(select unit from indicator_data_latest where category=hl.category limit 1) as unit FROM `indicator_data_historical` hl join country_list cl on "
				+ "hl.country = cl.country_name where hl.is_active = 1 group by hl.`category`";*/
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorLatestData.class);
		
		List<IndicatorLatestData> indicatorNames = (List<IndicatorLatestData>) query.list();
		List<IndicatorLatestDataDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorLatestDataDTO.class);
	
		return indicatorNameDTO;
	}

	@Override
	public List<CountryListDTO> findEconomyCountriesByIndicator(String indicatorName) {
		String baseQuery = "SELECT cl.* FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country`  "
				+ "where il.category in (:indicatorName) and cl.is_active =1 GROUP BY cl.`country_name`";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameter("indicatorName", indicatorName);
		
		List<CountryList> data = query.list();
		List<CountryListDTO> countryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return countryListDTO;
	}
	
	@Override
	public List<CountryListDTO> findEconomyCountriesByIndicatorAndSubscribtion(String indicatorName, List<String> userCountryList){
		
		_log.info(" indicatorName "+indicatorName+" userCountryList "+userCountryList);
		
		String baseQuery = "SELECT cl.* FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country`  "
				+ "where il.category in (:indicatorName) and cl.country_iso_code_3 IN(:userCountryList) and cl.is_active =1 GROUP BY cl.`country_name`";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameter("indicatorName", indicatorName);
		query.setParameterList("userCountryList", userCountryList);
		
		List<CountryList> data = query.list();
		List<CountryListDTO> countryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return countryListDTO;
	}

	@Override
	public List<IndicatorLatestDataDTO> findIndicatorsByCountry(List<String> countryIsoCodeList) {
			
		String baseQuery = "SELECT i.*,@rn \\:= @rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS "
				+ "tel_category_parent_id ,NULL as yoy_change_flag, NULL as forecast_value_1q, NULL as forecast_value_2q, NULL as forecast_value_3q, NULL as forecast_value_4q"
				+ ", NULL As year_end, NULL As year_end2, NULL As year_end3 FROM trading_economic.`indicator_data_latest` i "
				+ "inner join televisory_indicator_list til on til.tel_category = i.category, "
				+ "(select @rn\\:=0) j WHERE country in "
				+ "(select country_name from country_list where country_iso_code_3 in ( :countryIsoCodeList)) and i.is_active = 1 and til.is_active=1 "
				+ "group by category,frequency";
		
		
		/*String baseQuery = "SELECT hl.*,cl.country_id,cl.country_iso_code_3,(select unit from indicator_data_latest where category=hl.category limit 1) as unit FROM `indicator_data_historical` hl join country_list cl on "
				+ "hl.country = cl.country_name where cl.country_iso_code_3 in (:countryIsoCodeList) and hl.is_active = 1 group by hl.`category`";
		*/
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorLatestData.class);
		
		query.setParameterList("countryIsoCodeList", countryIsoCodeList);
		
		List<IndicatorLatestData> indicatorNames = (List<IndicatorLatestData>) query.list();
		
		List<IndicatorLatestDataDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorLatestDataDTO.class);
		
		return indicatorNameDTO;
	}
	
	@Override
	public List<CountryIndicatorDTO> getIndicatorPeriodicity(Integer indicatorId,Integer countryId) {
		String baseQuery =  "select * from country_indicators where indicator_name_id=:indicatorId and country_id=:countryId and is_active=1";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryIndicators.class);
		query.setParameter("indicatorId", indicatorId);
		query.setParameter("countryId", countryId);
		List<CountryIndicators> indicatorPeriodicity = (List<CountryIndicators>) query.list();
		List<CountryIndicatorDTO> countryIndicatorDTO = DozerHelper.map(dozerBeanMapper, indicatorPeriodicity, CountryIndicatorDTO.class);
		return countryIndicatorDTO;
	}
	
	@Override
	public List<CountryIndicatorDTO> getIndicatorPeriodicityList(Integer indicatorId,List<String> countryId) {
		String baseQuery =  "select * from country_indicators where indicator_name_id=:indicatorId and country_id in (:countryId) and is_active=1";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryIndicators.class);
		query.setParameter("indicatorId", indicatorId);
		query.setParameterList("countryId", countryId);
		List<CountryIndicators> indicatorPeriodicity = (List<CountryIndicators>) query.list();
		List<CountryIndicatorDTO> countryIndicatorDTO = DozerHelper.map(dozerBeanMapper, indicatorPeriodicity, CountryIndicatorDTO.class);
		return countryIndicatorDTO;
	}

	@Override
	public List<CountryListDTO> findEconomyCountriesByMultipleIndicator(List<Integer> indicatorList) {
		String baseQuery = "SELECT * FROM   country_list WHERE  country_id IN (SELECT DISTINCT country_id FROM   country_indicators WHERE indicator_name_id IN (:indicatorList));";
		_log.info(" indicator list "+indicatorList.toString());
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameterList("indicatorList", indicatorList);
		List<CountryList> data = query.list();
		List<CountryListDTO> eountryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return eountryListDTO;
	}

	@Override
	public List<IndicatorDTO> findIndicatorsByMultipleCountry(List<Integer> countryList) {
		String baseQuery = "select * From indicator_name where indicator_name_id IN (select distinct indicator_name_id from country_indicators where country_id IN (:countryList) and is_active=1) and is_active=1";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorName.class);
		query.setParameterList("countryList", countryList);
		List<IndicatorName> indicatorNames = (List<IndicatorName>) query.list();
		List<IndicatorDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorDTO.class);
		return indicatorNameDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPeriodicity(PeriodicityEconomyRequest periodicityRequest) {
		String baseQuery =  "select distinct period_type from country_indicators where indicator_name_id IN(:indicatorList) and country_id IN(:countryList) and is_active=1";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameterList("indicatorList", periodicityRequest.getIndicatorList());
		query.setParameterList("countryList", periodicityRequest.getCountryList());
		List<String> indicatorPeriodicity = (List<String>) query.list();
		return indicatorPeriodicity;
	}

	@Override
	public IndicatorTypeDTO getIndicatorData(Integer countryId, Integer indicatorTypeId, String periodType,String currency) {

		IndicatorTypeDTO indicatorType = new IndicatorTypeDTO();
		_log.info("extracting indicator data for country "+countryId+" and indicator "+indicatorTypeId+" and periodType "+periodType);
		String baseQuery =  "SELECT idt.indicators_data_id,idt.rating,iname.indicator_name_id, " + 
				"       CASE " + 
				"       		WHEN ci.period_type = 'MONTHLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_monthly_conversion(:in_currency,'USD', idt.period) " + 
				"       		WHEN ci.period_type = 'YEARLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_year_conversion(:in_currency,'USD', idt.period) " + 
				"       		WHEN ci.period_type = 'QUARTERLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_quarter_conversion(:in_currency,'USD', idt.period) " + 
				"       		WHEN ci.period_type = 'DAILY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_daily_conversion(:in_currency,'USD', idt.period) " + 
				"       		WHEN ci.currency is NULL THEN idt.value" + 
				"       	END as value," + 
				"       idt.period period, " + 
				"       idt.country_indicator_id, " + 
				"       idt.is_active, " + 
				"       idt.created_at, " + 
				"       idt.created_by, " + 
				"       idt.modified_at, " + 
				"       idt.modified_by " + 
				"FROM   imf_data.indicators_data idt " + 
				"       INNER JOIN imf_data.country_indicators ci " + 
				"               ON ci.country_indicators_id = idt.country_indicator_id " + 
				"       INNER JOIN imf_data.indicator_name iname " + 
				"               ON iname.indicator_name_id = ci.indicator_name_id " + 
				"WHERE  ci.country_id = :countryId " + 
				"       AND iname.indicator_type_id = :indicatorTypeId " + 
				"       AND ci.period_type = :periodType" + 
				"       AND idt.is_active = 1 " + 
				"       AND ci.is_active = 1 " + 
				"       AND iname.is_active = 1 " + 
				"ORDER  BY idt.period DESC ";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryId", countryId);
		query.setParameter("indicatorId", indicatorTypeId);
		query.setParameter("periodType", periodType);
		query.setParameter("in_currency", currency);
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			//_log.info("indicatorData "+indicatorData);
			List<IndicatorDataDTO_old> indicatorDataDTO=null;
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			//_log.info("indicatorDataDTO "+indicatorDataDTO);
			//List<IndicatorTypeDTO> indicatorType = new ArrayList<IndicatorTypeDTO>();
			if(indicatorData!=null && indicatorData.size()>0) {
				indicatorType.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
				indicatorType.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
				indicatorType.setIndicatorData(indicatorDataDTO);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return indicatorType;
	}

	@Override
	public IndicatorTypeDTO getIndicatorDataByCountryIndicator(Integer countryIndicatorId, String currency, Date startDate, Date endDate) {

		IndicatorTypeDTO indicatorType = new IndicatorTypeDTO();
		_log.info("extracting indicator data for countryIndicator: "+countryIndicatorId);
		String baseQuery =  "SELECT indicators_data_id, country_indicator_id, period, " + 
				"       CASE " + 
				"       		WHEN ci.period_type = 'MONTHLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_monthly_conversion(:in_currency,'USD',idt.period) " + 
				"       		WHEN ci.period_type = 'YEARLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_year_conversion(:in_currency,'USD',idt.period) " + 
				"       		WHEN ci.period_type = 'QUARTERLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_quarter_conversion(:in_currency,'USD',idt.period) " + 
				"       		WHEN ci.period_type = 'DAILY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_daily_conversion(:in_currency,'USD',idt.period) " + 
				"       		WHEN ci.currency is NULL THEN idt.value" + 
				"       	END as value, " + 
				"       rating,idt.is_active,idt.created_at, idt.created_by, idt.modified_at, idt.modified_by " + 
				"FROM   indicators_data idt " + 
				"       INNER JOIN country_indicators ci " + 
				"               ON ci.country_indicators_id = idt.country_indicator_id " + 
				"WHERE  country_indicator_id = :countryIndicatorId and idt.period >=DATE_ADD((select max(period) " + 
				" from indicators_data where country_indicator_id =:countryIndicatorId) , INTERVAL - 4 YEAR) ";
		if(startDate!=null) {
			baseQuery+= "AND idt.period > :startDate ";
		}
		if(endDate!=null) {
			baseQuery+= "AND idt.period < :endDate  ";
		}
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryIndicatorId", countryIndicatorId);
		query.setParameter("in_currency", currency);
		
		if(startDate!=null) {
			query.setParameter("startDate", startDate);
		}
		if(endDate!=null) {
			query.setParameter("endDate", endDate);
		}
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			_log.info("indicatorData "+indicatorData);
			List<IndicatorDataDTO_old> indicatorDataDTO=null;
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			//List<IndicatorTypeDTO> indicatorType = new ArrayList<IndicatorTypeDTO>();
			if(indicatorData!=null && indicatorData.size()>0) {
				indicatorType.setId(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getId());
				indicatorType.setName(indicatorData.get(0).getCountryIndicator().getIndicatorsName().getIndicatorsType().getIndicatorType());
				indicatorType.setIndicatorData(indicatorDataDTO);
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return indicatorType;
	
	}

	@Override
	public List<IndicatorData_old> getEconomyIndicatorData(List<String> countryList, List<String> indicatorList,
			Date startDate, Date startDate2) {
		return null;
	}
	
	@Override
	public String getCountryIsoCode(Integer countryId) {
		String countryCode="";
		String baseQuery = "select * FROM country_list WHERE country_id = :countryId AND is_active=1 ";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameter("countryId", countryId);
		
		try {
			List<CountryList> countryList = (List<CountryList>) query.list();
			_log.info("CountryList "+countryList);
			if(countryList!=null && countryList.size()!=0) {
				countryCode =  countryList.get(0).getCountryIsoCode3();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return countryCode;
	}
	


	@Override
	public List<IndicatorDataDTO_old> getEconomyRequestIndicatorData(Integer countryId, Integer indicatorId, String currency,String periodicity, Date startDate, Date endDate) {

		_log.info("extracting indicator data for indicatorId: "+indicatorId+" countryId "+countryId+" currency "+currency+" periodicity "+periodicity+ " startDate : " + startDate + " endDate : " + endDate);

		
		// Query to get latest 10 year data ....... Further Modified to 20 Years.
		String baseQuery = "SELECT idt.indicators_data_id, idt.rating, iname.indicator_name_id, " + 
				"       CASE WHEN ci.period_type = 'MONTHLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_monthly_conversion(ci.currency,'USD', idt.period) " + 
				"            WHEN ci.period_type = 'YEARLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_year_conversion(ci.currency, 'USD',idt.period) " + 
				"            WHEN ci.period_type = 'QUARTERLY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_quarter_conversion(ci.currency, 'USD',idt.period) " + 
				"            WHEN ci.period_type = 'DAILY' AND ci.currency is not NULL THEN idt.value * factset.get_fx_daily_conversion(ci.currency,'USD', idt.period) " + 
				"            WHEN ci.currency is NULL THEN idt.value " + 
				"       END  as value, " + 
				"       idt.period period, idt.country_indicator_id, idt.is_active, idt.created_at, idt.created_by, idt.modified_at, idt.modified_by " + 
				"FROM   imf_data.indicators_data idt " + 
				"       INNER JOIN imf_data.country_indicators ci " + 
				"               ON ci.country_indicators_id = idt.country_indicator_id " + 
				"       INNER JOIN imf_data.indicator_name iname " + 
				"               ON iname.indicator_name_id = ci.indicator_name_id " + 
				"WHERE  ci.country_id = :countryId AND iname.indicator_name_id = :indicatorId AND ci.period_type = :periodicity " + 
				"       AND idt.is_active = 1 AND ci.is_active = 1 " + 
				"       AND iname.is_active = 1 and idt.period >=DATE_ADD((select max(period) " + 
				"				 from indicators_data idtt INNER JOIN imf_data.country_indicators cii " + 
				"               ON cii.country_indicators_id = idtt.country_indicator_id INNER JOIN imf_data.indicator_name inn  " + 
				"               ON inn.indicator_name_id = cii.indicator_name_id WHERE  cii.country_id = :countryId AND cii.period_type = :periodicity AND cii.is_active = 1 " + 
				"AND inn.indicator_name_id = :indicatorId AND cii.is_active = 1 AND inn.is_active = 1) , INTERVAL - 19 YEAR) ";
		
		
		if(startDate!=null) {
			baseQuery+= "AND idt.period > :startDate ";
		}
		if(endDate!=null) {
			baseQuery+= "AND idt.period < :endDate  ";
		}
		baseQuery+= "ORDER  BY idt.period DESC";
		
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorData_old.class);
		query.setParameter("countryId", countryId);
		//query.setParameter("currency", currency);
		query.setParameter("periodicity", periodicity);
		query.setParameter("indicatorId", indicatorId);
		
		if(startDate!=null) {
			query.setParameter("startDate", startDate);
		}
		if(endDate!=null) {
			query.setParameter("endDate", endDate);
		}
		List<IndicatorDataDTO_old> indicatorDataDTO=null;
		try {
			List<IndicatorData_old> indicatorData = (List<IndicatorData_old>) query.list();
			//_log.info("indicatorData "+indicatorData);
			indicatorDataDTO = DozerHelper.map(dozerBeanMapper, indicatorData, IndicatorDataDTO_old.class);	
			_log.info("indicatorDataDTO "+indicatorDataDTO.size());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return indicatorDataDTO;
	
	}

	@Override
	public List<CountryListDTO> findCountriesBySearchCriteria(String searchCriteria,Integer resultCount) {
		String baseQuery = "SELECT cl.* FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` "
				+ "where  cl.country_name like '%"+searchCriteria+"%'  GROUP BY cl.`country_name`";
	
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		
		if(resultCount!=null){
			_log.info("limiting the no of results");
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		
		List<CountryListDTO> countryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		
		return countryListDTO;
	}
	
	@Override
	public List<CountryListDTO> findCountriesBySubscribedCountry(List<String> userCountryList) {
		String baseQuery = "SELECT cl.* FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` "
				+ "where  cl.country_iso_code_3 IN(:userCountryList)  GROUP BY cl.`country_name`";
	
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameterList("userCountryList", userCountryList);
		
		
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		
		List<CountryListDTO> countryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		
		return countryListDTO;
	}

	@Override
	public List<CurrencyListDTO> getAllCurrency() {
		//String baseQuery = "select * From currency_list  cl where is_active=1";
		String baseQuery = "select cl.* From currency_list  cl  join factset.ref_v2_iso_currency_map cmt on cl.iso_code = cmt.iso_currency and cmt.active = 1 and cl.is_active=1 order by cl.iso_code asc ";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CurrencyList.class);
		@SuppressWarnings("unchecked")
		List<CurrencyList> data = query.list();
		_log.info("data "+data.size());
		List<CurrencyListDTO> currencyListDTO = DozerHelper.map(dozerBeanMapper, data, CurrencyListDTO.class);
		return currencyListDTO;
	}

	@Override
	public List<CurrencyListDTO> getAllCurrency(String searchCriteria) {
		String baseQuery = "select * From currency_list where is_active=1 AND currency_name like :searchCriteria order by cl.iso_code asc ";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CurrencyList.class);
		query.setParameter("searchCriteria", "%"+searchCriteria+"%");
		@SuppressWarnings("unchecked")
		List<CurrencyList> data = query.list();
		_log.info("data "+data.size());
		List<CurrencyListDTO> currencyListDTO = DozerHelper.map(dozerBeanMapper, data, CurrencyListDTO.class);
		return currencyListDTO;
	}

	@Override
	public CurrencyMappingDTO getCountryCurrency(String countryId) throws Exception {
	
		String baseQuery="from CurrencyMapping where country.id =:countryId ";
		
		Query query = teSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("countryId", Integer.parseInt(countryId));
		
		@SuppressWarnings("unchecked")
		List<CurrencyMapping> data = (List<CurrencyMapping>) query.list();
		
		List<CurrencyMappingDTO> cMappingDTO = DozerHelper.map(dozerBeanMapper, data, CurrencyMappingDTO.class);
		
		//_log.info(" cMappingDTO "+cMappingDTO);
		if(cMappingDTO.size()!=0){
			return cMappingDTO.get(0);
		}else{
			throw new Exception("NO DATA AVAIL");
		}
	}
	
	@Override
	public CurrencyMappingDTO getCountryCurrencyByIsoCode(String countryIsoCode) throws Exception {
	
		String baseQuery="from CurrencyMapping where country_code =:countryIsoCode ";
		
		Query query = teSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("countryIsoCode", countryIsoCode);
		
		@SuppressWarnings("unchecked")
		List<CurrencyMapping> data = (List<CurrencyMapping>) query.list();
		
		List<CurrencyMappingDTO> cMappingDTO = DozerHelper.map(dozerBeanMapper, data, CurrencyMappingDTO.class);
		
		if(cMappingDTO.size()!=0){
			return cMappingDTO.get(0);
		}else{
			throw new Exception("NO DATA AVAIL");
		}
	}

	@Override
	public List<ExchangeRatesComparison> getFactSetExchangeRate(String sourceCurrencyCode, String targetCurrencyCode, Date startDate,Date endDate) {

		_log.info("getting ExchangeRate based on sourceCurrencyCode "+sourceCurrencyCode+" and targetCurrencyCode "+targetCurrencyCode);

		Query query = factSetSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_factset_exchange_rate(:sourceCurrencyCode,:targetCurrencyCode,:startDate,:endDate)")
				.addEntity(ExchangeRatesComparison.class);
		
		query.setParameter("sourceCurrencyCode", sourceCurrencyCode);
		query.setParameter("targetCurrencyCode", targetCurrencyCode);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		List<ExchangeRatesComparison> exchangeRates = null;
		
		try {
		
			exchangeRates = (List<ExchangeRatesComparison>) query.list();
			return exchangeRates;
		
		}catch(Exception e) {
			_log.error("preoblem occured in getting exchange for "+ sourceCurrencyCode +" to "+ targetCurrencyCode);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<IndicatorDTO> findIndicators(List<Integer> countryList) {
		String baseQuery = "select * From indicator_name where indicator_name_id IN (select distinct indicator_name_id from country_indicators where country_id IN(:countryList) and is_active=1) and is_active=1";
		Query query = imfDataFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorName.class);
		query.setParameter("countryList", countryList);
		List<IndicatorName> indicatorNames = (List<IndicatorName>) query.list();
		List<IndicatorDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorDTO.class);
		return indicatorNameDTO;
	}
	
	@Override
	public List<CountryListDTO> getCMCountriesByIdList(List<String> customCountryIds) {
		_log.info("extracting Company metadata info of : "+customCountryIds+" from database");
		
		Query query = imfDataFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM cm.country_list where country_id in (:customCountryIds)").addEntity(CountryList.class);
		query.setParameterList("customCountryIds",customCountryIds);
		
		List<CountryList> data = (List<CountryList>) query.list();
		
		List<CountryListDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		
		return cmCompanyDTOs;
		
		
	}

	@Override
	public Double getFxData(String periodType, Date fxDate, String currencyCode) {
		String queryStr = "SELECT get_fx_quarter_conversion(:currencyCode,'USD',:fxDate)";
		
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			queryStr = "SELECT get_fx_year_conversion(:currencyCode,'USD',:fxDate)";
		}
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(queryStr);
		
		query.setParameter("currencyCode", currencyCode);
		query.setParameter("fxDate", fxDate);
		
		Double fxRate = (Double) query.uniqueResult();
		return fxRate;
	}
	
	@Override
	public Double getFxData(String periodType, Date fxDate, String currencyCode, String countryCurrency) {
		String queryStr = "SELECT get_fx_quarter_conversion(:currencyCode,:countryCurrency,:fxDate)";
		
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			queryStr = "SELECT get_fx_year_conversion(:currencyCode,:countryCurrency,:fxDate)";
		}
		
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_DAILY)) {
			queryStr = "SELECT get_fx_daily_conversion(:currencyCode,:countryCurrency,:fxDate)";
		}
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(queryStr);
		
		query.setParameter("currencyCode", currencyCode);
		query.setParameter("fxDate", fxDate);
		query.setParameter("countryCurrency", countryCurrency);
		
		Double fxRate = (Double) query.uniqueResult();
		return fxRate;
	}
	
	
	@Override
	public Double getDailyFxData(String periodType, Date fxDate, String currencyCode, String countryCurrency) {
		String queryStr = "SELECT get_fx_daily_conversion(:currencyCode,:countryCurrency,:fxDate)";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(queryStr);

		query.setParameter("currencyCode", currencyCode);
		query.setParameter("countryCurrency", countryCurrency);
		query.setParameter("fxDate", fxDate);

		Double fxRate = (Double) query.uniqueResult();
		return fxRate;
	}


	@Override
	public List<IndicatorLatestDataDTO> getEconomyIndicatorLatestData(String countryIsoCode, Date startDate,
			Date endDate) {
		
		_log.info("extracting latest economy data for country "+countryIsoCode+ " for starting date "+startDate +" endDate "+endDate);
		
		/*Query query = teSessionFactory.getCurrentSession().createSQLQuery("SELECT l.*,idf.*,t.display_order,t.tel_indicator_id,t.show_default,t.tel_category,t.tel_category_group"
				+ ",t.tel_category_parent_id,t.yoy_change_flag FROM trading_economic.televisory_indicator_list t "
				+ "left join indicator_data_latest l on t.tel_category=l.category and  l.country= (select country_name from country_list where country_iso_code_3 = :countryIsoCode) "
				+ " left join indicator_data_forecast idf on idf.historical_data_symbol = l.historical_data_symbol order by t.display_order").addEntity(IndicatorLatestData.class);*/
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery("SELECT t.*, l.*,idf.* FROM trading_economic.televisory_indicator_list t "
				+ "LEFT JOIN indicator_data_latest l ON t.tel_category = l.category and l.country = (SELECT country_name FROM country_list "
				+ "WHERE country_iso_code_3 = :countryIsoCode) and t.is_active =1 LEFT JOIN "
				+ "indicator_data_forecast idf ON idf.historical_data_symbol = l.historical_data_symbol "
				+ "and l.category=idf.category and l.country=idf.country ORDER BY t.display_order").addEntity(IndicatorLatestData.class);
		
		query.setParameter("countryIsoCode",countryIsoCode);
		/*query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);*/
		
		_log.info(query.toString());
	
		List<IndicatorLatestData> latestIndicatorData =query.list();
		
		List<IndicatorLatestDataDTO> latestIndicatorDataDTO = DozerHelper.map(dozerBeanMapper, latestIndicatorData, IndicatorLatestDataDTO.class);
		
		return latestIndicatorDataDTO;
	}

	@Override
	public List<IndicatorLatestDataDTO> getEconomyIndicatorLatestDataByCurrency(String countryName, String currency,
			Date startDate, Date endDate) {
		
		_log.info("extracting latest economy data for country "+countryName+ " for starting date "+startDate +" endDate "+endDate);
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery("SELECT idl.*,t.display_order,til.tel_indicator_id,til.tel_category,til.show_default,til.tel_category_group,til.tel_category_parent_id,til.yoy_change_flag FROM `televisory_indicator_list` til left join indicator_data_latest idl on "
				+ "til.`tel_category`=idl.category where idl.country like :countryName and latest_value_date between :startDate and :endDate").addEntity(IndicatorLatestData.class);
		
		query.setParameter("countryName",countryName);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		
		List<IndicatorLatestData> latestIndicatorData =query.list();
		
		List<IndicatorLatestDataDTO> latestIndicatorDataDTO = DozerHelper.map(dozerBeanMapper, latestIndicatorData, IndicatorLatestDataDTO.class);
	
		return latestIndicatorDataDTO;
		
	}

	@Override
	public List<IndicatorHistoricalDataDTO> getDataByIndicatorAndCountry(List<String> countryIsoCodeList, List<String> indicatorNameList, String periodType,
			Date startDate,Date endDate) {
		
		_log.info("extracting historical economy data" +"----"+countryIsoCodeList.get(0)+"----"+indicatorNameList.get(0)+"--------"+startDate+"---------"+endDate);
		
		/*Query query = teSessionFactory.getCurrentSession().createSQLQuery("select x.*,cl.country_iso_code_3,cl.country_id,null as unit from "
				+ "(SELECT idl. *,til.tel_category,til.tel_category_group,til.tel_category_parent_id FROM `televisory_indicator_list` "
				+ "til join indicator_data_historical idl on til.`tel_category`=idl.category where idl.country in (select country_name "
				+ "from country_list where country_iso_code_3 in(:countryIsoCodeList)) and idl.frequency = :periodType and category in( :indicatorNameList) and "
				+ "idl.is_active =1 and til.is_active =1 and datetime between :startDate and :endDate) as x join country_list cl on x.country = cl.country_name").addEntity(IndicatorHistoricalData.class);*/
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery("SELECT cl.*,hl.*,(select unit from indicator_data_latest "
				+ "where category=hl.category and country=hl.country limit 1) as unit FROM `indicator_data_historical` hl join country_list cl "
				+ "on hl.`country` =cl.country_name where hl.`category`in(:indicatorNameList) and hl.`frequency` = :periodType "
						+ "and hl.`is_active`=1 and cl.country_iso_code_3 in(:countryIsoCodeList) and datetime BETWEEN :startDate and :endDate").addEntity(IndicatorHistoricalData.class);
		
		query.setParameterList("countryIsoCodeList",countryIsoCodeList);
		query.setParameterList("indicatorNameList",indicatorNameList);
		query.setParameter("periodType",periodType);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		
		List<IndicatorHistoricalData> histIndicatorData =query.list();
		List<IndicatorHistoricalDataDTO> histIndicatorDataDto = DozerHelper.map(dozerBeanMapper, histIndicatorData, IndicatorHistoricalDataDTO.class);
	
		return histIndicatorDataDto;
	}

	@Override
	public List<IndicatorHistoricalDataDTO> getDataByIndicatorAndCountry(List<String> countryIsoCodeList, List<String> indicatorNameList,
			String periodType, String currency, Date startDate, Date endDate) {

		
		
		String baseQuery = "select cl.country_iso_code_3,cl.country_id,hl.country,hl.indicator_datahis_id,hl.datetime,hl.frequency,hl.historical_data_symbol,hl.last_update,hl.is_active,hl.category,hl.last_modified_at, "
				+ "hl.created_at,hl.created_by,hl.last_modified_by,l.currency_flag, case when l.currency_flag=1 then "
				+ "replace(unit,get_currency(unit),:currency) else unit end as unit, case l.currency_flag when 1 then case  when hl.frequency='Quarterly' then "
				+ "hl.close *  factset.get_fx_quarter_conversion(get_currency(unit),:currency,datetime)"
				+ "when (hl.frequency='Yearly' or hl.frequency='Annual') then "
				+ "hl.close *  factset.get_fx_year_conversion(get_currency(unit),:currency,datetime) when hl.frequency='Biannually' then "
				+ "hl.close * factset.get_fx_semiannually_conversion(get_currency(unit),:currency,datetime) when hl.frequency='Monthly' then "
				+ "hl.close * factset.get_fx_monthly_conversion(get_currency(unit),:currency,datetime) when (hl.frequency='Weekly' or hl.frequency='Biweekly') "
				+ "then hl.close * factset.get_fx_weekly_conversion(get_currency(unit),:currency,datetime) when hl.frequency='Daily' then "
				+ "hl.close * factset.get_fx_daily_conversion(get_currency(unit),:currency,datetime) end else hl.close end  as close "
				+ "FROM `indicator_data_historical` hl join country_list cl on hl.`country` =cl.country_name "
				+ "join indicator_data_latest l on l.category=hl.category and l.frequency=hl.frequency and l.country=hl.country "
				+ "where hl.`category` in(:indicatorNameList) and hl.`frequency` = :periodType and hl.`is_active`=1 "
						+ "and cl.country_iso_code_3 in (:countryIsoCodeList) and datetime BETWEEN :startDate and :endDate";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorHistoricalData.class);

		query.setParameterList("indicatorNameList",indicatorNameList);
		query.setParameterList("countryIsoCodeList",countryIsoCodeList);
		query.setParameter("currency",currency);
		query.setParameter("periodType",periodType);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		
		List<IndicatorHistoricalData> histIndicatorData =query.list();
		
		List<IndicatorHistoricalDataDTO> histIndicatorDataDto = DozerHelper.map(dozerBeanMapper, histIndicatorData, IndicatorHistoricalDataDTO.class);
		return histIndicatorDataDto;
	}

	@Override
	public List<NewsDTO> getNews(String countryIsoCode) {
		_log.info("getting the news for country  "+countryIsoCode);
		
		String baseQuery = "SELECT * FROM `news` WHERE `country` =  (select country_name from country_list where country_iso_code_3 = :countryIsoCode) and is_active =1 order by date desc";
		Query query =teSessionFactory.getCurrentSession()
				.createSQLQuery(baseQuery).addEntity(News.class);
		
		query.setParameter("countryIsoCode", countryIsoCode);
		
		List<NewsDTO> newsList =query.list();
		return newsList;
	}

	@Override
	public CountryListDTO getCountryByIsoCode(String countryIsoCode) {
	
		String baseQuery = "SELECT cl.* FROM country_list cl where cl.country_iso_code_3 = :countryIsoCode";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameter("countryIsoCode", countryIsoCode);
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		List<CountryListDTO> countryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		
		if(!countryListDTO.isEmpty()){
			return countryListDTO.get(0);
		}
		
		return null;
	}

	@Override
	public List<String> getIndicatorPeriodIcity(List<String> indicatorName,List<String> countryList) {
		
		String baseQuery = "SELECT hd.frequency FROM `indicator_data_historical` hd join country_list cl on hd.`country`=cl.country_name where cl.country_iso_code_3 in(:countryList) and hd.`category` in (:indicatorNameList) GROUP BY `frequency`;";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameterList("indicatorNameList", indicatorName);
		query.setParameterList("countryList", countryList);
		
		return query.list();
		
	}

	@Override
	public List<EconomyCreditRatingLatestDTO> getEconomyCreditRatingByCountry(String countryIsoCode) {

		_log.info("Getting credit rating data for country "+countryIsoCode);
		
		String baseQuery = "SELECT * FROM `credit_rating_latest` WHERE `country` =(select country_name from country_list where country_iso_code_3 =:countryIsoCode)";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(EconomyCreditRatingLatest.class);
		query.setParameter("countryIsoCode", countryIsoCode);
		
		List<EconomyCreditRatingLatestDTO> creditReportData =query.list();
		
		List<EconomyCreditRatingLatestDTO> histIndicatorDataDto = DozerHelper.map(dozerBeanMapper, creditReportData, EconomyCreditRatingLatestDTO.class);
	
		return histIndicatorDataDto;	
	}

	@Override
	public CountryListDTO getCMCountryByIsoCodes(String countryIsoCode) {
		
		_log.info("Getting country details for Iso Code"+countryIsoCode);
		
		String baseQuery = "SELECT * FROM `country_list` WHERE `country_iso_code_3`=:countryIsoCode";
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		
		query.setParameter("countryIsoCode", countryIsoCode);
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		List<CountryListDTO> eountryListDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return eountryListDTO.get(0);
		
	}

	@Override
	public List<IndicatorLatestDataDTO> findAllIndicators(String searchParam) {
		
		String baseQuery="";
		
		baseQuery = "SELECT i.*,@rn \\:= @rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS "
				+ "tel_category_parent_id, NULL as yoy_change_flag FROM trading_economic.`indicator_data_latest` i, (select @rn\\:=0) j WHERE is_active = 1 and category like :searchParam group by category,frequency order by latest_value desc";
		
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(IndicatorLatestData.class);
		query.setParameter("searchParam", "%"+searchParam+"%");
		
		List<IndicatorLatestData> indicatorNames = (List<IndicatorLatestData>) query.list();
		
		List<IndicatorLatestDataDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorLatestDataDTO.class);
	
		return indicatorNameDTO;
	}

	@Override
	public List<TelevisoryIndicatorReportingFrequencyDTO> getReportedFrequencyBasedOnCountryAndCategory(String countryName, String countryIsoCode, String category){
		
		_log.info("CountryName ::: " +  countryName + " , Category :::: " + category);
		
		String querryString = "SELECT * FROM `televisory_indicator_reporting_frequency` where category = :category and country_name like(select country_name from country_list WHERE `country_iso_code_3` LIKE :countryIsoCode)";
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(querryString).addEntity(TelevisoryIndicatorReportingFrequency.class);
		query.setParameter("countryIsoCode", countryIsoCode);
		query.setParameter("category", category);
		
		@SuppressWarnings("unchecked")
		List<TelevisoryIndicatorReportingFrequency> dataList = (List<TelevisoryIndicatorReportingFrequency>) query.list();
		List<TelevisoryIndicatorReportingFrequencyDTO> televisoryIndicatorReportingFrequencyDTOList = DozerHelper.map(dozerBeanMapper, dataList, TelevisoryIndicatorReportingFrequencyDTO.class);
		return televisoryIndicatorReportingFrequencyDTOList;
	}

	@Override
	public List<IndicatorDataForecastDTO> getDefaultCountryForEconomy(String historicalDataSymbol) {
		
		_log.info("historicalDataSymbol :::: " + historicalDataSymbol);
		
		String querryString = "SELECT * FROM `indicator_data_forecast` where historical_data_symbol = :historicalDataSymbol";
	
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(querryString).addEntity(IndicatorDataForecast.class);
		query.setParameter("historicalDataSymbol", historicalDataSymbol);
		
		
		@SuppressWarnings("unchecked")
		List<IndicatorDataForecast> dataList = (List<IndicatorDataForecast>) query.list();
		List<IndicatorDataForecastDTO> indicatorDataForecastDTOList = DozerHelper.map(dozerBeanMapper, dataList, IndicatorDataForecastDTO.class);
		
		return indicatorDataForecastDTOList;
		
	}
}
