package com.televisory.capitalmarket.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.UserRepository;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.user.UserModelDTO;
import com.televisory.user.dto.UserDataScoringCommentDto;
@Service
public class UserService {

	Logger _log = Logger.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	public List<UserModelDTO> getUserFinancial(String userId, String financialType, String periodType, Date startDate, Date endDate,String currency) {
		if(financialType!=null 
				&& (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE) || financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE))) {
			financialType = CMStatic.FINANCIAL_ALL;
		}
		return userRepository.getUserFinancial(userId, financialType, periodType, startDate, endDate, currency);

	}
	public List<UserModelDTO> getUserSensitivityAnalysis(String userId, String financialType, String periodType, Date startDate, Date endDate,String currency) {
		if(financialType!=null && (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE))) {
			financialType = CMStatic.DATA_SENSITIVITY;
		}
		return userRepository.getUserSensitivityAnalysis(userId, financialType, periodType, startDate, endDate, currency);

	}
	public List<UserModelDTO> getUserDebtTrap(String userId, String financialType, String periodType, Date startDate, Date endDate,String currency) {
		if(financialType!=null && (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE))) {
			financialType = CMStatic.DTA;
		}
		return userRepository.getUserDebtTrap(userId, financialType, periodType, startDate, endDate, currency);

	}
	public List<UserModelDTO> getUserDebtSizing(String userId, String financialType, String periodType, Date startDate, Date endDate,String currency) {
		if(financialType!=null  && (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE))) {
			financialType = CMStatic.DEBT_SIZING;
		}
		return userRepository.getUserDebtSizing(userId, financialType, periodType, startDate, endDate, currency);

	}
	public List<UserModelDTO> getPeerData(String userId, String financialType, String periodType, Date startDate, Date endDate,String currency, String peerCalculation, String parameterName) {

		if((financialType==null && parameterName == null) || (financialType!=null 
				&& (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE) 
						|| financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE)))) {
			financialType = CMStatic.FINANCIAL_ALL;
		}
		return userRepository.getPeerData(userId, financialType, periodType, startDate, endDate, currency, peerCalculation, parameterName);
	}
	
	public List<UserModelDTO> getScore(String userId, String financialType, String periodType, Date startDate, Date endDate, String peerCalculation, String parameterName) {
		if(financialType!=null 
				&& (financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_CODE) || financialType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE))) {
			financialType = CMStatic.FINANCIAL_ALL;
		}
		return userRepository.getScore(userId, financialType, periodType, startDate, endDate, peerCalculation, parameterName);
	}

	public List<UserDataScoringCommentDto> getUserScoreComment(String userId, List<String> fieldName, Date period) {
		return userRepository.getUserScoreComment( userId,fieldName,  period);
	}




}
