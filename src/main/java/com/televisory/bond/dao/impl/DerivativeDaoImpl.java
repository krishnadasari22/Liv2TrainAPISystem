package com.televisory.bond.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.televisory.bond.dao.DerivativeDao;
import com.televisory.bond.dto.DerFuCoparableDTO;
import com.televisory.bond.dto.DerOpCoparableDTO;
import com.televisory.bond.dto.DerivativeBalanceModelDTO;
import com.televisory.bond.dto.DerivativeHistoricalDataDTO;
import com.televisory.bond.dto.DerivativeLatestDTO;
import com.televisory.bond.dto.DerivativeNameDTO;
import com.televisory.bond.entity.DerFuCoparable;
import com.televisory.bond.entity.DerOpCoparable;
import com.televisory.bond.entity.DerivativeBalanceModel;
import com.televisory.bond.entity.DerivativeHistoricalData;
import com.televisory.bond.entity.DerivativeIdentifierWithProp;
import com.televisory.bond.entity.DerivativeLatestData;
import com.televisory.bond.utils.BondStaticParams;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.util.DozerHelper;



@Repository
@Primary
@Transactional
public class DerivativeDaoImpl implements DerivativeDao{
	
	Logger _log = Logger.getLogger(DerivativeDaoImpl.class);
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Autowired
	CMRepository cmRepository;

	@Override
	public List<DerivativeNameDTO> getDerivativeNameList(String searchCriteria) {
		
		_log.info("getting the derivative name list for searchCriteria: "+ searchCriteria);

		String baseQuery;
		Query query;
		
		if(searchCriteria!=null && searchCriteria!=""){
				
			baseQuery="Select * from ("
					+ " (SELECT distinct underlying_name as underlyingName, asset_type as assetType FROM `etd_data_latest` "
					+ " WHERE is_active=1 AND `underlying_name` like :entityNameQ1 AND underlying_name IS NOT NULL and expiry_date >= CURRENT_DATE() order by underlying_name limit 200)  "
					+ " UNION "
					+ " (SELECT distinct underlying_name as underlyingName, asset_type as assetType FROM `etd_data_latest` "
					+ " WHERE is_active=1 AND `underlying_name` like :entityNameQ2 AND underlying_name IS NOT NULL  and expiry_date >= CURRENT_DATE() order by underlying_name limit 200) )x "
					+ " group by underlyingName, assetType limit 300";
				
				
				query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
				
				query.setParameter("entityNameQ1", searchCriteria+"%");
				query.setParameter("entityNameQ2", "%"+searchCriteria+"%");
				query.setResultTransformer(Transformers.aliasToBean(DerivativeNameDTO.class));
		} else {
			//baseQuery="select distinct underlying_name as underlyingName ,asset_type as assetType from etd_data_latest where underlying_name IS NOT NULL and expiry_date >= CURRENT_DATE() group by `underlying_name` order by underlying_name limit 1000";
			baseQuery="select underlying_name as underlyingName ,asset_type as assetType from (select underlying_name ,asset_type, expiry_date " + 
					"from etd_data_latest where  expiry_date >= CURRENT_DATE() AND is_active=1  order by underlying_name limit 50000)x where underlying_name IS NOT NULL and expiry_date >= CURRENT_DATE() group by `underlying_name` order by underlying_name limit 1000";

			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setResultTransformer(Transformers.aliasToBean(DerivativeNameDTO.class));
		}


		@SuppressWarnings("unchecked")
		List<DerivativeNameDTO> nameListDTO = query.list();

		return nameListDTO;
		
	}

	@Override
	public List<String> getDerivativeCategoryList(String derivativeName) {
		
		_log.info("getting the derivative Category list for derivativeName: "+ derivativeName);

		String baseQuery;
		Query query;
		
		if(derivativeName!=null && derivativeName!=""){
			baseQuery="select asset_type from etd_data_latest where is_active=1 AND underlying_name = :derivativeName and expiry_date >= CURRENT_DATE() group by asset_type";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setParameter("derivativeName", derivativeName);
		}else{
			baseQuery="select distinct asset_type from etd_data_latest where is_active=1 AND expiry_date >= CURRENT_DATE() ";
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		}
		
		@SuppressWarnings("unchecked")
		List<String> nameListDTO = query.list();

		return nameListDTO;
	}

	@Override
	public List<DerivativeIdentifierWithProp> getDerivativeExpiryDateList(String derivativeName, String category) {
		_log.info("getting the derivative expiry Date  list for derivativeName: "+ derivativeName +", category: "+ category);

		String baseQuery;
		Query query;
		
		baseQuery="select expiry_date as expiryDate, symbol as identifier from etd_data_latest where underlying_name = :derivativeName and asset_type = :assetType and  expiry_date >= CURRENT_DATE() group by expiry_date";
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).setResultTransformer(new AliasToBeanResultTransformer(DerivativeIdentifierWithProp.class));
		query.setParameter("derivativeName", derivativeName);
		query.setParameter("assetType", category);
		
