package com.televisory.capitalmarket.daoimpl;

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

import com.televisory.capitalmarket.dao.CommodityRepository;
import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.entities.economy.CommodityHistoricalData;
import com.televisory.capitalmarket.entities.economy.CommodityLatestData;
import com.televisory.capitalmarket.entities.economy.News;
import com.televisory.capitalmarket.util.DozerHelper;


@Repository
@Transactional
public class CommodityRepositoryImpl implements CommodityRepository {

	Logger _log = Logger.getLogger(CommodityRepositoryImpl.class);


	@Autowired
	@Qualifier(value="teSessionFactory")
	private SessionFactory teSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<CommodityLatestDataDTO> getCommodities() {
		_log.info("extracting list of commadies");

		//For now there is no difference bw latest data and commodity list query
		String baseQuery = "SELECT tcl.tel_commodity_id,tcl.definition, tcl.name, tcl.tel_market_group, tcl.display_order, mdl.*, NULL as forecast1, NULL as forecast2, NULL as forecast3"
					+ ", NULL as forecast4, NULL as PerChangeQ1, NULL as PerChangeQ2, NULL as PerChangeQ3, NULL as PerChangeQ4" + 
				" FROM televisory_commodity_list tcl" + 
				" left join  market_data_latest mdl on tcl.symbol = mdl.symbol" + 
				" WHERE mdl.type = 'commodities' order by mdl.name";



		Query query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityLatestData.class);


		List<CommodityLatestData> latestCommodityData =query.list();

		List<CommodityLatestDataDTO> latestCommodityDataDTO = DozerHelper.map(dozerBeanMapper, latestCommodityData, CommodityLatestDataDTO.class);

