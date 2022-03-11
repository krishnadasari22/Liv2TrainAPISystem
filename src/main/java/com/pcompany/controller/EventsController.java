package com.pcompany.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcompany.dto.CompanyDocumentMetadataDTO;
import com.pcompany.dto.EventsDTO;
import com.pcompany.dto.InsiderTransactionDTO;
import com.pcompany.model.Transcript;
import com.pcompany.service.EventsService;
import com.televisory.capitalmarket.util.CMStatic;

@RestController
@Api(value = "EventsAndFiling", description = "Rest API for Event and Filings", tags = "CM Events and Filing")
public class EventsController {
	Logger _log = Logger.getLogger(EventsController.class);
	
	@Autowired
	EventsService eventsService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/insiderTransaction")
	public ResponseEntity<List<InsiderTransactionDTO>> getInsiderTransaction(
			@RequestParam(value="securityId",required=true) String securityId,
			@RequestParam(value="currency",required=true) String currency,
			@RequestParam(value="startDate",required=false) Date startDate,
			@RequestParam(value="endDate",required=false) Date endDate){
		
		List<InsiderTransactionDTO> insiderTransactionData = null;
		try {
			_log.info("get Insider Transaction for :: " + securityId );
			insiderTransactionData = eventsService.getTransactionData(securityId,startDate,endDate,currency);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<InsiderTransactionDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<InsiderTransactionDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<InsiderTransactionDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<InsiderTransactionDTO>>(insiderTransactionData, HttpStatus.OK);
	}
	
	@GetMapping("/events")
	public ResponseEntity<List<EventsDTO>> events(
			@RequestParam("entityId") String entityId,
			@RequestParam(value="years",required=false,defaultValue="5") Integer years){
		_log.info("getEventsData entityId :: " +entityId );
		
		List<EventsDTO> eventsData = null;
		try {
			eventsData = eventsService.getEventsData(entityId,years);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<EventsDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<EventsDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<EventsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<EventsDTO>>(eventsData, HttpStatus.OK);
	}
	
	@GetMapping("/companyFilings")
	public ResponseEntity<List<CompanyDocumentMetadataDTO>> companyFilings(
			@RequestParam(value="companyId") String companyId,
			@RequestParam(value="pageNo") Integer pageNo,
			@RequestParam(value="formType",required=false) String formType,
			@RequestParam(value="years",required=false,defaultValue="5") Integer years){
		_log.info(pageNo+"Company Filings companyId: "+ companyId);
		
		List<CompanyDocumentMetadataDTO> companyDocumentMetadataDTOs = null;
		try {
			companyDocumentMetadataDTOs = eventsService.companyFilings(companyId, years,pageNo,formType);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDocumentMetadataDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<CompanyDocumentMetadataDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDocumentMetadataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyDocumentMetadataDTO>>(companyDocumentMetadataDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/transcript")
	public ResponseEntity<Transcript> transcript(@RequestParam(value="reportId") String reportId){
		_log.info("get transcript for report id: "+ reportId);
		
		Transcript transcript = null;
		try {
			transcript = eventsService.getTranscript(reportId);
		} catch (Exception e) {
			_log.error("Problem occured in extracting transcript data", e);
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<Transcript>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<Transcript>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<Transcript>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Transcript>(transcript, HttpStatus.OK);
	}
	
	@RequestMapping(value = "eventsDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download the Events and Filing data in Excel Format")
	public ResponseEntity<String> downloadEventsData(@RequestParam("companyId") String companyId, 
			@RequestParam("entityId") String entityId, 
			@RequestParam("companyName") String companyName,
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = "events_"+dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info(" events Download Data received for: "+ entityId +", "+ companyId);
			List<EventsDTO> eventsData = eventsService.getEventsData(entityId,5);
			List<CompanyDocumentMetadataDTO> companyDocumentMetadataDTOs = eventsService.companyFilings(companyId,5);
			
			// CREATE FOLDER
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}
			
			//CREATE FOLDER END
			String reportPath = cmReportPath+"/"+fsrn+".xls";
			HSSFWorkbook workbook = eventsService.createExcelReportEvents(eventsData,companyDocumentMetadataDTOs,companyName);
			//HSSFWorkbook workbook = capitalMarketService.createExcelReport(downloadRequest);		
			FileOutputStream outputStream = new FileOutputStream(reportPath);
			workbook.write(outputStream);
			_log.info("Capital Market report generated at: "+ reportPath);
			File file = new File(reportPath);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fsrn) );
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setContentType("application/octet-stream");
			response.setContentLength((int) file.length());
			FileInputStream fileIn=new FileInputStream(file);

			int BUFFER_SIZE = 4096;
			int bytesRead = -1;

			OutputStream outStream = response.getOutputStream();
			byte[] bufferData = new byte[BUFFER_SIZE];
			while ((bytesRead = fileIn.read(bufferData)) != -1) {
				outStream.write(bufferData, 0, bytesRead);
			}

			fileIn.close();
			outStream.close();
			if(!CMStatic.KEEP_CM_REPORT) {
				file.delete();
			}

		} catch (Exception e) {
			System.out.println("find "+e.getMessage());
			if(e.getMessage().contains("No Data Found") || e.getMessage().contains("could not extract ResultSet")){ 
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				return new ResponseEntity<String>(headers,HttpStatus.NO_CONTENT);
			}

			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "insiderTransactionDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download the insider transaction in Excel Format")
	public ResponseEntity<String> insiderTransactionDownload(@RequestParam("securityId") String securityId,
			@RequestParam("companyName") String companyName,
			@RequestParam("currency") String currency, 
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = "insiderTransaction_"+dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			List<InsiderTransactionDTO> insiderTransactionData = eventsService.getTransactionData(securityId, null, null,currency);
			
			//CREATE FOLDER
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}
			
			//CREATE FOLDER END
			String reportPath = cmReportPath+"/"+fsrn+".xls";
			HSSFWorkbook workbook = eventsService.createExcelReportInsider(insiderTransactionData,companyName);
			//HSSFWorkbook workbook = capitalMarketService.createExcelReport(downloadRequest);		
			FileOutputStream outputStream = new FileOutputStream(reportPath);
			workbook.write(outputStream);
			_log.info("Capital Market report generated at: "+ reportPath);
			File file = new File(reportPath);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fsrn +".xlsx") );
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setContentType("application/octet-stream");
			response.setContentLength((int) file.length());
			FileInputStream fileIn=new FileInputStream(file);

			int BUFFER_SIZE = 4096;
			int bytesRead = -1;

			OutputStream outStream = response.getOutputStream();
			byte[] bufferData = new byte[BUFFER_SIZE];
			while ((bytesRead = fileIn.read(bufferData)) != -1) {
				outStream.write(bufferData, 0, bytesRead);
			}

			fileIn.close();
			outStream.close();
			if(!CMStatic.KEEP_CM_REPORT) {
				file.delete();
			}

		} catch (Exception e) {
			System.out.println("find "+e.getMessage());
			if(e.getMessage().contains("No Data Found") || e.getMessage().contains("could not extract ResultSet")){ 
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				return new ResponseEntity<String>(headers,HttpStatus.NO_CONTENT);
			}

			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * This controller will return file or html content 
	 * @param recordKey
	 * @param request
	 * @param response
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "factsetDocument", method = RequestMethod.POST)
	@ApiOperation(value = "Download the factset dociments using on Demand API")
	public ResponseEntity<String> factsetDocument(@RequestParam("recordKey") String recordKey, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		try {
			_log.info("Request received for record Key : "+ recordKey);
			File file = eventsService.getFilingDocument(recordKey);
			
			if(file.getAbsolutePath().toLowerCase().endsWith("pdf")){
				
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"",recordKey ) );
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setContentType(MediaType.APPLICATION_PDF_VALUE);
				response.setContentLength((int) file.length());
				FileInputStream fileIn = new FileInputStream(file);

				int BUFFER_SIZE = 4096;
				int bytesRead = -1;

				OutputStream outStream = response.getOutputStream();
				byte[] bufferData = new byte[BUFFER_SIZE];
				while ((bytesRead = fileIn.read(bufferData)) != -1) {
					outStream.write(bufferData, 0, bytesRead);
				}

				fileIn.close();
				outStream.close();
				
			} else {
				String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
				
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.setContentType(MediaType.TEXT_HTML);
				 
				return new ResponseEntity<String>(content, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
	
			if(e.getMessage().contains("No Data Found") || e.getMessage().contains("could not extract ResultSet")){ 
				e.printStackTrace();		
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				return new ResponseEntity<String>(headers,HttpStatus.NO_CONTENT);
			}

			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
