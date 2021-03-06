package com.televisory.capitalmarket.daoimpl;

import java.util.Arrays;
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

import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.entities.cm.CMCompany;
import com.televisory.capitalmarket.entities.cm.IndustryBalanceModel;
import com.televisory.capitalmarket.entities.cm.IndustryMetaData;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.entities.cm.TicsSector;
import com.televisory.capitalmarket.entities.industry.CountryList;
import com.televisory.capitalmarket.entities.industry.IndustryFinancialData;
import com.televisory.capitalmarket.entities.industry.IndustryFinancialDataWithFlag;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class SectorRepositoryImpl implements SectorRepository{
	Logger _log = Logger.getLogger(SectorRepositoryImpl.class);

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmFactory;

	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<CountryList> findAllIndustryCountries(String ticsSectorCodeList, String ticsIndustryCodeList, String periodType, Date startDate, Date endDate, List<String> userCountyList) {
		_log.info("extracting countries for all sector");
		
		String tableName = "industry_data_af";
		if(periodType != null && periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY))
			tableName = "industry_data_qf";
		
		String baseQuery = "SELECT * FROM `country_list` WHERE `country_iso_code_3` in (SELECT distinct domicile_country_code FROM `"+ tableName +"` where `domicile_country_code` is not null ";
		
		if(ticsSectorCodeList != null && !ticsSectorCodeList.isEmpty())
			baseQuery += " and tics_sector_code in (:ticsSectorCodeList) ";
		if(userCountyList != null && !userCountyList.isEmpty())
			baseQuery += " and domicile_country_code in (:userCountyList) ";
		if(ticsIndustryCodeList != null && !ticsIndustryCodeList.isEmpty())
			baseQuery += " and tics_industry_code in (:ticsIndustryCodeList) ";
		if(startDate != null)
			baseQuery += " and applicable_date >= :startDate "; 
		if(endDate != null)
			baseQuery += " and applicable_date <= :endDate "; 
		baseQuery += " )"; 
		
		Query query = cmFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CountryList.class);
		
		
		if(ticsSectorCodeList != null && !ticsSectorCodeList.isEmpty()) 
			query.setParameterList("ticsSectorCodeList", Arrays.asList(ticsSectorCodeList.split("\\s*,\\s*")));
		if(userCountyList != null && !userCountyList.isEmpty()) 
			query.setParameterList("userCountyList", userCountyList);
		if(ticsIndustryCodeList != null && !ticsIndustryCodeList.isEmpty())
			query.setParameterList("ticsIndustryCodeList", Arrays.asList(ticsIndustryCodeList.split("\\s*,\\s*")));
		if(startDate != null)
			query.setParameter("startDate", startDate);
		if(endDate != null)
			query.setParameter("endDate", endDate); 
		
		@SuppressWarnings("unchecked")
		List<CountryList> data = query.list();
		return data;
	}

	@Override
	public List<IndustryFinancialDataDTO> getIDSectorDataYearly(String ticsSectorCode, List<String> userCountyList, String params, Date startDate, Date endDate, String currencyCode, Integer month){
		Query query = null;
		
		_log.info("Extracting industry Sector Data Yearly for: ticsSectorCode:"+ ticsSectorCode +", userCountyList: "+ userCountyList +", params: "+ params +", startDate: "+ startDate +", endDate: "+ endDate +", currencyCode: "+ currencyCode +", month: "+ month);
		
		String baseQuery;
		
		//list field cannot be null / it will give hibernate error if null is set in setParameterList
		if(userCountyList==null || userCountyList.get(0).equalsIgnoreCase("")){
			baseQuery="CALL get_id_sector_yr_params(:ticsSectorCode,null, :startDate, :endDate, :params, :currencyCode, :month)";
		}else{
			baseQuery="CALL get_id_sector_yr_params(:ticsSectorCode,:countryId, :startDate, :endDate, :params, :currencyCode, :month)";
		}
		
		query = cmFactory.getCurrentSession()
				.createSQLQuery(baseQuery)
				.addEntity(IndustryFinancialData.class);
		
		if(userCountyList!=null && !userCountyList.get(0).equalsIgnoreCase("")){
			query.setParameterList("countryId", userCountyList);
		}
		
		
		query.setParameter("ticsSectorCode", ticsSectorCode);
		query.setParameter("params",params);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currencyCode",currencyCode);
		query.setParameter("month", month);
		
		List<IndustryFinancialData> data = query.list();

		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);
		return sectorFinancialDataDTO;
	}
	@Override
	public List<IndustryFinancialDataDTO> getIDSectorDataQuarterly(String ticsSectorCode, List<String> userCountyList,String params, Date startDate, Date endDate, String currencyCode){
		Query query = null;
		
		
		_log.info("Extracting industry Sector Data Quarterly for: ticsSectorCode:"+ ticsSectorCode +", userCountyList: "+ userCountyList +", params: "+ params +", startDate: "+ startDate +", endDate: "+ endDate +", currencyCode: "+ currencyCode);
		
		String baseQuery;
		
		//list field cannot be null / it will give hibernate error if null is set in setParameterList
		if(userCountyList==null){
			baseQuery="CALL get_id_sector_qtr_params(:ticsSectorCode,null, :startDate, :endDate, :params, :currencyCode)";
		}else{
			baseQuery="CALL get_id_sector_qtr_params(:ticsSectorCode,:countryId, :startDate, :endDate, :params, :currencyCode)";
		}
		
		query = cmFactory.getCurrentSession()
				.createSQLQuery(baseQuery)
				.addEntity(IndustryFinancialData.class);

		if(userCountyList!=null){
			query.setParameterList("countryId", userCountyList);
		}
		
		query.setParameter("ticsSectorCode", ticsSectorCode);
		query.setParameter("params",params);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currencyCode",currencyCode);
		List<IndustryFinancialData> data = query.list();

		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);

		return sectorFinancialDataDTO;
	}

	@Override
	public List<IndustryFinancialDataDTO> getIDIndustryDataYearly(String ticsSectorCode, String ticsIndustryCode, String countryId, String params, Date startDate, Date endDate, String currencyCode, Integer month){

		Query query = null;
		_log.info("Extracting industry Data Yearly for: ticsSectorCode:"+ ticsSectorCode +", ticsIndustryCode: "+ ticsIndustryCode +", countryId: "+ countryId +", params: "+ params +", startDate: "+ startDate +", endDate: "+ endDate +", currencyCode: "+ currencyCode +", month: "+ month);
		query = cmFactory.getCurrentSession()
				.createSQLQuery("CALL get_id_industry_yr_params(:ticsSectorCode,:ticsIndustryCode, :countryId,:startDate,:endDate, :params, :currencyCode, :month)")
				.addEntity(IndustryFinancialData.class);
		query.setParameter("ticsSectorCode", ticsSectorCode);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("countryId", countryId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("params",params);
		query.setParameter("currencyCode",currencyCode);
		query.setParameter("month", month);
		List<IndustryFinancialData> data = query.list();
		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);

		return sectorFinancialDataDTO;
	}


	@Override
	public List<IndustryFinancialDataDTO> getIDIndustryDataQuarterly(String ticsSectorCode, String ticsIndustryCode, String countryId, String params, Date startDate, Date endDate, String currencyCode){
	
		Query query = null;
		_log.info("Extracting industry Data Quarterly for: ticsSectorCode:"+ ticsSectorCode +", ticsIndustryCode: "+ ticsIndustryCode +", countryId: "+ countryId +", params: "+ params +", startDate: "+ startDate +", endDate: "+ endDate +", currencyCode: "+ currencyCode);
		query = cmFactory.getCurrentSession()
				.createSQLQuery("CALL get_id_industry_qtr_params(:ticsSectorCode,:ticsIndustryCode, :countryId, :startDate,:endDate, :params, :currencyCode)")
				.addEntity(IndustryFinancialData.class);
		query.setParameter("ticsSectorCode", ticsSectorCode);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("countryId", countryId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("params",params);
		query.setParameter("currencyCode",currencyCode);
		List<IndustryFinancialData> data = query.list();

		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);

		return sectorFinancialDataDTO;
	}


	@Override
	public List<IndustryFinancialDataDTO> getIDCompanyDataYearly(String ticsIndustryCode, String companyId, String countryId,String params, Date startDate, Date endDate, String currencyCode, Integer month , Integer companyFlag) {
		Query query = null;
		_log.info("Extracting industry company Data Yearly for: ticsIndustryCode: "+ ticsIndustryCode +", companyId: "+ companyId +", countryId: "+ countryId +", params: "+ params +", startDate: "+ startDate +", endDate: "+ endDate +", currencyCode: "+ currencyCode +", month: "+ month + " , companyFlag: " + companyFlag);

		String querry = "CALL get_id_company_yr_params('"+ticsIndustryCode+"','"+companyId+"','"+countryId+"','"+startDate+"','"+endDate+"','"+params+"','"+currencyCode+"',"+month+","+companyFlag+")";
		querry = querry.replace("'null'", "null");

		_log.info(" ############  ::: " + querry);

		query = cmFactory.getCurrentSession()
				.createSQLQuery("CALL get_id_company_yr_params(:ticsIndustryCode, :companyId, :countryId, :startDate, :endDate, :params, :currencyCode, :month , :companyFlag)")
				//.createSQLQuery("CALL get_id_industry_company_yr_params(:ticsIndustryCode, :companyId, :countryId, :startDate, :endDate, :params, :currencyCode, :month , :companyFlag)")

				.addEntity(IndustryFinancialDataWithFlag.class);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("companyId", companyId);
		query.setParameter("countryId", countryId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("params",params);
		query.setParameter("currencyCode",currencyCode);
		query.setParameter("month", month);
		query.setParameter("companyFlag", companyFlag);

		List<IndustryFinancialDataWithFlag> data = query.list();
		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);
		return sectorFinancialDataDTO;
	}

	@Override
	public List<IndustryFinancialDataDTO> getIDCompanyDataQuarterly(String ticsIndustryCode, String companyId, String countryId,String params, Date startDate, Date endDate, String currencyCode,  Integer companyFlag) {
		Query query = null;
		_log.info("Extracting industry company Data Quarterly for: ticsIndustryCode: "+ ticsIndustryCode +", companyId: "+ companyId +", countryId: "+ countryId +", params: "+ params +", startDate: "+ startDate +", endDate: "+ endDate +", currencyCode: "+ currencyCode + ",companyFlag" + companyFlag);
		query = cmFactory.getCurrentSession()
				.createSQLQuery("CALL get_id_company_qtr_params(:ticsIndustryCode, :companyId, :countryId, :startDate, :endDate, :params, :currencyCode,:companyFlag)")
				//.createSQLQuery("CALL get_id_industry_company_qtr_params(:ticsIndustryCode, :companyId, :countryId, :startDate, :endDate, :params, :currencyCode,:companyFlag)")
				.addEntity(IndustryFinancialDataWithFlag.class);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("companyId", companyId);
		query.setParameter("countryId", countryId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("params",params);
		query.setParameter("currencyCode",currencyCode);
		query.setParameter("companyFlag", companyFlag);
		List<IndustryFinancialDataWithFlag> data = query.list();

		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);
		return sectorFinancialDataDTO;
	}

	@Override
	public List<TicsSector> getAllSectors() {
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_sector where tics_sector_code IN (SELECT DISTINCT tics_sector_code FROM industry_data_af WHERE period_type=:periodType AND tics_sector_code IS NOT NULL AND tics_industry_code IS NOT NULL ) AND is_active = 1 ORDER BY tics_sector_name ASC")
				.addEntity(TicsSector.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		List<TicsSector> data = query.list();
		return data;
	}

	@Override
	public List<TicsIndustry> getTicsIndustryByTicsSectorCode(String ticsSectorCode) {
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL ) AND tics_sector_code =:ticsSectorCode order by tics_industry_name asc")
				.addEntity(TicsIndustry.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		query.setParameter("ticsSectorCode", ticsSectorCode);
		List<TicsIndustry> data = query.list();
		return data;
	}

	@Override
	public List<TicsIndustry> getIndustryByTicsIndustryCode(String ticsIndustryCode) {
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where tics_industry_code =:ticsIndustryCode")
				.addEntity(TicsIndustry.class);		
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		List<TicsIndustry> data = query.list();
		return data;
	}

	/*@Override
	public List<IndustryFinancialDataDTO> getIndustryCountryByField(String fieldName, String periodType, Date startDate, Date endDate, String ticsSectorCode, String ticsIndustryCode, String countryCode, Integer recCount) {
		Query query = null;
		String queryString = "", orderby = "DESC";
		String tableName = "industry_data_af";
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			tableName = "industry_data_qf";
		}

		if(fieldName.equalsIgnoreCase(CMStatic.CASH_CONVERSION_FIELD_NAME)) {
			orderby = "ASC";
		}


		if(countryCode!=null && !countryCode.equals(""))
			queryString = "SELECT x.id, x.company_count, x.tics_sector_code, x.tics_industry_code, x.company_id, x.domicile_country_code, :periodType as period_type, x.date as date, x.applicable_date as applicable_date, y.currency, y.unit, x.data as data, y.field_name, y.display_order, y.display_level, y.display_flag, y.short_name, y.description from (select * from(select id, date, applicable_date, tics_sector_code, tics_industry_code, domicile_country_code, company_id,company_count, :fieldName as field_name, "+fieldName+" as data from "+tableName+" where tics_sector_code =:ticsSectorCode and tics_industry_code=:ticsIndustryCode and domicile_country_code=:countryCode and company_id is NOT NULL )abc) x join industry_balance_model y on y.field_name = x.field_name and x.applicable_date BETWEEN :startDate AND :endDate order by data "+orderby+" ";
		else {
			queryString = "SELECT x.id, x.company_count, x.tics_sector_code, x.tics_industry_code, x.company_id, x.domicile_country_code, :periodType as period_type, x.date as date, x.applicable_date as applicable_date, y.currency, y.unit, x.data as data, y.field_name, y.display_order, y.display_level, y.display_flag, y.short_name, y.description from (select * from(select id, date, applicable_date, tics_sector_code, tics_industry_code, domicile_country_code, company_id,company_count, :fieldName as field_name, "+fieldName+" as data from "+tableName+" where tics_sector_code =:ticsSectorCode and tics_industry_code=:ticsIndustryCode and company_id is NULL and domicile_country_code is NOT NULL)abc) x join industry_balance_model y on y.field_name = x.field_name and x.date BETWEEN :startDate AND :endDate order by data "+orderby+" ";
		}

		query = cmFactory.getCurrentSession().createSQLQuery(queryString).addEntity(IndustryFinancialData.class);
		query.setParameter("fieldName", fieldName);
		query.setParameter("ticsSectorCode", ticsSectorCode);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("periodType", periodType);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		//query.setParameter("recCount", recCount); 
		if(countryCode!=null && !countryCode.equals("")) {
			query.setParameter("countryCode", countryCode);
		}

		List<IndustryFinancialData> data = query.list();
		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);
		return sectorFinancialDataDTO;
	}*/

	@Override
	public List<TicsIndustry> getTicsIndustryByTicsSectorCode() {

		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType "
						+ "AND tics_industry_code IS NOT NULL) ORDER BY tics_industry_name ASC")
						.addEntity(TicsIndustry.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		List<TicsIndustry> data = query.list();
		return data;
	}

	@Override
	public List<TicsIndustry> getTicsIndustryByFactsetIndustry(String factsetIndustry) {
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where factset_industry =:factsetIndustry")
				.addEntity(TicsIndustry.class);

		query.setParameter("factsetIndustry",factsetIndustry);

		@SuppressWarnings("unchecked")
		List<TicsIndustry> data = query.list();
		return data;
	}

	@Override
	public List<TicsIndustry> getTicsIndustryByTicsSectorCode(String ticsSectorCode, String factsetIndustry) {
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType "
						+ "AND tics_industry_code IS NOT NULL) and factset_industry=:factsetIndustry")
						.addEntity(TicsIndustry.class);

		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		query.setParameter("factsetIndustry", factsetIndustry);

		@SuppressWarnings("unchecked")
		List<TicsIndustry> data = query.list();
		return data;
	}

	@Override
	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType) {

		_log.info("getting the financial parameters for the industry: "+industryType);

		String[] industryList=industryType.split("\\s*,\\s*");

		String baseQuery = "from IndustryBalanceModel where ";

		for(int i=0;i<=industryList.length-1;i++){

			if(i==0){
				baseQuery+="( factsetIndustry like '%"+industryList[i]+"%'";
			}else{
				baseQuery+=" OR factsetIndustry like '%"+industryList[i]+"%'";
			}

			if(industryList.length-1==i){
				baseQuery+=")";
			}
		}

		baseQuery+=" order by displayOrder asc";

		Query query = cmFactory.getCurrentSession().createQuery(baseQuery);

		@SuppressWarnings("unchecked")
		List<IndustryBalanceModel> data = (List<IndustryBalanceModel>) query.list();

		List<BalanceModelDTO> industryBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return industryBalanceModelDTOs;

	}


	@Override
	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, String type) {

		_log.info("getting the financial parameters for the industry: "+industryType+" financialType : "+type);

		String[] industryList=industryType.split("\\s*,\\s*");

		String baseQuery = "from IndustryBalanceModel where ";

		for(int i=0;i<=industryList.length-1;i++){

			if(i==0){
				baseQuery+="( factsetIndustry like '%"+industryList[i]+"%'";
			}else{
				baseQuery+=" OR factsetIndustry like '%"+industryList[i]+"%'";
			}

			if(industryList.length-1==i){
				baseQuery+=")";
			}
		}

		baseQuery+=" and financialType =:financialType order by displayOrder asc";

		Query query = cmFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("financialType", type);

		@SuppressWarnings("unchecked")
		List<IndustryBalanceModel> data = (List<IndustryBalanceModel>) query.list();

		List<BalanceModelDTO> industryBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return industryBalanceModelDTOs;
	}


	@Override
	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, Integer watchlistFlag) {

		//_log.info("getting the financial parameters for the industry: "+industryType +", watchlistFlag: "+ watchlistFlag);

		//	String baseQuery = "from IndustryBalanceModel where factSetIndustry like :industryType and watchlistFlag = :watchlistFlag order by displayOrder asc";

		String[] industryList=industryType.split("\\s*,\\s*");

		String baseQuery = "from IndustryBalanceModel where ";

		for(int i=0;i<=industryList.length-1;i++){

			if(i==0){
				baseQuery+="( factsetIndustry like '%"+industryList[i]+"%'";
			}else{
				baseQuery+=" OR factsetIndustry like '%"+industryList[i]+"%'";
			}

			if(industryList.length-1==i){
				baseQuery+=")";
			}
		}

		baseQuery+=" and watchlistFlag = :watchlistFlag order by displayOrder asc";

		Query query = cmFactory.getCurrentSession().createQuery(baseQuery);
		//query.setParameter("industryType", "%"+industryType+"%");
		query.setParameter("watchlistFlag", watchlistFlag);


		@SuppressWarnings("unchecked")
		List<IndustryBalanceModel> data = (List<IndustryBalanceModel>) query.list();

		List<BalanceModelDTO> industryBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return industryBalanceModelDTOs;

	}


	@Override
	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, String type, Integer watchlistFlag) {

		_log.info("getting the financial parameters for the industry: "+industryType +", watchlistFlag: "+ watchlistFlag);

		//	String baseQuery = "from IndustryBalanceModel where factSetIndustry like :industryType and watchlistFlag = :watchlistFlag order by displayOrder asc";

		String[] industryList=industryType.split("\\s*,\\s*");

		String baseQuery = "from IndustryBalanceModel where ";

		for(int i=0;i<=industryList.length-1;i++){

			if(i==0){
				baseQuery+="( factsetIndustry like '%"+industryList[i]+"%'";
			}else{
				baseQuery+=" OR factsetIndustry like '%"+industryList[i]+"%'";
			}

			if(industryList.length-1==i){
				baseQuery+=")";
			}
		}

		baseQuery+=" and watchlistFlag = :watchlistFlag and financialType =:financialType order by displayOrder asc";

		Query query = cmFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("watchlistFlag", watchlistFlag);
		query.setParameter("financialType", type);


		@SuppressWarnings("unchecked")
		List<IndustryBalanceModel> data = (List<IndustryBalanceModel>) query.list();

		List<BalanceModelDTO> industryBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return industryBalanceModelDTOs;
	}


	@Override
	public List<BalanceModelDTO> getIndustryFinancialModel(String industryType, List<String> fields) {
		_log.info("getIndustryFinancialModel ::: " + industryType +", fields: " + fields);
		String baseQuery = "";
		if(industryType!=null){
			baseQuery = "from IndustryBalanceModel where factSetIndustry like :industryType and fieldName in(:fields)";
		}else{
			baseQuery = "from IndustryBalanceModel where fieldName in(:fields)";
		}
		
		Query query = cmFactory.getCurrentSession().createQuery(baseQuery);
		if(industryType!=null){
			query.setParameter("industryType", industryType);
		}
		query.setParameterList("fields", fields);
		@SuppressWarnings("unchecked")
		List<IndustryBalanceModel> data = (List<IndustryBalanceModel>) query.list();
		List<BalanceModelDTO> industryBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);
		return industryBalanceModelDTOs;
	}




	/*	@Override
	public List<IndustryFinancialDataDTO> getIndustryCompanyData(String fieldName, String periodType, Date startDate, Date endDate, String ticsSectorCode, String ticsIndustryCode, String countryCode) {
		Query query = null;
		String queryString = "";
		String tableName = "industry_data_af";
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			tableName = "industry_data_qf";
		}
		if(countryCode!=null && !countryCode.equals("")) {
			queryString = "SELECT x.id, x.company_count, x.tics_sector_code, x.tics_industry_code, x.company_id, x.domicile_country_code, :periodType as period_type, x.date as date, x.applicable_date as applicable_date, y.currency, y.unit, x.data as data, y.field_name, y.display_order, y.display_level, y.display_flag, y.short_name, y.description from (select * from(select id, date, applicable_date, tics_sector_code, tics_industry_code, domicile_country_code, company_id,company_count, :fieldName as field_name, "+fieldName+" as data from "+tableName+" where tics_sector_code =:ticsSectorCode and tics_industry_code=:ticsIndustryCode and domicile_country_code=:countryCode and company_id is NOT NULL )abc) x join industry_balance_model y on y.field_name = x.field_name and y.display_flag =1 and x.applicable_date BETWEEN :startDate AND :endDate order by data DESC ";
		}else {
			queryString = "SELECT x.id, x.company_count, x.tics_sector_code, x.tics_industry_code, x.company_id, x.domicile_country_code, :periodType as period_type, x.date as date, x.applicable_date as applicable_date, y.currency, y.unit, x.data as data, y.field_name, y.display_order, y.display_level, y.display_flag, y.short_name, y.description from (select * from(select id, date, applicable_date, tics_sector_code, tics_industry_code, domicile_country_code, company_id,company_count, :fieldName as field_name, "+fieldName+" as data from "+tableName+" where tics_sector_code =:ticsSectorCode and tics_industry_code=:ticsIndustryCode and company_id is NOT NULL)abc) x join industry_balance_model y on y.field_name = x.field_name and y.display_flag =1 and x.date BETWEEN :startDate AND :endDate order by data DESC ";
		}

		query = cmFactory.getCurrentSession().createSQLQuery(queryString).addEntity(IndustryFinancialData.class);
		query.setParameter("fieldName", fieldName);
		query.setParameter("ticsSectorCode", ticsSectorCode);
		query.setParameter("ticsIndustryCode", ticsIndustryCode);
		query.setParameter("periodType", periodType);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(countryCode!=null && !countryCode.equals("")) {
			query.setParameter("countryCode", countryCode);
		}

		List<IndustryFinancialData> data = query.list();
		List<IndustryFinancialDataDTO> sectorFinancialDataDTO = DozerHelper.map(dozerBeanMapper, data, IndustryFinancialDataDTO.class);
		return sectorFinancialDataDTO;
	}*/


	@Override
	public List<TicsIndustry> getIndustryByIdList(List<String> customIndustryIds){
		//_log.info("extracting Company metadata info of : "+customIndustryIds+" from database");

		Query query = cmFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM tics_industry where tics_industry_code in (:customIndustryIds)").addEntity(TicsIndustry.class);
		query.setParameterList("customIndustryIds",customIndustryIds);

		List<TicsIndustry> data = (List<TicsIndustry>) query.list();

		/*List<CountryListDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);*/

		return data;

	}

	@Override
	public String getCountryDomocileCode(String countryId){
		//_log.info("extracting Country Code of : "+countryId+" from database");

		Query query = cmFactory.getCurrentSession()
				.createSQLQuery("SELECT country_iso_code_3 FROM country_list where country_id = :countryId");
		query.setParameter("countryId",countryId);

		List<Integer> data = query.list();

		/*List<CountryListDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CountryListDTO.class);*/

		return String.valueOf(data.get(0));
	}

	@Override
	public List<IndustryMetaData> getPeriodByTicsIndustryNCountryCode(String ticsIndustryCode, String countryCode,String ticsSectorCode) {
		// TODO Auto-generated method stub
		_log.info("extracting Company metadata info of : "+countryCode +" \n from database and ticsIndustryCode : "+ticsIndustryCode);
		Query query = null;
		if(countryCode!=null && !countryCode.equals("") && ticsIndustryCode!=null && ticsSectorCode!=null) {
			_log.info("cc null");
			List<String> countryCodeList = Arrays.asList(countryCode.split("\\s*,\\s*"));
			/*query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code = :ticsIndustryCode and domicile_country_code = :countryCode and company_id is null AND date < CURDATE()-interval 90 day group by date order by date desc limit 5)x" + 
						" union " + 
						"select * from (SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code = :ticsIndustryCode and domicile_country_code = :countryCode and company_id is null AND date < CURDATE()-interval 60 day  group by date order by date desc limit 5)y").addEntity(IndustryMetaData.class);*/
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code=:ticsIndustryCode and tics_sector_code =:ticsSectorCode and domicile_country_code in (:countryCode) and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code=:ticsIndustryCode and tics_sector_code=:ticsSectorCode  and domicile_country_code in (:countryCode) and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			query.setParameterList("countryCode",countryCodeList);
			query.setParameter("ticsIndustryCode",ticsIndustryCode);
			query.setParameter("ticsSectorCode",ticsSectorCode);
		}else if (countryCode==null && ticsIndustryCode!=null && ticsSectorCode!=null){
			_log.info("cc not null");
			/*query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code = :ticsIndustryCode and domicile_country_code is null and company_id is null AND date < CURDATE()-interval 90 day  group by date order by date desc limit 5)x" + 
							" union " + 
							"select * from (SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code = :ticsIndustryCode and domicile_country_code is null and company_id is null AND date < CURDATE()-interval 60 day group by date order by date desc limit 5)y").addEntity(IndustryMetaData.class);*/
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code = :ticsIndustryCode  and tics_sector_code =:ticsSectorCode and domicile_country_code is null and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code = :ticsIndustryCode  and tics_sector_code =:ticsSectorCode and domicile_country_code is null and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			query.setParameter("ticsIndustryCode",ticsIndustryCode);
			query.setParameter("ticsSectorCode",ticsSectorCode);
		}
		else if(countryCode==null  && ticsIndustryCode==null && ticsSectorCode!=null) {
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_sector_code =:ticsSectorCode and domicile_country_code is null and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE  tics_sector_code =:ticsSectorCode and domicile_country_code is null and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			
			query.setParameter("ticsSectorCode",ticsSectorCode);
			
		}
		
		else if(countryCode==null  && ticsIndustryCode!=null && ticsSectorCode==null) {
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code=:ticsIndustryCode and domicile_country_code is null and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code=:ticsIndustryCode and domicile_country_code is null and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			
			query.setParameter("ticsIndustryCode",ticsIndustryCode);
		}
		else if(countryCode==null  && ticsIndustryCode==null && ticsSectorCode==null) {
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE  domicile_country_code is null and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE  domicile_country_code is null and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			
		}
		else if(countryCode!=null  && ticsIndustryCode==null && ticsSectorCode==null) {
			List<String> countryCodeList = Arrays.asList(countryCode.split("\\s*,\\s*"));
			/*query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code = :ticsIndustryCode and domicile_country_code = :countryCode and company_id is null AND date < CURDATE()-interval 90 day group by date order by date desc limit 5)x" + 
						" union " + 
						"select * from (SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code = :ticsIndustryCode and domicile_country_code = :countryCode and company_id is null AND date < CURDATE()-interval 60 day  group by date order by date desc limit 5)y").addEntity(IndustryMetaData.class);*/
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE  domicile_country_code in (:countryCode) and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE  domicile_country_code in (:countryCode) and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			query.setParameterList("countryCode",countryCodeList);
		}
		
		else if(countryCode!=null  && ticsIndustryCode!=null && ticsSectorCode==null) {
			List<String> countryCodeList = Arrays.asList(countryCode.split("\\s*,\\s*"));
			query = cmFactory.getCurrentSession()
					.createSQLQuery("select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_af WHERE tics_industry_code=:ticsIndustryCode and  domicile_country_code in(:countryCode) and company_id is null)x where date < CURDATE()-interval 60 day group by date order by date desc limit 20)x1" + 
							" union " + 
							" select * from (select * from( SELECT id, tics_sector_code, tics_industry_code, company_id, domicile_country_code, date, period_type, company_count, country_coverage, fx  FROM industry_data_qf WHERE tics_industry_code=:ticsIndustryCode and domicile_country_code in(:countryCode) and company_id is null)y where date < CURDATE()-interval 60 day group by date order by date desc limit 20)y1").addEntity(IndustryMetaData.class);
			query.setParameterList("countryCode",countryCodeList);
			query.setParameter("ticsIndustryCode",ticsIndustryCode);
			
		}
	

		  List<IndustryMetaData> data = (List<IndustryMetaData>) query.list();
		return data;
	}

	@Override
	public List<TicsIndustry> getTicsIndustryByCountryCode(String countryList) {

		_log.info("getting the industries based on the country ::::: "+countryList);

		List<String> countryCodeList = Arrays.asList(countryList.split("\\s*,\\s*"));

		Query query = cmFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM tics_industry WHERE tics_industry_code IN ( SELECT DISTINCT ( tics_industry_code) "
						+ "from industry_data_af af WHERE domicile_country_code in (:countryList))").addEntity(TicsIndustry.class);

		query.setParameterList("countryList",countryCodeList);

		List<TicsIndustry> industryList = (List<TicsIndustry>) query.list();
		return industryList;
	}

	@Override
	public List<TicsIndustry> getTicsIndustryBySearchParam(String searchParam,Integer resultCount) {
		_log.info("running query with  param "+searchParam);
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType "
						+ "AND tics_industry_code IS NOT NULL) and tics_industry_name like :searchParam ORDER BY tics_industry_name ASC")
						.addEntity(TicsIndustry.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		query.setParameter("searchParam", "%"+searchParam+"%");

		if(resultCount!=null){
			query.setFirstResult(0);
			query.setMaxResults(resultCount);
		}

		List<TicsIndustry> data = query.list();
		return data;

	}

	@Override
	public CompanyDTO getDefaultCountryForIndustry(List<String> countryCode) {
		String baseQuery = "SELECT *,'public' as entity_type,count(*) as data_count FROM `company_list` where domicile_country_code IN(:countryCodes) group by domicile_country_code order by data_count desc ";
		
		Query query = cmFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(CMCompany .class);
		query.setParameterList("countryCodes", countryCode);

		List<CMCompany> data = (List<CMCompany>) query.list();

		List<CompanyDTO> cmCompanyDTOs = DozerHelper.map(dozerBeanMapper, data, CompanyDTO.class);

		return cmCompanyDTOs.get(0);
	}

	@Override
	public List<BalanceModelDTO> getIndustryFinancialParameterForIC(String industryType,
			Integer icFlag) {

		_log.info("getting the financial parameters for the IC industry: "+industryType+" ic flag "+icFlag);

		String[] industryList=industryType.split("\\s*,\\s*");

		String baseQuery = "from IndustryBalanceModel where ";

		for(int i=0;i<=industryList.length-1;i++){

			if(i==0){
				baseQuery+="( factsetIndustry like '%"+industryList[i]+"%'";
			}else{
				baseQuery+=" OR factsetIndustry like '%"+industryList[i]+"%'";
			}

			if(industryList.length-1==i){
				baseQuery+=")";
			}
		}

		if(icFlag!= null){
			baseQuery+=" and icFlag =:icFlag ";
		}

		baseQuery+=" order by displayOrder asc";

		Query query = cmFactory.getCurrentSession().createQuery(baseQuery);

		if(icFlag!= null){
			query.setParameter("icFlag", icFlag);
		}


		@SuppressWarnings("unchecked")
		List<IndustryBalanceModel> data = (List<IndustryBalanceModel>) query.list();

		List<BalanceModelDTO> industryBalanceModelDTOs = DozerHelper.map(dozerBeanMapper, data, BalanceModelDTO.class);

		return industryBalanceModelDTOs;

	}

	@Override
	public List<TicsSector> getSectorBySectorCode(String ticsSectorCode) {
		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_sector where tics_sector_code =:tics_sector_code AND is_active = 1 ORDER BY tics_sector_name ASC")
				.addEntity(TicsSector.class);
		query.setParameter("tics_sector_code", ticsSectorCode);
		List<TicsSector> data = query.list();
		return data;
	}

	@Override
	public List<TicsIndustry> getTicsIndustryByTicsIndustryCode(String ticsIndustryCode) {

		Query query = null;
		query = cmFactory.getCurrentSession()
				.createSQLQuery(" SELECT * FROM tics_industry where tics_industry_code IN (SELECT DISTINCT tics_industry_code FROM industry_data_af WHERE period_type=:periodType AND tics_industry_code IS NOT NULL ) AND tics_industry_code =:tics_industry_code order by tics_industry_name asc")
				.addEntity(TicsIndustry.class);
		query.setParameter("periodType", CMStatic.PERIODICITY_YEARLY);
		query.setParameter("tics_industry_code", ticsIndustryCode);
		List<TicsIndustry> data = query.list();
		return data;
	
		
	}

}
