package com.televisory.capitalmarket.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.model.BetaData;
import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.model.EconomyRequest;
import com.televisory.capitalmarket.model.EquityRequest;
import com.televisory.capitalmarket.util.CMStatic;



@Service
public class ExcelReportService {

	static Logger _log = Logger.getLogger(ExcelReportService.class);
	Calendar cal = null;
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * 
	 * @param downloadRequest
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Autowired
	ExcelDesignService excelDesignService;

	@Autowired
	EconomyService economyService;

	@Autowired
	FinancialSheetService financialSheetService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	public Workbook createExcelReport(DownloadRequest downloadRequest) throws JsonGenerationException, JsonMappingException, IOException {

		Workbook workbook = new XSSFWorkbook();
		ExcelStyle ges =new ExcelStyle((XSSFWorkbook) workbook);

		//All non financial data sheets will be created from this method
		Map<String,String>  sheetList = createSheetList(downloadRequest);

		for ( String sheetName : sheetList.keySet() ) {

			if(sheetName.equals(CMStatic.SHEET_STOCK_PRICE_COMPANY)){
				createCompanyStockPriceSheet(downloadRequest,sheetName,workbook,ges);
			}else if(sheetName.equals(CMStatic.SHEET_STOCK_PRICE_INDEX)){
				createIndexStockPriceSheet(downloadRequest,sheetName,workbook,ges);
			}else if(sheetName.equals(CMStatic.SHEET_MACRO_ECONOMIC_COUNTRY)){
				createMacroEconomicCountrySheet(downloadRequest,sheetName,workbook,ges);
			}
			else if(sheetName.equals(CMStatic.SHEET_MACRO_ECONOMIC_METRIC)){
				createMacroEconomicMetricSheet(downloadRequest,sheetName,workbook,ges);
			}else if(sheetName.equals(CMStatic.SHEET_COMPANY_BETA)){
				createCompanyBetaSheet(downloadRequest,sheetName,workbook,ges);
			}/*else if(sheetName.contains(CMStatic.SHEET_FINANCIAL)){
				createFinancialSheet(downloadRequest,sheetName,workbook,ges);
			}*/else if(sheetName.equals(CMStatic.SHEET_COMMODITY_PRICE)){
				createCommmoditySheet(downloadRequest,sheetName,workbook,ges);
			}else if(sheetName.equals(CMStatic.SHEET_COMMODITY_DATA_DOWNLOAD)){
				createCommmoditySheetDataDownload(downloadRequest,sheetName,workbook,ges);
			}else{
				_log.warn("Not implemented");
			}
		}

		//All public entity financial data sheets will be created from this method
		financialSheetService.createAndFillFinancialSheets(downloadRequest,workbook, ges);
		
		//All private entity financial data sheets will be created from this method
		financialSheetService.createAndFillFinancialSheetsPrivateEntity(downloadRequest,workbook, ges);
		return workbook;
	}

