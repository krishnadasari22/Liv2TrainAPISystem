package com.privatecompany.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dto.PEVCFundingInvestmentDTO;
import com.pcompany.dto.PEVCInvestmentHeaderDTO;
import com.pcompany.entity.PEVCAdvancedSearchFinType;
import com.pcompany.entity.PEVCFundingInvestment;
import com.pcompany.entity.PEVCInvestmentDetails;
import com.pcompany.entity.PEVCInvstmentHeader;
import com.pcompany.entity.PevcIssueType;
import com.privatecompany.dao.PeVcDaoA;
import com.privatecompany.dto.PEVCInvestmentDetailsDTO;
import com.televisory.capitalmarket.dto.AdvancedSearchFundingAmoutDto;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.FinancialTypeDto;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.PevcIssueTypeDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.entities.industry.CountryList;
import com.televisory.capitalmarket.util.DozerHelper;
import com.televisory.utils.PEVCFundingDetailAdvancedSerchedQueries;

@Repository
@Transactional
public class PeVcDaoAImpl implements PeVcDaoA {
	
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<CountryListDTO> getPeVcCountries() {
		if(true) {
                String baseQuery="select * from ((SELECT " + 
                " c.*,sum(valuation) as total_valuation " + 
				" FROM " + 
				" factset.sym_v1_sym_entity a " + 
				" JOIN " + 
				" factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
				" JOIN " + 
				" (SELECT * " + 
			    " FROM" + 
				" cm.country_list " + 
				" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country " + 
				" where b.inception_date >= date_sub(curdate(),interval 5 year) " + 
				" group by  a.iso_country) " + 
				" union " + 
				" (select " + 
				" c.*,sum(valuation) "+
				" as total_valuation FROM " + 
				" factset.pe_v1_pe_securities a " + 
				" JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id " + 
				" JOIN (SELECT * " + 
			    "  FROM " + 
				"  cm.country_list "+
				" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country " + 
				" JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " + 
				" JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " + 
				" where a.inception_date >= date_sub(curdate(),interval 5 year) and g.event_date>= date_sub(curdate(),interval 5 year) " + 
				" group by  b.iso_country))x group by country_name  having sum(total_valuation) > 0 " + 
				" order by country_name "; 
				
        Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		
		List<CountryList>country=query.list();
		List<CountryListDTO>countryListDTOs=DozerHelper.map(dozerBeanMapper, country, CountryListDTO.class);
		return countryListDTOs;
		}
		else {
		 String baseQuery="select country_name from ((SELECT DISTINCT " + 
		 		" c.country_name,sum(valuation) as total_valuation " + 
		 		" FROM " + 
		 		" factset.sym_v1_sym_entity a " + 
		 		" JOIN " + 
		 		" factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
		 		" JOIN " + 
		 		" (SELECT " + 
		 		" country_iso_code_2,country_iso_code_3, country_name " + 
		 		" FROM " + 
		 		" cm.country_list " + 
		 		" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country " + 
		 		" where b.inception_date between :startDate AND :endDate " + 
		 		" group by  a.iso_country) " + 
		 		"union " + 
		 		"(select DISTINCT " + 
		 		" c.country_name,sum(valuation)  as total_valuation FROM " + 
		 		" factset.pe_v1_pe_securities a " + 
		 		" JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id " + 
		 		" JOIN (SELECT " + 
		 		" country_iso_code_2,country_iso_code_3, country_name " + 
		 		" FROM " + 
		 		" cm.country_list " + 
		 		" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country  " + 
		 		" JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " + 
		 		" JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " + 
		 		" where a.inception_date between :startDate AND :endDate and g.event_date between :startDate AND :endDate " + 
		 		" group by  b.iso_country))x group by country_name  having sum(total_valuation) > 0 " + 
		 		" order by country_name " ;
					
	        Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
			query.setParameter("startDate", "");
			query.setParameter("endDate", "");
			List<CountryList>country=query.list();
			List<CountryListDTO>countryListDTOs=DozerHelper.map(dozerBeanMapper, country, CountryListDTO.class);
			return countryListDTOs;
		}
		
	}

