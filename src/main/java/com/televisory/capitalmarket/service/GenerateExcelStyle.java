package com.televisory.capitalmarket.service;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.televisory.capitalmarket.util.StyleUtil;

public class GenerateExcelStyle {
	
	static Logger _log = Logger.getLogger(GenerateExcelStyle.class);

	HSSFWorkbook workbook;
	
	
	public GenerateExcelStyle(HSSFWorkbook workbook) {
		
		Map<String, CellStyle> mapStyle =  StyleUtil.getAllStyles(workbook);		
		this.workbook = workbook;
		HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER = mapStyle.get("HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER");
		HEADER2_WITH_BORDER_ALL_CENTER = mapStyle.get("HEADER2_WITH_BORDER_ALL_CENTER");
		HEADER2_WITH_BORDER_RIGHT_TOP_BOTTOM_CENTER = mapStyle.get("HEADER2_WITH_BORDER_RIGHT_TOP_BOTTOM_CENTER");
		BLACK_TEXT_GRAY_BACKGROUND_WITH_BORDER_LEFT_RIGHT_CENTER= mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_WITH_BORDER_LEFT_RIGHT_CENTER");
		BLACK_TEXT_GRAY_BACKGROUND_WITH_BORDER_LEFT_RIGHT_TOP_CENTER = mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_WITH_BORDER_LEFT_RIGHT_TOP_CENTER");
		BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_TOP = mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_TOP");
		BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT = mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT");
		BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM = mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM");
		BLACK_TEXT_RIGHT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM = mapStyle.get("BLACK_TEXT_RIGHT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM");
		BLACK_TEXT_RIGHT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT = mapStyle.get("BLACK_TEXT_RIGHT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT");
		HEADER2_WITH_BORDER_LEFT_RIGHT = mapStyle.get("HEADER2_WITH_BORDER_LEFT_RIGHT");
		DARK_BLUE_BACKGROUND = mapStyle.get("DARK_BLUE_BACKGROUND");
		BORDER_TOP = mapStyle.get("BORDER_TOP");
		BORDER_TOP_LEFT_BLUE_FONT = mapStyle.get("BORDER_TOP_LEFT_BLUE_FONT");
		BORDER_TOP_RIGHT = mapStyle.get("BORDER_TOP_RIGHT");
		BORDER_LEFT_RIGHT = mapStyle.get("BORDER_LEFT_RIGHT");
		BORDER_BOTTOM_LEFT_RIGHT = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT");
		HEADER2_WITH_BORDER_TOP_BOTTOM_CENTER = mapStyle.get("HEADER2_WITH_BORDER_TOP_BOTTOM_CENTER");
		BORDER_LEFT_BLUE_FONT = mapStyle.get("BORDER_LEFT_BLUE_FONT");
		BORDER_RIGHT = mapStyle.get("BORDER_RIGHT");
		BORDER_BUTTOM_LEFT_BLUE_FONT = mapStyle.get("BORDER_BUTTOM_LEFT_BLUE_FONT");
		BORDER_BUTTOM_RIGHT = mapStyle.get("BORDER_BUTTOM_RIGHT");
		BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER = mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER");
		HEADER1_WITH_BORDER_LEFT_RIGHT_TOP = mapStyle.get("HEADER1_WITH_BORDER_LEFT_RIGHT_TOP");
		BLUE_FONT = mapStyle.get("BLUE_FONT");
		BLUE_FONT_WITHOUT_BACKGROUND = mapStyle.get("BLUE_FONT_WITHOUT_BACKGROUND");
		HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND = mapStyle.get("HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND");
		HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER = mapStyle.get("HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER");
		BORDER_ALL = mapStyle.get("BORDER_ALL");
		BORDER_LEFT = mapStyle.get("BORDER_LEFT");
		BORDER_BUTTOM_LEFT = mapStyle.get("BORDER_BUTTOM_LEFT");
		BORDER_TOP_LEFT = mapStyle.get("BORDER_TOP_LEFT");
		BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT");
		BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT");
		BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL");
		BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL");
		BORDER_LEFT_RIGHT_ALIGN_RIGHT = mapStyle.get("BORDER_LEFT_RIGHT_ALIGN_RIGHT");
		BORDER_LEFT_RIGHT_ALIGN_LEFT = mapStyle.get("BORDER_LEFT_RIGHT_ALIGN_LEFT");
		BORDER_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL = mapStyle.get("BORDER_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL");
		BORDER_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL = mapStyle.get("BORDER_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL");
		BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL");
		BORDER_RIGHT_NUMBER_FORMAT_TWO_DECIMAL =  mapStyle.get("BORDER_RIGHT_NUMBER_FORMAT_TWO_DECIMAL");
		BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL =  mapStyle.get("BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL");
		HEADER_WHITE_TEXT_BLACK_BACKGROUND =  mapStyle.get("HEADER_WHITE_TEXT_BLACK_BACKGROUND");
		BORDER_LEFT_RIGHT_BOLD_FONT =  mapStyle.get("BORDER_LEFT_RIGHT_BOLD_FONT");
		BORDER_BOTTOM_LEFT_RIGHT_BOLD_FONT = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_BOLD_FONT");
		BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL= mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL");
		BORDER_RIGHT_NUMBER_FORMAT_ONE_DECIMAL= mapStyle.get("BORDER_RIGHT_NUMBER_FORMAT_ONE_DECIMAL");
		BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL= mapStyle.get("BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL");
		BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL= mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL");
		BORDER_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL= mapStyle.get("BORDER_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL");
		BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL= mapStyle.get("BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL");
		BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER = mapStyle.get("BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER");
		BORDER_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL =  mapStyle.get("BORDER_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL");
		BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL");
		BOLD_FONT_HEADER = mapStyle.get("BOLD_FONT_HEADER");
		BORDER_BOTTOM_LEFT_RIGHT_TOP_ALIGN_RIGHT = mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_TOP_ALIGN_RIGHT");
		BORDER_LEFT_RIGHT_TOP_ALIGN_RIGHT = mapStyle.get("BORDER_LEFT_RIGHT_TOP_ALIGN_RIGHT");
		BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL_1= mapStyle.get("BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL_1");
	}
	
