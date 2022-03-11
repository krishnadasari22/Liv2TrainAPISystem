package com.televisory.capitalmarket.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CMStockRepository;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.factset.dto.FFStockSpinoffDTO;
import com.televisory.capitalmarket.factset.dto.FFStockSplitDTO;

@Service
public class CorporateAnnouncementService {
	
	Logger _log = Logger.getLogger(CorporateAnnouncementService.class);
	
	@Autowired
	CMStockRepository cmStockRepository;
	
	
	public List<StockPriceDTO> getCorpAnnouncementAdjustedStockPrice(List<StockPriceDTO> factSetStockPrice, String companyId) {
		try {
			//get the stock split data
			List<FFStockSplitDTO> stockAdjustFactorList =cmStockRepository.getSplitStockInfo(companyId);
			//Adjust stock price 
			stockAdjustFactorList.forEach(stockAdjustFactorObj -> {
				adjustSplitStockPrice(factSetStockPrice, stockAdjustFactorObj);			
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			List<FFStockSpinoffDTO> spinoffStockAdjustFactorList =cmStockRepository.getSpinoffStockInfo(companyId);
			//Adjust stock price 
			spinoffStockAdjustFactorList.forEach(stockAdjustFactorObj -> {
				adjustSpinoffStockPrice(factSetStockPrice, stockAdjustFactorObj);			
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return factSetStockPrice;
	}
	
	public StockPriceDTO getCorpAnnouncementAdjustedStockPrice(StockPriceDTO factSetStockPrice, String companyId) {
		
		try {
			//get the stock split data
			List<FFStockSplitDTO> stockAdjustFactorList =cmStockRepository.getSplitStockInfo(companyId);
			stockAdjustFactorList.forEach(stockAdjustFactorObj -> {
				adjustSplitStockPrice(factSetStockPrice,stockAdjustFactorObj);			
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			List<FFStockSpinoffDTO> spinoffStockAdjustFactorList =cmStockRepository.getSpinoffStockInfo(companyId);
			//Adjust stock price 
			spinoffStockAdjustFactorList.forEach(stockAdjustFactorObj -> {
				adjustSpinoffStockPrice(factSetStockPrice, stockAdjustFactorObj);			
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return factSetStockPrice;
	}

	private List<StockPriceDTO> adjustSplitStockPrice(List<StockPriceDTO> factSetStockPrice,FFStockSplitDTO stockAdjustFactorObj) {
		
		//_log.info(stockAdjustFactorObj.getFactor());
		factSetStockPrice.forEach(stockPriceData -> {
			if(stockAdjustFactorObj.getDate().after(stockPriceData.getDate())){
				if(stockPriceData.getOpen()!=null){
					stockPriceData.setOpen(stockPriceData.getOpen() * stockAdjustFactorObj.getFactor());
				}
				if(stockPriceData.getClose()!=null){
					stockPriceData.setClose(stockPriceData.getClose() * stockAdjustFactorObj.getFactor());
				}
				if(stockPriceData.getHigh()!=null){
					stockPriceData.setHigh(stockPriceData.getHigh() * stockAdjustFactorObj.getFactor());
				}
				if(stockPriceData.getLow()!=null){
					stockPriceData.setLow(stockPriceData.getLow() * stockAdjustFactorObj.getFactor());
				}
				/*if(stockPriceData.getVolume()!=null){
					stockPriceData.setVolume( Math.round(stockPriceData.getVolume() / stockAdjustFactorObj.getFactor())*1.0D );
				}*/
			}else{
				return;
			}
		});
		return factSetStockPrice;	
	}
	
	private StockPriceDTO adjustSplitStockPrice(StockPriceDTO stockPriceData,FFStockSplitDTO stockAdjustFactorObj) {
		//_log.info(stockAdjustFactorObj.getFactor());
		
		if(stockAdjustFactorObj.getDate().after(stockPriceData.getDate())){
			if(stockPriceData.getOpen()!=null){
				stockPriceData.setOpen(stockPriceData.getOpen() * stockAdjustFactorObj.getFactor());
			}
			if(stockPriceData.getClose()!=null){
				stockPriceData.setClose(stockPriceData.getClose() * stockAdjustFactorObj.getFactor());
			}
			if(stockPriceData.getHigh()!=null){
				stockPriceData.setHigh(stockPriceData.getHigh() * stockAdjustFactorObj.getFactor());
			}
			if(stockPriceData.getLow()!=null){
				stockPriceData.setLow(stockPriceData.getLow() * stockAdjustFactorObj.getFactor());
			}
			/*if(stockPriceData.getVolume()!=null){
				stockPriceData.setVolume( Math.round(stockPriceData.getVolume() / stockAdjustFactorObj.getFactor())*1.0D );
			}*/
		}
		return stockPriceData;	
	}
	
	
	
	private List<StockPriceDTO> adjustSpinoffStockPrice(List<StockPriceDTO> factSetStockPrice,FFStockSpinoffDTO stockAdjustFactorObj) {
		
		//_log.info(stockAdjustFactorObj.getFactor());
		factSetStockPrice.forEach(stockPriceData -> {
			if(stockAdjustFactorObj.getDate().after(stockPriceData.getDate())){
				if(stockPriceData.getOpen()!=null){
					stockPriceData.setOpen(stockPriceData.getOpen() * stockAdjustFactorObj.getFactor());
				}
				if(stockPriceData.getClose()!=null){
					stockPriceData.setClose(stockPriceData.getClose() * stockAdjustFactorObj.getFactor());
				}
				if(stockPriceData.getHigh()!=null){
					stockPriceData.setHigh(stockPriceData.getHigh() * stockAdjustFactorObj.getFactor());
				}
				if(stockPriceData.getLow()!=null){
					stockPriceData.setLow(stockPriceData.getLow() * stockAdjustFactorObj.getFactor());
				}
				/*if(stockPriceData.getVolume()!=null){
					stockPriceData.setVolume( Math.round(stockPriceData.getVolume() / stockAdjustFactorObj.getFactor())*1.0D );
				}*/
			}else{
				return;
			}
		});
		return factSetStockPrice;	
	}
	
	private StockPriceDTO adjustSpinoffStockPrice(StockPriceDTO stockPriceData,FFStockSpinoffDTO stockAdjustFactorObj) {
		//_log.info(stockAdjustFactorObj.getFactor());
		
		if(stockAdjustFactorObj.getDate().after(stockPriceData.getDate())){
			if(stockPriceData.getOpen()!=null){
				stockPriceData.setOpen(stockPriceData.getOpen() * stockAdjustFactorObj.getFactor());
			}
			if(stockPriceData.getClose()!=null){
				stockPriceData.setClose(stockPriceData.getClose() * stockAdjustFactorObj.getFactor());
			}
			if(stockPriceData.getHigh()!=null){
				stockPriceData.setHigh(stockPriceData.getHigh() * stockAdjustFactorObj.getFactor());
			}
			if(stockPriceData.getLow()!=null){
				stockPriceData.setLow(stockPriceData.getLow() * stockAdjustFactorObj.getFactor());
			}
			/*if(stockPriceData.getVolume()!=null){
				stockPriceData.setVolume( Math.round(stockPriceData.getVolume() / stockAdjustFactorObj.getFactor())*1.0D );
			}*/
		}
		return stockPriceData;	
	}

}
