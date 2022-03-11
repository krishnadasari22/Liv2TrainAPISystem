package com.pcompany.dao;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.CompanyDocumentMetadataDTO;
import com.pcompany.dto.EventsDTO;
import com.pcompany.dto.InsiderTransactionDTO;
import com.pcompany.dto.TranscriptFileNameDTO;

public interface EventsDao {

	List<InsiderTransactionDTO> getTransactionData(String securityId, Date startDate, Date endDate, String currency);

	List<EventsDTO> getEventsData(String entityId, Integer years);

	List<CompanyDocumentMetadataDTO> companyFilings(String companyId, Integer years, Integer pageNo,String formType);

	List<CompanyDocumentMetadataDTO> companyFilings(String companyId, Integer years);
	
	TranscriptFileNameDTO getTranscriptFileName(String reportId);

}