	public void setBackgroundOnSheet(HSSFSheet sheet,int minrows, int maxrows, int cols) {

		CellStyle cellStyle = BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER;
		sheet.setDisplayGridlines(false);
		sheet.setHorizontallyCenter(true);
		Row row;
		Cell cell;
		for (int i = minrows; i < maxrows; i++) {
			row = sheet.createRow(i);
			for (int j = 0; j < cols; j++) {
				if (j > 0) {
					sheet.setColumnWidth(j, 30 * 256);
				}
				cell = row.createCell(j);
				cell.setCellStyle(cellStyle);
			}

		}
	}
	
	public HSSFColor setColor(byte r, byte g, byte b) {
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		try {
			hssfColor = palette.findColor(r, g, b);
			if (hssfColor == null) {
				palette.setColorAtIndex(HSSFColor.WHITE.index, r, g, b);
				hssfColor = palette.getColor(HSSFColor.WHITE.index);
			}
		} catch (Exception e) {
			_log.error(e);
		}

		return hssfColor;
	}
	
	
	public void setAutoWidth(int sheet) {

		for (int colNum = 0; colNum < 12; colNum++){
			workbook.getSheetAt(sheet).autoSizeColumn(colNum);
		}
			
	}
	
	public void setAutoWidth(int sheet, int columnStart, int columnEnd) {

		for (int colNum = columnStart; colNum <= columnEnd; colNum++)
			workbook.getSheetAt(sheet).autoSizeColumn(colNum);
	}

	public void mergeCells(HSSFSheet sheet, int rowFrom, int rowTo, int colFrom, int colTo,boolean border) throws IOException {
		if(rowTo>=rowFrom && colTo>=colFrom){
			CellRangeAddress cellMerge =new CellRangeAddress(rowFrom, rowTo, colFrom, colTo);
			int result = sheet.addMergedRegion(cellMerge);
			if (border) {
		        setBordersToMergedCells(sheet, cellMerge);
		    }
		}
	}
	
