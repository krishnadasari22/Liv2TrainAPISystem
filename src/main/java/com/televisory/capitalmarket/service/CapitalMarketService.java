package com.televisory.capitalmarket.service;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.privatecompany.dto.EntityStructureDTO;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.CMStockRepository;
import com.televisory.capitalmarket.dao.GlobalSearchRepository;
import com.televisory.capitalmarket.dto.CMCDTO;
import com.televisory.capitalmarket.dto.CMExchangeDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;
import com.televisory.capitalmarket.dto.EntityNode;
import com.televisory.capitalmarket.dto.GlobalSearchIndustryDTO;
import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.PCCompanyDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorLatestDataDTO;
import com.televisory.capitalmarket.entities.cm.GlobalSearchCompany;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.factset.dto.FFEntityProfileDTO;
import com.televisory.capitalmarket.model.CommodityParams;
import com.televisory.capitalmarket.model.CompanyMetaData;
import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.model.EconomyRequest;
import com.televisory.capitalmarket.model.EquityRequest;
import com.televisory.capitalmarket.model.GlobalSearchResponseModel;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;
/**
 * 
 * @author vinay
 *
 */
@Service
public class CapitalMarketService {

	Logger _log = Logger.getLogger(CapitalMarketService.class);

	@Autowired
	EconomyService economyService;

	@Autowired
	ExcelReportService excelService;

	@Autowired
	ValidationService validationService;

	@Autowired
	CMRepository cmRepository;

	@Autowired
	GlobalSearchRepository globalSearchRepository;

	@Autowired
	CMStockService cmStockService;

	@Autowired
	CMStockRepository cmStockRepository;

	@Autowired
	CMFinancialDataService cmFinancialDataService;

	@Autowired
	RatioService ratioService;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	ExecutorService executorPool;

	@Autowired
	CommodityService commodityService;

	@Autowired
	SectorService industryService;
	
	static int tRow;
	/**
	 * 
	 * @param downloadRequest
	 * @return
	 * @throws Exception
	 */
	public DownloadRequest getResponseData(DownloadRequest downloadRequest) throws Exception {
		validationService.ValidateDownloadDataRequest(downloadRequest);
		downloadRequest = getEquityData(downloadRequest);
		downloadRequest = getEconomyData(downloadRequest);	
		downloadRequest = getCommodityData(downloadRequest);
		validationService.validateDownloadDataResponse(downloadRequest);
		return downloadRequest;
	}

