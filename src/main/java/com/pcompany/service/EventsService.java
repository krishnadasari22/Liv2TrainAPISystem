package com.pcompany.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pcompany.dao.EventsDao;
import com.pcompany.dao.FactSetOnDemandDao;
import com.pcompany.dto.CompanyDocumentMetadataDTO;
import com.pcompany.dto.EventsDTO;
import com.pcompany.dto.InsiderTransactionDTO;
import com.pcompany.dto.TranscriptFileNameDTO;
import com.pcompany.entity.CompanyDocumentMetadata;
import com.pcompany.model.Transcript;
import com.pcompany.model.OnDemand.OnDemandField;
import com.pcompany.model.OnDemand.OnDemandRecord;
import com.pcompany.model.OnDemand.OnDemandResponse;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.ExcelReportService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class EventsService {

	Logger _log = Logger.getLogger(EventsService.class);
	
	@Autowired
	EventsDao eventsDao;
	
	@Autowired
	FactSetOnDemandDao factSetOnDemandDao;
	
	@Autowired
	ExcelReportService excelService;
	
	@Autowired
	private ExcelDesignService excelDesignService;
	
	@Autowired
	FactSetOnDemandService factSetOnDemandService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;
	
	@Value("${CM.FACTSET.TRASCRIPT.DIR}")
	private String transcriptDIR;

	@Value("${CM.FACTSET.TRASCRIPT.DOWNLOADED.DIR}")
	private String transcriptDownloadedDIR;
	
	@Value("${CM.FACTSET.ONDEMAND.RESPONSE.LINK.ID}")
	private String responseLinkId;
	
	@Value("${CM.FACTSET.ENVIRONMENT}")
	private String environment;

	@Value("${CM.FACTSET.BETA.TRANSCRIPT.SEARCH.URL}")
	private String betaTranscrptSearchURL;
	
	@Value("${CM.FACTSET.PROD.TRANSCRIPT.SEARCH.URL}")
	private String prodTranscrptSearchURL;
	
	@Value("${CM.FACTSET.ONDEMAND.FILE.DOWNLOAD.PATH}")
	private String FILE_PATH;
	public List<InsiderTransactionDTO> getTransactionData(String securityId, Date startDate, Date endDate, String currency){
		return eventsDao.getTransactionData(securityId,startDate,endDate,currency);
	}

	public List<EventsDTO> getEventsData(String entityId, Integer years) {
		return eventsDao.getEventsData(entityId, years);
	}

	public List<CompanyDocumentMetadataDTO> companyFilings(String companyId, Integer years) {
		return eventsDao.companyFilings(companyId, years);
	}

	public List<CompanyDocumentMetadataDTO> companyFilings(String companyId, Integer years, Integer pageNo,String formType) {
		return eventsDao.companyFilings(companyId, years, pageNo,formType);
	}
	
	public Transcript getTranscript(String reportId) throws JAXBException, Exception {
		
		
		TranscriptFileNameDTO transcriptFileName = eventsDao.getTranscriptFileName(reportId);
		
		File transcriptFile = null;
		try {
			transcriptFile = getTranscriptFromFileStorage(transcriptFileName.getFileName());
		} catch(FileNotFoundException fe) {
			_log.info("Transcript file: "+ transcriptFileName.getFileName() +", dosn't exists in filesystem for reportId: "+ reportId);
			transcriptFile = getTranscriptFromOnDemandService(transcriptFileName.getFileName(), reportId);
		}
		
		_log.debug("parseing for report id: "+ reportId);
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Transcript.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Transcript transcript = (Transcript) jaxbUnmarshaller.unmarshal(transcriptFile);
			_log.info("Transcript parsed for report id: "+ reportId);
			return transcript;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	private File getTranscriptFromFileStorage(String filename) throws FileNotFoundException {
		
		String reportTranscriptDir = transcriptDIR.replace("YYYY", filename.substring(0, 4));
		
		File transcriptFile = new File(reportTranscriptDir + File.separator + filename);
		if(transcriptFile.exists()) {
			_log.debug("Transcrip File exists on path: "+ transcriptFile.getAbsolutePath());
		} else {
			_log.warn("Transcrip File dosn't exists on path: "+ transcriptFile.getAbsolutePath());
			
			transcriptFile = new File(transcriptDownloadedDIR + File.separator + filename);
			if(transcriptFile.exists()) {
				_log.debug("Transcrip File exists on path: "+ transcriptFile.getAbsolutePath());
			} else {
				throw new FileNotFoundException("Transcrip File not found in file system");
			}
		}
		return transcriptFile;
	}

	private File getTranscriptFromOnDemandService(String filename, String reportId) throws FileNotFoundException {
		
		String transcrptSearchURL;
		if(CMStatic.FACTSET_ENVIRONMENT_BETA.equals(environment))
			transcrptSearchURL = betaTranscrptSearchURL;
		else
			transcrptSearchURL = prodTranscrptSearchURL;

		String searchURL = transcrptSearchURL.replace("REPORT_IDS", reportId);
		
		
		String transcriptOutputPath = transcriptDownloadedDIR + File.separator + filename;
		File outputFile = null;
		String transcriptURL = null;
		
		JAXBContext jaxbContextR;
		try {
			String xmlText = factSetOnDemandService.searchRequestXML(searchURL);
			
			String xmlFixedText = xmlText.replaceAll("(&(?!amp;))", "&amp;");
			StringReader reader = new StringReader(xmlFixedText);
			
			_log.info("XML search text:"+ xmlFixedText);
			jaxbContextR = JAXBContext.newInstance(OnDemandResponse.class);
			Unmarshaller jaxbUnmarshallerR = jaxbContextR.createUnmarshaller();
			OnDemandResponse onDemandResponse = (OnDemandResponse) jaxbUnmarshallerR.unmarshal(reader);
			
			if(onDemandResponse.getRecord() == null) {
				_log.error("Zero results returned for transcript search for reportId: "+ reportId);
				throw new FileNotFoundException("Could not get Transcrip file from OnDemand Service");
			}
			
			OnDemandRecordLoop : for (OnDemandRecord record : onDemandResponse.getRecord()) {
				for (OnDemandField field : record.getField()) {
					if (responseLinkId.equals(field.getId())) {
						transcriptURL = field.getValue();
						break OnDemandRecordLoop;
					}
				}
			}
		} catch (JAXBException e) {
			_log.error("Problem occured in Serching Transcript From OnDemand Service", e);
			e.printStackTrace();
			throw new FileNotFoundException("Could not get Transcrip file from OnDemand Service"); 
		} catch (IOException e) {
			_log.error("Problem occured in searching Transcript From OnDemand Service", e);
			e.printStackTrace();
			throw new FileNotFoundException("Could not get Transcrip file from OnDemand Service"); 
		} catch (Exception e) {
			_log.error("Problem occured in searching Transcript From OnDemand Service", e);
			e.printStackTrace();
			throw new FileNotFoundException("Could not get Transcrip file from OnDemand Service"); 
		}
		
		try {
			/*transcriptURL = "https://datadirect-beta.factset.com/services/docretrieval?report=feed&key=U2FsdGVkX1%2fd1XS7oB3XC3v6llnTMJxe6dWqE4ajMxXzZi9K2epzlJRTuG2JFpTTMJefwt9I0V80xG1HwWFJdw%3d%3d&timezone=America/New_York";
			outputFile = factSetOnDemandService.downloadFileOnDemand(transcriptOutputPath, transcriptURL);*/
			
			String transcripText = factSetOnDemandService.searchRequestXML(transcriptURL);
			
			outputFile = new File(transcriptOutputPath);
			
			//create parent DIR if not exists
			if(!outputFile.getParentFile().exists())
				outputFile.getParentFile().mkdirs();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(transcriptOutputPath));
		    writer.write(transcripText);
		     
		    writer.close();
		    
		} catch (IOException e) {
			_log.error("Problem occured in retriving Transcript From OnDemand Service", e);
			e.printStackTrace();
			throw new FileNotFoundException("Could not get Transcrip file from OnDemand Service"); 
		}  catch (Exception e) {
			_log.error("Problem occured in retriving Transcript From OnDemand Service", e);
			e.printStackTrace();
			throw new FileNotFoundException("Could not get Transcrip file from OnDemand Service"); 
		}
		
		return outputFile;
	}

	/**
	 * Method to check and create file if needed and return the type of content (pdf or HTML) 
	 * @param recordKey
	 * @return
	 */
	public File getFilingDocument(String recordKey) {
		
		CompanyDocumentMetadata metadata = factSetOnDemandDao.getCompanyDocumentMetadata(recordKey);
		
		File fileObject = null;
		try {
			fileObject = getFilingDocFromFileStorage(recordKey);
			return fileObject;
		} catch(FileNotFoundException fe) {
			_log.info("Document for recodrd key: "+ recordKey +", dosn't exists in filesystem");
			try {
				fileObject = factSetOnDemandService.downloadFileOnDemand(metadata);
				return fileObject;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileObject;
		
		
	}
	
	private File getFilingDocFromFileStorage(String fileName) throws FileNotFoundException {
		
		File filingDocFile = new File(FILE_PATH + File.separator + fileName+".pdf");
		if(filingDocFile.exists()) {
			_log.debug("Document exists on path: "+ filingDocFile.getAbsolutePath());
		} else {
			_log.warn("Document dosn't exists on path: "+ filingDocFile.getAbsolutePath());
			
			filingDocFile = new File(FILE_PATH + File.separator + fileName+".html");
			if(filingDocFile.exists()) {
				_log.debug("Document exists on path: "+ filingDocFile.getAbsolutePath());
			} else {
				throw new FileNotFoundException("Document not found in file system");
			}
		}
		return filingDocFile;
	}
	
	
	
	
	
	public HSSFWorkbook createExcelReportEvents(
			List<EventsDTO> eventsData,
			List<CompanyDocumentMetadataDTO> companyDocumentMetadataDTOs,
			String companyName) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet(CMStatic.SHEET_COMPANY_EVENTS);
			sheet.setDisplayGridlines(false);
			//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			createAndFillSheetEvent(workbook,eventsData,companyDocumentMetadataDTOs, CMStatic.SHEET_COMPANY_EVENTS,ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}
	
	public HSSFWorkbook createExcelReportInsider(
			List<InsiderTransactionDTO> insiderTransactionData,
			String companyName) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet(CMStatic.SHEET_INSIDER_TRANSACTION);
			sheet.setDisplayGridlines(false);
			//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			createAndFillSheetInsider(workbook,insiderTransactionData, CMStatic.SHEET_INSIDER_TRANSACTION,ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}

	private void createAndFillSheetEvent(HSSFWorkbook workbook,
			List<EventsDTO> eventsData,
			List<CompanyDocumentMetadataDTO> companyDocumentMetadataDTOs,
			String sheetName, GenerateExcelStyle ges, String companyName) {
			
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MMM/yyyy");
		
		_log.info("creating the Events and Filing Tab data ::: ");
		
		try {

			int rowdesign = 0;
			
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(r==null) {
				r=workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			if(celldesign==null) {
				celldesign=r.createCell(1);
			}
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int headerEvents = 7;
			
			Row companyDesc = sheet.getRow(2);
			if(companyDesc==null) {
				companyDesc=sheet.createRow(2);
			}
			Cell compCell = companyDesc.getCell(1);
			if(null==compCell) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Company");
			Cell companyCellData = companyDesc.getCell(2);
			if(companyCellData==null) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			
			Boolean isLastRecord = false;
			
			
			/*Inside Transaction End*/
			/*EVENTS*/
			headerEvents+=5;
			
			createHeader(workbook.getSheet(sheetName), headerEvents, ges, "Events Data", CMStatic.EVENTS_HEADER);
			isLastRecord = false;
			for(int i=0;i<eventsData.size();i++){
				EventsDTO eventsDetail = eventsData.get(i);
				if(i==eventsData.size()-1){
					isLastRecord = true;
				}
				Row rowEvents = sheet.getRow(headerEvents);
				
				if(rowEvents==null) {
					rowEvents=sheet.createRow(headerEvents);
				}
				
				int cellIdxEvents = 1;
				
				Cell cellEvents = rowEvents.getCell(cellIdxEvents);
				
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				
				
				/*Setting Events Value Start*/
				setColumnValue(cellEvents,sdf3.format(eventsDetail.getEventDatetimeUtc()),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,eventsDetail.getTitle(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				
				/*Setting Events End*/
				
				headerEvents++;
			}
			
			headerEvents+=5;
			
			createHeader(workbook.getSheet(sheetName), headerEvents, ges, "Company Filing Data", CMStatic.COMPANY_FILINGS_HEADER);
			for(int i=0;i<companyDocumentMetadataDTOs.size();i++){
				CompanyDocumentMetadataDTO cdmDetail = companyDocumentMetadataDTOs.get(i);
				if(i==eventsData.size()-1){
					isLastRecord = true;
				}
				Row rowEvents = sheet.getRow(headerEvents);
				
				if(rowEvents==null)
				{
					rowEvents=sheet.createRow(headerEvents);
				}
				int cellIdxEvents = 1;
				
				Cell cellEvents = rowEvents.getCell(cellIdxEvents);
				
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				
				
				
				/*Setting Company Filing Value Start*/
				setColumnValue(cellEvents,sdf2.format(cdmDetail.getStoryTime())+" "+sdf1.format(cdmDetail.getStoryDate()),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,cdmDetail.getFormType(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,cdmDetail.getHeadline(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				
				/*Setting Company Filing End*/
				
				headerEvents++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet ::::: " + e.getLocalizedMessage());
		}
	}
	
	
	private void createAndFillSheetInsider(HSSFWorkbook workbook,
			List<InsiderTransactionDTO> insiderTransactionData,
			String sheetName, GenerateExcelStyle ges, String companyName) {
			
		_log.info("creating the Insider Transaction Tab data ::: ");
		
		try {
			int rowdesign = 0;
			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(r==null) {
				r=workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			if(celldesign==null) {
				celldesign=r.createCell(1);
			}
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int headerEvents = 7;
			
			Row companyDesc = sheet.getRow(2);
			if(companyDesc==null) {
				companyDesc=sheet.createRow(2);
			}
			Cell compCell = companyDesc.getCell(1);
			if(null==compCell) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Company");
			Cell companyCellData = companyDesc.getCell(2);
			if(companyCellData==null) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			
			Boolean isLastRecord = false;
			
			/*Inside Transaction Start*/
			/*List<String> transactDate = new ArrayList<String>();
			List<String> transactHeaderVal = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			transactHeaderVal.add("Metric");
			for (int i = insiderTransactionData.size()-1; i >=0 ; i--) {
				if(!transactDate.contains(insiderTransactionData.get(i).getEffective_date().split("-")[0]) && transactDate.size()<10){
					transactDate.add(insiderTransactionData.get(i).getEffective_date().split("-")[0]);
					transactHeaderVal.add(insiderTransactionData.get(i).getEffective_date().split("-")[0]);
				}
			}
			Collections.sort(transactDate);
			Collections.reverse(transactDate);
			Collections.sort(transactHeaderVal);
			Collections.reverse(transactHeaderVal);
			
			try {
				createHeaderWithMergeCell(workbook.getSheet(sheetName), headerEvents, ges, "Insider Transactions", transactHeaderVal);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Map<String, Map<String,String>> dataMap = new LinkedHashMap<String,Map<String,String>>();
			Map<String, Map<String,String>> dataMapSorted = new LinkedHashMap<String,Map<String,String>>();
			Map<String, String> entityNameMapper = new LinkedHashMap<String, String>();
			Set<String> periods = new TreeSet<>();
			
			
			for (int i = insiderTransactionData.size()-1; i >=0 ; i--) {
				periods.add(insiderTransactionData.get(i).getEffective_date().split("-")[0]);
				if(!dataMap.containsKey(insiderTransactionData.get(i).getEntityProperName())){
					dataMap.put(insiderTransactionData.get(i).getEntityProperName(), new HashMap<String,String>(1));
					entityNameMapper.put(insiderTransactionData.get(i).getEntityProperName(), insiderTransactionData.get(i).getEntityProperName()+" [ " + insiderTransactionData.get(i).getReportedTitle() + " ]");
				}
				dataMap.get(insiderTransactionData.get(i).getEntityProperName()).put(insiderTransactionData.get(i).getEffective_date().split("-")[0],insiderTransactionData.get(i).getTranShares()+"#:#"+insiderTransactionData.get(i).getTranValue());
			}
			
			dataMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> dataMapSorted.put(x.getKey(), x.getValue()));
			
			_log.info("dataMapSorted ::: " + dataMapSorted);
			_log.info("entityNameMapper ::: " + entityNameMapper);
			
			for(int i=0;i<dataMapSorted.size();i++){
				if(i==dataMapSorted.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerEvents);
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				//Setting Operational Value Start
				setColumnValue(cellDebt,entityNameMapper.get(dataMapSorted.keySet().toArray()[i].toString()),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
				
				for (int j = 0; j < transactDate.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					String cellVal = dataMapSorted.get(dataMapSorted.keySet().toArray()[i].toString()).get(transactDate.get(j));
					
					if(cellVal==null||cellVal=="null"){
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
					}else{
						if(!cellVal.split("#:#")[1].equals(null) && !cellVal.split("#:#")[1].equals("null") && !cellVal.split("#:#")[1].isEmpty()){
							setColumnValue(cellDebt,cellVal.split("#:#")[0],sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}else{
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
						cellIdxDebt = cellIdxDebt+1;
						cellDebt = rowDebt.getCell(cellIdxDebt);
						if(!cellVal.split("#:#")[1].equals(null) && !"null".equals(cellVal.split("#:#")[1]) && !cellVal.split("#:#")[1].isEmpty()){
							setColumnValue(cellDebt,cellVal.split("#:#")[1],sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}else{
							setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");
						}
					}
				}
				headerEvents++;
			}
			
			
			*/

			List<String> headCol = new ArrayList<String>();
			for (String headValue : CMStatic.INSIDE_TRANSACTION) {
				headCol.add(headValue.replace("CURRENCY", insiderTransactionData.get(0).getCurrency()));
			}
			
			createHeader(workbook.getSheet(sheetName), headerEvents, ges, "Insider Transactions", headCol);
			isLastRecord = false;
			for(int i=0;i<insiderTransactionData.size();i++){
				InsiderTransactionDTO transactionDetail = insiderTransactionData.get(i);
				if(i==insiderTransactionData.size()-1){
					isLastRecord = true;
				}
				Row rowEvents = sheet.getRow(headerEvents);
				
				if(rowEvents==null) {
					rowEvents=sheet.createRow(headerEvents);
				}
				
				int cellIdxEvents = 1;
				
				Cell cellEvents = rowEvents.getCell(cellIdxEvents);
				
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				
				
				/*Setting Events Value Start*/
				setColumnValue(cellEvents,transactionDetail.getEntityProperName()+" ( "+transactionDetail.getReportedTitle()+" )",sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,transactionDetail.getEffective_date(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,transactionDetail.getBuySell(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,transactionDetail.getTranShares().toString(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				if(transactionDetail.getTranValue()!=null){
					setColumnValue(cellEvents,transactionDetail.getTranValue().toString(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				}else{
					setColumnValue(cellEvents,"-",sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				}
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				if(transactionDetail.getMarket()!=null){
					setColumnValue(cellEvents,transactionDetail.getMarket(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				}else{
					setColumnValue(cellEvents,"-",sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				}
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,transactionDetail.getSharesOwned().toString(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				cellIdxEvents = cellIdxEvents+1;
				cellEvents = rowEvents.getCell(cellIdxEvents);
				if(cellEvents==null) {
					cellEvents=rowEvents.createCell(cellIdxEvents);
				}
				setColumnValue(cellEvents,transactionDetail.getPercentHolding().toString(),sheet,cellIdxEvents,ges,isLastRecord,"LEFT");
				
				
				/*Setting Events End*/
				
				headerEvents++;
			}
			/*Inside Transaction End*/
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Some error occured in creating the sheet ::::: " + e.getLocalizedMessage());
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
			sheet.autoSizeColumn(columnNumber);
		} else {
			cell.setCellValue(CMStatic.NA);
			if(isLastRecord){
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
			
		}
		
	}
	
	@SuppressWarnings("unused")
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
	
	@SuppressWarnings("unused")
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
	
	private void createHeader(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		if(null==sectionDesc) {
			sectionDesc=sheet.createRow(startingHeaderRow-3);
		}
		Cell sectionCell = sectionDesc.getCell(1);
		if(sectionCell==null) {
			sectionCell=sectionDesc.createCell(1);
		}
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		System.out.println("header "+header +" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		//Row r = sheet.getRow(startingHeaderRow);
		//sheet.setColumnWidth(cellIndex, 5000);
		//ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
		
		Row headerRow = sheet.getRow(row);
		
		if(headerRow==null) {
			headerRow=sheet.createRow(row);
		}
		
		@SuppressWarnings("rawtypes")
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			
			if(cellHeader==null) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			
			if(headCol.equalsIgnoreCase("Coupon Rate")){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+1, true);
				cellHeader.setCellValue(headCol);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row headerMinMax =  sheet.getRow(row+1);
				if(headerMinMax==null) {
					headerMinMax=sheet.createRow(row+1);
				}
				Cell cellMinMax = headerMinMax.getCell(cellIndex);
				if(cellMinMax==null) {
					cellMinMax=headerMinMax.createCell(cellIndex);
				}
				sheet.setColumnWidth(cellIndex, 5000);
				cellMinMax.setCellValue("Min");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.setColumnWidth(cellIndex, 5000);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				if(cellMax==null) {
					cellMax=headerMinMax.createCell(cellIndex);
				}
				cellMax.setCellValue("Max");
				cellMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				
			}else{
				cellHeader.setCellValue(headCol);
				ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
			}
			
		}
	}
	
	@SuppressWarnings("unused")
	private void createHeaderWithMergeCell(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,String header,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		Cell sectionCell = sectionDesc.getCell(1);
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
		
		
		int cellIndex = 1;
		System.out.println("header "+header +" startingHeaderRow "+startingHeaderRow);
		
		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		Row headerRow = sheet.getRow(row);
		@SuppressWarnings("rawtypes")
		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			//sheet.setColumnWidth(cellIndex, 10000);
			sheet.autoSizeColumn(cellIndex);
			String headCol = (String) itr.next();
			if(!headCol.equalsIgnoreCase("Metric")){
				ges.mergeCells(sheet, row, row, cellIndex, cellIndex+1, true);
				cellHeader.setCellValue(headCol);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				Row headerMinMax =  sheet.getRow(row+1);
				Cell cellMinMax = headerMinMax.getCell(cellIndex);
				//sheet.setColumnWidth(cellIndex, 5000);
				sheet.autoSizeColumn(cellIndex);
				cellMinMax.setCellValue("Stake Purchased/(Sold)");
				cellMinMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				//sheet.setColumnWidth(cellIndex, 5000);
				sheet.autoSizeColumn(cellIndex);
				Cell cellMax = headerMinMax.getCell(cellIndex);
				cellMax.setCellValue("Value");
				cellMax.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				
			}else{
				cellHeader.setCellValue(headCol);
				ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
				cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
				cellIndex = cellIndex+1;
				sheet.autoSizeColumn(cellIndex);
			}
			
		}
	}
	
	public void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	            Row row = sheet.getRow(sheet.getFirstRowNum());
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	            }
	        }
	    }
	}
	
}
