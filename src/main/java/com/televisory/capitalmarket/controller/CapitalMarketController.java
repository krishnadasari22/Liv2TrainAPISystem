package com.televisory.capitalmarket.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.privatecompany.dto.AdvanceSearchCompanyDTO;
import com.privatecompany.dto.EntityStructureDTO;
import com.televisory.capitalmarket.dto.CMCDTO;
import com.televisory.capitalmarket.dto.CMExchangeDTO;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.economy.CountryListDTO;
import com.televisory.capitalmarket.model.CompanyMetaData;
import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.model.GlobalSearchResponseModel;
import com.televisory.capitalmarket.model.IndustryMonitorRequest;
import com.televisory.capitalmarket.model.ReportCompanyProfile;
import com.televisory.capitalmarket.model.ReportIndustryMonitor;
import com.televisory.capitalmarket.service.CMFinancialDataService;
import com.televisory.capitalmarket.service.CMStockService;
import com.televisory.capitalmarket.service.CapitalMarketService;
import com.televisory.capitalmarket.service.JasperReportService;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
//@RequestMapping(value="cm")
@Api(value = "Equity", description = "Rest API for Capital Market", tags = "CM API")
public class CapitalMarketController {

	Logger _log = Logger.getLogger(CapitalMarketController.class);

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	CMStockService cmStockService;

	@Autowired
	CMFinancialDataService cmFinancialDataService;  

	@Autowired
	JasperReportService jasperService;

	@Autowired
	CacheManager cacheManager;

	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;


	@Value("${CM.CP.JASPER.TEMPLATE.PATH}")
	private String cpJasperTemplatePath;

	@Value("${CM.CP.REPORT.PATH}")
	private String cpDownloadPath;

	@Value("${CM.IM.JASPER.TEMPLATE.PATH}")
	private String imJasperTemplatePath;

	@Value("${CM.IM.REPORT.PATH}")
	private String imDownloadPath;

	@Value("${CM.RESOURCE.IMAGE.PATH}")
	private String imagePath;

