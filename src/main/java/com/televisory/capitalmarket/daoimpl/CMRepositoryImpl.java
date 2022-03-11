package com.televisory.capitalmarket.daoimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.privatecompany.dto.AdvanceSearchCompanyDTO;
import com.privatecompany.dto.EntityStructureDTO;
import com.privatecompany.entities.AdvancedSearchCompany;
import com.privatecompany.entities.EntityStructure;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CMCDTO;
import com.televisory.capitalmarket.dto.CMExchangeDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.ObjectPropertiesDTO;
import com.televisory.capitalmarket.dto.PCCompanyDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.entities.cm.CMCompany;
import com.televisory.capitalmarket.entities.cm.CMExchangeList;
import com.televisory.capitalmarket.entities.cm.ExchangeIndex;
import com.televisory.capitalmarket.entities.cm.ObjectProperties;
import com.televisory.capitalmarket.entities.cm.PCCompany;
import com.televisory.capitalmarket.entities.economy.CountryList;
import com.televisory.capitalmarket.entities.factset.FFBalanceModel;
import com.televisory.capitalmarket.entities.factset.FFEntityProfile;
import com.televisory.capitalmarket.factset.dto.FFEntityProfileDTO;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

/**
 * 
 * @author vinay
 *
 */
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class CMRepositoryImpl implements CMRepository {

	Logger _log = Logger.getLogger(CMRepositoryImpl.class);

	@Autowired
	@Qualifier(value = "cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Autowired
	@Qualifier(value = "factSetSessionFactory")
	private SessionFactory factSetSessionFactory;

	@Autowired
	@Qualifier(value = "pcDataSessionFactory")
	private SessionFactory pcDataSessionFactory;

	// static Map<Integer,String> industryMap;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public ObjectPropertiesDTO getObjectProperty(String name) {

		_log.info("Extracting Object Properties from the database for " + name);

		String baseQuery = "SELECT * FROM `object_properties` WHERE `object_name` = :name";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ObjectProperties.class);
		query.setParameter("name", name);

		@SuppressWarnings("unchecked")
		List<ObjectPropertiesDTO> data = (List<ObjectPropertiesDTO>) query.list();

		List<ObjectPropertiesDTO> objectPropertiesDTOs = DozerHelper.map(dozerBeanMapper, data,
				ObjectPropertiesDTO.class);
		return objectPropertiesDTOs.get(0);
	}

	@Override
	public List<CompanyDTO> getCMExchangeCompanies(String searchCriteria, Integer resultCount, Integer countryId,
			String entityType) {
		String baseQuery;
		Query query;
		Integer noOfResult = 500;
		if (resultCount != null) {
			noOfResult = resultCount;
		}

		_log.info("extracting companies for search criteria: " + searchCriteria + ", countryId: " + countryId
				+ ", entityType: " + entityType + ", resultCount: " + resultCount);

		String startsWithSearchCriteria = null;
		if (searchCriteria != null) {
			startsWithSearchCriteria = searchCriteria + "%";
			searchCriteria = "%" + searchCriteria + "%";
		}

		_log.info("extracting complete Capital Market companies List for search criteria " + searchCriteria
				+ " countryId" + countryId);

		if (searchCriteria == null && countryId == null) {

			String publicEntityQuery = "(select * from (select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type from cm.company_list c order by ff_iscomp desc,company_active_flag desc, name asc )x group by name )";
			String pvtEntityQuery = "(select entity_id as factset_entity_id, c.proper_name,entity_id as company_id,name,c.reporting_currency,c.industry_type as factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' as entity_type from pc.pc_company_list c where c.domicile_country_code!='IND' order by name limit "
					+ noOfResult + ")";
			String pvtIndEntityQuery="(SELECT c.cin AS factset_entity_id,c.name as proper_name,c.cin AS company_id,"
					+ " name,c.currency as reporting_currency,c.tics_industry_name AS factset_industry,c.tics_industry_code,c.domicile_country_code,null as security_id,c.description AS description,"
					+ " '1' as is_active,NULL AS exchange_code,NULL AS security_type,NULL AS company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' AS entity_type"
					+ " FROM ews.company_basic_info c  join ews.company_data_af x on x.cin = c.cin GROUP BY name  order by name limit "
					+ noOfResult + ")";

			if ("PUB".equalsIgnoreCase(entityType)) {
				baseQuery = "SELECT (@id\\:=@id + 1) AS id, a.* from " + publicEntityQuery + " a  "
						+ "JOIN (SELECT @id\\:=0) AS ai limit " + noOfResult;
			} else if ("PVT".equalsIgnoreCase(entityType)) {
				/*
				 * baseQuery = "SELECT (@id\\:=@id + 1) AS id, a.* from " + pvtEntityQuery +
				 * " a" + " join (SELECT @id \\:= 0) as ai ";
				 */
				StringBuilder sb = new StringBuilder();
				sb.append("select (@id \\:=@id+1) as id, x.* from (");
				sb.append("((select * from (SELECT * FROM " + pvtEntityQuery + " pub LIMIT 1000) y)");
				sb.append(" union ");
				sb.append(pvtIndEntityQuery);
				sb.append(") order by name ");
				sb.append(")x join (SELECT @id \\:= 0) as ai");

				baseQuery = sb.toString();
			}
			else if("PVTIND".equalsIgnoreCase(entityType)) {
				StringBuilder sb = new StringBuilder();
				sb.append("select (@id \\:=@id+1) as id, x.* from (");
				sb.append("((select * from (SELECT * FROM " + publicEntityQuery + " pub LIMIT 1000) y)");
				sb.append(" union ");
				sb.append(pvtIndEntityQuery);
				sb.append(") order by name ");
				sb.append(")x join (SELECT @id \\:= 0) as ai");

				baseQuery = sb.toString();
			}
			else {
				StringBuilder sb = new StringBuilder();
				sb.append("select (@id \\:=@id+1) as id, x.* from (");
				sb.append("((select * from (SELECT * FROM " + publicEntityQuery + " pub LIMIT 1000) y)");
				sb.append(" union ");
				sb.append(pvtEntityQuery);
				sb.append(") order by name ");
				sb.append(")x join (SELECT @id \\:= 0) as ai");

				baseQuery = sb.toString();
			}

			// baseQuery= "SELECT cl.*,'public' as entity_type FROM cm.company_list cl order
			// by name asc, domicile_flag desc limit "+ noOfResult;
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);

		} else if (searchCriteria == null && countryId != null) {

			String publicEntityQuery = "(select * from (select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type from cm.company_list c where domicile_country_code = (select country_iso_code_3 from country_list where country_id=:countryId) order by domicile_flag desc, name asc )x group by name )";
			String pvtEntityQuery = "(select entity_id as factset_entity_id, c.proper_name,entity_id as company_id,name,c.reporting_currency,c.industry_type as factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' as entity_type from pc.pc_company_list c where   c.domicile_country_code!='IND' and domicile_country_code = (select country_iso_code_3 from country_list where country_id=:countryId) order by name limit "
					+ noOfResult + ")";
			String pvtIndEntityQuery="(SELECT c.cin AS factset_entity_id,c.name as proper_name,c.cin AS company_id,"
					+ " name,c.currency as reporting_currency,c.tics_industry_name AS factset_industry,c.tics_industry_code,c.domicile_country_code,null as security_id,c.description AS description,"
					+ " '1' as is_active,NULL AS exchange_code,NULL AS security_type,NULL AS company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' AS entity_type"
					+ " FROM ews.company_basic_info c  join ews.company_data_af x on x.cin = c.cin GROUP BY name  order by name limit "
					+ noOfResult + ")";
			if ("PUB".equalsIgnoreCase(entityType)) {
				baseQuery = "SELECT (@id\\:=@id + 1) AS id, a.* from " + publicEntityQuery + " a  "
						+ "JOIN (SELECT @id\\:=0) AS ai limit " + noOfResult;
			} else if ("PVT".equalsIgnoreCase(entityType)) {
				/*
				 * baseQuery = "SELECT (@id\\:=@id + 1) AS id, a.* from " + pvtEntityQuery +
				 * " a" + " join (SELECT @id \\:= 0) as ai ";
				 */
				StringBuilder sb = new StringBuilder();
				sb.append("select (@id \\:=@id+1) as id, x.* from (");
				sb.append("((select * from (SELECT * FROM " + pvtEntityQuery + " pub LIMIT 1000) y)");
				sb.append(" union ");
				sb.append(pvtIndEntityQuery);
				sb.append(") order by name ");
				sb.append(")x join (SELECT @id \\:= 0) as ai limit " + noOfResult);

				baseQuery = sb.toString();
			} else if("PVTIND".equalsIgnoreCase(entityType)) {
				StringBuilder sb = new StringBuilder();
				sb.append("select (@id \\:=@id+1) as id, x.* from (");
				sb.append("((select * from (SELECT * FROM " + publicEntityQuery + " pub LIMIT 1000) y)");
				sb.append(" union ");
				sb.append(pvtIndEntityQuery);
				sb.append(") order by name ");
				sb.append(")x join (SELECT @id \\:= 0) as ai limit " + noOfResult);

				baseQuery = sb.toString();
			}
			else {
				StringBuilder sb = new StringBuilder();
				sb.append("select (@id \\:=@id+1) as id, x.* from (");
				sb.append("((select * from (SELECT * FROM " + publicEntityQuery + " pub LIMIT 1000) y)");
				sb.append(" union ");
				sb.append(pvtEntityQuery);
				sb.append(") order by name ");
				sb.append(")x join (SELECT @id \\:= 0) as ai limit " + noOfResult);

				baseQuery = sb.toString();
			}

			// baseQuery ="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where
			// domicile_country_code = (select country_iso_code_3 from country_list where
			// country_id=:countryId) order by name asc, domicile_flag desc";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("countryId", countryId);

		} else if (searchCriteria != null && countryId == null) {

			StringBuilder sb = new StringBuilder();

			String publicEntityQueryA = "(select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type "
					+ "from cm.company_list c  " + "where (c.company_ticker like  :startsWithsearchCriteria) "
					+ " order by c.ff_iscomp desc,c.company_active_flag desc limit " + noOfResult + ")a";
			String publicEntityQueryB = "(select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type "
					+ "from cm.company_list c " + "where ( c.name like :startsWithsearchCriteria) "
					+ " order by c.ff_iscomp desc,c.company_active_flag desc limit " + noOfResult + ")a";
			String publicEntityQueryC = "(select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type "
					+ "from cm.company_list c "
					+ "where ( c.name like :searchCriteria or c.proper_name like  :searchCriteria or c.company_ticker like  :searchCriteria) "
					+ " order by c.ff_iscomp desc,c.company_active_flag desc limit " + noOfResult + ")b";
			String pvtEntityQueryA = "(select entity_id as factset_entity_id,c.proper_name,entity_id as company_id,name,c.reporting_currency,c.industry_type as factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' as entity_type "
					+ "from pc.pc_company_list c " + "where c.domicile_country_code!='IND' and (c.name like :startsWithsearchCriteria)  limit "
					+ noOfResult + " )c";
			String pvtEntityQueryB = "(select entity_id as factset_entity_id,c.proper_name,entity_id as company_id,name,c.reporting_currency,c.industry_type as factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' as entity_type "
					+ "from pc.pc_company_list c "
					+ "where c.domicile_country_code!='IND' and (c.name like :searchCriteria or c.proper_name like  :searchCriteria)  limit " + noOfResult
					+ " )d";
			String pvtIndEntityQuery="(SELECT c.cin AS factset_entity_id,c.name as proper_name,c.cin AS company_id,"
					+ " name,c.currency as reporting_currency,c.tics_industry_name AS factset_industry,c.tics_industry_code,c.domicile_country_code,null as security_id,c.description AS description,"
					+ " '1' as is_active,NULL AS exchange_code,NULL AS security_type,NULL AS company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' AS entity_type"
					+ " FROM ews.company_basic_info c  join ews.company_data_af x on x.cin = c.cin "
					+ " where (c.name like :searchCriteria ) GROUP BY name  ORDER BY name  limit " + noOfResult
					+ " )d";
					
			if ("PUB".equalsIgnoreCase(entityType)) {
				sb.append("select (@id \\:=@id+1) as id, x.* from (" + "(select * from " + publicEntityQueryA
						+ " group by name order by c.name) " + "union " + "(select * from " + publicEntityQueryB
						+ " group by name order by c.name) " + "union " + "(select * from " + publicEntityQueryC
						+ " group by name order by c.name) " + ")x join (select @id \\:=0) as ai limit " + noOfResult);
			} else if ("PVT".equalsIgnoreCase(entityType)) {
				/*
				 * sb.append("select (@id \\:=@id+1) as id, x.* from (");
				 * sb.append(" (select * from " + pvtEntityQueryA + " order by name) ");
				 * sb.append(" union "); sb.append(" (select * from " + pvtEntityQueryB +
				 * " order by name) "); sb.append(")x join (SELECT @id \\:= 0) as ai limit " +
				 * noOfResult);
				 */
				sb.append("select (@id \\:=@id+1) as id, x.* from ");
				sb.append("(select * from (");

				sb.append("(select * from ");
				sb.append("((select * from " + pvtEntityQueryA + " group by name order by name) ");
				sb.append(" union ");
				sb.append(" (select * from " + pvtEntityQueryB + " group by name order by name) ");
				sb.append(" union ");
				sb.append("(select * from " + pvtIndEntityQuery + " ) ");
				sb.append(" )x ");
				sb.append(" ) ");
				// Section A End
				sb.append(")uc )x join (SELECT @id \\:= 0) as ai limit " + noOfResult);
			} else if("PVTIND".equalsIgnoreCase(entityType)) {

				sb.append("select (@id \\:=@id+1) as id, x.* from ");
				sb.append("(select * from (");

				sb.append("(select * from ");
				sb.append("((select * from " + publicEntityQueryA + " group by name order by name) ");
				sb.append(" union ");
				sb.append(" (select * from " + publicEntityQueryB + " group by name order by name) ");
				sb.append(" union ");
				sb.append("(select * from " + pvtIndEntityQuery + " ) ");
				sb.append(" )x ");
				sb.append(" ) ");
				// Section A End
				sb.append(")uc )x join (SELECT @id \\:= 0) as ai limit " + noOfResult);
			  }
			 else {

					sb.append("select (@id \\:=@id+1) as id, x.* from ");
					sb.append("(select * from (");

					// Section A Start
					sb.append("(select * from ");
					sb.append("((select * from " + publicEntityQueryA + " group by name order by name) ");
					sb.append(" union ");
					sb.append(" (select * from " + publicEntityQueryB + " group by name order by name) ");
					sb.append(" union ");
					sb.append("(select * from " + pvtEntityQueryA + " ) ");
					sb.append(" )x ");
					sb.append(" ) ");
					// Section A End
					sb.append(" union ");
					// Section B Start
					sb.append("(select * from ");
					sb.append("((select * from " + publicEntityQueryC + " group by name) ");
					sb.append(" union ");
					sb.append("(select * from " + pvtEntityQueryB + " ) ");
					sb.append(" )y ");
					sb.append("order by name)");
					// Section B END

					sb.append(")uc )x join (SELECT @id \\:= 0) as ai limit " + noOfResult);
				}

			baseQuery = sb.toString();
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("searchCriteria", searchCriteria);
			query.setParameter("startsWithsearchCriteria", startsWithSearchCriteria);

		} else {

			StringBuilder sb = new StringBuilder();

			String publicEntityQueryA = "(select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type "
					+ "from cm.company_list c  " + "where (c.company_ticker like  :startsWithsearchCriteria) "
					+ "and c.domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId)  order by c.domicile_flag desc,c.company_active_flag desc limit "
					+ noOfResult + ")a";
			String publicEntityQueryB = "(select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type "
					+ "from cm.company_list c " + "where ( c.name like :startsWithsearchCriteria) "
					+ "and c.domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId) order by c.domicile_flag desc,c.company_active_flag desc limit "
					+ noOfResult + ")a";
			String publicEntityQueryC = "(select c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type "
					+ "from cm.company_list c "
					+ "where ( c.name like :searchCriteria or c.proper_name like  :searchCriteria or c.company_ticker like  :searchCriteria) "
					+ "and c.domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId) order by c.domicile_flag desc,c.company_active_flag desc limit "
					+ noOfResult + ")b";
			String pvtEntityQueryA = "(select entity_id as factset_entity_id,c.proper_name,entity_id as company_id,name,c.reporting_currency,c.industry_type as factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' as entity_type "
					+ "from pc.pc_company_list c " + "where c.domicile_country_code!='IND' and (c.name like :startsWithsearchCriteria)"
					+ "and c.domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId) limit "
					+ noOfResult + " )c";
			String pvtEntityQueryB = "(select entity_id as factset_entity_id,c.proper_name,entity_id as company_id,name,c.reporting_currency,c.industry_type as factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' as entity_type "
					+ "from pc.pc_company_list c "
					+ "where c.domicile_country_code!='IND' and (c.name like :searchCriteria or c.proper_name like  :searchCriteria) "
					+ "and c.domicile_country_code =(select country_iso_code_3 from country_list where country_id=:countryId) limit "
					+ noOfResult + " )d";
			String pvtIndEntityQuery="(SELECT c.cin AS factset_entity_id,c.name as proper_name,c.cin AS company_id,"
					+ " name,c.currency as reporting_currency,c.tics_industry_name AS factset_industry,c.tics_industry_code,c.domicile_country_code,null as security_id,c.description AS description,"
					+ " '1' as is_active,NULL AS exchange_code,NULL AS security_type,NULL AS company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'private' AS entity_type"
					+ " FROM ews.company_basic_info c join ews.company_data_af x on x.cin = c.cin "
					+ " where (c.name like :searchCriteria and  c.domicile_country_code='ind' ) GROUP BY name  ORDER BY name  limit " + noOfResult
					+ " )d";

			if ("PUB".equalsIgnoreCase(entityType)) {
				sb.append("select (@id \\:=@id+1) as id, x.* from (" + "(select * from " + publicEntityQueryA
						+ " group by name order by c.name) " + "union " + "(select * from " + publicEntityQueryB
						+ " group by name order by c.name) " + "union " + "(select * from " + publicEntityQueryC
						+ " group by name order by c.name) " + ")x join (select @id \\:=0) as ai limit " + noOfResult);
			} else if ("PVT".equalsIgnoreCase(entityType)) {
				/*
				 * sb.append("select (@id \\:=@id+1) as id, x.* from (");
				 * sb.append(" (select * from " + pvtEntityQueryA + " order by name) ");
				 * sb.append(" union "); sb.append(" (select * from " + pvtEntityQueryB +
				 * " order by name) "); sb.append(")x join (SELECT @id \\:= 0) as ai limit " +
				 * noOfResult);
				 */
				sb.append("select (@id \\:=@id+1) as id, x.* from ");
				sb.append("(select * from (");

				// Section A Start
				sb.append("(select * from ");
				sb.append("((select * from " + pvtEntityQueryA + " group by name order by name) ");
				sb.append(" union ");
				sb.append(" (select * from " + pvtEntityQueryB + " group by name order by name) ");
				sb.append(" union ");
				sb.append("(select * from " + pvtIndEntityQuery + " ) ");
				sb.append(" )x ");
				sb.append(" ) ");
				// Section A End
			

				sb.append(")uc group by name)x join (SELECT @id \\:= 0) as ai limit " + noOfResult);
			} 
			else if("PVTIND".equalsIgnoreCase(entityType)) {
				sb.append("select (@id \\:=@id+1) as id, x.* from ");
				sb.append("(select * from (");

				// Section A Start
				sb.append("(select * from ");
				sb.append("((select * from " + publicEntityQueryA + " group by name order by name) ");
				sb.append(" union ");
				sb.append(" (select * from " + publicEntityQueryB + " group by name order by name) ");
				sb.append(" union ");
				sb.append("(select * from " + pvtIndEntityQuery + " ) ");
				sb.append(" )x ");
				sb.append(" ) ");
				// Section A End
			

				sb.append(")uc group by name)x join (SELECT @id \\:= 0) as ai limit " + noOfResult);
			}
			else {

				sb.append("select (@id \\:=@id+1) as id, x.* from ");
				sb.append("(select * from (");

				// Section A Start
				sb.append("(select * from ");
				sb.append("((select * from " + publicEntityQueryA + " group by name order by name) ");
				sb.append(" union ");
				sb.append(" (select * from " + publicEntityQueryB + " group by name order by name) ");
				sb.append(" union ");
				sb.append("(select * from " + pvtEntityQueryA + " ) ");
				sb.append(" )x ");
				sb.append(" ) ");
				// Section A End
				sb.append(" union ");
				// Section B Start
				sb.append("(select * from ");
				sb.append("((select * from " + publicEntityQueryC + " group by name) ");
				sb.append(" union ");
				sb.append("(select * from " + pvtEntityQueryB + " ) ");
				sb.append(" )y ");
				sb.append("order by name)");
				// Section B END

				sb.append(")uc group by name)x join (SELECT @id \\:= 0) as ai limit " + noOfResult);
			}

			baseQuery = sb.toString();
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("searchCriteria", searchCriteria);
			query.setParameter("startsWithsearchCriteria", startsWithSearchCriteria);
			if(!"PVTIND".equalsIgnoreCase(entityType))
			query.setParameter("countryId", countryId);
		}

		// query.setFirstResult(0);
		// query.setMaxResults(noOfResult);

		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		//_log.info(data.size());
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs;
	}

	@Override
	public List<CompanyDTO> getCMExchangeCompaniesByUserSubscription(String searchCriteria, Integer resultCount,
			List<String> userCountryList) {

		String baseQuery;
		Query query;
		Integer noOfResult = 1000;

		_log.info("extracting complete Capital Market companies List for search criteria " + searchCriteria
				+ " userCountryList" + userCountryList);

		// if(searchCriteria != null)
		// searchCriteria = "%"+searchCriteria.replaceAll("\\,", "")+"%";

		_log.info("extracting complete Capital Market companies List for search criteria " + searchCriteria
				+ " userCountryList" + userCountryList);

		if (searchCriteria == null && userCountryList == null) {

			baseQuery = "SELECT * from(SELECT cl.*,'public' as entity_type FROM cm.company_list cl order by ff_is_comp desc)x order by name asc";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);

		} else if (searchCriteria != null && userCountryList == null) {
			_log.info("1-1");
			// baseQuery ="SELECT cl.*,'public' as entity_type FROM cm.company_list cl where
			// name like :searchCriteria or proper_name like :searchCriteria or
			// company_ticker like :searchCriteria order by name asc, domicile_flag desc";

			StringBuilder queryStrBuilder = new StringBuilder();
			queryStrBuilder.append("select * from ");
			queryStrBuilder.append(
					"(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
							+ "where c.company_ticker = :startsWithsearchCriteria order by c.ff_is_comp desc, c.company_active_flag desc)a "
							+ "group by name order by name)");
			queryStrBuilder.append(" union ");
			queryStrBuilder.append(
					"(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
							+ "where c.name like :startsWithsearchCriteria order by c.ff_is_comp desc, c.company_active_flag desc)b "
							+ "group by name order by name)");
			queryStrBuilder.append(" union ");
			queryStrBuilder.append(
					"(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
							+ "where ( c.name like :searchCriteria or c.proper_name like :searchCriteria or c.company_ticker like :searchCriteria) order by c.ff_is_comp desc, c.company_active_flag desc)e "
							+ "group by name order by name))");
			baseQuery = queryStrBuilder.toString();

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			// query.setParameter("searchCriteria", searchCriteria);
			query.setParameter("searchCriteria", "%" + searchCriteria + "%");
			query.setParameter("startsWithsearchCriteria", searchCriteria + "%");

		} else if (searchCriteria == null && userCountryList != null) {
			baseQuery = "SELECT cl.*,'public' as entity_type FROM cm.company_list cl  where "
					+ " domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) order by name asc, domicile_flag desc";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameterList("userCountryList", userCountryList);
		} else {

			StringBuilder queryStrBuilder = new StringBuilder();
			queryStrBuilder.append("select * from (");
			queryStrBuilder.append(
					"(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
							+ "where c.company_ticker = :startsWithsearchCriteria "
							+ "and c.domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) order by c.domicile_flag desc,c.company_active_flag desc)a "
							+ "group by name order by name)");
			queryStrBuilder.append(" union ");
			queryStrBuilder.append(
					"(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
							+ "where c.name like :startsWithsearchCriteria "
							+ "and c.domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) order by c.domicile_flag desc,c.company_active_flag desc)b "
							+ "group by name order by name)");
			queryStrBuilder.append(" union ");
			queryStrBuilder.append(
					"(SELECT * from (SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
							+ "where ( c.name like :searchCriteria or c.proper_name like :searchCriteria or c.company_ticker like :searchCriteria) "
							+ "and c.domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) order by c.domicile_flag desc,c.company_active_flag desc)e "
							+ "group by name order by name))x");
			baseQuery = queryStrBuilder.toString();

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("searchCriteria", "%" + searchCriteria + "%");
			query.setParameter("startsWithsearchCriteria", searchCriteria + "%");
			query.setParameterList("userCountryList", userCountryList);
		}

		if (resultCount != null) {
			noOfResult = resultCount;
		}

		query.setFirstResult(0);
		query.setMaxResults(noOfResult);

		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();
		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs;
	}

	@Override
	public List<CountryListDTO> getCountryList() {

		_log.info("extracting country list from the database");

		String baseQuery = "SELECT cl.* FROM `country_list` cl "
				+ " join cm.company_list cm on cm.domicile_country_code = cl.`country_iso_code_3` "
				+ " where cm.company_active_flag=1 " + " group by cl.`country_id` order by cl.country_name";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);

		@SuppressWarnings("unchecked")
		List<CountryList> data = (List<CountryList>) query.list();

		List<CountryListDTO> cmCompanyDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return cmCompanyDTO;
	}

	@Override
	public CountryListDTO getCountry(String countryCode) {

		_log.info("Extracting country list from the database for " + countryCode);

		String baseQuery = null;

		if (countryCode.length() == 2)
			baseQuery = "SELECT cl.* FROM `country_list` cl "
					+ " join cm.company_list cm on cm.domicile_country_code = cl.`country_iso_code_3` "
					+ " where cl.country_iso_code_2 = :countryCode "
					+ " group by cl.`country_id` order by cl.country_name ";
		else
			baseQuery = "SELECT cl.* FROM `country_list` cl "
					+ " join cm.company_list cm on cm.domicile_country_code = cl.`country_iso_code_3` "
					+ " where cl.country_iso_code_3 = :countryCode "
					+ " group by cl.`country_id` order by cl.country_name ";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameter("countryCode", countryCode);

		@SuppressWarnings("unchecked")
		List<CountryList> data = (List<CountryList>) query.list();

		List<CountryListDTO> cmCompanyDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return cmCompanyDTO.get(0);
	}

	@Override
	public List<CountryListDTO> getCountryList(List<String> countryCodeList) {

		_log.info("Extracting country list from the database for " + countryCodeList);

		String baseQuery = null;

		if (countryCodeList.get(0).length() == 2)
			baseQuery = "SELECT cl.* FROM `country_list` cl "
					+ " join cm.company_list cm on cm.domicile_country_code = cl.`country_iso_code_3` "
					+ " where cl.country_iso_code_2 IN (:countryCodeList) "
					+ " group by cl.`country_id` order by cl.country_name ";
		else
			baseQuery = "SELECT cl.* FROM `country_list` cl "
					+ " join cm.company_list cm on cm.domicile_country_code = cl.`country_iso_code_3` "
					+ " where cl.country_iso_code_3 IN (:countryCodeList) "
					+ " group by cl.`country_id` order by cl.country_name ";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		query.setParameterList("countryCodeList", countryCodeList);

		@SuppressWarnings("unchecked")
		List<CountryList> data = (List<CountryList>) query.list();

		List<CountryListDTO> cmCompanyDTO = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);
		return cmCompanyDTO;
	}

	@Override
	public List<IndexDTO> getIndexList(String exchangeCode) {

		_log.info("extracting index of : " + exchangeCode + " from database");

		String baseQuery = "from ExchangeIndex where exchangeCode =:exchangeCode and isActive=1";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("exchangeCode", exchangeCode);

		@SuppressWarnings("unchecked")
		List<ExchangeIndex> data = (List<ExchangeIndex>) query.list();

		List<IndexDTO> cmIndexDTO = DozerHelper.map(dozerBeanMapper, data, IndexDTO.class);

		return cmIndexDTO;

	}

	@Override
	public IndexDTO getIndex(Integer indexId) throws Exception {

		_log.info("extracting index of : " + indexId + " from database");

		String baseQuery = "from ExchangeIndex where id =:indexId and isActive=1";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("indexId", indexId);

		@SuppressWarnings("unchecked")
		List<ExchangeIndex> data = (List<ExchangeIndex>) query.list();

		List<IndexDTO> cmIndexDTO = DozerHelper.map(dozerBeanMapper, data, IndexDTO.class);

		if (cmIndexDTO.size() != 0) {
			return cmIndexDTO.get(0);
		} else {
			throw new Exception("NO DATA AVAIL");
		}
	}

	@Override
	public List<IndexDTO> getIndex(List<Integer> indexId) throws Exception {

		_log.info("extracting index of : " + indexId + " from database");

		String baseQuery = "from ExchangeIndex where id in (:indexId) and isActive=1";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameterList("indexId", indexId);

		@SuppressWarnings("unchecked")
		List<ExchangeIndex> data = (List<ExchangeIndex>) query.list();

		List<IndexDTO> cmIndexDTO = DozerHelper.map(dozerBeanMapper, data, IndexDTO.class);

		if (cmIndexDTO.size() != 0) {
			return cmIndexDTO;
		} else {
			throw new Exception("NO DATA AVAIL");
		}
	}

	@Override
	public String getCompanyTicker(String companyId) throws Exception {

		// _log.info("extracting Company Ticker of : "+companyId+" from database");

		Query query = factSetSessionFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM factset.sym_v1_sym_ticker_exchange where fsym_id=:companyId")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		query.setParameter("companyId", companyId);

		List<Map<String, Object>> data = query.list();

		String ticker = null;

		if (data.size() != 0) {
			ticker = data.get(0).get("ticker_exchange").toString().split("-")[0];
		} else {
			throw new Exception("No data found");
		}

		return ticker;
	}

	@Override
	public List<FFEntityProfileDTO> getEntityProfile(String entityId) {

		_log.info("extracting split stock information of companiess");

		String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id, ep.* FROM "
				+ "ent_v1_ent_entity_profiles ep,(SELECT @row_number\\:=0) AS t where ep.factset_entity_id=:entityId";

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
				.addEntity(FFEntityProfile.class);
		query.setParameter("entityId", entityId);

		List<FFEntityProfile> data = (List<FFEntityProfile>) query.list();
		List<FFEntityProfileDTO> entityProfileDTOs = DozerHelper.map(dozerBeanMapper, data, FFEntityProfileDTO.class);

		return entityProfileDTOs;
	}

	@Override
	public CompanyDTO getCMCompaniesById(String companyId) {

		// _log.info("getting the company by id :-"+companyId);
		String baseQuery = "SELECT *,'public' as entity_type FROM cm.company_list cl where cl.company_id=:companyId";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		query.setParameter("companyId", companyId);

		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);
		// _log.info(cmCompanyDTOs);
		return cmCompanyDTOs.get(0);
	}

	@Override
	public PCCompanyDTO getPCCompaniesById(String entityId) {
		String baseQuery="";
		if(entityId!=null && !entityId.contains("-")) {
		 baseQuery = "SELECT id,NULL AS company_request_id,cl.cin AS company_id,cl.cin AS entity_id,"
				+ " cl.name,cl.name AS proper_name,cl.description,cl.currency AS reporting_currency,"
				+ " cl.incorporation_date AS latest_ann_update,cl.domicile_country_code,"
				+ " cl.factset_industry AS industry_type,cl.tics_industry_code,cl.industry_code AS ff_ind_code,"
				+ " NULL AS security_id,NULL AS security_type,NULL AS source,cl.is_active,"
				+ " NULL AS company_active_flag,cl.created_at,cl.created_by,cl.last_modified_at,"
				+ " cl.last_modified_by,'Private' AS entity_type FROM ews.company_basic_info cl WHERE cl.cin = :entityId ";
		
		}
		else {
		 baseQuery = "SELECT *,'private' as entity_type FROM pc.pc_company_list cl where cl.entity_id=:entityId";
		}
		Query query = pcDataSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PCCompany.class);
		query.setParameter("entityId", entityId);

		@SuppressWarnings("unchecked")
		List<PCCompany> data = (List<PCCompany>) query.list();

		List<PCCompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, PCCompanyDTO.class);
		// _log.info(cmCompanyDTOs);
		return cmCompanyDTOs.get(0);
	}

	@Override
	public List<CMExchangeDTO> getExchangeList() {

		_log.info("Extracting exchange list from database");

		String baseQuery = "select distinct el.* from cm.exchange_list el join cm.company_list cl on "
				+ "el.exchange_code=cl.exchange_code where el.is_active=1 order by exchange_name ";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMExchangeList.class);

		@SuppressWarnings("unchecked")
		List<CMExchangeList> data = (List<CMExchangeList>) query.list();

		List<CMExchangeDTO> cmExchangeList = DozerHelper.map(dozerBeanMapper, data, CMExchangeDTO.class);
		return cmExchangeList;
	}

	@Override
	public List<CMExchangeDTO> getExchangeList(List<String> userCountryList) {

		_log.info("Extracting exchange list from database");

		/*
		 * String
		 * baseQuery="select distinct el.* from cm.exchange_list el join cm.company_list cl on "
		 * + " el.exchange_code=cl.exchange_code where el.is_active=1 and "+
		 * " domicile_country_code IN(select country_iso_code_3 from country_list where country_iso_code_3 IN(:userCountryList)) order by exchange_name "
		 * ;
		 */

		String baseQuery = "select distinct el.* from cm.exchange_list el join cm.company_list cl on  el.exchange_code=cl.exchange_code where el.is_active=1 and "
				+ " el.country_id IN(select country_id from country_list where country_iso_code_3 IN(:userCountryList)) order by exchange_name ";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMExchangeList.class);
		query.setParameterList("userCountryList", userCountryList);

		@SuppressWarnings("unchecked")
		List<CMExchangeList> data = (List<CMExchangeList>) query.list();

		List<CMExchangeDTO> cmExchangeList = DozerHelper.map(dozerBeanMapper, data, CMExchangeDTO.class);
		return cmExchangeList;
	}

	@Override
	public List<CompanyDTO> getExchangeCompanyList(String exchangeCode, String searchCriteria) {

		// _log.info("Extracting company list for exchange code : "+exchangeCode+" from
		// database");

		/*
		 * String
		 * baseQuery="select cl.*,'public' as entity_type from cm.company_list cl where cl.exchange_code=:exchangeCode "
		 * ;
		 * 
		 * if(searchCriteria!=null){ baseQuery+="and cl.name like :searchCriteria "; }
		 * 
		 * baseQuery+="and cl.is_active=1 order by cl.name";
		 */
		StringBuilder queryStrBuilder = new StringBuilder();
		queryStrBuilder.append("select * from (");
		queryStrBuilder.append(
				"(SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
						+ "where c.company_ticker like :startsWithsearchCriteria "
						+ "and c.exchange_code=:exchangeCode and c.is_active=1 " + "order by c.name asc)");
		queryStrBuilder.append(" union ");
		queryStrBuilder.append(
				"(SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
						+ "where c.name like :startsWithsearchCriteria "
						+ "and c.exchange_code=:exchangeCode and c.is_active=1 " + "order by c.name asc)");
		queryStrBuilder.append(" union ");
		queryStrBuilder.append(
				"(SELECT c.id,c.factset_entity_id,c.proper_name,c.company_id,name,c.reporting_currency,c.factset_industry,c.tics_industry_code,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,c.security_type,c.company_ticker,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,'public' as entity_type FROM cm.company_list c "
						+ "where ( c.name like :searchCriteria or c.proper_name like :searchCriteria or c.company_ticker like :searchCriteria) "
						+ "and c.exchange_code=:exchangeCode and c.is_active=1 "
						+ "group by c.name order by c.name))x");
		String baseQuery = queryStrBuilder.toString();

		// _log.info("baseQuery - "+baseQuery);

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);

		query.setParameter("exchangeCode", exchangeCode);

		if (searchCriteria != null) {
			query.setParameter("searchCriteria", "%" + searchCriteria + "%");
			query.setParameter("startsWithsearchCriteria", searchCriteria + "%");
		}

		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmExchangeList = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);
		return cmExchangeList;
	}

	@Override
	public List<IndexDTO> getCompanyIndexList(String companyId) {

		// _log.info("getting index List from the database");

		String baseQuery = "select case when exchange_code = @ex_code then 1 else 0 end as exchange_order, a.* from index_list a,  (select @ex_code \\:= cm.exchange_code, @country_id \\:= el.country_id,"
				+ " @reg_id \\:= cl.region_id  from company_list cm   join exchange_list el on cm.exchange_code = el.exchange_code  "
				+ " join country_list cl on el.country_id = cl.country_id   "
				+ "where cm.company_id = :companyId)x where a.is_active=1 and ( exchange_code = @ex_code or country_id = @country_id or region_id = @reg_id) group by index_id order by exchange_order desc,  primary_flag desc";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ExchangeIndex.class);
		query.setParameter("companyId", companyId);

		@SuppressWarnings("unchecked")
		List<ExchangeIndex> data = (List<ExchangeIndex>) query.list();

		List<IndexDTO> cmIndexDTO = DozerHelper.map(dozerBeanMapper, data, IndexDTO.class);
		return cmIndexDTO;
	}

	@Override
	public List<BalanceModelDTO> getRatioBalanceModel(String industry, Boolean watchlistFlag, Boolean icFlag,
			Boolean screenerFlag) {
		_log.info("extracting ratio balance models of : " + industry + " from database");

		// split on commas and consume any spaces either side
		List<String> industryList = Arrays.asList(industry.split("\\s*,\\s*"));

		String baseQuery = "from FFBalanceModel where factsetIndustry in (:industry) and type in ('FR','VR') and isActive=1";
		if (watchlistFlag != null && watchlistFlag) {
			baseQuery += " and watchlist_flag=1 ";
		}
		if (icFlag != null && icFlag) {
			baseQuery += " and ic_flag=1 ";
		}
		if (screenerFlag != null && screenerFlag) {
			baseQuery += " and screener_flag=1 ";
		}

		baseQuery += "order by displayOrder asc";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameterList("industry", industryList);

		@SuppressWarnings("unchecked")
		List<FFBalanceModel> data = (List<FFBalanceModel>) query.list();

		List<BalanceModelDTO> balanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return balanceModelDTOs;
	}

	@Override
	public String getItemNameByFieldName(String fieldName) throws Exception {

		// _log.info("extracting Company Ticker of : "+fieldName+" from database");

		Query query = cmSessionFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM cm.industry_balance_model where field_name=:fieldName")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("fieldName", fieldName);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = query.list();
		String itemName = null;
		if (data.size() != 0) {
			itemName = data.get(0).get("description").toString();
		}
		return itemName;
	}

	@Override
	public List<CompanyDTO> getCMCompaniesByIdList(List<String> companyIdList) {
		_log.info("extracting Company metadata info of : " + companyIdList + " from database");

		Query query = cmSessionFactory.getCurrentSession()
				.createSQLQuery(
						"SELECT *,'public' as entity_type FROM cm.company_list where company_id in (:companyIdList)")
				.addEntity(CMCompany.class);
		query.setParameterList("companyIdList", companyIdList);

		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs;
	}

	@Override
	public CompanyDTO getDefaultCompanyForCountry() {

		_log.info("extracting default Company from Globe from database");

		String baseQuery;
		Query query = null;

		baseQuery = "SELECT *,'public' as entity_type FROM `company_list` WHERE `company_id` = ( "
				+ " SELECT `company_id` FROM `default_company_mapping` order by t_mcap desc" + " LIMIT 1" + " )";
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);

		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs.get(0);
	}

	@Override
	public CompanyDTO getDefaultCompanyForCountry(String primaryCountryCode) {

		_log.info("extracting default Company for country : " + primaryCountryCode + " from database");

		String baseQuery;
		Query query = null;

		baseQuery = "SELECT *,'public' as entity_type FROM `company_list` WHERE `company_id` = ( "
				+ " SELECT `company_id` FROM `default_company_mapping` WHERE `country_code` = :countryCode" + " Union "
				+ " (SELECT `company_id` FROM `default_company_mapping` order by t_mcap desc limit 1)" + " LIMIT 1"
				+ " )";
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		query.setParameter("countryCode", primaryCountryCode);

		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs.get(0);
	}

	@Override
	public CompanyDTO getDefaultCompanyForCountry(String primaryCountryCode, List<String> subscribedCountyList) {

		_log.info("extracting default Company for primaryCountryCode : " + primaryCountryCode
				+ ", and subscribedCountyList: " + subscribedCountyList + ", from database");

		String baseQuery;
		Query query = null;
		if (primaryCountryCode != null) {
			baseQuery = "SELECT *,'public' as entity_type FROM `company_list` WHERE `company_id` = ( "
					+ " SELECT `company_id` FROM `default_company_mapping` WHERE `country_code` = :countryCode"
					+ " Union "
					+ " (SELECT `company_id` FROM `default_company_mapping` WHERE `country_code` in (:countryCodes) order by t_mcap desc limit 1)"
					+ " LIMIT 1" + " )";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameter("countryCode", primaryCountryCode);
			query.setParameterList("countryCodes", subscribedCountyList);
		} else {
			baseQuery = "SELECT * FROM `company_list` WHERE `company_id` = (  "
					+ " SELECT `company_id` FROM `default_company_mapping` WHERE `country_code` IN (:countryCodes) order by t_mcap desc limit 1) ";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
			query.setParameterList("countryCodes", subscribedCountyList);
		}

		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs.get(0);
	}

	@Override
	public BalanceModelDTO getFinancialParams(String industry, String fieldName) {
		_log.info(
				"extracting ratio balance models of : " + industry + ", field name:  " + fieldName + " from database");

		String baseQuery = "from FFBalanceModel where factsetIndustry =:industry  and fieldName =:fieldName";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("industry", industry);
		query.setParameter("fieldName", fieldName);

		@SuppressWarnings("unchecked")
		List<FFBalanceModel> data = (List<FFBalanceModel>) query.list();

		List<BalanceModelDTO> balanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return balanceModelDTOs.get(0);
	}

	@Override
	public List<BalanceModelDTO> getFinancialParams(String industry, List<String> fieldNames) {
		_log.info(
				"extracting ratio balance models of : " + industry + ", field name:  " + fieldNames + "from database");

		String baseQuery = "from FFBalanceModel where factsetIndustry =:industry  and fieldName IN (:fieldNames) and isActive=1";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("industry", industry);
		query.setParameterList("fieldNames", fieldNames);

		@SuppressWarnings("unchecked")
		List<FFBalanceModel> data = (List<FFBalanceModel>) query.list();

		List<BalanceModelDTO> balanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return balanceModelDTOs;
	}

	@Override
	public List<CompanyDTO> getCMCompaniesByEntityId(String entityId) {

		_log.info("getting the company by entity :-" + entityId);

		String baseQuery = "SELECT *,'public' as entity_type FROM cm.company_list cl where cl.factset_entity_id=:entityId";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany.class);
		query.setParameter("entityId", entityId);

		@SuppressWarnings("unchecked")
		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs;
	}

	@Override
	public Double getFxData(String periodType, String fxDate, String sourceCurrency, String targetCurrency) {

		String queryString = null;
		if (periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY))
			queryString = "SELECT get_fx_year_conversion(:sourceCurrency,:targetCurrency,:fxDate)";
		else if (periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY))
			queryString = "SELECT get_fx_semiannually_conversion(:sourceCurrency,:targetCurrency,:fxDate)";
		else if (periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY))
			queryString = "SELECT get_fx_quarter_conversion(:sourceCurrency,:targetCurrency,:fxDate)";
		else if (periodType.equalsIgnoreCase(CMStatic.PERIODICITY_MONTHLY))
			queryString = "SELECT get_fx_monthly_conversion(:sourceCurrency,:targetCurrency,:fxDate)";
		else if (periodType.equalsIgnoreCase(CMStatic.PERIODICITY_WEEKLY))
			queryString = "SELECT get_fx_weekly_conversion(:sourceCurrency,:targetCurrency,:fxDate)";
		else
			queryString = "SELECT get_fx_daily_conversion(:sourceCurrency,:targetCurrency,:fxDate)";

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(queryString);

		query.setParameter("sourceCurrency", sourceCurrency);
		query.setParameter("targetCurrency", targetCurrency);
		query.setParameter("fxDate", fxDate);

		Double fxRate = (Double) query.uniqueResult();
		return fxRate;
	}

	@Override
	public String getMetricNameFromMetricCode(String metricCode) {
		try {
			String queryString = "SELECT short_name from industry_balance_model where field_name=:metricCode limit 1";
			Query query = cmSessionFactory.getCurrentSession().createSQLQuery(queryString);
			query.setParameter("metricCode", metricCode);
			String metricName = (String) query.uniqueResult();
			return metricName;

		} catch (Exception e) {
			_log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchAllEntity(String entitySelection,
			String industrySelection, String countrySelection, String currencySelection, String searchKeyWord) {

		Integer noOfResult = 1000;

		String searchKeyWordLike = "";
		if (searchKeyWord != null && searchKeyWord != "") {
			searchKeyWordLike = "%" + searchKeyWord + "%";
		}

		_log.info(" industrySelection " + industrySelection);
		_log.info(" countrySelection " + countrySelection);
		_log.info(" currencySelection " + currencySelection);

		StringBuilder query = new StringBuilder();

		query.append(
				" select (@id \\:= @id + 1) as id,factset_entity_id,company_id,name,proper_name,factset_industry,tics_industry_code, domicile_country_code,security_id,description,is_active,reporting_currency,exchange_code,security_type,company_ticker, revenue, debt_equity, pat ,entity_type,created_at,created_by,last_modified_at,last_modified_by,industry_name,sector_code,sector_name, "
						+ " country,exchange_name,net_worth, latest_ann_update  from((select * from (select  y.factset_entity_id,y.company_id,name,y.proper_name,y.factset_industry,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,y.description,y.is_active,y.exchange_code,y.security_type,y.company_ticker,case when ff_sales is not null then ff_sales * factset.get_fx_year_conversion_we(a.currency,:currencySelection,a.date) else ff_sales end as 'revenue', "
						+ " t_gross_debt_equity as debt_equity,t_pat_margin   as pat , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name,case when ff_eq_tot is not null then  ff_eq_tot* factset.get_fx_daily_conversion_we(a.currency,:currencySelection,a.date) else ff_eq_tot end as net_worth, latest_ann_update from (select *  from (select c.factset_entity_id,c.company_id,  name,c.proper_name,c.factset_industry,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code, "
						+ " c.security_id,c.description,c.is_active,c.exchange_code, c.security_type,c.company_ticker,'public' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by, c.latest_ann_update,ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,e.exchange_name as exchange_name from cm.company_list c  JOIN cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
						+ " join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code join cm.exchange_list e on e.exchange_code=c.exchange_code  join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code "
						+ " where ti.tics_industry_name like :industrySelection  ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			query.append(" and c.domicile_country_code = :countrySelection ");
		} else if (searchKeyWord != null && searchKeyWord != "") {
			query.append(" and c.name like :searchKeyWordLike ");
		}

		query.append(
				" order by domicile_flag DESC , company_active_flag DESC) x group by name order by latest_ann_update desc, name limit :noOfResult)y left join cm.company_latest_financial_af a on a.company_id=y.company_id and  a.period_type='yearly' left join cm.company_latest_financial_der_af d on a.id=d.company_data_id where a.currency!='BYB' or a.currency is null order by a.date desc)z group by name) ");

		query.append(
				" union (select * from (select  y.entity_id,y.entity_id as company_id,name,y.proper_name,y.industry_type,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,null as description,y.is_active,null as exchange_code,y.security_type,null as company_ticker,ff_sales * factset.get_fx_year_conversion_we(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity, t_pat_margin  as pat , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name, case when ff_eq_tot is not null then  ff_eq_tot * factset.get_fx_daily_conversion_we(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date)  else ff_eq_tot end AS net_worth, latest_ann_update  from (select c.entity_id,c.company_id,  name,c.proper_name,c.industry_type,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active, c.security_type,'private' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,c.latest_ann_update,ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,null as exchange_name  from pc.pc_company_list c "
						+ " join cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
						+ " join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			query.append(
					" join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code  where c.domicile_country_code = :countrySelection and ti.tics_industry_name like :industrySelection ");

		} else {
			query.append(
					" left join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code where  ti.tics_industry_name like :industrySelection ");

		}
		if (searchKeyWord != null && searchKeyWord != "") {
			query.append(" and c.name like :searchKeyWordLike ");
		}

		query.append(
				" group by name order by latest_ann_update desc, name limit :noOfResult)y left join pc.pc_latest_data_af a on a.entity_id=y.entity_id  left join pc.pc_latest_data_der_af d on a.entity_id=d.entity_id and a.date=d.date where a.currency!='BYB' or a.currency is null  order by a.date desc)z group by name))x  join (SELECT @id \\:= 0) as ai group by name order by latest_ann_update desc, name limit :noOfResult");

		// _log.info(query.toString());
		Query execQuery = cmSessionFactory.getCurrentSession().createSQLQuery(query.toString())
				.addEntity(AdvancedSearchCompany.class);
		execQuery.setParameter("currencySelection", currencySelection);
		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			execQuery.setParameter("countrySelection", countrySelection);
		}
		execQuery.setParameter("noOfResult", noOfResult);
		execQuery.setParameter("industrySelection", industrySelection);

		if (searchKeyWord != null && searchKeyWord != "") {
			execQuery.setParameter("searchKeyWordLike", searchKeyWordLike);
		}

		_log.info("start time " + new Date());
		@SuppressWarnings("unchecked")
		List<AdvancedSearchCompany> data = (List<AdvancedSearchCompany>) execQuery.list();
		_log.info("end time " + new Date());
//		_log.info(data.size());
		List<AdvanceSearchCompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data,
				AdvanceSearchCompanyDTO.class);
