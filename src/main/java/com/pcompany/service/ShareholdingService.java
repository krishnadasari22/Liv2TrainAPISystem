package com.pcompany.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pcompany.dao.ShareholdingDao;
import com.pcompany.dto.InstitutionalShareholdingOwnershipDetailsDTO;
import com.pcompany.dto.ShareholdingOwnershipDetailsDTO;
import com.pcompany.model.OwnershipModel;
import com.televisory.capitalmarket.service.ExcelDesignService;
import com.televisory.capitalmarket.service.GenerateExcelStyle;
import com.televisory.capitalmarket.util.CMStatic;
@Service
public class ShareholdingService {
	Logger _log = Logger.getLogger(ShareholdingService.class);

	@Autowired
	ShareholdingDao shareholdingDao;

	@Autowired
	private ExcelDesignService excelDesignService;

	@Value("${CM.IMAGE.LOGO.PATH}")
	private String logoPath;


	public List<OwnershipModel> getOwnershipDetails(String securityId) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<OwnershipModel> details = new LinkedList<>();
		List<ShareholdingOwnershipDetailsDTO> insiderDetails = null;
		List<InstitutionalShareholdingOwnershipDetailsDTO> institutionDetails = null;

		try {
			_log.info(" OwnerShip details ::: " + securityId);
			insiderDetails = getInsiderDetails(securityId);
			institutionDetails = getInstitutionalOwnershipDetails(securityId);

			Map<String, List<ShareholdingOwnershipDetailsDTO>> periodMapperIndividual = new LinkedHashMap<>();
			if(insiderDetails!=null && !insiderDetails.isEmpty()){
				for (ShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : insiderDetails) {
					Date period = shareholdingOwnershipDetailsDTO.getReportDate();
					Date asOnDate = shareholdingOwnershipDetailsDTO.getAsOnDate();
					List<ShareholdingOwnershipDetailsDTO> sList = periodMapperIndividual.get(sdf.format(asOnDate));
					if(sList==null){
						sList=new ArrayList<>();
					}
					sList.add(shareholdingOwnershipDetailsDTO);
					periodMapperIndividual.put(sdf.format(asOnDate), sList);
				}
			}

			Map<String,Double> insiderTotalMap = new LinkedHashMap<>();

			int i = 1;
			for (String key : periodMapperIndividual.keySet()) {
				Double insiderTotal = null;
				List<ShareholdingOwnershipDetailsDTO> sList = periodMapperIndividual.get(key);
				if(sList!=null && !sList.isEmpty()){
					double tot = 0d;
					for (ShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : sList) {
						tot = tot + shareholdingOwnershipDetailsDTO.getPercent();
					}
					insiderTotal = tot;
				}
				OwnershipModel ownershipModel = new OwnershipModel();
				ownershipModel.setId(i);
				ownershipModel.setCompanyId("Promoters/Insiders");
				ownershipModel.setPeriod(sdf.parse(key));
				ownershipModel.setAsOnDate(sdf.parse(key));
				ownershipModel.setTotal(insiderTotal);
				details.add(ownershipModel);
				insiderTotalMap.put(key, insiderTotal);
				i=i+1;
			}

			Map<String, List<InstitutionalShareholdingOwnershipDetailsDTO>> periodMapperInstitutional = new LinkedHashMap<>();
			if(institutionDetails!=null && !institutionDetails.isEmpty()){
				for (InstitutionalShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : institutionDetails) {
					Date period = shareholdingOwnershipDetailsDTO.getReportDate();
					Date asOnDate = shareholdingOwnershipDetailsDTO.getAsOnDate();
					List<InstitutionalShareholdingOwnershipDetailsDTO> sList = periodMapperInstitutional.get(sdf.format(asOnDate));
					if(sList==null){
						sList=new ArrayList<>();
					}
					sList.add(shareholdingOwnershipDetailsDTO);
					periodMapperInstitutional.put(sdf.format(asOnDate), sList);
				}
			}

			Map<String,Double> instituitionalTotalMap = new LinkedHashMap<>();
			for (String key : periodMapperInstitutional.keySet()) {
				Double intituteTotal = null;
				List<InstitutionalShareholdingOwnershipDetailsDTO> sList = periodMapperInstitutional.get(key);
				if(sList!=null && !sList.isEmpty()){
					double tot = 0d;
					for (InstitutionalShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : sList) {
						tot = tot + shareholdingOwnershipDetailsDTO.getPercent();
					}
					intituteTotal = tot;
				}
				OwnershipModel ownershipModel = new OwnershipModel();
				ownershipModel.setId(i);
				ownershipModel.setCompanyId("Institutions");
				ownershipModel.setPeriod(sdf.parse(key));
				ownershipModel.setAsOnDate(sdf.parse(key));
				ownershipModel.setTotal(intituteTotal);
				details.add(ownershipModel);
				instituitionalTotalMap.put(key, intituteTotal);
				i=i+1;
			}

			Set<String> pSet = new HashSet<>(instituitionalTotalMap.keySet());
			pSet.addAll(insiderTotalMap.keySet());

			for (String key : pSet) {
				Double totalInsider = insiderTotalMap.get(key);
				Double totalInstitutional = instituitionalTotalMap.get(key);

				Double totalPercentShareOther = null;
/*				if(totalInsider==null){
					totalPercentShareOther = null;
				}else if(totalInstitutional == null){
					totalPercentShareOther = null;
				}else{
					if(totalInsider+totalInstitutional >= 100){
						totalPercentShareOther = 0d;
					}else{
						totalPercentShareOther = 100-(totalInsider+totalInstitutional);
					}
				}*/
				
				if(totalInsider==null){
					totalPercentShareOther = null;
					totalInsider = 0d;
					OwnershipModel ownershipModel2 = new OwnershipModel();
					ownershipModel2.setId(i);
					ownershipModel2.setCompanyId("Promoters/Insiders");
					ownershipModel2.setPeriod(sdf.parse(key));
					ownershipModel2.setAsOnDate(sdf.parse(key));
					ownershipModel2.setTotal(null);
					details.add(ownershipModel2);
				}

				if(totalInstitutional == null){
					totalPercentShareOther = null;
					totalInstitutional = 0d;
					OwnershipModel ownershipModel2 = new OwnershipModel();
					ownershipModel2.setId(i);
					ownershipModel2.setCompanyId("Institutions");
					ownershipModel2.setPeriod(sdf.parse(key));
					ownershipModel2.setAsOnDate(sdf.parse(key));
					ownershipModel2.setTotal(null);
					details.add(ownershipModel2);
				}

				if(totalInsider + totalInstitutional >= 100){
					totalPercentShareOther = 0d;
				}else{
					totalPercentShareOther = 100-(totalInsider + totalInstitutional);
				}
				

				//	totalPercentShareOther = 100-(totalInsider+totalInstitutional);
				OwnershipModel ownershipModel2 = new OwnershipModel();
				ownershipModel2.setId(i);
				ownershipModel2.setCompanyId("Public & Others");
				ownershipModel2.setPeriod(sdf.parse(key));
				ownershipModel2.setAsOnDate(sdf.parse(key));
				ownershipModel2.setTotal(totalPercentShareOther);
				details.add(ownershipModel2);

				i=i+1;

			}



			details.sort(Comparator.comparing(OwnershipModel::getPeriod, (p1,p2)-> {
				return p2.compareTo(p1);
			}));
			
			for (String key : pSet) {
				Double totalInsider = insiderTotalMap.get(key);
				Double totalInstitutional = instituitionalTotalMap.get(key);

				Double totalPercentShareOther = null;


				if(totalInsider==null){
					totalPercentShareOther = null;
					totalInsider = 0d;
				}

				if(totalInstitutional == null){
					totalPercentShareOther = null;
					totalInstitutional = 0d;
				}


				/*if(totalInsider==null){
					totalPercentShareOther = null;
				}else if(totalInstitutional == null){
					totalPercentShareOther = null;
				}else{
					if(totalInsider+totalInstitutional >= 100){
						totalPercentShareOther = 0d;
					}else{
						totalPercentShareOther = 100-(totalInsider+totalInstitutional);
					}
				}
				 */
				Double total = null;
				if(totalInsider + totalInstitutional >= 100){
					totalPercentShareOther = 0d;
				}else{
					totalPercentShareOther = 100-(totalInsider + totalInstitutional);
				}

				if(totalPercentShareOther!=null){
					total = totalInsider + totalInstitutional + totalPercentShareOther;
				}
				OwnershipModel ownershipModel = new OwnershipModel();
				ownershipModel.setId(i);
				ownershipModel.setCompanyId("Total");
				ownershipModel.setPeriod(sdf.parse(key));
				ownershipModel.setAsOnDate(sdf.parse(key));
				ownershipModel.setTotal(total);
				details.add(ownershipModel);
				i=i+1;
			}

			return details;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}

