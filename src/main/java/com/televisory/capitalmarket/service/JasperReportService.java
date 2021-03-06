package com.televisory.capitalmarket.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.pcompany.model.RegionalGrowthMonitorsData;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.CMStockRepository;
import com.televisory.capitalmarket.dao.EconomyRepository;
import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.BenchMarkCompanyDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.IndicatorDataDTO_old;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.TelevisoryIndicatorReportingFrequencyDTO;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.model.BetaData;
import com.televisory.capitalmarket.model.CompanyMetaData;
import com.televisory.capitalmarket.model.MySlot;
import com.televisory.capitalmarket.model.ReportCompanyProfile;
import com.televisory.capitalmarket.model.ReportHistPerformance;
import com.televisory.capitalmarket.model.ReportIndustryMetaData;
import com.televisory.capitalmarket.model.ReportIndustryMonitor;
import com.televisory.capitalmarket.model.ReportIndustryToppersData;
import com.televisory.capitalmarket.model.ReportMonitorData;
import com.televisory.capitalmarket.model.ReportPeerFinancialData;
import com.televisory.capitalmarket.model.ReportPerformanceData;
import com.televisory.capitalmarket.model.ReportQoqYoyData;
import com.televisory.capitalmarket.model.ReportRatiosData;
import com.televisory.capitalmarket.model.ReportShareholding;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;
import com.televisory.user.dao.TelevisoryUserRepository;
import com.televisory.util.StaticParams;

@Service
public class JasperReportService {

	private static final Logger _log = Logger.getLogger(JasperReportService.class);

	@Autowired
	CMStockService cmStockService;

	@Autowired
	CMFinancialDataService cmFinancialDataService;

	@Autowired
	RatioService ratioService;

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	SectorService industryService;

	@Autowired
	EconomyService economyService ;

	@Autowired
	CMRepository cmRepository;

	@Autowired
	CMStockRepository cmStockRepository;

	@Autowired
	InteractiveComparisonService icService;

	@Autowired
	UserService userService;

	@Autowired
	TelevisoryUserRepository televisoryUserRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	IMService imService;

	@Autowired
	EconomyRepository economyRepository; 

	@Autowired
	SectorRepository sectorRepository;

	@Value("${CM.RESOURCE.IMAGE.PATH}")
	private String imagePath;

