package com.televisory.capitalmarket.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.televisory.capitalmarket.dto.StockPriceDTO;

@Component
public class DateUtil {

	private static final Logger _log = LoggerFactory.getLogger(DateUtil.class);
	public long daysBetween(Date startDate,Date endDate){
		LocalDate startLocalDate=startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate=endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
	}
	
	public String prevYear(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(sdf.format(date));
		return localDate.minusYears(1).toString();
	}
	
	
	public Date prevDateInYears(Date date,Integer years){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(sdf.format(date));
		
		return Date.from(localDate.minusYears(years).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public Date prevDateInDays(Date date,Integer days){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(sdf.format(date));
		
		return Date.from(localDate.minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public Date nextDate(Date startDate,long noOfDays){	
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		cal.add(Calendar.DATE, (int)noOfDays);
		
		return cal.getTime();		
	}

	public Date getNearestFriday(List<StockPriceDTO> dataList) {

		Date nearestFriday = null;
		Date CurrentDate = null;
		
		for (int i = 0; i <= 7; i++) {
			
			CurrentDate = dataList.get(dataList.size()-1 - i).getDate();
			
			if (CurrentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek().getValue() == 5) {
				nearestFriday = CurrentDate;
				return nearestFriday;
			}
		}
		return nearestFriday;
	}	
	
	/*	public List<String> generatePrevApplicablePeriodQtr(String applicableQtr , Integer totalQtrToGenerate){
		List<String> applicableQtrList = new ArrayList<>();
		try{
			if(totalQtrToGenerate==null){
				totalQtrToGenerate = 5;
			}
			applicableQtrList.add(applicableQtr);
			LocalDate localDate = LocalDate.parse(applicableQtr);
			long x =3;
			for(int index=1;index<totalQtrToGenerate;index++){
				applicableQtrList.add(localDate.minusMonths(x).toString());				
				x=x+3;
			}
			return applicableQtrList;
		}
		catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	} */

	public List<String> generatePrevApplicablePeriodQtr(String applicableQtrDate , Integer totalQtrToGenerate){
		List<String> generatedApplicablePeriodList = new ArrayList<>();
		try{
			if(totalQtrToGenerate==null){
				totalQtrToGenerate = 5;
			}
			String dateFormat = "yyyy-MM-dd";
			generatedApplicablePeriodList.add(applicableQtrDate);
			org.joda.time.LocalDate localDate = org.joda.time.LocalDate.parse(applicableQtrDate);
			int x =3;
			for(int index=1;index<totalQtrToGenerate;index++){
				generatedApplicablePeriodList.add(localDate.minusMonths(x).dayOfMonth().withMaximumValue().toString(dateFormat)); 
				x=x+3;
			}
			return generatedApplicablePeriodList;
		}
		catch(Exception e){
			_log.error(e.getMessage(),e); 
		}
		return null;
	} 


	/*public List<String> generatePrevApplicablePeriodYear(String applicableYear , Integer totalYrToGenerate){
		List<String> applicableYearList = new ArrayList<>();
		try{
			if(totalYrToGenerate==null){
				totalYrToGenerate = 4;
			}
			applicableYearList.add(applicableYear);
			LocalDate localDate = LocalDate.parse(applicableYear);
			int x =12;
			for(int index=1;index<totalYrToGenerate;index++){
				applicableYearList.add(localDate.minusMonths(x).toString());
				x=x+12;
			}
			return applicableYearList;
		}
		catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	} */
	
	public List<String> generatePrevApplicablePeriodYear(String applicableYear , Integer totalYrToGenerate){
		List<String> generatedApplicablePeriodList = new ArrayList<>();
		try{
			if(totalYrToGenerate==null){
				totalYrToGenerate = 4;
			}
			String dateFormat = "yyyy-MM-dd";
			generatedApplicablePeriodList.add(applicableYear);
			org.joda.time.LocalDate localDate = org.joda.time.LocalDate.parse(applicableYear);
			int x =12;
			for(int index=1;index<totalYrToGenerate;index++){
				generatedApplicablePeriodList.add(localDate.minusMonths(x).dayOfMonth().withMaximumValue().toString(dateFormat)); 
				x=x+12;
			}
			return generatedApplicablePeriodList;
		}
		catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	} 
	
	public HashMap<String, String> startAndEndOfYearOfAPeriod(Date period){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, String> periodMap = new HashMap<>();
		if(period!=null && period instanceof Date){
			String tempPeriod = sdf.format(period);
			String [] periodArray = tempPeriod.split("-");
			String startDate = periodArray[0]+"-01-01";
			String endDate = periodArray[0]+"-12-31";
			periodMap.put(CMStatic.STARTDATE, startDate);
			periodMap.put(CMStatic.ENDDATE, endDate);
			return periodMap;
		}
		return null;
	}
	
	/**
	 * Get the closest date 
	 * @param priceDates
	 * @param beforeDate
	 * @return
	 * @throws Exception 
	 */
	public Date getClosestYearDate(SortedMap<Date,Double> priceDates,Date beforeDate) throws Exception{
		
		if(priceDates.firstKey().after(beforeDate)){
			
			_log.info("exception thrown");
			throw new Exception("beta cannot be calculated for this period");
		}
		
		//_log.info("called for "+beforeDate+"---------------"+priceDates.get(beforeDate));
		
		if(priceDates.containsKey(beforeDate)){
			return beforeDate;
		}
		
		Date prevDate=prevDateInDays(beforeDate,1);
		
		return getClosestYearDate(priceDates,prevDate);
		
	}
	
	/**
	 * Generate previous date
	 * @param applicableYear
	 * @param years
	 * @return
	 */
	public String generatePreviousDate(String applicableYear , Integer years){
		try{
			String dateFormat = "yyyy-MM-dd";
			org.joda.time.LocalDate localDate = org.joda.time.LocalDate.parse(applicableYear);
			return localDate.minusYears(years).dayOfMonth().withMaximumValue().toString(dateFormat);
		}
		catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return null;
	}
}
