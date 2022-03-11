package com.televisory.capitalmarket.downloadreport;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.televisory.capitalmarket.model.DownloadRequest;
import com.televisory.capitalmarket.service.ExcelReportService;
import com.televisory.capitalmarket.util.CMStatic;

public class CapitalMarketDownloadReport {
	
	public static Workbook createExcelReport(DownloadRequest downloadRequest) throws Exception {
		
		ExcelReportService excelService =new ExcelReportService();
		try {
		Workbook workbook = excelService.createExcelReport(downloadRequest);
			_log.info("Capital Market report report generated for SSRN: ");
			return workbook;
			
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}

	}
	
	static Logger _log = Logger.getLogger(CapitalMarketDownloadReport.class);
	
}
