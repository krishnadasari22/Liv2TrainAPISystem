package com.pcompany.daoimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dao.AnalystCoverageDao;
import com.pcompany.dto.AnalystCoverageChartDTO;
import com.pcompany.dto.AnalystCoverageDTO;
import com.pcompany.dto.AnalystGuidanceDeviationDTO;
import com.pcompany.entity.AnalystConsensus;
import com.pcompany.entity.AnalystConsensusFinancial;
import com.pcompany.entity.AnalystCoverage;
import com.pcompany.entity.AnalystGuidanceDeviation;
import com.pcompany.entity.AnalystManagementGuide;
import com.pcompany.entity.AnalystMovementRating;
import com.pcompany.entity.AnalystTotalRec;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class AnalystCoverageDaoImpl implements AnalystCoverageDao{
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	Logger _log = Logger.getLogger(AnalystCoverageDaoImpl.class);
	@Override
	public List<AnalystCoverageChartDTO> getTotalRecommendation(String companyId, String recType) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_buy,fe_over,fe_no_rec,fe_under,fe_sell,cons_start_date FROM `fe_v4_fe_basic_conh_rec` , (SELECT @row_number\\:=0) AS t where fsym_id=:fsymid and fe_item=:feitem group by year(cons_start_date) having max(cons_start_date)  limit 10";
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id,t.fe_item,t.fe_buy,t.fe_over,t.fe_no_rec,t.fe_under,t.fe_sell,t.cons_start_date,m.fe_item_desc FROM (SELECT @row_number\\:=0) AS t, (select fsym_id,max(cons_start_date) as max_date FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem group by year(cons_start_date)) r INNER JOIN `fe_v4_fe_basic_conh_rec` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date join ref_v2_fe_item_map m on t.fe_item=m.fe_item where t.fsym_id=:fsymid and t.fe_item=:feitem limit 10";
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id,t.fe_item,t.fe_buy as buy,t.fe_over as overweight,t.fe_no_rec as neutral,t.fe_under as underweight,t.fe_sell as sell,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc" + 
		" FROM (select fsym_id,max(cons_start_date) as max_date" + 
		" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem group by year(cons_start_date)" + 
		" ) r" + 
		" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
		" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date" + 
		" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
		" join (SELECT @row_number\\:=0) AS t"+
		" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
		" and t.cons_start_date >= date_sub(curdate(),interval 5 year)";*/
		
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id,t.fe_item,t.fe_total as total,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc,currency" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem" + 
				" group by year(cons_start_date),month(cons_start_date)) r" + 
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
				" ON t.fsym_id = r.fsym_id AND cons_start_date = r.max_date" + 
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
				" join (SELECT @row_number\\:=0) AS t"+
				" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
				" and cons_start_date>= date_sub(curdate(),interval 1 year)" + 
				" order by cons_start_date desc";*/
		String baseQuery=" SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_total as total,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc,currency" + 
				" FROM  `fe_v4_fe_basic_conh_rec` t" + 
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
				" join (SELECT @row_number\\:=0) AS t"+
				" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
				" and cons_start_date>= date_sub(curdate(),interval 1 year)" + 
				" order by cons_start_date desc";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystTotalRec.class);
		query.setParameter("fsymid", companyId);
		query.setParameter("feitem", recType);
		
		@SuppressWarnings("unchecked")
		List<AnalystTotalRec> data = (List<AnalystTotalRec>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageChartDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageChartDTO.class);	

		return dcsDTOs;
		//return null;
	}
	
	@Override
	public List<AnalystCoverageDTO> getRecommendation(String companyId, String recType) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_buy,fe_over,fe_no_rec,fe_under,fe_sell,cons_start_date FROM `fe_v4_fe_basic_conh_rec` , (SELECT @row_number\\:=0) AS t where fsym_id=:fsymid and fe_item=:feitem group by year(cons_start_date) having max(cons_start_date)  limit 10";
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id,t.fe_item,t.fe_buy,t.fe_over,t.fe_no_rec,t.fe_under,t.fe_sell,t.cons_start_date,m.fe_item_desc FROM (SELECT @row_number\\:=0) AS t, (select fsym_id,max(cons_start_date) as max_date FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem group by year(cons_start_date)) r INNER JOIN `fe_v4_fe_basic_conh_rec` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date join ref_v2_fe_item_map m on t.fe_item=m.fe_item where t.fsym_id=:fsymid and t.fe_item=:feitem limit 10";
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id,t.fe_item,t.fe_buy as buy,t.fe_over as overweight,t.fe_no_rec as neutral,t.fe_under as underweight,t.fe_sell as sell,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc" + 
		" FROM (select fsym_id,max(cons_start_date) as max_date" + 
		" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem group by year(cons_start_date)" + 
		" ) r" + 
		" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
		" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date" + 
		" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
		" join (SELECT @row_number\\:=0) AS t"+
		" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
		" and t.cons_start_date >= date_sub(curdate(),interval 5 year)";*/
		/*String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id,t.fe_item,t.fe_buy as buy,t.fe_over as overweight,t.fe_no_rec as neutral,t.fe_under as underweight,t.fe_sell as sell,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem" + 
				" group by year(cons_start_date),month(cons_start_date)) r" + 
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
				" ON t.fsym_id = r.fsym_id AND cons_start_date = r.max_date" + 
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
				" join (SELECT @row_number\\:=0) AS t"+
				" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
				" and cons_start_date>= date_sub(curdate(),interval 1 year)" + 
				" order by cons_start_date desc";*/
		
		/*String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_buy as buy,t.fe_over as overweight,t.fe_no_rec as neutral,t.fe_under as underweight,t.fe_sell as sell,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc,currency" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item=:feitem" + 
				" group by year(cons_start_date),month(cons_start_date)) r" + 
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
				" ON t.fsym_id = r.fsym_id AND cons_start_date = r.max_date" + 
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
				" join (SELECT @row_number\\:=0) AS t"+
				" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
				" and cons_start_date>= date_sub(curdate(),interval 1 year)" + 
				" order by cons_start_date desc;";*/
		
		/*String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_buy as buy,t.fe_over as overweight,t.fe_no_rec as neutral,t.fe_under as underweight,t.fe_sell as sell,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc,currency" + 
				" FROM  `fe_v4_fe_basic_conh_rec` t" + 
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
				" join (SELECT @row_number\\:=0) AS t"+
				" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
				" and cons_start_date>= date_sub(curdate(),interval 1 year)" + 
				" order by cons_start_date desc";*/
		
		String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_buy as buy,t.fe_over as overweight,t.fe_hold as neutral,t.fe_under as underweight,t.fe_sell as sell,t.cons_start_date as cons_date,m.fe_item_desc as fe_item_desc,currency" + 
				" FROM  `fe_v4_fe_basic_conh_rec` t" + 
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item" + 
				" join (SELECT @row_number\\:=0) AS t"+
				" where t.fsym_id=:fsymid and t.fe_item=:feitem" + 
				" and cons_start_date>= date_sub(curdate(),interval 1 year)" + 
				" order by cons_start_date desc";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystCoverage.class);
		query.setParameter("fsymid", companyId);
		query.setParameter("feitem", recType);
		
		@SuppressWarnings("unchecked")
		List<AnalystCoverage> data = (List<AnalystCoverage>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
		//return null;
	}


	@Override
	public List<AnalystCoverageDTO> getBasicConsensusYearly(String companyId, Boolean isDownload) {
		String baseQuery = null;
		if(isDownload) {
			/*baseQuery="select (@id\\:=@id+1) as id,fsym_id,fe_item,mean,median,low,high,standard_deviation,fe_fp_end,fe_item_desc," + 
					" display_order,currency,unit from (select * from (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,t.currency as currency,m.units as unit" + 
					" FROM" + 
					"`fe_v4_fe_advanced_conh_af` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid" + 
					" and t.fe_fp_end between" + 
					" (select date_add(max(date),interval -5 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
					" and (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
					" union" + 
					" (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,t.currency as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_basic_conh_af` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid and" + 
					" t.fe_fp_end between" + 
					" (select date_add(max(date),interval -5 year) from ff_v3_ff_basic_af where fsym_id=:fsymid)" + 
					" and   (select date_add(max(date),interval 6 year) from ff_v3_ff_basic_af where fsym_id=:fsymid))) x" + 
					" group by fe_fp_end,fe_item" + 
					" order by display_order) y join (select @id\\:=0) as ai ";*/
			baseQuery="select (@id\\:=@id+1) as id,fsym_id,fe_item,mean,median,low,high,standard_deviation,fe_fp_end,fe_item_desc," + 
					" display_order,currency,unit from (select * from (SELECT cons_end_date,t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_advanced_conh_af` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid " + 
					" and t.fe_fp_end between" + 
					" (select date_sub(max(date),interval 5 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
					" and (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid) " + 
					" union" + 
					" (SELECT cons_end_date,t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_basic_conh_af` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid  and" + 
					" t.fe_fp_end between" + 
					" (select date_sub(max(date),interval 5 year) from ff_v3_ff_basic_af where fsym_id=:fsymid)" + 
					" and   (select date_add(max(date),interval 6 year) from ff_v3_ff_basic_af where fsym_id=:fsymid) " + 
					" ) order by cons_end_date desc) x" + 
					" group by fe_fp_end,fe_item" + 
					" order by display_order,fe_item) y join (select @id\\:=0) as ai";
		}else {
		baseQuery ="select (@id\\:=@id+1) as id,fsym_id,fe_item,mean,median,low,high,standard_deviation,fe_fp_end,fe_item_desc," + 
				" display_order,currency,unit from (select * from (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
				" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency,m.units as unit" + 
				" FROM" + 
				" `fe_v4_fe_advanced_conh_af` t" + 
				" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
				" where t.fsym_id=:fsymid and cons_end_date is null" + 
				" and t.fe_fp_end between" + 
				" (select date_add(max(date),interval 1 day) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
				" and (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
				" union" + 
				" (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
				" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency,m.units as unit" + 
				" FROM" + 
				" `fe_v4_fe_basic_conh_af` t" + 
				" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
				" where t.fsym_id=:fsymid and cons_end_date is null and" + 
				" t.fe_fp_end between" + 
				" (select date_add(max(date),interval 1 day) from ff_v3_ff_basic_af where fsym_id=:fsymid)" + 
				" and   (select date_add(max(date),interval 6 year) from ff_v3_ff_basic_af where fsym_id=:fsymid))) x" + 
				" group by fe_fp_end,fe_item" + 
				" order by display_order,fe_item) y join (select @id\\:=0) as ai";
		}
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystConsensus.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystConsensus> data = (List<AnalystConsensus>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	
		
		dcsDTOs.addAll(getAdvancedConsensusYearly(companyId));

		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getBasicConsensusQuarterly(String companyId, Boolean isDownload) {
		String baseQuery= null;
		if(isDownload) {
			/*baseQuery="select (@id\\:=@id+1) as id,fsym_id,fe_item,mean,median,low,high,standard_deviation,fe_fp_end,fe_item_desc," + 
					" display_order,currency,unit from (select * from (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,t.currency as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_advanced_conh_qf` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid" + 
					" and t.fe_fp_end between" + 
					" (select date_add(max(date),interval -5 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)" + 
					" and (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)" + 
					" union" + 
					" (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,t.currency as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_basic_conh_qf` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid and" + 
					" t.fe_fp_end between" + 
					" (select date_add(max(date),interval -5 year) from ff_v3_ff_basic_qf where fsym_id=:fsymid)" + 
					" and   (select date_add(max(date),interval 6 year) from ff_v3_ff_basic_qf where fsym_id=:fsymid))) x" + 
					" group by fe_fp_end,fe_item" + 
					" order by display_order) y join (select @id\\:=0) as ai ";*/
			baseQuery="select (@id\\:=@id+1) as id,fsym_id,fe_item,mean,median,low,high,standard_deviation,fe_fp_end,fe_item_desc," + 
					" display_order,currency,unit from (select * from (SELECT cons_end_date,t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_advanced_conh_qf` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid " + 
					" and t.fe_fp_end between" + 
					" (select date_sub(max(date),interval 5 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)" + 
					" and (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid) " + 
					" union" + 
					" (SELECT cons_end_date,t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_basic_conh_qf` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid  and" + 
					" t.fe_fp_end between" + 
					" (select date_sub(max(date),interval 5 year) from ff_v3_ff_basic_qf where fsym_id=:fsymid)" + 
					" and   (select date_add(max(date),interval 6 year) from ff_v3_ff_basic_qf where fsym_id=:fsymid) " + 
					" ) order by cons_end_date desc) x" + 
					" group by fe_fp_end,fe_item" + 
					" order by display_order,fe_item) y join (select @id\\:=0) as ai ";
		}else {
			baseQuery="select (@id\\:=@id+1) as id,fsym_id,fe_item,mean,median,low,high,standard_deviation,fe_fp_end,fe_item_desc," + 
					" display_order,currency,unit from (select * from (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency ,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_advanced_conh_qf` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid and cons_end_date is null" + 
					" and t.fe_fp_end between" + 
					" (select date_add(max(date),interval 1 day) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)" + 
					" and   (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)" + 
					" union" + 
					" (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
					" m.fe_item_desc as fe_item_desc,m.display_order as display_order,case when m.curindicator=1 then t.currency else null end as currency ,m.units as unit" + 
					" FROM" + 
					" `fe_v4_fe_basic_conh_qf` t" + 
					" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
					" where t.fsym_id=:fsymid and cons_end_date is null and" + 
					" t.fe_fp_end between" + 
					" (select date_add(max(date),interval 1 day) from ff_v3_ff_basic_qf where fsym_id=:fsymid)" + 
					" and (select date_add(max(date),interval 6 year) from ff_v3_ff_basic_qf where fsym_id=:fsymid))) x" + 
					" group by fe_fp_end,fe_item" + 
					" order by display_order,fe_item) y join (select @id\\:=0) as ai";

		}
				
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystConsensus.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystConsensus> data = (List<AnalystConsensus>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	
		
		dcsDTOs.addAll(getAdvancedConsensusQuarterly(companyId));
		
		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getAdvancedConsensusYearly(String companyId) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_mean,fe_median,fe_low,fe_high,fe_std_dev,fe_fp_end FROM `fe_v4_fe_advanced_conh_af`,  (SELECT @row_number\\:=0) AS t where fsym_id =:fsymid and cons_end_date is null group by year(cons_start_date),fe_item having max(cons_start_date)";
/*		String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id as fsym_id,t.fe_item,t.fe_mean,t.fe_median,t.fe_low ,t.fe_high,t.fe_std_dev,t.fe_fp_end,"+
		" m.fe_item_desc"+
		" FROM (select fsym_id,max(cons_start_date) as max_date,fe_item"+
		" FROM `fe_v4_fe_advanced_conh_af` where fsym_id=:fsymid"+
		" and cons_end_date is null group by year(cons_start_date),fe_item) r"+
		" INNER JOIN"+
		" `fe_v4_fe_advanced_conh_af` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date and t.fe_item=r.fe_item"+
		" join ref_v2_fe_item_map m on t.fe_item=m.fe_item join"+
		" (SELECT @row_number\\:=0) as ai"+		
		" where t.fsym_id =:fsymid and cons_end_date is null"+
		" group by year(t.fe_fp_end),t.fe_item";*/
		
		String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,mean,median,low,high, standard_deviation, fe_fp_end," + 
				" fe_item_desc from (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
				" m.fe_item_desc as fe_item_desc" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date,fe_item" + 
				" FROM `fe_v4_fe_advanced_conh_af` where fsym_id=:fsymid" + 
				" and cons_end_date is null group by year(cons_start_date),fe_item) r" + 
				" INNER JOIN" + 
				" `fe_v4_fe_advanced_conh_af` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date and t.fe_item=r.fe_item" + 
				" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
				" where t.fsym_id=:fsymid and cons_end_date is null" + 
				" and t.fe_fp_end between" + 
				" (select date_add(max(date),interval 1 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
				" and   (select date_add(max(date),interval 10 year) from ff_v3_ff_advanced_af where fsym_id=:fsymid)" + 
				" group by year(t.fe_fp_end),t.fe_item" + 
				" order by m.display_order)x join " + 
				" (SELECT @row_number\\:=0) as ai";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystConsensus.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystConsensus> data = (List<AnalystConsensus>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getAdvancedConsensusQuarterly(String companyId) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_mean,fe_median,fe_low,fe_high,fe_std_dev,fe_fp_end FROM `fe_v4_fe_advanced_conh_qf`,  (SELECT @row_number\\:=0) AS t where fsym_id =:fsym_id and cons_end_date is null group by year(cons_start_date),fe_item having max(cons_start_date)";
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id as fsym_id,t.fe_item,t.fe_mean,t.fe_median,t.fe_low ,t.fe_high,t.fe_std_dev,t.fe_fp_end,"+
				" m.fe_item_desc"+
				" FROM (select fsym_id,max(cons_start_date) as max_date,fe_item"+
				" FROM `fe_v4_fe_advanced_conh_qf` where fsym_id=:fsymid"+
				" and cons_end_date is null group by year(cons_start_date),fe_item) r"+
				" INNER JOIN"+
				" `fe_v4_fe_advanced_conh_qf` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date and t.fe_item=r.fe_item"+
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item join"+
				" (SELECT @row_number\\:=0) as ai"+		
				" where t.fsym_id =:fsymid and cons_end_date is null"+
				" group by year(t.fe_fp_end),t.fe_item";*/
		/*String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end," + 
				" m.fe_item_desc as fe_item_desc" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date,fe_item" + 
				" FROM `fe_v4_fe_advanced_conh_qf` where fsym_id=:fsymid" + 
				" and cons_end_date is null group by year(cons_start_date),fe_item) r" + 
				" INNER JOIN" + 
				" `fe_v4_fe_advanced_conh_qf` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date and t.fe_item=r.fe_item" + 
				" join cm.guidance_item_mapping m on t.fe_item=m.fe_item" + 
				" where t.fsym_id=:fsymid and cons_end_date is null" + 
				" and t.fe_fp_end between" + 
				" (select date_add(max(date),interval 1 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)" + 
				" and (select date_add(max(date),interval 6 year) from ff_v3_ff_advanced_qf join (SELECT @row_number\\:=0) as ai where fsym_id=:fsymid)" +
				" group by year(t.fe_fp_end),t.fe_item" + 
				" order by m.display_order";*/
		
		String baseQuery =" SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,mean,median,low,high, standard_deviation, fe_fp_end,"+
		" fe_item_desc from (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_mean as mean,t.fe_median as median,t.fe_low as low,t.fe_high as high,t.fe_std_dev as standard_deviation,t.fe_fp_end as fe_fp_end,"+
		" m.fe_item_desc as fe_item_desc"+
		" FROM (select fsym_id,max(cons_start_date) as max_date,fe_item"+
		" FROM `fe_v4_fe_advanced_conh_qf` where fsym_id=:fsymid"+
		" and cons_end_date is null group by year(cons_start_date),fe_item) r"+
		" INNER JOIN"+
		" `fe_v4_fe_advanced_conh_qf` t ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date and t.fe_item=r.fe_item"+
		" join cm.guidance_item_mapping m on t.fe_item=m.fe_item"+
		" where t.fsym_id=:fsymid and cons_end_date is null"+
		" and t.fe_fp_end between"+
		" (select date_add(max(date),interval 1 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)"+
		" and (select date_add(max(date),interval 10 year) from ff_v3_ff_advanced_qf where fsym_id=:fsymid)"+
		" group by year(t.fe_fp_end),t.fe_item"+
		" order by m.display_order)x join "+
		" (SELECT @row_number\\:=0) as ai ";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystConsensus.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystConsensus> data = (List<AnalystConsensus>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
	}
	
	
	@Override
	public List<AnalystCoverageDTO> getBasicManagementGuidanceYearly(String companyId) {
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_fp_end,t.guidance_type,t.guidance_value,m.fe_item_desc FROM"+
		" (select fsym_id,max(guidance_date) as max_date,fe_item"+
		" FROM `fe_v4_fe_basic_guid_af` where fsym_id=:fsymid"+
		" group by fe_item,guidance_type ) r"+
		" INNER JOIN"+
		" `fe_v4_fe_basic_guid_af` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date"+
		" join ref_v2_fe_item_map m on t.fe_item=m.fe_item join"+
		" (SELECT @row_number\\:=0) as ai"+		
		" where t.fsym_id=:fsymid group by t.fe_item,t.guidance_type ";*/
		String baseQuery= "select (@id \\:= @id + 1) as id,fsym_id,fe_item, date,type,guidance_value,fe_item_desc, currency, unit from"
				+ " (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_fp_end as date,t.guidance_type as type,t.guidance_value as guidance_value, "
				+ " m.fe_item_desc as fe_item_desc, units as unit, case when curindicator=1 then currency else null end as currency  FROM"
				+ " (select fsym_id,max(guidance_date) as max_date,fe_item"
				+ " FROM `fe_v4_fe_basic_guid_af` where fsym_id=:fsymid"
				+ " group by fe_item,guidance_type ) r"
				+ " INNER JOIN"
				+ " `fe_v4_fe_basic_guid_af` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date"
				+ " join cm.guidance_item_unit_map m on t.fe_item=m.fe_item "
				+ " where t.fsym_id=:fsymid and year(t.fe_fp_end) > year(curdate()) group by t.fe_item,t.guidance_type"
				+ " union"
				+ " (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_fp_end as date,t.guidance_type as type,t.guidance_value as guidance_value, "
				+ " m.fe_item_desc as fe_item_desc, units as unit, case when curindicator=1 then currency else null end as currency FROM"
				+ " (select fsym_id,max(guidance_date) as max_date,fe_item"
				+ " FROM `fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid"
				+ " group by fe_item,guidance_type ) r"
				+ " INNER JOIN"
				+ " `fe_v4_fe_advanced_guid_af` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date"
				+ " join cm.guidance_item_unit_map m on t.fe_item=m.fe_item "
				+ " where t.fsym_id=:fsymid and year(t.fe_fp_end) > year(curdate()) group by t.fe_item,t.guidance_type)) x"
				+ " join (SELECT @id \\:= 0) as ai"  ;
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystManagementGuide.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystManagementGuide> data = (List<AnalystManagementGuide>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	
		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getBasicManagementGuidanceQuarterly(String companyId) {
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_fp_end,t.guidance_type,t.guidance_value,m.fe_item_desc FROM"+
				" (select fsym_id,max(guidance_date) as max_date,fe_item"+
				" FROM `fe_v4_fe_basic_guid_qf` where fsym_id=:fsymid"+
				" group by fe_item,guidance_type ) r"+
				" INNER JOIN"+
				" `fe_v4_fe_basic_guid_qf` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date"+
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item join"+
				" (SELECT @row_number\\:=0) as ai"+		
				" where t.fsym_id=:fsymid group by t.fe_item,t.guidance_type";*/
		String baseQuery= "select (@id \\:= @id + 1) as id,fsym_id,fe_item, date,type,guidance_value,fe_item_desc, currency, unit from "
				+ " (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_fp_end as date,t.guidance_type as type,t.guidance_value as guidance_value, "
				+ " m.fe_item_desc as fe_item_desc, units as unit, case when curindicator=1 then currency else null end as currency FROM "
				+ " `fe_v4_fe_basic_guid_qf` t"
				+ " join cm.guidance_item_unit_map m on t.fe_item=m.fe_item "
				+ " where t.fsym_id=:fsymid and t.fe_fp_end > curdate() group by t.fe_item,t.guidance_type "
				+ " union "
				+ " (SELECT t.fsym_id as fsym_id,t.fe_item as fe_item,t.fe_fp_end as date,t.guidance_type as type,t.guidance_value as guidance_value, "
				+ " m.fe_item_desc as fe_item_desc, units as unit, case when curindicator=1 then currency else null end as currency FROM"
				+ " `fe_v4_fe_advanced_guid_qf` t"
				+ " join cm.guidance_item_unit_map m on t.fe_item=m.fe_item "
				+ " where t.fsym_id=:fsymid and t.fe_fp_end > curdate() group by t.fe_item,t.guidance_type)) x"
				+ " join (SELECT @id \\:= 0) as ai"; 
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystManagementGuide.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystManagementGuide> data = (List<AnalystManagementGuide>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	
		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getAdvancedManagementGuidanceYearly(String companyId) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_fp_end,guidance_type,guidance_value FROM `fe_v4_fe_advanced_guid_af`,  (SELECT @row_number\\:=0) AS t where fsym_id =:fsymid group by fe_item,guidance_type having max(guidance_date)";
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_fp_end,t.guidance_type,t.guidance_value,m.fe_item_desc FROM"+
		" (select fsym_id,max(guidance_date) as max_date,fe_item"+
		" FROM `fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid"+
		" group by fe_item,guidance_type ) r"+
		" INNER JOIN"+
		" `fe_v4_fe_advanced_guid_af` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date"+
		" join ref_v2_fe_item_map m on t.fe_item=m.fe_item join"+
		" (SELECT @row_number\\:=0) as ai"+		
		" where t.fsym_id=:fsymid group by t.fe_item,t.guidance_type";*/
		
		String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_fp_end,t.guidance_type,t.guidance_value,m.fe_item_desc,units as unit," + 
				" case when curindicator=1 then currency else null end as currency FROM" + 
				" (select fsym_id,max(guidance_date) as max_date,fe_item" + 
				" FROM `fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid" + 
				" group by fe_item,guidance_type ) r" + 
				" INNER JOIN " + 
				" `fe_v4_fe_advanced_guid_af` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date " + 
				" join cm.guidance_item_unit_map m on t.fe_item=m.fe_item join " + 
				" (SELECT @row_number\\:=0) as ai " + 
				" where t.fsym_id=:fsymid group by t.fe_item,t.guidance_type";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystManagementGuide.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystManagementGuide> data = (List<AnalystManagementGuide>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getAdvancedManagementGuidanceQuarterly(String companyId) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_fp_end,guidance_type,guidance_value FROM `fe_v4_fe_advanced_guid_qf`,  (SELECT @row_number\\:=0) AS t where fsym_id =:fsymid group by fe_item,guidance_type having max(guidance_date)";
		/*String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_fp_end,t.guidance_type,t.guidance_value,m.fe_item_desc FROM"+
				" (select fsym_id,max(guidance_date) as max_date,fe_item"+
				" FROM `fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid"+
				" group by fe_item,guidance_type ) r"+
				" INNER JOIN"+
				" `fe_v4_fe_advanced_guid_qf` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date"+
				" join ref_v2_fe_item_map m on t.fe_item=m.fe_item join"+
				" (SELECT @row_number\\:=0) as ai"+		
				" where t.fsym_id=:fsymid group by t.fe_item,t.guidance_type"; */
		
		String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,t.fsym_id,t.fe_item,t.fe_fp_end,t.guidance_type,t.guidance_value,m.fe_item_desc,units as unit," + 
				" case when curindicator=1 then currency else null end as currency FROM" + 
				" (select fsym_id,max(guidance_date) as max_date,fe_item" + 
				" FROM `fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid" + 
				" group by fe_item,guidance_type ) r" + 
				" INNER JOIN " + 
				" `fe_v4_fe_advanced_guid_qf` t on t.fsym_id=r.fsym_id and t.guidance_date = r.max_date " + 
				" join cm.guidance_item_unit_map m on t.fe_item=m.fe_item join " + 
				" (SELECT @row_number\\:=0) as ai " + 
				" where t.fsym_id=:fsymid group by t.fe_item,t.guidance_type";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystManagementGuide.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystManagementGuide> data = (List<AnalystManagementGuide>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getConsensusOperationYearly(String companyId) {
		/*String baseQuery="select (@id\\:= @id + 1) as id,x.fsym_id as fsym_id,x.fe_item as fe_item,x.m_fe_fp_end_date as management_date,x.a_fe_fp_end_date as analyst_date,x.analyst_value as analyst_value,x.management_value as management_value,m.fe_item_desc as fe_item_desc" + 
				" from ((select a1.fsym_id,a1.fe_item,m1.m_fe_fp_end_date,a1.a_fe_fp_end_date,a1.analyst_value,m1.management_value from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM (select fsym_id,max(guidance_date) as max_date FROM `fe_v4_fe_ind_guid_af` where fsym_id=:fsymid and guidance_type='high' group by year(guidance_date),fe_item ) r INNER JOIN `fe_v4_fe_ind_guid_af` g ON g.fsym_id = r.fsym_id AND g.guidance_date = r.max_date where g.fsym_id=:fsymid group by year(g.fe_fp_end),fe_item )m1  right join" + 
				" (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM" + 
				" (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_ind_conh_af` where fsym_id=:fsymid and cons_end_date is null group by year(cons_start_date),fe_item) r INNER JOIN `fe_v4_fe_ind_conh_af`c" + 
				" ON c.fsym_id = r.fsym_id AND c.cons_start_date = r.max_date" + 
				" where c.fsym_id=:fsymid  and cons_end_date is null group by year(c.fe_fp_end),fe_item)a1" + 
				" on a1.fe_item=m1.fe_item and a1.a_fe_fp_end_date=m1.m_fe_fp_end_date)" + 
				" union" + 
				" (select a2.fsym_id,a2.fe_item,m2.m_fe_fp_end_date,a2.a_fe_fp_end_date,a2.analyst_value,m2.management_value  from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM (select fsym_id,max(guidance_date) as max_date FROM `fe_v4_fe_ind_guid_af` where fsym_id=:fsymid and guidance_type='high' group by year(guidance_date),fe_item ) r INNER JOIN `fe_v4_fe_ind_guid_af` g ON g.fsym_id = r.fsym_id AND g.guidance_date = r.max_date where g.fsym_id=:fsymid group by year(g.fe_fp_end),fe_item )m2  right join" + 
				" (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM" + 
				" (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_ind_conh_af` where fsym_id=:fsymid and cons_end_date is null group by year(cons_start_date),fe_item) r INNER JOIN `fe_v4_fe_ind_conh_af`c" + 
				" ON c.fsym_id = r.fsym_id AND c.cons_start_date = r.max_date" + 
				" where c.fsym_id=:fsymid  and cons_end_date is null group by year(c.fe_fp_end),fe_item)a2" + 
				" on a2.fe_item=m2.fe_item and a2.a_fe_fp_end_date=m2.m_fe_fp_end_date)) x join (SELECT @id\\:= 0) as ai" + 
				" join ref_v2_fe_item_map m on x.fe_item=m.fe_item";*/
		
		String baseQuery="select (@id \\:= @id + 1) as id,x.fsym_id as fsym_id,x.fe_item as fe_item,x.m_fe_fp_end_date as management_date,x.a_fe_fp_end_date as analyst_date,x.analyst_value as analyst_value,x.management_value as management_value,m.fe_item_desc as fe_item_desc"
				+ " from ((select a1.fsym_id,a1.fe_item,m1.m_fe_fp_end_date,a1.a_fe_fp_end_date,a1.analyst_value,m1.management_value from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM `fe_v4_fe_ind_guid_af` g  where g.fsym_id=:fsymid and  year(g.fe_fp_end) > year(curdate()) group by year(g.fe_fp_end),fe_item )m1 left join"
				+ " (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM"
				+ " `fe_v4_fe_ind_conh_af`c"
				+ " where c.fsym_id=:fsymid  and cons_end_date is null and  year(c.fe_fp_end) > year(curdate()) group by year(c.fe_fp_end),fe_item)a1"
				+ " on a1.fe_item=m1.fe_item and a1.a_fe_fp_end_date=m1.m_fe_fp_end_date)"
				+ " union"
				+ " (select a2.fsym_id,a2.fe_item,m2.m_fe_fp_end_date,a2.a_fe_fp_end_date,a2.analyst_value,m2.management_value  from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM `fe_v4_fe_ind_guid_af` g  where g.fsym_id=:fsymid and  year(g.fe_fp_end) > year(curdate()) group by year(g.fe_fp_end),fe_item )m2  right join"
				+ " (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM"
				+ " `fe_v4_fe_ind_conh_af`c"
				+ " where c.fsym_id=:fsymid  and cons_end_date is null and  year(c.fe_fp_end) > year(curdate()) group by year(c.fe_fp_end),fe_item)a2"
				+ " on a2.fe_item=m2.fe_item and a2.a_fe_fp_end_date=m2.m_fe_fp_end_date)) x"
				+ " join ref_v2_fe_item_map m on x.fe_item=m.fe_item join (SELECT @id \\:= 0) as ai";

		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystConsensusFinancial.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystConsensusFinancial> data = (List<AnalystConsensusFinancial>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
	}
	
	@Override
	public List<AnalystCoverageDTO> getConsensusOperationQuarterly(String companyId) {
		
		/*String baseQuery="select (@id\\:= @id + 1) as id,x.fsym_id as fsym_id,x.fe_item as fe_item,x.m_fe_fp_end_date as management_date,x.a_fe_fp_end_date as analyst_date,x.analyst_value as analyst_value,x.management_value as management_value,m.fe_item_desc as fe_item_desc" + 
				" from ((select a1.fsym_id,a1.fe_item,m1.m_fe_fp_end_date,a1.a_fe_fp_end_date,a1.analyst_value,m1.management_value from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM (select fsym_id,max(guidance_date) as max_date FROM `fe_v4_fe_ind_guid_qf` where fsym_id=:fsymid and guidance_type='high' group by year(guidance_date),fe_item ) r INNER JOIN `fe_v4_fe_ind_guid_qf` g ON g.fsym_id = r.fsym_id AND g.guidance_date = r.max_date where g.fsym_id=:fsymid group by year(g.fe_fp_end),fe_item )m1  right join" + 
				" (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM" + 
				" (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_ind_conh_qf` where fsym_id=:fsymid and cons_end_date is null group by year(cons_start_date),fe_item) r INNER JOIN `fe_v4_fe_ind_conh_qf`c" + 
				" ON c.fsym_id = r.fsym_id AND c.cons_start_date = r.max_date" + 
				" where c.fsym_id=:fsymid  and cons_end_date is null group by year(c.fe_fp_end),fe_item)a1" + 
				" on a1.fe_item=m1.fe_item and a1.a_fe_fp_end_date=m1.m_fe_fp_end_date)" + 
				" union" + 
				" (select a2.fsym_id,a2.fe_item,m2.m_fe_fp_end_date,a2.a_fe_fp_end_date,a2.analyst_value,m2.management_value  from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM (select fsym_id,max(guidance_date) as max_date FROM `fe_v4_fe_ind_guid_qf` where fsym_id=:fsymid and guidance_type='high' group by year(guidance_date),fe_item ) r INNER JOIN `fe_v4_fe_ind_guid_qf` g ON g.fsym_id = r.fsym_id AND g.guidance_date = r.max_date where g.fsym_id=:fsymid group by year(g.fe_fp_end),fe_item )m2  right join" + 
				" (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM" + 
				" (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_ind_conh_qf` where fsym_id=:fsymid and cons_end_date is null group by year(cons_start_date),fe_item) r INNER JOIN `fe_v4_fe_ind_conh_qf`c" + 
				" ON c.fsym_id = r.fsym_id AND c.cons_start_date = r.max_date" + 
				" where c.fsym_id=:fsymid  and cons_end_date is null group by year(c.fe_fp_end),fe_item)a2" + 
				" on a2.fe_item=m2.fe_item and a2.a_fe_fp_end_date=m2.m_fe_fp_end_date)) x join (SELECT @id\\:= 0) as ai" + 
				" join ref_v2_fe_item_map m on x.fe_item=m.fe_item";*/
		
		String baseQuery="select (@id \\:= @id + 1) as id,x.fsym_id as fsym_id,x.fe_item as fe_item,x.m_fe_fp_end_date as management_date,x.a_fe_fp_end_date as analyst_date,x.analyst_value as analyst_value,x.management_value as management_value,m.fe_item_desc as fe_item_desc "
				+ " from ((select a1.fsym_id,a1.fe_item,m1.m_fe_fp_end_date,a1.a_fe_fp_end_date,a1.analyst_value,m1.management_value from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM  `fe_v4_fe_ind_guid_qf` g  where g.fsym_id=:fsymid and g.fe_fp_end > curdate() group by g.fe_fp_end,fe_item )m1  "
				+ " left join (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type "
				+ " FROM `fe_v4_fe_ind_conh_qf` c"
				+ " where c.fsym_id=:fsymid  and cons_end_date is null and c.fe_fp_end > year(curdate())group by c.fe_fp_end,fe_item)a1"
				+ " on a1.fe_item=m1.fe_item and a1.a_fe_fp_end_date=m1.m_fe_fp_end_date)"
				+ " union"
				+ " (select a2.fsym_id,a2.fe_item,m2.m_fe_fp_end_date,a2.a_fe_fp_end_date,a2.analyst_value,m2.management_value  from (SELECT g.fsym_id as fsym_id,g.fe_item as fe_item,g.fe_fp_end as m_fe_fp_end_date,g.guidance_date as guidance_date,g.guidance_value as management_value,'management' as type FROM `fe_v4_fe_ind_guid_qf` g where g.fsym_id=:fsymid and g.fe_fp_end > curdate() group by g.fe_fp_end,fe_item )m2  right join"
				+ " (SELECT c.fsym_id as fsym_id,c.fe_item as fe_item,c.fe_fp_end as a_fe_fp_end_date,c.cons_start_date as cons_start_date,c.fe_mean as analyst_value,'analyst'as type FROM"
				+ " `fe_v4_fe_ind_conh_qf`c"
				+ " where c.fsym_id=:fsymid  and cons_end_date is null and c.fe_fp_end > curdate() group by c.fe_fp_end,fe_item)a2"
				+ " on a2.fe_item=m2.fe_item and a2.a_fe_fp_end_date=m2.m_fe_fp_end_date)) x"
				+ " join ref_v2_fe_item_map m on x.fe_item=m.fe_item"
				+ " join (SELECT @id \\:= 0) as ai"
				;
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystConsensusFinancial.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystConsensusFinancial> data = (List<AnalystConsensusFinancial>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageDTO.class);	

		return dcsDTOs;
	}
	
	@Override
	public List<AnalystGuidanceDeviationDTO> getGuidanceDeviationYearly(String companyId) {
		/*String baseQuery="select (@id\\:=@id + 1) as id,  closest_guidance_date,fe_fp_end,guidance_date,m.fe_item,high_value,low_value,m.field_name,m.table_name,m.fe_item_desc as fe_item_desc" + 
				" from ((select (select guidance_date from factset.`fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid and year(guidance_date)=year(a.fe_fp_end) order by abs(datediff(a.fe_fp_end,guidance_date)) limit 1) as closest_guidance_date,fe_fp_end,guidance_date,fe_item," + 
				" (select guidance_value from factset.`fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='high') as high_value," + 
				" (select guidance_value from factset.`fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='low') as low_value" + 
				" from factset.`fe_v4_fe_advanced_guid_af` a where fsym_id=:fsymid group by year(fe_fp_end),fe_item)" + 
				" x join cm.guidance_item_mapping m on x.fe_item = m.fe_item) join (SELECT @id\\:=0) as ai";*/
	
		/*String baseQuery="select (@id \\:= @id + 1) as id,closest_guidance_date,fe_fp_end,guidance_date,fe_item,high_value,low_value,field_name,table_name, fe_item_desc from (select closest_guidance_date,fe_fp_end,guidance_date,m.fe_item,high_value,low_value,m.field_name,m.table_name,m.fe_item_desc as fe_item_desc"
				+ " from ((select (select guidance_date from factset.`fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid and year(guidance_date)=year(a.fe_fp_end) order by abs(datediff(a.fe_fp_end,guidance_date)) limit 1) as closest_guidance_date,fe_fp_end,guidance_date,fe_item,"
				+ " (select guidance_value from factset.`fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='high' limit 1) as high_value,"
				+ " (select guidance_value from factset.`fe_v4_fe_advanced_guid_af` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='low' limit 1) as low_value"
				+ " from factset.`fe_v4_fe_advanced_guid_af` a where fsym_id=:fsymid group by year(fe_fp_end),fe_item)"
				+ " x join cm.guidance_item_mapping m on x.fe_item = m.fe_item)"
				+ " union"
				+ " (select closest_guidance_date,fe_fp_end,guidance_date,m.fe_item,high_value,low_value,m.field_name,m.table_name,m.fe_item_desc as fe_item_desc"
				+ " from ((select (select guidance_date from factset.`fe_v4_fe_basic_guid_af` where fsym_id=:fsymid and year(guidance_date)=year(a.fe_fp_end) order by abs(datediff(a.fe_fp_end,guidance_date)) limit 1) as closest_guidance_date,fe_fp_end,guidance_date,fe_item,"
				+ " (select guidance_value from factset.`fe_v4_fe_basic_guid_af` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='high' limit 1) as high_value,"
				+ " (select guidance_value from factset.`fe_v4_fe_basic_guid_af` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='low' limit 1) as low_value"
				+ " from factset.`fe_v4_fe_basic_guid_af` a where fsym_id=:fsymid group by year(fe_fp_end),fe_item)"
				+ " x join cm.guidance_item_mapping m on x.fe_item = m.fe_item)))y  "
				+ " join (SELECT @id \\:= 0) as ai where y.fe_fp_end >=date_sub(curdate(),interval 3 year)"
				+ " order by fe_fp_end desc";*/
		String baseQuery="select (@id\\:=@id + 1) as id, fe_fp_end,guidance_date,fe_item,field_name,table_name,fe_item_desc as fe_item_desc,high_value,low_value, unit, currency from "+ 
				" (select * from (select a.fe_fp_end as fe_fp_end,a.guidance_date as guidance_date,m.fe_item as fe_item,m.field_name as field_name,m.af_table_name as table_name,m.fe_item_desc as fe_item_desc,a.guidance_value as high_value,b.guidance_value as low_value, "+
				" units as unit, case when curindicator=1 then a.currency else null end as currency "+
				" from factset.`fe_v4_fe_basic_guid_af` a" + 
				" join  `fe_v4_fe_basic_guid_af` b on a.fe_item=b.fe_item and a.fe_fp_end=b.fe_fp_end and" + 
				" a.guidance_type!=b.guidance_type and a.guidance_date=b.guidance_date and a.fsym_id=b.fsym_id" + 
				" and a.guidance_type='high' and b.guidance_type='low'" + 
				" join cm.guidance_item_mapping m on a.fe_item = m.fe_item" + 
				" where a.fsym_id=:fsymid and a.fe_fp_end >=date_sub(curdate(),interval 3 year)" + 
				" order by guidance_date desc)x group by fe_fp_end,fe_item" + 
				" union" + 
				" (select * from (select a.fe_fp_end as fe_fp_end,a.guidance_date as guidance_date,m.fe_item as fe_item,m.field_name as field_name,m.af_table_name as table_name,m.fe_item_desc as fe_item_desc,a.guidance_value as high_value,b.guidance_value as low_value, "+
				" units as unit, case when curindicator=1 then b.currency else null end as currency "+
				" from factset.`fe_v4_fe_advanced_guid_af` a" + 
				" join  `fe_v4_fe_advanced_guid_af` b on a.fe_item=b.fe_item and a.fe_fp_end=b.fe_fp_end and" + 
				" a.guidance_type!=b.guidance_type and a.guidance_date=b.guidance_date and a.fsym_id=b.fsym_id" + 
				" and a.guidance_type='high' and b.guidance_type='low'" + 
				" join cm.guidance_item_mapping m on a.fe_item = m.fe_item" + 
				" join (SELECT @id \\:= 0) as ai where a.fsym_id=:fsymid and a.fe_fp_end >=date_sub(curdate(),interval 3 year)" + 
				" order by guidance_date desc)x" + 
				" group by fe_fp_end,fe_item))y" + 
				" order by fe_fp_end desc";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystGuidanceDeviation.class);
		query.setParameter("fsymid", companyId);
		
		@SuppressWarnings("unchecked")
		List<AnalystGuidanceDeviation> data = (List<AnalystGuidanceDeviation>) query.list();
		/*_log.info(data);*/
		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		String guidanceDate = null;
		
		List<AnalystGuidanceDeviationDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystGuidanceDeviationDTO.class);
	
		for(int i=0;i<dcsDTOs.size();i++) {
			String baseQuery1="select "+dcsDTOs.get(i).getFeItemCode()+" as 'actualValue' from  "+dcsDTOs.get(i).getTableName()+" where date= :closestGuidanceDate and  fsym_id=:fsymid";
			guidanceDate=df.format(dcsDTOs.get(i).getStartDate());
			_log.info(guidanceDate);
			Query query1 = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery1);
			query1.setParameter("fsymid", companyId);
			query1.setParameter("closestGuidanceDate", guidanceDate);
			@SuppressWarnings("rawtypes")
			List actualValue = query1.list(); 
			_log.info(actualValue);
			if(actualValue.size()!=0) {
				dcsDTOs.get(i).setActualValue(actualValue.get(0).toString());
			}
		}
		
		return dcsDTOs;
	}
	
	@Override
	public List<AnalystGuidanceDeviationDTO> getGuidanceDeviationQuarterly(String companyId) {
		
		/*String baseQuery="select (@id\\:=@id + 1) as id,  closest_guidance_date,fe_fp_end,guidance_date,m.fe_item,high_value,low_value,m.field_name,m.table_name,m.fe_item_desc as fe_item_desc" + 
				" from ((select (select guidance_date from factset.`fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid and year(guidance_date)=year(a.fe_fp_end) order by abs(datediff(a.fe_fp_end,guidance_date)) limit 1) as closest_guidance_date,fe_fp_end,guidance_date,fe_item," + 
				" (select guidance_value from factset.`fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='high') as high_value," + 
				" (select guidance_value from factset.`fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='low') as low_value" + 
				" from factset.`fe_v4_fe_advanced_guid_qf` a where fsym_id=:fsymid group by year(fe_fp_end),fe_item)" + 
				" x join cm.guidance_item_mapping m on x.fe_item = m.fe_item) join (SELECT @id\\:=0) as ai";*/
		/*String baseQuery="select (@id \\:= @id + 1) as id,closest_guidance_date,fe_fp_end,guidance_date,fe_item,high_value,low_value,field_name,table_name, fe_item_desc from (select closest_guidance_date,fe_fp_end,guidance_date,m.fe_item,high_value,low_value,m.field_name,m.table_name,m.fe_item_desc as fe_item_desc"
				+ " from ((select (select guidance_date from factset.`fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid and year(guidance_date)=year(a.fe_fp_end) order by abs(datediff(a.fe_fp_end,guidance_date)) limit 1) as closest_guidance_date,fe_fp_end,guidance_date,fe_item,"
				+ " (select guidance_value from factset.`fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='high' limit 1) as high_value, "
				+ "(select guidance_value from factset.`fe_v4_fe_advanced_guid_qf` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='low' limit 1) as low_value "
				+ "from factset.`fe_v4_fe_advanced_guid_qf` a where fsym_id=:fsymid group by fe_fp_end,fe_item) x "
				+ " join cm.guidance_item_mapping m on x.fe_item = m.fe_item)"
				+ " union"
				+ " (select closest_guidance_date,fe_fp_end,guidance_date,m.fe_item,high_value,low_value,m.field_name,m.table_name,m.fe_item_desc as fe_item_desc"
				+ " from ((select (select guidance_date from factset.`fe_v4_fe_basic_guid_qf` where fsym_id=:fsymid and year(guidance_date)=year(a.fe_fp_end) order by abs(datediff(a.fe_fp_end,guidance_date)) limit 1) as closest_guidance_date,fe_fp_end,guidance_date,fe_item, "
				+ "(select guidance_value from factset.`fe_v4_fe_basic_guid_qf` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='high' limit 1) as high_value, "
				+ "(select guidance_value from factset.`fe_v4_fe_basic_guid_qf` where fsym_id=:fsymid and fe_item=a.fe_item and guidance_date=closest_guidance_date and guidance_type='low' limit 1) as low_value"
				+ " from factset.`fe_v4_fe_basic_guid_qf` a where fsym_id=:fsymid group by fe_fp_end,fe_item) x "
				+ " join cm.guidance_item_mapping m on x.fe_item = m.fe_item))) y  "
				+ " join (SELECT @id \\:= 0) as ai where y.fe_fp_end >=date_sub(curdate(),interval 3 year) order by fe_fp_end desc";*/
		
		String baseQuery="select (@id\\:=@id + 1) as id, fe_fp_end,guidance_date,fe_item,field_name,table_name,fe_item_desc as fe_item_desc,high_value,low_value, unit, currency from "+
				" (select * from (select a.fe_fp_end as fe_fp_end,a.guidance_date as guidance_date,m.fe_item as fe_item,m.field_name as field_name,m.qf_table_name as table_name,m.fe_item_desc as fe_item_desc,a.guidance_value as high_value,b.guidance_value as low_value, "+
				" units as unit, case when curindicator=1 then a.currency else null end as currency "+
				" from factset.`fe_v4_fe_basic_guid_qf` a" + 
				" join  `fe_v4_fe_basic_guid_qf` b on a.fe_item=b.fe_item and a.fe_fp_end=b.fe_fp_end and" + 
				" a.guidance_type!=b.guidance_type and a.guidance_date=b.guidance_date and a.fsym_id=b.fsym_id" + 
				" and a.guidance_type='high' and b.guidance_type='low'" + 
				" join cm.guidance_item_mapping m on a.fe_item = m.fe_item" + 
				" where a.fsym_id=:fsymid and a.fe_fp_end >=date_sub(curdate(),interval 3 year)" + 
				" order by guidance_date desc)x group by fe_fp_end,fe_item" + 
				" union" + 
				" (select * from (select a.fe_fp_end as fe_fp_end,a.guidance_date as guidance_date,m.fe_item as fe_item,m.field_name as field_name,m.qf_table_name as table_name,m.fe_item_desc as fe_item_desc,a.guidance_value as high_value,b.guidance_value as low_value, "+
				" units as unit, case when curindicator=1 then a.currency else null end as currency "+
				" from factset.`fe_v4_fe_advanced_guid_qf` a" + 
				" join `fe_v4_fe_advanced_guid_qf` b on a.fe_item=b.fe_item and a.fe_fp_end=b.fe_fp_end and" + 
				" a.guidance_type!=b.guidance_type and a.guidance_date=b.guidance_date and a.fsym_id=b.fsym_id" + 
				" and a.guidance_type='high' and b.guidance_type='low'" + 
				" join cm.guidance_item_mapping m on a.fe_item = m.fe_item" + 
				" join (SELECT @id\\:= 0) as ai where a.fsym_id=:fsymid and a.fe_fp_end >=date_sub(curdate(),interval 3 year)" + 
				" order by guidance_date desc)x" + 
				" group by fe_fp_end,fe_item))y" + 
				" order by fe_fp_end desc";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystGuidanceDeviation.class);
		query.setParameter("fsymid", companyId);
		@SuppressWarnings("unchecked")
		List<AnalystGuidanceDeviation> data = (List<AnalystGuidanceDeviation>) query.list();
		/*_log.info(data);*/

		List<AnalystGuidanceDeviationDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystGuidanceDeviationDTO.class);	
		for(int i=0;i<dcsDTOs.size();i++) {
			String baseQuery1="select "+dcsDTOs.get(i).getFeItemCode()+" as 'actualValue' from  "+dcsDTOs.get(i).getTableName()+" where date= :closestGuidanceDate and  fsym_id=:fsymid";
			Query query1 = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery1);
			query1.setParameter("fsymid", companyId);
			query1.setParameter("closestGuidanceDate", dcsDTOs.get(i).getStartDate());
			@SuppressWarnings("rawtypes")
			List actualValue = query1.list(); 
			if(actualValue.size()!=0) {
			dcsDTOs.get(i).setActualValue(actualValue.get(0).toString());}
			
			
		}
		
		return dcsDTOs;
	}
	@Override
	public List<AnalystCoverageChartDTO> getMovementRating(String companyId, String type) {
		//String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id,fe_item,fe_fp_end,guidance_type,guidance_value FROM `fe_v4_fe_advanced_guid_qf`,  (SELECT @row_number\\:=0) AS t where fsym_id =:fsymid group by fe_item,guidance_type having max(guidance_date)";
		/*String baseQuery="select (@id\\:=@id + 1) as id, a1.fsym_id as fsym_id,a1.fe_item as item,a1.cons_date1 as date,"+
				" case"+
				" when total1=total2 then"+
				" case"+
				" when sell1 > sell2 then 'Downgrade'"+
				" when overweight1> overweight2 and buy1 < buy2 then 'Downgrade'"+
				" when overweight1> overweight2 and hold1 < hold2 then 'Upgrade'"+
				" when overweight1> overweight2 and underweight1 < underweight2 then 'Upgrade'"+
				" when overweight1> overweight2 and sell1 < sell2 then 'Upgrade'"+
				" when hold1> hold2 and buy1 < buy2 then 'Downgrade'"+
				" when hold1> hold2 and overweight1 < overweight2 then 'Downgrade'"+
				" when hold1> hold2 and underweight1 < underweight2 then 'Upgrade'"+
				" when hold1> hold2 and sell1 < sell2 then 'Upgrade'"+
				" when underweight1> underweight2 and buy1 < buy2 then 'Downgrade'"+
				" when underweight1> underweight2 and hold1 < hold2 then 'Downgrade'"+
				" when underweight1> underweight2 and overweight1 < overweight2 then 'Downgrade'"+
				" when underweight1> underweight2 and sell1 < sell2 then 'Upgrade'"+
				" else 'no change'"+
				" end"+
				" when total1 > total2 then"+
				" case"+
				" when buy1 >buy2 then 'Upgrade'"+
				" when overweight1> overweight2 then 'Upgrade'"+
				" when hold1> hold2 then 'Upgrade'"+
				" when underweight1> underweight2 then 'Downgrade'"+
				" when sell1 > sell2 then 'Downgrade'"+
				" else 'no change'"+
				" end"+
				" else 'no change'"+
				" end as movement"+
				" from (SELECT t.fsym_id,t.fe_item,t.fe_buy as buy1,t.fe_over as overweight1,t.fe_hold as hold1,t.fe_under as underweight1,t.fe_sell as sell1,t.fe_total as total1,t.cons_start_date as cons_date1"+
				" FROM (select fsym_id,max(cons_start_date) as max_date"+
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item='REC' group by year(cons_start_date)"+
				" ) r"+
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t"+
				" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date"+
				" where t.fsym_id=:fsymid and t.fe_item='REC') a1"+
				" inner join"+
				" (SELECT t.fsym_id,t.fe_item,t.fe_buy as buy2,t.fe_over as overweight2,t.fe_hold as hold2,t.fe_under as underweight2,t.fe_sell as sell2,t.fe_total as total2,t.cons_start_date as cons_date2"+
				" FROM (select fsym_id,max(cons_start_date) as max_date"+
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item='REC' group by year(cons_start_date)"+
				" ) r"+
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t"+
				" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date"+
				" join (SELECT @id\\:=0) as ai"+
				" where t.fsym_id=:fsymid and t.fe_item='REC' ) a2"+
				" on year(a1.cons_date1)=year(a2.cons_date2)+1"; */
		/*String baseQuery="select (@id\\:=@id + 1) as id,((buy1-buy2)+(overweight1-overweight2)+(hold1-hold2)+(underweight1-underweight2)+(sell1-sell2)) as netmovement,cons_date1 as date,fsym_id,fe_item  from ("+ 
				" select a1.fsym_id,a1.fe_item,buy1,buy2,overweight1,overweight2,hold1,hold2,underweight1,underweight2, sell1,sell2,cons_date1, cons_date2,total1,total2 from (SELECT t.fsym_id,t.fe_item,t.fe_buy as buy1,t.fe_over as overweight1,t.fe_hold as hold1,t.fe_under as underweight1,t.fe_sell as sell1,t.fe_total as total1,t.cons_start_date as cons_date1"+
				" FROM (select fsym_id,max(cons_start_date) as max_date"+
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item='REC' group by year(cons_start_date)"+ 
				" ) r"+
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t"+
				" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date"+ 
				" where t.fsym_id=:fsymid and t.fe_item='REC') a1"+ 
				" inner join"+
				" (SELECT t.fsym_id,t.fe_item,t.fe_buy as buy2,t.fe_over as overweight2,t.fe_hold as hold2,t.fe_under as underweight2,t.fe_sell as sell2,t.fe_total as total2,t.cons_start_date as cons_date2"+ 
				" FROM (select fsym_id,max(cons_start_date) as max_date"+ 
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item='REC' group by year(cons_start_date)"+ 
				" ) r"+ 
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t"+ 
				" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date"+ 
				" join (SELECT @id\\:=0) as ai"+
				" where t.fsym_id=:fsymid and t.fe_item='REC' ) a2"+ 
				" on year(a1.cons_date1)=year(a2.cons_date2)+1)x";*/
		String baseQuery = "select (@id\\:=@id + 1) as id, ((buy1-buy2)+(overweight1-overweight2)+(hold1-hold2)+(underweight1-underweight2)+(sell1-sell2)) as netmovement,cons_date1 as date,fsym_id,fe_item  from (" + 
				" select a1.fsym_id,a1.fe_item,buy1,buy2,overweight1,overweight2,hold1,hold2,underweight1,underweight2, sell1,sell2,cons_date1, cons_date2,total1,total2 from (SELECT t.fsym_id,t.fe_item,t.fe_buy as buy1,t.fe_over as overweight1,t.fe_hold as hold1,t.fe_under as underweight1,t.fe_sell as sell1,t.fe_total as total1,t.cons_start_date as cons_date1" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item='REC' group by year(cons_start_date)" + 
				" ) r" + 
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
				" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date" + 
				" where t.fsym_id=:fsymid and t.fe_item='REC') a1" + 
				" inner join" + 
				" (SELECT t.fsym_id,t.fe_item,t.fe_buy as buy2,t.fe_over as overweight2,t.fe_hold as hold2,t.fe_under as underweight2,t.fe_sell as sell2,t.fe_total as total2,t.cons_start_date as cons_date2" + 
				" FROM (select fsym_id,max(cons_start_date) as max_date" + 
				" FROM `fe_v4_fe_basic_conh_rec` where fsym_id=:fsymid and fe_item='REC' group by year(cons_start_date)" + 
				" ) r" + 
				" INNER JOIN `fe_v4_fe_basic_conh_rec` t" + 
				" ON t.fsym_id = r.fsym_id AND t.cons_start_date = r.max_date" + 
				" join (SELECT @id\\:=0) as ai"+
				" where t.fsym_id=:fsymid and t.fe_item='REC' ) a2" + 
				" on year(a1.cons_date1)=year(a2.cons_date2)+1" + 
				" where a1.cons_date1 >= date_sub(curdate(),interval 5 YEAR))x";
		_log.info(companyId + " :: " +type);
		/*String baseQuery="call cm.movement_rating(:fsymid,:fetype)";*/
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(AnalystMovementRating.class);
		query.setParameter("fsymid", companyId);
		/*query.setParameter("fetype", type);*/
		
		@SuppressWarnings("unchecked")
		List<AnalystMovementRating> data = (List<AnalystMovementRating>) query.list();
		/*_log.info(data);*/

		List<AnalystCoverageChartDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, AnalystCoverageChartDTO.class);	

		return dcsDTOs;
	}

	
}
