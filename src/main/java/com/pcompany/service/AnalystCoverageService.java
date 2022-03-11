package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pcompany.dao.AnalystCoverageDao;
import com.pcompany.daoimpl.AnalystCoverageDaoImpl;
import com.pcompany.dto.AnalystCoverageChartDTO;
import com.pcompany.dto.AnalystCoverageDTO;
import com.pcompany.dto.AnalystGuidanceDeviationDTO;
import com.pcompany.dto.DebtCapitalStructureDetailsDTO;
import com.pcompany.dto.OwnershipManagementInfoDTO;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class AnalystCoverageService {
	
	Logger _log = Logger.getLogger(AnalystCoverageService.class);

	@Autowired
	AnalystCoverageDao analystCoverageDao;
	
	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	public List<AnalystCoverageChartDTO> getTotalRecommendation(String companyId, String recType) {
		return analystCoverageDao.getTotalRecommendation(companyId, recType);
	}
	public List<AnalystCoverageDTO> getRecommendation(String companyId, String recType) {
		return analystCoverageDao.getRecommendation(companyId, recType);
	}
	
	public List<AnalystCoverageDTO> getBasicConsensus(String companyId, String periodicity, Boolean isDownload) {
		if(periodicity.equalsIgnoreCase("yearly")) {
			return analystCoverageDao.getBasicConsensusYearly(companyId, isDownload);
		}else {
			return analystCoverageDao.getBasicConsensusQuarterly(companyId, isDownload);
		}
	}
	
	public List<AnalystCoverageDTO> getAdvancedConsensus(String companyId, String periodicity) {
		if(periodicity.equalsIgnoreCase("yearly")) {
			return analystCoverageDao.getAdvancedConsensusYearly(companyId);
		}else {
			return analystCoverageDao.getAdvancedConsensusQuarterly(companyId);
		}
	}
	
	public List<AnalystCoverageDTO> getBasicManagementGuidance(String companyId, String periodicity) {
		if(periodicity.equalsIgnoreCase("yearly")) {
			return analystCoverageDao.getBasicManagementGuidanceYearly(companyId);
		}else {
			return analystCoverageDao.getBasicManagementGuidanceQuarterly(companyId);
		}
	}
	
	public List<AnalystCoverageDTO> getAdvancedManagementGuidance(String companyId, String periodicity) {
		if(periodicity.equalsIgnoreCase("yearly")) {
			return analystCoverageDao.getAdvancedManagementGuidanceYearly(companyId);
		}else {
			return analystCoverageDao.getAdvancedManagementGuidanceQuarterly(companyId);
		}
	}
	
	public List<AnalystCoverageDTO> getConsensusOperation(String companyId, String periodicity) {
		if(periodicity.equalsIgnoreCase("yearly")) {
			return analystCoverageDao.getConsensusOperationYearly(companyId);
		}else {
			return analystCoverageDao.getConsensusOperationQuarterly(companyId);
		}
	}
	public List<AnalystGuidanceDeviationDTO> getGuidanceDeviation(String companyId, String periodicity) {
		if(periodicity.equalsIgnoreCase("yearly")) {
			return analystCoverageDao.getGuidanceDeviationYearly(companyId);
		}else {
			return analystCoverageDao.getGuidanceDeviationQuarterly(companyId);
		}
	}
	public List<AnalystCoverageChartDTO> getMovementRating(String companyId, String type) {
			return analystCoverageDao.getMovementRating(companyId,  type);
	}

	public HSSFWorkbook createExcelReport(List<AnalystCoverageDTO> recData,String companyId, String companyName) throws Exception{
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet("Analyst Recommendation");
			//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			sheet.setDisplayGridlines(false);
			createAndFillSheet(workbook,recData, "Analyst Recommendation",ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}

	private void createAndFillSheet(HSSFWorkbook workbook,List<AnalystCoverageDTO> recData, String sheetName,GenerateExcelStyle ges, String companyName) throws JsonProcessingException {
		
		_log.info("creating the financial sheet data ");
		
		try {

			int rowdesign = 0;
			
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(null==r) {
				r=workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			int headerOwner = 7;
			
			Row companyDesc = sheet.getRow(2);
			if(companyDesc==null) {
				companyDesc=sheet.createRow(2);
			}
			Cell compCell = companyDesc.getCell(1);
			if(compCell==null) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Company");
			Cell companyCellData = companyDesc.getCell(2);
			if(companyCellData==null) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			try {
				createHeader(workbook.getSheet(sheetName), headerOwner, ges,  CMStatic.ANALYST_RECOMMEDATION_HEADER);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Boolean isLastRecord = false;
			for(int i=0;i<recData.size();i++){
				AnalystCoverageDTO analystCoverage = recData.get(i);
				if(i==recData.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerOwner);
				
				if(rowDebt==null) {
					rowDebt=sheet.createRow(headerOwner);
				}
				
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,analystCoverage.getStartDate(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,analystCoverage.getBuy(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,analystCoverage.getOverWeight(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,analystCoverage.getNeutral(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,analystCoverage.getUnderweight(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,analystCoverage.getSell(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				headerOwner=headerOwner+1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}
	
	private void createLogo(Cell cell, HSSFSheet sheet, HSSFWorkbook workbook, GenerateExcelStyle ges) {
		short height = 800;
		cell.getRow().setHeight(height);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER);
		// SET LOGO IMAGE
		try {
			excelDesignService.readWorkBook(workbook);
			excelDesignService.setImage(sheet, cell.getRowIndex(), cell.getColumnIndex(), logoPath, 600, 600);
		} catch (IOException e) {

		}
	}
	
	private void createHeader(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		/*Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		Cell sectionCell = sectionDesc.getCell(1);
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);*/
		
		
		int cellIndex = 1;
		System.out.println(" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		
		if(r==null) {
			r=sheet.createRow(startingHeaderRow);
		}
		
		Row headerRow = sheet.getRow(row);
		
		if(headerRow==null) {
			headerRow=sheet.createRow(row);
		}
		
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			if(cellHeader==null) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			cellHeader.setCellValue(headCol);
			ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIndex = cellIndex+1;
		}
	}
	
	private void createHeader(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		if(null==sectionDesc) {
			sectionDesc=sheet.createRow(startingHeaderRow-3);
		}
		Cell sectionCell = sectionDesc.getCell(1);
		if(sectionCell==null) {
			sectionCell=sectionDesc.createCell(1);
		}
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		System.out.println("header "+header +" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		if(null==r) {
			r=sheet.createRow(startingHeaderRow);
		}
		
		Row headerRow = sheet.getRow(row);
		
		if(headerRow==null) {
			headerRow=sheet.createRow(row);
		}
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			if(cellHeader==null) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			if(!headCol.equalsIgnoreCase("Particulars")){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+4, true);
				cellHeader.setCellValue(headCol);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row headerMinMax =  sheet.getRow(row+1);
				if(null==headerMinMax) {
					headerMinMax=sheet.createRow(row+1);
				}
				Cell cellMinMax = headerMinMax.getCell(cellIndex);
				if(cellMinMax==null) {
					cellMinMax=headerMinMax.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, 5000);
				cellMinMax.setCellValue("Mean");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				if(cellMax==null) {
					cellMax=headerMinMax.createCell(cellIndex);
				}
				cellMax.setCellValue("Median");
				cellMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellHigh = headerMinMax.getCell(cellIndex);
				if(cellHigh==null) {
					cellHigh=headerMinMax.createCell(cellIndex);
				}
				cellHigh.setCellValue("High");
				cellHigh.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellLow = headerMinMax.getCell(cellIndex);
				if(cellLow==null) {
					cellLow=headerMinMax.createCell(cellIndex);
				}
				cellLow.setCellValue("Low");
				cellLow.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellSd= headerMinMax.getCell(cellIndex);
				if(cellSd==null) {
					cellSd=headerMinMax.createCell(cellIndex);
				}
				cellSd.setCellValue("Standard Deviation");
				cellSd.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				
			}else{
				cellHeader.setCellValue(headCol);
				ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
			}
		}
	}
	
	private void createHeaderConsensusOpr(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		Cell sectionCell = sectionDesc.getCell(1);
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		System.out.println("header "+header +" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		Row headerRow = sheet.getRow(row);
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			if(!headCol.equalsIgnoreCase("Particulars")){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+1, true);
				cellHeader.setCellValue(headCol);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row headerMinMax =  sheet.getRow(row+1);
				Cell cellMinMax = headerMinMax.getCell(cellIndex);
				sheet.setColumnWidth(cellIndex, 5000);
				cellMinMax.setCellValue("Management");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				cellMax.setCellValue("Analyst");
				cellMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				
			}else{
				cellHeader.setCellValue(headCol);
				ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
			}
		}
	}
	private void createHeaderManageGuide(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		Cell sectionCell = sectionDesc.getCell(1);
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		System.out.println("header "+header +" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		Row headerRow = sheet.getRow(row);
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			if(!headCol.equalsIgnoreCase("Particulars")){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+1, true);
				cellHeader.setCellValue(headCol);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row headerMinMax =  sheet.getRow(row+1);
				Cell cellMinMax = headerMinMax.getCell(cellIndex);
				sheet.setColumnWidth(cellIndex, 5000);
				cellMinMax.setCellValue("High");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				cellMax.setCellValue("Low");
				cellMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				
			}else{
				cellHeader.setCellValue(headCol);
				ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
			}
		}
	}
	
	private void setColumnValue(Cell cell, String data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges, Boolean isLastRecord,String Align) {
		if (data != null) {
			
			cell.setCellValue(data);
			//cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
			if(Align.equalsIgnoreCase("LEFT")){
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
				}
			}else{
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
				}
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
	
	private void setColumnValue(Cell cell, Integer value, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ge,Boolean isLastRecord) {
		if (value != null) {
			cell.setCellValue(value);
			if (isLastRecord) {
			if (Math.abs(value) >= 1.0) {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
			} else if (Math.abs(value) < 10.0) {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL);
			} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
			} else {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}

		} else {
			if (Math.abs(value) >= 1.0) {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
			} else if (Math.abs(value) < 10.0) {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL);
			} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
			} else {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}
		}
		} else {
			cell.setCellValue(CMStatic.NA);
			//cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			if(isLastRecord){
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}
		
	}
	
	private void setColumnValue(Cell cell, Date data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges,Boolean isLastRecord) {
		if (data != null) {
			DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String period = outputFormatter.format(data);
			cell.setCellValue(period);
			//cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
		} else {
			cell.setCellValue(CMStatic.NA);
			//cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
		}
		if(isLastRecord){
			cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
		}else{
			cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
		}
	}

	/*public HSSFWorkbook createExcelReportConsensus(List<List<AnalystCoverageDTO>> recData,List<List<AnalystCoverageDTO>> manData,List<List<AnalystCoverageDTO>> conData, String companyId,String companyName) throws Exception {*/
	public HSSFWorkbook createExcelReportConsensus(List<List<AnalystCoverageDTO>> recData, String companyId,String companyName) throws Exception {
	try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			for(int i=0;i<recData.size();i++){
				String periodType="";
				if(i==0){
					periodType = "Quarterly";
				}else{
					periodType = "Yearly";
				}
				
				HSSFSheet sheet = workbook.createSheet("Consensus-"+periodType);
				sheet.setDisplayGridlines(false);
				//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
				/*createAndFillSheetConsensus(workbook,recData.get(i), manData.get(i),conData.get(i),"Consensus-"+periodType,ges,companyName,periodType);*/
				createAndFillSheetConsensus(workbook,recData.get(i),"Consensus-"+periodType,ges,companyName,periodType);
				//createAndFillSheetManageGuide(workbook,manData.get(i), "Consensus-"+periodType,ges,companyName,periodType);
				//createAndFillSheetConsensusOper(workbook,conData.get(i), "Consensus-"+periodType,ges,companyName,periodType);
			}
			
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}

	/*private void createAndFillSheetConsensus(HSSFWorkbook workbook,List<AnalystCoverageDTO> recData, List<AnalystCoverageDTO> manData, List<AnalystCoverageDTO> conData,String sheetName,GenerateExcelStyle ges, String companyName,String periodType) {*/
	private void createAndFillSheetConsensus(HSSFWorkbook workbook,List<AnalystCoverageDTO> recData,String sheetName,GenerateExcelStyle ges, String companyName,String periodType) {
		_log.info("creating the Analyst Consensus sheet data ");
		_log.info("recData ::::: " + recData);
		try {
			int rowdesign = 0;
			
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(r==null) {
				r=workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			if(celldesign==null) {
				celldesign=r.createCell(1);
			}
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			int headerDebt = 10;
			
			Row companyDesc = sheet.getRow(2);
			if(null==companyDesc) {
				companyDesc=sheet.createRow(2);
			}
			Cell compCell = companyDesc.getCell(1);
			if(compCell==null) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Company");
			Cell companyCellData = companyDesc.getCell(2);
			if(companyCellData==null) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			/*companyDesc = sheet.getRow(3);
			compCell = companyDesc.getCell(1);
			compCell.setCellValue("Currency");
			companyCellData = companyDesc.getCell(2);
			companyCellData.setCellValue("USD");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			companyDesc = sheet.getRow(4);
			compCell = companyDesc.getCell(1);
			compCell.setCellValue("Unit");
			companyCellData = companyDesc.getCell(2);
			companyCellData.setCellValue("Million");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);*/
			/*companyDesc = sheet.getRow(6);
			compCell = companyDesc.getCell(1);
			compCell.setCellValue("Analyst Estimates-Financials");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);*/
			
			List<String> periodList = new ArrayList<String>();
			List<String> headerList = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			headerList.add("Particulars");
			for (int i = recData.size()-1; i >=0 ; i--) {
				if(!periodList.contains(sdf.format(recData.get(i).getStartDate()))){
					periodList.add(sdf.format(recData.get(i).getStartDate()));
					//operatingHeaderVal.add("Year Ended "+sdf.format(segmentBusinessData.get(i).getDate()));
				}
			}
			Collections.sort(periodList);
			Collections.reverse(periodList);
			for (int i = 0; i < periodList.size(); i++) {
				if(periodType.equalsIgnoreCase("yearly")){
					headerList.add("Year Ended "+periodList.get(i));
				}else{
					headerList.add("Quarter Ended "+periodList.get(i));
				}
			}
			try {
				createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Analyst Estimates-Financials", headerList);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Boolean isLastRecord = false;
			Map<String, Map<String,String>> dataMap = new LinkedHashMap<String,Map<String,String>>();
			Map<String, String> unitMap = new LinkedHashMap<String,String>();
			Set<String> periods = new TreeSet<>();
			
			/*for (int i = recData.size()-1; i >=0 ; i--) {*/
			for (int i = 0;i < recData.size()-1; i++) {
				periods.add(sdf.format(recData.get(i).getStartDate()));
				String currency = recData.get(i).getCurrency();
				String unit = recData.get(i).getUnit();
				String currNunit = "";
				if(currency!=null && unit!=null){
					currNunit = " ("+currency +" "+ unit+")";	
				}
				else if( currency!=null && unit==null){
					currNunit = " ("+currency+")";	
				}
				else if(unit!=null && currency==null){
					currNunit = " ("+unit+")";	
				}
				if(!dataMap.containsKey(recData.get(i).getDescription())){
					dataMap.put(recData.get(i).getDescription(), new HashMap<String,String>(1));
					unitMap.put(recData.get(i).getDescription(), currNunit);
				}
				dataMap.get(recData.get(i).getDescription()).put(sdf.format(recData.get(i).getStartDate()),String.valueOf(recData.get(i).getFeMean())+"#:#"+String.valueOf(recData.get(i).getFeMedian())+"#:#"+String.valueOf(recData.get(i).getFeHigh())+"#:#"+String.valueOf(recData.get(i).getFeLow())+"#:#"+String.valueOf(recData.get(i).getFeStdDev()));
			}
			
			_log.info(dataMap);
			_log.info(unitMap);
			for(int i=0;i<dataMap.size();i++){
				if(i==dataMap.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				if(rowDebt==null) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//Setting Operational Value Start
				_log.info(dataMap.keySet().toArray()[i].toString());
				_log.info(unitMap.get(recData.get(i).getDescription()));
				setColumnValue(cellDebt,dataMap.keySet().toArray()[i].toString()+unitMap.get(dataMap.keySet().toArray()[i].toString()),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < periodList.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(cellDebt==null) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					String cellVal = dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
					//_log.info(cellVal);
					if(cellVal==null || cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						String meanMedian[] = cellVal.split("#:#"); 
						String mean = meanMedian[0];
						String median = meanMedian[1];
						String high = meanMedian[2];
						String low = meanMedian[3];
						String sd = meanMedian[4];
						if(mean==null||mean.equalsIgnoreCase("null")){
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						else if(mean=="") {
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						else{
							setColumnValue(cellDebt,mean,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						if(median==null||median.equalsIgnoreCase("null")){
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}else if(median=="") {
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						else{
							setColumnValue(cellDebt,median,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						if(high==null||high.equalsIgnoreCase("null")){
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}else if(high=="") {
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						else{
							setColumnValue(cellDebt,high,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						if(low==null||low.equalsIgnoreCase("null")){
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}else if(low=="") {
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						else{
							setColumnValue(cellDebt,low,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(cellDebt==null) {
							cellDebt=rowDebt.createCell(cellIdxDebt);
						}
						if(sd==null||sd.equalsIgnoreCase("null")){
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}else if(sd=="") {
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						else{
							setColumnValue(cellDebt,sd,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
					}
				}
				headerDebt++;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			_log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	
	
	
	
}


/*headerDebt = headerDebt+5;
manage guide
periodList.clear();
headerList.clear();

headerList.add("Particulars");
for (int i = manData.size()-1; i >=0 ; i--) {
	if(!periodList.contains(sdf.format(manData.get(i).getStartDate())) && periodList.size()<10){
		periodList.add(sdf.format(manData.get(i).getStartDate()));
		//operatingHeaderVal.add("Year Ended "+sdf.format(segmentBusinessData.get(i).getDate()));
	}
}
Collections.sort(periodList);
Collections.reverse(periodList);
for (int i = 0; i < periodList.size(); i++) {
	if(periodType.equalsIgnoreCase("yearly")){
		headerList.add("Year Ended "+periodList.get(i));
	}else{
		headerList.add("Quarter Ended "+periodList.get(i));
	}
}
try {
	createHeaderManageGuide(workbook.getSheet(sheetName), headerDebt, ges, "Analyst ManageGuide-Financials", headerList);
}catch(Exception e){
	e.printStackTrace();
}

Boolean isLastRecord1 = false;
Map<String, Map<String,List<String>>> dataMap1 = new LinkedHashMap<String,Map<String,List<String>>>();
//dataMap1.clear();
Set<String> periods1 = new TreeSet<>();

for (int i = manData.size()-1; i >=0 ; i--) {
	periods1.add(sdf.format(manData.get(i).getStartDate()));
	
	if(!dataMap1.containsKey(manData.get(i).getDescription())){
		List<String> str = new ArrayList<String>(2);
		str.add("high");
		str.add("low");
		dataMap1.put(manData.get(i).getDescription(), new HashMap<String,List<String>>(1));
		if(manData.get(i).getGuidanceType().equalsIgnoreCase("high")) {
			str.set(0, String.valueOf(manData.get(i).getGuidanceValue())); 
			dataMap1.get(manData.get(i).getDescription()).put(sdf.format(manData.get(i).getStartDate()),str);
		
		}
		else {
			
			str.set(1, String.valueOf(manData.get(i).getGuidanceValue())); 
			dataMap1.get(manData.get(i).getDescription()).put(sdf.format(manData.get(i).getStartDate()),str);
			
		}
	}
	else{
		if(manData.get(i).getGuidanceType().equalsIgnoreCase("high")) {
			List<String> strRep = dataMap1.get(manData.get(i).getDescription()).get(sdf.format(manData.get(i).getStartDate()));
			strRep.set(0, String.valueOf(manData.get(i).getGuidanceValue())); 
			dataMap1.get(manData.get(i).getDescription()).put(sdf.format(manData.get(i).getStartDate()),strRep);
		
		}
		else {
			
			List<String> strRep = dataMap1.get(manData.get(i).getDescription()).get(sdf.format(manData.get(i).getStartDate()));
			strRep.set(1, String.valueOf(manData.get(i).getGuidanceValue())); 
			dataMap1.get(manData.get(i).getDescription()).put(sdf.format(manData.get(i).getStartDate()),strRep);
			
		}
		List<String> highlow = dataMap1.get(manData.get(i).getDescription()).get(sdf.format(manData.get(i).getStartDate()));
		highlow.concat("#:#"+String.valueOf(manData.get(i).getGuidanceValue()));
		if(manData.get(i).getGuidanceValue()!=null)
		highlow.add(String.valueOf(manData.get(i).getGuidanceValue()));
		dataMap1.get(manData.get(i).getDescription()).put(sdf.format(manData.get(i).getStartDate()),highlow);
	}
	
}

_log.info(dataMap1);
for(int i=0;i<dataMap1.size();i++){
	if(i==dataMap1.size()-1){
		isLastRecord1 = true;
	}
	Row rowDebt = sheet.getRow(headerDebt);
	int cellIdxDebt = 1;
	Cell cellDebt = rowDebt.getCell(cellIdxDebt);
	//Setting Operational Value Start
	setColumnValue(cellDebt,dataMap1.keySet().toArray()[i].toString(),sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
	
	for (int j = 0; j < periodList.size(); j++) {
		cellIdxDebt = cellIdxDebt+1;
		cellDebt = rowDebt.getCell(cellIdxDebt);
		List<String> cellVal = dataMap1.get(dataMap1.keySet().toArray()[i].toString()).get(periodList.get(j));
		if(cellVal==null){
			setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
			cellIdxDebt = cellIdxDebt+1;
			cellDebt = rowDebt.getCell(cellIdxDebt);
			setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
		}else{
			//String highLow[] = cellVal.get(index) 
			String high = cellVal.get(0);
			String low = cellVal.get(1);
			if(high==null||high=="null"){
				setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
			}else{
				setColumnValue(cellDebt,high,sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
			}
			cellIdxDebt = cellIdxDebt+1;
			cellDebt = rowDebt.getCell(cellIdxDebt);
			if(low==null||low=="null"){
				setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
			}else{
				setColumnValue(cellDebt,low,sheet,cellIdxDebt,ges,isLastRecord1,"LEFT");
			}
		}
	}
	headerDebt++;

}

consensus oper
headerDebt = headerDebt+5;
manage guide
periodList.clear();
headerList.clear();

headerList.add("Particulars");
for (int i = conData.size()-1; i >=0 ; i--) {
	if(!periodList.contains(sdf.format(conData.get(i).getAnalyst_date())) && periodList.size()<10){
		periodList.add(sdf.format(conData.get(i).getAnalyst_date()));
		//operatingHeaderVal.add("Year Ended "+sdf.format(segmentBusinessData.get(i).getDate()));
	}
}
Collections.sort(periodList);
Collections.reverse(periodList);
for (int i = 0; i < periodList.size(); i++) {
	if(periodType.equalsIgnoreCase("yearly")){
		headerList.add("Year Ended "+periodList.get(i));
	}else{
		headerList.add("Quarter Ended "+periodList.get(i));
	}
}
try {
	createHeaderConsensusOpr(workbook.getSheet(sheetName), headerDebt, ges, "Consensus Operational-Financials", headerList);
}catch(Exception e){
	e.printStackTrace();
}

isLastRecord = false;
//Map<String, Map<String,String>> dataMap = new LinkedHashMap<String,Map<String,String>>();
dataMap.clear();
Set<String> periods2 = new TreeSet<>();

for (int i = conData.size()-1; i >=0 ; i--) {
	periods2.add(sdf.format(conData.get(i).getAnalyst_date()));
	if(!dataMap.containsKey(conData.get(i).getDescription())){
		dataMap.put(conData.get(i).getDescription(), new HashMap<String,String>(1));
	}
	
		dataMap.get(conData.get(i).getDescription()).put(sdf.format(conData.get(i).getAnalyst_date()),String.valueOf(conData.get(i).getManagement_value())+"#:#"+String.valueOf(conData.get(i).getAnalyst_value()));
	
}

_log.info(dataMap);
for(int i=0;i<dataMap.size();i++){
	if(i==dataMap.size()-1){
		isLastRecord = true;
	}
	Row rowDebt = sheet.getRow(headerDebt);
	int cellIdxDebt = 1;
	Cell cellDebt = rowDebt.getCell(cellIdxDebt);
	//Setting Operational Value Start
	setColumnValue(cellDebt,dataMap.keySet().toArray()[i].toString(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
	
	for (int j = 0; j < periodList.size(); j++) {
		cellIdxDebt = cellIdxDebt+1;
		cellDebt = rowDebt.getCell(cellIdxDebt);
		String cellVal = dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
		if(cellVal==null || cellVal=="null"){
			setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
			cellIdxDebt = cellIdxDebt+1;
			cellDebt = rowDebt.getCell(cellIdxDebt);
			setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
		}else{
			String manageAna[] = cellVal.split("#:#"); 
			String manage = manageAna[0];
			String ana = manageAna[1];
			_log.info(manage);
			if(manage==null||manage=="null"){
				_log.info(manage);
				setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
			}else{
				_log.info(manage);
				setColumnValue(cellDebt,manage,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
			}
			cellIdxDebt = cellIdxDebt+1;
			cellDebt = rowDebt.getCell(cellIdxDebt);
			if(ana==null||ana=="null"){
				_log.info(ana);
				setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
			}else{
				_log.info(ana);
				setColumnValue(cellDebt,ana,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
			}
		}
	}
	headerDebt++;

}*/