		return latestCommodityDataDTO;
	}

	@Override
	public List<CommodityLatestDataDTO> getCommodityLatestData(String currency) {

		_log.info("extracting latest commadity  data in currency: "+ currency);


		/*String baseQuery = "SELECT tcl.tel_commodity_id, tcl.name,tcl.definition, tcl.tel_market_group, tcl.display_order, mdl.* " + 
				" FROM televisory_commodity_list tcl" + 
				" left join  market_data_latest mdl on tcl.symbol = mdl.symbol" + 
				" WHERE mdl.type = 'commodities' order by tcl.display_order";*/
		String baseQuery = null;
		Query query = null;
		//Updating query to extract the close data from historical table
		if(currency == null || currency.isEmpty()) {

			/*baseQuery = "SELECT " + 
					"		tcl.tel_commodity_id, tcl.name,tcl.definition, tcl.tel_market_group, tcl.display_order, mdf.forecast1, mdf.forecast2, mdf.forecast3"
					+ ", mdf.forecast4, mdf.PerChangeQ1, mdf.PerChangeQ2, mdf.PerChangeQ3, mdf.PerChangeQ4," + 
					"		mdl.`market_data_id`, mdl.`name`, mdl.`ticker`, mdl.`country`, mdl.`url`, mdl.`type`, mdl.`market_group`, mdl.`symbol`, mdl.`date`, mdl.`last`, " +
					"		mdl.`importance`, mdl.`daily_change`, mdl.`daily_percentual_change`, mdl.`weekly_change`, mdl.`weekly_percentual_change`, mdl.`monthly_change`, " +
					"		mdl.`monthly_percentual_change`, mdl.`yearly_change`, mdl.`yearly_percentual_change`, mdl.`ytd_change`, mdl.`ytd_percentual_change`, mdl.`yesterday`, " +
					"		mdl.`last_week`, mdl.`last_month`, mdl.`last_year`, mdl.`start_year`, mdl.`decimal_places`, mdl.`unit`, mdl.`currency_flag`, mdl.`frequency`, mdl.`last_update`, " +
					"		mdl.`is_active`, mdl.`created_at`, mdl.`created_by`, mdl.`last_modified_at`, mdl.`last_modified_by`, " + 
					"		(SELECT `close` FROM `market_data_historical` WHERE `symbol` = mdl.symbol order by `date` desc limit 1) close, " + 
					"		(SELECT `date`  FROM `market_data_historical` WHERE `symbol` = mdl.symbol order by `date` desc limit 1) close_date " +  
					"		FROM televisory_commodity_list tcl " + 
					"		left join  market_data_latest mdl on tcl.symbol = mdl.symbol "+
					"       left join market_data_forecast mdf on mdf.symbol = mdl.symbol" + 
					"		WHERE mdl.type = 'commodities' order by tcl.display_order";*/
			baseQuery = "select x.*, " + 
					" (close - previous_close) as daily_change, " + 
					" ((close - previous_close)/nullif(previous_close,0)) * 100 as daily_percentual_change " + 
					" from ( SELECT " + 
					" tcl.tel_commodity_id, " + 
					" tcl.name,tcl.definition, " + 
					" tcl.tel_market_group, " + 
					" tcl.display_order, " + 
					" mdf.forecast1, " + 
					" mdf.forecast2, " + 
					" mdf.forecast3," + 
					" mdf.forecast4, " + 
					" mdf.PerChangeQ1, " + 
					" mdf.PerChangeQ2, " + 
					" mdf.PerChangeQ3, " + 
					" mdf.PerChangeQ4," + 
					" mdl.`market_data_id`, " + 
					" mdl.`ticker`, " + 
					" mdl.`country`, " + 
					" mdl.`url`, " + 
					" mdl.`type`, " + 
					" mdl.`market_group`, " + 
					" mdl.`symbol`, " + 
					" mdl.`date`, " + 
					" mdl.`last`, " + 
					" mdl.`importance`, " + 
					" mdl.`weekly_change`, " + 
					" mdl.`weekly_percentual_change`, " + 
					" mdl.`monthly_change`, " + 
					" mdl.`monthly_percentual_change`, " + 
					" mdl.`yearly_change`, " + 
					" mdl.`yearly_percentual_change`, " + 
					" mdl.`ytd_change`, " + 
					" mdl.`ytd_percentual_change`, " + 
					" mdl.`yesterday`, " + 
					" mdl.`last_week`, " + 
					" mdl.`last_month`, " + 
					" mdl.`last_year`, " + 
					" mdl.`start_year`, " + 
					" mdl.`decimal_places`, " + 
					" mdl.`unit`, " + 
					" mdl.`currency_flag`, " + 
					" mdl.`frequency`, " + 
					" mdl.`last_update`, " + 
					" mdl.`is_active`, " + 
					" mdl.`created_at`, " + 
					" mdl.`created_by`, " + 
					" mdl.`last_modified_at`, " + 
					" mdl.`last_modified_by`, " + 
					" (SELECT `close` FROM `market_data_historical` WHERE `symbol` = mdl.symbol order by `date` desc limit 1) as close, " + 
					" (SELECT `date`  FROM `market_data_historical` WHERE `symbol` = mdl.symbol order by `date` desc limit 1) as close_date," + 
					" (SELECT `close` FROM `market_data_historical` WHERE `symbol` = mdl.symbol order by `date` desc limit 1,1) as previous_close, " + 
					" (SELECT `date`  FROM `market_data_historical` WHERE `symbol` = mdl.symbol order by `date` desc limit 1,1) as previous_close_date" + 
					" FROM televisory_commodity_list tcl " + 
					" left join  market_data_latest mdl on tcl.symbol = mdl.symbol " + 
					" left join market_data_forecast mdf on mdf.symbol = mdl.symbol" + 
					" WHERE mdl.type = 'commodities' order by tcl.display_order)x ";
			
			query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityLatestData.class);

		}else {
			/*baseQuery=" SELECT tcl.tel_commodity_id,tcl.name,tcl.definition,tcl.tel_market_group, tcl.display_order,mdl.`market_data_id`, "+
					" mdl.`name`,mdl.`ticker`,mdl.`country`,mdl.`url`,mdl.`type`,mdl.`market_group`,mdl.`symbol`,mdl.`date`, "+
					" case "+
						" when mdl.currency_flag=1 then mdl.`last` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
						" else mdl.`last` "+
					" end as last, "+
					" mdl.`importance`,mdl.`daily_change`,mdl.`daily_percentual_change`,mdl.`weekly_change`,mdl.`weekly_percentual_change`,"+
					" mdl.`monthly_change`,mdl.`monthly_percentual_change`,mdl.`yearly_change`,mdl.`yearly_percentual_change`,mdl.`ytd_change`,"+
					" mdl.`ytd_percentual_change`,mdl.`yesterday`, "+
					" case "+
						" when mdl.currency_flag=1 then mdl.`last_week` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date)"+
						" else mdl.last_week "+
					" end as last_week,"+
					" case "+
						" when mdl.currency_flag=1 then mdl.`last_month` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
						" else mdl.last_month "+
					" end as last_month, "+
					" case "+
						" when mdl.currency_flag=1 then mdl.`last_year` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date)"+
						" else mdl.last_year"+
					" end as last_year,"+
					" case "+
						" when mdl.currency_flag=1 then mdl.`start_year` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date)"+
						" else mdl.`start_year`"+
					" end as start_year,"+
					" mdl.`decimal_places`,"+
					" case "+ 
						" when mdl.currency_flag = 1 then replace(unit, get_currency(unit), :currency) "+ 
						" else unit "+
					" end as unit,"+
					" mdl.`currency_flag`,mdl.`frequency`,mdl.`last_update`,mdl.`is_active`,"+
					" mdl.`created_at`,mdl.`created_by`,mdl.`last_modified_at`,mdl.`last_modified_by`,"+
					" case "+
						" when mdl.currency_flag=1 then "+
						" (SELECT `close`FROM   `market_data_historical` WHERE  `symbol` = mdl.symbol order  by `last_update` desc "+
						" limit  1)* factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, (SELECT `date` "+
       					" FROM   `market_data_historical` WHERE  `symbol` = mdl.symbol order  by `last_update` desc limit  1)) "+
       					" else "+
       						" (SELECT `close` FROM   `market_data_historical` WHERE  `symbol` = mdl.symbol order  by `last_update` desc limit  1)"+
       				" end as close, "+
       				" (SELECT `date` FROM   `market_data_historical` WHERE  `symbol` = mdl.symbol order  by `last_update` desc limit  1) close_date "+
       				" FROM   televisory_commodity_list tcl left join market_data_latest mdl on tcl.symbol = mdl.symbol "+
       				" WHERE  mdl.type = 'commodities' order  by tcl.display_order  ";*/
			
			baseQuery=" select x.*, "+
					" (close - previous_close) as daily_change, "+
					" ((close - previous_close)/nullif(previous_close,0)) * 100 as daily_percentual_change "+
					" from (SELECT tcl.tel_commodity_id, "+
					"      tcl.name, "+
					"      tcl.definition, "+
					"      tcl.tel_market_group, "+
					"      tcl.display_order, "+
					"      mdl.`market_data_id`, "+
					"      mdl.`ticker`,"+
					"      mdl.`country`, "+
					"      mdl.`url`,"+
					"      mdl.`type`, "+
					"      mdl.`market_group`,"+
					"      mdl.`symbol`,"+
					"      mdl.`date`,"+
					"      case "+
					"      when mdl.currency_flag=1 then mdl.`last` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
					"      else mdl.`last` "+
					"      end as last, "+
					"      mdl.`importance`, "+
					"      mdl.`weekly_change`, "+
					"      mdl.`weekly_percentual_change`, "+
					"      mdl.`monthly_change`, "+
					"      mdl.`monthly_percentual_change`, "+
					"      mdl.`yearly_change`, "+
					"      mdl.`yearly_percentual_change`, "+
					"      mdl.`ytd_change`, "+
					"      mdl.`ytd_percentual_change`, "+
					"      mdl.`yesterday`, "+
					"      case "+
					"      when mdl.currency_flag=1 then mdl.`last_week` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
					"      else mdl.last_week "+
					"      end as last_week, "+
					"      case "+
					"      when mdl.currency_flag=1 then mdl.`last_month` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
					"      else mdl.last_month "+
					"      end as last_month, "+
					"      case "+
					"      when mdl.currency_flag=1 then mdl.`last_year` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
					"      else mdl.last_year "+
					"      end as last_year, "+
					"      case "+
					"      when mdl.currency_flag=1 then mdl.`start_year` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mdl.date) "+
					"      else mdl.`start_year` "+
					"      end as start_year, "+
					"      mdl.`decimal_places`, "+
					"      case when mdl.currency_flag=1 then replace(unit,get_currency(unit), :currency) else unit end as unit, "+
					
				
					"      case "+
					"      when mdl.currency_flag=1 then mdf.`forecast1` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency,mdl.date) "+
					"      else mdf.`forecast1` "+
					"      end as forecast1, "+
					
					"      case "+
					"      when mdl.currency_flag=1 then mdf.`forecast2` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency,mdl.date) "+
					"      else mdf.`forecast2` "+
					"      end as forecast2, "+
					
					"      case "+
					"      when mdl.currency_flag=1 then mdf.`forecast3` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency,mdl.date) "+
					"      else mdf.`forecast3` "+
					"      end as forecast3, "+
					
					"      case "+
					"      when mdl.currency_flag=1 then mdf.`forecast4` * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency,mdl.date) "+
					"      else mdf.`forecast4` "+
					"      end as forecast4, "+
				 
					"      mdf.PerChangeQ1, mdf.PerChangeQ2, mdf.PerChangeQ3, mdf.PerChangeQ4," + 
					"      mdl.`currency_flag`, "+
					"      mdl.`frequency`, "+
					"      mdl.`last_update`, "+ 
					"      mdl.`is_active`, "+
					"      mdl.`created_at`, "+ 
					"      mdl.`created_by`, "+
					"      mdl.`last_modified_at`, "+
					"      mdl.`last_modified_by`, "+
					"        case when mdl.currency_flag=1 then "+
					"      (SELECT `close` "+
					"       FROM   `market_data_historical` "+
					"       WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+
					"       limit  1)* factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, (SELECT `date` "+
					"       FROM   `market_data_historical` "+
					"       WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+
					"       limit  1)) "+
					"       else "+
					"       (SELECT `close` "+
					"       FROM   `market_data_historical` "+
					"       WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+
					"       limit  1)end as close, "+
					"      (SELECT `date` "+
					"       FROM   `market_data_historical` "+
					"         WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+
					"       limit  1) close_date, "+
					"         case when mdl.currency_flag=1 then "+
					"      (SELECT `close` "+
					"       FROM   `market_data_historical` "+
					"       WHERE  `symbol` = mdl.symbol "+ 
					"       order  by `date` desc "+
					"       limit  1,1)* factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, (SELECT `date` "+
					"       FROM   `market_data_historical` "+
					"         WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+ 
					"       limit  1,1)) "+
					"       else "+
					"       (SELECT `close` "+
					"       FROM   `market_data_historical` "+
					"       WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+
					"       limit  1,1)end as previous_close, "+
					"            (SELECT `date` "+
					"       FROM   `market_data_historical` "+
					"       WHERE  `symbol` = mdl.symbol "+
					"       order  by `date` desc "+
					"       limit  1,1) previous_close_date "+
					" FROM   televisory_commodity_list tcl "+
					"      left join market_data_latest mdl "+
					"             on tcl.symbol = mdl.symbol "+
					"       left join market_data_forecast mdf on mdf.symbol = mdl.symbol" + 
					" WHERE  mdl.type = 'commodities' "+
					" order  by tcl.display_order)x ";

			query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityLatestData.class);
			query.setParameter("currency", currency);
		}



		_log.info(query.toString());
		
		List<CommodityLatestData> latestCommodityData =query.list();
		List<CommodityLatestDataDTO> latestCommodityDataDTO = DozerHelper.map(dozerBeanMapper, latestCommodityData, CommodityLatestDataDTO.class);
		return latestCommodityDataDTO;
	}


	@Override
	public List<CommodityHistoricalDataDTO> getCommodityHistoricalData(List<String> symbolList, String currency, Date startDate, Date endDate) {
		_log.info("extracting historical commodity data for symbol: " + symbolList +". currency: "+ currency+" startDate "+startDate+" endDate "+endDate);

		String baseQuery = null;
		Query query = null;

		if(currency == null || currency.isEmpty()) {
			/*baseQuery = "SELECT mh.*, ml.name, ml.unit" + 
					" FROM `market_data_historical` mh" + 
					" JOIN `market_data_latest` ml on ml.symbol = mh.symbol" + 
					" WHERE mh.`symbol` in (:symbolList) and mh.date BETWEEN :startDate and :endDate and mh.`is_active`=1 order by mh.date ";*/

			baseQuery = "SELECT  x.*,@previous_close\\:= CASE WHEN @v_symbol = symbol THEN @previous_close ELSE NULL END "
					+ "AS previous_close,((x.close - @previous_close) / @previous_close) * 100 AS daily_change,"
					+ "@v_symbol\\:=symbol symbol_check,@previous_close\\:=x.close FROM (SELECT mh.*, ml.name, ml.unit "
					+ "FROM `market_data_historical` mh JOIN `market_data_latest` ml ON ml.symbol = mh.symbol WHERE mh.`symbol` IN "
					+ "(:symbolList) AND mh.`is_active` = 1 and mh.date between :startDate and :endDate "
					+ "ORDER BY mh.`symbol` , mh.date) x,"
					+ "(SELECT @v_symbol\\:=0, @previous_close\\:=0) AS t ";

			query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityHistoricalData.class);
			
			_log.info(query);

			query.setParameterList("symbolList",symbolList);
			query.setParameter("startDate",startDate);
			query.setParameter("endDate",endDate);
		} else {

			/*baseQuery = "select name,unit,market_data_hisid,symbol,type,date,open,high,low,close,last_update,is_active,created_at,created_by,"
					+ "last_modified_at,last_modified_by,previous_close,((close-previous_close)/previous_close)*100 as daily_change from "
					+ "(SELECT ml.name, case when ml.currency_flag=1 then replace(unit,get_currency(unit), :currency) else unit end as unit, "
					+ "mh.`market_data_hisid`, mh.`symbol`, mh.`type`, mh.`date`, case when ml.currency_flag=1 "
					+ "then mh.open * factset.get_fx_daily_conversion(get_currency(unit),:currency, mh.date) else mh.open end  as open, "
					+ "case when ml.currency_flag=1 then mh.high * factset.get_fx_daily_conversion(get_currency(unit),:currency, mh.date) "
					+ "else mh.high end  as high,  case when ml.currency_flag=1 then mh.low * factset.get_fx_daily_conversion(get_currency(unit)"
					+ ",:currency, mh.date) else mh.low end  as low,  case when ml.currency_flag=1 "
					+ "then mh.close * factset.get_fx_daily_conversion(get_currency(unit),:currency, mh.date) else mh.close end  as close,"
					+ "(select close from market_data_historical where date < mh.date and `symbol` in (:symbolList) and "
					+ "date BETWEEN :startDate and :endDate and `is_active`=1 order by date desc limit 1) as previous_close, "
					+ " mh.`last_update`, mh.`is_active`, mh.`created_at`, mh.`created_by`, mh.`last_modified_at`, mh.`last_modified_by` "
					+ "FROM `market_data_historical` mh JOIN `market_data_latest` ml on ml.symbol = mh.symbol  WHERE mh.`symbol` in "
					+ "(:symbolList) and mh.date BETWEEN :startDate and :endDate and mh.`is_active`=1 order by mh.date )x ";*/

			baseQuery = " SELECT  x.*,@previous_close\\:=CASE WHEN @v_symbol = symbol THEN @previous_close ELSE NULL END AS previous_close, "+
					" ((x.close - @previous_close) / @previous_close) * 100 AS daily_change, "+
					" @v_symbol\\:=symbol symbol_check, @previous_close\\:=x.close "+
					" FROM( SELECT ml.name, "+
					" CASE "+
					" WHEN ml.currency_flag = 1 THEN REPLACE(unit, GET_CURRENCY(unit), :currency) "+
					" ELSE unit "+
					" END AS unit, "+
					" ml.currency_flag,mh.`market_data_hisid`,mh.`symbol`,mh.`type`,mh.`date`, "+
					" CASE "+
					" WHEN ml.currency_flag = 1 THEN mh.open * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mh.date) "+
					" ELSE mh.open "+
					" END AS open, "+
					" CASE "+
					" WHEN ml.currency_flag = 1 THEN mh.high * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mh.date) "+
					" ELSE mh.high "+
					" END AS high, "+
					" CASE "+
					" WHEN ml.currency_flag = 1 THEN mh.low * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mh.date) "+
					" ELSE mh.low "+
					" END AS low, "+
					" CASE "+
					" WHEN ml.currency_flag = 1 THEN mh.close * factset.get_fx_daily_conversion(GET_CURRENCY(unit), :currency, mh.date) "+
					" ELSE mh.close "+
					" END AS close, "+
					" mh.`last_update`,mh.`is_active`,mh.`created_at`,mh.`created_by`,mh.`last_modified_at`,mh.`last_modified_by` "+
					" FROM `market_data_historical` mh JOIN `market_data_latest` ml ON ml.symbol = mh.symbol "+
					" WHERE "+
					" mh.`symbol` IN (:symbolList) "+
					" AND mh.`is_active` = 1 "+
					" and  mh.date BETWEEN :startDate and :endDate "+
					" ORDER BY mh.`symbol` , mh.date)x, "+
					" (SELECT @v_symbol\\:=0, @previous_close\\:=0) AS t ";

			query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityHistoricalData.class);



			query.setParameterList("symbolList", symbolList);
			query.setParameter("currency", currency);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		List<CommodityHistoricalData> histCommodityData = query.list();
		
		_log.info("###################"+histCommodityData.size());
		
		List<CommodityHistoricalDataDTO> histCommodityDataDto = DozerHelper.map(dozerBeanMapper, histCommodityData, CommodityHistoricalDataDTO.class);
		return histCommodityDataDto;

	}


