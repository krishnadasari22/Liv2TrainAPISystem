package com.pcompany.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcompany.model.MNAPriceEpsPerformanceModel;
import com.pcompany.service.MNAServiceC;
import com.privatecompany.dto.CompanyEpsDTO;
import com.privatecompany.dto.DealHistoryDTO;
import com.televisory.capitalmarket.util.CMStatic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Company MNA", description = "Rest API for merger and acquisition", tags = "Company MNA")
public class MNAControllerC {

	Logger _log = Logger.getLogger(MNAControllerC.class);

	@Autowired
	MNAServiceC mnaService;

	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;

	@RequestMapping(value = "/mna/priceEpsPerformance", method = RequestMethod.POST)
	public ResponseEntity<MNAPriceEpsPerformanceModel> getPriceEpsPerformance(
			@RequestBody MNAPriceEpsPerformanceModel mnaPriceEpsPerformance) {

		_log.info("Extracting M&A priceEpsPerformance for ..." + mnaPriceEpsPerformance.getCompanyId() + " startDate "
				+ mnaPriceEpsPerformance.getStartDate() + " endDate " + mnaPriceEpsPerformance.getEndDate());

		try {
			mnaService.getPriceEpsPerformance(mnaPriceEpsPerformance);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<MNAPriceEpsPerformanceModel>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<MNAPriceEpsPerformanceModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<MNAPriceEpsPerformanceModel>(mnaPriceEpsPerformance, HttpStatus.OK);
	}

	@RequestMapping(value = "/mna/priceEpsPerformanceDownload", method = RequestMethod.POST)
	public ResponseEntity<String> priceEpsPerformanceDownload(
			@RequestBody MNAPriceEpsPerformanceModel mnaPriceEpsPerformance, HttpServletRequest httpServletRequest,
			HttpServletResponse response) {

		_log.info("Extracting M&A priceEpsPerformance for ..." + mnaPriceEpsPerformance.getCompanyId() + " startDate "
				+ mnaPriceEpsPerformance.getStartDate() + " endDate " + mnaPriceEpsPerformance.getEndDate());

		MNAPriceEpsPerformanceModel performanceData;

		try {

			performanceData = mnaService.getPriceEpsPerformance(mnaPriceEpsPerformance);

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
			String reportPath = cmReportPath + "/" + performanceData.getCompanyId() + "epsStockPerformance" + ".xls";

			HSSFWorkbook workbook = mnaService.createEpsStockPerformanceExcel(performanceData);

			FileOutputStream outputStream = new FileOutputStream(reportPath);

			workbook.write(outputStream);

			File file = new File(reportPath);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"",
					performanceData.getCompanyId() + "epsStockPerformance" + ".xlsx"));
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

	@GetMapping("/mna/Companies/{entityId}/allDeal")
	public ResponseEntity<List<DealHistoryDTO>> getAllDealHistory(@PathVariable("entityId") String entityId,
			@RequestParam(name = "startDate", required = false) Date startDate,
			@RequestParam(name = "endDate", required = false) Date endDate,
			@RequestParam(name = "currency", required = false) String currency) {

		List<DealHistoryDTO> dealHistoryData;

		try {
			dealHistoryData = mnaService.getAllDealHistory(entityId, startDate, endDate, currency);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<DealHistoryDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<DealHistoryDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getMnaCompanies");
		return new ResponseEntity<List<DealHistoryDTO>>(dealHistoryData, HttpStatus.OK);
	}

	@GetMapping("/mna/Companies/eps")
	public ResponseEntity<List<CompanyEpsDTO>> getCompanyEps(@RequestParam(name = "companyId") String companyId,
			@RequestParam(name = "periodicity") String periodicity,
			@RequestParam(name = "startDate", required = false) Date startDate,
			@RequestParam(name = "endDate", required = false) Date endDate,
			@RequestParam(name = "currency", required = false) String currency) {

		List<CompanyEpsDTO> epsDataList;

		try {
			epsDataList = mnaService.getCompanyEps(companyId, periodicity, startDate, endDate, currency);

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<CompanyEpsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<CompanyEpsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<CompanyEpsDTO>>(epsDataList, HttpStatus.OK);
	}

	@RequestMapping(value = "/mna/dealHistroyDownload", method = RequestMethod.POST)
	@ApiOperation(value = "Download M&A Deal history data in Excel Format")
	public ResponseEntity<String> downloadDealHistory(@RequestParam(name = "entityId", required = true) String entityId,
			@RequestParam(name = "startDate", required = false) String periodStart,
			@RequestParam(name = "endDate", required = false) String periodEnd,
			@RequestParam(name = "currency", required = false) String currency, HttpServletRequest httpServletRequest,
			HttpServletResponse response) throws ParseException {

		_log.info("Download M&A Deal Histroy for entityId: " + entityId + ", currency: " + entityId);

		Date startDate = null;
		Date endDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int) (Math.random() * 10);
		List<DealHistoryDTO> dealHistoryData = null;
		String sdf = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf);
		if (null != periodStart) {
			startDate = simpleDateFormat.parse(periodStart);
		}
		if (null != periodEnd)
			endDate = simpleDateFormat.parse(periodEnd);

		try {
			dealHistoryData = mnaService.getAllDealHistory(entityId, startDate, endDate, currency);

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

			HSSFWorkbook workbook = mnaService.createDealHistroryExcel(dealHistoryData);
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
}
