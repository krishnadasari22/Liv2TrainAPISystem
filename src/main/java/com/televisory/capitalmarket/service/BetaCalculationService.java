package com.televisory.capitalmarket.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.model.BetaData;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;

/**
 * 
 * @author vinay
 *
 */
@Service
public class BetaCalculationService {

	Logger _log = Logger.getLogger(BetaCalculationService.class);

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Covariance covariance;

	@Autowired
	Variance variance;
	
	@Autowired
	CMStockService cmStockService;
	
	public BetaData getBetaData(List<StockPriceDTO> companyStockPrice, List<StockPriceDTO> indexStockPrice, IndexDTO index, String periodType) throws RuntimeException, Exception {
		
		Date maxIndexStockDate;
		Date minIndexStockDate;
		Date minDateCommonDataSet;
		Date maxDateCommonDataSet;
	
		List<Double> finalBetaCalculation = new ArrayList<>();
		
		int days = 1;
		if (periodType.toUpperCase().equals(CMStatic.PERIODICITY_WEEKLY)) {
			days = 7;
		} else if (periodType.toUpperCase().equals(CMStatic.PERIODICITY_MONTHLY)) {
			days = 30;
		}
		
		_log.debug("Rate size "+companyStockPrice.size()+" and "+indexStockPrice.size());
		
		Date maxCompanyStock = companyStockPrice.get(companyStockPrice.size() - 1).getDate();
		Date minCompanyStockDate = companyStockPrice.get(0).getDate();
		
		maxIndexStockDate = indexStockPrice.get(indexStockPrice.size() - 1).getDate();
		minIndexStockDate = indexStockPrice.get(0).getDate();

		/**
		 * For weekly beta latest date will start from last friday
		 */
		if (periodType.equalsIgnoreCase(CMStatic.PERIODICITY_WEEKLY)) {
			
			companyStockPrice =cmStockService.updateGapStockPrice(companyStockPrice);
			indexStockPrice =cmStockService.updateGapStockPrice(indexStockPrice);
		
			maxCompanyStock=dateUtil.getNearestFriday(companyStockPrice);
			maxIndexStockDate=dateUtil.getNearestFriday(indexStockPrice);
			
		}
		
		maxDateCommonDataSet = maxIndexStockDate.after(maxCompanyStock) || maxIndexStockDate.equals(maxCompanyStock)? maxCompanyStock:maxIndexStockDate;
		minDateCommonDataSet = minCompanyStockDate.after(minIndexStockDate) || minCompanyStockDate.equals(minIndexStockDate)?minCompanyStockDate:minIndexStockDate;
		
		_log.info(minDateCommonDataSet+" ----- "+maxDateCommonDataSet);
		
		List<StockPriceDTO> commonStockPrice=companyStockPrice.stream()
				.filter(price -> (price.getDate().after(minDateCommonDataSet) || price.getDate().equals(minDateCommonDataSet) ) && ((price.getDate().before(maxDateCommonDataSet) || price.getDate().equals(maxDateCommonDataSet))))
				.collect(Collectors.toList());
		
		List<StockPriceDTO> commonIndexPrice=indexStockPrice.stream()
				.filter(price -> (price.getDate().after(minDateCommonDataSet) || price.getDate().equals(minDateCommonDataSet) ) && ((price.getDate().before(maxDateCommonDataSet) || price.getDate().equals(maxDateCommonDataSet))))
				.collect(Collectors.toList());
		
		if(commonStockPrice == null || commonStockPrice.size() == 0
				|| commonIndexPrice == null || commonIndexPrice.size() == 0)
			throw new RuntimeException(HttpStatus.NO_CONTENT.toString());
		
		commonStockPrice=updatePricegap(commonStockPrice);
		commonIndexPrice=updatePricegap(commonIndexPrice);
			
		TreeMap<Date, Double> indexStockPriceCommonDataSet ;
		TreeMap<Date, Double> companyStockPriceCommonDataSet;

		indexStockPriceCommonDataSet = cmStockService.getBetaStockChangeRate(commonIndexPrice, periodType, days);
		companyStockPriceCommonDataSet = cmStockService.getBetaStockChangeRate(commonStockPrice, periodType, days);
		
		_log.debug(companyStockPriceCommonDataSet.size()+"------------"+indexStockPriceCommonDataSet.size());
		
		finalBetaCalculation = betaCalculation(companyStockPriceCommonDataSet, indexStockPriceCommonDataSet, periodType,days);
			
		BetaData beta = new BetaData();
		beta.setData(finalBetaCalculation);
		beta.setDate(minIndexStockDate);
		beta.setIndexId(index.getId());
		beta.setIndexName(index.getName());
			
		return beta;
	}
	
