package com.televisory.capitalmarket.service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.entities.cm.IndustryMetaData;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.entities.cm.TicsSector;
import com.televisory.capitalmarket.entities.industry.CountryList;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;

@Service
public class SectorService {
	Logger _log = Logger.getLogger(SectorService.class);

	@Autowired
	SectorRepository industryRepository;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Autowired
	DateUtil dateUtil;

	public List<CountryList> findAllIndustryCountries(String ticsSectorCodeList, String ticsIndustryCodeList, String periodType, Date startDate, Date endDate, List<String> userCountyList) {
		return industryRepository.findAllIndustryCountries(ticsSectorCodeList, ticsIndustryCodeList, periodType, startDate, endDate,userCountyList);
	}
	
	@Cacheable(cacheNames="CM_DAYS_CACHE",unless="#result.size()==0")
	public List<IndustryFinancialDataDTO> getIDSectorData(String periodType, String ticsSectorCode, List<String> userCountyList, String params, Date startDate, Date endDate, String currencyCode, Integer month){
		List<IndustryFinancialDataDTO> industryFinancialDataDTO =null;
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			if(month==null) {
				if(endDate!=null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					month = cal.get(Calendar.MONTH)+1;
					
				}
				
               else if(startDate!=null) {
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					month = cal.get(Calendar.MONTH)+1;
				}
			}
			industryFinancialDataDTO = industryRepository.getIDSectorDataYearly(ticsSectorCode, userCountyList, params, startDate, endDate, currencyCode, month);
		}
		else {
			industryFinancialDataDTO = industryRepository.getIDSectorDataQuarterly(ticsSectorCode, userCountyList, params, startDate, endDate, currencyCode);
		}
		industryFinancialDataDTO.sort(Comparator.comparing(IndustryFinancialDataDTO::getPeriod));
		return industryFinancialDataDTO;
	}
	
	@Cacheable(cacheNames="CM_DAYS_CACHE",unless="#result.size()==0")
	public List<IndustryFinancialDataDTO> getIDIndustryData(String periodType, String ticsSectorCode, String ticsIndustryCode, String countryId, String params, Date startDate, Date endDate, String currencyCode, Integer month) {
		
		List<IndustryFinancialDataDTO> industryFinancialDataDTO =null;
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			if(month==null) {
				if(endDate!=null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					month = cal.get(Calendar.MONTH)+1;
					
				}
				
                else if(startDate!=null) {
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					month = cal.get(Calendar.MONTH)+1;
				}
			}
			industryFinancialDataDTO = industryRepository.getIDIndustryDataYearly(ticsSectorCode, ticsIndustryCode, countryId, params, startDate, endDate, currencyCode, month);
		}
		else {
			industryFinancialDataDTO = industryRepository.getIDIndustryDataQuarterly(ticsSectorCode, ticsIndustryCode, countryId, params, startDate, endDate, currencyCode);
		}
		
		_log.info(industryFinancialDataDTO.size());
		
		industryFinancialDataDTO = industryFinancialDataDTO.stream().map(industryData -> {
			
			if(industryData.getCountryId()==null){
				
				industryData.setCountryId(0);
				industryData.setCountryName("World");
			}
			
			return industryData;
		
		}).collect(Collectors.toList());
		
		industryFinancialDataDTO.sort(Comparator.comparing(IndustryFinancialDataDTO::getPeriod));
		
		return industryFinancialDataDTO;
	
	}
	
	@Cacheable(cacheNames="CM_DAYS_CACHE",unless="#result.size()==0")
	public List<IndustryFinancialDataDTO> getIDCompanyData(String periodType, String ticsIndustryCode, String companyId, String countryId, String params, Date startDate, Date endDate, String currencyCode, Integer month, Integer companyFlag) {
		_log.info(" periodType "+periodType+" ticsIndustryCode "+ticsIndustryCode+" companyId "+companyId+" countryId "+countryId+" params "+params+" startDate "+startDate+" endDate "+endDate +" currencyCode: "+ currencyCode +" companyFlag: "+ companyFlag);
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
			if(month==null) {
				if(endDate!=null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					month = cal.get(Calendar.MONTH)+1;
					
				}
				else if(startDate!=null) {
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					month = cal.get(Calendar.MONTH)+1;
				}
			}
			return industryRepository.getIDCompanyDataYearly(ticsIndustryCode, companyId, countryId, params, startDate, endDate, currencyCode, month, companyFlag);
		}
		else {
			return industryRepository.getIDCompanyDataQuarterly(ticsIndustryCode, companyId, countryId, params, startDate, endDate, currencyCode, companyFlag);
		}
	}
	
	public List<TicsSector> getAllSectors(){
		return industryRepository.getAllSectors();
	}
	
		
	public List<TicsIndustry> getTicsIndustryByTicsSectorCode(String ticsSectorCode){
		return industryRepository.getTicsIndustryByTicsSectorCode(ticsSectorCode);
	}
	
	
	public List<TicsIndustry> getFactsetIndustry(String ticsIndustryCode) {
		return industryRepository.getIndustryByTicsIndustryCode(ticsIndustryCode);
	}
	
	/*public List<IndustryFinancialDataDTO> getIndustryCountryByField(String fieldName,String periodType,Date startDate, Date endDate,String ticsSectorCode,String ticsIndustryCode,String countryCode, Integer recCount){
		return industryRepository.getIndustryCountryByField(fieldName,periodType,startDate,endDate,ticsSectorCode,ticsIndustryCode,countryCode, recCount);
	}*/

	public List<TicsIndustry> getTicsIndustryByTicsSectorCode() {
		return industryRepository.getTicsIndustryByTicsSectorCode();
	}

	public List<TicsIndustry> getTicsIndustryByFactsetIndustry(String factsetIndustry) {
		return industryRepository.getTicsIndustryByFactsetIndustry(factsetIndustry);
	}

	public List<TicsIndustry> getTicsIndustryByTicsSectorCode(String ticsSectorCode, String factsetIndustry) {
		return industryRepository.getTicsIndustryByTicsSectorCode(ticsSectorCode,factsetIndustry);
	}

	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType) {
		return industryRepository.getIndustryFinancialParameter(industryType);
	}
	
	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, String type,Integer watchlistFlag) {
		
		if(type==null){
			return industryRepository.getIndustryFinancialParameter(industryType, watchlistFlag);
		}else{
			return industryRepository.getIndustryFinancialParameter(industryType,type, watchlistFlag);
		}
		
	}
	
	/*public List<IndustryFinancialDataDTO> getIndustryCompanyData(String fieldName, String periodType, Date startDate, Date endDate, String ticsSectorCode, String ticsIndustryCode, String countryCode) {
		return industryRepository.getIndustryCompanyData(fieldName, periodType, startDate, endDate, ticsSectorCode, ticsIndustryCode, countryCode);
	}*/
	
	public List<TicsIndustry> getIndustryByIdList(List<String> customIndustryIds) {
		return industryRepository.getIndustryByIdList(customIndustryIds);
	}
	
	public String getCountryDomocileCode(String countryId) {
		return industryRepository.getCountryDomocileCode(countryId);
	}
	
	public List<IndustryMetaData>getPeriodByTicsIndustryNCountryCode(String ticsIndustryCode, String countryCode,String ticsSectorCode) {
		return industryRepository.getPeriodByTicsIndustryNCountryCode(ticsIndustryCode, countryCode,ticsSectorCode);
	}

	public List<BalanceModelDTO> getIndustryFinancialParameter(String industryType, String financialType) {
		
		if(financialType==null){
			return industryRepository.getIndustryFinancialParameter(industryType);
		}else{
			return industryRepository.getIndustryFinancialParameter(industryType, financialType);
		}
	}

	public List<TicsIndustry> getTicsIndustryByCountryCode(String countryList) {
		return industryRepository.getTicsIndustryByCountryCode(countryList);
	}

	public List<TicsIndustry> getTicsIndustryBySearchParam(String searchParam,Integer resultCount) {
		return industryRepository.getTicsIndustryBySearchParam(searchParam,resultCount);
	}

	public CompanyDTO getDefaultCountryForIndustry(List<String> countryCode) {
		return industryRepository.getDefaultCountryForIndustry(countryCode);
	}

	public List<BalanceModelDTO> getIndustryFinancialParameterForIC(String industryType, Integer icFlag) {
		return industryRepository.getIndustryFinancialParameterForIC(industryType, icFlag);
	}

	public List<TicsSector> getSectorBySectorCode(String ticsSectorCode) {
		return industryRepository.getSectorBySectorCode(ticsSectorCode);
	}

	public List<TicsIndustry> getTicsIndustryByTicsIndustryCode(String ticsIndustryCode) {
		 return industryRepository.getTicsIndustryByTicsIndustryCode(ticsIndustryCode);
	}
}
