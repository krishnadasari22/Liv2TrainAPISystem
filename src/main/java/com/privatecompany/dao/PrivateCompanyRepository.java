package com.privatecompany.dao;

import java.util.List;

import com.privatecompany.dto.PrivateCompanyDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;


public interface PrivateCompanyRepository {

	PrivateCompanyDTO getBasicInfo(String entityId);
	List<CompanyLatestFilingInfoDTO> getCompanyLatestFilingInfo(String entityId);
}
