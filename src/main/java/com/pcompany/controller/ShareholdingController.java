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
import java.util.HashMap;
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

import com.google.gson.Gson;
import com.pcompany.dto.InstitutionalShareholdingOwnershipDetailsDTO;
import com.pcompany.dto.ShareholdingOwnershipDetailsDTO;
import com.pcompany.model.OwnershipModel;
import com.pcompany.service.ShareholdingService;
import com.televisory.capitalmarket.util.BadRequestException;
import com.televisory.capitalmarket.util.CMStatic;

@RestController
@Api(value = "Shareholders", description = "Rest API for Shareholders Information", tags = "CM Shareholders Information")
public class ShareholdingController {
	Logger _log = Logger.getLogger(ShareholdingController.class);

	@Value("${CM.DOWNLOAD.REPORT.PATH}")
	private String cmReportPath;

	@Autowired
	ShareholdingService shareholdingService; 

	@GetMapping("/shareholdings/ownership/details")
	public ResponseEntity<List<OwnershipModel>> getOwnershipDetails(
			@RequestParam("securityId") String securityId) {
		List<OwnershipModel> details = new ArrayList<>();
		try {
			details = shareholdingService.getOwnershipDetails(securityId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<OwnershipModel>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<OwnershipModel>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getOwnershipManagementInfo");
		return new ResponseEntity<List<OwnershipModel>>(details, HttpStatus.OK);
	}

	@GetMapping("/shareholdings/ownership/details/recent")
	public ResponseEntity<List<OwnershipModel>> getOwnershipDetailsRecent(
			@RequestParam("securityId") String securityId) {
		List<OwnershipModel> details = new ArrayList<>();
		try {
			details = shareholdingService.getOwnershipDetailsRecent(securityId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<OwnershipModel>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<OwnershipModel>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getOwnershipManagementInfo");
		return new ResponseEntity<List<OwnershipModel>>(details, HttpStatus.OK);
	}


	@GetMapping("/shareholdings/institutional")
	public ResponseEntity<List<InstitutionalShareholdingOwnershipDetailsDTO>> getInstitutionalOwnershipDetails(
			@RequestParam("securityId") String securityId) {
		List<InstitutionalShareholdingOwnershipDetailsDTO> details = null;
		try {
			_log.info(" securityId " + securityId);
			details = shareholdingService.getInstitutionalOwnershipDetails(securityId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<InstitutionalShareholdingOwnershipDetailsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<InstitutionalShareholdingOwnershipDetailsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getOwnershipManagementInfo");
		return new ResponseEntity<List<InstitutionalShareholdingOwnershipDetailsDTO>>(details, HttpStatus.OK);

	}

	@GetMapping("/shareholdings/insider/details")
	public ResponseEntity<List<ShareholdingOwnershipDetailsDTO>> getInsiderOwnershipDetails(
			@RequestParam("securityId") String securityId) {
		List<ShareholdingOwnershipDetailsDTO> details = null;
		try {
			_log.info(" securityId " + securityId);
			details = shareholdingService.getInsiderDetails(securityId);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<List<ShareholdingOwnershipDetailsDTO>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<List<ShareholdingOwnershipDetailsDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getOwnershipManagementInfo");
		return new ResponseEntity<List<ShareholdingOwnershipDetailsDTO>>(details, HttpStatus.OK);

	}

	@RequestMapping(value = "downloadShareHolding", method = RequestMethod.POST)
	@ApiOperation(value = "Download the required data in Excel Format")
	public ResponseEntity<String> downloadShareHolding(@RequestParam("entityId") String securityId,@RequestParam("companyName") String companyName, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM-ddHH-mmss-SSS");
		String fsrn = dateFormat.format(new Date()) + (int)(Math.random() * 10);
		try {
			_log.info("Request received for: "+ securityId);

			List<ShareholdingOwnershipDetailsDTO> ownerDetails = shareholdingService.getInsiderDetails(securityId);
			List<InstitutionalShareholdingOwnershipDetailsDTO> institutionalDetails = shareholdingService.getInstitutionalOwnershipDetails(securityId);
			List<OwnershipModel>ownershipModels=shareholdingService.getOwnershipDetails(securityId);
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
			HSSFWorkbook workbook = shareholdingService.createExcelReport(ownershipModels,ownerDetails,institutionalDetails,securityId,companyName);
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


	@GetMapping("/shareholdings/common")
	public ResponseEntity<HashMap<String, Object>> getShareholdingCommon(
			@RequestParam("securityId") String securityId) {


		HashMap<String, Object> myMap = new HashMap<>();

		List<ShareholdingOwnershipDetailsDTO> insiderDetails = null;
		List<InstitutionalShareholdingOwnershipDetailsDTO> institutionDetails = null;
		Date period = null;

		try {
			_log.info(" securityId " + securityId);
			insiderDetails = shareholdingService.getInsiderDetails(securityId);
			institutionDetails = shareholdingService.getInstitutionalOwnershipDetails(securityId);

			double totalPercentShareInsider = 0d;

			for (ShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : insiderDetails) {
				if(period==null){
					period = shareholdingOwnershipDetailsDTO.getReportDate();
				}
				Double percentShare = shareholdingOwnershipDetailsDTO.getPercent();
				if(percentShare!=null){
					totalPercentShareInsider = totalPercentShareInsider + percentShare;
				}
			}

			double totalPercentShareInstitutional = 0d;
			for (InstitutionalShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : institutionDetails) {
				if(period==null){
					period = shareholdingOwnershipDetailsDTO.getReportDate();
				}
				Double percentShare = shareholdingOwnershipDetailsDTO.getPercent();
				if(percentShare!=null){
					totalPercentShareInstitutional = totalPercentShareInstitutional + percentShare;
				}
			}

			double totalPercentShareOther = 100-(totalPercentShareInsider+totalPercentShareInstitutional);

			List<OwnershipModel> details = new ArrayList<>();
			OwnershipModel ownershipModel = new OwnershipModel();
			ownershipModel.setId(1);
			ownershipModel.setCompanyId("Promoters/Insiders");
			ownershipModel.setPeriod(period);
			ownershipModel.setTotal(totalPercentShareInsider);
			details.add(ownershipModel);
			OwnershipModel ownershipModel1 = new OwnershipModel();
			ownershipModel1.setId(2);
			ownershipModel1.setCompanyId("Institution");
			ownershipModel1.setPeriod(period);
			ownershipModel1.setTotal(totalPercentShareInstitutional);
			details.add(ownershipModel1);
			OwnershipModel ownershipModel2 = new OwnershipModel();
			ownershipModel2.setId(3);
			ownershipModel2.setCompanyId("Public & Others");
			ownershipModel2.setPeriod(period);
			ownershipModel2.setTotal(totalPercentShareOther);
			details.add(ownershipModel2);

			myMap.put("ownership", details);
			myMap.put("institutional", details);
			myMap.put("individual", details);

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().contains("401")) {
				_log.error(e.getMessage());
				return new ResponseEntity<HashMap<String, Object>>(HttpStatus.UNAUTHORIZED);
			}
			_log.error(e.getMessage());
			return new ResponseEntity<HashMap<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		_log.info("out getOwnershipManagementInfo");
		return new ResponseEntity<HashMap<String, Object>>(myMap, HttpStatus.OK);

	}

}