	private List<StockPriceDTO> updatePricegap(List<StockPriceDTO> priceList) {
		
		List<StockPriceDTO> finalStockPriceList=new ArrayList<>();
		
		for(int i=0;i<priceList.size()-1;i++){	
				
			finalStockPriceList.add(priceList.get(i));
			
			Date startDate = priceList.get(i).getDate();
			Date endDate = priceList.get(i + 1).getDate();
		
			int noOfDays = (int) dateUtil.daysBetween(startDate,endDate);
		
			_log.trace("Current date is : "+startDate+"");
			_log.trace("no of days beween start and end Date "+noOfDays);
			
			if (noOfDays > 1) {			
				
				for (int day = 1; day < noOfDays; day++) {
					
					Date nextDate = dateUtil.nextDate(startDate, day);
					
					_log.trace("next Date to be inserted is : "+nextDate);
					
					StockPriceDTO<Integer> stockPriceDTO =new StockPriceDTO<Integer>();
					stockPriceDTO.setClose(priceList.get(i).getClose());
					stockPriceDTO.setDate(nextDate);		
					finalStockPriceList.add(stockPriceDTO);
				}
			}
		};
		
		finalStockPriceList.add(priceList.get(priceList.size()-1));
		
		finalStockPriceList.sort(Comparator.comparing(StockPriceDTO::getDate));	
		
		return finalStockPriceList;

	}

	
	/**
	 * Method to calculate Beta for 1-5 Years
	 * 
	 * @param companyStockPriceCommonDataSet
	 * @param indexStockPriceCommonDataSet
	 * @param periodType
	 * @param period
	 * @param days
	 * @return
	 */
	private List<Double> betaCalculation(TreeMap<Date, Double> companyStockPriceCommonDataSet,
			TreeMap<Date, Double> indexStockPriceCommonDataSet, String periodType,Integer period) {

		List<Double> finalBetaCalculationResult = new ArrayList<>();
		
		int  periodParam = 365;
		
		_log.info(periodType);
		
		if (periodType.toUpperCase().equals(CMStatic.PERIODICITY_WEEKLY)) {
			periodParam = 52;
		}
		
		try {
			
			Date stockStartingDate;
			Date indexStartingDate;
			Double coVariance;
			Double variance;

			// Calculation for 1 year
			
			if(companyStockPriceCommonDataSet.size()>periodParam && indexStockPriceCommonDataSet.size()>periodParam){
				
				_log.info("calculating Beta for 1 year");

				 stockStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 1);
				 indexStartingDate = dateUtil.prevDateInYears(indexStockPriceCommonDataSet.lastKey(), 1);

				_log.info("1 Year date " + stockStartingDate + "  " + indexStartingDate);
			
				List<Double> OneYearVarOrCoVarValue = getVarOrCovariance(companyStockPriceCommonDataSet,
						indexStockPriceCommonDataSet, periodType, period, stockStartingDate, indexStartingDate);
				 coVariance = OneYearVarOrCoVarValue.get(0);
				 variance = OneYearVarOrCoVarValue.get(1);
				finalBetaCalculationResult.add(coVariance / variance);
				
			}
			
			if(companyStockPriceCommonDataSet.size()>=periodParam*2 && indexStockPriceCommonDataSet.size()>=periodParam*2){
				
				// Calculation for 2 year
				stockStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 2);
				indexStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 2);

				_log.info("calculating Beta for 2 year");
				
				List<Double> twoYearVarOrCoVarValue = getVarOrCovariance(companyStockPriceCommonDataSet,
						indexStockPriceCommonDataSet, periodType, period, stockStartingDate, indexStartingDate);
				coVariance = twoYearVarOrCoVarValue.get(0);
				variance = twoYearVarOrCoVarValue.get(1);
				
