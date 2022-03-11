package com.privatecompany.dao;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.PEVCFundingDTO;
import com.pcompany.dto.PEVCFundingInvestmentDTO;


public interface PEVCDaoC {

	List<PEVCFundingDTO> getFundingDetailList(String country, Date startDate,
			Date endDate, String industry, String currency,
			String financingType, String entityId, Integer rowOffset,
			Integer rowCount, String sortingColumn, String sortingType);

	Long getFundingDetailCount(String country, Date startDate, Date endDate,
			String industry, String currency, String financingType, String entityId);

	List<PEVCFundingInvestmentDTO> getFundingInvestmentList(String entityId,
			Date startDate, Date endDate, String currency,String financingType);

	List<PEVCFundingDTO> allFundingDetailAdvancedSearch(String country, Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId, String issueType, Double minAmount,
			Double maxAmount, Integer rowOffset, Integer rowCount, String sortingColumn, String sortingType);

	Long getFundingAdvancedSearchDetailCount(String country, Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId, String issueType, Double minAmount,
			Double maxAmount);
	
}
