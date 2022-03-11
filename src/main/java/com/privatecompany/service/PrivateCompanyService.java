package com.privatecompany.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.privatecompany.dao.PrivateCompanyRepository;
import com.privatecompany.dto.PrivateCompanyDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;


@Service
public class PrivateCompanyService {
	
	Logger _log = Logger.getLogger(PrivateCompanyService.class);
	
	@Autowired
	PrivateCompanyRepository pcRepository;

	public List<CompanyDTO> getPrivateCompanies(String searchCriteria,
			Integer resultCount, Integer countryIds, String companyIds,
			Boolean excludeDuplicateFlag, List<String> userCountryList) {
		// TODO Auto-generated method stub
		return null;
	}

	public PrivateCompanyDTO getBasicInfo(String entityId) {
		// TODO Auto-generated method stub
		return pcRepository.getBasicInfo(entityId);
	}
	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result.size()==0")
	public  List<CompanyLatestFilingInfoDTO> getCompanyLatestFilingInfo(String entityId) throws Exception {
		
		
		List<CompanyLatestFilingInfoDTO> companyLatestFilingInfoDTOs = pcRepository.getCompanyLatestFilingInfo(entityId);
		
		return companyLatestFilingInfoDTOs;
	}

}
