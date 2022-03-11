package com.televisory.capitalmarket.service;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.dozer.DozerBeanMapper;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.CMStockRepository;
import com.televisory.capitalmarket.dao.EconomyRepository;
import com.televisory.capitalmarket.dao.InteractiveComparisonRepository;
import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.dto.economy.CurrencyMappingDTO;
import com.televisory.capitalmarket.dto.economy.IndicatorHistoricalDataDTO;
import com.televisory.capitalmarket.entities.economy.ExchangeRatesComparison;
import com.televisory.capitalmarket.model.EconomyRequestIndicators;
import com.televisory.capitalmarket.model.IcCommodityRequestModel;
import com.televisory.capitalmarket.model.IcDataDownloadRequest;
import com.televisory.capitalmarket.model.IcDataDownloadResponse;
import com.televisory.capitalmarket.model.IcEconomyRequestModel;
import com.televisory.capitalmarket.model.IcEconomyResponseModel;
import com.televisory.capitalmarket.model.IcIndustryRequestModel;
import com.televisory.capitalmarket.model.IcStockRequestModel;
import com.televisory.capitalmarket.model.IcStockResponseModel;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;

/**
 * 
 * @author vinay
 *
 */
@Service
public class InteractiveComparisonService {

	Logger _log = Logger.getLogger(InteractiveComparisonService.class);

	@Autowired
	InteractiveComparisonRepository icRepository;

	@Autowired
	ExecutorService executorPool;

	@Autowired
	EconomyRepository economyRepository;

	@Autowired
	CMStockRepository cmStockRepository;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	RatioService ratioService;

	@Autowired
	SectorService sectorService;

	@Autowired
	SectorRepository sectorRepository;

	@Autowired
	EconomyService economyService;

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	CorporateAnnouncementService corpAnnService;

	@Autowired
	CMRepository cmRepository;

	@Autowired
	CMFinancialDataService cmFinancialDataService;

	@Autowired
	CommodityService commodityService;

	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	/**
	 * Method to get the companies for Interactive Comparison
	 * 
	 * @param searchCriteria
	 * @param companyId
	 * @param countryIdList
	 * @return
	 */
	public List<CompanyDTO> getIComCompanies(String searchCriteria, String companyId, String countryIdList) {

		List<CompanyDTO> iComCompanies;

		if (companyId == null && searchCriteria == null) {
			iComCompanies = icRepository.getIComCompanies(countryIdList);
		} else if (companyId == null && searchCriteria != null) {
			iComCompanies = icRepository.getIComCompanies(searchCriteria, countryIdList);
		} else if (companyId != null && searchCriteria == null) {

			@SuppressWarnings("rawtypes")
			CompanyDTO companyDTO = capitalMarketService.getCMCompaniesById(companyId);
			iComCompanies = icRepository.getIComCompaniesByCountryIndustry(companyDTO.getCountryCode(),
					companyDTO.getFf_industry());
		} else {
			@SuppressWarnings("rawtypes")
			CompanyDTO companyDTO = capitalMarketService.getCMCompaniesById(companyId);
			iComCompanies = icRepository.getIComCompanies(searchCriteria, companyDTO.getCountryCode(),
					companyDTO.getFf_industry());
		}
		return iComCompanies;
	}

	/**
	 * Method to get the financial parameter based on the industry type
	 * 
	 * @param industryType
	 * @return
	 */
	public List<BalanceModelDTO> getFinancialParameter(String industryType, Boolean watchlistFlag, Boolean icFlag,
			Boolean screenerFlag) {
		return icRepository.getFinancialParameter(industryType, watchlistFlag, icFlag, screenerFlag);
	}

	public List<BalanceModelDTO> getDistinctFinancialParameter(String industryType, Boolean watchlistFlag,
			Boolean icFlag, Boolean screenerFlag) {
		return icRepository.getDistinctFinancialParameter(industryType, watchlistFlag, icFlag, screenerFlag);
	}

	public List<BalanceModelDTO> getFinancialParams(String industryType, String fieldName) {
		return icRepository.getFinancialParams(industryType, fieldName);
	}

	/**
	 * Method to get the IC Stock Datas
	 * 
	 * @param icStockRequest
	 * @return
	 */
	public List<IcStockResponseModel> getIcStockData(IcStockRequestModel icStockRequest) {

		List<Callable<IcStockResponseModel>> taskList = new ArrayList<>();

		List<IcStockResponseModel> finalStockDataList = new ArrayList<>();

		icStockRequest.getCompanyList().forEach(company -> {

			taskList.add(() -> {
				return getIcStockData(company, icStockRequest);
			});

		});

		try {

			List<Future<IcStockResponseModel>> futures = executorPool.invokeAll(taskList);

			for (Future<IcStockResponseModel> future : futures) {
				finalStockDataList.add(future.get());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return finalStockDataList;
	}

	/**
	 * Method to get to update the financial /Stock data list
	 * 
	 * @param company
	 * @param icStockRequest
	 * @return
	 */
	private IcStockResponseModel getIcStockData(String company, IcStockRequestModel icStockRequest) {

		/*
		 * if(icStockRequest.getCurrency()==null){
		 * icStockRequest.setCurrency("USD"); }else{
		 * icStockRequest.setCurrency(icStockRequest.getCurrency()); }
		 */

		IcStockResponseModel stockResponse = new IcStockResponseModel();

		stockResponse.setCompanyId(company);
		stockResponse.setDataType(icStockRequest.getFieldType());
		stockResponse.setFieldName(icStockRequest.getFieldName());

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if (icStockRequest.getEndDate() == null)
			icStockRequest.setEndDate(new Date(System.currentTimeMillis()));

		try {

			if (icStockRequest.getStartDate() == null)
				icStockRequest.setStartDate(format.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE));

			if (icStockRequest.getFieldType().toLowerCase().trim().equals("stock")) {

				List<StockPriceDTO> stockPriceData = new ArrayList<>();

				List<StockPriceDTO> adjustedStockPrice = new ArrayList<>();

				stockPriceData = cmStockRepository.getFactSetCompanyStockPrice(company, icStockRequest.getPeriodicity(),
						icStockRequest.getStartDate(), icStockRequest.getEndDate(), icStockRequest.getCurrency());

				adjustedStockPrice = corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceData, company);

				stockResponse.setStockDataList(adjustedStockPrice);
			} else {
				List<CompanyFinancialMINDTO> financialData = new ArrayList<CompanyFinancialMINDTO>();
				if (icStockRequest.getFieldType().equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE)
						|| icStockRequest.getFieldType().equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {

					/*
					 * for (String fieldname : icStockRequest.getFieldName()) {
					 * financialData.addAll(ratioService.getCompanyRatio(
					 * company, fieldname, icStockRequest.getPeriodicity(),
					 * icStockRequest.getCurrency(),
					 * icStockRequest.getStartDate(),
					 * icStockRequest.getEndDate())); }
					 */
					financialData.addAll(ratioService.getCompanyRatio(company, icStockRequest.getFieldName(),
							icStockRequest.getPeriodicity(), icStockRequest.getStartDate(), icStockRequest.getEndDate(),
							icStockRequest.getCurrency()));

				} else
					// financialData =
					// icRepository.getStockTabFinancialData(company,icStockRequest);
					financialData = cmFinancialDataService.getCompanyFinancial(company, icStockRequest.getFieldName(),
							icStockRequest.getPeriodicity(), icStockRequest.getStartDate(), icStockRequest.getEndDate(),
							icStockRequest.getCurrency());

				stockResponse.setFinancialDataList(financialData);
			}
		} catch (Exception e) {

			_log.error("Data not found for Company : " + company);
			e.printStackTrace();
			return stockResponse;
		}
		return stockResponse;
	}

