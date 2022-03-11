package com.televisory.bond.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.televisory.bond.dao.FIDao;
import com.televisory.bond.dto.FIBalanceModelDTO;
import com.televisory.bond.dto.FIComparableDTO;
import com.televisory.bond.dto.FIHistoricalDTO;
import com.televisory.bond.dto.FILatestDataDTO;
import com.televisory.bond.dto.FINameDTO;
import com.televisory.bond.entity.FIBalanceModel;
import com.televisory.bond.entity.FIComparable;
import com.televisory.bond.entity.FIHistorical;
import com.televisory.bond.entity.FIDataLatest;
import com.televisory.bond.utils.BondStaticParams;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Primary
@Transactional
public class FIDaoImpl implements FIDao{
	
	public static Logger _log = Logger.getLogger(FIDaoImpl.class);
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<String> getFiCurrencyList(String categeory) {
		
		_log.info("getting the iso_currency_code list for: "+ categeory);

		String baseQuery;
		Query query = null;
		
		if(categeory!="" && categeory!=null){
			baseQuery="SELECT distinct `iso_currency_code` FROM `fi_data_latest` where industry_lvl_1_desc = :categeory";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setParameter("categeory", categeory);
		}else{
			baseQuery="SELECT distinct `iso_currency_code` FROM `fi_data_latest`";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		}

		@SuppressWarnings("unchecked")
		List<String> iso_currency_codeListDTO = query.list();

		return iso_currency_codeListDTO;
	
	}

	@Override
	public List<FINameDTO> getFiNameList(String categeory, String iso_currency_code, String searchCriteria) {
		
		_log.info("getting the FI Name list for categeory: "+categeory+", iso_currency_code: "+ iso_currency_code +", searchCriteria: "+searchCriteria );
	
		String baseQuery;
		Query query = null;
		
		//Check blank value and initialize with null to reduce if condition
		if(categeory != null && categeory.equals(""))
			categeory = null;
		if(iso_currency_code != null && iso_currency_code.equals(""))
			iso_currency_code = null;
		if(searchCriteria != null && searchCriteria.equals(""))
			searchCriteria = null;
		
		try {
			if(categeory != null && iso_currency_code != null && searchCriteria != null) {
				baseQuery="select * from (SELECT * FROM `fi_data_latest` WHERE `industry_lvl_1_desc` = :categeory and `iso_currency_code` = :iso_currency_code and (`description` like :descriptionQ1 or `isin_number` like :descriptionQ1) order by description) x " + 
						  "UNION " + 
						  "select * from (SELECT * FROM `fi_data_latest` WHERE `industry_lvl_1_desc` = :categeory and `iso_currency_code` = :iso_currency_code and (`description` like :descriptionQ2 or `isin_number` like :descriptionQ2)  order by description) y " + 
						  "group by `isin_number` limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("categeory", categeory);
				query.setParameter("iso_currency_code", iso_currency_code);
				query.setParameter("descriptionQ1", searchCriteria+"%");
				query.setParameter("descriptionQ2", "%"+searchCriteria+"%");
			} else if(iso_currency_code != null && searchCriteria != null) {
				
				baseQuery="select * from (SELECT * FROM `fi_data_latest` WHERE `iso_currency_code` = :iso_currency_code and (`description` like :descriptionQ1 or `isin_number` like :descriptionQ1) order by description) x " + 
						  "UNION " + 
						  "select * from (SELECT * FROM `fi_data_latest` WHERE `iso_currency_code` = :iso_currency_code and (`description` like :descriptionQ2 or `isin_number` like :descriptionQ2) order by description) y " + 
						  "group by `isin_number` limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("iso_currency_code", iso_currency_code);
				query.setParameter("descriptionQ1", searchCriteria+"%");
				query.setParameter("descriptionQ2", "%"+searchCriteria+"%");
			} else if(categeory != null && searchCriteria != null) {
				
				baseQuery="select * from (SELECT * FROM `fi_data_latest` WHERE `industry_lvl_1_desc` = :categeory and (`description` like :descriptionQ1 or `isin_number` like :descriptionQ1) order by description) x " + 
						  "UNION " + 
						  "select * from (SELECT * FROM `fi_data_latest` WHERE `industry_lvl_1_desc` = :categeory and (`description` like :descriptionQ2 or `isin_number` like :descriptionQ2) order by description) y " + 
						  "group by `isin_number` limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("categeory", categeory);
				query.setParameter("descriptionQ1", searchCriteria+"%");
				query.setParameter("descriptionQ2", "%"+searchCriteria+"%");
				
			} else if(categeory != null && iso_currency_code != null) {
				
				baseQuery="SELECT * FROM `fi_data_latest` WHERE `industry_lvl_1_desc` = :categeory and `iso_currency_code` = :iso_currency_code order by description limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("categeory", categeory);
				query.setParameter("iso_currency_code", iso_currency_code);
			} else if(categeory != null) {
				
				baseQuery="SELECT * FROM `fi_data_latest` WHERE `industry_lvl_1_desc` = :categeory order by description limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("categeory", categeory);
				
			} else if(iso_currency_code != null) {
				
				baseQuery="SELECT * FROM `fi_data_latest` WHERE `iso_currency_code` = :iso_currency_code order by description limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("iso_currency_code", iso_currency_code);
				
			} else if(searchCriteria != null) {
				
				baseQuery="select * from (SELECT * FROM `fi_data_latest` WHERE (`description` like :descriptionQ1 or `isin_number` like :descriptionQ1) order by description) x " + 
						  "UNION " + 
						  "select * from (SELECT * FROM `fi_data_latest` WHERE (`description` like :descriptionQ2 or `isin_number` like :descriptionQ2) order by description) y " + 
						  "group by `isin_number` limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				query.setParameter("descriptionQ1", searchCriteria+"%");
				query.setParameter("descriptionQ2", "%"+searchCriteria+"%");
				
			} else {
				
				baseQuery="SELECT * FROM `fi_data_latest` order by description limit 1000";
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
				
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		List<FIDataLatest> fiNameData = query.list();
		List<FINameDTO> fiNameDataDTO = DozerHelper.map(dozerBeanMapper, fiNameData, FINameDTO.class);
		return fiNameDataDTO;
	}

	@Override
	public String getDefaultFIIdentifier() {
		
		_log.info("getting Default FI Identifier");

		String baseQuery="select * from (select * from (SELECT `isin_number` FROM `fi_data_latest` WHERE `isin_number` = :defaultIdentifier) x " + 
				" union " + 
				" select * from (SELECT `isin_number` FROM `fi_data_latest` limit 1) y ) z " + 
				" limit 1";
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("defaultIdentifier", BondStaticParams.DEFAULT_FI_IDENTIFIER);
		
		@SuppressWarnings("unchecked")
		List<String> defaultFIIdentifiers = query.list();

		return defaultFIIdentifiers.get(0);
		
	}

	@Override
	public List<String> getFiCategeoryList() {
		
		_log.info("getting the categeory list");

		String baseQuery;
		Query query = null;
		
		baseQuery="SELECT distinct `industry_lvl_1_desc` FROM `fi_data_latest`";
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);

		@SuppressWarnings("unchecked")
		List<String> categeoryListDTO = query.list();

		return categeoryListDTO;
		
	}

	@Override
	public List<FIBalanceModelDTO> getFIMetricList() {
		
		_log.info("getting the metric list");

		String baseQuery="SELECT * FROM fi_balance_model where chart_history_flag=:chart_history_flag order by display_order ";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIBalanceModel.class);
		query.setParameter("chart_history_flag", 1);
		@SuppressWarnings("unchecked")
		List<FIBalanceModel> data = (List<FIBalanceModel>) query.list();

		List<FIBalanceModelDTO> metricListDTO = DozerHelper.map(dozerBeanMapper, data, FIBalanceModelDTO.class);	
		
		return metricListDTO;
	
	}