	@Override
	public List<IndustryFinancialDataDTO> getPeVcIndustries(String countryCode, Date startDate,Date endDate) {
		Query query = null;
		if(countryCode!=null && startDate!=null && endDate !=null) {
			query=cmSessionFactory.getCurrentSession().createSQLQuery("SELECT" + 
					"*" + 
					" FROM " + 
					" (" + 
					" (SELECT " + 
					"  f.tics_industry_name as ticsIndustryName,f.tics_industry_code as ticsIndustryCode " + 
					"  FROM " + 
					"  factset.sym_v1_sym_entity a " + 
					"  JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					"  JOIN cm.country_list c ON a.iso_country = c.country_iso_code_2 " + 
					"  JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					"  JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code " + 
					"  JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
					"  WHERE" + 
					"  c.country_iso_code_3 =:country_iso_code_3 " + 
					"  and b.inception_date between :startDate and :endDate " + 
					"   GROUP BY entity_proper_name limit 1000)  UNION (SELECT " + 
					"   f.tics_industry_name,f.tics_industry_code" + 
					"    FROM " + 
					"    factset.sym_v1_sym_entity a " + 
					"    JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					"    JOIN cm.country_list c ON a.iso_country = c.country_iso_code_2 " + 
					"    JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					"    JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code" + 
					"    JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
					"    WHERE" + 
					"   c.country_iso_code_3 = :country_iso_code_31" + 
					"   and b.inception_date between :startDate1 and :endDate1" + 
					"    GROUP BY entity_proper_name limit 1000) ) z order by ticsIndustryName ");
			
			query.setParameter("country_iso_code_3", countryCode);
			query.setParameter("country_iso_code_31", countryCode);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("startDate1", startDate);
			query.setParameter("endDate1", endDate);
		}
		else if((countryCode==null || countryCode.equalsIgnoreCase("")) && startDate!=null && endDate !=null) {
			query=cmSessionFactory.getCurrentSession().createSQLQuery("SELECT" + 
					"*" + 
					" FROM " + 
					" (" + 
					" (SELECT " + 
					"  f.tics_industry_name as ticsIndustryName,f.tics_industry_code as ticsIndustryCode " + 
					"  FROM " + 
					"  factset.sym_v1_sym_entity a " + 
					"  JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					"  JOIN cm.country_list c ON a.iso_country = c.country_iso_code_2 " + 
					"  JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					"  JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code " + 
					"  JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
					"  WHERE" + 
					" b.inception_date between :startDate and :endDate " + 
					"   GROUP BY entity_proper_name limit 1000)  UNION (SELECT " + 
					"   f.tics_industry_name,f.tics_industry_code" + 
					"    FROM " + 
					"    factset.sym_v1_sym_entity a " + 
					"    JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					"    JOIN cm.country_list c ON a.iso_country = c.country_iso_code_2 " + 
					"    JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					"    JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code" + 
					"    JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
					"    WHERE" + 
					"    b.inception_date between :startDate1 and :endDate1" + 
					"    GROUP BY entity_proper_name limit 1000) ) z order by ticsIndustryName ");
			
			
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("startDate1", startDate);
			query.setParameter("endDate1", endDate);
			
		}
		query.setResultTransformer(Transformers.aliasToBean(IndustryFinancialDataDTO.class));
		return query.list();
		//return null;
	}

	@Override
	public List<CompanyDTO> getPeVcCompanies(String searchCriteria, String countryCode, String industry, Date startDate,
			Date endDate) {
		Query query=null;
		if(searchCriteria==null) {
			query=cmSessionFactory.getCurrentSession().createSQLQuery(/*"SELECT " + 
					" * " + 
					" FROM " + 
					" ((SELECT " + 
					" a.factset_entity_id as factSetEntityId, a.entity_proper_name as properName, c.country_iso_code_3 as countryCode, c.country_name as countryName ,f.tics_industry_code as ticsIndustryCode ,f.tics_industry_name as ticsIndustryName " + 
					" FROM " + 
					" factset.sym_v1_sym_entity a " + 
					" JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					" JOIN (SELECT " + 
					" country_iso_code_2,country_iso_code_3, country_name " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 " + 
					" JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					" JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code " + 
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
				    " GROUP BY entity_proper_name " + 
					" LIMIT 1000) UNION (SELECT " + 
					" a.factset_entity_id as factSetEntityId , a.entity_proper_name as properName, c.country_iso_code_3 as countryCode, c.country_name as countryName ,f.tics_industry_code as ticsIndustryCode ,f.tics_industry_name as ticsIndustryName " + 
					" FROM " + 
					" factset.sym_v1_sym_entity a " + 
					" JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					" JOIN (SELECT " + 
					" country_iso_code_2,country_iso_code_3, country_name " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 " + 
					" JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					" JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code " + 
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
					" GROUP BY entity_proper_name " + 
					" LIMIT 1000)) z"*/
					"SELECT * FROM "
					+"((SELECT "
					+"a.factset_entity_id as factSetEntityId, a.entity_proper_name as properName "
					+"FROM factset.sym_v1_sym_entity a "
					+"JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "
		            +"GROUP BY entity_proper_name "
		            +"LIMIT 1000) UNION (SELECT "
		            +"a.factset_entity_id as factSetEntityId , a.entity_proper_name as properName "
		            +"FROM factset.sym_v1_sym_entity a "
		            +"JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "
		            +"GROUP BY entity_proper_name "
		            +"LIMIT 1000)) z");
			
		}
		else {

			query=cmSessionFactory.getCurrentSession().createSQLQuery(/*"SELECT " + 
					" * " + 
					" FROM " + 
					" ((SELECT " + 
					" a.factset_entity_id as factSetEntityId, a.entity_proper_name as properName, c.country_iso_code_3 as countryCode, c.country_name as countryName ,f.tics_industry_code as ticsIndustryCode ,f.tics_industry_name as ticsIndustryName " + 
					" FROM " + 
					" factset.sym_v1_sym_entity a " + 
					" JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					" JOIN (SELECT " + 
					" country_iso_code_2,country_iso_code_3, country_name " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 " + 
					" JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					" JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code " + 
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
                    "WHERE a.entity_proper_name LIKE :properName"+
				    " GROUP BY entity_proper_name " + 
					" LIMIT 1000) UNION (SELECT " + 
					" a.factset_entity_id as factSetEntityId , a.entity_proper_name as properName, c.country_iso_code_3 as countryCode, c.country_name as countryName ,f.tics_industry_code as ticsIndustryCode ,f.tics_industry_name as ticsIndustryName " + 
					" FROM " + 
					" factset.sym_v1_sym_entity a " + 
					" JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
					" JOIN (SELECT " + 
					" country_iso_code_2,country_iso_code_3, country_name " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 " + 
					" JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = a.factset_entity_id " + 
					" JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code " + 
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
					"WHERE a.entity_proper_name LIKE :properName1"+
					" GROUP BY entity_proper_name " + 
					" LIMIT 1000)) z"*/ 
					"SELECT * FROM "
					+"((SELECT "
					+"a.factset_entity_id as factSetEntityId, a.entity_proper_name as properName "
					+"FROM factset.sym_v1_sym_entity a "
					+"JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "
					+"WHERE a.entity_proper_name LIKE :properName "
		            +"GROUP BY entity_proper_name "
		            +"LIMIT 1000) UNION (SELECT "
		            +"a.factset_entity_id as factSetEntityId , a.entity_proper_name as properName "
		            +"FROM factset.sym_v1_sym_entity a "
		            +"JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "
		            +"WHERE a.entity_proper_name LIKE :properName1 "
		            +"GROUP BY entity_proper_name "
		            +"LIMIT 1000)) z");
			query.setParameter("properName", searchCriteria +"%");
			query.setParameter("properName1", "%"+searchCriteria +"%");
		
			
		}
		query.setResultTransformer(Transformers.aliasToBean(CompanyDTO.class));
		return query.list();
		
		
	}

