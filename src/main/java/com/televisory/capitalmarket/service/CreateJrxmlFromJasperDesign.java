package com.televisory.capitalmarket.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ar.com.fdvs.dj.core.DynamicJasperHelper;

public class CreateJrxmlFromJasperDesign {

	protected JasperPrint jp;
	protected JasperReport jr;
	protected Map params = new HashMap<>();
	String dir ="/home/navankur/Desktop/";
	String fileName = "test";

	public void testReport() throws Exception {
		JasperDesignForTemplate  jasperDesignForTemplate = new  JasperDesignForTemplate();
		jr = buildReport();
		jp = JasperFillManager.fillReport(jr, params);
		//exportReport();

	}
	
	/*protected void exportReport() throws Exception {
		ReportExporter.exportReport(jp, "/home/navankur/Desktop/file.pdf");
		exportToJRXML();
	}*/

	protected void exportToJRXML() throws Exception {
		if (this.jr != null) {
			DynamicJasperHelper.generateJRXML(this.jr, "UTF-8", dir + "/" + fileName + ".jrxml");
		}
	}

	public JasperPrint getJasperPrint(){
		try {
			JasperPrint jp = JasperFillManager.fillReport("/data/resources/JASPER_TEMPLATES/COMPANY_PROFILE/Company_Profile.jasper", params, new JREmptyDataSource());
			return jp;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JasperReport buildReport(){
		JasperReport jr = null;
		return jr;
	}



}
