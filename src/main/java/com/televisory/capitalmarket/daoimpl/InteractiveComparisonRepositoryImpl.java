package com.televisory.capitalmarket.daoimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.dao.InteractiveComparisonRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.entities.cm.CMCompany;
import com.televisory.capitalmarket.entities.factset.FFBalanceModel;
import com.televisory.capitalmarket.util.DozerHelper;

/**
 * 
 * @author vinay
 *
 */
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class InteractiveComparisonRepositoryImpl implements InteractiveComparisonRepository{
	
	Logger _log = Logger.getLogger(InteractiveComparisonRepositoryImpl.class);
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	

	@Override
	public List<CompanyDTO> getIComCompanies(String searchCriteria, String countryIdList) {
		
		_log.info("getting the company list for search Criteria : "+searchCriteria);
		
		List<String> countryIdArray = null;
		
		if(countryIdList!=null){
		
			countryIdArray = new ArrayList<>(Arrays.asList(countryIdList.split(",")));
		
		}
		
		_log.info("countryIdArray :::: " + countryIdArray);
		
		String baseQuery="";
		
		/*if(countryIdList!=null){
			baseQuery="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where cl.domicile_country_code IN (:countryIdArray) AND "
					+ "cl.is_active=1 and REPLACE( name, ',', '' ) like :searchCriteria order by name";
		}else{
			baseQuery="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where REPLACE( name, ',', '' ) like :searchCriteria order by "
					+ "name asc, domicile_flag desc";
		}*/
		
		if(countryIdList!=null){
			baseQuery = "SELECT * from ("
					+ "(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
					+ "where c.company_ticker like :startsWithsearchCriteria and c.domicile_country_code IN(:countryIdArray) and c.is_active=1 order by c.domicile_flag desc, c.company_active_flag desc)a group by name order by c.name) union "
					+ "(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
					+ "where c.name like :startsWithsearchCriteria and c.domicile_country_code IN(:countryIdArray) and c.is_active=1 order by c.domicile_flag desc, c.company_active_flag desc)b group by name order by name) union "
					+ "(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
					+ "where ( c.name like :searchCriteria or c.proper_name like :searchCriteria or c.company_ticker like :searchCriteria) and c.domicile_country_code IN(:countryIdArray) and c.is_active=1 order by c.domicile_flag desc, c.company_active_flag desc)e group by name order by name)"
					+ ")x";
		}else{
			baseQuery = "SELECT * from ("
					+ "(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
					+ "where c.company_ticker like :startsWithsearchCriteria order by c.domicile_flag desc, c.company_active_flag desc)a group by name order by name) union "
					+ "(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
					+ "where c.name like :startsWithsearchCriteria order by c.domicile_flag desc, c.company_active_flag desc)b group by name order by name) union "
					+ "(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
					+ "where ( c.name like :searchCriteria or c.proper_name like :searchCriteria or c.company_ticker like :searchCriteria) order by c.domicile_flag desc, c.company_active_flag desc)e group by name order by name)"
					+ ")x";
		}
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		
		if(countryIdList!=null){
			query.setParameterList("countryIdArray", countryIdArray);
		}
		//query.setParameter("searchCriteria", "%"+searchCriteria.replaceAll("\\,", "")+"%");
		query.setParameter("searchCriteria", "%"+searchCriteria+"%");
		query.setParameter("startsWithsearchCriteria", searchCriteria+"%");
		
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);	
		return cmCompanyDTOs;
	}

	@Override
	public List<CompanyDTO> getIComCompanies(String searchCriteria, String countryCode, String ffIndustry) {
	
		_log.info("getting the company list for country code: "+ countryCode +", industry: "+ ffIndustry +" search Criteria : "+searchCriteria);
		
		String baseQuery="select *,'public' as entity_type from (SELECT * FROM company_list "
				+ "WHERE domicile_country_code = :countryCode and factset_industry=:ffIndustry and name like :searchCriteria order by name) a "
				+ "union "
				+ "select *,'public' as entity_type from (SELECT * FROM company_list "
				+ "WHERE domicile_country_code != :countryCode and factset_industry=:ffIndustry and name like :searchCriteria order by name) b ";
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		query.setParameter("searchCriteria", "%"+searchCriteria+"%");
		query.setParameter("countryCode", countryCode);
		query.setParameter("ffIndustry", ffIndustry);
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);	
		return cmCompanyDTOs;
	}

	@Override
	public List<CompanyDTO> getIComCompanies(String countryIdList) {
		_log.info("getting the company list :: " + countryIdList);
		
		List<String> countryIdArray = null;
		if(countryIdList!=null){
			countryIdArray = Arrays.asList(countryIdList.split("\\s*,\\s*"));
		}
		
		_log.info("countryIdArray :::: " + countryIdArray);
		
		String baseQuery="";
		if(countryIdList!=null){
			baseQuery="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where cl.domicile_country_code IN (:countryIdArray) AND "
					+ "cl.is_active=1 order by name";
		}else{
			baseQuery="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where "
					+ "cl.is_active=1 order by name";
		}
		
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		if(countryIdList!=null){
			query.setParameterList("countryIdArray", countryIdArray);
		}
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);	
		return cmCompanyDTOs;
	}
	
	@Override
	public List<CompanyDTO> getIComCompanies() {
		_log.info("getting the company list");
		
		String baseQuery="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where "
									+ "cl.is_active=1 order by name";
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);	
		return cmCompanyDTOs;
	}

	@Override
	public List<CompanyDTO> getIComCompaniesByCountryIndustry(String countryCode, String ffIndustry) {

		_log.info("getting the company list for country code : "+ countryCode);
		
		String baseQuery="select *,'public' as entity_type from (SELECT * FROM company_list "
				+ "WHERE domicile_country_code = :countryCode and factset_industry=:ffIndustry order by name) a "
				+ "union "
				+ "select *,'public' as entity_type from (SELECT * FROM company_list "
				+ "WHERE domicile_country_code != :countryCode and factset_industry=:ffIndustry order by name) b ";
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		query.setParameter("countryCode", countryCode);
		query.setParameter("ffIndustry", ffIndustry);
		
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);	
		return cmCompanyDTOs;
	}

	@Override
	public List<BalanceModelDTO> getFinancialParameter(String industryType,Boolean watchlistFlag, Boolean icFlag,Boolean screenerFlag) {
	
		_log.info("getting the financial parameters for the industy : "+industryType);
		
		List<String> industryList = Arrays.asList(industryType.split("\\s*,\\s*"));
		
		String baseQuery = "SELECT * FROM cm.balance_model where factset_industry in (:industryList) ";
		
		if(watchlistFlag !=null && watchlistFlag) {
			baseQuery += " and watchlist_flag=1 ";
		}
		if(icFlag !=null && icFlag) {
			baseQuery += " and ic_flag=1 ";
		}
		if(screenerFlag !=null && screenerFlag) {
			baseQuery += " and screener_flag=1 ";
		} 
		if (screenerFlag == null && icFlag == null && watchlistFlag == null) {
			baseQuery += " and is_active = 1 ";
		}
		
		baseQuery += " order by id ";

		
		Query query=  cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFBalanceModel.class);
		
		query.setParameterList("industryList", industryList);
		
		@SuppressWarnings("unchecked")
		List<FFBalanceModel> data = (List<FFBalanceModel>) query.list();
		
		List<BalanceModelDTO> balanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);	
		
		return balanceModelDTOs;
		
	}
	
	@Override
	public List<BalanceModelDTO> getDistinctFinancialParameter(String industryType,Boolean watchlistFlag, Boolean icFlag,Boolean screenerFlag) {
	
		_log.info("getting the financial parameters for the industy : "+industryType);
		
		List<String> industryList = Arrays.asList(industryType.split("\\s*,\\s*"));
		
		String baseQuery = "SELECT id,display_order,display_level,display_flag,group_concat(distinct factset_industry)as 'factset_industry',type,key_parameter,key_parameter_order," + 
				" section,field_name,short_name,description,financial_field,is_active,ic_flag,screener_flag,watchlist_flag,currency_flag,unit,fx_aod,created_at,created_by,last_modified_at,last_modified_by from balance_model " +
				" where `factset_industry` in (:industryList) and `is_active`=1 ";
		
		if(watchlistFlag !=null && watchlistFlag) {
			baseQuery += " and watchlist_flag=1 ";
		}
		if(icFlag !=null && icFlag) {
			baseQuery += " and ic_flag=1 ";
		}
		if(screenerFlag !=null && screenerFlag) {
			baseQuery += " and screener_flag=1 ";
		}
		if (screenerFlag == null && icFlag == null && watchlistFlag == null) {
			baseQuery += " and is_active = 1 ";
		}
		
		baseQuery += " group by field_name order by id ";
		
		
		Query query=  cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFBalanceModel.class);
		
		query.setParameterList("industryList", industryList);
		
		@SuppressWarnings("unchecked")
		List<FFBalanceModel> data = (List<FFBalanceModel>) query.list();
		
		List<BalanceModelDTO> balanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);	
		
		return balanceModelDTOs;	
	}
	
	
	@Override
	public List<BalanceModelDTO> getFinancialParams(String industryType, String fieldName) {
	
		_log.info("getting the financial parameters for the industy : "+industryType);
		
		List<String> industryList = Arrays.asList(industryType.split("\\s*,\\s*"));
		
		
		String baseQuery = "SELECT * FROM cm.balance_model where factset_industry in (:industryList) " + 
				" and field_name =:fieldName and is_active = 1 ";
		
		Query query=  cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFBalanceModel.class);
		
		query.setParameterList("industryList", industryList);
		query.setParameter("fieldName", fieldName);
		
		@SuppressWarnings("unchecked")
		List<FFBalanceModel> data = (List<FFBalanceModel>) query.list();
		
		List<BalanceModelDTO> factSetBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);	
		
		return factSetBalanceModelDTOs;
		
	}

	
}
