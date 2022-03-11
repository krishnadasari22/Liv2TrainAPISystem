package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.pcompany.dao.OwnershipStructureDao;
import com.pcompany.dto.DebtCapitalStructureDetailsDTO;
import com.pcompany.dto.OwnershipEmployeeJobDTO;
import com.pcompany.dto.OwnershipManagementInfoDTO;
import com.pcompany.dto.OwnershipPeopleInfoDTO;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

/**
 * 
 * @author varun chawla
 *
 */
@Service
public class OwnershipStructureService {
	
	Logger _log = Logger.getLogger(OwnershipStructureService.class);
	
	@Autowired
	OwnershipStructureDao ownershipStructureDao;
	
	@Autowired
	private ExcelDesignService excelDesignService;
	
	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	public List<OwnershipManagementInfoDTO> getOwnershipManagementInfo(String entityId, String companyName) {
		List<OwnershipManagementInfoDTO> mgmtinfo = ownershipStructureDao.getOwnershipManagementInfo(entityId,companyName);
		//_log.info("before");
		//_log.info(mgmtinfo);
		//mgmtinfo.sort((OwnershipManagementInfoDTO mgmtinfo1,OwnershipManagementInfoDTO mgmtinfo2)->CMStatic.DESIGNATION.get(mgmtinfo1.getJobFunctionCode())-CMStatic.DESIGNATION.get(mgmtinfo2.getJobFunctionCode()));
		//_log.info("after");
		//_log.info(mgmtinfo);
		return mgmtinfo;
	}

	public List<OwnershipPeopleInfoDTO> getPeopleProfile(String personId) {
		return ownershipStructureDao.getPeopleProfile(personId);
	}

	public List<OwnershipEmployeeJobDTO> getEmploymentHistory(String personId) {
		return ownershipStructureDao.getEmploymentHistory(personId);
	}

	public List<OwnershipEmployeeJobDTO> getDirectorship(String personId) {
		return ownershipStructureDao.getDirectorship(personId);
	}

	public HSSFWorkbook createExcelReport(List<OwnershipManagementInfoDTO> managementData, String entityId,String companyName) throws Exception{
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet("Management & Key Employees");
			sheet.setDisplayGridlines(false);
			//ges.setBackgroundOnSheet(sheet, 0, managementData.size()*2+10,CMStatic.SHEET_MAX_COLUMN);
			createAndFillSheet(workbook,managementData, "Management & Key Employees",ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}
	
	public void createAndFillSheet(HSSFWorkbook workbook,List<OwnershipManagementInfoDTO> managementData, String sheetName,GenerateExcelStyle ges, String companyName) throws JsonProcessingException {
		
		_log.info("creating the financial sheet data ");
		
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
			
			int headerOwner = 7;
			
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
			if(null==companyCellData) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			try {
				createHeader(workbook.getSheet(sheetName), headerOwner, ges,  CMStatic.OWNERSHIP_MANAGEMENT_HEADER);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Boolean isLastRecord = false;
			for(int i=0;i<managementData.size();i++){
				OwnershipManagementInfoDTO ownerDetail = managementData.get(i);
				if(i==managementData.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerOwner);
				
				if(rowDebt==null) {
					rowDebt=sheet.createRow(headerOwner);
				}
				
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,ownerDetail.getManagement(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,ownerDetail.getInstrumentType(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,ownerDetail.getExperience(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,ownerDetail.getAppointed(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,ownerDetail.getEmail(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				setColumnValue(cellDebt,ownerDetail.getPhone(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				//setColumnValue(cellDebt,ownerDetail.getBiography(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				setColumnValueLargeCell(cellDebt,ownerDetail.getBiography(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				ges.mergeCells(sheet, headerOwner, headerOwner + 1, cellIdxDebt, cellIdxDebt, true);
				String owner = "";
				if(ownerDetail.getOtherCompanies()!=null){
					owner = ownerDetail.getOtherCompanies().replaceAll("#:#", ",");
				}else{
					owner = null;
				}
				//setColumnValue(cellDebt,owner,sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				setColumnValueLargeCell(cellDebt,owner,sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				headerOwner=headerOwner+2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
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
	
	private void setColumnValueLargeCell(Cell cell, String data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges, Boolean isLastRecord,String Align) {
		if (data != null) {
			
			cell.setCellValue(data);
			//cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
			if(Align.equalsIgnoreCase("LEFT")){
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL);
				}
			}else{
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL);
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
		
		if(null==headerRow) {
			headerRow=sheet.createRow(row);
		}
		
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			if(cellHeader==null) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			String headCol = (String) itr.next();
			if (headCol.equalsIgnoreCase("Biography") || headCol.equalsIgnoreCase("Other Related Companies")) {
				sheet.setColumnWidth(cellIndex, 25000);
			}else{
				sheet.setColumnWidth(cellIndex, 10000);
			}

			cellHeader.setCellValue(headCol);
			ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIndex = cellIndex+1;
		}
	}

	public List<OwnershipEmployeeJobDTO> getPeopleHoldings(String companyId, String personId) {
		return ownershipStructureDao.getPeopleHoldings(companyId,personId);
	}

}
