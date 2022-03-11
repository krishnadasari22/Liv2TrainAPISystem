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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.pcompany.dto.PEVCFundingDTO;
import com.pcompany.dto.PEVCFundingInvestmentDTO;
import com.pcompany.model.PEVCFundDetailResponse;
import com.pcompany.service.PEVCServiceC;
import com.televisory.capitalmarket.util.CMStatic;

@RestController
@Api(value = "PE & VC", description = "Rest API for PE & VC", tags = "PE & VC ")
public class PEVCControllerC {
	
	
	Logger _log = Logger.getLogger(PEVCControllerC.class);
	
	@Autowired
	PEVCServiceC pevcService;
	
	@Autowired 
	private ExecutorService executorService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/pevc/allFundingDetail")
	public ResponseEntity<PEVCFundDetailResponse> getAllFundingDetail(
			@RequestParam(name="country",required=false) String country,
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			@RequestParam(name="industry",required=false) String industry,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="financingType",required=false) String financingType,
			@RequestParam(name="entityId",required=false) String entityId,
			@RequestParam(name="rowOffset",required=false) Integer rowOffset,
			@RequestParam(name="rowCount",required=false) Integer rowCount,
			@RequestParam(name="sortingColumn",required=false) String sortingColumn,
			@RequestParam(value="sortingType",required=false) String sortingType) {
		_log.info("Extracting pevc funding details for country:"+country+" period:"+periodStart+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId+" rowOffset:"+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
     PEVCFundDetailResponse response = null;
    try {
    	String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date startDate = simpleDateFormat.parse(periodStart);
		Date endDate = simpleDateFormat.parse(periodEnd);
		
		Future<List<PEVCFundingDTO>> topDealMakerFut = executorService.submit(() -> {
			//return pevcService.getFundingAndInvestmentList(country,startDate,endDate,industry,currency,financingType,entityId,rowOffset,rowCount,sortingColumn, sortingType);
			return pevcService.getFundingDetailList(country, startDate, endDate, industry, currency, financingType, entityId, rowOffset, rowCount, sortingColumn, sortingType);
		});
		
		Future<Long> topDealMakerCountFut = executorService.submit(() -> {
			return pevcService.getFundingDetailCount(country,startDate,endDate,industry,currency,financingType,entityId);
		});
		
		//get PEVC funding detail
		List<PEVCFundingDTO> pevcFundingList = topDealMakerFut.get();
		response = new PEVCFundDetailResponse();
		response.setPevcFundingList(pevcFundingList);
		
		//get currency and unit
		if(!pevcFundingList.isEmpty()){
			PEVCFundingDTO pevcFundingDTO = pevcFundingList.get(0);
			response.setCurrency(pevcFundingDTO.getCurrency());
			response.setUnit(pevcFundingDTO.getUnit());
		}
		
		//get PEVC funding detail list count
		response.setTotalCount(topDealMakerCountFut.get());
		
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<PEVCFundDetailResponse>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<PEVCFundDetailResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<PEVCFundDetailResponse>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("/pevc/fundingInvestmentList")
	public ResponseEntity<List<PEVCFundingInvestmentDTO>> getAllFundingDetail(
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="financingType",required=false) String financingType,
			@RequestParam(name="entityId",required=false) String entityId) {
		_log.info("Extracting pevc funding investment list for periodStart:"+periodStart+" periodEnd:"+periodEnd+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId);
		List<PEVCFundingInvestmentDTO> response = null;
    try {
    	String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date startDate = simpleDateFormat.parse(periodStart);
		Date endDate = simpleDateFormat.parse(periodEnd);
		
		response = pevcService.getFundingInvestmentList(entityId, startDate, endDate, currency, financingType);
		
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<PEVCFundingInvestmentDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<PEVCFundingInvestmentDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<PEVCFundingInvestmentDTO>>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/pevc/allFundingDetailDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> downloadAllFundingDetail(
			@RequestParam(name="country",required=false) String country,
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			@RequestParam(name="industry",required=false) String industry,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="financingType",required=false) String financingType,
			@RequestParam(name="entityId",required=false) String entityId,
			@RequestParam(name="rowOffset",required=false) Integer rowOffset,
			@RequestParam(name="rowCount",required=false) Integer rowCount,
			@RequestParam(name="sortingColumn",required=false) String sortingColumn,
			@RequestParam(value="sortingType",required=false) String sortingType,HttpServletRequest request, HttpServletResponse response) {
		_log.info("Extracting pevc funding details for country:"+country+" period:"+periodStart+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId+" rowOffset:"+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
	 String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
    try {
    	String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date startDate = simpleDateFormat.parse(periodStart);
		Date endDate = simpleDateFormat.parse(periodEnd);
		
		//get PEVC funding detail
		List<PEVCFundingDTO> pevcFundingList = pevcService.getFundingDetailList(country,startDate,endDate,industry,currency,financingType,entityId,rowOffset,rowCount,sortingColumn, sortingType);
		
		// CREATE FOLDER
		File dirFile = new File(cmReportPath);
		if (!dirFile.exists()) {
			if (dirFile.mkdir()) {
				_log.info("CM DOWNLOAD FOLDER is created!");
			} else {
				_log.info("Failed to create directory!");
			}
		}

		String reportPath = cmReportPath+"/"+fsrn+".xls";
		HSSFWorkbook workbook = pevcService.createExcelReport(country, industry, startDate, endDate,rowCount, pevcFundingList);
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
	
	
	@GetMapping("/pevc/allFundingDetailAdvancedSearch")
	public ResponseEntity<PEVCFundDetailResponse> allFundingDetailAdvancedSearch(
			@RequestParam(name="country",required=false) String country,
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			@RequestParam(name="industry",required=false) String industry,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="financingType",required=false) String financingType,
			@RequestParam(name="entityId",required=false) String entityId,
			@RequestParam(name="issueType",required=false) String issueType,
			@RequestParam(name="minAmount",required=false) Double minAmount,
			@RequestParam(name="maxAmount",required=false) Double maxAmount,
			@RequestParam(name="rowOffset",required=false) Integer rowOffset,
			@RequestParam(name="rowCount",required=false) Integer rowCount,
			@RequestParam(name="sortingColumn",required=false) String sortingColumn,
			@RequestParam(value="sortingType",required=false) String sortingType) {
		_log.info("Extracting pevc funding details for country:"+country+" period:"+periodStart+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId+" rowOffset:"+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
     PEVCFundDetailResponse response = null;
    try {
    	String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date startDate = simpleDateFormat.parse(periodStart);
		Date endDate = simpleDateFormat.parse(periodEnd);
		
		Future<List<PEVCFundingDTO>> topDealMakerFut = executorService.submit(() -> {
			//return pevcService.getFundingAndInvestmentList(country,startDate,endDate,industry,currency,financingType,entityId,rowOffset,rowCount,sortingColumn, sortingType);
			return pevcService.allFundingDetailAdvancedSearch(country, startDate, endDate, industry, currency, financingType, entityId,issueType,minAmount,maxAmount, rowOffset, rowCount, sortingColumn, sortingType);
		});
		
		Future<Long> topDealMakerCountFut = executorService.submit(() -> {
			return pevcService.getFundingAdvancedSearchDetailCount(country,startDate,endDate,industry,currency,financingType,entityId,issueType,minAmount,maxAmount);
		});
		
		//get PEVC funding detail
		List<PEVCFundingDTO> pevcFundingList = topDealMakerFut.get();
		response = new PEVCFundDetailResponse();
		response.setPevcFundingList(pevcFundingList);
		
		//get currency and unit
		if(!pevcFundingList.isEmpty()){
			PEVCFundingDTO pevcFundingDTO = pevcFundingList.get(0);
			response.setCurrency(pevcFundingDTO.getCurrency());
			response.setUnit(pevcFundingDTO.getUnit());
		}
		
		//get PEVC funding detail list count
		response.setTotalCount(topDealMakerCountFut.get());
		
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<PEVCFundDetailResponse>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<PEVCFundDetailResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<PEVCFundDetailResponse>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/pevc/allFundingDetailAdvancedSearchDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> downloadAllFundingDetailAdvanceSearch(
			@RequestParam(name="country",required=false) String country,
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			@RequestParam(name="industry",required=false) String industry,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="financingType",required=false) String financingType,
			@RequestParam(name="entityId",required=false) String entityId,
			@RequestParam(name="issueType",required=false) String issueType,
			@RequestParam(name="minAmount",required=false) Double minAmount,
			@RequestParam(name="maxAmount",required=false) Double maxAmount,
			@RequestParam(name="rowOffset",required=false) Integer rowOffset,
			@RequestParam(name="rowCount",required=false) Integer rowCount,
			@RequestParam(name="sortingColumn",required=false) String sortingColumn,
			@RequestParam(value="sortingType",required=false) String sortingType,HttpServletRequest request, HttpServletResponse response) {
		_log.info("Extracting pevc funding details for country:"+country+" period:"+periodStart+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId+" issueType:"+issueType+" minAmount:"+minAmount+" maxAmount:"+maxAmount+" rowOffset:"+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
	 String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
    try {
    	String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date startDate = simpleDateFormat.parse(periodStart);
		Date endDate = simpleDateFormat.parse(periodEnd);
		
		//get PEVC funding detail
		List<PEVCFundingDTO> pevcFundingList = pevcService.allFundingDetailAdvancedSearch(country, startDate, endDate, industry, currency, 
				financingType, entityId, issueType, minAmount, maxAmount, rowOffset, rowCount, sortingColumn, sortingType);
		
		// CREATE FOLDER
		File dirFile = new File(cmReportPath);
		if (!dirFile.exists()) {
			if (dirFile.mkdir()) {
				_log.info("CM DOWNLOAD FOLDER is created!");
			} else {
				_log.info("Failed to create directory!");
			}
		}

		String reportPath = cmReportPath+"/"+fsrn+".xls";
		HSSFWorkbook workbook = pevcService.createAdvanceSearchExcelReport(country, industry, startDate, endDate,rowCount, pevcFundingList);
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
