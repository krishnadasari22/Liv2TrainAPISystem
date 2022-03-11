package com.privatecompany.dao.impl;

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

import com.privatecompany.dao.MNADaoC;
import com.privatecompany.dto.CompanyEpsDTO;
import com.privatecompany.dto.DealHistoryDTO;
import com.privatecompany.entities.CompanyEps;
import com.privatecompany.entities.DealHistory;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class MNAdaoImplC implements MNADaoC {

	Logger _log = Logger.getLogger(MNAdaoImplC.class);

	@Autowired
	@Qualifier(value = "factSetSessionFactory")
	private SessionFactory factsetSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<DealHistoryDTO> getAllDealHistory(String entityId, Date startDate, Date endDate, String currency) {

		String baseQuery;
		
		if(currency != null && !currency.isEmpty())
			baseQuery = "SELECT " + 
					"    i.deal_id, " + 
					"    '"+ currency +"' as deal_currency, " + 
					"    i.announce_date, " + 
					"    t.factset_entity_id as target_entity_id, " + 
					"    (select company_id from cm.company_list where factset_entity_id=t.factset_entity_id order by domicile_flag limit 1) as target_company_id, " + 
					"    s.entity_proper_name AS 'target', " + 
					"    (select co_role_desc from factset.ma_v1_ma_deal_relationship r join factset.ref_v2_ma_company_role_map co ON co.co_role_code = r.co_role_id " + 
					"    where r.factset_entity_id= :entityId  and r.deal_id=i.deal_id) as role, " + 
					"    ac.factset_entity_id as acquirer_entity_id, " + 
					"    (select company_id from cm.company_list where factset_entity_id=ac.factset_entity_id order by domicile_flag limit 1) as acquirer_company_id, " + 
					"    (select entity_proper_name from factset.sym_v1_sym_entity where factset_entity_id=ac.factset_entity_id) as 'acquirer', " + 
					"     se.factset_entity_id as seller_entity_id, " + 
					"    group_concat(concat((select entity_proper_name from factset.sym_v1_sym_entity where factset_entity_id=se.factset_entity_id),' (',co.co_role_desc,')') separator ' | ') AS 'seller', "+
					"    IFNULL(i.close_date, csm.status_desc) AS 'close_date/status', " + 
					"    t.transaction_value * factset.get_fx_daily_conversion(i.deal_currency,:currency,i.announce_date) as transaction_value, " + 
					"    dtm.deal_type_desc AS deal_type, " + 
					"    m.factset_industry_desc AS 'target_industry', " + 
					"    case when t.percent_sought < 0 then null else t.percent_sought end  AS 'percent_sought', " + 
					"    i.synopsis AS 'term&synopsis' ," + 
					" 'USD' as target_currency, "+
					"    t.mop, "+
                    " case when t.percent_owned <0 then null else t.percent_owned end  AS 'percent_Owned',"+
                    " c.source_funds_desc,"+
                    " t.ev * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) as ev, "+
                    " t.revenue_ltm_before_deal * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) AS revenue_ltm_before_deal, "+
                    " t.ebitda_ltm_before_deal * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) AS ebidta_ltm_before_deal,"+
                    " t.price_share * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) AS price_share, "+
                    " t.stock_price_share * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) AS stock_price_share, "+
                    " t.cash_price_share * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) AS cash_price_share, "+
                    " (t.one_day_prem *100) AS one_day_prem ,"+
                    " t.ev_rev AS ev_rev, "+
                    " t.ev_ebitda , "+
                    " (t.transaction_value - (t.cash_equiv * (t.percent_sought/100))) * factset.get_fx_daily_conversion(t.deal_currency, :currency, t.effect_date) AS cash_adjusted_deal_value, "+
                    " en.country_name as entity_country_name," + 
                    "   en.proper_name as entity_name," + 
                    "   en.tics_industry_name as entity_industry_name "+
					" FROM " + 
					"    factset.ma_v1_ma_deal_info i " + 
					"    left join (select deal_id, factset_entity_id from factset.ma_v1_ma_deal_relationship where co_role_id =3) ac on ac.deal_id = i.deal_id " + 
					"    left join (select deal_id, factset_entity_id,co_role_id from factset.ma_v1_ma_deal_relationship where co_role_id in (4,10,12,13)) se on se.deal_id = i.deal_id " + 
					"    JOIN factset.ma_v1_ma_deal_relationship r ON i.deal_id = r.deal_id " + 
					"    JOIN factset.ma_v1_ma_deal_terms t ON t.deal_id = r.deal_id " + 
					"    JOIN factset.sym_v1_sym_entity s ON s.factset_entity_id = t.factset_entity_id " + 
					"    JOIN factset.ent_v1_ent_entity_coverage ec ON s.factset_entity_id = ec.factset_entity_id " + 
					"    JOIN factset.ref_v2_factset_industry_map m ON ec.industry_code = m.factset_industry_code " + 
					"    JOIN factset.ref_v2_ma_closing_status_map csm ON csm.status_code = i.status_id " + 
					"    JOIN factset.ma_v1_ma_deal_types dt ON dt.deal_id = i.deal_id " + 
					"    JOIN factset.ref_v2_ma_deal_type_map dtm ON dtm.deal_type_code = dt.deal_type_id " + 
					"    left join factset.ref_v2_ma_company_role_map co ON co.co_role_code = se.co_role_id " + 
					" LEFT JOIN " + 
					"factset.ma_v1_ma_source_funds_info c ON t.deal_id = c.deal_id "+
					"left join (select * from (select cl.country_name,c.proper_name,ti.tics_industry_name,c.factset_entity_id from cm.company_list c join cm.country_list cl on "+
					"c.domicile_country_code=cl.country_iso_code_3 join cm.tics_industry ti on ti.tics_industry_code=c.tics_industry_code "+
					"order by domicile_flag desc )x group by factset_entity_id) en on en.factset_entity_id=r.factset_entity_id "+
					" WHERE r.factset_entity_id = :entityId and t.ver=1 and dt.primary_deal_type=1 group by i.deal_id " + 
					" ORDER BY announce_date DESC";
		else 
			baseQuery = "SELECT " + 
					"    i.deal_id, " + 
					"    i.deal_currency, " + 
					"    i.announce_date, " + 
					"    t.factset_entity_id as target_entity_id, " + 
					"    (select company_id from cm.company_list where factset_entity_id=t.factset_entity_id order by domicile_flag limit 1) as target_company_id, " + 
					"    s.entity_proper_name AS 'target', " + 
					"    (select co_role_desc from factset.ma_v1_ma_deal_relationship r join factset.ref_v2_ma_company_role_map co ON co.co_role_code = r.co_role_id " + 
					"    where r.factset_entity_id= :entityId  and r.deal_id=i.deal_id) as role, " + 
					"    ac.factset_entity_id as acquirer_entity_id, " + 
					"    (select company_id from cm.company_list where factset_entity_id=ac.factset_entity_id order by domicile_flag limit 1) as acquirer_company_id, " + 
					"    (select entity_proper_name from factset.sym_v1_sym_entity where factset_entity_id=ac.factset_entity_id) as 'acquirer', " + 
					"    se.factset_entity_id as seller_entity_id, " + 
					"    group_concat(concat((select entity_proper_name from factset.sym_v1_sym_entity where factset_entity_id=se.factset_entity_id),' (',co.co_role_desc,')') separator ' | ') AS 'seller',"+
					"    IFNULL(i.close_date, csm.status_desc) AS 'close_date/status', " + 
					"    t.transaction_value, " + 
					"    dtm.deal_type_desc AS deal_type, " + 
					"    m.factset_industry_desc AS 'target_industry', " + 
					"    case when t.percent_sought < 0 then null else t.percent_sought end  AS 'percent_sought', " + 
					"    i.synopsis AS 'term&synopsis', " + 
					" 'USD' as target_currency, "+
					"    t.mop, "+
                    "   case when t.percent_owned <0 then null else t.percent_owned end  AS 'percent_Owned',"+
                    "   c.source_funds_desc,"+
                    "   t.ev * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) as ev, "+
                    "   t.revenue_ltm_before_deal * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) AS revenue_ltm_before_deal, "+
                    "   t.ebitda_ltm_before_deal * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) AS ebidta_ltm_before_deal,"+
                    "   t.price_share * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) AS price_share, "+
                    "   t.stock_price_share * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) AS stock_price_share, "+
                    "   t.cash_price_share * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) AS cash_price_share, "+
                    "   (t.one_day_prem *100) AS one_day_prem, "+
                    "   t.ev_rev AS ev_rev, "+
                    "   t.ev_ebitda AS ev_ebitda, "+
                    " (t.transaction_value - (t.cash_equiv * (t.percent_sought/100))) * factset.get_fx_daily_conversion(t.deal_currency, 'USD', t.effect_date) AS cash_adjusted_deal_value , "+
                    " en.country_name as entity_country_name, "+
                    " en.proper_name as entity_name, "+
                   " en.tics_industry_name as entity_industry_name "+
					" FROM " + 
					"    factset.ma_v1_ma_deal_info i " + 
					"    left join (select deal_id, factset_entity_id from factset.ma_v1_ma_deal_relationship where co_role_id =3) ac on ac.deal_id = i.deal_id " + 
					"    left join (select deal_id, factset_entity_id,co_role_id from factset.ma_v1_ma_deal_relationship where co_role_id in (4,10,12,13)) se on se.deal_id = i.deal_id " + 
					"    JOIN factset.ma_v1_ma_deal_relationship r ON i.deal_id = r.deal_id " + 
					"    JOIN factset.ma_v1_ma_deal_terms t ON t.deal_id = r.deal_id " + 
					"    JOIN factset.sym_v1_sym_entity s ON s.factset_entity_id = t.factset_entity_id " + 
					"    JOIN factset.ent_v1_ent_entity_coverage ec ON s.factset_entity_id = ec.factset_entity_id " + 
					"    JOIN factset.ref_v2_factset_industry_map m ON ec.industry_code = m.factset_industry_code " + 
					"    JOIN factset.ref_v2_ma_closing_status_map csm ON csm.status_code = i.status_id " + 
					"    JOIN factset.ma_v1_ma_deal_types dt ON dt.deal_id = i.deal_id " + 
					"    JOIN factset.ref_v2_ma_deal_type_map dtm ON dtm.deal_type_code = dt.deal_type_id " + 
					"    left join factset.ref_v2_ma_company_role_map co ON co.co_role_code = se.co_role_id " + 
					" LEFT JOIN " + 
					"factset.ma_v1_ma_source_funds_info c ON t.deal_id = c.deal_id "+
					"left join (select * from (select cl.country_name,c.proper_name,ti.tics_industry_name,c.factset_entity_id from cm.company_list c join cm.country_list cl on "+
					"c.domicile_country_code=cl.country_iso_code_3 join cm.tics_industry ti on ti.tics_industry_code=c.tics_industry_code "+
					"order by domicile_flag desc )x group by factset_entity_id) en on en.factset_entity_id=r.factset_entity_id "+
					" WHERE r.factset_entity_id = :entityId and t.ver=1 and dt.primary_deal_type=1 group by i.deal_id " + 
					" ORDER BY announce_date DESC";
		
		
		Query query = factsetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DealHistory.class);
		query.setParameter("entityId", entityId);
		
		if(currency != null && !currency.isEmpty())
			query.setParameter("currency", currency);
		
		return DozerHelper.map(dozerBeanMapper, query.list(), DealHistoryDTO.class);
	}

	@Override
	public List<CompanyEpsDTO> getEpsData(String companyId, String periodicity, Date startDate, Date endDate,
			String currency) {

		String baseQuery = "select * from (select  fsym_id,currency,date,ff_eps_basic from factset.ff_v3_ff_basic_ltm where fsym_id=:companyId "
				+ "and date between :startDate and :endDate union select  fsym_id,currency,date,ff_eps_basic "
				+ "from factset.ff_v3_ff_basic_ltm where fsym_id=:companyId and date between :startDate and :endDate UNION select  "
				+ "fsym_id,currency,date,ff_eps_basic from factset.ff_v3_ff_basic_ltm where fsym_id=:companyId and "
				+ "date between :startDate and :endDate)x " + "group by date order by date desc  ";

		Query query = factsetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CompanyEps.class);
		query.setParameter("companyId", companyId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		return DozerHelper.map(dozerBeanMapper, query.list(), CompanyEpsDTO.class);
	}

}
