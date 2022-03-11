package com.televisory.capitalmarket.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.dao.ForexRepository;
import com.televisory.capitalmarket.entities.factset.ForexRate;
import com.televisory.capitalmarket.factset.dto.ForexRateDto;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class ForexRepositoryImpl implements ForexRepository {

	private static final Logger _log = Logger.getLogger(ForexRepositoryImpl.class);
	
	@Autowired
	@Qualifier(value = "factSetSessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Override
	public List<ForexRateDto> getForexMonthly(String startDate) throws Exception {

		try {
			_log.info("getting the monthly fx from the factset");
			List<ForexRateDto> forexRateDtos = null;
			
			String baseQuery ="select avg(exch_rate_usd) as exch_rate_usd,fx.iso_currency,LAST_DAY(date) as date , avg(exch_rate_per_usd) as exch_rate_per_usd from ref_v2_fx_rates_usd fx inner join factset.ref_v2_iso_currency_map cm on cm.iso_currency=fx.iso_currency where active='1'  group by MONTH(date),YEAR(date),fx.iso_currency having date>? and count(*)>=25  order by date";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createSQLQuery(baseQuery).addEntity(ForexRate.class);
			query.setParameter(0, startDate);
			
			List<ForexRate> forexRate = query.list();
			
			forexRateDtos = DozerHelper.map(dozerBeanMapper, forexRate, ForexRateDto.class);
			
			return forexRateDtos;
	
		} catch (Exception e) {
			_log.error("Some error occured in getting the fx data from factset::" + e.getMessage());
			throw new Exception();
		}
	}
}