				finalBetaCalculationResult.add(coVariance / variance);
				
			}
			

			if(companyStockPriceCommonDataSet.size()>=periodParam*3 && indexStockPriceCommonDataSet.size()>=periodParam*3){
				
				// Calculation for 3 year
				stockStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 3);
				indexStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 3);

				_log.info("calculating Beta for 3 year");

				List<Double> threeYearVarOrCoVarValue = getVarOrCovariance(companyStockPriceCommonDataSet,
						indexStockPriceCommonDataSet, periodType, period, stockStartingDate, indexStartingDate);
				coVariance = threeYearVarOrCoVarValue.get(0);
				variance = threeYearVarOrCoVarValue.get(1);
				
				finalBetaCalculationResult.add(coVariance / variance);
				
			}

			if(companyStockPriceCommonDataSet.size()>=periodParam*4 && indexStockPriceCommonDataSet.size()>=periodParam*4){
				
				// Calculation for 4 year
				stockStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 4);
				indexStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 4);

				_log.info("calculating Beta for 4 year");

				List<Double> fourYearVarOrCoVarValue = getVarOrCovariance(companyStockPriceCommonDataSet,
						indexStockPriceCommonDataSet, periodType, period, stockStartingDate, indexStartingDate);
				coVariance = fourYearVarOrCoVarValue.get(0);
				variance = fourYearVarOrCoVarValue.get(1);
				finalBetaCalculationResult.add(coVariance / variance);
				
			}

			if(companyStockPriceCommonDataSet.size()>=periodParam*5 && indexStockPriceCommonDataSet.size()>=periodParam*5){
				
				// Calculation for 5 year
				stockStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 5);
				indexStartingDate = dateUtil.prevDateInYears(companyStockPriceCommonDataSet.lastKey(), 5);

				_log.info("calculating Beta for 5 year");

				List<Double> fiveYearVarOrCoVarValue = getVarOrCovariance(companyStockPriceCommonDataSet,
						indexStockPriceCommonDataSet, periodType, period, stockStartingDate, indexStartingDate);
				coVariance = fiveYearVarOrCoVarValue.get(0);
				variance = fiveYearVarOrCoVarValue.get(1);
				
				finalBetaCalculationResult.add(coVariance / variance);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return finalBetaCalculationResult;
		}
		return finalBetaCalculationResult;
	}	

	/**
	 * Method to calculate the variance and covariance of the data Set
	 * 
	 * @param companyStockPriceCommonDataSet
	 * @param indexStockPriceCommonDataSet
	 * @param stockDateList
	 * @param period
	 * @param periodType
	 * @param days
	 * @return
	 */
	private List<Double> getVarOrCovariance(TreeMap<Date, Double> companyStockPriceRateChangeMap,
			TreeMap<Date, Double> indexStockPriceRateChangeMap,
			String periodType,Integer period,Date stockStartingDate,Date indexStartingDate) {
		
		_log.info("Calculating covariance and variance");
		
		List<Double> resultList = new ArrayList<>();
		double[] finalCompanyStockPriceArray ;
		double[] finalIndexStockPriceArray;
		
		try {
			
			resultList = new ArrayList<>();

			Map<Date,Double> indexMap =new TreeMap<>();
			Map<Date,Double> stockMap=new TreeMap<>();
			
			/**
			 * get the stock rate data set excluding the public holidays 
			 */
			stockMap = companyStockPriceRateChangeMap.entrySet().stream().filter(map -> {
					return map.getKey().after(stockStartingDate)||map.getKey().equals(stockStartingDate);
			}).sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
					(oldValue, newValue) -> oldValue, TreeMap::new));

			/**
			 * get the index rate data set excluding the public holidays 
			 */
			indexMap = indexStockPriceRateChangeMap.entrySet().stream().filter(map -> {
					return map.getKey().after(indexStartingDate)||map.getKey().equals(indexStartingDate);
			}).sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
					(oldValue, newValue) -> oldValue, TreeMap::new));
			
			
			//stockMap.forEach((k,v)->_log.info(k+" ------------ "+v));
		
			finalCompanyStockPriceArray = stockMap.values().stream().mapToDouble(d -> d).toArray();
			finalIndexStockPriceArray = indexMap.values().stream().mapToDouble(d -> d).toArray();
			
		/*	System.out.println(Arrays.toString(finalCompanyStockPriceArray));
			System.out.println(Arrays.toString(finalIndexStockPriceArray));
			
			System.out.print("                                                                                                          ");
			System.out.print("                                                                                                          ");
			System.out.print("                                                                                                          ");*/
	
			_log.debug("Final Lists size "+finalCompanyStockPriceArray.length+"--------"+finalIndexStockPriceArray.length);
			
			 DecimalFormat df = new DecimalFormat("#.##########");
		     df.setRoundingMode(RoundingMode.CEILING);
			
			String coValueString=df.format(covariance.covariance(finalCompanyStockPriceArray, finalIndexStockPriceArray));			
			String varianceValString=df.format(variance.evaluate(finalIndexStockPriceArray));
	
			double coValue = Double.parseDouble(coValueString);
			double varianceVal = Double.parseDouble(varianceValString);
			
			//_log.info(coValueString+" ------------------- "+varianceValString);
			
			
		//	_log.info(coValue/varianceVal);
				
			resultList.add(coValue);
			resultList.add(varianceVal);
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
}
