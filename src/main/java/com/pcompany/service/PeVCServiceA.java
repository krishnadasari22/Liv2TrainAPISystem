package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pcompany.dto.PEVCFundingRoundDetailsDTO;
import com.pcompany.dto.PEVCInvestmentHeaderDTO;
import com.pcompany.entity.PEVCInvstmentHeader;
import com.privatecompany.dao.PeVcDaoA;
import com.privatecompany.dto.PEVCInvestmentDetailsDTO;
import com.televisory.capitalmarket.dto.AdvancedSearchFundingAmoutDto;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.FinancialTypeDto;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.PevcIssueTypeDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class PeVCServiceA {
	
	@Autowired
	PeVcDaoA peVcDaoA;
	
	@Autowired
	private ExcelDesignService excelDesignService;
	
	Logger _log = Logger.getLogger(PeVCServiceA.class);
	
	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;

	public List<IndustryFinancialDataDTO> getPeVcIndustries(String countryCode, Date startDate,Date endDate) {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcIndustries(countryCode,startDate,endDate);
	}

	public List<CountryListDTO> getPeVcCountries() {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcCountries();
	}

	

	public List<String> getPeVcFinancialType() {
		// TODO Auto-generated method stub
		return CMStatic.PEVC_FINANCIAL_TYPE;
	}

	public List<CompanyDTO> getPeVcCompanies(String searchCriteria, String countryCode, String industry, Date startDate,
			Date endDate) {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcCompanies(searchCriteria,countryCode,industry,startDate,endDate);
	}

	public PEVCFundingRoundDetailsDTO getPeVcFundingRoundDetails(String entityId, String category, Date startDate,
			Date endDate, String financialType,String currencyCode) {
		PEVCFundingRoundDetailsDTO pevcFundingRoundDetailsDTO=new PEVCFundingRoundDetailsDTO();
		PEVCInvestmentHeaderDTO pevcInvestmentHeaderDTO=new PEVCInvestmentHeaderDTO();
		List<PEVCInvestmentDetailsDTO>pevcFundingInvestmentDTOs=peVcDaoA.getPeVcFundingInvestments(entityId,category);
		PEVCInvstmentHeader pevcInvstmentHeader=peVcDaoA.getPeVcInvestmentHeader(entityId,category,startDate,endDate,financialType,currencyCode);
		if(null!=pevcInvstmentHeader) {
		BeanUtils.copyProperties(pevcInvstmentHeader, pevcInvestmentHeaderDTO);
		}
		pevcFundingRoundDetailsDTO.setPevcFundingInvestmentDTOs(pevcFundingInvestmentDTOs);
		pevcFundingRoundDetailsDTO.setPevcInvestmentHeaderDTO(pevcInvestmentHeaderDTO);
		return pevcFundingRoundDetailsDTO;
	}

	public HSSFWorkbook createFundDetailExcel(PEVCFundingRoundDetailsDTO pevcFundingRoundDetailsDTO) {
		_log.info("downloading fund detaild excel");
		_log.info("indownload");
		String sheetName =pevcFundingRoundDetailsDTO.getPevcInvestmentHeaderDTO().getEntityProperName() + " " + pevcFundingRoundDetailsDTO.getPevcInvestmentHeaderDTO().getCategoryNameDesc();
        PEVCInvestmentHeaderDTO pevcInvestmentHeaderDTO=pevcFundingRoundDetailsDTO.getPevcInvestmentHeaderDTO();
		HSSFWorkbook workbook = new HSSFWorkbook();
		GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
		HSSFSheet sheet = workbook.createSheet(sheetName.replace("/", ""));
		sheet.setDisplayGridlines(false);
		
		int rowdesign = 0;
		
		Row r = sheet.getRow(rowdesign);
		if(null==r) {
			r=sheet.createRow(rowdesign);
		}

		Cell celldesign = r.createCell(1);
		sheet.setColumnWidth(1, 10000);

		// create the televisory logo
		createLogo(celldesign,sheet, workbook, ges);
		
		sheet.setColumnWidth(1, 10000);
		
		_log.info("downloading excel file");
		
		
		int currentRowIndex=rowdesign+2;
		
		Row entityRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell entitycell=entityRow.getCell(1) != null ? entityRow.getCell(1) : entityRow.createCell(1);
		entitycell.setCellValue("Entity Name");
		Cell entitycellVal=entityRow.getCell(2) != null ? entityRow.getCell(2) : entityRow.createCell(2);
		entitycell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getEntityProperName()!=null) {
			entitycellVal.setCellValue(pevcInvestmentHeaderDTO.getEntityProperName());
		}
		currentRowIndex++;
		Row countryRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell countrycell=countryRow.getCell(1) != null ? countryRow.getCell(1) : countryRow.createCell(1);
		Cell countrycellVal=countryRow.getCell(2) != null ? countryRow.getCell(2) : countryRow.createCell(2);
		countrycell.setCellValue("Country");
		countrycell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getCountryName()!=null) {
			countrycellVal.setCellValue(pevcInvestmentHeaderDTO.getCountryName());
		}
		
		currentRowIndex++;
		Row industryRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell industrycell=industryRow.getCell(1) != null ? industryRow.getCell(1) : industryRow.createCell(1);
		industrycell.setCellValue("Industry");
		industrycell.setCellStyle(ges.BOLD_FONT_HEADER);
		Cell industrycellVal=industryRow.getCell(2) != null ? industryRow.getCell(2) : industryRow.createCell(2);
		if(pevcInvestmentHeaderDTO.getTicsIndustryName()!=null) {
			industrycellVal.setCellValue(pevcInvestmentHeaderDTO.getTicsIndustryName());
		}
		
		currentRowIndex++;
		
		Row roundRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell roundcell=roundRow.getCell(1) != null ? roundRow.getCell(1) : roundRow.createCell(1);
		Cell roundcellVal=roundRow.getCell(2) != null ? roundRow.getCell(2) : roundRow.createCell(2);
		roundcell.setCellValue("Round Name");
		roundcell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getCategoryNameDesc()!=null) {
			roundcellVal.setCellValue(pevcInvestmentHeaderDTO.getCategoryNameDesc());
		}
		currentRowIndex++;
		Row issueRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell issuecell=issueRow.getCell(1) != null ? issueRow.getCell(1) : issueRow.createCell(1);
		Cell issuecellVal=issueRow.getCell(2) != null ? issueRow.getCell(2) : issueRow.createCell(2);
		issuecell.setCellValue("Issue Type");
		issuecell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getIssueType()!=null) {
			issuecellVal.setCellValue(pevcInvestmentHeaderDTO.getIssueType());
		}
		currentRowIndex++;
		
		Row financeRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell financecell=financeRow.getCell(1) != null ? financeRow.getCell(1) : financeRow.createCell(1);
		Cell financecellVal=financeRow.getCell(2) != null ? financeRow.getCell(2) : financeRow.createCell(2);
		financecell.setCellValue("Finance Type");
		financecell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getPortcoFinType()!=null) {
			financecellVal.setCellValue(pevcInvestmentHeaderDTO.getPortcoFinType());
		}
		currentRowIndex++;
		
		Row roundDateRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell roundDateRowcell=roundDateRow.getCell(1) != null ? roundDateRow.getCell(1) : roundDateRow.createCell(1);
		Cell roundDateRowcellVal=roundDateRow.getCell(2) != null ? roundDateRow.getCell(2) : roundDateRow.createCell(2);
		roundDateRowcell.setCellValue("Round Date");
		roundDateRowcell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getInceptionDate()!=null) {
			DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String period = outputFormatter.format(pevcInvestmentHeaderDTO.getInceptionDate());
			roundDateRowcellVal.setCellValue(period);
			roundDateRowcellVal.setCellValue(period);
		}
		
		currentRowIndex++;
		
		Row currencyRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell currencycell=currencyRow.getCell(1) != null ? currencyRow.getCell(1) : currencyRow.createCell(1);
		Cell currencycellVal=currencyRow.getCell(2) != null ? currencyRow.getCell(2) : currencyRow.createCell(2);
		currencycell.setCellValue("Investment Curreny");
		currencycell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(null!=pevcInvestmentHeaderDTO.getIsoCurrency()) {
			currencycellVal.setCellValue(pevcInvestmentHeaderDTO.getIsoCurrency());
		}
		
		currentRowIndex++;
		
		Row amountRow = sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		Cell amountcell=amountRow.getCell(1) != null ? amountRow.getCell(1) : amountRow.createCell(1);
		Cell amountcellVal=amountRow.getCell(2) != null ? amountRow.getCell(2) : amountRow.createCell(2);
		amountcell.setCellValue("Amount Raised (Currency Million)".replace("Currency", pevcInvestmentHeaderDTO.getTargetCurrency().trim()));
		amountcell.setCellStyle(ges.BOLD_FONT_HEADER);
		if(pevcInvestmentHeaderDTO.getValuationCal()!=null) {
			amountcellVal.setCellValue(pevcInvestmentHeaderDTO.getValuationCal());
		}
		
		currentRowIndex=currentRowIndex+2;
		
		//Create header 
		
		Row headerRow=sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
		
		int tempIndex = 1;

		Cell headerCell;

		for (String headerText : CMStatic.PEVC_FUNDING_HEADER) {
			headerCell = headerRow.getCell(tempIndex) != null ? headerRow.getCell(tempIndex) : headerRow.createCell(tempIndex);
			
			headerCell.setCellValue(headerText);

			headerCell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER);
			
			sheet.setColumnWidth(tempIndex, 6000);
			
			tempIndex++;
		}
		
		currentRowIndex++;
		
		int index=1;
		Cell currentCell;
		boolean isLastRecord=false;
		for(PEVCInvestmentDetailsDTO dto :pevcFundingRoundDetailsDTO.getPevcFundingInvestmentDTOs()) {
			if(index==pevcFundingRoundDetailsDTO.getPevcFundingInvestmentDTOs().size()) {
				isLastRecord=true;
			}
			int currentCellIndex=1;
			Row tableValueRow=sheet.getRow(currentRowIndex) != null ? sheet.getRow(currentRowIndex) : sheet.createRow(currentRowIndex);
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			currentCell.setCellValue(" " + index);
			if(!isLastRecord) {
				currentCell.setCellStyle(ges.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}else {
				currentCell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
			}
			currentCellIndex++;
			
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			
			setColumnValue(currentCell, dto.getEntityProperName(), sheet, currentCellIndex, ges, isLastRecord, "LEFT");
			
			
            currentCellIndex++;
			
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			
			setColumnValue(currentCell, dto.getFundName(), sheet, currentCellIndex, ges, isLastRecord, "LEFT");
			
			
			currentCellIndex++;
				
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			
			
			setColumnValue(currentCell, dto.getEntityType(), sheet, currentCellIndex, ges, isLastRecord, "LEFT");
			
			
			
			currentCellIndex++;
			
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			
			setColumnValue(currentCell, dto.getPctHeld(), sheet, currentCellIndex, ges, isLastRecord, "LEFT");
			
			
            currentCellIndex++;
			
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			
			setColumnValue(currentCell, dto.getStatus(), sheet, currentCellIndex, ges, isLastRecord, "LEFT");
			
            currentCellIndex++;
			
			currentCell=tableValueRow.getCell(currentCellIndex) != null ? tableValueRow.getCell(currentCellIndex) : tableValueRow.createCell(currentCellIndex);
			
			setColumnValue(currentCell, dto.getTerminationDate(), sheet, currentCellIndex, ges, isLastRecord);
			
			currentRowIndex++;
			index++;
		}
		
		
		return workbook ;
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
	
	private void setColumnValue(Cell cell, String data, Sheet sheet, int columnNumber, GenerateExcelStyle ges, Boolean isLastRecord,String Align) {
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

	public List<PevcIssueTypeDTO> getPeVcIssueType(String country,String industry,String fintype) {
		return peVcDaoA.getPeVcIssueType(country,industry,fintype);
	}

	public List<CountryListDTO> getPeVcAdvancedSearchCountries(String ticsIndustry, String financialType,
			String issueType) {
		return peVcDaoA.getPeVcAdvancedSearchCountries(ticsIndustry,financialType,issueType);
	}

	public List<IndustryFinancialDataDTO> getPeVcAdvancedSearchIndustries(String countryCode, String financialType,
			String issueType) {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcAdvancedSearchIndustries(countryCode,financialType,issueType);
	}

	public List<FinancialTypeDto> getPeVcAdvancedSearchFinancialType(String countryCode, String ticsIndustry, String issueType) {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcAdvancedSearchFinancialType(countryCode,ticsIndustry,issueType);
	}

	public List<PevcIssueTypeDTO> getPeVcAdvancedSearchIssueType(String countryCode, String ticsIndustry,
			String financialType) {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcAdvancedSearchIssueType(countryCode,ticsIndustry,financialType);
	}

	public AdvancedSearchFundingAmoutDto getPeVcfundingAmout(String countryCode, String ticsIndustry,
			String financialType, String issueType, String entityId, String currency, Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		return peVcDaoA.getPeVcfundingAmout(countryCode,ticsIndustry,financialType,issueType,entityId,currency,sDate,eDate);
	}
	

}