//		_log.info(cmCompanyDTOs.size());
		return cmCompanyDTOs;
	}

	@Override
	public List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchAllEntityAllIndustry(String entitySelection,
			String industrySelection, String countrySelection, String currencySelection, String searchKeyWord) {

		Integer noOfResult = 1000;

		String searchKeyWordLike = "";
		if (searchKeyWord != null && searchKeyWord != "") {
			searchKeyWordLike = "%" + searchKeyWord + "%";
		}

		/*
		 * if(industrySelection != null) industrySelection =
		 * "%"+industrySelection.replaceAll("\\,", "")+"%";
		 * 
		 * if(countrySelection != null) countrySelection =
		 * "%"+countrySelection.replaceAll("\\,", "")+"%";
		 */

		/*
		 * if(currencySelection != null) currencySelection =
		 * "%"+currencySelection.replaceAll("\\,", "")+"%";
		 */

		_log.info(" industrySelection  ::: " + industrySelection);
		_log.info(" countrySelection :::: " + countrySelection);
		_log.info(" currencySelection ::: " + currencySelection);

		StringBuilder query = new StringBuilder();
		/*
		 * query.
		 * append("select (@id \\:= @id + 1) as id,factset_entity_id,company_id,name,proper_name,factset_industry,tics_industry_code,"
		 * +
		 * " domicile_country_code,security_id,description,is_active,reporting_currency,exchange_code,security_type,company_ticker,closing_price, daily_change,"
		 * +
		 * " revenue, debt_equity, pat ,entity_type,created_at,created_by,last_modified_at,last_modified_by  from ((select c.factset_entity_id,c.company_id, "
		 * +
		 * " name,c.proper_name,c.factset_industry,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code,"
		 * +
		 * " c.security_type,c.company_ticker,a.p_price * factset.get_fx_daily_conversion(a.currency,:currencySelection,a.date) as closing_price,t_price_change as daily_change,t_net_sales_growth * factset.get_fx_year_conversion(a.currency,:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity,t_pat_margin as pat ,'public' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by "
		 * +
		 * " from cm.company_list c  JOIN cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
		 * +
		 * " left join cm.company_latest_financial_af a on a.company_id=c.company_id "+
		 * " join cm.company_latest_financial_der_af d on a.company_id=d.company_id "+
		 * " left join cm.company_latest_pricing p on p.company_id=a.company_id "+
		 * " where a.currency like :currencySelection ");
		 * 
		 * if (countrySelection!=null && !countrySelection.isEmpty() &&
		 * !countrySelection.equalsIgnoreCase("world")) {
		 * query.append(" and c.domicile_country_code like :countrySelection "); }
		 * query.append("   order by a.date desc) union "+
		 * " (select c.entity_id,c.company_id,name,c.proper_name,c.industry_type,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,null as description,c.is_active, null as exchange_code,c.security_type,null as company_ticker,p_price * factset.get_fx_daily_conversion(a.currency,:currencySelection,a.date) as closing_price,null as daily_change,t_net_sales_growth * factset.get_fx_year_conversion(a.currency,:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity,t_pat_margin as pat ,'private' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by "
		 * +
		 * " from pc.pc_company_list c left join cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
		 * + " left join pc.pc_latest_data_af a on a.entity_id=c.entity_id "+
		 * " left join pc.pc_latest_data_der_af d on a.entity_id=d.entity_id "+
		 * " where  a.currency like :currencySelection ");
		 * 
		 * if (countrySelection!=null && !countrySelection.isEmpty() &&
		 * !countrySelection.equalsIgnoreCase("world")) {
		 * query.append(" and c.domicile_country_code like :countrySelection "); }
		 * query.
		 * append("   order by a.date desc))x  join (SELECT @id \\:= 0) as ai group by name order by name "
		 * );
		 */

		/*
		 * query.
		 * append(" select (@id \\:= @id + 1) as id,factset_entity_id,company_id,name,proper_name,factset_industry,tics_industry_code, "
		 * +
		 * " domicile_country_code,security_id,description,is_active,reporting_currency,exchange_code,security_type,company_ticker,closing_price, "
		 * +
		 * " daily_change, revenue, debt_equity, pat ,entity_type,created_at,created_by,last_modified_at,last_modified_by  from((select * from (select  y.factset_entity_id, "
		 * +
		 * " y.company_id,name,y.proper_name,y.factset_industry,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,y.description,y.is_active, "
		 * +
		 * " y.exchange_code,y.security_type,y.company_ticker,a.p_price * factset.get_fx_daily_conversion(a.currency,:currencySelection,a.date) as closing_price,t_price_change as daily_change, "
		 * +
		 * " t_net_sales_growth * factset.get_fx_year_conversion(a.currency,:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity,t_pat_margin as pat , entity_type,y.created_at,y.created_by, "
		 * +
		 * " y.last_modified_at,y.last_modified_by from (select *  from (select c.factset_entity_id,c.company_id,  name,c.proper_name,c.factset_industry,c.tics_industry_code, "
		 * +
		 * " c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code, c.security_type,c.company_ticker,'public' as entity_type, "
		 * +
		 * " c.created_at,c.created_by,c.last_modified_at,c.last_modified_by  from cm.company_list c  JOIN cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
		 * +
		 * " order by domicile_flag DESC , company_active_flag DESC) x group by name limit 500)y left join cm.company_latest_financial_af a on a.company_id=y.company_id  join cm.company_latest_financial_der_af d "
		 * +
		 * " on a.company_id=d.company_id  left join cm.company_latest_pricing p on p.company_id=a.company_id  order by a.date desc)z group by name) "
		 * );
		 * 
		 * query.
		 * append(" union (select * from (select  y.entity_id,y.company_id,name,y.proper_name,y.industry_type,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,null as description,y.is_active, "
		 * +
		 * " null as exchange_code,y.security_type,null as company_ticker,a.p_price * factset.get_fx_daily_conversion(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date) as closing_price,null as daily_change, "
		 * +
		 * " t_net_sales_growth * factset.get_fx_year_conversion(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity,t_pat_margin as pat , entity_type,y.created_at,y.created_by, "
		 * +
		 * " y.last_modified_at,y.last_modified_by from (select c.entity_id,c.company_id,  name,c.proper_name,c.industry_type,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code, "
		 * +
		 * " c.security_id,c.description,c.is_active, c.security_type,'private' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by  from pc.pc_company_list c left join cm.tics_industry ti "
		 * +
		 * " ON ti.tics_industry_code = c.tics_industry_code group by name limit 500)y left join pc.pc_latest_data_af a on a.entity_id=y.entity_id  left join pc.pc_latest_data_der_af d on a.entity_id=d.entity_id where "
		 * +
		 * " a.date > date_sub(a.date ,interval 5 year)  order by a.date desc)z group by name))x  join (SELECT @id \\:= 0) as ai group by name order by name "
		 * );
		 */

		query.append(
				" select (@id \\:= @id + 1) as id,factset_entity_id,company_id,name,proper_name,factset_industry,tics_industry_code, domicile_country_code,security_id,description,is_active,reporting_currency,exchange_code,security_type,company_ticker, revenue, debt_equity, pat ,entity_type,created_at,created_by,last_modified_at,last_modified_by,industry_name,sector_code,sector_name, "
						+ " country,exchange_name, net_worth, latest_ann_update  from((select * from (select  y.factset_entity_id,y.company_id,name,y.proper_name,y.factset_industry,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,y.description,y.is_active,y.exchange_code,y.security_type,y.company_ticker,case when ff_sales is not null then ff_sales * factset.get_fx_year_conversion_we(a.currency,:currencySelection,a.date) else ff_sales end as 'revenue', "
						+ " t_gross_debt_equity as debt_equity,t_pat_margin   as pat , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name,case when ff_eq_tot is not null then  ff_eq_tot* factset.get_fx_daily_conversion_we(a.currency,:currencySelection,a.date) else ff_eq_tot end as net_worth, y.latest_ann_update from (select *  from (select c.factset_entity_id,c.company_id,  name,c.proper_name,c.factset_industry,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code, "
						+ " c.security_id,c.description,c.is_active,c.exchange_code, c.security_type,c.company_ticker,'public' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,c.latest_ann_update,ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,e.exchange_name as exchange_name from cm.company_list c  JOIN cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
						+ " join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code join cm.exchange_list e on e.exchange_code=c.exchange_code  join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			query.append(" where c.domicile_country_code = :countrySelection ");
			if (searchKeyWord != null && searchKeyWord != "") {
				query.append(" and c.name like :searchKeyWordLike ");
			}
		} else if (searchKeyWord != null && searchKeyWord != "") {
			query.append(" where c.name like :searchKeyWordLike ");
		}

		query.append(
				" order by domicile_flag DESC , company_active_flag DESC) x group by name order by latest_ann_update desc, name limit :noOfResult)y left join cm.company_latest_financial_af a on a.company_id=y.company_id  and  a.period_type='yearly' left join cm.company_latest_financial_der_af d on a.id=d.company_data_id where a.currency!='BYB' or a.currency is null order by a.date desc)z group by name) ");

		query.append(
				" union (select * from (select  y.entity_id,y.entity_id as company_id,name,y.proper_name,y.industry_type,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,null as description,y.is_active,null as exchange_code,y.security_type,null as company_ticker,ff_sales * factset.get_fx_year_conversion_we(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity, t_pat_margin  as pat , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name,case when ff_eq_tot is not null then  ff_eq_tot * factset.get_fx_daily_conversion_we(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date)  else ff_eq_tot end AS net_worth, y.latest_ann_update from (select c.entity_id,c.company_id,  name,c.proper_name,c.industry_type,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active, c.security_type,'private' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,c.latest_ann_update,ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,null as exchange_name  from pc.pc_company_list c left join cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
						+ " left join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			query.append(
					" inner join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code  where c.domicile_country_code = :countrySelection ");
			if (searchKeyWord != null && searchKeyWord != "") {
				query.append(" and c.name like :searchKeyWordLike ");
			}
		} else {
			query.append(" left join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code ");
			if (searchKeyWord != null && searchKeyWord != "") {
				query.append(" where c.name like :searchKeyWordLike ");
			}
		}

		query.append(
				" group by name order by latest_ann_update desc, name limit :noOfResult)y left join pc.pc_latest_data_af a on a.entity_id=y.entity_id  left join pc.pc_latest_data_der_af d on a.entity_id=d.entity_id and a.date=d.date where a.currency!='BYB' or a.currency is null  order by a.date desc)z group by name))x  join (SELECT @id \\:= 0) as ai group by name order by latest_ann_update desc, name limit :noOfResult");

