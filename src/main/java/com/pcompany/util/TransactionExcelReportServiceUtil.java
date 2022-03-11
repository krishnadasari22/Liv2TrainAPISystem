package com.pcompany.util;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

public class TransactionExcelReportServiceUtil {
	
	private static final Logger _log = Logger.getLogger(TransactionExcelReportServiceUtil.class);

	//private constructor
	private TransactionExcelReportServiceUtil() { 
		
	}

	public static void createHeader(HSSFSheet sheet, int startingHeaderRow,
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

	public static void setColumnValue(int cellIdxDebt, int headerDebt,
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
	
	public static void setColumnValue(int cellIdxDebt, int headerDebt, Integer value,
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
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL_1);
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

	public static void setColumnValue(int cellIdxDebt, int headerDebt, String value,
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
	
	public static void createSheetHeading(int headerDebt, String name,String value,HSSFSheet sheet, GenerateExcelStyle ges) {
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
	

	public static void createLogo(Cell cell, HSSFSheet sheet,
			HSSFWorkbook workbook, GenerateExcelStyle ges, ExcelDesignService excelDesignService, String logoPath) {
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
