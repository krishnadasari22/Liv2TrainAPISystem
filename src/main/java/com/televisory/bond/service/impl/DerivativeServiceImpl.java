package com.televisory.bond.service.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.bond.Model.DerivativeResponseModel;
import com.televisory.bond.dao.DerivativeDao;
import com.televisory.bond.dto.DerFuCoparableDTO;
import com.televisory.bond.dto.DerOpCoparableDTO;
import com.televisory.bond.dto.DerivativeBalanceModelDTO;
import com.televisory.bond.dto.DerivativeHistoricalDataDTO;
import com.televisory.bond.dto.DerivativeLatestDTO;
import com.televisory.bond.dto.DerivativeNameDTO;
import com.televisory.bond.entity.DerivativeIdentifierWithProp;
import com.televisory.bond.service.DerivativeService;

@Service
@Transactional
public class DerivativeServiceImpl implements DerivativeService{
	
	public static Logger _log = Logger.getLogger(DerivativeServiceImpl.class);
	
	@Autowired
	DerivativeDao derivativeDao;
	
	@Autowired
	ExecutorService executorService;

	@Override
	public List<DerivativeNameDTO> getDerivativeNameList(String searchCriteria) {
		return derivativeDao.getDerivativeNameList(searchCriteria);
	}

	@Override
	public List<String> getDerivativeCategoryList(String derivativeName) {
		return derivativeDao.getDerivativeCategoryList(derivativeName);
	}

	

	@Override
	public DerivativeResponseModel getDerivativeData(String identifier, String fieldName) {
		
		Future<List<DerivativeBalanceModelDTO>> balanceModelFuture = executorService.submit(() -> {
			return derivativeDao.getDerivativeMetricList();
		});
		
		Future<DerivativeLatestDTO> latestDataFuture = executorService.submit(() -> {
			return derivativeDao.getLatestDateData(identifier);
		});
		
		Future<List<DerivativeHistoricalDataDTO>> historicalDataFuture = executorService.submit(() -> {
			return derivativeDao.getHistoricalData(identifier, fieldName);
		});
		
		DerivativeResponseModel responseModel =new DerivativeResponseModel();
		
		try {
			
			responseModel.setBalanceModel(balanceModelFuture.get());
			_log.info("Balance Model updated for Derivative Response Model");
			
			
			responseModel.setLatestData(latestDataFuture.get());
			_log.info("Derivative Latest Data updated for Derivative Response Model");
			
			responseModel.setHistoricalData(historicalDataFuture.get());
			_log.info("Derivative Historical Data updated for Derivative Response Model");
			
			/*responseModel.setHistoricalData(historicalDataFuture.get());
			_log.info("Derivative Historical Data updated for Derivative Response Model");*/
		
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return responseModel;
		
	}

	@Override
	public List<DerivativeIdentifierWithProp> getDerivativeExpiryDateList(String derivativeName, String category) {
		return derivativeDao.getDerivativeExpiryDateList(derivativeName,category);
	}

	@Override
	public List<String> getDerivativeOptionType(String derivativeName, String category, String expiryDate) {
		return derivativeDao.getDerivativeOptionType(derivativeName, category, expiryDate);
	}

	@Override
	public List<DerivativeIdentifierWithProp> getDerivativeStrikeList(String derivativeName, String category,
			String expiryDate, String optionType) {
		return derivativeDao.getDerivativeStrikeList(derivativeName,category,expiryDate,  optionType);
	}
	
	@Override
	public String getDefaultIdentifier() {
		return derivativeDao.getDefaultIdentifier();
	}
	
	@Override
	public List<DerFuCoparableDTO> getFuComparable(String identifier) {
		return derivativeDao.getFuComparable(identifier);
	}

	@Override
	public List<DerOpCoparableDTO> getOpComparable(String identifier) {
		return derivativeDao.getOpComparable(identifier);
	}
}