	/**
	 * Method to download commodity data
	 * @param downloadRequest
	 * @return
	 */
	private DownloadRequest getCommodityData(DownloadRequest downloadRequest) {

		try{
			if("dataDownload".equalsIgnoreCase(downloadRequest.getCommodity().getType())){

				Date startDate=downloadRequest.getStartDate() ;
				Date endDate = downloadRequest.getEndDate();

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				Date date = null;

				if(startDate==null){
					try {
						date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					startDate=date;
				}

				if(endDate==null){
					endDate=new Date();
				}

				List<CommodityParams> commodityList = downloadRequest.getCommodity().getCommodityParams();
				List<String> commoditySymbolList = new ArrayList<>();
				commodityList.forEach((commodityParams)->{
					commoditySymbolList.add(commodityParams.getSymbol());
				});
				downloadRequest.getCommodity().setCommodityData(commodityService.getCommodityHistoricalData(commoditySymbolList, downloadRequest.getCommodity().getCurrency(), startDate, endDate));
				downloadRequest.getCommodity().setCommodityList(commoditySymbolList);
			}else if(downloadRequest.getCommodity()!=null
					&& downloadRequest.getCommodity().getCommodityList()!=null
					&& !downloadRequest.getCommodity().getCommodityList().isEmpty()){

				Date startDate=downloadRequest.getStartDate() ;
				Date endDate = downloadRequest.getEndDate();

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				Date date = null;

				if(startDate==null){
					try {
						date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					startDate=date;
				}

				if(endDate==null){
					endDate=new Date();
				}
				downloadRequest.getCommodity().setCommodityData(commodityService.getCommodityHistoricalData(downloadRequest.getCommodity().getCommodityList(), downloadRequest.getCommodity().getCurrency(), startDate, endDate));

			}

		}catch(Exception e){
			_log.error(e.getMessage());
		}
		return downloadRequest;
	}


	/**
	 * 
	 * @param downloadRequest
	 * @return
	 */
	private DownloadRequest getEconomyData(DownloadRequest downloadRequest) {

		if(downloadRequest.getEconomy() != null && downloadRequest.getEconomy().size() > 0) {

			for (EconomyRequest economyRequest : downloadRequest.getEconomy()) {

				if(economyRequest.getCurrency()!=null && economyRequest.getCurrency().equals("")) {
					economyRequest.setCurrency(null);
				}

				switch(economyRequest.getType()) {
				case CMStatic.TYPE_ECONOMY_INDICATOR :
					economyRequest.setEconomyData(getEconomybyIndicator(downloadRequest.getStartDate(), downloadRequest.getEndDate(),economyRequest));
					break;
				case CMStatic.TYPE_ECONOMY_COUNTRY :
					economyRequest.setEconomyData(getEconomybyCountry(economyRequest, downloadRequest.getStartDate(), downloadRequest.getEndDate()));
					break;
				default : 
					_log.warn("Data not available for Economy: "+ economyRequest.getType() +"-"+ economyRequest.getId());
				}		
			}
		}	

		return downloadRequest;
	}


	/**
	 * get economy data based on country
	 * @param economyRequest
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<IndicatorHistoricalDataDTO> getEconomybyCountry(EconomyRequest economyRequest, Date startDate, Date endDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = null;

		List<String> countryIsoCodeList = new ArrayList<>();		
		countryIsoCodeList.add(economyRequest.getId());

		String periodType = economyRequest.getPeriodicity();

		List<String> macroIndicatorList = economyRequest.getFilterList();
		String currency =economyRequest.getCurrency();

		if(startDate==null){
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate=date;
		}

		if(endDate==null){
			LocalDate now = LocalDate.now(); 
			LocalDate lastDay = now.with(lastDayOfYear()); 

			endDate=Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

		if(currency!=null){
			return economyService.getDataByIndicatorAndCountry(countryIsoCodeList,macroIndicatorList,periodType,currency, startDate, endDate);
		}else{
			return economyService.getDataByIndicatorAndCountry(countryIsoCodeList,macroIndicatorList,periodType, startDate, endDate);
		}

	}


	/**
	 * Method to get indicator class
	 * @param macroIndicator
	 * @param countryList
	 * @param startDate
	 * @param endDate
	 * @param economyRequest
	 * @return
	 */
	private List<IndicatorHistoricalDataDTO> getEconomybyIndicator(Date startDate, Date endDate,EconomyRequest economyRequest) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = null;

		List<String> macroIndicatorList = new ArrayList<>();		
		macroIndicatorList.add(economyRequest.getId());

		String periodType = economyRequest.getPeriodicity();

		List<String> countryIsoCodeList = economyRequest.getFilterList();
		String currency =economyRequest.getCurrency();

		if(startDate==null){
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate=date;
		}

		if(endDate==null){
			LocalDate now = LocalDate.now(); 
			LocalDate lastDay = now.with(lastDayOfYear()); 

			endDate=Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

		if(currency!=null){
			return economyService.getDataByIndicatorAndCountry(countryIsoCodeList,macroIndicatorList,periodType,currency, startDate, endDate);
		}else{
			return economyService.getDataByIndicatorAndCountry(countryIsoCodeList,macroIndicatorList,periodType, startDate, endDate);
		}
	}

	/**
	 * 
	 * @param downloadRequest
	 * @return
	 * @throws Exception
	 */
	private DownloadRequest getEquityData(DownloadRequest downloadRequest) throws Exception {
		if (downloadRequest.getEquity() != null && downloadRequest.getEquity().size() > 0) {
			for (EquityRequest equityRequest : downloadRequest.getEquity()) {
				try {
					getFactSetEquityData(equityRequest, downloadRequest.getStartDate(), downloadRequest.getEndDate());
				} catch (Exception e) {
					_log.error("Problem in getting data for: "+ equityRequest.getCode() +", type: "+ equityRequest.getType());
				}
			}
		}
		return downloadRequest;
	}


	/**
	 * 
	 * @param equityRequest
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
	private void getFactSetEquityData(EquityRequest equityRequest,Date startDate,Date endDate) throws Exception {

		List<CompanyFinancialMINDTO> companyFinancialList=new ArrayList<>();
		List<CompanyFinancialMINDTO> financialRatioList=new ArrayList<>();

		if(equityRequest.getCurrency()!=null && equityRequest.getCurrency().equals("")) {
			equityRequest.setCurrency(null);
		}

		if(equityRequest.getEntityType() != null && equityRequest.getEntityType().equalsIgnoreCase(CMStatic.ENTITY_TYPE_PRIVATE)) {
			
			switch (equityRequest.getDataType()) {
				case CMStatic.DATA_TYPE_BALANCE_SHEET:
					_log.debug("Getting BS for: "+ equityRequest.getEntityType() +"-"+ equityRequest.getType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getPrivateCompanyFinancial((String)equityRequest.getCode(), CMStatic.DATA_TYPE_BALANCE_SHEET_CODE,  equityRequest.getPeriodicity(),  startDate, endDate,equityRequest.getCurrency(), equityRequest.getRestated()));
					break;
				case CMStatic.DATA_TYPE_PNL:
					_log.debug("Getting PNL for: "+ equityRequest.getEntityType() +"-"+ equityRequest.getType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getPrivateCompanyFinancial((String) equityRequest.getCode(), CMStatic.DATA_TYPE_IS_CODE, equityRequest.getPeriodicity(), startDate, endDate,equityRequest.getCurrency(),equityRequest.getRestated()));
					break;
				case CMStatic.DATA_TYPE_CASH_FLOW:
					_log.debug("Getting CF for: "+ equityRequest.getEntityType() +"-"+ equityRequest.getType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getPrivateCompanyFinancial((String) equityRequest.getCode(), CMStatic.DATA_TYPE_CASH_FLOW_CODE, equityRequest.getPeriodicity(), startDate, endDate,equityRequest.getCurrency(),equityRequest.getRestated()));
					break;
				case CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE:
					_log.debug("Getting FR for: "+ equityRequest.getEntityType() +"-"+ equityRequest.getType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getPrivateCompanyFinancial((String) equityRequest.getCode(), CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE, equityRequest.getPeriodicity(), startDate, endDate,equityRequest.getCurrency(),equityRequest.getRestated()));
					break;
				default :
					_log.warn("Not implemented for: "+ equityRequest.getEntityType() +" - "+ equityRequest.getExchange() +"-"+ equityRequest.getType() +"-"+ equityRequest.getDataType());
			}
			
		} else {
			//For equity type public
			switch (equityRequest.getDataType()) {
	
				case CMStatic.DATA_TYPE_STOCK_PRICE:
					_log.info("Getting Price for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
					equityRequest.setStockPriceDTOs(cmStockService.getFactSetCompanyStockPrice((String) equityRequest.getCode(), equityRequest.getPeriodicity(), startDate, endDate,equityRequest.getCurrency()));
					break;
					
				case CMStatic.DATA_TYPE_BETA:
					_log.info("Getting BETA for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
					equityRequest.setBetaDatas(cmStockService.getCompanyBeta((String) equityRequest.getCode(), equityRequest.getPeriodicity(), equityRequest.getFilterList(),equityRequest.getCurrency()));
					break;
					
				case CMStatic.DATA_TYPE_BALANCE_SHEET:
					_log.debug("Getting BS for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getCompanyFinancial((String)equityRequest.getCode(), CMStatic.DATA_TYPE_BALANCE_SHEET_CODE,  equityRequest.getPeriodicity(),  startDate, endDate,equityRequest.getCurrency(), equityRequest.getRestated()));
					break;
					
				case CMStatic.DATA_TYPE_PNL:
					_log.debug("Getting PNL for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getCompanyFinancial((String) equityRequest.getCode(), CMStatic.DATA_TYPE_IS_CODE, equityRequest.getPeriodicity(), startDate, endDate,equityRequest.getCurrency(),equityRequest.getRestated()));
					break;
					
				case CMStatic.DATA_TYPE_CASH_FLOW:
					_log.debug("Getting CF for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
					equityRequest.setCompanyFinancialDTOs(cmFinancialDataService.getCompanyFinancial((String) equityRequest.getCode(), CMStatic.DATA_TYPE_CASH_FLOW_CODE, equityRequest.getPeriodicity(), startDate, endDate,equityRequest.getCurrency(),equityRequest.getRestated()));
					break;

				case CMStatic.DATA_TYPE_VALUATION_RATIO_CODE:
					_log.info("Getting VR for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
					companyFinancialList =cmFinancialDataService.getCompanyFinancial((String)equityRequest.getCode(), CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE, equityRequest.getPeriodicity(), startDate, endDate, equityRequest.getCurrency(),equityRequest.getRestated());
		
					financialRatioList=ratioService.getCompanyRatio((String) equityRequest.getCode(), equityRequest.getPeriodicity(), startDate, endDate, equityRequest.getCurrency());
		
					List<CompanyFinancialMINDTO> vrFinancialList= financialRatioList.stream().filter(companyFinancialDTO -> 
					companyFinancialDTO.getFinancialType().equalsIgnoreCase(equityRequest.getDataType())).collect(Collectors.toList());
		
					equityRequest.setCompanyFinancialDTOs(vrFinancialList);
					break;
		
				case CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE:
					_log.info("Getting FR for: "+ equityRequest.getExchange() +"-"+ equityRequest.getEntityType() +"-"+ equityRequest.getCode());
		
					companyFinancialList =cmFinancialDataService.getCompanyFinancial((String)equityRequest.getCode(), CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE, equityRequest.getPeriodicity(),  startDate, endDate,equityRequest.getCurrency(),equityRequest.getRestated());
		
					financialRatioList=ratioService.getCompanyRatio((String) equityRequest.getCode(),  equityRequest.getPeriodicity(), startDate, endDate, equityRequest.getCurrency());
		
					List<CompanyFinancialMINDTO> frFinancialList= financialRatioList.stream().filter(companyFinancialDTO -> 
					companyFinancialDTO.getFinancialType().equalsIgnoreCase(equityRequest.getDataType())
							).collect(Collectors.toList());
		
					equityRequest.setCompanyFinancialDTOs(frFinancialList);
					break;
		
				default :
					_log.warn("Not implemented for: "+ equityRequest.getEntityType() +" - "+ equityRequest.getExchange() +"-"+ equityRequest.getType() +"-"+ equityRequest.getDataType());
			}
		}
	}



	public Workbook createExcelReport(DownloadRequest downloadRequest) throws Exception {

		try {
			Workbook workbook = excelService.createExcelReport(downloadRequest);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}


	/**
	 * Method to return all the companies in all the exchangess
	 * @param userCountryList 
	 * @param ticsIndustryCode 
	 * @param industry 
	 * @return list of company Objects
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<CompanyDTO> getCMExchangeCompanies(String searchCriteria,Integer resultCount,Integer countryId, String companyIds, Boolean excludeDuplicateFlag, List<String> userCountryList, String entityType) {	

		List<CompanyDTO> companyDTOs = null;

		if(companyIds != null && !companyIds.equals("")){
			// split on commas and consume any spaces either side
			List<String> companyIdList = Arrays.asList(companyIds.split("\\s*,\\s*"));
			companyDTOs = getCMCompaniesByIdList(companyIdList);

		}else if(userCountryList!=null && userCountryList.size()>0){
			List<CompanyDTO> allCompany = cmRepository.getCMExchangeCompaniesByUserSubscription(searchCriteria,resultCount,userCountryList);

			if(null != allCompany && allCompany.size() > 0 && excludeDuplicateFlag) {

				_log.info("removing duplicate company names from list");

				//remove duplicate companies based on company name
				companyDTOs = allCompany.stream()
						.collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyDTO<String>::getName))),
								ArrayList::new));
			} else {
				companyDTOs = allCompany;
			}
		} else {

			List<CompanyDTO> allCompany = cmRepository.getCMExchangeCompanies(searchCriteria,resultCount,countryId, entityType);

			if(null != allCompany && allCompany.size() > 0 && excludeDuplicateFlag) {

				//remove duplicate companies based on company name
				/*companyDTOs = allCompany.stream()
	                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyDTO<String>::getName))),
	                    		ArrayList::new));*/

				//remove duplicate companies based on company name and entity type
				companyDTOs = allCompany.stream().filter(distinctByKeys(CompanyDTO::getName, CompanyDTO::getEntityType))
						.collect(Collectors.toList());
			} else {
				companyDTOs = allCompany;
			}
		}

