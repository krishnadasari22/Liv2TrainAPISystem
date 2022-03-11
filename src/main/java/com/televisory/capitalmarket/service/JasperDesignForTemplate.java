package com.televisory.capitalmarket.service;


/***
 * created by navankur chauhan,
 * 
 This file to be used for the dynamic creation of the jasper templates , and these dynamically created
 template should be used with the main template to integrate as the sub - report or any other way.
 /////////////////////Try calling the main method in this file for the result//////////////////
 */


import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.beanutils.BeanUtils;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.ConditionalStyle;
import ar.com.fdvs.dj.domain.entities.conditionalStyle.StatusLightCondition;

public class JasperDesignForTemplate {

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public DynamicReport buildReport() throws Exception {

		Style detailStyle = new Style();
		Style headerStyle = new Style();
		headerStyle.setFont(Font.ARIAL_BIG_BOLD);
		headerStyle.setBorderBottom(Border.PEN_2_POINT());
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setBackgroundColor(Color.LIGHT_GRAY);
		headerStyle.setTextColor(Color.WHITE);
		headerStyle.setTransparency(Transparency.OPAQUE);

		Style titleStyle = new Style();
		titleStyle.setFont(new Font(18,Font._FONT_VERDANA,true));
		Style amountStyle = new Style();
		amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		Style oddRowStyle = new Style();
		oddRowStyle.setBorder(Border.NO_BORDER());
		Color veryLightGrey = new Color(230,230,230);
		oddRowStyle.setBackgroundColor(veryLightGrey);oddRowStyle.setTransparency(Transparency.OPAQUE);

		DynamicReportBuilder drb = new DynamicReportBuilder();
		Integer margin = new Integer(20);
		drb.setTitle("Televisory Sample dynamic report")
		/*.setSubtitle("This report is property of televisory india pvt ltd")*/
		.setTitleStyle(titleStyle).setTitleHeight(new Integer(30))
		.setSubtitleHeight(new Integer(20))
		.setDetailHeight(new Integer(15))
		.setLeftMargin(margin)
		.setRightMargin(margin)
		.setTopMargin(margin)
		.setBottomMargin(margin)
		.setPrintBackgroundOnOddRows(true)
		.setOddRowBackgroundStyle(oddRowStyle)
		.setColumnsPerPage(new Integer(1))
		.setColumnSpace(new Integer(5));

		AbstractColumn columnState = ColumnBuilder.getNew().setColumnProperty("state", String.class.getName())
				.setTitle("State").setWidth(new Integer(85))
				.setStyle(detailStyle).setHeaderStyle(headerStyle).build();

		AbstractColumn columnBranch = ColumnBuilder.getNew().setColumnProperty("branch", String.class.getName())
				.setTitle("Branch").setWidth(new Integer(85))
				.setStyle(detailStyle).setHeaderStyle(headerStyle).build();

		AbstractColumn columnaProductLine = ColumnBuilder.getNew().setColumnProperty("productLine", String.class.getName())
				.setTitle("Product Line").setWidth(new Integer(85))
				.setStyle(detailStyle).setHeaderStyle(headerStyle).build();

		AbstractColumn columnaItem = ColumnBuilder.getNew().setColumnProperty("item", String.class.getName())
				.setTitle("Item").setWidth(new Integer(85))
				.setStyle(detailStyle).setHeaderStyle(headerStyle).build();

		AbstractColumn columnCode = ColumnBuilder.getNew().setColumnProperty("id", Long.class.getName())
				.setTitle("ID").setWidth(new Integer(40))
				.setStyle(amountStyle).setHeaderStyle(headerStyle).build();

		AbstractColumn columnaCantidad = ColumnBuilder.getNew().setColumnProperty("quantity", Long.class.getName())
				.setTitle("Quantity").setWidth(new Integer(80))
				.setStyle(amountStyle).setHeaderStyle(headerStyle).build();
		//Define Conditional Styles

		ArrayList conditionalStyles = createConditionalStyles(amountStyle);

		AbstractColumn columnAmount = ColumnBuilder.getNew().setColumnProperty("amount", Float.class.getName())
				.setTitle("Amount").setWidth(new Integer(90)).setPattern("$ 0.00")
				.addConditionalStyles(conditionalStyles)
				.setStyle(amountStyle)
				.setHeaderStyle(headerStyle).build();

		drb.addColumn(columnState);
		drb.addColumn(columnBranch);
		drb.addColumn(columnaProductLine);
		//drb.addColumn(columnaItem);
		drb.addColumn(columnCode);
		//drb.addColumn(columnaCantidad);
		drb.addColumn(columnAmount);

		//drb.setUseFullPageWidth(true);
		JasperDesign jasperDesign = new JasperDesign();
		JRDesignField field = new JRDesignField();
		field.setName("firstName");
		field.setValueClass(String.class);
		jasperDesign.addField(field);

		field = new JRDesignField();
		field.setName("lastName");  // set name for field.
		field.setValueClass(String.class);  // set class for field. Its always depends upon data type which we want to get in this field.
		jasperDesign.addField(field);   // Added field in design.

		field = new JRDesignField();
		field.setName("age");
		field.setValueClass(Integer.class);
		jasperDesign.addField(field);

		JRDesignBand band = new JRDesignBand();

		//Title Band
		band = new JRDesignBand();
		band.setHeight(30);


		DynamicReport dr = drb.build();
		return dr;
	}

