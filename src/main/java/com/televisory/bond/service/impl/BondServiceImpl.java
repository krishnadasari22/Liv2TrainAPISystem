package com.televisory.bond.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.bond.Model.CDSResponseModel;
import com.televisory.bond.dao.IBondDao;
import com.televisory.bond.dto.CDSBalanceModelDTO;
import com.televisory.bond.dto.CDSComparableDTO;
import com.televisory.bond.dto.CDSDataLatestDTO;
import com.televisory.bond.dto.CDSHistoricalDataDTO;
import com.televisory.bond.dto.CDSNameDTO;
import com.televisory.bond.service.IBondService;

@Service
@Transactional
public class BondServiceImpl implements IBondService {

	Logger _log = Logger.getLogger(BondServiceImpl.class);

	@Autowired
	IBondDao bondDao;

	@Autowired
	ExecutorService executorService;


	@Override
	public String getDefaultCDSIdentifier() {
		return bondDao.getDefaultCDSIdentifier();
	}

	@Override
	public <T> List<T> getAdvanceSearch(Map<String,String> filters, String requestType){
		filters.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().equals(""));
		return bondDao.getAdvanceBondSearchGen(filters ,requestType);
	}

	@Override
	public Map<String, Object> getProjectedValues(List<String> vals, String moduleName){
		Map<String, Object> projectionMap = new ConcurrentHashMap<>();
		for (int i = 0; i < vals.size(); i++) {
			final int valCount = i;
			//executorService.execute(() -> {
				Map<String, Object> myMap =	bondDao.getProjectedValues(vals.get(valCount),moduleName);
				//synchronized (projectionMap) {
				if(myMap!=null){
					projectionMap.putAll(myMap);
				}
				//}
			//});
		}
		return projectionMap;
	}

	@Override
	public Map<String,Object> getCurrentValues(Map<String,String> vals,String moduleName) {
		vals.entrySet().removeIf(entry -> entry.getValue() == null);
		return bondDao.getCurrentValues(vals, moduleName);
	}

	@Override
	public List<CDSBalanceModelDTO> getCdsMetricList() {
		return bondDao.getCdsMetricList();
	}

	@Override
	public List<String> getCdsCountryList() {
		return bondDao.getCdsCountryList();
	}

	@Override
	public List<String> getCdsCurrencyList(String sector) {
		return bondDao.getCdsCurrencyList(sector);
	}

	@Override
	public CDSDataLatestDTO getLatestDateData(String identifier) throws Exception {
		return bondDao.getLatestDateData(identifier);
	}

	@Override
	public List<CDSNameDTO> getCdsNameList(String sector, String currency, String searchCriteria) {
		return bondDao.getCdsNameList(sector, currency, searchCriteria);
	}

	@Override
	public List<CDSHistoricalDataDTO> getHistoricalData(String identifier, String fieldName) {
		return bondDao.getHistoricalData(identifier, fieldName);
	}

	@Override
	public List<String> getCdsSectorList() {
		return bondDao.getCdsSectorList();
	}


	@Override
	public CDSResponseModel getCDSData(String identifier, String fieldName) {

		AtomicReference<String> field =new AtomicReference<>();

		field.set(fieldName);

		Future<List<String>> sectorFuture = executorService.submit(() -> {
			return bondDao.getCdsSectorList();
		});

		Future<List<String>> currencyFuture = executorService.submit(() -> {
			return bondDao.getCdsCurrencyList("");
		});

		Future<List<CDSNameDTO>> cdsNameFuture = executorService.submit(() -> {
			return bondDao.getCdsNameList(null, null, null);
		});

		Future<List<CDSBalanceModelDTO>> balanceModelFuture = executorService.submit(() -> {
			return bondDao.getCdsMetricList();
		});

		Future<CDSDataLatestDTO> latestDataFuture = executorService.submit(() -> {
			return bondDao.getLatestDateData(identifier);
		});

		CDSResponseModel responseModel =new CDSResponseModel();

		try {

			CDSDataLatestDTO latestData = latestDataFuture.get();

			if(fieldName=="" || fieldName== null){
				if(latestData.getMarket_quoting_convention().equals("QuoteSpread")){
					field.set("quote_spread_mid");
				}else if(latestData.getMarket_quoting_convention().equals("PercentOfPar")){
					field.set("percent_of_par_mid");
				}else{
					field.set("up_front_mid");
				}
			}

			Future<List<CDSHistoricalDataDTO>> historicalDataFuture = executorService.submit(() -> {
				return bondDao.getHistoricalData(identifier, field.get());
			});

			responseModel.setSector(sectorFuture.get());
			_log.info("Sector updated for CDS Response Model");

			responseModel.setCurrency(currencyFuture.get());
			_log.info("Currency updated for CDS Response Model");

			responseModel.setBalanceModel(balanceModelFuture.get());
			_log.info("Balance Model updated for CDS Response Model");

			responseModel.setCdsNameList(cdsNameFuture.get());
			_log.info("CDS Name List updated for CDS Response Model");

			responseModel.setCdsLatestData(latestData);
			_log.info("CDS Latest Data updated for CDS Response Model");

			responseModel.setCdsHistoricalData(historicalDataFuture.get());
			_log.info("CDS Historical Data updated for CDS Response Model");

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return responseModel;

	}
	
	@Override
	public List<CDSComparableDTO> getComparable(String identifier, List<String> includeIdentifier, List<String> excludeIdentifier) throws Exception {
		return bondDao.getComparable(identifier, includeIdentifier, excludeIdentifier);
	}

}