//		_log.info(query.toString());
		Query execQuery = cmSessionFactory.getCurrentSession().createSQLQuery(query.toString())
				.addEntity(AdvancedSearchCompany.class);
		execQuery.setParameter("currencySelection", currencySelection);
		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			execQuery.setParameter("countrySelection", countrySelection);
		}
		if (searchKeyWord != null && searchKeyWord != "") {
			execQuery.setParameter("searchKeyWordLike", searchKeyWordLike);
		}
		execQuery.setParameter("noOfResult", noOfResult);

		_log.info("start time " + new Date());
		@SuppressWarnings("unchecked")
		List<AdvancedSearchCompany> data = (List<AdvancedSearchCompany>) execQuery.list();
		_log.info("end time " + new Date());
//		_log.info(data.size());
		List<AdvanceSearchCompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data,
				AdvanceSearchCompanyDTO.class);
//		_log.info(cmCompanyDTOs.size());
		return cmCompanyDTOs;
	}

	@Override
	public List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchPublicEntityAllIndustry(String entitySelection,
			String industrySelection, String countrySelection, String currencySelection, String searchKeyWord) {

		// Integer noOfResult=1000;
		String searchKeyWordLike = "";
		if (searchKeyWord != null && searchKeyWord != "") {
			searchKeyWordLike = "%" + searchKeyWord + "%";
		}

		_log.info("searchKeyWordLike ::: " + searchKeyWordLike);

		StringBuilder query = new StringBuilder();
		query.append(
				" select (@id \\:= @id + 1) as id,factset_entity_id,company_id,name,proper_name,factset_industry,tics_industry_code, domicile_country_code,security_id, "
						+ " description,is_active,reporting_currency,exchange_code,security_type,company_ticker, revenue, debt_equity, pat ,entity_type,created_at, "
						+ " created_by,last_modified_at,last_modified_by,industry_name,sector_code,sector_name,country,exchange_name, net_worth, latest_ann_update  from((select * from (select  y.factset_entity_id,y.company_id, "
						+ " name,y.proper_name,y.factset_industry,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,y.description,y.is_active,y.exchange_code,y.security_type, "
						+ " y.company_ticker,case when ff_sales is not null then ff_sales * factset.get_fx_year_conversion_we(a.currency,:currencySelection,a.date) else ff_sales end as 'revenue', "
						+ " t_gross_debt_equity as debt_equity,t_pat_margin   as pat  , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name,case when ff_eq_tot is not null then  ff_eq_tot* factset.get_fx_daily_conversion_we(a.currency,:currencySelection,a.date) else ff_eq_tot end as net_worth, latest_ann_update from (select *  from (select c.factset_entity_id,c.company_id,  name,c.proper_name,"
						+ " c.factset_industry,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code, c.security_type,c.company_ticker,'public' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,c.latest_ann_update, "
						+ " ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,e.exchange_name as exchange_name from cm.company_list c  JOIN cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
						+ " join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code join cm.exchange_list e on e.exchange_code=c.exchange_code join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			query.append(" where c.domicile_country_code = :countrySelection ");
			if (searchKeyWord != null && searchKeyWord != "") {
				query.append(" and c.name like :searchKeyWordLike ");
			}
		} else if (searchKeyWord != null && searchKeyWord != "") {
			query.append(" where c.name like :searchKeyWordLike ");
		}

		query.append(
				" order by domicile_flag DESC , company_active_flag DESC) x group by name order by latest_ann_update desc, name limit 1000)y left join cm.company_latest_financial_af a on a.company_id=y.company_id and  a.period_type='yearly' left join cm.company_latest_financial_der_af d on a.id=d.company_data_id where a.currency!='BYB' or a.currency is null order by a.date desc)z group by name))x  join (SELECT @id \\:= 0) as ai group by name order by latest_ann_update desc, name limit 1000 ");

		Query execQuery = cmSessionFactory.getCurrentSession().createSQLQuery(query.toString())
				.addEntity(AdvancedSearchCompany.class);
		// execQuery.setParameter("industrySelection", industrySelection);
		execQuery.setParameter("currencySelection", currencySelection);
		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			execQuery.setParameter("countrySelection", countrySelection);
		}
		if (searchKeyWord != null && searchKeyWord != "") {
			execQuery.setParameter("searchKeyWordLike", searchKeyWordLike);
		}
		/*
		 * execQuery.setFirstResult(0); execQuery.setMaxResults(noOfResult);
		 */

		@SuppressWarnings("unchecked")
		List<AdvancedSearchCompany> data = (List<AdvancedSearchCompany>) execQuery.list();
