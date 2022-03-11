package com.pcompany.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
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

import com.pcompany.dto.DebtCapitalStructureDetailsDTO;
import com.pcompany.service.DebtCapitalStructureService;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "DebtCapitalStructure", description = "Rest API for Debt Capital Structure", tags = "CM Debt Capital Structure")
public class DebtCapitalStructureController {
	
	Logger _log = Logger.getLogger(DebtCapitalStructureController.class);
	
	@Autowired
	DebtCapitalStructureService debtService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/debtstructure/debt")
	public ResponseEntity<List<DebtCapitalStructureDetailsDTO>> getDebt(@RequestParam("entityId") String entityId){
		List<DebtCapitalStructureDetailsDTO> debtData = null;
		try {
			_log.info(" entityId "+entityId);
			debtData = debtService.getFactsetEntityDebt(entityId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(debtData, HttpStatus.OK);
		
	}
	
	@GetMapping("/debtstructure/borrowinglimit")
	public ResponseEntity<List<DebtCapitalStructureDetailsDTO>> getBorrowingLimit(@RequestParam("entityId") String entityId){
		List<DebtCapitalStructureDetailsDTO> debtData = null;
		try {
			debtData = debtService.getBorrowingLimit(entityId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(debtData, HttpStatus.OK);
		
	}
	
	@GetMapping("/debtstructure/capitalcharges")
	public ResponseEntity<List<DebtCapitalStructureDetailsDTO>> getCapitalCharges(@RequestParam("entityId") String entityId){
		List<DebtCapitalStructureDetailsDTO> debtData = null;
		try {
			debtData = debtService.getCapitalCharges(entityId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getFactSetCompanyStockPrice");
		return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(debtData, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "downloadDebtStructure", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> createForm(@RequestParam("entityId") String entityId,@RequestParam("companyName") String companyName, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info("Request received for: "+ entityId);
			List<DebtCapitalStructureDetailsDTO> debtData = debtService.getFactsetEntityDebtDownload(entityId);
			//List<DebtCapitalStructureDetailsDTO> borrowingData = debtService.getBorrowingLimit(entityId);	
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
			companyName = URLDecoder.decode(companyName, "UTF-8");
			_log.info(" companyName "+companyName);
			
			//CREATE FOLDER END
			String reportPath = cmReportPath+"/"+fsrn+".xls";
			HSSFWorkbook workbook = debtService.createExcelReport(debtData,null,entityId,companyName);
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
	
	@GetMapping("/debtstructure/maturity")
	public ResponseEntity<List<DebtCapitalStructureDetailsDTO>> getDebtMaturity(@RequestParam("entityId") String entityId){
		List<DebtCapitalStructureDetailsDTO> debtData = null;
		try {
			_log.info(" entityId "+entityId);
			debtData = debtService.getEntityMaturity(entityId);	
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<DebtCapitalStructureDetailsDTO>>(debtData, HttpStatus.OK);
		
	}

}