	@Override
	public List<PEVCInvestmentDetailsDTO> getPeVcFundingInvestments(String entityId, String category) {
		Query query=factSetSessionFactory.getCurrentSession().createSQLQuery("SELECT (@id \\:= @id + 1) as id,x.* from(select " + 
				"spi.pevc_security_id, sec.security_name, sec.inception_date, sec.category_name_desc," + 
				"spi.factset_investor_entity_id, ei.entity_proper_name ," + 
				"case when spi.termination_date is null then 'Active' else 'Exit' end as status, " + 
				"spi.pct_held, " + 
				"spi.termination_date, " + 
				"m.entity_type_desc as entity_type, " + 
				"(select entity_proper_name from factset.pe_v1_pe_fund_portco p join factset.pe_v1_pe_fund_structure s on " + 
				" s.factset_fund_entity_id=p.factset_fund_entity_id " + 
				" join factset.sym_v1_sym_entity e on e.factset_entity_id=p.factset_fund_entity_id where p.factset_portco_entity_id=spi.factset_portco_entity_id " + 
				" and s.factset_pevc_firm_entity_id=spi.factset_investor_entity_id group by s.factset_pevc_firm_entity_id) as fundName " + 
				"from " + 
				"factset.pe_v1_pe_stakes_securities_pvt_invest spi " + 
				"inner join factset.pe_v1_pe_securities sec on sec.pevc_security_id = spi.pevc_security_id " + 
				"left join factset.sym_v1_sym_entity ei on ei.factset_entity_id = spi.factset_investor_entity_id " + 
				"join factset.ref_v2_entity_type_map m on m.entity_type_code=ei.entity_type where " + 
				"spi.factset_portco_entity_id=:factset_portco_entity_id and category_name_desc=:category_name_desc " + 
				"order by spi.pevc_security_id) x join (SELECT @id \\:= 0) as ai").addEntity(PEVCInvestmentDetails.class);
		
		query.setParameter("factset_portco_entity_id", entityId);
		query.setParameter("category_name_desc", category);
		
		@SuppressWarnings("unchecked")
		List<PEVCFundingInvestment> data = (List<PEVCFundingInvestment>) query.list();
		List<PEVCInvestmentDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PEVCInvestmentDetailsDTO.class);	
		return dcsDTOs;
		
		
		
	}

	@Override
	public PEVCInvstmentHeader getPeVcInvestmentHeader(String entityId, String category, Date startDate,
			Date endDate, String financialType,String currencyCode) {
		PEVCInvestmentHeaderDTO pevcInvestmentHeaderDTO=new PEVCInvestmentHeaderDTO();
		Query query=factSetSessionFactory.getCurrentSession().createSQLQuery("SELECT 1 as id ,"+
               "factset_portco_entity_id,"+
                "entity_proper_name, "+
               "country_iso_code_3, "+
               " country_name, "+
               " tics_industry_code, "+
               " tics_industry_name, "+
               " security_name, "+
               "  category_name_desc, "+
               "  issue_type, "+
               "  portco_fin_type, "+
                "  inception_date, "+
                " iso_currency, "+
               "  valuation, "+
               "  valuation_cal, "+
               "  ' " +currencyCode +" ' AS target_currency," + 
               "  iso_currency AS investment_currency "+
               "  FROM "+
               "  ((SELECT "+
               "  a.factset_portco_entity_id, "+
               "  b.entity_proper_name, "+
                " c.country_iso_code_3, "+
               "  c.country_name, "+
               " f.tics_industry_code, "+
               "  f.tics_industry_name, "+
               "  security_name, "+
               "  category_name_desc, "+
               " issue_type_desc as issue_type, "+
               "  portco_fin_type, "+
               " inception_date, "+
               "  iso_currency, "+
               " valuation  AS valuation, "+
               "  (valuation * factset.get_fx_daily_conversion(iso_currency, :currency, inception_date))  AS valuation_cal,  "+
               "  ' " +currencyCode +" ' AS target_currency," + 
               " iso_currency AS investment_currency"+
               "  FROM  "+
               " factset.pe_v1_pe_securities a "+
               "  JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "+
               "  JOIN (SELECT "+
               " country_iso_code_2,country_iso_code_3, country_name "+
               "  FROM "+
               "  cm.country_list "+
               " GROUP BY country_iso_code_2) c ON b.iso_country = c.country_iso_code_2 "+
               "  JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_entity_id "+
               "  JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "+
               "  JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "+
               " join factset.ref_v2_issue_type_map m on m.issue_type_code=a.issue_type "+
                " WHERE "+
               " a.factset_portco_entity_id =:factset_portco_entity_id "+
               "  AND a.inception_date BETWEEN :startDate AND  :endDate "+
               "  AND a.category_name_desc =:category_name_desc "+
               "  AND a.portco_fin_type ='vc') UNION (SELECT "+
                " a.factset_portco_entity_id, "+
                " d.entity_proper_name, "+
               " e.country_iso_code_3, "+
                "e.country_name, "+
                " h.tics_industry_code, "+
                " h.tics_industry_name, "+
                "  security_name, "+
                " category_name_desc, "+
                " issue_type_desc as issue_type, "+
                " portco_fin_type, "+
               "  b.event_date AS inception_date, "+
               "  iso_currency, "+
               " transaction_value  AS valuation, "+
               "  (transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, b.event_date)) AS valuation_cal, "+
               "  ' " +currencyCode +" ' AS target_currency," + 
              "  iso_currency AS investment_currency "+
                    " FROM "+
               " factset.pe_v1_pe_securities a "+
               "  JOIN factset.pe_v1_pe_portco_pvt_invest_evts b ON a.factset_portco_entity_id = b.factset_portco_entity_id "+
                " JOIN factset.ma_v1_ma_deal_terms c ON b.deal_id = c.deal_id "+
                " JOIN factset.sym_v1_sym_entity d ON a.factset_portco_entity_id = d.factset_entity_id "+
                " JOIN (SELECT "+
               " country_iso_code_2,country_iso_code_3, country_name "+
                " FROM "+
                " cm.country_list "+
                " GROUP BY country_iso_code_2) e ON d.iso_country = e.country_iso_code_2 "+
                " JOIN factset.sym_v1_sym_entity_sector f ON d.factset_entity_id = f.factset_entity_id "+
                  "  JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) g ON f.industry_code = g.ff_ind_code "+
                " JOIN cm.tics_industry h ON h.tics_industry_code = g.tics_industry_code "+
                " join factset.ref_v2_issue_type_map m on m.issue_type_code=a.issue_type "+
                "  WHERE " + 
				"  a.factset_portco_entity_id =:factset_portco_entity_id" + 
				"  AND b.event_date BETWEEN :startDate AND :endDate " + 
				" AND a.inception_date BETWEEN :startDate AND :endDate " + 
				" AND category_name_desc =:category_name_desc " + 
				" and portco_fin_type!='vc' "+
				" AND c.ver = 1)) x " + 
				" ORDER BY inception_date DESC").addEntity(PEVCInvstmentHeader.class);
		query.setParameter("factset_portco_entity_id", entityId);
		query.setParameter("category_name_desc", category);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currency", currencyCode);
		//query.setParameter("portco_fin_type", financialType);
		PEVCInvstmentHeader data = (PEVCInvstmentHeader) query.uniqueResult();
		return data;
		
	}

	@Override
	public List<PevcIssueTypeDTO> getPeVcIssueType(String country,String industry,String financingType) {

		boolean industrySelected=false;
	    boolean countrySelected=false;
	    String baseQuery=null;
	    String countryJoin1="";
	    String countryJoin2="";
	    String countryfilter="";
	    String industryJoin1 ="";
	    String industryjoin2="";
	    String industryFilter="";
	    boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<>();
	    if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelectedIssue;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCIssue;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePEIssue;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOTIssue;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPEIssue;
		    }
		    else {
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultiIssue;
		    	isMultiPleFintype=true;
		    	 multipleFinType= Arrays.asList(financingType.split(","));
		    }
		  }
	    
	    if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
	    	countryJoin1+=" JOIN "+
	    	   " (SELECT "+
	    	      "  country_iso_code_2, country_name,country_iso_code_3 "+
	    	    " FROM "+
	    	      " cm.country_list "+
	    	   " GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 ";
	    	countryfilter+="and c.country_iso_code_3 in (:country)";
	    	
	    	countryJoin2+=" JOIN (SELECT " + 
	    			" country_iso_code_2, country_name, country_iso_code_3 " + 
	    			" FROM " + 
	    			" cm.country_list " + 
	    			" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 ";
	    	countrySelected=true;
	    	
	    }
	    
	    if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry) && !Arrays.asList(industry.split(",")).contains("All")){
	    	industryJoin1+="JOIN factset.sym_v1_sym_entity_sector d ON b.factset_portco_entity_id = d.factset_entity_id " + 
	    			" JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code ";
	    	
	    	industryjoin2+=" JOIN factset.sym_v1_sym_entity_sector d ON b.factset_portco_entity_id = d.factset_entity_id " + 
			" JOIN (SELECT " + 
			" tics_industry_code, ff_ind_code " + 
			" FROM " + 
			" cm.tics_industry_mapping " + 
			" GROUP BY ff_ind_code) e ON d.industry_code = e.ff_ind_code " + 
			" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " ;
	    	
	    	industryFilter+=" AND f.tics_industry_code in (:tics_industry_code)";
	    	industrySelected=true;
	    }
	    baseQuery=baseQuery.replace("#countryJoin1", countryJoin1);
	    baseQuery=baseQuery.replace("#countryJoin2", countryJoin2);
	    baseQuery=baseQuery.replace("#countryilter", countryfilter);
	    baseQuery=baseQuery.replace("#countryilter", countryfilter);
	    baseQuery=baseQuery.replace("#industryJoin1", industryJoin1);
	    baseQuery=baseQuery.replace("#industryJoin2", industryjoin2);
	    baseQuery=baseQuery.replace("#industryFilter", industryFilter);
	    baseQuery=baseQuery.replace("#industryFilter", industryFilter);
	    Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PevcIssueType.class);
	    if(countrySelected) {
	    	query.setParameterList("country", Arrays.asList(country.split(",")));
	    }
	    if(industrySelected) {
	    	query.setParameterList("tics_industry_code", Arrays.asList(industry.split(",")));
	    }
	    if(isMultiPleFintype) {
			 query.setParameterList("multipleFinType", multipleFinType);
		 }
	    @SuppressWarnings("unchecked")
		List<PevcIssueType> data = (List<PevcIssueType>) query.list();
		List<PevcIssueTypeDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PevcIssueTypeDTO.class);	
		return dcsDTOs;
		
	
	}

	@Override
	public List<CountryListDTO> getPeVcAdvancedSearchCountries(String ticsIndustry, String financingType,
			String issueType) {
		boolean industrySelected=false;
		boolean issueTypeSelected=false;
		String baseQuery=null;
		boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<String>();
		if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelectedCountry;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCCountry;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePECountry;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOTCountry;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPECountry;
		    	
		    }
		    else {
		        baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultipleCountry;
		        multipleFinType= Arrays.asList(financingType.split(","));
		    	isMultiPleFintype=true;
		    	
		    }
		  }
		String issueFilter1="";
		String issueFilter2="";
		String industryFilter="";
		String industryJoin1="";
		String industryJoin2="";
		if(issueType!=null && !"All".equalsIgnoreCase(issueType) && !"".equals(issueType) && !"null".equals(issueType) && !Arrays.asList(issueType.split(",")).contains("All")) {
	    	issueFilter1+="AND b.issue_type in (:issue_type) ";
	    	issueFilter2+="AND a.issue_type in (:issue_type) ";
	    	issueTypeSelected=true;
	    }
		if(ticsIndustry!=null && !"All".equalsIgnoreCase(ticsIndustry) && !"".equals(ticsIndustry) && !"null".equals(ticsIndustry) && !Arrays.asList(ticsIndustry.split(",")).contains("All")) {
			industryJoin1+="JOIN factset.sym_v1_sym_entity_sector d ON b.factset_portco_entity_id = d.factset_entity_id "+
					"JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "+
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code ";
			
			
			industryJoin2+="JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "+
					"JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "+
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code ";
			
			industryFilter+="AND e.tics_industry_code in (:tics_industry_code)";
			
			industrySelected=true;
		}
		
		 baseQuery = baseQuery.replace("#issueFilter1", issueFilter1);
		 baseQuery = baseQuery.replace("#issueFilter2", issueFilter2);
		 baseQuery = baseQuery.replace("#industryJoin1", industryJoin1);
		 baseQuery = baseQuery.replace("#industryJoin2", industryJoin2);
		 baseQuery = baseQuery.replace("#industry", industryFilter);
		 baseQuery = baseQuery.replace("#industry", industryFilter);
		 Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		 if(issueTypeSelected) {
			query.setParameterList("issue_type", Arrays.asList(issueType.split(",")));
		 }
		 if(industrySelected) {
			query.setParameterList("tics_industry_code", Arrays.asList(ticsIndustry.split(","))) ;
		 }
		 if(isMultiPleFintype) {
			 query.setParameterList("multipleFinType", multipleFinType);
		 }
		 List<CountryList>country=query.list();
	   List<CountryListDTO>countryListDTOs=DozerHelper.map(dozerBeanMapper, country, CountryListDTO.class);
		return countryListDTOs;
	}

	@Override
	public List<IndustryFinancialDataDTO> getPeVcAdvancedSearchIndustries(String country, String financingType,
			String issueType) {
		boolean countrySelected=false;
		boolean issueSelected=false;
		String countryJoin1="";
		String countryJoin2="";
		String countryFilter="";
		String issueFilter1="";
		String baseQuery=null;
		boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<>();
		
		 if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelectedIndustry;
		    }else{
		    	if("VC".equalsIgnoreCase(financingType)){
			    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCIndustry;
			    }else if("PE".equalsIgnoreCase(financingType)){
			    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePEIndustry;
			    }else if("OT".equalsIgnoreCase(financingType)){
			    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOTIndustry;
			    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
			    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPEIndustry;
			    }
			    else {
			    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultiPleIndustry;
			    	multipleFinType=Arrays.asList(financingType.split(","));
			    	isMultiPleFintype=true;
			    }
			  }
		 
		 if(issueType!=null && !"All".equalsIgnoreCase(issueType) && !"".equals(issueType) && !"null".equals(issueType) && !Arrays.asList(issueType.split(",")).contains("All")) {
		    	issueFilter1+="AND b.issue_type in (:issue_type) ";
		    	
		    	issueSelected=true;
		    }
		 if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
			 if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType) || isMultiPleFintype) {
				 countryJoin1+=" JOIN " + 
					 		" (SELECT " + 
					 		" country_iso_code_2, country_name,country_iso_code_3 " + 
					 		"  FROM " + 
					 		" cm.country_list " + 
					 		" GROUP BY country_iso_code_2) c ON d.iso_country = c.country_iso_code_2 ";
				 

				 countryJoin2+=" JOIN " + 
				 		" (SELECT " + 
				 		" country_iso_code_2, country_name,country_iso_code_3 " + 
				 		"  FROM " + 
				 		" cm.country_list " + 
				 		" GROUP BY country_iso_code_2) c ON x.iso_country = c.country_iso_code_2 ";
				 
			 }
			 else {
			 countryJoin1+=" JOIN " + 
			 		" (SELECT " + 
			 		" country_iso_code_2, country_name,country_iso_code_3 " + 
			 		"  FROM " + 
			 		" cm.country_list " + 
			 		" GROUP BY country_iso_code_2) c ON x.iso_country = c.country_iso_code_2 ";
			 
			 

			 countryJoin2+=" JOIN " + 
			 		" (SELECT " + 
			 		" country_iso_code_2, country_name,country_iso_code_3 " + 
			 		"  FROM " + 
			 		" cm.country_list " + 
			 		" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 ";
			 
			 }
			 countryFilter+= " AND c.country_iso_code_3 in (:country)";
			 countrySelected=true;
		 }
		 baseQuery=baseQuery.replace("#countryJoin1", countryJoin1);
		 baseQuery=baseQuery.replace("#countryJoin2", countryJoin2);
		 baseQuery=baseQuery.replace("#countryJoin2", countryJoin2);
		 baseQuery=baseQuery.replace("#countryFilter", countryFilter);
		 baseQuery=baseQuery.replace("#countryFilter", countryFilter);
		 baseQuery=baseQuery.replace("#issueFilter", issueFilter1);
		 baseQuery=baseQuery.replace("#issueFilter", issueFilter1);
		 Query query=cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		 if(countrySelected) {
			 query.setParameterList("country", Arrays.asList(country.split(",")));
		 }
		 if(issueSelected) {
			 query.setParameterList("issue_type",  Arrays.asList(issueType.split(",")));
		 }
		 
		 if(isMultiPleFintype) {
			 query.setParameterList("multipleFinType", multipleFinType);
		 }
		 query.setResultTransformer(Transformers.aliasToBean(IndustryFinancialDataDTO.class));
		 return query.list();
		 
		
	}

	@Override
	public List<FinancialTypeDto> getPeVcAdvancedSearchFinancialType(String country, String industry, String issueType) {
		String baseQuery=null;
		baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcAdvancedSearchFinancialType;
		boolean countrySelected=false;
		boolean industrySelected=false;
		boolean issueTypeSelected=false;
		String countryJoin="";
		String countryFilter="";
		String industryJoin="";
		String industryFilter="";
		String issueFilter="";
		if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
			countryJoin+=" JOIN (SELECT * " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country ";
			countryFilter+=" AND c.country_iso_code_3 in (:country) ";
					
          countrySelected = true;
	    }
		
		if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry) && !Arrays.asList(industry.split(",")).contains("All")){
			industryJoin+=" JOIN factset.sym_v1_sym_entity_sector d ON x.factset_portco_entity_id = d.factset_entity_id " + 
					" JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code " + 
					" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code ";
			industryFilter+="AND f.tics_industry_code in (:industry)";
			industrySelected=true;
		}
		
		 if(issueType!=null && !"All".equalsIgnoreCase(issueType) && !"".equals(issueType) && !"null".equals(issueType) && !Arrays.asList(issueType.split(",")).contains("All")) {
			 issueFilter+=" where b.issue_type in (:issue_type) ";
			 issueTypeSelected=true;
		 }
		 baseQuery=baseQuery.replace("#industryJoin", industryJoin);
		 baseQuery=baseQuery.replace("#industry", industryFilter);
		 baseQuery=baseQuery.replace("#countryJoin", countryJoin);
		 baseQuery=baseQuery.replace("#country", countryFilter);
		 baseQuery=baseQuery.replace("#issueFilter", issueFilter);
		 Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PEVCAdvancedSearchFinType.class);
		 
		 if(countrySelected) {
			 query.setParameterList("country", Arrays.asList(country.split(",")));
		 }
		 
		 if(industrySelected) {
			 query.setParameterList("industry", Arrays.asList(industry.split(",")));
		 }
		 if(issueTypeSelected) {
			 query.setParameterList("issue_type", Arrays.asList(issueType.split(",")));
		 }
		 
		 
		
			List<PEVCAdvancedSearchFinType> data = (List<PEVCAdvancedSearchFinType>) query.list();
			List<FinancialTypeDto> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, FinancialTypeDto.class);	
			return dcsDTOs;
	}

	@Override
	public List<PevcIssueTypeDTO> getPeVcAdvancedSearchIssueType(String country, String industry,
			String financingType) {
		boolean industrySelected=false;
	    boolean countrySelected=false;
	    String baseQuery=null;
	    String countryJoin1="";
	    String countryJoin2="";
	    String countryfilter="";
	    String industryJoin1 ="";
	    String industryjoin2="";
	    String industryFilter="";
	    boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<>();
	    if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelectedIssue;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCIssue;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePEIssue;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOTIssue;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPEIssue;
		    }
		    else {
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultiIssue;
		    	isMultiPleFintype=true;
		    	 multipleFinType=Arrays.asList(financingType.split(","));
		    }
		  }
	    
	    if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
	    	countryJoin1+=" JOIN "+
	    	   " (SELECT "+
	    	      "  country_iso_code_2, country_name "+
	    	    " FROM "+
	    	      " cm.country_list "+
	    	   " GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 ";
	    	countryfilter+="and c.country_iso_code_3 in (:country) ";
	    	
	    	countryJoin2+=" JOIN (SELECT " + 
	    			" country_iso_code_2, country_name, country_iso_code_3 " + 
	    			" FROM " + 
	    			" cm.country_list " + 
	    			" GROUP BY country_iso_code_2) c ON a.iso_country = c.country_iso_code_2 ";
	    	countrySelected=true;
	    	
	    }
	    
	    if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry) && !Arrays.asList(country.split(",")).contains("All")){
	    	industryJoin1+="JOIN factset.sym_v1_sym_entity_sector d ON b.factset_portco_entity_id = d.factset_entity_id " + 
	    			" JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code ";
	    	
	    	industryjoin2+=" JOIN factset.sym_v1_sym_entity_sector d ON b.factset_portco_entity_id = d.factset_entity_id " + 
			" JOIN (SELECT " + 
			" tics_industry_code, ff_ind_code " + 
			" FROM " + 
			" cm.tics_industry_mapping " + 
			" GROUP BY ff_ind_code) e ON d.industry_code = e.ff_ind_code " + 
			" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " ;
	    	
	    	industryFilter+=" AND f.tics_industry_code in (:tics_industry_code) ";
	    }
	    baseQuery=baseQuery.replace("#countryJoin1", countryJoin1);
	    baseQuery=baseQuery.replace("#countryJoin2", countryJoin2);
	    baseQuery=baseQuery.replace("#countryilter", countryfilter);
	    baseQuery=baseQuery.replace("#countryilter", countryfilter);
	    baseQuery=baseQuery.replace("#industryJoin1", industryJoin1);
	    baseQuery=baseQuery.replace("#industryJoin2", industryjoin2);
	    baseQuery=baseQuery.replace("#industryFilter", industryFilter);
	    baseQuery=baseQuery.replace("#industryFilter", industryFilter);
	    Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PevcIssueType.class);
	    if(countrySelected) {
	    	query.setParameterList("country", Arrays.asList(country.split(",")));
	    }
	    if(industrySelected) {
	    	query.setParameterList("tics_industry_code", Arrays.asList(industry.split(",")));
	    }
	    
	    if(isMultiPleFintype) {
			 query.setParameterList("multipleFinType", multipleFinType);
		 }
	    @SuppressWarnings("unchecked")
		List<PevcIssueType> data = (List<PevcIssueType>) query.list();
		List<PevcIssueTypeDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PevcIssueTypeDTO.class);	
		return dcsDTOs;
		
	}

	@Override
	public AdvancedSearchFundingAmoutDto getPeVcfundingAmout(String country, String industry,
			String financingType, String issueType, String entityId,String currency, Date sDate, Date eDate) {
		boolean issueSelected=false;
		boolean countrySelected=false;
		boolean industrySelected=false;
		boolean companySelected=false;
		String countryJoin1="";
		String countryJoin2="";
		String industryJoin1="";
		String industryJoin2="";
		String issueFilter1="";
		String issueFilter2="";
		String countryFilter1="";
		String countryFilter2="";
		String industryFilter1="";
		String industryFilter2="";
		String companyFilter1="";
		String companyFilter2="";
		String baseQuery=null;
		boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<String>();
		if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelectedFxAmount;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCFxAmount;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePEAmount;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOTAmount;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPEAmount;
		    }
		    else {
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultipleAmount;
		    	
		    	
		    	isMultiPleFintype=true;
		    }
		  }
		
		if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
			
			if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType) || "VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType) || isMultiPleFintype) {
	    	countryJoin1+=" JOIN (SELECT " + 
					" country_iso_code_2, country_iso_code_3 " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country ";
	    	countryFilter1+="and c.country_iso_code_3 in (:country)";
	    	countryFilter2+="and c.country_iso_code_3 in (:country)";
	    	
	    	countryJoin2+=" JOIN (SELECT " + 
					" country_iso_code_2, country_iso_code_3 " + 
					" FROM " + 
					" cm.country_list " + 
					" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country ";
	    	countrySelected=true;
			}
			
			else {
		    	countryJoin1+=" JOIN (SELECT " + 
						" country_iso_code_2, country_iso_code_3 " + 
						" FROM " + 
						" cm.country_list " + 
						" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country ";
		    	countryFilter1+="and c.country_iso_code_3 in (:country)";
		    	countryFilter2+="and c.country_iso_code_3 in (:country)";
		    	
		    	countryJoin2+=" JOIN (SELECT " + 
						" country_iso_code_2, country_iso_code_3 " + 
						" FROM " + 
						" cm.country_list " + 
						" GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country ";
		    	countrySelected=true;
				
				
			}
	    	
	    }
	    
	    if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry) && !Arrays.asList(industry.split(",")).contains("All")){
	    	industryJoin1+="JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id " + 
	    			" JOIN (SELECT " + 
	    			" tics_industry_code, ff_ind_code " + 
	    			" FROM " + 
	    			" cm.tics_industry_mapping " + 
	    			" GROUP BY ff_ind_code) e ON d.industry_code = e.ff_ind_code " + 
	    			" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code ";
	    	industryFilter2+="AND f.tics_industry_code in (:tics_industry_code) ";
	    	industryFilter1+="AND f.tics_industry_code in (:tics_industry_code) ";
	    	industrySelected=true;
	    	
	    	industryJoin2+="JOIN factset.sym_v1_sym_entity_sector d ON b.factset_portco_entity_id = d.factset_entity_id " + 
	    			" JOIN (SELECT " + 
	    			" tics_industry_code, ff_ind_code " + 
	    			" FROM " + 
	    			" cm.tics_industry_mapping " + 
	    			" GROUP BY ff_ind_code) e ON d.industry_code = e.ff_ind_code " + 
	    			" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code ";
	    	}
	    
	    if(issueType!=null && !"All".equalsIgnoreCase(issueType) && !"".equals(issueType) && !"null".equals(issueType)  && !Arrays.asList(issueType.split(",")).contains("All")) {
			 issueFilter1+=" AND issue_type in (:issue_type)";
			 issueFilter2+=" AND issue_type in (:issue_type)";
			 issueSelected=true;
		 }
	    
	    if(entityId!=null && !"".equals(entityId) && !"null".equals(entityId)){
	    	companySelected=true;
	    	companyFilter1+=" AND b.factset_portco_entity_id in (:factset_portco_entity_id) ";
	    	companyFilter2+=" AND b.factset_portco_entity_id in (:factset_portco_entity_id) ";
	    }
		baseQuery=baseQuery.replace("#countryJoin1", countryJoin1);
		baseQuery=baseQuery.replace("#countryJoin2", countryJoin2);
		baseQuery=baseQuery.replace("#industryJoin1", industryJoin1);
		baseQuery=baseQuery.replace("#industryJoin2", industryJoin2);
		baseQuery=baseQuery.replace("#industryJoin2", industryJoin2);
		baseQuery=baseQuery.replace("#countryFilter1", countryFilter1);
		baseQuery=baseQuery.replace("#countryFilter2", countryFilter2);
		baseQuery=baseQuery.replace("#companyFilter1", companyFilter1);
		baseQuery=baseQuery.replace("#companyFilter2", companyFilter2);
		baseQuery=baseQuery.replace("#issueFilter2", issueFilter2);
		baseQuery=baseQuery.replace("#issueFilter1", issueFilter1);
		
		baseQuery=baseQuery.replace("#industryFilter1", industryFilter1);
		baseQuery=baseQuery.replace("#industryFilter2", industryFilter2);
		Query query=cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		if(companySelected) {
			query.setParameterList("factset_portco_entity_id", Arrays.asList(entityId.split(",")));
		}
		if(countrySelected) {
			query.setParameterList("country", Arrays.asList(country.split(",")));
		}
		if(industrySelected) {
			query.setParameterList("tics_industry_code", Arrays.asList(industry.split(",")));
		}
		if(issueSelected) {
			query.setParameterList("issue_type", Arrays.asList(issueType.split(",")));
		}
		 if(isMultiPleFintype) {
		 query.setParameterList("multi",Arrays.asList(financingType.split(",")));
		 }
		query.setParameter("currency", currency);
		query.setResultTransformer(Transformers.aliasToBean(AdvancedSearchFundingAmoutDto.class));
		return (AdvancedSearchFundingAmoutDto) query.uniqueResult();
	
	}

}