//		_log.info(data.size());
		List<AdvanceSearchCompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data,
				AdvanceSearchCompanyDTO.class);
//		_log.info(cmCompanyDTOs.size());
		return cmCompanyDTOs;
	}

	@Override
	public List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchPublicEntity(String entitySelection,
			String industrySelection, String countrySelection, String currencySelection, String searchKeyWord) {

		// Integer noOfResult=1000;

		String searchKeyWordLike = "";
		if (searchKeyWord != null && searchKeyWord != "") {
			searchKeyWordLike = "%" + searchKeyWord + "%";
		}

		StringBuilder query = new StringBuilder();
		query.append(
				" select (@id \\:= @id + 1) as id,factset_entity_id,company_id,name,proper_name,factset_industry,tics_industry_code, domicile_country_code,security_id, "
						+ " description,is_active,reporting_currency,exchange_code,security_type,company_ticker, revenue, debt_equity, pat ,entity_type,created_at, "
						+ " created_by,last_modified_at,last_modified_by,industry_name,sector_code,sector_name,country,exchange_name,net_worth, latest_ann_update  from((select * from (select  y.factset_entity_id,y.company_id, "
						+ " name,y.proper_name,y.factset_industry,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,y.description,y.is_active,y.exchange_code,y.security_type, "
						+ " y.company_ticker,case when ff_sales is not null then ff_sales * factset.get_fx_year_conversion_we(a.currency,:currencySelection,a.date) else ff_sales end as 'revenue', "
						+ " t_gross_debt_equity as debt_equity,t_pat_margin   as pat  , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name,case when ff_eq_tot is not null then  ff_eq_tot* factset.get_fx_daily_conversion_we(a.currency,:currencySelection,a.date) else ff_eq_tot end as net_worth, latest_ann_update from (select *  from (select c.factset_entity_id,c.company_id,  name,c.proper_name,"
						+ " c.factset_industry,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active,c.exchange_code, c.security_type,c.company_ticker,'public' as entity_type,c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,latest_ann_update , "
						+ " ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,e.exchange_name as exchange_name from cm.company_list c  JOIN cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "
						+ " join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code join cm.exchange_list e on e.exchange_code=c.exchange_code join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code "
						+ " where ti.tics_industry_name like :industrySelection ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			query.append(" and c.domicile_country_code = :countrySelection ");
		}

		if (searchKeyWord != null && searchKeyWord != "") {
			query.append(" and c.name like :searchKeyWordLike ");
		}

		query.append(
				" order by domicile_flag DESC , company_active_flag DESC) x group by name order by latest_ann_update desc, name limit 1000)y left join cm.company_latest_financial_af a on a.company_id=y.company_id and  a.period_type='yearly' left join cm.company_latest_financial_der_af d on a.id=d.company_data_id where a.currency!='BYB' or a.currency is null order by a.date desc)z group by name))x  join (SELECT @id \\:= 0) as ai group by name order by latest_ann_update desc, name limit 1000 ");

		Query execQuery = cmSessionFactory.getCurrentSession().createSQLQuery(query.toString())
				.addEntity(AdvancedSearchCompany.class);
		execQuery.setParameter("industrySelection", industrySelection);
		execQuery.setParameter("currencySelection", currencySelection);
		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			execQuery.setParameter("countrySelection", countrySelection);
		}

		if (searchKeyWord != null && searchKeyWord != "") {
			execQuery.setParameter("searchKeyWordLike", searchKeyWordLike);
		}

		@SuppressWarnings("unchecked")
		List<AdvancedSearchCompany> data = (List<AdvancedSearchCompany>) execQuery.list();
