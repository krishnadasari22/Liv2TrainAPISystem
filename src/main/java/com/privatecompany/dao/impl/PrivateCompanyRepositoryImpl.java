package com.privatecompany.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.privatecompany.dao.PrivateCompanyRepository;
import com.privatecompany.dto.PrivateCompanyDTO;
import com.privatecompany.entities.PrivateCompany;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;
import com.televisory.capitalmarket.entities.factset.FFLatestFilingInfo;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class PrivateCompanyRepositoryImpl implements PrivateCompanyRepository{

	@Autowired
	@Qualifier(value="pcDataSessionFactory")
	private SessionFactory pcDataSessionFactory;
	

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public PrivateCompanyDTO getBasicInfo(String entityId) {
	
		StringBuilder sb=new StringBuilder();
		sb.append("SELECT c.entity_id AS entity_id,c.name AS name,c.tics_industry_code AS tics_industry_code,tics_industry_name,");
		sb.append(" ts.tics_sector_code,ts.tics_sector_name,c.domicile_country_code AS domicile_country_code,");
		sb.append(" cl.country_name AS domicile_country_name,f.date AS date,fpc_year_founded AS year_founded,");
		sb.append(" fpc_website AS website,fpc_hq_addr_phone AS contact_no,fpc_email AS email,fpc_hq_addr_street_1,");
		sb.append(" fpc_hq_addr_street_2,fpc_hq_addr_street_3,fpc_hq_addr_street_4,fpc_hq_addr_street_5,");
		sb.append(" fpc_hq_addr_city,");
		sb.append("  fpc_hq_addr_state_name,fpc_hq_addr_postal_code,fpc_hq_addr_country_name,fpc_emp_num AS tot_emp,");
		sb.append(" fpc_crunchbase_rank AS crunchbase_rank,c.reporting_currency AS reporting_currency,");
		sb.append("  ep.entity_profile AS detailed_description");
		sb.append(" FROM pc.pc_company_list c");
		sb.append(" LEFT JOIN (SELECT * FROM pc.pc_snapshot_data_af");
		sb.append(" WHERE entity_id = :entityId) s ON c.entity_id = s.entity_id");
		sb.append(" LEFT JOIN (SELECT * FROM pc.pc_factset_data");
		sb.append(" WHERE entity_id = :entityId) f ON c.entity_id = f.entity_id");
		sb.append(" LEFT JOIN cm.country_list cl ON cl.country_iso_code_3 = c.domicile_country_code");
		sb.append(" LEFT JOIN cm.tics_industry t ON t.tics_industry_code = c.tics_industry_code");
		sb.append(" LEFT JOIN cm.tics_sector ts ON ts.tics_sector_code = t.tics_sector_code");
		sb.append(" LEFT JOIN (SELECT factset_entity_id, entity_profile");
		sb.append(" FROM factset.ent_v1_ent_entity_profiles");
		sb.append(" WHERE factset_entity_id = :entityId AND ");
		sb.append(" entity_profile_type = 'PRO') ep ON c.entity_id =  ep.factset_entity_id");
		sb.append(" WHERE c.entity_id = :entityId ORDER BY date DESC LIMIT 1");
		Query query = pcDataSessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(PrivateCompany.class);
		query.setParameter("entityId", entityId);

		
		@SuppressWarnings("unchecked")
		List<PrivateCompany> data = (List<PrivateCompany>) query.list();
		//System.out.println(data.size());

		List<PrivateCompanyDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PrivateCompanyDTO.class);	

		if (dcsDTOs.size()>0) {
			return dcsDTOs.get(0);
		}
		return null;
	}
	
	@Override
	public List<CompanyLatestFilingInfoDTO> getCompanyLatestFilingInfo(String entityId) {
		System.out.println(entityId);
		
		String baseQuery = "SELECT 'Yearly' as period_type, date, currency, entity_id as fsym_id, null as ff_curn_doc FROM pc_latest_data_af WHERE entity_id = :entityId order by date desc limit 1";
				 
		Query query = pcDataSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFLatestFilingInfo.class);
		query.setParameter("entityId",entityId);

		List<FFLatestFilingInfo> latestFilingInfo = null;
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs=null;

		try {		
			latestFilingInfo = query.list();	
			companyLatestFilingInfoDTOs = DozerHelper.map(dozerBeanMapper, latestFilingInfo, CompanyLatestFilingInfoDTO.class);

		} catch (Exception e) {
			throw e;
		}
		return companyLatestFilingInfoDTOs;
	}
	
	
}
