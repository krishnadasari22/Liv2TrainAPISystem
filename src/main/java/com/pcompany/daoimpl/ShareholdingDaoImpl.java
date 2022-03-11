package com.pcompany.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dao.ShareholdingDao;
import com.pcompany.dto.InstitutionalShareholdingOwnershipDetailsDTO;
import com.pcompany.dto.ShareholdingOwnershipDetailsDTO;
import com.pcompany.entity.InstituitionalShareholdingOwnershipDetails;
import com.pcompany.entity.ShareholdingOwnershipDetails;
import com.televisory.capitalmarket.util.DozerHelper;
import com.televisory.utils.ShareholdingQueries;

@Repository
@Transactional
public class ShareholdingDaoImpl implements ShareholdingDao{
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	Logger _log = Logger.getLogger(ShareholdingDaoImpl.class);
	
	@Override
	public List<ShareholdingOwnershipDetailsDTO> getInsiderDetailsRecent(String securityId) {
		String baseQuery= "select (@id\\:=@id+1) as id, fsym_id,factset_entity_id,as_on_date,entity_name,report_date,total_shares,pct_os,position,entity_type from "
				+ " (select hld.fsym_id as fsym_id,hld.factset_entity_id as factset_entity_id, "
				+ " case when sp.price_date=(SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices " 
				+ " WHERE  PRICE_DATE BETWEEN DATE_SUB(CURDATE(), INTERVAL 2 YEAR) AND CURDATE()) then CURDATE() " 
				+ " else date_add(sp.price_date,interval 1 month) end AS as_on_date,"
				+ " e.entity_proper_name as entity_name, hld.report_date as report_date,sp.adj_shares_outstanding as total_shares, (hld.position / sp.adj_shares_outstanding) * 100 as pct_os, "
				+ " hld.position as position, em.entity_type_desc as entity_type from own_v5_own_stakes_detail hld "
				+ " join (select max(d.report_date) max_report_date, d.factset_entity_id, d.fsym_id "
				+ " from own_v5_own_stakes_detail d "
				+ " where d.report_date BETWEEN DATE_SUB((SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices WHERE fsym_id=:fsymid), INTERVAL 2 YEAR) AND  DATE_Add((SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices WHERE fsym_id=:fsymid), INTERVAL 1 month) and d.fsym_id=:fsymid "
				+ " group by d.factset_entity_id, d.fsym_id ) md on md.factset_entity_id = hld.factset_entity_id and md.fsym_id = hld.fsym_id and md.max_report_date = hld.report_date "
				+ " join sym_v1_sym_entity e on e.factset_entity_id = hld.factset_entity_id "
				+ " join own_v5_own_sec_prices sp on sp.fsym_id = hld.fsym_id and sp.price_date =( SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices WHERE fsym_id=:fsymid ) "
				+ " join ref_v2_entity_type_map em on em.entity_type_code = e.entity_type where hld.fsym_id = :fsymid order by hld.position desc) x join (select @id\\:=0) a";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ShareholdingOwnershipDetails.class);
		query.setParameter("fsymid", securityId);
		@SuppressWarnings("unchecked")
		List<ShareholdingOwnershipDetails> data = (List<ShareholdingOwnershipDetails>) query.list();
		List<ShareholdingOwnershipDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, ShareholdingOwnershipDetailsDTO.class);	
		return dcsDTOs;
	}
	
	@Override
	public List<ShareholdingOwnershipDetailsDTO> getInsiderDetails(String securityId) {
		String baseQuery= "select (@id\\:=@id+1) as id, fsym_id,factset_entity_id,as_on_date,entity_name,report_date,total_shares,pct_os,position,entity_type from "
				+ " (select hld.fsym_id as fsym_id,hld.factset_entity_id as factset_entity_id, "
				+ " case when sp.price_date=(SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices " 
				+ " WHERE  PRICE_DATE BETWEEN DATE_SUB(CURDATE(), INTERVAL 2 YEAR) AND CURDATE()) then CURDATE() " 
				+ " else date_add(sp.price_date,interval 1 month) end AS as_on_date,"
				+ " e.entity_proper_name as entity_name, hld.report_date as report_date,sp.adj_shares_outstanding as total_shares, (hld.position / sp.adj_shares_outstanding) * 100 as pct_os,  "
				+ " hld.position as position, em.entity_type_desc as entity_type from own_v5_own_stakes_detail hld "
				+ " join (select max(d.report_date) max_report_date, d.factset_entity_id, d.fsym_id "
				+ " from own_v5_own_stakes_detail d "
				+ " where d.report_date BETWEEN DATE_SUB((SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices WHERE fsym_id=:fsymid), INTERVAL 2 YEAR) AND  DATE_Add((SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices WHERE fsym_id=:fsymid), INTERVAL 1 month) and d.fsym_id=:fsymid "
				+ " group by d.factset_entity_id, d.fsym_id ) md on md.factset_entity_id = hld.factset_entity_id and md.fsym_id = hld.fsym_id and md.max_report_date = hld.report_date "
				+ " join sym_v1_sym_entity e on e.factset_entity_id = hld.factset_entity_id "
				+ " join own_v5_own_sec_prices sp on sp.fsym_id = hld.fsym_id and sp.price_date =( SELECT MAX(PRICE_DATE) FROM factset.own_v5_own_sec_prices WHERE fsym_id=:fsymid ) "
				+ " join ref_v2_entity_type_map em on em.entity_type_code = e.entity_type where hld.fsym_id = :fsymid order by hld.position desc) x join (select @id\\:=0) a";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ShareholdingOwnershipDetails.class);
		query.setParameter("fsymid", securityId);
		@SuppressWarnings("unchecked")
		List<ShareholdingOwnershipDetails> data = (List<ShareholdingOwnershipDetails>) query.list();
		List<ShareholdingOwnershipDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, ShareholdingOwnershipDetailsDTO.class);	
		return dcsDTOs;
	}
	
	@Override
	public List<InstitutionalShareholdingOwnershipDetailsDTO> getInstitutionalOwnershipDetails(String securityId) {
		_log.info(securityId);
		String baseQuery=ShareholdingQueries.instituitionalQuery;
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(InstituitionalShareholdingOwnershipDetails.class);
		query.setParameter("fsymid", securityId);
		@SuppressWarnings("unchecked")
		List<InstituitionalShareholdingOwnershipDetails> data = (List<InstituitionalShareholdingOwnershipDetails>) query.list();
		List<InstitutionalShareholdingOwnershipDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, InstitutionalShareholdingOwnershipDetailsDTO.class);	
		return dcsDTOs;
	}
	
	@Override
	public List<InstitutionalShareholdingOwnershipDetailsDTO> getInstitutionalOwnershipRecent(String securityId) {
		String baseQuery=ShareholdingQueries.instituitionalQuery;
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(InstituitionalShareholdingOwnershipDetails.class);
		query.setParameter("fsymid", securityId);
		@SuppressWarnings("unchecked")
		List<InstituitionalShareholdingOwnershipDetails> data = (List<InstituitionalShareholdingOwnershipDetails>) query.list();
		List<InstitutionalShareholdingOwnershipDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, InstitutionalShareholdingOwnershipDetailsDTO.class);	
		return dcsDTOs;
	}
	

}