/*	//////// This method to be used as per the periodicity based
	@Override
	public List<CommodityHistoricalDataDTO> getCommodityHistoricalData(DownloadRequest downloadRequest) {
		//_log.info("extracting historical commodity data for symbol: " + symbolList +". currency: "+ currency+" startDate "+startDate+" endDate "+endDate);

		String baseQuery = null;
		Query query = null;
		baseQuery = "SELECT  x.*,@previous_close\\:= CASE WHEN @v_symbol = symbol THEN @previous_close ELSE NULL END "
				+ "AS previous_close,((x.close - @previous_close) / @previous_close) * 100 AS daily_change,"
				+ "@v_symbol\\:=symbol symbol_check,@previous_close\\:=x.close FROM (SELECT mh.*, ml.name, ml.unit "
				+ "FROM `market_data_historical` mh JOIN `market_data_latest` ml ON ml.symbol = mh.symbol WHERE mh.`symbol` IN "
				+ "(:symbolList) AND mh.`is_active` = 1 and mh.date between :startDate and :endDate "
				+ "ORDER BY mh.`symbol` , mh.date) x,"
				+ "(SELECT @v_symbol\\:=0, @previous_close\\:=0) AS t ";

		query = teSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CommodityHistoricalData.class);

		query.setParameterList("symbolList",symbolList);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		List<CommodityHistoricalData> histCommodityData =query.list();

		List<CommodityHistoricalDataDTO> histCommodityDataDto = DozerHelper.map(dozerBeanMapper, histCommodityData, CommodityHistoricalDataDTO.class);
		return histCommodityDataDto;

	}
*/

	@Override
	public List<NewsDTO> getNews() {
		_log.info("getting the news for commodity");

		String baseQuery = "SELECT * FROM `news` WHERE `country` =  :country and is_active =1 order by date desc";
		Query query =teSessionFactory.getCurrentSession()
				.createSQLQuery(baseQuery).addEntity(News.class);

		query.setParameter("country", "Commodity");

		List<NewsDTO> newsList =query.list();
		return newsList;
	}


}
