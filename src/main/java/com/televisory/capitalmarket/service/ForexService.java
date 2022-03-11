package com.televisory.capitalmarket.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.ForexRepository;
import com.televisory.capitalmarket.factset.dto.ForexRateDto;

@Service
public class ForexService {
	
	private static final Logger _log = Logger.getLogger(ForexService.class);
	
	@Autowired
	private ForexRepository forexDao;
	
	public List<ForexRateDto> getFxMonthly(String date) throws Exception {
		try {
			List<ForexRateDto> forexDtos = forexDao.getForexMonthly(date);
			return forexDtos;
		} catch (Exception e) {
			_log.error("Some error occured in getting the data::" + e.getMessage());
			throw new Exception();
		}
	}
}
