package com.pcompany.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcompany.dto.AnalystCoverageChartDTO;
import com.pcompany.dto.AnalystCoverageDTO;
import com.pcompany.dto.AnalystGuidanceDeviationDTO;
import com.pcompany.service.AnalystCoverageService;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

@RestController
@Api(value = "AnalystCoverage", description = "Rest API for Analyst Coverage", tags = "CM Analyst Coverage")

public class AnalystCoverageController {
Logger _log = Logger.getLogger(AnalystCoverageController.class);
	
	@Autowired
	AnalystCoverageService analystCoverageService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/analyst/{companyId}/totalrecommendation")
	public ResponseEntity<List<AnalystCoverageChartDTO>> getTotalRecommendation(@PathVariable("companyId") String companyId, @RequestParam("recType") String recTypeId){
		List<AnalystCoverageChartDTO> recData = null;
		try {
			recData = analystCoverageService.getTotalRecommendation(companyId,recTypeId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageChartDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageChartDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageChartDTO>>(recData, HttpStatus.OK);
		
	}
	
	
	@GetMapping("/analyst/{companyId}/recommendation")
	public ResponseEntity<List<AnalystCoverageDTO>> getRecommendation(@PathVariable("companyId") String companyId, @RequestParam("recType") String recTypeId){
		List<AnalystCoverageDTO> recData = null;
		try {
			recData = analystCoverageService.getRecommendation(companyId,recTypeId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/basic/consensus")
	public ResponseEntity<List<AnalystCoverageDTO>> getBasiConsensus(@PathVariable("companyId") String companyId, @RequestParam("periodicity") String periodicity, @RequestParam(value="isDownload", required=false) Boolean isDownload){
		List<AnalystCoverageDTO> recData = null;
		try {
			if(isDownload ==null) {
				isDownload = false;
			}
			recData = analystCoverageService.getBasicConsensus(companyId,periodicity, isDownload);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/advanced/consensus")
	public ResponseEntity<List<AnalystCoverageDTO>> getAdvancedConsensus(@PathVariable("companyId") String companyId, @RequestParam("periodicity") String periodicity){
		List<AnalystCoverageDTO> recData = null;
		try {
			recData = analystCoverageService.getAdvancedConsensus(companyId,periodicity);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/basic/manageguide")
	public ResponseEntity<List<AnalystCoverageDTO>> getBasiManagementGuidance(@PathVariable("companyId") String companyId, @RequestParam("periodicity") String periodicity){
		List<AnalystCoverageDTO> recData = null;
		try {
			recData = analystCoverageService.getBasicManagementGuidance(companyId,periodicity);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/advanced/manageguide")
	public ResponseEntity<List<AnalystCoverageDTO>> getAdvancedManagementGuidance(@PathVariable("companyId") String companyId, @RequestParam("periodicity") String periodicity){
		List<AnalystCoverageDTO> recData = null;
		try {
			recData = analystCoverageService.getAdvancedManagementGuidance(companyId,periodicity);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/consensus/financial")
	public ResponseEntity<List<AnalystCoverageDTO>> getConsensusOperation(@PathVariable("companyId") String companyId, @RequestParam("periodicity") String periodicity){
		List<AnalystCoverageDTO> recData = null;
		try {
			recData = analystCoverageService.getConsensusOperation(companyId,periodicity);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/deviation")
	public ResponseEntity<List<AnalystGuidanceDeviationDTO>> getGuidanceDeviation(@PathVariable("companyId") String companyId, @RequestParam("periodicity") String periodicity){
		List<AnalystGuidanceDeviationDTO> recData = null;
		try {
			recData = analystCoverageService.getGuidanceDeviation(companyId,periodicity);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystGuidanceDeviationDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystGuidanceDeviationDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystGuidanceDeviationDTO>>(recData, HttpStatus.OK);
		
	}
	
	@GetMapping("/analyst/{companyId}/movement/rating")
	public ResponseEntity<List<AnalystCoverageChartDTO>> getMovementRating(@PathVariable("companyId") String companyId,@RequestParam("recType") String recTypeId){
		List<AnalystCoverageChartDTO> recData = null;
		try {
			recData = analystCoverageService.getMovementRating(companyId, recTypeId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<AnalystCoverageChartDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<AnalystCoverageChartDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<AnalystCoverageChartDTO>>(recData, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "downloadAnalystRecommendation", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> createForm(@RequestParam("companyId") String companyId,@RequestParam("recType") String recTypeId,@RequestParam("companyName") String companyName, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info("Request received for: "+ companyId);
			List<AnalystCoverageDTO> recData  = analystCoverageService.getRecommendation(companyId,recTypeId);

			// CREATE FOLDER
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}
			companyName = URLDecoder.decode(companyName, "UTF-8");
			_log.info(" companyName "+companyName);
			
			//CREATE FOLDER END
			String reportPath = cmReportPath+"/"+fsrn+".xls";
			HSSFWorkbook workbook = analystCoverageService.createExcelReport(recData,companyId,companyName);
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
	
	@RequestMapping(value = "downloadAnalystConsensus", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> downloadAnalystConsensus(@RequestParam("companyId") String companyId,@RequestParam("companyName") String companyName, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info("Request received for: "+ companyId);
			List<AnalystCoverageDTO> recDataQuarterly  = analystCoverageService.getBasicConsensus(companyId,"quarterly",true);
			List<AnalystCoverageDTO> recDataYearly  = analystCoverageService.getBasicConsensus(companyId,"yearly",true);
			List<List<AnalystCoverageDTO>> recData = new ArrayList<List<AnalystCoverageDTO>>();
			recData.add(recDataQuarterly);
			recData.add(recDataYearly);
			
		/*	List<AnalystCoverageDTO> manDataQuarterly  = analystCoverageService.getBasicManagementGuidance(companyId,"quarterly");
			List<AnalystCoverageDTO> manDataYearly  = analystCoverageService.getBasicManagementGuidance(companyId,"yearly");
			List<List<AnalystCoverageDTO>> manData = new ArrayList<List<AnalystCoverageDTO>>();
			manData.add(manDataQuarterly);
			manData.add(manDataYearly);
			
			List<AnalystCoverageDTO> conDataQuarterly  = analystCoverageService.getConsensusOperation(companyId,"quarterly");
			List<AnalystCoverageDTO> conDataYearly  = analystCoverageService.getConsensusOperation(companyId,"yearly");
			List<List<AnalystCoverageDTO>> conData = new ArrayList<List<AnalystCoverageDTO>>();
			conData.add(conDataQuarterly);
			conData.add(conDataYearly);*/
			
			// CREATE FOLDER
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}
			companyName = URLDecoder.decode(companyName, "UTF-8");
			_log.info(" companyName "+companyName);
			
			//CREATE FOLDER END
			String reportPath = cmReportPath+"/"+fsrn+".xls";
			/*HSSFWorkbook workbook = analystCoverageService.createExcelReportConsensus(recData,manData,conData,companyId,companyName);*/
			HSSFWorkbook workbook = analystCoverageService.createExcelReportConsensus(recData,companyId,companyName);
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
}
