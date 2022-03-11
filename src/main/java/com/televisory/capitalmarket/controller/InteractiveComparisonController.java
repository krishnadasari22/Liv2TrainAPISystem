package com.televisory.capitalmarket.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.model.IcCommodityRequestModel;
import com.televisory.capitalmarket.model.IcDataDownloadRequest;
import com.televisory.capitalmarket.model.IcDataDownloadResponse;
import com.televisory.capitalmarket.model.IcEconomyRequestModel;
import com.televisory.capitalmarket.model.IcEconomyResponseModel;
import com.televisory.capitalmarket.model.IcIndustryRequestModel;
import com.televisory.capitalmarket.model.IcStockRequestModel;
import com.televisory.capitalmarket.model.IcStockResponseModel;
import com.televisory.capitalmarket.service.InteractiveComparisonService;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

/**
 * 
 * @author vinay
 *
 */
@Controller
@RequestMapping(value="comparison")
@Api(value = "Equity", description = "Rest API for Iteractive Comparison", tags = "CM Comparison API")
public class InteractiveComparisonController {
	
	Logger _log = Logger.getLogger(InteractiveComparisonController.class);
	
	@Autowired
	InteractiveComparisonService icService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@Autowired
	ExecutorService executorPool;
	
	@GetMapping("/companies")
	public ResponseEntity<List<CompanyDTO>> getIcomCompanies(@RequestParam(name="searchCriteria",required=false) String searchCriteria,
			@RequestParam(name="companyId",required=false) String companyId,
			@RequestParam(name="countryList",required=false) String countryIdList){
		
		List<CompanyDTO> companies = null;
		_log.info("countryIdList :::::: " + countryIdList);
		try {	
			companies = icService.getIComCompanies(searchCriteria,companyId,countryIdList);	
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyDTO>>(companies, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/companies/stock/data", method = RequestMethod.POST)
	public ResponseEntity<List<IcStockResponseModel>> getIcStockData(@RequestBody IcStockRequestModel icStockRequest) {
		
		List<IcStockResponseModel> icStockResponseData = null;
		
		try {	
			icStockResponseData = icService.getIcStockData(icStockRequest);	
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<IcStockResponseModel>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IcStockResponseModel>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IcStockResponseModel>>(icStockResponseData, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/companies/industry/data", method = RequestMethod.POST)
	public ResponseEntity<List<IndustryFinancialDataDTO>> getIcIndustryData(@RequestBody IcIndustryRequestModel icIndustryRequest) {
		
		List<IndustryFinancialDataDTO> icIndustryResponseData = null;
		
		try {	
			icIndustryResponseData = icService.getIcIndustryData(icIndustryRequest);	
		
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(icIndustryResponseData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/companies/economy/data", method = RequestMethod.POST)
	public ResponseEntity<List<IcEconomyResponseModel>> getIcEconomyData(@RequestBody IcEconomyRequestModel icEconomyRequest) {
		
		List<IcEconomyResponseModel> icEconomyResponseData = null;
		try {	
			icEconomyResponseData = icService.getIcEconomyData(icEconomyRequest);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<IcEconomyResponseModel>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IcEconomyResponseModel>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IcEconomyResponseModel>>(icEconomyResponseData, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/commodity/data", method = RequestMethod.POST)
	public ResponseEntity<IcCommodityRequestModel> getIcCommodityData(@RequestBody IcCommodityRequestModel icCommodityRequest) {
		
		IcCommodityRequestModel icCommodityResponseData = null;
		try {	
			icCommodityResponseData = icService.getIcCommodityData(icCommodityRequest);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<IcCommodityRequestModel>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<IcCommodityRequestModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<IcCommodityRequestModel>(icCommodityResponseData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "downloadInterativeComparisonData", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> createForm(@RequestBody IcDataDownloadRequest icDownloadRequest, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		
		try {
			_log.info("Request received for: "+ icDownloadRequest);

			// CREATE FOLDER
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}

			File file = new File(cmReportPath+"/"+fsrn+".xls"); 
			
			IcDataDownloadResponse icDataDownloadResponse = icService.getIcDataDownloadData(icDownloadRequest);
			
			HSSFWorkbook workbook = icService.createExcelReport(icDataDownloadResponse,icDownloadRequest);		
			
			FileOutputStream outputStream = new FileOutputStream(file);
			
			workbook.write(outputStream);
			
			_log.info("Capital Market report generated at: "+ file.getAbsolutePath());

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()) );
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
