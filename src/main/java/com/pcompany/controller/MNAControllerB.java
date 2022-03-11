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

import com.pcompany.dto.MNATopDealMakerDTO;
import com.pcompany.dto.MNATopDealMakerTotalDTO;
import com.pcompany.model.MNATopDealMakerResponse;
import com.pcompany.service.MNAServiceB;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

@RestController
@Api(value = "Company MNA", description = "Rest API for merger and acquisition", tags = "Company MNA")
public class MNAControllerB {

	Logger _log = Logger.getLogger(MNAControllerB.class);
	
	@Autowired
	private MNAServiceB mnaService;
	
	@Autowired 
	private ExecutorService executorService;
	
	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;
	
	@GetMapping("/mna/topDealMaker")
	public ResponseEntity<MNATopDealMakerResponse> getTopDealMaker(
			@RequestParam(name="country",required=false) String country,
			@RequestParam(name="industry",required=false) String industry,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			@RequestParam(name="rowOffset",required=false) Integer rowOffset,
			@RequestParam(name="rowCount",required=false) Integer rowCount,
			@RequestParam(name="sortingColumn",required=false) String sortingColumn,
			@RequestParam(value="sortingType",required=false) String sortingType) {
		_log.info("Extracting M&A topDealMaker for country:"+country+" industry:"+industry+" periodStart:"+periodStart+" periodEnd:"+periodEnd+" currency:"+currency+" rowOffset:"+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
		MNATopDealMakerResponse response = null;
		try {
			String sdf = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
			Date startDate = simpleDateFormat.parse(periodStart);
			Date endDate = simpleDateFormat.parse(periodEnd);
			
			Future<MNATopDealMakerResponse> topDealMakerFut = executorService.submit(() -> {
				return mnaService.getTopDealMaker(country,industry,currency,startDate,endDate,rowOffset,rowCount,sortingColumn,sortingType);
			});
			
			Future<Long> topDealMakerCountFut = executorService.submit(() -> {
				return mnaService.getTopDealMakerCount(country,industry,currency,startDate,endDate);
			});
			
			Future<MNATopDealMakerTotalDTO> topDealMakerTotalFut = executorService.submit(() -> {
				return mnaService.getTopDealMakerTotal(country, industry, currency, startDate, endDate);
			});
			
			//get MNA Top Deal Maker
			response = topDealMakerFut.get();
			//get top deals maker count
			response.setTotalCount(topDealMakerCountFut.get());
			//get top deals maker total
			response.setTopDealTotal(topDealMakerTotalFut.get());
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());		
				return new ResponseEntity<MNATopDealMakerResponse>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<MNATopDealMakerResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getTopDealMaker");
		return new ResponseEntity<MNATopDealMakerResponse>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "mna/topDealMakerDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> downloadShareHolding(
			@RequestParam(name="country",required=false) String country,
			@RequestParam(name="industry",required=false) String industry,
			@RequestParam(name="currency") String currency,
			@RequestParam(name="periodStart") String periodStart,
			@RequestParam(name="periodEnd") String periodEnd,
			//@RequestParam(name="rowOffset",required=false) Integer rowOffset,
			@RequestParam(name="rowCount",required=false) Integer rowCount,
			@RequestParam(name="sortingColumn",required=false) String sortingColumn,
			@RequestParam(value="sortingType",required=false) String sortingType,
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info("Extracting M&A topDealMaker for country:"+country+" industry:"+industry+" periodStart:"+periodStart+" periodEnd:"+periodEnd+" currency:"+currency);
			
			String sdf = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
			Date startDate = simpleDateFormat.parse(periodStart);
			Date endDate = simpleDateFormat.parse(periodEnd);
			
			List<MNATopDealMakerDTO> topDealMakerList = mnaService.getTopDealMakerList(country, industry, currency, startDate, endDate, 0, rowCount, sortingColumn, sortingType);
			
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
			HSSFWorkbook workbook = mnaService.createExcelReport(country, industry, startDate, endDate,rowCount, topDealMakerList);
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

		}/* catch(BadRequestException e){
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}*/ catch (Exception e) {
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
