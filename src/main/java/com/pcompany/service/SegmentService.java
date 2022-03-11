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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pcompany.dao.SegmentDao;
import com.pcompany.dto.SegmentBusinesDTO;
import com.pcompany.dto.SegmentOperationalDTO;
import com.pcompany.dto.SegmentRegionDTO;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.ExcelReportService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class SegmentService {
	
	@Autowired
	ExcelReportService excelService;
	
	@Autowired
	private ExcelDesignService excelDesignService;
	
	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	Logger _log = Logger.getLogger(SegmentService.class);
	
	@Autowired
	SegmentDao segmentDao;

	public List<SegmentBusinesDTO> getBusinessData(String fsimId,Date startDate, Date endDate, String currency) {
		// TODO Auto-generated method stub
		_log.info("In Service get Business Data");
		return segmentDao.getBusinessData(fsimId,startDate,endDate,currency);
	}

	public List<SegmentRegionDTO> getRegionData(String fsimId, Date startDate,Date endDate, String currency) {
		// TODO Auto-generated method stub
		_log.info("In Service get region Data");
		return segmentDao.getRegionData(fsimId,startDate,endDate,currency);
	}

	public List<SegmentOperationalDTO> getoperationalData(String fsimId,Date startDate, Date endDate) {
		_log.info("In Service get Operational Data Annual.");
		return segmentDao.getOperationalData(fsimId,startDate,endDate);
	}
	
	public List<SegmentOperationalDTO> getoperationalDataQuarter(String fsimId,	Date startDate, Date endDate) {
		_log.info("In Service get region Data Quarter.");
		return segmentDao.getOperationalDataQuarter(fsimId,startDate,endDate);
	}
	
	public HSSFWorkbook createExcelReport(List<SegmentOperationalDTO> opData, String entityId,String companyName) throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet("Segment-Operational-Data");
			ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			createAndFillSheet(workbook,opData, "Segment-Operational-Data",ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createAndFillSheet(HSSFWorkbook workbook,List<SegmentOperationalDTO> opData, String sheetName,GenerateExcelStyle ges,String companyName) throws JsonProcessingException {
		
		_log.info("creating the financial sheet data ");
		
		try {

			int rowdesign = 0;
			
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			int headerDebt = 10;
			
			Row companyDesc = sheet.getRow(2);
			Cell compCell = companyDesc.getCell(1);
			compCell.setCellValue("Company");
			Cell companyCellData = companyDesc.getCell(2);
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			
			List<String> name = new ArrayList<String>();
			LinkedHashSet<Integer> reportDate = new LinkedHashSet<Integer>();
			HashMap<String,List<String>> mapData = new HashMap<String,List<String>>();
			List<String> headerVal = new ArrayList<String>();
			
			headerVal.add("Name");
			for(int i=opData.size()-1;i>=0;i--){
				
				if(reportDate.size()<10){
					if(!reportDate.contains(opData.get(i).getReportDate().getYear()+1900)){
						reportDate.add(opData.get(i).getReportDate().getYear()+1900);
						headerVal.add(String.valueOf(opData.get(i).getReportDate().getYear()+1900));
					}
				}
			}
			try {
				createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Operational Details", headerVal);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Boolean isLastRecord = false;
			
			List<String> data = new ArrayList<String>();
			for(int i=opData.size()-1;i>=0;i--){
				if(name.size()<10){
					if(!name.contains(opData.get(i).getName())){
						data.add(opData.get(i).getReportDate().getYear()+1900+"#:#"+opData.get(i).getActualValue());
						name.add(opData.get(i).getName());
						if(opData.get(i).getName()!=opData.get(i-1).getName() && i!=0){
							if(data.size()<10){
								for (int j = data.size(); j < 10; j++) {
								data.add(String.valueOf(opData.get(i).getReportDate().getYear()+1900)+"#:#"+"-");
								}
								mapData.put(opData.get(i).getName(), data);
								data.clear();
							}else{
								mapData.put(opData.get(i).getName(), data);
								data.clear();
							}
						}else {
							
						}
					}else{
						if(data.size()<10){
							data.add(opData.get(i).getReportDate().getYear()+1900+"#:#"+opData.get(i).getActualValue());
						}
					}
					_log.info("data ::: " + data.size() + "\treportDate ::: " + reportDate);
					
				}
			}
			_log.info("NAME :::::::::: " + name);
			_log.info("mapData :::::::: " + mapData);
			
			for(int i=0;i<name.size();i++){
				SegmentOperationalDTO oprationalDetail = opData.get(i);
				if(i==name.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				
				int cellIdxDebt = 1;
				
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				
				
				
				//Setting Operational Value Start
				setColumnValue(cellDebt,name.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < mapData.get(name.get(i)).size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = mapData.get(name.get(i)).get(i).split("#:#")[1];
					if(cellVal==null){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				
				headerDebt++;
			}
			
			/*for(int i=0;i<opData.size();i++){
				SegmentOperationalDTO oprationalDetail = opData.get(i);
				if(i==opData.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				
				int cellIdxDebt = 1;
				
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				
				
				
				//Setting Operational Value Start
				setColumnValue(cellDebt,oprationalDetail.getName(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(oprationalDetail.getActualValue().toString()==null){
					setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}else{
					setColumnValue(cellDebt,oprationalDetail.getActualValue().toString(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}
				
				
				
				headerDebt++;
			}*/

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
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
	
	private void setColumnValue(Cell cell, Double value, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ge,Boolean isLastRecord) {
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
	
	private void createHeader(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		if(sectionDesc==null) {
			sectionDesc=sheet.createRow(startingHeaderRow-3);
		}
		Cell sectionCell = sectionDesc.getCell(1);
		if(null==sectionCell) {
			sectionCell=sectionDesc.createCell(1);
		}
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		System.out.println("header "+header +" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		
		if(r==null) {
			r=sheet.createRow(startingHeaderRow);
		}
		
		//sheet.setColumnWidth(cellIndex, 5000);
		//ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
		
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
			
			if(headCol.equalsIgnoreCase("Coupon Rate")){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+1, true);
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
				cellMinMax.setCellValue("Min");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				if(null==cellMax) {
					cellMax=headerMinMax.createCell(cellIndex);
				}
				cellMax.setCellValue("Max");
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

	public HSSFWorkbook createExcelReportSegment(List<SegmentBusinesDTO> segmentBusinessData,List<SegmentRegionDTO> segmentRegionData, String fsimId, String companyName) 
	{
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet("Segment Information");
			//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			sheet.setDisplayGridlines(false);
			createAndFillSheetRegionSegment(workbook,segmentBusinessData,segmentRegionData, "Segment Information",ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}

	private void createAndFillSheetRegionSegment(HSSFWorkbook workbook,List<SegmentBusinesDTO> segmentBusinessData,List<SegmentRegionDTO> segmentRegionData, String sheetName,GenerateExcelStyle ges, String companyName) 
	{
		
		_log.info("creating the Regional / Business sheet data ::::::");
		try {

			int rowdesign = 0;
			
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(null==r) {
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
			companyDesc = sheet.getRow(3);
			
			if(companyDesc==null) {
				companyDesc=sheet.createRow(3);
			}
			compCell = companyDesc.getCell(1);
			if(compCell==null) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Currency");
			companyCellData = companyDesc.getCell(2);
			if(companyCellData==null) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(segmentBusinessData.get(0).getCurrency());
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			companyDesc = sheet.getRow(4);
			if(companyDesc==null) {
				companyDesc=sheet.createRow(4);
			}
			compCell = companyDesc.getCell(1);
			if(compCell==null) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Unit");
			companyCellData = companyDesc.getCell(2);
			if(null==companyCellData) {
          companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue("Million");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			companyDesc = sheet.getRow(6);
			if(null==companyDesc) {
				companyDesc=sheet.createRow(6);
			}
			compCell = companyDesc.getCell(1);
			if(compCell==null) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Operating Segments");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			
			List<String> operatingReportDate = new ArrayList<String>();
			List<String> operatingHeaderVal = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
			operatingHeaderVal.add("Segment");
			for (int i = segmentBusinessData.size()-1; i >=0 ; i--) {
				if(!operatingReportDate.contains(sdf.format(segmentBusinessData.get(i).getDate())) && operatingReportDate.size()<10){
					operatingReportDate.add(sdf.format(segmentBusinessData.get(i).getDate()));
					//operatingHeaderVal.add("Year Ended "+sdf.format(segmentBusinessData.get(i).getDate()));
				}
			}
			Collections.sort(operatingReportDate);
			Collections.reverse(operatingReportDate);
			/*for (int i = 0; i < operatingReportDate.size(); i++) {
				operatingHeaderVal.add("Year Ended "+operatingReportDate.get(i));
			}*/
			try {
				createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Sales", operatingHeaderVal);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Boolean isLastRecord = false;
			
			Map<String, Map<String,String>> dataMap = new LinkedHashMap<String,Map<String,String>>();
			Map<String, Map<String,Double>> dataMapSales = new LinkedHashMap<String,Map<String,Double>>();
			Map<String, Map<String,Double>> dataMapOpinc = new LinkedHashMap<String,Map<String,Double>>();
			Map<String, Map<String,Double>> dataMapAssets = new LinkedHashMap<String,Map<String,Double>>();
			Map<String, Map<String,Double>> dataMapCapex = new LinkedHashMap<String,Map<String,Double>>();
			Map<String, Map<String,Double>> dataMapDep = new LinkedHashMap<String,Map<String,Double>>();
			Set<String> periods = new TreeSet<>();
			List<String> label = new ArrayList<String>();
			
			
			
			for (int i = segmentBusinessData.size()-1; i >=0 ; i--) {
				periods.add(sdf.format(segmentBusinessData.get(i).getDate()));
				/*if(!dataMap.containsKey(segmentBusinessData.get(i).getLabel())){
					dataMap.put(segmentBusinessData.get(i).getLabel(), new HashMap<String,String>(1));
				}
				dataMap.get(segmentBusinessData.get(i).getLabel()).put(sdf.format(segmentBusinessData.get(i).getDate()),String.valueOf(segmentBusinessData.get(i).getSales()));*/
				if(!dataMapSales.containsKey(segmentBusinessData.get(i).getLabel())){
					dataMapSales.put(segmentBusinessData.get(i).getLabel(), new HashMap<String,Double>(1));
					label.add(segmentBusinessData.get(i).getLabel());
				}
				dataMapSales.get(segmentBusinessData.get(i).getLabel()).put(sdf.format(segmentBusinessData.get(i).getDate()),segmentBusinessData.get(i).getSales());
				if(!dataMapOpinc.containsKey(segmentBusinessData.get(i).getLabel())){
					dataMapOpinc.put(segmentBusinessData.get(i).getLabel(), new HashMap<String,Double>(1));
				}
				dataMapOpinc.get(segmentBusinessData.get(i).getLabel()).put(sdf.format(segmentBusinessData.get(i).getDate()),segmentBusinessData.get(i).getOpinc());
				if(!dataMapAssets.containsKey(segmentBusinessData.get(i).getLabel())){
					dataMapAssets.put(segmentBusinessData.get(i).getLabel(), new HashMap<String,Double>(1));
				}
				dataMapAssets.get(segmentBusinessData.get(i).getLabel()).put(sdf.format(segmentBusinessData.get(i).getDate()),segmentBusinessData.get(i).getAssets());
				if(!dataMapCapex.containsKey(segmentBusinessData.get(i).getLabel())){
					dataMapCapex.put(segmentBusinessData.get(i).getLabel(), new HashMap<String,Double>(1));
				}
				dataMapCapex.get(segmentBusinessData.get(i).getLabel()).put(sdf.format(segmentBusinessData.get(i).getDate()),segmentBusinessData.get(i).getCapex());
				if(!dataMapDep.containsKey(segmentBusinessData.get(i).getLabel())){
					dataMapDep.put(segmentBusinessData.get(i).getLabel(), new HashMap<String,Double>(1));
				}
				dataMapDep.get(segmentBusinessData.get(i).getLabel()).put(sdf.format(segmentBusinessData.get(i).getDate()),segmentBusinessData.get(i).getDep());
			}
			
			List<String> list = new ArrayList<>(periods);
			list.sort(Collections.reverseOrder());
			periods = new LinkedHashSet<>(list);
			Collections.reverse(label);
			
			for (int i = 0; i < list.size(); i++) {
				operatingHeaderVal.add("Year Ended "+list.get(i));
			}
			
			//_log.info("dataMap ::::: "+ dataMap);
			_log.info("dataMapSales ::::: "+ dataMapSales);
			_log.info("dataMapOpinc ::::: "+ dataMapOpinc);
			_log.info("dataMapAssets ::::: "+ dataMapAssets);
			_log.info("dataMapCapex ::::: "+ dataMapCapex);
			_log.info("dataMapDep ::::: "+ dataMapDep);
			_log.info("periods ::::: "+ periods);
			_log.info("label ::: " + label);
			
			
			 /*
			  * sales ----- Min : Start
			  */
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Sales", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(cellDebt==null) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMapSales.get(label.get(i)).get(list.get(j));
					if(cellVal==null){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			/*
			  * Operating Income ----- Min : Start
			  */
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Operating Income", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMapOpinc.get(label.get(i)).get(list.get(j));
					if(cellVal==null){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			/*
			  * Assets ----- Min : Start
			  */
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Assets", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMapAssets.get(label.get(i)).get(list.get(j));
					if(cellVal==null){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			/*
			  * Capital Expenditure ----- Min : Start
			  */
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Capital Expenditure", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMapCapex.get(label.get(i)).get(list.get(j));
					if(cellVal==null){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			/*
			  * Depreciation Expense ----- Min : Start
			  */
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Depreciation Expense", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMapDep.get(label.get(i)).get(list.get(j));
					if(cellVal==null){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
					}
				}
				headerDebt++;
			}
			headerDebt += 3;
			
			/*		Operating Segments End		*/
			
			/*		Geographic Segments start		*/
			
			
			/*_log.info(headerDebt);
			companyDesc = sheet.getRow(headerDebt);
			compCell = companyDesc.getCell(1);
			compCell.setCellValue("Geographic Segment");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			
			headerDebt+=4;
			_log.info(headerDebt);
			operatingReportDate = new ArrayList<String>();
			operatingHeaderVal = new ArrayList<String>();
			operatingHeaderVal.add("Metric");
			for (int i = segmentRegionData.size()-1; i >=0 ; i--) {
				if(!operatingReportDate.contains(sdf.format(segmentRegionData.get(i).getDate())) && operatingReportDate.size()<10){
					operatingReportDate.add(sdf.format(segmentRegionData.get(i).getDate()));
					//operatingHeaderVal.add("Year Ended "+sdf.format(segmentBusinessData.get(i).getDate()));
				}
			}
			Collections.sort(operatingReportDate);
			Collections.reverse(operatingReportDate);
			for (int i = 0; i < operatingReportDate.size(); i++) {
				operatingHeaderVal.add("Year Ended "+operatingReportDate.get(i));
			}
			
			isLastRecord = false;
			
			dataMap = new LinkedHashMap<String,Map<String,String>>();
			dataMapSales = new LinkedHashMap<String,Map<String,String>>();
			dataMapOpinc = new LinkedHashMap<String,Map<String,String>>();
			dataMapAssets = new LinkedHashMap<String,Map<String,String>>();
			dataMapCapex = new LinkedHashMap<String,Map<String,String>>();
			dataMapDep = new LinkedHashMap<String,Map<String,String>>();
			periods = new TreeSet<>();
			label = new ArrayList<String>();
			
			
			
			for (int i = segmentRegionData.size()-1; i >=0 ; i--) {
				periods.add(sdf.format(segmentRegionData.get(i).getDate()));
				if(!dataMap.containsKey(segmentRegionData.get(i).getLabel())){
					dataMap.put(segmentRegionData.get(i).getLabel(), new HashMap<String,String>(1));
				}
				dataMap.get(segmentRegionData.get(i).getLabel()).put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getSales()));
				if(!dataMapSales.containsKey(segmentRegionData.get(i).getLabel())){
					dataMapSales.put(segmentRegionData.get(i).getLabel(), new HashMap<String,String>(1));
					label.add(segmentRegionData.get(i).getLabel());
				}
				dataMapSales.get(segmentRegionData.get(i).getLabel()).put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getSales()));
				if(!dataMapOpinc.containsKey(segmentRegionData.get(i).getLabel())){
					dataMapOpinc.put(segmentRegionData.get(i).getLabel(), new HashMap<String,String>(1));
				}
				dataMapOpinc.get(segmentRegionData.get(i).getLabel()).put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getOpinc()));
				if(!dataMapAssets.containsKey(segmentRegionData.get(i).getLabel())){
					dataMapAssets.put(segmentRegionData.get(i).getLabel(), new HashMap<String,String>(1));
				}
				dataMapAssets.get(segmentRegionData.get(i).getLabel()).put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getAssets()));
				if(!dataMapCapex.containsKey(segmentRegionData.get(i).getLabel())){
					dataMapCapex.put(segmentRegionData.get(i).getLabel(), new HashMap<String,String>(1));
				}
				dataMapCapex.get(segmentRegionData.get(i).getLabel()).put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getCapex()));
				if(!dataMapDep.containsKey(segmentRegionData.get(i).getLabel())){
					dataMapDep.put(segmentRegionData.get(i).getLabel(), new HashMap<String,String>(1));
				}
				dataMapDep.get(segmentRegionData.get(i).getLabel()).put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getDep()));
			}
			
			list = new ArrayList<>(periods);
			list.sort(Collections.reverseOrder());
			periods = new LinkedHashSet<>(list);
			Collections.reverse(label);
			
			for (int i = 0; i < list.size(); i++) {
				operatingHeaderVal.add("Year Ended "+list.get(i));
			}
			
			//_log.info("dataMap ::::: "+ dataMap);
			_log.info("dataMapSales ::::: "+ dataMapSales);
			_log.info("dataMapOpinc ::::: "+ dataMapOpinc);
			_log.info("dataMapAssets ::::: "+ dataMapAssets);
			_log.info("dataMapCapex ::::: "+ dataMapCapex);
			_log.info("dataMapDep ::::: "+ dataMapDep);
			_log.info("periods ::::: "+ periods);
			_log.info("label ::: " + label);
			
			
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Sales", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMapSales.get(label.get(i)).get(list.get(j));
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Operating Income", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMapOpinc.get(label.get(i)).get(list.get(j));
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Assets", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMapAssets.get(label.get(i)).get(list.get(j));
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Capital Expenditure", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMapCapex.get(label.get(i)).get(list.get(j));
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				headerDebt++;
			}
			headerDebt += 6;
			
			
			createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Depreciation Expense", operatingHeaderVal);
			
			for (int i = 0; i < label.size(); i++) {
				isLastRecord = true;
				Row rowDebt = sheet.getRow(headerDebt);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				setColumnValue(cellDebt,label.get(i),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < list.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMapDep.get(label.get(i)).get(list.get(j));
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				headerDebt++;
			}
			headerDebt += 6;*/
			
			
/*headerDebt+=5;
			
			companyDesc = sheet.getRow(headerDebt);
			compCell = companyDesc.getCell(1);
			compCell.setCellValue("Geographic Segment");
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			
			List<String> geoReportDate = new ArrayList<String>();
			List<String> geoHeaderVal = new ArrayList<String>();
			geoHeaderVal.add("Metric");
			for (int i = segmentRegionData.size()-1; i >=0 ; i--) {
				if(!geoReportDate.contains(sdf.format(segmentRegionData.get(i).getDate())) && geoReportDate.size()<10){
					geoReportDate.add(sdf.format(segmentRegionData.get(i).getDate()));
					//geoHeaderVal.add("Year Ended "+sdf.format(segmentRegionData.get(i).getDate()));
				}
			}
			Collections.sort(geoReportDate);
			Collections.reverse(geoReportDate);
			for (int i = 0; i < geoReportDate.size(); i++) {
				geoHeaderVal.add("Year Ended "+geoReportDate.get(i));
			}
			
			dataMap = new LinkedHashMap<String,Map<String,String>>();
			periods = new TreeSet<>();
			List<String> regLocations = new ArrayList<String>(); 
			for (int i = segmentRegionData.size()-1; i >=0 ; i--) {
							periods.add(sdf.format(segmentRegionData.get(i).getDate()));
							if(!regLocations.contains(segmentRegionData.get(i).getLabel())){
								dataMap.put(segmentRegionData.get(i).getLabel()+"#:#sales", new HashMap<String,String>(1));
								dataMap.put(segmentRegionData.get(i).getLabel()+"#:#opinc", new HashMap<String,String>(1));
								dataMap.put(segmentRegionData.get(i).getLabel()+"#:#assets", new HashMap<String,String>(1));
								dataMap.put(segmentRegionData.get(i).getLabel()+"#:#capex", new HashMap<String,String>(1));
								dataMap.put(segmentRegionData.get(i).getLabel()+"#:#dep", new HashMap<String,String>(1));
								regLocations.add(segmentRegionData.get(i).getLabel());
							}
							dataMap.get(segmentRegionData.get(i).getLabel()+"#:#sales").put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getSales()));
							dataMap.get(segmentRegionData.get(i).getLabel()+"#:#opinc").put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getOpinc()));
							dataMap.get(segmentRegionData.get(i).getLabel()+"#:#assets").put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getAssets()));
							dataMap.get(segmentRegionData.get(i).getLabel()+"#:#capex").put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getCapex()));
							dataMap.get(segmentRegionData.get(i).getLabel()+"#:#dep").put(sdf.format(segmentRegionData.get(i).getDate()),String.valueOf(segmentRegionData.get(i).getDep()));
			}
			
			_log.info("REGION dataMap :::: " + dataMap);
			
			
			for(int i=0;i<dataMap.size();i++){
				if(i==dataMap.size()-1){
					isLastRecord = true;
				}
				if(dataMap.keySet().toArray()[i].toString().contains("sales")){
					headerDebt += 6;
					try {
						createHeader(workbook.getSheet(sheetName), headerDebt, ges, dataMap.keySet().toArray()[i].toString().split("#:#")[0], geoHeaderVal);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				Row rowDebt = sheet.getRow(headerDebt);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				//Setting Operational Value Start
				if("sales".equals(dataMap.keySet().toArray()[i].toString().split("#:#")[1])){
					setColumnValue(cellDebt,"Sales",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}else if("opinc".equals(dataMap.keySet().toArray()[i].toString().split("#:#")[1])){
					setColumnValue(cellDebt,"Operating Income",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}else if("assets".equals(dataMap.keySet().toArray()[i].toString().split("#:#")[1])){
					setColumnValue(cellDebt,"Assets",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}else if("capex".equals(dataMap.keySet().toArray()[i].toString().split("#:#")[1])){
					setColumnValue(cellDebt,"Capital Expenditure",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}else if("dep".equals(dataMap.keySet().toArray()[i].toString().split("#:#")[1])){
					setColumnValue(cellDebt,"Depreciation Expenses",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				}
				
				for (int j = 0; j < geoReportDate.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMap.get(dataMap.keySet().toArray()[i].toString()).get(geoReportDate.get(j));
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}
				}
				headerDebt++;
			}
			
			
			*/
			
			
			/*		Geographic Segments end		*/
			

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("Some error occured in creating the sheet ::: " + e.getLocalizedMessage());
		}
	}

	
	
}
