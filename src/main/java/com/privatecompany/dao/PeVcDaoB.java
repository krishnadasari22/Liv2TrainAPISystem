package com.privatecompany.dao;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.ChartFundingDetailDTO;
import com.pcompany.dto.ChartIndustryFundingDetailDTO;
import com.pcompany.dto.TopFundedCompaniesDTO;

public interface PeVcDaoB {

	List<ChartFundingDetailDTO> getPeVcSummaryChartByCountry(String countryIsoCode, String currency, Date startDate,
			Date endDate);

	List<ChartIndustryFundingDetailDTO> getPeVcSummaryChartByIndustry(String countryIsoCode, String currency,
			Integer sectorCode, Date startDate, Date endDate);

	List<TopFundedCompaniesDTO> getPeVcTopFundedCompanies(String countryIsoCode, String currency, Date startDate,
			Date endDate, int limit);

	List<ChartFundingDetailDTO> getPeVcSummaryChartBySector(String countryIsoCode, String currency, Date startDate,
			Date endDate);

	List<ChartIndustryFundingDetailDTO> getPeVcSummaryByIndustry(String countryIsoCode, String currency, Date startDate,
			Date endDate);

}
