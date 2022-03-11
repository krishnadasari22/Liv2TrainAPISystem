package com.televisory.capitalmarket.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.math3.exception.NoDataException;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.televisory.capitalmarket.service.JasperReportCreditReportService;
import com.televisory.capitalmarket.service.JasperReportService;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.user.dto.CreditReportInfo;
import com.televisory.utils.LoanStatic;


@RestController
@Api(value = "Loan", description = "Rest API for Loan Application", tags = "Loan Application API")
public class LoanApplicationController {

	public static Logger _log = Logger.getLogger(LoanApplicationController.class);


	@Autowired
	JasperReportCreditReportService jasperReportCreditReportService;

	@Autowired
	JasperReportService jasperReportService;

	@RequestMapping(value = "downloadCreditReport", method = RequestMethod.POST)
	@ApiOperation(value = "Generate the credit report")
	public ResponseEntity<String> downloadCreditReport(@RequestBody CreditReportInfo companyInfo, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		_log.info(new Gson().toJson(companyInfo));
		try{
			/*if(companyInfo.getUserId()==null || companyInfo.getUserId().equals("") || LoanStatic.PORTFOLIO.equalsIgnoreCase(companyInfo.getRequestType())){
				companyInfo.setUserId("rahul.gautam@televisory.com");
				companyInfo.setApplicableToYear("2018-12-31");
				companyInfo.setApplicableFromYear("2016-03-31");
			}*/
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
			String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
			/////////////////////////////
			String mainJasperFile  = LoanStatic.LOAN_MAIN_FILE_PATH ;
			String mainTocJasperFile = LoanStatic.FINANCIAL_SNAPSHOT_AND_TOC_MAIN_FILE_PATH;
			String pdfFileNameMain = LoanStatic.LOAN_REPORT_FILE_PATH +"Credit_Report"+fsrn+".pdf" ; 
			String pdfFileNameOutput = LoanStatic.LOAN_REPORT_FILE_PATH +"Credit_Report_Output"+fsrn+".pdf" ; 
			/////////////////////////////


			if(LoanStatic.PORTFOLIO.equalsIgnoreCase(companyInfo.getRequestType())){
				mainJasperFile  = LoanStatic.PORTFOLIO_LOAN_MAIN_FILE_PATH ;
				mainTocJasperFile = LoanStatic.PORTFOLIO_FINANCIAL_SNAPSHOT_AND_TOC_MAIN_FILE_PATH;
				pdfFileNameMain = LoanStatic.PORTFOLIO_LOAN_REPORT_FILE_PATH +"Credit_Report"+fsrn+".pdf" ; 
				pdfFileNameOutput = LoanStatic.PORTFOLIO_LOAN_REPORT_FILE_PATH +"Credit_Report_Output"+fsrn+".pdf" ; 
			}


			//String mainJasperFile ="/Data-drive/gitnav/CapitalMarket/Credit_Report/Credit_Rating_Report_Main"+CMStatic.JASPER_EXTENSION;
			//String mainTocJasperFile ="/Data-drive/gitnav/CapitalMarket/Credit_Report/Toc_Main"+CMStatic.JASPER_EXTENSION;
			
			if(!isFromAndToDateValid(companyInfo)){
				_log.error("Invalid Request");
				return new ResponseEntity<String>("Report time duration cannot exceeds more than five years.", HttpStatus.BAD_REQUEST);
			 }

			HashMap<String, Object> parametersList = null;
			try{
				parametersList = jasperReportCreditReportService.generateCreditReport(mainJasperFile,pdfFileNameMain, companyInfo);
				if(null==parametersList){
					throw new NoDataException();
				}
			}catch(Exception e){
				throw e;
			}

			boolean statusMainRepoort = jasperReportService.generatePdf( mainJasperFile, pdfFileNameMain, parametersList);

			if(statusMainRepoort){
				List<String> pageList = jasperReportCreditReportService.getTableOfContent(pdfFileNameMain,companyInfo);
				parametersList.put("pageList",pageList);
				boolean statusOutput = jasperReportService.generatePdf( mainTocJasperFile, pdfFileNameOutput, parametersList);
				File mainFile = new File(pdfFileNameMain);
				if(statusOutput && statusMainRepoort){
					File file = new File(pdfFileNameOutput);
					response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()) );
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
					mainFile.delete();
				}else{
					return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}else{
				return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (Exception e) {
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
	
	private boolean isFromAndToDateValid(CreditReportInfo companyInfo) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String toDate = companyInfo.getApplicableToYear();
		String fromDate = companyInfo.getApplicableFromYear();
		Date to = sdf.parse(toDate);
		Date from = sdf.parse(fromDate);
		long diffInMilliSec = to.getTime() - from.getTime();
		long years =  (diffInMilliSec / (1000l * 60 * 60 * 24 * 365));
		int diff = (int) years + 1;
		if(diff > 5){
			_log.info("Number of years between "+companyInfo.getApplicableFromYear() +" and "+ companyInfo.getApplicableToYear()+" is "+diff);
			return false;
		}
		return true;
	}

	public static void main(String[] args) {

		JasperReportService js = new JasperReportService();
		System.out.println("Value is ::: " + js.customization(BigDecimal.valueOf(11d), null, null, null));
	}

}
