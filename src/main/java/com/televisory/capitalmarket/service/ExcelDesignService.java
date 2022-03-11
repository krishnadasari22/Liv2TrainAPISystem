package com.televisory.capitalmarket.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


@SuppressWarnings("deprecation")
@Service
public class ExcelDesignService {

	Logger _log = Logger.getLogger(ExcelDesignService.class);
/*
	private HSSFWorkbook hssfWorkbook;*/
	private Workbook workbook;

	private File file;

	public void readWorkBook(Workbook workbook) {

		this.workbook = workbook;

	}

	public Workbook readSheet(File file) throws IOException {

		this.file = file;

		_log.info("In the service to READ THE Sheet for file " + this.file);

		FileInputStream fileRead = new FileInputStream(file);
		workbook = new XSSFWorkbook(fileRead);

		return workbook;

	}

	public void applyDesign(Workbook hssfWorkbook) throws IOException {

		Sheet sheet = hssfWorkbook.getSheetAt(0);

		hssfWorkbook.getSheetAt(0);

		_log.info("The sheet is " + sheet.getSheetName());

		/* CellReference cellReference = new CellReference("B2"); */

		// RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, region, sheet, hssfWorkbook);

		/*
		 * int row = cellReference.getRow(); int col = cellReference.getCol();
		 */

		_log.info("Perform all the operations here related to a sheet ");
		// B2 to E2

		mergeCells(sheet, 1, 3, 1, 5);
		setImage(sheet, 1, 1, "/data/rtda//1.jpg", 200 ,200);

		saveWorkbook(new File("/data/rtda/" + new Date() + ".xls"));

	}

	public void setBorderLeft(Sheet sheet, int row, int col) {

		Row r = sheet.getRow(row);

		if (r != null) {

			_log.info("Applying the border setBorderLeft on " + row + " :: " + col + "  for sheet " + sheet.getSheetName());

			Cell cell = sheet.getRow(row).getCell(col);

		    CellStyle style = workbook.createCellStyle();

			style.setBorderLeft(HSSFCellStyle.BORDER_THICK);

			cell.setCellStyle(style);

		} else {

			_log.info("The row is blank");

		}

	}

	public void setBorderRight(Sheet sheet, int row, int col) {

		Row r = sheet.getRow(row);

		// HSSFCell cella = hssfWorkbook.createSheet().createRow(row).createCell(col);

		if (r != null) {

			_log.info("Applying the border setBorderRight on " + row + " :: " + col + "  for sheet " + sheet.getSheetName());

			Cell cell = sheet.getRow(row).getCell(col);

		   CellStyle style = workbook.createCellStyle();

			style.setBorderRight(HSSFCellStyle.BORDER_THICK);

			cell.setCellStyle(style);

		} else {

			_log.error("The row is blank");

		}

	}

	/*public void setBorderTop(HSSFSheet sheet, int row, int col, GenerateComparisonExcel ge) {

		Row r = sheet.getRow(row);

		if (r != null) {

			_log.info("Applying the border setBorderTop on " + row + " :: " + col + "  for sheet " + sheet.getSheetName());

			Cell cell = sheet.getRow(row).getCell(col);
			if(cell!=null){
				CellStyle style = ge.getMapStyles().get("BLACK_TEXT_GRAY_BACKGROUND_BORDER_TOP");
				cell.setCellStyle(style);
			}
		} else {

			_log.info("The row is blank");

		}

	}*/

	public void setBorderBottom(Sheet sheet, int row, int col) {

		Row r = sheet.getRow(row);

		if (r != null) {

			_log.info("Applying the border setBorderBottom on " + row + " :: " + col + "  for sheet " + sheet.getSheetName());

			Cell cell = sheet.getRow(row).getCell(col);

			if(cell!=null){
				CellStyle style = workbook.createCellStyle();

				style.setBorderBottom(HSSFCellStyle.BORDER_THICK);

				cell.setCellStyle(style);
			}
		} else {

			_log.info("The row is blank");

		}

	}

