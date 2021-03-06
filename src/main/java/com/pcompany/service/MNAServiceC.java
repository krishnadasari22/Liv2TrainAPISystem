package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pcompany.model.MNAPriceEpsPerformanceModel;
import com.privatecompany.dao.MNADaoC;
import com.privatecompany.dto.CompanyEpsDTO;
import com.privatecompany.dto.DealHistoryDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.CompanyStockPriceResponseDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.service.CMStockService;
import com.televisory.capitalmarket.service.CapitalMarketService;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DozerHelper;

@Service
public class MNAServiceC {

	Logger _log = Logger.getLogger(MNAServiceC.class);

	@Autowired
	CMStockService cmStockService;

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Autowired
	MNADaoC mnaDaoC;

	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	public MNAPriceEpsPerformanceModel getPriceEpsPerformance(MNAPriceEpsPerformanceModel mnaPriceEpsPerformance) {

		String currency = mnaPriceEpsPerformance.getCurrency();

		List<StockPriceDTO> stockPriceData = null;
		List<CompanyStockPriceResponseDTO> stockPriceResponseDTO = null;

		if (currency != null && currency.equals("")) {
			currency = null;
		}

		stockPriceData = cmStockService.getFactSetCompanyStockPrice(mnaPriceEpsPerformance.getCompanyId(),
				CMStatic.DAILY, mnaPriceEpsPerformance.getStartDate(), mnaPriceEpsPerformance.getEndDate(), currency);

		stockPriceResponseDTO = DozerHelper.map(dozerBeanMapper, stockPriceData, CompanyStockPriceResponseDTO.class);

		mnaPriceEpsPerformance.setStockPriceList(stockPriceResponseDTO);

		List<DealHistoryDTO> dealHistoryData = getAllDealHistory(mnaPriceEpsPerformance.getEntityId(),
				mnaPriceEpsPerformance.getStartDate(), mnaPriceEpsPerformance.getEndDate(), currency);

		mnaPriceEpsPerformance.setDealHistoryList(dealHistoryData);

		List<CompanyEpsDTO> epsData = getCompanyEps(mnaPriceEpsPerformance.getCompanyId(),
				mnaPriceEpsPerformance.getEpsPeriodicity(), mnaPriceEpsPerformance.getStartDate(),
				mnaPriceEpsPerformance.getEndDate(), currency);

		mnaPriceEpsPerformance.setEpsList(epsData);

		return mnaPriceEpsPerformance;

	}

	public List<DealHistoryDTO> getAllDealHistory(String entityId, Date startDate, Date endDate, String currency) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Date date = null;

		if (startDate == null) {
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate = date;
		}

		if (endDate == null) {
			endDate = new Date();
		}

