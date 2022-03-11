package com.privatecompany.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.privatecompany.dao.MNAdaoA;
import com.televisory.capitalmarket.daoimpl.CMRepositoryImpl;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.entities.cm.CMCompany;
import com.televisory.capitalmarket.entities.industry.CountryList;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class MNAdaoImplA implements MNAdaoA{
	
	Logger _log = Logger.getLogger(CMRepositoryImpl.class);

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<CompanyDTO> getMnaCompanies(String searchCriteria) {
		String baseQuery="";
		Query query ;
		if(searchCriteria ==null || searchCriteria.equalsIgnoreCase("")) {
			baseQuery="SELECT * "+
					" FROM "+
					" (select * "+
				    " from(SELECT c.company_id as id , "+
				    " c.security_id as securityId, "+
				    " mc.factset_entity_id as factSetEntityId, "+
				    " c.proper_name as properName," +
				    " c.name as name ,"+
				    " c.domicile_country_code as countryCode , "+
				    " c.domicile_flag as domicileFlag, "+
				    " c.is_active as is_Active " +
				    //" c.company_active_flag,"+
				   // " c.ticker_exchange "+
				    " FROM cm.company_list c "+
				    "JOIN " +
				    " factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id "+
				    " union " +
				    " SELECT c.company_id as id, "+
				    " c.security_id as securityId ,"+
				    " mc.factset_entity_id as factSetEntityId, "+
				    " c.proper_name as properName, "+
				    " c.name as name ,"+
				    " c.domicile_country_code as countryCode, "+
				    " c.domicile_flag as domicileFlag, "+
				    " c.is_active as is_Active "+
				    "FROM cm.company_list c "+
				    "JOIN factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id )y "+
				    " ORDER BY domicileFlag DESC) x GROUP BY factSetEntityId limit 1000";

				               
		}
		else {
			baseQuery="SELECT * "+
					" FROM "+
					" (select * "+
				    " from(SELECT c.company_id as id , "+
				    " c.security_id as securityId, "+
				    " mc.factset_entity_id as factSetEntityId, "+
				    " c.proper_name as properName," +
				    " c.name as name ,"+
				    " c.domicile_country_code as countryCode , "+
				    " c.domicile_flag as domicileFlag, "+
				    " c.is_active as is_Active " +
				    //" c.company_active_flag,"+
				   // " c.ticker_exchange "+
				    " FROM cm.company_list c "+
				    "JOIN " +
				    " factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id "+
				    " WHERE "+
			        " c.name LIKE:name or c.proper_name like :proper_name or c.ticker_exchange=:ticker_exchange "+
				    " union " +
				    " SELECT c.company_id as id, "+
				    " c.security_id as securityId ,"+
				    " mc.factset_entity_id as factSetEntityId, "+
				    " c.proper_name as properName, "+
				    " c.name as name ,"+
				    " c.domicile_country_code as countryCode, "+
				    " c.domicile_flag as domicileFlag, "+
				    " c.is_active as is_Active "+
				    "FROM cm.company_list c "+
				    "JOIN factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id WHERE " + 
				    "c.name LIKE :name1 or c.proper_name like :proper_name1 or c.ticker_exchange like :ticker_exchange1)y "+
				    "ORDER BY domicileFlag DESC) x GROUP BY factSetEntityId limit 1000 ";
		}
		
		
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		if(searchCriteria!=null && !searchCriteria.equalsIgnoreCase("")) {
			query.setParameter("name", searchCriteria +"%");
			query.setParameter("proper_name", searchCriteria +"%");
			query.setParameter("ticker_exchange", searchCriteria);
			query.setParameter("name1", "%"+searchCriteria +"%");
			query.setParameter("proper_name1", "%"+searchCriteria +"%");
			query.setParameter("ticker_exchange1", "%"+searchCriteria +"%");
		}
		query.setResultTransformer(Transformers.aliasToBean(CompanyDTO.class));
		return query.list();
		/*query.setResultTransformer(Transformers.aliasToBean(CompanyDTO.class));
       if(searchCriteria!=null && !searchCriteria.equalsIgnoreCase("")) {
	query.setParameter("name", "%"+searchCriteria + "%");
		}
		@SuppressWarnings("unchecked")
		List<CompanyDTO> cmCompanyDTOs =query.list();
		return cmCompanyDTOs;	*/
		
	}

	@Override
	public List<CountryListDTO> getMnaCountries() {
		String baseQuery="SELECT DISTINCT " + 
				"cl.country_name , cl.country_iso_code_3 ,cl.country_id , " + 
				"cl.country_iso_code_2 ,cl.is_active ,cl.created_at,cl.created_by ,cl.last_modified_at, cl.last_modified_by,cl.country_id ,cl.region_id  " +
				" FROM " + 
				"cm.company_list c " + 
				"JOIN " + 
				"factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id " + 
				" JOIN " + 
				" cm.country_list cl ON cl.country_iso_code_3 = c.domicile_country_code " + 
				" ORDER BY cl.country_name ";
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		
		List<CountryList>country=query.list();
		List<CountryListDTO>countryListDTOs=DozerHelper.map(dozerBeanMapper, country, CountryListDTO.class);
		return countryListDTOs;
	}

	@Override
	public List<IndustryFinancialDataDTO> getMnaIndustries(String countryCode) {
		String baseQuery="";
		Query query ;
		if(countryCode!=null && !countryCode.equalsIgnoreCase("")) {
			baseQuery="SELECT DISTINCT "+
					" i.tics_industry_code as ticsIndustryCode, i.tics_industry_name as ticsIndustryName"+
					" FROM "+
				    "cm.company_list c "+
				    " JOIN "+
				    "factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id "+
					 " JOIN "+
					 "cm.tics_industry i ON c.tics_industry_code = i.tics_industry_code " +
					 "WHERE c.domicile_country_code=:domicile_country_code ";
		}
		else {
		baseQuery="SELECT DISTINCT "+
		" i.tics_industry_code as ticsIndustryCode, i.tics_industry_name as ticsIndustryName"+
		" FROM "+
	    "cm.company_list c "+
	    " JOIN "+
	    "factset.ma_v1_ma_coverage mc ON c.factset_entity_id = mc.factset_entity_id "+
		 " JOIN "+
		 "cm.tics_industry i ON c.tics_industry_code = i.tics_industry_code ";
		
		}
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		if(countryCode!=null && !countryCode.equalsIgnoreCase("")) {
			query.setParameter("domicile_country_code", countryCode);
		}
		query.setResultTransformer(Transformers.aliasToBean(IndustryFinancialDataDTO.class));
		return query.list();
		
	}
	



		
}	
