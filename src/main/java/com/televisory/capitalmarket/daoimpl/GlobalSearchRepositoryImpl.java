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

import com.televisory.capitalmarket.dao.GlobalSearchRepository;
import com.televisory.capitalmarket.dto.GlobalSearchIndustryDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.entities.cm.CMCompany;
import com.televisory.capitalmarket.entities.cm.GlobalSearchCompany;
import com.televisory.capitalmarket.entities.cm.GlobalSearchIndustry;
import com.televisory.capitalmarket.entities.economy.CommodityLatestData;
import com.televisory.capitalmarket.entities.economy.CountryList;
import com.televisory.capitalmarket.entities.economy.GlobalSearchIndicationData;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class GlobalSearchRepositoryImpl implements GlobalSearchRepository{
	Logger _log = Logger.getLogger(GlobalSearchRepositoryImpl.class);

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="teSessionFactory")
	private SessionFactory teSessionFactory;
		
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Override
	public List<GlobalSearchCompany> getCMExchangeCompanies(String searchCriteria,Integer resultCount , Integer countryId) {
		_log.info("inside function time "+new Date());
		String baseQuery;
		Query query ;
		Integer noOfResult=1000;
		
		
		_log.info("extracting complete Capital Market companies List for search criteria ::::: "+searchCriteria +" countryId"+countryId);

		/*if(searchCriteria != null) 
			searchCriteria = "%"+searchCriteria.replaceAll("\\,", "")+"%";
		
		_log.info("extracting complete Capital Market companies List for search criteria "+searchCriteria +" countryId"+countryId);
		
		if(searchCriteria == null && countryId==null){
		
			baseQuery= "SELECT cl.* FROM cm.company_list cl order by name asc, domicile_flag desc";
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		
		} else if(searchCriteria!=null && countryId==null){
			
			baseQuery ="SELECT cl.* FROM cm.company_list cl where REPLACE( name, ',', '' ) like :searchCriteria order by name asc, domicile_flag desc";
		
			query =cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("searchCriteria", searchCriteria);
		
		}else if(searchCriteria==null && countryId!=null){
			baseQuery ="SELECT cl.* FROM cm.company_list cl  where "
					+ " domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId) order by name asc, domicile_flag desc"; 
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("countryId",countryId);
		} else{
			baseQuery ="SELECT cl.* FROM cm.company_list cl where REPLACE( name, ',', '' ) like :searchCriteria "
					+ "and domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId) order by name asc, domicile_flag desc"; 
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("searchCriteria", searchCriteria);
			query.setParameter("countryId",countryId);
		}*/
		
		if(searchCriteria != null) {
			String startWithCriteria = searchCriteria+"%";
			searchCriteria = "%"+searchCriteria+"%";
			_log.info("extracting complete Capital Market companies List for search criteria "+searchCriteria +" countryId"+countryId);
			/*baseQuery=" select * from ( SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,1 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,2 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,3 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,4 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,5 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.description "
					+ " like :searchCriteria limit 100  ) as A group by name order by  order_display ASC,name asc, domicile_flag desc ";*/
			
			/*baseQuery=" select * from (select * from ( SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,1 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :statWithSearchCriteria OR cl.proper_name like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,2 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :searchCriteria OR cl.proper_name like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,3 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,4 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,5 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.description "
					+ " like :searchCriteria limit 100  ) as A order by order_display ASC,name asc, domicile_flag desc) x group by name ";*/
			
			baseQuery = " SELECT * FROM (SELECT * FROM ((SELECT id,company_id,factset_entity_id,name,proper_name,description,exchange_code,company_ticker,ticker_exchange,domicile_country_code,domicile_flag,cl.factset_industry, "+
					" ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,1 AS order_display, "+
					" 'public' AS entity_type "+
					" FROM cm.company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE "+
					" cl.name LIKE :statWithSearchCriteria OR cl.proper_name LIKE :statWithSearchCriteria OR cl.company_ticker LIKE :statWithSearchCriteria  LIMIT 100) UNION (SELECT id, entity_id as company_id, entity_id, name,proper_name,description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,domicile_country_code, "+
					" NULL AS domicile_flag,cl.industry_type,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,NULL AS isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,1 AS order_display, "+
					" 'private' AS entity_type "+
					" FROM pc.pc_company_list cl LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code "+
					" WHERE domicile_country_code != 'IND' AND " + 
					" (cl.name LIKE :statWithSearchCriteria OR cl.proper_name LIKE :statWithSearchCriteria) LIMIT 100) "+
					" UNION "+
					" (SELECT cl.id,cl.cin AS company_id,cl.cin AS entity_id,name,name AS proper_name,"+
					" description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,"+
					" domicile_country_code,NULL AS domicile_flag,NULL AS industry_type,NULL AS ff_ind_code,"+
					" cl.tics_industry_code,ti.tics_industry_name,NULL AS security_id,NULL AS security_type,"+
					" NULL AS isin,cl.is_active,NULL AS company_active_flag,cl.created_at,cl.created_by,"+
					" cl.last_modified_at,cl.last_modified_by,1 AS order_display,'private' AS entity_type"+
					" FROM ews.company_basic_info cl JOIN (SELECT DISTINCT cin FROM ews.company_data_af) cf ON cl.cin = cf.cin"+
					" LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code"+
					" WHERE cl.name LIKE :statWithSearchCriteria  LIMIT 100)"+
					" UNION (SELECT id, "+
					" company_id, factset_entity_id,name,proper_name,description,exchange_code,company_ticker,ticker_exchange,domicile_country_code,domicile_flag,cl.factset_industry,ff_ind_code, "+
					" cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,2 AS order_display, "+
					" 'public' AS entity_type "+
					" FROM cm.company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE cl.name LIKE :searchCriteria OR cl.proper_name LIKE :searchCriteria OR cl.company_ticker LIKE :searchCriteria  LIMIT 100) UNION (SELECT id,entity_id as company_id,entity_id,name,proper_name,description, "+
					" NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,domicile_country_code,NULL AS domicile_flag,cl.industry_type,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id, "+
					" security_type,NULL AS isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,2 AS order_display, "+
					" 'private' AS entity_type "+
					" FROM pc.pc_company_list cl LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE domicile_country_code != 'IND' AND (cl.name LIKE :searchCriteria OR cl.proper_name LIKE :searchCriteria)  LIMIT 100) "+
					" UNION "+
					" (SELECT cl.id,cl.cin AS company_id,cl.cin AS entity_id,name,name AS proper_name,"+
					" description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,"+
					" domicile_country_code,NULL AS domicile_flag,NULL AS industry_type,NULL AS ff_ind_code,"+
					" cl.tics_industry_code,ti.tics_industry_name,NULL AS security_id,NULL AS security_type,"+
					" NULL AS isin,cl.is_active,NULL AS company_active_flag,cl.created_at,cl.created_by,"+
					" cl.last_modified_at,cl.last_modified_by,2 AS order_display,'private' AS entity_type"+
					" FROM ews.company_basic_info cl JOIN (SELECT DISTINCT cin FROM ews.company_data_af) cf ON cl.cin = cf.cin"+
					" LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code"+
					" WHERE cl.name LIKE :statWithSearchCriteria  LIMIT 100)"+
					" UNION (SELECT id,company_id,factset_entity_id,name,proper_name,description,exchange_code,company_ticker,ticker_exchange,domicile_country_code, "+
					" domicile_flag,cl.factset_industry,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,3 AS order_display, "+
					" 'public' AS entity_type "+
					" FROM cm.company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE ti.tics_industry_name LIKE :statWithSearchCriteria LIMIT 100) UNION (SELECT id, "+
					" entity_id as company_id,entity_id,name,proper_name,description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,domicile_country_code,NULL AS domicile_flag, "+
					" cl.industry_type,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,NULL AS isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,3 AS order_display, "+
					" 'private' AS entity_type "+
					" FROM pc.pc_company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE domicile_country_code != 'IND' AND ti.tics_industry_name LIKE :statWithSearchCriteria LIMIT 100) "+
					" UNION "+
					" (SELECT cl.id,cl.cin AS company_id,cl.cin AS entity_id,name,name AS proper_name,"+
					" description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,"+
					" domicile_country_code,NULL AS domicile_flag,NULL AS industry_type,NULL AS ff_ind_code,"+
					" cl.tics_industry_code,ti.tics_industry_name,NULL AS security_id,NULL AS security_type,"+
					" NULL AS isin,cl.is_active,NULL AS company_active_flag,cl.created_at,cl.created_by,"+
					" cl.last_modified_at,cl.last_modified_by,3 AS order_display,'private' AS entity_type"+
					" FROM ews.company_basic_info cl JOIN (SELECT DISTINCT cin FROM ews.company_data_af) cf ON cl.cin = cf.cin"+
					" LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code"+
					" WHERE cl.name LIKE :statWithSearchCriteria  LIMIT 100)"+
					" UNION (SELECT id,company_id,factset_entity_id,name,proper_name,description,exchange_code,company_ticker, "+
					" ticker_exchange,domicile_country_code,domicile_flag,cl.factset_industry,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,isin, cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,4 AS order_display, "+
					" 'public' AS entity_type "+
					" FROM cm.company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE ti.tics_industry_name LIKE :searchCriteria OR cl.company_ticker LIKE :searchCriteria  "+
					" LIMIT 100) UNION (SELECT id,entity_id as company_id,entity_id,name,proper_name,description,NULL AS exchange_code,NULL AS company_ticker, NULL AS ticker_exchange,domicile_country_code,NULL AS domicile_flag,cl.industry_type, "+
					" ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,NULL AS isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,4 AS order_display,"+
					"'private' AS entity_type "+
					" FROM pc.pc_company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE domicile_country_code != 'IND' AND ti.tics_industry_name LIKE :searchCriteria  "+
					" LIMIT 100) "+
					" UNION "+
					" (SELECT cl.id,cl.cin AS company_id,cl.cin AS entity_id,name,name AS proper_name,"+
					" description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,"+
					" domicile_country_code,NULL AS domicile_flag,NULL AS industry_type,NULL AS ff_ind_code,"+
					" cl.tics_industry_code,ti.tics_industry_name,NULL AS security_id,NULL AS security_type,"+
					" NULL AS isin,cl.is_active,NULL AS company_active_flag,cl.created_at,cl.created_by,"+
					" cl.last_modified_at,cl.last_modified_by,4 AS order_display,'private' AS entity_type"+
					" FROM ews.company_basic_info cl JOIN (SELECT DISTINCT cin FROM ews.company_data_af) cf ON cl.cin = cf.cin"+
					" LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code"+
					" WHERE cl.name LIKE :statWithSearchCriteria  LIMIT 100)"+
				    " UNION (SELECT id,company_id,factset_entity_id,name,proper_name,description,exchange_code,company_ticker,ticker_exchange,domicile_country_code,domicile_flag,cl.factset_industry,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name, "+
					" security_id,security_type,isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,5 AS order_display, "+
					" 'public' AS entity_type "+
					" FROM cm.company_list cl INNER JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE cl.description LIKE :searchCriteria OR cl.company_ticker LIKE :searchCriteria  LIMIT 100) UNION (SELECT id,entity_id as company_id,entity_id,name,proper_name,description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange, "+
					" domicile_country_code,NULL AS domicile_flag,cl.industry_type,ff_ind_code,cl.tics_industry_code,ti.tics_industry_name,security_id,security_type,NULL AS isin,cl.is_active,company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,cl.last_modified_by,5 AS order_display, "+
					" 'private' AS entity_type "+
					" FROM pc.pc_company_list cl LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code WHERE domicile_country_code != 'IND' AND cl.description LIKE :searchCriteria "+
					" LIMIT 100)"+
					" UNION "+
					" (SELECT cl.id,cl.cin AS company_id,cl.cin AS entity_id,name,name AS proper_name,"+
					" description,NULL AS exchange_code,NULL AS company_ticker,NULL AS ticker_exchange,"+
					" domicile_country_code,NULL AS domicile_flag,NULL AS industry_type,NULL AS ff_ind_code,"+
					" cl.tics_industry_code,ti.tics_industry_name,NULL AS security_id,NULL AS security_type,"+
					" NULL AS isin,cl.is_active,NULL AS company_active_flag,cl.created_at,cl.created_by,"+
					" cl.last_modified_at,cl.last_modified_by,5 AS order_display,'private' AS entity_type"+
					" FROM ews.company_basic_info cl JOIN (SELECT DISTINCT cin FROM ews.company_data_af) cf ON cl.cin = cf.cin"+
					" LEFT JOIN cm.tics_industry ti ON ti.tics_industry_code = cl.tics_industry_code"+
					" WHERE cl.description LIKE :searchCriteria  LIMIT 100)"+
					 " ) AS A ORDER BY order_display ASC , name ASC , domicile_flag DESC) x GROUP BY name order by order_display";
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(GlobalSearchCompany.class);
			query.setParameter("searchCriteria", searchCriteria);
			query.setParameter("statWithSearchCriteria", startWithCriteria);
		}else{
		
			baseQuery= "SELECT cl.* FROM cm.company_list cl order by name asc, domicile_flag desc";
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		
		} 
		
		_log.info("QUERY COMPANY ::: " + query);
		
		if(resultCount!=null){
			noOfResult= resultCount;
		}
			
		query.setFirstResult(0);
		query.setMaxResults(noOfResult);
		
		_log.info("query execution start time "+new Date());
		@SuppressWarnings("unchecked")
		List<GlobalSearchCompany> cmCompanyDTOs = (List<GlobalSearchCompany>) query.list();
		/*_log.info("query execution end time "+new Date());
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);
		_log.info("query dto mapping end time "+new Date());*/
		
		return cmCompanyDTOs;
	}
	
	@Override
	public List<GlobalSearchCompany> getCMExchangeCompaniesWithSubscribedCountries(String searchCriteria,Integer resultCount , Integer countryId,List<String> userCountryList) {

		
		String baseQuery;
		Query query ;
		Integer noOfResult=1000;
		_log.info("extracting complete Capital Market companies List for search criteria "+searchCriteria +" userCountryList "+userCountryList);
		if(searchCriteria != null) {
			String startWithCriteria = searchCriteria+"%";
			searchCriteria = "%"+searchCriteria+"%";
			_log.info("extracting complete Capital Market companies List for search criteria "+searchCriteria +" countryId"+countryId);
			//baseQuery="SELECT * FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name like :searchCriteria or cl.description like :searchCriteria or ti.tics_industry_name like :searchCriteria order by cl.name asc, cl.domicile_flag desc";
			/*baseQuery=" select * from ( SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,1 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,2 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,3 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,4 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,5 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.description "
					+ " like :searchCriteria limit 100  ) as A where domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) group by name order by  order_display ASC,name asc, domicile_flag desc ";*/
			
			baseQuery=" select * from (select * from ( SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,1 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,2 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,3 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :statWithSearchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,4 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where ti.tics_industry_name "
					+ " like :searchCriteria limit 100 union SELECT id, company_id, factset_entity_id, name, proper_name, description, exchange_code, company_ticker, ticker_exchange, domicile_country_code, domicile_flag, cl.factset_industry, ff_ind_code, cl.tics_industry_code, ti.tics_industry_name, security_id, security_type, isin, cl.is_active, company_active_flag, cl.created_at, cl.created_by, cl.last_modified_at, cl.last_modified_by,5 as order_display FROM cm.company_list cl inner join cm.tics_industry ti on ti.tics_industry_code = cl.tics_industry_code where cl.description "
					+ " like :searchCriteria limit 100  ) as A where domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) order by order_display ASC,name asc, domicile_flag desc) x group by name order by order_display ";
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(GlobalSearchCompany.class);
			query.setParameter("searchCriteria", searchCriteria);
			query.setParameter("statWithSearchCriteria", startWithCriteria);
			query.setParameterList("userCountryList", userCountryList);
		}else{
			baseQuery= "SELECT cl.* FROM cm.company_list cl order by name asc, domicile_flag desc";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(GlobalSearchCompany.class);
		} 
		
		if(resultCount!=null){
			noOfResult= resultCount;
		}
		query.setFirstResult(0);
		query.setMaxResults(noOfResult);
		
		@SuppressWarnings("unchecked")
		List<GlobalSearchCompany> data = (List<GlobalSearchCompany>) query.list();
		//List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);	
		
		return data;
	
	}
	
	@Override
	public List<GlobalSearchIndustryDTO> getTicsIndustryBySearchParam(String searchParam,Integer resultCount) {
		
		Query query = null;
		
		
		/*String baseQuery=" Select *,@rn \\:=@rn+1 AS gs_industry_id from ( "+
				" SELECT industry_id, tics_industry_code, tics_industry_name, tics_industry_desc, tics_sector_code, factset_industry, null as domicile_country_code, null as country_name, null as country_id,null as sfield,1 as orderInd FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL) and tics_industry_name like  :searchParamStart union "+
				" SELECT industry_id, tics_industry_code, tics_industry_name, tics_industry_desc, tics_sector_code, factset_industry, null as domicile_country_code, null as country_name, null as country_id,null as sfield,2 as orderInd FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL) and tics_industry_name like  :searchParam union "+
				" Select *,3 as orderInd from (SELECT ti.industry_id,ti.tics_industry_code,ti.tics_industry_name,ti.tics_industry_desc,ti.tics_sector_code,ti.factset_industry,data.domicile_country_code,cl.country_name, cl.country_id, concat(cl.country_name, ti.tics_industry_name) as sfield  FROM `industry_data_af` data inner join tics_industry ti on ti.tics_industry_code=data.tics_industry_code inner join country_list cl on cl.country_iso_code_3=data.domicile_country_code "+
				" where applicable_date >= DATE_SUB(curdate(), INTERVAL 5 YEAR) and company_id is null and `domicile_country_code` is not null and data.tics_industry_Code is not null and period_type=:periodType and ( ";*/

		String baseQuery=" Select *,@rn \\:=@rn+1 AS gs_industry_id from ( "+
				" SELECT industry_id, tics_industry_code, tics_industry_name, tics_industry_desc, tics_sector_code, factset_industry, null as domicile_country_code, null as country_name, null as country_id,null as sfield,1 as orderInd FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL) and tics_industry_name like  :searchParamStart union "+
				" SELECT industry_id, tics_industry_code, tics_industry_name, tics_industry_desc, tics_sector_code, factset_industry, null as domicile_country_code, null as country_name, null as country_id,null as sfield,2 as orderInd FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL) and tics_industry_name like  :searchParam union "+
				" Select *,3 as orderInd from (SELECT industry_id,tics_industry_code, tics_industry_name, tics_industry_desc,tics_sector_code,factset_industry,domicile_country_code,country_name, country_id, concat(country_name, tics_industry_name) as sfield  FROM global_search g "+
				" where period_type=:periodType and ( ";
		
		String[] splitedParam = searchParam.split("\\s");
		
		int i=0;
		StringBuilder dynamicParamCountry  = new StringBuilder();
		StringBuilder dynamicParamIndustry  = new StringBuilder();
		StringBuilder dynamicParam  = new StringBuilder();
		
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParamCountry.append(" country_name like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParamCountry.append(" country_name like '%"+splitedParam[i]+"%' or ");
			}
		}
		_log.info(dynamicParamCountry.toString());
		baseQuery = baseQuery+dynamicParamCountry.toString()+" or ";
		
		i=0;
		
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParamIndustry.append(" tics_industry_name like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParamIndustry.append(" tics_industry_name like '%"+splitedParam[i]+"%' or ");
			}
		}
		
		baseQuery = baseQuery+dynamicParamIndustry.toString()+" ) group by tics_industry_code, domicile_country_code)x where ";
		
		i=0;
		
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' and ");
			}
		}
		_log.info(dynamicParam.toString());
		
		//`sfield` like '%United%' and `sfield` like '%KING%' and `sfield` like '%GDP%' and `sfield` like '%growth%'    
		baseQuery = baseQuery+dynamicParam.toString();
				
		baseQuery = baseQuery + " )A join (select @rn \\:=0)b group by tics_industry_name,domicile_country_code ORDER BY orderInd asc,tics_industry_name ASC,country_name asc";
		
		query = cmSessionFactory.getCurrentSession()
				.createSQLQuery(baseQuery)
				.addEntity(GlobalSearchIndustry.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		query.setParameter("searchParam", "%"+searchParam+"%");
		query.setParameter("searchParamStart", searchParam+"%");
		
		if(resultCount!=null){
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}
		
		List<GlobalSearchIndustry> data = (List<GlobalSearchIndustry>)query.list();
		
		List<GlobalSearchIndustryDTO> industryData = DozerHelper.map(dozerBeanMapper, data, GlobalSearchIndustryDTO.class);
		return industryData;
		
	}
	
	@Override
	public List<IndicatorLatestDataDTO> findAllIndicators(String searchParam,Integer resultCount) {
		
		String baseQuery="";
		
		/*baseQuery = "SELECT i.*,cl.country_iso_code_3 country_iso_code_3,@rn \\:= @rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS "
				+ "tel_category_parent_id, NULL as yoy_change_flag FROM trading_economic.`indicator_data_latest` i inner join country_list cl on cl.country_name=i.country, (select @rn\\:=0) j WHERE i.is_active = 1 and category like :searchParam  OR country like :searchParam group by category,frequency,country order by country asc";*/
		
		
		baseQuery= " Select *,@rn \\:=@rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS tel_category_parent_id, NULL as yoy_change_flag from ( "+
				" SELECT i.*,1 as ordr, cl.country_iso_code_3 country_iso_code_3 FROM trading_economic.`indicator_data_latest` i inner join country_list cl on cl.country_name=i.country WHERE i.is_active = 1 and category like :searchParamStart  OR country like :searchParamStart  union "+
				" SELECT i.*,2 as ordr,cl.country_iso_code_3 country_iso_code_3 FROM trading_economic.`indicator_data_latest` i inner join country_list cl on cl.country_name=i.country WHERE i.is_active = 1 and category like :searchParam  OR country like :searchParam union "+
				" SELECT i.*,3 as ordr, cl.country_iso_code_3 country_iso_code_3  FROM indicator_data_latest i inner join country_list cl on cl.country_name=i.country WHERE `indicator_data_id` in ( "+
				" select indicator_data_id from (SELECT indicator_data_id, concat(country, category) as sfield FROM `indicator_data_latest`)x "+
				" where (";

		String[] splitedParam = searchParam.split("\\s");
		
		int i=0;
		StringBuilder dynamicParam  = new StringBuilder(); 
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' and ");
			}
		}
		_log.info(dynamicParam.toString());
		
		//`sfield` like '%United%' and `sfield` like '%KING%' and `sfield` like '%GDP%' and `sfield` like '%growth%'    
		baseQuery = baseQuery+dynamicParam.toString();
		
		baseQuery=baseQuery+" )) )A join (select @rn \\:=0)b group by category,frequency,country order by ordr asc,country asc";
		
		_log.info(baseQuery);
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(GlobalSearchIndicationData.class);
		query.setParameter("searchParam", "%"+searchParam+"%");
		query.setParameter("searchParamStart", searchParam+"%");
		
		if(resultCount!=null){
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}
		
		List<GlobalSearchIndicationData> indicatorNames = (List<GlobalSearchIndicationData>) query.list();
		//_log.info(indicatorNames);
		List<IndicatorLatestDataDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorLatestDataDTO.class);
	
		return indicatorNameDTO;
	}
	
	@Override
	public List<CommodityLatestDataDTO> getCommodities(String searchParam,Integer resultCount) {
		_log.info("extracting list of commadies "+searchParam);
		
		//For now there is no difference bw latest data and commodity list query
		/*String baseQuery = "SELECT tcl.tel_commodity_id,tcl.definition, tcl.name, tcl.tel_market_group, tcl.display_order, mdl.* " + 
				" FROM televisory_commodity_list tcl" + 
				" left join  market_data_latest mdl on tcl.name = mdl.name" + 
				" WHERE mdl.type = 'commodities' AND tcl.name like :searchParam group by tcl.name";*/
		
		String baseQuery=" select * from ( "+
				" SELECT tcl.tel_commodity_id,tcl.definition,  tcl.tel_market_group, tcl.display_order, mdl.*,1 as orderC, NULL as forecast1, NULL as forecast2, NULL as forecast3, NULL as forecast4, NULL as PerChangeQ1, NULL as PerChangeQ2, NULL as PerChangeQ3, NULL as PerChangeQ4  FROM televisory_commodity_list tcl  left join  market_data_latest mdl on tcl.symbol = mdl.symbol WHERE mdl.type = 'commodities' AND tcl.name like :searchParamStart union "+
				" SELECT tcl.tel_commodity_id,tcl.definition,  tcl.tel_market_group, tcl.display_order, mdl.*,2 as orderC, NULL as forecast1, NULL as forecast2, NULL as forecast3, NULL as forecast4, NULL as PerChangeQ1, NULL as PerChangeQ2, NULL as PerChangeQ3, NULL as PerChangeQ4  FROM televisory_commodity_list tcl  left join  market_data_latest mdl on tcl.symbol = mdl.symbol WHERE mdl.type = 'commodities' AND tcl.name like :searchParam ) "+
				" A group by A.name order by orderC asc, name asc";
		
		_log.info(" baseQuery "+baseQuery);
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityLatestData.class);
		query.setParameter("searchParam", "%"+searchParam+"%");
		query.setParameter("searchParamStart", searchParam+"%");
		
		if(resultCount!=null){
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}
		
		
		List<CommodityLatestData> latestCommodityData =query.list();
		
		List<CommodityLatestDataDTO> latestCommodityDataDTO = DozerHelper.map(dozerBeanMapper, latestCommodityData, CommodityLatestDataDTO.class);
	
		return latestCommodityDataDTO;
	}
	
	@Override
	public List<CountryListDTO> findCountriesBySearchCriteria(String searchCriteria,Integer resultCount) {
		/*String baseQuery = "SELECT cl.* FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` "
				+ "where  cl.country_name like '%"+searchCriteria+"%'  GROUP BY cl.`country_name`";*/
		
		String baseQuery = "Select * from (SELECT cl.*,1 as orderShow FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` where  cl.country_name like '"+searchCriteria+"%'  GROUP BY cl.`country_name`  " +
				"union SELECT cl.*,2 as orderShow FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` where  cl.country_name like '%"+searchCriteria+"%' )A  GROUP BY `country_name` order by orderShow asc, country_name asc ";
	
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
	public List<CountryListDTO> findCountriesBySearchCriteriaAndSubscription(String searchCriteria, Integer resultCount,List<String> userCountryList) {
		
		String baseQuery = "Select * from (SELECT cl.*,1 as orderShow FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` where  cl.country_name like '"+searchCriteria+"%'  GROUP BY cl.`country_name`  " +
				"union SELECT cl.*,2 as orderShow FROM country_list cl JOIN `indicator_data_latest` il ON cl.`country_name` = il.`country` where  cl.country_name like '%"+searchCriteria+"%' )A where A.country_iso_code_3 IN(:userCountryList) GROUP BY `country_name` order by orderShow asc, country_name asc ";
	
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameterList("userCountryList", userCountryList);
		
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
	public List<GlobalSearchIndustryDTO> getTicsIndustryBySearchParamAndSubscription(String searchParam, Integer resultCount, List<String> userCountryList) {
		
		Query query = null;
		
		String baseQuery=" Select *,@rn \\:=@rn+1 AS gs_industry_id from ( "+
				" SELECT industry_id, tics_industry_code, tics_industry_name, tics_industry_desc, tics_sector_code, factset_industry, null as domicile_country_code, null as country_name, null as country_id,null as sfield,1 as orderInd FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL) and tics_industry_name like  :searchParamStart union "+
				" SELECT industry_id, tics_industry_code, tics_industry_name, tics_industry_desc, tics_sector_code, factset_industry, null as domicile_country_code, null as country_name, null as country_id,null as sfield,2 as orderInd FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL) and tics_industry_name like  :searchParam union "+
				" Select *,3 as orderInd from (SELECT industry_id,tics_industry_code, tics_industry_name, tics_industry_desc,tics_sector_code,factset_industry,domicile_country_code,country_name, country_id, concat(country_name, tics_industry_name) as sfield  FROM global_search g "+
				" where period_type=:periodType and ( ";
				
		String[] splitedParam = searchParam.split("\\s");
		
		int i=0;
		StringBuilder dynamicParamCountry  = new StringBuilder();
		StringBuilder dynamicParamIndustry  = new StringBuilder();
		StringBuilder dynamicParam  = new StringBuilder();
		
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParamCountry.append(" country_name like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParamCountry.append(" country_name like '%"+splitedParam[i]+"%' or ");
			}
		}
		_log.info(dynamicParamCountry.toString());
		baseQuery = baseQuery+dynamicParamCountry.toString()+" or ";
		
		i=0;
		
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParamIndustry.append(" tics_industry_name like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParamIndustry.append(" tics_industry_name like '%"+splitedParam[i]+"%' or ");
			}
		}
		
		baseQuery = baseQuery+dynamicParamIndustry.toString()+" ) group by tics_industry_code, domicile_country_code)x where ";
		
		i=0;
		
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' and ");
			}
		}
		_log.info(dynamicParam.toString());
		
		//`sfield` like '%United%' and `sfield` like '%KING%' and `sfield` like '%GDP%' and `sfield` like '%growth%'    
		baseQuery = baseQuery+dynamicParam.toString();
				
		baseQuery = baseQuery + " )A join (select @rn \\:=0)b where domicile_country_code IN(:userCountryList) group by tics_industry_name,domicile_country_code ORDER BY orderInd asc,tics_industry_name ASC,country_name asc";
		
		query = cmSessionFactory.getCurrentSession()
				.createSQLQuery(baseQuery)
				.addEntity(GlobalSearchIndustry.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		query.setParameter("searchParam", "%"+searchParam+"%");
		query.setParameter("searchParamStart", searchParam+"%");
		query.setParameterList("userCountryList", userCountryList);
		
		if(resultCount!=null){
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}
		
		List<GlobalSearchIndustry> data = (List<GlobalSearchIndustry>)query.list();
		
		List<GlobalSearchIndustryDTO> industryData = DozerHelper.map(dozerBeanMapper, data, GlobalSearchIndustryDTO.class);
		return industryData;
		
	}

	@Override
	public List<IndicatorLatestDataDTO> findAllIndicatorsBySubscription(String searchParam, Integer resultCount, List<String> userCountryList) {
		
		String baseQuery="";
		
		/*baseQuery = "SELECT i.*,cl.country_iso_code_3 country_iso_code_3,@rn \\:= @rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS "
				+ "tel_category_parent_id, NULL as yoy_change_flag FROM trading_economic.`indicator_data_latest` i inner join country_list cl on cl.country_name=i.country, (select @rn\\:=0) j WHERE i.is_active = 1 and category like :searchParam  OR country like :searchParam group by category,frequency,country order by country asc";*/
		
		
		baseQuery= " Select *,@rn \\:=@rn+1 AS tel_indicator_id,NULL as show_default,NULL as display_order,NULL AS tel_category, NULL AS tel_category_group,NULL AS tel_category_parent_id, NULL as yoy_change_flag from ( "+
				" SELECT i.*,1 as ordr, cl.country_iso_code_3 country_iso_code_3 FROM trading_economic.`indicator_data_latest` i inner join country_list cl on cl.country_name=i.country WHERE i.is_active = 1 and cl.country_iso_code_3 IN(:userCountryList) and (category like :searchParamStart  OR country like :searchParamStart)  union "+
				" SELECT i.*,2 as ordr,cl.country_iso_code_3 country_iso_code_3 FROM trading_economic.`indicator_data_latest` i inner join country_list cl on cl.country_name=i.country WHERE i.is_active = 1 and cl.country_iso_code_3 IN(:userCountryList) and (category like :searchParam  OR country like :searchParam) union "+
				" SELECT i.*,3 as ordr, cl.country_iso_code_3 country_iso_code_3  FROM indicator_data_latest i inner join country_list cl on cl.country_name=i.country WHERE `indicator_data_id` in ( "+
				" select indicator_data_id from (SELECT indicator_data_id, concat(country, category) as sfield FROM `indicator_data_latest`)x "+
				" where (";

		String[] splitedParam = searchParam.split("\\s");
		
		int i=0;
		StringBuilder dynamicParam  = new StringBuilder(); 
		for(;i<splitedParam.length;i++){
			if(i==splitedParam.length-1){
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' ");
			}else{
				dynamicParam.append(" sfield like '%"+splitedParam[i]+"%' and ");
			}
		}
		_log.info(dynamicParam.toString());
		
		//`sfield` like '%United%' and `sfield` like '%KING%' and `sfield` like '%GDP%' and `sfield` like '%growth%'    
		baseQuery = baseQuery+dynamicParam.toString();
		
		baseQuery=baseQuery+" )) and cl.country_iso_code_3 IN(:userCountryList) )A join (select @rn \\:=0)b group by category,frequency,country order by ordr asc,country asc";
		
		_log.info(baseQuery);
		
		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(GlobalSearchIndicationData.class);
		query.setParameter("searchParam", "%"+searchParam+"%");
		query.setParameter("searchParamStart", searchParam+"%");
		query.setParameterList("userCountryList", userCountryList);
		
		if(resultCount!=null){
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}
		
		List<GlobalSearchIndicationData> indicatorNames = (List<GlobalSearchIndicationData>) query.list();
		//_log.info(indicatorNames);
		List<IndicatorLatestDataDTO> indicatorNameDTO = DozerHelper.map(dozerBeanMapper, indicatorNames, IndicatorLatestDataDTO.class);
	
		return indicatorNameDTO;
	}
}
