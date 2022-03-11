package com.pcompany.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.pcompany.dto.MNADealTermSynopsis;
import com.pcompany.service.MNAServiceD;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Company MNA", description = "Rest API for merger and acquisition", tags = "Company MNA")
public class MNAControllerD {

	Logger _log = Logger.getLogger(MNAControllerD.class);
	
	@Autowired
	private MNAServiceD mnaService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/mna/dealTermSynopsis")
	public ResponseEntity<MNADealTermSynopsis> getDealTermSynopsis(
			@RequestParam(name="dealId",required=true) Integer dealId,
			@RequestParam(name="entityId",required=true) String entityId,
			@RequestParam(name="currency",required=false) String currency) {
		
		_log.info("Extracting M&A Deal Term And Synopsis for dealId: "+ dealId +", currency: "+ dealId);
		
		MNADealTermSynopsis dealTermSynopsis;
		try {
			dealTermSynopsis = mnaService.getDealTermSynopsis(dealId, entityId, currency);
			//Call service to get MNA Price and EPS Performance
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<MNADealTermSynopsis>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<MNADealTermSynopsis>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out M&A Deal Term Synopsis for dealId: "+ dealId);
		return new ResponseEntity<MNADealTermSynopsis>(dealTermSynopsis, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/mna/dealTermSynopsisDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download M&A Deal Term And Synopsis data in Excel Format")
	public ResponseEntity<String> downloadDealTermSynopsis(
			@RequestParam(name="dealId",required=true) Integer dealId,
			@RequestParam(name="entityId",required=true) String entityId,
			@RequestParam(name="currency",required=false) String currency,
			HttpServletRequest request, HttpServletResponse response) {
		
		_log.info("Download M&A Deal Term And Synopsis for dealId: "+ dealId +", currency: "+ dealId);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			MNADealTermSynopsis dealTermSynopsis = mnaService.getDealTermSynopsis(dealId, entityId, currency);
			
			// CREATE FOLDER for Downloaded report
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
			
			HSSFWorkbook workbook = mnaService.createDealTermSynopsisExcel(dealTermSynopsis);
			FileOutputStream outputStream = new FileOutputStream(reportPath);
			workbook.write(outputStream);
			
			_log.info("M&A Deal Term And Synopsis report generated for dealId: "+dealId+" at: "+ reportPath);
			File file = new File(reportPath);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fsrn+".xlsx") );
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
			e.printStackTrace();
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
