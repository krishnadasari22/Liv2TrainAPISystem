package com.pcompany.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.pcompany.dto.PEVCFundingDTO;
import com.pcompany.dto.PEVCFundingInvestmentDTO;
import com.pcompany.util.TransactionExcelReportServiceUtil;
import com.privatecompany.dao.PEVCDaoC;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;

@Service
public class PEVCServiceC {
	
	Logger _log = Logger.getLogger(PEVCServiceC.class);
	
	@Autowired
	private PEVCDaoC pevcDao;
	
	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	@Autowired
	private SectorRepository sectorRepository;
	
	@Autowired
	private CMRepository cmRepository;

	public List<PEVCFundingDTO> getFundingDetailList(String country,
			Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId,
			Integer rowOffset, Integer rowCount, String sortingColumn, String sortingType) {
		try{
			return pevcDao.getFundingDetailList(country,startDate,endDate,industry,currency,financingType,entityId,rowOffset,rowCount,sortingColumn, sortingType);
		}catch(Exception ex){
			_log.error("failed in getFundingDetailList", ex);
			throw ex;
		}
	}
	
	public List<PEVCFundingDTO> getFundingAndInvestmentList(String country,
			Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId,
			Integer rowOffset, Integer rowCount, String sortingColumn, String sortingType) {
		try{
			List<PEVCFundingDTO> fundingDetailList = pevcDao.getFundingDetailList(country,startDate,endDate,industry,currency,financingType,entityId,rowOffset,rowCount,sortingColumn, sortingType);
			
			for (PEVCFundingDTO pevcFundingDTO : fundingDetailList) {
				if(pevcFundingDTO.getCount()>0){
					pevcFundingDTO.setInvestmentList(this.getFundingInvestmentList(pevcFundingDTO.getEntityId(),startDate,endDate,currency,pevcFundingDTO.getFinancingType()));	
				}
			}
			
			return fundingDetailList;
		}catch(Exception ex){
			_log.error("failed in getFundingDetailList", ex);
			throw ex;
		}
	}
	
