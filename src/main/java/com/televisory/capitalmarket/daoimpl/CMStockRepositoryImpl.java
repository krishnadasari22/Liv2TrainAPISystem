package com.televisory.capitalmarket.daoimpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.dao.CMStockRepository;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.entities.cm.ExchangeIndexStockPrice;
import com.televisory.capitalmarket.entities.factset.FFBasicCf;
import com.televisory.capitalmarket.entities.factset.FFStockPrice;
import com.televisory.capitalmarket.entities.factset.FFStockPriceLatest;
import com.televisory.capitalmarket.entities.factset.FFStockSpinoff;
import com.televisory.capitalmarket.entities.factset.FFStockSplit;
import com.televisory.capitalmarket.factset.dto.FFStockSplitDTO;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.factset.dto.FFStockSpinoffDTO;
import com.televisory.capitalmarket.service.CMStockService;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

/**
 * 
 * @author vinay
 *
 */
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class CMStockRepositoryImpl implements CMStockRepository{
	
	
Logger _log = Logger.getLogger(CMStockRepositoryImpl.class);
	

	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Autowired
	@Lazy
	CMStockService cmStockService;

	@Override
	public StockPriceDTO getCompanyLatestStockPrice(String companyId, String currencyCode) throws Exception{
		
		_log.info("extracting latest stock price of the company");
		
		/*String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id, bp.p_date,";
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		}else {
			baseQuery += " bp.currency as currency, ";
		}
		baseQuery += " bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
		+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
		+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
		+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
		+ " FROM fp_v2_fp_basic_prices bp,(SELECT @row_number\\:=0) AS t where bp.fsym_id=:Id order by bp.p_date desc limit 1";
		*/
		
		
		String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id,fsym_id, p_date,currency, p_price, p_price_high, p_price_low," 
				+ " p_price_open, p_volume, p_prev_price, ((p_price-p_prev_price)/p_prev_price)*100 as percent_change " 
				+ " from ("
				+ " SELECT bp.fsym_id, bp.p_date,";
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		}else {
			baseQuery += " bp.currency as currency, ";
		}
		baseQuery += " bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
		+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
		+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
		+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume, "
		+ " (select p_price * factset.get_fx_daily_conversion(currency,:currencyCode,p_date)  from fp_v2_fp_basic_prices where fsym_id=bp.fsym_id and p_date<bp.p_date order by p_date desc limit 1) as p_prev_price "
		+ " FROM fp_v2_fp_basic_prices bp where bp.fsym_id=:Id order by bp.p_date desc limit 1"
		+ " )x join (SELECT @row_number\\:=0) AS t";
		
		
		_log.info("query "+baseQuery);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockPriceLatest.class);
		query.setParameter("Id", companyId);
		query.setParameter("currencyCode", currencyCode);
		
		List<FFStockPriceLatest> data = (List<FFStockPriceLatest>) query.list();
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		return stockPriceDTO.get(0);
	}
		
	@Override
	public StockPriceDTO getCompanyStockPrice(String companyId,String date, String currencyCode) throws Exception{
		
		_log.info("extracting stock price of the company for period "+date);
		
		/*String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, bp.* FROM "
				+ "fp_v2_fp_basic_prices bp,(SELECT @row_number\\:=0) AS t where bp.fsym_id=:Id and bp.p_date<=:period order by bp.p_date desc limit 1";
		*/
		
		String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id, bp.p_date,";
				if(currencyCode!=null && currencyCode!="") {
					baseQuery += " :currencyCode as currency, ";
				}else {
					baseQuery += " bp.currency as currency, ";
				}
				//baseQuery += " :currencyCode as currency, "
				baseQuery += " bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
				+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
				+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
				+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
				+ " FROM fp_v2_fp_basic_prices bp,(SELECT @row_number\\:=0) AS t where bp.fsym_id=:Id and bp.p_date<=:date order by bp.p_date desc limit 1";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockPrice.class);
		query.setParameter("Id", companyId);
		query.setParameter("currencyCode", currencyCode);
		query.setParameter("date", date);
		
		List<FFStockPrice> data = (List<FFStockPrice>) query.list();
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		return stockPriceDTO.get(0);
	}

	@Override
	public List<StockPriceDTO> getFactSetCompanyStockPrice(String Id, String periodType, Date startDate,Date endDate,String currencyCode)  {
	
		_log.info("extracting factset stock price for ID: "+ Id +", in currency: "+ currencyCode +", From: "+ startDate +", to: "+ endDate);
		
		String baseQuery = "Select * from (SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id,bp.p_date, ";
		
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		} else {
			baseQuery += " bp.currency as currency, ";
		}
				
		baseQuery += "bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
				+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
				+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
				+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
				+ " ,hl.holiday_name "
				+ " FROM fp_v2_fp_basic_prices bp "
				+ " INNER JOIN cm.company_list cl on bp.fsym_id = cl.company_id " 
				+ " LEFT JOIN factset.ref_v2_ref_calendar_holidays hl on cl.exchange_code = hl.fref_exchange_code " 
				+ " and bp.p_date = hl.holiday_date "
				+ " ,(SELECT @row_number\\:=0) AS t where bp.fsym_id=:Id ";
		
		baseQuery+=" and bp.p_date BETWEEN :startDate AND :endDate";
		baseQuery+=" order by bp.p_date";
		
		baseQuery+=") x where x.holiday_name is null order by x.id";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockPrice.class);
		
		if(startDate==null && endDate!=null){
			query.setParameter("startDate", CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			query.setParameter("endDate", endDate);
		}
		else if(endDate==null && startDate!=null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate",  new Date(), TimestampType.INSTANCE);
		}
		else if(startDate!=null && endDate!=null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}else{
			query.setParameter("startDate",CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			query.setParameter("endDate",  new Date(), TimestampType.INSTANCE);
		}
		query.setParameter("Id",Id);
		query.setParameter("currencyCode",currencyCode);
		
		@SuppressWarnings("unchecked")
		List<FFStockPrice> data =  query.list();
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		
		//stockPriceDTO = cmStockService.updateGapStockPrice(stockPriceDTO);
		
		return stockPriceDTO;
	}

	@Override
	public StockPriceDTO getStockPriceHighest(String companyId, String currencyCode) {
	
		_log.info("extracting stock price highest for  ID: "+ companyId  );
		
		String baseQuery = "Select * from (SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id,bp.p_date, ";
		
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		} else {
			baseQuery += " bp.currency as currency, ";
		}
		
		baseQuery += "bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
				+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
				+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
				+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
				+ " FROM fp_v2_fp_basic_prices bp "
				+ " ,(SELECT @row_number\\:=0) AS t where bp.fsym_id=:companyId ";
		
		baseQuery+=" order by bp.p_price_high DESC";
		
		baseQuery+=") x limit 1";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery)
				.addEntity(FFStockPrice.class);
		
		query.setParameter("companyId",companyId);
		query.setParameter("currencyCode",currencyCode);
		
		@SuppressWarnings("unchecked")
		List<FFStockPrice> data =  query.list();
		
		
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		//stockPriceDTO = cmStockService.updateGapStockPrice(stockPriceDTO);
		
		return stockPriceDTO.get(0);
	}
	
	@Override
	public StockPriceDTO getStockPriceHighest(String companyId, Integer days, String currencyCode) {
	
		_log.info("extracting stock price highest for  ID: "+ companyId +", days: "+ days );
	
		String baseQuery = "Select * from (SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id,bp.p_date, ";
		
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		} else {
			baseQuery += " bp.currency as currency, ";
		}
		
		baseQuery += "bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
				+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
				+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
				+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
				+ " FROM fp_v2_fp_basic_prices bp, "
				+ " (SELECT @row_number\\:=0) AS t, "
				+ " (SELECT @maxDate\\:=max(p_date) FROM fp_v2_fp_basic_prices where fsym_id=:companyId) AS d "
				+ " where bp.fsym_id=:companyId and p_date between date_sub(@maxDate, interval "+ days +" day) and @maxDate";
		
		baseQuery+=" order by bp.p_date DESC ";
		
		baseQuery+=") x order by x.p_price_high DESC limit 1";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockPrice.class);
		
		query.setParameter("companyId",companyId);
		query.setParameter("currencyCode",currencyCode);
		
		@SuppressWarnings("unchecked")
		List<FFStockPrice> data =  query.list();
			
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		
		return stockPriceDTO.get(0);
	}

	@Override
	public StockPriceDTO getStockPriceLowest(String companyId, String currencyCode) {
	
		_log.info("extracting stock price lowest for  ID: "+ companyId);
		
		String baseQuery = "Select * from (SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id,bp.p_date, ";
		
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		} else {
			baseQuery += " bp.currency as currency, ";
		}
		
		baseQuery += "bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
				+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
				+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
				+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
				+ " FROM fp_v2_fp_basic_prices bp "
				+ " ,(SELECT @row_number\\:=0) AS t where bp.fsym_id=:companyId ";
		
		baseQuery+=" order by bp.p_price_low ";
		
		baseQuery+=") x where p_price_low is not null limit 1";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery)
				.addEntity(FFStockPrice.class);
		
		query.setParameter("companyId",companyId);
		query.setParameter("currencyCode",currencyCode);
		
		@SuppressWarnings("unchecked")
		List<FFStockPrice> data =  query.list();
		
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		//stockPriceDTO = cmStockService.updateGapStockPrice(stockPriceDTO);
		
		return stockPriceDTO.get(0);
	}
	
	@Override
	public StockPriceDTO getStockPriceLowest(String companyId, Integer days, String currencyCode) {
		_log.info("extracting stock price lowest for  ID: "+ companyId +", days: "+ days );
		
		String baseQuery = "Select * from (SELECT (@row_number\\:=@row_number + 1) AS id, bp.fsym_id,bp.p_date, ";
		
		if(currencyCode!=null && currencyCode!="") {
			baseQuery += " :currencyCode as currency, ";
		} else {
			baseQuery += " bp.currency as currency, ";
		}
		
		baseQuery += "bp.p_price * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price, "
				+ " bp.p_price_high * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_high, "
				+ " bp.p_price_low * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_low, "
				+ " bp.p_price_open * factset.get_fx_daily_conversion(bp.currency,:currencyCode,bp.p_date) as p_price_open,bp.p_volume *1000 as p_volume "
				+ " FROM fp_v2_fp_basic_prices bp, "
				+ " (SELECT @row_number\\:=0) AS t, "
				+ " (SELECT @maxDate\\:=max(p_date) FROM fp_v2_fp_basic_prices where fsym_id=:companyId) AS d "
				+ " where bp.fsym_id=:companyId and p_date between date_sub(@maxDate, interval "+ days +" day) and @maxDate";
		
		baseQuery+=" order by bp.p_date DESC ";
		
		baseQuery+=") x where x.p_price_low is not null order by x.p_price_low limit 1";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockPrice.class);
		
		query.setParameter("companyId",companyId);
		query.setParameter("currencyCode",currencyCode);
		
		@SuppressWarnings("unchecked")
		List<FFStockPrice> data =  query.list();
		
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		//stockPriceDTO = cmStockService.updateGapStockPrice(stockPriceDTO);
		
		return stockPriceDTO.get(0);
	}
	
	@Override
	public List<FFStockSplitDTO> getSplitStockInfo(String companyId) {
		
		_log.info("extracting split stock information of companiess" );
		
		String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id, sp.* FROM "
				+ "fp_v2_fp_basic_splits sp,(SELECT @row_number\\:=0) AS t where sp.fsym_id=:companyId and p_split_date < now()";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockSplit.class);
		query.setParameter("companyId", companyId);
		
		List<FFStockSplit> data = (List<FFStockSplit>) query.list();
		List<FFStockSplitDTO> factSetSplitStockDTOs = DozerHelper.map(dozerBeanMapper, data, FFStockSplitDTO.class);	
		
		return factSetSplitStockDTOs;
	}
	
	@Override
	public List<FFStockSpinoffDTO> getSpinoffStockInfo(String companyId) {
		
		_log.info("extracting spinoff stock information of companiess" );
		
		String baseQuery = "SELECT (@row_number\\:=@row_number + 1) AS id, x.*, (p_price - p_divs_pd)/p_price as p_spinoff_factor " + 
				" from (SELECT fsym_id, p_divs_exdate as p_date, p_divs_pd, " + 
				" (SELECT p_price from factset.fp_v2_fp_basic_prices " + 
				" where p_date < d.p_divs_exdate and fsym_id = :companyId order by p_date desc limit 1) as p_price " + 
				" FROM factset.`fp_v2_fp_basic_dividends` d " + 
				" WHERE `fsym_id` = :companyId and `p_divs_s_pd` = 1)x,(SELECT @row_number\\:=0) AS t ";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFStockSpinoff.class);
		query.setParameter("companyId", companyId);
		
		List<FFStockSpinoff> data = (List<FFStockSpinoff>) query.list();
		List<FFStockSpinoffDTO> factSetSpinoffStockDTOs = DozerHelper.map(dozerBeanMapper, data, FFStockSpinoffDTO.class);	
		
		return factSetSpinoffStockDTOs;
	}
	
	@Override
	public List<StockPriceDTO> getIndexStockPrice(Integer indexId, Date startDate, Date endDate, String periodType) {

		_log.info("extracting Index stock price for ID: "+ indexId +", From: "+ startDate +", to: "+ endDate);
		
		String baseQuery = "select * from index_movement where index_id =:indexId "
				+ " and date between :startDate and :endDate  group by date order by date"; 	
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(ExchangeIndexStockPrice.class);
		
		if(startDate==null && endDate!=null){
			query.setParameter("startDate", CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			query.setParameter("endDate", endDate);
		}
		else if(endDate==null && startDate!=null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate",  new Date(), TimestampType.INSTANCE);
		}
		else if(startDate!=null && endDate!=null){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}else{
			query.setParameter("startDate",CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			query.setParameter("endDate",  new Date(), TimestampType.INSTANCE);
		}
		query.setParameter("indexId",indexId);
		
		@SuppressWarnings("unchecked")
		List<ExchangeIndexStockPrice> data = null;
		try {
			data = query.list();
			_log.debug("Index Price Size "+data.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<StockPriceDTO> stockPriceDTO = DozerHelper.map(dozerBeanMapper, data, StockPriceDTO.class);
		
		_log.debug("after dto mapping");
		
		//stockPriceDTO = cmStockService.updateGapStockPrice(stockPriceDTO);
		
		return stockPriceDTO;
	}
	
	@Override
	public List<Map<String, Object>> getCompanyTotalSharesByPeriod(String companyId,String period) {
		
		_log.info("extracting total shares of the company For period "+period);
		
		//String baseQuery = "SELECT fsym_id as company_id, p_com_shs_out*1000 as total_shares from fp_v2_fp_basic_shares_hist where fsym_id in ( select fsym_id from ff_v3_ff_sec_coverage where ff_co_name = (select ff_co_name from ff_v3_ff_sec_coverage where fsym_id = :companyId)) and p_date<=:period order by p_date desc limit 1";
		
		String baseQuery = " select company_id, total_shares from (" + 
				" select * from (select fsym_id as company_id, date, ff_com_shs_out*1000000 as total_shares from ff_v3_ff_basic_af where fsym_id = :companyId and date<=:period and ff_com_shs_out is not null order by date desc limit 1)a" + 
				" union " + 
				" select * from (select fsym_id as company_id, date, ff_com_shs_out*1000000 as total_shares from ff_v3_ff_basic_qf where fsym_id = :companyId and date<=:period and ff_com_shs_out is not null order by date desc limit 1)b" + 
				" )x order by date desc limit 1";
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("companyId", companyId);
		query.setParameter("period", period);
		
		List<Map<String, Object>> data = query.list();
		return data;
	}

	@Override
	public FFBasicCfDTO getFFBasicCfDTO(String companyId) {
		
		_log.info("extracting Basic Cf entry for the company: "+ companyId);
		
		//String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, cf.* FROM ff_v3_ff_basic_cf cf,(SELECT @row_number\\:=0) AS t where cf.fsym_id=:companyId ORDER BY cf.ff_com_shs_out_secs_date DESC ";
		String baseQuery ="SELECT (@row_number\\:=@row_number + 1) AS id, "
				+ "cf.fsym_id, "
				+ "cf.ff_mkt_val * 1000000 as ff_mkt_val,  cf.ff_mkt_val_date, "
				+ "cf.ff_com_shs_out * 1000000 as ff_com_shs_out,  cf.ff_com_shs_out_date, "
				+ "cf.ff_shs_float * 1000000 as ff_shs_float,  cf.ff_shs_float_date "
				+ "FROM ff_v3_ff_basic_cf cf,(SELECT @row_number\\:=0) AS t where cf.fsym_id=:companyId ORDER BY cf.ff_com_shs_out_secs_date DESC ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(FFBasicCf.class);
		
		query.setParameter("companyId", companyId);
		
		List<FFBasicCf> data = query.list();
		
		List<FFBasicCfDTO> basicCfDTOs = DozerHelper.map(dozerBeanMapper, data, FFBasicCfDTO.class);
		
		FFBasicCfDTO basicCfDTO = basicCfDTOs.get(0);
		
		return basicCfDTO;
	
	}


}