		@SuppressWarnings("unchecked")
		List<DerivativeIdentifierWithProp> nameListDTO = query.list();

		return nameListDTO;
	}

	@Override
	public List<String> getDerivativeOptionType(String derivativeName, String category, String expiryDate) {
		
		_log.info("getting the derivative OptionType list for derivativeName: "+ derivativeName +", category: "+ category +", expiryDate: "+expiryDate);

		String baseQuery;
		Query query;
		
		baseQuery="select call_put from etd_data_latest where underlying_name = :derivativeName and asset_type = :assetType and expiry_date=:expiryDate and expiry_date >= CURRENT_DATE() group by call_put";
		query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("derivativeName", derivativeName);
		query.setParameter("assetType", category);
		query.setParameter("expiryDate", expiryDate);
		
		@SuppressWarnings("unchecked")
		List<String> listDTO = query.list();

		return listDTO;
	}

	@Override
	public List<DerivativeIdentifierWithProp> getDerivativeStrikeList(String derivativeName, String category, String expiryDate, String optionType) {
		
		_log.info("getting the derivative Strike Price list for derivativeName: "+ derivativeName +", category: "+ category +", expiryDate: "+expiryDate +", optionType: "+ optionType);

		@SuppressWarnings("unchecked")
		List<DerivativeIdentifierWithProp> listDTO = null;
		try {
			String baseQuery;
			Query query;
			
			baseQuery="select strike as strikePrice, symbol as identifier from etd_data_latest where underlying_name = :derivativeName and asset_type = :assetType and expiry_date=:expiryDate and call_put =:optionType and expiry_date >= CURRENT_DATE()  group by strike";
			//query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).setResultTransformer(new AliasToBeanResultTransformer(DerivativeIdentifierWithProp.class));
			
			query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setParameter("derivativeName", derivativeName);
			query.setParameter("assetType", category);
			query.setParameter("expiryDate", expiryDate);
			query.setParameter("optionType", optionType);
			
			listDTO = query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listDTO;
	}
	
	@Override
	public String getDefaultIdentifier() {
		
		_log.info("getting Default Identifier");
		
		String dbDefaultIdentifier = null;
		try {
			//get default identifier from DB  BondStaticParams.DEFAULT_ETD_IDENTIFIER
			dbDefaultIdentifier = cmRepository.getObjectProperty(BondStaticParams.DEFAULT_ETD_IDENTIFIER_PROPERTY_NAME).getValue();
		} catch (Exception e) {
			_log.warn("Problem occured in getting default identifier from DB");
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append(" SELECT * from (SELECT * from (SELECT `symbol` FROM `etd_data_latest` WHERE `symbol` =:defaultIdentifier AND is_active=1) x ");
		sb.append(" UNION ");
		sb.append(" SELECT * from (SELECT `symbol` FROM `etd_data_latest` WHERE is_active=1 and last_trade is not null limit 1) y ) z  limit 1");
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		query.setParameter("defaultIdentifier", dbDefaultIdentifier);
		
		@SuppressWarnings("unchecked")
		List<String> defaultIdentifiers = query.list();

		return defaultIdentifiers.get(0);
		
	}
	
	@Override
	public List<DerivativeBalanceModelDTO> getDerivativeMetricList() {
		
		_log.info("getting the metric list");

		String baseQuery="SELECT * FROM etd_balance_model";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DerivativeBalanceModel.class);

		@SuppressWarnings("unchecked")
		List<DerivativeBalanceModel> data = (List<DerivativeBalanceModel>) query.list();

		List<DerivativeBalanceModelDTO> metricListDTO = DozerHelper.map(dozerBeanMapper, data, DerivativeBalanceModelDTO.class);	
		
		return metricListDTO;
	
	}
	
	@Override
	public DerivativeLatestDTO getLatestDateData(String identifier) throws Exception {
		
		_log.info("getting the LatestData for identifier: "+ identifier);
		
		String baseQuery;
		
		baseQuery="SELECT * FROM `etd_data_latest` WHERE symbol = :identifier";
	
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DerivativeLatestData.class);
		query.setParameter("identifier", identifier);
		
		@SuppressWarnings("unchecked")
		List<DerivativeLatestData> latestData = query.list();
		
		List<DerivativeLatestDTO> latestDataDTO = DozerHelper.map(dozerBeanMapper, latestData, DerivativeLatestDTO.class);
		
		if(latestDataDTO.size()>0){
			return latestDataDTO.get(0);
		}else{
			throw new Exception("data not available");
		}
	}
	
	@Override
	public List<DerivativeHistoricalDataDTO> getHistoricalData(String identifier, String fieldName) {
		
		_log.info("getting the Historical Data for identifier: "+identifier +", fieldName"+ fieldName);
		
		String baseQuery;
		String startDate = null;

		try {
			baseQuery="select min(as_on_date) from `etd_data` WHERE symbol = :identifier and "+fieldName+" is not null";
			Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
			query.setParameter("identifier", identifier);
			startDate = query.list().get(0).toString();
		} catch (Exception e){
			return new ArrayList<DerivativeHistoricalDataDTO>();
		}
		
		
		baseQuery="SELECT symbol, as_on_date as period, '"+fieldName+"' as field_name,  "+fieldName+" as data "
				+ " FROM `etd_data` WHERE symbol = :identifier";
	
		if(startDate != null)
			baseQuery += " and as_on_date >= '"+ startDate +"'";
		
		baseQuery += " order by as_on_date asc ";
		
		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DerivativeHistoricalData.class);
		query.setParameter("identifier", identifier);
		
		@SuppressWarnings("unchecked")
		List<DerivativeHistoricalData> historicalData = query.list();
		
		List<DerivativeHistoricalDataDTO> historicalDataDTO = DozerHelper.map(dozerBeanMapper, historicalData, DerivativeHistoricalDataDTO.class);
		
		if(historicalDataDTO != null && historicalDataDTO.size() == 0)
			return new ArrayList<DerivativeHistoricalDataDTO>();
		
		return historicalDataDTO;
		
	}
	
	@Override
	public List<DerFuCoparableDTO> getFuComparable(String identifier) {

		_log.info("getting comparable for Future identifier: "+identifier );

		String baseQuery;

		/*baseQuery="SELECT " + 
				"	l2.symbol, " + 
				"	replace(CONCAT(l2.underlying_name, ' | ',l2.asset_type,' | ',ifnull(l2.exchange_name,'') ,' | ',l2.expiry_date),'|  |', '|') AS display_name, "+
				"	l2.underlying_name, " + 
				"	l2.expiry_date, " + 
				"   l2.latest_settlement, " + 
				"   l2.latest_settlement_date, " + 
				"   l2.last_trade, " + 
				"   l2.last_trade_date, " + 
				"   l2.open_interest, " + 
				"   l2.change_in_oi " + 
				"FROM etd_data_latest l1 " + 
				"join etd_data_latest l2 on l1.underlying_name = l2.underlying_name and l1.asset_type = l2.asset_type " + 
				"WHERE l1.symbol = :symbol " + 
				"ORDER BY ABS(DATEDIFF(l1.expiry_date, l2.expiry_date)) , ABS(l1.latest_settlement - l2.latest_settlement)" + 
				"LIMIT "+ BondStaticParams.ETD_FUTURE_COMPARABLE_LIMIT;*/
		
		/*baseQuery = "select symbol,display_name," + 
				"                    underlying_name,  " + 
				"                    expiry_date,  " + 
				"                   latest_settlement,  " + 
				"                   latest_settlement_date,  " + 
				"                   last_trade,  " + 
				"                    last_trade_date,  " + 
				"                   open_interest,  " + 
				"                   change_in_oi from  (SELECT l2.symbol,  replace(CONCAT(l2.underlying_name, ' | ',l2.asset_type,' | ',ifnull(l2.exchange_name,'') ,' | ',l2.expiry_date),'|  |', '|') AS display_name," + 
				"                    l2.underlying_name,  " + 
				"                    l2.expiry_date,  " + 
				"                   l2.latest_settlement,  " + 
				"                   l2.latest_settlement_date,  " + 
				"                   l2.last_trade,  " + 
				"                   l2.last_trade_date,  " + 
				"                   l2.open_interest,  " + 
				"                   l2.change_in_oi," + 
				"                   case" + 
				"                   when l1.underlying_name=l2.underlying_name then 1" + 
				"                   else 2 end as underlying_name_flag," + 
				"                   case when l1.contract_size_units=l2.contract_size_units then 1" + 
				"                   else 2 end as contract_size_units_flag," + 
				"                   case  when l1.exchange_name=l2.exchange_name then 1" + 
				"                   else 2 end as exchange_flag" + 
				"                FROM etd_data_latest l1  " + 
				"                join etd_data_latest l2 on l1.underlying_type = l2.underlying_type and l1.asset_type = l2.asset_type   " + 
				"               where l1.symbol = :symbol " + 
				"                ORDER BY underlying_name_flag," + 
				"                case when underlying_name_flag =1 then ABS(DATEDIFF(l1.expiry_date, l2.expiry_date)) " + 
				"				else  contract_size_units_flag  end , abs(l1.contract_size-l2.contract_size),exchange_flag limit "+ BondStaticParams.ETD_FUTURE_COMPARABLE_LIMIT +")x";*/
		
		baseQuery = "SELECT symbol,display_name,underlying_name,expiry_date,latest_settlement,latest_settlement_date,last_trade,last_trade_date,open_interest,change_in_oi,currency "
				       +"FROM  (SELECT l2.symbol,  replace(CONCAT(l2.underlying_name, ' | ',l2.asset_type,' | ',ifnull(l2.exchange_name,'') ,' | ',l2.expiry_date),'|  |', '|') AS display_name,"
					   +"l2.underlying_name,l2.expiry_date,l2.latest_settlement * factset.get_fx_daily_conversion(l2.currency,l1.currency,l2.latest_settlement_date) as latest_settlement,"
					   +"l2.latest_settlement_date ,l2.last_trade  * factset.get_fx_daily_conversion(l2.currency,l1.currency,l2.latest_settlement_date) as last_trade,l2.last_trade_date,l2.open_interest,l2.change_in_oi,"
					   +"case "
					   +"when l1.underlying_name=l2.underlying_name then 1 "
					   +"else 2 end as underlying_name_flag,"
					   +"case when l1.contract_size_units=l2.contract_size_units then 1 "
					   +"else 2 end as contract_size_units_flag,"
					   +"case  when l1.exchange_name=l2.exchange_name then 1 "
					   +"else 2 end as exchange_flag,"
					   +"l1.currency "
					+"FROM etd_data_latest l1 "
					+"JOIN etd_data_latest l2 on l1.underlying_type = l2.underlying_type and l1.asset_type = l2.asset_type " 
				    +"WHERE l1.symbol = :symbol AND  l1.is_active=1 AND  l2.is_active=1 "
					+"ORDER BY underlying_name_flag,"
					+"case when underlying_name_flag =1 then ABS(DATEDIFF(l1.expiry_date, l2.expiry_date)) "
					+"else  contract_size_units_flag  end , abs(l1.contract_size-l2.contract_size),exchange_flag limit "+ BondStaticParams.ETD_FUTURE_COMPARABLE_LIMIT +")x";
		

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DerFuCoparable.class);
		query.setParameter("symbol", identifier);

		@SuppressWarnings("unchecked")
		List<DerFuCoparable> derFuCoparables = query.list();

		List<DerFuCoparableDTO> derFuCoparableDTOs = DozerHelper.map(dozerBeanMapper, derFuCoparables, DerFuCoparableDTO.class);
		return derFuCoparableDTOs;

	}
	
	
	
	@Override
	public List<DerOpCoparableDTO> getOpComparable(String identifier) {

		_log.info("getting comparable for Option identifier: "+identifier );

		String baseQuery;

		baseQuery="SELECT * FROM (SELECT " + 
				"    e1.symbol as call_symbol, " + 
				"    e1.expiry_date as call_expiry_date, " + 
				"    e1.latest_settlement as call_latest_settlement, " + 
				"    e1.last_trade as call_last_trade, " + 
				"    e1.total_volume as call_total_volume, " + 
				"    e1.open_interest as call_open_interest, " + 
				"    e1.strike as strike, " + 
				"    e2.latest_settlement as put_latest_settlement, " + 
				"    e2.last_trade as put_last_trade, " + 
				"    e2.total_volume as put_total_volume, " + 
				"    e2.open_interest as put_open_interest, " + 
				"    e2.expiry_date as put_expiry_date, " + 
				"    e2.symbol as put_symbol " + 
				" FROM etd_data_latest e " + 
				"    JOIN etd_data_latest e1 ON e.underlying_name = e1.underlying_name " + 
				"        AND e.asset_type = e1.asset_type " + 
				"        AND e.expiry_date = e1.expiry_date " + 
				"    JOIN etd_data_latest e2 ON e1.underlying_name = e2.underlying_name " + 
				"        AND e1.asset_type = e2.asset_type " + 
				"        AND e1.expiry_date = e2.expiry_date " + 
				"        AND e1.strike = e2.strike " + 
				" WHERE e.symbol = :symbol AND  e1.is_active=1" + 
				"        AND e1.call_put = 'call' " + 
				"        AND e2.call_put = 'put' AND e2.is_active=1" + 
				" ORDER BY abs(e1.strike - e.strike) ASC limit "+ BondStaticParams.ETD_OPTION_COMPARABLE_LIMIT + ")x"+
				" order by strike";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DerOpCoparable.class);
		query.setParameter("symbol", identifier);

		@SuppressWarnings("unchecked")
		List<DerOpCoparable> derOpCoparables = query.list();

		List<DerOpCoparableDTO> derOpCoparableDTOs = DozerHelper.map(dozerBeanMapper, derOpCoparables, DerOpCoparableDTO.class);
		return derOpCoparableDTOs;

	}

}