		return companyDTOs;
	}

	private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) 
	{
		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> 
		{
			final List<?> keys = Arrays.stream(keyExtractors)
					.map(ke -> ke.apply(t))
					.collect(Collectors.toList());

			return seen.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}


	/**
	 * Method to get index list of a particular company
	 * @param companyId
	 * @return
	 */
	public List<IndexDTO> getIndexList(String companyId) {
		return cmRepository.getIndexList(companyId);
	}

	/**
	 * 
	 * @param indexId
	 * @return
	 * @throws Exception
	 */
	public IndexDTO getIndex(Integer indexId) throws Exception {
		return cmRepository.getIndex(indexId);
	}


	/**
	 * 
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public String getCompanyTicker(String companyId) throws Exception{
		return cmRepository.getCompanyTicker(companyId);
	}





	/**
	 * 
	 * @param userCountryList 
	 * @return
	 */
	public List<CMExchangeDTO> getExchangeList(List<String> userCountryList) {
		if(userCountryList!=null && userCountryList.size()>0){
			return cmRepository.getExchangeList(userCountryList);
		}else{
			return cmRepository.getExchangeList();
		}
	}


	/**
	 * 
	 * @param exchangeCode
	 * @param searchCriteria 
	 * @return
	 */
	public List<CompanyDTO> getExchangeCompanyList(String exchangeCode, String searchCriteria) {
		return cmRepository.getExchangeCompanyList(exchangeCode,searchCriteria);
	}


	/**
	 * 
	 * @param companyId
	 * @return
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<IndexDTO> getCompanyIndexList(String companyId) {
		return cmRepository.getCompanyIndexList(companyId);
	}



	/**
	 * Method to get the Company Meta Data
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.sharePrice==null")
	public CompanyMetaData getCompanyMetaData(String companyId,String currencyCode) throws Exception {

		CompanyMetaData companyMetaData=new CompanyMetaData();

		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfo = cmFinancialDataService.getCompanyLatestFilingInfo(companyId);
		CompanyLatestFilingInfoDTO yearlyLatestFilingInfo = companyLatestFilingInfo.stream()
				.filter(x -> CMStatic.PERIODICITY_YEARLY.equalsIgnoreCase(x.getPeriodType()))
				.findAny()
				.orElse(null);


		//if currency not provided then extract the financial data in reported currency
		final String filingCurrency = (currencyCode != null && !currencyCode.isEmpty()) ? currencyCode : companyLatestFilingInfo.get(0).getReportingCurrency();

		companyMetaData.setCurrency(filingCurrency);

		try {
			CompanyDTO companyDetails = getCMCompaniesById(companyId);
			companyMetaData.setCompanyId(companyDetails.getId()+"");
			companyMetaData.setEntityId(companyDetails.getFactSetEntityId());
			companyMetaData.setCompanyName(companyDetails.getName());
			companyMetaData.setProperName(companyDetails.getProperName());
			companyMetaData.setSecurityType(companyDetails.getSecurityType());
			companyMetaData.setCompanyDescription(companyDetails.getDescription());
			companyMetaData.setCompanyTicker(companyDetails.getCompanyTicker());
			companyMetaData.setCountryCode(companyDetails.getCountryCode());
			companyMetaData.setCountryName(companyDetails.getCountryName());
			companyMetaData.setCountryId(companyDetails.getCountryId());
			companyMetaData.setExchangeCode(companyDetails.getExchangeCode());
			companyMetaData.setExchangeName(companyDetails.getExchangeName());
			companyMetaData.setFf_industry(companyDetails.getFf_industry());
			companyMetaData.setTicsIndustryCode(companyDetails.getTicsIndustryCode());
			companyMetaData.setTicsIndustryName(companyDetails.getTicsIndustryName());
			companyMetaData.setTicsSectorCode(companyDetails.getTicsSectorCode());
			companyMetaData.setTicsSectorName(companyDetails.getTicsSectorName());
			_log.debug("companyDetails extracted for company id: "+ companyId +", Currency: "+ currencyCode +", filing currency: "+ filingCurrency);
		} catch (Exception e) {
			companyMetaData.setCountryEconomyFlag(false);
			_log.warn("Company Information not extracted");
		}

		Callable<List<FFEntityProfileDTO>> entityProfileCallable = () -> {
			return cmRepository.getEntityProfile(companyMetaData.getEntityId());
		};
		
		Callable<List<CountryListDTO>> countryListCallable = () -> {
			return economyService.findCountries(null, companyMetaData.getCountryName(),null,null);
		};

		Callable<FFBasicCfDTO> basicCfCallable = () -> {
			return cmStockRepository.getFFBasicCfDTO(companyId);
		};

		Callable<CompanyFinancialDTO> peCompanyFinancialCallable = () -> {
			return cmFinancialDataService.getCompanyLatestBasicDerivedAnnualFinancial(companyId, CMStatic.RATIO_PE_FINANCIAL_FIELD, filingCurrency);
		};

		Callable<CompanyFinancialDTO> dyCompanyFinancialCallable = () -> {
			return cmFinancialDataService.getCompanyLatestAdvanceAnnualFinancial(companyId, CMStatic.RATIO_DY_FINANCIAL_FIELD, filingCurrency);
		};

		Callable<CompanyFinancialDTO> bvCompanyFinancialCallable = () -> {
			return cmFinancialDataService.getCompanyLatestBasicBSFinancial(companyId, CMStatic.RATIO_BV_FINANCIAL_FIELD, filingCurrency);
		};

		Callable<CompanyFinancialDTO> cashMcapCompanyFinancialCallable = () -> {
			return cmFinancialDataService.getCompanyLatestBasicBSFinancial(companyId, CMStatic.RATIO_CASH_MCAP_FINANCIAL_FIELD, filingCurrency);
		};

		Callable<List<CompanyFinancialMINDTO>> ratioCompanyFinancialCallable = () -> {
			List<String> ratioList = new ArrayList<String>();
			if(CMStatic.INDUSTRY_IND.equalsIgnoreCase(companyMetaData.getFf_industry())) {
				ratioList.add("t_ev_ebitda");
				ratioList.add("t_fin_viability");
			} else if(CMStatic.INDUSTRY_BANK.equalsIgnoreCase(companyMetaData.getFf_industry())) {
				ratioList.add("t_price_income");
				ratioList.add("t_roa");
			} else if(CMStatic.INDUSTRY_INSURANCE.equalsIgnoreCase(companyMetaData.getFf_industry())) {
				ratioList.add("t_price_income");
				ratioList.add("t_roa");
			} else {
				ratioList.add("t_ev_ebitda");
				ratioList.add("t_roa");
			}

			Date startDate = null, endDate = null;
			if(yearlyLatestFilingInfo != null) {
				endDate = yearlyLatestFilingInfo.getPeriod();
				startDate = DateUtils.addYears(yearlyLatestFilingInfo.getPeriod(), -1);
			}
			return ratioService.getCompanyRatio(companyId, ratioList, CMStatic.PERIODICITY_YEARLY, startDate, endDate, filingCurrency);
		};

		Callable<StockPriceDTO> latestFilingCurrencyStockPriceCallable = () -> {
			return cmStockService.getCompanyLatestStockPrice(companyId, filingCurrency);
		};

		Callable<StockPriceDTO> latestStockPriceCallable = () -> {
			return cmStockService.getCompanyLatestStockPrice(companyId,currencyCode);
		};

		Callable<StockPriceDTO> highestStockPriceCallable = () -> {
			return cmStockService.getStockPriceHighest(companyId, 365, currencyCode);
		};

		Callable<StockPriceDTO> lowestStockPriceCallable = () -> {
			return cmStockService.getStockPriceLowest(companyId, 365, currencyCode);
		};

		Future<List<FFEntityProfileDTO>> entityProfileFuture=executorPool.submit(entityProfileCallable);
		Future<List<CountryListDTO>> countryListFuture=executorPool.submit(countryListCallable);
		Future<FFBasicCfDTO> basicCfFuture=executorPool.submit(basicCfCallable);
		Future<CompanyFinancialDTO> peCompanyFinancialFuture=executorPool.submit(peCompanyFinancialCallable);
		Future<CompanyFinancialDTO> dyCompanyFinancialFuture=executorPool.submit(dyCompanyFinancialCallable);
		Future<CompanyFinancialDTO> bvCompanyFinancialFuture=executorPool.submit(bvCompanyFinancialCallable);
		Future<CompanyFinancialDTO> cashMcapCompanyFinancialFuture=executorPool.submit(cashMcapCompanyFinancialCallable);
		Future<List<CompanyFinancialMINDTO>> ratioCompanyFinancialFuture=executorPool.submit(ratioCompanyFinancialCallable);

		Future<StockPriceDTO> latestFilingCurrencyStockPriceFuture=executorPool.submit(latestFilingCurrencyStockPriceCallable);
		Future<StockPriceDTO> latestStockPriceFuture=executorPool.submit(latestStockPriceCallable);
		Future<StockPriceDTO> highestStockPriceFuture=executorPool.submit(highestStockPriceCallable);
		Future<StockPriceDTO> lowestStockPriceFuture=executorPool.submit(lowestStockPriceCallable);

		try {
			List<FFEntityProfileDTO> ffEntityProfileDTOs = entityProfileFuture.get();
			if(ffEntityProfileDTOs != null && ffEntityProfileDTOs.size() > 0)
				for (Iterator<FFEntityProfileDTO> iterator = ffEntityProfileDTOs.iterator(); iterator.hasNext();) {
					FFEntityProfileDTO ffEntityProfileDTO = iterator.next();
					if(CMStatic.FACTSET_ENTITY_PROFILE_PRO.equals(ffEntityProfileDTO.getProfileType()))
						companyMetaData.setCompanyDetailedDescription(ffEntityProfileDTO.getEntityProfile());
					if(CMStatic.FACTSET_ENTITY_PROFILE_PRD.equals(ffEntityProfileDTO.getProfileType()))
						companyMetaData.setCompanyDescription(ffEntityProfileDTO.getEntityProfile());
				}
			
			if(companyMetaData.getCompanyDetailedDescription() == null)
				companyMetaData.setCompanyDetailedDescription(companyMetaData.getCompanyDescription());
			
		}catch (Exception e) {
			_log.warn("No data available for entity profile: "+ companyMetaData.getEntityId());
		}

		try {
			if(countryListFuture.get().size()>0) {
				companyMetaData.setCountryEconomyFlag(true);
			}else {
				companyMetaData.setCountryEconomyFlag(false);
			}
			_log.debug("Country economy data flag extracted");
		}catch (Exception e) {
			companyMetaData.setCountryEconomyFlag(false);
			_log.warn("country has no economy data: "+ companyId);
		}


		try {

			//StockPriceDTO latestStock = cmStockService.getCompanyLatestStockPrice(companyId,currencyCode);
			StockPriceDTO latestFilingCurrencyStock = latestFilingCurrencyStockPriceFuture.get();
			StockPriceDTO latestStock = latestStockPriceFuture.get();
			StockPriceDTO highestStock = highestStockPriceFuture.get();
			StockPriceDTO lowestStock = lowestStockPriceFuture.get();

			companyMetaData.setSharePrice(latestStock.getClose());
			companyMetaData.setSharePricePercentChange(latestStock.getPercentChange());
			companyMetaData.setSharePriceDate(latestStock.getDate());
			companyMetaData.setSharePriceCurrency(latestStock.getCurrency());
			companyMetaData.setDailyVolume(latestStock.getVolume());
			companyMetaData.setFilingCurrencySharePrice(latestFilingCurrencyStock.getClose());

			companyMetaData.setHighStockPrice(highestStock.getHigh());
			companyMetaData.setLowStockPrice(lowestStock.getLow());

			//FFBasicCfDTO basicCfDTO = cmStockRepository.getFFBasicCfDTO(companyId);
			FFBasicCfDTO basicCfDTO = basicCfFuture.get();
			companyMetaData.setShareOutStanding(basicCfDTO.getShareOutStanding());
			companyMetaData.setShareFreeFloat(basicCfDTO.getShareFreeFloat());

			companyMetaData.setMarketCap(basicCfDTO.getShareOutStanding() * latestStock.getClose());
			companyMetaData.setMarketCapCurrency(latestStock.getCurrency());

			companyMetaData.setMarketCapFiling(basicCfDTO.getShareOutStanding() * latestFilingCurrencyStock.getClose());
			companyMetaData.setMarketCapFilingCurrency(latestFilingCurrencyStock.getCurrency());

			_log.debug("Freefloat, Market Cap, stock price extracted");
		} catch (Exception e) {
			_log.error("Freefloat, Market Cap not found for: "+ companyId, e);
		}

		try {
			//CompanyFinancialDTO peFinancialDTO = cmFinancialDataService.getCompanyLatestBasicDerivedAnnualFinancial(companyId, CMStatic.RATIO_PE_FINANCIAL_FIELD, currencyCode);
			CompanyFinancialDTO peFinancialDTO = peCompanyFinancialFuture.get();

			if(peFinancialDTO.getData() != null && peFinancialDTO.getData() != 0 && companyMetaData.getMarketCapFiling() != null ) {
				if(CMStatic.UNIT_MILLION.equalsIgnoreCase(peFinancialDTO.getUnit())) {
					peFinancialDTO.setData(peFinancialDTO.getData() * CMStatic.UNIT_FACTOR_MILLION);
					peFinancialDTO.setUnit(null);
				}
				companyMetaData.setPriceEarnings(companyMetaData.getMarketCapFiling() / peFinancialDTO.getData());
			}
			_log.debug("Price Earnings extracted");
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("Price Earnings not calculated for: "+ companyId);
		}

		try {
			//CompanyFinancialDTO dyFinancialDTO = cmFinancialDataService.getCompanyLatestAdvanceAnnualFinancial(companyId, CMStatic.RATIO_DY_FINANCIAL_FIELD, currencyCode);
			CompanyFinancialDTO dyFinancialDTO = dyCompanyFinancialFuture.get();

			if(dyFinancialDTO.getData() != null && dyFinancialDTO.getData() != 0 && companyMetaData.getMarketCapFiling() != null ) {
				if(CMStatic.UNIT_MILLION.equalsIgnoreCase(dyFinancialDTO.getUnit())) {
					dyFinancialDTO.setData(dyFinancialDTO.getData() * CMStatic.UNIT_FACTOR_MILLION);
					dyFinancialDTO.setUnit(null);
				}
				companyMetaData.setDividendYield(dyFinancialDTO.getData() / companyMetaData.getMarketCapFiling() * 100);
				companyMetaData.setDividendYieldUnit("%");
			}
			_log.debug("Dividend Yield extracted");
		}  catch (Exception e) {
			e.printStackTrace();
			_log.error("dividend Yield not calculated for: "+ companyId);
		}

		try {
			//CompanyFinancialDTO bvFinancialDTO = cmFinancialDataService.getCompanyLatestBasicBSFinancial(companyId, CMStatic.RATIO_BV_FINANCIAL_FIELD, currencyCode);
			CompanyFinancialDTO bvFinancialDTO = bvCompanyFinancialFuture.get();

			if(bvFinancialDTO.getData() != null && bvFinancialDTO.getData() != 0 && companyMetaData.getMarketCapFiling() != null ) {

				if(CMStatic.UNIT_MILLION.equalsIgnoreCase(bvFinancialDTO.getUnit())) {

					bvFinancialDTO.setData(bvFinancialDTO.getData() * CMStatic.UNIT_FACTOR_MILLION);
					bvFinancialDTO.setUnit(null);

				}

				companyMetaData.setBookValue(bvFinancialDTO.getData() / companyMetaData.getShareOutStanding());
				companyMetaData.setBookValueCurrency(bvFinancialDTO.getCurrency());

				companyMetaData.setPriceBookValue(companyMetaData.getFilingCurrencySharePrice() / companyMetaData.getBookValue());
				companyMetaData.setBookValueUnit("x");
			}
			_log.debug("Book Value extracted");
		}  catch (Exception e) {
			e.printStackTrace();
			_log.error("book value not calculated for: "+ companyId);
		}

		try {
			CompanyFinancialDTO cashMcapFinancialDTO = cashMcapCompanyFinancialFuture.get();

			if(cashMcapFinancialDTO!=null && cashMcapFinancialDTO.getData() != null && cashMcapFinancialDTO.getData() != 0 && companyMetaData.getMarketCapFiling() != null ) {

				if(CMStatic.UNIT_MILLION.equalsIgnoreCase(cashMcapFinancialDTO.getUnit())) {
					cashMcapFinancialDTO.setData(cashMcapFinancialDTO.getData() * CMStatic.UNIT_FACTOR_MILLION);
					cashMcapFinancialDTO.setUnit(null);
				}

				companyMetaData.setCashMcap(cashMcapFinancialDTO.getData() / companyMetaData.getMarketCapFiling() * 100);
				companyMetaData.setCashMcapUnit("%");
			}
			_log.debug("CashMcap Value extracted");
		}  catch (Exception e) {
			e.printStackTrace();
			_log.error("CashMcap not calculated for: "+ companyId);
		}

		try {
			List<CompanyFinancialMINDTO> ratioData = ratioCompanyFinancialFuture.get();
			Date latestPeriod = null;

			for (CompanyFinancialMINDTO companyFinancialMINDTO : ratioData) {
				if(latestPeriod == null || companyFinancialMINDTO.getPeriod().compareTo(latestPeriod) >= 0) {

					if(companyFinancialMINDTO.getFieldName().equals("t_ev_ebitda")) {
						companyMetaData.setEvEbitda(companyFinancialMINDTO.getData());
						companyMetaData.setEvEbitdaCurrency(companyFinancialMINDTO.getCurrency());
						companyMetaData.setEvEbitdaUnit(companyFinancialMINDTO.getUnit());
					}

					if(companyFinancialMINDTO.getFieldName().equals("t_fin_viability")) {
						companyMetaData.setFinViability(companyFinancialMINDTO.getData());
						companyMetaData.setFinViabilityCurrency(companyFinancialMINDTO.getCurrency());
						companyMetaData.setFinViabilityUnit(companyFinancialMINDTO.getUnit());
					}

					if(companyFinancialMINDTO.getFieldName().equals("t_price_income")) {
						companyMetaData.setPriceIncome(companyFinancialMINDTO.getData());
						companyMetaData.setPriceIncomeCurrency(companyFinancialMINDTO.getCurrency());
						companyMetaData.setPriceIncomeUnit(companyFinancialMINDTO.getUnit());
					}

					if(companyFinancialMINDTO.getFieldName().equals("t_roa")) {
						companyMetaData.setRoa(companyFinancialMINDTO.getData());
						companyMetaData.setRoaCurrency(companyFinancialMINDTO.getCurrency());
						companyMetaData.setRoaUnit(companyFinancialMINDTO.getUnit());
					}
					latestPeriod = companyFinancialMINDTO.getPeriod();
				}

			}

			_log.debug("ev_ebitda and fin_viability Value extracted");
		}  catch (Exception e) {
			e.printStackTrace();
			_log.error("ev_ebitda and fin_viability not calculated for: "+ companyId);
		}

		return companyMetaData;
	}


	/**
	 * 
	 * @param companyId
	 * @return
	 */
	public CompanyDTO getCMCompaniesById(String companyId) {
		return cmRepository.getCMCompaniesById(companyId);
	}
	
	public PCCompanyDTO getPCCompaniesById(String entityId) {
		return cmRepository.getPCCompaniesById(entityId);
	}
	
	

	/**
	 * 
	 * @param companyIdList
	 * @return
	 */
	public List<CompanyDTO> getCMCompaniesByIdList(List<String> companyIdList) {
		return cmRepository.getCMCompaniesByIdList(companyIdList);		
	}

	public CompanyDTO getDefaultCompanyForCountry(String primaryCountryCode, List<String> subscribedCountyList) {

		//convert country code to ISO 3 if not already
		if(primaryCountryCode != null && primaryCountryCode.length() == 2) {
			try {
				CountryListDTO country = cmRepository.getCountry(primaryCountryCode);
				primaryCountryCode = country.getCountryIsoCode3();
			} catch (Exception e) {
				//Country code not available in DB so assigning null
				primaryCountryCode = null;
			}
		}
		if(subscribedCountyList != null && subscribedCountyList.size() > 0 && subscribedCountyList.get(0).length() == 2) {
			try {
				List<CountryListDTO> countryList = cmRepository.getCountryList(subscribedCountyList);
				subscribedCountyList = countryList.stream().map(cl -> cl.getCountryIsoCode3()).collect(Collectors.toList());
			} catch (Exception e) {
				//Country code not available in DB so assigning null
				subscribedCountyList = null;
			}
		}

		if(subscribedCountyList != null && subscribedCountyList.size() > 0 )
			return cmRepository.getDefaultCompanyForCountry(primaryCountryCode, subscribedCountyList);
		else if(primaryCountryCode != null)
			return cmRepository.getDefaultCompanyForCountry(primaryCountryCode);
		else
			return cmRepository.getDefaultCompanyForCountry();

	}



	public List<CompanyDTO> getCMCompaniesByEntityId(String entityId) {
		return cmRepository.getCMCompaniesByEntityId(entityId);
	}



	public List<CompanyDTO> getCMExchangeCompaniesById(String companyId) {
		String entityId=cmRepository.getCMCompaniesById(companyId).getFactSetEntityId();
		return cmRepository.getCMCompaniesByEntityId(entityId);
	}
	
	public List<CMCDTO> getCMExchangeCompaniesTriggerUsedById(String companyId) {
		String entityId=cmRepository.getCMCompaniesById(companyId).getFactSetEntityId();
		return cmRepository.getCMCompaniesUsedTriggerByEntityId(entityId);
	}




	/**
	 * get the search result of different modules 
	 * @param keyword
	 * @param userCountryList 
	 * @return GlobalSearchResponseModel
	 */
	public GlobalSearchResponseModel getCMGlobalSearchByKeyword(String keyword,Integer resultCount, List<String> userCountryList) {

		GlobalSearchResponseModel globalSearchResponseModel =new GlobalSearchResponseModel();

		globalSearchResponseModel.setSearchKeyWord(keyword);

		Future<List<GlobalSearchCompany>> companyResult = executorPool.submit(() -> {
			//return cmRepository.getCMExchangeCompanies(keyword,resultCount,null);
			if(userCountryList!=null && userCountryList.size()>0){
				return globalSearchRepository.getCMExchangeCompaniesWithSubscribedCountries(keyword,resultCount,null,userCountryList);
			}else{
				return globalSearchRepository.getCMExchangeCompanies(keyword,resultCount,null);
			}
		});

		Future<List<CountryListDTO>> countryResult = executorPool.submit(() -> {
			//return economyService.findCountries(null,keyword,resultCount);
			if(userCountryList!=null && userCountryList.size()>0){
				return findCountriesBySubscription(null,keyword,resultCount,userCountryList);
			}else{
				return findCountries(null,keyword,resultCount);
			}
		});

		Future<List<GlobalSearchIndustryDTO>> industryResult = executorPool.submit(() -> {
			if(userCountryList!=null && userCountryList.size()>0){
				return globalSearchRepository.getTicsIndustryBySearchParamAndSubscription(keyword,resultCount,userCountryList);
			}else{
				return globalSearchRepository.getTicsIndustryBySearchParam(keyword,resultCount);
			}
		});

		Future<List<IndicatorLatestDataDTO>> indicatorResult = executorPool.submit(() -> {
			//return economyService.findAllIndicators(keyword,resultCount);
			if(userCountryList!=null && userCountryList.size()>0){
				return globalSearchRepository.findAllIndicatorsBySubscription(keyword, resultCount,userCountryList);
			}else{
				return globalSearchRepository.findAllIndicators(keyword, resultCount);
			}
		});

		Future<List<CommodityLatestDataDTO>> commodityResult = executorPool.submit(() -> {
			//return economyService.findAllIndicators(keyword,resultCount);
			return globalSearchRepository.getCommodities(keyword, resultCount);
		});

		try {
			globalSearchResponseModel.setCompany(companyResult.get());
			globalSearchResponseModel.setCountry(countryResult.get());
			globalSearchResponseModel.setIndicator(indicatorResult.get());
			globalSearchResponseModel.setIndustry(industryResult.get());
			globalSearchResponseModel.setCommodity(commodityResult.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return globalSearchResponseModel;
	}

	private List<CountryListDTO> findCountriesBySubscription(String indicatorName,String searchCriteria,Integer resultCount, List<String> userCountryList) {
		return globalSearchRepository.findCountriesBySearchCriteriaAndSubscription(searchCriteria,resultCount,userCountryList);
	}

	private List<CountryListDTO> findCountries(String indicatorName,String searchCriteria,Integer resultCount){

		if(searchCriteria!=null && !searchCriteria.isEmpty()){
			return globalSearchRepository.findCountriesBySearchCriteria(searchCriteria,resultCount);
		}else{
			return globalSearchRepository.findAllEconomyCountries();
		}

	}

	public List<? extends Object> getAdvancedCompanySearch(
			String entitySelection, String industrySelection,
			String countrySelection, String currencySelection, String searchKeyWord) {

		if (entitySelection==null || entitySelection.equals("null") || entitySelection.equals("") || entitySelection.equalsIgnoreCase("All")) {
			//Entity Type not Selected
			if (industrySelection==null || industrySelection.equals("") || industrySelection.equalsIgnoreCase("All")) {
				//Industry not Selected
				return cmRepository.getAdvancedCompanySearchAllEntityAllIndustry(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
			}else{
				return cmRepository.getAdvancedCompanySearchAllEntity(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
			}
		}else if (entitySelection.equalsIgnoreCase("public")) {
			if (industrySelection==null || industrySelection.equals("") || industrySelection.equalsIgnoreCase("All")) {
				//Industry not Selected
				return cmRepository.getAdvancedCompanySearchPublicEntityAllIndustry(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
			}else{
				return cmRepository.getAdvancedCompanySearchPublicEntity(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
			}
		}else if (entitySelection.equalsIgnoreCase("private")) {
			    //  if (industrySelection==null || industrySelection.equals("") || industrySelection.equalsIgnoreCase("All")) {
				//Industry not Selected
				//return cmRepository.getAdvancedCompanySearchPrivateEntityAllIndustry(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
			   //}else{
				return cmRepository.getAdvancedCompanySearchPrivateEntity(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
			  //}
		 }
		
		return null;
	

	}

	public List<EntityStructureDTO> getEntityStructure(String entityId) {
		return cmRepository.getEntityStructure(entityId);
	}


	public HSSFWorkbook createEntityStructureExcelReport(List<EntityStructureDTO> entityStructureDtoList) throws JsonGenerationException, JsonMappingException, IOException {

		_log.info("Excel Creation Zone for the entity");
		HSSFWorkbook workbook = new HSSFWorkbook();
		GenerateExcelStyle ges =new GenerateExcelStyle(workbook);

		//////////////// Change this from the company name
		String sheetName = "Entity Structure";

		Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		sheet.setDisplayGridlines(false);
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
			r=sheet.createRow(row);
		}
		Cell cell = r.createCell(1);
		if(cell==null) {
			cell=r.createCell(1);
		}
		excelService.createExcelLogo(cell, sheet, workbook, ges);		
		row++;
		r = sheet.getRow(row);
		
		if(r==null) {
			r=sheet.createRow(row);
		}

		EntityNode parentEntityNode = new EntityNode();
		Map<String, EntityNode> entityMap = new LinkedHashMap<>();
		AtomicBoolean parentCheck = new AtomicBoolean();
		parentCheck.set(false);

		_log.info(new Gson().toJson(entityStructureDtoList));

		Collections.sort(entityStructureDtoList, (s1,s2)->{
			return s1.getEntity().compareTo(s2.getEntity());
		});
		_log.info(new Gson().toJson(entityStructureDtoList));


		entityStructureDtoList.stream().forEach((entityStructureDTO)->{

			EntityNode entityNode = new EntityNode();
			entityNode.setCountry(entityStructureDTO.getEntityCountryName());
			entityNode.setEntityType(entityStructureDTO.getEntityTypeDesc());
			entityNode.setName(entityStructureDTO.getEntity());
			entityNode.setParentId(entityStructureDTO.getParentEntityId());
			entityNode.setEntityId(entityStructureDTO.getEntityId());
			entityMap.put(entityStructureDTO.getEntityId(), entityNode);
			if(!parentCheck.equals(false)){
				parentEntityNode.setName(entityStructureDTO.getUltimateParentEntity());
				parentEntityNode.setEntityType(entityStructureDTO.getUltimateParentEntityTypeDesc());
				parentEntityNode.setEntityId(entityStructureDTO.getUltimateParentEntityId());
				parentEntityNode.setCountry(entityStructureDTO.getUltimateParentCountryName());
				parentEntityNode.setLevel("0");
				parentEntityNode.setiLevel(0);
				parentCheck.set(true);
			}
		});


		for (String entityId : entityMap.keySet()) {
			// This loop maps the child to the parent
			try{
				EntityNode entityNode = entityMap.get(entityId);
				String parentId = entityNode.getParentId();

				if(parentId!=null){
					EntityNode tempParent = entityMap.get(parentId);
					if(tempParent!=null){
						if(parentId.equals(parentEntityNode.getEntityId())){
							Map<String, EntityNode> hm = parentEntityNode.getChildNodes();
							if(hm==null){
								hm = new LinkedHashMap<>();
							}
							int size = hm.size();
							String tempLevel = "";
							if(parentEntityNode.getLevel()==null){
								tempLevel = "0"+"."+size;
							}else{
								tempLevel = parentEntityNode.getLevel()+"."+size;
							}
							entityNode.setNoOfChild(++size);
							entityNode.setLevel(tempLevel);
							entityNode.setiLevel(++size);
							hm.put(entityId, entityNode);
							parentEntityNode.setChildNodes(hm);
						}else{
							if(tempParent.getHasChild()==null || tempParent.getHasChild()==false){
								tempParent.setHasChild(true);
							}
							if(tempParent.getNoOfChild()==null){
								tempParent.setNoOfChild(1);
							}else{
								Integer noOfChild = tempParent.getNoOfChild();
								++noOfChild;
								tempParent.setNoOfChild(noOfChild);
							}
							Map<String,EntityNode> childNodes = tempParent.getChildNodes();
							if(childNodes==null){
								childNodes = new LinkedHashMap<>();
							}
							int size = childNodes.size();
							String tempLevel = "";
							if(tempParent.getLevel()==null){
								tempLevel = "0"+"."+size;
							}else{
								tempLevel = tempParent.getLevel()+"."+size;
							}
							entityNode.setNoOfChild(++size);
							entityNode.setLevel(tempLevel);
							entityNode.setiLevel(++size);

							childNodes.put(entityId,entityNode);
							tempParent.setChildNodes(childNodes);
						}
					}else{
						Map<String, EntityNode> hm = parentEntityNode.getChildNodes();
						if(hm==null){
							hm = new LinkedHashMap<>();
						}
						int size = hm.size();
						String tempLevel = "";
						if(parentEntityNode.getLevel()==null){
							tempLevel = "0"+"."+size;
						}else{
							tempLevel = parentEntityNode.getLevel()+"."+size;
						}
						entityNode.setNoOfChild(++size);
						entityNode.setLevel(tempLevel);
						entityNode.setiLevel(++size);
						hm.put(entityId, entityNode);
						parentEntityNode.setChildNodes(hm);
					}
				}else{
					Map<String, EntityNode> hm = parentEntityNode.getChildNodes();
					if(hm==null){
						hm = new LinkedHashMap<>();
					}
					int size = hm.size();
					String tempLevel = "";
					if(parentEntityNode.getLevel()==null){
						tempLevel = "0"+"."+size;
					}else{
						tempLevel = parentEntityNode.getLevel()+"."+size;
					}
					entityNode.setNoOfChild(++size);
					entityNode.setLevel(tempLevel);
					entityNode.setiLevel(++size);
					hm.put(entityId, entityNode);
					parentEntityNode.setChildNodes(hm);
				}
			}catch(Exception e){
				_log.error(e.getMessage());
			}
		}

		_log.info(new Gson().toJson(entityMap));
		tRow = row;
		createPrivateCompanyTable(tRow, sheet, ges, entityMap , parentEntityNode);
		return workbook;
	}





	private void createPrivateCompanyTable(int row, Sheet sheet, GenerateExcelStyle ges, Map<String, EntityNode> dataSet, EntityNode parentEntityNode) {

		Row r;
		Cell cell;
		int cellIndex=1;
		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;
		
		

		int minRow = 0;

		if(row > maxRow-1) {
			minRow = maxRow;
			maxRow = maxRow+addRow;
			//ges.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
		}
		row++;
		row++;
		if(parentEntityNode==null){
			r = sheet.getRow( row );
			if(r==null) {
				r=sheet.createRow(row);
			}
			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue(CMStatic.NO_DATA_FOUND);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			cellIndex++;

		}else{
			r = sheet.getRow(row);
			if(r==null) {
				r=sheet.createRow(row);
			}
			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			//sheet.setColumnWidth(cellIndex, (short) (17 * 500));

			/*try {
				ge.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			cell.setCellValue("Entity Level");
			cell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER);
			cellIndex++;

			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue("Company Name");
			cell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER);
			sheet.setColumnWidth(cellIndex, 70 * 256);
			cellIndex++;

			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue("Country");
			cell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER);
			cellIndex++;

			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue("Entity Type");
			cell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER);

			row++;
			cellIndex = 1;
			String space = "";

			recPrint(cellIndex,  row, sheet, ges, dataSet, parentEntityNode, space,"0");
		}
	}



	public int recPrint(int cellIndex, int row, Sheet sheet, GenerateExcelStyle ges, Map<String, EntityNode> dataSet, EntityNode parentEntityNode, String space, String index){

		Row r;
		Cell cell;
		
		boolean isLastRecord=false;
		
		if(row==dataSet.keySet().size()+4) {
			isLastRecord=true;
		}

		if(parentEntityNode!=null){

			r = sheet.getRow(row);
			if(r==null) {
				r=sheet.createRow(row);
			}
			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue(" " + index);
			if(!isLastRecord) {
			cell.setCellStyle(ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}else {
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}
			cellIndex++;


			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			//cell.setCellValue(parentEntityNode.getName()==null?"": space + parentEntityNode.getName());
			//cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			setColumnValue(cell,parentEntityNode.getName()==null?"": space + parentEntityNode.getName(),sheet,cellIndex,ges,isLastRecord,"LEFT");
			cellIndex++;


			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			//cell.setCellValue("  "+ parentEntityNode.getCountry()==null?"":parentEntityNode.getCountry());
			setColumnValue(cell,"  "+ parentEntityNode.getCountry()==null?"":parentEntityNode.getCountry(),sheet,cellIndex,ges,isLastRecord,"LEFT");
			cellIndex++;


			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			/*cell.setCellValue("  "+ parentEntityNode.getEntityType()==null?"":parentEntityNode.getEntityType());
			cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);*/
			setColumnValue(cell,"  "+ parentEntityNode.getEntityType()==null?"":parentEntityNode.getEntityType(),sheet,cellIndex,ges,isLastRecord,"LEFT");
			cellIndex++;

		}

		Map<String, EntityNode> childNodesMap = parentEntityNode.getChildNodes();
		if(childNodesMap!=null){
			space = space + "     ";
			//space = space + "\t";
			int i=1;
			for (String childDetails : childNodesMap.keySet()){
				String tempIndex = "";
				if(index.equals("0")){
					tempIndex = ""+i;
				}else{
					tempIndex = index+"."+i;
				}
				row = recPrint(1, ++row, sheet, ges, dataSet, dataSet.get(childDetails),space,tempIndex);
				i++;
			}
		}

		return row;
	}


	public boolean append(HashMap<String, EntityNode> entityMap, EntityNode parentEntityNode){
		System.out.println("In append ");
		boolean check = false;
		HashMap<String, EntityNode> noMapping = new HashMap<>();
		for (String entityId : entityMap.keySet()) {
			boolean tempcheck = findParentAndAppend(parentEntityNode, entityMap.get(entityId));
			if(!tempcheck){
				noMapping.put(entityId,entityMap.get(entityId));
				check = true;
			}
		}

		while (noMapping.size()>0) {
			append(noMapping,parentEntityNode);
		}
		return check;
	}


	public boolean findParentAndAppend(EntityNode parentNode,EntityNode tempNode ){
		boolean check = false;
		System.out.println("In append to parent");
		System.out.println(parentNode.getEntityId());
		System.out.println(tempNode.getParentId());
		if(tempNode.getParentId()!=null){
			if(parentNode.getEntityId().equals(tempNode.getParentId())){
				if(parentNode.getChildNodes()!=null){
					parentNode.getChildNodes().put(tempNode.getEntityId(), tempNode);
				}else{
					HashMap<String, EntityNode> childNodes = new HashMap<>();
					childNodes.put(tempNode.getEntityId(), tempNode);
					parentNode.setChildNodes(childNodes);
				}
				System.out.println("Parent Found and child appended" + parentNode);
				check = true;
			}else{
				if(parentNode.getChildNodes()!=null){
					for (String childId : parentNode.getChildNodes().keySet()) {
						if( parentNode.getChildNodes().get(childId)==null){
							boolean tempCheck = findParentAndAppend( parentNode.getChildNodes().get(childId), tempNode);
							if(tempCheck){
								break;
							}
						}else{
							System.out.println("*********** Child already exists ********** ");
							check = true;
							break;
						}
					}
				}
			}
		}else{
			System.out.println("*********** IT is ultimate parent ********** ");
			check = true;
		}
		return check;
	}


	public EntityNode recObj(Map<String, EntityNode> dataSet, EntityNode parentEntityNode){
		try{
			Map<String, EntityNode> tempSet = new LinkedHashMap<>();
			tempSet.putAll(dataSet);
			for (String key : dataSet.keySet()) {
				EntityNode childEntity = dataSet.get(key);
				String parent = childEntity.getParentId();
				_log.info("In here");
				if(parent!=null){
					EntityNode parentEntity =  dataSet.get(parent);
					if(parentEntity!=null){
						if(childEntity.getParentId().equals(parentEntity.getEntityId())){
							tempSet.remove(childEntity.getEntityId());
							Map<String, EntityNode> childMap = parentEntity.getChildNodes();
							if(childMap==null){
								childMap = new LinkedHashMap<>();
							}
							childMap.put(key, childEntity);
							parentEntityNode.setChildNodes(childMap);
						}
					}
				}
			}

			_log.info(parentEntityNode);

			Map<String, EntityNode> childMap = parentEntityNode.getChildNodes();
			if(childMap!=null){
				for (String key : childMap.keySet()) {
					recObj(tempSet, childMap.get(key));
				}
			}
		}catch(Exception e){
			_log.error(e.getMessage(),e);

		}
		return parentEntityNode;
	}

	
	private void setColumnValue(Cell cell, String data, Sheet sheet, int columnNumber, GenerateExcelStyle ges, Boolean isLastRecord,String Align) {
		if (data != null) {
			
			cell.setCellValue(data);
			//cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
			if(Align.equalsIgnoreCase("LEFT")){
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
				}
			}else{
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if(isLastRecord){
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
			
		}
		
	}


}
