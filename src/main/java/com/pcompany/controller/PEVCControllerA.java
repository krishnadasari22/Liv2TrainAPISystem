package com.pcompany.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.pcompany.dto.PEVCFundingRoundDetailsDTO;
import com.pcompany.service.PeVCServiceA;
import com.privatecompany.dto.DealHistoryDTO;
import com.televisory.capitalmarket.dto.AdvancedSearchFundingAmoutDto;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.FinancialTypeDto;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.dto.PevcIssueTypeDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "PE & VC", description = "Rest API for PE & VC", tags = "PE & VC ")
public class PEVCControllerA {
	
	
	Logger _log = Logger.getLogger(PEVCControllerA.class);
	
	@Autowired
	PeVCServiceA peVCServiceA;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/pevc/countries")
	public ResponseEntity<List<CountryListDTO>> getPeVcCountries() {
     _log.info("getting man countries");
		  List<CountryListDTO> countryList = null;
    try {
			countryList=peVCServiceA.getPeVcCountries();
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CountryListDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CountryListDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CountryListDTO>>(countryList, HttpStatus.OK);
	}
	
	@GetMapping("/pevc/industry")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getPeVcIndustries(@RequestParam(value="countryCode",required=false)String countryCode,@RequestParam(value="startDate",required=false) String startDate,@RequestParam(value="endDate",required=false) String endDate) throws ParseException {
     _log.info("getting man countries");
		  List<IndustryFinancialDataDTO>industryFinancialDataDTOs = null;
		  String sdf = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
			Date sDate = simpleDateFormat.parse(startDate);
			Date eDate = simpleDateFormat.parse(endDate);
    try {
    	industryFinancialDataDTOs=peVCServiceA.getPeVcIndustries(countryCode,sDate,eDate);
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(industryFinancialDataDTOs, HttpStatus.OK);
	}
	
	@GetMapping("/pevc/company")
	public ResponseEntity<List<CompanyDTO>> getPeVcCompanies(
			@RequestParam(name="searchCriteria",required=false) String searchCriteria,@RequestParam(value="countryCode",required=false) String countryCode,
			@RequestParam(value="industry",required=false) String industry,@RequestParam(value="startDate",required=false) String startDate,@RequestParam(value="endDate",required=false) String endDate) throws ParseException {
		
		_log.info("Extracting M&A company list for searchCriteria:"+ searchCriteria);
		String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date sDate = null;
		Date eDate = null;
		List<CompanyDTO> companies = null;
		try {
			if(null!=searchCriteria) {
				searchCriteria = java.net.URLDecoder.decode(searchCriteria, StandardCharsets.UTF_8.name());
				_log.info("Extracting companies like: '"+ searchCriteria +"'");
			}
			companies=peVCServiceA.getPeVcCompanies(searchCriteria,countryCode,industry,sDate,eDate);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<List<CompanyDTO>>(companies, HttpStatus.OK);
	}
	
	
	@GetMapping("/pevc/financialtype")
	public ResponseEntity<List<String>> getPeVcFinancialType() {
		
		_log.info("getting financial type");
		
		List<String> financialsType = null;
		try {
			
			financialsType=peVCServiceA.getPeVcFinancialType();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<List<String>>(financialsType, HttpStatus.OK);
	}
	
	
	@GetMapping("/pevc/fundingDetail")
	public ResponseEntity<PEVCFundingRoundDetailsDTO> getPeVcFundingRoundDetails(@RequestParam(value="entityId") String entityId,@RequestParam(value="category") String category,@RequestParam(value="startDate") String startDate,@RequestParam(value="endDate") String endDate,@RequestParam(value="financialType") String financialType,@RequestParam(value="currencyCode") String currencyCode) throws ParseException {
		PEVCFundingRoundDetailsDTO fundingRoundDetailsDTO=null;
		try {
			String sdf = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
			Date sDate = simpleDateFormat.parse(startDate);
			Date eDate = simpleDateFormat.parse(endDate);
			
			fundingRoundDetailsDTO=peVCServiceA.getPeVcFundingRoundDetails(entityId,category,sDate,eDate,financialType,currencyCode);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<PEVCFundingRoundDetailsDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<PEVCFundingRoundDetailsDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<PEVCFundingRoundDetailsDTO>(fundingRoundDetailsDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/pevc/fundingDetailDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download Fund Details data in Excel Format")
	public ResponseEntity<String> downloadDealHistory(@RequestParam(name = "entityId", required = true) String entityId,
			@RequestParam(name = "startDate", required = true) String periodStart,
			@RequestParam(name = "endDate", required = true) String periodEnd,
			@RequestParam(name = "category", required = true) String category,@RequestParam(value="financialType") String financialType,@RequestParam(value="currencyCode") String currencyCode, HttpServletRequest httpServletRequest,
			HttpServletResponse response) throws ParseException {

		_log.info("Download fund details for entity id :: "+entityId +"category ::"+category);

		

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int) (Math.random() * 10);
		List<DealHistoryDTO> dealHistoryData = null;
		String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		Date sDate=simpleDateFormat.parse(periodStart);
		Date eDate=simpleDateFormat.parse(periodEnd);
		PEVCFundingRoundDetailsDTO pevcFundingRoundDetailsDTO=null;

		try {
			pevcFundingRoundDetailsDTO = peVCServiceA.getPeVcFundingRoundDetails(entityId, category, sDate, eDate, financialType,currencyCode);

			// CREATE FOLDER for Downloaded report
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}

			// CREATE FOLDER END
			String reportPath = cmReportPath + "/" + fsrn + ".xls";

			HSSFWorkbook workbook = peVCServiceA.createFundDetailExcel(pevcFundingRoundDetailsDTO);
			FileOutputStream outputStream = new FileOutputStream(reportPath);
			workbook.write(outputStream);

			_log.info("Download M&A Deal Histroy for entityId: " + entityId + ", currency: " + entityId);
			File file = new File(reportPath);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fsrn + ".xlsx"));
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setContentType("application/octet-stream");
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
			if (!CMStatic.KEEP_CM_REPORT) {
				file.delete();
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("No Data Found") || e.getMessage().contains("could not extract ResultSet")) {
				e.printStackTrace();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				return new ResponseEntity<String>(headers, HttpStatus.NO_CONTENT);
			}

			if (e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			_log.error("Something went wrong.Try  after some time: " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/pevc/issueType")
	public ResponseEntity<List<PevcIssueTypeDTO>> getPeVcIssueType(@RequestParam(value="ticsIndustry",required=false) String ticsIndustry,@RequestParam(value="financialType",required=false) String financialType,@RequestParam(value="countryCode",required=false) String countryCode) throws ParseException {
		List<PevcIssueTypeDTO>pevcIssueTypeDTOs=new ArrayList<>();
		try {
		
			
			pevcIssueTypeDTOs=peVCServiceA.getPeVcIssueType(countryCode,ticsIndustry,financialType);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<PevcIssueTypeDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<PevcIssueTypeDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<List<PevcIssueTypeDTO>>(pevcIssueTypeDTOs, HttpStatus.OK);
	}
	
	
	@GetMapping("/pevc/advancesearchcountries")
	public ResponseEntity<List<CountryListDTO>> getPeVcAdvancedSearchCountries(@RequestParam(value="ticsIndustry",required=false) String ticsIndustry,@RequestParam(value="financialType",required=false) String financialType,@RequestParam(value="issueType",required=false) String issueType) {
     _log.info("getting man countries");
		  List<CountryListDTO> countryList = null;
    try {
			countryList=peVCServiceA.getPeVcAdvancedSearchCountries(ticsIndustry,financialType,issueType);
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CountryListDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CountryListDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CountryListDTO>>(countryList, HttpStatus.OK);
	}
	
	@GetMapping("/pevc/advancesearchindustry")
	public ResponseEntity<List<IndustryFinancialDataDTO>> getPeVcAdvancedSearchIndustries(@RequestParam(value="countryCode",required=false)String countryCode,@RequestParam(value="issueType",required=false) String issueType,@RequestParam(value="financialType",required=false) String financialType) throws ParseException {
     _log.info("getting man countries");
		  List<IndustryFinancialDataDTO>industryFinancialDataDTOs = null;
		 
    try {
    	industryFinancialDataDTOs=peVCServiceA.getPeVcAdvancedSearchIndustries(countryCode,financialType,issueType);
		} catch (Exception e){
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<IndustryFinancialDataDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<IndustryFinancialDataDTO>>(industryFinancialDataDTOs, HttpStatus.OK);
	}

	
	@GetMapping("/pevc/minmaxFundingAmount")
	public ResponseEntity<AdvancedSearchFundingAmoutDto> getPeVcfundingAmout(@RequestParam(value="countryCode",required=false) String countryCode,
			@RequestParam(value="ticsIndustry",required=false) String ticsIndustry,
			@RequestParam(value="financialType",required=false) String financialType,
			@RequestParam(value="issueType",required=false) String issueType,
			@RequestParam(value="entityId",required=false) String entityId,
			@RequestParam(value="currency",required=false)String currency ){
		AdvancedSearchFundingAmoutDto advancedSearchFundingAmoutDto=null;
		try {
			 String sdf = "yyyy-MM-dd";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
				Date sDate = simpleDateFormat.parse("2020-01-12");
				Date eDate = simpleDateFormat.parse("2020-01-12");
			
			advancedSearchFundingAmoutDto=peVCServiceA.getPeVcfundingAmout(countryCode,ticsIndustry,financialType,issueType,entityId,currency,sDate,eDate);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<AdvancedSearchFundingAmoutDto>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<AdvancedSearchFundingAmoutDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<AdvancedSearchFundingAmoutDto>(advancedSearchFundingAmoutDto, HttpStatus.OK);
	}
	

	
	
	@GetMapping("/pevc/advancesearchfinancialtype")
	public ResponseEntity<List<FinancialTypeDto>> getPeVcAdvancedSearchFinancialType(@RequestParam(value="countryCode",required=false)String countryCode,@RequestParam(value="issueType",required=false) String issueType,@RequestParam(value="ticsIndustry",required=false) String ticsIndustry)  {
		
		_log.info("getting financial type");
		
		List<FinancialTypeDto> financialsType = null;
		try {
			
			financialsType=peVCServiceA.getPeVcAdvancedSearchFinancialType(countryCode,ticsIndustry,issueType);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<FinancialTypeDto>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<FinancialTypeDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<List<FinancialTypeDto>>(financialsType, HttpStatus.OK);
	}
	

	
	

}




