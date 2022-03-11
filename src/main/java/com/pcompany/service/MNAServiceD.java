package com.pcompany.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pcompany.dao.MNADaoD;
import com.pcompany.dto.MNABalanceModelDTO;
import com.pcompany.dto.MNADealAdvisorDTO;
import com.pcompany.dto.MNADealMetaDataDTO;
import com.pcompany.dto.MNADealTermDTO;
import com.pcompany.dto.MNADealTermSynopsis;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

import ar.com.fdvs.dj.domain.constants.Border;

@Service
public class MNAServiceD {
	
	Logger _log = Logger.getLogger(MNAServiceD.class);
	
	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	@Autowired
	MNADaoD mnaDao; 

	@Autowired
	ExecutorService executorPool;
	
	
	
	
	@Autowired
	private ExcelDesignService excelDesignService;
	
	public MNADealTermSynopsis getDealTermSynopsis(Integer dealId, String entityId, String currency) {
		_log.info("getting Deal Term and Synopsis for dealId: "+ dealId +", currency: "+ currency);
		try{
			MNADealTermSynopsis response = new MNADealTermSynopsis();
			
			Callable<String> synopsisCallable = () -> {
				return getSynopsis(dealId);
			};
			
			Callable<List<MNABalanceModelDTO>> balanceModelCallable = () -> {
				return getMNABalanceModel();
			};
			
			Callable<MNADealMetaDataDTO> metaDataCallable = () -> {
				return getDealMetaData(dealId, entityId);
			};

			Callable<List<MNADealAdvisorDTO>> advisorCallable = () -> {
				return getDealAdvisor(dealId, currency);
			};
			
			Callable<List<MNADealTermDTO>> termsCallable = () -> {
				return getDealTerms(dealId, currency);
			};
			
			Future<String> synopsisFuture = executorPool.submit(synopsisCallable);
			Future<List<MNABalanceModelDTO>> balanceModelFuture = executorPool.submit(balanceModelCallable);
			Future<MNADealMetaDataDTO> metaDataFuture = executorPool.submit(metaDataCallable);
			Future<List<MNADealAdvisorDTO>> advisorFuture = executorPool.submit(advisorCallable);
			Future<List<MNADealTermDTO>> termsFuture = executorPool.submit(termsCallable);
			
			String synopsis = synopsisFuture.get();
			List<MNABalanceModelDTO> balanceModel = balanceModelFuture.get();
			MNADealMetaDataDTO metaData = metaDataFuture.get();
			List<MNADealAdvisorDTO> advisorDTOs = advisorFuture.get();
			List<MNADealTermDTO> termDTOs = termsFuture.get();
			
			response.setSynopsis(synopsis);
			response.setBalanceModel(balanceModel);
			response.setDealMetaData(metaData);
			response.setDealAdvisors(advisorDTOs);
			response.setDealTerms(termDTOs);
			
			return response;	
		}catch(Exception ex){
			_log.error("Problem occured in getting MNA Deal Term and Synopsis", ex);
			return null;
		}
	}
	
	
	
	public String getSynopsis(Integer dealId) {
		return mnaDao.getSynopsis(dealId);
	}
	
	public List<MNABalanceModelDTO> getMNABalanceModel() {
		return mnaDao.getMNABalanceModel();
	}
	
	public MNADealMetaDataDTO getDealMetaData(Integer dealId, String entityId) {
		return mnaDao.getDealMetaData(dealId, entityId);
	}
	
	public List<MNADealAdvisorDTO> getDealAdvisor(Integer dealId, String currency){
		return mnaDao.getDealAdvisor(dealId, currency);
	}