	@RequestMapping(value = "downloadData", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> createForm(@RequestBody DownloadRequest downloadRequest, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		_log.info(new Gson().toJson(downloadRequest));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");

		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);

		try {
			_log.info("Request received for: "+ downloadRequest);

			downloadRequest = capitalMarketService.getResponseData(downloadRequest);

			// CREATE FOLDER
			File dirFile = new File(cmReportPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("CM DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}

			File file = new File(cmReportPath+"/"+fsrn+".xlsx"); 
			
			Workbook workbook = new XSSFWorkbook();

			workbook = capitalMarketService.createExcelReport(downloadRequest);	
			

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
			_log.error("Error while generating report ", e);
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
			_log.error("Something went wrong. Try  after some time: "+ e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "downloadDataDummy", method = RequestMethod.POST)
	@ApiOperation(value = "Dummy Download controller for testing the json response", response = DownloadRequest.class)
	public ResponseEntity<DownloadRequest> createDummy(@RequestBody DownloadRequest downloadRequest, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
			_log.info("Request received for: "+ downloadRequest);
			downloadRequest = capitalMarketService.getResponseData(downloadRequest);
		} catch(BadRequestException e){ 
			e.printStackTrace();
			return new ResponseEntity<DownloadRequest>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("Something went wrong.Try  after some time: "+ e.getMessage());
			if(e.getMessage() != null && e.getMessage().contains("No Data Found")){
				e.printStackTrace();
				return new ResponseEntity<DownloadRequest>(HttpStatus.NO_CONTENT);
			}

			if (e.getMessage() != null && e.getMessage().contains("Invalid Request")) {
				_log.error("Invalid Request");
				return new ResponseEntity<DownloadRequest>(HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<DownloadRequest>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Request processed successfully");
		return new ResponseEntity<DownloadRequest>(downloadRequest, HttpStatus.OK);
	}

	@GetMapping("countries")
	public ResponseEntity<List<CountryListDTO>> getCountryList(@RequestParam(value = "userCountryList",required=false) List<String> userCountryList) {

		_log.info("getCountryList controller called ::::::: " + userCountryList);

		List<CountryListDTO> countryList = null;

		try {
			if(userCountryList!=null && userCountryList.size()>0){
				countryList=cmStockService.getCountryList(userCountryList);
			}else{
				countryList=cmStockService.getCountryList();
			}
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


	@SuppressWarnings("rawtypes")
	@GetMapping("/companies")
	public ResponseEntity<List<CompanyDTO>> getCMCompanies(
			@RequestParam(name="searchCriteria",required=false) String searchCriteria,
			@RequestParam(name="countryId",required=false) Integer countryIds, 
			@RequestParam(name="encodedFlag",required=false) Integer encodedFlag,
			@RequestParam(name="companyId",required=false) String companyIds,
			@RequestParam(name="resultCount",required=false) Integer resultCount,
			@RequestParam(name="excludeDuplicateFlag",required=false, defaultValue="false") Boolean excludeDuplicateFlag,
			@RequestParam(value="userCountryList",required=false) List<String> userCountryList,
			@RequestParam(name="entityType",required=false, defaultValue="PUB") String entityType) {
		_log.info("Extracting company list for countryId:"+ countryIds +", companyId: "+ companyIds +", excludeDuplicateFlag: "+ excludeDuplicateFlag +", searchCriteria:"+ searchCriteria);
		List<CompanyDTO> companies = null;
		try {
			if(encodedFlag != null && searchCriteria != null && encodedFlag.equals(1)) {
				searchCriteria = java.net.URLDecoder.decode(searchCriteria, StandardCharsets.UTF_8.name());
				_log.info("Extracting companies like: '"+ searchCriteria +"'");
			}
			companies = capitalMarketService.getCMExchangeCompanies(searchCriteria,resultCount, countryIds, companyIds, excludeDuplicateFlag,userCountryList,entityType);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getCMCompanies");
		return new ResponseEntity<List<CompanyDTO>>(companies, HttpStatus.OK);
	}


	@SuppressWarnings("rawtypes")
	@GetMapping("/companies/{companyId}")
	public ResponseEntity<CompanyDTO> getCMCompaniesById(@PathVariable(value="companyId") String companyId) {

		CompanyDTO company = null;

		try {
			company = capitalMarketService.getCMCompaniesById(companyId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<CompanyDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<CompanyDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CompanyDTO>(company, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/companies/entity/{entityId}")
	public ResponseEntity<List<CompanyDTO>> getCMCompaniesByEntityId(@PathVariable(value="entityId") String entityId) {

		List<CompanyDTO> company = null;

		try {
			company = capitalMarketService.getCMCompaniesByEntityId(entityId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyDTO>>(company, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/companies/{companyId}/exchanges")
	public ResponseEntity<List<CompanyDTO>> getCMExchangeCompaniesById(@PathVariable(value="companyId") String companyId) {

		List<CompanyDTO> company = null;

		try {
			company = capitalMarketService.getCMExchangeCompaniesById(companyId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyDTO>>(company, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/companies/{companyId}/exchanges/data")
	public ResponseEntity<List<CMCDTO>> getCMExchangeCompaniesTriggerUsedById(@PathVariable(value="companyId") String companyId) {

		List<CMCDTO> company = null;

		try {
			company = capitalMarketService.getCMExchangeCompaniesTriggerUsedById(companyId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CMCDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CMCDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CMCDTO>>(company, HttpStatus.OK);
	}


	@SuppressWarnings("rawtypes")
	@GetMapping("/companies/list/{companyIds}")
	public ResponseEntity<List<CompanyDTO>> getCMCompaniesByIdList(@PathVariable(value="companyIds") String companyIds) {

		List<CompanyDTO> company = null;

		try {
			List<String> companyIdsList = Arrays.asList(companyIds.split("\\s*,\\s*"));
			company = capitalMarketService.getCMCompaniesByIdList(companyIdsList);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyDTO>>(company, HttpStatus.OK);
	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	@GetMapping("/defaultcompany/{countryCode}")
	public ResponseEntity<CompanyDTO> getDefaultCompanyForCountryOld(@PathVariable(value="countryCode") String countryCode) {

		CompanyDTO company = null;
		try {
			_log.info("getting default company for countryCode "+ countryCode );
			company = capitalMarketService.getDefaultCompanyForCountry(countryCode, null);	
		} catch (IndexOutOfBoundsException e) {
			_log.error(e.getMessage());		
			return new ResponseEntity<CompanyDTO>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();

			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<CompanyDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<CompanyDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CompanyDTO>(company, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/defaultcompany")
	public ResponseEntity<CompanyDTO> getDefaultCompanyForCountry(@RequestParam(value="primaryCountry", required=false) String primaryCountryCode, 
			@RequestParam(value="subscribedCountyList", required=false) List<String> subscribedCountyList) {

		CompanyDTO company = null;
		try {
			_log.info("getting default company for PrimaryCountryCode "+ primaryCountryCode +", subscribedCountyList: "+ subscribedCountyList);
			company = capitalMarketService.getDefaultCompanyForCountry(primaryCountryCode, subscribedCountyList);	
		} catch (IndexOutOfBoundsException e) {
			_log.error(e.getMessage());		
			return new ResponseEntity<CompanyDTO>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();

			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<CompanyDTO>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<CompanyDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CompanyDTO>(company, HttpStatus.OK);
	}


	@GetMapping("companies/{companyId}/ticker")
	public ResponseEntity<String> getCompanyTicker(@PathVariable(name="companyId") String companyId) {

		String companiesTicker = null;
		try {
			companiesTicker = capitalMarketService.getCompanyTicker(companyId);	
		} catch (Exception e) {

			e.printStackTrace();

			if(e.getLocalizedMessage().equals("No data found")){
				_log.error(e.getMessage());		
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}

			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}

			_log.error(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(companiesTicker, HttpStatus.OK);
	}

	@GetMapping("exchange")
	public ResponseEntity<List<CMExchangeDTO>> getExchangeList(@RequestParam(value="userCountryList",required=false) List<String> userCountryList) {

		List<CMExchangeDTO> exchangeList = null;
		try {
			exchangeList = capitalMarketService.getExchangeList(userCountryList);	
		} catch (Exception e) {

			e.printStackTrace();

			if(e.getLocalizedMessage().equals("No data found")){
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CMExchangeDTO>>(HttpStatus.NO_CONTENT);
			}

			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CMExchangeDTO>>(HttpStatus.UNAUTHORIZED);
			}

			_log.error(e.getMessage());
			return new ResponseEntity<List<CMExchangeDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CMExchangeDTO>>(exchangeList, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("exchange/{exchangeCode}/companies")
	public ResponseEntity<List<CompanyDTO>> getExchangeCompanyList(@PathVariable(value="exchangeCode") String exchangeCode,
			@RequestParam(name="searchCriteria",required=false) String searchCriteria ) {

		List<CompanyDTO> exchangeCompanyList = null;
		try {
			exchangeCompanyList = capitalMarketService.getExchangeCompanyList(exchangeCode,searchCriteria);	
		} catch (Exception e) {

			e.printStackTrace();

			if(e.getLocalizedMessage().equals("No data found")){
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.NO_CONTENT);
			}

			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<CompanyDTO>>(HttpStatus.UNAUTHORIZED);
			}

			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<CompanyDTO>>(exchangeCompanyList, HttpStatus.OK);
	}


	@GetMapping("/companies/{companyId}/metadata/info")
	public ResponseEntity<CompanyMetaData> getCompanyMetaData(@PathVariable(value = "companyId") String companyId,
			@RequestParam(value="currency", required=false) String currency) {

		_log.info("Getting company MetaData of the company:- "+ companyId +", currency: "+ currency );
		CompanyMetaData  companyMetaData = null;
		try {
			currency = (currency != null && currency.isEmpty()) ? null : currency;
			companyMetaData = capitalMarketService.getCompanyMetaData(companyId,currency);
		} catch (Exception e) {
			_log.error("Problem occured in getting company MetaData of the company: "+companyId);
			e.printStackTrace();
			if (e.getLocalizedMessage() != null && e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<CompanyMetaData>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<CompanyMetaData>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("Getting company MetaData of the company completed:- "+companyId);
		return new ResponseEntity<CompanyMetaData>(companyMetaData, HttpStatus.OK);
	}

	// NEW CODE FOR PDF DOWNLOAD

	@RequestMapping(value = "downloadCompanyProfile", method = RequestMethod.POST)
	@ApiOperation(value = "Download the Capirtal Market  Company Profile")
	public ResponseEntity<String> downloadCompanyProfile(@RequestParam(name="companyId",required=true) String companyId,@RequestParam(name="currency",required=false) String currency, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
			//companyId = "N2CVVM-R";//"C7MPD4-R";//"C7MPD4-R";//"DH9S6B-R";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
			String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
			// CREATE FOLDER
			if(currency!=null && currency.equals("")) {
				currency = null;
			}
			File dirFile = new File(cpDownloadPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("COMPANY PROFILE DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create directory!");
				}
			}

			//CREATE FOLDER END
			String mainJasperFile = cpJasperTemplatePath+"/"+CMStatic.COMPANY_PROFILE_FILE_NAME+CMStatic.JASPER_EXTENSION;

			_log.info("Main Jasper File : "+mainJasperFile);
			String pdfFileName = cpDownloadPath+"/"+companyId+"-"+fsrn+".pdf";

			if(companyId!=null && !companyId.equals("")) {
				ReportCompanyProfile companyProfile = jasperService.getCompanyProfileData(companyId,currency);
				jasperService.generateCompanyProfile(companyProfile, mainJasperFile, pdfFileName,imagePath);  
				File file = new File(pdfFileName);
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fsrn) );
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.setContentType("application/pdf");
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

			}

		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("No Data Found")){
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


	@RequestMapping(value = "downloadIndustryMonitor", method = RequestMethod.POST)
	@ApiOperation(value = "Generate the Banking Industry page")
	public ResponseEntity<String> downloadIndustryMonitor(@RequestBody IndustryMonitorRequest industryMonitorRequest, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		try {
			ReportIndustryMonitor reportIndustryMonitor = new ReportIndustryMonitor(industryMonitorRequest);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
			String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);

			File dirFile = new File(imDownloadPath);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("INDUSTRY MONITOR DOWNLOAD FOLDER is created!");
				} else {
					_log.info("Failed to create INDUSTRY MONITOR DOWNLOAD directory!");
				}
			}

			//CREATE FOLDER END
			String mainJasperFile =imJasperTemplatePath+"/"+CMStatic.INDUSTRY_MONTITOR_FILE_NAME+CMStatic.JASPER_EXTENSION;
			String pdfFileName = imDownloadPath+"/"+reportIndustryMonitor.getTicsIndustryCode()+"-"+fsrn+".pdf";

			if(reportIndustryMonitor!=null) {
				reportIndustryMonitor = jasperService.getIndustryMonitorData(response, reportIndustryMonitor);
				if(reportIndustryMonitor!=null){
					boolean status = jasperService.generateIndustryMonitor(reportIndustryMonitor, mainJasperFile, pdfFileName,imagePath);  
					if(status){
						_log.info("Report generated successfully");
						File file = new File(pdfFileName);
						response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fsrn) );
						response.setHeader("Set-Cookie", "fileDownload=true; path=/");
						response.setContentType("application/pdf");
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
					}else{
						return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}else{
					throw new Exception("No Data Found");
				}
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
			if(e.getMessage().contains("No Data Found")){
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<String>(headers,HttpStatus.NO_CONTENT);
			}

			if (e.getMessage().contains("Invalid Request")) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@GetMapping("/refreshCMCache")
	public String refreshCache() {
		_log.info("Refreshing CM Cache");
		try {
			cacheManager.getCacheNames().stream().forEach(name -> {
				Cache cache = cacheManager.getCache(name);
				cache.clear();
			});
		} catch (Exception e) {
			_log.error("failed to refresh cache", e);
			return "Internal Server Error!";
		}
		_log.info("Refreshed CM Cache");
		return "CACHE REFRESHED";
	}


	@GetMapping("/global-search/{keyword}/search")
	public ResponseEntity<GlobalSearchResponseModel> getCMGlobalSearchByKeyword(@PathVariable(value="keyword") String keyword,
			@RequestParam(value="resultCount") Integer resultCount,
			@RequestParam(value = "userCountryList",required=false) List<String> userCountryList) {

		GlobalSearchResponseModel searchResults = null;

		try {
			_log.info("keyword "+keyword);
			keyword = URLDecoder.decode(keyword,"UTF-8");
			_log.info("keyword "+keyword);
			keyword = keyword.trim();
			_log.info("start time "+new Date());
			searchResults = capitalMarketService.getCMGlobalSearchByKeyword(keyword,resultCount,userCountryList);
			_log.info("end time "+new Date());
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<GlobalSearchResponseModel>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<GlobalSearchResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GlobalSearchResponseModel>(searchResults, HttpStatus.OK);
	}
	@GetMapping("/advancecompanysearch")
	public ResponseEntity<List<? extends Object>> getAdvancedCompanySearch(@RequestParam(value = "entitySelection",required=false) String entitySelection,
			@RequestParam(value = "industrySelection",required=false) String industrySelection,
			@RequestParam(value = "countrySelection",required=false) String countrySelection,
			@RequestParam(value = "searchKeyWord",required=false) String searchKeyWord,
			@RequestParam(value = "currencySelection",required=false) String currencySelection) {

		List<? extends Object> searchResults = null;
		try {
			if (industrySelection!=null) {
				
				industrySelection = URLDecoder.decode(industrySelection,"UTF-8");
			}
			
			if(searchKeyWord!=null){
				searchKeyWord = java.net.URLDecoder.decode(searchKeyWord, StandardCharsets.UTF_8.name());
			}else{
				searchKeyWord = "";
			}
			_log.info("entitySelection ::::: " + entitySelection + "\nindustrySelection ::: " + industrySelection + "\ncountrySelection :: " + countrySelection);
			_log.info("searchKeyWord :::: " + searchKeyWord + "\ncurrencySelection :::: " + currencySelection);
			
			searchResults = capitalMarketService.getAdvancedCompanySearch(entitySelection,industrySelection,countrySelection,currencySelection,searchKeyWord);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<? extends Object>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<? extends Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<? extends Object>>(searchResults, HttpStatus.OK);
	}

	@GetMapping("/entityStructure/{entityId}")
	@ApiOperation(value = "Entity Structure of Companies")
	public ResponseEntity<List<EntityStructureDTO>> getEntityStructure(@PathVariable(value = "entityId") String entityId) {

		List<EntityStructureDTO> entityStructureDTOs = null;

		try {
			entityStructureDTOs = capitalMarketService.getEntityStructure(entityId);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<EntityStructureDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage(), e);
			return new ResponseEntity<List<EntityStructureDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<EntityStructureDTO>>(entityStructureDTOs, HttpStatus.OK);
	}


	@RequestMapping(value = "/downloadEntityStructure/{entityId}", method = RequestMethod.POST)
	//@GetMapping("/downloadEntityStructure/{entityId}")
	@ApiOperation(value = "Download Entity Structure Excel")
	public ResponseEntity<String> downloadEntityStructure(@PathVariable(value = "entityId") String entityId, HttpServletRequest request, HttpServletResponse response) {
		_log.info("Downloading the entity Structure for the Entity Id :: " + entityId);
		List<EntityStructureDTO> entityStructureDTOs = null;
		try {
			entityStructureDTOs = capitalMarketService.getEntityStructure(entityId);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
			String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
			String path = "/data/PRIVATE_COMPANY_DOWNLOAD";

			File dirFile = new File(path);
			if (!dirFile.exists()) {
				if (dirFile.mkdir()) {
					_log.info("Private Company folder created");
				} else {
					_log.error("Failed to create Private Company folder!!!!!");
				}
			}

			String downloadedFile = path+"/"+"EntityStructure"+fsrn+".xls";
			File file = new File(downloadedFile); 
			HSSFWorkbook workbook = capitalMarketService.createEntityStructureExcelReport(entityStructureDTOs);		
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);

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

		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}



}
