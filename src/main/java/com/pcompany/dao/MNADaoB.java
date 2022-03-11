package com.pcompany.dao;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.MNATopDealMakerDTO;
import com.pcompany.dto.MNATopDealMakerTotalDTO;

public interface MNADaoB {

	List<MNATopDealMakerDTO> getTopDealMakerList(String country,
			String industry, String currency, Date startDate, Date endDate,
			Integer rowOffset, Integer rowCount, String sortingColumn,
			String sortingType);

	Long getTopDealMakerCount(String country, String industry,
			String currency, Date startDate, Date endDate);

	MNATopDealMakerTotalDTO getTopDealMakerTotal(String country, String industry,
			String currency, Date startDate, Date endDate);

}
