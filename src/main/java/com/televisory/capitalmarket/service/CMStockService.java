package com.televisory.capitalmarket.service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.CMStockRepository;
import com.televisory.capitalmarket.dto.IndexDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.model.BetaData;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;

@Service
@SuppressWarnings("rawtypes")
public class CMStockService {

	Logger _log = Logger.getLogger(CMStockService.class);

	@Autowired
	@Lazy
	CMStockRepository cmStockRepository;

	@Autowired
	CorporateAnnouncementService corpAnnService;

	@Autowired
	CMStockService cmStockService;

	@Autowired
	BetaCalculationService betaCalculationService;


	@Autowired
	CMRepository cmRepository;

	@Autowired
	DateUtil dateUtil;


	/**
	 * Method to get Stock Price of factset companies
	 * @param companyId
	 * @param periodType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<StockPriceDTO> getFactSetCompanyStockPrice(String companyId, String periodType, Date startDate,Date endDate,String currencyCode) {

		List<StockPriceDTO> factSetStockPrice= cmStockRepository.getFactSetCompanyStockPrice(companyId, periodType, startDate, endDate,currencyCode);
		//_log.info(factSetStockPrice.size());
		List<StockPriceDTO> adjustedStockPrice=new ArrayList<>();
		if(factSetStockPrice.size()==0) {
			return adjustedStockPrice;
		}
		
		Integer period = null;
		try {
			if (periodType.equalsIgnoreCase(CMStatic.WEEKLY)) {
				period = 7;
				factSetStockPrice = cmStockService.getStockPriceBasedOnPeriodicity(factSetStockPrice, period);
			} else if (periodType.equalsIgnoreCase(CMStatic.DAILY)) {
				period=1;
			}
			factSetStockPrice = cmStockService.getStockChangeRate(factSetStockPrice, periodType, period);
			adjustedStockPrice = corpAnnService.getCorpAnnouncementAdjustedStockPrice(factSetStockPrice,companyId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return adjustedStockPrice;
	}


	/**
	 * Method to get Stock Price of factset companies
	 * @param companyId
	 * @param periodType
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception 
	 */
	public List<StockPriceDTO> getFactSetCompanyStockPriceWithoutGap(String companyId, String periodType, Date startDate,Date endDate,String currencyCode) {

		List<StockPriceDTO> factSetStockPrice= cmStockRepository.getFactSetCompanyStockPrice(companyId, periodType, startDate, endDate,currencyCode);
		//_log.info(factSetStockPrice.size());
		List<StockPriceDTO> adjustedStockPrice=new ArrayList<>();
		if(factSetStockPrice.size()==0) {
			return adjustedStockPrice;
		}
		factSetStockPrice=updateGapStockPrice(factSetStockPrice);

		Integer period = null;
		try {
			if (periodType.equalsIgnoreCase(CMStatic.WEEKLY)) {
				period = 7;
				factSetStockPrice = cmStockService.getStockPriceBasedOnPeriodicity(factSetStockPrice, period);
			} else if (periodType.equalsIgnoreCase(CMStatic.DAILY)) {
				period=1;
			}
			//factSetStockPrice = cmStockService.getStockChangeRate(factSetStockPrice, periodType, period);
			adjustedStockPrice = corpAnnService.getCorpAnnouncementAdjustedStockPrice(factSetStockPrice,companyId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return adjustedStockPrice;
	}

	/**
	 * Method to get the highest Stock Prices
	 * @param companyId
	 * @return
	 */
	public StockPriceDTO getStockPriceHighest(String companyId, String currencyCode) {
		StockPriceDTO stockPriceDTO = cmStockRepository.getStockPriceHighest(companyId, currencyCode);
		return corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceDTO,companyId);
	}

	/**
	 * Method to get the highest Stock Price data
	 * @param companyId
	 * @param days
	 * @return
	 */
	public StockPriceDTO getStockPriceHighest(String companyId, Integer days, String currencyCode) {
		StockPriceDTO stockPriceDTO = cmStockRepository.getStockPriceHighest(companyId, days, currencyCode);
		return corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceDTO,companyId);
	}

