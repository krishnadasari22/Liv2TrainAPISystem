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

import com.pcompany.dao.EventsDao;
import com.pcompany.dto.CompanyDocumentMetadataDTO;
import com.pcompany.dto.EventsDTO;
import com.pcompany.dto.InsiderTransactionDTO;
import com.pcompany.dto.TranscriptFileNameDTO;
import com.pcompany.entity.CompanyDocumentMetadata;
import com.pcompany.entity.Events;
import com.pcompany.entity.InsiderTransaction;
import com.pcompany.entity.TranscriptFileName;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class EventsDaoImpl implements EventsDao{
	
	private Logger _log = Logger.getLogger(EventsDaoImpl.class);
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<InsiderTransactionDTO> getTransactionData(String securityId, Date startDate, Date endDate,String currency) {
		
		_log.info("extracting Transactional Data from the database :: " );
		
		String baseQuery=""
				+ "SELECT (@row_number\\:=@row_number + 1) AS id, fsym_id, tran_shares, factset_entity_id, tran_value, entity_proper_name, "
				+ " Date_format(effective_date,'%d-%b-%Y') AS effective_date, reported_title, market, shares_owned, shares_outstanding, percent_holding, "
				+ " buy_sell, currency FROM ( SELECT * FROM ( SELECT t.fsym_id fsym_id, t.tran_shares AS tran_shares, "
				+ " t.factset_entity_id  AS factset_entity_id, t.tran_value AS tran_value, e.entity_proper_name AS entity_proper_name, "
				+ " t.effective_date AS effective_date, t.reported_title AS reported_title, "
				+ " CASE deriv_nonderiv "
				+ "  WHEN 'D' THEN 'Derivative' "
				+ "  WHEN 'N' THEN 'Non-Derivative' "
				+ " END AS market, position AS shares_owned, (b.ff_com_shs_out*1000000) AS shares_outstanding, "
				+ " (position/(b.ff_com_shs_out*1000000))*100 AS percent_holding, "
				+ " CASE "
				+ "  WHEN t.tran_shares > 0 THEN 'Buy' "
				+ "  WHEN t.tran_shares < 0 THEN 'Sell' "
				+ " END AS buy_sell, b.currency AS currency FROM `own_v5_own_insider_trans`t JOIN own_v5_own_stakes_detail s "
				+ " ON s.report_date=t.effective_date "
				+ " AND s.fsym_id=t.fsym_id "
				+ " AND s.factset_entity_id=t.factset_entity_id "
				+ " JOIN ( SELECT company_id, security_id FROM cm.company_list c "
				+ " WHERE c.security_id=:securityId ORDER BY domicile_flag DESC limit 1) c "
				+ " ON c.security_id=s.fsym_id "
				+ " JOIN ff_v3_ff_basic_cf b "
				+ " ON b.fsym_id=c.company_id "
				+ " JOIN sym_v1_sym_entity e "
				+ " ON t.factset_entity_id=e.factset_entity_id "
				+ " WHERE t.`fsym_id`=:securityId "
				+ " AND effective_date >=(date_sub(curdate(),interval 1 year)) ORDER BY t.effective_date DESC) y ORDER BY effective_date DESC) x JOIN ( SELECT @row_number\\:=0) AS a ;";
		
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(InsiderTransaction.class);
		query.setParameter("securityId", securityId);
		
		@SuppressWarnings("unchecked")
		List<InsiderTransaction> data =  query.list();
		_log.info("data :::: getTransactionData Call Done");
		List<InsiderTransactionDTO> insiderTransactionDTO = (DozerHelper.map(dozerBeanMapper, data, InsiderTransactionDTO.class));
		return insiderTransactionDTO;
	}

	@Override
	public List<EventsDTO> getEventsData(String entityId, Integer years) {
		
		_log.info("extracting events Data from the database" );
		
		StringBuilder baseQuery = new StringBuilder();
		/*baseQuery.append("select (@row_number\\:=@row_number+1) as id,event_id, event_type,event_datetime_utc, "
				+ " title, url_pr, report_id,transcript_completed, webcast_live, webcast_replay, "
				+ " slide_id, webcast_slides "
				+ " from "
				+ " (SELECT  e.event_id, e.event_type,e.event_datetime_utc, e.title, e.url_pr, r.report_id, "
				+ " r.transcript_completed, r.webcast_live, r.webcast_replay, rs.slide_id, "
				+ " rs.webcast_slides "
				+ " FROM evt_v1_ce_events_coverage ec "
				+ " inner join evt_v1_ce_events e on e.event_id=ec.event_id "
				+ " left join evt_v1_ce_reports r on r.event_id=e.event_id "
				+ " left join evt_v1_ce_report_slides rs on rs.report_id=r.report_id "
				+ " where ec.factset_entity_id=:entityId ");
		if(years!=null){
			baseQuery.append(" AND e.event_datetime_utc >= date_sub(curdate(),interval :years year) ");
		}
		baseQuery.append(" ORDER BY e.event_datetime_utc DESC) x "
				+ " join (select @row_number\\:=0) as t");*/
		
		baseQuery.append("select (@row_number\\:=@row_number+1) as id,event_id, event_type,event_datetime_utc, title, url_pr, report_id,transcript_completed, webcast_live, webcast_replay, slide_id, webcast_slides "
				+ " FROM "
				+ " ("
				+ " SELECT  e.event_id, e.event_type,e.event_datetime_utc, e.title, e.url_pr, r.report_id, "
				+ " r.transcript_completed, r.webcast_live, r.webcast_replay, rs.slide_id, "
				+ " rs.webcast_slides "
				+ " FROM evt_v1_ce_events_coverage ec "
				+ " inner join evt_v1_ce_events e on e.event_id=ec.event_id "
				+ " left join evt_v1_ce_reports r on r.event_id=e.event_id "
				+ " left join evt_v1_ce_report_slides rs on rs.report_id=r.report_id "
				+ " where ec.factset_entity_id=:entityId"
				+ " AND e.event_datetime_utc > CURDATE()"
				+ " UNION"
				+ " SELECT  e.event_id, e.event_type,e.event_datetime_utc, e.title, e.url_pr, r.report_id, "
				+ " r.transcript_completed, r.webcast_live, r.webcast_replay, rs.slide_id, "
				+ " rs.webcast_slides "
				+ " FROM evt_v1_ce_events_coverage ec "
				+ " inner join evt_v1_ce_events e on e.event_id=ec.event_id "
				+ " inner join evt_v1_ce_reports r on r.event_id=e.event_id and r.transcript_completed is not null"
				+ " inner join evt_v1_ce_report_slides rs on rs.report_id=r.report_id "
				+ " where ec.factset_entity_id=:entityId"
				+ " AND e.event_datetime_utc BETWEEN date_sub(curdate(),interval :years year) AND CURDATE()"
				+ " ) x"
				+ " join (select @row_number\\:=0) as t "
				+ " ORDER BY event_datetime_utc DESC");
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery.toString()).addEntity(Events.class);
		query.setParameter("entityId", entityId);
		query.setParameter("years", years);
		
		@SuppressWarnings("unchecked")
		List<Events> data =  query.list();
		_log.info("data :::: getEventsData call Done");
		List<EventsDTO> eventsDTO = (DozerHelper.map(dozerBeanMapper, data, EventsDTO.class));
		return eventsDTO;
	}
	
	@Override
	public List<CompanyDocumentMetadataDTO> companyFilings(String companyId, Integer years) {
		
		_log.info("extracting company filing data" );
		StringBuilder baseQuery = new StringBuilder();
		baseQuery.append("SELECT comapny_documents_metadata_id, ticker_country_code, `key`, req_sym, period, headline, `source`, "
				+ " cast(story_date as date) as story_date, cast(story_time as time) as story_time, link1, transcript_expected, "
				+ " report_expected, filing_size, "
				+ " case when cdt.name is not null then cdt.name else cdm.form_type end as form_type, "
				+ " all_ids, search_ids, count, categories, industries, sa_categories, security_info, filing_size "
				+ " FROM cm.company_document_metadata cdm "
				+ " JOIN cm.company_list cl on cdm.ticker_country_code = cl.ticker_exchange "
				+ " LEFT JOIN cm.company_document_type_mapping cdt on UPPER(TRIM(cdt.form_type)) = UPPER(TRIM(cdm.form_type)) " 
				+ " WHERE cl.company_id=:companyId and cdm.form_type not in :formType ");
		if(years!=null){
			baseQuery.append(" AND story_date >= date_sub(curdate(),interval :years year)");
		}
		baseQuery.append(" group by `key` ORDER BY story_date DESC, story_time DESC");
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery.toString()).addEntity(CompanyDocumentMetadata.class);
		query.setParameter("companyId", companyId);
		query.setParameterList("formType", CMStatic.FACTSET_FILING_EXCLUDED_FORM_TYPE);
		
		if(years!=null){
			query.setParameter("years", years);
		}
		
		@SuppressWarnings("unchecked")
		List<CompanyDocumentMetadata> data =  query.list();
		_log.info("data :::: CcompanyFilings  Done" );
		List<CompanyDocumentMetadataDTO> companyDocumentMetadataDTOs = (DozerHelper.map(dozerBeanMapper, data, CompanyDocumentMetadataDTO.class));
		return companyDocumentMetadataDTOs;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyDocumentMetadataDTO> companyFilings(String companyId, Integer years, Integer PageNo,String formType) {

		_log.info("extracting company filing data");
		StringBuilder baseQuery = new StringBuilder();
		baseQuery.append(
				"SELECT * FROM (SELECT comapny_documents_metadata_id,(SELECT count(comapny_documents_metadata_id)/50 FROM cm.company_document_metadata incdm");
		baseQuery.append(" JOIN cm.company_list incl on incdm.ticker_country_code = incl.ticker_exchange ");
		if(formType==null || formType.equals("")){
		baseQuery.append(" WHERE incl.company_id=:companyId and incdm.form_type not in :formType");
		}
		else{
		baseQuery.append(" WHERE incl.company_id=:companyId and incdm.form_type =:formType");
		}
		if (years != null) {
			baseQuery.append(" AND story_date >= date_sub(curdate(),interval :years year)");
		}
		baseQuery.append(" ) as count,");
		baseQuery.append(" ticker_country_code, `key`, req_sym, period, headline, `source`, ");
		baseQuery.append(
				" cast(story_date as date) as story_date, cast(story_time as time) as story_time, link1, transcript_expected, ");
		baseQuery.append(" report_expected, ");
		baseQuery.append(" case when cdt.name is not null then cdt.name else cdm.form_type end as form_type, ");
		baseQuery.append(" all_ids, search_ids, categories, industries, sa_categories, security_info, filing_size ");
		baseQuery.append(" FROM cm.company_document_metadata cdm ");
		baseQuery.append(" JOIN cm.company_list cl on cdm.ticker_country_code = cl.ticker_exchange ");
		baseQuery.append(
				" LEFT JOIN cm.company_document_type_mapping cdt on UPPER(TRIM(cdt.form_type)) = UPPER(TRIM(cdm.form_type)) ");
		if(formType==null || formType.equals("")){
		baseQuery.append(" WHERE cl.company_id=:companyId and cdm.form_type not in :formType ");
		}
		else{
			baseQuery.append(" WHERE cl.company_id=:companyId  ");
		}
		if (years != null) {
			baseQuery.append(" AND story_date >= date_sub(curdate(),interval :years year)");
		}
		baseQuery.append(" group by `key` ORDER BY story_date DESC, story_time DESC) x ");
		if(formType!=null && !formType.equals("")){
		baseQuery.append(" WHERE form_type=:formType");
		}
		baseQuery.append("  LIMIT :PageNo,50");
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery.toString())
				.addEntity(CompanyDocumentMetadata.class);
		query.setParameter("companyId", companyId);
		if(formType==null || formType.equals("")){
		query.setParameterList("formType", CMStatic.FACTSET_FILING_EXCLUDED_FORM_TYPE);
		}
		else{
			query.setParameter("formType", formType);	
		}

		if (years != null) {
			query.setParameter("years", years);
		}
		query.setParameter("PageNo", PageNo * 50);

		List<CompanyDocumentMetadata> data = query.list();
		
		
		_log.info("companyFilings method :::: completed ");
		List<CompanyDocumentMetadataDTO> companyDocumentMetadataDTOs = (DozerHelper.map(dozerBeanMapper, data,
				CompanyDocumentMetadataDTO.class));
		
		return companyDocumentMetadataDTOs;
	}

	@Override
	public TranscriptFileNameDTO getTranscriptFileName(String reportId) {
		_log.info("extracting Trnscript File Name from DB for report Id: "+ reportId);
		
		String baseQuery = "select r.report_id, concat(replace(cast(cast(event_datetime_utc as date) as char),'-',''),'-',cast(r.report_id as char),'-',replace(r.transcript_completed,'r','t'),'.xml') as file_name, e.event_type\n" + 
				"from evt_v1_ce_reports r\n" + 
				"join evt_v1_ce_events e on r.event_id = e.event_id\n" + 
				"where r.report_id = :reportId";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(TranscriptFileName.class);
		query.setParameter("reportId", reportId);
		
		@SuppressWarnings("unchecked")
		List<TranscriptFileName> data =  query.list();
		
		List<TranscriptFileNameDTO> fileNameDTOs = (DozerHelper.map(dozerBeanMapper, data, TranscriptFileNameDTO.class));
		
		_log.debug("Trnscript File Name: "+ fileNameDTOs.get(0).getFileName() +", for report Id: "+ reportId);
		
		return fileNameDTOs.get(0);
	}

	
}