	/**
	 * Method to create the sheet for commodity data
	 * @param downloadRequest
	 * @param sheetName
	 * @param workbook
	 * @param ges
	 */
	private void createCommmoditySheet(DownloadRequest downloadRequest, String sheetName, Workbook workbook,
			ExcelStyle ges) {

	    Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
	    sheet.setDisplayGridlines(false);
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 

		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }
		Cell cell = r.createCell(1);
        if(cell==null) {
        	cell=r.createCell(1);
        }
		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }

		LinkedHashSet<String> periodMap = new LinkedHashSet<>();
		Map<String, Double> dataSet = new HashMap<>();
		HashSet<String> distinctCommodity =new HashSet<>();
		HashMap<String,String> unitMap =new HashMap<>();

		downloadRequest.getCommodity().getCommodityData().forEach(commodityDataObject -> {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			String period = dateFormat.format(commodityDataObject.getPeriod());

			dataSet.put(commodityDataObject.getSymbol()+"::"+period, commodityDataObject.getClose());
			distinctCommodity.add(commodityDataObject.getSymbol()+"::"+commodityDataObject.getName());
			periodMap.add(period);

			unitMap.put(commodityDataObject.getSymbol(),commodityDataObject.getUnit());

		});

		createCommodityTable(row+4, sheet, ges, dataSet, distinctCommodity, periodMap,unitMap);		
	}


	/**
	 * Method to create the sheet for commodity data
	 * @param downloadRequest
	 * @param sheetName
	 * @param workbook
	 * @param ges
	 */
	private void createCommmoditySheetDataDownload(DownloadRequest downloadRequest, String sheetName, Workbook workbook, ExcelStyle ges) {

	    Sheet sheet = workbook.createSheet(sheetName);
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
        sheet.setDisplayGridlines(false);
		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }
		Cell cell = r.createCell(1);
		
		if(cell==null) {
			cell=r.createCell(1);
		}

		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }
		TreeSet<Date> periodMap = new TreeSet<>();
		Map<String, Double> dataSetClose = new HashMap<>();
		Map<String, Double> dataSetOpen = new HashMap<>();
		Map<String, Double> dataSetHigh = new HashMap<>();
		Map<String, Double> dataSetLow = new HashMap<>();

		LinkedHashMap<String, List<Date>> mapPeriod = new LinkedHashMap<>();



		Map<String,Map<String, Double>> dataSetList = new HashMap<>();

		HashSet<String> distinctCommodity =new HashSet<>();
		HashMap<String,String> unitMap =new HashMap<>();

		downloadRequest.getCommodity().getCommodityData().forEach(commodityDataObject -> {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			String period = dateFormat.format(commodityDataObject.getPeriod());

			dataSetOpen.put(commodityDataObject.getSymbol()+"::"+period, commodityDataObject.getOpen());
			dataSetClose.put(commodityDataObject.getSymbol()+"::"+period, commodityDataObject.getClose());
			dataSetHigh.put(commodityDataObject.getSymbol()+"::"+period, commodityDataObject.getHigh());
			dataSetLow.put(commodityDataObject.getSymbol()+"::"+period, commodityDataObject.getLow());


			distinctCommodity.add(commodityDataObject.getSymbol()+"::"+commodityDataObject.getName());
			//periodMap.add(period);
			periodMap.add(commodityDataObject.getPeriod());

			List<Date> myTempPeriod = mapPeriod.get(commodityDataObject.getSymbol()+"::"+commodityDataObject.getName());
			if(myTempPeriod==null){
				myTempPeriod = new ArrayList<>();
			}
			myTempPeriod.add(commodityDataObject.getPeriod());
			mapPeriod.put(commodityDataObject.getSymbol()+"::"+commodityDataObject.getName(), myTempPeriod);

			unitMap.put(commodityDataObject.getSymbol(),commodityDataObject.getUnit());

		});

		_log.info("#########################");
		_log.info(mapPeriod);
		_log.info("#########################");


		dataSetList.put("OPEN",dataSetOpen);
		dataSetList.put("CLOSE",dataSetClose);
		dataSetList.put("HIGH",dataSetHigh);
		dataSetList.put("LOW",dataSetLow);

		createCommodityTableDataDownload(row+2, sheet, ges, dataSetList, distinctCommodity, mapPeriod,unitMap, downloadRequest);		
	}





	private void createCommodityTable(int row, Sheet sheet, ExcelStyle ges, Map<String, Double> dataSet,
			HashSet<String> distinctCommodity, LinkedHashSet<String> periodMap,HashMap<String,String> unitMap) {
		sheet.setDisplayGridlines(false);

		Row r;

		Cell cell;
		int cellIndex=1;
		int oc = cellIndex;

		int or = row;

		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;

		int minRow = 0;

		if(row > maxRow-1) {
			minRow = maxRow;
			maxRow = maxRow+addRow;
		//	ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
		}

		if(periodMap.isEmpty()){

			r = sheet.getRow( row );
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue(CMStatic.NO_DATA_FOUND);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			cellIndex++;

		}else{
			r = sheet.getRow(row);
			
			if(r==null) {
            	r=sheet.createRow(row);
            }

			cell = r.getCell(cellIndex);
			
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}

			sheet.setColumnWidth(cellIndex, (short) (17 * 500));

			try {
				ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cell.setCellValue("Period");
			cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
			cellIndex++;

			for (String commodityName : distinctCommodity) {
				// CREATE HEADER

				cell = r.getCell(cellIndex);
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, (short) (17 * 500));
				try {
					ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				cell.setCellValue(commodityName.split("::")[1] + " ( " +unitMap.get(commodityName.split("::")[0])+" )");
				cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
				cellIndex++;
				// END HEADER
			}

			row++;

			int periodMapCounter=0;

			for (String commodityDate : periodMap) {	

				cellIndex=1;

				if(sheet.getRow(row)==null){
					//ge.setBackgroundOnSheet(sheet, row, row+1, CMStatic.SHEET_MAX_COLUMN);
				}

				r = sheet.getRow(row);
				
				if(r==null) {
                	r=sheet.createRow(row);
                }

				cell = r.getCell(cellIndex);
				
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}

				sheet.setColumnWidth(cellIndex, (short) (17 * 500));

				cell.setCellValue(commodityDate);

				cellIndex++;

				if(periodMap.size()-1==periodMapCounter){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
				}

				for (String commodityName : distinctCommodity) {

					r = sheet.getRow(row);
					
					if(r==null) {
                    	r=sheet.createRow(row);
                    }

					cell = r.getCell(cellIndex);
					
					if(cell==null) {
						cell=r.createCell(cellIndex);
					}

					sheet.setColumnWidth(cellIndex, (short) (17 * 500));

					if(dataSet.get(commodityName.split("::")[0]+"::"+commodityDate)!=null){
						cell.setCellValue(dataSet.get(commodityName.split("::")[0]+"::"+commodityDate));

						if(periodMap.size()-1==periodMapCounter ){
							cell.setCellStyle(getNumberFormatStyle(dataSet.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, true));
						}else{
							cell.setCellStyle(getNumberFormatStyle(dataSet.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, false));
						}	
					}else{
						cell.setCellValue("-");			
						if(periodMap.size()-1==periodMapCounter){
							cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
						}else{
							cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
						}	
					}	
					cellIndex++;
				}	
				periodMapCounter++;
				row++;
			}
		}
	}


	private void createCommodityTableDataDownload(int row, Sheet sheet, ExcelStyle ges,Map<String,Map<String, Double>> dataSetMap,
			HashSet<String> distinctCommodity, LinkedHashMap<String, List<Date>> periodMap,HashMap<String,String> unitMap,DownloadRequest downloadRequest) {

        sheet.setDisplayGridlines(false);

		Row r;
		Cell cell;
		int cellIndex=1;

		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;
		int minRow = 0;

		if(row > maxRow-1) {
			minRow = maxRow;
			maxRow = maxRow+addRow;
			//ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
		}

		if(periodMap.isEmpty()){
			r = sheet.getRow( row );
			cell = r.getCell(cellIndex);
			cell.setCellValue(CMStatic.NO_DATA_FOUND);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			cellIndex++;
		}else{

			cell = getCell(null, sheet, row, cellIndex);
			cell.setCellStyle(ges.BLUE_FONT);
			cell.setCellValue("Commodity");
			row++;
			row++;
			cell = getCell(null, sheet, row, cellIndex);
			cell.setCellStyle(ges.BLUE_FONT);
			cell.setCellValue("Start Date");
			cellIndex++;

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			cell = getCell(null, sheet, row, cellIndex);
			cell.setCellValue(sdf.format(downloadRequest.getStartDate()));

			row++;
			cellIndex = 1;
			cell = getCell(null, sheet, row, cellIndex);
			cell.setCellStyle(ges.BLUE_FONT);
			cell.setCellValue("End Date");
			cellIndex++;



			cell = getCell(null, sheet, row, cellIndex);
			cell.setCellValue(sdf.format(downloadRequest.getEndDate()));
			row++;
			row++;
			row++;
			cellIndex = 1;
			/////////////////////////////////
			////GERERATED THE HEADER 
			
			HashMap<String, Integer> startLoc = new HashMap<>();

			int position = 0;
			
			for (String commodityName : distinctCommodity) {
				
				String unit = null;

				if(unitMap.get(commodityName.split("::")[0])!=null){
					unit = unitMap.get(commodityName.split("::")[0]);
				}

				if(unit==null){
					unit = "";
				}else{
					unit="("+unit+")";
				}


				List<String> commodityCheckedParams = new ArrayList<>();
				for (int i=0;i<downloadRequest.getCommodity().getCommodityParams().size();i++) {
					if(downloadRequest.getCommodity().getCommodityParams().get(i).getTicker_name().split("_")[1].equals(commodityName.split("::")[1])){
						commodityCheckedParams = downloadRequest.getCommodity().getCommodityParams().get(i).getCommodityCheckedParams();
						break;
					}
				}


				try {
					ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
					cell = getCell(null, sheet, row, cellIndex);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cell.setCellValue("Date");
				} catch (IOException e) {
					e.printStackTrace();
				}
				cellIndex++;

				try {
					ges.mergeCells(sheet, row, row, cellIndex, cellIndex+(commodityCheckedParams.size()-1), true);
					cell = getCell(null, sheet, row, cellIndex);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cell.setCellValue(commodityName.split("::")[1]+ " " + unit);
				} catch (IOException e) {
					e.printStackTrace();
				}
				row++;

				startLoc.put(commodityName, cellIndex);

				if(commodityCheckedParams.contains("Open".toUpperCase())){
					cell = getCell(null, sheet, row, cellIndex);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cell.setCellValue("Open");
					cellIndex++;
				}

				if(commodityCheckedParams.contains("High".toUpperCase())){
					cell = getCell(null, sheet, row, cellIndex);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cell.setCellValue("High");
					cellIndex++;
				}

				if(commodityCheckedParams.contains("Low".toUpperCase())){
					cell = getCell(null, sheet, row, cellIndex);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cell.setCellValue("Low");
					cellIndex++;
				}

				if(commodityCheckedParams.contains("Close".toUpperCase())){
					cell = getCell(null, sheet, row, cellIndex);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cell.setCellValue("Close");
					cellIndex++;
				}

				cellIndex++;
				row--;

			}

			row++;
			row++;

			int printRow = 11;
			for (String commodityName : distinctCommodity) {
				row = printRow;
				List<Date> dateList = periodMap.get(commodityName);
				int periodMapCounter = 0;
				int periodSize = dateList.size();
				
				for (Date period : dateList) {	
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					String commodityDate = dateFormat.format(period);
					//cellIndex=1;
					
					cellIndex = startLoc.get(commodityName);
					cellIndex--;
					//for (String commodityName : distinctCommodity) {


						List<String> commodityCheckedParams = new ArrayList<>();
						for (int i=0;i<downloadRequest.getCommodity().getCommodityParams().size();i++) {
							if(downloadRequest.getCommodity().getCommodityParams().get(i).getTicker_name().split("_")[1].equals(commodityName.split("::")[1])){
								commodityCheckedParams = downloadRequest.getCommodity().getCommodityParams().get(i).getCommodityCheckedParams();
								break;
							}
						}


						Map<String, Double> myMapOPEN = dataSetMap.get("OPEN");
						Map<String, Double> myMapHIGH = dataSetMap.get("HIGH");
						Map<String, Double> myMapLOW = dataSetMap.get("LOW");
						Map<String, Double> myMapCLOSE = dataSetMap.get("CLOSE");

						cell = getCell(null, sheet, row, cellIndex);
						cell.setCellValue(commodityDate);
						if(periodSize-1==periodMapCounter){
							cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);	
						}else{
							cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
						}

						cellIndex++;
						if(commodityCheckedParams.contains("Open".toUpperCase())){
							cell = getCell(null, sheet, row, cellIndex);
							if(myMapOPEN.get(commodityName.split("::")[0]+"::"+commodityDate)!=null){
								cell.setCellValue(myMapOPEN.get(commodityName.split("::")[0]+"::"+commodityDate));
								if(periodSize-1==periodMapCounter ){
									cell.setCellStyle(getNumberFormatStyle(myMapOPEN.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, true));
								}else{
									cell.setCellStyle(getNumberFormatStyle(myMapOPEN.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, false));
								}	
							}else{
								cell.setCellValue("-");			
								if(periodSize-1==periodMapCounter){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
							}	
							cellIndex++;
						}


						if(commodityCheckedParams.contains("High".toUpperCase())){
							cell = getCell(null, sheet, row, cellIndex);
							if(myMapHIGH.get(commodityName.split("::")[0]+"::"+commodityDate)!=null){
								cell.setCellValue(myMapHIGH.get(commodityName.split("::")[0]+"::"+commodityDate));
								if(periodSize-1==periodMapCounter ){
									cell.setCellStyle(getNumberFormatStyle(myMapHIGH.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, true));
								}else{
									cell.setCellStyle(getNumberFormatStyle(myMapHIGH.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, false));
								}	
							}else{
								cell.setCellValue("-");			
								if(periodSize-1==periodMapCounter){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
							}	
							cellIndex++;
						}

						if(commodityCheckedParams.contains("Low".toUpperCase())){
							cell = getCell(null, sheet, row, cellIndex);
							if(myMapLOW.get(commodityName.split("::")[0]+"::"+commodityDate)!=null){
								cell.setCellValue(myMapLOW.get(commodityName.split("::")[0]+"::"+commodityDate));
								if(periodSize-1==periodMapCounter ){
									cell.setCellStyle(getNumberFormatStyle(myMapLOW.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, true));
								}else{
									cell.setCellStyle(getNumberFormatStyle(myMapLOW.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, false));
								}	
							}else{
								cell.setCellValue("-");			
								if(periodSize-1==periodMapCounter){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
							}	

							cellIndex++;
						}

						if(commodityCheckedParams.contains("Close".toUpperCase())){
							cell = getCell(null, sheet, row, cellIndex);
							if(myMapCLOSE.get(commodityName.split("::")[0]+"::"+commodityDate)!=null){
								cell.setCellValue(myMapCLOSE.get(commodityName.split("::")[0]+"::"+commodityDate));
								if(periodSize-1==periodMapCounter ){
									cell.setCellStyle(getNumberFormatStyle(myMapCLOSE.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, true));
								}else{
									cell.setCellStyle(getNumberFormatStyle(myMapCLOSE.get(commodityName.split("::")[0]+"::"+commodityDate), "", ges, false));
								}	
							}else{
								cell.setCellValue("-");			
								if(periodSize-1==periodMapCounter){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
							}	

							cellIndex++;
						}

						cellIndex++;
					//}

					row++;
					periodMapCounter++;
				}
			}
		}
	}


	public void setCell(Workbook workbook, Sheet sheet,int rowNo , int colNo , String cellValue ,String cellFormulae ,CellStyle cellStyle ){
		Cell cell = getCell(workbook, sheet, rowNo, colNo);
		_log.info(cell);
		if(cellValue!=null){
			if(cell!=null ){
				if(cell!=null && cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
					if (DateUtil.isCellDateFormatted(cell)) {
						String sDate1=cellValue;  
						Date date1 = null;
						try {
							SimpleDateFormat sd1 = new SimpleDateFormat("dd/MM/yyyy");
							SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd");
							date1 = sd1.parse(sd1.format(sd2.parse(sDate1)));
						} catch (ParseException e) {
							e.printStackTrace();
						}  
						if(date1!=null){
							cell.setCellValue(date1);
						}else{
							cell.setCellValue(cellValue);
						}
					}
				}else{
					cell.setCellValue(cellValue);
				}

			}
		}
		if(cellFormulae!=null){
			cell.setCellFormula(cellFormulae);
		}
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}

	public Cell getCell(Workbook workbook, Sheet sheet,int rowNo , int colNo){
		_log.debug("Row No ::: " + rowNo + " , Col no : " + colNo);
		Row row = sheet.getRow(rowNo);
		if(row==null){
			_log.debug("The row is blank");
			row = sheet.createRow(rowNo);
		}

		Cell cell = row.getCell(colNo);
		if(cell==null){
			cell = row.createCell(colNo);
		}
		_log.debug("Tbe cell is ::: " + cell);
		return cell;
	}



	private Map<String,String> createSheetList(DownloadRequest downloadRequest) throws UnsupportedEncodingException {
		//CREATE SHEET LIST
		Map<String, String> sheetList = new TreeMap<String,String>();

		if(downloadRequest.getEquity()!=null && downloadRequest.getEquity().size()!=0) {

			for (Iterator<EquityRequest> equityRequests = downloadRequest.getEquity().iterator(); equityRequests.hasNext();) {

				EquityRequest equityRequest = equityRequests.next();

				if(equityRequest.getType().equals(CMStatic.TYPE_COMPANY) && equityRequest.getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
					sheetList.put(CMStatic.SHEET_STOCK_PRICE_COMPANY, CMStatic.SHEET_STOCK_PRICE_COMPANY);
				}else if(equityRequest.getType().equals(CMStatic.TYPE_INDEX) && equityRequest.getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
					sheetList.put(CMStatic.SHEET_STOCK_PRICE_INDEX, CMStatic.SHEET_STOCK_PRICE_INDEX);
				}else if(equityRequest.getType().equals(CMStatic.TYPE_COMPANY) && equityRequest.getDataType().equals(CMStatic.DATA_TYPE_BETA)) {
					sheetList.put(CMStatic.SHEET_COMPANY_BETA, CMStatic.SHEET_COMPANY_BETA);
				}else {
					_log.info("No need to create sheet for:"+ equityRequest.getType() +", "+ equityRequest.getDataType());

					//String compName = equityRequest.getName();
					//sheetList.put(CMStatic.SHEET_FINANCIAL+"--"+compName, CMStatic.SHEET_FINANCIAL+"--"+compName);
				}
			}
		}

		if(downloadRequest.getEconomy()!=null && downloadRequest.getEconomy().size()!=0) {
			for (Iterator<EconomyRequest> economyRequests = downloadRequest.getEconomy().iterator(); economyRequests.hasNext();) {
				EconomyRequest economyRequest = economyRequests.next();
				if(economyRequest.getType().equals(CMStatic.TYPE_ECONOMY_COUNTRY)) {
					sheetList.put(CMStatic.SHEET_MACRO_ECONOMIC_COUNTRY, CMStatic.SHEET_MACRO_ECONOMIC_COUNTRY);
				}else if(economyRequest.getType().equals(CMStatic.TYPE_ECONOMY_INDICATOR)) {
					sheetList.put(CMStatic.SHEET_MACRO_ECONOMIC_METRIC, CMStatic.SHEET_MACRO_ECONOMIC_METRIC);
				}
			}
		}
		if(downloadRequest.getCommodity()!=null){
			if(downloadRequest.getCommodity().getType()!=null && downloadRequest.getCommodity().getType().equals("dataDownload")){
				sheetList.put(CMStatic.SHEET_COMMODITY_DATA_DOWNLOAD, CMStatic.SHEET_COMMODITY_DATA_DOWNLOAD);
			}else{
				sheetList.put(CMStatic.SHEET_COMMODITY_PRICE, CMStatic.SHEET_COMMODITY_PRICE);
			}
		}

		return sheetList;
	}

	private void createCompanyStockPriceSheet(DownloadRequest downloadRequest, String sheetName, Workbook workbook, ExcelStyle ges) throws IOException {
		Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		sheet.setDisplayGridlines(false);
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
			r=sheet.createRow(row);
		}
		Cell cell = r.createCell(1);
        
		if(null==cell) {
			cell=r.createCell(1);
		}
		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
        if(r==null) {
        	r=sheet.createRow(row);
        }

		Set<String> companyList = new HashSet<String>();
		SortedSet<Date> distinctDate = new TreeSet<Date>();
		Map<String, Double> dataSet = new HashMap<String, Double>();
		String dataType="",currency="", unit="",periodicity="";
		HashMap<String,List<String>> dataTypeList = new HashMap<String,List<String>>();
		HashMap<String,String> currencyList = new HashMap<String,String>();

		for (Iterator<EquityRequest> equityRequests = downloadRequest.getEquity().iterator(); equityRequests.hasNext();) {
			int i=0;	

			EquityRequest equityRequest =  equityRequests.next();

			if(equityRequest.getType().equals(CMStatic.TYPE_COMPANY) && equityRequest.getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
				List<String> filterList = equityRequest.getFilterList();
				//In case of blank filter list use closing price
				if(filterList.size() == 0)
					filterList.add(CMStatic.CLOSE);
				
				for (Iterator<StockPriceDTO> stockPriceDTOs = equityRequest.getStockPriceDTOs().iterator(); stockPriceDTOs.hasNext();) {
					StockPriceDTO stockPriceDTO = stockPriceDTOs.next();
					distinctDate.add(stockPriceDTO.getDate());
					if(i==0) {
						currency = stockPriceDTO.getCurrency();
						unit = stockPriceDTO.getUnit();
						i=1;
					}
					for (String filter : filterList ) {						
						String key = equityRequest.getExchange()+"--"+equityRequest.getCode()+"--"+dateFormat.format(stockPriceDTO.getDate())+"--";
						switch(filter.toUpperCase()){
						case CMStatic.HIGH:
							key = key+CMStatic.HIGH;
							dataSet.put(key, stockPriceDTO.getHigh());
							break;
						case CMStatic.LOW:
							key = key+CMStatic.LOW;
							dataSet.put(key, stockPriceDTO.getLow());
							break;
						case CMStatic.OPEN:
							key = key+CMStatic.OPEN;
							dataSet.put(key, stockPriceDTO.getOpen());
							break;
						case CMStatic.CLOSE:
							key = key+CMStatic.CLOSE;
							dataSet.put(key, stockPriceDTO.getClose());
							break;
						default:
							_log.error("Error in getting data::");
						}						
					}									
				}
				String compName = equityRequest.getName();
				companyList.add(compName);
				dataType = CMStatic.SHARE_PRICE_TEXT;


				if(!periodicity.contains(equityRequest.getPeriodicity())) {
					periodicity+=equityRequest.getPeriodicity()+",";
				}
				dataTypeList.put(equityRequest.getExchange()+"--"+compName+"--"+equityRequest.getCode(), filterList);
				currencyList.put(equityRequest.getExchange()+"--"+compName+"--"+equityRequest.getCode(),currency);
			}

		}	
		if(!periodicity.equals(""))periodicity = periodicity.substring(0,periodicity.length()-1);

		createInfo(companyList, downloadRequest.getStartDate(), downloadRequest.getEndDate(), dataType, "", periodicity,unit,sheet, row+1, ges );
		createStockPriceTable(row+11, sheet, ges, distinctDate, dataSet, dataTypeList,currencyList);				

	}


	private void createIndexStockPriceSheet(DownloadRequest downloadRequest, String sheetName, Workbook workbook, ExcelStyle ges) throws IOException {
		Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		sheet.setDisplayGridlines(false);
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
		int row = 0;
		Row r = sheet.getRow(row);
		if(null==r) {
			r=sheet.createRow(row);
		}
		Cell cell = r.createCell(1);
        if(cell==null) {
        	cell=r.createCell(1);
        }
		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
		if(r==null) {
			r=sheet.createRow(row);
		}
		Set<String> companyList = new HashSet<String>();
		SortedSet<Date> distinctDate = new TreeSet<Date>();
		Map<String, Double> dataSet = new HashMap<String, Double>();
		String dataType="",currency="", unit="",periodicity="";
		HashMap<String,List<String>> dataTypeList = new HashMap<String,List<String>>();
		HashMap<String,String> currencyList = new HashMap<String,String>();
		int i=0;
		for (Iterator<EquityRequest> equityRequests = downloadRequest.getEquity().iterator(); equityRequests.hasNext();) {
			EquityRequest equityRequest =  equityRequests.next();

			if(equityRequest.getType().equals(CMStatic.TYPE_INDEX) && equityRequest.getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
				List<String> filterList = equityRequest.getFilterList();

				for (Iterator<StockPriceDTO> stockPriceDTOs = equityRequest.getStockPriceDTOs().iterator(); stockPriceDTOs.hasNext();) {
					StockPriceDTO stockPriceDTO = stockPriceDTOs.next();
					distinctDate.add(stockPriceDTO.getDate());

					for (String filter : filterList ) {
						if(i==0) {
							currency = stockPriceDTO.getCurrency();
							unit = stockPriceDTO.getUnit();
							i=1;
						}
						String key = equityRequest.getExchange()+"--"+equityRequest.getCode()+"--"+dateFormat.format(stockPriceDTO.getDate())+"--";
						switch(filter.toUpperCase()){
						case CMStatic.HIGH:
							key = key+CMStatic.HIGH;
							dataSet.put(key, stockPriceDTO.getHigh());
							break;
						case CMStatic.LOW:
							key = key+CMStatic.LOW;
							dataSet.put(key, stockPriceDTO.getLow());
							break;
						case CMStatic.OPEN:
							key = key+CMStatic.OPEN;
							dataSet.put(key, stockPriceDTO.getOpen());
							break;
						case CMStatic.CLOSE:
							key = key+CMStatic.CLOSE;
							dataSet.put(key, stockPriceDTO.getClose());
							break;
						default:
							_log.error("Error in getting data::");
						}						
					}									
				}

				companyList.add(equityRequest.getName());
				dataType = CMStatic.INDEX_MOVEMENT_TEXT;

				if(!periodicity.contains(equityRequest.getPeriodicity())) {
					periodicity+=equityRequest.getPeriodicity()+",";
				}
				dataTypeList.put(equityRequest.getExchange()+"--"+equityRequest.getName()+"--"+equityRequest.getCode(), filterList);
				//currencyList.put(equityRequest.getExchange()+"--"+equityRequest.getName()+"--"+equityRequest.getCode(),currency);
			}

		}	
		if(!periodicity.equals(""))periodicity = periodicity.substring(0,periodicity.length()-1);


		createInfo(companyList, downloadRequest.getStartDate(), downloadRequest.getEndDate(), dataType, "", periodicity, unit, sheet, row+1, ges );
		createStockPriceTable(row+11, sheet, ges, distinctDate, dataSet, dataTypeList, currencyList);				

	}


	public void createLogo(Cell cell, Sheet sheet, Workbook workbook, ExcelStyle ges) {
		short height = 700;
		cell.getRow().setHeight(height);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER);
		//SET LOGO IMAGE
		try {
			excelDesignService.readWorkBook(workbook);
			excelDesignService.setImage(sheet, cell.getRowIndex(), cell.getColumnIndex(),logoPath , 600, 600);
		} catch (IOException e) {
			_log.error(e);
		}
	}
	
	public void createExcelLogo(Cell cell, Sheet sheet, HSSFWorkbook workbook, GenerateExcelStyle ges) {
		short height = 700;
		cell.getRow().setHeight(height);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER);
		//SET LOGO IMAGE
		try {
			excelDesignService.readWorkBook(workbook);
			excelDesignService.setImage(sheet, cell.getRowIndex(), cell.getColumnIndex(),logoPath , 600, 600);
		} catch (IOException e) {
			_log.error(e);
		}
	}

	private void createInfo(Set<String> companyList, Date startDate, Date endDate, String dataType, String currency, String periodicity, String unit, Sheet sheet, int row,ExcelStyle ges){

		Row r;
		Cell cell;

		_log.info("creating Info in row number: "+ row);

		r = sheet.getRow(row);
		if(r==null) {
			r=sheet.createRow(row);
		}
		cell=r.getCell(1);
		if(cell==null) {
			cell=r.createCell(1);
		}
		String heading="";

		for (Iterator<String> cList = companyList.iterator(); cList.hasNext();) {
			heading +=  cList.next()+",";		
		}

		if(!heading.equals("")) {
			heading = heading.substring(0,heading.length()-1);
			if(sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_STOCK_PRICE_COMPANY) || sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_STOCK_PRICE_INDEX)) {
				//heading += "-"+CMStatic.SHARE_PRICE_DATA;
				heading ="";
			}
		}
		cell.setCellValue(heading);
		cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);

		row = row+2;

		/**
		 * Update heading if country Indicator
		 */
		if(sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_MACRO_ECONOMIC_COUNTRY)) {

			r = sheet.getRow(row);
			if(r==null) {
				r=sheet.createRow(row);
			}

			cell=r.getCell(1);		
			if(cell==null) {
				cell=r.createCell(1);
			}
			cell.setCellValue(CMStatic.COUNTRY);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);						

			cell=r.getCell(2);	
			if(null==cell) {
				cell=r.createCell(2);
			}
			cell.setCellValue(dataType);					
			row++;
		}

		if(!sheet.getSheetName().contains(CMStatic.SHEET_FINANCIAL)) {
			r = sheet.getRow(row);
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell=r.getCell(1);		
			if(cell==null) {
				cell=r.createCell(1);
			}
			cell.setCellValue(CMStatic.START_DATE);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);						

			cell=r.getCell(2);	
			if(cell==null) {
				cell=r.createCell(2);
			}
			cell.setCellValue(dateFormat.format(startDate));	
			row++;

			r = sheet.getRow(row);
			
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell=r.getCell(1);	
			if(cell==null) {
				cell=r.createCell(1);
			}
			cell.setCellValue(CMStatic.END_DATE);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);


			cell=r.getCell(2);	
			
			if(cell==null) {
				cell=r.createCell(2);
			}
			cal = Calendar.getInstance();
			cal.setTime(endDate);

			cell.setCellValue(dateFormat.format(endDate));	
			row++;
		}

		/**
		 * set currency heading
		 */
		if(null!= currency && !currency.equals("")) {
			r = sheet.getRow(row);
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell=r.getCell(1);		
			if(cell==null) {
				cell=r.createCell(1);
			}
			cell.setCellValue(CMStatic.CURRENCY);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);						

			cell=r.getCell(2);	
			if(cell==null) {
				cell=r.createCell(2);
			}
			cell.setCellValue(currency);					
			row++;
		}


		if(sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_STOCK_PRICE_COMPANY) || sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_STOCK_PRICE_INDEX)) {
			r = sheet.getRow(row);
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell=r.getCell(1);	
			if(cell==null) {
				cell=r.createCell(1);
			}
			cell.setCellValue(CMStatic.DATA_TYPE);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);						

			cell=r.getCell(2);		
			if(cell==null) {
				cell=r.createCell(2);
			}
			cell.setCellValue(dataType);					
			row++;
		}else {
			if(null!=unit && !unit.equals("")) {
				r = sheet.getRow(row);
				if(r==null) {
                	r=sheet.createRow(row);
                }
				cell=r.getCell(1);	
				if(cell==null) {
					cell=r.createCell(1);
				}
				cell.setCellValue(CMStatic.UNIT);
				cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);						

				cell=r.getCell(2);	
				if(cell==null) {
					cell=r.createCell(2);
				}
				cell.setCellValue(unit);					
				row++;
			}
		}

		if(!periodicity.equals("") &&  !sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_MACRO_ECONOMIC_COUNTRY) && !sheet.getSheetName().equalsIgnoreCase(CMStatic.SHEET_MACRO_ECONOMIC_METRIC)) {

			r = sheet.getRow(row);
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell=r.getCell(1);	
			if(cell==null) {
				cell=r.createCell(1);
			}
			cell.setCellValue(CMStatic.PERIODICITY);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);						

			r = sheet.getRow(row);
			if(r==null) {
            	r=sheet.createRow(row);
            }
			cell=r.getCell(2);	
			if(cell==null) {
				cell=r.createCell(2);
			}
			cell.setCellValue(periodicity.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)?"Half-Yearly":periodicity);						
			row++;
		}

		_log.info("creating header in row number: "+ row);

	}


	private void createStockPriceTable(int row, Sheet sheet, ExcelStyle ges,SortedSet<Date> distinctDate ,Map<String, Double> dataSet, Map<String,List<String>> dataTypeList, Map<String,String> currencyList) throws IOException {
		_log.info("creating header in row number: "+ row);

		Row r;
		Cell cell;
		int cellIndex=1;
		int oc = cellIndex;
		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;
		int minrow = 0;
		//CREATE HEADER
		if(cellIndex==1) {

			//CREATE DATE
			r = sheet.getRow( row );
			if(r==null) {
				r=sheet.createRow(row);
			}
			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			sheet.setColumnWidth(cellIndex, (short) (10 * 500));	
			ges.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);							
			cell.setCellValue(CMStatic.DATE);
			cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
			cellIndex++;
		}
		for(String compExName : dataTypeList.keySet()) {
			List<String> filterList = dataTypeList.get(compExName);
			String currency = "";
			//exchange--name--code

			if(null!=currencyList.get(compExName)) {
				currency = " ( "+currencyList.get(compExName)+" ) ";
			}
			String companyName = compExName.split("--")[1]+currency;

			r = sheet.getRow( row );
			if(r==null) {
				r=sheet.createRow(row);
			}
			r.setHeight((short) (7.5 * 100));
			sheet.setColumnWidth(cellIndex, (short) (7.5 * 500));
			cell = r.getCell(cellIndex);
            if(cell==null) {
            	cell=r.createCell(cellIndex);
            }
			ges.mergeCells(sheet, row, row, cellIndex, cellIndex+filterList.size()-1,true);	

			cell.setCellValue(companyName);
			cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);

			if(filterList!=null){
				for(int f=0; f<filterList.size();f++) {
					r = sheet.getRow( row+1);
					if(r==null) {
						r=sheet.createRow(row+1);
					}
					sheet.setColumnWidth(cellIndex, (short) (7.5 * 500));					
					cell = r.getCell(cellIndex);
					if(cell==null) {
						cell=r.createCell(cellIndex);
					}
					cell.setCellValue(filterList.get(f));
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					cellIndex++;
				}
			}
		}

		//CREATE HEADER END
		//PRINT DATA STARTS
		row=row+2;
		if(distinctDate!=null && distinctDate.size()!=0) {
			Iterator<Date> iterator = distinctDate.iterator();
			while(iterator.hasNext()){
				cellIndex = oc;
				Date rowDate = iterator.next();
				if(row > maxRow-1) {
					minrow = maxRow;
					maxRow = maxRow+addRow;
					//ge.setBackgroundOnSheet(sheet, minrow, maxRow, CMStatic.SHEET_MAX_COLUMN);
				}
				r = sheet.getRow( row );
				if(r==null) {
					r=sheet.createRow(row);
				}
				cell = r.getCell(cellIndex);
				
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}
				cell.setCellValue(dateFormat.format(rowDate));
				if(! iterator.hasNext() ){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT);	
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT);
				}
				cellIndex++;

				StringBuilder combine = new StringBuilder();
				for(String compExName : dataTypeList.keySet()) {
					List<String> filterList = dataTypeList.get(compExName);
					//exchange--name--code
					String exchangeKey = compExName.split("--")[0];
					String nameKey = compExName.split("--")[1];
					String codeKey = compExName.split("--")[2];
					String dateKey = dateFormat.format(rowDate);
					r = sheet.getRow( row );
					if(r==null) {
						r=sheet.createRow(row);
					}
					cell = r.getCell(cellIndex);
					
					if(cell==null) {
						cell=r.createCell(cellIndex);
					}

					if(filterList!=null){
						for(int f=0; f<filterList.size();f++) {
							//Key : exchange--code--date--dataType
							combine.setLength(0);
							combine.append(exchangeKey).append("--").append(codeKey).append("--").append(dateKey).append("--"+filterList.get(f));
							if(dataSet.containsKey(combine.toString())) {
								cell = r.getCell(cellIndex);
								if(null==cell) {
									cell=r.createCell(cellIndex);
								}
								sheet.setColumnWidth(cellIndex, (short) (7.5 * 500));

								if(dataSet.get(combine.toString()) != null) {	
									Double cellVal = dataSet.get(combine.toString());
									cell.setCellValue(cellVal);	
									if(! iterator.hasNext() ){
										cell.setCellStyle(getNumberFormatStyle(cellVal, "", ges, true));
									}else{
										cell.setCellStyle(getNumberFormatStyle(cellVal, "", ges, false));
									}
								}else {
									cell.setCellValue(CMStatic.NA);
									if(! iterator.hasNext() ){
										cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
									}else{
										cell.setCellStyle(ges.BORDER_RIGHT);
									}
								}
								cellIndex++;
							}else {
								cell = r.getCell(cellIndex);
								if(cell==null) {
									cell=r.createCell(cellIndex);
								}
								sheet.setColumnWidth(cellIndex, (short) (7.5 * 500));

								cell.setCellValue(CMStatic.NA);

								if(! iterator.hasNext() ){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_RIGHT);
								}
								cellIndex++;
							}
						}
					}
				}
				row++;
			}
		}else {
			r = sheet.getRow( row );
			if(r==null) {
				r=sheet.createRow(row);
			}
			cell = r.getCell(cellIndex);
			if(cell==null) {
				cell=r.createCell(cellIndex);
			}
			cell.setCellValue(CMStatic.NO_DATA_FOUND);
			cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			cellIndex++;
		}
		//PRINT DATA END
	}


	/*private void createFinancialSheet(DownloadRequest downloadRequest, String sheetName, HSSFWorkbook workbook, GenerateExcelStyle ges) throws IOException {
		HSSFSheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
		int row = 0;
		Row r = sheet.getRow(row);
		Cell cell = r.createCell(1);

		createLogo(cell, sheet, workbook, ges);

		row++;
		r = sheet.getRow(row);

		Set<String> companyList = new HashSet<String>();
		String dataType="",currency="", unit="",periodicity="";
		Map <String, Double> dataSet = new HashMap<String, Double>();
		Map <String,SortedSet<Date>> dateMap = new HashMap<String, SortedSet<Date>>();
		Map <String,List<String>> metricsMap = new HashMap<String, List<String>>();
		Map <String,List<String>> mandatoryMetricsMap = new HashMap<String, List<String>>();
		for (Iterator<EquityRequest> equityRequests = downloadRequest.getEquity().iterator(); equityRequests.hasNext();) {
			EquityRequest equityRequest =  equityRequests.next();
			SortedSet<Date> distinctDate = new TreeSet<Date>();	 
			List<String> distinctMetrics = new ArrayList<String>();
			List<String> metricDepthMandatory = new ArrayList<String>();
			String compName = equityRequest.getName();
			if(equityRequest.getType().equals(CMStatic.TYPE_COMPANY) && !equityRequest.getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE) && !equityRequest.getDataType().equals(CMStatic.DATA_TYPE_BETA) && compName.equals(sheetName.split("--")[1])) {
				if(equityRequest.getCompanyFinancialDTOs()!=null && equityRequest.getCompanyFinancialDTOs().size()!=0) {
					int i=0;
					for (Iterator<CompanyFinancialMINDTO> companyFinancialDTOs = equityRequest.getCompanyFinancialDTOs().iterator(); companyFinancialDTOs.hasNext();) {
						CompanyFinancialMINDTO compFinDTO = companyFinancialDTOs.next();
						String itemName = compFinDTO.getItemName();
						if(compFinDTO.getFinancialType().equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO) || compFinDTO.getFinancialType().equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO)) {
							itemName = compFinDTO.getFieldName();
						}

						if(!distinctMetrics.contains(itemName+"--"+compFinDTO.getDisplayOrder()+"--"+compFinDTO.getUnit()+"--"+compFinDTO.getCurrency())) {
							distinctMetrics.add(itemName+"--"+compFinDTO.getDisplayOrder()+"--"+compFinDTO.getUnit()+"--"+compFinDTO.getCurrency());		
							metricDepthMandatory.add(itemName+"--"+compFinDTO.getDepthLevel()+"--"+compFinDTO.getMandatory());
						}
						distinctDate.add(compFinDTO.getPeriod());

						if(i==0 && !compFinDTO.getFinancialType().equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE) && !compFinDTO.getFinancialType().equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {
							currency = compFinDTO.getCurrency();
							unit = compFinDTO.getUnit();
							i=1;
						}
						String key = equityRequest.getExchange()+"--"+equityRequest.getCode()+"--"+equityRequest.getDataType()+"--"+itemName+"--"+compFinDTO.getDisplayOrder()+"--"+dateFormat.format(compFinDTO.getPeriod());
						dataSet.put(key, compFinDTO.getData());

					}					
				}

				if(!periodicity.contains(equityRequest.getPeriodicity())) {
					periodicity+=equityRequest.getPeriodicity()+",";
				}	
				companyList.add(compName);
				String metricKey = equityRequest.getExchange()+"--"+equityRequest.getCode()+"--"+equityRequest.getDataType()+"--"+equityRequest.getPeriodicity();
				dateMap.put(metricKey, distinctDate);
				metricsMap.put(metricKey, distinctMetrics);
				mandatoryMetricsMap.put(metricKey, metricDepthMandatory);
			}

		}	
		if(!periodicity.equals(""))periodicity = periodicity.substring(0,periodicity.length()-1).toUpperCase();

		createInfo(companyList, downloadRequest.getStartDate(), downloadRequest.getEndDate(), dataType, currency, periodicity,unit,sheet, row+1, ges );
		createFinancialTable(row+9, sheet, ges,dataSet, metricsMap, dateMap, mandatoryMetricsMap);				

	}

*/

	private void createFinancialTable(int row, HSSFSheet sheet, ExcelStyle ge, Map<String, Double> dataSet, Map <String,List<String>> metricsMap, Map <String,SortedSet<Date>> dateMap, Map <String,List<String>> mandatoryMetricsMap) throws IOException {

		_log.info("creating header in row number: "+ row);
		Row r;
		Cell cell;
		int cellIndex=1;
		int oc = cellIndex;
		boolean firstDataType=true;
		int or = row;
		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;
		int minRow=0;
		//	row = row+2;
		if(metricsMap!=null && metricsMap.size()!=0) {

			for (Iterator<String> sortedTypeList = CMStatic.dataTypeList.iterator(); sortedTypeList.hasNext();) {
				String type = sortedTypeList.next();
				for(String metricsKey : metricsMap.keySet()) {
					String dataType = metricsKey.split("--")[2];
					if(type.equals(dataType)) {
						String exchangeName = metricsKey.split("--")[0];
						String compCode = metricsKey.split("--")[1];

						String periodicity = metricsKey.split("--")[3];
						List<String> distinctMetrics = metricsMap.get(metricsKey);
						List<String> metricDepthMandatory = mandatoryMetricsMap.get(metricsKey);
						SortedSet<Date> dateList = dateMap.get(metricsKey);
						//row = or;

						cellIndex = oc;
						if(!firstDataType) {
							row = row+5;
							//or = row;
						}

						String heading = "";
						if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_BALANCE_SHEET)) {
							heading = CMStatic.BALANCE_SHEET;
						}else if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_PNL)) {
							heading = CMStatic.INCOME_STATEMENT;
						}else if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_CASH_FLOW)) {
							heading = CMStatic.CASH_FLOW;
						}else if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE)) {
							heading = CMStatic.VALUATION_RATIO;
						}else if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {
							heading = CMStatic.FINANCIAL_RATIO; 
						}
						//CREATE HEADER

						if(row > maxRow-1) {
							minRow = maxRow;
							maxRow = maxRow+addRow;
							ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
						}

						r = sheet.getRow( row );
						cell = r.getCell(cellIndex);
						sheet.setColumnWidth(cellIndex, (short) (14 * 500));
						ge.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);	
						cell.setCellValue(heading);
						cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
						cellIndex++;
						if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE) || dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {
							r = sheet.getRow( row );
							cell = r.getCell(cellIndex);
							sheet.setColumnWidth(cellIndex, (short) (12 * 500));
							ge.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);	
							cell.setCellValue(CMStatic.UNIT);
							cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
							cellIndex++;
						}

						//END HEADER

						for(Iterator<Date> dates = dateList.iterator();dates.hasNext();) {
							Date date = dates.next();

							//row = or;
							r = sheet.getRow( row );
							cell = r.getCell(cellIndex);		
							ge.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);
							if(periodicity.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY))
								cell.setCellValue(CMStatic.QUARTER_END+"\n"+dateFormat.format(date));
							else if(periodicity.equalsIgnoreCase(CMStatic.PERIODICITY_YEARLY))
								cell.setCellValue(CMStatic.YEAR_END+"\n"+dateFormat.format(date));
							else if(periodicity.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY))
								cell.setCellValue(CMStatic.HALF_YEAR_END+"\n"+dateFormat.format(date));
							cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);


							cellIndex++;
						}
						row=row+2;
						if(distinctMetrics!=null && distinctMetrics.size()!=0) {
							for(int m=0; m<distinctMetrics.size();m++) {
								int depthLevel =0, mandatory = 0;
								String intendSpace="";
								cellIndex = oc;
								String metricName = distinctMetrics.get(m).split("--")[0];
								String displayOrder = distinctMetrics.get(m).split("--")[1];
								String unit = distinctMetrics.get(m).split("--")[2];
								if(unit.equals("null"))unit="";
								String currency = distinctMetrics.get(m).split("--")[3];
								if(currency.equals("null"))currency="";
								String metricDetails = metricDepthMandatory.get(m);
								if(metricDetails!=null && metricDetails.length()!=0) {
									if(metricDetails.split("--")[0].equals(metricName)) {
										depthLevel = metricDetails.split("--")[1].equals("null")?0:Integer.parseInt(metricDetails.split("--")[1]);
										mandatory = metricDetails.split("--")[2].equals("null")?0:Integer.parseInt(metricDetails.split("--")[2]);
									}
								}

								for(int i=0;i<=depthLevel;i++) {
									if(i!=0 && i!=1)
										intendSpace+="  ";
								}
								String tabmetricName = intendSpace+metricName;

								if(row > maxRow-1) {
									minRow = maxRow;
									maxRow = maxRow+addRow;
									ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
								}


								r = sheet.getRow( row );
								cell = r.getCell(cellIndex);				
								cell.setCellValue(tabmetricName);
								if(m != distinctMetrics.size()-1){
									if(depthLevel==0)
										cell.setCellStyle(ge.BORDER_LEFT_RIGHT_BOLD_FONT);
									else {
										cell.setCellStyle(ge.BORDER_LEFT_RIGHT);
									}

								}else{
									if(depthLevel==0)
										cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_BOLD_FONT);
									else {
										cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT);

									}

								}
								cellIndex++;

								//UNIT ADDITIONS
								if(dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE) || dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) {
									r = sheet.getRow( row );
									cell = r.getCell(cellIndex);				
									cell.setCellValue(currency+" "+unit);
									if(m != distinctMetrics.size()-1){
										cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
									}else{
										cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);

									}
									cellIndex++;
								}

								StringBuilder combine = new StringBuilder();
								int dateLength = dateList.size();
								int counter = 0;
								for(Iterator<Date> dates = dateList.iterator();dates.hasNext();) {							
									Date date = dates.next();
									//Key : exchange()+"--"+code()+"--"+dataType()+"--"+itemName()+displayOrder+"--"+date)
									combine.setLength(0);
									combine.append(exchangeName).append("--").append(compCode).append("--").append(dataType).append("--").append(metricName).append("--").append(displayOrder).append("--"+dateFormat.format(date));
									if(dataSet.containsKey(combine.toString())) {						
										r =  sheet.getRow( row);
										cell = r.getCell(cellIndex);
										sheet.setColumnWidth(cellIndex, (short) (12 * 500));
										if(dataSet.get(combine.toString()) != null) {
											Double cellVal = dataSet.get(combine.toString());
											cell.setCellValue(cellVal);
											if(m == distinctMetrics.size()-1){
												cell.setCellStyle(getNumberFormatStyle(cellVal, "", ge, true));
											}else{
												cell.setCellStyle(getNumberFormatStyle(cellVal, "", ge, false)); 
											}
										}else {
											counter++;
											if((dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE) || dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) && depthLevel==0) {
												cell.setCellValue("");
											}else {
												cell.setCellValue(CMStatic.NA);
											}
											if(m != distinctMetrics.size()-1){
												cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
											}else{
												cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
											}
										}
										cellIndex++;
									}else {
										counter++;
										r =  sheet.getRow( row);
										cell = r.getCell(cellIndex);
										if((dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE) || dataType.equalsIgnoreCase(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE)) && depthLevel==0) {
											cell.setCellValue("");
										}else {
											cell.setCellValue(CMStatic.NA);
										}
										sheet.setColumnWidth(cellIndex, (short) (12 * 500));

										if(m != distinctMetrics.size()-1){
											cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
										}else{
											cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
										}	
										cellIndex++;
									}

								}
								/*if(counter != dateLength || mandatory==1)
							row++;
						else if(m != distinctMetrics.size()-1) {
							cellIndex = oc;
							for(int i=0;i<=dateLength;i++) {								
								r =  sheet.getRow( row);
								cell = r.getCell(cellIndex);
								cell.setCellValue("");
								cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								cellIndex++;
							}

						}*/
								row++;


							}
						}else {

							r =  sheet.getRow( row-1);
							cell = r.getCell(cellIndex+1);				
							cell.setCellValue(CMStatic.NO_DATA_FOUND);
							cell.setCellStyle(ge.BLUE_FONT_WITHOUT_BACKGROUND);
						}
						firstDataType=false;				
					}
				}		
			}
		}
	}

	private void createMacroEconomicMetricSheet(DownloadRequest downloadRequest, String sheetName, Workbook workbook, ExcelStyle ges) throws IOException {

	     Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
	     sheet.setDisplayGridlines(false);
		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }
		Cell cell = r.createCell(1);
        if(cell==null) {
        	cell=r.createCell(1);
        }
		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
		
		if(r==null) {
        	r=sheet.createRow(row);
        }

		String dataType="",currency="", unit="",periodicity="";
		Set<String> companyList = new HashSet<String>();
		Map<String, SortedSet<Date>> distinctDateMap= new TreeMap<String,SortedSet<Date>>();
		Map<String, Object> dataSet = new TreeMap<String, Object>();
		Map<String, String> unitSet = new TreeMap<String, String>();
		Map<String, List<String>> dataTypeList = new TreeMap<String, List<String>>();
		SortedSet<String> distinctCountry = new TreeSet<String>();	
		SortedSet<String> distinctIndicator = new TreeSet<String>();	
		Map<String,String> periodMap = new TreeMap<String,String>();		
		Map<String,String> currencyList = new TreeMap<String,String>();

		for (Iterator<EconomyRequest> economyRequests = downloadRequest.getEconomy().iterator(); economyRequests.hasNext();) {

			EconomyRequest economyRequest =  economyRequests.next();			

			SortedSet<Date> distinctDate = new TreeSet<Date>();	

			if(economyRequest.getType().equals(CMStatic.TYPE_ECONOMY_INDICATOR)){


				String dType = economyRequest.getId();

				if(economyRequest.getPeriodicity()!=null) {
					if(!periodicity.contains(economyRequest.getPeriodicity())) {
						periodicity+=economyRequest.getPeriodicity()+",";
					}
				}

				dataType += dType+",";	

				//	if(!isRiskPremiumList(dType)) {

				List<String> filterList = economyRequest.getFilterList();
				List<String> countryList = new ArrayList<String>();

				for(String countryIsoCode : filterList) {
					countryList.add(economyService.getCountryByIsoCode(countryIsoCode).getCountryName());
				}


				economyRequest.getEconomyData().forEach(economyDataObjectList -> {

					//dataType--Date--Country
					String key = dType+"--"+dateFormat.format(economyDataObjectList.getPeriod())+"--"+economyDataObjectList.getCountry();
					dataSet.put(key, economyDataObjectList.getData());
					distinctDate.add(economyDataObjectList.getPeriod());						
					dataTypeList.put(dType, countryList);
					distinctDateMap.put(dType, distinctDate);

					//currencyList.put(dType+"|"+economyDataObjectList.getCountryName(), economyDTO.getCurrency());
					currencyList.put(dType+"|"+economyDataObjectList.getCountry(),null);

					unitSet.put(dType+"|"+economyDataObjectList.getCountry(), economyDataObjectList.getUnit());
					periodMap.put(dType, economyRequest.getPeriodicity());

				});



				/*for(Iterator<IndicatorDataDTO_old> indicatorDataDTOs = economyRequest.getEconomyData().iterator(); indicatorDataDTOs.hasNext();) {
						IndicatorDataDTO_old economyDTO = indicatorDataDTOs.next();
						//dataType--Date--Country
						String key = dType+"--"+dateFormat.format(economyDTO.getPeriod())+"--"+economyDTO.getCountryName();
						dataSet.put(key, economyDTO.getData());
						distinctDate.add(economyDTO.getPeriod());						
						dataTypeList.put(dType, countryList);
						distinctDateMap.put(dType, distinctDate);
						currencyList.put(dType+"|"+economyDTO.getCountryName(), economyDTO.getCurrency());
						unitSet.put(dType+"|"+economyDTO.getCountryName(), economyDTO.getUnit());
						periodMap.put(dType, economyRequest.getPeriodicity());
					}	*/
				//	}else {
				/*for(Iterator<IndicatorDataDTO_old> indicatorDataDTOs = economyRequest.getEconomyData().iterator(); indicatorDataDTOs.hasNext();) {
						IndicatorDataDTO_old economyDTO = indicatorDataDTOs.next();
						//dataType--Country
						String key = economyDTO.getName().toUpperCase()+"--"+economyDTO.getCountryName();
						if(economyDTO.getName().equalsIgnoreCase(CMStatic.ECONOMY_CREDIT_RATING)) {
							dataSet.put(key, economyDTO.getRating());
						}else { 
							dataSet.put(key, economyDTO.getData());
						}
						unitSet.put(key, economyDTO.getUnit());
						distinctCountry.add(economyDTO.getCountryName());
						distinctIndicator.add(economyDTO.getName().toUpperCase());
					}*/

				//}							
			}

			if(!periodicity.equals(""))periodicity = periodicity.substring(0,periodicity.length()-1).toUpperCase();
			if(!dataType.equals(""))dataType = dataType.substring(0,dataType.length()-1);

			createInfo(companyList, downloadRequest.getStartDate(), downloadRequest.getEndDate(), dataType, currency, periodicity,unit,sheet, row+1, ges );

			createMacroEconomicMetricTable(row+9, sheet, ges, distinctDateMap, dataSet, periodMap, dataTypeList, currencyList, unitSet, distinctCountry,distinctIndicator);				

		}

	}

	private int createMacroEconomicMetricTable(int row, Sheet sheet, ExcelStyle ges,Map<String, SortedSet<Date>> distinctDateMap ,Map<String, Object> dataSet, Map<String,String> periodMap, Map<String, List<String>> dataTypeList, Map<String,String> currencyList, Map<String, String> unitSet, SortedSet<String> distinctCountry , SortedSet<String> distinctIndicatorType) throws IOException {

		_log.info("creating header in row number: "+ row);

		Row r;
		Cell cell;
		int cellIndex=1;
		int oc = cellIndex;
		int or = row;
		boolean firstDataType=true;
		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;
		int minRow = 0;

		if(dataTypeList!=null && dataTypeList.size()!=0) {

			for(String dataType : dataTypeList.keySet()) {

				List<String> filterList = dataTypeList.get(dataType);

				Collections.sort(filterList);

				String periodicity = periodMap.get(dataType);

				String indicatorName = dataType.replaceAll(CMStatic.NUMERIC_REGEX, "");

				cellIndex = oc;
				if(!firstDataType) {
					row = row+3;
				}
				or = row;

				if(row > maxRow-1) {
					minRow = maxRow;
					maxRow = maxRow+addRow;
				//	ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
				}

				SortedSet<Date> distinctDate = distinctDateMap.get(dataType);



				r = sheet.getRow( row-1 );
				if(r==null) {
                	r=sheet.createRow(row-1);
                }
				cell = r.getCell(cellIndex);
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, (short) (19 * 500));
				cell.setCellValue(periodicity+" "+indicatorName);
				cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);



				//CREATE HEADER
				r = sheet.getRow( row );
				if(r==null) {
                	r=sheet.createRow(row);
                }
				cell = r.getCell(cellIndex);
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}
				ges.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);	
				cell.setCellValue(CMStatic.COUNTRIES+" ("+indicatorName+")");
				cell.setCellValue(CMStatic.DATE);
				cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
				cellIndex++;

				//END HEADER

				for(Iterator<String> filters = filterList.iterator();filters.hasNext();) {

					String country = filters.next();
					String currUnit = "";
					String currencyKey = dataType+"|"+country;

					String currency = currencyList.get(currencyKey)==null?"":currencyList.get(currencyKey);

					String unit = unitSet.get(currencyKey)==null?"":unitSet.get(currencyKey);

					if(indicatorName.equalsIgnoreCase(CMStatic.ECONOMY_FX)) {
						currUnit = currency+" "+CMStatic.USD_TEXT;
					}else {
						currUnit = currency+" "+unit;
					}

					row = or;

					r = sheet.getRow( row );
					if(r==null) {
                    	r=sheet.createRow(row);
                    }

					cell = r.getCell(cellIndex);
					
					if(cell==null) {
						cell=r.createCell(cellIndex);
					}

					if(currUnit.equals("")) {
						ges.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);
					}
					cell.setCellValue(country);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					row++;

					if(!currUnit.equals("")) {
						r = sheet.getRow( row );
						if(r==null) {
	                    	r=sheet.createRow(row);
	                    }
						cell = r.getCell(cellIndex);
						if(cell==null) {
							cell=r.createCell(cellIndex);
						}
						cell.setCellValue(currUnit);
						cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);	
					}
					cellIndex++;
				}

				row++;

				if(distinctDate!=null && distinctDate.size()!=0) {

					for(Iterator<Date> dateList = distinctDate.iterator(); dateList.hasNext();) { 
						cellIndex = oc;
						Date colDate = dateList.next();	
						if(row > maxRow-1) {
							minRow = maxRow;
							maxRow = maxRow+addRow;
							//ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
						}

						r = sheet.getRow( row );
						if(r==null) {
	                    	r=sheet.createRow(row);
	                    }
						cell = r.getCell(cellIndex);
						if(cell==null) {
							cell=r.createCell(cellIndex);
						}
						cell.setCellValue(dateFormat.format(colDate));
						if(! dateList.hasNext() ){
							cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT);	
						}else{
							cell.setCellStyle(ges.BORDER_LEFT_RIGHT);
						}
						cellIndex++;
						StringBuilder combine = new StringBuilder();
						for(Iterator<String> filters = filterList.iterator();filters.hasNext();) {
							String country = filters.next();
							String currencyKey = dataType+"|"+country;
							String unit = unitSet.get(currencyKey)==null?"":unitSet.get(currencyKey);
							//Key : dataType--Date--Country
							combine.setLength(0);
							combine.append(dataType).append("--").append(dateFormat.format(colDate)).append("--"+country);
							if(dataSet.containsKey(combine.toString())) {						
								r =  sheet.getRow( row);
								if(r==null) {
			                    	r=sheet.createRow(row);
			                    }
								cell = r.getCell(cellIndex);
								if(cell==null) {
									cell=r.createCell(cellIndex);
								}
								sheet.setColumnWidth(cellIndex, (short) (12 * 500));
								if(dataSet.get(combine.toString()) != null) {
									if(!indicatorName.equalsIgnoreCase(CMStatic.ECONOMY_CREDIT_RATING)){
										Double cellVal = (Double)dataSet.get(combine.toString());
										cell.setCellValue(cellVal);
										if(! dateList.hasNext() ){											
											cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ges, true));
										}else{											
											cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ges, false));
										}
									}else {
										String cellVal = (String)dataSet.get(combine.toString());
										cell.setCellValue(cellVal);
										if(! dateList.hasNext() ){
											cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);	
										}else{
											cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
										}
									}


								}else {
									cell.setCellValue(CMStatic.NA);
									if(! dateList.hasNext() ){
										cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);	
									}else{
										cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
									}
								}
								cellIndex++;
							}else {

								r =  sheet.getRow( row);
								if(r==null) {
			                    	r=sheet.createRow(row);
			                    }
								cell = r.getCell(cellIndex);
								if(cell==null) {
									cell=r.createCell(cellIndex);
								}
								sheet.setColumnWidth(cellIndex, (short) (12 * 500));

								cell.setCellValue(CMStatic.NA);

								if(! dateList.hasNext() ){
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
								cellIndex++;
							}
						}
						row++;
					}
				}else {
					r =  sheet.getRow( row-1);
					if(r==null) {
                    	r=sheet.createRow(row-1);
                    }
					cell = r.getCell(cellIndex+1);
					if(cell==null) {
						cell=r.createCell(cellIndex+1);
					}
					cell.setCellValue(CMStatic.NO_DATA_FOUND);
					cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
				}

				//sheet.groupRow(or, row);

				/*if(dataTypeList.size()!=1) {
					sheet.setRowGroupCollapsed(or, true);
				}*/

				//sheet.setRowSumsBelow(false);
				firstDataType=false;

			}	
		}

		/*		if(distinctCountry !=null && distinctCountry.size()!=0) {
			row = row+3;
			cellIndex = oc;

			if(row > maxRow-1) {
				minRow = maxRow;
				maxRow = maxRow+addRow;
				ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
			}

			int i=0;
			for(Iterator<String> countries = distinctCountry.iterator(); countries.hasNext();) {
				String country = countries.next();
				cellIndex = oc;					
				if(i==0) {
					r = sheet.getRow( row-1 );
					cell = r.getCell(cellIndex);
					ge.mergeCells(sheet, row-1, row, cellIndex, cellIndex,true);	
					cell.setCellValue(CMStatic.COUNTRIES);
					cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
					cellIndex++;

				}
				int j=0;
				StringBuilder combine = new StringBuilder();
				for(Iterator<String> dataTypes = distinctIndicatorType.iterator(); dataTypes.hasNext();) {
					String indicator = dataTypes.next();
					combine.setLength(0);
					combine.append(indicator).append("--").append(country);
					String unit = unitSet.get(combine.toString())==null?"":unitSet.get(combine.toString());

					if(i==0) {
						r = sheet.getRow( row-1 );
						cell = r.getCell(cellIndex);
						ge.mergeCells(sheet, row-1, row, cellIndex, cellIndex,true);
						if(!unit.equals(""))
							cell.setCellValue(indicator+" ( "+unit+" ) ");
						else
							cell.setCellValue(indicator);
						cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
						//cellIndex++;							
					}


					if(j==0) {
						cellIndex = oc;											
						r = sheet.getRow( row+1 );
						cell = r.getCell(cellIndex);
						cell.setCellValue(country);
						if(! countries.hasNext() ){
							cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT);	
						}else{
							cell.setCellStyle(ge.BORDER_LEFT_RIGHT);
						}
						cellIndex++;
					}
					if(dataSet.containsKey(combine.toString())) {						
						r =  sheet.getRow( row+1);
						cell = r.getCell(cellIndex);
						sheet.setColumnWidth(cellIndex, (short) (12 * 500));
						if(dataSet.get(combine.toString()) != null) {
							if(!indicator.equalsIgnoreCase(CMStatic.ECONOMY_CREDIT_RATING)){
								Double cellVal = (Double)dataSet.get(combine.toString());
								cell.setCellValue(cellVal);
								if(! countries.hasNext() ){											
									cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ge, true));
								}else{											
									cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ge, false));
								}
							}else {
								String cellVal = (String)dataSet.get(combine.toString());
								cell.setCellValue(cellVal);
								if(! countries.hasNext() ){
									cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}
							}


						}else {
							cell.setCellValue(CMStatic.NA);
							if(! countries.hasNext() ){
								cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
							}else{
								cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
							}
						}
						cellIndex++;
					}else {

						r =  sheet.getRow( row+1);
						cell = r.getCell(cellIndex);
						sheet.setColumnWidth(cellIndex, (short) (12 * 500));

						cell.setCellValue(CMStatic.NA);

						if(! countries.hasNext() ){
							cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
						}else{
							cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
						}	
						cellIndex++;
					}
					j=1;
				}
				i=1;
				row++;
			}
		}*/

		return row;
	}

	/**
	 * 
	 * @param downloadRequest
	 * @param sheetName
	 * @param workbook
	 * @param ges
	 * @throws IOException
	 */
	private void createMacroEconomicCountrySheet(DownloadRequest downloadRequest, String sheetName, Workbook workbook, ExcelStyle ges) throws IOException {

	    Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		/*ges.setBackgroundOnSheet(sheet,0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); */
	    sheet.setDisplayGridlines(false);
		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
			r=sheet.createRow(row);
		}
		Cell cell = r.createCell(1);
       if(cell==null) {
    	   cell=r.createCell(1);
       }
		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
		if(r==null) {
			r=sheet.createRow(row);
		}
		String dataType="",currency="", unit="",periodicity="";

		Map<String,String> periodMap = new TreeMap<String,String>();
		Set<String> companyList = new HashSet<String>();
		Map<String, SortedSet<Date>> distinctDateMap= new TreeMap<String,SortedSet<Date>>();
		Map<String, Object> dataSet = new TreeMap<String, Object>();
		Map<String, String> unitSet = new TreeMap<String, String>();
		Map<String, List<String>> dataTypeList = new TreeMap<String, List<String>>();
		Map<String,String> currencyList = new TreeMap<String,String>();
		SortedSet<String> distinctCountry = new TreeSet<String>();	
		SortedSet<String> distinctIndicator = new TreeSet<String>();	
		SortedSet<Date> distinctFirstDate = new TreeSet<Date>();

		Date startDate =downloadRequest.getStartDate();
		Date endDate = downloadRequest.getEndDate();


		for (Iterator<EconomyRequest> economyRequests = downloadRequest.getEconomy().iterator(); economyRequests.hasNext();) {

			EconomyRequest economyRequest =  economyRequests.next();
			SortedSet<Date> distinctDate = new TreeSet<Date>();


			if(economyRequest.getType().equals(CMStatic.TYPE_ECONOMY_COUNTRY)){

				List<String> filterList = economyRequest.getFilterList();			

				List<String> indicatorList= economyRequest.getFilterList();

				String dType = economyService.getCountryByIsoCode(economyRequest.getId()).getCountryName();

				if(economyRequest.getPeriodicity()!=null) {
					if(!periodicity.contains(economyRequest.getPeriodicity())) {
						periodicity+=economyRequest.getPeriodicity()+",";
					}	
				}

				if(!dataType.contains(dType)) {
					dataType += dType+",";
				}


				if(economyRequest.getEconomyData()!=null && economyRequest.getEconomyData().size()!=0) {

					economyRequest.getEconomyData().forEach(indicatorHistoricalData -> {

						String indicator = indicatorHistoricalData.getCategory();

						indicator = indicator.toUpperCase();


						//if(!isRiskPremiumList(indicator)) {

						//dataType--Date--Indicator
						String key = dType+"--"+dateFormat.format(indicatorHistoricalData.getPeriod())+"--"+indicator;

						dataSet.put(key, indicatorHistoricalData.getData());

						distinctDate.add(indicatorHistoricalData.getPeriod());						

						//	currencyList.put(dType+"|"+indicator, economyDTO.getCurrency());

						currencyList.put(dType+"|"+indicator, null);


						//unitSet.put(dType+"|"+indicator, economyDTO.getUnit());

						unitSet.put(dType+"|"+indicator, indicatorHistoricalData.getUnit());

						dataTypeList.put(dType+"|"+indicator, indicatorList);
						distinctDateMap.put(dType+"|"+indicator, distinctDate);
						periodMap.put(dType+"|"+indicator, economyRequest.getPeriodicity());
						//_log.info(dType+"|"+indicator+" --> "+economyRequest.getPeriodicity());

					});

					/*for(Iterator<IndicatorHistoricalDataDTO> indicatorDataDTOs = economyRequest.getEconomyData().iterator(); indicatorDataDTOs.hasNext();) {

						IndicatorHistoricalDataDTO economyDTO = indicatorDataDTOs.next();

						String indicator = economyDTO.getCategory();

						indicator = indicator.toUpperCase();

						//if(!isRiskPremiumList(indicator)) {

							//dataType--Date--Indicator
							String key = dType+"--"+dateFormat.format(economyDTO.getPeriod())+"--"+indicator;

							dataSet.put(key, economyDTO.getData());

							distinctDate.add(economyDTO.getPeriod());						

						//	currencyList.put(dType+"|"+indicator, economyDTO.getCurrency());

							currencyList.put(dType+"|"+indicator, "USD");


							//unitSet.put(dType+"|"+indicator, economyDTO.getUnit());

							unitSet.put(dType+"|"+indicator, "%");

							dataTypeList.put(dType+"|"+indicator, indicatorList);
							distinctDateMap.put(dType+"|"+indicator, distinctDate);
							periodMap.put(dType+"|"+indicator, economyRequest.getPeriodicity());

						//}
							else {

							String key = indicator+"--"+economyDTO.getCountryName();

							if(economyDTO.getName().equalsIgnoreCase(CMStatic.ECONOMY_CREDIT_RATING)) {
								dataSet.put(key, economyDTO.getRating());
							}else {
								dataSet.put(key, economyDTO.getData());
							}

							unitSet.put(key, economyDTO.getUnit());
							distinctCountry.add(economyDTO.getCountryName());
							distinctIndicator.add(economyDTO.getName().toUpperCase());

						}	

					}*/				

				}
			}
			distinctFirstDate.add(distinctDate.first());
		}

		if(!periodicity.equals(""))periodicity = periodicity.substring(0,periodicity.length()-1).toUpperCase();
		if(!dataType.equals(""))dataType = dataType.substring(0,dataType.length()-1);

		if(downloadRequest.getStartDate()==null){
			startDate = distinctFirstDate.first();
		}

		if(downloadRequest.getEndDate() == null ){
			endDate=new Date();
		}

		createInfo(companyList, startDate,endDate, dataType, currency, periodicity,unit,sheet, row+1, ges );
		createMacroEconomicCountryTable(row+9, sheet, ges, distinctDateMap, dataSet, periodMap, dataTypeList, currencyList, unitSet, distinctCountry, distinctIndicator);

	}

	private void createMacroEconomicCountryTable(int row, Sheet sheet, ExcelStyle ges,Map<String, SortedSet<Date>> distinctDateMap ,Map<String, Object> dataSet, Map<String,String> periodMap, Map<String, List<String>> dataTypeList,Map<String,String> currencyList, Map<String, String> unitSet, SortedSet<String> distinctCountry, SortedSet<String> distinctIndicatorType) throws IOException {

		_log.info("creating header in row number: "+ row);

		Row r;
		Cell cell;
		int cellIndex=1;
		int oc = cellIndex;
		boolean firstDataType=true;

		int or = row;

		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;

		int minRow = 0;

		//	row = row+2;
		if(dataTypeList!=null && dataTypeList.size()!=0) {

			for(String dataType : dataTypeList.keySet()) {

				List<String> filterList = dataTypeList.get(dataType);
				Collections.sort(filterList);

				String periodicity = periodMap.get(dataType);
				String countryName = dataType.replaceAll(CMStatic.NUMERIC_REGEX, "");

				countryName = countryName.split("\\|")[0];

				SortedSet<Date> distinctDate = distinctDateMap.get(dataType);

				cellIndex = oc;

				if(!firstDataType) {
					row = row+3;
					or = row;
				}

				if(row > maxRow-1) {
					minRow = maxRow;
					maxRow = maxRow+addRow;
					//ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
				}

				//CREATE HEADER
				r = sheet.getRow( row );
                if(r==null) {
                	r=sheet.createRow(row);
                }
				cell = r.getCell(cellIndex);
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, (short) (17 * 500));
				ges.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);	
				cell.setCellValue(CMStatic.PARAMETERS+" ("+countryName+")");
				cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
				cellIndex++;
				//END HEADER

				/**
				 * create black backgound heading
				 */
				for(Iterator<String> filters = filterList.iterator();filters.hasNext();) {

					String indicator = filters.next();
					String currUnit = "";
					String currency = currencyList.get(dataType)==null?"":currencyList.get(dataType);
					String unit = unitSet.get(dataType)==null?"":unitSet.get(dataType);
					if(indicator.equalsIgnoreCase(CMStatic.ECONOMY_FX)) {
						currUnit = currency+" "+CMStatic.USD_TEXT;
					}else {
						currUnit = currency+" "+unit;
					}

					r = sheet.getRow( row-1 );
                    if(r==null) {
                    	r=sheet.createRow(row-1);
                    }
					cell = r.getCell(cellIndex-1);	
					if(cell==null) {
						cell=r.createCell(cellIndex-1);
					}
					cell.setCellValue(periodicity+" "+indicator);
					cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
					//row++;

					if(!currUnit.equals("")) {
						r = sheet.getRow( row );
						if(r==null) {
	                    	r=sheet.createRow(row);
	                    }
						cell = r.getCell(cellIndex);
						if(cell==null) {
							cell=r.createCell(cellIndex);
						}
						ges.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);
						cell.setCellValue(currUnit);
						cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);	
					}
					cellIndex++;row++;
				}

				row++;

				if(distinctDate!=null && distinctDate.size()!=0) {

					for(Iterator<Date> dateList = distinctDate.iterator(); dateList.hasNext();) {

						cellIndex = oc;

						Date colDate = dateList.next();	

						if(row > maxRow-1) {
							minRow = maxRow;
							maxRow = maxRow+addRow;
						//	ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
						}

						/**
						 * Set Date field
						 */
						r = sheet.getRow( row );
						if(r==null) {
	                    	r=sheet.createRow(row);
	                    }
						cell = r.getCell(cellIndex);				
                        if(cell==null) {
                        	cell=r.createCell(cellIndex);
                        }
						cell.setCellValue(dateFormat.format(colDate));

						if(! dateList.hasNext() ){
							cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT);	
						}else{
							cell.setCellStyle(ges.BORDER_LEFT_RIGHT);
						}

						cellIndex++;

						StringBuilder combine = new StringBuilder();

						for(Iterator<String> filters = filterList.iterator();filters.hasNext();) {

							String indicator = filters.next();

							String unit = unitSet.get(dataType)==null?"":unitSet.get(dataType);

							//Key : dataType--Date--Country
							combine.setLength(0);
							combine.append(countryName).append("--").append(dateFormat.format(colDate)).append("--"+indicator.toUpperCase());

							if(dataSet.containsKey(combine.toString())) {						

								r =  sheet.getRow( row);
								if(null==r) {
									r=sheet.createRow(row);
								}
								cell = r.getCell(cellIndex);
                                if(cell==null) {
                                	cell=r.createCell(cellIndex);
                                }
								sheet.setColumnWidth(cellIndex, (short) (12 * 500));

								if(dataSet.get(combine.toString()) != null) {									

									//try {
									if(!indicator.equalsIgnoreCase(CMStatic.ECONOMY_CREDIT_RATING)){
										Double cellVal = (Double)dataSet.get(combine.toString());
										cell.setCellValue(cellVal);
										if(! dateList.hasNext() ){											
											cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ges, true));
										}else{											
											cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ges, false));
										}
									}else {
										String cellVal = (String)dataSet.get(combine.toString());
										cell.setCellValue(cellVal);
										if(! dateList.hasNext() ){
											cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
										}else{
											cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
										}
									}
								}else {
									cell.setCellValue(CMStatic.NA);
									if(! dateList.hasNext() ){
										cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
									}else{
										cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
									}
								}
								cellIndex++;
							}else {

								r =  sheet.getRow( row);
								if(r==null) {
			                    	r=sheet.createRow(row);
			                    }
								cell = r.getCell(cellIndex);
								if(cell==null) {
									cell=r.createCell(cellIndex);
								}
								cell.setCellValue(CMStatic.NA);
								sheet.setColumnWidth(cellIndex, (short) (12 * 500));

								if(! dateList.hasNext() ){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}	
								cellIndex++;
							}
						}
						row++;
					}
				}else {
					r =  sheet.getRow( row-1);
					if(r==null) {
                    	r=sheet.createRow(row-1);
                    }
					cell = r.getCell(cellIndex+1);	
					if(cell==null) {
						cell=r.createCell(cellIndex+1);
					}
					cell.setCellValue(CMStatic.NO_DATA_FOUND);
					cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
				}
				sheet.groupRow(or, row);
				if(dataTypeList.size()!=1) {
					sheet.setRowGroupCollapsed(or, false);
				}
				firstDataType=false;				

			}		
		}


		/*if(distinctCountry !=null && distinctCountry.size()!=0) {

			row = row+3;
			or = row;
			cellIndex = oc;

			if(row > maxRow-1) {
				minRow = maxRow;
				maxRow = maxRow+addRow;
				ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
			}

			int i=0;
			for(Iterator<String> countries = distinctCountry.iterator(); countries.hasNext();) {
				String country = countries.next();
				cellIndex = oc;					
				if(i==0) {
					r = sheet.getRow( row-1 );
					cell = r.getCell(cellIndex);
					ge.mergeCells(sheet, row-1, row, cellIndex, cellIndex,true);	
					cell.setCellValue(CMStatic.COUNTRIES);
					cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
					cellIndex++;

				}
				int j=0;

				StringBuilder combine = new StringBuilder();

				for(Iterator<String> dataTypes = distinctIndicatorType.iterator(); dataTypes.hasNext();) {

					String indicator = dataTypes.next();
					combine.setLength(0);
					combine.append(indicator).append("--").append(country);
					String unit = unitSet.get(combine.toString())==null?"":unitSet.get(combine.toString());

					if(i==0) {
						r = sheet.getRow( row-1 );
						cell = r.getCell(cellIndex);
						ge.mergeCells(sheet, row-1, row, cellIndex, cellIndex,true);	
						if(!unit.equals(""))
							cell.setCellValue(indicator+" ( "+unit+" ) ");
						else
							cell.setCellValue(indicator);
						cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
						//cellIndex++;							
					}


					if(j==0) {

						cellIndex = oc;											
						r = sheet.getRow( row+1 );
						cell = r.getCell(cellIndex);
						cell.setCellValue(country);
						if(! countries.hasNext() ){
							cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT);	
						}else{
							cell.setCellStyle(ge.BORDER_LEFT_RIGHT);
						}
						cellIndex++;
					}

					if(dataSet.containsKey(combine.toString())) {						
						r =  sheet.getRow( row+1);
						cell = r.getCell(cellIndex);
						sheet.setColumnWidth(cellIndex, (short) (12 * 500));
						if(dataSet.get(combine.toString()) != null) {
							if(!indicator.equalsIgnoreCase(CMStatic.ECONOMY_CREDIT_RATING)){
								Double cellVal = (Double)dataSet.get(combine.toString());
								cell.setCellValue(cellVal);
								if(! countries.hasNext() ){											
									cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ge, true));
								}else{											
									cell.setCellStyle(getNumberFormatStyle(cellVal, unit, ge, false));
								}
							}else {
								String cellVal = (String)dataSet.get(combine.toString());
								cell.setCellValue(cellVal);
								if(! countries.hasNext() ){
									cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}
							}


						}else {
							cell.setCellValue(CMStatic.NA);
							if(! countries.hasNext() ){
								cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
							}else{
								cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
							}
						}
						cellIndex++;
					}else {

						r =  sheet.getRow( row+1);
						cell = r.getCell(cellIndex);
						sheet.setColumnWidth(cellIndex, (short) (12 * 500));

						cell.setCellValue(CMStatic.NA);

						if(! countries.hasNext() ){
							cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
						}else{
							cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
						}	
						cellIndex++;
					}
					j=1;
				}
				i=1;				
				row++;
			}
		}*/
	}


	private void createCompanyBetaSheet(DownloadRequest downloadRequest, String sheetName, Workbook workbook, ExcelStyle ges) throws IOException {
		Sheet sheet = workbook.createSheet(sheetName.replaceAll(CMStatic.SPECIAL_CHAR_REGEX, ""));
		//ges.setBackgroundOnSheet(sheet, 0, CMStatic.SHEET_MAX_ROW, CMStatic.SHEET_MAX_COLUMN); 
		sheet.setDisplayGridlines(false);
		int row = 0;
		Row r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }
		Cell cell = r.createCell(1);
        if(cell==null) {
        	cell=r.createCell(1);
        }
		createLogo(cell, sheet, workbook, ges);		

		row++;
		r = sheet.getRow(row);
		if(r==null) {
        	r=sheet.createRow(row);
        }
		Map<String,String> periodMap = new TreeMap<String,String>();
		Map<String, List<Double>> dataSet = new TreeMap<String, List<Double>>();
		Map<String, SortedSet<String>> distinctCompany = new TreeMap<String,SortedSet<String>>();
		Map<String, Integer> compYrCount = new TreeMap<String,Integer>();
		for (Iterator<EquityRequest> equityRequests = downloadRequest.getEquity().iterator(); equityRequests.hasNext();) {
			EquityRequest equityRequest =  equityRequests.next();	
			SortedSet<String> distinctIndex = new TreeSet<String>();
			int maxcount=0;
			if(equityRequest.getDataType().equals(CMStatic.DATA_TYPE_BETA)){
				for(Iterator<BetaData> betaDatas = equityRequest.getBetaDatas().iterator(); betaDatas.hasNext();) {
					BetaData betaData = betaDatas.next();
					//dataType--Country
					String key = betaData.getIndexId()+"--"+equityRequest.getCode()+"--"+equityRequest.getExchange();
					dataSet.put(key, betaData.getData());	
					if(betaData.getData()!=null && betaData.getData().size()>maxcount) {
						maxcount = betaData.getData().size();
					}
					if(betaData.getData()!=null && betaData.getData().size()!=0)
						distinctIndex.add(betaData.getIndexId()+"|"+betaData.getIndexName());
				}
				String compName = equityRequest.getName();
				distinctCompany.put(equityRequest.getCode()+"|"+compName+"|"+equityRequest.getExchange(), distinctIndex);
				compYrCount.put(equityRequest.getCode()+"|"+compName+"|"+equityRequest.getExchange(), maxcount);
				periodMap.put(equityRequest.getCode()+"|"+compName+"|"+equityRequest.getExchange(), equityRequest.getPeriodicity());
			}
		}
		createCompanyBetaTable(row+4, sheet, ges, dataSet, distinctCompany, compYrCount, periodMap);				

	}

	public boolean isRiskPremiumList(String indicatorType) {
		List<String> riskPremiumList = new ArrayList<String>();
		riskPremiumList.add(CMStatic.ECONOMY_CREDIT_RATING);
		riskPremiumList.add(CMStatic.ECONOMY_COUNTRY_DEFAULT_SPREAD);
		riskPremiumList.add(CMStatic.ECONOMY_EQUITY_RISK_PREMIUM);
		riskPremiumList.add(CMStatic.ECONOMY_COUNTRY_RISK_PREMIUM);

		if(riskPremiumList.contains(indicatorType.toUpperCase())) {
			return true;
		}
		return false;
	}


	private void createCompanyBetaTable(int row, Sheet sheet, ExcelStyle ges,Map<String, List<Double>> dataSet, Map<String, SortedSet<String>> distinctCompany, Map<String, Integer> compYrCount, Map<String,String> periodMap) throws IOException {

		_log.info("creating header in row number: "+ row);
		Row r;
		Cell cell;
		int cellIndex=1;
		int oc = cellIndex;
		boolean firstCompany=true;
		int or = row;
		int maxRow = CMStatic.SHEET_MAX_ROW;
		int addRow = CMStatic.SHEET_ADD_ROW;
		int minRow = 0;
		if(distinctCompany!=null && distinctCompany.size()!=0) {
			for(String companyDetails : distinctCompany.keySet()) {
				String compCode = companyDetails.split("\\|")[0];
				String compName = companyDetails.split("\\|")[1];
				String exchangeName = companyDetails.split("\\|")[2];
				Integer yrCount = compYrCount.get(companyDetails);
				SortedSet<String> indexList = distinctCompany.get(companyDetails);
				String periodicity = periodMap.get(companyDetails);

				cellIndex = oc;
				if(!firstCompany) {
					row = row+5;
				}

				if(row > maxRow-1) {
					minRow = maxRow;
					maxRow = maxRow+addRow;
					//ge.setBackgroundOnSheet(sheet, minRow, maxRow, CMStatic.SHEET_MAX_COLUMN);
				}

				r = sheet.getRow( row-2 );
				if(r==null) {
                	r=sheet.createRow(row-2);
                }
				cell = r.getCell(cellIndex);
				if(cell==null) {
					cell=r.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, (short) (16 * 500));
				ges.mergeCells(sheet, row-2, row-1, cellIndex, cellIndex,true);	
				cell.setCellValue(compName+" ( "+exchangeName+" )");
				cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);

				row=row+2;
				if(yrCount!=0) {
					for(int i=yrCount ; i>0 ; i--) {
						cellIndex=oc;
						if(i==yrCount) {
							r = sheet.getRow( row-2 );
							if(r==null) {
		                    	r=sheet.createRow(row-2);
		                    }
							cell = r.getCell(cellIndex);
							if(cell==null) {
								cell=r.createCell(cellIndex);
							}
							sheet.setColumnWidth(cellIndex, (short) (16 * 500));
							ges.mergeCells(sheet, row-2, row-1, cellIndex, cellIndex,true);	
							cell.setCellValue(periodicity+" "+CMStatic.BETA);
							cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
							//cellIndex++;
						}

						r =  sheet.getRow(row);
						if(r==null) {
	                    	r=sheet.createRow(row);
	                    }
						cell = r.getCell(cellIndex);
						if(cell==null) {
							cell=r.createCell(cellIndex);
						}
						cell.setCellValue(i+" "+CMStatic.YEARS);
						if(i==1 ){
							cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT);	
						}else{
							cell.setCellStyle(ges.BORDER_LEFT_RIGHT);
						}
						//cellIndex++;

						cellIndex = oc+1;

						for(Iterator<String> indexes = indexList.iterator();indexes.hasNext();) {
							String indexKey = indexes.next();
							String indexName = indexKey.split("\\|")[1];
							String indexId = indexKey.split("\\|")[0];
							String key = indexId+"--"+compCode+"--"+exchangeName;
							List<Double> dataList = dataSet.get(key);
							if(i==yrCount) {
								r = sheet.getRow( row-2 );
								if(r==null) {
			                    	r=sheet.createRow(row-2);
			                    }
								cell = r.getCell(cellIndex);
								if(cell==null) {
									cell=r.createCell(cellIndex);
								}
								sheet.setColumnWidth(cellIndex, (short) (14 * 500));
								ges.mergeCells(sheet, row-2, row-1, cellIndex, cellIndex,true);	
								cell.setCellValue(indexName);
								cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_BORDER_LEFT_RIGHT_BUTTOM_CENTER);
								//cellIndex++;
							}


							r =  sheet.getRow(row);
							if(r==null) {
		                    	r=sheet.createRow(row);
		                    }
							cell = r.getCell(cellIndex);
							if(cell==null) {
								cell=r.createCell(cellIndex);
							}
							if(dataList!=null && dataList.size()!=0) {
								if(dataList.size() < i || dataList.get(i-1)==null) {
									cell.setCellValue(CMStatic.NA);
									if(i==1 ){
										cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
									}else{
										cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
									}
								}else {
									Double cellVal = dataList.get(i-1);
									cell.setCellValue(cellVal);
									if(i==1){
										cell.setCellStyle(getNumberFormatStyle(cellVal, "", ges, true));
									}else{
										cell.setCellStyle(getNumberFormatStyle(cellVal, "", ges, false));
									}
								}
							}else {
								cell.setCellValue(CMStatic.NA);
								if(i==1 ){
									cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);	
								}else{
									cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
								}
							}

							cellIndex++;
						}
						row++;
					}
				}else {

					r =  sheet.getRow( row);
					if(r==null) {
                    	r=sheet.createRow(row);
                    }
					cell = r.getCell(cellIndex);
					if(cell==null) {
						cell=r.createCell(cellIndex);
					}
					cell.setCellValue(CMStatic.NO_DATA_FOUND);
					cell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
				}

				firstCompany=false;				

			}		
		}

	}

	public CellStyle getNumberFormatStyle(Double value, String unit, ExcelStyle ges, Boolean isLastRecord) {
		if(isLastRecord) {
			if(unit.contains("%") && value >=1.0) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			}else if(unit.equals("x")) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			}else if(value<10.0) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			}else if(value>=10.0 && value<=999.0) {
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			}else{
				return ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
			}

		}else {
			if(unit.contains("%") && value >=1.0) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			}else if(unit.equals("x")) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			}else if(value<10.0) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_TWO_DECIMAL;
			}else if(value>=10.0 && value<=999.0) {
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL;
			}else{
				return ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL;
			}
		}
	}

}
