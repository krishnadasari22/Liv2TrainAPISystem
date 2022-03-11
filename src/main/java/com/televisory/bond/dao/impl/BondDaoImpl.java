package com.televisory.bond.dao.impl;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.televisory.bond.dao.IBondDao;
import com.televisory.bond.dto.CDSBalanceModelDTO;
import com.televisory.bond.dto.CDSComparableDTO;
import com.televisory.bond.dto.CDSDataLatestDTO;
import com.televisory.bond.dto.CDSHistoricalDataDTO;
import com.televisory.bond.dto.CDSNameDTO;
import com.televisory.bond.entity.CDSBalanceModel;
import com.televisory.bond.entity.CDSComparable;
import com.televisory.bond.entity.CDSData;
import com.televisory.bond.entity.CDSDataDetails;
import com.televisory.bond.entity.CDSDataLatest;
import com.televisory.bond.entity.CDSHistoricalData;
import com.televisory.bond.entity.FIDataLatest;
import com.televisory.bond.utils.BondStaticParams;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Primary
@Transactional
public class BondDaoImpl implements IBondDao{

	Logger _log = Logger.getLogger(BondDaoImpl.class);

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;


	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public String getDefaultCDSIdentifier() {

		_log.info("getting Default CDS Identifier");

		String baseQuery="select * from (select * from (SELECT `cds_identifier` FROM `cds_data_latest` WHERE `cds_identifier` = :defaultIdentifier) x " + 
				" union " + 
				" select * from (SELECT `cds_identifier` FROM `cds_data_latest` limit 1) y ) z " + 
				" limit 1";
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("defaultIdentifier", BondStaticParams.DEFAULT_CDS_IDENTIFIER);

		@SuppressWarnings("unchecked")
		List<String> defaultCDSIdentifiers = query.list();

		return defaultCDSIdentifiers.get(0);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAdvanceBondSearchGen(Map<String,String> filters, String requestType) {
		System.out.println("Getting the "+requestType +" Data");
		_log.info("filters :::: " + filters);
		StringBuilder query = new StringBuilder();

		if(requestType==null || requestType.equals(BondStaticParams.CDS)){
			query.append("from CDSDataLatest ");
		}else if(requestType.equals(BondStaticParams.BOND)){
			query.append("from FIDataLatest ");
		}



		int i=0;
		for (String key : filters.keySet()) {
			String value = filters.get(key);
			if(i==0){
				if(value==null){
					query.append("where " +key+"=null");
				}else{
					if(value.contains(":::")){
						String[] valArr = value.split(":::");
						if(valArr.length==2){
							if(!valArr[0].equals("") && !valArr[1].equals("")){
								query.append("where " +key+" between "+valArr[0]+" AND " + valArr[1]);
							}else if(!valArr[0].equals("") && valArr[1].equals("")){
								query.append("where " +key+" <= "+valArr[0]);
							}else if(valArr[0].equals("") && !valArr[1].equals("")){
								query.append("where " +key+" >= "+valArr[1]);
							}
						}else{
							query.append("where " +key+" <= "+valArr[0]);
						}
					}else{
						Field field = null;
						try{
							if(requestType==null || requestType.equals(BondStaticParams.CDS)){
								field = CDSDataLatest.class.getDeclaredField(key);
							}else if(requestType.equals(BondStaticParams.BOND)){
								field = FIDataLatest.class.getDeclaredField(key);
							}
							if(String.class.equals(field.getType())){
								query.append("where " +key+" LIKE '%"+value+"%'");
							}else{
								query.append("where " +key+" = '"+value+"'");
							}
						}catch(Exception e){
							try{
								if(requestType==null || requestType.equals(BondStaticParams.CDS)){
									field = CDSDataDetails.class.getDeclaredField(key);
								}else if(requestType.equals(BondStaticParams.BOND)){
									field = FIDataLatest.class.getDeclaredField(key);
								}
								if(String.class.equals(field.getType())){
									query.append("where " +key+" LIKE '%"+value+"%'");
								}else{
									query.append("where " +key+" = '"+value+"'");
								}
							}catch(Exception e1){
								e1.printStackTrace();
							}
						}
					}
				}
				i++;
			}else{
				if(value==null){
					query.append(" AND "+key+"=null");
				}else{
					if(value.contains(":::")){
						String[] valArr = value.split(":::");
						if(valArr.length==2){
							if(!valArr[0].equals("") && !valArr[1].equals("")){
								query.append(" AND " +key+" between "+valArr[0]+" AND " + valArr[1]);
							}else if(!valArr[0].equals("") && valArr[1].equals("")){
								query.append(" AND " +key+" <= "+valArr[0]);
							}else if(valArr[0].equals("") && !valArr[1].equals("")){
								query.append(" AND " +key+" >= "+valArr[1]);
							}
						}else{
							query.append(" AND " +key+" <= "+valArr[0]);
						}
					}else{

						Field field = null;
						try{
							if(requestType==null || requestType.equals(BondStaticParams.CDS)){
								field = CDSDataLatest.class.getDeclaredField(key);
							}else if(requestType.equals(BondStaticParams.BOND)){
								field = FIDataLatest.class.getDeclaredField(key);
							}
							if(String.class.equals(field.getType())){
								query.append("AND " +key+" LIKE '%"+value+"%'");
							}else{
								query.append("AND " +key+" = '"+value+"'");
							}
						}catch(Exception e){
							try{
								if(requestType==null || requestType.equals(BondStaticParams.CDS)){
									CDSDataDetails.class.getDeclaredField(key);
								}else if(requestType.equals(BondStaticParams.BOND)){
									field = FIDataLatest.class.getDeclaredField(key);
								}
								if(String.class.equals(field.getType())){
									query.append("AND " +key+" LIKE '%"+value+"%'");
								}else{
									query.append("AND " +key+" = '"+value+"'");
								}
							}catch(Exception e1){
								e1.printStackTrace();
							}
						}


					}
				}
			}
		}


		try {
			String queryString = query.toString();
			_log.info("Advance search query :::: " + query);
			Query myQuery = cmSessionFactory.getCurrentSession().createQuery(queryString);
			myQuery.setMaxResults(1000);
			List<T> data = (List<T>) myQuery.list();
			return data;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}

		return null;
	}


	/*@SuppressWarnings("unchecked")
	public List<CDSDataLatest> getAdvanceBondSearch(Map<String,String> filters, String requestType) {
		System.out.println("Getting the bond Data");
		StringBuilder query = new StringBuilder();

		if(requestType.equals(BondStaticParams.BOND)){
			query.append("from CDSDataLatest ");
		}else if(requestType.equals(BondStaticParams.CDS)){
			query.append("from CDSDataLatest ");
		}

		//query.append("Select * from cds_data_latest ");
		//query.append("from CDSDataLatest ");

		int i=0;
		for (String key : filters.keySet()) {
			String value = filters.get(key);
			if(i==0){
				if(value==null){
					query.append("where " +key+"=null");
				}else{
					if(value.contains(":::")){
						String[] valArr = value.split(":::");
						if(valArr.length==2){
							if(!valArr[0].equals("") && !valArr[1].equals("")){
								query.append("where " +key+" between "+valArr[0]+" AND " + valArr[1]);
							}else if(!valArr[0].equals("") && valArr[1].equals("")){
								query.append("where " +key+" >= "+valArr[0]);
							}else if(valArr[0].equals("") && !valArr[1].equals("")){
								query.append("where " +key+" <= "+valArr[1]);
							}
						}else{
							query.append("where " +key+" <= "+valArr[0]);
						}
					}else{
						query.append("where " +key+"= '"+value+"'");

					}
				}
				i++;
			}else{
				if(value==null){
					query.append(" AND "+key+"=null");
				}else{
					if(value.contains(":::")){
						String[] valArr = value.split(":::");
						if(valArr.length==2){
							if(!valArr[0].equals("") && !valArr[1].equals("")){
								query.append(" AND " +key+" between "+valArr[0]+" AND " + valArr[1]);
							}else if(!valArr[0].equals("") && valArr[1].equals("")){
								query.append(" AND " +key+" >= "+valArr[0]);
							}else if(valArr[0].equals("") && !valArr[1].equals("")){
								query.append(" AND " +key+" <= "+valArr[1]);
							}
						}else{
							query.append(" AND " +key+" <= "+valArr[0]);
						}
					}else{
						query.append(" AND " +key+" = '"+value+"'");

					}
				}
			}
		}

		System.out.println(query);
		try {
			String queryString = query.toString();
			Query myQuery = cmSessionFactory.getCurrentSession().createQuery(queryString);
			myQuery.setMaxResults(1000);
			//Query myQuery = cmSessionFactory.getCurrentSession().createSQLQuery(queryString).addEntity(CDSData.class);
			List<CDSDataLatest> data = null;
			if(requestType.equals(BondStaticParams.BOND)){
				data = (List<CDSDataLatest>) myQuery.list();
			}else if(requestType.equals(BondStaticParams.CDS)){
				data = (List<CDSDataLatest>) myQuery.list();
			}

			//List<CDSDataLatest> data = (List<CDSDataLatest>) myQuery.list();
			_log.info(data.size());
			return data;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}

		return null;
	}
	 */

	@Override
	public Map<String,Object> getProjectedValues(String vals,String moduleName) {
		_log.info(vals + " ::::: " + moduleName);
		Map<String, Object> hm = new ConcurrentHashMap<String, Object>();
		try {
			Criteria criteria = null;
			if(moduleName==null || moduleName.equalsIgnoreCase(BondStaticParams.BOND)){
				criteria = cmSessionFactory.getCurrentSession().createCriteria(FIDataLatest.class);
				criteria = criteria.setProjection(Projections.projectionList().add(Projections.property(vals)));
				criteria.addOrder(Order.asc(vals));
			}else if(moduleName.equalsIgnoreCase(BondStaticParams.CDS)){
				criteria = cmSessionFactory.getCurrentSession().createCriteria(CDSDataLatest.class);
				try{
					Field field = CDSDataLatest.class.getDeclaredField(vals);	
					criteria = criteria.setProjection(Projections.projectionList().add(Projections.property(vals)));
					criteria.addOrder(Order.asc(vals));
					_log.info("Field is :: " + field);
				}catch(Exception e){
					_log.error("Field not found checking the internal class");
					criteria = criteria.setProjection(Projections.projectionList().add(Projections.property("cdsDataDetails."+vals)));
					criteria.addOrder(Order.asc("cdsDataDetails."+vals));
				}
			}

			@SuppressWarnings("unchecked")
			Set<?> dataList = new CopyOnWriteArraySet<>(criteria.list());
			if(dataList!=null){
				hm.put(vals, dataList);
			}
			return hm;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	}



	@Override
	public Map<String,Object> getProjectedValuesNon(String vals,String moduleName) {
		Map<String, Object> hm = new HashMap<String, Object>();
		try {
			String query = "Select distinct("+vals+") from cds_data_latest";
			try {
				String queryString = query.toString();
				Query myQuery = cmSessionFactory.getCurrentSession().createSQLQuery(queryString);
				List<Object> data = (List<Object>) myQuery.list();

				for (Object object : data) {
					System.out.println(object);
				}

				_log.info(data.size());
			}catch(Exception e){
				_log.error(e.getMessage(),e);
			}
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	}



	@Override
	public Map<String,Object> getCurrentValues(Map<String,String> vals,String moduleName) {
		Map<String, Object> hm = new HashMap<String, Object>();
		try {
			String queryString = "SELECT entity_name FROM cds_data_latest limit 1";
			Query myQuery = cmSessionFactory.getCurrentSession().createSQLQuery(queryString).addEntity(CDSData.class);
			String pattern = "yyyy-MM-dd";
			DateFormat df = new SimpleDateFormat(pattern);
			List<CDSData> cDSDataList = myQuery.list();	
			return hm;	
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public Map<String,Object> getProjectedValues(List<String> vals) {
		return null;
	}


	@Override
	public List<CDSBalanceModelDTO> getCdsMetricList() {

		_log.info("getting the metric list");

		String baseQuery="SELECT * FROM cds_balance_model order by table_display_order";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSBalanceModel.class);

		@SuppressWarnings("unchecked")
		List<CDSBalanceModel> data = (List<CDSBalanceModel>) query.list();

		List<CDSBalanceModelDTO> metricListDTO = DozerHelper.map(dozerBeanMapper, data, CDSBalanceModelDTO.class);	
		return metricListDTO;
	}


	@Override
	public List<String> getCdsCountryList() {

		_log.info("getting the country list");

		String baseQuery="SELECT distinct `Country` FROM `cds_data_mapping`";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);

		@SuppressWarnings("unchecked")
		List<String> countryListDTO = query.list();

		return countryListDTO;

	}


	@Override
	public List<String> getCdsCurrencyList(String sector) {

		_log.info("getting the currency list for: "+ sector);

		String baseQuery;
		Query query = null;

		if(sector!="" && sector!=null){
			baseQuery="SELECT distinct `currency` FROM `cds_data_latest` where sector = :sector";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setParameter("sector", sector);
		}else{
			baseQuery="SELECT distinct `currency` FROM `cds_data_latest`";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		}

		@SuppressWarnings("unchecked")
		List<String> currencyListDTO = query.list();

		return currencyListDTO;
	}


	@Override
	public CDSDataLatestDTO getLatestDateData(String identifier) throws Exception {

		_log.info("getting the LatestData for identifier: "+ identifier);

		String baseQuery;

		baseQuery="SELECT * FROM `cds_data_latest` WHERE cds_identifier = :identifier";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSDataLatest.class);
		query.setParameter("identifier", identifier);

		@SuppressWarnings("unchecked")
		List<CDSDataLatest> latestData = query.list();

		List<CDSDataLatestDTO> latestDataDTO = DozerHelper.map(dozerBeanMapper, latestData, CDSDataLatestDTO.class);

		if(latestDataDTO.size()>0){
			return latestDataDTO.get(0);
		}else{
			throw new Exception("data not available");
		}


	}

	@Override
	public List<CDSNameDTO> getCdsNameList(String sector, String currency, String searchCriteria) {

		_log.info("getting the cds Name list for sector: "+sector+", currency: "+ currency +", searchCriteria: "+searchCriteria );

		String baseQuery;
		Query query;

		//Check blank value and initialize with null to reduce if condition
		if(sector != null && sector.equals(""))
			sector = null;
		if(currency != null && currency.equals(""))
			currency = null;
		if(searchCriteria != null && searchCriteria.equals(""))
			searchCriteria = null;

		if(sector != null && currency != null && searchCriteria != null) {
			baseQuery="select * from (SELECT * FROM `cds_data_latest` WHERE `sector` = :sector and `currency` = :currency and `entity_name` like :entityNameQ1 order by entity_name) x " + 
					"UNION " + 
					"select * from (SELECT * FROM `cds_data_latest` WHERE `sector` = :sector and `currency` = :currency and `entity_name` like :entityNameQ2 order by entity_name) y " + 
					"group by `cds_identifier` limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("sector", sector);
			query.setParameter("currency", currency);
			query.setParameter("entityNameQ1", searchCriteria+"%");
			query.setParameter("entityNameQ2", "%"+searchCriteria+"%");
		} else if(currency != null && searchCriteria != null) {

			baseQuery="select * from (SELECT * FROM `cds_data_latest` WHERE `currency` = :currency and `entity_name` like :entityNameQ1 order by entity_name) x " + 
					"UNION " + 
					"select * from (SELECT * FROM `cds_data_latest` WHERE `currency` = :currency and `entity_name` like :entityNameQ2 order by entity_name) y " + 
					"group by `cds_identifier` limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("currency", currency);
			query.setParameter("entityNameQ1", searchCriteria+"%");
			query.setParameter("entityNameQ2", "%"+searchCriteria+"%");
		} else if(sector != null && searchCriteria != null) {

			baseQuery="select * from (SELECT * FROM `cds_data_latest` WHERE `sector` = :sector and `entity_name` like :entityNameQ1 order by entity_name) x " + 
					"UNION " + 
					"select * from (SELECT * FROM `cds_data_latest` WHERE `sector` = :sector and `entity_name` like :entityNameQ2 order by entity_name) y " + 
					"group by `cds_identifier` limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("sector", sector);
			query.setParameter("entityNameQ1", searchCriteria+"%");
			query.setParameter("entityNameQ2", "%"+searchCriteria+"%");

		} else if(sector != null && currency != null) {

			baseQuery="SELECT * FROM `cds_data_latest` WHERE `sector` = :sector and `currency` = :currency order by entity_name limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("sector", sector);
			query.setParameter("currency", currency);
		} else if(sector != null) {

			baseQuery="SELECT * FROM `cds_data_latest` WHERE `sector` = :sector order by entity_name limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("sector", sector);

		} else if(currency != null) {

			baseQuery="SELECT * FROM `cds_data_latest` WHERE `currency` = :currency order by entity_name limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("currency", currency);

		} else if(searchCriteria != null) {

			baseQuery="select * from (SELECT * FROM `cds_data_latest` WHERE `entity_name` like :entityNameQ1 order by entity_name) x " + 
					"UNION " + 
					"select * from (SELECT * FROM `cds_data_latest` WHERE `entity_name` like :entityNameQ2 order by entity_name) y " + 
					"group by `cds_identifier` limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("entityNameQ1", searchCriteria+"%");
			query.setParameter("entityNameQ2", "%"+searchCriteria+"%");

		} else {

			baseQuery="SELECT * FROM `cds_data_latest` order by entity_name limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);

		}
		@SuppressWarnings("unchecked")
		List<CDSData> cdsNameData = query.list();
		List<CDSNameDTO> cdsNameDataDTO = DozerHelper.map(dozerBeanMapper, cdsNameData, CDSNameDTO.class);
		return cdsNameDataDTO;
	}


	@Override
	public List<CDSNameDTO> getCdsNameListByCurrency(String searchCriteria, String currency) {

		_log.info("getting the cds Name list for:"+searchCriteria );

		String baseQuery;
		Query query;

		if(searchCriteria=="" || searchCriteria==null){

			baseQuery="SELECT * FROM `cds_data_latest` where currency = :currency";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
		}else{

			baseQuery="SELECT * FROM `cds_data_latest` WHERE `entity_name` LIKE :entityName and currency = :currency";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("entityName", "%"+searchCriteria+"%");

		}

		query.setFirstResult(0);
		query.setMaxResults(1000);

		@SuppressWarnings("unchecked")
		List<CDSData> cdsNameData = query.list();

		List<CDSNameDTO> cdsNameDataDTO = DozerHelper.map(dozerBeanMapper, cdsNameData, CDSNameDTO.class);

		return cdsNameDataDTO;

	}


	@Override
	public List<CDSNameDTO> getCdsNameListByCountryCurrency(String searchCriteria, String country, String currency) {

		_log.info("getting the cds Name list for:"+searchCriteria );

		String baseQuery;
		Query query;

		if(searchCriteria=="" || searchCriteria==null){

			baseQuery="SELECT * FROM `cds_data_latest`";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
		}else{

			baseQuery="SELECT * FROM `cds_data_latest` WHERE `entity_name` LIKE :entityName";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("entityName", "%"+searchCriteria+"%");

		}

		query.setFirstResult(0);
		query.setMaxResults(1000);

		@SuppressWarnings("unchecked")
		List<CDSData> cdsNameData = query.list();

		List<CDSNameDTO> cdsNameDataDTO = DozerHelper.map(dozerBeanMapper, cdsNameData, CDSNameDTO.class);

		return cdsNameDataDTO;

	}


	@Override
	public List<CDSNameDTO> getCdsNameListByCountry(String searchCriteria, String country) {

		_log.info("getting the cds Name list for:"+searchCriteria );

		String baseQuery;
		Query query;

		if(searchCriteria=="" || searchCriteria==null){

			baseQuery="SELECT * FROM `cds_data_latest`";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
		}else{

			baseQuery="SELECT * FROM `cds_data_latest` WHERE `entity_name` LIKE :entityName";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);
			query.setParameter("entityName", "%"+searchCriteria+"%");

		}

		query.setFirstResult(0);
		query.setMaxResults(1000);

		@SuppressWarnings("unchecked")
		List<CDSData> cdsNameData = query.list();
		List<CDSNameDTO> cdsNameDataDTO = DozerHelper.map(dozerBeanMapper, cdsNameData, CDSNameDTO.class);

		return cdsNameDataDTO;

	}


	@Override
	public List<CDSHistoricalDataDTO> getHistoricalData(String identifier, String fieldName) {

		_log.info("getting the Historical Data for identifier: "+identifier +", fieldName"+ fieldName);

		String baseQuery;
		String startDate = null;

		try {
			baseQuery="select min(business_date_time) from `cds_data` WHERE cds_identifier = :identifier and "+fieldName+" is not null";
			Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setParameter("identifier", identifier);
			startDate = query.list().get(0).toString();
		} catch (Exception e){
		}
		
		baseQuery="SELECT entity_name,currency,tenor,restructuring_type,seniority , business_date_time, '"+fieldName+"' as field_name,  "+fieldName+" as data "
				+ " FROM `cds_data` WHERE cds_identifier = :identifier";
		
		if(startDate != null)
			baseQuery += " and  business_date_time >= '"+ startDate +"'";
		
		baseQuery += " order by business_date_time asc ";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSHistoricalData.class);
		query.setParameter("identifier", identifier);

		@SuppressWarnings("unchecked")
		List<CDSHistoricalData> historicalData = query.list();

		List<CDSHistoricalDataDTO> historicalDataDTO = DozerHelper.map(dozerBeanMapper, historicalData, CDSHistoricalDataDTO.class);
		return historicalDataDTO;

	}


