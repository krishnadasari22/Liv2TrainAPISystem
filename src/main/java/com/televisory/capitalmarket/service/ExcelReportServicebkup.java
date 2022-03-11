package com.televisory.capitalmarket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class ExcelReportServicebkup {

	static Logger _log = Logger.getLogger(ExcelReportServicebkup.class);
	Calendar cal = null;
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

	public HSSFWorkbook createExcelReport(DownloadRequest downloadRequest) throws JsonGenerationException, JsonMappingException, IOException {


		HSSFWorkbook workbook = new HSSFWorkbook();
		GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
		String imagePath = System.getProperty("user.home")+"/resources/logo-white-transparent.png";

		//CREATE SHEET LIST
		HashMap<String, String> sheetList = new HashMap<String,String>();
		if(downloadRequest.getEquity()!=null && downloadRequest.getEquity().size()!=0) {
			for(int i=0; i<downloadRequest.getEquity().size();i++ ) {
				if(downloadRequest.getEquity().get(i).getType().equals(CMStatic.TYPE_COMPANY) && downloadRequest.getEquity().get(i).getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
					sheetList.put(CMStatic.SHEET_STOCK_PRICE_COMPANY, CMStatic.SHEET_STOCK_PRICE_COMPANY);
				}else if(downloadRequest.getEquity().get(i).getType().equals(CMStatic.TYPE_INDEX) && downloadRequest.getEquity().get(i).getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
					sheetList.put(CMStatic.SHEET_STOCK_PRICE_INDEX, CMStatic.SHEET_STOCK_PRICE_INDEX);
				}
			}
		}

		if(sheetList!=null && sheetList.size()!=0) {
			for ( String sheetName : sheetList.keySet() ) {
				_log.info(sheetName);			
				HSSFSheet sheet = workbook.createSheet(sheetName);
				//ges.setBackgroundOnSheet(sheet, 2000, 100); 

				int row = 0;
				Row r = sheet.getRow(row);
				Cell cell = r.createCell(1);
				short height = 700;
				cell.getRow().setHeight(height);
				cell.setCellStyle(ges.DARK_BLUE_BACKGROUND);
				row++;
				r = sheet.getRow(row);
				//SET LOGO IMAGE
				try {
					excelDesignService.readWorkBook(workbook);
					excelDesignService.setImage(sheet, 0, 1,imagePath , 150, 150);
				} catch (IOException e) {
					_log.error(e);
				}

				// CREATE INFO PART
				String title = "",dataType="",currency="", unit="",periodicity="";
				HashMap<String,List<StockPriceDTO>> stockDataList = new HashMap<String,List<StockPriceDTO>>();
				//HashMap<String,List<StockPriceDTO>> indexStockDataList = new HashMap<String,List<StockPriceDTO>>();
				List<String> filterList = new ArrayList<String>();
				HashMap<String,List<String>> dataTypeList = new HashMap<String,List<String>>();
				for(int i=0; i<downloadRequest.getEquity().size();i++ ) {

					if(sheetName.equals(CMStatic.SHEET_STOCK_PRICE_COMPANY) && downloadRequest.getEquity().get(i).getType().equals(CMStatic.TYPE_COMPANY) && downloadRequest.getEquity().get(i).getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
						filterList = downloadRequest.getEquity().get(i).getFilterList();
						title+= downloadRequest.getEquity().get(i).getName()+",";
						for (String filter : filterList ) {
							if(!dataType.contains(filter)) {
								dataType += filter+",";
							}
						}
						if(!currency.contains(downloadRequest.getEquity().get(i).getName())) {
							//currency+=downloadRequest.getEquity().get(i).getName()+",";
						}

						if(!unit.contains(downloadRequest.getEquity().get(i).getName())) {
							//unit+=downloadRequest.getEquity().get(i).getName()+",";
						}

						if(!periodicity.contains(downloadRequest.getEquity().get(i).getPeriodicity())) {
							periodicity+=downloadRequest.getEquity().get(i).getPeriodicity()+",";
						}
						dataTypeList.put(downloadRequest.getEquity().get(i).getName()+"|"+downloadRequest.getEquity().get(i).getExchange()+"", filterList);
						stockDataList.put(downloadRequest.getEquity().get(i).getName()+"|"+downloadRequest.getEquity().get(i).getExchange()+"",downloadRequest.getEquity().get(i).getStockPriceDTOs());


					}else if(sheetName.equals(CMStatic.SHEET_STOCK_PRICE_INDEX) && downloadRequest.getEquity().get(i).getType().equals(CMStatic.TYPE_INDEX) && downloadRequest.getEquity().get(i).getDataType().equals(CMStatic.DATA_TYPE_STOCK_PRICE)) {
						filterList = downloadRequest.getEquity().get(i).getFilterList();

						title+= downloadRequest.getEquity().get(i).getName()+",";
						for (String filter : filterList ) {
							if(!dataType.contains(filter)) {
								dataType += filter+",";
							}
						}
						if(!currency.contains(downloadRequest.getEquity().get(i).getName())) {
							currency+=downloadRequest.getEquity().get(i).getName()+",";
						}

						if(!unit.contains(downloadRequest.getEquity().get(i).getName())) {
							unit+=downloadRequest.getEquity().get(i).getName()+",";
						}

						if(!periodicity.contains(downloadRequest.getEquity().get(i).getPeriodicity())) {
							periodicity+=downloadRequest.getEquity().get(i).getPeriodicity()+",";
						}
						dataTypeList.put(downloadRequest.getEquity().get(i).getName()+"|"+downloadRequest.getEquity().get(i).getExchange()+"", filterList);						
						stockDataList.put(downloadRequest.getEquity().get(i).getName()+"|"+downloadRequest.getEquity().get(i).getExchange()+"",downloadRequest.getEquity().get(i).getStockPriceDTOs());

					}

				}
				if(!title.equals(""))title = title.substring(0,title.length()-1);
				if(!currency.equals(""))currency = currency.substring(0,currency.length()-1);
				if(!unit.equals(""))unit = unit.substring(0,unit.length()-1);
				if(!periodicity.equals(""))periodicity = periodicity.substring(0,periodicity.length()-1);
				if(!dataType.equals(""))dataType = dataType.substring(0,dataType.length()-1);

				createInfo(title, downloadRequest.getStartDate(), downloadRequest.getEndDate(), dataType, currency, periodicity,unit,sheet, row+1, ges );
				createTableHeader(row+11, sheet, ges, stockDataList,dataTypeList );				
				createTableData(row, sheet, ges, stockDataList, dataTypeList);

				//ges.setAutoWidth(0);

			}
		}
		return workbook;

	}

	private void createTableHeader(int row, HSSFSheet sheet, GenerateExcelStyle ge, HashMap<String,List<StockPriceDTO>> stockDataList, HashMap<String,List<String>> dataTypeList) throws IOException {
		_log.info("creating header in row number: "+ row);
		Row r;
		Cell cell;
		if(stockDataList!=null && stockDataList.size()!=0) {
			int cellIndex = 1;
			for( String companyName : stockDataList.keySet()) {
				List<String> filterList = dataTypeList.get(companyName);
				String compName = companyName.substring(0, companyName.indexOf("|"));

				if(cellIndex==1) {
					r = sheet.getRow( row );
					cell = r.getCell(cellIndex);

					ge.mergeCells(sheet, row, row+1, cellIndex, cellIndex,true);							
					cell.setCellValue(CMStatic.DATE);
					cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
					cellIndex++;
				}

				r = sheet.getRow( row );
				r.setHeight((short) (7.5 * 100));
				cell = r.getCell(cellIndex);

				ge.mergeCells(sheet, row, row, cellIndex, cellIndex+filterList.size()-1,true);	

				cell.setCellValue(compName);
				cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);

				if(filterList!=null){
					for(int f=0; f<filterList.size();f++) {
						r = sheet.getRow( row+1);
						cell = r.getCell(cellIndex);							
						cell.setCellValue(filterList.get(f));
						cell.setCellStyle(ge.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
						cellIndex++;
					}
				}
			}
		}
	}


	private List<Object> createDataSet( int row, HSSFSheet sheet, GenerateExcelStyle ge, HashMap<String,List<StockPriceDTO>> stockDataList,HashMap<String,List<String>> dataTypeList) throws IOException {

		_log.info("createDataSet: "+ row);
		List<Object> finalDataList = new ArrayList<Object>();
		SortedSet<Date> distinctDate = new TreeSet<Date>();
		HashMap<String, List<Double>> dateWiseData = new HashMap<String, List<Double>>();
		if(stockDataList!=null && stockDataList.size()!=0) {
			for( String companyName : stockDataList.keySet()) {
				List<StockPriceDTO> indexStockPrice = stockDataList.get(companyName);
				List<String> filterList = dataTypeList.get(companyName);

				if(indexStockPrice!=null && indexStockPrice.size()!=0) {
					for (int i = 0; i < indexStockPrice.size(); i++) {
						StockPriceDTO rowData = (StockPriceDTO) indexStockPrice.get(i);
						if(rowData!=null){
							distinctDate.add(rowData.getDate());
							List<Double> dataList = new ArrayList<Double>();

							for(String filter : filterList) {
								Double cellValue=0.0D;
								if(filter.equalsIgnoreCase(CMStatic.HIGH)) {
									cellValue = rowData.getHigh();
								}else if(filter.equalsIgnoreCase(CMStatic.LOW)) {
									cellValue = rowData.getLow();
								}else if(filter.equalsIgnoreCase(CMStatic.OPEN)) {
									cellValue = rowData.getOpen();
								}else {
									cellValue = rowData.getClose();
								}
								dataList.add(cellValue);								
							}
							dateWiseData.put(rowData.getDate()+"|"+companyName, dataList);
						}
					}

				}

			}
		}
		System.out.println("distinctDate:: "+distinctDate);
		System.out.println("dateWiseData:: "+dateWiseData);
		finalDataList.add(distinctDate);
		finalDataList.add(dateWiseData);
		return finalDataList;

	}
	@SuppressWarnings("unchecked")
	private void printTableData( int row, HSSFSheet sheet, GenerateExcelStyle ge, List<Object> finalDataList) throws IOException {

		Row r;
		Cell cell;
		_log.info("creating header in row number: "+ row);
		int orignalRow=row;

		int cellIndex = 1;
		int oc = cellIndex;
		boolean firstC = true;

		if(finalDataList!=null && finalDataList.size()!=0) {			
			SortedSet<Date> distincDate = (SortedSet<Date>) finalDataList.get(0);
			HashMap<String, List<Object>> dateWiseData = (HashMap<String, List<Object>>) finalDataList.get(1); 

			Iterator<Date> iterator = distincDate.iterator();
			while(iterator.hasNext()){
				Date rowDate = iterator.next();
				r = sheet.getRow( row );
				cell = r.getCell(cellIndex);
				//if(i==indexStockPrice.size()-1) {
					//cell.setCellStyle(ge.DATE_FORMAT_BORDER_BOTTOM);
			//	}else {
					//cell.setCellStyle(ge.DATE_FORMAT_BORDER_LEFT_RIGHT);
			//	}
				cell.setCellValue(rowDate);
				cellIndex++;
				
				//List<Double> dataList = dateWiseData.get("");
				
				
				row++;
			}


			/*for( String companyName : stockDataList.keySet()) {
				row = orignalRow;
				List<StockPriceDTO> indexStockPrice = stockDataList.get(companyName);
				List<String> filterList = dataTypeList.get(companyName);

				if(indexStockPrice!=null && indexStockPrice.size()!=0) {


					for (int i = 0; i < indexStockPrice.size(); i++) {
						cellIndex = oc;
						StockPriceDTO rowData = (StockPriceDTO) indexStockPrice.get(i);

						if(rowData!=null){

							r = sheet.getRow( row );
							if(firstC) {
								cell = r.getCell(cellIndex);
								if(i==indexStockPrice.size()-1) {
									cell.setCellStyle(ge.DATE_FORMAT_BORDER_BOTTOM);
								}else {
									cell.setCellStyle(ge.DATE_FORMAT_BORDER_LEFT_RIGHT);
								}
								cell.setCellValue(rowData.getDate());
								cellIndex++;
							}


							for(String filter : filterList) {
								Double cellValue=0.0D;
								if(filter.equalsIgnoreCase(CMStatic.HIGH)) {
									cellValue = rowData.getHigh();
								}else if(filter.equalsIgnoreCase(CMStatic.LOW)) {
									cellValue = rowData.getLow();
								}else if(filter.equalsIgnoreCase(CMStatic.OPEN)) {
									cellValue = rowData.getOpen();
								}else {
									cellValue = rowData.getClose();
								}

								cell = r.getCell(cellIndex);
								cell.setCellValue(cellValue);
								if(i==indexStockPrice.size()-1) {
									cell.setCellStyle(ge.BORDER_BUTTOM_RIGHT);
								}else if(i==0){
									cell.setCellStyle(ge.BORDER_TOP_RIGHT);
								}else {
									cell.setCellStyle(ge.BORDER_RIGHT);
								}
								cellIndex++;
							}
							row++;



						}
					}

				}
				oc = oc+filterList.size()+1;
				firstC=false;
			}*/
		}

	}



	private void createTableData( int row, HSSFSheet sheet, GenerateExcelStyle ge, HashMap<String,List<StockPriceDTO>> stockDataList,HashMap<String,List<String>> dataTypeList) throws IOException {

		Row r;
		Cell cell;
		_log.info("creating header in row number: "+ row);
		int orignalRow=row;

		if(stockDataList!=null && stockDataList.size()!=0) {
			int cellIndex = 1;
			int oc = cellIndex;
			boolean firstC = true;
			for( String companyName : stockDataList.keySet()) {
				row = orignalRow;
				List<StockPriceDTO> indexStockPrice = stockDataList.get(companyName);
				List<String> filterList = dataTypeList.get(companyName);

				if(indexStockPrice!=null && indexStockPrice.size()!=0) {


					for (int i = 0; i < indexStockPrice.size(); i++) {
						cellIndex = oc;
						StockPriceDTO rowData = (StockPriceDTO) indexStockPrice.get(i);

						if(rowData!=null){

							r = sheet.getRow( row );
							if(firstC) {
								cell = r.getCell(cellIndex);
								if(i==indexStockPrice.size()-1) {
								//	cell.setCellStyle(ge.DATE_FORMAT_BORDER_BOTTOM);
								}else {
									//cell.setCellStyle(ge.DATE_FORMAT_BORDER_LEFT_RIGHT);
								}
								cell.setCellValue(rowData.getDate());
								cellIndex++;
							}


							for(String filter : filterList) {
								Double cellValue=0.0D;
								if(filter.equalsIgnoreCase(CMStatic.HIGH)) {
									cellValue = rowData.getHigh();
								}else if(filter.equalsIgnoreCase(CMStatic.LOW)) {
									cellValue = rowData.getLow();
								}else if(filter.equalsIgnoreCase(CMStatic.OPEN)) {
									cellValue = rowData.getOpen();
								}else {
									cellValue = rowData.getClose();
								}

								cell = r.getCell(cellIndex);
								cell.setCellValue(cellValue);
								if(i==indexStockPrice.size()-1) {
									cell.setCellStyle(ge.BORDER_BUTTOM_RIGHT);
								}else if(i==0){
									cell.setCellStyle(ge.BORDER_TOP_RIGHT);
								}else {
									cell.setCellStyle(ge.BORDER_RIGHT);
								}
								cellIndex++;
							}
							row++;



						}
					}

				}
				oc = oc+filterList.size()+1;
				firstC=false;
			}
		}

	}

	private void createInfo(String heading, Date startDate, Date endDate, String dataType, String currency, String periodicity, String unit, HSSFSheet sheet, int row,GenerateExcelStyle ge){
		Row r;
		Cell cell;

		_log.info("creating Info in row number: "+ row);

		r = sheet.getRow(row);
		cell=r.getCell(1);


		cell.setCellValue(heading.replaceAll(",", "\n"));
		cell.setCellStyle(ge.BLUE_FONT);

		row = row+2;

		r = sheet.getRow(row);
		cell=r.getCell(1);		
		cell.setCellValue(CMStatic.START_DATE);
		cell.setCellStyle(ge.BLUE_FONT);						

		cell=r.getCell(2);		
		cal = Calendar.getInstance();
		cal.setTime(startDate);
		cell.setCellValue(cal);	
		//cell.setCellStyle(ge.DATE_FORMAT);
		row++;

		r = sheet.getRow(row);
		cell=r.getCell(1);		
		cell.setCellValue(CMStatic.END_DATE);
		cell.setCellStyle(ge.BLUE_FONT);


		cell=r.getCell(2);		
		cal = Calendar.getInstance();
		cal.setTime(endDate);

		cell.setCellValue(cal);		
		//cell.setCellStyle(ge.DATE_FORMAT);
		row++;

		r = sheet.getRow(row);
		cell=r.getCell(1);		
		cell.setCellValue(CMStatic.DATA_TYPE);
		cell.setCellStyle(ge.BLUE_FONT);						

		cell=r.getCell(2);		
		cell.setCellValue(dataType);					
		row++;

		if(!currency.equals("")) {
			r = sheet.getRow(row);
			cell=r.getCell(1);		
			cell.setCellValue(CMStatic.CURRENCY);
			cell.setCellStyle(ge.BLUE_FONT);						

			cell=r.getCell(2);		
			cell.setCellValue(currency);					
			row++;
		}

		if(!unit.equals("")) {
			r = sheet.getRow(row);
			cell=r.getCell(1);		
			cell.setCellValue(CMStatic.UNIT);
			cell.setCellStyle(ge.BLUE_FONT);						

			cell=r.getCell(2);		
			cell.setCellValue(unit);					
			row++;
		}


		if(!periodicity.equals("")) {

			r = sheet.getRow(row);
			cell=r.getCell(1);		
			cell.setCellValue(CMStatic.PERIODICITY);
			cell.setCellStyle(ge.BLUE_FONT);						

			r = sheet.getRow(row);
			cell=r.getCell(2);		
			cell.setCellValue(periodicity);						
			row++;
		}

		_log.info("creating header in row number: "+ row);

	}
}
