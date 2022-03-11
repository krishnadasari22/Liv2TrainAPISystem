package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.pcompany.dao.DebtStructureDao;
import com.pcompany.dto.DebtCapitalStructureDetailsDTO;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.ExcelReportService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

/**
 * 
 * @author varun chawla
 *
 */
@Service
public class DebtCapitalStructureService {
	
	Logger _log = Logger.getLogger(DebtCapitalStructureService.class);
	
	@Autowired
	DebtStructureDao debtStructureDao;
	
	@Autowired
	ExcelReportService excelService;
	
	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	public List<DebtCapitalStructureDetailsDTO> getFactsetEntityDebt(String entityId) {
		return debtStructureDao.getFactsetEntityDebt(entityId);
	}
	
	public List<DebtCapitalStructureDetailsDTO> getFactsetEntityDebtDownload(String entityId) {
		return debtStructureDao.getFactsetEntityDebtDownload(entityId);
	}

	public List<DebtCapitalStructureDetailsDTO> getBorrowingLimit(String entityId) {
		return debtStructureDao.getBorrowingLimit(entityId);
	}

	public List<DebtCapitalStructureDetailsDTO> getCapitalCharges(String entityId) {
		return debtStructureDao.getCapitalCharges(entityId);
	}

	public HSSFWorkbook createExcelReport(List<DebtCapitalStructureDetailsDTO> debtData, List<DebtCapitalStructureDetailsDTO> borrowingData, String entityId, String companyName) throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet("Debt-Structure");
			//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			//ges.setBackgroundOnSheet(sheet, 0, (debtData.size()*2)+50,CMStatic.SHEET_MAX_COLUMN);
			sheet.setDisplayGridlines(false);
			createAndFillSheet(workbook,debtData,borrowingData, "Debt-Structure",ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}
	