	/**
	 * Create a Conditional Styles that redefines the text color of the values of the "amount" column
	 * @param baseStyle
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList createConditionalStyles(Style baseStyle) throws IllegalAccessException, InstantiationException,        InvocationTargetException, NoSuchMethodException {
		Style style0 = (Style) BeanUtils.cloneBean(baseStyle);
		style0.setTextColor(Color.RED);
		style0.setFont(Font.GEORGIA_MEDIUM_BOLD);
		Style style1 = (Style) BeanUtils.cloneBean(baseStyle);
		style1.setTextColor(new Color(128,128,0));
		Style style2 = (Style) BeanUtils.cloneBean(baseStyle);
		style2.setTextColor(new Color(0,128,0)); //dark green
		style2.setFont(Font.ARIAL_SMALL_BOLD);

		StatusLightCondition status0 = new StatusLightCondition(new Double(0), new Double(3000)); //TODO ENHANCEMENT make it come from a parameter??? $P{...}
		StatusLightCondition status1 = new StatusLightCondition(new Double(5000), new Double(6000));
		StatusLightCondition status2 = new StatusLightCondition(new Double(6000),new Double(100000));

		ConditionalStyle condition0 = new ConditionalStyle(status0,style0);
		ConditionalStyle condition1 = new ConditionalStyle(status1,style1);
		ConditionalStyle condition2 = new ConditionalStyle(status2,style2);

		ArrayList conditionalStyles = new ArrayList();
		conditionalStyles.add(condition0);
		conditionalStyles.add(condition1);
		conditionalStyles.add(condition2);
		return conditionalStyles;
	}


	protected void exportToJRXML(String path,String fileName) throws Exception {
		if (this.jr != null) {
			DynamicJasperHelper.generateJRXML(this.jr, "UTF-8", dir + "/" + fileName + ".jrxml");
		}
	}

	protected LayoutManager getLayoutManager() {
		return new ClassicLayoutManager();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void testReport() throws Exception {
		dr = buildReport();
		JRDataSource ds = new JREmptyDataSource();
		jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
		if (ds != null){
			jp = JasperFillManager.fillReport(jr, params, ds);
		}else{
			jp = JasperFillManager.fillReport(jr, params);
		}

		exportToJRXML(dir,fileName);
	}

	protected JasperPrint jp;
	protected JasperReport jr;
	protected DynamicReport dr;
	protected Map params = new HashMap<>();
	static String dir ="/home/navankur/Desktop";
	String fileName = "test";


	public JasperReport toJRXMLFromDesign(){

		try {
			JasperDesign jasperDesign = createDesign();
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperCompileManager.writeReportToXmlFile(jasperReport , dir + "/myFile.jrxml");
			return jasperReport;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}


	public JasperDesign createDesign() throws JRException {
		JasperDesign jasperDesign = new JasperDesign();
		/*Set basic design of page.*/
		jasperDesign.setName("sampleDynamicJasperDesign");
		jasperDesign.setPageWidth(595); // page width
		jasperDesign.setPageHeight(842); // page height
		jasperDesign.setColumnWidth(515);   // column width of page
		jasperDesign.setColumnSpacing(0);
		jasperDesign.setLeftMargin(40);
		jasperDesign.setRightMargin(40);
		jasperDesign.setTopMargin(20);
		jasperDesign.setBottomMargin(20);