	public void setBorderForAllSide(Sheet sheet, int row, int col) {

		Row r = sheet.getRow(row);

		if (r != null) {

			_log.info("Applying the border setBorderForAllSide on " + row + " :: " + col + "  for sheet " + sheet.getSheetName());

			Cell cell = sheet.getRow(row).getCell(col);

			CellStyle style = workbook.createCellStyle();

			style.setBorderTop(HSSFCellStyle.BORDER_THICK);
			style.setBorderBottom(HSSFCellStyle.BORDER_THICK);
			style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
			style.setBorderRight(HSSFCellStyle.BORDER_THICK);
			cell.setCellStyle(style);

		} else {

			_log.info("The row is blank");

		}

	}

	public void setColorBlue(Sheet sheet, int row, int col) {

		_log.info("Apllying the blue color");

		Row r = sheet.getRow(row);

		if (r != null) {

			Cell cell = sheet.getRow(row).getCell(col);

			CellStyle style = workbook.createCellStyle();

			Font font = this.workbook.createFont();

			font.setColor(IndexedColors.BLUE.getIndex());

			style.setFont(font);

			cell.setCellStyle(style);

		} else {

			_log.info("The row is blank");

		}

	}

	/*public void setFontBold(HSSFSheet sheet, int row, int col) {

		_log.info("Apllying the setFontBold");

		Row r = sheet.getRow(row);

		if (r != null) {

			Cell cell = sheet.getRow(row).getCell(col);

			HSSFCellStyle style = hssfWorkbook.createCellStyle();

			HSSFFont font = hssfWorkbook.createFont();

			font.setBold(true);

			style.setFont(font);

			cell.setCellStyle(style);

		} else {

			_log.info("The row is blank");

		}

	}*/

	public void setFontUnderlined(Sheet sheet, int row, int col, HSSFFont font) {

		_log.info("Apllying the setFontUnderlined : )");

		Row r = sheet.getRow(row);

		if (r != null) {

			Cell cell = sheet.getRow(row).getCell(col);

			CellStyle style = workbook.createCellStyle();

			font.setUnderline(HSSFFont.U_DOUBLE);

			style.setFont(font);

			cell.setCellStyle(style);

		} else {

			_log.info("The row is blank");

		}

	}

	public void mergeCells(Sheet sheet, int rowFrom, int rowTo, int colFrom, int colTo) throws IOException {

		_log.info("Apllying the mergeCells rowFrom :: " + rowFrom + " :: rowTo :: " + rowTo + " :: colFrom :: " + colFrom + " ::  colTo :: " + colTo);

		int result = sheet.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));

		_log.info("finished the mergeCells: " + result);

	}
	
	public void wrapText(HSSFWorkbook wb, HSSFSheet sheet, int row, int col){
		CellStyle style = wb.createCellStyle(); 
        style.setWrapText(true);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        Cell cell = sheet.getRow(row).getCell(col);
        cell.setCellStyle(style); 
	}

	public void setImage(Sheet sheet, int row, int col, String path, int dx1, int dx2) throws IOException {

		_log.info("Adding image ");
		InputStream inputStream = new FileInputStream(path);
		byte[] imageBytes = IOUtils.toByteArray(inputStream);
		inputStream.close();
		int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();

		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
		 anchor.setCol1(1); //Column B
		   anchor.setRow1(0); //Row 3
		   anchor.setCol2(2); //Column C
		   anchor.setRow2(1); //Row 4

	/*	anchor.setDx1(dx1);
		anchor.setDx2(dx2);
		anchor.setDy1(50);*/
//		anchor.setDy2(20);

		Picture pict = drawing.createPicture(anchor, pictureureIdx);
		//pict.resize(3);
	}

	public void saveWorkbook(File outFile) throws IOException {

		FileOutputStream out = new FileOutputStream(outFile);
		workbook.write(out);
		_log.info("Image added :)");
		out.close();

	}

	public void setCellWidthHeight(HSSFSheet sheet, int rowNo, int colNo, int height, int width){
		Row row = null;
		if (sheet.getRow(rowNo) == null) {
			row = sheet.createRow(rowNo);
		} else {
			row = sheet.getRow(rowNo);
		}
		sheet.setColumnWidth(colNo, width);
		row.setHeight((short)height);
	}
}