	public List<IcEconomyResponseModel> getIcEconomyData(IcEconomyRequestModel icEconomyRequest) {

		List<IcEconomyResponseModel> finalStockDataList = new ArrayList<>();
		List<IndicatorHistoricalDataDTO> finalIndicatorDataList = new ArrayList<>();

		Map<String, List<String>> indicatorByPeriodMap = new HashMap<>();

		List<Callable<List<IndicatorHistoricalDataDTO>>> indicatortaskList = new ArrayList<>();

		if (icEconomyRequest.getFieldType().toLowerCase().trim().equals("indicator")) {

			IcEconomyResponseModel resposeModel = new IcEconomyResponseModel();

			resposeModel.setCountryIsoCode("");

			// group the indicator list based on the periodicity to reduce the
			// number of database hits (only 1 hit hor each periodicity)
			indicatorByPeriodMap = icEconomyRequest.getIndicators().stream()
					.collect(Collectors.groupingBy(EconomyRequestIndicators::getPeriodicity,
							Collectors.mapping(EconomyRequestIndicators::getIndicatorName, Collectors.toList())));

			indicatorByPeriodMap.forEach((key, value) -> {
				indicatortaskList.add(() -> {
					return getIcIndicatorData(value, key, icEconomyRequest);
				});
			});

			try {
				List<Future<List<IndicatorHistoricalDataDTO>>> futures = executorPool.invokeAll(indicatortaskList);

				for (Future<List<IndicatorHistoricalDataDTO>> future : futures) {
					finalIndicatorDataList.addAll(future.get());
				}
				resposeModel.setIndicatorsDataList(finalIndicatorDataList);
				finalStockDataList.add(resposeModel);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {

			List<Callable<IcEconomyResponseModel>> forextaskList = new ArrayList<>();

			icEconomyRequest.getCountryList().forEach(country -> {

				forextaskList.add(() -> {
					return getIcForexData(country, icEconomyRequest);
				});

			});

			try {
				List<Future<IcEconomyResponseModel>> futures = executorPool.invokeAll(forextaskList);

				for (Future<IcEconomyResponseModel> future : futures) {

					finalStockDataList.add(future.get());

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return finalStockDataList;
	}

	public List<IndicatorHistoricalDataDTO> getIcIndicatorData(List<String> indicatorList, String periodicity,
			IcEconomyRequestModel icEconomyRequest) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Date date = null;

		if (icEconomyRequest.getStartDate() == null) {
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			icEconomyRequest.setStartDate(date);
		}

		if (icEconomyRequest.getEndDate() == null) {

			LocalDate now = LocalDate.now();
			LocalDate lastDay = now.with(lastDayOfYear());

			icEconomyRequest.setEndDate(Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		}

		if (icEconomyRequest.getTargetCurrency() != null) {
			return economyService.getDataByIndicatorAndCountry(icEconomyRequest.getCountryList(), indicatorList,
					periodicity, icEconomyRequest.getTargetCurrency(), icEconomyRequest.getStartDate(),
					icEconomyRequest.getEndDate());
		}

		return economyService.getDataByIndicatorAndCountry(icEconomyRequest.getCountryList(), indicatorList,
				periodicity, icEconomyRequest.getStartDate(), icEconomyRequest.getEndDate());

	}

	public IcEconomyResponseModel getIcForexData(String country, IcEconomyRequestModel icEconomyRequest)
			throws Exception {

		IcEconomyResponseModel resposeModel = new IcEconomyResponseModel();
		resposeModel.setCountryIsoCode(country);

		resposeModel.setDataType(icEconomyRequest.getFieldType());

		CurrencyMappingDTO sourceCurrency = economyService.getCountryCurrencyByIsoCode(country);

		_log.info("Currency List size is " + icEconomyRequest);

		List<List<ExchangeRatesComparison>> forexData = new ArrayList<List<ExchangeRatesComparison>>();

		if (sourceCurrency != null) {

			icEconomyRequest.getCurrencyList().forEach(targetCurrencyCode -> {

				_log.info("getting Exchange rate for source Currency " + sourceCurrency.getCurrencyCode()
						+ " targetCurrency" + targetCurrencyCode);

				forexData.add(economyService.getFactSetExchangeRate(sourceCurrency.getCurrencyCode(),
						targetCurrencyCode, icEconomyRequest.getStartDate(), icEconomyRequest.getEndDate()));
			});

		}
		resposeModel.setExchangeRate(forexData);

		return resposeModel;
	}

	/**
	 * Method to get the industry data
	 * 
	 * @param icIndustryRequest
	 * @return
	 */
	public List<IndustryFinancialDataDTO> getIcIndustryData(IcIndustryRequestModel icIndustryRequest) {

		List<IndustryFinancialDataDTO> industryData = new ArrayList<>();

		List<Integer> countryId = icIndustryRequest.getCountryId();

		Date startDate = icIndustryRequest.getStartDate();
		Date endDate = icIndustryRequest.getEndDate();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Date date = null;

		if (startDate == null) {
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate = date;
		}

		if (endDate == null) {
			endDate = new Date();
		}

		String fieldNameString = String.join(",", icIndustryRequest.getFieldNames());

		String industryListString = String.join(",", icIndustryRequest.getTicsIndustryCodes());

		String commaSeperatedCountryList = countryId.stream().map(String::valueOf).collect(Collectors.joining(","));

		industryData = sectorService.getIDIndustryData(icIndustryRequest.getPeriodicity(), null, industryListString,
				commaSeperatedCountryList, fieldNameString, startDate, endDate, icIndustryRequest.getCurrency(), 12);

		return industryData;
	}

	/*
	 * Method to get the commodity data for all the symbols based as a data map
	 */
	public IcCommodityRequestModel getIcCommodityData(IcCommodityRequestModel icCommodityRequest) {

		ConcurrentHashMap<String, List<CommodityHistoricalDataDTO>> commodityDataMap = new ConcurrentHashMap<>();

		Date startDate = icCommodityRequest.getStartDate();
		Date endDate = icCommodityRequest.getEndDate();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Date date = null;

		if (startDate == null) {
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate = date;
		}

		if (endDate == null) {
			endDate = new Date();
		}

		icCommodityRequest.setStartDate(startDate);
		icCommodityRequest.setEndDate(endDate);

		List<Callable<List<CommodityHistoricalDataDTO>>> taskList = new ArrayList<>();

		icCommodityRequest.getCommoditySymbolList().forEach(symbol -> {

			List symbolList = new ArrayList<>();

			_log.info("Getting data for symbol " + symbol);

			symbolList.clear();

			symbolList.add(symbol);

			taskList.add(() -> {
				return commodityService.getCommodityHistoricalData(symbolList, icCommodityRequest.getCurrency(),
						icCommodityRequest.getStartDate(), icCommodityRequest.getEndDate());
			});

		});

		try {

			List<Future<List<CommodityHistoricalDataDTO>>> futures = executorPool.invokeAll(taskList);

			futures.forEach(future -> {

				List<CommodityHistoricalDataDTO> resultCommodityData;

				try {
					resultCommodityData = future.get();

					if (resultCommodityData.size() > 0) {
						// commodityDataMap.put(resultCommodityData.get(0).getSymbol(),commodityService.updateGapCommodityPrice(resultCommodityData));
						commodityDataMap.put(resultCommodityData.get(0).getSymbol(), resultCommodityData);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			});

			icCommodityRequest.setCommodityDataMap(commodityDataMap);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return icCommodityRequest;
	}

	public IcDataDownloadResponse getIcDataDownloadData(IcDataDownloadRequest icDownloadRequest) {
		IcDataDownloadResponse icDataDownloadResponse = new IcDataDownloadResponse();

		Future<List<IcStockResponseModel>> icStockResponse = executorPool.submit(() -> {
			if (icDownloadRequest.getStockRequest() != null) {
				return getIcStockData(icDownloadRequest.getStockRequest());
			} else {
				return null;
			}
		});

		Future<List<IcStockResponseModel>> icCompanyResponse = executorPool.submit(() -> {
			List<IcStockResponseModel> icCompanyResponseData = new ArrayList<IcStockResponseModel>();
			if (icDownloadRequest.getCompanyRequest() != null && icDownloadRequest.getCompanyRequest().size() > 0) {
				icDownloadRequest.getCompanyRequest().forEach(stockRequest -> {
					icCompanyResponseData.addAll(getIcStockData(stockRequest));
				});
				return icCompanyResponseData;
			} else {
				return null;
			}
		});

		Future<List<IcEconomyResponseModel>> icEconomyResponse = executorPool.submit(() -> {
			if (icDownloadRequest.getEconomyRequest() != null) {
				return getIcEconomyData(icDownloadRequest.getEconomyRequest());
			} else {
				return null;
			}
		});

		Future<List<IcEconomyResponseModel>> icForexResponse = executorPool.submit(() -> {
			if (icDownloadRequest.getForexRequest() != null) {
				return getIcEconomyData(icDownloadRequest.getForexRequest());
			} else {
				return null;
			}
		});

		Future<List<IndustryFinancialDataDTO>> icIndustryResponse = executorPool.submit(() -> {
			if (icDownloadRequest.getIndustryRequest() != null) {
				return getIcIndustryData(icDownloadRequest.getIndustryRequest());
			} else {
				return null;
			}
		});

		Future<IcCommodityRequestModel> icCommodityResponse = executorPool.submit(() -> {
			if (icDownloadRequest.getCommodityRequest() != null) {
				return getIcCommodityData(icDownloadRequest.getCommodityRequest());
			} else {
				return null;
			}
		});

		Future<List<CompanyDTO>> icCompanyInfo = executorPool.submit(() -> {
			if (icDownloadRequest.getStockRequest() != null) {
				return capitalMarketService
						.getCMCompaniesByIdList(icDownloadRequest.getStockRequest().getCompanyList());
			} else if (icDownloadRequest.getCompanyRequest() != null
					&& icDownloadRequest.getCompanyRequest().size() > 0) {
				Set<String> companyIds = new HashSet<String>();
				icDownloadRequest.getCompanyRequest().forEach(companyId -> {
					companyIds.addAll(companyId.getCompanyList());
				});
				return capitalMarketService.getCMCompaniesByIdList(new ArrayList<String>(companyIds));
			} else {
				return null;
			}
		});

		try {
			icDataDownloadResponse.setIcStockResponseData(icStockResponse.get());
			icDataDownloadResponse.setIcCompanyResponseData(icCompanyResponse.get());
			icDataDownloadResponse.setIcEconomyResponseData(icEconomyResponse.get());
			icDataDownloadResponse.setIcIndustryResponseData(icIndustryResponse.get());
			icDataDownloadResponse.setIcCommodityResponseData(icCommodityResponse.get());
			icDataDownloadResponse.setIcCompanyData(icCompanyInfo.get());
			icDataDownloadResponse.setIcForexResponseData(icForexResponse.get());
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
			}
			_log.error(e.getMessage());
		}
		return icDataDownloadResponse;

		/*
		 * List<IcStockResponseModel> icStockResponseData = new
		 * ArrayList<IcStockResponseModel>();
		 * 
		 * try { //icDownloadRequest.getStockRequest().forEach(stockRequest -> {
		 * icStockResponseData.addAll(icService.getIcStockData(icDownloadRequest
		 * .getStockRequest())); //}); } catch (Exception e) {
		 * e.printStackTrace(); if (e.getLocalizedMessage().contains("401")) {
		 * _log.error(e.getMessage()); } _log.error(e.getMessage()); }
		 * 
		 * icDataDownloadResponse.setIcStockResponseData(icStockResponseData);
		 * 
		 * List<IcStockResponseModel> icCompanyResponseData = new
		 * ArrayList<IcStockResponseModel>();
		 * 
		 * try { icDownloadRequest.getCompanyRequest().forEach(stockRequest -> {
		 * icCompanyResponseData.addAll(icService.getIcStockData(
		 * icDownloadRequest.getStockRequest())); }); } catch (Exception e) {
		 * e.printStackTrace(); if (e.getLocalizedMessage().contains("401")) {
		 * _log.error(e.getMessage()); } _log.error(e.getMessage()); }
		 * 
		 * icDataDownloadResponse.setIcCompanyResponseData(icCompanyResponseData
		 * );
		 * 
		 * List<IcEconomyResponseModel> icEconomyResponseData = new
		 * ArrayList<IcEconomyResponseModel>(); try {
		 * //icDownloadRequest.getEconomyRequest().forEach(economyRequest -> {
		 * icEconomyResponseData.addAll(icService.getIcEconomyData(
		 * icDownloadRequest.getEconomyRequest())); //}); } catch (Exception e)
		 * { e.printStackTrace(); if (e.getLocalizedMessage().contains("401")) {
		 * _log.error(e.getMessage()); } _log.error(e.getMessage()); }
		 * 
		 * icDataDownloadResponse.setIcEconomyResponseData(icEconomyResponseData
		 * );
		 * 
		 * List<IndustryFinancialDataDTO> icIndustryResponseData = null;
		 * 
		 * try { icIndustryResponseData =
		 * icService.getIcIndustryData(icDownloadRequest.getIndustryRequest());
		 * } catch (Exception e) { e.printStackTrace(); if
		 * (e.getLocalizedMessage().contains("401")) {
		 * _log.error(e.getMessage()); } _log.error(e.getMessage()); }
		 * 
		 * icDataDownloadResponse.setIcIndustryResponseData(
		 * icIndustryResponseData);
		 * 
		 * IcCommodityRequestModel icCommodityResponseData = null;
		 * 
		 * try { icCommodityResponseData =
		 * icService.getIcCommodityData(icDownloadRequest.getCommodityRequest())
		 * ; } catch (Exception e) { e.printStackTrace(); if
		 * (e.getLocalizedMessage().contains("401")) {
		 * _log.error(e.getMessage()); } _log.error(e.getMessage()); }
		 * 
		 * icDataDownloadResponse.setIcCommodityResponseData(
		 * icCommodityResponseData);
		 */
		// return null;
	}

	public HSSFWorkbook createExcelReport(IcDataDownloadResponse icDataDownloadResponse,
			IcDataDownloadRequest icDownloadRequest) throws Exception {
		try {

			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges = new GenerateExcelStyle(workbook);

			if (icDataDownloadResponse.getIcStockResponseData() != null
					&& icDataDownloadResponse.getIcStockResponseData().size() > 0) {
				HSSFSheet priceMetricSheet = workbook.createSheet("Price Metrics");
				// ges.setBackgroundOnSheet(priceMetricSheet, 0, 4000
				// ,CMStatic.SHEET_MAX_COLUMN);
				priceMetricSheet.setDisplayGridlines(false);
				Map<String, String> companyMap = getCompanyInfo(icDataDownloadResponse.getIcCompanyData());
				createAndFillPriceMetricSheet(workbook, icDataDownloadResponse.getIcStockResponseData(),
						icDownloadRequest.getStockRequest(), companyMap, "Price Metrics", ges);
			}

			if (icDataDownloadResponse.getIcCompanyResponseData() != null
					&& icDataDownloadResponse.getIcCompanyResponseData().size() > 0) {
				Map<String, String> companyMap = getCompanyInfo(icDataDownloadResponse.getIcCompanyData());
				HSSFSheet companySheet = workbook.createSheet("Company");
				companySheet.setDisplayGridlines(false);
				// ges.setBackgroundOnSheet(companySheet, 0, 4000
				// ,CMStatic.SHEET_MAX_COLUMN);
				createAndFillCompanySheet(workbook, icDataDownloadResponse.getIcCompanyResponseData(),
						icDownloadRequest.getCompanyRequest(), companyMap, "Company", ges);
			}

			if (icDataDownloadResponse.getIcEconomyResponseData() != null
					&& icDataDownloadResponse.getIcEconomyResponseData().size() > 0) {
				HSSFSheet economySheet = workbook.createSheet("Economy");
				// ges.setBackgroundOnSheet(economySheet, 0, 4000
				// ,CMStatic.SHEET_MAX_COLUMN);
				economySheet.setDisplayGridlines(false);
				createAndFillEconomySheet(workbook, icDataDownloadResponse.getIcEconomyResponseData(),
						icDownloadRequest.getEconomyRequest(), "Economy", ges);
			}

			if (icDataDownloadResponse.getIcForexResponseData() != null
					&& icDataDownloadResponse.getIcForexResponseData().size() > 0) {
				HSSFSheet economySheet = workbook.createSheet("Forex");
				// ges.setBackgroundOnSheet(economySheet, 0, 6000
				// ,CMStatic.SHEET_MAX_COLUMN);
				economySheet.setDisplayGridlines(false);

				createAndFillForexSheet(workbook, icDataDownloadResponse.getIcForexResponseData(),
						icDownloadRequest.getForexRequest(), "Forex", ges);
			}
			if (icDataDownloadResponse.getIcIndustryResponseData() != null
					&& icDataDownloadResponse.getIcIndustryResponseData().size() > 0) {
				if (icDownloadRequest.getIndustryRequest().getType().equalsIgnoreCase("Country")) {
					HSSFSheet economySheet = workbook.createSheet("Industry (Country wise)");
					// ges.setBackgroundOnSheet(economySheet, 0, 4000
					// ,CMStatic.SHEET_MAX_COLUMN);
					economySheet.setDisplayGridlines(false);
					createAndFillIndustrySheetCountryWise(workbook, icDataDownloadResponse.getIcIndustryResponseData(),
							icDownloadRequest.getIndustryRequest(), "Industry (Country wise)", ges);
				} else if (icDownloadRequest.getIndustryRequest().getType().equalsIgnoreCase("Industry")) {
					HSSFSheet economySheet = workbook.createSheet("Industry (Industry wise)");
					// ges.setBackgroundOnSheet(economySheet, 0, 4000
					// ,CMStatic.SHEET_MAX_COLUMN);
					economySheet.setDisplayGridlines(false);
					createAndFillIndustrySheetIndustryWise(workbook, icDataDownloadResponse.getIcIndustryResponseData(),
							icDownloadRequest.getIndustryRequest(), "Industry (Industry wise)", ges);
				}
			}
			if (icDataDownloadResponse.getIcCommodityResponseData() != null) {
				HSSFSheet economySheet = workbook.createSheet("Commodity");
				economySheet.setDisplayGridlines(false);
				// ges.setBackgroundOnSheet(economySheet, 0, 4000
				// ,CMStatic.SHEET_MAX_COLUMN);
				createAndFillCommoditySheet(workbook, icDataDownloadResponse.getIcCommodityResponseData(),
						icDownloadRequest.getCommodityRequest(), "Commodity", ges);
			}
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void createAndFillCommoditySheet(HSSFWorkbook workbook, IcCommodityRequestModel icCommodityResponseData,
			IcCommodityRequestModel commodityRequest, String sheetName, GenerateExcelStyle ges) {
		_log.info("creating the Commodity Sheet data ");
		try {
			int rowdesign = 0;
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);

			List<String> metricList = new ArrayList<String>();
			metricList.add("Price");
			metricList.add("% Daily Change");
			List<String> commodityList = new ArrayList<String>();
			Map<String, List<CommodityHistoricalDataDTO>> data = icCommodityResponseData.getCommodityDataMap();
			Map<String, Map<String, Map<String, Double>>> commodityMap = new LinkedHashMap<String, Map<String, Map<String, Double>>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
			for (int i = 0; i < data.size(); i++) {
				List<CommodityHistoricalDataDTO> commodityData = data.get(data.keySet().toArray()[i].toString());
				if (commodityData != null && commodityData.size() > 0) {
					String commodityName = commodityData.get(0).getName();
					if (commodityData.get(0).getUnit() != null && !commodityData.get(0).getUnit().equals("")) {
						commodityName = commodityName + " (" + commodityData.get(0).getUnit() + ")";
					}
					if (!commodityList.contains(commodityName)) {
						commodityList.add(commodityName);
					}
					// Map<String,Map<String,Double>> valuesMap = new
					// HashMap<String,Map<String,Double>>();
					for (int j = 0; j < commodityData.size(); j++) {
						if (!commodityMap.containsKey(sdf.format(commodityData.get(j).getPeriod()))) {
							commodityMap.put(sdf.format(commodityData.get(j).getPeriod()),
									new HashMap<String, Map<String, Double>>());
						}
						Map<String, Map<String, Double>> valuesMap = commodityMap
								.get(sdf.format(commodityData.get(j).getPeriod()));
						if (!valuesMap.containsKey(commodityName)) {
							valuesMap.put(commodityName, new HashMap<String, Double>());
						}
						Map<String, Double> metricValueMap = valuesMap.get(commodityName);
						if (!metricValueMap.containsKey("% Daily Change")) {
							metricValueMap.put("% Daily Change", commodityData.get(j).getDailyChange());
						} else {
							metricValueMap.put("% Daily Change", commodityData.get(j).getDailyChange());
						}
						if (!metricValueMap.containsKey("Price")) {
							metricValueMap.put("Price", commodityData.get(j).getClose());
						} else {
							metricValueMap.put("Price", commodityData.get(j).getClose());
						}
						valuesMap.put(commodityName, metricValueMap);
						commodityMap.put(sdf.format(commodityData.get(j).getPeriod()), valuesMap);
					}
				}
			}
			// _log.info(commodityMap);

			int row = 4;
			int cellIndex = 1;
			Row headerRow = sheet.getRow(row);

			if (headerRow == null) {
				headerRow = sheet.createRow(row);
			}

			Iterator itr = commodityList.iterator();
			Cell cellHeader = headerRow.getCell(cellIndex);
			if (cellHeader == null) {
				cellHeader = headerRow.createCell(cellIndex);
			}
			if (cellIndex != 1)
				sheet.setColumnWidth(cellIndex, 5000);
			ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			cellHeader.setCellValue("Date");
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIndex = cellIndex + 1;
			while (itr.hasNext()) {
				cellHeader = headerRow.getCell(cellIndex);
				if (cellHeader == null) {
					cellHeader = headerRow.createCell(cellIndex);
				}
				String headCol = (String) itr.next();
				if (cellIndex != 1)
					sheet.setColumnWidth(cellIndex, 5000 * 2);
				cellHeader.setCellValue(headCol);
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex + 1, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row companyRow = sheet.getRow(row + 1);
				if (companyRow == null) {
					companyRow = sheet.createRow(row + 1);
				}
				for (int j = 0; j < metricList.size(); j++) {
					cellHeader = companyRow.getCell(cellIndex);
					if (cellHeader == null) {
						cellHeader = companyRow.createCell(cellIndex);
					}
					if (cellIndex != 1)
						sheet.setColumnWidth(cellIndex, 5000);
					cellHeader.setCellValue(metricList.get(j));
					// cellHeader.setCellValue(icStockResponseData.get(j).getCompanyId());
					cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
					cellIndex = cellIndex + 1;
				}
			}

			Boolean isLastRecord = false;

			int cellIdxCompany = 1;
			int cellIdxCompanyData = 1;
			int rowStart = 7;
			row = row + 2;
			List<String> keys = new ArrayList<String>(commodityMap.keySet());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyy");
			Collections.sort(keys,
					(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));
			for (int i = 0; i < keys.size(); i++) {
				if (i == keys.size() - 1) {
					isLastRecord = true;
				}
				/*
				 * for(int i=0;i<commodityMap.size();i++){
				 * if(i==commodityMap.size()-1){ isLastRecord = true; }
				 */
				Row rowDebt = sheet.getRow(row);
				if (rowDebt == null) {

					rowDebt = sheet.createRow(row);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if (cellDebt == null) {
					cellDebt = rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt, keys.get(i), sheet, cellIdxDebt, ges, isLastRecord, "LEFT");
				for (int j = 0; j < commodityList.size(); j++) {
					// _log.info(" for "+keys.get(i)+"-->
					// "+commodityList.get(j));
					Map<String, Double> commodityData = commodityMap.get(keys.get(i)).get(commodityList.get(j));
					for (int k = 0; k < metricList.size(); k++) {
						cellIdxDebt = cellIdxDebt + 1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if (cellDebt == null) {
							cellDebt = rowDebt.createCell(cellIdxDebt);
						}
						Double cellVal = null;
						if (commodityData != null && commodityData.containsKey(metricList.get(k))) {
							cellVal = commodityData.get(metricList.get(k));
						}
						// Double cellVal =
						// commodityData.get(commodityData.keySet().toArray()[k].toString());
						if (cellVal == null) {
							setColumnValue(cellDebt, "-", sheet, cellIdxDebt, ges, isLastRecord, "RIGHT");
						} else {
							cellDebt.setCellValue(cellVal);
							cellDebt.setCellStyle(getNumberFormatStyle(cellVal, "", ges, isLastRecord));
						}
					}
				}
				row++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	private void createAndFillIndustrySheetIndustryWise(HSSFWorkbook workbook,
			List<IndustryFinancialDataDTO> icIndustryResponseData, IcIndustryRequestModel industryRequest,
			String sheetName, GenerateExcelStyle ges) {
		_log.info("creating the Industry Sheet Industry Wise data ");
		try {
			int rowdesign = 0;
			String period = icIndustryResponseData.get(0).getPeriodType();
			if (period.toLowerCase().contains("year")) {
				period = "Year";
			} else {
				period = "Quarter";
			}
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int rowStart = 4;
			Map<String, List<IndustryFinancialDataDTO>> industryMap = new HashMap<>();
			industryMap = icIndustryResponseData.stream()
					.collect(Collectors.groupingBy(IndustryFinancialDataDTO::getTicsIndustryName));

			Map<String, Map<String, Map<String, Map<String, Double>>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, Map<String, Double>>>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
			List<String> metricList = new ArrayList<String>();
			List<String> countryList = new ArrayList<String>();
			_log.info(industryMap);
			for (int i = 0; i < industryMap.size(); i++) {
				String industryName = industryMap.keySet().toArray()[i].toString();
				List<IndustryFinancialDataDTO> industryData = industryMap.get(industryName);
				Map<String, Map<String, Map<String, Double>>> valuesMap = new HashMap<String, Map<String, Map<String, Double>>>();
				for (int j = 0; j < industryData.size(); j++) {
					String itemName = industryData.get(j).getItemName();
					String countryName = industryData.get(j).getCountryName();
					if (industryData.get(j).getCurrency() != null && !industryData.get(j).getCurrency().equals("")) {
						itemName = itemName + "(" + industryData.get(j).getCurrency();
						if (industryData.get(j).getUnit() != null && !industryData.get(j).getUnit().equals("")) {
							itemName = itemName + " " + industryData.get(j).getUnit();
						}
						itemName = itemName + ")";
					} else {
						if (industryData.get(j).getUnit() != null && !industryData.get(j).getUnit().equals("")) {
							itemName = itemName + "(" + industryData.get(j).getUnit() + ")";
						}
					}
					if (!valuesMap.containsKey(sdf.format(industryData.get(j).getPeriod()))) {
						valuesMap.put(sdf.format(industryData.get(j).getPeriod()),
								new HashMap<String, Map<String, Double>>());
					}
					Map<String, Map<String, Double>> metricValueMap = valuesMap
							.get(sdf.format(industryData.get(j).getPeriod()));
					if (!metricValueMap.containsKey(itemName)) {
						metricValueMap.put(itemName, new HashMap<String, Double>());
					}

					Map<String, Double> valuePerCountryMap = metricValueMap.get(itemName);
					if (!valuePerCountryMap.containsKey(countryName)) {
						valuePerCountryMap.put(countryName, industryData.get(j).getData());
					}
					valuePerCountryMap.put(countryName, industryData.get(j).getData());
					metricValueMap.put(itemName, valuePerCountryMap);
					valuesMap.put(sdf.format(industryData.get(j).getPeriod()), metricValueMap);

					if (!metricList.contains(itemName)) {
						metricList.add(itemName);
					}
					if (!countryList.contains(countryName)) {
						countryList.add(countryName);
					}
				}
				dataMap.put(industryName, valuesMap);
			}
			_log.info(dataMap);
			createAndFillIndustrySheetCountryWiseTable(dataMap, sheet, rowStart, period, metricList, countryList,
					workbook, sheetName, ges);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	private void createAndFillIndustrySheetCountryWise(HSSFWorkbook workbook,
			List<IndustryFinancialDataDTO> icIndustryResponseData, IcIndustryRequestModel industryRequest,
			String sheetName, GenerateExcelStyle ges) {
		_log.info("creating the Industry Sheet Country Wise data ");
		try {
			int rowdesign = 0;
			String period = icIndustryResponseData.get(0).getPeriodType();
			if (period.toLowerCase().contains("year")) {
				period = "Year";
			} else {
				period = "Quarter";
			}
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int rowStart = 4;
			Map<String, List<IndustryFinancialDataDTO>> industryMap = new HashMap<>();
			industryMap = icIndustryResponseData.stream()
					.collect(Collectors.groupingBy(IndustryFinancialDataDTO::getCountryName));

			Map<String, Map<String, Map<String, Map<String, Double>>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, Map<String, Double>>>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
			List<String> metricList = new ArrayList<String>();
			List<String> industryList = new ArrayList<String>();

			for (int i = 0; i < industryMap.size(); i++) {
				String countryName = industryMap.keySet().toArray()[i].toString();
				List<IndustryFinancialDataDTO> industryData = industryMap.get(countryName);
				Map<String, Map<String, Map<String, Double>>> valuesMap = new HashMap<String, Map<String, Map<String, Double>>>();
				for (int j = 0; j < industryData.size(); j++) {
					String itemName = industryData.get(j).getItemName();
					String industryName = industryData.get(j).getTicsIndustryName();
					if (industryData.get(j).getCurrency() != null && !industryData.get(j).getCurrency().equals("")) {
						itemName = itemName + "(" + industryData.get(j).getCurrency();
						if (industryData.get(j).getUnit() != null && !industryData.get(j).getUnit().equals("")) {
							itemName = itemName + " " + industryData.get(j).getUnit();
						}
						itemName = itemName + ")";
					} else {
						if (industryData.get(j).getUnit() != null && !industryData.get(j).getUnit().equals("")) {
							itemName = itemName + "(" + industryData.get(j).getUnit() + ")";
						}
					}
					if (!valuesMap.containsKey(sdf.format(industryData.get(j).getPeriod()))) {
						valuesMap.put(sdf.format(industryData.get(j).getPeriod()),
								new HashMap<String, Map<String, Double>>());
					}
					Map<String, Map<String, Double>> metricValueMap = valuesMap
							.get(sdf.format(industryData.get(j).getPeriod()));
					if (!metricValueMap.containsKey(itemName)) {
						metricValueMap.put(itemName, new HashMap<String, Double>());
					}

					Map<String, Double> valuePerIndustryMap = metricValueMap.get(itemName);
					if (!valuePerIndustryMap.containsKey(industryName)) {
						valuePerIndustryMap.put(industryName, industryData.get(j).getData());
					}
					valuePerIndustryMap.put(industryName, industryData.get(j).getData());
					metricValueMap.put(itemName, valuePerIndustryMap);
					valuesMap.put(sdf.format(industryData.get(j).getPeriod()), metricValueMap);

					if (!metricList.contains(itemName)) {
						metricList.add(itemName);
					}
					if (!industryList.contains(industryName)) {
						industryList.add(industryName);
					}
				}
				dataMap.put(countryName, valuesMap);
			}
			_log.debug(dataMap);
			createAndFillIndustrySheetCountryWiseTable(dataMap, sheet, rowStart, period, metricList, industryList,
					workbook, sheetName, ges);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	private void createAndFillIndustrySheetCountryWiseTable(
			Map<String, Map<String, Map<String, Map<String, Double>>>> dataMap, HSSFSheet sheet, int rowStart,
			String period, List<String> metricList, List<String> industryList, HSSFWorkbook workbook, String sheetName,
			GenerateExcelStyle ges) {

		try {
			Boolean isLastRecord = false;

			int cellIdxCompany = 1;
			int cellIdxCompanyData = 1;
			for (int i = 0; i < dataMap.size(); i++) {
				int row = rowStart + 3;

				String countryName = dataMap.keySet().toArray()[i].toString();
				// List<String> metrics = headerMap.get(category);
				createCompanyHeaderForIndustry(countryName, period, metricList, industryList, cellIdxCompany, workbook,
						sheetName, row, ges);

				row = row + 3;

				Map<String, Map<String, Map<String, Double>>> valueMap = dataMap.get(countryName);
				// _log.info(valueMap);
				// SortedSet<String> keys = new TreeSet<>(valueMap.keySet());
				List<String> keys = new ArrayList<String>(valueMap.keySet());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyy");
				Collections.sort(keys,
						(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));

				// _log.info(keys);
				int k = 0;
				// cellIdxCompany = cellIdxCompany-1;
				for (String key : keys) {
					if (k == keys.size() - 1) {
						isLastRecord = true;
					}

					cellIdxCompanyData = cellIdxCompany;
					Row rowCompany = sheet.getRow(row);
					if (rowCompany == null) {
						rowCompany = sheet.createRow(row);
					}
					Cell cellCompany = rowCompany.getCell(cellIdxCompanyData);
					if (cellCompany == null) {
						cellCompany = rowCompany.createCell(cellIdxCompanyData);
					}
					setColumnValue(cellCompany, key, sheet, cellIdxCompanyData, ges, isLastRecord, "LEFT");
					Map<String, Map<String, Double>> metricValue = valueMap.get(key);
					cellIdxCompanyData = cellIdxCompanyData + 1;
					for (int j = 0; j < metricList.size(); j++) {
						Map<String, Double> perIndustryValue = metricValue.get(metricList.get(j));
						for (int a = 0; a < industryList.size(); a++) {
							cellCompany = rowCompany.getCell(cellIdxCompanyData);
							if (cellCompany == null) {
								cellCompany = rowCompany.createCell(cellIdxCompanyData);
							}
							Double cellVal = perIndustryValue.get(industryList.get(a));
							if (cellVal == null) {
								setColumnValue(cellCompany, "-", sheet, cellIdxCompanyData, ges, isLastRecord, "RIGHT");
							} else {
								cellCompany.setCellValue(cellVal);
								cellCompany.setCellStyle(getNumberFormatStyle(cellVal, "", ges, isLastRecord));
							}
							cellIdxCompanyData = cellIdxCompanyData + 1;
						}
					}
					row = row + 1;
					k = k + 1;
				}
				isLastRecord = false;
				// cellIdxCompany =
				// cellIdxCompany+metricList.size()+industryList.size()+3;
				/*
				 * cellIdxCompany =
				 * cellIdxCompany+metricList.size()*industryList.size()+1;
				 * cellIdxCompanyData = cellIdxCompanyData+3;
				 */
				cellIdxCompany = cellIdxCompany + (metricList.size() * industryList.size()) + 2;
				cellIdxCompanyData = cellIdxCompanyData + 3;

			}
			// cellIdxCompany = cellIdxCompany+1;
			/*
			 * cellIdxCompany =cellIdxCompany+10; cellIdxCompanyData
			 * =cellIdxCompanyData+10;
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createCompanyHeaderForIndustry(String countryName, String period, List<String> metricList,
			List<String> industryList, int cellIdxCompany, HSSFWorkbook workbook, String sheetName, int row,
			GenerateExcelStyle ges) {
		try {

			HSSFSheet sheet = workbook.getSheet(sheetName);
			int metricSize = metricList.size();
			int industrySize = industryList.size();
			if (metricSize == 0) {
				metricSize = 1;
			}
			if (industrySize == 0) {
				industrySize = 1;
			}
			Row headerRow = sheet.getRow(row);
			if (headerRow == null) {
				headerRow = sheet.createRow(row);
			}
			Cell cellHeader = headerRow.getCell(cellIdxCompany);
			if (cellHeader == null) {
				cellHeader = headerRow.createCell(cellIdxCompany);
			}
			if (cellIdxCompany != 1)
				sheet.setColumnWidth(cellIdxCompany, 5000);
			ges.mergeCells(sheet, row, row + 2, cellIdxCompany, cellIdxCompany, true);
			cellHeader.setCellValue(period);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIdxCompany = cellIdxCompany + 1;

			cellHeader = headerRow.getCell(cellIdxCompany);
			if (cellHeader == null) {
				cellHeader = headerRow.createCell(cellIdxCompany);
			}
			if (cellIdxCompany != 1)
				sheet.setColumnWidth(cellIdxCompany, 5000 * (metricSize) * (industrySize));
			cellHeader.setCellValue(countryName);
			ges.mergeCells(sheet, row, row, cellIdxCompany, cellIdxCompany + (metricSize * industrySize) - 1, true);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			row = row + 1;
			Row companyRow = sheet.getRow(row);
			if (companyRow == null) {
				companyRow = sheet.createRow(row);
			}
			for (int j = 0; j < metricList.size(); j++) {
				cellHeader = companyRow.getCell(cellIdxCompany);
				if (cellHeader == null) {
					cellHeader = companyRow.createCell(cellIdxCompany);
				}
				if (cellIdxCompany != 1)
					sheet.setColumnWidth(cellIdxCompany, 5000 * industrySize);
				cellHeader.setCellValue(metricList.get(j));
				ges.mergeCells(sheet, row, row, cellIdxCompany, cellIdxCompany + industrySize - 1, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row industryRow = sheet.getRow(row + 1);
				if (null == industryRow) {
					industryRow = sheet.createRow(row + 1);
				}
				for (int k = 0; k < industryList.size(); k++) {
					cellHeader = industryRow.getCell(cellIdxCompany);
					if (null == cellHeader) {
						cellHeader = industryRow.createCell(cellIdxCompany);
					}
					if (cellIdxCompany != 1)
						sheet.setColumnWidth(cellIdxCompany, 5000);
					cellHeader.setCellValue(industryList.get(k));
					cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
					cellIdxCompany = cellIdxCompany + 1;
				}
				// cellIdxCompany = cellIdxCompany+industrySize;
			}
			// cellIdxCompany = cellIdxCompany+1;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}

	}

	private void createAndFillEconomySheet(HSSFWorkbook workbook, List<IcEconomyResponseModel> icEconomyResponseData,
			IcEconomyRequestModel economyRequest, String sheetName, GenerateExcelStyle ges)
			throws JsonProcessingException {
		_log.info("creating the Economy Sheet data ");
		try {
			int rowdesign = 0;
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int rowStart = 4;

			List<IndicatorHistoricalDataDTO> indicatorDataList = icEconomyResponseData.get(0).getIndicatorsDataList();

			Map<String, List<IndicatorHistoricalDataDTO>> economyMap = new HashMap<>();
			economyMap = indicatorDataList.stream()
					.collect(Collectors.groupingBy(IndicatorHistoricalDataDTO::getCategory));

			Map<String, Map<String, Map<String, Double>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, Double>>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
			// Map<String,List<String>> headerMap = new
			// LinkedHashMap<String,List<String>>();
			List<String> indicatorList = new ArrayList<String>();
			for (int i = 0; i < economyMap.size(); i++) {
				String category = economyMap.keySet().toArray()[i].toString();
				/*
				 * if(!headerMap.containsKey(category)){ headerMap.put(category,
				 * new ArrayList<String>()); } List<String> indicatorList =
				 * headerMap.get(category);
				 */
				List<IndicatorHistoricalDataDTO> indicatorData = economyMap.get(category);
				Map<String, Map<String, Double>> valuesMap = new HashMap<String, Map<String, Double>>();
				for (int j = 0; j < indicatorData.size(); j++) {
					String itemName = indicatorData.get(j).getCountry();
					/*
					 * if(indicatorData.get(j).getUnit()!=null &&
					 * !indicatorData.get(j).getUnit().equals("")){ itemName =
					 * itemName+"("+indicatorData.get(j).getUnit()+")"; }
					 */

					if (!valuesMap.containsKey(sdf.format(indicatorData.get(j).getPeriod()))) {
						valuesMap.put(sdf.format(indicatorData.get(j).getPeriod()), new HashMap<String, Double>());
					}
					Map<String, Double> metricValueMap = valuesMap.get(sdf.format(indicatorData.get(j).getPeriod()));
					if (!metricValueMap.containsKey(itemName)) {
						metricValueMap.put(itemName, indicatorData.get(j).getData());
					}
					metricValueMap.put(itemName, indicatorData.get(j).getData());
					valuesMap.put(sdf.format(indicatorData.get(j).getPeriod()), metricValueMap);
					if (!indicatorList.contains(itemName)) {
						indicatorList.add(itemName);
					}
				}
				// headerMap.put(category, indicatorList);
				String categoryName = economyMap.keySet().toArray()[i].toString();
				if (indicatorData != null && indicatorData.size() > 0) {
					if (indicatorData.get(0).getPeriodType() != null
							&& !indicatorData.get(0).getPeriodType().equals("")) {
						categoryName = categoryName + " (" + indicatorData.get(0).getPeriodType() + ")";
					}
					if (indicatorData.get(0).getUnit() != null && !indicatorData.get(0).getUnit().equals("")) {
						categoryName = categoryName + " (" + indicatorData.get(0).getUnit() + ")";
					}
				}
				dataMap.put(categoryName, valuesMap);
			}
			_log.debug(economyMap);

			Boolean isLastRecord = false;

			int cellIdxCompany = 1;
			int cellIdxCompanyData = 1;
			for (int i = 0; i < dataMap.size(); i++) {
				int row = rowStart + 3;

				String category = dataMap.keySet().toArray()[i].toString();
				// List<String> metrics = headerMap.get(category);
				createCompanyHeader(category, "Period", indicatorList, cellIdxCompany, workbook, sheetName, row, ges);

				row = row + 3;

				Map<String, Map<String, Double>> valueMap = dataMap.get(category);
				// _log.info(valueMap);
				// SortedSet<String> keys = new TreeSet<>(valueMap.keySet());
				List<String> keys = new ArrayList<String>(valueMap.keySet());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyy");
				Collections.sort(keys,
						(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));

				// _log.info(keys);
				int k = 0;
				for (String key : keys) {
					if (k == keys.size() - 1) {
						isLastRecord = true;
					}
					cellIdxCompanyData = cellIdxCompany;
					Row rowCompany = sheet.getRow(row);
					if (rowCompany == null) {
						rowCompany = sheet.createRow(row);
					}
					Cell cellCompany = rowCompany.getCell(cellIdxCompanyData);
					if (cellCompany == null) {
						cellCompany = rowCompany.createCell(cellIdxCompanyData);
					}
					setColumnValue(cellCompany, key, sheet, cellIdxCompanyData, ges, isLastRecord, "LEFT");
					Map<String, Double> metricValue = valueMap.get(key);
					cellIdxCompanyData = cellIdxCompanyData + 1;
					for (int j = 0; j < indicatorList.size(); j++) {
						cellCompany = rowCompany.getCell(cellIdxCompanyData);
						if (cellCompany == null) {
							cellCompany = rowCompany.createCell(cellIdxCompanyData);
						}
						Double cellVal = metricValue.get(indicatorList.get(j));
						if (cellVal == null) {
							setColumnValue(cellCompany, "-", sheet, cellIdxCompanyData, ges, isLastRecord, "RIGHT");
						} else {
							cellCompany.setCellValue(cellVal);
							cellCompany.setCellStyle(getNumberFormatStyle(cellVal, "", ges, isLastRecord));
						}
						cellIdxCompanyData = cellIdxCompanyData + 1;
					}
					row = row + 1;
					k = k + 1;
				}
				isLastRecord = false;
				cellIdxCompany = cellIdxCompany + indicatorList.size() + 2;
				cellIdxCompanyData = cellIdxCompanyData + 2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	private void createAndFillForexSheet(HSSFWorkbook workbook, List<IcEconomyResponseModel> icEconomyResponseData,
			IcEconomyRequestModel economyRequest, String sheetName, GenerateExcelStyle ges)
			throws JsonProcessingException {
		_log.info("creating the Forex Sheet data ");
		try {
			int rowdesign = 0;
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int rowStart = 4;

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");

			Map<String, List<String>> headerMap = new HashMap<String, List<String>>();
			List<String> countryList = new ArrayList<String>();
			Map<String, Map<String, Map<String, Double>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, Double>>>();
			for (int i = 0; i < icEconomyResponseData.size(); i++) {
				List<List<ExchangeRatesComparison>> exchangeRateList = icEconomyResponseData.get(i).getExchangeRate();
				String country = icEconomyResponseData.get(i).getCountryIsoCode();
				Map<String, Map<String, Double>> valuesMap = new HashMap<String, Map<String, Double>>();
				for (List<ExchangeRatesComparison> exchangeData : exchangeRateList) {
					for (int j = 0; j < exchangeData.size(); j++) {
						String itemName = exchangeData.get(j).getTargetCurrencyCode() + "/"
								+ exchangeData.get(j).getSourceCurrencyCode() + " ("
								+ exchangeData.get(j).getPeriodType() + ")";
						if (!valuesMap.containsKey(sdf.format(exchangeData.get(j).getPeriod()))) {
							valuesMap.put(sdf.format(exchangeData.get(j).getPeriod()), new HashMap<String, Double>());
						}
						Map<String, Double> metricValueMap = valuesMap.get(sdf.format(exchangeData.get(j).getPeriod()));
						if (!metricValueMap.containsKey(itemName)) {
							metricValueMap.put(itemName, exchangeData.get(j).getData());
						}
						metricValueMap.put(itemName, exchangeData.get(j).getData());
						valuesMap.put(sdf.format(exchangeData.get(j).getPeriod()), metricValueMap);
						if (!headerMap.containsKey(country)) {
							headerMap.put(country, new ArrayList<String>());
						}
						List<String> headerList = headerMap.get(country);
						if (!headerList.contains(itemName)) {
							headerList.add(itemName);
						}
						headerMap.put(country, headerList);
						if (!countryList.contains(country)) {
							countryList.add(country);
						}
					}
				}
				dataMap.put(country, valuesMap);
			}

			/*
			 * List<IndicatorHistoricalDataDTO> indicatorDataList =
			 * icEconomyResponseData.get(0).getIndicatorsDataList();
			 * 
			 * Map<String, List<IndicatorHistoricalDataDTO>> economyMap = new
			 * HashMap<>(); economyMap =
			 * indicatorDataList.stream().collect(Collectors.groupingBy(
			 * IndicatorHistoricalDataDTO::getCategory));
			 * 
			 * 
			 * 
			 * //Map<String,List<String>> headerMap = new
			 * LinkedHashMap<String,List<String>>(); List<String> indicatorList
			 * = new ArrayList<String>(); for(int i=0;i<economyMap.size();i++){
			 * String category = economyMap.keySet().toArray()[i].toString();
			 * if(!headerMap.containsKey(category)){ headerMap.put(category, new
			 * ArrayList<String>()); } List<String> indicatorList =
			 * headerMap.get(category); List<IndicatorHistoricalDataDTO>
			 * indicatorData = economyMap.get(category);
			 * Map<String,Map<String,Double>> valuesMap = new
			 * HashMap<String,Map<String,Double>>(); for(int
			 * j=0;j<indicatorData.size();j++){ String itemName =
			 * indicatorData.get(j).getCountry();
			 * if(indicatorData.get(j).getUnit()!=null &&
			 * !indicatorData.get(j).getUnit().equals("")){ itemName =
			 * itemName+"("+indicatorData.get(j).getUnit()+")"; }
			 * 
			 * if(!valuesMap.containsKey(sdf.format(indicatorData.get(j).
			 * getPeriod()))){
			 * valuesMap.put(sdf.format(indicatorData.get(j).getPeriod()), new
			 * HashMap<String,Double>()); } Map<String,Double> metricValueMap =
			 * valuesMap.get(sdf.format(indicatorData.get(j).getPeriod()));
			 * if(!metricValueMap.containsKey(itemName)){
			 * metricValueMap.put(itemName, indicatorData.get(j).getData()); }
			 * metricValueMap.put(itemName, indicatorData.get(j).getData());
			 * valuesMap.put(sdf.format(indicatorData.get(j).getPeriod()),
			 * metricValueMap); if(!indicatorList.contains(itemName)){
			 * indicatorList.add(itemName); } } //headerMap.put(category,
			 * indicatorList); String categoryName =
			 * economyMap.keySet().toArray()[i].toString();
			 * if(indicatorData!=null && indicatorData.size()>0){
			 * if(indicatorData.get(0).getPeriodType()!=null &&
			 * !indicatorData.get(0).getPeriodType().equals("")){ categoryName =
			 * categoryName+" ("+indicatorData.get(0).getPeriodType()+")"; }
			 * if(indicatorData.get(0).getUnit()!=null &&
			 * !indicatorData.get(0).getUnit().equals("")){ categoryName =
			 * categoryName+" ("+indicatorData.get(0).getUnit()+")"; } }
			 * dataMap.put(categoryName, valuesMap); }
			 */
			_log.debug(dataMap);
			// List<CountryListDTO> ctList =
			// cmRepository.getCountryList(countryList);
			List<CountryListDTO> ctList = economyRepository.findCountriesBySubscribedCountry(countryList);
			Map<String, CountryListDTO> countryMap = ctList.stream()
					.collect(Collectors.toMap(CountryListDTO::getCountryIsoCode3, Function.identity()));
			Boolean isLastRecord = false;

			int cellIdxCompany = 1;
			int cellIdxCompanyData = 1;
			for (int i = 0; i < dataMap.size(); i++) {
				int row = rowStart + 3;

				String category = dataMap.keySet().toArray()[i].toString();
				List<String> metrics = headerMap.get(category);
				String countryName = countryMap.get(category).getCountryName();
				createCompanyHeader(countryName, "Period", metrics, cellIdxCompany, workbook, sheetName, row, ges);

				row = row + 3;

				Map<String, Map<String, Double>> valueMap = dataMap.get(category);
				// _log.info(valueMap);
				// SortedSet<String> keys = new TreeSet<>(valueMap.keySet());
				List<String> keys = new ArrayList<String>(valueMap.keySet());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyy");
				Collections.sort(keys,
						(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));

				// _log.info(keys);
				int k = 0;
				for (String key : keys) {
					if (k == keys.size() - 1) {
						isLastRecord = true;
					}
					cellIdxCompanyData = cellIdxCompany;
					Row rowCompany = sheet.getRow(row);
					if (null == rowCompany) {
						rowCompany = sheet.createRow(row);
					}
					// System.out.println("printing for "+category+" at "+key);
					Cell cellCompany = rowCompany.getCell(cellIdxCompanyData);
					if (cellCompany == null) {
						cellCompany = rowCompany.createCell(cellIdxCompanyData);
					}
					setColumnValue(cellCompany, key, sheet, cellIdxCompanyData, ges, isLastRecord, "LEFT");
					Map<String, Double> metricValue = valueMap.get(key);
					cellIdxCompanyData = cellIdxCompanyData + 1;
					for (int j = 0; j < metrics.size(); j++) {
						cellCompany = rowCompany.getCell(cellIdxCompanyData);
						if (cellCompany == null) {
							cellCompany = rowCompany.createCell(cellIdxCompanyData);
						}
						Double cellVal = metricValue.get(metrics.get(j));
						if (cellVal == null) {
							setColumnValue(cellCompany, "-", sheet, cellIdxCompanyData, ges, isLastRecord, "RIGHT");
						} else {
							cellCompany.setCellValue(cellVal);
							cellCompany.setCellStyle(getNumberFormatStyle(cellVal, "", ges, isLastRecord));
						}
						cellIdxCompanyData = cellIdxCompanyData + 1;
					}
					row = row + 1;
					k = k + 1;
				}
				isLastRecord = false;
				cellIdxCompany = cellIdxCompany + metrics.size() + 2;
				cellIdxCompanyData = cellIdxCompanyData + 2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	private Map<String, String> getCompanyInfo(List<CompanyDTO> icCompanyData) {
		Map<String, String> companyInfo = new HashMap<String, String>();
		for (int i = 0; i < icCompanyData.size(); i++) {
			if (!companyInfo.containsKey(icCompanyData.get(i).getId())) {
				companyInfo.put((String) icCompanyData.get(i).getId(),
						icCompanyData.get(i).getName() + "#:#" + icCompanyData.get(i).getExchangeName());
			} else {
				companyInfo.put((String) icCompanyData.get(i).getId(),
						icCompanyData.get(i).getName() + "#:#" + icCompanyData.get(i).getExchangeName());
			}
		}
		return companyInfo;
	}

	private void createAndFillPriceMetricSheet(HSSFWorkbook workbook, List<IcStockResponseModel> icStockResponseData,
			IcStockRequestModel icStockRequestModel, Map<String, String> companyMap, String sheetName,
			GenerateExcelStyle ges) throws JsonProcessingException {

		_log.info("creating the Price Metric sheet data ");

		try {

			int rowdesign = 0;
			int companySize = icStockResponseData.size();
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);

			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);

			int row = 4;
			int cellIndex = 1;

			Row headerRow = sheet.getRow(row);

			if (headerRow == null) {
				headerRow = sheet.createRow(row);
			}

			List<String> headerList = new ArrayList<String>();

			headerList.add("Date");

			if (icStockRequestModel.getFieldName().contains("stock")) {
				String str = "Stock Price (Closing) (" + icStockRequestModel.getCurrency() + ")";
				headerList.add(str);
			}
			if (icStockRequestModel.getFieldName().contains("volume")) {
				String str = "Stock Volume";
				headerList.add(str);
			}

			// Map<String, Map<String,List<Double>>> dataMap = new
			// LinkedHashMap<String,Map<String,List<Double>>>();
			Map<String, Map<String, Map<String, Double>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, Double>>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
			List<String> companyIds = new ArrayList<String>();

			for (int j = 0; j < icStockResponseData.size(); j++) {

				List<StockPriceDTO> stockData = icStockResponseData.get(j).getStockDataList();

				if (!companyIds.contains(icStockResponseData.get(j).getCompanyId())) {
					companyIds.add(icStockResponseData.get(j).getCompanyId());
				}

				for (int i = 0; i < stockData.size(); i++) {
					if (!dataMap.containsKey(sdf.format(stockData.get(i).getDate()))) {
						dataMap.put(sdf.format(stockData.get(i).getDate()), new HashMap<String, Map<String, Double>>());
					}
					Map<String, Double> stockInfo = new HashMap<String, Double>();
					// List<Double> stockInfo = new ArrayList<Double>();
					if (icStockRequestModel.getFieldName().contains("stock")) {
						String str = "Stock Price (Closing) (" + icStockRequestModel.getCurrency() + ")";
						stockInfo.put(str, stockData.get(i).getClose());
					}
					if (icStockRequestModel.getFieldName().contains("volume")) {
						String str = "Stock Volume";
						stockInfo.put(str, stockData.get(i).getVolume());
					}
					// stockInfo.add(stockData.get(i).getClose());
					// stockInfo.add(stockData.get(i).getVolume());
					dataMap.get(sdf.format(stockData.get(i).getDate())).put(stockData.get(i).getCompanyId(), stockInfo);
				}
			}

			Iterator itr = headerList.iterator();

			while (itr.hasNext()) {

				Cell cellHeader = headerRow.getCell(cellIndex);

				if (null == cellHeader) {
					cellHeader = headerRow.createCell(cellIndex);
				}

				String headCol = (String) itr.next();

				if (headCol.equalsIgnoreCase("Date")) {
					if (cellIndex != 1)
						sheet.setColumnWidth(cellIndex, 5000);
					ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
					cellHeader.setCellValue(headCol);
					cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
					cellIndex = cellIndex + 1;
				} else {

					if (cellIndex != 1)
						sheet.setColumnWidth(cellIndex, 5000 * companySize);
					cellHeader.setCellValue(headCol);
					ges.mergeCells(sheet, row, row, cellIndex, cellIndex + companySize - 1, true);
					cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);

					Row companyRow = sheet.getRow(row + 1);

					if (companyRow == null) {
						companyRow = sheet.createRow(row + 1);
					}

					for (int j = 0; j < icStockResponseData.size(); j++) {

						cellHeader = companyRow.getCell(cellIndex);

						if (null == cellHeader) {
							cellHeader = companyRow.createCell(cellIndex);
						}
						if (cellIndex != 1)
							sheet.setColumnWidth(cellIndex, 5000);

						String companyInfo = companyMap.get(icStockResponseData.get(j).getCompanyId());
						String companyName = companyInfo.split("#:#")[0];
						String exchangeName = companyInfo.split("#:#")[1];
						cellHeader.setCellValue(companyName + "(" + exchangeName + ")");
						// cellHeader.setCellValue(icStockResponseData.get(j).getCompanyId());
						cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
						cellIndex = cellIndex + 1;
					}
				}
			}

			List<String> keys = new ArrayList<String>(dataMap.keySet());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyy");

			Collections.sort(keys,
					(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));

			Boolean isLastRecord = false;

			row = row + 2;

			for (int i = 0; i < keys.size(); i++) {

				if (i == keys.size() - 1) {
					isLastRecord = true;
				}
				/*
				 * for(int i=0;i<dataMap.size();i++){ if(i==dataMap.size()-1){
				 * isLastRecord = true; }
				 */
				Row rowDebt = sheet.getRow(row);

				if (rowDebt == null) {
					rowDebt = sheet.createRow(row);
				}

				int cellIdxDebt = 1;

				Cell cellDebt = rowDebt.getCell(cellIdxDebt);

				if (null == cellDebt) {
					cellDebt = rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt, keys.get(i), sheet, cellIdxDebt, ges, isLastRecord, "LEFT");

				for (int j = 0; j < headerList.size(); j++) {

					if (headerList.get(j).equalsIgnoreCase("Date")) {
						continue;
					}

					for (int k = 0; k < companyIds.size(); k++) {

						cellIdxDebt = cellIdxDebt + 1;

						cellDebt = rowDebt.getCell(cellIdxDebt);

						if (null == cellDebt) {
							cellDebt = rowDebt.createCell(cellIdxDebt);
						}

						Map<String, Double> stockInfo = dataMap.get(keys.get(i)).get(companyIds.get(k));

						Double cellVal = null;

						if (stockInfo == null) {
							cellVal = null;
						} else {
							cellVal = stockInfo.get(headerList.get(j));
						}

						if (cellVal == null) {
							setColumnValue(cellDebt, "-", sheet, cellIdxDebt, ges, isLastRecord, "RIGHT");
						} else {

							cellDebt.setCellValue(cellVal);
							cellDebt.setCellStyle(getNumberFormatStyle(cellVal, "", ges, isLastRecord));
						}
					}
				}

				row++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}

	}

	private void createAndFillCompanySheet(HSSFWorkbook workbook, List<IcStockResponseModel> icFinancialResponseData,
			List<IcStockRequestModel> request, Map<String, String> companyInfo, String sheetName,
			GenerateExcelStyle ges) throws JsonProcessingException {
		_log.info("creating the Company sheet data ");

		try {
			int rowdesign = 0;
			int companySize = icFinancialResponseData.size();
			String period = request.get(0).getPeriodicity();
			if (period.toLowerCase().contains("year")) {
				period = "Year";
			} else {
				period = "Quarter";
			}

			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int rowStart = 4;
			int cellIndex = 1;
			Row headerRow = sheet.getRow(rowStart);
			if (headerRow == null) {
				headerRow = sheet.createRow(rowStart);
			}

			Map<String, List<CompanyFinancialMINDTO>> companyMap = new HashMap<String, List<CompanyFinancialMINDTO>>();
			for (int j = 0; j < icFinancialResponseData.size(); j++) {
				List<CompanyFinancialMINDTO> financialData = icFinancialResponseData.get(j).getFinancialDataList();
				if (!companyMap.containsKey(icFinancialResponseData.get(j).getCompanyId())) {
					companyMap.put(icFinancialResponseData.get(j).getCompanyId(),
							new ArrayList<CompanyFinancialMINDTO>());
				}
				companyMap.get(icFinancialResponseData.get(j).getCompanyId()).addAll(financialData);
			}
			_log.debug(companyMap);
			Map<String, Map<String, Map<String, Double>>> dataMap = new LinkedHashMap<String, Map<String, Map<String, Double>>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
			Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();
			// List<String> metricList = new ArrayList<String>();
			for (int i = 0; i < companyMap.size(); i++) {
				String companyId = companyMap.keySet().toArray()[i].toString();
				List<CompanyFinancialMINDTO> companyFinancial = companyMap.get(companyId);
				Map<String, Map<String, Double>> valuesMap = new HashMap<String, Map<String, Double>>();

				if (!headerMap.containsKey(companyId)) {
					headerMap.put(companyId, new ArrayList<String>());
				}
				List<String> metricList = headerMap.get(companyId);

				for (int j = 0; j < companyFinancial.size(); j++) {
					String itemName = companyFinancial.get(j).getItemName();
					if (companyFinancial.get(j).getCurrency() != null
							&& !companyFinancial.get(j).getCurrency().equals("")) {
						itemName = itemName + "(" + companyFinancial.get(j).getCurrency();
						if (companyFinancial.get(j).getUnit() != null
								&& !companyFinancial.get(j).getUnit().equals("")) {
							itemName = itemName + " " + companyFinancial.get(j).getUnit();
						}
						itemName = itemName + ")";
					} else {
						if (companyFinancial.get(j).getUnit() != null
								&& !companyFinancial.get(j).getUnit().equals("")) {
							itemName = itemName + "(" + companyFinancial.get(j).getUnit() + ")";
						}
					}
					if (!valuesMap.containsKey(sdf.format(companyFinancial.get(j).getPeriod()))) {
						valuesMap.put(sdf.format(companyFinancial.get(j).getPeriod()), new HashMap<String, Double>());
					}
					Map<String, Double> metricValueMap = valuesMap.get(sdf.format(companyFinancial.get(j).getPeriod()));
					if (!metricValueMap.containsKey(itemName)) {
						metricValueMap.put(itemName, companyFinancial.get(j).getData());
					}
					metricValueMap.put(itemName, companyFinancial.get(j).getData());
					valuesMap.put(sdf.format(companyFinancial.get(j).getPeriod()), metricValueMap);
					if (!metricList.contains(itemName)) {
						metricList.add(itemName);
					}
				}
				headerMap.put(companyId, metricList);
				dataMap.put(companyMap.keySet().toArray()[i].toString(), valuesMap);
			}
			_log.debug(dataMap);

			Boolean isLastRecord = false;

			int cellIdxCompany = 1;
			int cellIdxCompanyData = 1;
			for (int i = 0; i < dataMap.size(); i++) {
				int row = rowStart + 3;

				String companyId = dataMap.keySet().toArray()[i].toString();
				String company = companyInfo.get(companyId);
				if (company != null) {
					String companyName = company.split("#:#")[0];
					String exchangeName = company.split("#:#")[1];
					companyName = companyName + "(" + exchangeName + ")";
					List<String> metricList = headerMap.get(headerMap.keySet().toArray()[i].toString());
					createCompanyHeader(companyName, period, metricList, cellIdxCompany, workbook, sheetName, row, ges);

					row = row + 3;

					Map<String, Map<String, Double>> valueMap = dataMap.get(companyId);
					// SortedSet<String> keys = new
					// TreeSet<>(valueMap.keySet());

					// SortedSet<String> keys = new
					// TreeSet<>(valueMap.keySet());
					List<String> keys = new ArrayList<String>(valueMap.keySet());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyy");
					Collections.sort(keys,
							(s1, s2) -> LocalDate.parse(s1, formatter).compareTo(LocalDate.parse(s2, formatter)));

					_log.debug(keys);

					int k = 0;
					for (String key : keys) {
						if (k == keys.size() - 1) {
							isLastRecord = true;
						}
						cellIdxCompanyData = cellIdxCompany;
						Row rowCompany = sheet.getRow(row);
						if (rowCompany == null) {
							rowCompany = sheet.createRow(row);
						}
						Cell cellCompany = rowCompany.getCell(cellIdxCompanyData);
						if (cellCompany == null) {
							cellCompany = rowCompany.createCell(cellIdxCompanyData);
						}
						setColumnValue(cellCompany, key, sheet, cellIdxCompanyData, ges, isLastRecord, "LEFT");
						Map<String, Double> metricValue = valueMap.get(key);
						cellIdxCompanyData = cellIdxCompanyData + 1;
						for (int j = 0; j < metricList.size(); j++) {
							cellCompany = rowCompany.getCell(cellIdxCompanyData);
							if (cellCompany == null) {
								cellCompany = rowCompany.createCell(cellIdxCompanyData);
							}
							Double cellVal = metricValue.get(metricList.get(j));
							if (cellVal == null) {
								setColumnValue(cellCompany, "-", sheet, cellIdxCompanyData, ges, isLastRecord, "RIGHT");
							} else {
								cellCompany.setCellValue(cellVal);
								cellCompany.setCellStyle(getNumberFormatStyle(cellVal, "", ges, isLastRecord));
							}
							cellIdxCompanyData = cellIdxCompanyData + 1;
						}
						row = row + 1;
						k = k + 1;

					}
					isLastRecord = false;
					cellIdxCompany = cellIdxCompany + metricList.size() + 2;
					cellIdxCompanyData = cellIdxCompanyData + 3;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}

	}

	private void createCompanyHeader(String companyName, String period, List<String> metrics, int cellIndex,
			HSSFWorkbook workbook, String sheetName, int row, GenerateExcelStyle ges) {
		try {
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int metricSize = metrics.size();
			Row headerRow = sheet.getRow(row);
			if (headerRow == null) {
				headerRow = sheet.createRow(row);
			}
			Cell cellHeader = headerRow.getCell(cellIndex);
			if (null == cellHeader) {
				cellHeader = headerRow.createCell(cellIndex);
			}
			if (cellIndex != 1)
				sheet.setColumnWidth(cellIndex, 5000);
			ges.mergeCells(sheet, row, row + 2, cellIndex, cellIndex, true);
			cellHeader.setCellValue(period);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIndex = cellIndex + 1;
			cellHeader = headerRow.getCell(cellIndex);

			if (null == cellHeader) {
				cellHeader = headerRow.createCell(cellIndex);
			}

			int headerWidth = 5000;
			int cellWidth = 5000;
			if (metricSize > 0) {
				if (metricSize == 1) {
					headerWidth = 10000;
					cellWidth = 10000;
				} else {
					headerWidth = 5000 * metricSize;
				}
			}

			sheet.setColumnWidth(cellIndex, headerWidth);
			if (cellIndex != 1)
				sheet.setColumnWidth(cellIndex, 5000 * (metricSize > 0 ? metricSize : 1));
			cellHeader.setCellValue(companyName);
			ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex + metricSize - 1, true);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			Row companyRow = sheet.getRow(row + 2);
			if (companyRow == null) {
				companyRow = sheet.createRow(row + 2);
			}
			for (int j = 0; j < metrics.size(); j++) {
				cellHeader = companyRow.getCell(cellIndex);
				if (null == cellHeader) {
					cellHeader = companyRow.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, cellWidth);
				if (cellIndex != 1)
					sheet.setColumnWidth(cellIndex, 5000);
				cellHeader.setCellValue(metrics.get(j));
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}

	}

	private void createLogo(Cell cell, HSSFSheet sheet, HSSFWorkbook workbook, GenerateExcelStyle ges) {
		short height = 800;
		cell.getRow().setHeight(height);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER);
		// SET LOGO IMAGE
		try {
			excelDesignService.readWorkBook(workbook);
			excelDesignService.setImage(sheet, cell.getRowIndex(), cell.getColumnIndex(), logoPath, 600, 600);
		} catch (IOException e) {

		}
	}

	private void setColumnValue(Cell cell, String data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges,
			Boolean isLastRecord, String Align) {
		if (data != null) {

			cell.setCellValue(data);
			// cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
			if (Align.equalsIgnoreCase("LEFT")) {
				if (isLastRecord) {
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
				} else {
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
				}
			} else {
				if (isLastRecord) {
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
				} else {
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if (isLastRecord) {
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			} else {
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}

		}

	}

	private void setColumnValue(Cell cell, Integer value, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ge,
			Boolean isLastRecord) {
		if (value != null) {
			cell.setCellValue(value);
			if (isLastRecord) {
				if (Math.abs(value) >= 1.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else if (Math.abs(value) < 10.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL);
				} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
				}

			} else {
				if (Math.abs(value) >= 1.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else if (Math.abs(value) < 10.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL);
				} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			// cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			if (isLastRecord) {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			} else {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}

	}

	private void setColumnValue(Cell cell, Date data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges,
			Boolean isLastRecord) {
		if (data != null) {
			DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String period = outputFormatter.format(data);
			cell.setCellValue(period);
			// cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
		} else {
			cell.setCellValue(CMStatic.NA);
			// cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
		}
		if (isLastRecord) {
			cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
		} else {
			cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
		}
	}

	public CellStyle getNumberFormatStyle(Double value, String unit, GenerateExcelStyle ge, Boolean isLastRecord) {
		if (isLastRecord) {
			if (unit.contains("%") && value >= 1.0) {
				return ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else if (unit.equals("x")) {
				return ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (value < 10.0) {
				return ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (value >= 10.0 && value <= 999.0) {
				return ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else {
				return ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
			}

		} else {
			if (unit.contains("%") && value >= 1.0) {
				return ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else if (unit.equals("x")) {
				return ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (value < 10.0) {
				return ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (value >= 10.0 && value <= 999.0) {
				return ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else {
				return ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
			}
		}
	}
}