		JRDesignExpression expression = new JRDesignExpression();

		//Set style of page.
		JRDesignStyle normalStyle = new JRDesignStyle();
		normalStyle.setName("Sans_Normal");
		normalStyle.setDefault(true);
		normalStyle.setFontName("DejaVu Sans");
		normalStyle.setFontSize(12);
		normalStyle.setPdfFontName("Helvetica");
		normalStyle.setPdfEncoding("Cp1252");
		normalStyle.setPdfEmbedded(false);
		jasperDesign.addStyle(normalStyle);


		/*
		 * Generate field dynamically
		 * */

		JRDesignField field = new JRDesignField();
		field.setName("firstName");
		field.setValueClass(String.class);
		jasperDesign.addField(field);

		field = new JRDesignField();
		field.setName("lastName");  // set name for field.
		field.setValueClass(String.class);  // set class for field. Its always depends upon data type which we want to get in this field.
		jasperDesign.addField(field);   // Added field in design.

		field = new JRDesignField();
		field.setName("age");
		field.setValueClass(Integer.class);
		jasperDesign.addField(field);

		JRDesignBand band = new JRDesignBand();

		//Title Band
		band = new JRDesignBand();
		band.setHeight(30);


		JRDesignStaticText staticText = new JRDesignStaticText();
		staticText.setText("Person's Specification");
		staticText.setX(0);
		staticText.setY(0);
		staticText.setHeight(20);
		staticText.setWidth(515);
		staticText.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
		band.addElement(staticText);
		jasperDesign.setTitle(band);


		//        Detail Band
		band = new JRDesignBand(); // New band
		band.setHeight(20); // Set band height

		/*Create text field dynamically*/
		JRDesignTextField textField = new JRDesignTextField();
		textField.setX(0);  // x position of text field.
		textField.setY(0);  // y position of text field.
		textField.setWidth(160);    // set width of text field.
		textField.setHeight(20);    // set height of text field.
		JRDesignExpression jrExpression = new JRDesignExpression(); // new instanse of expression. We need create new instance always when need to set expression.
		jrExpression.setText("\"" + "First Name: " + "\"" + "+" + "$F{firstName}"); //  Added String before field in expression.
		textField.setExpression(jrExpression);  // set expression value in textfield.
		band.addElement(textField); // Added element in textfield.

		textField = new JRDesignTextField();
		textField.setX(160);
		textField.setY(0);
		textField.setWidth(160);
		textField.setHeight(20);
		jrExpression = new JRDesignExpression();
		jrExpression.setText("$F{lastName}" + "+" + "\"" + " :Last Name" + "\""); // Added string after field value
		textField.setExpression(jrExpression);
		band.addElement(textField);

		textField = new JRDesignTextField();
		textField.setX(320);
		textField.setY(0);
		textField.setWidth(160);
		textField.setHeight(20);
		jrExpression = new JRDesignExpression();
		String age = "\"" + "<html><font color=" + "\\" + "\"" + "#66FF33" + "\\" + "\"" + ">" + "\"" + "+" + "\"" + "Age is: " + "\"" + "+" + "\"" + "</font><font color=" + "\\" + "\"" + "#6600FF" + "\\" + "\"" + ">" + "\"" + "+" + "$F{age}" + "+" + "\"" + "</font></html>" + "\"";  // added html in text field with different color.
		jrExpression.setText(age);
		textField.setExpression(jrExpression);
		textField.setMarkup("html"); // By Default markup is none, We need to set it as html if we set expression as html.
		band.addElement(textField);
		((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);
		return jasperDesign;
	}


	public static void main(String[] args) throws Exception {

		System.out.println("JAsper Design creating");
		JasperDesignForTemplate test = new JasperDesignForTemplate();
		//JasperReport jasperReport = test.toJRXMLFromDesign();

		JasperDesign jasperDesign = test.createDesign();
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "sample.pdf");

		/*	JasperDesignForTemplate test = new JasperDesignForTemplate();
		test.testReport();
		JasperViewer.viewReport(test.jp);*/
	}

}


