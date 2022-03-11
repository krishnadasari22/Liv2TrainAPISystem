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

import com.pcompany.dao.MNADaoD;
import com.pcompany.dto.MNABalanceModelDTO;
import com.pcompany.dto.MNADealAdvisorDTO;
import com.pcompany.dto.MNADealMetaDataDTO;
import com.pcompany.dto.MNADealTermDTO;
import com.pcompany.entity.MNABalanceModel;
import com.pcompany.entity.MNADealAdvisor;
import com.pcompany.entity.MNADealMetaData;
import com.pcompany.entity.MNADealTerm;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class MNADaoDImpl implements MNADaoD {
	
	Logger _log = Logger.getLogger(MNADaoDImpl.class);
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Override
	public String getSynopsis(Integer dealId) {
		_log.info("getting Synopsis for dealId: "+dealId);
		String baseQuery = "SELECT synopsis FROM factset.ma_v1_ma_deal_info WHERE deal_id = :dealId";
		 
			Query query = cmFactory.getCurrentSession().createSQLQuery(baseQuery);
			
			query.setParameter("dealId", dealId);	
			
			@SuppressWarnings("unchecked")
			String data = (String) query.uniqueResult();
			if(data != null){
				return data;	
			}else{
				return null; 
			}
	}
	
	@Override
	public List<MNABalanceModelDTO> getMNABalanceModel() {
		
		_log.info("Getting MNA Balance Model");
		
		String baseQuery =  "select * from cm.mna_balance_model";
			
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(MNABalanceModel.class);
		
		@SuppressWarnings("unchecked")
		List<MNABalanceModel> data = (List<MNABalanceModel>) query.list();
		List<MNABalanceModelDTO> balanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, MNABalanceModelDTO.class);
		
		return balanceModelDTOs;
	}
	
	@Override
	public MNADealMetaDataDTO getDealMetaData(Integer dealId, String entityId) {
		
		_log.info("Getting Deal Meta Data for dealId: "+ dealId +", entity ID: "+ entityId);
		
		String baseQuery =  "select i.deal_id as deal_id, "+
				"(select name from cm.company_list "+
				"where factset_entity_id=:factset_entity_id order by domicile_flag desc limit 1) as company, "+
				"(select co_role_desc from factset.ma_v1_ma_deal_relationship r join factset.ref_v2_ma_company_role_map co ON co.co_role_code = r.co_role_id "+
				"where r.factset_entity_id=:factset_entity_id1  and r.deal_id=i.deal_id) as role ,"+
				"ac.factset_entity_id as acquirer_entity_id,"+
				"(select entity_proper_name from factset.sym_v1_sym_entity where factset_entity_id=ac.factset_entity_id) as acquirer ,"+
				"t.factset_entity_id as target_entity_id, "+
				 " s.entity_proper_name as target, "+
				 "group_concat(se.factset_entity_id separator ' | ' ) as seller_entity_id, "+
				 " group_concat(concat((select entity_proper_name from factset.sym_v1_sym_entity where factset_entity_id=se.factset_entity_id),' (',co.co_role_desc,')') separator ' | ') AS 'seller',"+
				  "  i.announce_date,"+
				   "i.close_date, csm.status_desc as status "+
				" FROM "+
				   " factset.ma_v1_ma_deal_info i "+ 
				   " left  join (select deal_id, factset_entity_id from factset.ma_v1_ma_deal_relationship where co_role_id =3) ac on ac.deal_id = i.deal_id "+
				    "left  join (select deal_id, factset_entity_id ,co_role_id  from factset.ma_v1_ma_deal_relationship where co_role_id in (4,10,12,13)) se on se.deal_id = i.deal_id "+
				    " JOIN "+
				    "factset.ma_v1_ma_deal_relationship r ON i.deal_id = r.deal_id "+
				       " JOIN "+
				   "factset.ma_v1_ma_deal_terms t ON t.deal_id = r.deal_id "+
				       " JOIN "+
				   " factset.sym_v1_sym_entity s ON s.factset_entity_id = t.factset_entity_id "+
				    " INNER JOIN "+
				    " factset.ref_v2_ma_closing_status_map csm ON csm.status_code = i.status_id "+
				   " left join factset.ref_v2_ma_company_role_map co ON co.co_role_code = se.co_role_id "+
				   " WHERE "+
				   " r.factset_entity_id=:factset_entity_id2 and i.deal_id=:deal_id and t.ver=1 ";
		
			
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(MNADealMetaData.class);
		
		query.setParameter("deal_id", dealId);	
		query.setParameter("factset_entity_id", entityId);	
		query.setParameter("factset_entity_id1", entityId);	
		query.setParameter("factset_entity_id2", entityId);	
		
		@SuppressWarnings("unchecked")
		List<MNADealMetaData> data = (List<MNADealMetaData>) query.list();
		List<MNADealMetaDataDTO> dealMetaDataDTOs = DozerHelper.map(dozerBeanMapper, data, MNADealMetaDataDTO.class);
		
		if(dealMetaDataDTOs != null && dealMetaDataDTOs.size() > 0)
			return dealMetaDataDTOs.get(0);
		else
			return null;
		
		
	}
	
	
	@Override
	public List<MNADealAdvisorDTO> getDealAdvisor(Integer dealId, String currency) {
		
		_log.info("getting Deal Advisor for dealId: "+ dealId +", currency: "+ currency);
		
		String baseQuery;
		if(currency != null && !currency.isEmpty()) {
			baseQuery =  "SELECT a.deal_id, " + 
					"    b.entity_name AS advisor_name, " + 
					"    c.prof_actas_desc AS advisor_role, " + 
					"    i.entity_name AS client_name, " + 
					"    (select co_role_desc from   factset.ref_v2_ma_company_role_map where co_role_code = d.co_role_id) AS client_role, " + 
					"    g.status_desc AS deal_status, " + 
					"    a.fee * factset.get_fx_daily_conversion(a.deal_currency, :currency, f.announce_date) AS fee, " + 
					"    a.comment, " + 
					"    '"+ currency +"' as currency " + 
					"FROM factset.ma_v1_ma_profession a " + 
					"JOIN factset.ent_v1_ent_entity_coverage b ON a.factset_advisor_entity_id = b.factset_entity_id " + 
					"JOIN factset.ent_v1_ent_entity_coverage i ON a.factset_client_entity_id = i.factset_entity_id " + 
					"JOIN factset.ref_v2_ma_profession_role_map c ON a.prof_actas_id = c.prof_actas_code " + 
					"JOIN factset.ma_v1_ma_deal_relationship d ON d.deal_id = a.deal_id and a.factset_client_entity_id=d.factset_entity_id " + 
					"JOIN factset.ma_v1_ma_deal_info f ON f.deal_id = a.deal_id " + 
					"JOIN factset.ref_v2_ma_closing_status_map g ON f.status_id = g.status_code " + 
					"WHERE a.deal_id = :dealId";
		} else {
			baseQuery =  "SELECT a.deal_id, " + 
					"    b.entity_name AS advisor_name, " + 
					"    c.prof_actas_desc AS advisor_role, " + 
					"    i.entity_name AS client_name, " + 
					"    (select co_role_desc from   factset.ref_v2_ma_company_role_map where co_role_code = d.co_role_id) AS client_role, " + 
					"    g.status_desc AS deal_status, " + 
					"    a.fee AS fee, " + 
					"    a.comment, " + 
					"    a.deal_currency as currency " + 
					"FROM factset.ma_v1_ma_profession a " + 
					"JOIN factset.ent_v1_ent_entity_coverage b ON a.factset_advisor_entity_id = b.factset_entity_id " + 
					"JOIN factset.ent_v1_ent_entity_coverage i ON a.factset_client_entity_id = i.factset_entity_id " + 
					"JOIN factset.ref_v2_ma_profession_role_map c ON a.prof_actas_id = c.prof_actas_code " + 
					"JOIN factset.ma_v1_ma_deal_relationship d ON d.deal_id = a.deal_id and a.factset_client_entity_id=d.factset_entity_id " + 
					"JOIN factset.ma_v1_ma_deal_info f ON f.deal_id = a.deal_id " + 
					"JOIN factset.ref_v2_ma_closing_status_map g ON f.status_id = g.status_code " + 
					"WHERE a.deal_id = :dealId";
		}
			
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(MNADealAdvisor.class);
		
		query.setParameter("dealId", dealId);	
		if(currency != null && !currency.isEmpty()) {
			query.setParameter("currency", currency);	
		}
		@SuppressWarnings("unchecked")
		List<MNADealAdvisor> data = (List<MNADealAdvisor>) query.list();
		List<MNADealAdvisorDTO> advisorDTOs = DozerHelper.map(dozerBeanMapper, data, MNADealAdvisorDTO.class);
		
		return advisorDTOs;
	}
	
	@Override
	public List<MNADealTermDTO> getDealTerms(Integer dealId, String currency) {
		
	_log.info("getting Deal Terms for dealId: "+ dealId +", currency: "+ currency);
		
		String baseQuery;
		if(currency != null && !currency.isEmpty()) {
			baseQuery =  "SELECT " + 
					"    a.ver, " + 
					"    a.effect_date, " + 
					"    b.entity_name AS target, " + 
					"    a.mop, " + 
					"    c.source_funds_desc, " + 
					"    a.cash * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) as cash, " + 
					"    a.stock * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) as stock, " + 
					"    case when a.percent_sought < 0 then null else a.percent_sought end AS 'percent_sought', "+
					"    case when a.percent_owned <0 then null else a.percent_owned end  AS 'percent_owned', " + 
					"    a.transaction_value * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS transaction_value, " + 
					"    a.ev * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) as ev, " + 
					"    a.revenue_ltm_before_deal * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS revenue_ltm_before_deal, " + 
					"    a.ebitda_ltm_before_deal * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS ebitda_ltm_before_deal, " + 
					"    a.price_share * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS price_share, " + 
					"    a.stock_price_share * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS stock_price_share, " + 
					"    a.cash_price_share * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS cash_price_share, " + 
					"    (a.one_day_prem *100) * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS one_day_prem, " + 
					"    a.ev_rev AS ev_rev, " + 
					"    a.ev_ebitda AS ev_ebitda, " + 
					"    (a.transaction_value - (a.cash_equiv * (a.percent_sought/100))) * factset.get_fx_daily_conversion(a.deal_currency, :currency, a.effect_date) AS cash_adjusted_deal_value, " + 
					"    d.term_change_desc AS term_change_desc " + 
					" FROM factset.ma_v1_ma_deal_terms a " + 
					" LEFT JOIN factset.ent_v1_ent_entity_coverage b ON a.factset_entity_id = b.factset_entity_id " + 
					" LEFT JOIN factset.ma_v1_ma_source_funds_info c ON a.deal_id = c.deal_id " + 
					" LEFT JOIN factset.ref_v2_ma_term_change_types_map d ON a.term_change_code = d.term_change_code " + 
					" WHERE a.deal_id = :dealId group by a.ver ";
		} else {
			baseQuery =  "SELECT " + 
					"    a.ver, " + 
					"    a.effect_date, " + 
					"    b.entity_name AS target, " + 
					"    a.mop, " + 
					"    c.source_funds_desc, " + 
					"    a.cash as cash, " + 
					"    a.stock as stock, " + 
					"    case when a.percent_sought < 0 then null else a.percent_sought end AS 'percent_sought', "+
					"    case when a.percent_owned <0 then null else a.percent_owned end  AS 'percent_owned', " + 
					"    a.transaction_value AS transaction_value, " + 
					"    a.ev as ev, " + 
					"    a.revenue_ltm_before_deal AS revenue_ltm_before_deal, " + 
					"    a.ebitda_ltm_before_deal AS ebitda_ltm_before_deal, " + 
					"    a.price_share AS price_share, " + 
					"    a.stock_price_share AS stock_price_share, " + 
					"    a.cash_price_share AS cash_price_share, " + 
					"    (a.one_day_prem *100) AS one_day_prem, " + 
					"    a.ev_rev AS ev_rev, " + 
					"    a.ev_ebitda AS ev_ebitda, " + 
					"    (a.transaction_value - (a.cash_equiv * (a.percent_sought/100))) AS cash_adjusted_deal_value, " + 
					"    d.term_change_desc AS term_change_desc " + 
					" FROM factset.ma_v1_ma_deal_terms a " + 
					" LEFT JOIN factset.ent_v1_ent_entity_coverage b ON a.factset_entity_id = b.factset_entity_id " + 
					" LEFT JOIN factset.ma_v1_ma_source_funds_info c ON a.deal_id = c.deal_id " + 
					" LEFT JOIN factset.ref_v2_ma_term_change_types_map d ON a.term_change_code = d.term_change_code " + 
					" WHERE a.deal_id = :dealId group by a.ver;";
		}
			
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(MNADealTerm.class);
		
		query.setParameter("dealId", dealId);	
		if(currency != null && !currency.isEmpty()) {
			query.setParameter("currency", currency);	
		}
		@SuppressWarnings("unchecked")
		List<MNADealTerm> data = (List<MNADealTerm>) query.list();
		List<MNADealTermDTO> dealTermDTOs = DozerHelper.map(dozerBeanMapper, data, MNADealTermDTO.class);
		
		return dealTermDTOs;
	}
}