	@SuppressWarnings("rawtypes")
	public ReportCompanyProfile getCompanyProfileData(String companyId,String currency) throws Exception {
		ReportCompanyProfile companyProfile = new ReportCompanyProfile();
		List<CompanyMetaData> companyList = new ArrayList<CompanyMetaData>();
		List<ReportHistPerformance> histPerformanceList = new ArrayList<ReportHistPerformance>();
		List<ReportHistPerformance> peerHistPerformanceList = new ArrayList<ReportHistPerformance>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfY = new SimpleDateFormat("MMM-yyyy");
		//String peerPeriod=null, peerPeriodType = null;

		//String industryType = capitalMarketService.getCMCompaniesById(companyId).getFf_industry().trim()/*.replaceAll(" ", "") */;

		//SET COMPANY META DATA
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		Callable<CompanyMetaData> companyMetaDataCallable = () -> {
			CompanyMetaData companyDetails = capitalMarketService.getCompanyMetaData(companyId,currency);
			return companyDetails;
		};


		// CALCULATE SHARE HOLDING FREE FLOAT RATIOS
		Callable<Void> shareHoldingCallable = () -> {
			try {
				List<ReportShareholding> shareHoldings = getShareholdingsData(companyId);
				companyProfile.setShareholdingRatios(shareHoldings);
			}catch(Exception e) {
				_log.error("Share Holding Data not found for Company: "+companyId);
			}
			return null;
		};	


		//SET COMPANY STOCK DATA
		Callable<Void> stockPriceCallable = () -> {
			Date currDate = new Date();
			Date endDate = sdf.parse(sdf.format(currDate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(currDate);		
			cal.add(Calendar.YEAR, -1);
			Date startDate = sdf.parse(sdf.format(cal.getTime()));

			List<StockPriceDTO> stockPriceData = cmStockService.getFactSetCompanyStockPrice(companyId,CMStatic.DAILY,startDate,endDate,currency);
			companyProfile.setStockData(stockPriceData);
			return null;
		};	


		//SET COMPANY BETA DATA
		Callable<Void> betaCallable = () -> {
			List<ReportHistPerformance> riskRatios = getRiskRatioData(companyId,currency);
			companyProfile.setRiskRatios(riskRatios); 
			return null;
		};

		Future<CompanyMetaData> companyMetaDataFuture = executorService.submit(companyMetaDataCallable);
		CompanyMetaData companyDetails = companyMetaDataFuture.get();
		String industryType = companyDetails.getFf_industry().trim();
		String companyName = companyDetails.getCompanyName();
		companyList.add(companyDetails);
		companyProfile.setCompanyDetails(companyList);

		//COMPANY HISTORICAL PERFORMANCE
		Callable<Void> companyHistoryPerformanceCallable = () -> {

			String indexId = null;
			List<IndexDTO> indexList = null;
			try {
				indexList = capitalMarketService.getCompanyIndexList(companyId);
			} catch (Exception e) {
				_log.error("Index details not found for the company :: " + companyId);
			}
			if(indexList!=null && !indexList.isEmpty()){
				indexId = ""+indexList.get(0).getId();
			}
			List<ReportHistPerformance> rhp = getCompanyHistoricalPerformance(companyId,companyName, indexId,currency);
			histPerformanceList.addAll(rhp);
			companyProfile.setHistPerformance(histPerformanceList);
			return null;
		};



		//BENCHMARK COMPANIES
		Integer resultLimit = 4;
		Callable<List<BenchMarkCompanyDTO>> benchmarkCompanyCallable = () -> {
			List<BenchMarkCompanyDTO> benchMarkCompanies= new ArrayList<>();
			try{
				benchMarkCompanies = cmFinancialDataService.editbenchMarkCompaniesByCompanyId(companyId, resultLimit, currency, null, null, "yearly");
			}catch(Exception e){
				_log.error(e.getMessage());			
			}
			//List<BenchMarkCompanyDTO> benchMarkCompanies=cmFinancialDataService.getbenchMarkCompaniesByCompanyId(companyId,4,currency); 
			//PEER HISTORICAL PERFORMANCE
			List<String> companyIdList = new ArrayList<>();
			for(BenchMarkCompanyDTO benchmarkCompany : benchMarkCompanies) {
				if(benchmarkCompany.getCompanyName()!=null){
					if(!benchmarkCompany.getCompanyName().startsWith(CMStatic.PEER_IGNORE)){
						if(!companyIdList.contains(benchmarkCompany.getCompanyId())){
							List<ReportHistPerformance> histPerf = getCompanyHistoricalPerformance(benchmarkCompany.getCompanyId(), benchmarkCompany.getCompanyName(), null,currency);
							peerHistPerformanceList.addAll(histPerf);
							companyIdList.add(benchmarkCompany.getCompanyId());
						}
					}
				}
			}
			companyProfile.setPeerHistPerformance(peerHistPerformanceList); 
			return benchMarkCompanies;
		};

		Future<Void> shareHoldingFuture = executorService.submit(shareHoldingCallable);
		Future<Void> stockPriceFuture = executorService.submit(stockPriceCallable);
		Future<Void> betaFuture = executorService.submit(betaCallable);
		Future<Void> companyHistoryPerformanceFuture = executorService.submit(companyHistoryPerformanceCallable);
		Future<List<BenchMarkCompanyDTO>> benchmarkCompanyFuture = executorService.submit(benchmarkCompanyCallable);


		//GET FINANCIAL DATA
		SortedSet<String> ratiosList = new TreeSet<String>();

		switch(industryType) {
		case CMStatic.INDUSTRY_IND :
			ratiosList.addAll(CMStatic.REPORT_INDUSTRY_RATIOS_PROFITABILITY);
			ratiosList.addAll(CMStatic.REPORT_INDUSTRY_RATIOS_LIQUIDITY);
			ratiosList.addAll(CMStatic.REPORT_INDUSTRY_RATIOS_LEVERAGE);
			ratiosList.addAll(CMStatic.REPORT_INDUSTRY_RATIOS_RETURN);
			ratiosList.addAll(CMStatic.REPORT_INDUSTRY_RATIOS_VALUATIONS);
			ratiosList.addAll(CMStatic.REPORT_INDUSTRY_FINANCIALS);
			break;
		case CMStatic.INDUSTRY_BANK :
			ratiosList.addAll(CMStatic.REPORT_BANK_RATIOS_PROFITABILITY);
			ratiosList.addAll(CMStatic.REPORT_BANK_RATIOS_LIQUIDITY);
			ratiosList.addAll(CMStatic.REPORT_BANK_RATIOS_RISK_MANAGEMENT);
			ratiosList.addAll(CMStatic.REPORT_BANK_RATIOS_RETURN);
			ratiosList.addAll(CMStatic.REPORT_BANK_RATIOS_VALUATIONS);
			ratiosList.addAll(CMStatic.REPORT_BANK_FINANCIALS);
			break;
		case CMStatic.INDUSTRY_INSURANCE :
			ratiosList.addAll(CMStatic.REPORT_INSURANCE_RATIOS_PROFITABILITY);
			ratiosList.addAll(CMStatic.REPORT_INSURANCE_RATIOS_LIQUIDITY);
			ratiosList.addAll(CMStatic.REPORT_INSURANCE_RATIOS_SOLVENCY);
			ratiosList.addAll(CMStatic.REPORT_INSURANCE_RATIOS_RETURN);
			ratiosList.addAll(CMStatic.REPORT_INSURANCE_RATIOS_VALUATIONS);
			ratiosList.addAll(CMStatic.REPORT_INSURANCE_FINANCIALS);
			break;
		case CMStatic.INDUSTRY_OTHER :
			ratiosList.addAll(CMStatic.REPORT_OTHERS_RATIOS_PROFITABILITY);
			ratiosList.addAll(CMStatic.REPORT_OTHERS_RATIOS_LIQUIDITY);
			ratiosList.addAll(CMStatic.REPORT_OTHERS_RATIOS_SOLVENCY);
			ratiosList.addAll(CMStatic.REPORT_OTHERS_RATIOS_RETURN);
			ratiosList.addAll(CMStatic.REPORT_OTHERS_RATIOS_VALUATIONS);
			ratiosList.addAll(CMStatic.REPORT_OTHERS_FINANCIALS);
			break;

		default : 
			_log.warn("Industry type not present for company : "+companyId + " , for Industry type : "+industryType);
		}	


		String fields="";
		List<String> ratioFieldList = new ArrayList<String>();
		List<CompanyFinancialMINDTO> qtFinancialData = new ArrayList<CompanyFinancialMINDTO>();
		List<CompanyFinancialMINDTO> yearlyFinancialData = new ArrayList<CompanyFinancialMINDTO>();
		List<CompanyFinancialMINDTO> qtFinRatioData = new ArrayList<CompanyFinancialMINDTO>();
		List<CompanyFinancialMINDTO> yearlyFinRatioData = new ArrayList<CompanyFinancialMINDTO>();

		for(String field : ratiosList) {
			if(field.startsWith("ff_")) {
				fields+=field+",";
			}else {
				ratioFieldList.add(field);
			}
		}

		if(!fields.equals("")) {
			fields = fields.substring(0, fields.length()-1);
		}

		final String temFields = fields;

		Callable<List<CompanyFinancialMINDTO>> qtFinancialDataCall = () -> {
			List<CompanyFinancialMINDTO> qtFinancialDataTemp = ratioService.getAllCompanyFinancial(companyId,temFields, CMStatic.PERIODICITY_QUARTERLY, null, null,currency);
			return qtFinancialDataTemp;
		};

		Callable<List<CompanyFinancialMINDTO>> yearlyFinancialDataCallable = () -> {
			List<CompanyFinancialMINDTO> yearlyFinancialDataTemp = ratioService.getAllCompanyFinancial(companyId,temFields, CMStatic.PERIODICITY_YEARLY, null, null,currency);
			return yearlyFinancialDataTemp;
		};

		Future<List<CompanyFinancialMINDTO>> qtFinRatioDataFuture = null;
		Future<List<CompanyFinancialMINDTO>> yearlyFinRatioDataFuture = null;
		if(ratioFieldList.size()!=0) {
			Callable<List<CompanyFinancialMINDTO>> qtFinRatioDataCallable = () -> {
				List<CompanyFinancialMINDTO> qtFinRatioDataCall = ratioService.getCompanyRatio(companyId, ratioFieldList, CMStatic.PERIODICITY_QUARTERLY, null, null,currency);
				return qtFinRatioDataCall;
			};
			Callable<List<CompanyFinancialMINDTO>> yearlyFinRatioDataCallable = () -> {
				List<CompanyFinancialMINDTO> yearlyFinRatioDataCall = ratioService.getCompanyRatio(companyId, ratioFieldList, CMStatic.PERIODICITY_YEARLY, null, null,currency);
				return yearlyFinRatioDataCall;
			};

			qtFinRatioDataFuture = executorService.submit(qtFinRatioDataCallable);
			yearlyFinRatioDataFuture = executorService.submit(yearlyFinRatioDataCallable);
			qtFinRatioData = qtFinRatioDataFuture.get();
			yearlyFinRatioData = yearlyFinRatioDataFuture.get();

		}

		Future<List<CompanyFinancialMINDTO>> qtFinancialDataFuture = executorService.submit(qtFinancialDataCall);
		Future<List<CompanyFinancialMINDTO>> yearlyFinancialDataFuture = executorService.submit(yearlyFinancialDataCallable);

		qtFinancialData = qtFinancialDataFuture.get();
		yearlyFinancialData = yearlyFinancialDataFuture.get();

		if(qtFinancialData!=null && qtFinancialData.size()!=0) {
			qtFinRatioData.addAll(qtFinancialData);
		}
		if(yearlyFinancialData!=null && yearlyFinancialData.size()!=0) {
			yearlyFinRatioData.addAll(yearlyFinancialData);
		}
		SortedSet<Date> qtPeriodSet = new TreeSet<Date>();
		SortedSet<Date> yearlyPeriodSet = new TreeSet<Date>();

		if(qtFinRatioData!=null && qtFinRatioData.size()!=0) {
			for(CompanyFinancialMINDTO finData : qtFinRatioData) {
				qtPeriodSet.add(finData.getPeriod());
			}
		}

		if(yearlyFinRatioData!=null && yearlyFinRatioData.size()!=0) {
			for(CompanyFinancialMINDTO finData : yearlyFinRatioData) {
				yearlyPeriodSet.add(finData.getPeriod());
			}
		}

		String cqPeriod = null, cyPeriod=null;
		String prevPeriod = null;
		List<String> qtPeriodList = new ArrayList<String>();
		List<String> yearlyPeriodList = new ArrayList<String>();

		if(qtPeriodSet!=null && qtPeriodSet.size()!=0) {
			cqPeriod = sdf.format(qtPeriodSet.last());
			qtPeriodList = dateUtil.generatePrevApplicablePeriodQtr(cqPeriod, 5);
			prevPeriod = qtPeriodList.get(1);
			if(!qtPeriodSet.contains(sdf.parse(prevPeriod))) {
				prevPeriod = null;
			}
		}
		if(yearlyPeriodSet!=null && yearlyPeriodSet.size()!=0) {
			cyPeriod = sdf.format(yearlyPeriodSet.last());
			yearlyPeriodList = dateUtil.generatePrevApplicablePeriodYear(cyPeriod, 6);
		} 
		String peerPeriod = null, peerPeriodType = null;
		Integer templateType = null;

		if(cqPeriod!=null && !cqPeriod.equals("") && prevPeriod!=null && !prevPeriod.equals("") && cyPeriod!=null && !cyPeriod.equals("")) {
			peerPeriod = sdf.format(sdf.parse(cqPeriod));
			peerPeriodType= CMStatic.PERIODICITY_QUARTERLY;
			//templateType = CMStatic.REPORT_TEMPLATE_TYPE_QUARTER;
			templateType = CMStatic.REPORT_TEMPLATE_TYPE_BOTH;
		}else if(cqPeriod!=null && !cqPeriod.equals("") && prevPeriod!=null && !prevPeriod.equals("")){
			peerPeriod = sdf.format(sdf.parse(cqPeriod));
			peerPeriodType= CMStatic.PERIODICITY_QUARTERLY;
			templateType = CMStatic.REPORT_TEMPLATE_TYPE_QUARTER;
		}else {
			peerPeriod = sdf.format(sdf.parse(cyPeriod));
			peerPeriodType= CMStatic.PERIODICITY_YEARLY;
			templateType = CMStatic.REPORT_TEMPLATE_TYPE_YEAR;
		}

		if(peerPeriodType.equals(CMStatic.PERIODICITY_QUARTERLY)) {
			companyProfile.setPeerPeriod(sdfY.format(sdf.parse(peerPeriod)));
		}else {
			companyProfile.setPeerPeriod(peerPeriod.split("-")[0]);
		}
		companyProfile.setPeerPeriodType(peerPeriodType);
		companyProfile.setTemplateType(templateType);


		/* Getting all the values before proceeding */
		List<BenchMarkCompanyDTO> benchMarkCompanies = benchmarkCompanyFuture.get();
		shareHoldingFuture.get();
		stockPriceFuture.get();
		betaFuture.get();
		companyHistoryPerformanceFuture.get();





		final List<String> qtPeriodListTemp = qtPeriodList;
		final List<String> yearlyPeriodListTemp = yearlyPeriodList;
		final List<CompanyFinancialMINDTO> qtFinRatioDatatemp = qtFinRatioData;
		final List<CompanyFinancialMINDTO>  yearlyFinRatioDataTemp = yearlyFinRatioData;
		final Integer templateTypeTemp = templateType;
		final String peerPeriodTemp = peerPeriod;
		final String peerPeriodTypeTemp = peerPeriodType;


		Future<?> setProfitabilityRatios = null;
		Future<?> setLiquidityRatios = null;
		Future<?> setLeverageRatios = null;
		Future<?> setReturnRatios = null;
		Future<?> setValuationRatios = null;
		Future<?> setFinancialData = null;
		Future<?> setPeerFinancialData = null;


		switch(industryType) {
		case CMStatic.INDUSTRY_IND :

			setProfitabilityRatios = executorService.submit(() -> {
				try {
					companyProfile.setProfitabilityRatios(getRatios(companyId, CMStatic.REPORT_INDUSTRY_RATIOS_PROFITABILITY,CMStatic.RATIO_PROFITABILITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setLiquidityRatios = executorService.submit(() -> {
				try {
					companyProfile.setLiquidityRatios(getRatios(companyId, CMStatic.REPORT_INDUSTRY_RATIOS_LIQUIDITY, CMStatic.RATIO_LIQUIDITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setLeverageRatios = executorService.submit(() -> {
				try {
					companyProfile.setLeverageRatios(getRatios(companyId, CMStatic.REPORT_INDUSTRY_RATIOS_LEVERAGE, CMStatic.RATIO_LEVERAGE, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setReturnRatios = executorService.submit(() -> {
				try {
					companyProfile.setReturnRatios(getRatios(companyId, CMStatic.REPORT_INDUSTRY_RATIOS_RETURN, CMStatic.RATIO_RETURN, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setValuationRatios = executorService.submit(() -> {
				try {
					companyProfile.setValuationRatios(getRatios(companyId, CMStatic.REPORT_INDUSTRY_RATIOS_VALUATIONS, CMStatic.RATIO_VALUATION, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setFinancialData(getRatios(companyId, CMStatic.REPORT_INDUSTRY_FINANCIALS, companyName, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});

			if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_ROE, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_NET_PROFIT, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
			}else {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INDUSTRY_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}
			setPeerFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setPeerFinancialData(getPeerFinancials(CMStatic.REPORT_INDUSTRY_FINANCIALS, peerPeriodTemp, peerPeriodTypeTemp, benchMarkCompanies, currency, companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			break;
		case CMStatic.INDUSTRY_BANK :
			setProfitabilityRatios = executorService.submit(() -> {
				try {
					companyProfile.setProfitabilityRatios(getRatios(companyId, CMStatic.REPORT_BANK_RATIOS_PROFITABILITY, CMStatic.RATIO_PROFITABILITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			setLiquidityRatios = executorService.submit(() -> {
				try {
					companyProfile.setLiquidityRatios(getRatios(companyId, CMStatic.REPORT_BANK_RATIOS_LIQUIDITY, CMStatic.RATIO_LIQUIDITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			setLeverageRatios = executorService.submit(() -> {
				try {
					companyProfile.setLeverageRatios(getRatios(companyId, CMStatic.REPORT_BANK_RATIOS_RISK_MANAGEMENT, CMStatic.RATIO_LEVERAGE, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setReturnRatios = executorService.submit(() -> {
				try {
					companyProfile.setReturnRatios(getRatios(companyId, CMStatic.REPORT_BANK_RATIOS_RETURN, CMStatic.RATIO_RETURN, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setValuationRatios = executorService.submit(() -> {
				try {
					companyProfile.setValuationRatios(getRatios(companyId, CMStatic.REPORT_BANK_RATIOS_VALUATIONS, CMStatic.RATIO_VALUATION, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setFinancialData(getRatios(companyId, CMStatic.REPORT_BANK_FINANCIALS, companyName, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});

			if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_LOAN_BOOK, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_ROA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}else {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_BANK_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}
			setPeerFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setPeerFinancialData(getPeerFinancials(CMStatic.REPORT_BANK_FINANCIALS, peerPeriodTemp, peerPeriodTypeTemp, benchMarkCompanies, currency, companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			break;
		case CMStatic.INDUSTRY_INSURANCE :
			setProfitabilityRatios = executorService.submit(() -> {
				try {
					companyProfile.setProfitabilityRatios(getRatios(companyId, CMStatic.REPORT_INSURANCE_RATIOS_PROFITABILITY,CMStatic.RATIO_PROFITABILITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});

			setLiquidityRatios = executorService.submit(() -> {
				try {
					companyProfile.setLiquidityRatios(getRatios(companyId, CMStatic.REPORT_INSURANCE_RATIOS_LIQUIDITY, CMStatic.RATIO_LIQUIDITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			setLeverageRatios = executorService.submit(() -> {
				try {
					companyProfile.setLeverageRatios(getRatios(companyId, CMStatic.REPORT_INSURANCE_RATIOS_SOLVENCY, CMStatic.RATIO_LEVERAGE, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setReturnRatios = executorService.submit(() -> {
				try {
					companyProfile.setReturnRatios(getRatios(companyId, CMStatic.REPORT_INSURANCE_RATIOS_RETURN, CMStatic.RATIO_RETURN, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setValuationRatios = executorService.submit(() -> {
				try {
					companyProfile.setValuationRatios(getRatios(companyId, CMStatic.REPORT_INSURANCE_RATIOS_VALUATIONS, CMStatic.RATIO_VALUATION, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setFinancialData(getRatios(companyId, CMStatic.REPORT_INSURANCE_FINANCIALS, companyName, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_AUM, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_ROA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}else {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_INSURANCE_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}
			setPeerFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setPeerFinancialData(getPeerFinancials(CMStatic.REPORT_INSURANCE_FINANCIALS, peerPeriodTemp, peerPeriodTypeTemp, benchMarkCompanies, currency, companyProfile, industryType));

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			break;
		case CMStatic.INDUSTRY_OTHER :
			setProfitabilityRatios = executorService.submit(() -> {
				try {
					companyProfile.setProfitabilityRatios(getRatios(companyId, CMStatic.REPORT_OTHERS_RATIOS_PROFITABILITY,CMStatic.RATIO_PROFITABILITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setLiquidityRatios = executorService.submit(() -> {
				try {
					companyProfile.setLiquidityRatios(getRatios(companyId, CMStatic.REPORT_OTHERS_RATIOS_LIQUIDITY, CMStatic.RATIO_LIQUIDITY, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			setLeverageRatios = executorService.submit(() -> {
				try {
					companyProfile.setLeverageRatios(getRatios(companyId, CMStatic.REPORT_OTHERS_RATIOS_SOLVENCY, CMStatic.RATIO_LEVERAGE, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setReturnRatios = executorService.submit(() -> {
				try {
					companyProfile.setReturnRatios(getRatios(companyId, CMStatic.REPORT_OTHERS_RATIOS_RETURN, CMStatic.RATIO_RETURN, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setValuationRatios = executorService.submit(() -> {
				try {
					companyProfile.setValuationRatios(getRatios(companyId, CMStatic.REPORT_OTHERS_RATIOS_VALUATIONS, CMStatic.RATIO_VALUATION, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			setFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setFinancialData(getRatios(companyId, CMStatic.REPORT_OTHERS_FINANCIALS, companyName, qtPeriodListTemp, yearlyPeriodListTemp, qtFinRatioDatatemp, yearlyFinRatioDataTemp, templateTypeTemp,companyProfile, industryType));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_AUM, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_ROA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}else {
				companyProfile.setQtPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setQtSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_QUARTERLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
				companyProfile.setYrPriPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_REVENUE, CMStatic.REPORT_CALC_TYPE_FIN, templateType, currency));
				companyProfile.setYrSecPerformance(getPerformanceData(companyId, CMStatic.PERIODICITY_YEARLY, CMStatic.REPORT_PERFORMANCE_PARAMETER_OTHERS_EBITDA, CMStatic.REPORT_CALC_TYPE_RATIO, templateType, currency));
			}
			setPeerFinancialData = executorService.submit(() -> {
				try {
					companyProfile.setPeerFinancialData(getPeerFinancials(CMStatic.REPORT_OTHERS_FINANCIALS, peerPeriodTemp, peerPeriodTypeTemp, benchMarkCompanies, currency, companyProfile, industryType));

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
			break;

		default : 
			_log.warn("Industry type not present for company : "+companyId + " , for Industry type : "+industryType);
		}	

		setProfitabilityRatios.get();
		setLiquidityRatios.get();
		setLeverageRatios.get();
		setReturnRatios.get();
		setValuationRatios.get();
		setFinancialData.get();
		setPeerFinancialData.get();

		executorService.shutdown();
		return companyProfile;

	}

	public List<ReportRatiosData> getRatios(String companyId, List<String> ratiosList, String fieldName,List<String> qtPeriodList, List<String> yearlyPeriodList, List<CompanyFinancialMINDTO> qtFinRatioData,List<CompanyFinancialMINDTO> yearlyFinRatioData, Integer templateType, ReportCompanyProfile reportCompanyProfile, String industryType) throws Exception{

		SimpleDateFormat sdf1 =  new SimpleDateFormat("MMM-yyyy");
		SimpleDateFormat sdf2 =  new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<ReportRatiosData> pRatiosList = new ArrayList<ReportRatiosData>();		
		String cqPeriod = "";
		String cqminus1Period = "";
		String cqminus2Period = "";
		String cqminus3Period = "";
		String cqminus4Period = "";
		String cyPeriod = "";
		String cyminus1Period = "";
		String cyminus2Period = "";
		String cyminus3Period = "";
		String cyminus4Period = "";
		String cyminus5Period = "";

		cqPeriod = qtPeriodList.size()>0?sdf1.format(sdf.parse(qtPeriodList.get(0))):"-";
		cqminus1Period = qtPeriodList.size()>1?sdf1.format(sdf.parse(qtPeriodList.get(1))):"-";
		cqminus2Period = qtPeriodList.size()>2?sdf1.format(sdf.parse(qtPeriodList.get(2))):"-";
		cqminus3Period = qtPeriodList.size()>3?sdf1.format(sdf.parse(qtPeriodList.get(3))):"-";
		cqminus4Period = qtPeriodList.size()>4?sdf1.format(sdf.parse(qtPeriodList.get(4))):"-";		
		cyPeriod = yearlyPeriodList.size()>0?sdf2.format(sdf.parse(yearlyPeriodList.get(0))):"-";
		cyminus1Period = yearlyPeriodList.size()>1?sdf2.format(sdf.parse(yearlyPeriodList.get(1))):"-";
		cyminus2Period = yearlyPeriodList.size()>2?sdf2.format(sdf.parse(yearlyPeriodList.get(2))):"-";
		cyminus3Period = yearlyPeriodList.size()>3?sdf2.format(sdf.parse(yearlyPeriodList.get(3))):"-";
		cyminus4Period = yearlyPeriodList.size()>4?sdf2.format(sdf.parse(yearlyPeriodList.get(4))):"-";
		cyminus5Period = yearlyPeriodList.size()>5?sdf2.format(sdf.parse(yearlyPeriodList.get(5))):"-";

		//String unit = "", currency="";
		Boolean nmFlag = reportCompanyProfile.getNmFlag();
		if(nmFlag==null){
			nmFlag = false;
		}
		for(String ratio:ratiosList) {
			ReportRatiosData reportRatios = new ReportRatiosData();
			Double cqData =null; Double cqDataMinus1 =null; Double cqDataMinus2 = null; Double cqDataMinus3 = null; Double cqDataMinus4 = null;
			Double qoq = null; Double yoy = null;
			Double cyData = null, cyDataMinus1 = null,cyDataMinus2 = null,cyDataMinus3 = null,cyDataMinus4 = null,cyDataMinus5 = null;

			String itemName = null;
			String unit = null;
			String currency = null;
			BalanceModelDTO finDTO = cmRepository.getFinancialParams(industryType, ratio);
			if(finDTO!=null) {
				itemName = finDTO.getDescription();
			}

			if(qtFinRatioData!=null && qtFinRatioData.size()!=0) {
				for(CompanyFinancialMINDTO finData : qtFinRatioData) {

					String ratio_key = finData.getFieldName();
					if(ratio.equals(ratio_key)) {
						//String unit = "", currency="";
						if(itemName==null){
							itemName = finData.getItemName();
						}
						if(!itemName.contains("Earnings per Share")){
							if(unit==null){
								unit = finData.getUnit();
							}
						}
						if(currency==null){
							currency = finData.getCurrency();
						}
						if(sdf1.format(finData.getPeriod()).equals(cqPeriod)) {
							cqData = finData.getData();
						}else if(sdf1.format(finData.getPeriod()).equals(cqminus1Period)) {
							cqDataMinus1 = finData.getData();
						}else if(sdf1.format(finData.getPeriod()).equals(cqminus2Period)) {
							cqDataMinus2 = finData.getData();
						}else if(sdf1.format(finData.getPeriod()).equals(cqminus3Period)) {
							cqDataMinus3 = finData.getData();
						}else if(sdf1.format(finData.getPeriod()).equals(cqminus4Period)) {
							cqDataMinus4 = finData.getData();
						}

						if(templateType!=CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
							if( cqData==null && cqDataMinus1!=null) {
								//qoq = cqDataMinus1;
							}else if(cqData!=null && cqDataMinus1==null) {
								//qoq = cqData;
							}else if(cqData!=null && cqDataMinus1!=null && cqDataMinus1!=0) {
								if(finData.getUnit()!=null && finData.getUnit().contains("%")) {
									qoq = ((cqData-cqDataMinus1))*100;
								}else {
									//qoq = ((cqData-cqDataMinus1)/cqDataMinus1)*100;
									qoq = commonService.percentageChange(cqData, cqDataMinus1);
								}
							}

							if( cqData==null && cqDataMinus4!=null) {
								//yoy = cqDataMinus4;
							}else if(cqData!=null && cqDataMinus4==null) {
								//yoy = cqData; 
							}else if(cqData!=null && cqDataMinus4!=null && cqDataMinus4!=0) {
								if(finData.getUnit()!=null && finData.getUnit().contains("%")) {
									yoy = ((cqData-cqDataMinus4))*100;
								}else {
									//yoy = ((cqData-cqDataMinus4)/cqDataMinus4)*100;
									yoy = commonService.percentageChange(cqData, cqDataMinus4);
								}

							}
						}

						reportRatios.setCqData(cqData);
						reportRatios.setCqDataMinus1(cqDataMinus1);
						reportRatios.setCqDataMinus2(cqDataMinus2);
						reportRatios.setCqDataMinus3(cqDataMinus3);
						reportRatios.setCqDataMinus4(cqDataMinus4);
						reportRatios.setQoq(qoq);
						reportRatios.setYoy(yoy);
					}
				}
			}

			if(yearlyFinRatioData!=null && yearlyFinRatioData.size()!=0) {
				for(CompanyFinancialMINDTO finData : yearlyFinRatioData) {
					//String itemName = finData.getItemName()==null?finData.getFieldName():finData.getItemName();
					String ratio_key = finData.getFieldName();
					if(ratio.equals(ratio_key)) {
						if(itemName==null){
							itemName = finData.getItemName();
						}
						if(!itemName.contains("Earnings per Share")){
							if(unit==null){
								unit = finData.getUnit();
							}
						}

						if(currency==null){
							currency = finData.getCurrency();
						}

						if(sdf2.format(finData.getPeriod()).equals(cyPeriod)) {
							cyData = finData.getData();
						}else if(sdf2.format(finData.getPeriod()).equals(cyminus1Period)) {
							cyDataMinus1 = finData.getData();
						}else if(sdf2.format(finData.getPeriod()).equals(cyminus2Period)) {
							cyDataMinus2 = finData.getData();
						}else if(sdf2.format(finData.getPeriod()).equals(cyminus3Period)) {
							cyDataMinus3 = finData.getData();
						}else if(sdf2.format(finData.getPeriod()).equals(cyminus4Period)) {
							cyDataMinus4 = finData.getData();
						}else if(sdf2.format(finData.getPeriod()).equals(cyminus5Period)) {
							cyDataMinus5 = finData.getData();
						}


						if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
							if( cyData==null && cyDataMinus1!=null) {
								//yoy = cqDataMinus4;
							}else if(cyData!=null && cyDataMinus1==null) {
								//yoy = cqData; 
							}else if(cyData!=null && cyDataMinus1!=null && cyDataMinus1!=0) {
								if(finData.getUnit()!=null && finData.getUnit().contains("%")) {
									yoy = ((cyData-cyDataMinus1))*100;
								}else {
									//yoy = ((cyData-cyDataMinus1)/cyDataMinus1)*100;
									yoy = commonService.percentageChange(cyData, cyDataMinus1);
								}
							}
							reportRatios.setYoy(yoy);	

						}

						reportRatios.setCyData(cyData);
						reportRatios.setCyDataMinus1(cyDataMinus1);
						reportRatios.setCyDataMinus2(cyDataMinus2);
						reportRatios.setCyDataMinus3(cyDataMinus3);
						reportRatios.setCyDataMinus4(cyDataMinus4);
						reportRatios.setCyDataMinus5(cyDataMinus5);

					}
				}
			}

			reportRatios.setUnit(unit);
			reportRatios.setCurrency(currency);
			reportRatios.setItemName(itemName);

			if(unit!=null && unit.contains("%")){
				reportRatios.setQoqyoyUnit("bps");
			}else{ 
				reportRatios.setQoqyoyUnit("%");
			}

			//reportRatios.setItemName(ratio);
			reportRatios.setFieldName(fieldName);

			SimpleDateFormat sdfYr =  new SimpleDateFormat("yy");
			reportRatios.setCqPeriod(cqPeriod==null?"-":cqPeriod);
			reportRatios.setCqPeriodMinus1(cqminus1Period==null?"-":cqminus1Period);
			reportRatios.setCqPeriodMinus2(cqminus2Period==null?"-":cqminus2Period);
			reportRatios.setCqPeriodMinus3(cqminus3Period==null?"-":cqminus3Period);
			reportRatios.setCqPeriodMinus4(cqminus4Period==null?"-":cqminus4Period);
			reportRatios.setCyPeriod(cyPeriod==null?"-":cyPeriod.split("-")[0]);
			reportRatios.setCyPeriodMinus1(cyminus1Period==null?"-":cyminus1Period);
			reportRatios.setCyPeriodMinus2(cyminus2Period==null?"-":cyminus2Period);
			reportRatios.setCyPeriodMinus3(cyminus3Period==null?"-":cyminus3Period);
			reportRatios.setCyPeriodMinus4(cyminus4Period==null?"-":cyminus4Period);
			reportRatios.setCyPeriodMinus5(cyminus5Period==null?"-":cyminus5Period);
			if(fieldName.equalsIgnoreCase(CMStatic.RATIO_VALUATION) && templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR){ 
				reportRatios.setCyPeriod(cyPeriod==null?"-":CMStatic.REPORT_PREFIX_YEAR_FY+sdfYr.format(sdf.parse(yearlyPeriodList.get(0))));
				reportRatios.setCyPeriodMinus1(cyminus1Period==null?"-":CMStatic.REPORT_PREFIX_YEAR_FY+sdfYr.format(sdf.parse(yearlyPeriodList.get(1))));
				reportRatios.setCyPeriodMinus2(cyminus2Period==null?"-":CMStatic.REPORT_PREFIX_YEAR_FY+sdfYr.format(sdf.parse(yearlyPeriodList.get(2))));
				reportRatios.setCyPeriodMinus3(cyminus3Period==null?"-":CMStatic.REPORT_PREFIX_YEAR_FY+sdfYr.format(sdf.parse(yearlyPeriodList.get(3))));
				reportRatios.setCyPeriodMinus4(cyminus4Period==null?"-":CMStatic.REPORT_PREFIX_YEAR_FY+sdfYr.format(sdf.parse(yearlyPeriodList.get(4))));
				reportRatios.setCyPeriodMinus5(cyminus5Period==null?"-":CMStatic.REPORT_PREFIX_YEAR_FY+sdfYr.format(sdf.parse(yearlyPeriodList.get(5))));
			}

			if(!fieldName.equals(CMStatic.RATIO_VALUATION)){
				if(!nmFlag){
					if((reportRatios.getQoq()!=null && Math.abs(reportRatios.getQoq()) >9999) || (reportRatios.getYoy()!=null && Math.abs(reportRatios.getYoy()) > 9999)){
						reportCompanyProfile.setNmFlag(true);
					}
				}
			}

			pRatiosList.add(reportRatios);
		}
		return pRatiosList;
	}



	public List<ReportHistPerformance> getCompanyHistoricalPerformance(String companyId,String companyName, String indexId,String currency){
		List<ReportHistPerformance> histPerfList = new ArrayList<>();
		ReportHistPerformance rhp = null;
		ReportHistPerformance indrhp = null;
		List<Map<String,Object>> histPerformance = cmFinancialDataService.getCompanyRelativePerformance(companyId,indexId,currency); 
		if(histPerformance!=null && !histPerformance.isEmpty()) {
			rhp = new ReportHistPerformance();
			indrhp = new ReportHistPerformance();
			for(Map<String,Object> histData : histPerformance) {
				String duration = (String) histData.get("duration");
				Double companyData = (Double) histData.get("comPerChange"); 
				if(StringUtils.hasText(indexId)){
					Double indexData = (Double) histData.get("indexPerChange"); 
					if(duration.equals(CMStatic.oneYear)) {
						indrhp.setOneYearData(indexData);
					}else if(duration.equals(CMStatic.threeYear)) {
						indrhp.setThreeYearData(indexData);
					}else if(duration.equals(CMStatic.fiveYear)) {
						indrhp.setFiveYearData(indexData);
					}
				}
				if(duration.equals(CMStatic.oneYear)) {
					rhp.setOneYearData(companyData);
				}else if(duration.equals(CMStatic.threeYear)) {
					rhp.setThreeYearData(companyData);
				}else if(duration.equals(CMStatic.fiveYear)) {
					rhp.setFiveYearData(companyData);
				}
			}
			rhp.setCompanyName(companyName);

			histPerfList.add(rhp);
			if(StringUtils.hasText(indexId)){
				IndexDTO indexes = null;
				try {
					indexes = capitalMarketService.getIndex(Integer.parseInt(indexId));	
					indrhp.setCompanyName(indexes.getName());
				} catch (Exception e) {
					_log.error(e.getMessage());
					indrhp.setCompanyName("Index");
				}

				histPerfList.add(indrhp);
			}
		}

		return histPerfList;
	}

	public List<ReportPerformanceData> getPerformanceData(String companyId, String periodType, String series, String finCalcType, Integer templateType, String currencyCode) throws ParseException{
		SimpleDateFormat sdf =  new SimpleDateFormat("MMM-yy");
		SimpleDateFormat sdf2 =  new SimpleDateFormat("yy");
		SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
		List<CompanyFinancialMINDTO> financialData = null;
		List<ReportPerformanceData> performanceData = new ArrayList<ReportPerformanceData>();
		if(finCalcType.equals(CMStatic.REPORT_CALC_TYPE_FIN)) {
			financialData = ratioService.getAllCompanyFinancial(companyId, series, periodType, null, null,currencyCode);
		}else {
			List<String> seriesList = Arrays.asList(series.split("\\s*,\\s*"));
			financialData = ratioService.getCompanyRatio(companyId, seriesList, periodType, null, null, currencyCode);
		}
		SortedSet<Date> periodSet = new TreeSet<Date>();
		for(CompanyFinancialMINDTO finData : financialData) {
			periodSet.add(finData.getPeriod());
		}

		List<String> periodList = new ArrayList<String>();
		if(periodSet!=null && periodSet.size()!=0) {
			String period = sdf1.format(periodSet.last());
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				periodList = dateUtil.generatePrevApplicablePeriodQtr(period, 5);
			}else {
				periodList = dateUtil.generatePrevApplicablePeriodYear(period, 4);
			}
		}


		Collections.sort(periodList);
		if(!periodList.isEmpty()){
			for(String period : periodList) {
				boolean check = false;
				String seriesAll = "";	
				String currency = "";
				String unit = "";
				String curUnit = "";

				ReportPerformanceData rpd = new ReportPerformanceData();
				for(CompanyFinancialMINDTO finData : financialData) {
					if(currency.equals("")){
						currency = finData.getCurrency()==null?"":finData.getCurrency();
					}
					if(unit.equals("")){
						unit = finData.getUnit()==null?"":finData.getUnit();
					}
					if(curUnit.equals("")){
						if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
							curUnit = "("+currency + " " + unit+")";
						}else if(currency!=null && !currency.trim().equals("")){
							curUnit = "("+currency+")";
						}else if(unit!=null && !unit.trim().equals("")){
							curUnit = "("+unit+")";
						}
					}

					String key_ratio = finData.getFieldName();
					_log.info(sdf1.format(finData.getPeriod()));
					_log.info(period);

					if(key_ratio.equals(series) && sdf1.format(finData.getPeriod()).equals(period)) {
						rpd.setData(finData.getData()==null?0.0:finData.getData());
						if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
							rpd.setPeriod(CMStatic.REPORT_PREFIX_YEAR_FY+sdf2.format(finData.getPeriod()));
						}else {
							rpd.setPeriod(sdf.format(finData.getPeriod()));
						}

						//rpd.setSeries(finData.getShortName()+" "+curUnit);
						if(seriesAll.equals("")){
							seriesAll = finData.getShortName()+" "+curUnit;
							rpd.setSeries(seriesAll);
						}
						performanceData.add(rpd);
						check = true;
						break;
					}
				}


				if(!check){
					//ReportPerformanceData rpd = new ReportPerformanceData();

					if(seriesAll.equals("")){
						seriesAll = cmRepository.getMetricNameFromMetricCode(series);
						seriesAll = seriesAll+" "+curUnit;
						rpd.setSeries(seriesAll);
					}
					rpd.setData(0.0);
					Date date = sdf1.parse(period);
					if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
						rpd.setPeriod(CMStatic.REPORT_PREFIX_YEAR_FY+sdf2.format(date));
					}else {
						rpd.setPeriod(sdf.format(date));
					}

					performanceData.add(rpd);
				}
			}
		}else{
			ReportPerformanceData rpd = new ReportPerformanceData();
			String seriesAll = cmRepository.getMetricNameFromMetricCode(series);
			rpd.setData(0.0);
			rpd.setSeries(seriesAll);
			LocalDate ld = new LocalDate(new Date());
			Date date = ld.minusYears(1).toDate();
			if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR) {
				rpd.setPeriod(CMStatic.REPORT_PREFIX_YEAR_FY+sdf2.format(date));
			}else {
				rpd.setPeriod(sdf.format(date));
			}
			performanceData.add(rpd);
		}

		_log.info(new Gson().toJson(performanceData));
		return performanceData;
	}

	public List<ReportPeerFinancialData> getPeerFinancials(List<String> ratiosList,String peerPeriod, String peerPeriodType, List<BenchMarkCompanyDTO> benchMarkCompanies, String currencyCode, ReportCompanyProfile companyProfile, String industryType) throws Exception{
		SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fields="";
		List<String> ratioFieldList = new ArrayList<String>();

		for(String field : ratiosList) {
			if(field.startsWith("ff_")) {
				fields+=field+",";
			}else {
				ratioFieldList.add(field);
			}
		}
		Date startDate = null, endDate = null;
		if(peerPeriodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(peerPeriod));		
			cal.add(Calendar.DAY_OF_MONTH, -45);
			startDate = sdf.parse(sdf.format(cal.getTime()));

			cal = Calendar.getInstance();
			cal.setTime(sdf.parse(peerPeriod));		
			cal.add(Calendar.DAY_OF_MONTH, 45);
			endDate = sdf.parse(sdf.format(cal.getTime()));

		}else if(peerPeriodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(peerPeriod));		
			cal.add(Calendar.MONTH, -3);
			startDate = sdf.parse(sdf.format(cal.getTime()));

			cal = Calendar.getInstance();
			cal.setTime(sdf.parse(peerPeriod));		
			cal.add(Calendar.MONTH, 3);
			endDate = sdf.parse(sdf.format(cal.getTime()));
		}

		List<ReportPeerFinancialData> peerFinancialDatas = new ArrayList<ReportPeerFinancialData>();

		for(String ratio:ratiosList) {
			String itemName = "";
			BalanceModelDTO finDTO = cmRepository.getFinancialParams(industryType, ratio);
			if(finDTO!=null) {
				itemName = finDTO.getDescription();
			}

			ReportPeerFinancialData peerData = new ReportPeerFinancialData();
			int compCount=1;
			for(BenchMarkCompanyDTO benchmarkCompany : benchMarkCompanies) {
				boolean gotData = false;
				String companyName = benchmarkCompany.getCompanyName();
				String currency = currencyCode;// benchmarkCompany.getCurrency();
				if(currency==null || currency.equals("")){
					currency = benchmarkCompany.getCurrency();
				}
				String compPeriod = null;

				List<CompanyFinancialMINDTO> ratioDatas = new ArrayList<>();
				List<CompanyFinancialMINDTO> finDatas = new ArrayList<>();


				/*if(!fields.equals("")){	
					finDatas = ratioService.getAllCompanyFinancial(benchmarkCompany.getCompanyId(),fields, peerPeriodType, sdf.parse(peerPeriod), sdf.parse(peerPeriod),currencyCode);
				}
				if(!ratioFieldList.isEmpty()){
					ratioDatas = ratioService.getCompanyRatio(benchmarkCompany.getCompanyId(), ratioFieldList, peerPeriodType, sdf.parse(peerPeriod), sdf.parse(peerPeriod),currencyCode);
				}
				finDatas.addAll(ratioDatas);
				 */
				compPeriod = peerPeriod;

				if(finDatas==null || finDatas.size()==0) {
					// CHECK FOR NEAREST PERIOD LIES IN 45 DAYS FOR QUARTER AND 90 DAYS FOR YEAR
					if(!fields.equals("")){	
						finDatas = ratioService.getAllCompanyFinancial(benchmarkCompany.getCompanyId(),fields, peerPeriodType, startDate, endDate,currencyCode);
					}
					if(!ratioFieldList.isEmpty()){
						ratioDatas = ratioService.getCompanyRatio(benchmarkCompany.getCompanyId(), ratioFieldList, peerPeriodType, startDate, endDate,currencyCode);
					}
					finDatas.addAll(ratioDatas);
					if(finDatas!=null && finDatas.size()!=0) {
						compPeriod = sdf.format(finDatas.get(0).getPeriod());
					}
				}
				if(peerPeriodType.equals(CMStatic.PERIODICITY_YEARLY)) {
					compPeriod = sdf1.format(sdf.parse(compPeriod));
				}
				if(finDatas!=null && finDatas.size()!=0) {
					for(CompanyFinancialMINDTO finData : finDatas) {
						String period = sdf.format(finData.getPeriod());
						if(peerPeriodType.equals(CMStatic.PERIODICITY_YEARLY)) {
							period = sdf1.format(finData.getPeriod());
						}

						if(period.equals(compPeriod) &&  ratio.equals(finData.getFieldName())) {
							String unit = finData.getUnit()==null?"":finData.getUnit();
							if(itemName!=null && !itemName.equals("")) {
								itemName = finData.getItemName()==null?"":finData.getItemName();
							}
							if(!itemName.contains("Earnings per Share")){
								if(peerData.getUnit()==null || peerData.getUnit().equals("")) {
									peerData.setUnit(unit);
								}
							}
							/*if(peerData.getItemName()==null || peerData.getItemName().equals("")) {
								peerData.setItemName(itemName);
							}	*/
							//peerData.setCurrency(finData.getCurrency()==null?"":finData.getCurrency());
							peerData.setCurrency("");
							if(compCount==1) {
								peerData.setCompanyData1(finData.getData());
								gotData = true;
							}else if(compCount==2) {
								peerData.setCompanyData2(finData.getData());
								gotData = true;
							}else if(compCount==3) {
								peerData.setCompanyData3(finData.getData());
								gotData = true;
							}else if(compCount==4) {
								peerData.setCompanyData4(finData.getData());
								gotData = true;
								break;
							}
						}
					}
				}
				// CHECK FOR LTM
				else if(peerPeriodType.equals(CMStatic.PERIODICITY_YEARLY)) {
					List<CompanyFinancialMINDTO> allFinData = ratioService.getAllCompanyFinancial(benchmarkCompany.getCompanyId(),fields, CMStatic.PERIODICITY_QUARTERLY, null, null,currencyCode);
					List<CompanyFinancialMINDTO> allRatioDatas = ratioService.getCompanyRatio(benchmarkCompany.getCompanyId(), ratioFieldList, CMStatic.PERIODICITY_QUARTERLY, null, null, currencyCode);
					allFinData.addAll(allRatioDatas);

					SortedSet<Date> qtPeriodSet = new TreeSet<Date>();
					if(allFinData!=null && allFinData.size()!=0) {
						for(CompanyFinancialMINDTO finData : allFinData) {
							qtPeriodSet.add(finData.getPeriod());
						}
					}
					if(qtPeriodSet.size()>4) {
						finDatas = allFinData;
						endDate = qtPeriodSet.last();
						List<String> periodList = dateUtil.generatePrevApplicablePeriodQtr(sdf.format(endDate), 4);
						Double data = 0.0D;
						for(CompanyFinancialMINDTO finData : finDatas) {
							if(ratio.equals(finData.getFieldName()) && periodList.contains(sdf.format(finData.getPeriod()))) {
								String unit = finData.getUnit()==null?"":finData.getUnit();
								if(itemName!=null && !itemName.equals("")) {
									itemName = finData.getItemName()==null?"":finData.getItemName();
								}
								if(!itemName.contains("Earnings per Share")){
									if(peerData.getUnit()==null || peerData.getUnit().equals("")) {
										peerData.setUnit(unit);
									}
								}
								/*if(peerData.getItemName()==null || peerData.getItemName().equals("")) {
									peerData.setItemName(itemName);
								}*/	
								peerData.setCurrency("");
								data += finData.getData();
							}

						}

						if(compCount==1) {
							peerData.setCompanyData1(data);
							gotData = true;
						}else if(compCount==2) {
							peerData.setCompanyData2(data);
							gotData = true;
						}else if(compCount==3) {
							peerData.setCompanyData3(data);
							gotData = true;
						}else if(compCount==4) {
							peerData.setCompanyData4(data);
							gotData = true;
							break;
						}
					}
				}
				companyName = companyName+"("+currency+")";
				if(!compPeriod.equals(peerPeriod)){
					companyName = "*"+companyName;
					companyProfile.setPeerFlag(true);
				}

				peerData.setItemName(itemName);

				if(compCount==1) {
					peerData.setCompanyName1(companyName);
				}else if(compCount==2) {
					peerData.setCompanyName2(companyName);
				}else if(compCount==3) {
					peerData.setCompanyName3(companyName);
				}else if(compCount==4) {
					peerData.setCompanyName4(companyName);
					break;
				}
				if(gotData){
					compCount++;
				}
			}
			peerFinancialDatas.add(peerData); 
		}
		return peerFinancialDatas;
	}


	// This method generates a PDF report 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generateCompanyProfile(ReportCompanyProfile companyProfile,String mainJasperFile,String pdfFileName, String imagePath) {
		try {

			Map parametersList = new HashMap<>();
			List<MySlot> filterdLiquidity = getFilteredData(companyProfile.getLiquidityRatios(), companyProfile.getTemplateType());
			parametersList.put("liquidityDataSource", new JRBeanCollectionDataSource(filterdLiquidity));
			//parametersList.put("liquidityDataSource", new JRBeanCollectionDataSource(companyProfile.getLiquidityRatios())); 

			List<MySlot> filterdProfitability = getFilteredData(companyProfile.getProfitabilityRatios(), companyProfile.getTemplateType());
			parametersList.put("profitabilityDataSource", new JRBeanCollectionDataSource(filterdProfitability));
			//parametersList.put("profitabilityDataSource", new JRBeanCollectionDataSource(companyProfile.getProfitabilityRatios()));

			List<MySlot> filterdLeverageRatio= getFilteredData(companyProfile.getLeverageRatios(), companyProfile.getTemplateType());
			parametersList.put("leverageDataSource", new JRBeanCollectionDataSource(filterdLeverageRatio));
			//parametersList.put("leverageDataSource", new JRBeanCollectionDataSource(companyProfile.getLeverageRatios())); 

			List<MySlot> filterdReturnRatio= getFilteredData(companyProfile.getReturnRatios(), companyProfile.getTemplateType());
			parametersList.put("returnDataSource", new JRBeanCollectionDataSource(filterdReturnRatio));
			//parametersList.put("returnDataSource", new JRBeanCollectionDataSource(companyProfile.getReturnRatios()));

			List<MySlot> filterdFinancialRatio = getFilteredData(companyProfile.getFinancialData(), companyProfile.getTemplateType());
			parametersList.put("financialDataSource", new JRBeanCollectionDataSource(filterdFinancialRatio));
			//parametersList.put("financialDataSource", new JRBeanCollectionDataSource(companyProfile.getFinancialData()));

			parametersList.put("companyMetaDataSource", new JRBeanCollectionDataSource(companyProfile.getCompanyDetails()));
			parametersList.put("companyMetaDataSource1", new JRBeanCollectionDataSource(companyProfile.getCompanyDetails()));
			parametersList.put("companyMetaDataSource2", new JRBeanCollectionDataSource(companyProfile.getCompanyDetails()));
			parametersList.put("companyHistDataSource", new JRBeanCollectionDataSource(companyProfile.getHistPerformance()));
			parametersList.put("peerHistDataSource", new JRBeanCollectionDataSource(companyProfile.getPeerHistPerformance()));
			parametersList.put("stockDataSource", new JRBeanCollectionDataSource(companyProfile.getStockData()));
			parametersList.put("valuationDataSource", new JRBeanCollectionDataSource(companyProfile.getValuationRatios()));
			parametersList.put("riskDataSource", new JRBeanCollectionDataSource(companyProfile.getRiskRatios()));
			parametersList.put("qtPriPerformanceDataSource", companyProfile.getQtPriPerformance());
			parametersList.put("qtSecPerformanceDataSource", companyProfile.getQtSecPerformance());
			parametersList.put("yrPriPerformanceDataSource", companyProfile.getYrPriPerformance());
			parametersList.put("yrSecPerformanceDataSource", companyProfile.getYrSecPerformance());
			parametersList.put("peerFinancialDataSource", new JRBeanCollectionDataSource(companyProfile.getPeerFinancialData())); 
			parametersList.put("period", companyProfile.getPeerPeriod());
			parametersList.put("periodType", companyProfile.getPeerPeriodType());
			parametersList.put("templateType", companyProfile.getTemplateType());
			if(companyProfile.getStockData()!=null && !companyProfile.getStockData().isEmpty()){
				parametersList.put("currency", companyProfile.getStockData().get(0).getCurrency());
			}else{
				parametersList.put("currency", "");
			}
			parametersList.put("imageLogoPath", imagePath +"/"+ CMStatic.IMAGE_LOGO);
			parametersList.put("shareholdingDataSource", new JRBeanCollectionDataSource(companyProfile.getShareholdingRatios()));

			parametersList.put("peerFlag", companyProfile.getPeerFlag());
			parametersList.put("nmFlag", companyProfile.getNmFlag());

			boolean status = generatePdf( mainJasperFile, pdfFileName, parametersList);
			//boolean statusHtml = generateHtml(mainJasperFile, pdfFileName.split(".pdf")[0]+".html", parametersList);
			_log.info("Jasper report generation status :::  " + status);
		} catch (Exception e) {
			_log.error("Error in Jasper report generation: ", e); 
		}

	}

	// This method generates a PDF report 
	@SuppressWarnings("unchecked")
	public ReportIndustryMonitor getIndustryMonitorData(HttpServletResponse response,ReportIndustryMonitor industryMonitor) throws ParseException {
		SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
		String industryType = "";
		_log.info("imRequest:: "+industryMonitor.toString());
		String countryCode = "";
		String countryName = "";

		String currency = industryMonitor.getCurrency();
		if(!StringUtils.hasText(currency)){
			// default currency set for default currency
			currency = "USD";
		}
		try {
			String ticsSectorCode =industryMonitor.getTicsSectorCode();
			String periodType =industryMonitor.getPeriodType();
			String ticsIndustryCode = industryMonitor.getTicsIndustryCode();
			Integer countryId = industryMonitor.getCountryId();
			Date period = industryMonitor.getPeriod ()/*==null?sdf1.parse("2018-12-31"):industryMonitor.getPeriod()*/;
			if(countryId!=null){
				countryCode = economyService.getCountryIsoCode(countryId);
				countryName = economyService.getCountryName(countryId);
			}

			System.out.println(ticsSectorCode + periodType + ticsIndustryCode +countryId+ period );

			LocalDate currentPeriod = LocalDate.fromDateFields(period);
			LocalDate economyStartDateLd = currentPeriod.minusYears(4)/*.dayOfMonth().withMaximumValue()*/;

			LocalDate startDateLd = currentPeriod;
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
				startDateLd = startDateLd.minusYears(4);
			}else {
				startDateLd = startDateLd.minusYears(1);
			}

			LocalDate prevPeriodLd = LocalDate.fromDateFields(period);
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
				prevPeriodLd = prevPeriodLd.minusYears(1);
			}else {
				prevPeriodLd = prevPeriodLd.minusMonths(3).dayOfMonth().withMaximumValue();
			}

			Date startDate = startDateLd.toDate();
			Date economyStartDate = economyStartDateLd.toDate();
			Date prevPeriod = prevPeriodLd.toDate();
			Date prevPeriodYearly = currentPeriod.minusYears(1).toDate();

			Date previousStartdateYearly = null;
			Date startDateYearly = null;

			HashMap<String, String> periodMap = dateUtil.startAndEndOfYearOfAPeriod(prevPeriodYearly);
			if(periodMap.get(CMStatic.STARTDATE)!=null){
				previousStartdateYearly = sdf1.parse(dateUtil.startAndEndOfYearOfAPeriod(prevPeriodYearly).get(CMStatic.STARTDATE));
			}

			periodMap = dateUtil.startAndEndOfYearOfAPeriod(period);
			if(periodMap.get(CMStatic.STARTDATE)!=null){
				startDateYearly = sdf1.parse(dateUtil.startAndEndOfYearOfAPeriod(period).get(CMStatic.STARTDATE));
			}

			List<TicsIndustry> industryDetails = industryService.getFactsetIndustry(ticsIndustryCode);
			if(industryDetails!=null && industryDetails.size()!=0) {
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
					List<ReportIndustryMetaData> indMetaData =	getIndustryMetaData(industryDetails, periodType, ticsSectorCode, ticsIndustryCode,startDateYearly, period, countryId, countryCode, countryName, currency);
					industryMonitor.setIndustryMetaData(indMetaData);
				}else{
					List<ReportIndustryMetaData> indMetaData = getIndustryMetaData(industryDetails, periodType, ticsSectorCode, ticsIndustryCode,period, period, countryId, countryCode,countryName, currency);
					industryMonitor.setIndustryMetaData(indMetaData);
				}
				industryType = industryDetails.get(0).getFactsetIndustry();
			}else {
				throw new Exception("No Data Found");
			}

			_log.info("industryType: "+industryType);

			industryMonitor.setIndustryType(industryType);
			switch(industryType) {
			case CMStatic.INDUSTRY_IND :
				industryMonitor.setFundamentalMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_INDUSTRY_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL,currency));
				industryMonitor.setValuationMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_INDUSTRY_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION,currency));
				List<Object> myListFunda_I_Ind = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_INDUSTRY_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL, currency);
				industryMonitor.setFundamentalQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListFunda_I_Ind).get(0));
				//industryMonitor.setFundamentalQoqYoySlot((LinkedHashMap<String, Integer>)myListFunda_I_Ind.get(1));
				List<Object> myListValuation_I_Ind = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_INDUSTRY_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION, currency);
				industryMonitor.setValuationQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListValuation_I_Ind).get(0));
				//industryMonitor.setValuationQoqYoySlot((LinkedHashMap<String, Integer>)myListValuation_I_Ind.get(1));

				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					//SET TOP 5 Countries a/c to different field names:
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_INDUSTRY_TOPPERS_LIST, periodType,prevPeriod, period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, period,period,  countryId, CMStatic.REPORT_INDUSTRY_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period, countryId,CMStatic.REPORT_INDUSTRY_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataQoQ = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_INDUSTRY_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsQoQ(growthMonitorsDataQoQ.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsQoQDoubleStarMsg(growthMonitorsDataQoQ.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsQoQTripleStarMsg(growthMonitorsDataQoQ.getTripleStarMsg());
				}else{
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_INDUSTRY_TOPPERS_LIST, periodType, previousStartdateYearly ,period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, startDateYearly, period, countryId, CMStatic.REPORT_INDUSTRY_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, previousStartdateYearly, period, countryId,CMStatic.REPORT_INDUSTRY_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
				}

				break;
			case CMStatic.INDUSTRY_BANK :
				industryMonitor.setFundamentalMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_BANK_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL, currency));
				industryMonitor.setValuationMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_BANK_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION, currency));
				List<Object> myListFunda_I_B = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_BANK_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL, currency);
				industryMonitor.setFundamentalQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListFunda_I_B).get(0));
				//industryMonitor.setFundamentalQoqYoySlot((LinkedHashMap<String, Integer>)myListFunda_I_B.get(1));

				List<Object> myListValuation_I_B = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_BANK_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION, currency);
				industryMonitor.setValuationQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListValuation_I_B).get(0));
				//industryMonitor.setValuationQoqYoySlot((LinkedHashMap<String, Integer>)myListValuation_I_B.get(1));

				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					//SET TOP 5 Countries a/c to different field names:
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_BANK_TOPPERS_LIST, periodType,prevPeriod, period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, period,period,  countryId, CMStatic.REPORT_BANK_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataQoQ = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_BANK_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsQoQ(growthMonitorsDataQoQ.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsQoQDoubleStarMsg(growthMonitorsDataQoQ.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsQoQTripleStarMsg(growthMonitorsDataQoQ.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_BANK_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
				}else{
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_BANK_TOPPERS_LIST, periodType, previousStartdateYearly ,period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, startDateYearly, period, countryId, CMStatic.REPORT_BANK_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, previousStartdateYearly, period, countryId,CMStatic.REPORT_BANK_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
				}

				break;
			case CMStatic.INDUSTRY_INSURANCE :
				industryMonitor.setFundamentalMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_INSURANCE_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL,currency));
				industryMonitor.setValuationMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_INSURANCE_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION,currency));
				List<Object> myListFunda_I_I = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_INSURANCE_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL, currency);
				industryMonitor.setFundamentalQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListFunda_I_I).get(0));
				//industryMonitor.setFundamentalQoqYoySlot((LinkedHashMap<String, Integer>)myListFunda_I_I.get(1));

				List<Object> myListValuation_I_I = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_INSURANCE_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION, currency);
				industryMonitor.setValuationQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListValuation_I_I).get(0));
				//industryMonitor.setValuationQoqYoySlot((LinkedHashMap<String, Integer>)myListValuation_I_I.get(1));

				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					//SET TOP 5 Countries a/c to different field names:
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_INSURANCE_TOPPERS_LIST, periodType,prevPeriod, period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, period,period,  countryId, CMStatic.REPORT_INSURANCE_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataQoQ = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_INSURANCE_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsQoQ(growthMonitorsDataQoQ.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsQoQDoubleStarMsg(growthMonitorsDataQoQ.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsQoQTripleStarMsg(growthMonitorsDataQoQ.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_INSURANCE_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
				}else{
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_INSURANCE_TOPPERS_LIST, periodType, previousStartdateYearly ,period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, startDateYearly, period, countryId, CMStatic.REPORT_INSURANCE_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, previousStartdateYearly, period, countryId,CMStatic.REPORT_INSURANCE_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());

				}

				break;
			case CMStatic.INDUSTRY_OTHER :
				industryMonitor.setFundamentalMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_OTHERS_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL, currency));
				industryMonitor.setValuationMonitors(getIndustryRatios(periodType, ticsSectorCode, ticsIndustryCode, period, startDate, countryId, CMStatic.REPORT_OTHERS_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION, currency));
				List<Object> myListFunda_I_O = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_OTHERS_FUNDAMENTAL_MONITORS, CMStatic.REPORT_MONITOR_FUNDAMENTAL, currency);
				industryMonitor.setFundamentalQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListFunda_I_O).get(0));
				//industryMonitor.setFundamentalQoqYoySlot((LinkedHashMap<String, Integer>)myListFunda_I_O.get(1));

				List<Object> myListValuation_I_O = getQoqYoyMonitorData(periodType, ticsSectorCode, ticsIndustryCode, startDate, period,  countryId, CMStatic.REPORT_OTHERS_VALUATION_MONITORS, CMStatic.REPORT_MONITOR_VALUATION, currency);
				industryMonitor.setValuationQoqYoy((LinkedHashMap<String, List<ReportQoqYoyData>>)(myListValuation_I_O).get(0));
				//industryMonitor.setValuationQoqYoySlot((LinkedHashMap<String, Integer>)myListValuation_I_O.get(1));

				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					//SET TOP 5 Countries a/c to different field names:
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_OTHERS_TOPPERS_LIST, periodType,prevPeriod, period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, period,period,  countryId, CMStatic.REPORT_OTHERS_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataQoQ = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_OTHERS_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsQoQ(growthMonitorsDataQoQ.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, prevPeriod, period, countryId,CMStatic.REPORT_OTHERS_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
				}else{
					industryMonitor.setIndustryToppersList(getIndustryToppersList(CMStatic.REPORT_OTHERS_TOPPERS_LIST, periodType, previousStartdateYearly ,period, ticsSectorCode, ticsIndustryCode, countryId, CMStatic.REPORT_TOPPERS_COUNT, currency));
					
					RegionalGrowthMonitorsData regionalMonitorsData = getRegionalMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, startDateYearly, period, countryId, CMStatic.REPORT_OTHERS_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setRegionalMonitors(regionalMonitorsData.getRegionalMonitors());
					industryMonitor.setRegionalMonitorsDoubleStarMsg(regionalMonitorsData.getDoubleStarMsg());
					industryMonitor.setRegionalMonitorsTripleStarMsg(regionalMonitorsData.getTripleStarMsg());
					
					RegionalGrowthMonitorsData growthMonitorsDataYoY = getGrowthMonitorsData(periodType, ticsSectorCode, ticsIndustryCode, previousStartdateYearly, period, countryId,CMStatic.REPORT_OTHERS_REGIONAL_GROWTH_MONITOR, currency);
					industryMonitor.setGrowthMonitorsYoY(growthMonitorsDataYoY.getRegionalMonitors());
					industryMonitor.setGrowthMonitorsYoYDoubleStarMsg(growthMonitorsDataYoY.getDoubleStarMsg());
					industryMonitor.setGrowthMonitorsYoYTripleStarMsg(growthMonitorsDataYoY.getTripleStarMsg());
				}
				break;
			default : 
				_log.warn("Industry type not present for tics Industry Code "+ticsIndustryCode);
				throw new Exception("No Data Found");
			}	
			// SET ECONOMY MONITOR
			if(countryId!=null){
				//industryMonitor.setEconomyMonitors(getEconomyMonitors(countryId, CMStatic.PERIODICITY_YEARLY, economyStartDate, period, CMStatic.REPORT_ECONOMY_MONITORS));

				industryMonitor.setEconomyMonitors(getEconomyMonitorsTradingEconomy(countryId, periodType, economyStartDate, period, CMStatic.REPORT_ECONOMY_MONITORS_TRADING_ECONOMICS , currency));

				//industryMonitor.setEconomyQoqYoy(getEconomyQoqYoyData(CMStatic.PERIODICITY_YEARLY, period, economyStartDate, countryId, CMStatic.REPORT_ECONOMY_MONITORS));

				industryMonitor.setEconomyQoqYoy(getEconomyQoqYoyDataTradingeconomics(periodType, period, economyStartDate, countryId, CMStatic.REPORT_ECONOMY_MONITORS_TRADING_ECONOMICS , currency));
			}

			return industryMonitor;
		}catch (Exception e) {
			_log.error(e.getMessage(),e);

		}
		return null;

	}

	public List<ReportIndustryMetaData> getIndustryMetaData(List<TicsIndustry> industryDetails,  String periodType, String ticsSectorCode,String ticsIndustryCode,  Date startDate, Date endDate, Integer countryId, String countryCode, String countryName, String reqCurrency){
		List<ReportIndustryMetaData> industryMetaData = new ArrayList<ReportIndustryMetaData>();
		String fields = "";
		ReportIndustryMetaData metaData = new ReportIndustryMetaData();

		String countryIdString = null;
		if(countryId != null)
			countryIdString = countryId.toString();

		if(countryId==null || countryId==0){
			metaData.setIndustryName(CMStatic.SCOPE+" - "+industryDetails.get(0).getTicsIndustryName());
		}else{
			metaData.setIndustryName(countryName + " - " + industryDetails.get(0).getTicsIndustryName());
		}

		metaData.setIndustryDescription(industryDetails.get(0).getTicsIndustryDesc());
		metaData.setQuartileDataByMcap(getQuartileData(CMStatic.REPORT_IM_FIELD_MARKET_CAP, periodType, ticsSectorCode, ticsIndustryCode, startDate,endDate, countryIdString, reqCurrency));
		metaData.setQuartileDataByRev(getQuartileData(CMStatic.REPORT_IM_FIELD_REVENUE, periodType, ticsSectorCode, ticsIndustryCode, startDate,endDate, countryIdString, reqCurrency));

		List<String> fieldList = CMStatic.REPORT_IM_META_DATA_FIELDS;
		for(String fieldNames : fieldList) {
			fields+=fieldNames+",";
		}
		if(!fields.equals("")) {
			fields = fields.substring(0, fields.length()-1);
		}
		Integer month = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		if(endDate!=null){
			month = Integer.parseInt(sdfMonth.format(endDate));
		}

		List<IndustryFinancialDataDTO> totTicsIndustryData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, countryIdString, fields, startDate, endDate, reqCurrency, month);
		if(totTicsIndustryData!=null && totTicsIndustryData.size()!=0) {
			for(IndustryFinancialDataDTO industryData : totTicsIndustryData) {
				String fieldName = industryData.getFieldName();
				/*if(fieldName.equals(CMStatic.REPORT_IM_FIELD_COMP_COUNT)) {
					metaData.setCompanyCoverage(industryData.getData().intValue());
				}*/
				if(fieldName.equals(CMStatic.REPORT_IM_FIELD_MARKET_CAP)) {
					metaData.setTotMarketCap(industryData.getData());
					metaData.setCurrency(industryData.getCurrency());
					metaData.setUnit(industryData.getUnit());
				}
				if(fieldName.equals(CMStatic.REPORT_IM_FIELD_REVENUE)) {
					metaData.setTotRevenue(industryData.getData());
					metaData.setCurrency(industryData.getCurrency());
					metaData.setUnit(industryData.getUnit());
				}
				if(fieldName.equals(CMStatic.REPORT_IM_FIELD_COUNTRY_COUNT)) {
					if(countryId!=null){
						metaData.setCountryCoverage(industryData.getCountryName());
					}else{
						metaData.setCountryCoverage(String.valueOf(industryData.getData().intValue()));
					}
				}else{
					metaData.setCountryCoverage("");
				}
			}
		}

		industryMetaData.add(metaData);
		_log.trace("industryMetaData:: "+industryMetaData);
		return industryMetaData;
	}

	public LinkedHashMap<String,Object> getIndustryRatios(String periodType, String ticsSectorCode, String ticsIndustryCode, Date period, Date startDate, Integer countryId, List<String> fieldNames, String monitorType, String reqCurrency) throws Exception{

		SimpleDateFormat sdf =  new SimpleDateFormat("MMM-yy");
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String fields="";
		for(String field : fieldNames) {
			fields+=field+",";
		}
		if(!fields.equals("")) {
			fields = fields.substring(0, fields.length()-1);
		}

		List<LocalDate> dateList = new ArrayList<>();
		if(periodType.equalsIgnoreCase("yearly")){
			for (LocalDate date = new LocalDate(startDate); date.isBefore(new LocalDate(period)) || date.isEqual(new LocalDate(period)); date = date.plusYears(1).dayOfMonth().withMaximumValue()) {
				dateList.add(date);
			}	
		}else{
			for (LocalDate date = new LocalDate(startDate); date.isBefore(new LocalDate(period)) || date.isEqual(new LocalDate(period)); date = date.plusMonths(3).dayOfMonth().withMaximumValue()) {
				dateList.add(date);
			}
		}


		Integer month = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		if(period!=null){
			month = Integer.parseInt(sdfMonth.format(period));
		}
		String countryIdString = null;
		if(countryId != null){
			countryIdString = countryId.toString();
		}
		List<IndustryFinancialDataDTO> ticsIndustryData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, countryIdString ,fields, startDate, period, reqCurrency, month);


		int i=1;
		for(String field: fieldNames) {
			String itemName = null;
			String unit = null;
			String currency = null;
			itemName = cmRepository.getItemNameByFieldName(field);
			if(itemName!=null){
				List<ReportPerformanceData> monitorDatas = new ArrayList<ReportPerformanceData>();
				for (LocalDate myDate : dateList) {
					ReportPerformanceData monitorData = new ReportPerformanceData();
					monitorData.setPeriod(sdf.format(myDate.toDate()));
					monitorData.setSeries(itemName);
					for (IndustryFinancialDataDTO industryFinancialDataDTO : ticsIndustryData) {
						if(industryFinancialDataDTO.getFieldName().equals(field)){ 
							if(unit==null){
								unit = industryFinancialDataDTO.getUnit();
							}
							if(currency==null){
								currency = industryFinancialDataDTO.getCurrency();
							}
							if(sdf.format(myDate.toDate()).equals(sdf.format(industryFinancialDataDTO.getPeriod()))){
								monitorData.setData(industryFinancialDataDTO.getData());
							}
						}
					}
					monitorDatas.add(monitorData);
				}

				String curUnit = "";
				if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
					curUnit = "("+currency + " " + unit+")";
				}else if(currency!=null && !currency.trim().equals("")){
					curUnit = "("+currency+")";
				}else if(unit!=null && !unit.trim().equals("")){
					curUnit = "("+unit+")";
				}

				map.put(monitorType+CMStatic.CHART+i,monitorDatas);

				List<String> li = CMStatic.curIgnoreList;
				if(li.contains(itemName.toLowerCase())){
					map.put(monitorType+CMStatic.CHART_TITLE+i,itemName);
				}else{
					map.put(monitorType+CMStatic.CHART_TITLE+i,itemName+" "+curUnit);
				}
			}
			i++;
		}
		_log.trace("getIndustryRatios:: "+new Gson().toJson(map));
		return map;
	}

	public LinkedHashMap<String, Object> getEconomyMonitors(Integer countryId,String periodType,Date startDate,Date endDate, List<Integer> indicatorList) throws Exception{
		SimpleDateFormat sdf =  new SimpleDateFormat("MMM-yy");
		SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");

		List<LocalDate> dateList = new ArrayList<>();
		if(periodType.equalsIgnoreCase("yearly")){
			for (LocalDate date = new LocalDate(startDate); date.isBefore(new LocalDate(endDate)) || date.isEqual(new LocalDate(endDate)); date = date.plusYears(1).dayOfMonth().withMaximumValue()) {
				dateList.add(date);
			}	
		}else{
			for (LocalDate date = new LocalDate(startDate); date.isBefore(new LocalDate(endDate)) || date.isEqual(new LocalDate(endDate)); date = date.plusMonths(3).dayOfMonth().withMaximumValue()) {
				dateList.add(date);
			}
		}

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		int i=1;
		for(Integer indicatorId : indicatorList) {
			String indicatorName = "";
			String unit = null;
			String currency = null;
			List<ReportPerformanceData> economyMonitors = new ArrayList<ReportPerformanceData>();
			if(indicatorId!=-1) {
				indicatorName = economyService.getIndicatorName(indicatorId);
				List<IndicatorDataDTO_old> indicatorDataDTO = economyService.getIndicatorDataByIndicator(countryId,indicatorId, periodType,startDate,endDate);
				for (LocalDate myDate : dateList) {
					ReportPerformanceData em = new ReportPerformanceData();
					em.setPeriod(sdf.format(myDate.toDate()));
					em.setSeries(indicatorName);
					for(IndicatorDataDTO_old dataDTO : indicatorDataDTO) {
						if(unit==null){
							unit = dataDTO.getUnit();
						}
						if(currency==null){
							currency = dataDTO.getCurrency();
						}
						if(periodType.equals(CMStatic.PERIODICITY_YEARLY)){
							if(sdfY.format(myDate.toDate()).equals(sdfY.format(dataDTO.getPeriod()))){
								em.setData(dataDTO.getData());
							}
						}else{
							if(sdf.format(myDate.toDate()).equals(sdf.format(dataDTO.getPeriod()))){
								em.setData(dataDTO.getData());
							}
						}
					}
					economyMonitors.add(em);	
				}
			}else {
				//GET FX DATA




				String currencyCode = economyService.getCountryCurrency(String.valueOf(countryId)).getCurrencyCode();
				indicatorName = currencyCode+" / "+CMStatic.CURRENCY_CODE_USD+" "+CMStatic.RATE;
				if(currencyCode.equals(CMStatic.CURRENCY_CODE_USD)) {
					currencyCode = CMStatic.CURRENCY_CODE_EURO;
					indicatorName = CMStatic.CURRENCY_CODE_USD+" / "+currencyCode+" "+CMStatic.RATE;
				}
				List<String> periodList = null;
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					periodList = dateUtil.generatePrevApplicablePeriodQtr(sdf1.format(endDate), 5);
				}else {
					periodList = dateUtil.generatePrevApplicablePeriodYear(sdf1.format(endDate), 5);
				}
				Collections.reverse(periodList);
				for(String period:periodList) {
					ReportPerformanceData em = new ReportPerformanceData();
					Double fxData = economyService.getFxData(periodType, sdf1.parse(period), currencyCode);
					em.setData(fxData);
					em.setPeriod(sdf.format(sdf1.parse(period)));
					em.setSeries(indicatorName);
					economyMonitors.add(em);		
				}
			}

			String curUnit = "";
			if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
				curUnit = " ("+currency + " " + unit+")";
			}else if(currency!=null && !currency.trim().equals("")){
				curUnit = " ("+currency+")";
			}else if(unit!=null && !unit.trim().equals("")){
				curUnit = " ("+unit+")";
			}
			map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART+i,economyMonitors);
			if(indicatorId!=-1){
				map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART_TITLE+i,indicatorName +" "+ curUnit);
			}else{
				map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART_TITLE+i,indicatorName);
			}
			i++;
		}

		_log.trace("getEconomyMonitors:: " + new Gson().toJson(map));
		return map;
	}


	public LinkedHashMap<String, Object> getEconomyMonitorsTradingEconomy(Integer countryId,String periodType,Date startDate,Date endDate, List<String> indicatorList , String reqCurrency) throws Exception{
		SimpleDateFormat sdf =  new SimpleDateFormat("MMM-yy");
		SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
		@SuppressWarnings("unused")
		SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");

		String requestedDataFormat = periodType;
		List<LocalDate> dateList = null;
		LinkedHashMap<String, Object> mappedData = new LinkedHashMap<>();
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<CountryListDTO> countryListTemp = null;
		List<String> countryList = new ArrayList<>();
		org.joda.time.LocalDate localDateRequest = new org.joda.time.LocalDate(endDate);
		String requestPeriod = sdf1.format(endDate);
		String[] periodArr = requestPeriod.split("-");
		String countryName = "";
		String countryIsoCode = "";
		if(countryListTemp==null){
			countryList.add(""+countryId);
			countryListTemp = economyService.getCMCountriesByIdList(countryList);
			if(countryListTemp!=null && !countryListTemp.isEmpty()){
				//String countryIsoCode = countryListTemp.get(0).getCountryIsoCode3();
				//List<CountryListDTO> teCountryList = imService.getTECountry(countryIsoCode);
				countryName = 	countryListTemp.get(0).getCountryName();
				//countryName = teCountryList.get(0).getCountryName();
				countryIsoCode = countryListTemp.get(0).getCountryIsoCode3();
				Integer month = null;
				String freq = null;

				List<Object> categoryData = imService.getBasePeriodMonth(countryIsoCode, periodType);
				if(categoryData!=null){
					freq = (String)categoryData.get(1);
					if(freq!=null){
						if(freq.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY) || freq.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY) || freq.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
							month = Integer.parseInt(periodArr[1]);	
						}else{
							month = (Integer)categoryData.get(0);
						}
					}else{
						month = (Integer)categoryData.get(0);
						if(month==null){
							month = 03;
						}
					}
				}

				if(month!=null){
					String date = periodArr[0]+"-"+month+"-"+"01";
					org.joda.time.LocalDate localDate = new org.joda.time.LocalDate(date);
					localDate = localDate.dayOfMonth().withMaximumValue();
					if(localDate.isAfter(localDateRequest)){
						if("yearly".equalsIgnoreCase(requestedDataFormat)){
							dateList = imService.getPeriod(requestedDataFormat, true, localDate.minusYears(1), 5, false);
						}
						if("quarterly".equalsIgnoreCase(requestedDataFormat)){
							//	dateList = imService.getPeriod(requestedDataFormat, true, localDate.minusMonths(3), 5, false);

							dateList = imService.getPeriod(requestedDataFormat, true, localDate.minusYears(1), 5, false);
						}
					}else if(localDate.isBefore(localDateRequest) || localDate.isEqual(localDateRequest)){
						dateList = imService.getPeriod(requestedDataFormat, true, localDate, 5, false);
					}else {
						return null;
					}

					Collections.reverse(dateList);
				}
			}
		}

		List<String> periodList = new ArrayList<>();
		for (LocalDate localDate : dateList) {
			String formattedDate = localDate.toString("yyyy-MM-dd");
			periodList.add(formattedDate);
		}

		int i=1;
		for(String indicatorId : indicatorList) {
			String currencyCode = "";
			currencyCode = reqCurrency;

			String currency = null;
			String indicatorName = "";
			indicatorName = indicatorId;
			String unit = null;
			List<ReportPerformanceData> economyMonitors = new ArrayList<ReportPerformanceData>();
			if(!indicatorId.equals("fx")) {
				try {
					if(countryId!=null){


						if(CMStatic.GDP.equals(indicatorName) && countryName.equals("China")){
							mappedData = chinaIssue(countryId, periodType, dateList, indicatorName, reqCurrency, requestedDataFormat,null);
						}else{
							mappedData =	imService.getEconomicalDataForIM( countryId, periodType, dateList, indicatorName, reqCurrency, requestedDataFormat);
						}

						for (LocalDate myDate : dateList) {
							ReportPerformanceData em = new ReportPerformanceData();
							em.setPeriod(sdf.format(myDate.toDate()));
							em.setSeries(indicatorName);

							String formattedDate = myDate.toString("yyyy-MM-dd");
							@SuppressWarnings("unchecked")
							List<IndicatorHistoricalDataDTO> data = (List<IndicatorHistoricalDataDTO>) mappedData.get(formattedDate);
							String frequency = (String)mappedData.get(CMStatic.FREQUENCY);
							if(data!=null && !data.isEmpty()){

								IndicatorHistoricalDataDTO dataDTO = data.get(0);
								if(indicatorName.equalsIgnoreCase(CMStatic.EXPORTS)){
									// Accumulate the data if the export is there 
									// as per the requested frequency , make the data to that frequency 
									//IndicatorHistoricalDataDTO dataDto  = data.get(0);
									if(requestedDataFormat.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
										Double tempData = 0d;
										for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
											tempData = tempData + indicatorHistoricalDataDTO.getData();

										}
										em.setData(tempData);
									}

									if(requestedDataFormat.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
										if(imService.canDataBeUse(requestedDataFormat, frequency)){
											Double tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {

												tempData = tempData + indicatorHistoricalDataDTO.getData();
											}
											em.setData(tempData);
										}else{
											Double tempData = null;
											em.setData(tempData);
										}

									}
								}else if(indicatorName.equalsIgnoreCase(CMStatic.GDP)){
									List<TelevisoryIndicatorReportingFrequencyDTO>  tvIRFDList = economyRepository.getReportedFrequencyBasedOnCountryAndCategory(countryName, countryIsoCode,indicatorName);
									Double tempData = null;
									if(tvIRFDList!=null && !tvIRFDList.isEmpty()){
										TelevisoryIndicatorReportingFrequencyDTO tvIRFD = tvIRFDList.get(0);
										if(requestedDataFormat.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
											if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.ANNUAL_REPORTED)){
												tempData=  dataDTO.getData();
											}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.QUARTER_REPORTED)){
												if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
													tempData =  null;
												}else if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
													tempData = 0d;
													for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
														Double tdata = indicatorHistoricalDataDTO.getData();
														if(tdata!=null){
															tempData = tempData + tdata;
														}
													}
												}else if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
													tempData = 0d;
													for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
														Double tdata = indicatorHistoricalDataDTO.getData();
														if(tdata!=null){
															tempData = tempData + tdata;
														}
													}
												}else if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
													tempData = 0d;
													for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
														Double tdata = indicatorHistoricalDataDTO.getData();
														if(tdata!=null){
															tempData = tempData + tdata;
														}
													}
												}
											}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.MONTHLY_REPORTED)){
												if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
													tempData = null;
												}

												if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
													tempData = null;
												}

												if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
													tempData = 0d;
													for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
														Double tdata = indicatorHistoricalDataDTO.getData();
														if(tdata!=null){
															tempData = tempData + tdata;
														}
													}

												}

												if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
													tempData = 0d;
													for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
														Double tdata = indicatorHistoricalDataDTO.getData();
														if(tdata!=null){
															tempData = tempData + tdata;
														}
													}
												}
											}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.WEEKLY_REPORTED)){
												if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
													tempData = null;
												}

												if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
													tempData = null;
												}

												if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
													tempData = null;
												}

												if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
													tempData = 0d;
													for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
														Double tdata = indicatorHistoricalDataDTO.getData();
														if(tdata!=null){
															tempData = tempData + tdata;
														}
													}
												}
											}
										}else if(requestedDataFormat.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
											if(imService.canDataBeUse(requestedDataFormat, frequency)){
												if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.ANNUAL_REPORTED)){
													if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
														if(dataDTO.getData()!=null && dataDTO.getData()!=0){
															tempData = dataDTO.getData()/4;
														}
													}

													if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
														if(dataDTO.getData()!=null && dataDTO.getData()!=0){
															tempData = dataDTO.getData()/4;
														}
													}

													if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
														if(dataDTO.getData()!=null && dataDTO.getData()!=0){
															tempData = dataDTO.getData()/4;
														}
													}

													if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
														if(dataDTO.getData()!=null && dataDTO.getData()!=0){
															tempData = dataDTO.getData()/4;
														}
													}

												}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.QUARTER_REPORTED)){

													if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
														tempData = null;
													}

													if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
														tempData = dataDTO.getData();
													}

													if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
														tempData = 0d;
														for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
															Double tdata = indicatorHistoricalDataDTO.getData();
															if(tdata!=null){
																tempData = tempData + tdata;
															}
														}
													}

													if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
														tempData = 0d;
														for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
															Double tdata = indicatorHistoricalDataDTO.getData();
															if(tdata!=null){
																tempData = tempData + tdata;
															}
														}
													}

												}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.MONTHLY_REPORTED)){
													if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
														tempData = null;
													}

													if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
														tempData = null;
													}

													if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
														tempData = dataDTO.getData();
													}

													if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
														tempData = 0d;
														for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
															Double tdata = indicatorHistoricalDataDTO.getData();
															if(tdata!=null){
																tempData = tempData + tdata;
															}
														}
													}

												}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.WEEKLY_REPORTED)){
													if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
														tempData = null;
													}

													if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
														tempData = null;
													}

													if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
														tempData = null;
													}

													if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
														tempData = dataDTO.getData();
													}

												}
											}else{
												em.setData(tempData);
											}
										}
										em.setData(tempData);
									}else{
										em.setData(dataDTO.getData());
									}
								}else{
									em.setData(dataDTO.getData());
								}
							}
							economyMonitors.add(em);
						}
					}
					//List<Double> dataToUnit = new ArrayList<>();
					/*for (ReportPerformanceData reportPerformanceData : economyMonitors) {
						dataToUnit.add(reportPerformanceData.getData());
					}*/

