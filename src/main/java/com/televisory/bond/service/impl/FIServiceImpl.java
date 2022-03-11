package com.televisory.bond.service.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.bond.Model.FIResponseModel;
import com.televisory.bond.dao.FIDao;
import com.televisory.bond.dto.FIBalanceModelDTO;
import com.televisory.bond.dto.FIComparableDTO;
import com.televisory.bond.dto.FIHistoricalDTO;
import com.televisory.bond.dto.FILatestDataDTO;
import com.televisory.bond.dto.FINameDTO;
import com.televisory.bond.service.FiService;

@Service
public class FIServiceImpl implements FiService{
	
	public static Logger _log = Logger.getLogger(FIServiceImpl.class);
	
	@Autowired
	ExecutorService executorService;
	
	@Autowired
	FIDao fiDao;

	@Override
	public List<String> getFiCurrencyList(String categeory) {
		return fiDao.getFiCurrencyList(categeory);
	}

	@Override
	public List<FINameDTO> getFiNameList(String categeory, String currency, String searchCriteria) {
		return fiDao.getFiNameList(categeory,currency,searchCriteria);
	}

	@Override
	public String getDefaultFIIdentifier() {
		return fiDao.getDefaultFIIdentifier();
	}

	@Override
	public FIResponseModel getFiData(String identifier, String fieldName) {
		
		Future<List<String>> categeoryFuture = executorService.submit(() -> {
			return fiDao.getFiCategeoryList();
		});

		Future<List<String>> currencyFuture = executorService.submit(() -> {
			return fiDao.getFiCurrencyList("");
		});

		Future<List<FINameDTO>> cdsNameFuture = executorService.submit(() -> {
			return fiDao.getFiNameList(null, null, null);
		});

		Future<List<FIBalanceModelDTO>> balanceModelFuture = executorService.submit(() -> {
			return fiDao.getFIMetricList();
		});
		
		Future<FILatestDataDTO> latestDataFuture = executorService.submit(() -> {
			return fiDao.getLatestDateData(identifier);
		});
		
		Future<List<FIHistoricalDTO>> historicalDataFuture = executorService.submit(() -> {
			return fiDao.getHistoricalData(identifier, fieldName);
		});
		
		FIResponseModel responseModel =new FIResponseModel();
		
		try {
			responseModel.setCategory(categeoryFuture.get());
			_log.info("Categeory updated for FI Response Model");
			
			responseModel.setCurrency(currencyFuture.get());
			_log.info("Currency updated for FI Response Model");
		
			responseModel.setBalanceModel(balanceModelFuture.get());
			_log.info("Balance Model updated for FI Response Model");
			
			responseModel.setBondNameList(cdsNameFuture.get());
			_log.info("CDS Name List updated for FI Response Model");
			
			responseModel.setBondLatestData(latestDataFuture.get());
			_log.info("CDS Latest Data updated for FI Response Model");
			
			responseModel.setBondHistoricalData(historicalDataFuture.get());
			_log.info("CDS Historical Data updated for FI Response Model");
		
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return responseModel;
		
	}

	@Override
	public List<String> getFiCategeoryList() {
		// TODO Auto-generated method stub
		return fiDao.getFiCategeoryList();
	}
	
	@Override
	public List<FIComparableDTO> getComparable(String identifier, List<String> includeIdentifier, List<String> excludeIdentifier) throws Exception {
		return fiDao.getComparable(identifier, includeIdentifier, excludeIdentifier);
	}

}