	@Override
	public FILatestDataDTO getLatestDateData(String identifier) throws Exception {
		
		_log.info("getting the LatestData for identifier: "+ identifier);
		
		String baseQuery;
		
		baseQuery="SELECT * FROM `fi_data_latest` WHERE isin_number = :identifier";
	
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIDataLatest.class);
		query.setParameter("identifier", identifier);
		
		@SuppressWarnings("unchecked")
		List<FIDataLatest> latestData = query.list();
		
		List<FILatestDataDTO> latestDataDTO = DozerHelper.map(dozerBeanMapper, latestData, FILatestDataDTO.class);
		
		if(latestDataDTO.size()>0){
			return latestDataDTO.get(0);
		}else{
			throw new Exception("data not available");
		}
	}

	@Override
	public List<FIHistoricalDTO> getHistoricalData(String identifier, String fieldName) {
		
		_log.info("getting the Historical Data for identifier: "+identifier +", fieldName"+ fieldName);
		
		String baseQuery;
		
		baseQuery="SELECT isin_number,description,ticker,as_of_date , '"+fieldName+"' as field_name,  "+fieldName+" as data "
				+ " FROM `fi_data` WHERE isin_number = :identifier order by as_of_date asc";
	
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIHistorical.class);
		query.setParameter("identifier", identifier);
		
		@SuppressWarnings("unchecked")
		List<FIHistorical> historicalData = query.list();
		
		List<FIHistoricalDTO> historicalDataDTO = DozerHelper.map(dozerBeanMapper, historicalData, FIHistoricalDTO.class);
		return historicalDataDTO;
		
	}


	@Override
	public List<FIComparableDTO> getComparable(String identifier, List<String> includeIdentifier, List<String> excludeIdentifier) throws Exception {

		_log.info("getting FI comparable for identifier: "+ identifier +", includeIdentifier: "+includeIdentifier +", excludeIdentifier: "+ excludeIdentifier);
		
		//these will be used after adding feature of edit in FO
		String includeIdentifierString = null;
		String excludeIdentifierString = null;
		
		if(includeIdentifier != null && includeIdentifier.size()>0)
			includeIdentifierString = String.join(",", includeIdentifier);
		if(excludeIdentifier != null && excludeIdentifier.size()>0)
			excludeIdentifierString = String.join(",", excludeIdentifier);

		
		String baseQuery ="Call get_fi_comparables( :identifier, :limitCount, :fieldNames, :includeIdentifierString, :excludeIdentifierString)";

		Query query=  cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FIComparable.class);


		query.setParameter("identifier",identifier);
		query.setParameter("limitCount",BondStaticParams.FI_COMPARABLE_LIMIT);
		query.setParameter("fieldNames",BondStaticParams.FI_COMPARABLE_FIELFNAMES);
		query.setParameter("includeIdentifierString",includeIdentifierString);
		query.setParameter("excludeIdentifierString",excludeIdentifierString);

		List<FIComparable> data =(List<FIComparable>) query.list();

		List<FIComparableDTO> fiComparableDTOs = DozerHelper.map(dozerBeanMapper, data, FIComparableDTO.class);

		return fiComparableDTOs;
	}

}
