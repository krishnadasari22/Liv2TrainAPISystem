package com.televisory.capitalmarket.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.ScreenerRepository;
import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.ScreenerCompanyFinancialDTO;
import com.televisory.capitalmarket.dto.ScreenerStockPriceDTO;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.entities.factset.FxConversionRate;
import com.televisory.capitalmarket.factset.dto.FFBasicCfDTO;
import com.televisory.capitalmarket.model.RatioCompanyData;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class ScreenerService {
	@Autowired
	ScreenerRepository screenerRepository;

	@Autowired
	CMRepository cmRepository;

	@Autowired
	SectorRepository sectorRepository;

	@Autowired
	RatioService ratioService;

	Logger _log = Logger.getLogger(SectorService.class);
	public List<ScreenerCompanyFinancialDTO> getCompaniesForScreener(String ticsIndustryCode, String countryIsoList) {
			return screenerRepository.getCompanyYearly(ticsIndustryCode,countryIsoList);
	}

	public List<ScreenerCompanyFinancialDTO> getCompanyFinancial(String subIndustryCode,String currency,String fieldName,String domicileCountryCode){
		return screenerRepository.getCompanyFinancial(subIndustryCode,currency,fieldName,domicileCountryCode);
	}

	public List<CompanyFinancialDTO> getCompanyFinancialRatio(String ticsIndustryCode,String currency,String ratioNames,String domicileCountryCode){
		
		_log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		_log.info(ratioNames);
		_log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		
		String fieldName = null;
		String industryType = null;
		List<TicsIndustry> ticsIndustryList	= sectorRepository.getIndustryByTicsIndustryCode(ticsIndustryCode);
		if(ticsIndustryList!=null && ticsIndustryList.size()>0) {
			industryType = ticsIndustryList.get(0).getFactsetIndustry();
		}
		AtomicReference industryReference = new AtomicReference(industryType);
		List<String> ratiosList = Stream.of(ratioNames.split(",", -1)).collect(Collectors.toList());
		List<BalanceModelDTO> ratios = cmRepository.getFinancialParams(industryType, ratiosList);
		_log.info("ratios "+ratios);
		fieldName = ratios.parallelStream().map(BalanceModelDTO->BalanceModelDTO.getFinancialField()).collect(Collectors.joining(","));
		_log.info("fieldName "+fieldName);

		HashMap<String,Double> financialDataMap = new HashMap<String,Double>();
		HashMap<String,RatioCompanyData> companyMap = new HashMap<String,RatioCompanyData>();
		/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		TreeSet<String> periods = new TreeSet<>();*/
		List<ScreenerCompanyFinancialDTO> allFinancialData =  screenerRepository.getCompanyFinancialRatio(ticsIndustryCode,currency,fieldName,domicileCountryCode);

		String finCurrency = "";
		String unit = "";
		if(allFinancialData!=null && allFinancialData.size()>0) {
			finCurrency = allFinancialData.get(0).getCurrency();
			unit 	 = allFinancialData.get(0).getUnit();

			allFinancialData.stream().forEach(fData -> {
				LocalDate localDate = LocalDate.fromDateFields(fData.getPeriod());
				financialDataMap.put(fData.getCompanyId()+"_"+localDate.toString()+"_"+fData.getFieldName(), fData.getData());
				if(companyMap.containsKey(fData.getCompanyId())) {
					//List<String> period = companyMap.get(fData.getCompanyId());
					RatioCompanyData companyData = companyMap.get(fData.getCompanyId());
					if(!companyData.getPeriods().contains(localDate.toString())) {
						companyData.getPeriods().add(localDate.toString());
						companyMap.put(fData.getCompanyId(), companyData);
					}
				}else {
					List<String> period = new ArrayList<String>();
					period.add(localDate.toString());
					RatioCompanyData companyData = new RatioCompanyData();
					companyData.setCompanyId(fData.getCompanyId());
					companyData.setCompanyName(fData.getCompanyName());
					companyData.setCountryId(fData.getCountryId());
					companyData.setCountryName(fData.getCountryName());
					companyData.setPeriods(period);
					companyMap.put(fData.getCompanyId(), companyData);
				}
			});
			_log.info(companyMap);
		}
		List<ScreenerStockPriceDTO> stockPrices = getCompanyLatestStockPrice(ticsIndustryCode,currency,new ArrayList<String>());
		List<FFBasicCfDTO> totalShares = getCompaniesTotalStock(ticsIndustryCode);
		Map<String, ScreenerStockPriceDTO> stockPriceMap  = stockPrices.stream().collect(Collectors.toMap(ScreenerStockPriceDTO::getCompanyId, b -> b));
		Map<String, FFBasicCfDTO> totalShareMap  = totalShares.stream().collect(Collectors.toMap(FFBasicCfDTO::getCompanyId, b -> b));

		AtomicReference currencyReference = new AtomicReference(finCurrency);
		AtomicReference unitReference = new AtomicReference(unit);
		List<CompanyFinancialDTO> financialData = new ArrayList<CompanyFinancialDTO>();
		companyMap.entrySet().parallelStream().forEach(e ->{
			financialData.addAll(ratioService.getCompanyRatio(industryReference.get().toString(),e.getKey(),stockPriceMap.get(e.getKey()),totalShareMap.get(e.getKey()), ratios, financialDataMap, "Yearly",e.getValue(), currencyReference.get().toString(),unitReference.get().toString(), null, null));
		});

		return financialData;
	}

	public List<ScreenerStockPriceDTO> getCompanyLatestStockPrice(String ticsIndustryCode, String currency, List<String> countryIsoList) {
		return screenerRepository.getCompaniesLatestStockPrice(ticsIndustryCode,currency,countryIsoList);
	}

	public List<FFBasicCfDTO> getCompaniesTotalStock(String ticsIndustryCode) {
		return screenerRepository.getCompaniesTotalStock(ticsIndustryCode);
	}
	
	public List<FxConversionRate>getCurrencyConversionRate(String sourceCurrency,String destinationCurrency,String[] periodList){
		List<FxConversionRate> data = new ArrayList<FxConversionRate>();
		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		for(int i=0;i<periodList.length;i++) {
				Date period = new Date();
				try {
					period = df.parse(periodList[i]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String periodToPass =  df.format(period);
				FxConversionRate fxrate = new FxConversionRate();
				fxrate.setSourceCurrency(sourceCurrency);
				fxrate.setDestinationCurrency(destinationCurrency);
				fxrate.setPeriod(periodToPass);
				fxrate.setRate(screenerRepository.getCurrencyConversionRate(sourceCurrency, destinationCurrency, periodToPass));
				data.add(fxrate);
		}
		return data;
	}
}