	public List<MNADealTermDTO> getDealTerms(Integer dealId, String currency) {
		return mnaDao.getDealTerms(dealId, currency);
	}
	
	
	public HSSFWorkbook createDealTermSynopsisExcel(MNADealTermSynopsis data) throws Exception{
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			int row=0;
			
			int length=data.getSynopsis().toCharArray().length;
			row=length / 212;
			
			
			String sheetName = CMStatic.SHEET_MNA_DEAL_TERM_SYNOPSIS;
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet(sheetName);
			sheet.setDisplayGridlines(false);
			_log.info("creating Deal Term Synopsis sheet");
			int rowdesign = 0;
			
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(null==r) {
				r=workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			
			//Create Header
			int metadataStartIndex = 4;
			int cellIndex = 1;
			
			int currentMetaDataIndex = metadataStartIndex;

			Row metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			Cell metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Company");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null && data.getDealMetaData().getCompany() != null)
				metaDataCell.setCellValue(data.getDealMetaData().getCompany());
			currentMetaDataIndex++;
			
			metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Role");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null && data.getDealMetaData().getRole() != null)
				metaDataCell.setCellValue(data.getDealMetaData().getRole());
			currentMetaDataIndex++;
			
			metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Acquirer");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null && data.getDealMetaData().getAcquirer() != null)
				metaDataCell.setCellValue(data.getDealMetaData().getAcquirer());
			currentMetaDataIndex++;
			
			metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Target");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null && data.getDealMetaData().getTarget() != null)
				metaDataCell.setCellValue(data.getDealMetaData().getTarget());
			currentMetaDataIndex++;
			
			metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Seller");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null && data.getDealMetaData().getSeller() != null)
				metaDataCell.setCellValue(data.getDealMetaData().getSeller());
			currentMetaDataIndex++;

			metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Announce Date");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null && data.getDealMetaData().getAnnounceDate() != null)
				metaDataCell.setCellValue(dateFormat.format(data.getDealMetaData().getAnnounceDate()));
			currentMetaDataIndex++;
			
			metaDataRow = sheet.getRow(currentMetaDataIndex) != null ? sheet.getRow(currentMetaDataIndex) : sheet.createRow(currentMetaDataIndex);
			metaDataCell = metaDataRow.getCell(cellIndex) != null ? metaDataRow.getCell(cellIndex) : metaDataRow.createCell(cellIndex);
			metaDataCell.setCellValue("Close Date/Status");
			metaDataCell.setCellStyle(ges.BOLD_FONT_HEADER);
			metaDataCell = metaDataRow.getCell(cellIndex+1) != null ? metaDataRow.getCell(cellIndex+1) : metaDataRow.createCell(cellIndex+1);
			if(data.getDealMetaData() != null) {
				String celltext = "";
				if(null!=data.getDealMetaData().getCloseDate()) {
					celltext=dateFormat.format(data.getDealMetaData().getCloseDate());
				}
				
				else {
					celltext=data.getDealMetaData().getStatus();
				}
				
				
				metaDataCell.setCellValue(celltext);
			}
			currentMetaDataIndex++;
			
			int synopsisStartIndex = metadataStartIndex + 9;
			int synopsisValueRowIndex = synopsisStartIndex + 1;
			int synopsisWidth = 6;
			
			//Insert Synopsis Header
			Row synopsisHeaderRow = sheet.getRow(synopsisStartIndex) != null ? sheet.getRow(synopsisStartIndex) : sheet.createRow(synopsisStartIndex);
			
			for (int i = 0; i <= synopsisWidth; i++) {
				if(i == 0)
					sheet.setColumnWidth(i+cellIndex, 10000);
				else
					sheet.setColumnWidth(i+cellIndex, 7000);
			}
			Cell synopsisHeaderCell = synopsisHeaderRow.getCell(cellIndex) != null ? synopsisHeaderRow.getCell(cellIndex) : synopsisHeaderRow.createCell(cellIndex);
			synopsisHeaderCell.setCellValue(CMStatic.MNA_SYNOPSIS_HEADER);
			ges.mergeCells(sheet, synopsisStartIndex, synopsisStartIndex, cellIndex, cellIndex+synopsisWidth, true);
			synopsisHeaderCell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER);			
			
			//Insert Synopsis Value
			Row synopsisValueRow = sheet.getRow(synopsisValueRowIndex) != null ? sheet.getRow(synopsisValueRowIndex) : sheet.createRow(synopsisValueRowIndex);

			Cell synopsisValueCell = synopsisValueRow.getCell(cellIndex) != null ? synopsisValueRow.getCell(cellIndex) : synopsisValueRow.createCell(cellIndex);
			
			CellStyle wrapStyle = workbook.createCellStyle();
		   
		    wrapStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
		    wrapStyle.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
		    
		    //wrapStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		    wrapStyle.setWrapText(true);
		    synopsisValueCell.setCellStyle(wrapStyle);
		    /*wrapStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		    wrapStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	*/
		    /*wrapStyle.setRightBorderColor(color);*/
		    synopsisValueCell.setCellValue(data.getSynopsis());
			ges.mergeCells(sheet, synopsisValueRowIndex, synopsisValueRowIndex+row, cellIndex, cellIndex+synopsisWidth, true);
			ges.mergeCells(sheet, synopsisValueRowIndex, synopsisValueRowIndex+6, 0, 0, false);
			
			int advisorStartIndex = synopsisStartIndex +2+row;
			int advisorValueRowIndex = advisorStartIndex + 2;
			if(data.getDealAdvisors() != null && data.getDealAdvisors().size() >0) {
				//Create Advisor Header
				Row emptyRow = sheet.getRow(advisorStartIndex) != null ? sheet.getRow(advisorStartIndex) : sheet.createRow(advisorStartIndex);
				advisorStartIndex++;
				advisorValueRowIndex++;
				Row advisorHeaderRow = sheet.getRow(advisorStartIndex) != null ? sheet.getRow(advisorStartIndex) : sheet.createRow(advisorStartIndex);
				Cell advisorHeaderCell = advisorHeaderRow.getCell(cellIndex) != null ? advisorHeaderRow.getCell(cellIndex) : advisorHeaderRow.createCell(cellIndex);
				advisorHeaderCell.setCellValue(CMStatic.MNA_DEAL_ADVISOR_HEADER_PRE);
				ges.mergeCells(sheet, advisorStartIndex, advisorStartIndex, cellIndex, cellIndex+synopsisWidth, true);
				advisorHeaderCell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER);
				
				//2nd Line of advisor Header
				advisorHeaderRow = sheet.getRow(advisorStartIndex+1) != null ? sheet.getRow(advisorStartIndex+1) : sheet.createRow(advisorStartIndex+1);
				int tempIndex = cellIndex;
				for (String headerText : CMStatic.MNA_DEAL_ADVISOR_HEADER) {
					HSSFFont WHITE_WITHOUT_HEIGHT = workbook.createFont();
					WHITE_WITHOUT_HEIGHT.setColor(HSSFColor.WHITE.index);
					WHITE_WITHOUT_HEIGHT.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					CellStyle style = workbook.createCellStyle();
					style.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
					style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					style.setFont(WHITE_WITHOUT_HEIGHT);	
					style.setWrapText(true);
					style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					style.setBorderTop(HSSFCellStyle.BORDER_THIN);
					advisorHeaderCell = advisorHeaderRow.getCell(tempIndex) != null ? advisorHeaderRow.getCell(tempIndex) : advisorHeaderRow.createCell(tempIndex);
					if(headerText.contains("CURRENCY"))
						headerText = headerText.replace("CURRENCY", data.getDealAdvisors().get(0).getCurrency());
					advisorHeaderCell.setCellValue(headerText);
					advisorHeaderCell.setCellStyle(style);
					/*advisorHeaderCell.setCellStyle(style);*/
					
					tempIndex++;
				}
				
				Row advisorValueRow;
				Cell advisorValueCell;
				int currentCell = cellIndex;
				boolean dealAdvisorLastRecord=false;
				int i=0;
				for (MNADealAdvisorDTO advisorDTO : data.getDealAdvisors()) {
					advisorValueRow = sheet.getRow(advisorValueRowIndex) != null ? sheet.getRow(advisorValueRowIndex) : sheet.createRow(advisorValueRowIndex);
					if(i==data.getDealAdvisors().size()-1) {
						dealAdvisorLastRecord=true;
					}
					//Advisor Name
					currentCell = cellIndex;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getAdvisorName() != null)
						advisorValueCell.setCellValue(advisorDTO.getAdvisorName());
						setColumnValue(advisorValueCell, advisorDTO.getAdvisorName(), sheet, currentCell, ges, dealAdvisorLastRecord, "LEFT");
					
					//Advisor Role
					currentCell++;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getAdvisorRole() != null)
					setColumnValue(advisorValueCell, advisorDTO.getAdvisorRole(), sheet, currentCell, ges, dealAdvisorLastRecord, "LEFT");
					
