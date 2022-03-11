package com.privatecompany.dao;

import java.util.Date;
import java.util.List;

import com.privatecompany.dto.CompanyEpsDTO;
import com.privatecompany.dto.DealHistoryDTO;

public interface MNADaoC {

	List<DealHistoryDTO> getAllDealHistory(String entityId, Date startDate, Date endDate, String currency);

	List<CompanyEpsDTO> getEpsData(String companyId, String periodicity, Date startDate, Date endDate, String currency);

}
