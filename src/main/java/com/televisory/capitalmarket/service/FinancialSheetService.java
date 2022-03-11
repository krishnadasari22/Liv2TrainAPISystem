package com.televisory.capitalmarket.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.model.EquityRequest;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class FinancialSheetService {

	static Logger _log = Logger.getLogger(FinancialSheetService.class);

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	private ExcelDesignService excelDesignService;
	
	 static String currency_=null;
	
	 static String unit_=null;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	public void createAndFillFinancialSheets(DownloadRequest downloadRequest, Workbook workbook, ExcelStyle ges)
			throws JsonProcessingException {
		    currency_=null;
		    unit_=null;
		   _log.info("creating the financial sheet data ");
		try {

			Map<String, Set<String>> industryCompanyIdMap = getIndustryCompanyIDMap(downloadRequest.getEquity());

			Set<String> distinctIndustries = industryCompanyIdMap.keySet();

			_log.info("Creating financial sheet for: " + distinctIndustries);
			for (String industry : distinctIndustries) {

				String sheetName = "Financial Of " + industry;

				if (distinctIndustries.size() == 1 && industryCompanyIdMap.get(industry).size() == 1)
					sheetName = capitalMarketService
							.getCMCompaniesById(industryCompanyIdMap.get(industry).iterator().next()).getName();

				// remove spcial Characters
				sheetName = sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, "");
				// check and trim length
				sheetName = sheetName.substring(0,
						Math.min(sheetName.length(), CMStatic.MAX_FINANCIL_SHEET_NAME_LENGTH));

				createAndFillSheets(sheetName, industry, industryCompanyIdMap.get(industry), downloadRequest, workbook,
						ges);
			}

			/*
			 * for (Map.Entry<String, Set<String>> entry :
			 * industryCompanyIdMap.entrySet()) {
			 * 
			 * String sheetName = "Financial Of " +
			 * entry.getKey().replaceAll(CMStatic.SPECIAL_CHAR_REGEX, "");
			 * 
			 * if (industryCompanyIdMap.keySet().size() == 1 &&
			 * entry.getValue().size() == 1) sheetName =
			 * entry.getValue().iterator().next().replaceAll(CMStatic.
			 * SPECIAL_CHAR_REGEX, "");
			 * 
			 * createAndFillSheets( sheetName.substring(0,
			 * Math.min(sheetName.length(),
			 * CMStatic.MAX_FINANCIL_SHEET_NAME_LENGTH)), entry.getKey(),
			 * entry.getValue(), downloadRequest, workbook, ges); }
			 */

		} catch (Exception e) {
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	// this code will only work for private enttity data download from company
	// page.
	// for buld data download code need to update like private company
	public void createAndFillFinancialSheetsPrivateEntity(DownloadRequest downloadRequest, Workbook workbook,
			ExcelStyle ges) throws JsonProcessingException {

		_log.info("creating the financial sheet data for private Entity");

		try {
			// start of temp code for company private entity download
			Map<String, Set<String>> hmap = new HashMap<>();
			Set<String> industrial = new HashSet<String>();
			for (Iterator<EquityRequest> equityRequests = downloadRequest.getEquity().iterator(); equityRequests
					.hasNext();) {

				EquityRequest equityRequest = equityRequests.next();

				if (equityRequest.getEntityType() == null
						|| CMStatic.ENTITY_TYPE_PUBLIC.equalsIgnoreCase(equityRequest.getEntityType())
						|| CMStatic.DATA_TYPE_STOCK_PRICE.equalsIgnoreCase(equityRequest.getDataType())
						|| CMStatic.DATA_TYPE_BETA.equalsIgnoreCase(equityRequest.getDataType()))
					continue;

				industrial.add(equityRequest.getCompanyFinancialDTOs().get(0).getCompanyId());

			}

			if (industrial.size() > 0) {
				hmap.put(CMStatic.INDUSTRY_IND, industrial);
				// End of temp code for company private entity download

				Map<String, Set<String>> industryCompanyIdMap = hmap;

				Set<String> distinctIndustries = industryCompanyIdMap.keySet();
				for (String industry : distinctIndustries) {
					String sheetName = "Financial Of " + industry;
					if (distinctIndustries.size() == 1 && industryCompanyIdMap.get(industry).size() == 1)
						sheetName = capitalMarketService
								.getPCCompaniesById(industryCompanyIdMap.get(industry).iterator().next()).getName();
					// remove spcial Characters
					sheetName = sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, "");
					// check and trim length
					sheetName = sheetName.substring(0,
							Math.min(sheetName.length(), CMStatic.MAX_FINANCIL_SHEET_NAME_LENGTH));

					createAndFillSheets(sheetName, industry, industryCompanyIdMap.get(industry), downloadRequest,
							workbook, ges);
				}
			}
		} catch (Exception e) {
			Log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}
	}

	private void createAndFillSheets(String sheetName, String financialSheetIndustry, Set<String> companyIds,
			DownloadRequest downloadRequest, Workbook workbook, ExcelStyle ges) throws Exception {

		_log.info("filling the sheet: '" + sheetName + "', for industry: " + financialSheetIndustry + " and company: "
				+ companyIds);

		try {

			// sheetName=sheetName.replaceAll(CMStatic.COMPANY_REMOVE_SPECIAL_CHAR_REGEX,
			// "");

			Sheet sheet = workbook.createSheet(sheetName);

			_log.info("After Creating Sheet");

			/*
			 * ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW +
			 * CMStatic.SHEET_ADD_ROW, CMStatic.SHEET_MAX_COLUMN);
			 */

			_log.info("Inserting data in excel");
			sheet.setDisplayGridlines(false);

			createMetaData(CMStatic.FINANCIAL_DATA_STARTING_ROW, CMStatic.FINANCIAL_DATA_STARTING_COLUMN,
					financialSheetIndustry, companyIds, downloadRequest, workbook, sheetName, ges);

		} catch (Exception e) {
			throw new Exception();
		}
	}

	private void createMetaData(int startingindexRow, int startingIndexCol, String financialSheetIndustry,
			Set<String> companyIds, DownloadRequest downloadRequest, Workbook workbook, String sheetName,
			ExcelStyle ges) {

		try {

			Map<String, Integer> pnlColumnMap = new LinkedHashMap<>();
			Map<String, Integer> pnlRowMap = new LinkedHashMap<>();
			Map<String, Integer> bsColumnMap = new LinkedHashMap<>();
			Map<String, Integer> bsRowMap = new LinkedHashMap<>();
			Map<String, Integer> cfColumnMap = new LinkedHashMap<>();
			Map<String, Integer> cfRowMap = new LinkedHashMap<>();
			Map<String, Integer> frColumnMap = new LinkedHashMap<>();
			Map<String, Integer> frRowMap = new LinkedHashMap<>();
			Map<String, Integer> vrColumnMap = new LinkedHashMap<>();
			Map<String, Integer> vrRowMap = new LinkedHashMap<>();

			Set<String> pnlRowSet = new LinkedHashSet<>();
			Set<String> pnlColumnSet = new LinkedHashSet<>();

			Set<String> bsRowSet = new LinkedHashSet<>();
			Set<String> bsColumnSet = new LinkedHashSet<>();

			Set<String> cfRowSet = new LinkedHashSet<>();
			Set<String> cfColumnSet = new LinkedHashSet<>();

			Set<String> frRowSet = new LinkedHashSet<>();
			Set<String> frColumnSet = new LinkedHashSet<>();

			Set<String> vrRowSet = new LinkedHashSet<>();
			Set<String> vrColumnSet = new LinkedHashSet<>();

			List<EquityRequest> pnlEquityRequest = new ArrayList<>();
			List<EquityRequest> bsEquityRequest = new ArrayList<>();
			List<EquityRequest> cfEquityRequest = new ArrayList<>();
			List<EquityRequest> frEquityRequest = new ArrayList<>();
			List<EquityRequest> vrEquityRequest = new ArrayList<>();

			SortedSet<String> periodSet = new TreeSet<>();

			// To get the data of a particular indicator
			Map<String, CompanyFinancialMINDTO> sheetDataMap = new HashMap<>();

			// _log.info(downloadRequest.getEquity());

			downloadRequest.getEquity().forEach(equityRequest -> {

				try {
					String companyId = equityRequest.getCompanyFinancialDTOs().get(0).getCompanyId();

					if (companyIds.contains(companyId)) {

						Integer currentObjectIndex = 0;

						List<CompanyFinancialMINDTO> companyFinancialDTOs = equityRequest.getCompanyFinancialDTOs();

						Collections.sort(companyFinancialDTOs,
								Comparator.comparing(CompanyFinancialMINDTO::getPeriod).thenComparing(
										Comparator.comparing(CompanyFinancialMINDTO::getDisplayOrder, (s1, s2) -> {
											Integer firstNum = Integer.parseInt(s1);
											Integer secondNum = Integer.parseInt(s2);
											return firstNum.compareTo(secondNum);
										})));

						/*
						 * companyFinancialDTOs.forEach(i -> { _log.info(i); });
						 */

						for (CompanyFinancialMINDTO companyFinancialDTO : companyFinancialDTOs) {

							DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
							String period = outputFormatter.format(companyFinancialDTO.getPeriod());
							// String companyName =
							// companyFinancialDTO.getCompanyName();
							String displayOrder = companyFinancialDTO.getDisplayOrder();
							String itemName = companyFinancialDTO.getItemName();
							String fieldName = companyFinancialDTO.getFieldName();
							periodSet.add(period);

							if (equityRequest.getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_PNL)) {

								if (currentObjectIndex == 0) {
									pnlEquityRequest.add(equityRequest);
								}

								if (companyFinancialDTO.getSection() != null
										&& companyFinancialDTO.getSection() != "") {
									pnlRowSet.add(companyFinancialDTO.getSection() + "-section");
								}

								sheetDataMap.put(
										CMStatic.DATA_TYPE_PNL + "-" + fieldName + "-" + companyId + "-" + period,
										companyFinancialDTO);
								pnlRowSet.add(companyFinancialDTO.getFieldName());
								pnlColumnSet.add(companyFinancialDTO.getCompanyId() + "-" + period);

							} else if (equityRequest.getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_BALANCE_SHEET)) {

								if (currentObjectIndex == 0) {
									bsEquityRequest.add(equityRequest);
								}

								if (companyFinancialDTO.getSection() != null
										&& companyFinancialDTO.getSection() != "") {
									bsRowSet.add(companyFinancialDTO.getSection() + "-section");
								}

								sheetDataMap.put(CMStatic.DATA_TYPE_BALANCE_SHEET + "-" + fieldName + "-" + companyId
										+ "-" + period, companyFinancialDTO);

								bsRowSet.add(companyFinancialDTO.getFieldName());
								bsColumnSet.add(companyFinancialDTO.getCompanyId() + "-" + period);

							} else if (equityRequest.getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_CASH_FLOW)) {

								if (currentObjectIndex == 0) {
									cfEquityRequest.add(equityRequest);
								}

								if (companyFinancialDTO.getSection() != null
										&& companyFinancialDTO.getSection() != "") {
									cfRowSet.add(companyFinancialDTO.getSection() + "-section");
								}

								sheetDataMap.put(
										CMStatic.DATA_TYPE_CASH_FLOW + "-" + fieldName + "-" + companyId + "-" + period,
										companyFinancialDTO);
								cfRowSet.add(companyFinancialDTO.getFieldName());
								cfColumnSet.add(companyFinancialDTO.getCompanyId() + "-" + period);

							} else if (equityRequest.getDataType()
									.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {

								if (currentObjectIndex == 0) {
									frEquityRequest.add(equityRequest);
								}

								if (companyFinancialDTO.getSection() != null
										&& companyFinancialDTO.getSection() != "") {
									frRowSet.add(companyFinancialDTO.getSection() + "-section");
								}
								
								String unitkey = CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE + "-" + fieldName + "-"
										+ CMStatic.FR_UNIT_CODE;
								if(!sheetDataMap.containsKey(unitkey)){
									sheetDataMap.put(unitkey, companyFinancialDTO);	
								}
								frColumnSet.add(CMStatic.FR_UNIT_CODE);

								sheetDataMap.put(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE + "-" + fieldName + "-"
										+ companyId + "-" + period, companyFinancialDTO);
								frRowSet.add(companyFinancialDTO.getFieldName());
								frColumnSet.add(companyFinancialDTO.getCompanyId() + "-" + period);

							} else if (equityRequest.getDataType()
									.equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE)) {

								if (currentObjectIndex == 0) {
									vrEquityRequest.add(equityRequest);
								}

								if (companyFinancialDTO.getSection() != null
										&& companyFinancialDTO.getSection() != "") {
									vrRowSet.add(companyFinancialDTO.getSection() + "-section");
								}
								
								String unitkey = CMStatic.DATA_TYPE_VALUATION_RATIO_CODE + "-" + fieldName + "-"
										+ CMStatic.VR_UNIT_CODE;
								if(!sheetDataMap.containsKey(unitkey)){
									sheetDataMap.put(unitkey, companyFinancialDTO);	
								}
								vrColumnSet.add(CMStatic.VR_UNIT_CODE);

								sheetDataMap.put(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE + "-" + fieldName + "-"
										+ companyId + "-" + period, companyFinancialDTO);
								vrRowSet.add(companyFinancialDTO.getFieldName());
								vrColumnSet.add(companyFinancialDTO.getCompanyId() + "-" + period);

							}
							currentObjectIndex++;
						}
					}
				} catch (Exception e) {
					_log.info("Something went wrong");
				}
			});

			// create the televisory logo
			int rowdesign = 0;

			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (r == null) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			if (null == celldesign) {
				celldesign = r.createCell(1);
			}
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);

			// creating the pnlrowmap
			Iterator<String> itrRowPnl = pnlRowSet.iterator();

			int headerPnl = startingindexRow;

			while (itrRowPnl.hasNext()) {
				pnlRowMap.put(itrRowPnl.next(), startingindexRow);
				startingindexRow++;
			}

			// creating the pnlcolumnmap
			Iterator<String> itrColPnl = pnlColumnSet.iterator();
			int pnlStartingColIndex = startingIndexCol;
			while (itrColPnl.hasNext()) {
				pnlColumnMap.put(itrColPnl.next(), pnlStartingColIndex);
				pnlStartingColIndex++;
			}

			if (!pnlEquityRequest.isEmpty()) {
				fillFinancialData(pnlRowMap, pnlColumnMap, sheetDataMap, companyIds, pnlEquityRequest, workbook,
						sheetName, ges, headerPnl, periodSet, pnlStartingColIndex, startingindexRow);
			}

			if (!bsEquityRequest.isEmpty()) {

				// creating the balance sheet map
				int headerBs = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;
				startingindexRow = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;

				Iterator<String> itrRowBs = bsRowSet.iterator();

				while (itrRowBs.hasNext()) {
					bsRowMap.put(itrRowBs.next(), startingindexRow);
					startingindexRow++;
				}

				int bsStartingColIndex = startingIndexCol;

				Iterator<String> itrColBs = bsColumnSet.iterator();

				while (itrColBs.hasNext()) {
					bsColumnMap.put(itrColBs.next(), bsStartingColIndex);
					bsStartingColIndex++;
				}

				fillFinancialData(bsRowMap, bsColumnMap, sheetDataMap, companyIds, bsEquityRequest, workbook, sheetName,
						ges, headerBs, periodSet, bsStartingColIndex, startingindexRow);
			}

			if (!cfEquityRequest.isEmpty()) {

				// creating the cashflow map
				int headerCf = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;
				startingindexRow = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;
				Iterator<String> itrRowCf = cfRowSet.iterator();

				while (itrRowCf.hasNext()) {
					cfRowMap.put(itrRowCf.next(), startingindexRow);
					startingindexRow++;
				}

				int cfStartingColIndex = startingIndexCol;

				Iterator<String> itrColCf = cfColumnSet.iterator();

				while (itrColCf.hasNext()) {
					cfColumnMap.put(itrColCf.next(), cfStartingColIndex);
					cfStartingColIndex++;
				}

				fillFinancialData(cfRowMap, cfColumnMap, sheetDataMap, companyIds, cfEquityRequest, workbook, sheetName,
						ges, headerCf, periodSet, cfStartingColIndex, startingindexRow);
			}

			if (!frEquityRequest.isEmpty()) {

				// creating the fr map
				Iterator<String> itrRowFr = frRowSet.iterator();

				int headerFr = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;
				startingindexRow = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;

				while (itrRowFr.hasNext()) {
					frRowMap.put(itrRowFr.next(), startingindexRow);
					startingindexRow++;
				}

				int frStartingColIndex = startingIndexCol;

				Iterator<String> itrColFr = frColumnSet.iterator();

				while (itrColFr.hasNext()) {
					frColumnMap.put(itrColFr.next(), frStartingColIndex);
					frStartingColIndex++;
				}

				fillFinancialData(frRowMap, frColumnMap, sheetDataMap, companyIds, frEquityRequest, workbook, sheetName,
						ges, headerFr, periodSet, frStartingColIndex, startingindexRow);
			}

			if (!vrEquityRequest.isEmpty()) {

				// creating the vr map
				int headerVr = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;
				startingindexRow = startingindexRow + CMStatic.ROW_SHEET_GAP + CMStatic.FINANCIAL_DATA_HEADER_SPACE;

				Iterator<String> itrRowVr = vrRowSet.iterator();

				while (itrRowVr.hasNext()) {
					vrRowMap.put(itrRowVr.next(), startingindexRow);
					startingindexRow++;
				}

				int vrStartingColIndex = startingIndexCol;
				Iterator<String> itrColVr = vrColumnSet.iterator();

				while (itrColVr.hasNext()) {
					vrColumnMap.put(itrColVr.next(), vrStartingColIndex);
					vrStartingColIndex++;
				}

				fillFinancialData(vrRowMap, vrColumnMap, sheetDataMap, companyIds, vrEquityRequest, workbook, sheetName,
						ges, headerVr, periodSet, vrStartingColIndex, startingindexRow);
			}

		} catch (Exception e) {
			Log.error("error occured in creating the financial metadata" + e.getMessage());
		}

	}

	private String getCurrencyUnit(String currency, String unit) {
		String currUnit = "";
		if(currency!=null && !currency.equals("")){
			currUnit+=currency;
		}
		if(unit!=null && !unit.equals("")){
			currUnit+=" "+unit;
		}
		return currUnit.trim();
	}

	private void fillFinancialData(Map<String, Integer> tableRowMap, Map<String, Integer> tableColumnMap,
			Map<String, CompanyFinancialMINDTO> sheetDataMap, Set<String> companyIds,
			List<EquityRequest> equityRequests, Workbook workbook, String sheetName, ExcelStyle ges,
			int headerStartingrow, SortedSet<String> periodSet, int endIndexCol, int endIndexRow) {
		try {

			DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Sheet sheet = workbook.getSheet(sheetName);
			Row currency = sheet.getRow(4);
			if (null == currency) {
				currency = sheet.createRow(4);
			}
			Row unit = sheet.getRow(5);
			if (null == unit) {
				unit = sheet.createRow(5);
			}
			Cell currencyCell = currency.getCell(1);
			if (null == currencyCell) {
				currencyCell = currency.createCell(1);
			}
			Cell unitCell = unit.getCell(1);
			if (null == unitCell) {
				unitCell = unit.createCell(1);
			}
			Cell currencyCellData = currency.getCell(2);
			if (null == currencyCellData) {
				currencyCellData = currency.createCell(2);
			}
			Cell unitCellData = unit.getCell(2);
			if (null == unitCellData) {
				unitCellData = unit.createCell(2);
			}
			currencyCell.setCellValue("Currency");
			unitCell.setCellValue("Unit");
			currency_=currency_!=null && !currency_.equals("")?currency_:equityRequests.get(0).getCompanyFinancialDTOs().get(0).getCurrency();
			currencyCellData.setCellValue(currency_);
			unit_=unit_!=null && !unit_.equals("")?unit_:equityRequests.get(0).getCompanyFinancialDTOs().get(0).getUnit();
			unitCellData.setCellValue(unit_);
			currencyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			unitCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			unitCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			currencyCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);

			try {
				createHeader(equityRequests, sheet, headerStartingrow, ges, tableColumnMap, periodSet, endIndexCol,
						endIndexRow);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//int counter = 0;

			String tableType = equityRequests.get(0).getDataType();

			Integer currentRow = null;

			tableColumnMap.forEach((columnKey, columnNumber) -> {
				AtomicInteger counter = new AtomicInteger(0);
				tableRowMap.forEach((rowKey, rowNumber) -> {

					try {

						Row row = sheet.getRow(rowNumber);

						if (null == row) {
							row = sheet.createRow(rowNumber);
						}

						String dataKey = tableType + "-" + rowKey + "-" + columnKey;
						CompanyFinancialMINDTO companyDataObject = sheetDataMap.get(dataKey);
						
						if (counter.get() == 0) {

							Cell rationNameCell = row.getCell(CMStatic.FINANCIAL_KEY_COL_NUM);
							
							if(rationNameCell==null){
								rationNameCell = row.createCell(CMStatic.FINANCIAL_KEY_COL_NUM);	
							}

							if (rowKey.contains("section")) {
								rationNameCell.setCellValue(rowKey.split("-")[0]);
							} else if (rationNameCell.getStringCellValue()
									.equalsIgnoreCase("")) {
								rationNameCell
										.setCellValue(companyDataObject.getItemName());
							}

							if (rowNumber == endIndexRow - 1) {
								rationNameCell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT);
							} else {

								if (rowKey.contains("section")) {
									rationNameCell
											.setCellStyle(ges.BORDER_LEFT_RIGHT_BOLD_FONT);
								} else {
									rationNameCell.setCellStyle(ges.BORDER_LEFT);
								}
							}
						}

						if (companyDataObject != null) {

							//String period = outputFormatter.format(companyDataObject.getPeriod());

							if (columnNumber != 1) {
								sheet.setColumnWidth(columnNumber, 5000);
							}

							org.apache.poi.ss.usermodel.Cell cell = row.getCell(columnNumber);

							// if cell is not created then create the cell
							if (cell == null) {
								cell = row.createCell(columnNumber);
							}
							
							if(dataKey.contains(CMStatic.FR_UNIT_CODE) || dataKey.contains(CMStatic.VR_UNIT_CODE)){
								sheet.setColumnWidth(columnNumber, 5000);
								cell.setCellValue(getCurrencyUnit(companyDataObject.getCurrency(), companyDataObject.getUnit()));
								cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
							}else{
								if (companyDataObject.getData() != null) {
									sheet.setColumnWidth(columnNumber, 5000);
									cell.setCellValue(companyDataObject.getData());
									cell.setCellStyle(getNumberFormatStyle(companyDataObject.getData(), "", ges, false));
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								} else {
									sheet.setColumnWidth(columnNumber, 5000);
									cell.setCellValue(CMStatic.NA);
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
							}

						}else { //create blank cell with left right border
							org.apache.poi.ss.usermodel.Cell cell = row.getCell(columnNumber);

							// if cell is not created then create the cell
							if (cell == null) {
								cell = row.createCell(columnNumber);
							}
							
							sheet.setColumnWidth(columnNumber, 5000);
							cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
						}

					} catch (Exception e) {

						// if no data is available for that period in the
						// database then fill empty data and apply styles in the
						// cell
						Row row = sheet.getRow(rowNumber);

						if (null == row) {
							row = sheet.createRow(rowNumber);
						}

						org.apache.poi.ss.usermodel.Cell cell = row.getCell(columnNumber);

						if (null == cell) {
							cell = row.createCell(columnNumber);
						}

						sheet.setColumnWidth(columnNumber, 5000);
						cell.setCellValue(CMStatic.NA);
						cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);

					}
				});
				counter.incrementAndGet();
			});

			createBoundry(sheet, endIndexCol, endIndexRow, ges);
		} catch (Exception e) {
			Log.error("error occured in filling the financial data" + e.getMessage());
		}

	}

	private void createBoundry(Sheet sheet, int endIndexCol, int endIndexRow, ExcelStyle ges) {

		Row row = sheet.getRow(endIndexRow - 1);

		for (int i = CMStatic.FINANCIAL_DATA_STARTING_COLUMN; i < endIndexCol; i++) {
			Cell cell = row.getCell(i);
			cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL);
		}

	}

	private void createHeader(List<EquityRequest> equityRequests, Sheet sheet, int startingHeaderRow, ExcelStyle ges,
			Map<String, Integer> financialColumnMap, SortedSet<String> periodSet, int endIndexCol, int endIndexRow)
			throws IOException {

		/*
		 * filling the first cell like balancesheet PNL etc
		 */
		int cellIndex = 1;
		String heading = "";
		Row r;
		Cell cell;

		Map<String, Integer> companyNameMap = new LinkedHashMap<>();

		int row = startingHeaderRow - 2;

		if (equityRequests.get(0).getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_BALANCE_SHEET)) {
			heading = CMStatic.BALANCE_SHEET;
		} else if (equityRequests.get(0).getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_PNL)) {
			heading = CMStatic.INCOME_STATEMENT;
		} else if (equityRequests.get(0).getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_CASH_FLOW)) {
			heading = CMStatic.CASH_FLOW;
		} else if (equityRequests.get(0).getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE)) {
			heading = CMStatic.VALUATION_RATIO;
		} else if (equityRequests.get(0).getDataType().equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {
			heading = CMStatic.FINANCIAL_RATIO;
		}

		r = sheet.getRow(row - 2);
		if (null == r) {
			r = sheet.createRow(row - 2);
		}
		cell = r.getCell(cellIndex);

		if (cell == null) {
			cell = r.createCell(cellIndex);
		}
		if (cellIndex != 1) {
			sheet.setColumnWidth(cellIndex, 5000);
		}

		ges.mergeCells(sheet, row - 2, row + 1, cellIndex, cellIndex, true);
		cell.setCellValue(heading);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);

		Row headerRow = sheet.getRow(row);

		if (null == headerRow) {
			headerRow = sheet.createRow(row);
		}
		
		/**Add unit column in FR and VR***/
		if(heading.equalsIgnoreCase(CMStatic.VALUATION_RATIO) || heading.equalsIgnoreCase(CMStatic.FINANCIAL_RATIO)){
			String unitKey = null;
			if(heading.equalsIgnoreCase(CMStatic.VALUATION_RATIO)){
				unitKey = CMStatic.VR_UNIT_CODE;
			}else if(heading.equalsIgnoreCase(CMStatic.FINANCIAL_RATIO)){
				unitKey = CMStatic.FR_UNIT_CODE;
			}
			if(financialColumnMap.get(unitKey)!=null){
				int unitColNum = financialColumnMap.get(unitKey) ;
				
				Cell unitCol = headerRow.getCell(unitColNum);

				if (unitCol == null) {
					unitCol = headerRow.createCell(unitColNum);
				}
				if (unitColNum != 1) {
					sheet.setColumnWidth(unitColNum, 5000);
				}

				ges.mergeCells(sheet, row, row + 1, unitColNum, unitColNum, true);
				unitCol.setCellValue("Unit");
				unitCol.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);				
			}
		}

		for (EquityRequest eq : equityRequests) {

			Iterator itr = periodSet.iterator();

			while (itr.hasNext()) {

				String period = (String) itr.next();

				String key = eq.getCompanyFinancialDTOs().get(0).getCompanyId() + "-" + period;

				if (financialColumnMap.get(key) != null) {

					int colNum = financialColumnMap.get(key);

					if (colNum != 1) {
						sheet.setColumnWidth(colNum, 5000);
					}

					if (colNum != 0) {

						if (companyNameMap.containsKey(eq.getName())) {
							int count = companyNameMap.get(eq.getName());
							companyNameMap.put(eq.getName(), count + 1);
						} else {
							companyNameMap.put(eq.getName(), 1);
						}
						ges.mergeCells(sheet, row, row + 1, colNum, colNum, true);

						Cell cellHeader = headerRow.getCell(colNum);
						if (null == cellHeader) {
							cellHeader = headerRow.createCell(colNum);
						}

						DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
						DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");

						try {
							period = outputFormatter.format(inputFormatter.parse(period));
						} catch (ParseException e) {
							_log.warn("problem occured in convertnig date format for: " + equityRequests);
						}

						if (eq.getPeriodicity().equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
							cellHeader.setCellValue(CMStatic.QUARTER_END + "\n" + period);
							cellHeader.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
						} else if (eq.getPeriodicity().equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY)) {
							cellHeader.setCellValue(CMStatic.YEAR_END + "\n" + period);
							cellHeader.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
						} else {
							cellHeader.setCellValue(CMStatic.HALF_YEAR_END + "\n" + period);

							cellHeader.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
						}

					}
				}
			}
		}

		/*
		 * write the company name in header
		 */
		int startingColIndex = CMStatic.FINANCIAL_DATA_STARTING_COLUMN;
		int index = CMStatic.FINANCIAL_DATA_STARTING_COLUMN;
		for (Map.Entry<String, Integer> enrty : companyNameMap.entrySet()) {
			try {
				int endColIndex = index + enrty.getValue();
				//sheet.addMergedRegion(new CellRangeAddress(row - 2,row - 1,startingColIndex,endColIndex)); 
				if(heading.equalsIgnoreCase(CMStatic.VALUATION_RATIO) || heading.equalsIgnoreCase(CMStatic.FINANCIAL_RATIO)){
				ges.mergeCells(sheet, row - 2, row - 1, startingColIndex, endColIndex, true);	
				}else{
					ges.mergeCells(sheet, row - 2, row - 1, startingColIndex, endColIndex - 1, true);
				}
				Row rowComp = sheet.getRow(row - 2);
				if (rowComp == null) {
					rowComp = sheet.createRow(row - 2);
				}
				Cell cellComp = rowComp.getCell(startingColIndex);
				if (cellComp == null) {
					cellComp = rowComp.createCell(startingColIndex);
				}
				cellComp.setCellValue(enrty.getKey());
			/*	Row rowdisplay = sheet.getRow(row - 2);
				if (rowdisplay == null) {
					rowdisplay = sheet.createRow(row - 2);
				}
				Cell c = rowdisplay.getCell(startingColIndex);
				if (c == null) {
					c = rowdisplay.createCell(startingColIndex);
				}
				c.setCellValue(enrty.getKey());*/
				cellComp.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
				if(heading.equalsIgnoreCase(CMStatic.VALUATION_RATIO) || heading.equalsIgnoreCase(CMStatic.FINANCIAL_RATIO)){
					startingColIndex = endColIndex+1;
					}else{
					startingColIndex = endColIndex;
					}
				index=endColIndex;
			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}

	/*
	 * to get the all sheet with combination of same in a same sheet type (only
	 * for public entity)
	 */
	private Map<String, Set<String>> getIndustryCompanyIDMap(List<EquityRequest> equityReques)
			throws JsonProcessingException {

		Map<String, Set<String>> hmap = new HashMap<>();
		Set<String> insurance = new HashSet<String>();
		Set<String> bank = new HashSet<String>();
		Set<String> industrial = new HashSet<String>();
		Set<String> otherFinancial = new HashSet<String>();

		for (Iterator<EquityRequest> equityRequests = equityReques.iterator(); equityRequests.hasNext();) {

			EquityRequest equityRequest = equityRequests.next();

			if (CMStatic.ENTITY_TYPE_PRIVATE.equalsIgnoreCase(equityRequest.getEntityType())
					|| CMStatic.DATA_TYPE_STOCK_PRICE.equalsIgnoreCase(equityRequest.getDataType())
					|| CMStatic.DATA_TYPE_BETA.equalsIgnoreCase(equityRequest.getDataType()))
				continue;

			String industryType = capitalMarketService.getCMCompaniesById(String.valueOf(equityRequest.getCode()))
					.getFf_industry();

			if (industryType.equalsIgnoreCase(CMStatic.INDUSTRY_INSURANCE)) {
				if (null != equityRequest.getCompanyFinancialDTOs()) {
					insurance.add(equityRequest.getCompanyFinancialDTOs().get(0).getCompanyId());
				}
			} else if (industryType.equalsIgnoreCase(CMStatic.INDUSTRY_BANK)) {
				if (equityRequest.getCompanyFinancialDTOs() != null) {
					bank.add(equityRequest.getCompanyFinancialDTOs().get(0).getCompanyId());
				}
			} else if (industryType.equalsIgnoreCase(CMStatic.INDUSTRY_IND)) {
				if (equityRequest.getCompanyFinancialDTOs() != null) {
					industrial.add(equityRequest.getCompanyFinancialDTOs().get(0).getCompanyId());
				}
			} else {
				otherFinancial.add(equityRequest.getCompanyFinancialDTOs().get(0).getCompanyId());
			}
		}

		if (!insurance.isEmpty()) {
			hmap.put(CMStatic.INDUSTRY_INSURANCE, insurance);
		}
		if (!bank.isEmpty()) {
			hmap.put(CMStatic.INDUSTRY_BANK, bank);
		}
		if (!industrial.isEmpty()) {
			hmap.put(CMStatic.INDUSTRY_IND, industrial);
		}
		if (!otherFinancial.isEmpty()) {
			hmap.put(CMStatic.INDUSTRY_OTHER, otherFinancial);
		}

		return hmap;
	}

	private void createLogo(Cell cell, Sheet sheet, Workbook workbook, ExcelStyle ges) {
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

	public CellStyle getNumberFormatStyle(Double value, String unit, ExcelStyle ges, Boolean isLastRecord) {

		if (isLastRecord) {
			if (unit.contains("%") && Math.abs(value) >= 1.0) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else if (unit.equals("x")) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (Math.abs(value) < 10.0) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
			}

		} else {
			if (unit.contains("%") && Math.abs(value) >= 1.0) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else if (unit.equals("x")) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (Math.abs(value) < 10.0) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			} else {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
			}
		}
	}

}