					//Client Name
					currentCell++;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getClientName() != null)
					setColumnValue(advisorValueCell, advisorDTO.getClientName(), sheet, currentCell, ges, dealAdvisorLastRecord, "LEFT");
						//advisorValueCell.setCellValue(advisorDTO.getClientName());
	
					//Client Role
					currentCell++;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getClientRole() != null)
						//advisorValueCell.setCellValue(advisorDTO.getClientRole());
					setColumnValue(advisorValueCell, advisorDTO.getClientRole(), sheet, currentCell, ges, dealAdvisorLastRecord, "LEFT");
					
					currentCell++;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getDealStatus() != null)
						//advisorValueCell.setCellValue(advisorDTO.getDealStatus());
					setColumnValue(advisorValueCell,advisorDTO.getDealStatus(), sheet, currentCell, ges, dealAdvisorLastRecord, "LEFT");
					
					
					currentCell++;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getFee() != null)
						//.advisorDTO.advisorValueCell.setCellValue(advisorDTO.getFee());
					setColumnValue(advisorValueCell, advisorDTO.getFee(), sheet, currentCell, ges,dealAdvisorLastRecord );
					
					currentCell++;
					advisorValueCell = advisorValueRow.getCell(currentCell) != null ? advisorValueRow.getCell(currentCell) : advisorValueRow.createCell(currentCell);
					//if(advisorDTO.getComment() != null)
						//advisorValueCell.setCellValue(advisorDTO.getComment());
					setColumnValue(advisorValueCell,advisorDTO.getComment(), sheet, currentCell, ges, dealAdvisorLastRecord, "LEFT");
					
					
					advisorValueRowIndex++;
					i++;
				}
			}
			
			int termsStartIndex = advisorValueRowIndex + 2;
			int termsValueRowIndex = termsStartIndex + 1;
			if(data.getDealTerms() != null && data.getDealTerms().size() >0) {
				//Create Term Header
				Row termsHeaderRow = sheet.getRow(termsStartIndex) != null ? sheet.getRow(termsStartIndex) : sheet.createRow(termsStartIndex);
				Cell termsHeaderCell = termsHeaderRow.getCell(cellIndex) != null ? termsHeaderRow.getCell(cellIndex) : termsHeaderRow.createCell(cellIndex);
				termsHeaderCell.setCellValue(CMStatic.MNA_DEAL_TERM_HEADER_PRE);
				ges.mergeCells(sheet, termsStartIndex, termsStartIndex, cellIndex, cellIndex+data.getDealTerms().size(), true);
				termsHeaderCell.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND_WITHOUT_BORDER);
				
				//Code Pending
				Row termValueRow;
				Cell termValueCell;
				int currentCellIndex = cellIndex;
				int currentValueRowIndex = termsValueRowIndex;
				//create head 
				for(MNABalanceModelDTO balance :data.getBalanceModel()) {
					if(null!=balance && !"Version".equalsIgnoreCase(balance.getShortName())) {
					int headerCell=1;
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(headerCell) != null ? termValueRow.getCell(headerCell) : termValueRow.createCell(headerCell);
					/*termValueCell.setCellValue(balance.getShortName());
					termValueCell.setCellStyle(ges.BOLD_FONT_HEADER);*/
					if("Deal Stage".equalsIgnoreCase(balance.getShortName())) {
					setColumnValue(termValueCell, balance.getShortName(), sheet, headerCell, ges, true, "LEFT");
					}
					else {
						setColumnValue(termValueCell, balance.getShortName(), sheet, headerCell, ges, false, "LEFT");
					}
					
					currentValueRowIndex++;
				}
					
				}
				
				//
				currentCellIndex++;
				
				currentValueRowIndex = termsValueRowIndex;
				for (MNADealTermDTO termDTO : data.getDealTerms()) {
					
					currentValueRowIndex = termsValueRowIndex;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					sheet.setColumnWidth(currentCellIndex, 7000);
					//if(termDTO.getEffect_date() != null)
					if(currentCellIndex>7) {
						setColumnValue(termValueCell, termDTO.getEffect_date(), sheet,currentCellIndex , ges, false, true);
					}
					else {
					setColumnValue(termValueCell, termDTO.getEffect_date(), sheet, currentCellIndex, ges, false);
					}
						//termValueCell.setCellValue(dateFormat.format(termDTO.getEffect_date()));
					//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getTarget() != null)
						setColumnValue(termValueCell,termDTO.getTarget(),sheet,currentCellIndex,ges,false,"LEFT");
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getTarget());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getMop() != null)
						setColumnValue(termValueCell,termDTO.getMop(),sheet,currentCellIndex,ges,false,"LEFT");
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getMop());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getSource_funds_desc() != null)
					setColumnValue(termValueCell,termDTO.getSource_funds_desc(),sheet,currentCellIndex,ges,false,"LEFT");
					//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getSource_funds_desc());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getCash() != null)
						setColumnValue(termValueCell,termDTO.getCash() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getCash());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getStock() != null)
						setColumnValue(termValueCell,termDTO.getStock() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getStock());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getPercent_sought() != null)
						setColumnValue(termValueCell,termDTO.getPercent_sought() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getPercent_sought());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getPercent_owned() != null)
						setColumnValue(termValueCell,termDTO.getPercent_owned() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getPercent_owned());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getTransaction_value() != null)
						setColumnValue(termValueCell,termDTO.getTransaction_value() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getTransaction_value());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getEv() != null)
						setColumnValue(termValueCell,termDTO.getEv() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getEv());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getRevenue_ltm_before_deal() != null)
						setColumnValue(termValueCell,termDTO.getRevenue_ltm_before_deal() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getRevenue_ltm_before_deal());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getEbitda_ltm_before_deal() != null)
						setColumnValue(termValueCell,termDTO.getEbitda_ltm_before_deal() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getEbitda_ltm_before_deal());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getPrice_share() != null)
						setColumnValue(termValueCell,termDTO.getPrice_share() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getPrice_share());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getStock_price_share() != null)
						setColumnValue(termValueCell,termDTO.getStock_price_share() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getStock_price_share());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getCash_price_share() != null)
						setColumnValue(termValueCell,termDTO.getCash_price_share() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getCash_price_share());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getOne_day_prem() != null)
						setColumnValue(termValueCell,termDTO.getOne_day_prem() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getOne_day_prem());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getEv_rev() != null)
						setColumnValue(termValueCell,termDTO.getEv_rev() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getEv_rev());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getEv_ebitda() != null)
						setColumnValue(termValueCell,termDTO.getEv_ebitda() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getEv_ebitda());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getCash_adjusted_deal_value() != null)
						setColumnValue(termValueCell,termDTO.getCash_adjusted_deal_value() , sheet, currentCellIndex, ges, false);
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,false);
						//termValueCell.setCellValue(termDTO.getCash_adjusted_deal_value());
					currentValueRowIndex++;
					
					termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					//if(termDTO.getTerm_change_desc() != null)
						setColumnValue(termValueCell, termDTO.getTerm_change_desc(), sheet, currentCellIndex, ges, true, "LEFT");
						//ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,true);
					currentValueRowIndex++;
					
					/*termValueRow = sheet.getRow(currentValueRowIndex) != null ? sheet.getRow(currentValueRowIndex) : sheet.createRow(currentValueRowIndex);
					termValueCell = termValueRow.getCell(currentCellIndex) != null ? termValueRow.getCell(currentCellIndex) : termValueRow.createCell(currentCellIndex);
					termValueCell.setCellValue(" "+termDTO.getVersion());
					termValueCell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
					ges.mergeCellsRightBorder(sheet, currentValueRowIndex, currentValueRowIndex, 3, 7, true,true);*/
					
					currentCellIndex++;
				}
				
			}
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
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
	
	
	private void setColumnValue(Cell cell, Date data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges,Boolean isLastRecord,Boolean isTopBorder) {
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
			if(isTopBorder) {
			cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_TOP_ALIGN_RIGHT);
			}
			else {
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}else{
			if(isTopBorder) {
			cell.setCellStyle(ges.BORDER_LEFT_RIGHT_TOP_ALIGN_RIGHT);
			}else {
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
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
}
