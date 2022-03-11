package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.pcompany.dto.ChartFundingDetailDTO;
import com.pcompany.dto.ChartIndustryFundingDetailDTO;
import com.pcompany.dto.TopFundedCompaniesDTO;
import com.pcompany.model.PEVCSummaryModel;
import com.privatecompany.dao.PeVcDaoB;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class PeVCServiceB {

	Logger _log = Logger.getLogger(PeVCServiceB.class);

	@Autowired
	PeVcDaoB peVcDaoB;

	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	public List<ChartFundingDetailDTO> getPeVcSummaryChartByCountry(String countryIsoCode, String currency,
			Date startDate, Date endDate) {

		if (startDate == null) {
			startDate = getDefaultStartDate();
		}

		if (endDate == null) {
			endDate = new Date();
		}

		return peVcDaoB.getPeVcSummaryChartByCountry(countryIsoCode, currency, startDate, endDate);
	}

	private Date getDefaultStartDate() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		try {
			return formatter.parse(CMStatic.FACTSET_STOCK_DEFAULT_START_DATE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ChartIndustryFundingDetailDTO> getPeVcSummaryChartByIndustry(String countryIsoCode, String currency,
			Integer sectorCode, Date startDate, Date endDate) {

		if (startDate == null) {
			startDate = getDefaultStartDate();
		}

		if (endDate == null) {
			endDate = new Date();
		}

		return peVcDaoB.getPeVcSummaryChartByIndustry(countryIsoCode, currency, sectorCode, startDate, endDate);
	}

	public List<TopFundedCompaniesDTO> getPeVcTopFundedCompanies(String countryIsoCode, String currency, Date startDate,
			Date endDate, int limit) {

		if (startDate == null) {
			startDate = getDefaultStartDate();
		}

		if (endDate == null) {
			endDate = new Date();
		}

		return peVcDaoB.getPeVcTopFundedCompanies(countryIsoCode, currency, startDate, endDate, limit);
	}

	public List<ChartFundingDetailDTO> getPeVcSummaryChartBySector(String countryIsoCode, String currency,
			Date startDate, Date endDate) {

		if (startDate == null) {
			startDate = getDefaultStartDate();
		}

		if (endDate == null) {
			endDate = new Date();
		}

		return peVcDaoB.getPeVcSummaryChartBySector(countryIsoCode, currency, startDate, endDate);
	}

	public PEVCSummaryModel peVcSummaryDetails(String countryIsoCode, String currency, Date startDate, Date endDate) {

		PEVCSummaryModel summaryDetails = new PEVCSummaryModel();

		if (startDate == null) {
			startDate = getDefaultStartDate();
		}

		if (endDate == null) {
			endDate = new Date();
		}

		summaryDetails.setCountryIsoCode(countryIsoCode);
		summaryDetails.setStartDate(startDate);
		summaryDetails.setEndDate(endDate);
		summaryDetails.setCurrency(currency);

		List<ChartFundingDetailDTO> byCountryFundingDetail = null;
		List<ChartIndustryFundingDetailDTO> byIndustryFundingDetail;
		List<TopFundedCompaniesDTO> topFundedCompanies;

		AtomicDouble sum = new AtomicDouble(0);

		if (countryIsoCode.equals("Global")) {

			byCountryFundingDetail = getPeVcSummaryChartByCountry(countryIsoCode, currency, startDate, endDate);

			if (byCountryFundingDetail.size() > 0) {

				sum.set(byCountryFundingDetail.stream().filter(obj -> obj.getValuation() != null)
						.mapToDouble(ChartFundingDetailDTO::getValuation).sum());

				byCountryFundingDetail.stream().forEach(fundingDetailObject -> {
					if (fundingDetailObject.getValuation() != null) {
						fundingDetailObject.setPercent(fundingDetailObject.getValuation() / sum.get() * 100);
					}
				});
			}
			summaryDetails.setByCountryFundingDetail(byCountryFundingDetail);
		}

		byIndustryFundingDetail = getPeVcSummaryByIndustry(countryIsoCode, currency, startDate, endDate);

		if (byIndustryFundingDetail.size() > 0) {

			sum.set(byIndustryFundingDetail.stream().filter(obj -> obj.getValuation() != null)
					.map(i -> i.getValuation()).reduce(0.0, Double::sum));

			byIndustryFundingDetail.stream().forEach(fundingDetailObject -> {
				if (fundingDetailObject.getValuation() != null) {
					fundingDetailObject.setPercent(fundingDetailObject.getValuation() / sum.get() * 100);
				}
			});
		}

		summaryDetails.setByIndustryFundingDetail(byIndustryFundingDetail);
		topFundedCompanies = getPeVcTopFundedCompanies(countryIsoCode, currency, startDate, endDate, 100);

		if (topFundedCompanies.size() > 0) {

			sum.set(topFundedCompanies.stream().filter(obj -> obj.getTotalValuation() != null)
					.mapToDouble(TopFundedCompaniesDTO::getTotalValuation).sum());

			topFundedCompanies.stream().forEach(fundingDetailObject -> {
				if (fundingDetailObject.getTotalValuation() != null) {
					fundingDetailObject.setPercent(fundingDetailObject.getTotalValuation() / sum.get() * 100);
				}
			});
		}

		summaryDetails.setTopFundedCompanies(topFundedCompanies);
		return summaryDetails;
	}

	private List<ChartIndustryFundingDetailDTO> getPeVcSummaryByIndustry(String countryIsoCode, String currency,
			Date startDate, Date endDate) {

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

		return peVcDaoB.getPeVcSummaryByIndustry(countryIsoCode, currency, startDate, endDate);
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

		if (data != null && !data.equals("null%")) {
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

	public static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	private Double formatNumber(Double value) {

		if (value != null) {
			if (Math.abs(value) < 10.0) {
				return roundAvoid(value, 2);
			} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
				return roundAvoid(value, 1);
			} else {
				return roundAvoid(value, 0);
			}
		}

		return value;

	}

	public HSSFWorkbook createFundingSummaryExcel(PEVCSummaryModel peVcSummaryData) {

		HSSFWorkbook workbook = null;
		GenerateExcelStyle ges;
		HSSFSheet sheet;

		try {

			String sheetName;

			if (peVcSummaryData.getCountryIsoCode().equals("Global")) {
				sheetName = CMStatic.PEVC_SUMMARY_GLOBAL_SHEET_NAME;
			} else {
				sheetName = CMStatic.PEVC_SUMMARY_COUNTRY_SHEET_NAME;
			}

			_log.info(sheetName);

			workbook = new HSSFWorkbook();
			ges = new GenerateExcelStyle(workbook);
			sheet = workbook.createSheet(sheetName);

			sheet.setDisplayGridlines(false);

			_log.info("creating Funding Summary sheet");

			int rowdesign = 0;

			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if (null == r) {
				r = workbook.getSheet(sheetName).createRow(rowdesign);
			}

			Cell celldesign = r.createCell(1);

			workbook.getSheet(sheetName).setColumnWidth(1, 6000);

			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);

			createPeVcSummaryExcelHeader(sheet, peVcSummaryData, ges);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return workbook;
	}

	private void createPeVcSummaryExcelHeader(HSSFSheet sheet, PEVCSummaryModel peVcSummaryData,
			GenerateExcelStyle ges) {

		int currentRowIndex = 4;

		Row headerRow;
		Cell namecell;
		Cell cellVal;

		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		namecell = headerRow.getCell(1) != null ? headerRow.getCell(1) : headerRow.createCell(1);

		cellVal = headerRow.getCell(2) != null ? headerRow.getCell(2) : headerRow.createCell(2);

		namecell.setCellValue("Country");
		namecell.setCellStyle(ges.BOLD_FONT_HEADER);

		if (peVcSummaryData.getCountryIsoCode().equals("Global")) {
			cellVal.setCellValue("World");
		} else {
			if (peVcSummaryData.getTopFundedCompanies().size() > 0) {
				cellVal.setCellValue(peVcSummaryData.getTopFundedCompanies().get(0).getCountry());
			} else {
				cellVal.setCellValue("-");
			}

		}

		currentRowIndex++;

		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		namecell = headerRow.getCell(1) != null ? headerRow.getCell(1) : headerRow.createCell(1);

		cellVal = headerRow.getCell(2) != null ? headerRow.getCell(2) : headerRow.createCell(2);

		namecell.setCellValue("Start Date ");
		namecell.setCellStyle(ges.BOLD_FONT_HEADER);

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		cellVal.setCellValue(formatter.format(peVcSummaryData.getStartDate()));

		currentRowIndex++;

		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		namecell = headerRow.getCell(1) != null ? headerRow.getCell(1) : headerRow.createCell(1);

		cellVal = headerRow.getCell(2) != null ? headerRow.getCell(2) : headerRow.createCell(2);

		namecell.setCellValue("End Date ");
		namecell.setCellStyle(ges.BOLD_FONT_HEADER);

		cellVal.setCellValue(formatter.format(peVcSummaryData.getEndDate()));

		if (peVcSummaryData.getCountryIsoCode().equals("Global")) {

			fillFundingdetailsByCountry(sheet, peVcSummaryData, ges);
			fillFundingdetailsByIndustry(sheet, "Global", peVcSummaryData, ges);
			fillFundingdetailsByTopFundedCompanies(sheet, "Global", peVcSummaryData, ges);

		} else {

			fillFundingdetailsByIndustry(sheet, "Country", peVcSummaryData, ges);
			fillFundingdetailsByTopFundedCompanies(sheet, "Country", peVcSummaryData, ges);

		}
	}

	private void fillFundingdetailsByTopFundedCompanies(HSSFSheet sheet, String sheetType,
			PEVCSummaryModel peVcSummaryData, GenerateExcelStyle ges) {

		int currentRowIndex = 8;

		Row headerRow;

		Cell namecell;
		int startIndex;

		if (sheetType.equals("Global")) {
			startIndex = 10;
		} else {
			startIndex = 7;
		}

		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		namecell = headerRow.getCell(startIndex) != null ? headerRow.getCell(startIndex)
				: headerRow.createCell(startIndex);

		namecell.setCellValue("Top Funded Companies");
		namecell.setCellStyle(ges.BOLD_FONT_HEADER);

		currentRowIndex++;

		int currentColumnIndex = startIndex;

		// Create headers
		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		String cellValue;
		Row currentRow;

		Cell currentCell;

		try {

			cellValue = "Rank";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Company Name";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Country";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Industry";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Financing Type";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Number of Rounds";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Total Funding Amount (" + peVcSummaryData.getCurrency() + "  Million)";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "% of total";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			TopFundedCompaniesDTO topFundedCompaniesObject;

			int currentIteration = 0;

			boolean isLastRecord = false;

			_log.info(peVcSummaryData.getTopFundedCompanies().size());

			currentRowIndex = 11;

			for (int i = 0; i < peVcSummaryData.getTopFundedCompanies().size(); i++) {

				currentRowIndex++;

				if (currentIteration == peVcSummaryData.getTopFundedCompanies().size() - 1) {
					isLastRecord = true;
				}

				currentColumnIndex = startIndex;

				topFundedCompaniesObject = peVcSummaryData.getTopFundedCompanies().get(i);

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, (currentIteration + 1) + "", sheet, currentColumnIndex, ges, isLastRecord,
						"RIGHT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, topFundedCompaniesObject.getEntityName(), sheet, currentColumnIndex, ges,
						isLastRecord, "LEFT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, topFundedCompaniesObject.getCountry(), sheet, currentColumnIndex, ges,
						isLastRecord, "LEFT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, topFundedCompaniesObject.getIndustryName(), sheet, currentColumnIndex, ges,
						isLastRecord, "LEFT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, topFundedCompaniesObject.getEntityType(), sheet, currentColumnIndex, ges,
						isLastRecord, "LEFT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, topFundedCompaniesObject.getRounds() + "", sheet, currentColumnIndex, ges,
						isLastRecord, "RIGHT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, topFundedCompaniesObject.getTotalValuation(), sheet, currentColumnIndex,
						ges, isLastRecord);

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, formatNumber(topFundedCompaniesObject.getPercent()) + "%", sheet,
						currentColumnIndex, ges, isLastRecord, "RIGHT");

				currentIteration++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void fillFundingdetailsByIndustry(HSSFSheet sheet, String sheetType, PEVCSummaryModel peVcSummaryData,
			GenerateExcelStyle ges) {

		int currentRowIndex = 8;

		Row headerRow;
		Cell namecell;
		Cell cellVal;

		int startIndex;

		if (sheetType.equals("Global")) {
			startIndex = 5;
		} else {
			startIndex = 1;
		}

		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		namecell = headerRow.getCell(startIndex) != null ? headerRow.getCell(startIndex)
				: headerRow.createCell(startIndex);

		namecell.setCellValue("VC/PE Funding by Industry");
		namecell.setCellStyle(ges.BOLD_FONT_HEADER);

		currentRowIndex++;

		int currentColumnIndex = startIndex;

		// Create headers
		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		String cellValue;
		Row currentRow;
		Cell currentCell;

		try {

			cellValue = "Industry";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Sector";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Total Funding Amount (" + peVcSummaryData.getCurrency() + "  Million)";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "% of total";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			ChartIndustryFundingDetailDTO industryDetailObject;

			int currentIteration = 0;

			boolean isLastRecord = false;

			for (int i = 0; i < peVcSummaryData.getByIndustryFundingDetail().size(); i++) {

				if (currentIteration == peVcSummaryData.getByIndustryFundingDetail().size() - 1) {
					isLastRecord = true;
				}

				currentRowIndex++;

				currentColumnIndex = startIndex;

				industryDetailObject = peVcSummaryData.getByIndustryFundingDetail().get(i);

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, industryDetailObject.getName(), sheet, currentColumnIndex, ges,
						isLastRecord, "LEFT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, industryDetailObject.getSector(), sheet, currentColumnIndex, ges,
						isLastRecord, "LEFT");

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, industryDetailObject.getValuation(), sheet, currentColumnIndex, ges,
						isLastRecord);

				currentColumnIndex++;

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, formatNumber(industryDetailObject.getPercent()) + "%", sheet,
						currentColumnIndex, ges, isLastRecord, "RIGHT");

				currentIteration++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void fillFundingdetailsByCountry(HSSFSheet sheet, PEVCSummaryModel peVcSummaryData,
			GenerateExcelStyle ges) {

		int currentRowIndex = 8;

		Row headerRow;
		Cell namecell;
		// Cell cellVal;

		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		namecell = headerRow.getCell(1) != null ? headerRow.getCell(1) : headerRow.createCell(1);

		namecell.setCellValue("VC/PE Funding by Country");
		namecell.setCellStyle(ges.BOLD_FONT_HEADER);

		currentRowIndex++;

		int currentColumnIndex = 1;

		// Create headers
		headerRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
				: sheet.createRow(currentRowIndex);

		String cellValue;
		Row currentRow;
		Cell currentCell;

		try {

			cellValue = "Country";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "Total Funding Amount (" + peVcSummaryData.getCurrency() + "  Million)";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			cellValue = "% of total";

			setHeaderCell(sheet, headerRow, currentColumnIndex, currentRowIndex, cellValue, ges);

			currentColumnIndex++;

			ChartFundingDetailDTO countryDetailObject;

			int currentIteration = 0;

			boolean isLastRecord = false;

			for (int i = 0; i < peVcSummaryData.getByCountryFundingDetail().size(); i++) {

				if (currentIteration == peVcSummaryData.getByCountryFundingDetail().size() - 1) {
					isLastRecord = true;
				}

				currentRowIndex++;

				currentColumnIndex = 1;

				countryDetailObject = peVcSummaryData.getByCountryFundingDetail().get(i);

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, countryDetailObject.getName(), sheet, currentColumnIndex, ges, isLastRecord,
						"LEFT");

				currentColumnIndex++;

				countryDetailObject = peVcSummaryData.getByCountryFundingDetail().get(i);

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, countryDetailObject.getValuation(), sheet, currentColumnIndex, ges,
						isLastRecord);

				currentColumnIndex++;

				countryDetailObject = peVcSummaryData.getByCountryFundingDetail().get(i);

				currentRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex)
						: sheet.createRow(currentRowIndex);

				currentCell = currentRow.getCell(currentColumnIndex) != null ? currentRow.getCell(currentColumnIndex)
						: currentRow.createCell(currentColumnIndex);

				setColumnValue(currentCell, formatNumber(countryDetailObject.getPercent()) + "%", sheet,
						currentColumnIndex, ges, isLastRecord, "RIGHT");

				currentIteration++;
			}

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