	public void createAndFillSheet(HSSFWorkbook workbook,List<DebtCapitalStructureDetailsDTO> debtData, List<DebtCapitalStructureDetailsDTO> borrowingData, String sheetName,GenerateExcelStyle ges, String companyName) throws JsonProcessingException {
		
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
			
			int headerDebt = 7;
			
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
			try {
				List<String> debtHeader = new ArrayList<>();
				
				for (int i = 0; i < CMStatic.DEBT_HEADER.size(); i++) {
					if (CMStatic.DEBT_HEADER.get(i).toUpperCase().indexOf(CMStatic.UNIT_MILLION) >= 0)
						debtHeader.add(CMStatic.DEBT_HEADER.get(i).replace("Million", debtData.get(0).getCurrency() +" Million"));
					else
						debtHeader.add(CMStatic.DEBT_HEADER.get(i));
				}
				createHeader(workbook.getSheet(sheetName), headerDebt, ges, "Debt Details", debtHeader);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			/*int headerBorrowing = headerDebt + debtData.size() + 6;
			createHeader(workbook.getSheet(sheetName), headerBorrowing, ges, "Borrowing Limit", CMStatic.DEBT_BORROWING_HEADER);
			
			int headerCapital = headerBorrowing + borrowingData.size() + 6;
			createHeader(workbook.getSheet(sheetName), headerCapital, ges, "Capital Charges", CMStatic.DEBT_CAPITAL_HEADER);*/
			
			Boolean isLastRecord = false;
			int countCapital =0;
			for(int i=0;i<debtData.size();i++){
				DebtCapitalStructureDetailsDTO debtDetail = debtData.get(i);
				if(i==debtData.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				if(rowDebt==null) {
					rowDebt=sheet.createRow(headerDebt);
				}
				//Row rowBorrowing = sheet.getRow(headerBorrowing);
				//Row rowCapital = sheet.getRow(headerCapital);
				
				int cellIdxDebt = 1;
				//int cellIdxBorrow =1;
				int cellIdxCapital =1;
				
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//Cell cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				//Cell cellCapital = rowCapital.getCell(cellIdxCapital);
				
				
				/*Setting Debt Value Start*/
				setColumnValue(cellDebt,debtDetail.getInstrumentType(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getInstrumentDescription(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getIssueDate(),sheet,cellIdxDebt,ges,isLastRecord,"Right");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getMaturityDate(),sheet,cellIdxDebt,ges,isLastRecord,"Right");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getCouponType(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getCouponIndex(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getCouponRateMin(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getCouponRateMax(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getCouponSpread(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getIssueCurrency(),sheet,cellIdxDebt,ges,isLastRecord,"RIGHT");
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getIssueAmount(),sheet,cellIdxDebt,ges,isLastRecord);
				
				cellIdxDebt = cellIdxDebt+1;
				cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				setColumnValue(cellDebt,debtDetail.getOutstandingAmount(),sheet,cellIdxDebt,ges,isLastRecord);
				/*Setting Debt Value End*/
				
				/*Setting Borrowing Limit Start*/
				/*setColumnValue(cellBorrow,debtDetail.getInstrumentDescription(),sheet,cellIdxBorrow,ges,isLastRecord,"LEFT");
				
				cellIdxBorrow = cellIdxBorrow+1;
				cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				setColumnValue(cellBorrow,debtDetail.getIssueCurrency(),sheet,cellIdxBorrow,ges,isLastRecord,"RIGHT");
				
				cellIdxBorrow = cellIdxBorrow+1;
				cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				setColumnValue(cellBorrow,debtDetail.getIssueAmount(),sheet,cellIdxBorrow,ges,isLastRecord);
				
				cellIdxBorrow = cellIdxBorrow+1;
				cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				setColumnValue(cellBorrow,debtDetail.getOutstandingAmount(),sheet,cellIdxBorrow,ges,isLastRecord);
				
				cellIdxBorrow = cellIdxBorrow+1;
				cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				setColumnValue(cellBorrow,debtDetail.getAvailAmount(),sheet,cellIdxBorrow,ges,isLastRecord);*/
				/*Setting Borrowing Limit End*/
				
				/*Setting Capital Charges Start*/
				/*if(debtDetail.getCollateralType()!=null || debtDetail.getAssetDesc()!=null){
					setColumnValue(cellCapital,debtDetail.getCollateralType(),sheet,cellIdxCapital,ges,isLastRecord,"LEFT");

					cellIdxCapital = cellIdxCapital+1;
					cellCapital = rowCapital.getCell(cellIdxCapital);
					setColumnValue(cellCapital,debtDetail.getAssetDesc(),sheet,cellIdxCapital,ges,isLastRecord,"RIGHT");
					headerCapital++;
					countCapital++;
				}*/
				/*Setting Capital Charges  End*/
				
				headerDebt++;
				//headerBorrowing++;
				
			}
			/*if(countCapital==0){
				Row row = sheet.getRow(headerCapital);
				Cell cellCapital = row.getCell(1);
				ges.mergeCells(sheet, headerCapital, headerCapital, 1, 2, true);
				cellCapital.setCellValue("No Data Available");
				cellCapital.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
			}else{
				Row row = sheet.getRow(headerCapital-1);
				Cell cellCapital1 = row.getCell(1);
				Cell cellCapital2 = row.getCell(2);
				cellCapital1.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
				cellCapital2.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
			}*/
			isLastRecord = false;
			/*for(int i=0;i<borrowingData.size();i++){
				DebtCapitalStructureDetailsDTO borrowDetail = borrowingData.get(i);
				if(i==borrowingData.size()-1){
					isLastRecord = true;
				}
				Row rowBorrowing = sheet.getRow(headerBorrowing);
				int cellIdxBorrow =1;
				Cell cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				setColumnValue(cellBorrow,borrowDetail.getInstrumentType(),sheet,cellIdxBorrow,ges,isLastRecord,"LEFT");
				
				cellIdxBorrow = cellIdxBorrow+1;
				cellBorrow = rowBorrowing.getCell(cellIdxBorrow);
				setColumnValue(cellBorrow,borrowDetail.getOutstandingAmount(),sheet,cellIdxBorrow,ges,isLastRecord);
				headerBorrowing++;
			}*/

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
	
	private void setColumnValue(Cell cell, Double value, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ge,Boolean isLastRecord) {
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
	
	private void createHeader(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		if(sectionDesc==null) {
			sectionDesc=sheet.createRow(startingHeaderRow-3);
		}
		Cell sectionCell = sectionDesc.getCell(1);
		if(sectionCell==null) {
			sectionCell=sectionDesc.createCell(1);
		}
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		_log.info("header "+header +" startingHeaderRow "+startingHeaderRow);
		
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
			
			if(null==cellHeader) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			
			if(headCol.indexOf("Coupon Rate") >= 0){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+1, true);
				cellHeader.setCellValue(headCol);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row headerMinMax =  sheet.getRow(row+1);
				if(headerMinMax==null) {
					headerMinMax=sheet.createRow(row+1);
				}
				Cell cellMinMax = headerMinMax.getCell(cellIndex);
				
				if(null==cellMinMax) {
					cellMinMax=headerMinMax.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, 5000);
				cellMinMax.setCellValue("Min");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				if(cellMax==null) {
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

	public List<DebtCapitalStructureDetailsDTO> getEntityMaturity(String entityId) {
		return debtStructureDao.getEntityMaturity(entityId);
	}
}
