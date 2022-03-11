package com.televisory.capitalmarket.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CommodityRepository;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.util.DateUtil;

@Service
public class CommodityService {

	Logger _log = Logger.getLogger(CommodityService.class);

	@Autowired
	CommodityRepository commodityRepository;
	
	@Autowired
	DateUtil dateUtil;
	
	
	public List<CommodityLatestDataDTO> getCommodityLatestData(String currency) {
			return commodityRepository.getCommodityLatestData(currency);
	}
	
	public List<CommodityLatestDataDTO> getCommodities() {
		return commodityRepository.getCommodities();
	}

	public List<CommodityHistoricalDataDTO> getCommodityHistoricalData(List<String> symbolList, String currency,
			Date startDate, Date endDate) {
		return commodityRepository.getCommodityHistoricalData(symbolList, currency, startDate, endDate);
	}
	

	/*public List<CommodityHistoricalDataDTO> getCommodityHistoricalData(DownloadRequest downloadRequest) {
		return commodityRepository.getCommodityHistoricalData(DownloadRequest downloadRequest);
	}*/
	
	public List<NewsDTO> getNews() {
		return commodityRepository.getNews();
	}
	
	

	/**
	 * Method to update the gap between commodity price (public holidays/Sat/Sun)
	 * @param indexDTO
	 * @return list of stock prices
	 */
	@SuppressWarnings("unchecked")
	public List<CommodityHistoricalDataDTO> updateGapCommodityPrice(List<CommodityHistoricalDataDTO> commodityDTO) {
		
		_log.info("Updating the gap in Stock Price");

		List<CommodityHistoricalDataDTO> finalCommodityPriceList=new ArrayList<>();
			
		try {
		
			for(int i=0;i<commodityDTO.size()-1;i++){	

				finalCommodityPriceList.add(commodityDTO.get(i));

				Date startDate = commodityDTO.get(i).getPeriod();
				Date endDate = commodityDTO.get(i + 1).getPeriod();

				int noOfDays = (int) dateUtil.daysBetween(startDate,endDate);

				_log.trace("Current date is : "+startDate);
				_log.trace("no of days beween start and end Date "+noOfDays);

				if (noOfDays > 1) {			
					for (int day = 1; day < noOfDays; day++) {
						
						Date nextDate = dateUtil.nextDate(startDate, day);
						_log.trace("next Date to be inserted is : "+nextDate);
						
						CommodityHistoricalDataDTO commodityPriceDTO =new CommodityHistoricalDataDTO();
						
						commodityPriceDTO.setClose(null);
						commodityPriceDTO.setPeriod(nextDate);
						commodityPriceDTO.setDailyChange(null);
						
						finalCommodityPriceList.add(commodityPriceDTO);
					}
				}
			};

			finalCommodityPriceList.add(commodityDTO.get(commodityDTO.size()-1));

		//	finalStockPriceList.sort(Comparator.comparing(StockPriceDTO::getDate));	
		
		}catch(Exception e) {
			try {
				throw new Exception("No Data Found");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return finalCommodityPriceList;
	}

}