		return null;
	}


	public List<OwnershipModel> getOwnershipDetailsRecent(String securityId) {

		List<OwnershipModel> details = new ArrayList<>();
		List<ShareholdingOwnershipDetailsDTO> insiderDetails = null;
		List<InstitutionalShareholdingOwnershipDetailsDTO> institutionDetails = null;
		Date period = null;
		Date asOnDate = null;

		try {
			_log.info(" securityId " + securityId);
			insiderDetails = getInsiderDetailsRecent(securityId);
			institutionDetails = getInstitutionalOwnershipRecent(securityId);

			Double totalPercentShareInsider = null;
			if(insiderDetails!=null && !insiderDetails.isEmpty()){
				double tot = 0d;
				for (ShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : insiderDetails) {
					if(period==null){
						period = shareholdingOwnershipDetailsDTO.getReportDate();
					}
					if(asOnDate==null){
						asOnDate = shareholdingOwnershipDetailsDTO.getAsOnDate();
					}
					Double percentShare = shareholdingOwnershipDetailsDTO.getPercent();
					if(percentShare!=null){
						tot = tot + percentShare;
					}
				}
				totalPercentShareInsider = tot;
			}

			Double totalPercentShareInstitutional = null;
			if(institutionDetails!=null && !institutionDetails.isEmpty()){
				double tot = 0d;
				for (InstitutionalShareholdingOwnershipDetailsDTO shareholdingOwnershipDetailsDTO : institutionDetails) {
					if(period==null){
						period = shareholdingOwnershipDetailsDTO.getReportDate();
					}
					if(asOnDate==null){
						asOnDate = shareholdingOwnershipDetailsDTO.getAsOnDate();
					}
					Double percentShare = shareholdingOwnershipDetailsDTO.getPercent();
					if(percentShare!=null){
						tot = tot + percentShare;
					}
				}
				totalPercentShareInstitutional = tot;
			}

			//boolean check = true;
			Double totalPercentShareOther = null;
			if(totalPercentShareInsider==null){
				totalPercentShareOther = null;
				totalPercentShareInsider = 0d;
				//check = false;
				
				/*OwnershipModel ownershipModel = new OwnershipModel();
				ownershipModel.setId(1);
				ownershipModel.setCompanyId("Promoters/Insiders");
				ownershipModel.setPeriod(period);
				ownershipModel.setTotal(null);
				details.add(ownershipModel);*/
			}else{
				OwnershipModel ownershipModel = new OwnershipModel();
				ownershipModel.setId(1);
				ownershipModel.setCompanyId("Promoters/Insiders");
				ownershipModel.setPeriod(period);
				ownershipModel.setAsOnDate(asOnDate);
				ownershipModel.setTotal(totalPercentShareInsider);
				details.add(ownershipModel);
			}

			if(totalPercentShareInstitutional==null){
				totalPercentShareOther = null;
				totalPercentShareInstitutional = 0d;
				//check = false;
				

				/*OwnershipModel ownershipModel1 = new OwnershipModel();
				ownershipModel1.setId(2);
				ownershipModel1.setCompanyId("Institutions");
				ownershipModel1.setPeriod(period);
				ownershipModel1.setTotal(null);
				details.add(ownershipModel1);*/
			}else{

				OwnershipModel ownershipModel1 = new OwnershipModel();
				ownershipModel1.setId(2);
				ownershipModel1.setCompanyId("Institutions");
				ownershipModel1.setPeriod(period);
				ownershipModel1.setAsOnDate(asOnDate);
				ownershipModel1.setTotal(totalPercentShareInstitutional);
				details.add(ownershipModel1);
			}

			/*if(check){*/
			if(totalPercentShareInsider+totalPercentShareInstitutional >= 100){
				totalPercentShareOther = 0d;
			}else{
				totalPercentShareOther = 100-(totalPercentShareInsider+totalPercentShareInstitutional);
			}
			/*}else{
				totalPercentShareOther = 0d;
			}*/


			
			OwnershipModel ownershipModel2 = new OwnershipModel();
			ownershipModel2.setId(3);
			ownershipModel2.setCompanyId("Public & Others");
			ownershipModel2.setPeriod(period);
			ownershipModel2.setAsOnDate(asOnDate);
			ownershipModel2.setTotal(totalPercentShareOther);
			details.add(ownershipModel2);
			return details;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}

		return null;
	}



	public List<ShareholdingOwnershipDetailsDTO> getInsiderDetailsRecent(String securityId) {
		return shareholdingDao.getInsiderDetailsRecent(securityId);
	}

	public List<ShareholdingOwnershipDetailsDTO> getInsiderDetails(String securityId) {
		List<ShareholdingOwnershipDetailsDTO> list =  shareholdingDao.getInsiderDetails(securityId);

		/*list.sort(Comparator.comparing(ShareholdingOwnershipDetailsDTO::getReportDate, (p1,p2)-> {
			return p2.compareTo(p1);
		}));*/

		Predicate<ShareholdingOwnershipDetailsDTO> byPos = filterPred -> filterPred.getPercent() > 0d;
		List<ShareholdingOwnershipDetailsDTO> listResult = list.stream().filter(byPos)
				.collect(Collectors.toList());

		return listResult;
	}

	public List<InstitutionalShareholdingOwnershipDetailsDTO> getInstitutionalOwnershipDetails(String securityId) {

		List<InstitutionalShareholdingOwnershipDetailsDTO> list = shareholdingDao.getInstitutionalOwnershipDetails(securityId);
		/*list.sort(Comparator.comparing(InstitutionalShareholdingOwnershipDetailsDTO::getReportDate, (p1,p2)-> {
			return p2.compareTo(p1);
		}));*/

		Predicate<InstitutionalShareholdingOwnershipDetailsDTO> byPos = filterPred -> filterPred.getPercent() > 0d;
		List<InstitutionalShareholdingOwnershipDetailsDTO> listResult = list.stream().filter(byPos)
				.collect(Collectors.toList());

		return listResult;

	}

	public List<InstitutionalShareholdingOwnershipDetailsDTO> getInstitutionalOwnershipRecent(String securityId) {
		return shareholdingDao.getInstitutionalOwnershipRecent(securityId);
	}



	public HSSFWorkbook createExcelReport(List<OwnershipModel> ownerDetails, List<ShareholdingOwnershipDetailsDTO> promoterDetails, List<InstitutionalShareholdingOwnershipDetailsDTO> institutionalDetails, String entityId,String companyName) throws Exception{
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			GenerateExcelStyle ges =new GenerateExcelStyle(workbook);
			HSSFSheet sheet = workbook.createSheet("Shareholders Info");
			//ges.setBackgroundOnSheet(sheet, 0, 4000,CMStatic.SHEET_MAX_COLUMN);
			sheet.setDisplayGridlines(false);
			createAndFillSheet(workbook,ownerDetails,promoterDetails,institutionalDetails,"Shareholders Info",ges,companyName);
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();	
			throw e;
		}
	}

	private void createAndFillSheet(HSSFWorkbook workbook,List<OwnershipModel> ownerDetails, List<ShareholdingOwnershipDetailsDTO> promoterDetails, List<InstitutionalShareholdingOwnershipDetailsDTO> institutionalDetails, String sheetName,GenerateExcelStyle ges, String companyName) {
		_log.info("creating the Share Holding sheet data ");
		try {
			int rowdesign = 0;

			Row r = workbook.getSheet(sheetName).getRow(rowdesign);
			if(r==null) {
				r=workbook.getSheet(sheetName).createRow(rowdesign);
			}
			Cell celldesign = r.createCell(1);
			if(celldesign==null) {
				celldesign=r.createCell(1);
			}
			workbook.getSheet(sheetName).setColumnWidth(1, 10000);
			// create the televisory logo
			createLogo(celldesign, workbook.getSheet(sheetName), workbook, ges);
			HSSFSheet sheet = workbook.getSheet(sheetName);

			int headerDebt = 7;

			Row companyDesc = sheet.getRow(2);
			if(null==companyDesc) {
				companyDesc=sheet.createRow(2);
			}
			Cell compCell = companyDesc.getCell(1);
			if(compCell==null) {
				compCell=companyDesc.createCell(1);
			}
			compCell.setCellValue("Company");
			Cell companyCellData = companyDesc.getCell(2);
			if(companyCellData==null) {
				companyCellData=companyDesc.createCell(2);
			}
			companyCellData.setCellValue(companyName);
			companyCellData.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);
			compCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);

			List<String> periodList = new ArrayList<String>();
			List<String> headerList = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			headerList.add("Ownership Details");
			for (int i = ownerDetails.size()-1; i >=0 ; i--) {
				if(!periodList.contains(sdf.format(ownerDetails.get(i).getAsOnDate())) && periodList.size()<10){
					periodList.add(sdf.format(ownerDetails.get(i).getAsOnDate()));
				}
			}

			Collections.sort(periodList);
			Collections.reverse(periodList);
			for (int i = 0; i < periodList.size(); i++) {
				headerList.add(periodList.get(i));
			}
			try {
				createHeader(workbook.getSheet(sheetName), headerDebt, ges,  headerList);
			}catch(Exception e){
				e.printStackTrace();
			}

			Boolean isLastRecord = false;
			Map<String, Map<String,Double>> dataMap = new LinkedHashMap<String,Map<String,Double>>();
			Set<String> periods = new TreeSet<>();

			for (int i = 0; i <=ownerDetails.size()-1;  i++) {
				periods.add(sdf.format(ownerDetails.get(i).getAsOnDate()));
				if(!dataMap.containsKey(ownerDetails.get(i).getCompanyId())){
					dataMap.put(ownerDetails.get(i).getCompanyId(), new HashMap<String,Double>(1));
				}
				dataMap.get(ownerDetails.get(i).getCompanyId()).put(sdf.format(ownerDetails.get(i).getAsOnDate()),ownerDetails.get(i).getTotal());
			}

			for(int i=0;i<dataMap.size();i++){
				if(i==dataMap.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				if(rowDebt==null) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(cellDebt==null) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//Setting Operational Value Start
				setColumnValue(cellDebt,dataMap.keySet().toArray()[i].toString(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");

				for (int j = 0; j < periodList.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
					setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
				}
				headerDebt++;
			}

			headerDebt += 6;


			if(!promoterDetails.isEmpty()) {
				periodList.clear();	
			}
			//periodList.clear();
			headerList.clear();
            headerList.add("Promoters/Insiders Ownership Details");
           if(promoterDetails.isEmpty()) {
            Collections.sort(periodList);
   			Collections.reverse(periodList);
   			for (int i = 0; i < periodList.size(); i++) {
   				headerList.add(periodList.get(i));
   			}
   			try {
   				createHeader(workbook.getSheet(sheetName), headerDebt, ges,  headerList);
   			}catch(Exception e){
   				e.printStackTrace();
   			}

   			isLastRecord = true;

   			dataMap = new LinkedHashMap<String,Map<String,Double>>();
   			periods = new TreeSet<>();

   			
   			
   			//sort datamap
   			
   			

   			/*for(int i=0;i<dataMap.size();i++){
   				if(i==dataMap.size()-1){
   					isLastRecord = true;
   				}*/
   				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
   				//Setting Operational Value Start
   				setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");

   				for (int j = 0; j < periodList.size(); j++) {
   					cellIdxDebt = cellIdxDebt+1;
   					cellDebt = rowDebt.getCell(cellIdxDebt);
   					if(null==cellDebt) {
   						cellDebt=rowDebt.createCell(cellIdxDebt);
   					}
   					Double cellVal =null;//dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
   					setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
   				}
   				headerDebt++;
   			//}

   			headerDebt += 6;
              
           }
           else {
			for (int i = promoterDetails.size()-1; i >=0 ; i--) {
				if(!periodList.contains(sdf.format(promoterDetails.get(i).getAsOnDate())) && periodList.size()<10){
					periodList.add(sdf.format(promoterDetails.get(i).getAsOnDate()));
				}
			}

			Collections.sort(periodList);
			Collections.reverse(periodList);
			for (int i = 0; i < periodList.size(); i++) {
				headerList.add(periodList.get(i));
			}
			try {
				createHeader(workbook.getSheet(sheetName), headerDebt, ges,  headerList);
			}catch(Exception e){
				e.printStackTrace();
			}

			isLastRecord = false;

			dataMap = new LinkedHashMap<String,Map<String,Double>>();
			periods = new TreeSet<>();

			for (int i = 0; i<=promoterDetails.size()-1 ; i++) {
				periods.add(sdf.format(promoterDetails.get(i).getAsOnDate()));
				if(!dataMap.containsKey(promoterDetails.get(i).getCompanyName())){
					dataMap.put(promoterDetails.get(i).getCompanyName(), new HashMap<String,Double>(1));
				}
				dataMap.get(promoterDetails.get(i).getCompanyName()).put(sdf.format(promoterDetails.get(i).getAsOnDate()),promoterDetails.get(i).getPercent());
			}
			
			//sort datamap
			
			

			for(int i=0;i<dataMap.size();i++){
				if(i==dataMap.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				if(null==rowDebt) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				
				//Setting Operational Value Start
				setColumnValue(cellDebt,dataMap.keySet().toArray()[i].toString(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");

				for (int j = 0; j < periodList.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal =dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
					setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
				}
				headerDebt++;
			}

			headerDebt += 6;
           }

			/*Institutional  details*/
			if(!institutionalDetails.isEmpty()) {
				periodList.clear();	
			}
			headerList.clear();
            headerList.add("Institutional- Ownership Details");
            if(institutionalDetails.isEmpty()) {

    			Collections.sort(periodList);
    			Collections.reverse(periodList);
    			for (int i = 0; i < periodList.size(); i++) {
    				headerList.add(periodList.get(i));
    			}
    			try {
    				createHeader(workbook.getSheet(sheetName), headerDebt, ges,  headerList);
    			}catch(Exception e){
    				e.printStackTrace();
    			}

    			isLastRecord = true;

    			dataMap = new LinkedHashMap<String,Map<String,Double>>();
    			periods = new TreeSet<>();

    			/*for (int i =0 ;i <= institutionalDetails.size()-1 ; i++) {
    				periods.add(sdf.format(institutionalDetails.get(i).getAsOnDate()));
    				if(!dataMap.containsKey(institutionalDetails.get(i).getCompanyName())){
    					dataMap.put(institutionalDetails.get(i).getCompanyName(), new HashMap<String,Double>(1));
    				}
    				dataMap.get(institutionalDetails.get(i).getCompanyName()).put(sdf.format(institutionalDetails.get(i).getAsOnDate()),institutionalDetails.get(i).getPercent());
    			}*/

    			/*for(int i=0;i<dataMap.size();i++){*/
    				/*if(i==dataMap.size()-1){
    					isLastRecord = true;
    				}*/
    			
    			/*for(int i=0;i<dataMap.size();i++){
    				if(i==dataMap.size()-1){
    					isLastRecord = true;
    					}*/
    				
    				Row rowDebt = sheet.getRow(headerDebt);
    				if(rowDebt==null) {
    					rowDebt=sheet.createRow(headerDebt);
    				}
    				int cellIdxDebt = 1;
    				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
    				
    				if(null==cellDebt) {
    					cellDebt=rowDebt.createCell(cellIdxDebt);
    				}
    				//Setting Operational Value Start
    				setColumnValue(cellDebt,"-",sheet,cellIdxDebt,ges,isLastRecord,"LEFT");

    				for (int j = 0; j < periodList.size(); j++) {
    					cellIdxDebt = cellIdxDebt+1;
    					cellDebt = rowDebt.getCell(cellIdxDebt);
    					if(null==cellDebt) {
    						cellDebt=rowDebt.createCell(cellIdxDebt);
    					}
    					Double cellVal = null;//dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
    					setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
    				}
    				headerDebt++;
    			//}

                
            	
    			/*}*/ }else {
			for (int i = institutionalDetails.size()-1; i >=0 ; i--) {
				if(!periodList.contains(sdf.format(institutionalDetails.get(i).getAsOnDate())) && periodList.size()<10){
					periodList.add(sdf.format(institutionalDetails.get(i).getAsOnDate()));
				}
			}

			Collections.sort(periodList);
			Collections.reverse(periodList);
			for (int i = 0; i < periodList.size(); i++) {
				headerList.add(periodList.get(i));
			}
			try {
				createHeader(workbook.getSheet(sheetName), headerDebt, ges,  headerList);
			}catch(Exception e){
				e.printStackTrace();
			}

			isLastRecord = false;

			dataMap = new LinkedHashMap<String,Map<String,Double>>();
			periods = new TreeSet<>();

			for (int i =0 ;i <= institutionalDetails.size()-1 ; i++) {
				periods.add(sdf.format(institutionalDetails.get(i).getAsOnDate()));
				if(!dataMap.containsKey(institutionalDetails.get(i).getCompanyName())){
					dataMap.put(institutionalDetails.get(i).getCompanyName(), new HashMap<String,Double>(1));
				}
				dataMap.get(institutionalDetails.get(i).getCompanyName()).put(sdf.format(institutionalDetails.get(i).getAsOnDate()),institutionalDetails.get(i).getPercent());
			}

			for(int i=0;i<dataMap.size();i++){
				if(i==dataMap.size()-1){
					isLastRecord = true;
				}
				Row rowDebt = sheet.getRow(headerDebt);
				if(rowDebt==null) {
					rowDebt=sheet.createRow(headerDebt);
				}
				int cellIdxDebt = 1;
				Cell cellDebt = rowDebt.getCell(cellIdxDebt);
				if(null==cellDebt) {
					cellDebt=rowDebt.createCell(cellIdxDebt);
				}
				//Setting Operational Value Start
				setColumnValue(cellDebt,dataMap.keySet().toArray()[i].toString(),sheet,cellIdxDebt,ges,isLastRecord,"LEFT");

				for (int j = 0; j < periodList.size(); j++) {
					cellIdxDebt = cellIdxDebt+1;
					cellDebt = rowDebt.getCell(cellIdxDebt);
					if(null==cellDebt) {
						cellDebt=rowDebt.createCell(cellIdxDebt);
					}
					Double cellVal = dataMap.get(dataMap.keySet().toArray()[i].toString()).get(periodList.get(j));
					setColumnValue(cellDebt,cellVal,sheet,cellIdxDebt,ges,isLastRecord);
				}
				headerDebt++;
			}

            }

		}catch (Exception e) {
			e.printStackTrace();
			_log.error("Some error occured in creating the sheet" + e.getLocalizedMessage());
		}

	}

	private void setColumnValue(Cell cell, String data, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ges, Boolean isLastRecord,String Align) {
		if (data != null) {

			cell.setCellValue(data);
			//cell.setCellStyle(getNumberFormatStyle(data, "", ges, false));
			if(Align.equalsIgnoreCase("LEFT")){
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_LEFT);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_LEFT);
				}
			}else{
				if(isLastRecord){
					cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
				}else{
					cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			if(isLastRecord){
				cell.setCellStyle(ges.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ges.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}

		}

	}

	private void setColumnValue(Cell cell, Double value, HSSFSheet sheet, int columnNumber, GenerateExcelStyle ge,Boolean isLastRecord) {
		if (value != null) {
			cell.setCellValue(value);
			if (isLastRecord) {
				if (Math.abs(value) >= 1.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else if (Math.abs(value) < 10.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL);
				} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else {
					cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
				}

			} else {
				if (Math.abs(value) >= 1.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else if (Math.abs(value) < 10.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_THREE_DECIMAL);
				} else if (Math.abs(value) >= 10.0 && Math.abs(value) <= 999.0) {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ONE_DECIMAL);
				} else {
					cell.setCellStyle(ge.BORDER_LEFT_RIGHT_NUMBER_FORMAT_ZERO_DECIMAL);
				}
			}
		} else {
			cell.setCellValue(CMStatic.NA);
			//cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			if(isLastRecord){
				cell.setCellStyle(ge.BORDER_BOTTOM_LEFT_RIGHT_ALIGN_RIGHT);
			}else{
				cell.setCellStyle(ge.BORDER_LEFT_RIGHT_ALIGN_RIGHT);
			}
		}

	}

	private void createHeader(HSSFSheet sheet, int startingHeaderRow,GenerateExcelStyle ges,List<String> headerSet) throws IOException{
		/*
		 * filling the first cell 
		 */
		/*Row sectionDesc = sheet.getRow(startingHeaderRow-3);
		Cell sectionCell = sectionDesc.getCell(1);
		sectionCell.setCellValue(header);
		sectionCell.setCellStyle(ges.BLUE_FONT_WITHOUT_BACKGROUND);*/


		int cellIndex = 1;
		System.out.println(" startingHeaderRow "+startingHeaderRow);

		int row = startingHeaderRow - 2;

		Row r = sheet.getRow(startingHeaderRow);
		
		if(r==null) {
			r=sheet.createRow(startingHeaderRow);
		}

		Row headerRow = sheet.getRow(row);
		if(null==headerRow) {
			headerRow=sheet.createRow(row);
		}

		Iterator itr = headerSet.iterator();

		while (itr.hasNext()) {
			Cell cellHeader = headerRow.getCell(cellIndex);
			if(cellHeader==null) {
				cellHeader=headerRow.createCell(cellIndex);
			}
			sheet.setColumnWidth(cellIndex, 10000);
			String headCol = (String) itr.next();
			cellHeader.setCellValue(headCol);
			ges.mergeCells(sheet, row, row + 1, cellIndex, cellIndex, true);
			cellHeader.setCellStyle(ges.HEADER_WHITE_TEXT_DARK_BLUE_BACKGROUND);
			cellIndex = cellIndex+1;
		}
	}

	private void createLogo(Cell cell, HSSFSheet sheet, HSSFWorkbook workbook, GenerateExcelStyle ges) {
		short height = 800;
		cell.getRow().setHeight(height);
		cell.setCellStyle(ges.BLACK_TEXT_GRAY_BACKGROUND_WITHOUT_BORDER);
		// SET LOGO IMAGE
		try {
			excelDesignService.readWorkBook(workbook);
			excelDesignService.setImage(sheet, cell.getRowIndex(), cell.getColumnIndex(), logoPath, 600, 600);
		} catch (IOException e) {

		}
	}






}