	/**
	 * Method to get lowest Stock Price
	 * @param companyId
	 * @return
	 */
	public StockPriceDTO getStockPriceLowest(String companyId, String currencyCode) {
		StockPriceDTO stockPriceDTO = cmStockRepository.getStockPriceLowest(companyId, currencyCode);
		return corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceDTO,companyId);
	}

	/**
	 * Method to get lowest price based on days
	 * @param companyId
	 * @param days
	 * @return
	 */
	public StockPriceDTO getStockPriceLowest(String companyId, Integer days, String currencyCode) {
		StockPriceDTO stockPriceDTO = cmStockRepository.getStockPriceLowest(companyId, days, currencyCode);
		return corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceDTO,companyId);
	}




	public StockPriceDTO getCompanyLatestStockPrice(String companyId,String currencyCode) throws Exception {
		StockPriceDTO stockPriceDTO = cmStockRepository.getCompanyLatestStockPrice(companyId,currencyCode);
		return corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceDTO,companyId);
	}

	@Cacheable(cacheNames="CM_DAYS_CACHE",unless="#result == null")
	public StockPriceDTO getCompanyStockPrice(String companyId, String date, String currencyCode) throws Exception {
		StockPriceDTO stockPriceDTO = cmStockRepository.getCompanyStockPrice(companyId, date, currencyCode);
		return corpAnnService.getCorpAnnouncementAdjustedStockPrice(stockPriceDTO,companyId);
	}

	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public  List<BetaData> getCompanyBeta(String companyId, String periodicity, List<String> indexIds,String currency) throws RuntimeException, Exception {

		_log.info("getting beta data");
		List<StockPriceDTO> companyStockPrice = cmStockRepository.getFactSetCompanyStockPrice(companyId, CMStatic.DAILY, null, null,currency);

		List<Integer> intIndexIds = indexIds.stream().map(Integer::parseInt).collect(Collectors.toList());
		List<BetaData> finalBetaCalculationObj = new ArrayList<>();

		if(intIndexIds!=null && !intIndexIds.isEmpty()){
			List<IndexDTO> indexList=cmRepository.getIndex(intIndexIds);
			for (IndexDTO indexDTO : indexList) {
				List<StockPriceDTO> indexData=getIndexStockPrice(indexDTO.getId(),null,null,periodicity);
				try{
					finalBetaCalculationObj.add(betaCalculationService.getBetaData(companyStockPrice, indexData, indexDTO, periodicity));
				}catch(Exception e){
					_log.error(e.getMessage());
				}
			}
		}
		return finalBetaCalculationObj;
	}


	/**
	 * Method to update the gap between stock price (public holidays/Sat/Sun)
	 * @param indexDTO
	 * @return list of stock prices
	 */
	@SuppressWarnings("unchecked")
	public List<StockPriceDTO> updateGapStockPrice(List<StockPriceDTO> indexDTO) {

		_log.info("Updating the gap in Stock Price");

		List<StockPriceDTO> finalStockPriceList=new ArrayList<>();

		int counter=0;
		try {
			for(int i=0;i<indexDTO.size()-1;i++){	

				indexDTO.get(i).setId(counter);
				counter++;

				finalStockPriceList.add(indexDTO.get(i));

				Date startDate = indexDTO.get(i).getDate();
				Date endDate = indexDTO.get(i + 1).getDate();

				int noOfDays = (int) dateUtil.daysBetween(startDate,endDate);

				_log.trace("Current date is : "+startDate);
				_log.trace("no of days beween start and end Date "+noOfDays);

				if (noOfDays > 1) {			
					for (int day = 1; day < noOfDays; day++) {
						Date nextDate = dateUtil.nextDate(startDate, day);
						_log.trace("next Date to be inserted is : "+nextDate);
						StockPriceDTO<Integer> stockPriceDTO =new StockPriceDTO<Integer>();
						stockPriceDTO.setId(counter);
						stockPriceDTO.setCompanyId(indexDTO.get(i).getCompanyId());
						stockPriceDTO.setClose(indexDTO.get(i).getClose());
						stockPriceDTO.setHigh(indexDTO.get(i).getClose());
						stockPriceDTO.setVolume(indexDTO.get(i).getVolume());
						stockPriceDTO.setLow(indexDTO.get(i).getClose());
						stockPriceDTO.setUnit(indexDTO.get(i).getUnit());
						stockPriceDTO.setCurrency(indexDTO.get(i).getCurrency());
						stockPriceDTO.setCreatedAt(indexDTO.get(i).getCreatedAt());
						stockPriceDTO.setCreatedBy(indexDTO.get(i).getCreatedBy());
						stockPriceDTO.setLastModifiedAt(indexDTO.get(i).getLastModifiedAt());
						stockPriceDTO.setLastModifiedBy(indexDTO.get(i).getLastModifiedBy());	
						stockPriceDTO.setDate(nextDate);
						counter++;			
						finalStockPriceList.add(stockPriceDTO);
					}
				}
			};

			finalStockPriceList.add(indexDTO.get(indexDTO.size()-1));

			finalStockPriceList.sort(Comparator.comparing(StockPriceDTO::getDate));	
		}catch(Exception e) {
			try {
				throw new Exception("No Data Found");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return finalStockPriceList;
	}

	/**
	 * Method to get Stock price based on periodicity daily/weekly
	 * @param stockPriceList
	 * @param period
	 * @return
	 */
	public List<StockPriceDTO> getStockPriceBasedOnPeriodicity(List<StockPriceDTO> stockPriceList, Integer period) {

		List<StockPriceDTO> stockPriceWithNearestFriday = getStockPriceWithNearestFriday(stockPriceList);
		List<StockPriceDTO> finalStockPriceList = new ArrayList<>();

		for (int i = stockPriceWithNearestFriday.size() - 1; i >= 0; i -= period) {
			finalStockPriceList.add(stockPriceWithNearestFriday.get(i));
		}
		finalStockPriceList.sort(Comparator.comparing(StockPriceDTO::getDate));
		return finalStockPriceList;
	}

	/**
	 * Method to get the list of stock price with nearest friday as latest date 
	 * @param stockPriceList
	 * @return
	 */
	private List<StockPriceDTO> getStockPriceWithNearestFriday(List<StockPriceDTO> stockPriceList) {

		Date CurrentDate;
		for (int i = 1; i <= 7; i++) {
			if (stockPriceList.size() - 1 > 0) {
				CurrentDate = stockPriceList.get(stockPriceList.size() - 1).getDate();
				if (CurrentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek()
						.getValue() == 5) {		
					return stockPriceList;
				}
				stockPriceList.remove(stockPriceList.size() - 1);
			}
		}
		return stockPriceList;
	}


	/**
	 * Method to calculate the stock change Rate
	 * 
	 * @param stockPrices
	 * @return
	 * @throws Exception
	 */
	public TreeMap<Date, Double> getBetaStockChangeRate(List<StockPriceDTO> priceList, String periodType, int days)
			throws Exception {

		if (priceList == null || priceList.size() <= 1) {
			throw new Exception("Insufficient Data");
		}


		_log.info("Updating the gap in Stock Price");


		TreeMap<Date, Double> changeRate = new TreeMap<>();

		_log.info("getting stock exchange Rate");

		for (int i = priceList.size() - 1; i >= 0; i -= days) {

			Date startDate = priceList.get(i).getDate();

			_log.trace("getting the rate for the date :- "+startDate);

			if (i - days >= 0)
				changeRate.put(startDate, priceList.get(i).getClose() / priceList.get(i - days).getClose() - 1);
		}

		return changeRate;

	}

	/**
	 * Method to calculate the stock change Rate
	 * 
	 * @param stockPrices
	 * @return
	 * @throws Exception
	 */
	public List<StockPriceDTO> getStockChangeRate(List<StockPriceDTO> stockPrices, String periodType, int days)
			throws Exception {

		if (stockPrices == null || stockPrices.size() <= 1) {
			throw new Exception("Insufficient Data");
		}

		_log.info("getting stock exchange Rate");
		for (int i = stockPrices.size() - 1; i >= 0; i -= days) {
			Date startDate = stockPrices.get(i).getDate();
			if (i - days >= 0) {
				Double currClose = stockPrices.get(i).getClose();
				Double prevClose = stockPrices.get(i - days).getClose();
				if(currClose!=null && prevClose!=null){
					double percentChange = currClose / prevClose - 1;
					stockPrices.get(i).setPercentChange(percentChange);	
				}
			}
		}
		return stockPrices;
	}

	@Cacheable(cacheNames = "CM_CACHE",unless="#result.size()==0")
	public List<CountryListDTO> getCountryList() {
		return cmRepository.getCountryList();
	}


	public List<StockPriceDTO> getIndexStockPrice(Integer indexId, Date startDate, Date endDate, String periodType) {
		return cmStockRepository.getIndexStockPrice(indexId,startDate,endDate,periodType);
	}

	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0")
	public List<Map<String, Object>> getCompanyTotalSharesByPeriod(String companyId,String period) {
		return cmStockRepository.getCompanyTotalSharesByPeriod(companyId,period);
	}

	public List<CountryListDTO> getCountryList(List<String> userCompanyList) {
		return cmRepository.getCountryList(userCompanyList);
	}
}
