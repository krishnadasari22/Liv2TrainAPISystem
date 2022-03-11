package com.privatecompany.dao.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dto.ChartFundingDetailDTO;
import com.pcompany.dto.ChartIndustryFundingDetailDTO;
import com.pcompany.dto.TopFundedCompaniesDTO;
import com.pcompany.entity.ChartFundingDetail;
import com.pcompany.entity.ChartIndustryFundingDetail;
import com.pcompany.entity.TopFundedCompanies;
import com.privatecompany.dao.PeVcDaoB;
import com.televisory.capitalmarket.util.DozerHelper;
import com.televisory.utils.PEVCFundingDetailQueries;

@Repository
@Transactional
public class PeVcDaoImplB implements PeVcDaoB {

	Logger _log = Logger.getLogger(PeVcDaoImplB.class);

	@Autowired
	@Qualifier(value = "factSetSessionFactory")
	private SessionFactory factSetSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<ChartFundingDetailDTO> getPeVcSummaryChartByCountry(String countryIsoCode, String currency,
			Date startDate, Date endDate) {

		String baseQuery;

		baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsByCountryGlobalQuery;

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
				.addEntity(ChartFundingDetail.class);

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currency", currency);

		return DozerHelper.map(dozerBeanMapper, query.list(), ChartFundingDetailDTO.class);

	}

	@Override
	public List<ChartFundingDetailDTO> getPeVcSummaryChartBySector(String countryIsoCode, String currency,
			Date startDate, Date endDate) {

		String baseQuery;
		Query query;

		if (countryIsoCode.equals("Global")) {

			baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsBySectorGlobalQuery;

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(ChartFundingDetail.class);

			// query.setParameter("period", startDate);

		} else {

			baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsBySectorCountryQuery;

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(ChartFundingDetail.class);

			query.setParameter("countryIsoCode", countryIsoCode);
			// query.setParameter("period", period);
		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currency", currency);

		return DozerHelper.map(dozerBeanMapper, query.list(), ChartFundingDetailDTO.class);
	}

	@Override
	public List<ChartIndustryFundingDetailDTO> getPeVcSummaryChartByIndustry(String countryIsoCode, String currency,
			Integer sectorCode, Date startDate, Date endDate) {

		String baseQuery;
		Query query;

		if (countryIsoCode.equals("Global")) {

			baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsBySectorIndustryGlobalQuery;

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(ChartIndustryFundingDetail.class);

			query.setParameter("sectorCode", sectorCode);
			// query.setParameter("period", period);

		} else {

			baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsBySectorIndustryCountryQuery;

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(ChartIndustryFundingDetail.class);

			query.setParameter("countryIsoCode", countryIsoCode);
			query.setParameter("sectorCode", sectorCode);
			// query.setParameter("period", period);

		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currency", currency);

		return DozerHelper.map(dozerBeanMapper, query.list(), ChartIndustryFundingDetailDTO.class);
	}

	@Override
	public List<TopFundedCompaniesDTO> getPeVcTopFundedCompanies(String countryIsoCode, String currency, Date startDate,
			Date endDate, int limit) {

		String baseQuery;
		Query query;

		if (countryIsoCode.equals("Global")) {

			baseQuery = PEVCFundingDetailQueries.pevcTopFundedCompaniesBasedOnGlobalQuery + limit;

			_log.info(baseQuery);

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(TopFundedCompanies.class);

		} else {

			baseQuery = PEVCFundingDetailQueries.pevcTopFundedCompaniesBasedOnCountryQuery + limit;

			_log.info(baseQuery);

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(TopFundedCompanies.class);

			query.setParameter("countryIsoCode", countryIsoCode);
		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currency", currency);

		return DozerHelper.map(dozerBeanMapper, query.list(), TopFundedCompaniesDTO.class);

	}

	@Override
	public List<ChartIndustryFundingDetailDTO> getPeVcSummaryByIndustry(String countryIsoCode, String currency,
			Date startDate, Date endDate) {

		String baseQuery;
		Query query;

		if (countryIsoCode.equals("Global")) {

			baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsByAllSectorIndustryGlobalQuery;

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(ChartIndustryFundingDetail.class);

		} else {

			baseQuery = PEVCFundingDetailQueries.pevcFundingDetailsByAllSectorIndustryCountryQuery;

			query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery)
					.addEntity(ChartIndustryFundingDetail.class);

			query.setParameter("countryIsoCode", countryIsoCode);

		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("currency", currency);

		return DozerHelper.map(dozerBeanMapper, query.list(), ChartIndustryFundingDetailDTO.class);
	}

}