	public List<PEVCFundingInvestmentDTO> getFundingInvestmentList(
			String entityId, Date startDate, Date endDate, String currency, String financingType) {
		return pevcDao.getFundingInvestmentList(entityId,startDate,endDate,currency,financingType);
	}

	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result==null",key="{#root.methodName,#p0,#p1,#p2,#p3,#p4,#p5,#p6}")
	public Long getFundingDetailCount(String country, Date startDate,
			Date endDate, String industry, String currency,
			String financingType, String entityId) {
		try{
			return pevcDao.getFundingDetailCount(country,startDate,endDate,industry,currency,financingType, entityId);
		}catch(Exception ex){
			_log.error("failed in getFundingDetailCount", ex);
			throw ex;
		}
	}
	
	public HSSFWorkbook createExcelReport(String country, String industry,Date startDate, Date endDate,
			Integer rowCount, List<PEVCFundingDTO> pevcFundingList) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			String sheetname = "VCPE Funding Details";
			
			boolean addCountryCol = false;
		 	boolean addIndustryCol = false;
		    
		 	if(country==null || "world".equalsIgnoreCase(country) || "global".equalsIgnoreCase(country) || country==""){
		    	 addCountryCol = true;
			     country = "Global";
			 }else{
				 if(pevcFundingList!=null && !pevcFundingList.isEmpty()){
					country = pevcFundingList.get(0).getCountryName(); 
				 }else{
					 CountryListDTO countrylist = cmRepository.getCountry(country);
					 if(country!=null){
						 country = countrylist.getCountryName();	 
					 }
				 }			 
			 }
		    
		    if(industry==null || "all".equalsIgnoreCase(industry) || "".equals(industry)){
		    	addIndustryCol = true;
			    industry = "All";
			 }else{
				 if(pevcFundingList!=null && !pevcFundingList.isEmpty()){
						industry = pevcFundingList.get(0).getIndustryName(); 
				  }else{
					  List<TicsIndustry> industryByCode = sectorRepository.getIndustryByTicsIndustryCode(industry);
					  if(!industryByCode.isEmpty()){
						  industry = industryByCode.get(0).getTicsIndustryName();  
					  }
				  }
			 }
		    
		    if(addCountryCol && addIndustryCol){
		    	//sheetname+=" Country Industry";
		    }else if(addCountryCol){
		    	sheetname+="-"+industry;
		    }else if(addIndustryCol){
		    	sheetname+="-"+country;
		    }else{
		    	sheetname+="-"+country+"-"+industry;
		    }
			
			createAndFillSheet(workbook,ges,sheetname,country,industry,startDate,endDate,pevcFundingList,addCountryCol,addIndustryCol);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}

	}

	private void createAndFillSheet(HSSFWorkbook workbook,GenerateExcelStyle ges, String sheetName,
			String country, String industry, Date startDate, Date endDate,
			List<PEVCFundingDTO> pevcFundingList, boolean addCountryFlag, boolean addIndustryFlag) {
		_log.info("creating the pevc funding detail sheet data");
		try {
			int rowdesign = 0;
			HSSFSheet sheet = workbook.createSheet(sheetName);
			sheet.setDisplayGridlines(false);
			Row r = sheet.getRow(rowdesign);
			if(r==null) {
				r=sheet.createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			if(celldesign==null) {
				celldesign=r.createCell(1);
			}
			sheet.setColumnWidth(1, 10000);
			// create the televisory logo
			TransactionExcelReportServiceUtil.createLogo(celldesign, sheet, workbook, ges,excelDesignService,logoPath);
			//HSSFSheet sheet = workbook.getSheet(sheetName);
			
			SimpleDateFormat sdfSheet = new SimpleDateFormat("dd-MMM-yyyy");
			
			int headerDebt = 2;
			
			TransactionExcelReportServiceUtil.createSheetHeading(headerDebt,"Country",country,sheet,ges);
			headerDebt++;
			
			TransactionExcelReportServiceUtil.createSheetHeading(headerDebt,"Industry",industry,sheet,ges);
			headerDebt++;
			
			TransactionExcelReportServiceUtil.createSheetHeading(headerDebt,"From Date",sdfSheet.format(startDate),sheet,ges);
			headerDebt++;
				
			TransactionExcelReportServiceUtil.createSheetHeading(headerDebt,"To Date",sdfSheet.format(endDate),sheet,ges);
			headerDebt+=5;
			
			if(pevcFundingList==null || pevcFundingList.isEmpty()){
				_log.info("no data available");
				return;
			}

			List<String> headerList = new ArrayList<String>();
			headerList.add("Rank");	
			headerList.add("Company Name");	
			if(addCountryFlag){
				headerList.add("Country");	
			}
			if(addIndustryFlag){
				headerList.add("Industry");	
			}
			headerList.add("Latest Round");	
			headerList.add("Latest Round Date");	
			headerList.add("Financing Type");	
			headerList.add("Total Amount Raised"+getCurrencyUnit(pevcFundingList));
			
			try {
				TransactionExcelReportServiceUtil.createHeader(sheet, headerDebt, ges,  headerList);
			}catch(Exception e){
				e.printStackTrace();
			}

			Boolean isLastRecord = false;
   			int rowCount = 0;
   			for (PEVCFundingDTO pevcFundingDto : pevcFundingList) {
   				if(rowCount == pevcFundingList.size()-1){
					isLastRecord = true;
				}
				int cellIdxDebt = 1;
				
   				TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getId(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getCompanyName(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				if(addCountryFlag){
   					TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getCountryName(),sheet,ges,isLastRecord);
   	   				cellIdxDebt++;	
   				}
   				
   				if(addIndustryFlag){
   					TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getIndustryName(),sheet,ges,isLastRecord);
   	   				cellIdxDebt++;	
   				}
   				
   				TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getLatestRound(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,sdfSheet.format(pevcFundingDto.getLatestRoundDate()),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getFinancingType(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				TransactionExcelReportServiceUtil.setColumnValue(cellIdxDebt,headerDebt,pevcFundingDto.getFxTotalFundingAmount(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				headerDebt++;
   				rowCount++;
			}

			headerDebt += 6;
			
		}catch(Exception ex){
			ex.printStackTrace();
			_log.error("Some error occured in creating the sheet" + ex.getLocalizedMessage());
		}


	}
	
	private String getCurrencyUnit(List<PEVCFundingDTO> pevcFundingList) {
		String currencyUnit = "";
		if(pevcFundingList.isEmpty()){
			return currencyUnit;
		}else{
			String currency = pevcFundingList.get(0).getCurrency();
			String unit = pevcFundingList.get(0).getUnit();
			currencyUnit+=" (";
			if(currency != null){
				currencyUnit+=currency;
			}
			if(unit != null){
				currencyUnit+=" "+unit;
			}
			currencyUnit+=")";
			return currencyUnit;
		}
	}

	public List<PEVCFundingDTO> allFundingDetailAdvancedSearch(String country, Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId, String issueType, Double minAmount,
			Double maxAmount, Integer rowOffset, Integer rowCount, String sortingColumn, String sortingType) {
		try{
			return pevcDao.allFundingDetailAdvancedSearch(country,startDate,endDate,industry,currency,financingType,entityId,issueType,minAmount,maxAmount,rowOffset,rowCount,sortingColumn, sortingType);
		}catch(Exception ex){
			_log.error("failed in getFundingDetailList", ex);
			throw ex;
		}
	}
	/*@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result==null",key="{#root.methodName,#p0,#p1,#p2,#p3,#p4,#p5,#p6}")*/
	public Long getFundingAdvancedSearchDetailCount(String country, Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId, String issueType, Double minAmount,
			Double maxAmount) {
		return pevcDao.getFundingAdvancedSearchDetailCount(country,startDate,endDate,industry,currency,financingType, entityId,issueType,minAmount,maxAmount);
	}

	public HSSFWorkbook createAdvanceSearchExcelReport(String country,
			String industry, Date startDate, Date endDate, Integer rowCount,
			List<PEVCFundingDTO> pevcFundingList) {

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			String sheetname = "VCPE Funding Details";
			
			boolean addCountryCol = false;
		 	boolean addIndustryCol = false;
		    
		 	if(country==null || "world".equalsIgnoreCase(country) || "global".equalsIgnoreCase(country) || "".equals(country) || "null".equals(country)){
		    	 addCountryCol = true;
			     country = "Global";
			 }else{
				 List<CountryListDTO> countrylist = cmRepository.getCountryList(Arrays.asList(country.split(",")));
				 if(country!=null){
					 country = countrylist.stream().map(CountryListDTO::getCountryName).collect(Collectors.joining(","));	 
				 }			 
			 }
		    
		    if(industry==null || "all".equalsIgnoreCase(industry) || "".equals(industry) || "null".equals(industry)){
		    	addIndustryCol = true;
			    industry = "All";
			 }else{
				 List<TicsIndustry> industryByCode = sectorRepository.getIndustryByIdList(Arrays.asList(industry.split(",")));
				  if(!industryByCode.isEmpty()){
					  industry = industryByCode.stream().map(TicsIndustry::getTicsIndustryName).collect(Collectors.joining(","));  
				  }
			 }
		    
		    if(addCountryCol && addIndustryCol){
		    	//sheetname+=" Country Industry";
		    }else if(addCountryCol){
		    	sheetname+="-"+industry;
		    }else if(addIndustryCol){
		    	sheetname+="-"+country;
		    }else{
		    	sheetname+="-"+country+"-"+industry;
		    }
			
			createAndFillSheet(workbook,ges,sheetname,country,industry,startDate,endDate,pevcFundingList,addCountryCol,addIndustryCol);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}


}