	protected void setBordersToMergedCells(Sheet sheet, CellRangeAddress rangeAddress) {
	    RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	    RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	    RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	    RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	}
	
	public CellStyle HEADER2_WITH_BORDER_ALL_CENTER;
	public CellStyle HEADER2_WITH_BORDER_RIGHT_TOP_BOTTOM_CENTER;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_WITH_BORDER_LEFT_RIGHT_CENTER;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_WITH_BORDER_LEFT_RIGHT_TOP_CENTER;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_TOP;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM;
	public CellStyle BLACK_TEXT_RIGHT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM;
	public CellStyle BLACK_TEXT_RIGHT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT;
	public CellStyle HEADER2_WITH_BORDER_LEFT_RIGHT;
	public CellStyle DARK_BLUE_BACKGROUND;
	public CellStyle BORDER_TOP;
	public CellStyle BORDER_TOP_LEFT_BLUE_FONT;
	public CellStyle BORDER_TOP_RIGHT;
	public CellStyle HEADER2_WITH_BORDER_TOP_BOTTOM_CENTER;
	public CellStyle BORDER_LEFT_BLUE_FONT;
	public CellStyle BORDER_RIGHT;
	public CellStyle BORDER_BUTTOM_LEFT_BLUE_FONT;
	public CellStyle BORDER_BUTTOM_RIGHT;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER;
	public CellStyle HEADER1_WITH_BORDER_LEFT_RIGHT_TOP;
	public CellStyle BLUE_FONT;
	public CellStyle BLUE_FONT_WITHOUT_BACKGROUND;
	public CellStyle HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND;
	public CellStyle HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER;
	public CellStyle HEADER_WHITE_TEXT_BLACK_BACKGROUND;
	public CellStyle BORDER_ALL;
	public CellStyle BORDER_LEFT;
	public CellStyle BORDER_BUTTOM_LEFT;
	public CellStyle BORDER_TOP_LEFT;
	public CellStyle BORDER_LEFT_RIGHT;
	public CellStyle BORDER_LEFT_RIGHT_BOLD_FONT;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_BOLD_FONT;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL;
	public CellStyle BORDER_LEFT_RIGHT_ALIGN_LEFT;
	public CellStyle BORDER_LEFT_RIGHT_ALIGN_RIGHT;
	public CellStyle BORDER_LEFT_RIGHT_ALIGN_LEFT_LARGE_CELL;
	public CellStyle BORDER_LEFT_RIGHT_ALIGN_RIGHT_LARGE_CELL;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
	public CellStyle BORDER_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
	public CellStyle BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
	public CellStyle BORDER_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
	public CellStyle BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
	public CellStyle BORDER_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
	public CellStyle BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
	public CellStyle BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER;
	public CellStyle HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WHITE_BORDER;
	public CellStyle BORDER_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL;
	public CellStyle BOLD_FONT_HEADER;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_TOP_ALIGN_RIGHT;
	public CellStyle BORDER_LEFT_RIGHT_TOP_ALIGN_RIGHT;
	public CellStyle BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL_1;
	
	
	public void mergeCellsRightBorder(HSSFSheet sheet, int rowFrom, int rowTo, int colFrom, int colTo,boolean border,boolean isLastRecord) throws IOException {
		if(rowTo>=rowFrom && colTo>=colFrom){
			CellRangeAddress cellMerge =new CellRangeAddress(rowFrom, rowTo, colFrom, colTo);
			int result = sheet.addMergedRegion(cellMerge);
			if (border) {
		        setRightBordersToMergedCells(sheet, cellMerge,isLastRecord);
		    }
		}
	}
	
	protected void setRightBordersToMergedCells(Sheet sheet, CellRangeAddress rangeAddress,boolean isLastRecord) {
	    //RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	  if(isLastRecord) {
	   RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	  }
	    RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	  //  RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, rangeAddress, sheet,workbook);
	}
	
	
}


