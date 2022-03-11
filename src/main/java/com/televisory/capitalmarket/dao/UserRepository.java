package com.televisory.capitalmarket.dao;

import java.util.Date;
import java.util.List;

import com.televisory.user.UserModelDTO;
import com.televisory.user.dto.UserDataScoringCommentDto;

public interface UserRepository {

	List<UserModelDTO> getUserFinancial(String userId, String financialType, String periodType,Date startDate, Date endDate, String currency);
	List<UserModelDTO> getUserSensitivityAnalysis(String userId, String financialType, String periodType,Date startDate, Date endDate, String currency);
	List<UserModelDTO> getUserDebtTrap(String userId, String financialType, String periodType,Date startDate, Date endDate, String currency);
	List<UserModelDTO> getUserDebtSizing(String userId, String financialType, String periodType,Date startDate, Date endDate, String currency);
	List<UserModelDTO> getPeerData(String userId, String financialType, String periodType,Date startDate, Date endDate, String currency, String peerCalculation, String parameterName);
	List<UserModelDTO> getScore(String userId, String financialType, String periodType,Date startDate, Date endDate, String peerCalculation, String parameterName);
	List<UserDataScoringCommentDto> getUserScoreComment(String userId, List<String> fieldName, Date period);
}
