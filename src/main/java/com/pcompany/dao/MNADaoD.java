package com.pcompany.dao;

import java.util.List;

import com.pcompany.dto.MNABalanceModelDTO;
import com.pcompany.dto.MNADealAdvisorDTO;
import com.pcompany.dto.MNADealMetaDataDTO;
import com.pcompany.dto.MNADealTermDTO;

public interface MNADaoD {

	String getSynopsis(Integer dealId);
	
	List<MNABalanceModelDTO> getMNABalanceModel();
	
	MNADealMetaDataDTO getDealMetaData(Integer dealId, String entityId);
	
	List<MNADealAdvisorDTO> getDealAdvisor(Integer dealId, String currency);

	List<MNADealTermDTO> getDealTerms(Integer dealId, String currency);

}