		return mnaDaoC.getAllDealHistory(entityId, startDate, endDate, currency);
	}

	public List<CompanyEpsDTO> getCompanyEps(String companyId, String periodicity, Date startDate, Date endDate,
			String currency) {

		_log.info("getting eps from database");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Date date = null;

		if (startDate == null) {
			try {
				date = formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startDate = date;
		}

		if (endDate == null) {
			endDate = new Date();
		}

		return mnaDaoC.getEpsData(companyId, null, startDate, endDate, currency);
	}

	public HSSFWorkbook createDealHistroryExcel(List<DealHistoryDTO> dealHistoryData) throws Exception {

		try {
			String sheetName = CMStatic.DEAL_HISTORY_SHEET_NAME;

			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges = new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet(sheetName);

			sheet.setDisplayGridlines(false);

			_log.info("creating Deal history sheet");

			int rowdesign = 0;

			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (null == r) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}

			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);

			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);

			// write company country industry name;

			int currentRowIndex = rowdesign + 4;

			Row dealHeaderRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
					: sheet.createRow(currentRowIndex);

			Cell entitycell = dealHeaderRow.getCell(1) != null ? dealHeaderRow.getCell(1) : dealHeaderRow.createCell(1);

			Cell entitycellVal = dealHeaderRow.getCell(2) != null ? dealHeaderRow.getCell(2)
					: dealHeaderRow.createCell(2);

			entitycell.setCellValue("Entity");

			entitycell.setCellStyle(ges.BOLD_FONT_HEADER);

			if (null != dealHistoryData.get(0))
				entitycellVal.setCellValue(dealHistoryData.get(0).getEntity_name());

			currentRowIndex++;

			Row dealHeaderRowCountry = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
					: sheet.createRow(currentRowIndex);

			Cell countrycell = dealHeaderRowCountry.getCell(1) != null ? dealHeaderRowCountry.getCell(1)
					: dealHeaderRowCountry.createCell(1);

			Cell countrycellVal = dealHeaderRowCountry.getCell(2) != null ? dealHeaderRowCountry.getCell(2)
					: dealHeaderRowCountry.createCell(2);

			countrycell.setCellValue("Country");

			countrycell.setCellStyle(ges.BOLD_FONT_HEADER);

			if (null != dealHistoryData.get(0))
				countrycellVal.setCellValue(dealHistoryData.get(0).getEntity_country_name());

			currentRowIndex++;

			Row dealHeaderRowIndustry = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
					: sheet.createRow(currentRowIndex);

			Cell industryCell = dealHeaderRowIndustry.getCell(1) != null ? dealHeaderRowIndustry.getCell(1)
					: dealHeaderRowIndustry.createCell(1);

			Cell industryCellVal = dealHeaderRowIndustry.getCell(2) != null ? dealHeaderRowIndustry.getCell(2)
					: dealHeaderRowIndustry.createCell(2);

			industryCell.setCellValue("Industry");

			industryCell.setCellStyle(ges.BOLD_FONT_HEADER);
			if (dealHistoryData.get(0) != null)
				industryCellVal.setCellValue(dealHistoryData.get(0).getEntity_industry_name());

			currentRowIndex = currentRowIndex + 2;

			// Create headers

			Row headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
					: sheet.createRow(currentRowIndex);
			int tempIndex = 1;

			Cell headerCell;

			for (String headerText : CMStatic.DEAL_HISTORY_HEADER) {
				headerCell = headerRow.getCell(tempIndex) != null ? headerRow.getCell(tempIndex)
						: headerRow.createCell(tempIndex);
				if (headerText.contains("CURRENCY"))
					headerText = headerText.replace("CURRENCY", dealHistoryData.get(0).getDealCurrency());

				headerCell.setCellValue(headerText);

				headerCell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER);
				if (CMStatic.DEAL_HISTORY_HEADER_COLUMN_WIDTH_INDEX.contains(tempIndex)) {
					sheet.setColumnWidth(tempIndex, 6000);
				} else {
					sheet.setColumnWidth(tempIndex, 10000);
				}
				ges.mergeCells(sheet, currentRowIndex, currentRowIndex + 2, tempIndex, tempIndex, false);
				tempIndex++;
			}

			currentRowIndex = currentRowIndex + 3;

			// Fill data in table
			Row currentRow;
			Cell currentCell;
			int i = 0;
			for (DealHistoryDTO dealHistoryDTO : dealHistoryData) {
				boolean isLastRecord = false;

				if (i == dealHistoryData.size() - 1) {
					isLastRecord = true;
				}

				int currentCellIndex = 1;
				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getAnnounceDate(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getTarget(), sheet, currentCellIndex, ges, isLastRecord,
						"LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getRole(), sheet, currentCellIndex, ges, isLastRecord,
						"LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getAcquirer(), sheet, currentCellIndex, ges, isLastRecord,
						"LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getSeller(), sheet, currentCellIndex, ges, isLastRecord,
						"LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getCloseDateOrStatus(), sheet, currentCellIndex, ges,
						isLastRecord, "LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getTransactionValue(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getDealType(), sheet, currentCellIndex, ges, isLastRecord,
						"LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getTargetIndustry(), sheet, currentCellIndex, ges,
						isLastRecord, "LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getPercentSought(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getPercent_owned(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getMop(), sheet, currentCellIndex, ges, isLastRecord,
						"LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getSource_funds_desc(), sheet, currentCellIndex, ges,
						isLastRecord, "LEFT");

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getEv(), sheet, currentCellIndex, ges, isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getRevenue_ltm_before_deal(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getEbitda_ltm_before_deal(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getPrice_share(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getStock_price_share(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getCash_price_share(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getOne_day_prem(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getEv_rev(), sheet, currentCellIndex, ges, isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getEv_ebitda(), sheet, currentCellIndex, ges, isLastRecord);

				currentCellIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);
				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);
				setColumnValue(currentCell, dealHistoryDTO.getCash_adjusted_deal_value(), sheet, currentCellIndex, ges,
						isLastRecord);

				currentRowIndex++;

				i++;
			}

			return workbook;
		} catch (Exception e) {
			_log.error("error occured in downloading deal history excel ::" + e.getLocalizedMessage());
			e.printStackTrace();
			throw e;
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

	private void setColumnValue(Cell cell, String data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges,
			Boolean isLastRecord, String Align) {
		if (data != null) {

			cell.setCellValue(data);
			// cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
			if (Align.equalsIgnoreCase("LEFT")) {
				if (isLastRecord) {
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
				} else {
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
				}
			} else {
				if (isLastRecord) {
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
				} else {
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if (isLastRecord) {
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			} else {
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}

		}

	}

	private void setColumnValue(Cell cell, Double value, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ge,
			Boolean isLastRecord) {
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
			// cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			if (isLastRecord) {
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			} else {
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}

	}

	private void setColumnValue(Cell cell, Date data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges,
			Boolean isLastRecord) {
		if (data != null) {
			DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String period = outputFormatter.format(data);
			cell.setCellValue(period);
			// cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
		} else {
			cell.setCellValue(CMStatic.NA);
			// cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
		}
		if (isLastRecord) {
			cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
		} else {
			cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
		}
	}

	public HSSFWorkbook createEpsStockPerformanceExcel(MNAPriceEpsPerformanceModel performanceData) {

		HSSFWorkbook workbook = new HSSFWorkbook();

		GenerateExcelStyle ges = new GenerateExcelStyle(workbook);

		Row r;

		try {

			String sheetName = CMStatic.DEAL_HISTORY_SHEET_NAME;

			HSSFSheet sheet = workbook.createSheet(sheetName);

			sheet.setDisplayGridlines(false);

			_log.info("creating Deal history sheet");

			int rowdesign = 0;

			r = workbook.getSheet(sheetName).getRow(rowdesign);

			if (null == r) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}

			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 6000);

			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);

			Comparator<String> sortComparator = (s1, s2) -> {
				return s1.compareTo(s2);
			};

			TreeMap<String, String> periodMap = new TreeMap<>(sortComparator);

			Map<String, CompanyStockPriceResponseDTO> stockPriceMap = new HashMap<>();
			Map<String, CompanyEpsDTO> epsDataMap = new HashMap<>();
			Map<String, DealHistoryDTO> dealHistoryDataMap = new HashMap<>();

			DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");

			performanceData.getStockPriceList().forEach(stockPriceData -> {
				periodMap.put(outputFormatter.format(stockPriceData.getDate()), "");
				stockPriceMap.put(outputFormatter.format(stockPriceData.getDate()), stockPriceData);
			});

			performanceData.getEpsList().forEach(epsPriceData -> {
				periodMap.put(outputFormatter.format(epsPriceData.getDate()), "");
				epsDataMap.put(outputFormatter.format(epsPriceData.getDate()), epsPriceData);
			});

			performanceData.getDealHistoryList().forEach(dealHistoryData -> {
				periodMap.put(outputFormatter.format(dealHistoryData.getAnnounceDate()), "");
				dealHistoryDataMap.put(outputFormatter.format(dealHistoryData.getAnnounceDate()), dealHistoryData);
			});

			createEpsStockExcelIntro(sheet, performanceData, ges, periodMap);
			createEpsStockExcelHeader(sheet, performanceData, ges);

			fillEpsPricePerformanceSheetData(periodMap, stockPriceMap, epsDataMap, dealHistoryDataMap, sheet, ges);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return workbook;
	}

	private void fillEpsPricePerformanceSheetData(Map<String, String> periodMap,
			Map<String, CompanyStockPriceResponseDTO> stockPriceMap, Map<String, CompanyEpsDTO> epsDataMap,
			Map<String, DealHistoryDTO> dealHistoryDataMap, HSSFSheet sheet, GenerateExcelStyle ges) {

		int currentRowIndex = 12;
		int currentCellIndex = 1;
		int currentIteration = 0;
		int periodMapSize = periodMap.size() - 1;
		boolean isLastRecord = false;
		Row currentRow;
		Cell currentCell;
		Double blankValue = null;
		Double currentEpsValue = null;

		String[] dateArray = new String[periodMapSize];

		String[] periodList = periodMap.keySet().toArray(dateArray);

		for (int i = 0; i < periodMapSize; i++) {

			if (currentIteration == periodMapSize) {
				isLastRecord = true;
			}

			currentCellIndex = 1;

			currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
					: sheet.createRow(currentRowIndex);

			currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
					: currentRow.createCell(currentCellIndex);

			setColumnValue(currentCell, periodList[i], sheet, currentCellIndex, ges, isLastRecord, "LEFT");

			currentCellIndex++;

			currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
					: currentRow.createCell(currentCellIndex);

			if (stockPriceMap.get(periodList[i]) != null) {

				setColumnValue(currentCell, stockPriceMap.get(periodList[i]).getClose(), sheet, currentCellIndex, ges,
						isLastRecord);
			} else {
				setColumnValue(currentCell, blankValue, sheet, currentCellIndex, ges, isLastRecord);
			}

			currentCellIndex++;

			currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
					: currentRow.createCell(currentCellIndex);

			if (epsDataMap.get(periodList[i]) != null) {
				currentEpsValue = epsDataMap.get(periodList[i]).getEpsData();
				setColumnValue(currentCell, epsDataMap.get(periodList[i]).getEpsData(), sheet, currentCellIndex, ges,
						isLastRecord);
			} else {
				setColumnValue(currentCell, currentEpsValue, sheet, currentCellIndex, ges, isLastRecord);
			}

			currentCellIndex++;

			currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
					: currentRow.createCell(currentCellIndex);

			if (dealHistoryDataMap.get(periodList[i]) != null) {

				setColumnValue(currentCell, dealHistoryDataMap.get(periodList[i]).getAcquirer(), sheet,
						currentCellIndex, ges, isLastRecord, "LEFT");

				currentCellIndex++;

				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);

				setColumnValue(currentCell, dealHistoryDataMap.get(periodList[i]).getTarget(), sheet, currentCellIndex,
						ges, isLastRecord, "LEFT");

				currentCellIndex++;

				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);

				setColumnValue(currentCell, dealHistoryDataMap.get(periodList[i]).getTransactionValue(), sheet,
						currentCellIndex, ges, isLastRecord);

			} else {
				setColumnValue(currentCell, blankValue, sheet, currentCellIndex, ges, isLastRecord);

				currentCellIndex++;

				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);

				setColumnValue(currentCell, blankValue, sheet, currentCellIndex, ges, isLastRecord);

				currentCellIndex++;

				currentCell = currentRow.getCell(currentCellIndex) != null ? currentRow.getCell(currentCellIndex)
						: currentRow.createCell(currentCellIndex);

				setColumnValue(currentCell, blankValue, sheet, currentCellIndex, ges, isLastRecord);
			}

			currentRowIndex++;
		}
	}

	private void createEpsStockExcelIntro(HSSFSheet sheet, MNAPriceEpsPerformanceModel performanceData,
			GenerateExcelStyle ges, TreeMap<String, String> periodMap) {

		CompanyDTO companyInfo = capitalMarketService.getCMCompaniesById(performanceData.getCompanyId());

		int currentRowIndex = 4;

		Row entityHeaderRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		Cell entityNamecell = entityHeaderRow.getCell(1) != null ? entityHeaderRow.getCell(1)
				: entityHeaderRow.createCell(1);

		Cell entityNamecellVal = entityHeaderRow.getCell(2) != null ? entityHeaderRow.getCell(2)
				: entityHeaderRow.createCell(2);

		entityNamecell.setCellValue("Entity");

		entityNamecell.setCellStyle(ges.BOLD_FONT_HEADER);

		currentRowIndex++;

		Row countryHeaderRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		Cell countryNamecell = countryHeaderRow.getCell(1) != null ? countryHeaderRow.getCell(1)
				: countryHeaderRow.createCell(1);

		Cell countryNamecellValue = countryHeaderRow.getCell(2) != null ? countryHeaderRow.getCell(2)
				: countryHeaderRow.createCell(2);

		countryNamecell.setCellValue("Country");

		countryNamecell.setCellStyle(ges.BOLD_FONT_HEADER);

		currentRowIndex++;

		Row industryHeaderRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		Cell industryNamecell = industryHeaderRow.getCell(1) != null ? industryHeaderRow.getCell(1)
				: industryHeaderRow.createCell(1);

		Cell industryNamecellValue = industryHeaderRow.getCell(2) != null ? industryHeaderRow.getCell(2)
				: industryHeaderRow.createCell(2);

		industryNamecell.setCellValue("Industry");

		industryNamecell.setCellStyle(ges.BOLD_FONT_HEADER);

		if (companyInfo != null) {
			entityNamecellVal.setCellValue(companyInfo.getName());
			countryNamecellValue.setCellValue(companyInfo.getCountryName());
			industryNamecellValue.setCellValue(companyInfo.getTicsIndustryName());
		} else {
			entityNamecellVal.setCellValue("NA");
			countryNamecellValue.setCellValue("NA");
			industryNamecellValue.setCellValue("NA");
		}

		currentRowIndex++;

		Row fromDateHeaderRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		Cell fromDatecell = fromDateHeaderRow.getCell(1) != null ? fromDateHeaderRow.getCell(1)
				: fromDateHeaderRow.createCell(1);

		Cell fromDatecellValue = fromDateHeaderRow.getCell(2) != null ? fromDateHeaderRow.getCell(2)
				: fromDateHeaderRow.createCell(2);

		fromDatecell.setCellValue("From Date");

		fromDatecell.setCellStyle(ges.BOLD_FONT_HEADER);

		if (performanceData.getStartDate() != null) {
			fromDatecellValue.setCellValue(performanceData.getStartDate());
		} else {

			fromDatecellValue.setCellValue(periodMap.firstKey());
		}

		currentRowIndex++;

		Row endDateHeaderRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		Cell endDatecell = endDateHeaderRow.getCell(1) != null ? endDateHeaderRow.getCell(1)
				: endDateHeaderRow.createCell(1);

		Cell endDatecellValue = endDateHeaderRow.getCell(2) != null ? endDateHeaderRow.getCell(2)
				: endDateHeaderRow.createCell(2);

		endDatecell.setCellValue("End Date");

		endDatecell.setCellStyle(ges.BOLD_FONT_HEADER);

		if (performanceData.getEndDate() != null) {
			endDatecellValue.setCellValue(performanceData.getEndDate());
		} else {
			endDatecellValue.setCellValue(periodMap.lastKey());
		}

	}

	private void createEpsStockExcelHeader(HSSFSheet sheet, MNAPriceEpsPerformanceModel performanceData,
			GenerateExcelStyle ges) {

		int currentRowIndex = 10;

		int currentColumnIndex = 1;

		// Create headers
		Row headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		String cellValue;

		try {

			cellValue = "Date";
			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Close Price (" + performanceData.getCurrency() + ")";
			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "TTM EPS (" + performanceData.getCurrency() + ")";
			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Acquirer";
			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Target";
			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Transactional Value (" + performanceData.getCurrency() + ")";
			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setHeaderCell(HSSFSheet sheet, Row headerRow, int currentColumnIndex, int currentRowIndex,
			String cellValue, GenerateExcelStyle ges) throws IOException {

		Cell headerCell = headerRow.getCell(currentColumnIndex) != null ? headerRow.getCell(currentColumnIndex)
				: headerRow.createCell(currentColumnIndex);

		headerCell.setCellValue(cellValue);

		headerCell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER);

		sheet.setColumnWidth(currentColumnIndex, 10000);

		ges.mergeCells(sheet, currentRowIndex, currentRowIndex + 2, currentColumnIndex, currentColumnIndex, false);

	}

}
