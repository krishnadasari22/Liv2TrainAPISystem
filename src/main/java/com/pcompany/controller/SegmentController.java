package com.pcompany.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

import com.pcompany.dto.DebtCapitalMaturityDTO;
import com.pcompany.dto.DebtCapitalStructureDetailsDTO;
import com.pcompany.dto.SegmentBusinesDTO;
import com.pcompany.dto.SegmentOperationalDTO;
import com.pcompany.dto.SegmentRegionDTO;
import com.pcompany.service.SegmentService;
import com.televisory.capitalmarket.controller.ScreenerController;
import com.televisory.capitalmarket.service.CMFinancialDataService;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

@RestController
@Api(value = "SegmentInformationStructure", description = "Rest API for Segment Information Structure", tags = "CM Segment Information Structure")
public class SegmentController {
	
	@Autowired
	SegmentService segmentService;
	
	@Autowired
	CMFinancialDataService cmFinancialDataService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	Logger _log = Logger.getLogger(SegmentController.class);
	
	@GetMapping("/segmentstructure/business")
	public ResponseEntity<List<SegmentBusinesDTO>> getBusinessData(@RequestParam("fsimId") String fsimId,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) Date startDate,
			@RequestParam(value="endDate",required=false) Date endDate){
		
		List<SegmentBusinesDTO> segmentBusinessData = null;
		try {
			_log.info("fsimIdId :: Business :: fsimId :: " + fsimId  + " currency :: " + currency);
			
			//condition added by Hitesh
			//if currency not provided then extract the financial data in reported currency
			currency = (currency != null && !currency.isEmpty()) ? currency : cmFinancialDataService.getCompanyLatestFilingInfo(fsimId).get(0).getReportingCurrency();
			
			segmentBusinessData = segmentService.getBusinessData(fsimId,startDate,endDate,currency);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<SegmentBusinesDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<SegmentBusinesDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<SegmentBusinesDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//_log.info("segmentBusinessData :: " + segmentBusinessData);
		return new ResponseEntity<List<SegmentBusinesDTO>>(segmentBusinessData, HttpStatus.OK);
	}
	
	@GetMapping("/segmentstructure/region")
	public ResponseEntity<List<SegmentRegionDTO>> getRegionData(@RequestParam("fsimId") String fsimId,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) Date startDate,
			@RequestParam(value="endDate",required=false) Date endDate){
		
		List<SegmentRegionDTO> segmentBusinessData = null;
		try {
			_log.info("fsimIdId :: Region :: " + fsimId );
			
			//condition added by Hitesh
			//if currency not provided then extract the financial data in reported currency
			currency = (currency != null && !currency.isEmpty()) ? currency : cmFinancialDataService.getCompanyLatestFilingInfo(fsimId).get(0).getReportingCurrency();
			
			segmentBusinessData = segmentService.getRegionData(fsimId,startDate,endDate,currency);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<SegmentRegionDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<SegmentRegionDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<SegmentRegionDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//_log.info("segmentBusinessData :: " + segmentBusinessData);
		return new ResponseEntity<List<SegmentRegionDTO>>(segmentBusinessData, HttpStatus.OK);
	}
	
	@GetMapping("/segmentstructure/operational")
	public ResponseEntity<List<SegmentOperationalDTO>> getoperationalData(@RequestParam("fsimId") String fsimId,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) Date startDate,
			@RequestParam(value="endDate",required=false) Date endDate){
		
		List<SegmentOperationalDTO> segmentOperationalData = null;
		try {
			_log.info("fsimIdId :: Region :: " + fsimId );
			segmentOperationalData = segmentService.getoperationalData(fsimId,startDate,endDate);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<SegmentOperationalDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<SegmentOperationalDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<SegmentOperationalDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//_log.info("segmentOperationalData :: " + segmentOperationalData);
		return new ResponseEntity<List<SegmentOperationalDTO>>(segmentOperationalData, HttpStatus.OK);
	}

	@GetMapping("/segmentstructure/operationalQuarter")
	public ResponseEntity<List<SegmentOperationalDTO>> getoperationalDataQuarter(@RequestParam("fsimId") String fsimId,
			@RequestParam(value="currency",required=false) String currency,
			@RequestParam(value="startDate",required=false) Date startDate,
			@RequestParam(value="endDate",required=false) Date endDate){
		
		List<SegmentOperationalDTO> segmentOperationalData = null;
		try {
			_log.info("Quarterly :::: fsimIdId :: Region :: " + fsimId );
			segmentOperationalData = segmentService.getoperationalDataQuarter(fsimId,startDate,endDate);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<SegmentOperationalDTO>>(HttpStatus.UNAUTHORIZED);
			}else if(e.getCause().getMessage().trim().equals("NO DATA AVAIL")){
				return new ResponseEntity<List<SegmentOperationalDTO>>(HttpStatus.NO_CONTENT);
			}	
			_log.error(e.getMessage());
			return new ResponseEntity<List<SegmentOperationalDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//_log.info("segmentOperationalData Quarter :: " + segmentOperationalData);
		return new ResponseEntity<List<SegmentOperationalDTO>>(segmentOperationalData, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "downloadSegmentOperationalData", method = RequestMethod.POST)
	@ApiOperation(value = "Download the Segment Operational data in Excel Format")
	public ResponseEntity<String> downloadSegmentOperationalData(@RequestParam("fsimId") String fsimId,
			@RequestParam("companyName") String companyName,
			@RequestParam(value="currency",required=false) String currency,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info("Request received for: "+ fsimId + " CompanyName ::::: " + companyName);
			List<SegmentOperationalDTO> opData = segmentService.getoperationalData(fsimId,null,null);

			//downloadRequest = capitalMarketService.getResponseData(downloadRequest);

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
			HSSFWorkbook workbook = segmentService.createExcelReport(opData,fsimId,companyName);
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

		} catch(BadRequestException e){
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
	
	
	@RequestMapping(value = "downloadSegmentInfoExcelData", method = RequestMethod.POST)
	@ApiOperation(value = "Download the Segment Operational data in Excel Format")
	public ResponseEntity<String> downloadSegmentInfoExcelData(@RequestParam("fsimId") String fsimId,
			@RequestParam("companyName") String companyName,
			@RequestParam(value="currency",required=false) String currency, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		
		_log.info("companyId ::: " +fsimId + "\tcompany Name :::: " + companyName+ "\tcurrency :::: " + currency);
		try {
			_log.info("Request received for: "+ fsimId);
			List<SegmentBusinesDTO> segmentBusinessData = segmentService.getBusinessData(fsimId,null,null,currency);
			List<SegmentRegionDTO> segmentRegionData = segmentService.getRegionData(fsimId,null,null,currency);
			//downloadRequest = capitalMarketService.getResponseData(downloadRequest);

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
			HSSFWorkbook workbook = segmentService.createExcelReportSegment(segmentBusinessData,segmentRegionData,fsimId,companyName);
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
	
	
}