					if(unit==null){
						if(indicatorName.equalsIgnoreCase(CMStatic.CPI)){
							unit = "points";
						}else if(indicatorName.equalsIgnoreCase(CMStatic.EXPORTS)){
							unit = "Billion";
						}else if(indicatorName.equalsIgnoreCase(CMStatic.GDP)){
							unit = "Billion";
						}else if(indicatorName.equalsIgnoreCase(CMStatic.FER)){
							unit = "Billion";
						}else if(indicatorName.equalsIgnoreCase(CMStatic.INTR_RATE)){
							unit = "%";
						}else{
							unit = "";
						}
					}

					if(currency==null){
						if(indicatorName.equalsIgnoreCase(CMStatic.CPI)){
							currency = "";
						}else if(indicatorName.equalsIgnoreCase(CMStatic.EXPORTS)){
							currency = currencyCode;
						}else if(indicatorName.equalsIgnoreCase(CMStatic.GDP)){
							currency = currencyCode;
						}else if(indicatorName.equalsIgnoreCase(CMStatic.FER)){
							currency = currencyCode;
						}else if(indicatorName.equalsIgnoreCase(CMStatic.INTR_RATE)){
							currency = "";
						}else{
							currency = "";
						}
					}

					if(unit!=null){
						for (ReportPerformanceData reportPerformanceData : economyMonitors) {
							if(reportPerformanceData.getData()!=null){
								if(unit.equals("Million")){
									Double newData = reportPerformanceData.getData()/1000000;
									reportPerformanceData.setData(newData);
								}

								if(unit.equals("Billion")){
									Double newData = reportPerformanceData.getData()/1000000000;
									reportPerformanceData.setData(newData);
								}
							}
							//dataToUnit.add(reportPerformanceData.getData());
						}
					}
				} catch (Exception e) {
					_log.error(e.getMessage(),e);
				}

			}else {
				//GET FX DATA
				String countryCurrencyCode = economyService.getCountryCurrency(String.valueOf(countryId)).getCurrencyCode();
				//indicatorName = currencyCode+" / "+CMStatic.CURRENCY_CODE_USD+" "+CMStatic.RATE;
				String cc1 = currencyCode;
				String cc2 = countryCurrencyCode;

				if(countryCurrencyCode.equals(currencyCode)){
					cc1 = "USD";
					cc2 = countryCurrencyCode;
				}

				/*if(currencyCode.equals("USD")){
						cc1 = "USD";
						cc2 = countryCurrencyCode;
					}
				 */

				indicatorName = cc1 +" / "+ cc2 +" "+CMStatic.RATE;
				if(cc2.equals(CMStatic.CURRENCY_CODE_USD) && cc1.equals(CMStatic.CURRENCY_CODE_USD)) {
					cc2 = CMStatic.CURRENCY_CODE_EURO;
					indicatorName = cc1 +" / "+cc2+" "+CMStatic.RATE;
				}

				for(String period : periodList) {
					ReportPerformanceData em = new ReportPerformanceData();
					Double fxData = economyService.getDailyFxData(periodType, sdf1.parse(period), cc1, cc2);
					em.setData(fxData);
					em.setPeriod(sdf.format(sdf1.parse(period)));
					em.setSeries(indicatorName);
					economyMonitors.add(em);		
				}
			}

			String curUnit = "";
			if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
				curUnit = " ("+currency + " " + unit+")";
			}else if(currency!=null && !currency.trim().equals("")){
				curUnit = " ("+currency+")";
			}else if(unit!=null && !unit.trim().equals("")){
				curUnit = " ("+unit+")";
			}

			map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART+i,economyMonitors);
			if(!indicatorId.equals("fx")) {
				if(indicatorName.equalsIgnoreCase(CMStatic.GDP)){
					map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART_TITLE+i,CMStatic.GDP_DISPLAY_NAME +" "+ curUnit);
				}else{
					map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART_TITLE+i,indicatorName +" "+ curUnit);
				}
			}else{
				map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.CHART_TITLE+i,indicatorName);
			}
			i++;
		}

		_log.trace("getEconomyMonitors:: " + new Gson().toJson(map));
		return map;
	}


	public LinkedHashMap<String, List<ReportIndustryToppersData>> getIndustryToppersList(List<String> fieldList, String periodType, Date prevPeriod,Date period, String ticsSectorCode, String ticsIndustryCode, Integer countryId, Integer recCount, String reqCurrency) throws ParseException{
		LinkedHashMap<String, List<ReportIndustryToppersData>> map = new LinkedHashMap<String, List<ReportIndustryToppersData>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
		int i=1;	

		String countryIdString = null;
		if(countryId != null)
			countryIdString = countryId.toString();

		for(String fieldName : fieldList) { 
			List<ReportIndustryToppersData> industryToppersData = new ArrayList<ReportIndustryToppersData>();

			//List<IndustryFinancialDataDTO> industryFinData = industryService.getIndustryCountryByField(fieldName,periodType,prevPeriod,period,ticsSectorCode,ticsIndustryCode,countryId,recCount);
			List<IndustryFinancialDataDTO> industryFinData = null;
			Integer month = null;
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			if(period!=null){
				month = Integer.parseInt(sdfMonth.format(period));
			}
			//In case of country specific Industry Monitor ToppersList will have Comapany names
			//And In case of Global Industry Monitor ToppersList will have Country names
			Integer companyFlag = null;
			if(countryIdString!=null && !countryIdString.equals("")) {
				industryFinData = industryService.getIDCompanyData(periodType, ticsIndustryCode, null, countryIdString, fieldName, prevPeriod, period, reqCurrency, month,companyFlag);
			} else {
				industryFinData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, "-1", fieldName, prevPeriod, period, reqCurrency, month);
			}

			//filter out industry not null data
			industryFinData = industryFinData.stream().filter(finData -> finData.getData()!=null)
					.collect(Collectors.toList());
			if(CMStatic.REPORT_TOPPERS_LIST_ORDER_ASC.contains(fieldName)) {
				if(CMStatic.REPORT_TOPPERS_LIST_ORDER_ASC_AND_DESC_NEGATIVE_NUMBER.contains(fieldName)){
					//Toppers list will be sort by data ASC and negative number will be sorted by desc
					Comparator<? super Double> comparatorAscAndDescNegativeNumber = (data1,data2) -> {
						if(data1.compareTo(0d) < 0 || data2.compareTo(0d) < 0){//order desc
							return data2.compareTo(data1);
						}else{//order asc
							return data1.compareTo(data2);				
						}
					};
					industryFinData.sort(Comparator.comparing(IndustryFinancialDataDTO::getData, comparatorAscAndDescNegativeNumber));
				}else{//toppers list will be sort by data ASC
					industryFinData.sort(Comparator.comparing(IndustryFinancialDataDTO::getData, Comparator.naturalOrder()));	
				}
			} else {
				//Toppers list will be sort by data DESC 
				industryFinData.sort(Comparator.comparing(IndustryFinancialDataDTO::getData, Comparator.naturalOrder()).reversed());
				//industryFinData.sort(Comparator.comparing(IndustryFinancialDataDTO::getData, Comparator.nullsFirst(Comparator.naturalOrder())).reversed());
			}

			List<String> countryCompList = new ArrayList<String>();
			int count=0;
			for(IndustryFinancialDataDTO industryData : industryFinData) {
				String tempPeriod = null ,industryTempPeriod = null;
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					if(period!=null){
						tempPeriod = sdf.format(period);
					}
					if(industryData.getApplicablePeriod()!=null){
						industryTempPeriod = sdf.format(industryData.getApplicablePeriod());
					}
				}else{
					if(period!=null){
						tempPeriod = sdfY.format(period);
					}
					if(industryData.getApplicablePeriod()!=null){
						industryTempPeriod = sdfY.format(industryData.getApplicablePeriod());
					}
				}
				if(industryTempPeriod!=null && tempPeriod!=null){
					if(industryTempPeriod.equals(tempPeriod) && count<CMStatic.TOPPER_ROW_COUNT){
						if(countryId!=null && !countryId.equals("")) {
							if(!countryCompList.contains(industryData.getCompanyName())){
								countryCompList.add(industryData.getCompanyName());
							}
						}else {
							if(!countryCompList.contains(industryData.getCountryName())){
								countryCompList.add(industryData.getCountryName());
							}
						}
						count++;
					}
				}
			}


			for(String countryComp : countryCompList) {
				ReportIndustryToppersData tp = new ReportIndustryToppersData();	
				Integer qoqYoyTrend = null;
				Double currData = null, prevData = null, qoqYoy = null;
				String unit =null,currency=null,itemName=null;
				Integer companyCount =null;
				for(IndustryFinancialDataDTO dataDTO : industryFinData) {
					String category = "";
					if(countryId!=null && !countryId.equals("")) {
						category = dataDTO.getCompanyName();
					}else {
						category = dataDTO.getCountryName();
					}
					if(fieldName.equals(dataDTO.getFieldName()) && countryComp.equals(category)){
						if(unit==null){
							unit=dataDTO.getUnit();
						}
						if(currency==null){
							currency=dataDTO.getCurrency();
						}
						if(itemName==null){
							itemName=dataDTO.getItemName();
						}
						if(companyCount==null){
							companyCount=dataDTO.getCompanyCount();
						}
						if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
							if(sdf.format(dataDTO.getApplicablePeriod()).equals(sdf.format(prevPeriod))) {
								prevData = dataDTO.getData();
							}
							if(sdf.format(dataDTO.getApplicablePeriod()).equals(sdf.format(period))) {
								currData = dataDTO.getData();
							}

						}else{
							if(sdfY.format(dataDTO.getApplicablePeriod()).equals(sdfY.format(prevPeriod))) {
								prevData = dataDTO.getData();
							}

							if(sdfY.format(dataDTO.getApplicablePeriod()).equals(sdfY.format(period))) {
								currData = dataDTO.getData();
							}
						}

						if(currData==null && prevData!=null) {
						}else if(currData!=null && prevData==null) {
						}else if(currData!=null && prevData!=null && prevData!=0) {
							if(unit!=null && unit.contains("%")) {
								qoqYoy = ((currData-prevData))*100;
							}else {
								//qoqYoy = ((currData-prevData)/prevData)*100;
								qoqYoy = commonService.percentageChange(currData, prevData);
							}
						}

						if(qoqYoy!=null){
							if(qoqYoy < 0){
								qoqYoyTrend = -1;
							}
							if(qoqYoy > 0){
								qoqYoyTrend = 1;
							}
							if(qoqYoy == 0){
								qoqYoyTrend = 0;
							}
						}
					}
				}
				if(countryId!=null && !countryId.equals("")) {
					tp.setCategoryName(countryComp);
				}else {
					tp.setCategoryName(countryComp+" ( "+companyCount+" )");
				}
				tp.setCompanyCount(companyCount);
				tp.setCurrency(currency);
				tp.setData(currData);
				tp.setQoqTrend(qoqYoyTrend);
				if(unit.equals("days")) {unit=CMStatic.DAYS;}
				tp.setUnit(unit);
				tp.setItemName(itemName);
				industryToppersData.add(tp); 
			}
			if(industryToppersData!=null && !industryToppersData.isEmpty()){
				int topperRowsToDisplay = CMStatic.TOPPER_ROW_COUNT;
				int totEntry = industryToppersData.size();
				int diff = totEntry - topperRowsToDisplay;
				if(diff < 0){//add blank toppers data if toppers count is less than topperRowsToDisplay count.
					for (int j = 0; j < Math.abs(diff); j++) {
						ReportIndustryToppersData tp = new ReportIndustryToppersData();	
						industryToppersData.add(tp); 
					}
				}
			}else{



			}

			map.put(CMStatic.REPORT_MONITOR_TOPPERS+"Table"+i,industryToppersData);
			i++;
		}
		_log.trace("getIndustryToppersList:: "+new Gson().toJson(map));
		return map;
	}

	public RegionalGrowthMonitorsData getRegionalMonitorsData(String periodType,String ticsSectorCode,String ticsIndustryCode,Date startDate, Date period, Integer countryId, List<String> fieldList, String reqCurrency){
		List<ReportMonitorData> monitorList = new LinkedList<ReportMonitorData>();
		Set<String> countryCompList = new LinkedHashSet<String>();
		List<IndustryFinancialDataDTO> ticsIndustryMonitorsData =  null;
		String fields="";
		for(String field : fieldList) {
			fields+=field+",";
		}
		if(!fields.equals("")) {
			fields = fields.substring(0, fields.length()-1);
		}
		Integer month = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		if(period!=null){
			month = Integer.parseInt(sdfMonth.format(period));
		}

		if(countryId!=null) {
			Integer companyFlag = 1;
			ticsIndustryMonitorsData = industryService.getIDCompanyData(periodType, ticsIndustryCode, null, countryId.toString(), fields, startDate, period, reqCurrency, month, companyFlag);
		}else {
			//send countryid = -1 for getting all/global data
			ticsIndustryMonitorsData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, "-1" , fields, startDate, period, reqCurrency, month);
		}

		for(IndustryFinancialDataDTO industryData : ticsIndustryMonitorsData) {
			if(countryId!=null) {
				if(industryData.getCompanyName()!=null){
					countryCompList.add(industryData.getCompanyName());
				}
			}else{
				if(industryData.getCountryName()!=null){
					countryCompList.add(industryData.getCountryName());
				}
			}
		}

		List<ReportMonitorData> monitorListDataAndActive = new LinkedList<ReportMonitorData>();
		List<ReportMonitorData> monitorListDataAndNotActive = new LinkedList<ReportMonitorData>();
		List<ReportMonitorData> monitorListNoDataActive = new LinkedList<ReportMonitorData>();
		List<ReportMonitorData> monitorListNoDataNotActive = new LinkedList<ReportMonitorData>();
		String doubleStarMsg = null;
		String tripleStarMsg = null;

		List<BalanceModelDTO> fieldDetails = sectorRepository.getIndustryFinancialModel(null, fieldList);
		for(String countryComp: countryCompList) {
			boolean hasData = false;
			boolean isActive = true;

			
			int j=1;
			ReportMonitorData monitorData = new ReportMonitorData();
			for(String fieldName : fieldList) {
				String tempFieldNameDisp = null;
				String curUnit = "";
				if(fieldDetails!=null){
					BalanceModelDTO bmDetails = fieldDetails.parallelStream().filter(i-> fieldName.equals(i.getFieldName())).findFirst().orElse(null);
					if(bmDetails!=null){
						tempFieldNameDisp = bmDetails.getShortName();
					}
					String currency = "";
					if(bmDetails.getCurrencyFlag()!=null && bmDetails.getCurrencyFlag()==1){
						currency = reqCurrency;
					}
					String unit = bmDetails.getUnit();
					if(unit!=null && (unit.equals("%") || unit.equals("bps"))){
						currency = "";
					}

					if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
						curUnit = " ("+currency + " " + unit+")";
					}else if(currency!=null && !currency.trim().equals("")){
						curUnit = " ("+currency+")";
					}else if(unit!=null && !unit.trim().equals("")){
						curUnit = " ("+unit+")";
					}
				}


				for(IndustryFinancialDataDTO findData : ticsIndustryMonitorsData) {
					String category = "";
					if(countryId!=null) {
						category = findData.getCompanyName();
					}else {
						category = findData.getCountryName();
					}

					if(fieldName.equals(findData.getFieldName()) && countryComp.equals(category)){

						if(isActive && findData.getCompanyActiveFlag()!=null && findData.getCompanyActiveFlag()==0){
							isActive = false;
						}

						String currency = null;
						String unit = null;
						if(curUnit.equals("")){
							currency = findData.getCurrency();
							unit = findData.getUnit();
							if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
								curUnit = " ("+currency + " " + unit+")";
							}else if(currency!=null && !currency.trim().equals("")){
								curUnit = " ("+currency+")";
							}else if(unit!=null && !unit.trim().equals("")){
								curUnit = " ("+unit+")";
							}
						}

						if(findData.getData()!=null){
							hasData = true;
						}

						if(countryId!=null) {
						}else {
							monitorData.setCategoryName(findData.getCountryName()+"("+findData.getCompanyCount()+")");
						}
						if(j==1) {
							monitorData.setItemCurrency1(findData.getCurrency());
							monitorData.setItemUnit1(findData.getUnit());
							monitorData.setItemData1(findData.getData());
						}
						if(j==2) {
							monitorData.setItemCurrency2(findData.getCurrency());
							monitorData.setItemUnit2(findData.getUnit());
							monitorData.setItemData2(findData.getData());
						}
						if(j==3) {
							monitorData.setItemCurrency3(findData.getCurrency());
							monitorData.setItemUnit3(findData.getUnit());
							monitorData.setItemData3(findData.getData());
						}
						if(j==4) {
							monitorData.setItemCurrency4(findData.getCurrency());
							monitorData.setItemUnit4(findData.getUnit());
							monitorData.setItemData4(findData.getData());
						}
						if(j==5) {
							monitorData.setItemCurrency5(findData.getCurrency());
							monitorData.setItemUnit5(findData.getUnit());
							monitorData.setItemData5(findData.getData());
						}
						if(j==6) {
							monitorData.setItemCurrency6(findData.getCurrency());
							monitorData.setItemUnit6(findData.getUnit());
							monitorData.setItemData6(findData.getData());
						}
						if(j==7) {
							monitorData.setItemCurrency7(findData.getCurrency());
							monitorData.setItemUnit7(findData.getUnit());
							monitorData.setItemData7(findData.getData());
						}
						if(j==8) {
							monitorData.setItemCurrency8(findData.getCurrency());
							monitorData.setItemUnit8(findData.getUnit());
							monitorData.setItemData8(findData.getData());
						}
						if(j==9) {
							monitorData.setItemCurrency9(findData.getCurrency());
							monitorData.setItemUnit9(findData.getUnit());
							monitorData.setItemData9(findData.getData());
						}
						if(j==10) {
							monitorData.setItemCurrency10(findData.getCurrency());
							monitorData.setItemUnit10(findData.getUnit());
							monitorData.setItemData10(findData.getData());
						}
					}
				}

				String itemName = tempFieldNameDisp + curUnit;

				if(j==1) {
					monitorData.setItemName1(itemName);
				}
				if(j==2) {
					monitorData.setItemName2(itemName);
				}
				if(j==3) {
					monitorData.setItemName3(itemName);
				}
				if(j==4) {
					monitorData.setItemName4(itemName);
				}
				if(j==5) {
					monitorData.setItemName5(itemName);
				}
				if(j==6) {
					monitorData.setItemName6(itemName);
				}
				if(j==7) {
					monitorData.setItemName7(itemName);
				}
				if(j==8) {
					monitorData.setItemName8(itemName);
				}
				if(j==9) {
					monitorData.setItemName9(itemName);
				}
				if(j==10) {
					monitorData.setItemName10(itemName);
				}




				j++;
			}

			if(hasData && isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(countryComp);
				}
				monitorListDataAndActive.add(monitorData);
			}else if(hasData && !isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(CMStatic.DOUBLE_STAR + " " + countryComp);
					if(doubleStarMsg==null){
						doubleStarMsg = CMStatic.DOUBLE_STAR_MSG;
					}
				}
				monitorListDataAndNotActive.add(monitorData);
			}else if(!hasData && isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(CMStatic.TRIPLE_STAR + " " + countryComp);
					if(tripleStarMsg==null){
						tripleStarMsg = CMStatic.TRIPLE_STAR_MSG;
					}
				}
				monitorListNoDataActive.add(monitorData);
			}else if(!hasData && !isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(CMStatic.DOUBLE_STAR + " " + countryComp);
					if(doubleStarMsg==null){
						doubleStarMsg = CMStatic.DOUBLE_STAR_MSG;
					}
				}
				monitorListNoDataNotActive.add(monitorData);
			}
		}
		monitorList.addAll(sortMonitorList(monitorListDataAndActive));
		monitorList.addAll(sortMonitorList(monitorListDataAndNotActive));
		monitorList.addAll(sortMonitorList(monitorListNoDataNotActive));
		monitorList.addAll(sortMonitorList(monitorListNoDataActive));
		RegionalGrowthMonitorsData regionalMonitorsData = new RegionalGrowthMonitorsData(monitorList,doubleStarMsg,tripleStarMsg);


		/*if(monitorList!=null && !monitorList.isEmpty() && monitorList.size()>1){
			Collections.sort(monitorList,new Comparator<ReportMonitorData>() {
				@Override
				public int compare(ReportMonitorData o1, ReportMonitorData o2) {
					if(o1.getItemData1()!=null && o2.getItemData1()!=null){
						if(o2.getItemData1() > o1.getItemData1()){
							return 1;
						}else if(o2.getItemData1() < o1.getItemData1()){
							return -1;
						}else{
							return 0;
						}
					}else {
						if(o1.getItemData1()!=null){
							return -1;
						}else if(o2.getItemData1()!=null){
							return 1;
						}else{
							return 0;
						}
					}
				}
			});
		}*/
		return regionalMonitorsData;
	}

	public RegionalGrowthMonitorsData getGrowthMonitorsData(String periodType,String ticsSectorCode, String ticsIndustryCode, Date prevPeriod,  Date period, Integer countryId,List<String> fieldList, String reqCurrency) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
		List<ReportMonitorData> monitorList = new LinkedList<ReportMonitorData>();
		Set<String> countryCompList = new LinkedHashSet<String>();
		List<IndustryFinancialDataDTO> ticsIndustryMonitorsData =  null;
		String fields="";
		for(String field : fieldList) {
			fields+=field+",";
		}
		if(!fields.equals("")) {
			fields = fields.substring(0, fields.length()-1);
		}

		Integer month = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		if(period!=null){
			month = Integer.parseInt(sdfMonth.format(period));
		}

		if(countryId!=null) {
			Integer companyFlag = 1;
			ticsIndustryMonitorsData = industryService.getIDCompanyData(periodType, ticsIndustryCode, null, countryId.toString(), fields, prevPeriod, period, reqCurrency, month,companyFlag);
		}else {
			//send countryid = -1 for getting all/global data
			ticsIndustryMonitorsData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode,"-1" ,fields,prevPeriod, period, reqCurrency, month);
		}

		/*try {
			Files.write(Paths.get("/home/navankur/Desktop/jsonFile.txt"), new Gson().toJson(ticsIndustryMonitorsData).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 */

		for(IndustryFinancialDataDTO industryData : ticsIndustryMonitorsData) {
			if(countryId!=null) {
				if(industryData.getCompanyName()!=null){
					countryCompList.add(industryData.getCompanyName());
				}
			}else {
				if(industryData.getCountryName()!=null){
					countryCompList.add(industryData.getCountryName());
				}
			}
		}


		List<ReportMonitorData> monitorListDataAndActive = new LinkedList<ReportMonitorData>();
		List<ReportMonitorData> monitorListDataAndNotActive = new LinkedList<ReportMonitorData>();
		List<ReportMonitorData> monitorListNoDataActive = new LinkedList<ReportMonitorData>();
		List<ReportMonitorData> monitorListNoDataNotActive = new LinkedList<ReportMonitorData>();
		String doubleStarMsg = null;
		String tripleStarMsg = null;

		List<BalanceModelDTO> fieldDetails = sectorRepository.getIndustryFinancialModel(null, fieldList);

		for(String countryComp: countryCompList) {

			boolean hasData = false;
			boolean isActive = true;


			int j=1;
			ReportMonitorData monitorData = new ReportMonitorData();
			for(String fieldName : fieldList) {

				String tempFieldNameDisp = null;
				String curUnit = "";
				if(fieldDetails!=null){
					BalanceModelDTO bmDetails = fieldDetails.parallelStream().filter(i-> fieldName.equals(i.getFieldName())).findFirst().orElse(null);
					if(bmDetails!=null){
						tempFieldNameDisp = bmDetails.getShortName();
					}
					
					String currency = "";
					if(bmDetails.getCurrencyFlag()!=null && bmDetails.getCurrencyFlag()==1){
						currency = reqCurrency;
					}

					String unit = bmDetails.getUnit();
					if(unit!=null && unit.contains("%")){
						unit = "bps";
					}else{ 
						unit = "%";
					}
					
					if(unit!=null && (unit.equals("%") || unit.equals("bps"))){
						currency = "";
					}

					if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
						curUnit = " ("+currency + " " + unit+")";
					}else if(currency!=null && !currency.trim().equals("")){
						curUnit = " ("+currency+")";
					}else if(unit!=null && !unit.trim().equals("")){
						curUnit = " ("+unit+")";
					}
				}



				Double currData = null, prevData = null, qoqYoy=null ;
				for(IndustryFinancialDataDTO findData : ticsIndustryMonitorsData) {
					String category = "";
					if(countryId!=null) {
						category = findData.getCompanyName();
					}else {
						category = findData.getCountryName();
					}

					if(fieldName.equals(findData.getFieldName()) && countryComp.equals(category)){

						if(isActive && findData.getCompanyActiveFlag()!=null && findData.getCompanyActiveFlag()==0){
							isActive = false;
						}
						String unitTemp=findData.getUnit();
						String unit = null;
						if(unitTemp!=null && unitTemp.contains("%")){
							unit = "bps";
						}else{ 
							unit = "%";
						}

						String currency ="";// findData.getCurrency();

						if(curUnit.equals("")){
							if(currency!=null && unit!=null && !currency.trim().equals("") && !unit.trim().equals("")){
								curUnit = " ("+currency + " " + unit+")";
							}else if(currency!=null && !currency.trim().equals("")){
								curUnit = " ("+currency+")";
							}else if(unit!=null && !unit.trim().equals("")){
								curUnit = " ("+unit+")";
							}
						}

						if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
							if(sdf.format(findData.getApplicablePeriod()).equals(sdf.format(prevPeriod))) {
								prevData = findData.getData();
							}
							if(sdf.format(findData.getApplicablePeriod()).equals(sdf.format(period))) {
								currData = findData.getData();	
							}

						}else{
							if(sdfY.format(findData.getApplicablePeriod()).equals(sdfY.format(prevPeriod))) {
								prevData = findData.getData();
							}

							if(sdfY.format(findData.getApplicablePeriod()).equals(sdfY.format(period))) {
								currData = findData.getData();
							}

						}


						if(currData==null && prevData!=null) {
						}else if(currData!=null && prevData==null) {
						}else if(currData!=null && prevData!=null && prevData!=0) {
							if(unitTemp!=null && unitTemp.contains("%")) {
								qoqYoy = ((currData-prevData))*100;
							}else {
								qoqYoy = commonService.percentageChange(currData, prevData);
							}

							hasData = true;
						}

						if(countryId!=null) {
							/*if(findData.getCompanyActiveFlag()!=null && findData.getCompanyActiveFlag()==0){
								monitorData.setCategoryName(CMStatic.DOUBLE_STAR + " " +findData.getCompanyName());
							}else{
								monitorData.setCategoryName(findData.getCompanyName());
							}*/
						}else {
							monitorData.setCategoryName(findData.getCountryName()+"("+findData.getCompanyCount()+")");
						}
						if(j==1) {
							monitorData.setItemCurrency1(currency);
							monitorData.setItemUnit1(unit);
							monitorData.setItemData1(qoqYoy);
						}
						if(j==2) {
							monitorData.setItemCurrency2(currency);
							monitorData.setItemUnit2(unit);
							monitorData.setItemData2(qoqYoy);
						}
						if(j==3) {
							monitorData.setItemCurrency3(currency);
							monitorData.setItemUnit3(unit);
							monitorData.setItemData3(qoqYoy);
						}
						if(j==4) {
							monitorData.setItemCurrency4(currency);
							monitorData.setItemUnit4(unit);
							monitorData.setItemData4(qoqYoy);
						}
						if(j==5) {
							monitorData.setItemCurrency5(currency);
							monitorData.setItemUnit5(unit);
							monitorData.setItemData5(qoqYoy);
						}
						if(j==6) {
							monitorData.setItemCurrency6(currency);
							monitorData.setItemUnit6(unit);
							monitorData.setItemData6(qoqYoy);
						}
						if(j==7) {
							monitorData.setItemCurrency7(currency);
							monitorData.setItemUnit7(unit);
							monitorData.setItemData7(qoqYoy);
						}
						if(j==8) {
							monitorData.setItemCurrency8(currency);
							monitorData.setItemUnit8(unit);
							monitorData.setItemData8(qoqYoy);
						}
						if(j==9) {
							monitorData.setItemCurrency9(currency);
							monitorData.setItemUnit9(unit);
							monitorData.setItemData9(qoqYoy);
						}
						if(j==10) {
							monitorData.setItemCurrency10(currency);
							monitorData.setItemUnit10(unit);
							monitorData.setItemData10(qoqYoy);
						}
					}
				}
				
				String itemName = tempFieldNameDisp + curUnit;
				
				if(j==1) {
					monitorData.setItemName1(itemName);
				}
				if(j==2) {
					monitorData.setItemName2(itemName);
				}
				if(j==3) {
					monitorData.setItemName3(itemName);
				}
				if(j==4) {
					monitorData.setItemName4(itemName);
				}
				if(j==5) {
					monitorData.setItemName5(itemName);
				}
				if(j==6) {
					monitorData.setItemName6(itemName);
				}
				if(j==7) {
					monitorData.setItemName7(itemName);
				}
				if(j==8) {
					monitorData.setItemName8(itemName);
				}
				if(j==9) {
					monitorData.setItemName9(itemName);
				}
				if(j==10) {
					monitorData.setItemName10(itemName);
				}
				j++;
			}
			
			if(hasData && isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(countryComp);
				}
				monitorListDataAndActive.add(monitorData);
			}else if(hasData && !isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(CMStatic.DOUBLE_STAR + " " + countryComp);
					if(doubleStarMsg==null){
						doubleStarMsg = CMStatic.DOUBLE_STAR_MSG;
					}
				}
				monitorListDataAndNotActive.add(monitorData);
			}else if(!hasData && isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(CMStatic.TRIPLE_STAR + " " + countryComp);
					if(tripleStarMsg==null){
						tripleStarMsg = CMStatic.TRIPLE_STAR_MSG;
					}
				}
				monitorListNoDataActive.add(monitorData);
			}else if(!hasData && !isActive){
				if(countryId!=null) {
					monitorData.setCategoryName(CMStatic.DOUBLE_STAR + " " + countryComp);
					if(doubleStarMsg==null){
						doubleStarMsg = CMStatic.DOUBLE_STAR_MSG;
					}
				}
				monitorListNoDataNotActive.add(monitorData);
			}
		}

		monitorList.addAll(sortMonitorList(monitorListDataAndActive));
		monitorList.addAll(sortMonitorList(monitorListDataAndNotActive));
		monitorList.addAll(sortMonitorList(monitorListNoDataNotActive));
		monitorList.addAll(sortMonitorList(monitorListNoDataActive));
		RegionalGrowthMonitorsData growthMonitorsData = new RegionalGrowthMonitorsData(monitorList,doubleStarMsg,tripleStarMsg);

		/*if(monitorList!=null && !monitorList.isEmpty() && monitorList.size() > 1){
			Collections.sort(monitorList ,new Comparator<ReportMonitorData>() {
				@Override 
				public int compare(ReportMonitorData o1, ReportMonitorData o2) {
					if(o1.getItemData1()!=null && o2.getItemData1()!=null){
						if(o2.getItemData1() > o1.getItemData1()){
							return 1;
						}else if(o2.getItemData1() < o1.getItemData1()){
							return -1;
						}else{
							return 0;
						}
					}else {
						if(o1.getItemData1()!=null){
							return -1;
						}else if(o2.getItemData1()!=null){
							return 1;
						}else{
							return 0;
						}
					}
				}
			});
		}
		 */
		//_log.info("getGrowthMonitorsData:: - "+periodType +" - :: "+new Gson().toJson(monitorList));
		return growthMonitorsData;
	}

	public List<ReportMonitorData> sortMonitorList(List<ReportMonitorData> monitorList) {
		if(monitorList!=null && !monitorList.isEmpty() && monitorList.size() > 1){
			Collections.sort(monitorList ,new Comparator<ReportMonitorData>() {
				@Override 
				public int compare(ReportMonitorData o1, ReportMonitorData o2) {
					if(o1.getItemData1()!=null && o2.getItemData1()!=null){
						if(o2.getItemData1() > o1.getItemData1()){
							return 1;
						}else if(o2.getItemData1() < o1.getItemData1()){
							return -1;
						}else{
							return 0;
						}
					}else {
						if(o1.getItemData1()!=null){
							return -1;
						}else if(o2.getItemData1()!=null){
							return 1;
						}else{
							return 0;
						}
					}
				}
			});
		}
		return monitorList;
	}

	@SuppressWarnings("unused")
	public List<Object> getQoqYoyMonitorData(String periodType, String ticsSectorCode, String ticsIndustryCode, Date startDate, Date period, Integer countryId, List<String> fieldNames, String monitorType, String reqCurrency) throws Exception{
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfYearly =  new SimpleDateFormat("yyyy");
		List<IndustryFinancialDataDTO> ticsIndustryData = null;//, ticsIndustryDataY = null;
		List<String> qtPeriodList = dateUtil.generatePrevApplicablePeriodQtr(sdf.format(period), 6);
		List<String> yearlyPeriodList = dateUtil.generatePrevApplicablePeriodYear(sdf.format(period), 6);
		String cPeriod = "", cminus1Period = "", cminus2Period="", cminus3Period="",cminus4Period="",cminus5Period=""/*, cyPeriod="", cyminus1Period=""*/;
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			cPeriod = yearlyPeriodList.get(0).split("-")[0];
			cminus1Period = yearlyPeriodList.get(1).split("-")[0];
			cminus2Period = yearlyPeriodList.get(2).split("-")[0];
			cminus3Period =yearlyPeriodList.get(3).split("-")[0];
			cminus4Period = yearlyPeriodList.get(4).split("-")[0];
			cminus5Period = yearlyPeriodList.get(5).split("-")[0];			
		}else {
			cPeriod = qtPeriodList.get(0);
			cminus1Period = qtPeriodList.get(1);
			cminus2Period = qtPeriodList.get(2);
			cminus3Period = qtPeriodList.get(3);
			cminus4Period = qtPeriodList.get(4);
			cminus5Period = qtPeriodList.get(5);
			/*cyPeriod = yearlyPeriodList.get(0).split("-")[0];
			cyminus1Period = yearlyPeriodList.get(1).split("-")[0];*/
		}

		LinkedHashMap<String,  List<ReportQoqYoyData>> map = new LinkedHashMap<String,  List<ReportQoqYoyData>>();
		LinkedHashMap<String, Integer> mapSlot = new LinkedHashMap<String, Integer>(1);
		String fields="";
		for(String field : fieldNames) {
			fields+=field+",";
		}
		if(!fields.equals("")) {
			fields = fields.substring(0, fields.length()-1);
		}
		Integer month = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		if(period!=null){
			month = Integer.parseInt(sdfMonth.format(period));
		}
		String countryIdString = null;
		if(countryId != null)
			countryIdString = countryId.toString();
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			ticsIndustryData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, countryIdString ,fields, sdf.parse(yearlyPeriodList.get(5)), sdf.parse(yearlyPeriodList.get(0)), reqCurrency, month);
		}else {
			ticsIndustryData = industryService.getIDIndustryData(periodType, ticsSectorCode, ticsIndustryCode, countryIdString ,fields, sdf.parse(cminus5Period), sdf.parse(cPeriod), reqCurrency, month);
		}

		int i=1;
		for(String field: fieldNames) {
			ReportQoqYoyData qoqYoyData = new ReportQoqYoyData();
			Double cData =null; Double cDataMinus1 =null; Double cDataMinus2 = null; Double cDataMinus3 = null; Double cDataMinus4 = null;Double cDataMinus5 = null;
			Double qoq = null;
			Double cyData = null, cyDataMinus1 = null;
			String unit = null;
			for(IndustryFinancialDataDTO finData : ticsIndustryData) {
				if(field.equals(finData.getFieldName())) {
					if(unit==null){
						unit = finData.getUnit();
					}
					if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)){
						String myPeriod = sdf.format(finData.getApplicablePeriod());
						if(myPeriod.equals(cPeriod)) {
							cData = finData.getData();
						}else if(myPeriod.equals(cminus1Period)) {
							cDataMinus1 = finData.getData();
						}else if(myPeriod.equals(cminus2Period)) {
							cDataMinus2 = finData.getData();
						}else if(myPeriod.equals(cminus3Period)) {
							cDataMinus3 = finData.getData();
						}else if(myPeriod.equals(cminus4Period)) {
							cDataMinus4 = finData.getData();
						}else if(myPeriod.equals(cminus5Period)) {
							cDataMinus5 = finData.getData();
						}
						/*else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY) && sdf.format(indicatorData.getPeriod()).equals(cminus5Period)) {
							cDataMinus5 = indicatorData.getData();
						}*/
					}else{
						String myYearlyPeriod = sdfYearly.format(finData.getApplicablePeriod());
						if(myYearlyPeriod.equals(cPeriod)) {
							cData = finData.getData();
						}else if(myYearlyPeriod.equals(cminus1Period)) {
							cDataMinus1 = finData.getData();
						}else if(myYearlyPeriod.equals(cminus2Period)) {
							cDataMinus2 = finData.getData();
						}else if(myYearlyPeriod.equals(cminus3Period)) {
							cDataMinus3 = finData.getData();
						}else if(myYearlyPeriod.equals(cminus4Period)) {
							cDataMinus4 = finData.getData();
						}else if(myYearlyPeriod.equals(cminus5Period)) {
							cDataMinus5 = finData.getData();
						}
					}
				}
			}	

			if(cData!=null && cDataMinus1!=null && cDataMinus1!=0) {
				if(unit!=null && unit.contains("%")) {
					qoq = ((cData-cDataMinus1))*100;
				}else {

					//qoq = ((cData-cDataMinus1)/cDataMinus1)*100;

					qoq = commonService.percentageChange(cData, cDataMinus1);

				}
				/*if(!periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY))
					qoqYoyData.setQoqYoyData4(qoq);
				else*/
				qoqYoyData.setQoqYoyData5(qoq);
			}

			if(cDataMinus1!=null && cDataMinus2!=null && cDataMinus2!=0) {
				if(unit!=null && unit.contains("%")) {
					qoq = ((cDataMinus1-cDataMinus2))*100;
				}else {
					//qoq = ((cDataMinus1-cDataMinus2)/cDataMinus2)*100;

					qoq = commonService.percentageChange(cDataMinus1, cDataMinus2);
				}
				/*if(!periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY))
					qoqYoyData.setQoqYoyData3(qoq);
				else*/
				qoqYoyData.setQoqYoyData4(qoq);
			}

			if(cDataMinus2!=null && cDataMinus3!=null && cDataMinus3!=0) {
				if(unit!=null && unit.contains("%")) {
					qoq = ((cDataMinus2-cDataMinus3))*100;
				}else {
					//	qoq = ((cDataMinus2-cDataMinus3)/cDataMinus3)*100;

					qoq = commonService.percentageChange(cDataMinus2, cDataMinus3);
				}
				/*if(!periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY))
					qoqYoyData.setQoqYoyData2(qoq);
				else*/
				qoqYoyData.setQoqYoyData3(qoq);
			}

			if(cDataMinus3!=null && cDataMinus4!=null && cDataMinus4!=0) {
				if(unit!=null && unit.contains("%")) {
					qoq = ((cDataMinus3-cDataMinus4))*100;
				}else {
					//qoq = ((cDataMinus3-cDataMinus4)/cDataMinus4)*100;

					qoq = commonService.percentageChange(cDataMinus3, cDataMinus4);
				}
				/*if(!periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY))
					qoqYoyData.setQoqYoyData1(qoq);
				else*/
				qoqYoyData.setQoqYoyData2(qoq);
			}

			if(cDataMinus4!=null && cDataMinus5!=null && cDataMinus5!=0) {
				if(unit!=null && unit.contains("%")) {
					qoq = ((cDataMinus4-cDataMinus5))*100;
				}else {
					//	qoq = ((cDataMinus4-cDataMinus5)/cDataMinus5)*100;

					qoq = commonService.percentageChange(cDataMinus4, cDataMinus5);
				}
				qoqYoyData.setQoqYoyData1(qoq);
			}

			if(unit!=null){
				if( unit.contains("%")){
					qoqYoyData.setUnit("bps");
				}else{ 
					qoqYoyData.setUnit("%");
				}
			}else{
				qoqYoyData.setUnit("%");
			}


			List<ReportQoqYoyData> dataList= new ArrayList<ReportQoqYoyData>();
			dataList.add(qoqYoyData);
			map.put(monitorType+CMStatic.QOQ_YOY+i,dataList);
			i++;
		}
		List<Object> myList = new ArrayList<>(1);
		myList.add(map);
		_log.trace("getQoqYoyMonitorData:: "+new Gson().toJson(myList));
		return myList;
	}//



	public LinkedHashMap<String,List<ReportQoqYoyData>> getEconomyQoqYoyData(String periodType,Date endDate, Date startDate, Integer countryId, List<Integer> indicatorList) throws Exception{
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfYearly =  new SimpleDateFormat("yyyy");
		List<String> periodList = null;
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			periodList = dateUtil.generatePrevApplicablePeriodQtr(sdf.format(endDate), 6);
		}else {
			periodList = dateUtil.generatePrevApplicablePeriodYear(sdf.format(endDate), 6);
		}

		String cPeriod = "", cminus1Period = "", cminus2Period="", cminus3Period="",cminus4Period="",cminus5Period="";
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			cPeriod = periodList.get(0).split("-")[0];
			cminus1Period = periodList.get(1).split("-")[0];
			cminus2Period = periodList.get(2).split("-")[0];
			cminus3Period = periodList.get(3).split("-")[0];
			cminus4Period = periodList.get(4).split("-")[0];
			cminus5Period = periodList.get(5).split("-")[0];			
		}else {
			cPeriod = periodList.get(0);
			cminus1Period = periodList.get(1);
			cminus2Period = periodList.get(2);
			cminus3Period = periodList.get(3);
			cminus4Period = periodList.get(4);
			cminus5Period = periodList.get(5);
		}

		LinkedHashMap<String, List<ReportQoqYoyData>> map = new LinkedHashMap<String, List<ReportQoqYoyData>>();


		int i=1;
		for(Integer indicatorId : indicatorList) {

			ReportQoqYoyData qoqYoyData = new ReportQoqYoyData();
			Double cData =null; Double cDataMinus1 =null; Double cDataMinus2 = null; Double cDataMinus3 = null; Double cDataMinus4 = null;Double cDataMinus5 = null;
			Double qoq = null;
			String indicatorUnit = null;

			if(indicatorId!=-1) {  //NOT FX DATA
				List<IndicatorDataDTO_old> indicatorDataDTO = null;
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)){
					indicatorDataDTO = economyService.getIndicatorDataByIndicator(countryId,indicatorId, periodType,sdf.parse(cminus5Period),sdf.parse(cPeriod));	
				}else{
					indicatorDataDTO = economyService.getIndicatorDataByIndicator(countryId,indicatorId, periodType,sdf.parse(periodList.get(5)),sdf.parse(periodList.get(0)));
				}


				for(IndicatorDataDTO_old indicatorData : indicatorDataDTO) {
					if(indicatorUnit==null){
						indicatorUnit = indicatorData.getUnit();
					}
					String pe = sdf.format(indicatorData.getPeriod());
					if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
						pe = sdfYearly.format(indicatorData.getPeriod());
					}
					if(pe.equals(cPeriod)) {
						cData = indicatorData.getData();
					}else if(pe.equals(cminus1Period)) {
						cDataMinus1 = indicatorData.getData();
					}else if(pe.equals(cminus2Period)) {
						cDataMinus2 = indicatorData.getData();
					}else if(pe.equals(cminus3Period)) {
						cDataMinus3 = indicatorData.getData();
					}else if(pe.equals(cminus4Period)) {
						cDataMinus4 = indicatorData.getData();
					}else if(pe.equals(cminus5Period)) {
						cDataMinus5 = indicatorData.getData();
					}
				}
			}else {
				//Collections.reverse(periodList);
				String currencyCode = economyService.getCountryCurrency(String.valueOf(countryId)).getCurrencyCode();
				if(currencyCode.equals(CMStatic.CURRENCY_CODE_USD)) {
					currencyCode = CMStatic.CURRENCY_CODE_EURO;
				}
				cData = economyService.getFxData(periodType, sdf.parse(periodList.get(0)), currencyCode);
				cDataMinus1 = economyService.getFxData(periodType, sdf.parse(periodList.get(1)), currencyCode);
				cDataMinus2 = economyService.getFxData(periodType, sdf.parse(periodList.get(2)), currencyCode);
				cDataMinus3 = economyService.getFxData(periodType, sdf.parse(periodList.get(3)), currencyCode);
				cDataMinus4 = economyService.getFxData(periodType, sdf.parse(periodList.get(4)), currencyCode);
				cDataMinus5 = economyService.getFxData(periodType, sdf.parse(periodList.get(5)), currencyCode);
				indicatorUnit = "";
			}

			if(cData!=null && cDataMinus1!=null && cDataMinus1!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cData-cDataMinus1))*100;
				}else {
					//qoq = ((cData-cDataMinus1)/cDataMinus1)*100;
					qoq = commonService.percentageChange(cData, cDataMinus1);
				}
				qoqYoyData.setQoqYoyData5(qoq);
			}

			if(cDataMinus1!=null && cDataMinus2!=null && cDataMinus2!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus1-cDataMinus2))*100;
				}else {
					//qoq = ((cDataMinus1-cDataMinus2)/cDataMinus2)*100;
					qoq = commonService.percentageChange(cDataMinus1, cDataMinus2);
				}
				qoqYoyData.setQoqYoyData4(qoq);
			}

			if(cDataMinus2!=null && cDataMinus3!=null && cDataMinus3!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus2-cDataMinus3))*100;
				}else {
					//qoq = ((cDataMinus2-cDataMinus3)/cDataMinus3)*100;
					qoq = commonService.percentageChange(cDataMinus2, cDataMinus3);
				}
				qoqYoyData.setQoqYoyData3(qoq);
			}

			if(cDataMinus3!=null && cDataMinus4!=null && cDataMinus4!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus3-cDataMinus4))*100;
				}else {
					//qoq = ((cDataMinus3-cDataMinus4)/cDataMinus4)*100;
					qoq = commonService.percentageChange(cDataMinus3, cDataMinus4);
				}
				qoqYoyData.setQoqYoyData2(qoq);
			}

			if(cDataMinus4!=null && cDataMinus5!=null && cDataMinus5!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus4-cDataMinus5))*100;
				}else {
					//qoq = ((cDataMinus4-cDataMinus5)/cDataMinus5)*100;
					qoq = commonService.percentageChange(cDataMinus4, cDataMinus5);
				}
				qoqYoyData.setQoqYoyData1(qoq);
			}

			if(indicatorUnit!=null){
				if( indicatorUnit.contains("%")){
					qoqYoyData.setUnit("bps");
				}else{ 
					qoqYoyData.setUnit("%");
				}
			}else {
				qoqYoyData.setUnit("%");
			}

			List<ReportQoqYoyData> dataList = new ArrayList<ReportQoqYoyData>();
			dataList.add(qoqYoyData);
			map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.QOQ_YOY+i,dataList);
			i++;
		}

		_log.trace("getQoqYoyMonitorData:: "+map);
		return map;
	}




	public LinkedHashMap<String,List<ReportQoqYoyData>> getEconomyQoqYoyDataTradingeconomics(String periodType,Date endDate, Date startDate, Integer countryId, List<String> indicatorList , String currency) throws Exception{
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfYearly =  new SimpleDateFormat("yyyy");

		LinkedHashMap<String, List<ReportQoqYoyData>> map = new LinkedHashMap<String, List<ReportQoqYoyData>>();
		String requestedDataFormat = periodType;
		List<LocalDate> dateList = null;
		LinkedHashMap<String, Object> mappedData = new LinkedHashMap<>();

		List<CountryListDTO> countryListTemp = null;
		List<String> countryList = new ArrayList<>();

		String countryName = "";	
		String countryIsoCode = "";	

		org.joda.time.LocalDate localDateRequest = new org.joda.time.LocalDate(endDate);
		String requestPeriod = sdf.format(endDate);
		String[] periodArr = requestPeriod.split("-");
		if(countryId!=null){
			if(countryListTemp==null){
				countryList.add(""+countryId);
				countryListTemp = economyService.getCMCountriesByIdList(countryList);
				if(countryListTemp!=null && !countryListTemp.isEmpty()){
					countryName = 	countryListTemp.get(0).getCountryName();	
					countryIsoCode = 	countryListTemp.get(0).getCountryIsoCode3();	


					Integer month = null;
					String freq = null;

					List<Object> categoryData = imService.getBasePeriodMonth(countryIsoCode, periodType);
					if(categoryData!=null){
						freq = (String)categoryData.get(1);
						if(freq!=null){
							if(freq.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY) || freq.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY) || freq.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
								month = Integer.parseInt(periodArr[1]);	
							}else{
								month = (Integer)categoryData.get(0);
							}
						}else{
							month = (Integer)categoryData.get(0);
							if(month==null){
								month = 03;
							}
						}
					}

					if(month!=null){
						String date = periodArr[0]+"-"+month+"-"+"01";
						org.joda.time.LocalDate localDate = new org.joda.time.LocalDate(date);
						localDate = localDate.dayOfMonth().withMaximumValue();
						if(localDate.isAfter(localDateRequest)){
							if("yearly".equalsIgnoreCase(requestedDataFormat)){
								dateList = imService.getPeriod(requestedDataFormat, true, localDate.minusYears(1), 6, false);
							}
							if("quarterly".equalsIgnoreCase(requestedDataFormat)){
								//	dateList = imService.getPeriod(requestedDataFormat, true, localDate.minusMonths(3), 6, false);

								dateList = imService.getPeriod(requestedDataFormat, true, localDate.minusYears(1), 6, false);							}
						}else if(localDate.isBefore(localDateRequest) || localDate.isEqual(localDateRequest)){
							dateList = imService.getPeriod(requestedDataFormat, true, localDate, 6, false);
						}else {
							return null;
						}
					}
				}
			}
		}




		List<String> periodList = new ArrayList<>();
		for (LocalDate localDate : dateList) {
			String formattedDate = localDate.toString("yyyy-MM-dd");
			periodList.add(formattedDate);
		}


		String cPeriod = "", cminus1Period = "", cminus2Period="", cminus3Period="",cminus4Period="",cminus5Period="";
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			cPeriod = periodList.get(0).split("-")[0];
			cminus1Period = periodList.get(1).split("-")[0];
			cminus2Period = periodList.get(2).split("-")[0];
			cminus3Period = periodList.get(3).split("-")[0];
			cminus4Period = periodList.get(4).split("-")[0];
			cminus5Period = periodList.get(5).split("-")[0];			
		}else {
			cPeriod = periodList.get(0);
			cminus1Period = periodList.get(1);
			cminus2Period = periodList.get(2);
			cminus3Period = periodList.get(3);
			cminus4Period = periodList.get(4);
			cminus5Period = periodList.get(5);
		}


		int i=1;
		for(String indicatorId : indicatorList) {

			ReportQoqYoyData qoqYoyData = new ReportQoqYoyData();
			Double cData =null; Double cDataMinus1 =null; Double cDataMinus2 = null; Double cDataMinus3 = null; Double cDataMinus4 = null;Double cDataMinus5 = null;
			Double qoq = null;
			String indicatorUnit = null;
			String indicatorName = indicatorId;
			if(!indicatorId.equals("fx")) {  //NOT FX DATA
				////////////////////////////////////////////////////////

				if(CMStatic.GDP.equalsIgnoreCase(indicatorName) && countryName.equalsIgnoreCase("China")){
					mappedData = chinaIssue(countryId, periodType, dateList, indicatorName, currency, requestedDataFormat,"POP");
				}else{
					mappedData =	imService.getEconomicalDataForIM( countryId, periodType, dateList, indicatorName, currency, requestedDataFormat);
				}

				//mappedData = imService.getEconomicalDataForIM(countryId, periodType, dateList, indicatorName,currency, requestedDataFormat);
				/////////////////////////////////////////////////////////////

				/*for(IndicatorDataDTO_old indicatorData : indicatorDataDTO) {*/

				///////////////////////////////////////////////
				for (LocalDate myDate : dateList) {
					ReportPerformanceData em = new ReportPerformanceData();
					em.setPeriod(sdf.format(myDate.toDate()));
					if(indicatorName.equalsIgnoreCase(CMStatic.GDP)){
						em.setSeries(CMStatic.GDP_DISPLAY_NAME);
					}else{
						em.setSeries(indicatorName);
					}
					String formattedDate = myDate.toString("yyyy-MM-dd");
					@SuppressWarnings("unchecked")
					List<IndicatorHistoricalDataDTO> data = (List<IndicatorHistoricalDataDTO>)mappedData.get(formattedDate);

					String frequency = (String)mappedData.get(CMStatic.FREQUENCY);
					if(data!=null && !data.isEmpty()){
						IndicatorHistoricalDataDTO dataDTO = data.get(0);
						if(indicatorUnit==null){
							indicatorUnit = dataDTO.getUnit();
							if(indicatorName.equalsIgnoreCase(CMStatic.CPI)){
								indicatorUnit = "";
							}
							if(indicatorName.equalsIgnoreCase(CMStatic.INTR_RATE)){
								indicatorUnit = "%";
							}
						}

						if(indicatorName.equalsIgnoreCase(CMStatic.EXPORTS)){
							// Accumulate the data if the export is there 
							// as per the requested frequency , make the data to that frequency 

							if(requestedDataFormat.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
								Double tempData = 0d;
								for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
									tempData = tempData + indicatorHistoricalDataDTO.getData();
								}
								String pe = sdf.format(myDate.toDate());
								if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
									pe = sdfYearly.format(myDate.toDate());
								}
								if(pe.equals(cPeriod)) {
									cData = tempData;
								}else if(pe.equals(cminus1Period)) {
									cDataMinus1 = tempData;
								}else if(pe.equals(cminus2Period)) {
									cDataMinus2 = tempData;
								}else if(pe.equals(cminus3Period)) {
									cDataMinus3 = tempData;
								}else if(pe.equals(cminus4Period)) {
									cDataMinus4 = tempData;
								}else if(pe.equals(cminus5Period)) {
									cDataMinus5 = tempData;
								}

							}

							if(requestedDataFormat.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
								if(imService.canDataBeUse(requestedDataFormat, frequency)){
									Double tempData = 0d;
									for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
										tempData = tempData + indicatorHistoricalDataDTO.getData();
									}

									String pe = sdf.format(myDate.toDate());
									if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
										pe = sdfYearly.format(myDate.toDate());
									}
									if(pe.equals(cPeriod)) {
										cData = tempData;
									}else if(pe.equals(cminus1Period)) {
										cDataMinus1 = tempData;
									}else if(pe.equals(cminus2Period)) {
										cDataMinus2 = tempData;
									}else if(pe.equals(cminus3Period)) {
										cDataMinus3 = tempData;
									}else if(pe.equals(cminus4Period)) {
										cDataMinus4 = tempData;
									}else if(pe.equals(cminus5Period)) {
										cDataMinus5 = tempData;
									}
								}else{
									_log.info("The frequency is mismatched");
								}
							}
						}else if(indicatorName.equalsIgnoreCase(CMStatic.GDP)){
							List<TelevisoryIndicatorReportingFrequencyDTO>  tvIRFDList = economyRepository.getReportedFrequencyBasedOnCountryAndCategory(countryName,countryIsoCode	,indicatorName);
							Double tempData = null;
							if(tvIRFDList!=null && !tvIRFDList.isEmpty()){
								TelevisoryIndicatorReportingFrequencyDTO tvIRFD = tvIRFDList.get(0);
								if(requestedDataFormat.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
									if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.ANNUAL_REPORTED)){
										tempData=  dataDTO.getData();
									}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.QUARTER_REPORTED)){
										if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
											tempData =  null;
										}else if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
											tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
												Double tdata = indicatorHistoricalDataDTO.getData();
												if(tdata!=null){
													tempData = tempData + tdata;
												}
											}
										}else if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
											tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
												Double tdata = indicatorHistoricalDataDTO.getData();
												if(tdata!=null){
													tempData = tempData + tdata;
												}
											}
										}else if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
											tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
												Double tdata = indicatorHistoricalDataDTO.getData();
												if(tdata!=null){
													tempData = tempData + tdata;
												}
											}
										}
									}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.MONTHLY_REPORTED)){
										if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
											tempData = null;
										}

										if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
											tempData = null;
										}

										if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
											tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
												Double tdata = indicatorHistoricalDataDTO.getData();
												if(tdata!=null){
													tempData = tempData + tdata;
												}
											}
										}

										if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
											tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
												Double tdata = indicatorHistoricalDataDTO.getData();
												if(tdata!=null){
													tempData = tempData + tdata;
												}
											}
										}
									}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.WEEKLY_REPORTED)){
										if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
											tempData = null;
										}

										if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
											tempData = null;
										}

										if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
											tempData = null;
										}

										if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
											tempData = 0d;
											for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
												Double tdata = indicatorHistoricalDataDTO.getData();
												if(tdata!=null){
													tempData = tempData + tdata;
												}
											}
										}
									}
								}else if(requestedDataFormat.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
									if(imService.canDataBeUse(requestedDataFormat, frequency)){
										if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.ANNUAL_REPORTED)){
											if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
												if(dataDTO.getData()!=null && dataDTO.getData()!=0){
													tempData = dataDTO.getData()/4;
												}
											}

											if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
												if(dataDTO.getData()!=null && dataDTO.getData()!=0){
													tempData = dataDTO.getData()/4;
												}
											}

											if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
												if(dataDTO.getData()!=null && dataDTO.getData()!=0){
													tempData = dataDTO.getData()/4;
												}
											}

											if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
												if(dataDTO.getData()!=null && dataDTO.getData()!=0){
													tempData = dataDTO.getData()/4;
												}
											}

										}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.QUARTER_REPORTED)){

											if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
												tempData = null;
											}

											if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
												tempData = dataDTO.getData();
											}

											if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
												tempData = 0d;
												for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
													Double tdata = indicatorHistoricalDataDTO.getData();
													if(tdata!=null){
														tempData = tempData + tdata;
													}
												}
											}

											if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
												tempData = 0d;
												for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
													Double tdata = indicatorHistoricalDataDTO.getData();
													if(tdata!=null){
														tempData = tempData + tdata;
													}
												}
											}

										}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.MONTHLY_REPORTED)){
											if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
												tempData = null;
											}

											if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
												tempData = null;
											}

											if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
												tempData = dataDTO.getData();
											}

											if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
												tempData = 0d;
												for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
													Double tdata = indicatorHistoricalDataDTO.getData();
													if(tdata!=null){
														tempData = tempData + tdata;
													}
												}
											}

										}else if(tvIRFD.getReportedFrequency().equalsIgnoreCase(CMStatic.WEEKLY_REPORTED)){
											if(frequency.equalsIgnoreCase(CMStatic.YEARLY_FREQUENCY)){
												tempData = null;
											}

											if(frequency.equalsIgnoreCase(CMStatic.QUARTERLY_FREQUENCY)){
												tempData = null;
											}

											if(frequency.equalsIgnoreCase(CMStatic.MONTHLY_FREQUENCY)){
												tempData = null;
											}

											if(frequency.equalsIgnoreCase(CMStatic.WEEKLY_FREQUENCY)){
												tempData = dataDTO.getData();
											}

										}
									}else{


										String pe = sdf.format(myDate.toDate());
										if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
											pe = sdfYearly.format(myDate.toDate());
										}
										if(pe.equals(cPeriod)) {
											cData = tempData;
										}else if(pe.equals(cminus1Period)) {
											cDataMinus1 = tempData;
										}else if(pe.equals(cminus2Period)) {
											cDataMinus2 = tempData;
										}else if(pe.equals(cminus3Period)) {
											cDataMinus3 = tempData;
										}else if(pe.equals(cminus4Period)) {
											cDataMinus4 = tempData;
										}else if(pe.equals(cminus5Period)) {
											cDataMinus5 = tempData;
										}
									}
								}
								String pe = sdf.format(myDate.toDate());
								if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
									pe = sdfYearly.format(myDate.toDate());
								}
								if(pe.equals(cPeriod)) {
									cData = tempData;
								}else if(pe.equals(cminus1Period)) {
									cDataMinus1 = tempData;
								}else if(pe.equals(cminus2Period)) {
									cDataMinus2 = tempData;
								}else if(pe.equals(cminus3Period)) {
									cDataMinus3 = tempData;
								}else if(pe.equals(cminus4Period)) {
									cDataMinus4 = tempData;
								}else if(pe.equals(cminus5Period)) {
									cDataMinus5 = tempData;
								}
							}else{
								String pe = sdf.format(myDate.toDate());
								if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
									pe = sdfYearly.format(myDate.toDate());
								}
								if(pe.equals(cPeriod)) {
									cData = dataDTO.getData();
								}else if(pe.equals(cminus1Period)) {
									cDataMinus1 = dataDTO.getData();
								}else if(pe.equals(cminus2Period)) {
									cDataMinus2 = dataDTO.getData();
								}else if(pe.equals(cminus3Period)) {
									cDataMinus3 = dataDTO.getData();
								}else if(pe.equals(cminus4Period)) {
									cDataMinus4 = dataDTO.getData();
								}else if(pe.equals(cminus5Period)) {
									cDataMinus5 = dataDTO.getData();
								}
							}
						}else{
							String pe = sdf.format(myDate.toDate());
							if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)){
								pe = sdfYearly.format(myDate.toDate());
							}
							if(pe.equals(cPeriod)) {
								cData = dataDTO.getData();
							}else if(pe.equals(cminus1Period)) {
								cDataMinus1 = dataDTO.getData();
							}else if(pe.equals(cminus2Period)) {
								cDataMinus2 = dataDTO.getData();
							}else if(pe.equals(cminus3Period)) {
								cDataMinus3 = dataDTO.getData();
							}else if(pe.equals(cminus4Period)) {
								cDataMinus4 = dataDTO.getData();
							}else if(pe.equals(cminus5Period)) {
								cDataMinus5 = dataDTO.getData();
							}
						}
					}

				}
			}else {

				String countryCurrencyCode = economyService.getCountryCurrency(String.valueOf(countryId)).getCurrencyCode();
				String currencyCode = currency;

				String cc1 = currencyCode;
				String cc2 = countryCurrencyCode;

				if(countryCurrencyCode.equals(currencyCode)){
					cc1 = "USD";
					cc2 = countryCurrencyCode;
				}

				/*if(currencyCode.equals("USD")){
						cc1 = "USD";
						cc2 = countryCurrencyCode;
					}
				 */

				if(cc1.equals(CMStatic.CURRENCY_CODE_USD) && cc2.equals(CMStatic.CURRENCY_CODE_USD)) {
					cc2 = CMStatic.CURRENCY_CODE_EURO;
				}
				/*cData = economyService.getFxData(periodType, sdf.parse(periodList.get(0)), currencyCode);
				cDataMinus1 = economyService.getFxData(periodType, sdf.parse(periodList.get(1)), currencyCode);
				cDataMinus2 = economyService.getFxData(periodType, sdf.parse(periodList.get(2)), currencyCode);
				cDataMinus3 = economyService.getFxData(periodType, sdf.parse(periodList.get(3)), currencyCode);
				cDataMinus4 = economyService.getFxData(periodType, sdf.parse(periodList.get(4)), currencyCode);
				cDataMinus5 = economyService.getFxData(periodType, sdf.parse(periodList.get(5)), currencyCode);*/
				cData = economyService.getDailyFxData(periodType, sdf.parse(periodList.get(0)), cc1, cc2);
				cDataMinus1 = economyService.getDailyFxData(periodType, sdf.parse(periodList.get(1)), cc1, cc2);
				cDataMinus2 = economyService.getDailyFxData(periodType, sdf.parse(periodList.get(2)), cc1, cc2);
				cDataMinus3 = economyService.getDailyFxData(periodType, sdf.parse(periodList.get(3)), cc1, cc2);
				cDataMinus4 = economyService.getDailyFxData(periodType, sdf.parse(periodList.get(4)), cc1, cc2);
				cDataMinus5 = economyService.getDailyFxData(periodType, sdf.parse(periodList.get(5)), cc1, cc2);


				indicatorUnit = "";
			}


			if(cData!=null && cDataMinus1!=null && cDataMinus1!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cData-cDataMinus1))*100;
				}else {
					//qoq = ((cData-cDataMinus1)/cDataMinus1)*100;
					qoq = commonService.percentageChange(cData, cDataMinus1);

				}
				qoqYoyData.setQoqYoyData5(qoq);
			}

			if(cDataMinus1!=null && cDataMinus2!=null && cDataMinus2!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus1-cDataMinus2))*100;
				}else {
					//qoq = ((cDataMinus1-cDataMinus2)/cDataMinus2)*100;
					qoq = commonService.percentageChange(cDataMinus1, cDataMinus2);
				}
				qoqYoyData.setQoqYoyData4(qoq);
			}

			if(cDataMinus2!=null && cDataMinus3!=null && cDataMinus3!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus2-cDataMinus3))*100;
				}else {
					//qoq = ((cDataMinus2-cDataMinus3)/cDataMinus3)*100;
					qoq = commonService.percentageChange(cDataMinus2, cDataMinus3);
				}
				qoqYoyData.setQoqYoyData3(qoq);
			}

			if(cDataMinus3!=null && cDataMinus4!=null && cDataMinus4!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus3-cDataMinus4))*100;
				}else {
					//qoq = ((cDataMinus3-cDataMinus4)/cDataMinus4)*100;
					qoq = commonService.percentageChange(cDataMinus3, cDataMinus4);
				}
				qoqYoyData.setQoqYoyData2(qoq);
			}

			if(cDataMinus4!=null && cDataMinus5!=null && cDataMinus5!=0) {
				if(indicatorUnit!=null && indicatorUnit.contains("%")) {
					qoq = ((cDataMinus4-cDataMinus5))*100;
				}else {
					//qoq = ((cDataMinus4-cDataMinus5)/cDataMinus5)*100;
					qoq = commonService.percentageChange(cDataMinus4, cDataMinus5);
				}
				qoqYoyData.setQoqYoyData1(qoq);
			}

			if(indicatorUnit!=null){
				if( indicatorUnit.contains("%")){
					qoqYoyData.setUnit("bps");
				}else{ 
					qoqYoyData.setUnit("%");
				}
			}else {
				qoqYoyData.setUnit("%");
			}

			List<ReportQoqYoyData> dataList = new ArrayList<ReportQoqYoyData>();
			dataList.add(qoqYoyData);
			map.put(CMStatic.REPORT_MONITOR_ECONOMY+CMStatic.QOQ_YOY+i,dataList);
			i++;
		}

		_log.trace("getQoqYoyMonitorData:: "+map);
		return map;
	}




	public List<ReportPerformanceData> getQuartileData(String fieldName, String periodType, String ticsSectorCode,String ticsIndustryCode, Date startDate ,Date endDate, String countryId, String reqCurrency){
		List<ReportPerformanceData> quartileList = new ArrayList<ReportPerformanceData>();

		//List<IndustryFinancialDataDTO> companyData = industryService.getIndustryCompanyData(fieldName, periodType, startDate, endDate, ticsSectorCode, ticsIndustryCode, countryCode);
		Integer month = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		if(endDate!=null){
			month = Integer.parseInt(sdfMonth.format(endDate));
		}
		Integer companyFlag = null;
		List<IndustryFinancialDataDTO> companyData = industryService.getIDCompanyData(periodType, ticsIndustryCode, null, countryId, fieldName, startDate, endDate, reqCurrency, month,companyFlag);

		//Sort the list by data order DESC (null values at last)
		companyData.sort(Comparator.comparing(IndustryFinancialDataDTO::getData, Comparator.nullsFirst(Comparator.naturalOrder())).reversed());

		Double maxData = 0.0;
		Integer q1Counter = 0, q2Counter = 0, q3Counter = 0, q4Counter=0;
		if(companyData!=null) {
			for(int i=0; i<companyData.size(); i++) {
				if(companyData.get(i)!=null){
					if(companyData.get(i).getData()!=null){
						Double rank = null;
						if(i==0) {
							maxData = companyData.get(i).getData();
							rank = 1.0;
						}else {
							rank = companyData.get(i).getData()/maxData;
						}
						if(rank>0.75){
							q1Counter++;
						}else if(rank<=0.75 && rank >0.5) {
							q2Counter++;
						}else if(rank<=0.5 && rank >0.25){
							q3Counter++;
						}else if(rank<=0.25){
							q4Counter++;
						}
					}
				}
			}
		}
		ReportPerformanceData quartileData = new ReportPerformanceData();
		quartileData.setSeries(CMStatic.QUADRANT_FIRST);
		quartileData.setData(q1Counter.doubleValue());
		quartileList.add(quartileData);

		quartileData = new ReportPerformanceData();
		quartileData.setSeries(CMStatic.QUADRANT_SECOND);
		quartileData.setData(q2Counter.doubleValue());
		quartileList.add(quartileData);

		quartileData = new ReportPerformanceData();
		quartileData.setSeries(CMStatic.QUADRANT_THIRD);
		quartileData.setData(q3Counter.doubleValue());
		quartileList.add(quartileData);

		quartileData = new ReportPerformanceData();
		quartileData.setSeries(CMStatic.QUADRANT_FOURTH);
		quartileData.setData(q4Counter.doubleValue());
		quartileList.add(quartileData);

		return quartileList;
	}


	@SuppressWarnings({ "unchecked", "rawtypes"})
	public boolean generateIndustryMonitor(ReportIndustryMonitor industryMonitor,String mainJasperFile,String pdfFileName, String imagePath) {
		boolean status = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Map parametersList = new HashMap<>();
			HashMap<String, String> periodDetails = new HashMap<>();
			periodDetails.put("03", "Mar");
			periodDetails.put("06", "Jun");
			periodDetails.put("09", "Sep");
			periodDetails.put("12", "Dec");
			String period[] = sdf.format(industryMonitor.getPeriod()).split("-");
			parametersList.put("periodType", industryMonitor.getPeriodType());
			parametersList.put("periodYear", period[0]);
			parametersList.put("industryType", industryMonitor.getIndustryType());
			parametersList.put("periodMonth", periodDetails.get(period[1]));
			parametersList.put("period", sdf.format(industryMonitor.getPeriod()));
			Integer companyCoverageCount = 0;

			if(industryMonitor.getGrowthMonitorsQoQ()==null){
				if(industryMonitor.getGrowthMonitorsYoY()!=null){
					for (ReportMonitorData reportMonitorData : industryMonitor.getGrowthMonitorsYoY()) {
						if(!reportMonitorData.getCategoryName().contains(CMStatic.TRIPLE_STAR)){
							companyCoverageCount++;
						}
					}
				}
			}else{
				for (ReportMonitorData reportMonitorData : industryMonitor.getGrowthMonitorsQoQ()) {
					if(!reportMonitorData.getCategoryName().contains(CMStatic.TRIPLE_STAR)){
						companyCoverageCount++;
					}
				}
			}

			/*if(fieldName.equals(CMStatic.REPORT_IM_FIELD_COMP_COUNT)) {
			metaData.setCompanyCoverage(industryData.getData().intValue());
		}*/

			List<ReportIndustryMetaData>  industryMetaData = industryMonitor.getIndustryMetaData();
			if(industryMetaData!=null && !industryMetaData.isEmpty()){
				industryMetaData.get(0).setCompanyCoverage(companyCoverageCount);
				parametersList.put("metaData", new JRBeanCollectionDataSource(industryMetaData));
			}
			if(industryMonitor.getIndustryMetaData()!=null){
				parametersList.put("metaData1", industryMonitor.getIndustryMetaData());
			}

			parametersList.put("upArrow",imagePath +"/"+ CMStatic.IMAGE_UP_TREND);
			parametersList.put("downArrow",imagePath +"/"+ CMStatic.IMAGE_DOWN_TREND);
			parametersList.put("noTrend",imagePath +"/"+ CMStatic.IMAGE_NO_TREND);
			parametersList.put("noTrendInfo",imagePath +"/"+ CMStatic.IMAGE_NO_TREND_INFO);

			if(industryMonitor.getEconomyQoqYoy()!=null){
				parametersList.putAll(industryMonitor.getEconomyQoqYoy());
			}
			if(industryMonitor.getFundamentalQoqYoy()!=null){
				parametersList.putAll(industryMonitor.getFundamentalQoqYoy());
			}
			if(industryMonitor.getValuationQoqYoy()!=null){
				parametersList.putAll(industryMonitor.getValuationQoqYoy());
			}
			if(industryMonitor.getCountryId()!=null) {
				if(industryMonitor.getEconomyMonitors()!=null){
					parametersList.putAll(industryMonitor.getEconomyMonitors());
				}
			}
			parametersList.put("countryId",industryMonitor.getCountryId());

			if(industryMonitor.getValuationMonitors()!=null){
				parametersList.putAll(industryMonitor.getValuationMonitors());
			}

			if(industryMonitor.getFundamentalMonitors()!=null){
				parametersList.putAll(industryMonitor.getFundamentalMonitors());
			}

			if(industryMonitor.getIndustryToppersList()!=null){
				parametersList.putAll(industryMonitor.getIndustryToppersList());
			}
			parametersList.put("imageLogoPath", imagePath +"/"+ CMStatic.IMAGE_LOGO);
			if(industryMonitor.getRegionalMonitors()!=null){
				parametersList.put("monitorTable1", new JRBeanCollectionDataSource(industryMonitor.getRegionalMonitors()));
			    parametersList.put("regionalMonitorsDoubleStarMsg", industryMonitor.getRegionalMonitorsDoubleStarMsg());
			    parametersList.put("regionalMonitorsTripleStarMsg", industryMonitor.getRegionalMonitorsTripleStarMsg());
			}
			if(industryMonitor.getGrowthMonitorsQoQ()!=null){
				parametersList.put("monitorTable2", new JRBeanCollectionDataSource(industryMonitor.getGrowthMonitorsQoQ())); 
				parametersList.put("growthMonitorsQoQDoubleStarMsg", industryMonitor.getGrowthMonitorsQoQDoubleStarMsg());
			    parametersList.put("growthMonitorsQoQTripleStarMsg", industryMonitor.getGrowthMonitorsQoQTripleStarMsg());
			}
			if(industryMonitor.getGrowthMonitorsYoY()!=null){
				parametersList.put("monitorTable3", new JRBeanCollectionDataSource(industryMonitor.getGrowthMonitorsYoY()));
				parametersList.put("growthMonitorsYoYDoubleStarMsg", industryMonitor.getGrowthMonitorsYoYDoubleStarMsg());
			    parametersList.put("growthMonitorsYoYTripleStarMsg", industryMonitor.getGrowthMonitorsYoYTripleStarMsg());
			}
			status = generatePdf( mainJasperFile, pdfFileName, parametersList);
		} catch(Exception e) {
			_log.error("Error in Jasper report generation: ", e);
		}

		return status;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean generatePdf(String mainJasperFile,String pdfFileName,Map parametersList){
		boolean check = false;
		try{
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainJasperFile, parametersList, new JREmptyDataSource());
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFileName);
			check = true;
		} catch (Exception e) {
			_log.error("Error in Jasper report generation: ", e);
		}
		return check;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean generateHtml(String mainJasperFile,String pdfFileName,Map parametersList){
		boolean check = false;
		try{
			_log.info("Generating the HTML");
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainJasperFile, parametersList, new JREmptyDataSource());
			JasperExportManager.exportReportToHtmlFile(jasperPrint, pdfFileName);
			check = true;
			_log.info("HTML report generation status ::: " + check);
		} catch (Exception e) {
			_log.error("Error in Jasper report generation: ", e);
		}
		return check;

	}




	public List<ReportShareholding> getShareholdingsData(String companyId){
		SimpleDateFormat sdfY = new SimpleDateFormat("MMM-yyyy");
		FFBasicCfDTO basicCfDTO = cmStockRepository.getFFBasicCfDTO(companyId);

		Double shareFreeFloat = basicCfDTO.getShareFreeFloat();
		Double shareOutStanding = basicCfDTO.getShareOutStanding();
		Double freeFloat = 0.0, closelyHeld = 0.0;
		if(shareFreeFloat==null || shareFreeFloat==0.0 || shareOutStanding==null || shareOutStanding==0.0) {
			freeFloat = 0.0;
		}else {
			freeFloat = (shareFreeFloat/shareOutStanding)*100;
		}
		closelyHeld = CMStatic.UNIT_FACTOR_HUNDRED-freeFloat;

		List<ReportShareholding> shareHoldings = new ArrayList<ReportShareholding>();
		ReportShareholding sh = new ReportShareholding();
		sh.setData(closelyHeld);
		sh.setCompanyName(CMStatic.SHAREHOLDING);
		sh.setText(CMStatic.CLOSELY_HELD);
		if(basicCfDTO.getShareFreeFloatDate()!=null) {
			sh.setPeriod(sdfY.format(basicCfDTO.getShareFreeFloatDate()));
		}else {
			sh.setPeriod(sdfY.format(basicCfDTO.getShareOutStandingDate()));
		}
		shareHoldings.add(sh);

		sh = new ReportShareholding();
		sh.setData(freeFloat);
		sh.setCompanyName(CMStatic.SHAREHOLDING);
		sh.setText(CMStatic.FREE_FLOAT);
		if(basicCfDTO.getShareFreeFloatDate()!=null) {
			sh.setPeriod(sdfY.format(basicCfDTO.getShareFreeFloatDate()));
		}else {
			sh.setPeriod(sdfY.format(basicCfDTO.getShareOutStandingDate()));
		}
		shareHoldings.add(sh);
		return shareHoldings;
	}


	public List<ReportHistPerformance> getRiskRatioData(String companyId,String currency) throws Exception{
		List<String> compIndexList = new ArrayList<String>();
		List<IndexDTO> indexList = capitalMarketService.getCompanyIndexList(companyId);
		if(indexList!=null && indexList.size()!=0)
			compIndexList.add(String.valueOf(indexList.get(0).getId()));

		List<ReportHistPerformance> riskRatios = new ArrayList<ReportHistPerformance>();
		for(int i=0;i<2;i++) {
			String periodType = CMStatic.DAILY;
			if(i==1) {
				periodType = CMStatic.WEEKLY;
			}
			List<BetaData> dailyBetaDatas = cmStockService.getCompanyBeta(companyId, periodType, compIndexList,currency);
			if(dailyBetaDatas!=null && !dailyBetaDatas.isEmpty()) {
				for(BetaData betaData: dailyBetaDatas) {
					if(betaData.getData()!=null && !betaData.getData().isEmpty()) {
						ReportHistPerformance risk = new ReportHistPerformance();
						risk.setCompanyName(CMStatic.BETA+" ("+periodType+")");
						risk.setOneYearData(betaData.getData().get(0));
						if(betaData.getData().size()>1)
							risk.setTwoYearData(betaData.getData().get(1));
						if(betaData.getData().size()>2)
							risk.setThreeYearData(betaData.getData().get(2));
						riskRatios.add(risk);
					}
				}
			}
		}
		return riskRatios;
	}

	public List<MySlot> getFilteredData(List<ReportRatiosData> myList , Integer templateType){
		try{
			List<MySlot> processedList = new ArrayList<>();
			for (ReportRatiosData reportRatiosData : myList) {
				MySlot m = new MySlot();
				if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_QUARTER){

					m.setCurrency(reportRatiosData.getCurrency());
					m.setUnit(reportRatiosData.getUnit());
					m.setFieldName(reportRatiosData.getFieldName());
					m.setItemName(reportRatiosData.getItemName());

					m.setS1(reportRatiosData.getCqPeriod());
					m.setS1Unit(reportRatiosData.getUnit());
					m.setS1Data(reportRatiosData.getCqData());

					m.setS2(reportRatiosData.getCqPeriodMinus1());
					m.setS2Unit(reportRatiosData.getUnit());
					m.setS2Data(reportRatiosData.getCqDataMinus1());

					m.setS3(reportRatiosData.getCqPeriodMinus2());
					m.setS3Unit(reportRatiosData.getUnit());
					m.setS3Data(reportRatiosData.getCqDataMinus2());

					m.setS4(reportRatiosData.getCqPeriodMinus3());
					m.setS4Unit(reportRatiosData.getUnit());
					m.setS4Data(reportRatiosData.getCqDataMinus3());

					m.setS5(reportRatiosData.getCqPeriodMinus4());
					m.setS5Unit(reportRatiosData.getUnit());
					m.setS5Data(reportRatiosData.getCqDataMinus4());

					m.setS6("QoQ Change");
					m.setS6Unit(reportRatiosData.getQoqyoyUnit());
					m.setS6Data(reportRatiosData.getQoq());
					m.setS6Append("APPEND");

					m.setS7("YoY Change");
					m.setS7Unit(reportRatiosData.getQoqyoyUnit());
					m.setS7Data(reportRatiosData.getYoy());
					m.setS7Append("APPEND");

				}

				if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_YEAR){
					m.setCurrency(reportRatiosData.getCurrency());
					m.setUnit(reportRatiosData.getUnit());
					m.setFieldName(reportRatiosData.getFieldName());
					m.setItemName(reportRatiosData.getItemName());

					m.setS1(CMStatic.REPORT_PREFIX_YEAR_FY+reportRatiosData.getCyPeriod().substring(reportRatiosData.getCyPeriod().length()-2, reportRatiosData.getCyPeriod().length()));
					m.setS1Unit(reportRatiosData.getUnit());
					m.setS1Data(reportRatiosData.getCyData());

					m.setS2(CMStatic.REPORT_PREFIX_YEAR_FY+reportRatiosData.getCyPeriodMinus1().substring(reportRatiosData.getCyPeriodMinus1().length()-2, reportRatiosData.getCyPeriodMinus1().length()));
					m.setS2Unit(reportRatiosData.getUnit());
					m.setS2Data(reportRatiosData.getCyDataMinus1());

					m.setS3("YoY Change");
					m.setS3Unit(reportRatiosData.getQoqyoyUnit());
					m.setS3Data(reportRatiosData.getYoy());
					m.setS3Append("APPEND");

					m.setS4(CMStatic.REPORT_PREFIX_YEAR_FY+reportRatiosData.getCyPeriodMinus2().substring(reportRatiosData.getCyPeriodMinus2().length()-2, reportRatiosData.getCyPeriodMinus2().length()));
					m.setS4Unit(reportRatiosData.getUnit());
					m.setS4Data(reportRatiosData.getCyDataMinus2());

					m.setS5(CMStatic.REPORT_PREFIX_YEAR_FY+reportRatiosData.getCyPeriodMinus3().substring(reportRatiosData.getCyPeriodMinus3().length()-2, reportRatiosData.getCyPeriodMinus3().length()));
					m.setS5Unit(reportRatiosData.getUnit());
					m.setS5Data(reportRatiosData.getCyDataMinus3());

					m.setS6(CMStatic.REPORT_PREFIX_YEAR_FY+reportRatiosData.getCyPeriodMinus4().substring(reportRatiosData.getCyPeriodMinus4().length()-2, reportRatiosData.getCyPeriodMinus4().length()));
					m.setS6Unit(reportRatiosData.getUnit());
					m.setS6Data(reportRatiosData.getCyDataMinus4());

					m.setS7(CMStatic.REPORT_PREFIX_YEAR_FY+reportRatiosData.getCyPeriodMinus5().substring(reportRatiosData.getCyPeriodMinus5().length()-2, reportRatiosData.getCyPeriodMinus5().length()));
					m.setS7Unit(reportRatiosData.getUnit());
					m.setS7Data(reportRatiosData.getCyDataMinus5());

				}

				if(templateType==CMStatic.REPORT_TEMPLATE_TYPE_BOTH){

					m.setCurrency(reportRatiosData.getCurrency());
					m.setUnit(reportRatiosData.getUnit());
					m.setFieldName(reportRatiosData.getFieldName());
					m.setItemName(reportRatiosData.getItemName());

					m.setS1(reportRatiosData.getCqPeriod());
					m.setS1Unit(reportRatiosData.getUnit());
					m.setS1Data(reportRatiosData.getCqData());

					m.setS2(reportRatiosData.getCqPeriodMinus1());
					m.setS2Unit(reportRatiosData.getUnit());
					m.setS2Data(reportRatiosData.getCqDataMinus1());

					m.setS3("QoQ Change");
					m.setS3Unit(reportRatiosData.getQoqyoyUnit());
					m.setS3Data(reportRatiosData.getQoq());
					m.setS3Append("APPEND");

					m.setS4(reportRatiosData.getCqPeriodMinus4());
					m.setS4Unit(reportRatiosData.getUnit());
					m.setS4Data(reportRatiosData.getCqDataMinus4());

					m.setS5("YoY Change");
					m.setS5Unit(reportRatiosData.getQoqyoyUnit());
					m.setS5Data(reportRatiosData.getYoy());
					m.setS5Append("APPEND");

					m.setS6(reportRatiosData.getCyPeriod());
					m.setS6Unit(reportRatiosData.getUnit());
					m.setS6Data(reportRatiosData.getCyData());

					m.setS7(reportRatiosData.getCyPeriodMinus1());
					m.setS7Unit(reportRatiosData.getUnit());
					m.setS7Data(reportRatiosData.getCyDataMinus1());
				}
				processedList.add(m);
			}

			return processedList;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	}


	public HashMap<Integer, Integer> slotNumberMapper(Double data1, Double data2,Double data3,Double data4, Double data5){
		List<Double> listOfSlot = new ArrayList<>(1);
		listOfSlot.add(data1);
		listOfSlot.add(data2);
		listOfSlot.add(data3);
		listOfSlot.add(data4);
		listOfSlot.add(data5);
		HashMap<Integer, Integer> mapper = new HashMap<>(1);
		int slotNumber = 0;
		for (int i = 0; i < 5; i++) {
			if(listOfSlot.get(i)!=null){
				mapper.put(slotNumber, i);
				slotNumber++;
			}
		}
		return mapper;
	}



	public String unitCurrencyAppender(String currency, String unit){
		String tempAppender = "";
		if(currency!=null && unit!=null){
			tempAppender = "(" + currency +" " + unit +")";
		}else if(unit!=null){
			tempAppender = "(" + unit +")";
		}else if(currency!=null){
			tempAppender = currency;
		}else{
			tempAppender = "";
		}
		return tempAppender;
	}


	public String applyFontAndFormattor(Double tempData,String unit, String append, String moduleName){
		if(tempData!=null){
			String value = customization(BigDecimal.valueOf(tempData), unit, append, moduleName);
			String data = fontCheck(value);
			return data;
		}else{
			return "-";
		}
	}

	public String applyFormattor(Double tempData,String unit, String append, String moduleName){
		if(tempData!=null){
			String value = customization(BigDecimal.valueOf(tempData), unit, append, moduleName);
			String data = value;
			return data;
		}else{
			return "-";
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String customization(BigDecimal v, String unit ,String append,String moduleName)
	{
		if(unit==null){
			unit ="";
		}
		if(v!=null){
			if(moduleName!=null && !moduleName.equals("") && moduleName.equals(StaticParams.MODULE_NAME_COMPANY_PROFILE)){
				if(unit!=null && (unit.equals("bps") || unit.equals("%"))){
					if(v!=null){
						BigDecimal bd = new BigDecimal(Math.abs(v.doubleValue()));						
						if(bd.compareTo(new BigDecimal(StaticParams.COMPANY_PROFILE_MAX))==1){
							return StaticParams.NM;
						}
					}
				}
			}

			boolean checkNeg = false;
			if(v.doubleValue() < 0d){
				checkNeg = true;
			}

			ArrayList<Map> ranges = new ArrayList<Map>();
			Map<String, Object> rangeMap = new HashMap<String, Object>();
			rangeMap.put("divider", 1e18);
			rangeMap.put("suffix", "P");
			ranges.add(rangeMap);

			rangeMap = new HashMap<String, Object>();
			rangeMap.put("divider", 1e15);
			rangeMap.put("suffix", "E");
			ranges.add(rangeMap);

			rangeMap = new HashMap<String, Object>();
			rangeMap.put("divider", 1e12);
			rangeMap.put("suffix", "T");
			ranges.add(rangeMap);

			rangeMap = new HashMap<String, Object>();
			rangeMap.put("divider", 1e9);
			rangeMap.put("suffix", "B");
			ranges.add(rangeMap);

			rangeMap = new HashMap<String, Object>();
			rangeMap.put("divider", 1e6);
			rangeMap.put("suffix", "M");
			ranges.add(rangeMap);

			Double value = Math.abs(v.doubleValue());

			String updateValue="";
			NumberFormat formatter = new DecimalFormat();
			if(unit.contains("%") && value.compareTo(0.1) >0 ) {
				formatter = new DecimalFormat("#,##0.0");
				updateValue = formatter.format(value);
			}else if(unit.equals("x")) {
				formatter = new DecimalFormat("#,##0.00");
				if(value.compareTo(0d) < 0 ) {
					updateValue="("+formatter.format(value).replace("-","")+")";
				}else{
					updateValue = formatter.format(value);
				}
			}else if(unit.equals("bps")) {
				formatter = new DecimalFormat("#,##0");
				if(value.compareTo(0d)< 0) {
					updateValue="("+formatter.format(value).replace("-","")+")";
				}else{
					updateValue = formatter.format(value);
				}
			}else if(unit.equals("days")) {
				formatter = new DecimalFormat("#,###");
				updateValue = formatter.format(value);
			}else if(unit.equals("Years")) {
				formatter = new DecimalFormat("#,##0.0");
				updateValue = formatter.format(value);
			}else if(value.compareTo(0d)< 0) {
				formatter = new DecimalFormat("#,##0.00");
				updateValue="("+formatter.format(value).replace("-","")+")";
			}else if(value.compareTo(0d)>=0  && value.compareTo(10d)< 0) {
				formatter = new DecimalFormat("#,##0.00");
				updateValue = formatter.format(value);
			}else if(value.compareTo(10d)>=0 && value.compareTo(999d)<0) {
				formatter = new DecimalFormat("#,###.0");
				updateValue = formatter.format(value);
			}else if(value.compareTo(1000d)>=0 && value.compareTo(999999d) <0 ){
				formatter = new DecimalFormat("#,###");
				updateValue = formatter.format(value);
			}else {
				for (int i = 0; i < ranges.size(); i++) {
					HashMap<String, Object> hm = (HashMap<String, Object>) ranges.get(i);
					BigDecimal val = BigDecimal.valueOf((Double)hm.get("divider"));
					if (value.compareTo(val.doubleValue())>0){
						value = value/val.doubleValue();
						formatter = new DecimalFormat("#,##0.00");
						updateValue = formatter.format(value) +" "+ (String)hm.get("suffix");
					}
				}
			}

			if(updateValue==null){
				return "-";
			}

			if(checkNeg){
				updateValue="("+updateValue+")";
			}

			if(append!=null && !append.equals("")){
				if(unit!=null && !unit.equals("")){
					return updateValue+" "+unit;
				}else{
					return updateValue;
				}
			}else{
				return updateValue;
			}
		}else{
			return "-";
		}
	}



	public String applyFormattor(BigDecimal v, String unit ,String append,String moduleName){
		if(unit==null){
			unit ="";
		}
		if(v!=null){
			if(moduleName!=null && !moduleName.equals("") && moduleName.equals(StaticParams.MODULE_NAME_COMPANY_PROFILE)){
				if(unit!=null && (unit.equals("bps") || unit.equals("%"))){
					if(v!=null){
						BigDecimal bd = new BigDecimal(Math.abs(v.doubleValue()));						
						if(bd.compareTo(new BigDecimal(StaticParams.COMPANY_PROFILE_MAX))==1){
							return StaticParams.NM;
						}
					}
				}
			}

			boolean checkNeg = false;
			if(v.doubleValue() < 0d){
				checkNeg = true;
			}

			Double value = Math.abs(v.doubleValue());
			String updateValue="";
			NumberFormat formatter = new DecimalFormat();
			if(unit.contains("%") && value.compareTo(0.1) >0 ) {
				formatter = new DecimalFormat("#,##0.0");
				updateValue = formatter.format(value);
			}else if(unit.equals("x")) {
				formatter = new DecimalFormat("#,##0.00");
				if(value.compareTo(0d) < 0 ) {
					updateValue="("+formatter.format(value).replace("-","")+")";
				}else{
					updateValue = formatter.format(value);
				}
			}else if(unit.equals("bps")) {
				formatter = new DecimalFormat("#,##0");
				if(value.compareTo(0d)< 0) {
					updateValue="("+formatter.format(value).replace("-","")+")";
				}else{
					updateValue = formatter.format(value);
				}
			}else if(unit.equals("days")) {
				formatter = new DecimalFormat("#,###");
				updateValue = formatter.format(value);
			}else if(unit.equals("Years")) {
				formatter = new DecimalFormat("#,##0.0");
				updateValue = formatter.format(value);
			}else {
				formatter = new DecimalFormat("#,##0.00");
				updateValue = formatter.format(value);
			}

			if(updateValue==null){
				return "-";
			}

			if(checkNeg){
				updateValue="("+updateValue+")";
			}

			if(append!=null && !append.equals("")){
				if(unit!=null && !unit.equals("")){
					return updateValue+" "+unit;
				}else{
					return updateValue;
				}
			}else{
				return updateValue;
			}
		}else{
			return "-";
		}
	}

	public String fontCheck(String value){
		String customVal = "";
		if(value!=null){
			if(value.equals("")){
				return "-";
			}else{

				/*DO not forget to change the field markup as styled in the jasper template*/
				if(value.length()>12 && value.length()<30){
					customVal = "<font size='6'>" + value.replaceAll("&", "&amp;") + "</font>";
				}else if(value.length()>30 && value.length()<50){
					customVal = "<font size='5'>" + value.replaceAll("&", "&amp;") + "</font>";
				}else if(value.length()>50 && value.length()<70){
					customVal = "<font size='4'>" + value.replaceAll("&", "&amp;") + "</font>";
				}else{
					customVal = value.replaceAll("&", "&amp;");
				}
				return customVal;

				/*	if(value.length()>12 && value.length()<30){
					customVal = "<font size='6'>" + StringEscapeUtils.escapeXml(value) + "</font>";
				}else if(value.length()>30 && value.length()<50){
					customVal = "<font size='5'>" + StringEscapeUtils.escapeXml(value) + "</font>";
				}else if(value.length()>50 && value.length()<70){
					customVal = "<font size='4'>" + StringEscapeUtils.escapeXml(value) + "</font>";
				}else{
					customVal = value;
				}
				return customVal;*/


			}
		}
		return "-";
	}

	public String fontCheck(String value,int size){
		String customVal = "";
		if(value!=null){
			if(value.equals("")){
				return "-";
			}else{
				/*DO not forget to change the field markup as styled in the jasper template*/
				customVal = "<font size='"+size+"'>" + value.replaceAll("&", "&amp;") + "</font>";
				return customVal;
			}
		}
		return "-";
	}





	public String createBold(String value){
		String customVal = "";
		customVal = "<style isBold='true' pdfFontName='Helvetica-Bold' isPdfEmbedded='true'>" + value.replaceAll("&", "&amp;") + "</style>";
		return customVal;
	}

	public String addColor(String value, String colorCode){
		String customVal = "<font color='"+colorCode+"'>" + value.replaceAll("&","&amp;") + "</font>";
		return customVal;
	}

	public String getUnitAndCurrency(String currency,String unit){
		String tempUnitPlusCurrency = "";
		if(currency!=null && unit!=null){
			tempUnitPlusCurrency = currency +" " + unit;
		}else if(unit!=null){
			tempUnitPlusCurrency = unit;
		}else if(currency!=null){
			tempUnitPlusCurrency = currency;
		}else{
			tempUnitPlusCurrency = "";
		}
		return tempUnitPlusCurrency;

	}


	public String customFormattor(Double value, String unit ,String append,boolean negInBracket){
		if(unit==null){
			unit ="";
		}
		if(value!=null){

			boolean checkNeg = false;
			if(value < 0d){
				checkNeg = true;
			}

			value = Math.abs(value);
			String updateValue="";
			NumberFormat formatter = new DecimalFormat();
			if(unit.contains("%") && value.compareTo(0.1) >0 ) {
				formatter = new DecimalFormat("#,##0.0");
				updateValue = formatter.format(value);
			}else if(unit.equals("x")) {
				formatter = new DecimalFormat("#,##0.00");
				updateValue = formatter.format(value);
			}else if(unit.equals("bps")) {
				formatter = new DecimalFormat("#,##0");
				updateValue = formatter.format(value);
			}else if(unit.equals("days")) {
				formatter = new DecimalFormat("#,###");
				updateValue = formatter.format(value);
			}else if(unit.equals("Years")) {
				formatter = new DecimalFormat("#,##0.0");
				updateValue = formatter.format(value);
			}else if(value.compareTo(0d)< 0) {
				formatter = new DecimalFormat("#,##0.00");
				updateValue="("+formatter.format(value).replace("-","")+")";
			}else if(value.compareTo(0d)>=0  && value.compareTo(10d)< 0) {
				formatter = new DecimalFormat("#,##0.00");
				updateValue = formatter.format(value);
			}else if(value.compareTo(10d)>=0 && value.compareTo(999d)<0) {
				formatter = new DecimalFormat("#,###.0");
				updateValue = formatter.format(value);
			}else if(value.compareTo(1000d)>=0 && value.compareTo(999999d) <0 ){
				formatter = new DecimalFormat("#,###");
				updateValue = formatter.format(value);
			}else {
				formatter = new DecimalFormat("#,##0");
				updateValue = formatter.format(value);
			}

			if(updateValue==null){
				return "-";
			}

			if(checkNeg && negInBracket){
				updateValue="("+updateValue+")";
			}else if(checkNeg){
				updateValue= "-"+updateValue;
			}

			if(append!=null && !append.equals("")){
				if(unit!=null && !unit.equals("")){
					return updateValue+" "+unit;
				}else{
					return updateValue;
				}
			}else{
				return updateValue;
			}
		}else{
			return "-";
		}
	}




	public List<LocalDate> chinaIssueDate(List<LocalDate> dateList){
		List<LocalDate> dateListNew = dateList;
		LocalDate currentperiod = dateListNew.get(0);
		LocalDate tempPeriod = currentperiod.minusMonths(3).dayOfMonth().withMaximumValue();
		dateListNew.add(tempPeriod);		
		return dateListNew;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public LinkedHashMap<String, Object> chinaIssue(Integer countryId,String periodType,List<LocalDate> dateList, String category,String currency , String requestedDataFormat,String type){
		LinkedHashMap<String, Object> seperatedData = new LinkedHashMap<>();
		List<IndicatorHistoricalDataDTO> newdata = new ArrayList<>(); 
		LinkedHashMap<String, Object> mappedData = imService.getEconomicalDataForIM( countryId, periodType, dateList, category, null, requestedDataFormat);
		if("quarterly".equalsIgnoreCase(requestedDataFormat)){
			for (LocalDate localDate : dateList) {
				String myDateFormat = localDate.toString("MM");
				String formattedDate = localDate.toString("yyyy-MM-dd");
				List<IndicatorHistoricalDataDTO> data = (List<IndicatorHistoricalDataDTO>)mappedData.get(formattedDate);
				List<IndicatorHistoricalDataDTO> newDataList = new ArrayList<>();
				if(myDateFormat.equals("03")){
					String unit = null;
					if(data!=null){
						unit = data.get(0).getUnit();
						IndicatorHistoricalDataDTO tempData = new IndicatorHistoricalDataDTO();
						if(data.get(0)!=null){
							BeanUtils.copyProperties(data.get(0), tempData);
						}
						Double myDataValue = tempData.getData();
						Double newDataValue = null; 
						Double fxData = economyService.getFxData(periodType, localDate.toDate(), unit, currency);
						if(myDataValue!=null){
							newDataValue = myDataValue*fxData;
						}
						tempData.setData(newDataValue);
						newDataList.add(tempData);
					}
				}else{

					LocalDate prevQteDate = localDate.minusMonths(3).dayOfMonth().withMaximumValue();
					String previousqtr = localDate.minusMonths(3).dayOfMonth().withMaximumValue().toString("yyyy-MM-dd");
					List<LocalDate> newLocalDateList = new ArrayList<>();
					newLocalDateList.add(prevQteDate);
					List<IndicatorHistoricalDataDTO> dataPrevious = (List<IndicatorHistoricalDataDTO>)mappedData.get(previousqtr);
					if(dataPrevious==null || dataPrevious.isEmpty()){
						LinkedHashMap<String, Object> prevQtrDetails = imService.getEconomicalDataForIM( countryId, periodType, newLocalDateList, category, null, requestedDataFormat);
						dataPrevious = (List<IndicatorHistoricalDataDTO>)prevQtrDetails.get(previousqtr);
					}

					IndicatorHistoricalDataDTO newDataDto = new IndicatorHistoricalDataDTO();
					if(data!=null && dataPrevious!=null){
						String unit = null;
						if(data!=null){
							unit = data.get(0).getUnit();
						}
						Double fxData = economyService.getFxData(periodType, localDate.toDate(), unit, currency);
						IndicatorHistoricalDataDTO dataDto = new IndicatorHistoricalDataDTO();
						if(data.get(0)!=null){
							BeanUtils.copyProperties(data.get(0), dataDto);
						}
						IndicatorHistoricalDataDTO dataPreviousDto = new IndicatorHistoricalDataDTO();
						if(dataPrevious.get(0)!=null){
							BeanUtils.copyProperties(dataPrevious.get(0), dataPreviousDto);
						}
						if(dataDto!=null && dataPrevious!=null){
							Double dataVal = dataDto.getData();
							Double dataPrevValue =  dataPreviousDto.getData();
							Double newData = null;
							newDataDto = dataDto;
							if(dataVal!=null && dataPrevValue!=null){
								newData = dataVal-dataPrevValue;
								if(newData!=null && newData!=0d){
									newData = newData * fxData;	
								}
								newDataDto.setData(newData);
							}
						}
						newDataList.add(newDataDto);
					}else{
						newDataList.add(newDataDto);
					}
				}
				seperatedData.put(formattedDate, newDataList);
			}
		}else if("yearly".equalsIgnoreCase(requestedDataFormat)){

			for (LocalDate localDate : dateList) {
				String myDateFormat = localDate.toString("MM");
				String formattedDate = localDate.toString("yyyy-MM-dd");
				List<IndicatorHistoricalDataDTO> data = (List<IndicatorHistoricalDataDTO>)mappedData.get(formattedDate);
				List<IndicatorHistoricalDataDTO> newDataList = new ArrayList<>();
				if(myDateFormat.equals("12")){
					for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
						LocalDate yd = new LocalDate(indicatorHistoricalDataDTO.getPeriod());
						LocalDate finDate = yd.dayOfMonth().withMaximumValue();
						if(finDate.isEqual(localDate)){

							String unit = null;
							if(data!=null){
								unit = indicatorHistoricalDataDTO.getUnit();
							}

							IndicatorHistoricalDataDTO dataDto = new IndicatorHistoricalDataDTO();
							if(indicatorHistoricalDataDTO!=null){
								BeanUtils.copyProperties(indicatorHistoricalDataDTO, dataDto);
							}

							Double fxData = economyService.getFxData(periodType, localDate.toDate(), unit, currency);
							if(indicatorHistoricalDataDTO.getData()!=null && indicatorHistoricalDataDTO.getData()!=0d){
								Double tempData = indicatorHistoricalDataDTO.getData()*fxData;
								dataDto.setData(tempData);
							}

							newDataList.add(dataDto);
							break;
						}
					}
				}else{

					/////getting the previous period data 

					for (IndicatorHistoricalDataDTO indicatorHistoricalDataDTO : data) {
						LocalDate yd = new LocalDate(indicatorHistoricalDataDTO.getPeriod());
						LocalDate prevQteDate = yd.minusMonths(3).dayOfMonth().withMaximumValue();
						String previousqtr = yd.minusMonths(3).dayOfMonth().withMaximumValue().toString("yyyy-MM-dd");

						List<LocalDate> newLocalDateList = new ArrayList<>();
						newLocalDateList.add(prevQteDate);
						String ydFormat = yd.toString("MM");

						if(ydFormat.equals("03")){
							IndicatorHistoricalDataDTO dataDto = new IndicatorHistoricalDataDTO();
							if(indicatorHistoricalDataDTO!=null){
								BeanUtils.copyProperties(indicatorHistoricalDataDTO, dataDto);
							}
							String unit = null;
							if(data!=null){
								unit = dataDto.getUnit();
							}

							if(dataDto.getData()!=null && dataDto.getData()!=0d){
								Double fxData = economyService.getFxData(periodType, localDate.toDate(), unit, currency);
								Double tempData = dataDto.getData()*fxData;
								dataDto.setData(tempData);
							}

							newDataList.add(dataDto);
						}else{
							List<IndicatorHistoricalDataDTO> dataPrevious = (List<IndicatorHistoricalDataDTO>)mappedData.get(previousqtr);
							if(dataPrevious==null || dataPrevious.isEmpty()){
								LinkedHashMap<String, Object> prevQtrDetails = imService.getEconomicalDataForIM( countryId, periodType, newLocalDateList, category, null, requestedDataFormat);
								dataPrevious = (List<IndicatorHistoricalDataDTO>)prevQtrDetails.get(previousqtr);
							}

							IndicatorHistoricalDataDTO newDataDto = new IndicatorHistoricalDataDTO();
							if(data!=null && dataPrevious!=null){
								IndicatorHistoricalDataDTO dataDto = indicatorHistoricalDataDTO;
								IndicatorHistoricalDataDTO dataPreviousDto = dataPrevious.get(0);
								if(indicatorHistoricalDataDTO!=null){
									BeanUtils.copyProperties(indicatorHistoricalDataDTO, newDataDto);
								}

								Double dataVal = dataDto.getData();
								Double dataPrevValue =  dataPreviousDto.getData();
								Double newData = null;
								if(dataVal!=null && dataPrevValue!=null){
									newData = dataVal-dataPrevValue;
									String unit = null;
									if(data!=null){
										unit = dataDto.getUnit();
									}

									Double fxData = economyService.getFxData(periodType, localDate.toDate(), unit, currency);
									if(newData!=null && newData!=0d){
										newData =newData*fxData;
									}

									newDataDto.setData(newData);
								}
								newDataList.add(newDataDto);
							}
						}
					}
				}
				seperatedData.put(formattedDate, newDataList);
			}
		}

		String frequency = (String)mappedData.get(CMStatic.FREQUENCY);
		seperatedData.put(CMStatic.FREQUENCY, frequency);
		return seperatedData;
	}


}