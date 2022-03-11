package com.televisory.capitalmarket.daoimpl;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.dao.ScreenerRepository;
import com.televisory.capitalmarket.dto.ScreenerCompanyFinancialDTO;
import com.televisory.capitalmarket.dto.ScreenerStockPriceDTO;
import com.televisory.capitalmarket.entities.cm.ScreenerCompanyFinancialData;
import com.televisory.capitalmarket.entities.factset.FFBasicCf;
import com.televisory.capitalmarket.entities.factset.StockPriceScreener;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class ScreenerRepositoryImpl implements ScreenerRepository{
	
	Logger _log = Logger.getLogger(ScreenerRepositoryImpl.class);
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmFactory;
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Override
	public List<ScreenerCompanyFinancialDTO> getCompanyYearly(String ticsIndustryCode, String countryIso) {
		_log.info("ticsIndustryCode :::: " + ticsIndustryCode + " :::: countryIsoList :::: " + countryIso );
		Query query = null;
		
		String baseQuery = "SELECT @rn\\:=@rn+1 as id,com.company_id as fsym_id ,null as financial_stmt_code,null as display_order,null as display_level,null as field_name, " + 
				"null as description, b.date as date,b.currency as currency,null as data " + 
				"FROM (SELECT @rn \\:= 0) a,cm.company_list com " + 
				"join factset.ff_v3_ff_basic_af b on b.fsym_id=com.company_id " + 
				"join factset.ff_v3_ff_sec_coverage s on b.fsym_id=s.fsym_id " + 
				"and year(b.date)=s.fa_latest_ann_update ";
		if(countryIso!=null){
			baseQuery = baseQuery + "where com.tics_industry_code=:ticsIndustryCode and com.is_active=1 AND com.domicile_country_code IN (:countryIsoList) and b.date >= date_sub(now(),interval 5 year) group by com.name;";
		}else{
			baseQuery = baseQuery + "where com.tics_industry_code=:ticsIndustryCode and com.is_active=1 and b.date >= date_sub(now(),interval 5 year) group by com.name;";
		}
		query = cmFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ScreenerCompanyFinancialData.class);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		if(countryIso!=null){
			List<String> countryIsoList = Arrays.asList(countryIso.split("\\s*,\\s*"));
			query.setParameterList("countryIsoList", countryIsoList);
		}
		List<ScreenerCompanyFinancialData> data = query.list();

		List<ScreenerCompanyFinancialDTO> companyDataDTO = DozerHelper.map(dozerBeanMapper, data, ScreenerCompanyFinancialDTO.class);

		return companyDataDTO;
	}
	
	@Override
	public List<ScreenerCompanyFinancialDTO> getCompanyFinancial(String subIndustryCode,String currency,String fieldName,String domicileCountryCode) {
		
		String fieldNames=String.join(",", fieldName);
		
		_log.info("getting the financial data for the subIndustryCode ::::: "+subIndustryCode+" Field Names : "+fieldNames + " domicileCountryCode ::: " + domicileCountryCode);
		_log.info(" domicileCountryCode  "+domicileCountryCode+" currency "+currency);
		String baseQuery ="Call cm.get_advance_fin_records_financial_multifields(:in_sub_industry_code,:in_dom_country,:in_field_names,:in_currency_code)";
		
		Query query=  cmFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ScreenerCompanyFinancialData.class);
		query.setParameter("in_sub_industry_code",subIndustryCode);
		query.setParameter("in_dom_country",domicileCountryCode);
		query.setParameter("in_field_names",fieldNames);
		query.setParameter("in_currency_code",currency);	
		
		List<ScreenerCompanyFinancialData> data =(List<ScreenerCompanyFinancialData>) query.list();
		/*_log.info(data.get(0));*/
		
		List<ScreenerCompanyFinancialDTO> financialDataDTOs = DozerHelper.map(dozerBeanMapper, data, ScreenerCompanyFinancialDTO.class);
		/*_log.info(financialDataDTOs.get(0));*/
		
		return financialDataDTOs;
	}
	
	@Override
	public List<ScreenerCompanyFinancialDTO> getCompanyFinancialRatio(String ticsIndustryCode,String currency,String fieldName,String domicileCountryCode) {
		
		String fieldNames=String.join(",", fieldName);
		
		_log.info("getting the financial ratio data for the ticsIndustryCode : "+ticsIndustryCode+" Field Names : "+fieldNames);
		_log.info(" domicileCountryCode  "+domicileCountryCode+" currency "+currency);
		String baseQuery ="Call cm.get_advance_fin_records_ratio_multifields(:in_sub_industry_code,:in_dom_country,:in_field_names,:in_currency_code)";
		
		Query query=  cmFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ScreenerCompanyFinancialData.class);
		query.setParameter("in_sub_industry_code",ticsIndustryCode);
		query.setParameter("in_dom_country",domicileCountryCode);
		query.setParameter("in_field_names",fieldNames);
		query.setParameter("in_currency_code",currency);	
		
		List<ScreenerCompanyFinancialData> data =(List<ScreenerCompanyFinancialData>) query.list();
		_log.info(data.size());
		
		List<ScreenerCompanyFinancialDTO> financialDataDTOs = DozerHelper.map(dozerBeanMapper, data, ScreenerCompanyFinancialDTO.class);
		_log.info(financialDataDTOs.get(0));
		
		return financialDataDTOs;
	}

	@Override
	public List<ScreenerStockPriceDTO> getCompaniesLatestStockPrice(
			String ticsIndustryCode, String currencyCode, List<String> countryIsoList) {
		
		_log.info("extracting latest stock price for the TICS Industry Code: " + ticsIndustryCode + " and Currency  : " + currencyCode +"countryIsoList :: "+countryIsoList);
		
		/*String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, fc2.fsym_id, v3b.date as p_date, ";
		baseQuery += " :currencyCode as currency, ";
		baseQuery += " fc2.p_price * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price,"
		+ " fc2.p_price_high * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_high,"
		+ " fc2.p_price_low * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_low,"
		+ " fc2.p_price_open * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_open,"
		+ " fc2.p_volume *1000 as p_volume,"
		+ " cm1.company_id as company_id,"
		+ " cm1.name as company_name,"
		+ " cl.country_iso_code_3 as country_id, "
		+ " cl.country_name as country_name "
		+ " from cm.company_list as cm1 "
		+ " INNER JOIN factset.ff_v3_ff_sec_coverage as v3c on v3c.fsym_id = cm1.company_id"
		+ " INNER JOIN factset.ff_v3_ff_basic_af v3b on v3b.fsym_id = v3c.fsym_id and v3c.fa_latest_ann_update = year(v3b.date)"
		+ " INNER JOIN cm.country_list as cl ON cl.country_iso_code_3 = cm1.domicile_country_code "
		+ " INNER JOIN factset.fp_v2_fp_sec_coverage as fc1 ON fc1.fsym_id = cm1.company_id "
		+ " INNER JOIN factset.fp_v2_fp_basic_prices AS fc2 ON fc2.fsym_id = fc1.fsym_id"
		+ " AND fc2.p_date = (select * from (SELECT max(`p_date`) FROM factset.`fp_v2_fp_prices_last_exch`)x) , (SELECT @row_number\\:=0) AS t"
		+ " WHERE  cm1.tics_industry_code LIKE  :ticsIndustryCode	AND  cm1.company_active_flag =1 group by cm1.name ";*/
		
		
		/*String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id,x.* from (select fc2.fsym_id, v3b.date as p_date,  :currencyCode as currency,  fc2.p_price * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price, fc2.p_price_high * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_high, fc2.p_price_low * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_low, fc2.p_price_open * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_open, fc2.p_volume *1000 as p_volume, cm1.company_id as company_id, cm1.name as company_name, cl.country_iso_code_3 as country_id,  cl.country_name as country_name,cm1.domicile_flag  from cm.company_list as cm1  INNER JOIN factset.ff_v3_ff_sec_coverage as v3c on v3c.fsym_id = cm1.company_id INNER JOIN factset.ff_v3_ff_basic_af v3b on v3b.fsym_id = v3c.fsym_id and v3c.fa_latest_ann_update = year(v3b.date) INNER JOIN cm.country_list as cl ON cl.country_iso_code_3 = cm1.domicile_country_code  INNER JOIN factset.fp_v2_fp_sec_coverage as fc1 ON fc1.fsym_id = cm1.company_id  INNER JOIN factset.fp_v2_fp_basic_prices AS fc2 ON fc2.fsym_id = fc1.fsym_id  WHERE  cm1.tics_industry_code LIKE  :ticsIndustryCode AND fc2.p_date >=date_sub(curdate(),interval 3 month) AND  cm1.company_active_flag =1 order by fc2.p_date desc)x join (SELECT @row_number\\:=0)AS t group by company_id order by domicile_flag desc";*/
		String baseQuery = "";
		/*if(countryIsoList!=null && countryIsoList.size()>0){
			baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id,y.* from (select x.* from (select fc2.fsym_id, v3b.date as p_date,  :currencyCode as currency, fc2.p_price * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price, fc2.p_price_high * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_high, fc2.p_price_low * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_low, fc2.p_price_open * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_open, fc2.p_volume *1000 as p_volume, cm1.company_id as company_id, cm1.name as company_name, cl.country_iso_code_3 as country_id,  cl.country_name as country_name,cm1.domicile_flag from cm.company_list as cm1  INNER JOIN factset.ff_v3_ff_sec_coverage as v3c on v3c.fsym_id = cm1.company_id INNER JOIN factset.ff_v3_ff_basic_af v3b on v3b.fsym_id = v3c.fsym_id and v3c.fa_latest_ann_update = year(v3b.date) INNER JOIN cm.country_list as cl ON cl.country_iso_code_3 = cm1.domicile_country_code  INNER JOIN factset.fp_v2_fp_sec_coverage as fc1 ON fc1.fsym_id = cm1.company_id  INNER JOIN factset.fp_v2_fp_basic_prices AS fc2 ON fc2.fsym_id = fc1.fsym_id  WHERE  cm1.tics_industry_code LIKE :ticsIndustryCode AND fc2.p_date >=date_sub(curdate(),interval 3 month)    AND  cm1.company_active_flag =1 AND cm1.domicile_country_code IN (:countryIsoList)  order by fc2.p_date desc)x order by domicile_flag desc) y join (SELECT @row_number\\:=0)AS t group by company_name order by domicile_flag desc";
		}else{
			baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id,y.* from (select x.* from (select fc2.fsym_id, v3b.date as p_date,  :currencyCode as currency, fc2.p_price * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price, fc2.p_price_high * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_high, fc2.p_price_low * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_low, fc2.p_price_open * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_open, fc2.p_volume *1000 as p_volume, cm1.company_id as company_id, cm1.name as company_name, cl.country_iso_code_3 as country_id,  cl.country_name as country_name,cm1.domicile_flag from cm.company_list as cm1  INNER JOIN factset.ff_v3_ff_sec_coverage as v3c on v3c.fsym_id = cm1.company_id INNER JOIN factset.ff_v3_ff_basic_af v3b on v3b.fsym_id = v3c.fsym_id and v3c.fa_latest_ann_update = year(v3b.date) INNER JOIN cm.country_list as cl ON cl.country_iso_code_3 = cm1.domicile_country_code  INNER JOIN factset.fp_v2_fp_sec_coverage as fc1 ON fc1.fsym_id = cm1.company_id  INNER JOIN factset.fp_v2_fp_basic_prices AS fc2 ON fc2.fsym_id = fc1.fsym_id  WHERE  cm1.tics_industry_code LIKE :ticsIndustryCode AND fc2.p_date >=date_sub(curdate(),interval 3 month)    AND  cm1.company_active_flag =1  order by fc2.p_date desc)x order by domicile_flag desc) y join (SELECT @row_number\\:=0)AS t group by company_name order by domicile_flag desc";
		}*/
		if(countryIsoList!=null && countryIsoList.size()>0){
			baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id,y.* from (select x.* from (select fc2.fsym_id, v3b.date as p_date,fc2.p_date as price_date,  :currencyCode as currency, fc2.p_price * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price, fc2.p_price_high * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_high, fc2.p_price_low * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_low, fc2.p_price_open * factset.get_fx_daily_conversion(fc2.currency,:currencyCode,fc2.p_date) as p_price_open, fc2.p_volume *1000 as p_volume, cm1.company_id as company_id, cm1.name as company_name, cl.country_iso_code_3 as country_id,  cl.country_name as country_name,cm1.domicile_flag from cm.company_list as cm1  INNER JOIN factset.ff_v3_ff_sec_coverage as v3c on v3c.fsym_id = cm1.company_id INNER JOIN factset.ff_v3_ff_basic_af v3b on v3b.fsym_id = v3c.fsym_id and v3c.fa_latest_ann_update = year(v3b.date) INNER JOIN cm.country_list as cl ON cl.country_iso_code_3 = cm1.domicile_country_code  INNER JOIN factset.fp_v2_fp_sec_coverage as fc1 ON fc1.fsym_id = cm1.company_id  INNER JOIN factset.fp_v2_fp_basic_prices AS fc2 ON fc2.fsym_id = fc1.fsym_id  WHERE  cm1.tics_industry_code LIKE :ticsIndustryCode AND fc2.p_date >=date_sub(curdate(),interval 3 month)    AND  cm1.company_active_flag =1 AND cm1.domicile_country_code IN (:countryIsoList)  order by fc2.p_date desc)x order by domicile_flag desc, price_date desc) y join (SELECT @row_number\\:=0)AS t group by company_name order by domicile_flag desc";
		}else{
			baseQuery = "SELECT " + 
					" (@row_number\\:=@row_number + 1) AS id, y.*" + 
					" FROM" + 
					" (SELECT " + 
					" x.*" + 
					" FROM" + 
					" (SELECT " + 
					" fc2.fsym_id," + 
					" v3b.date AS p_date,fc2.p_date as price_date," + 
					" :currencyCode AS currency," + 
					" fc2.p_price * factset.get_fx_daily_conversion(fc2.currency, :currencyCode, fc2.p_date) AS p_price," + 
					" fc2.p_price_high * factset.get_fx_daily_conversion(fc2.currency, :currencyCode, fc2.p_date) AS p_price_high," + 
					" fc2.p_price_low * factset.get_fx_daily_conversion(fc2.currency, :currencyCode, fc2.p_date) AS p_price_low," + 
					" fc2.p_price_open * factset.get_fx_daily_conversion(fc2.currency, :currencyCode, fc2.p_date) AS p_price_open," + 
					" fc2.p_volume * 1000 AS p_volume," + 
					" cm1.company_id AS company_id," + 
					" cm1.name AS company_name," + 
					" cl.country_iso_code_3 AS country_id," + 
					" cl.country_name AS country_name," + 
					" cm1.domicile_flag" + 
					" FROM" + 
					" cm.company_list AS cm1" + 
					" INNER JOIN factset.ff_v3_ff_sec_coverage AS v3c ON v3c.fsym_id = cm1.company_id" + 
					" INNER JOIN factset.ff_v3_ff_basic_af v3b ON v3b.fsym_id = v3c.fsym_id" + 
					" AND v3c.fa_latest_ann_update = YEAR(v3b.date)" + 
					" INNER JOIN cm.country_list AS cl ON cl.country_iso_code_3 = cm1.domicile_country_code" + 
					" INNER JOIN factset.fp_v2_fp_sec_coverage AS fc1 ON fc1.fsym_id = cm1.company_id" + 
					" INNER JOIN factset.fp_v2_fp_basic_prices AS fc2 ON fc2.fsym_id = fc1.fsym_id" + 
					" WHERE" + 
					" cm1.tics_industry_code LIKE :ticsIndustryCode" + 
					" AND fc2.p_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)" + 
					" AND cm1.company_active_flag = 1" + 
					" ORDER BY fc2.p_date DESC) x" + 
					" ORDER BY domicile_flag DESC,price_date desc) y" + 
					" JOIN" + 
					" (SELECT @row_number\\:=0) AS t" + 
					" GROUP BY company_name" + 
					" ORDER BY domicile_flag DESC";
		}
		
		_log.info("query "+baseQuery);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(StockPriceScreener.class);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("currencyCode", currencyCode);
		if(countryIsoList!=null && countryIsoList.size()>0){
			query.setParameterList("countryIsoList", countryIsoList);
		}
		
		List<StockPriceScreener> data = (List<StockPriceScreener>) query.list();
		List<ScreenerStockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, ScreenerStockPriceDTO.class);
		return stockPriceDTO;
	}

	@Override
	public List<FFBasicCfDTO> getCompaniesTotalStock(String ticsIndustryCode) {
		
		_log.info("extracting Outstanding shares of the All companies");
		/*String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, "
				+ "cf.fsym_id as fsym_id, "
				+ "cf.ff_mkt_val * 1000000 as ff_mkt_val,  cf.ff_mkt_val_date, "
				+ "cf.ff_com_shs_out * 1000000 as ff_com_shs_out,  cf.ff_com_shs_out_date, "
				+ "cf.ff_shs_float * 1000000 as ff_shs_float,  cf.ff_shs_float_date "
				+ "FROM factset.ff_v3_ff_basic_cf cf "
				+ "INNER JOIN cm.company_list as cm ON cf.fsym_id = cm.company_id, "
				+ "(SELECT @row_number\\:=0) AS t "
				+ "WHERE cm.tics_industry_code = :ticsIndustryCode AND cm.company_active_flag =1 "
				+ "ORDER BY cf.ff_com_shs_out_secs_date DESC ";*/
		String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id, cf.fsym_id as fsym_id, cf.ff_mkt_val * 1000000 as ff_mkt_val,  cf.ff_mkt_val_date, cf.ff_com_shs_out * 1000000 as ff_com_shs_out,  cf.ff_com_shs_out_date, cf.ff_shs_float * 1000000 as ff_shs_float,  cf.ff_shs_float_date FROM factset.ff_v3_ff_basic_cf cf INNER JOIN cm.company_list as cm ON cf.fsym_id = cm.company_id, (SELECT @row_number\\:=0) AS t WHERE cm.tics_industry_code =:ticsIndustryCode AND cm.company_active_flag =1 ORDER BY cm.domicile_flag desc,cf.ff_com_shs_out_secs_date DESC";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFBasicCf.class);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		
		List<FFBasicCf> data = query.list();
		
		List<FFBasicCfDTO> basicCfDTOs = DozerHelper.map(dozerBeanMapper, data, FFBasicCfDTO.class);
		
		//FFBasicCfDTO basicCfDTO = basicCfDTOs.get(0);
		_log.info("Data Size : " + basicCfDTOs.size());
		return basicCfDTOs;
	
	}
	
	@Override
	public Double getCurrencyConversionRate(String sourceCurrency,String destinationCurrency,String period){
		_log.info("extracting Outstanding shares of the All companies ::: \n sourceCurrency ::: " + sourceCurrency + " ::::: destinationCurrency ::: " +  destinationCurrency + " :::: period ::: " + period);
		
				String baseQuery ="SELECT factset.get_fx_year_conversion(:sourceCurrency, :destinationCurrency, :period)";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery);
				query.setParameter("sourceCurrency", sourceCurrency);
				query.setParameter("destinationCurrency", destinationCurrency);
				query.setParameter("period", period);
				//_log.info(query.list());
				Double data = (Double) query.uniqueResult();
			//FxConversionRate rate = null;
		
		
		//List<FFBasicCfDTO> basicCfDTOs = DozerHelper.map(dozerBeanMapper, data, FFBasicCfDTO.class);
		
		//FFBasicCfDTO basicCfDTO = basicCfDTOs.get(0);
		_log.info("Data Size : " + data);
		return data;
	}
}
