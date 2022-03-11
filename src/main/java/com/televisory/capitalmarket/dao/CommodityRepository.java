package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;

import com.televisory.capitalmarket.dto.economy.CommodityHistoricalDataDTO;
import com.televisory.capitalmarket.dto.economy.CommodityLatestDataDTO;
import com.televisory.capitalmarket.dto.economy.NewsDTO;
import com.televisory.capitalmarket.model.DownloadRequest;

public interface CommodityRepository {
	
	List<CommodityLatestDataDTO> getCommodityLatestData(String currency);
	
	List<CommodityLatestDataDTO> getCommodities();

	List<CommodityHistoricalDataDTO> getCommodityHistoricalData(List<String> symbolList, String currency,
			Date startDate, Date endDate);

	List<NewsDTO> getNews();

	
	/*List<CommodityHistoricalDataDTO> getCommodityHistoricalData(
			DownloadRequest downloadRequest);*/
	
}