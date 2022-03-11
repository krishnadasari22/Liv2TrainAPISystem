package com.televisory.capitalmarket.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.service.CMFinancialDataService;
import com.televisory.capitalmarket.service.UserService;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.user.UserModelDTO;

import io.swagger.annotations.Api;

@Controller
@RequestMapping(value="user")
@Api(value = "User", description = "Rest API for User", tags = "CM User API")
public class UserController {
Logger _log = Logger.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	
	@GetMapping("/user/{userId:.+}/financial/{financialType}")
	public ResponseEntity<List<UserModelDTO>> getUserFinancial(@PathVariable(name="userId") String userId
			,@PathVariable("financialType") String financialType,
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency){
		_log.info("in getCompanyFinancial");
		List<UserModelDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			financialData = userService.getUserFinancial(userId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency);
			/*if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
				financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, financialData, currency));
			} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
				financialData = ratioService.getCompanyRatio(companyId, periodType, financialData, currency);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<UserModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<UserModelDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId:.+}/sensitivityAnalysis")
	public ResponseEntity<List<UserModelDTO>> getUserSensitivityAnalysis(@PathVariable(name="userId") String userId
			,/*@PathVariable("financialType") String financialType,*/
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency){
		_log.info("in getCompanyFinancial");
		List<UserModelDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			String financialType = CMStatic.DATA_SENSITIVITY;
			financialData = userService.getUserSensitivityAnalysis(userId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency);
			/*if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
				financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, financialData, currency));
			} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
				financialData = ratioService.getCompanyRatio(companyId, periodType, financialData, currency);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<UserModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<UserModelDTO>>(financialData, HttpStatus.OK);
	}
	
	
	@GetMapping("/user/{userId:.+}/debtTrap")
	public ResponseEntity<List<UserModelDTO>> getUserDebtTrap(@PathVariable(name="userId") String userId
			,/*@PathVariable("financialType") String financialType,*/
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency){
		_log.info("in getCompanyFinancial");
		List<UserModelDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			String financialType = CMStatic.DTA;
			financialData = userService.getUserDebtTrap(userId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency);
			/*if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
				financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, financialData, currency));
			} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
				financialData = ratioService.getCompanyRatio(companyId, periodType, financialData, currency);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<UserModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<UserModelDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId:.+}/debtSizing")
	public ResponseEntity<List<UserModelDTO>> getUserDebtSizing(@PathVariable(name="userId") String userId
			,/*@PathVariable("financialType") String financialType,*/
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency){
		_log.info("in getCompanyFinancial");
		List<UserModelDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			String financialType = CMStatic.DEBT_SIZING;
			financialData = userService.getUserDebtSizing(userId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency);
			/*if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
				financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, financialData, currency));
			} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
				financialData = ratioService.getCompanyRatio(companyId, periodType, financialData, currency);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<UserModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<UserModelDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("/peerData/{userId:.+}")
	public ResponseEntity<List<UserModelDTO>> getPeerData(@PathVariable(name="userId") String userId,
			@RequestParam(value="financialType",required=false) String financialType,
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="parameterName",required=false) String parameterName,
			@RequestParam(value="peerCalculation",required=false) String peerCalculation){
		_log.info("in get user peer data for userId: "+ userId);
		List<UserModelDTO> financialData = null;
		try {
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			financialData = userService.getPeerData(userId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,currency, parameterName, peerCalculation);
			/*if (financialType != null && financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE)) {
				financialData.addAll(ratioService.getCompanyRatio(companyId, periodType, financialData, currency));
			} else if(financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)) {
				financialData = ratioService.getCompanyRatio(companyId, periodType, financialData, currency);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<UserModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<UserModelDTO>>(financialData, HttpStatus.OK);
	}
	
	@GetMapping("/score/{userId:.+}")
	public ResponseEntity<List<UserModelDTO>> getScore(@PathVariable(name="userId") String userId,
			@RequestParam(value="financialType",required=false) String financialType,
			@RequestParam("periodType") String periodType,@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate, 
			@RequestParam(value="parameterName",required=false) String parameterName,
			@RequestParam(value="peerCalculation",required=false) String peerCalculation){
		_log.info("in getCompanyFinancial");
		List<UserModelDTO> financialData = null;
		try {
			
			financialData = userService.getScore(userId, financialType, periodType, startDate!=null?startDate.toDate():null, endDate!=null?endDate.toDate():null,peerCalculation, parameterName);
		
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<UserModelDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<UserModelDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCompanyFinancial");
		return new ResponseEntity<List<UserModelDTO>>(financialData, HttpStatus.OK);
	}
}

