package com.televisory.capitalmarket.daoimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.dao.CMFinancialDataRepository;
import com.televisory.capitalmarket.dto.BenchMarkCompanyDTO;
import com.televisory.capitalmarket.dto.BenchMarkCompanyOldDTO;
import com.televisory.capitalmarket.dto.BenchmarkCompanyNewDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;
import com.televisory.capitalmarket.entities.cm.BenchMarkCompany;
import com.televisory.capitalmarket.entities.cm.BenchMarkCompanyOld;
import com.televisory.capitalmarket.entities.cm.BenchmarkCompanyNew;
import com.televisory.capitalmarket.entities.factset.FFCompanyFinancialData;
import com.televisory.capitalmarket.entities.factset.FFCompanyFinancialDataMIN;
import com.televisory.capitalmarket.entities.factset.FFLatestFilingInfo;
import com.televisory.capitalmarket.entities.factset.PrivateCompanyFinancialData;
import com.televisory.capitalmarket.entities.factset.PvtIndCompanyFinancialData;
import com.televisory.capitalmarket.service.CapitalMarketService;
import com.televisory.capitalmarket.util.DozerHelper;


@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class CMFinancialDataRepositoryImpl implements CMFinancialDataRepository {

	Logger _log = Logger.getLogger(this.getClass());


	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factsetSessionFactory;
	
	@Autowired
	@Qualifier(value="pcDataSessionFactory")
	private SessionFactory pcDataSessionFactory;

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Autowired
	@Lazy
	CapitalMarketService capitalMarketService;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<Map<String, Object>> getCompanyRelativePerformance(String companyId, String indexId, String currency) {

		Query query =cmSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_relative_performance(:companyId,:indexId,:currency)")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("companyId",companyId);
		query.setParameter("indexId",indexId);
		query.setParameter("currency",currency);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = query.list();	
		return data;
	}



	/*	@Override
	public List<CompanyFinancialDTO> getCompanykeyFinancials(String companyId, String keyFinancials, Date startDate, Date endDate) {

		_log.info("getting key financials data for: "+ companyId +". Params: "+ keyFinancials);

		Query query = sessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_key_financials(:companyId,:keyFinancials,:startDate,:endDate)")
				.addEntity(FFCompanyFinancialData.class);


		query.setParameter("companyId", companyId);
		query.setParameter("keyFinancials", keyFinancials);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);


		@SuppressWarnings("unchecked")
		List<FFCompanyFinancialData> data = query.list();

		List<CompanyFinancialDTO> companyFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, CompanyFinancialDTO.class);

		return companyFinancialDataDTO;
	}*/

	public CompanyFinancialDTO getCompanyLatestBasicBSFinancial(String companyId, String fieldName, String currencyCode) {
		_log.info("Extracting Latest annual data from ff_v3_ff_basic_af for company Id: "+ companyId +", feild name: "+ fieldName +", currency: "+ currencyCode);

		String baseQuery;

		if(currencyCode == null) {

			baseQuery = "select * from " + 
					"( " + 
					"select ad.id, ad.fsym_id, null as financial_stmt_code, amd.display_order, amd.display_level, amd.field_name, amd.section, amd.short_name, amd.description, amd.display_name, amd.key_parameter, amd.key_parameter_order, ad.date, get_applicable_quarter(ad.date) as applicable_date, ad.currency, ad.data, amd.ic_flag, amd.screener_flag, amd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +" as data, currency FROM factset.ff_v3_ff_basic_af af where `fsym_id` = :companyId) ad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)amd " + 
					"on amd.field_name = ad.field_name " + 
					"union all " + 
					"select qd.id, qd.fsym_id, null as financial_stmt_code, qmd.display_order, qmd.display_level, qmd.field_name, qmd.section, qmd.short_name, qmd.description, qmd.display_name, qmd.key_parameter, qmd.key_parameter_order, qd.date, get_applicable_quarter(qt.date) as applicable_date, qd.currency, qd.data, qmd.ic_flag, qmd.screener_flag, qmd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +" as data, currency FROM factset.ff_v3_ff_basic_qf qf where `fsym_id` = :companyId) qd " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)qmd " + 
					"on qmd.field_name = qd.field_name " + 
					"union all " + 
					"select sad.id, sad.fsym_id, null as financial_stmt_code, samd.display_order, samd.display_level, samd.field_name, samd.section, samd.short_name, samd.description, samd.display_name, samd.key_parameter, samd.key_parameter_order, sad.date, get_applicable_quarter(sad.date) as applicable_date, sad.currency, sad.data, samd.ic_flag, samd.screener_flag, samd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +" as data, currency FROM factset.ff_v3_ff_basic_saf saf where `fsym_id` = :companyId) sad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)samd " + 
					"on samd.field_name = sad.field_name " + 
					")z  where data is NOT null " + 
					"order by `date` desc limit 1";

		} else {
			currencyCode = currencyCode.toUpperCase();

			/*baseQuery = "select a.id, a.fsym_id, null as financial_stmt_code, y.display_order, y.display_level, y.field_name, y.description, a.date, '"+currencyCode+"' as currency, a.data  from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, date) as data, currency FROM factset.ff_v3_ff_basic_af where `fsym_id` = :companyId)a " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)y " + 
					"on y.field_name = a.field_name order by `date` desc limit 1";*/

			baseQuery = "select * from " + 
					"( " + 
					"select ad.id, ad.fsym_id, null as financial_stmt_code, amd.display_order, amd.display_level, amd.field_name, amd.section, amd.short_name, amd.description, amd.display_name, amd.key_parameter, amd.key_parameter_order, ad.date, get_applicable_quarter(ad.date) as applicable_date, '"+currencyCode+"' as currency, ad.data, amd.ic_flag, amd.screener_flag, amd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, af.date) as data, currency FROM factset.ff_v3_ff_basic_af af where `fsym_id` = :companyId) ad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)amd " + 
					"on amd.field_name = ad.field_name " + 
					"union all " + 
					"select qd.id, qd.fsym_id, null as financial_stmt_code, qmd.display_order, qmd.display_level, qmd.field_name, qmd.section, qmd.short_name, qmd.description, qmd.display_name, qmd.key_parameter, qmd.key_parameter_order, qd.date, get_applicable_quarter(qd.date) as applicable_date, '"+currencyCode+"' as currency, qd.data, qmd.ic_flag, qmd.screener_flag, qmd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, qf.date) as data, currency FROM factset.ff_v3_ff_basic_qf qf where `fsym_id` = :companyId) qd " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)qmd " + 
					"on qmd.field_name = qd.field_name " + 
					"union all " + 
					"select sad.id, sad.fsym_id, null as financial_stmt_code, samd.display_order, samd.display_level, samd.field_name, samd.section, samd.short_name, samd.description, samd.display_name, samd.key_parameter, samd.key_parameter_order, sad.date, get_applicable_quarter(sad.date) as applicable_date, '"+currencyCode+"' as currency, sad.data, samd.ic_flag, samd.screener_flag, samd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, saf.date) as data, currency FROM factset.ff_v3_ff_basic_saf saf where `fsym_id` = :companyId) sad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)samd " + 
					"on samd.field_name = sad.field_name " + 
					")z  where data is NOT null " + 
					"order by `date` desc limit 1";
		}

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFCompanyFinancialData.class);
		query.setParameter("fieldName", fieldName);

		query.setParameter("companyId",companyId);

		if(currencyCode != null) {
			query.setParameter("currencyCode",currencyCode);
		}

		List<FFCompanyFinancialData> companyFinancialData = null;
		CompanyFinancialDTO companyFinancialDataDTO=null;

		try {		
			companyFinancialData = query.list();	
			if(!companyFinancialData.isEmpty()){
				companyFinancialDataDTO = DozerHelper.map(dozerBeanMapper, companyFinancialData, CompanyFinancialDTO.class).get(0);
			}
		} catch (Exception e) {
			throw e;
		}
		return companyFinancialDataDTO;
	}

	public CompanyFinancialDTO getCompanyLatestBasicDerivedAnnualFinancial(String companyId, String fieldName, String currencyCode) {

		_log.info("Extracting Latest annual data from ff_v3_ff_basic_der_af for company Id: "+ companyId +", feild name: "+ fieldName +", currency: "+ currencyCode);

		String baseQuery;
		if(currencyCode == null) {
			baseQuery = "select * from " + 
					"( " + 
					"select ad.id, ad.fsym_id, null as financial_stmt_code, amd.display_order, amd.display_level, amd.field_name, amd.section, amd.short_name, amd.description, amd.display_name, amd.key_parameter, amd.key_parameter_order, ad.date, get_applicable_quarter(ad.date) as applicable_date, ad.currency, ad.data, amd.ic_flag, amd.screener_flag, amd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +" as data, currency FROM factset.ff_v3_ff_basic_der_af af where `fsym_id` = :companyId) ad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)amd " + 
					"on amd.field_name = ad.field_name " + 
					"union all " + 
					"select qd.id, qd.fsym_id, null as financial_stmt_code, qmd.display_order, qmd.display_level, qmd.field_name, qmd.section, qmd.short_name, qmd.description, qmd.display_name, qmd.key_parameter, qmd.key_parameter_order, qd.date, get_applicable_quarter(qd.date) as applicable_date, qd.currency, qd.data, qmd.ic_flag, qmd.screener_flag, qmd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +") as data, currency FROM factset.ff_v3_ff_basic_der_qf qf, (select @maxDate\\:=max(date)  from factset.ff_v3_ff_basic_der_qf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and qf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 4)qd " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)qmd " + 
					"on qmd.field_name = qd.field_name " + 
					"union all " + 
					"select sad.id, sad.fsym_id, null as financial_stmt_code, samd.display_order, samd.display_level, samd.field_name, samd.section, samd.short_name, samd.description, samd.display_name, samd.key_parameter, samd.key_parameter_order, sad.date, get_applicable_quarter(sad.date) as applicable_date, sad.currency, sad.data, samd.ic_flag, samd.screener_flag, samd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +") as data, currency FROM factset.ff_v3_ff_basic_der_saf saf, (select @maxDate\\:=max(date) as date  from factset.ff_v3_ff_basic_der_saf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and saf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 2)sad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)samd " + 
					"on samd.field_name = sad.field_name " + 
					")z  where data is NOT null " + 
					"order by `date` desc limit 1";

		} else {
			currencyCode = currencyCode.toUpperCase();
			/*baseQuery = "select a.id, a.fsym_id, null as financial_stmt_code, y.display_order, y.display_level, y.field_name, y.description, a.date, '"+currencyCode+"' as currency, a.data  from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, date) as data, currency FROM factset.ff_v3_ff_basic_der_af where `fsym_id` = :companyId)a " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)y " + 
					"on y.field_name = a.field_name order by `date` desc limit 1";*/

			baseQuery = "select * from " + 
					"( " + 
					"select ad.id, ad.fsym_id, null as financial_stmt_code, amd.display_order, amd.display_level, amd.field_name, amd.section, amd.short_name, amd.description, amd.display_name, amd.key_parameter, amd.key_parameter_order, ad.date, get_applicable_quarter(ad.date) as applicable_date, '"+currencyCode+"' as currency, ad.data, amd.ic_flag, amd.screener_flag, amd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, af.date) as data, currency FROM factset.ff_v3_ff_basic_der_af af where `fsym_id` = :companyId) ad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)amd " + 
					"on amd.field_name = ad.field_name " + 
					"union all " + 
					"select qd.id, qd.fsym_id, null as financial_stmt_code, qmd.display_order, qmd.display_level, qmd.field_name, qmd.section, qmd.short_name, qmd.description, qmd.display_name, qmd.key_parameter, qmd.key_parameter_order, qd.date, get_applicable_quarter(qd.date) as applicable_date, '"+currencyCode+"' as currency, qd.data, qmd.ic_flag, qmd.screener_flag, qmd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +")*factset.get_fx_year_conversion(currency, :currencyCode, @maxDate) as data, currency FROM factset.ff_v3_ff_basic_der_qf qf, (select @maxDate\\:=max(date)  from factset.ff_v3_ff_basic_der_qf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and qf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 4)qd " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)qmd " + 
					"on qmd.field_name = qd.field_name " + 
					"union all " + 
					"select sad.id, sad.fsym_id, null as financial_stmt_code, samd.display_order, samd.display_level, samd.field_name, samd.section, samd.short_name, samd.description, samd.display_name, samd.key_parameter, samd.key_parameter_order, sad.date, get_applicable_quarter(sad.date) as applicable_date, '"+currencyCode+"' as currency, sad.data, samd.ic_flag, samd.screener_flag, samd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +")*factset.get_fx_year_conversion(currency, :currencyCode, @maxDate) as data, currency FROM factset.ff_v3_ff_basic_der_saf saf, (select @maxDate\\:=max(date) as date  from factset.ff_v3_ff_basic_der_saf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and saf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 2)sad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)samd " + 
					"on samd.field_name = sad.field_name " + 
					")z  where data is NOT null " + 
					"order by `date` desc limit 1";
		}

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFCompanyFinancialData.class);
		query.setParameter("fieldName", fieldName);
		query.setParameter("companyId",companyId);

		if(currencyCode != null) {
			query.setParameter("currencyCode",currencyCode);
		}

		List<FFCompanyFinancialData> companyFinancialData = null;
		CompanyFinancialDTO companyFinancialDataDTO=null;

		try {		
			companyFinancialData = query.list();	
			companyFinancialDataDTO = DozerHelper.map(dozerBeanMapper, companyFinancialData, CompanyFinancialDTO.class).get(0);

		} catch (Exception e) {
			throw e;
		}
		return companyFinancialDataDTO;
	}

	public CompanyFinancialDTO getCompanyLatestAdvanceAnnualFinancial(String companyId, String fieldName, String currencyCode) {
		_log.info("Extracting Latest annual data from ff_v3_ff_advanced_af for company Id: "+ companyId +", feild name: "+ fieldName +", currency: "+ currencyCode);

		String baseQuery;

		if(currencyCode == null) {
			/*baseQuery = "select a.id, a.fsym_id, null as financial_stmt_code, y.display_order, y.display_level, y.field_name, y.description, a.date, a.currency, a.data  from " + 
				"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +" as data, currency FROM factset.ff_v3_ff_advanced_af where `fsym_id` = :companyId)a " + 
				"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)y " + 
				"on y.field_name = a.field_name order by `date` desc limit 1";*/

			baseQuery = "select * from " + 
					"( " + 
					"select ad.id, ad.fsym_id, null as financial_stmt_code, amd.display_order, amd.display_level, amd.field_name, amd.section, amd.short_name, amd.description, amd.display_name, amd.key_parameter, amd.key_parameter_order, ad.date, get_applicable_quarter(ad.date) as applicable_date, ad.currency, ad.data, amd.ic_flag, amd.screener_flag, amd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +" as data, currency FROM factset.ff_v3_ff_advanced_af af where `fsym_id` = :companyId) ad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)amd " + 
					"on amd.field_name = ad.field_name " + 
					"union all " + 
					"select qd.id, qd.fsym_id, null as financial_stmt_code, qmd.display_order, qmd.display_level, qmd.field_name, qmd.section, qmd.short_name, qmd.description, qmd.display_name, qmd.key_parameter, qmd.key_parameter_order, qd.date, get_applicable_quarter(qd.date) as applicable_date, qd.currency, qd.data, qmd.ic_flag, qmd.screener_flag, qmd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +") as data, currency FROM factset.ff_v3_ff_advanced_qf qf, (select @maxDate\\:=max(date)  from factset.ff_v3_ff_advanced_qf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and qf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 4)qd " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)qmd " + 
					"on qmd.field_name = qd.field_name " + 
					"union all " + 
					"select sad.id, sad.fsym_id, null as financial_stmt_code, samd.display_order, samd.display_level, samd.field_name, samd.section, samd.short_name, samd.description, samd.display_name, samd.key_parameter, samd.key_parameter_order, sad.date, get_applicable_quarter(sad.date) as applicable_date, sad.currency, sad.data, samd.ic_flag, samd.screener_flag, samd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +") as data, currency FROM factset.ff_v3_ff_advanced_saf saf, (select @maxDate\\:=max(date) as date  from factset.ff_v3_ff_advanced_saf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and saf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 2)sad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)samd " + 
					"on samd.field_name = sad.field_name " + 
					")z  where data is NOT null " + 
					"order by `date` desc limit 1";

		} else {
			currencyCode = currencyCode.toUpperCase();
			/*baseQuery = "select a.id, a.fsym_id, null as financial_stmt_code, y.display_order, y.display_level, y.field_name, y.description, a.date, '"+currencyCode+"' as currency, a.data  from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, date) as data, currency FROM factset.ff_v3_ff_advanced_af where `fsym_id` = :companyId)a " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)y " + 
					"on y.field_name = a.field_name order by `date` desc limit 1";*/
			baseQuery = "select * from " + 
					"( " + 
					"select ad.id, ad.fsym_id, null as financial_stmt_code, amd.display_order, amd.display_level, amd.field_name, amd.section, amd.short_name, amd.description, amd.display_name, amd.key_parameter, amd.key_parameter_order, ad.date, get_applicable_quarter(ad.date) as applicable_date, '"+currencyCode+"' as currency, ad.data, amd.ic_flag, amd.screener_flag, amd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, date,'"+ fieldName +"' as field_name, "+ fieldName +"*factset.get_fx_year_conversion(currency, :currencyCode, af.date) as data, currency FROM factset.ff_v3_ff_advanced_af af where `fsym_id` = :companyId) ad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)amd " + 
					"on amd.field_name = ad.field_name " + 
					"union all " + 
					"select qd.id, qd.fsym_id, null as financial_stmt_code, qmd.display_order, qmd.display_level, qmd.field_name, qmd.section, qmd.short_name, qmd.description, qmd.display_name, qmd.key_parameter, qmd.key_parameter_order, qd.date, get_applicable_quarter(qd.date) as applicable_date, '"+currencyCode+"' as currency, qd.data, qmd.ic_flag, qmd.screener_flag, qmd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +")*factset.get_fx_year_conversion(currency, :currencyCode, @maxDate) as data, currency FROM factset.ff_v3_ff_advanced_qf qf, (select @maxDate\\:=max(date)  from factset.ff_v3_ff_advanced_qf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and qf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 4)qd " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)qmd " + 
					"on qmd.field_name = qd.field_name " + 
					"union all " + 
					"select sad.id, sad.fsym_id, null as financial_stmt_code, samd.display_order, samd.display_level, samd.field_name, samd.section, samd.short_name, samd.description, samd.display_name, samd.key_parameter, samd.key_parameter_order, sad.date, get_applicable_quarter(sad.date) as applicable_date, '"+currencyCode+"' as currency, sad.data, samd.ic_flag, samd.screener_flag, samd.watchlist_flag from " + 
					"(SELECT 1 as id, fsym_id, @maxDate as date, '"+ fieldName +"' as field_name, sum("+ fieldName +")*factset.get_fx_year_conversion(currency, :currencyCode, @maxDate) as data, currency FROM factset.ff_v3_ff_advanced_saf saf, (select @maxDate\\:=max(date) as date  from factset.ff_v3_ff_advanced_saf where `fsym_id` = :companyId)m  where `fsym_id` = :companyId and saf.date between date_sub(@maxDate, interval 300 day) and @maxDate group by fsym_id having count(*) = 2)sad " + 
					"join (select * from ( select * from cm.balance_model where field_name = :fieldName limit 1)x)samd " + 
					"on samd.field_name = sad.field_name " + 
					")z  where data is NOT null " + 
					"order by `date` desc limit 1";
		}

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFCompanyFinancialData.class);
		query.setParameter("fieldName", fieldName);

		query.setParameter("companyId",companyId);

		if(currencyCode != null) {
			query.setParameter("currencyCode",currencyCode);
		}

		List<FFCompanyFinancialData> companyFinancialData = null;
		CompanyFinancialDTO companyFinancialDataDTO=null;

		try {		
			companyFinancialData = query.list();	
			companyFinancialDataDTO = DozerHelper.map(dozerBeanMapper, companyFinancialData, CompanyFinancialDTO.class).get(0);

		} catch (Exception e) {
			throw e;
		}
		return companyFinancialDataDTO;
	}


	@Override
	public  List<CompanyFinancialMINDTO> getCompanyFinancial(String companyId, String financialType, String periodType, Date startDate, Date endDate, String currency, Boolean restated) throws Exception{

		_log.info("getting financial Data for company: "+ companyId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency +", restated: "+ restated);

		String procedureQuery = "CALL cm.get_advanced_fin_records(:companyId,:periodType,:financialType,:startDate,:endDate,:currency)";

		if(restated != null && restated == false)
			procedureQuery = "CALL cm.get_advanced_fin_records_nonrestated(:companyId,:periodType,:financialType,:startDate,:endDate,:currency)";

		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(FFCompanyFinancialDataMIN.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("companyId",companyId);
		financialDataQuery.setParameter("periodType",periodType);
		financialDataQuery.setParameter("financialType",financialType);
		financialDataQuery.setParameter("startDate",stDate);
		financialDataQuery.setParameter("endDate",enDate);
		financialDataQuery.setParameter("currency",currency);

		List<FFCompanyFinancialDataMIN> companyFinancialData = null;
		List<CompanyFinancialMINDTO> companyFinancialDataDTO=null;

		try {		
			companyFinancialData = financialDataQuery.list();	
			companyFinancialDataDTO = DozerHelper.map(dozerBeanMapper, companyFinancialData, CompanyFinancialMINDTO.class);
			companyFinancialDataDTO.sort(Comparator.comparing(CompanyFinancialMINDTO::getPeriod).reversed());
		} catch (Exception e) {
			throw e;
		}
		return companyFinancialDataDTO;
	}
	
	@Override
	public  List<CompanyFinancialMINDTO> getPrivateCompanyFinancial(String companyId, String financialType, String periodType, Date startDate, Date endDate, String currency, Boolean restated) throws Exception{

		_log.info("getting private financial Data for company: "+ companyId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency +", restated: "+ restated);
		String params = null;
		String procedureQuery="";
		Query financialDataQuery=null;
		if(companyId!=null && !companyId.contains("-")) {
		 procedureQuery = "CALL ews.get_ews_fin_records(:companyId,:periodType,:financialType,:startDate,:endDate,:currency,:params)";
		 financialDataQuery = pcDataSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(PvtIndCompanyFinancialData.class);
		}
		else {
			 procedureQuery = "CALL pc.get_pc_fin_records(:companyId,:periodType,:financialType,:startDate,:endDate,:currency, :params)";
			 financialDataQuery = pcDataSessionFactory.getCurrentSession()
					.createSQLQuery(procedureQuery)
					.addEntity(PrivateCompanyFinancialData.class);
		}

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("companyId",companyId);
		financialDataQuery.setParameter("periodType",periodType);
		financialDataQuery.setParameter("financialType",financialType);
		financialDataQuery.setParameter("startDate",stDate);
		financialDataQuery.setParameter("endDate",enDate);
		financialDataQuery.setParameter("currency",currency);
		financialDataQuery.setParameter("params",params);

		List<? extends Object> companyFinancialData = null;
		List<CompanyFinancialMINDTO> companyFinancialDataDTO=null;

		try {		
			companyFinancialData = financialDataQuery.list();	
			companyFinancialDataDTO = DozerHelper.map(dozerBeanMapper, companyFinancialData, CompanyFinancialMINDTO.class);
			companyFinancialDataDTO.sort(Comparator.comparing(CompanyFinancialMINDTO::getPeriod).reversed());
		} catch (Exception e) {
			throw e;
		}
		return companyFinancialDataDTO;
	}
	

	@Override
	public List<CompanyFinancialMINDTO> getCompanyFinancial(String company, List<String> fieldName, String periodType, Date startDate, Date endDate,String currency) {
		if(fieldName==null || fieldName.isEmpty()){
			throw new RuntimeException("Field Name list is Blank");
			//return new ArrayList<CompanyFinancialMINDTO>();
		}
		String fieldNames=String.join(",", fieldName);

		_log.info("getting the financial data for the company : "+company+", Field Names : "+fieldNames +", startDate "+startDate+" endDate "+endDate+" periodType "+periodType+" currencyCode "+currency );

		String baseQuery ="Call cm.get_advanced_fin_records_params(:companyId,:periodType,:startDate,:endDate,:currency,:params)";

		Query query=  cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFCompanyFinancialDataMIN.class);


		query.setParameter("companyId",company);
		query.setParameter("periodType", periodType);
		query.setParameter("currency",currency);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		query.setParameter("params",fieldNames);

		List<FFCompanyFinancialDataMIN> data =(List<FFCompanyFinancialDataMIN>) query.list();

		List<CompanyFinancialMINDTO> financialDataDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyFinancialMINDTO.class);

		return financialDataDTOs;
	}

	@Override
	public List<BenchMarkCompanyOldDTO> getbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency) {
		Query query = factsetSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_benchmark_comp(:companyId,:currency,:resultLimit)")
				.addEntity(BenchMarkCompanyOld.class);

		query.setParameter("companyId", companyId);
		query.setParameter("currency", currency);
		query.setParameter("resultLimit", resultLimit);
		@SuppressWarnings("unchecked")
		List<BenchMarkCompanyOld> data = query.list();
		List<BenchMarkCompanyOldDTO> benchMarkCompanyDTO = DozerHelper.map(dozerBeanMapper, data, BenchMarkCompanyOldDTO.class);
		return benchMarkCompanyDTO;
	}

	@Override
	public List<BenchMarkCompanyDTO> getbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove, String period) {
		Query query = factsetSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_benchmark_comp(:companyId,:currency,:resultLimit)")
				.addEntity(BenchMarkCompany.class);

		query.setParameter("companyId", companyId);
		query.setParameter("currency", currency);
		query.setParameter("resultLimit", resultLimit);

		@SuppressWarnings("unchecked")
		List<BenchMarkCompany> data = query.list();

		List<BenchMarkCompanyDTO> benchMarkCompanyDTO = DozerHelper.map(dozerBeanMapper, data, BenchMarkCompanyDTO.class);

		return benchMarkCompanyDTO;
	}

	@Override
	public List<BenchMarkCompanyDTO> editbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency, String  benchmarkCompanyAdd, String benchmarkCompanyRemove, String periodType) {

		_log.info("getting benchmark company data from the database fro : companyId "+ companyId+" benchmarkCompanyAdd "+benchmarkCompanyAdd +" benchmarkCompanyRemove "+benchmarkCompanyRemove +" periodType "+periodType+" currency "+currency);

		Query query = cmSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_benchmark_companies(:companyId,:benchmarkCompanyAdd,:benchmarkCompanyRemove,:periodType,:currency)")
				.addEntity(BenchMarkCompany.class);

		query.setParameter("companyId", companyId);
		query.setParameter("benchmarkCompanyAdd", benchmarkCompanyAdd);
		query.setParameter("benchmarkCompanyRemove", benchmarkCompanyRemove);
		query.setParameter("periodType", periodType);
		query.setParameter("currency", currency);
		//query.setParameter("resultLimit", resultLimit);

		@SuppressWarnings("unchecked")
		List<BenchMarkCompany> data = query.list();

		List<BenchMarkCompanyDTO> benchMarkCompanyDTO = DozerHelper.map(dozerBeanMapper, data, BenchMarkCompanyDTO.class);

		return benchMarkCompanyDTO;
	}
	@Override
	public List<BenchmarkCompanyNewDTO> editbenchMarkCompaniesByEntityId(String companyId,Integer resultLimit,String currency, String  benchmarkCompanyAdd, String benchmarkCompanyRemove, String periodType, String entityType) {

		_log.info("getting benchmark company data from the database fro : companyId "+ companyId+" benchmarkCompanyAdd "+benchmarkCompanyAdd +" benchmarkCompanyRemove "+benchmarkCompanyRemove +" periodType "+periodType+" currency "+currency);

		Query query = cmSessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_benchmark_companies_pc(:companyId,:benchmarkCompanyAdd,:benchmarkCompanyRemove,:periodType,:currency, :entityType)")
				.addEntity(BenchmarkCompanyNew.class);

		query.setParameter("companyId", companyId);
		query.setParameter("benchmarkCompanyAdd", benchmarkCompanyAdd);
		query.setParameter("benchmarkCompanyRemove", benchmarkCompanyRemove);
		query.setParameter("periodType", periodType);
		query.setParameter("entityType", entityType);
		query.setParameter("currency", currency);
		//query.setParameter("resultLimit", resultLimit);

		@SuppressWarnings("unchecked")
		List<BenchmarkCompanyNew> data = query.list();

		List<BenchmarkCompanyNewDTO> benchMarkCompanyDTO = DozerHelper.map(dozerBeanMapper, data, BenchmarkCompanyNewDTO.class);

		return benchMarkCompanyDTO;
	}
	
	@Override
	public List<CompanyLatestFilingInfoDTO> getCompanyLatestFilingInfo(String companyId) {

		_log.info("Extracting Company Latest Financial Info for company Id: "+ companyId );

		String baseQuery = " (SELECT 'Yearly' as period_type, date, currency, fsym_id, ff_curn_doc FROM ff_v3_ff_advanced_af WHERE fsym_id = :companyId order by date desc limit 1) " + 
				" union all " + 
				" (SELECT 'Half-Yearly' as period_type, date, currency, fsym_id, ff_curn_doc FROM ff_v3_ff_advanced_saf WHERE fsym_id = :companyId order by date desc limit 1) " + 
				" union all " + 
				" (SELECT 'Quarterly' as period_type, date, currency, fsym_id, ff_curn_doc FROM ff_v3_ff_advanced_qf WHERE fsym_id = :companyId order by date desc limit 1) ";

		Query query = factsetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFLatestFilingInfo.class);
		query.setParameter("companyId",companyId);

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



	@Override
	public List<CompanyLatestFilingInfoDTO> getBenchmarkLatestFilingInfo(
			String companyId) {
		_log.info("Extracting Benchmark Company Latest Financial Info for company Id: "+ companyId );

		String baseQuery = "(SELECT 'Yearly' as periodType, a.date as period, a.currency as currency, a.company_id as companyId "
				+ "FROM cm.company_latest_financial_af a left "
				+ "join cm.company_list cl on cl.company_id=a.company_id "
				+ "WHERE a.company_id=:companyId order by date desc limit 1)  "
				+ "union all "
				+ "(SELECT 'Half-Yearly' as periodType, s.date as period, s.currency as currency, s.company_id as companyId "
				+ "FROM cm.company_latest_financial_saf s left join cm.company_list cl on cl.company_id=s.company_id "
				+ "WHERE s.company_id=:companyId order by date desc limit 1)  union all  "
				+ "(SELECT 'Quarterly' as periodType, q.date as period, q.currency as currency, q.company_id as companyId "
				+ "FROM cm.company_latest_financial_qf q left join cm.company_list cl on  cl.company_id=q.company_id "
				+ "WHERE q.company_id=:companyId order by date desc limit 1)";

		Query query = factsetSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("companyId",companyId);
		query.setResultTransformer(Transformers.aliasToBean(CompanyLatestFilingInfoDTO.class));

		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs=query.list();
		return companyLatestFilingInfoDTOs;
	}



	@Override
	public List<CompanyLatestFilingInfoDTO> getBenchmarkPeriods(String companyId) {
		_log.info("Extracting Benchmark Company Latest Financial Info for company Id: "+ companyId );

		String baseQuery = "select m.ff_report_freq_desc as periodType,a.ff_report_freq_code as statusCode, date as period,fsym_id as companyId "
				+ "FROM factset.ff_v3_ff_basic_af a join ref_v2_ff_report_freq_map m on a.ff_report_freq_code=m.ff_report_freq_code where fsym_id=:companyId order by date desc limit 1";

		Query query = factsetSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("companyId",companyId);
		query.setResultTransformer(Transformers.aliasToBean(CompanyLatestFilingInfoDTO.class));
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs=query.list();
		return companyLatestFilingInfoDTOs;
	}

}
