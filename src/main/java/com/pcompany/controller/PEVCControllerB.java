package com.pcompany.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcompany.dto.ChartFundingDetailDTO;
import com.pcompany.dto.ChartIndustryFundingDetailDTO;
import com.pcompany.dto.TopFundedCompaniesDTO;
import com.pcompany.model.PEVCSummaryModel;
import com.pcompany.service.PeVCServiceB;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "PE & VC", description = "Rest API for PE & VC", tags = "PE & VC ")
public class PEVCControllerB {

	Logger _log = Logger.getLogger(PEVCControllerB.class);

	@Autowired
	PeVCServiceB peVCServiceB;

	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;

	@GetMapping("/pevc/summary/countryChart")
	public ResponseEntity<List<ChartFundingDetailDTO>> getPeVcSummaryChartByCountry(
			@RequestParam(name = "countryIsoCode") String countryIsoCode,
			@RequestParam(name = "currency") String currency,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		_log.info("getting Summary Chart by country " + countryIsoCode + " startDate " + startDate + " endDate "
				+ endDate + " Currency " + currency);

		List<ChartFundingDetailDTO> fundingDetails = null;

		try {
			fundingDetails = peVCServiceB.getPeVcSummaryChartByCountry(countryIsoCode, currency, startDate, endDate);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<ChartFundingDetailDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<ChartFundingDetailDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ChartFundingDetailDTO>>(fundingDetails, HttpStatus.OK);
	}

	@GetMapping("/pevc/summary/sectorCharts")
	public ResponseEntity<List<ChartFundingDetailDTO>> getPeVcSummaryChartBySector(
			@RequestParam(name = "countryIsoCode") String countryIsoCode,
			@RequestParam(name = "currency") String currency,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		_log.info("getting Summary Chart by country " + countryIsoCode + " startDate " + startDate + " endDate "
				+ endDate + " Currency " + currency);

		List<ChartFundingDetailDTO> fundingDetails = null;

		try {
			fundingDetails = peVCServiceB.getPeVcSummaryChartBySector(countryIsoCode, currency, startDate, endDate);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<ChartFundingDetailDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<ChartFundingDetailDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ChartFundingDetailDTO>>(fundingDetails, HttpStatus.OK);
	}

	@GetMapping("/pevc/summary/industryChart")
	public ResponseEntity<List<ChartIndustryFundingDetailDTO>> getPeVcSummaryChartByIndustry(
			@RequestParam(name = "countryIsoCode") String countryIsoCode,
			@RequestParam(name = "currency") String currency, @RequestParam(name = "sectorCode") Integer sectorCode,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		_log.info("getting Summary Chart by country " + countryIsoCode + " startDate " + startDate + " endDate "
				+ endDate + " Currency " + currency);

		List<ChartIndustryFundingDetailDTO> fundingDetails = null;

		try {
			fundingDetails = peVCServiceB.getPeVcSummaryChartByIndustry(countryIsoCode, currency, sectorCode, startDate,
					endDate);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<ChartIndustryFundingDetailDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<ChartIndustryFundingDetailDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<ChartIndustryFundingDetailDTO>>(fundingDetails, HttpStatus.OK);
	}

	@GetMapping("/pevc/topFundedCompanies")
	public ResponseEntity<List<TopFundedCompaniesDTO>> getPeVcTopFundedCompanies(
			@RequestParam(name = "countryIsoCode") String countryIsoCode,
			@RequestParam(name = "currency") String currency, @RequestParam(name = "limit") Integer limit,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		_log.info("getting Top Funded Companies" + countryIsoCode + " startDate " + startDate + " endDate " + endDate
				+ " Currency " + currency);

		List<TopFundedCompaniesDTO> topFundingDetails = null;

		try {
			topFundingDetails = peVCServiceB.getPeVcTopFundedCompanies(countryIsoCode, currency, startDate, endDate,
					limit);
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<TopFundedCompaniesDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<TopFundedCompaniesDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<TopFundedCompaniesDTO>>(topFundingDetails, HttpStatus.OK);
	}

	@RequestMapping(value = "/pevc/summary/download", method = RequestMethod.POST)
	@ApiOperation(value = "Download Pe/Vc data in Excel Format")
	public ResponseEntity<String> downloadDealHistory(@RequestParam(name = "countryIsoCode") String countryIsoCode,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "currency") String currency,
			@RequestParam(name = "endDate", required = false) String endDate, HttpServletRequest httpServletRequest,
			HttpServletResponse response) throws ParseException {

		_log.info("Download Pe/Vc Summary Data for  countryIsoCode : " + countryIsoCode + " startDate" + startDate
				+ " endDate" + endDate + " Currency " + currency);

		PEVCSummaryModel peVcSummaryData = null;

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");

		Date start = null;
		Date end = null;

		if (startDate != null) {
			start = format.parse(startDate);
		}

		if (endDate != null) {
			end = format.parse(endDate);
		}

		String fsrn = dateFormat.format(new Date()) + (int) (Math.random() * 10);

		try {
			peVcSummaryData = peVCServiceB.peVcSummaryDetails(countryIsoCode, currency, start, end);

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

			HSSFWorkbook workbook = peVCServiceB.createFundingSummaryExcel(peVcSummaryData);

			FileOutputStream outputStream = new FileOutputStream(reportPath);

			workbook.write(outputStream);

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

}
