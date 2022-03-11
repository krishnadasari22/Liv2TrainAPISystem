package com.pcompany.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.pcompany.dao.MNADaoB;
import com.pcompany.dto.MNATopDealMakerDTO;
import com.pcompany.dto.MNATopDealMakerTotalDTO;
import com.pcompany.model.MNATopDealMakerResponse;
import com.televisory.capitalmarket.dao.CMRepository;
import com.televisory.capitalmarket.dao.SectorRepository;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.entities.cm.TicsIndustry;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class MNAServiceB {
	
	Logger _log = Logger.getLogger(MNAServiceB.class);
	
	@Autowired
	MNADaoB mnaDao; 
	
	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	@Autowired
	private SectorRepository sectorRepository;
	
	@Autowired
	private CMRepository cmRepository;


	public MNATopDealMakerResponse getTopDealMaker(String country, String industry,
			String currency, Date startDate, Date endDate,
			Integer rowOffset, Integer rowCount, String sortingColumn,
			String sortingType) {
		//_log.info("getting top deal maker list for country:"+country+" industry:"+industry+" periodStart:"+periodStart+" periodEnd:"+periodEnd+" currency:"+currency+" indexStart:"+indexStart+" indexEnd:"+indexEnd+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
		try{
			MNATopDealMakerResponse response = new MNATopDealMakerResponse();
			List<MNATopDealMakerDTO> topDealMakerList = mnaDao.getTopDealMakerList(country,industry,currency,startDate,endDate,rowOffset,rowCount,sortingColumn,sortingType);
			
			int totalDeals = 0;
			double totalValue = 0;
			double avgValue = 0;
			double maxValue = 0;
			
			for (MNATopDealMakerDTO mnaTopDealMaker : topDealMakerList) {
				totalDeals += mnaTopDealMaker.getTotalDeals()==null?0:mnaTopDealMaker.getTotalDeals();
				totalValue += mnaTopDealMaker.getTotalValue()==null?0:mnaTopDealMaker.getTotalValue();
				avgValue += mnaTopDealMaker.getAvgValue()==null?0:mnaTopDealMaker.getAvgValue();
				maxValue += mnaTopDealMaker.getMaxValue()==null?0:mnaTopDealMaker.getMaxValue();
			}
			if(!topDealMakerList.isEmpty()){
				response.setCurrency(topDealMakerList.get(0).getCurrency());
				response.setUnit(topDealMakerList.get(0).getUnit());		
			}
			response.setTotalDeals(totalDeals);
			response.setTotalValue(totalValue);
			response.setAvgValue(avgValue/topDealMakerList.size());
			response.setMaxValue(maxValue);
			
			response.setTopDealList(topDealMakerList);
			return response;	
		}catch(Exception ex){
			_log.error("failed in getTopDealMaker", ex);
			throw ex;
		}
	}

	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result==null",key="{#root.methodName,#p0,#p1,#p2,#p3}")
	public Long getTopDealMakerCount(String country, String industry,
			String currency, Date startDate, Date endDate) {
		try{
			return mnaDao.getTopDealMakerCount(country, industry, currency, startDate, endDate);
		}catch(Exception ex){
			_log.error("failed in getTopDealMakerCount", ex);
			throw ex;
		}
	}

	@Cacheable(cacheNames = "CM_DAYS_CACHE",unless="#result==null",key="{#root.methodName,#p0,#p1,#p2,#p3}")
	public MNATopDealMakerTotalDTO getTopDealMakerTotal(String country,
			String industry, String currency, Date startDate,
			Date endDate) {
		try{
			return mnaDao.getTopDealMakerTotal(country, industry, currency, startDate, endDate);
		}catch(Exception ex){
			_log.error("failed in getTopDealMakerTotal", ex);
			throw ex;
		}
	}

	public List<MNATopDealMakerDTO> getTopDealMakerList(String country,
			String industry, String currency, Date startDate, Date endDate,
			int rowOffset, int rowCount, String sortingColumn, String sortingType) {
		try{
			return mnaDao.getTopDealMakerList(country,industry,currency,startDate,endDate,rowOffset,rowCount,sortingColumn,sortingType);
		}catch(Exception ex){
			_log.error("failed in getTopDealMakerList", ex);
			throw ex;
		}
	}

	public HSSFWorkbook createExcelReport(String country, String industry,Date startDate, Date endDate,
			Integer rowCount, List<MNATopDealMakerDTO> topDealMakerList) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			String sheetname = "Top Deal Makers";
			//String sheetname = "M&A Top-100 Country";
			//String sheetname = "M&A Top-100 Industry";
			
			boolean addCountryCol = false;
		 	boolean addIndustryCol = false;
		    
		 	if(country==null || "World".equals(country) || country==""){
		    	 addCountryCol = true;
			     country = "World";
			 }else{
				 if(topDealMakerList!=null && !topDealMakerList.isEmpty()){
					country = topDealMakerList.get(0).getCountry(); 
				 }else{
					 CountryListDTO countrylist = cmRepository.getCountry(country);
					 if(country!=null){
						 country = countrylist.getCountryName();	 
					 }
				 }			 
			 }
		    
		    if(industry==null || "All".equals(industry) || "".equals(industry)){
		    	addIndustryCol = true;
			    industry = "All";
			 }else{
				 if(topDealMakerList!=null && !topDealMakerList.isEmpty()){
						industry = topDealMakerList.get(0).getIndustryName(); 
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
			
			createAndFillSheet(workbook,ges,sheetname,country,industry,startDate,endDate,topDealMakerList,addCountryCol,addIndustryCol);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}

	}

	private void createAndFillSheet(HSSFWorkbook workbook,GenerateExcelStyle ges, String sheetName,
			String country, String industry, Date startDate, Date endDate,
			List<MNATopDealMakerDTO> topDealMakerList, boolean addCountryFlag, boolean addIndustryFlag) {
		_log.info("creating the Share Holding sheet data ");
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
			createLogo(celldesign, sheet, workbook, ges);
			//HSSFSheet sheet = workbook.getSheet(sheetName);
			
			SimpleDateFormat sdfSheet = new SimpleDateFormat("dd-MMM-yyyy");
			
			int headerDebt = 2;
			
			createSheetHeading(headerDebt,"Country",country,sheet,ges);
			headerDebt++;
			
			createSheetHeading(headerDebt,"Industry",industry,sheet,ges);
			headerDebt++;
			
			createSheetHeading(headerDebt,"From Date",sdfSheet.format(startDate),sheet,ges);
			headerDebt++;
				
			createSheetHeading(headerDebt,"To Date",sdfSheet.format(endDate),sheet,ges);
			headerDebt+=5;
			
			if(topDealMakerList==null || topDealMakerList.isEmpty()){
				_log.info("no data available");
				return;
			}

			List<String> headerList = new ArrayList<String>();
			headerList.add("Rank");	
			headerList.add("Entity");	
			if(addCountryFlag){
				headerList.add("Country");	
			}
			if(addIndustryFlag){
				headerList.add("Industry");	
			}
			headerList.add("No. of Transactions");	
			String currencyUnit = getCurrencyUnit(topDealMakerList);
			headerList.add("Total Value"+currencyUnit);	
			headerList.add("Avg. Value"+currencyUnit);	
			headerList.add("Largest Transactions"+currencyUnit);
			
			try {
				createHeader(sheet, headerDebt, ges,  headerList);
			}catch(Exception e){
				e.printStackTrace();
			}

			Boolean isLastRecord = false;
   			int rowCount = 0;
   			for (MNATopDealMakerDTO topDealDto : topDealMakerList) {
   				if(rowCount == topDealMakerList.size()-1){
					isLastRecord = true;
				}
				int cellIdxDebt = 1;
				
   				setColumnValue(cellIdxDebt,headerDebt,topDealDto.getRank(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				setColumnValue(cellIdxDebt,headerDebt,topDealDto.getName(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				if(addCountryFlag){
   					setColumnValue(cellIdxDebt,headerDebt,topDealDto.getCountry(),sheet,ges,isLastRecord);
   	   				cellIdxDebt++;	
   				}
   				
   				if(addIndustryFlag){
   					setColumnValue(cellIdxDebt,headerDebt,topDealDto.getIndustryName(),sheet,ges,isLastRecord);
   	   				cellIdxDebt++;	
   				}
   				
   				setColumnValue(cellIdxDebt,headerDebt,topDealDto.getTotalDeals(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				setColumnValue(cellIdxDebt,headerDebt,topDealDto.getTotalValue(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				setColumnValue(cellIdxDebt,headerDebt,topDealDto.getAvgValue(),sheet,ges,isLastRecord);
   				cellIdxDebt++;
   				
   				setColumnValue(cellIdxDebt,headerDebt,topDealDto.getMaxValue(),sheet,ges,isLastRecord);
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

	private void createHeader(HSSFSheet sheet, int startingHeaderRow,
			GenerateExcelStyle ges, List<String> headerList) throws IOException {
		
		int cellIndex = 1;
		System.out.println(" startingHeaderRow "+startingHeaderRow);

		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		
		if(r==null) {
			r=sheet.createRow(startingHeaderRow);
		}

		Row headerRow = sheet.getRow(row);
		if(null==headerRow) {
			headerRow=sheet.createRow(row);
		}
		
		for (String headCol : headerList) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			if(cellHeader==null) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			sheet.setColumnWidth(cellIndex, 10000);
			cellHeader.setCellValue(headCol);
			ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIndex = cellIndex+1;
		}
		
	}

	private void setColumnValue(int cellIdxDebt, int headerDebt,
			Double value, HSSFSheet sheet, GenerateExcelStyle ge,
			Boolean isLastRecord) {
		Row rowDebt = sheet.getRow(headerDebt);
		if(rowDebt==null) {
			rowDebt=sheet.createRow(headerDebt);
		}
		Cell cell = rowDebt.getCell(cellIdxDebt);
		if(cell==null) {
			cell=rowDebt.createCell(cellIdxDebt);
		}
		
		if (value != null) {
			cell.setCellValue(value);
			if (isLastRecord) {
				if (Math.abs(value) >= 1.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else if (Math.abs(value) < 10.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL);
				} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
				}

			} else {
				if (Math.abs(value) >= 1.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else if (Math.abs(value) < 10.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL);
				} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if(isLastRecord){
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}
	}
	
	private void setColumnValue(int cellIdxDebt, int headerDebt, Integer value,
			HSSFSheet sheet, GenerateExcelStyle ge, Boolean isLastRecord) {
		Row rowDebt = sheet.getRow(headerDebt);
		if(rowDebt==null) {
			rowDebt=sheet.createRow(headerDebt);
		}
		Cell cell = rowDebt.getCell(cellIdxDebt);
		if(cell==null) {
			cell=rowDebt.createCell(cellIdxDebt);
		}
		
		if (value != null) {
			cell.setCellValue(value);
			if (isLastRecord) {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			} else {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if(isLastRecord){
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}
	}

	private void setColumnValue(int cellIdxDebt, int headerDebt, String value,
			HSSFSheet sheet, GenerateExcelStyle ges, Boolean isLastRecord) {
		Row rowDebt = sheet.getRow(headerDebt);
		if(rowDebt==null) {
			rowDebt=sheet.createRow(headerDebt);
		}
		Cell cell = rowDebt.getCell(cellIdxDebt);
		if(cell==null) {
			cell=rowDebt.createCell(cellIdxDebt);
		}
		if (value != null) {
			cell.setCellValue(value);
			if(isLastRecord){
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
			}else{
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if(isLastRecord){
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}
		
	}
	
	private void createSheetHeading(int headerDebt, String name,String value,HSSFSheet sheet, GenerateExcelStyle ges) {
		Row companyDesc = sheet.getRow(headerDebt);
		if(null==companyDesc) {
			companyDesc=sheet.createRow(headerDebt);
		}
		Cell compCell = companyDesc.getCell(1);
		if(compCell==null) {
			compCell=companyDesc.createCell(1);
		}
		compCell.setCellValue(name);
		Cell companyCellData = companyDesc.getCell(2);
		if(companyCellData==null) {
			companyCellData=companyDesc.createCell(2);
		}
		companyCellData.setCellValue(value);
		companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
	}
	
	

	private String getCurrencyUnit(List<MNATopDealMakerDTO> topDealMakerList) {
		String currencyUnit = "";
		if(topDealMakerList.isEmpty()){
			return currencyUnit;
		}else{
			String currency = topDealMakerList.get(0).getCurrency();
			String unit = topDealMakerList.get(0).getUnit();
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

	private void createLogo(Cell cell, HSSFSheet sheet,
			HSSFWorkbook workbook, GenerateExcelStyle ges) {
		short height = 800;
		cell.getRow().setHeight(height);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER);
		// SET LOGO IMAGE
		try {
			excelDesignService.readWorkBook(workbook);
			excelDesignService.setImage(sheet, cell.getRowIndex(), cell.getColumnIndex(), logoPath, 600, 600);
		} catch (IOException ex) {
			_log.error("failed in createLogo",ex);
		}
	}

}