//		_log.info(data.size());
		List<AdvanceSearchCompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data,
				AdvanceSearchCompanyDTO.class);
//		_log.info(cmCompanyDTOs.size());
		return cmCompanyDTOs;
	}

	
	@Override
	public List<AdvanceSearchCompanyDTO> getAdvancedCompanySearchPrivateEntity(String entitySelection,
			String industrySelection, String countrySelection, String currencySelection, String searchKeyWord) {
		boolean isIndustryFilter = true;
		if (industrySelection == null || industrySelection.equals("") || industrySelection.equalsIgnoreCase("All"))
			isIndustryFilter = false;
		_log.info("entitySelection ::: " + entitySelection + "\nsearchKeyWord ::: " + searchKeyWord);
		String searchKeyWordLike = "";
		if (searchKeyWord != null && searchKeyWord != "") {
			searchKeyWordLike = "%" + searchKeyWord + "%";
		}
		StringBuilder query = new StringBuilder();
		query.append(
				" SELECT (@id \\:=@id + 1) AS id, q.* FROM ((SELECT * FROM (select factset_entity_id, factset_entity_id as company_id,name,proper_name,factset_industry,tics_industry_code, domicile_country_code,security_id,description,is_active,reporting_currency,exchange_code,security_type,company_ticker, revenue, debt_equity, pat ,entity_type,created_at,created_by,last_modified_at,last_modified_by,industry_name,sector_code,sector_name,country,exchange_name,net_worth, latest_ann_update  from(select * from (select  y.entity_id as factset_entity_id,y.company_id ,name,y.proper_name,y.industry_type factset_industry,y.tics_industry_code,y.reporting_currency,y.domicile_country_code,y.security_id,null as description,y.is_active,null as exchange_code,y.security_type,null as company_ticker, "
						+ " ff_sales * factset.get_fx_year_conversion_we(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date) as 'revenue',t_gross_debt_equity as debt_equity, t_pat_margin   as pat , entity_type,y.created_at,y.created_by,y.last_modified_at,y.last_modified_by,y.industry_name,y.sector_code,y.sector_name,y.country,y.exchange_name, case when ff_eq_tot is not null then  ff_eq_tot * factset.get_fx_daily_conversion_we(ifnull(a.currency,y.reporting_currency),:currencySelection,a.date)  else ff_eq_tot end AS net_worth, latest_ann_update from (select c.entity_id,c.company_id,  name,c.proper_name,c.industry_type,c.tics_industry_code,c.reporting_currency as reporting_currency,c.domicile_country_code,c.security_id,c.description,c.is_active, c.security_type,'private' as entity_type, "
						+ " c.created_at,c.created_by,c.last_modified_at,c.last_modified_by,latest_ann_update ,ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,cl.country_name as country,null as exchange_name  ");
						query.append("  from pc.pc_company_list c ");
						query.append(" JOIN (SELECT tics_industry_code, tics_industry_name, tics_sector_code");
						query.append(" FROM cm.tics_industry) ti ON ti.tics_industry_code = c.tics_industry_code");
						query.append(" JOIN (SELECT tics_sector_code, tics_sector_name FROM cm.tics_sector) s ON s.tics_sector_code = ti.tics_sector_code");
						query.append(" JOIN (SELECT country_iso_code_2, country_name, country_iso_code_3 ");
						query.append(" FROM cm.country_list GROUP BY country_iso_code_2) cl ON cl.country_iso_code_3 = c.domicile_country_code where c.domicile_country_code != 'ind' ");

		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
				///query.append(" join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code ");
				query.append("  and c.domicile_country_code =:countrySelection ");
				if (isIndustryFilter)
					query.append(" AND ti.tics_industry_name like :industrySelection ");
		}
		else {
			if (isIndustryFilter)
			query.append(" AND ti.tics_industry_name like :industrySelection ");
			
		   }
		
		if (searchKeyWord != null && searchKeyWord != "") {
			query.append(" AND c.name LIKE :searchKeyWordLike");
		}
		query.append(
				" group by name order by latest_ann_update desc, name  limit 1000 )y left join pc.pc_latest_data_af a on a.entity_id=y.entity_id  left join pc.pc_latest_data_der_af d on a.entity_id=d.entity_id and a.date=d.date where a.currency!='BYB' or a.currency is null order by a.date desc)z group by name)x ");
		query.append("  group by name order by latest_ann_update desc, name) pc) ");
		query.append(" UNION ");
		query.append("(SELECT factset_entity_id,company_id,name,proper_name,factset_industry, tics_industry_code,");
		query.append(" domicile_country_code,security_id,description,is_active,reporting_currency,exchange_code,");
		query.append(" security_type, company_ticker,revenue,debt_equity,pat,entity_type,created_at,created_by,");
		query.append(" last_modified_at,last_modified_by,industry_name,sector_code,sector_name,country,exchange_name,");
		query.append(" net_worth,latest_ann_update");
		query.append(
				" FROM (SELECT a.cin AS factset_entity_id,a.cin AS company_id,a.name,NULL AS proper_name,a.factset_industry AS factset_industry,");
		query.append(" a.tics_industry_code AS tics_industry_code,'IND' AS domicile_country_code,NULL AS security_id,description,");
		query.append(" a.is_active,:currencySelection AS reporting_currency,NULL AS exchange_code,NULL AS security_type,");
		query.append(
				" NULL AS company_ticker,b.applicable_date,ff_sales * factset.get_fx_year_conversion_we(ifnull(a.currency,b.currency),:currencySelection,b.applicable_date) as 'revenue',t_gross_debt_equity AS debt_equity,");
		query.append(" t_pat_margin AS pat,'Private' AS entity_type,a.created_at,a.created_by,a.last_modified_at,");
		query.append(" a.last_modified_by,a.tics_industry_name AS industry_name,NULL AS sector_code,NULL AS sector_name,");
		query.append(" country_name AS country,NULL AS exchange_name,ff_eq_tot * factset.get_fx_year_conversion_we(ifnull(a.currency,b.currency),:currencySelection,b.applicable_date) AS net_worth,");
		query.append(" a.reporting_yr_start AS latest_ann_update");
		query.append(" FROM ews.company_basic_info a");
		query.append(" JOIN ews.company_data_af b ON a.cin = b.cin");
		query.append(" JOIN ews.company_data_der_af c ON a.cin = c.cin");
		/*
		 * if (searchKeyWord != null && searchKeyWord != "") {
		 * query.append(" Where a.name LIKE :searchKeyWordLike"); }
		 */
		if (searchKeyWord != null && searchKeyWord != "" && isIndustryFilter) {
			query.append(" Where a.name LIKE :searchKeyWordLike AND a.tics_industry_name LIKE :industrySelection");
		}
		else if (isIndustryFilter) {
			query.append(" Where  a.tics_industry_name LIKE :industrySelection");
		}
		else if(searchKeyWord != null && searchKeyWord != "") {
			query.append(" Where a.name LIKE :searchKeyWordLike ");
		}
		query.append(" ORDER BY b.applicable_date DESC,c.applicable_date DESC, name) x");
		query.append(" GROUP BY company_id)) q");
		query.append(" JOIN");
		query.append(" (SELECT @id \\:=0) AS ai");
		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world"))
			query.append("	where domicile_country_code =:countrySelection ");
		query.append(" order by latest_ann_update desc,name LIMIT 1000 ");

		Query execQuery = cmSessionFactory.getCurrentSession().createSQLQuery(query.toString())
				.addEntity(AdvancedSearchCompany.class);
		if(isIndustryFilter)
		execQuery.setParameter("industrySelection", industrySelection);
		execQuery.setParameter("currencySelection", currencySelection);
		if (countrySelection != null && !countrySelection.isEmpty() && !countrySelection.equalsIgnoreCase("world")) {
			execQuery.setParameter("countrySelection", countrySelection);
		}
		if (searchKeyWord != null && searchKeyWord != "") {
			execQuery.setParameter("searchKeyWordLike", searchKeyWordLike);
		}

		@SuppressWarnings("unchecked")
		List<AdvancedSearchCompany> data = (List<AdvancedSearchCompany>) execQuery.list();
		List<AdvanceSearchCompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data,
				AdvanceSearchCompanyDTO.class);
		return cmCompanyDTOs;
	}

	@Override
	public List<EntityStructureDTO> getEntityStructure(String entityId) {

		_log.info("getting getEntityStructure :-" + entityId);

		/*
		 * String baseQuery=
		 * "select a.factset_entity_id as 'entity_id',b.entity_proper_name as 'entity',"
		 * +
		 * " b.entity_type,mt.entity_type_desc as 'entity_type_desc',cl.country_name as entity_country_name,"
		 * +
		 * " a.factset_parent_entity_id as 'parent_entity_id',c.entity_proper_name as 'parent_entity',"
		 * +
		 * " c.entity_type as 'parent_entity_type', a.factset_ultimate_parent_entity_id as 'ultimate_parent_entity_id',"
		 * +
		 * " d.entity_proper_name as 'ultimate_parent_entity',d.entity_type as 'ultimate_parent_entity_type',"
		 * +
		 * " m.entity_type_desc as 'ultimate_parent_entity_type_desc',co.country_name as 'ultimate_parent_country_name' "
		 * + " from factset.ent_v1_ent_entity_structure a" +
		 * " left join factset.sym_v1_sym_entity b on a.factset_entity_id = b.factset_entity_id"
		 * +
		 * " left join factset.sym_v1_sym_entity c on a.factset_parent_entity_id = c.factset_entity_id"
		 * +
		 * " left join factset.sym_v1_sym_entity d on a.factset_ultimate_parent_entity_id = d.factset_entity_id"
		 * + " left join cm.country_list cl on cl.country_iso_code_2=b.iso_country" +
		 * " left join cm.country_list co on co.country_iso_code_2=d.iso_country" +
		 * " left join factset.ref_v2_entity_type_map mt on mt.entity_type_code=b.entity_type"
		 * +
		 * " left join factset.ref_v2_entity_type_map m on m.entity_type_code=d.entity_type"
		 * +
		 * " where a.factset_ultimate_parent_entity_id=:entityId and b.entity_type!='MUT' and c.entity_proper_name is not null"
		 * + " ORDER BY ultimate_parent_entity, parent_entity, entity";
		 */

		String baseQuery = "select a.factset_entity_id as 'entity_id',b.entity_proper_name as 'entity', b.entity_type,mt.entity_type_desc as 'entity_type_desc',cl.country_name as entity_country_name, a.factset_parent_entity_id as 'parent_entity_id',c.entity_proper_name as 'parent_entity', c.entity_type as 'parent_entity_type', a.factset_ultimate_parent_entity_id as 'ultimate_parent_entity_id', d.entity_proper_name as 'ultimate_parent_entity',d.entity_type as 'ultimate_parent_entity_type', m.entity_type_desc as 'ultimate_parent_entity_type_desc',co.country_name as 'ultimate_parent_country_name' "
				+ " from factset.ent_v1_ent_entity_structure a"
				+ " left join factset.sym_v1_sym_entity b on a.factset_entity_id = b.factset_entity_id"
				+ " left join factset.sym_v1_sym_entity c on a.factset_parent_entity_id = c.factset_entity_id"
				+ " left join factset.sym_v1_sym_entity d on a.factset_ultimate_parent_entity_id = d.factset_entity_id"
				+ " left join factset.ref_v2_entity_type_map mt on mt.entity_type_code=b.entity_type"
				+ " left join factset.ref_v2_entity_type_map m on m.entity_type_code=d.entity_type"
				+ " left join cm.country_list cl on cl.country_iso_code_2=b.iso_country"
				+ " left join cm.country_list co on co.country_iso_code_2=d.iso_country"
				+ " where a.factset_ultimate_parent_entity_id=:entityId and b.entity_type!='MUT' "
				+ " and c.entity_proper_name is not null group by a.factset_entity_id,cl.country_iso_code_2 "
				+ " ORDER BY ultimate_parent_entity, parent_entity, entity";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(EntityStructure.class);
		query.setParameter("entityId", entityId);

		@SuppressWarnings("unchecked")
		List<EntityStructure> data = (List<EntityStructure>) query.list();
		_log.info("entityStructureDTOs : " + data.size());
		List<EntityStructureDTO> entityStructureDTOs = DozerHelper.map(dozerBeanMapper, data, EntityStructureDTO.class);
		return entityStructureDTOs;
	}

	@Override
	public List<CMCDTO> getCMCompaniesUsedTriggerByEntityId(String entityId) {
		_log.info("getting the company by entity :-" + entityId);

		String baseQuery = "call get_exchange_list(:entityId)";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("entityId", entityId);
		List<CMCDTO> data = new ArrayList<CMCDTO>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Object[]> result = query.list();
		for (Object[] ob : result) {
			Integer key = map.get((String) ob[7]);
			map.put((String) ob[7], key == null ? 1 : key + 1);
		}

		for (Object[] objects : result) {
			CMCDTO comp = new CMCDTO();
			comp.setRowNo((Integer) objects[0]);
			comp.setId((String) objects[1]);
			comp.setFactSetEntityId((String) objects[2]);
			comp.setName((String) objects[3]);
			comp.setProperName((String) objects[4]);
			comp.setDescription((String) objects[5]);
			comp.setReportingCurrency((String) objects[6]);
			comp.setExchangeCode((String) objects[7]);
			comp.setExchangeName((String) objects[8]);
			comp.setCompanyTicker((String) objects[9]);
			comp.setTickerExchange((String) objects[10]);
			comp.setFf_industry((String) objects[11]);
			comp.setSecurityId((String) objects[12]);
			comp.setEntityType((String) objects[13]);
			Integer key = map.get((String) objects[7]);
			if (key > 1) {
				comp.setDublicate(true);
			}
			comp.setIsActive((boolean) objects[14]);

			data.add(comp);
		}

		return data;
	}
}
