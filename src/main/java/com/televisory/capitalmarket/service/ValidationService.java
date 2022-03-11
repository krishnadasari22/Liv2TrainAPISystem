package com.televisory.capitalmarket.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.model.EconomyRequest;
import com.televisory.capitalmarket.model.EquityRequest;
import com.televisory.capitalmarket.util.BadRequestException;

/**
 * 
 * @author vinay
 *
 */
@Service
public class ValidationService {
	
	Logger _log = Logger.getLogger(CapitalMarketService.class);
	
	/**
	 * Method to validate the Data Request for Excel Data Download
	 * 
	 * @param downloadRequest
	 * @throws BadRequestException
	 */
	public void ValidateDownloadDataRequest(DownloadRequest downloadRequest) throws BadRequestException {
			
			/*if(downloadRequest.getStartDate()==null){
				_log.error("Start date is not defined");
				throw new BadRequestException("Start date is not defined");
			}
			
			if(downloadRequest.getEndDate()==null){
				_log.error("End date is not defined");
				throw new BadRequestException("End date is not defined");
			}*/
			
			if(downloadRequest.getEquity()!=null){
				for(EquityRequest equityRequest:downloadRequest.getEquity()){
					validateEquityRequest(equityRequest);
				}
			}
			
			if(downloadRequest.getEconomy()!=null){
				for(EconomyRequest economyRequest:downloadRequest.getEconomy()){
					validateEconomyRequest(economyRequest);
				}
			}
			
		}


	/**
	 * Method to valid the economy Request
	 * @param economyRequest
	 * @throws BadRequestException
	 */
	private void validateEconomyRequest(EconomyRequest economyRequest) throws BadRequestException {
		
		if(economyRequest.getType()==null ||economyRequest.getType()==""){
			_log.error("Economy Type cannot be null or empty");
			throw new BadRequestException("Economy Type cannot be null or empty");
		}
		
		if(economyRequest.getId()==null||economyRequest.getId().isEmpty()){
			_log.error("Economy Data Id cannot be null or empty");
			throw new BadRequestException("Econpmy Data Id cannot be null or empty");
		}
		
		if(economyRequest.getId()==null ){
			_log.error("Economy data Type name cannot be null or empty");
			throw new BadRequestException("Econpmy data Type name cannot be null or empty");
		}
		
		if(economyRequest.getFilterList().size()==0||economyRequest.getFilterList().size()==0){
			_log.error("Economy filter list cannot be null or empty");
			throw new BadRequestException("Economy filter list cannot be null or empty");
		}
		
		/*if(economyRequest.getPeriodicity()==null||economyRequest.getPeriodicity()==""){
			List<String> filterList = economyRequest.getFilterList().stream()
				.map(i -> i.toUpperCase())
				.collect(Collectors.toList());
			if( ! filterList.contains(CMStatic.ECONOMY_CREDIT_RATING) 
					&& ! filterList.contains(CMStatic.ECONOMY_COUNTRY_DEFAULT_SPREAD)
					&& ! filterList.contains(CMStatic.ECONOMY_EQUITY_RISK_PREMIUM)
					&& ! filterList.contains(CMStatic.ECONOMY_COUNTRY_RISK_PREMIUM) ) {
				_log.error("Economy data periodicity cannot be null or empty");
				throw new BadRequestException("Economy data periodicity cannot be null or empty");
			}
		}*/
	}


	private void validateEquityRequest(EquityRequest equityRequest) throws BadRequestException {
		
		if(equityRequest.getExchange()==null || equityRequest.getExchange()==""){
			_log.error("Equity exchange name cannot be null or empty");
			throw new BadRequestException("Equity exchange name cannot be null or empty");
		}
		
		if(equityRequest.getType()==null ||equityRequest.getType()==""){
			_log.error("Equity Type cannot be null or empty");
			throw new BadRequestException("Equity Type cannot be null or empty");
		}
		if(equityRequest.getCode()==null){
			_log.error("Equity code cannot be null or empty");
			throw new BadRequestException("Equity code cannot be null or empty");
		}
		
		if(equityRequest.getDataType()==null||equityRequest.getDataType()==""){
			_log.error("Equity Data Type cannot be null or empty");
			throw new BadRequestException("Equity data Type cannot be null or empty");
		}
		
		if(equityRequest.getName()==null || equityRequest.getName()==""){
			_log.error("Equity data Type name cannot be null or empty");
			throw new BadRequestException("Equity data Type name cannot be null or empty");
		}
		
		if(equityRequest.getPeriodicity()==null||equityRequest.getPeriodicity()==""){
			_log.error("Equity data periodicity cannot be null or empty");
			throw new BadRequestException("Equity data periodicity cannot be null or empty");
		}
	}
	
	
	public boolean validateDownloadDataResponse(DownloadRequest downloadRequest) throws Exception{
		if(downloadRequest.getEquity() != null)
			for(EquityRequest equityRequest:downloadRequest.getEquity()){
				if( equityRequest.getStockPriceDTOs()!=null && equityRequest.getStockPriceDTOs().size()>0)
					return true;
				
				if( equityRequest.getCompanyFinancialDTOs()!=null && equityRequest.getCompanyFinancialDTOs().size()>0)
					return true;
				
				if( equityRequest.getBetaDatas()!=null && equityRequest.getBetaDatas().size()>0)
					return true;
			}
		
		if(downloadRequest.getEconomy() != null)
			for(EconomyRequest economyRequest:downloadRequest.getEconomy()){
				if( economyRequest.getEconomyData()!=null && economyRequest.getEconomyData().size()>0)
					return true;
			}
		
		if(downloadRequest.getCommodity() != null){
			if( downloadRequest.getCommodity()!=null 
					&& downloadRequest.getCommodity().getCommodityList()!=null
					&& !downloadRequest.getCommodity().getCommodityData().isEmpty())
				return true;
		}
		
		throw new Exception("No Data Found");
	}

}
