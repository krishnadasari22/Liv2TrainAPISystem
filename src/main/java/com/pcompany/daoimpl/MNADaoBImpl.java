package com.pcompany.daoimpl;

import java.math.BigInteger;
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

import com.pcompany.dao.MNADaoB;
import com.pcompany.dto.MNATopDealMakerDTO;
import com.pcompany.dto.MNATopDealMakerTotal;
import com.pcompany.dto.MNATopDealMakerTotalDTO;
import com.pcompany.entity.MNATopDealMaker;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class MNADaoBImpl implements MNADaoB {
	
	Logger _log = Logger.getLogger(MNADaoBImpl.class);
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<MNATopDealMakerDTO> getTopDealMakerList(String country,
			String industry, String currency, Date startDate, Date endDate,
			Integer rowOffset, Integer rowCount, String sortingColumn,
			String sortingType) {
		_log.info("getting top deal maker list for country:"+country+" industry:"+industry+" startDate:"+startDate+" endDate:"+endDate+" currency:"+currency+" rowOffset:"+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
		String baseQuery = "SELECT (@id\\:=@id + 1) AS rank, z.* FROM (select * from (select country_code,country,industry_code,industry_name,sector_code,sector_name,"
			+"x.factset_entity_id,"
            +"name,"
            +"company_id,"
            +"total_deals,"
            +"transact_tot_value,"
            +"transact_avg_value,"
            +"transact_max_value,deal_currency,currency,unit FROM "
            +"(SELECT country_code,country,industry_code,industry_name,sector_code,sector_name,"
            +"factset_entity_id,"
            +"COUNT(deal_id) AS total_deals,"
            +"SUM(transaction_value) AS transact_tot_value,"
            +"AVG(transaction_value) AS transact_avg_value,"
            +"MAX(transaction_value) AS transact_max_value,"
            +"deal_currency,"
            +"currency,"
            +"unit FROM (SELECT "
            +"c.domicile_country_code as country_code,cl.country_name as country,ti.tics_industry_code as industry_code,ti.tics_industry_name as industry_name,s.tics_sector_code as sector_code,s.tics_sector_name as sector_name,"
            +"r.factset_entity_id,"
            +"r.deal_id AS deal_id,"
            +"transaction_value * factset.get_fx_daily_conversion(t.deal_currency, :currency, announce_date) AS transaction_value,"
            //+"count(distinct r.deal_id) AS total_deals,"
            //+"sum(transaction_value * factset.get_fx_daily_conversion(t.deal_currency,:currency,announce_date)) AS transact_tot_value,"
            //+"avg(transaction_value * factset.get_fx_daily_conversion(t.deal_currency,:currency,announce_date)) AS transact_avg_value,"
            //+"max(transaction_value * factset.get_fx_daily_conversion(t.deal_currency,:currency,announce_date)) AS transact_max_value,"
            +"t.deal_currency,"
            +":currency as currency,"
            +"'Million' as unit FROM factset.ma_v1_ma_deal_info i join "
            +"factset.ma_v1_ma_deal_relationship r  on i.deal_id=r.deal_id "
            +"join factset.ma_v1_ma_deal_terms t on t.deal_id=r.deal_id "
            +"left join cm.company_list c on r.factset_entity_id=c.factset_entity_id "
            +"join cm.tics_industry ti ON ti.tics_industry_code = c.tics_industry_code "  
            +"join cm.tics_sector s on s.tics_sector_code=ti.tics_sector_code "
            +"join cm.country_list cl on cl.country_iso_code_3=c.domicile_country_code "
            +"where co_role_id in (3,5,8,11,2,4,10,12,13) and announce_date between :startDate and :endDate ";
            boolean selectCountry = false;
            boolean selectIndustry = false;
		    if(country!=null && !"World".equals(country) && !"".equals(country)){
		    	baseQuery+="and c.domicile_country_code=:country ";
		    	selectCountry = true;
		    }
		    
		    if(industry!=null && !"All".equals(industry) && !"".equals(industry)){
		    	baseQuery+="and c.tics_industry_code=:industry ";
		    	selectIndustry = true;
		    }
		    
		    baseQuery+= "AND i.status_id IN (1 , 2) GROUP BY i.deal_id,factset_entity_id) w GROUP BY factset_entity_id) x left join cm.company_list c on x.factset_entity_id=c.factset_entity_id order by domicile_flag desc)y group by factset_entity_id "
		    		+ "order by ";
		    
		    baseQuery+=sortingColumn+" "+sortingType;
		    
		    if("total_deals".equalsIgnoreCase(sortingColumn)){
		    	baseQuery+=", transact_tot_value "+sortingType;
		    }
		    
		    baseQuery+=" )z  JOIN (SELECT @id\\:=:rowOffset) AS ai LIMIT :rowOffset , :rowCount";
            
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(MNATopDealMaker.class);
		if(selectCountry){
			query.setParameter("country", country);	
		}
		if(selectIndustry){
			query.setParameter("industry", industry);	
		}
		query.setParameter("currency", currency);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		query.setParameter("rowOffset", rowOffset);
		query.setParameter("rowCount", rowCount);
		//query.setParameter("sortingColumn", sortingColumn);
		//query.setParameter("sortingType", sortingType);
		@SuppressWarnings("unchecked")
		List<MNATopDealMaker> data = (List<MNATopDealMaker>) query.list();
		List<MNATopDealMakerDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, MNATopDealMakerDTO.class);	
		return dcsDTOs;
	}

	@Override
	public Long getTopDealMakerCount(String country, String industry,
			String currency, Date startDate, Date endDate) {
		_log.info("getting top deals count for country:"+country+" industry:"+industry+" startDate:"+startDate+" endDate:"+endDate);
		 String baseQuery = "SELECT COUNT(*) FROM (select * from (SELECT x.factset_entity_id "
						    +"FROM ( SELECT * FROM (SELECT r.factset_entity_id "
						    +"FROM factset.ma_v1_ma_deal_info i "
						    +"JOIN factset.ma_v1_ma_deal_relationship r ON i.deal_id = r.deal_id "
						    +"JOIN factset.ma_v1_ma_deal_terms t ON t.deal_id = r.deal_id "
						    +"LEFT JOIN cm.company_list c ON r.factset_entity_id = c.factset_entity_id "
						    +"WHERE co_role_id IN (3 , 5, 8, 11, 2, 4, 10, 12, 13) "
							+"AND announce_date BETWEEN :startDate AND :endDate ";
							
	 	boolean selectCountry = false;
	 	boolean selectIndustry = false;
	    if(country!=null && !"World".equals(country) && !"".equals(country)){
		    	baseQuery+="AND c.domicile_country_code = :country ";
		    	selectCountry = true;
		    }
		    
	    if(industry!=null && !"All".equals(industry) && !"".equals(industry)){
		    	baseQuery+="AND c.tics_industry_code = :industry ";
		    	selectIndustry = true;
		    }
						    
							baseQuery+="AND i.status_id IN (1 , 2) GROUP BY i.deal_id , factset_entity_id)w GROUP BY factset_entity_id) x "
						    +"LEFT JOIN cm.company_list c ON x.factset_entity_id = c.factset_entity_id "
						    +"ORDER BY domicile_flag DESC)y group by factset_entity_id LIMIT 0 , 100) z";
		 
			Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			
			if(selectCountry){
				query.setParameter("country", country);	
			}
			if(selectIndustry){
				query.setParameter("industry", industry);	
			}
			
			query.setDate("startDate", startDate);
			query.setDate("endDate", endDate);
			@SuppressWarnings("unchecked")
			BigInteger data = (BigInteger) query.uniqueResult();
			if(data != null){
				return data.longValue();	
			}else{
				return null; 
			}
	}
	
	@Override
	public MNATopDealMakerTotalDTO getTopDealMakerTotal(String country, String industry,
			String currency, Date startDate, Date endDate) {
		_log.info("getting top deals total for country:"+country+" industry:"+industry+" startDate:"+startDate+" endDate:"+endDate);
		String baseQuery =  "SELECT (@id\\:=@id + 1) AS id,"
						    +"COUNT(*) AS total_rows,"
						    +"SUM(y.total_deals) AS total_deals,"
						    +"SUM(y.transact_tot_value) AS transact_tot_value,"
						    +"AVG(y.transact_avg_value) AS transact_avg_value,"
						    +"SUM(y.transact_max_value) AS transact_max_value,"
						    +"y.deal_currency,y.currency,y.unit "
						    +"FROM (select * from (SELECT x.factset_entity_id,name,company_id,total_deals,transact_tot_value,transact_avg_value,transact_max_value,deal_currency,currency,unit "
						    +"FROM (SELECT factset_entity_id,"
						    +"COUNT(deal_id) AS total_deals,"
				    		+"SUM(transaction_value) AS transact_tot_value,"
				    		+"AVG(transaction_value) AS transact_avg_value,"
				    		+"MAX(transaction_value) AS transact_max_value,"
				    		+"deal_currency,currency,unit "
						    +"FROM (SELECT r.factset_entity_id,"
						    +"r.deal_id as deal_id,"
						    +"transaction_value * factset.get_fx_daily_conversion(t.deal_currency, :currency, announce_date) as transaction_value,"
						    +"t.deal_currency,:currency AS currency,'Million' AS unit "
						    +"FROM factset.ma_v1_ma_deal_info i "
						    +"JOIN factset.ma_v1_ma_deal_relationship r ON i.deal_id = r.deal_id "
						    +"JOIN factset.ma_v1_ma_deal_terms t ON t.deal_id = r.deal_id "
						    +"LEFT JOIN cm.company_list c ON r.factset_entity_id = c.factset_entity_id "
						    +"WHERE co_role_id IN (3 , 5, 8, 11, 2, 4, 10, 12, 13) "
						    +"AND announce_date BETWEEN :startDate AND :endDate ";
		boolean selectCountry = false;
	 	boolean selectIndustry = false;
	    if(country!=null && !"World".equals(country) && !"".equals(country)){
		    	baseQuery+="AND c.domicile_country_code = :country ";
		    	selectCountry = true;
		    }
		    
	    if(industry!=null && !"All".equals(industry) && !"".equals(industry)){
		    	baseQuery+="AND c.tics_industry_code = :industry ";
		    	selectIndustry = true;
		    }
	    		baseQuery+="AND i.status_id IN (1 , 2) GROUP BY i.deal_id , factset_entity_id) w GROUP BY factset_entity_id) x "
						    +"LEFT JOIN cm.company_list c ON x.factset_entity_id = c.factset_entity_id "
						    +"ORDER BY domicile_flag DESC)z group by factset_entity_id)y JOIN (SELECT @id\\:=0) AS ai";
			Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(MNATopDealMakerTotal.class);;
			if(selectCountry){
				query.setParameter("country", country);	
			}
			if(selectIndustry){
				query.setParameter("industry", industry);	
			}
			query.setParameter("currency", currency);
			query.setDate("startDate", startDate);
			query.setDate("endDate", endDate);
			@SuppressWarnings("unchecked")
			List<MNATopDealMakerTotal> data = (List<MNATopDealMakerTotal>) query.list();
			List<MNATopDealMakerTotalDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, MNATopDealMakerTotalDTO.class);
			if(dcsDTOs.isEmpty()){
				return null;
			}else{
				return dcsDTOs.get(0);
			}
	}

}
