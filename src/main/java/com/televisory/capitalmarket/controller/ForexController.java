package com.televisory.capitalmarket.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.factset.dto.ForexRateDto;
import com.televisory.capitalmarket.service.ForexService;

@Controller
@RequestMapping(value = "forex")
public class ForexController {
	
	Logger _log = Logger.getLogger(ForexController.class);
	
	@Autowired
	private ForexService forexService;

	@RequestMapping(value="getForexMonthly",method=RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<ForexRateDto>> getFxMonthly(@RequestParam(value="date") String date) {
		List<ForexRateDto> forexRateDtos = null;
		try {
			
			forexRateDtos = forexService.getFxMonthly(date);
			return new ResponseEntity<List<ForexRateDto>>(forexRateDtos, HttpStatus.OK);
		
		}catch (Exception e) {
			_log.error("Some Error Ocurred in getting the data from factset"+e.getMessage().toString());
			if(e.getLocalizedMessage().contains("404")) {
				return new ResponseEntity<List<ForexRateDto>>(forexRateDtos, HttpStatus.NOT_FOUND);
			}
			if(e.getLocalizedMessage().contains("204")) {
				return new ResponseEntity<List<ForexRateDto>>(forexRateDtos, HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<ForexRateDto>>(forexRateDtos, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}

}
