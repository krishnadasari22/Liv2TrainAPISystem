package com.televisory.capitalmarket.dao;

import java.util.List;

import com.televisory.capitalmarket.factset.dto.ForexRateDto;

public interface ForexRepository {

	List<ForexRateDto> getForexMonthly(String date) throws Exception;

}