	@Override
	public List<String> getCdsSectorList() {

		_log.info("getting the sector list");

		String baseQuery="SELECT distinct `sector` FROM `cds_data_latest`";
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);

		@SuppressWarnings("unchecked")
		List<String> currencyListDTO = query.list();

		return currencyListDTO;
	}


	/*	@Override
	public CDSDataDTO getLatestDateData() throws Exception {

		_log.info("getting the LatestData");

		String baseQuery;

		baseQuery="SELECT * FROM `cds_data_latest`";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSData.class);


		@SuppressWarnings("unchecked")
		List<CDSData> latestData = query.list();

		List<CDSDataDTO> latestDataDTO = DozerHelper.map(dozerBeanMapper, latestData, CDSDataDTO.class);

		if(latestDataDTO.size()>0){
			return latestDataDTO.get(0);
		}else{
			throw new Exception("data not available");

		}

	}*/

	@Override
	public List<CDSComparableDTO> getComparable(String identifier, List<String> includeIdentifier, List<String> excludeIdentifier) throws Exception {

		_log.info("getting CDS comparable for identifier: "+ identifier +", includeIdentifier: "+includeIdentifier +", excludeIdentifier: "+ excludeIdentifier);

		//these will be used after adding feature of edit in FO
		String includeIdentifierString = null;
		String excludeIdentifierString = null;

		if(includeIdentifier != null && includeIdentifier.size()>0)
			includeIdentifierString = String.join(",", includeIdentifier);
		if(excludeIdentifier != null && excludeIdentifier.size()>0)
			excludeIdentifierString = String.join(",", excludeIdentifier);


		String baseQuery ="Call get_cds_comparables(:identifier, :limitCount, :fieldNames, :includeIdentifierString, :excludeIdentifierString)";

		Query query=  cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CDSComparable.class);


		query.setParameter("identifier",identifier);
		query.setParameter("limitCount",BondStaticParams.CDS_COMPARABLE_LIMIT);
		query.setParameter("fieldNames",BondStaticParams.CDS_COMPARABLE_FIELFNAMES);
		query.setParameter("includeIdentifierString",includeIdentifierString);
		query.setParameter("excludeIdentifierString",excludeIdentifierString);

		List<CDSComparable> data =(List<CDSComparable>) query.list();

		List<CDSComparableDTO> cdsComparableDTOs = DozerHelper.map(dozerBeanMapper, data, CDSComparableDTO.class);

		return cdsComparableDTOs;


	}


}
