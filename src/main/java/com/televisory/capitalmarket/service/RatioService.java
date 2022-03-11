package com.televisory.capitalmarket.service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.ScreenerStockPriceDTO;
import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.model.IcStockResponseModel;
import com.televisory.capitalmarket.model.RatioCompanyData;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class RatioService {
	Logger _log = Logger.getLogger(RatioService.class);

	@Autowired
	CMFinancialDataService cmFinancialDataService;

	@Autowired
	RatioServiceBank bankRatioCalculateService;
	
	@Autowired
	ExecutorService executorPool;

	@Autowired
	RatioServiceInsurance insuranceRatioCalculateService;

	@Autowired
	RatioServiceIndustry ratioServiceIndustry;

	@Autowired
	RatioServiceOther ratioServiceOther;

	@Autowired
	CMStockService cmStockService;

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	CMRepository cmRepository;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	public List<CompanyFinancialDTO> getCompanyRatio(String industryType, String companyId, ScreenerStockPriceDTO stockPrice, FFBasicCfDTO totalShare,List<BalanceModelDTO> ratios, HashMap<String,Double> financialDataMap, String periodType, RatioCompanyData companyData/*List<String> availPeriod*/, String currency, String unit,Date startDate, Date endDate) {
		//Collections.reverse(availPeriod);
		List<String> availPeriod = companyData.getPeriods();
		_log.debug(" company :: "+companyId +" availPeriod :: "+companyData.getPeriods()+" stockPrice :: "+stockPrice+" totalShare :: "+totalShare);

		List<CompanyFinancialMINDTO> financialDataMIN = new ArrayList<CompanyFinancialMINDTO>();
		try {
			Double totalShares;
			Double closePrice;
			int j=0;
			int i=0;
			//for(int i=0;i<availPeriod.size();i++) {
			j=0;
			totalShares = null;
			closePrice = null;
			try {
				//closePrice = cmStockService.getCompanyStockPrice(companyId, availPeriod.get(i),null).getClose();
				closePrice = stockPrice.getData();
			} catch (Exception e) {
				_log.warn("stock price not found for companyId: "+ companyId +", period: "+ availPeriod.get(i));
			}

			try {
				//totalShares = (Double) cmStockService.getCompanyTotalSharesByPeriod(companyId,availPeriod.get(i)).get(0).get("total_shares");
				totalShares = (Double)totalShare.getShareOutStanding();
			} catch (Exception e) {
				_log.warn("totalShares not found for companyId: "+ companyId +", period: "+ availPeriod.get(i));
			}

			for(int index=0 ; index< ratios.size();index++) {
				Class ratiosCalculation[] = new Class[14];
				ratiosCalculation[0] = String.class;
				ratiosCalculation[1] = HashMap.class;
				ratiosCalculation[2] = Double.class;
				ratiosCalculation[3] = Double.class;
				ratiosCalculation[4] = List.class;
				ratiosCalculation[5] = String.class;
				ratiosCalculation[6] = String.class;
				ratiosCalculation[7] = String.class;
				ratiosCalculation[8] = int.class;
				ratiosCalculation[9] = String.class;
				ratiosCalculation[10] = String.class;
				ratiosCalculation[11] = String.class;
				ratiosCalculation[12] = BalanceModelDTO.class;
				ratiosCalculation[13] = RatioCompanyData.class;

				Method methRatioCalc;
				Object arglistRatioCalc[] = new Object[14];
				arglistRatioCalc[0] = companyId;
				arglistRatioCalc[1] = financialDataMap;
				arglistRatioCalc[2] = closePrice;
				arglistRatioCalc[3] = totalShares;
				arglistRatioCalc[4] = availPeriod;
				arglistRatioCalc[5] = availPeriod.get(i);
				arglistRatioCalc[6] = availPeriod.get(i);
				arglistRatioCalc[7] = periodType;
				arglistRatioCalc[8] = j++;
				arglistRatioCalc[9] = currency;
				arglistRatioCalc[10] = unit;
				arglistRatioCalc[11] = null;
				arglistRatioCalc[12] = ratios.get(index);
				arglistRatioCalc[13] = companyData;

				try {
					if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_BANK)) {
						methRatioCalc = RatioServiceBank.class.getDeclaredMethod(ratios.get(index).getFieldName(), ratiosCalculation);
						financialDataMIN.add((CompanyFinancialMINDTO)methRatioCalc.invoke(bankRatioCalculateService, arglistRatioCalc));
					}
					else if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_INSURANCE)) {
						methRatioCalc = RatioServiceInsurance.class.getDeclaredMethod(ratios.get(index).getFieldName(), ratiosCalculation);
						financialDataMIN.add((CompanyFinancialMINDTO)methRatioCalc.invoke(insuranceRatioCalculateService, arglistRatioCalc));
					}
					else if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_OTHER)) {
						methRatioCalc = RatioServiceOther.class.getDeclaredMethod(ratios.get(index).getFieldName(), ratiosCalculation);
						financialDataMIN.add((CompanyFinancialMINDTO)methRatioCalc.invoke(ratioServiceOther, arglistRatioCalc));
					}else {
						methRatioCalc = RatioServiceIndustry.class.getDeclaredMethod(ratios.get(index).getFieldName(), ratiosCalculation);
						financialDataMIN.add((CompanyFinancialMINDTO)methRatioCalc.invoke(ratioServiceIndustry, arglistRatioCalc));
					}	
				} catch(Exception ee) {
					_log.error("Problem occured in calculating ratio: "+ ratios.get(index).getFieldName() +" for company: "+ companyId);
					ee.printStackTrace();
				}
			}
			//}
		}catch(Exception e) {
			e.printStackTrace();
		}

		//List<CompanyFinancialDTO> financialData = DozerHelper.map(dozerBeanMapper, financialDataMIN, CompanyFinancialDTO.class);

		List<CompanyFinancialDTO> financialData =  financialDataMIN.stream().map(a -> {
			CompanyFinancialDTO c = new CompanyFinancialDTO();
			c.setId(a.getId());
			c.setPeriodType(a.getPeriodType());
			c.setPeriod(a.getPeriod());
			c.setDepthLevel(a.getDepthLevel());
			c.setMandatory(a.getMandatory());
			c.setDisplayOrder(a.getDisplayOrder());
			c.setFinancialType(a.getFinancialType());
			c.setFieldName(a.getFieldName());
			c.setSection(a.getSection());
			c.setShortName(a.getShortName());
			c.setItemName(a.getItemName());
			c.setDisplayName(a.getDisplayName());
			c.setData(a.getData());
			c.setUnit(a.getUnit());
			c.setCurrency(a.getCurrency());
			c.setCompanyId(a.getCompanyId());
			c.setKeyParameter(a.getKeyParameter());
			c.setKeyParameterOrder(a.getKeyParameterOrder());
			c.setCompanyName(companyData.getCompanyName());
			c.setCountryId(companyData.getCountryId());
			c.setCountryName(companyData.getCountryName());
			return c;
		}
				).collect(Collectors.toList());

		return financialData;
	}



	public List<CompanyFinancialMINDTO> getCompanyRatio(String companyId, List<String> ratioList, String periodType, Date startDate, Date endDate, String targetCurrency ) {

		List<CompanyFinancialMINDTO> financialRatioData = new ArrayList<CompanyFinancialMINDTO>();
		if(ratioList!=null && !ratioList.isEmpty()){
		
			CompanyFinancialMINDTO ratioData;
			
			try {
				
				String industryType = capitalMarketService.getCMCompaniesById(companyId).getFf_industry()/*.replaceAll(" ", "")*/;
				List<BalanceModelDTO> balanceModelDTOs = getBalanceModel(industryType, ratioList);
				List<CompanyFinancialMINDTO> allFinancialData = null;
				Set<String> distinctFields = new HashSet<String>();
				String fieldNames = "";
				RatioCompanyData companyData = new RatioCompanyData();

				if(balanceModelDTOs!=null && !balanceModelDTOs.isEmpty()) {
					for(BalanceModelDTO ratios : balanceModelDTOs) {
						if(ratios.getFinancialField() != null && !ratios.getFinancialField().equals("")) {
							//fieldNames += ratios.getFinancialField()+",";
							distinctFields.addAll(Arrays.asList(ratios.getFinancialField().split(",")));
						}
					}
				}

				if(distinctFields!=null && distinctFields.size()!=0) {
					for(String field : distinctFields) {
						fieldNames += field.trim()+",";
					}
				}

				if(!fieldNames.equals("")) {
					fieldNames = fieldNames.substring(0, fieldNames.length()-1);
					allFinancialData = getAllCompanyFinancial(companyId, fieldNames, periodType, startDate, endDate, null);
				}

				HashMap<String,Double> financialDataMap = new HashMap<String,Double>();
				HashMap<String, String> applicablePeriodMap = new HashMap<String, String>();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				TreeSet<String> periods = new TreeSet<>();
				String currency = null;
				String unit = null;
				String latestAnnualDate = null;
				if(allFinancialData!=null && allFinancialData.size()>0) {
					currency = allFinancialData.get(0).getCurrency();
					unit 	 = allFinancialData.get(0).getUnit();
					if(allFinancialData.get(0).getLatestAnnualDate() != null)
						latestAnnualDate = df.format(allFinancialData.get(0).getLatestAnnualDate());
					/*companyData.setCompanyName(allFinancialData.get(0).getCompanyName());
				companyData.setCountryId(allFinancialData.get(0).getCountryId());
				companyData.setCountryName(allFinancialData.get(0).getCountryName());*/
					allFinancialData.forEach(fData -> {
						//industryRatio.put(df.format(fData.getPeriod())+"_"+fData.getFieldName(), fData.getData());
						financialDataMap.put(fData.getCompanyId()+"_"+df.format(fData.getPeriod())+"_"+fData.getFieldName(), fData.getData());
						periods.add(df.format(fData.getPeriod()));
						applicablePeriodMap.put(df.format(fData.getPeriod()), df.format(fData.getApplicablePeriod()));
					});
				}

				periods.descendingSet();
				List<String> availPeriod = new ArrayList<String>(periods);
				Collections.reverse(availPeriod);
				_log.debug(" availPeriod  "+availPeriod );

				Double totalShares;
				Double closePrice;

				int j=0;
				Double fx = null;
				Double fxAsOnDate = null;
				
				List<Callable<Double>> taskList = new ArrayList<>();
				
				for(int i=0; i < periods.size(); i++) {
				
					j=0;
					totalShares = null;
					closePrice = null;

					// // fx = cmRepository.getFxData(periodType, availPeriod.get(i), currency, targetCurrency);
					// // fxAsOnDate = cmRepository.getFxData(CMStatic.PERIODICITY_DAILY, availPeriod.get(i), currency, targetCurrency);
					
					AtomicInteger loopCounter = new AtomicInteger(i);
					AtomicReference<String> currencyVal =new AtomicReference<>(currency);
					
					Future<Double> fxFuture = executorPool.submit(() ->{
						return cmRepository.getFxData(periodType, availPeriod.get(loopCounter.get()), currencyVal.get(), targetCurrency);
					});
					
					Future<Double> fxAsOnDateFuture = executorPool.submit(() ->{
						return cmRepository.getFxData(CMStatic.PERIODICITY_DAILY, availPeriod.get(loopCounter.get()),  currencyVal.get(), targetCurrency);
					});

					Future<Double> closePriceFuture = null;
					
					try {
						//String stockPriceCurrency = (requiredCurrency != null && !requiredCurrency.isEmpty()) ? requiredCurrency : cmFinancialDataService.getCompanyLatestFilingInfo(companyId).get(0).getReportingCurrency();
						// //closePrice = cmStockService.getCompanyStockPrice(companyId, availPeriod.get(i), currency).getClose();
						
						 closePriceFuture = executorPool.submit(() ->{
							return cmStockService.getCompanyStockPrice(companyId, availPeriod.get(loopCounter.get()),  currencyVal.get()).getClose();
						});
						
					} catch (Exception e) {
						_log.warn("stock price not found for companyId: "+ companyId +", period: "+ availPeriod.get(i));
					}

					Future<Double> totalShareFuture = null;
					
					try {
						// // totalShares = (Double) cmStockService.getCompanyTotalSharesByPeriod(companyId,availPeriod.get(i)).get(0).get("total_shares");
					
						 totalShareFuture = executorPool.submit(() ->{
							return (Double) cmStockService.getCompanyTotalSharesByPeriod(companyId,availPeriod.get(loopCounter.get())).get(0).get("total_shares");
						});
					} catch (Exception e) {
						_log.warn("totalShares not found for companyId: "+ companyId +", period: "+ availPeriod.get(i));
					}
					
					fx=fxFuture.get();
					fxAsOnDate = fxAsOnDateFuture.get();
					closePrice= closePriceFuture.get();
					
					totalShares = totalShareFuture.get();
					
					
					for(BalanceModelDTO balanceModelDTO : balanceModelDTOs) {
						Class ratiosCalculation[] = new Class[14];
						ratiosCalculation[0] = String.class;
						ratiosCalculation[1] = HashMap.class;
						ratiosCalculation[2] = Double.class;
						ratiosCalculation[3] = Double.class;
						ratiosCalculation[4] = List.class;
						ratiosCalculation[5] = String.class;
						ratiosCalculation[6] = String.class;
						ratiosCalculation[7] = String.class;
						ratiosCalculation[8] = int.class;
						ratiosCalculation[9] = String.class;
						ratiosCalculation[10] = String.class;
						ratiosCalculation[11] = String.class;
						ratiosCalculation[12] = BalanceModelDTO.class;
						ratiosCalculation[13] = RatioCompanyData.class;

						Method meth;
						Object arglist[] = new Object[14];
						arglist[0] = companyId;
						arglist[1] = financialDataMap;
						arglist[2] = closePrice;
						arglist[3] = totalShares;
						arglist[4] = availPeriod;
						arglist[5] = availPeriod.get(i);
						arglist[6] = applicablePeriodMap.get(availPeriod.get(i));
						arglist[7] = periodType;
						arglist[8] = j++;
						arglist[9] = currency;
						arglist[10] = unit;
						arglist[11] = latestAnnualDate;
						arglist[12] = balanceModelDTO;
						arglist[13] = companyData;

						try {
							if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_BANK)) {
								meth = RatioServiceBank.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
								ratioData = (CompanyFinancialMINDTO)meth.invoke(bankRatioCalculateService, arglist);
							}else if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_INSURANCE)) {
								meth = RatioServiceInsurance.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
								ratioData = (CompanyFinancialMINDTO)meth.invoke(insuranceRatioCalculateService, arglist);
							}else if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_OTHER)) {
								meth = RatioServiceOther.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
								ratioData = (CompanyFinancialMINDTO)meth.invoke(ratioServiceOther, arglist);
							}else {
								meth = RatioServiceIndustry.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
								ratioData = (CompanyFinancialMINDTO)meth.invoke(ratioServiceIndustry, arglist);
							}

							if(targetCurrency != null && balanceModelDTO.getCurrencyFlag() != null && balanceModelDTO.getCurrencyFlag() == 1) {
								ratioData.setCurrency(targetCurrency);
								
								if(ratioData.getData() != null && balanceModelDTO.getFxAod() == 1)
									ratioData.setData(ratioData.getData()*fxAsOnDate);
								else
									ratioData.setData(ratioData.getData()*fx);
								
							}
							financialRatioData.add(ratioData);

						} catch(Exception ee) {
							_log.error("Problem occured in calculating ratio: "+ balanceModelDTO.getFieldName() +" for company: "+ companyId);
							ee.printStackTrace();
						}
					}

				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return financialRatioData; 
	}

	@SuppressWarnings("unchecked")
	@Cacheable(cacheNames = "CM_DAYS_CACHE", unless="#result.size()==0")
	public List<CompanyFinancialMINDTO> getCompanyRatio(String companyId, String periodType, Date startDate, Date endDate, String targetCurrency) {

		List<CompanyFinancialMINDTO> allFinancialData = getAllCompanyFinancial(companyId, periodType, startDate, endDate, null);
		List<CompanyFinancialMINDTO> financialData = null;
		CompanyFinancialMINDTO ratioData;
		try {
			financialData = new ArrayList<CompanyFinancialMINDTO>();

			HashMap<String,Double> financialDataMap = new HashMap<String,Double>();
			HashMap<String, String> applicablePeriodMap = new HashMap<String, String>();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			TreeSet<String> periods = new TreeSet<>();

			RatioCompanyData companyData = new RatioCompanyData();
			companyData.setCompanyId(companyId);
			String currency = null;
			String unit = null;
			String latestAnnualDate = null;
			Double totalShares;
			Double closePrice;

			if(allFinancialData!=null && allFinancialData.size()>0) {
				currency = allFinancialData.get(0).getCurrency();
				unit 	 = allFinancialData.get(0).getUnit();
				if(allFinancialData.get(0).getLatestAnnualDate() != null)
					latestAnnualDate = df.format(allFinancialData.get(0).getLatestAnnualDate());
			}

			//Creating financial data map
			allFinancialData.forEach(fData -> {
				financialDataMap.put(fData.getCompanyId()+"_"+df.format(fData.getPeriod())+"_"+fData.getFieldName(), fData.getData());
				periods.add(df.format(fData.getPeriod()));
				applicablePeriodMap.put(df.format(fData.getPeriod()), df.format(fData.getApplicablePeriod()));
			});
			String industryType = capitalMarketService.getCMCompaniesById(companyId).getFf_industry();

			periods.descendingSet();
			List<String> availPeriod = new ArrayList<String>(periods);
			Collections.reverse(availPeriod);

			List<BalanceModelDTO> balanceModelDTOs = cmRepository.getRatioBalanceModel(industryType,null, null,null);

			int j=0;
			Double fx = null;
			Double fxAsOnDate = null;

			for(int i=0; i < periods.size(); i++) {
				j=0;
				totalShares = null;
				closePrice = null;

				fx = cmRepository.getFxData(periodType, availPeriod.get(i), currency, targetCurrency);
				fxAsOnDate = cmRepository.getFxData(CMStatic.PERIODICITY_DAILY, availPeriod.get(i), currency, targetCurrency);

				try {
					//String stockPriceCurrency = (currencyCodeForConversion != null && !currencyCodeForConversion.isEmpty()) ? currencyCodeForConversion : cmFinancialDataService.getCompanyLatestFilingInfo(companyId).get(0).getReportingCurrency();
					closePrice = cmStockService.getCompanyStockPrice(companyId, availPeriod.get(i), currency).getClose();
				} catch (Exception e) {
					_log.warn("stock price not found for companyId: "+ companyId +", period: "+ availPeriod.get(i));
				}

				try {
					totalShares = (Double) cmStockService.getCompanyTotalSharesByPeriod(companyId,availPeriod.get(i)).get(0).get("total_shares");
				} catch (Exception e) {
					_log.warn("totalShares not found for companyId: "+ companyId +", period: "+ availPeriod.get(i));
				}

				for(BalanceModelDTO balanceModelDTO : balanceModelDTOs) {

					Class ratiosCalculation[] = new Class[14];
					ratiosCalculation[0] = String.class;
					ratiosCalculation[1] = HashMap.class;
					ratiosCalculation[2] = Double.class;
					ratiosCalculation[3] = Double.class;
					ratiosCalculation[4] = List.class;
					ratiosCalculation[5] = String.class;
					ratiosCalculation[6] = String.class;
					ratiosCalculation[7] = String.class;
					ratiosCalculation[8] = int.class;
					ratiosCalculation[9] = String.class;
					ratiosCalculation[10] = String.class;
					ratiosCalculation[11] = String.class;
					ratiosCalculation[12] = BalanceModelDTO.class;
					ratiosCalculation[13] = RatioCompanyData.class;

					Method methRatioCalc;
					Object arglistRatioCalc[] = new Object[14];
					arglistRatioCalc[0] = companyId;
					arglistRatioCalc[1] = financialDataMap;
					arglistRatioCalc[2] = closePrice;
					arglistRatioCalc[3] = totalShares;
					arglistRatioCalc[4] = availPeriod;
					arglistRatioCalc[5] = availPeriod.get(i);
					arglistRatioCalc[6] = applicablePeriodMap.get(availPeriod.get(i));
					arglistRatioCalc[7] = periodType;
					arglistRatioCalc[8] = j++;
					arglistRatioCalc[9] = currency;
					arglistRatioCalc[10] = unit;
					arglistRatioCalc[11] = latestAnnualDate;
					arglistRatioCalc[12] = balanceModelDTO;
					arglistRatioCalc[13] = companyData;


					try {
						if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_BANK)) {
							methRatioCalc = RatioServiceBank.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
							ratioData = (CompanyFinancialMINDTO)methRatioCalc.invoke(bankRatioCalculateService, arglistRatioCalc);
						}
						else if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_INSURANCE)) {
							methRatioCalc = RatioServiceInsurance.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
							ratioData = (CompanyFinancialMINDTO)methRatioCalc.invoke(insuranceRatioCalculateService, arglistRatioCalc);
						}
						else if(industryType.equalsIgnoreCase(CMStatic.INDUSTRY_OTHER)) {
							methRatioCalc = RatioServiceOther.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
							ratioData = (CompanyFinancialMINDTO)methRatioCalc.invoke(ratioServiceOther, arglistRatioCalc);
						}else {
							methRatioCalc = RatioServiceIndustry.class.getDeclaredMethod(balanceModelDTO.getFieldName(), ratiosCalculation);
							ratioData = (CompanyFinancialMINDTO)methRatioCalc.invoke(ratioServiceIndustry, arglistRatioCalc);
						}

						if(targetCurrency != null && balanceModelDTO.getCurrencyFlag() != null && balanceModelDTO.getCurrencyFlag() == 1) {
							ratioData.setCurrency(targetCurrency);
							
							if(ratioData.getData() != null && balanceModelDTO.getFxAod() == 1)
								ratioData.setData(ratioData.getData()*fxAsOnDate);
							else
								ratioData.setData(ratioData.getData()*fx);
							
						}
						financialData.add(ratioData);
					}
					catch(Exception ee) {
						_log.error("Problem occured in calculating ratio: "+ balanceModelDTO.getFieldName() +" for company: "+ companyId);
					}
				}
				_log.debug("ratios calculated for period: "+ availPeriod.get(i) +", company id: "+ companyId);
			}



		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
			} else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){

			}	
			_log.error(e.getMessage());
		}

		return financialData; 
	}



	public List<CompanyFinancialMINDTO> getAllCompanyFinancial(String companyId, String fieldName, String periodType, Date startDate, Date endDate,String currency) {

		List<String> fieldNameList =  Arrays.asList(fieldName.trim().split(","));

		List<CompanyFinancialMINDTO> financialData = new ArrayList<CompanyFinancialMINDTO>();
		try {
			financialData.addAll(cmFinancialDataService.getCompanyFinancial(companyId, fieldNameList, periodType, startDate, endDate,currency));	
		}catch(Exception e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		}
		return financialData;
	}

	public List<CompanyFinancialMINDTO> getAllCompanyFinancial(String companyId, String periodType, Date startDate, Date endDate,String currency) {

		List<CompanyFinancialMINDTO> financialData = new ArrayList<CompanyFinancialMINDTO>();
		try {
			return cmFinancialDataService.getCompanyFinancial(companyId, CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE, periodType, startDate, endDate,currency, null);	
		}catch(Exception e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		}
		return financialData;
	}



	public BalanceModelDTO getBalanceModel(String industry, String ratioFieldName) {
		return cmRepository.getFinancialParams(industry, ratioFieldName);
	}

	public List<BalanceModelDTO> getBalanceModel(String industry, List<String> ratiosList) {
		return cmRepository.getFinancialParams(industry, ratiosList);
	}
}
