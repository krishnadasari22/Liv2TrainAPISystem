package com.pcompany.daoimpl;

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

import com.pcompany.dao.SegmentDao;
import com.pcompany.dto.SegmentBusinesDTO;
import com.pcompany.dto.SegmentOperationalDTO;
import com.pcompany.dto.SegmentRegionDTO;
import com.pcompany.entity.SegmentBusiness;
import com.pcompany.entity.SegmentOperational;
import com.pcompany.entity.SegmentRegion;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class SegmentDaoImpl implements SegmentDao {

	Logger _log = Logger.getLogger(SegmentDaoImpl.class);
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Override
	public List<SegmentBusinesDTO> getBusinessData(String fsimId,Date startDate, Date endDate, String currency) {
		
		_log.info("extracting Segment Business Data for currency ::: " + currency );
		
		if(currency==null){
			currency="";
		}
		String baseQuery="";
						/*+ "SELECT (@row_number\\:=@row_number + 1) AS id, fsym_id,date,ff_segment_type,ff_segment_num,adjdate,currency,label, "
						+ "sales * factset.get_fx_year_conversion(currency,:currency,date)  as sales, "
						+ "opinc * factset.get_fx_year_conversion(currency,:currency,date)  as opinc, "
						+ "assets * factset.get_fx_year_conversion(currency,:currency,date)  as assets, "
						+ "capex * factset.get_fx_year_conversion(currency,:currency,date)  as capex, "
						+ "dep * factset.get_fx_year_conversion(currency,:currency,date)  as dep, "
						+ "ff_sic_code, :currency as currency ,'Million' as unit "
						+ "FROM `ff_v3_ff_segbus_af` as sb "
						+ "join  (SELECT @row_number\\:=0) AS t "
						+ "where fsym_id=:fsimId and "
						+ "date >= date_sub((select max(date) from factset.`ff_v3_ff_segbus_af` where fsym_id=:fsimId), "
						+ " interval 10 year) order by label,date,ff_segment_num";*/
				if( !currency.equals("null") && !currency.isEmpty()){
					
					baseQuery+= "SELECT (@row_number\\:=@row_number + 1) AS id, fsym_id,date,ff_segment_type,ff_segment_num,adjdate,label, "
							+ "sales * factset.get_fx_year_conversion(currency,:currency,date)  as sales, "
							+ "opinc * factset.get_fx_year_conversion(currency,:currency,date)  as opinc, "
							+ "assets * factset.get_fx_year_conversion(currency,:currency,date)  as assets, "
							+ "capex * factset.get_fx_year_conversion(currency,:currency,date)  as capex, "
							+ "dep * factset.get_fx_year_conversion(currency,:currency,date)  as dep, "
							+ "ff_sic_code, :currency as currency ,'Million' as unit, "
							+ "case label "
							+ "when label like 'Other%' then 99 "
							+ "when label like '%unallo%' then 100 "
							+ "else 1 "
							+ "end as display_order "
							+ "FROM `ff_v3_ff_segbus_af` as sb "
							+ "join  (SELECT @row_number\\:=0) AS t "
							+ "where fsym_id=:fsimId and "
							+ "date >= date_sub((select max(date) from factset.`ff_v3_ff_segbus_af` where fsym_id=:fsimId), "
							+ " interval 7 year) order by display_order,date desc,label";
				}else{
					_log.info("No Currency");
					baseQuery+= "SELECT (@row_number\\:=@row_number + 1) AS id, fsym_id,date,ff_segment_type,ff_segment_num,adjdate,label, "
							+ "sales as sales, "
							+ "opinc as opinc, "
							+ "assets as assets, "
							+ "capex as capex, "
							+ "dep as dep, "
							+ "ff_sic_code, currency ,'Million' as unit, "
							+ "case label "
							+ "when label like 'Other%' then 99 "
							+ "when label like '%unallo%' then 100 "
							+ "else 1 "
							+ "end as display_order "
							+ "FROM `ff_v3_ff_segbus_af` as sb "
							+ "join  (SELECT @row_number\\:=0) AS t "
							+ "where fsym_id=:fsimId and "
							+ "date >= date_sub((select max(date) from factset.`ff_v3_ff_segbus_af` where fsym_id=:fsimId), "
							+ " interval 7 year) order by display_order,date desc,label";
				}
						
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(SegmentBusiness.class);
		query.setParameter("fsimId", fsimId);
		if( !currency.equals("null") && !currency.isEmpty()){
			query.setParameter("currency", currency);
		}
		/*if(startDate!=null && endDate!=null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}*/
		@SuppressWarnings("unchecked")
		List<SegmentBusiness> data =  query.list();
		List<SegmentBusinesDTO> segmentBusinesDTO = (DozerHelper.map(dozerBeanMapper, data, SegmentBusinesDTO.class));
		return segmentBusinesDTO;
	}

	@Override
	public List<SegmentRegionDTO> getRegionData(String fsimId, Date startDate,Date endDate,String currency) {
		
		_log.info("extracting Segment Region Data from the database :: " + currency );
		
	
		/*if( currency.equals("null")){
			currency="";
		}*/
		String baseQuery="";
				/*+ "SELECT (@row_number\\:=@row_number + 1) as id, fsym_id,date,ff_segment_type,label,adjdate,currency,sales,ff_segment_num,opinc,assets,capex,dep FROM `ff_v3_ff_segreg_af`, "
				+ " (SELECT @row_number\\:=0) AS t where fsym_id=:fsimId and date >= date_sub((select max(date) from factset.`ff_v3_ff_segreg_af` where fsym_id=:fsimId), interval 10 year)";*/
		if(!currency.equals("null") && !currency.isEmpty()){
			baseQuery+= "  SELECT (@row_number\\:=@row_number + 1) as id, fsym_id,date,ff_segment_type,label,adjdate, "
					+ "  sales * factset.get_fx_year_conversion(currency,:currency,date)  as sales, "
					+ "  opinc * factset.get_fx_year_conversion(currency,:currency,date)  as opinc, "
					+ "  assets * factset.get_fx_year_conversion(currency,:currency,date)  as assets, "
					+ "  capex * factset.get_fx_year_conversion(currency,:currency,date)  as capex, "
					+ "  dep * factset.get_fx_year_conversion(currency,:currency,date)  as dep, "
					+ "  ff_segment_num,:currency as currency ,'Million' as unit, "
					+ "	 case label "
					+ "  when label like 'Other%' then 99 "
					+ "  when label like 'International%' then 100 "
					+ "  else 1 "
					+ "  end as display_order "
					+ "  FROM `ff_v3_ff_segreg_af`, "
					+ "  (SELECT @row_number\\:=0) AS t "
					+ "  where fsym_id=:fsimId and "
					+ "  date >= date_sub((select max(date) from factset.`ff_v3_ff_segreg_af` where fsym_id=:fsimId), "
					+ "  interval 7 year) order by display_order,date desc,label";
		}else{
			baseQuery+= "  SELECT (@row_number\\:=@row_number + 1) as id, fsym_id,date,ff_segment_type,label,adjdate, "
					+ "  sales as sales, "
					+ "  opinc as opinc, "
					+ "  assets as assets, "
					+ "  capex as capex, "
					+ "  dep as dep, "
					+ "  ff_segment_num,currency ,'Million' as unit, "
					+ "	 case label "
					+ "  when label like 'Other%' then 99 "
					+ "  when label like 'International%' then 100 "
					+ "  else 1 "
					+ "  end as display_order "
					+ "  FROM `ff_v3_ff_segreg_af`, "
					+ "  (SELECT @row_number\\:=0) AS t "
					+ "  where fsym_id=:fsimId and "
					+ "  date >= date_sub((select max(date) from factset.`ff_v3_ff_segreg_af` where fsym_id=:fsimId), "
					+ "  interval 7 year) order by display_order,date desc,label";
		}
			
				
				
				
				/*if(startDate!=null && endDate!=null){
					baseQuery+= "and date between :startDate and :endDate; ";
				}*/
				/*baseQuery+=" order by sales desc,opinc desc,assets desc,capex desc,dep desc";*/
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(SegmentRegion.class);
		query.setParameter("fsimId", fsimId);
		if(!currency.equals("null") && !currency.isEmpty()){
			query.setParameter("currency", currency);
		}
		/*if(startDate!=null && endDate!=null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}*/
		@SuppressWarnings("unchecked")
		List<SegmentRegion> data = query.list();
		
		List<SegmentRegionDTO> cmCompanyDTO = (DozerHelper.map(dozerBeanMapper, data, SegmentRegionDTO.class));
		return cmCompanyDTO;
	}

	@Override
	public List<SegmentOperationalDTO> getOperationalData(String fsimId,Date startDate, Date endDate) {
		
		_log.info("extracting Segment Operational Data from the database " );
		
	
		String baseQuery=""
				+ "SELECT (@row_number\\:=@row_number + 1) as id, a.fsym_id as fsym_id, a.fe_item as fe_item, "
				+ "b.name as name, a.currency as currency, a.report_date AS report_date, a.actual_value as actual_value, "
				+ "a.adjdate as adjdate,a.fe_fp_end as fe_fp_end,a.publication_date as publication_date, "
				+ "a.actual_flag_code as actual_flag_code FROM `fe_v4_fe_ind_act_af` a "
				+ "inner join ref_v2_fe_item b on a.fe_item=b.fe_item "
				+ "join (SELECT @row_number\\:=0) AS t WHERE `fsym_id`=:fsimId and "
				+ "report_date >= date_sub(curdate(),interval 10 year) and report_date <=curdate()";
				
		baseQuery+=" ORDER BY b.name,a.report_date ASC";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(SegmentOperational.class);
		query.setParameter("fsimId", fsimId);
		@SuppressWarnings("unchecked")
		List<SegmentOperational> data =  query.list();
		_log.info("data :::: " + data);
		List<SegmentOperationalDTO> segmentOperationalDTO = (DozerHelper.map(dozerBeanMapper, data, SegmentOperationalDTO.class));
		return segmentOperationalDTO;
	}

	@Override
	public List<SegmentOperationalDTO> getOperationalDataQuarter(String fsimId,Date startDate, Date endDate) {
		
		_log.info("extracting Segment Operational Data Quarter from the database " );
	
		String baseQuery=""
				+ "SELECT (@row_number\\:=@row_number + 1) as id, a.fsym_id as fsym_id, a.fe_item as fe_item, "
				+ "b.name as name, a.currency as currency, a.report_date AS report_date, a.actual_value as actual_value, "
				+ "a.adjdate as adjdate,a.fe_fp_end as fe_fp_end,a.publication_date as publication_date, "
				+ "a.actual_flag_code as actual_flag_code FROM `fe_v4_fe_ind_act_qf` a "
				+ "inner join ref_v2_fe_item b on a.fe_item=b.fe_item "
				+ "join (SELECT @row_number\\:=0) AS t WHERE `fsym_id`=:fsimId and "
				+ "report_date >= date_sub(curdate(),interval 10 year) and report_date <=curdate()";
				
		baseQuery+=" ORDER BY b.name,a.report_date ASC";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(SegmentOperational.class);
		query.setParameter("fsimId", fsimId);
		@SuppressWarnings("unchecked")
		List<SegmentOperational> data =  query.list();
		_log.info("data :::: " + data);
		List<SegmentOperationalDTO> segmentOperationalDTO = (DozerHelper.map(dozerBeanMapper, data, SegmentOperationalDTO.class));
		return segmentOperationalDTO;
	}

}
