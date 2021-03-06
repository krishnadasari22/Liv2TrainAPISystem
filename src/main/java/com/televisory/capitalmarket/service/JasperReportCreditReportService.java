package com.televisory.capitalmarket.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.televisory.capitalmarket.dto.CompanyDTO;
import com.televisory.capitalmarket.dto.IndustryFinancialDataDTO;
import com.televisory.capitalmarket.model.CreditReportDualInformation;
import com.televisory.capitalmarket.model.CreditReportFinancialSnapshot;
import com.televisory.capitalmarket.model.CreditReportFourCol;
import com.televisory.capitalmarket.model.CreditReportLoanDetails;
import com.televisory.capitalmarket.model.JasperDynamicModel;
import com.televisory.capitalmarket.model.ReportAnnextureModel;
import com.televisory.capitalmarket.model.ReportPerformanceData;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;
import com.televisory.user.UserModelDTO;
import com.televisory.user.dao.TelevisoryUserRepository;
import com.televisory.user.dto.AssetDetailDto;
import com.televisory.user.dto.BankUITableDto;
import com.televisory.user.dto.CreditReportInfo;
import com.televisory.user.dto.DataRequestDocDto;
import com.televisory.user.dto.FacilityDetailDto;
import com.televisory.user.dto.OwnershipStructureDto;
import com.televisory.user.dto.PromoterGroupOwnershipStructureDto;
import com.televisory.user.dto.UserDataScoringCommentDto;
import com.televisory.user.dto.UserPeerInfoDTO;
import com.televisory.user.dto.keyManagementInfoDto;
import com.televisory.utils.LoanStatic;

@Service
public class JasperReportCreditReportService {

	Logger _log = Logger.getLogger(JasperReportCreditReportService.class);

	@Autowired
	DateUtil dateUtil;

	@Autowired
	UserService userService;

	@Autowired
	SectorService industryService;

	@Autowired
	CapitalMarketService capitalMarketService;

	@Autowired
	TelevisoryUserRepository televisoryUserRepository;

	@Autowired
	JasperReportService jasperReportService;
	
	@Autowired ExecutorService executorService;

	@Value("${CM.RESOURCE.IMAGE.PATH}")
	private String imagePath;

	@SuppressWarnings("rawtypes")
	public HashMap<String, Object> generateCreditReport(String mainJasperFile,String pdfFileName,CreditReportInfo companyInfo) throws Exception{
		_log.info("Generating the credit report");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfChart = new SimpleDateFormat("MMM-yy");
		SimpleDateFormat sdft= new SimpleDateFormat("yy");
		HashMap<String, Object> parametersList = new HashMap<String, Object>();
		try{
			////////////////////
			////////////////////
			//boolean isTempForPeerCompaniesTobedeleted = true;


			Boolean isTempCode = true;
			String userId = companyInfo.getUserId();
			//List<String> periodList = dateUtil.generatePrevApplicablePeriodYear(companyInfo.getApplicableToYear(), 5);


			String toDate = companyInfo.getApplicableToYear();
			String fromDate = companyInfo.getApplicableFromYear();
			Date to = sdf.parse(toDate);
			Date from = sdf.parse(fromDate);
			long diffInMilliSec = to.getTime() - from.getTime();
			long years =  (diffInMilliSec / (1000l * 60 * 60 * 24 * 365));
			int diff = (int) years + 1;

			List<String> applicablePeriodList = dateUtil.generatePrevApplicablePeriodYear(companyInfo.getApplicableToYear(), diff);
			_log.debug(applicablePeriodList);

			///////////////////////////
			String companyName = companyInfo.getBasicInfo().getCompanyName();
			String tempCurr = companyInfo.getUserCurrency();
			if(!StringUtils.hasText(tempCurr)){
				tempCurr = "USD";
			}
			
			if(StringUtils.hasText(companyName)){
				companyName = companyName.replaceAll("&", "&amp;");
			}
			
			final String currency = tempCurr;
			parametersList.put("companyName", companyName);

			/////////////////////////////
			String companyDescription = "";
			if(companyInfo.getBasicInfo()!=null){
				companyDescription = URLDecoder.decode(companyInfo.getBasicInfo().getDescription(),"UTF-8");
			}
			parametersList.put("companyDescription",companyDescription!=null?companyDescription.replaceAll("&", "&amp;"):"");
			/////////////////////////////////
			parametersList.put("imageLogoPath", imagePath +"/"+ CMStatic.IMAGE_LOGO);
			//////////////////////////////////

			List<CreditReportLoanDetails> creditReportLoanDetailsList = new ArrayList<>();
			List<FacilityDetailDto> facilityList = null;
			List<AssetDetailDto> assestDetailList = null;
			if(companyInfo.getCollateralDetailInfo()!=null){
				facilityList = companyInfo.getCollateralDetailInfo().getFacilityDetailDtos();
				assestDetailList = companyInfo.getCollateralDetailInfo().getAssetDetailDtos();
			}

			Double totFundBased = 0d;
			Double totNonFundBased = 0d;

			String unit = companyInfo.getUserUnit();
			int unitDivider = 1;
			if(!StringUtils.hasText(unit)){
				unit = "million";
			}else if("million".equalsIgnoreCase(unit.toLowerCase())){
				unitDivider = 1000000;
			}else if("billion".equalsIgnoreCase(unit.toLowerCase())){
				unitDivider = 1000000000;
			}else if("thousand".equalsIgnoreCase(unit.toLowerCase())){
				unitDivider = 1000;
			}

			if(facilityList!=null){
				for(FacilityDetailDto facility : facilityList) {
					totFundBased = totFundBased + facility.getLoanAMount();
					creditReportLoanDetailsList.add( new CreditReportLoanDetails(facility.getFacilityName()!=null?facility.getFacilityName().replaceAll("&", "&amp;"):"", jasperReportService.applyFormattor(BigDecimal.valueOf(facility.getLoanAMount()/unitDivider), null, null, null), String.valueOf(facility.getFundLimit())));
				}

				Double totProposed = totFundBased + totNonFundBased;
				creditReportLoanDetailsList.add( new CreditReportLoanDetails(jasperReportService.createBold("Total Fund Based"), jasperReportService.createBold(jasperReportService.applyFormattor(BigDecimal.valueOf(totFundBased/unitDivider),null,null,null)), ""));
				creditReportLoanDetailsList.add( new CreditReportLoanDetails(jasperReportService.createBold("Total Non-Fund Based"), jasperReportService.createBold(jasperReportService.applyFormattor(BigDecimal.valueOf(totNonFundBased/unitDivider),null,null,null)), ""));
				creditReportLoanDetailsList.add( new CreditReportLoanDetails(jasperReportService.createBold("Total Proposed Facilities"), jasperReportService.createBold(jasperReportService.applyFormattor(BigDecimal.valueOf(totProposed/unitDivider),null,null,null)), ""));
			}
			parametersList.put("creditReportLoanDetailsList", creditReportLoanDetailsList);

			////////////////////LOAN HISTORY , To be aaded by Rahul /////////////
			List<BankUITableDto> bankUiList = companyInfo.getBankUITableDtos();
			List<JasperDynamicModel> loanHistoryList = new LinkedList<>();
			/*

			BankUITableDto b = new BankUITableDto();
			b.setApplicationDate("2012-03-31");
			b.setLoanAmount(100000);
			b.setBankName("XYZ");
			b.setStatus("In progresss");
			b.setComments("XYZ");
			b.setLsrn("123");

			bankUiList.add(b);
			b = new BankUITableDto();
			b.setApplicationDate("2012-03-31");
			b.setLoanAmount(104000);
			b.setBankName("Axis");
			b.setStatus("Completed");
			b.setComments("Whatever");
			b.setLsrn("123");
			bankUiList.add(b);

			b = new BankUITableDto();
			b.setApplicationDate("2012-03-31");
			b.setLoanAmount(108000);
			b.setBankName("Kotak");
			b.setStatus("In progress");
			b.setComments("Whatever");
			b.setLsrn("123");
			bankUiList.add(b);


			b = new BankUITableDto();
			b.setApplicationDate("2012-03-31");
			b.setLoanAmount(108000);
			b.setBankName("Kotak");
			b.setStatus("In progress");
			b.setComments("Whatever");
			b.setLsrn("568");
			bankUiList.add(b);

			b = new BankUITableDto();
			b.setApplicationDate("2012-06-31");
			b.setLoanAmount(108000);
			b.setBankName("Kotak");
			b.setStatus("In progress");
			b.setComments("Whatever");
			b.setLsrn("568");
			bankUiList.add(b);



			b = new BankUITableDto();
			b.setApplicationDate("2012-06-31");
			b.setLoanAmount(108000);
			b.setBankName("Kotak");
			b.setStatus("In progress");
			b.setComments("Whatever");
			b.setLsrn("852");
			bankUiList.add(b);

			 */



			if(bankUiList!=null && !bankUiList.isEmpty()){
				Map<String, List<BankUITableDto>> myMap = new HashMap<>();
				for (BankUITableDto bankHist : bankUiList) {
					List<BankUITableDto> list = myMap.get(bankHist.getSrn());
					if(list==null){
						list = new ArrayList<>();
					}
					list.add(bankHist);
					myMap.put(bankHist.getSrn(), list);
				}

				for (String key : myMap.keySet()) {
					List<BankUITableDto> list =	myMap.get(key);
					String[] tempString = new String[5];
					tempString[0]="";
					tempString[1]="";
					tempString[2]="";
					tempString[3]="";
					tempString[4]="";

					tempString[0] = tempString[0].concat(list.get(0).getApplicationDate()+ "<br/>");
					tempString[1] = tempString[1].concat(""+list.get(0).getLoanAmount() + "<br/>");
					for (BankUITableDto bankUITableDto : list) {
						tempString[2] = tempString[2] + ""+bankUITableDto.getBankName() + "<br/>";
						tempString[3] = tempString[3] + ""+bankUITableDto.getStatus() + "<br/>";
						tempString[4] = tempString[4] + ""+bankUITableDto.getComments() + "<br/>";
					}

					for (int i = 0; i < tempString.length; i++) {
						if(!tempString[i].equals("")){
							tempString[i] = tempString[i].substring(0, tempString[i].length() - 5);
						}else{
							tempString[i] = "-";
						}
					}

					loanHistoryList.add(new JasperDynamicModel(Arrays.asList(tempString)));

				}



			}
			parametersList.put("loanHistoryList", loanHistoryList);

			////////////////////////////////
			List<CreditReportLoanDetails> unencumberedAssetDetails = new ArrayList<>();
			if(assestDetailList!=null){
				Double totAssets = 0d;
				Double calculatedUnEncumbered = 0d;
				for(AssetDetailDto assest : assestDetailList) {
					totAssets = totAssets + assest.getAssetValue();
					Double unEncom = (assest.getAssetValue()*assest.getEncumbered())/100;
					calculatedUnEncumbered = calculatedUnEncumbered + unEncom;
					unencumberedAssetDetails.add( new CreditReportLoanDetails(assest.getAssetName(), jasperReportService.applyFormattor(BigDecimal.valueOf(assest.getAssetValue()/unitDivider),null,null,null), String.valueOf(assest.getEncumbered())+"%"));
				}

				Double totalUnEncoumbered = totAssets-calculatedUnEncumbered;
				unencumberedAssetDetails.add( new CreditReportLoanDetails(jasperReportService.createBold("Total Assets"), jasperReportService.createBold(jasperReportService.applyFormattor(BigDecimal.valueOf(totAssets/unitDivider),null,null,null)),""));
				unencumberedAssetDetails.add( new CreditReportLoanDetails(jasperReportService.createBold("Total Unencumbered Assets"), jasperReportService.createBold(jasperReportService.applyFormattor(BigDecimal.valueOf((totalUnEncoumbered/unitDivider)),null,null,null)), ""));
			}
			parametersList.put("creditReportAssetsDetailsList", unencumberedAssetDetails);

			////////////////////////////////////////////////
			String amountHeading = "Amount ("+currency + " " + unit+")";
			parametersList.put("amountHeading", amountHeading);
			//////////////////////////////////////

			List<CreditReportDualInformation> keyBorrowerInformation = new ArrayList<>();
			if(LoanStatic.PORTFOLIO.equalsIgnoreCase(companyInfo.getRequestType())){
				keyBorrowerInformation = getBorrowerCompanyInformation(companyInfo);
			}else{
				keyBorrowerInformation = getBorrowerInformation(companyInfo);
			}
			parametersList.put("keyBorrowerInformation", keyBorrowerInformation);

			////////////////
			List<CreditReportDualInformation> encumberedAssetDetails = new ArrayList<>();
			if(assestDetailList!=null){
				for(AssetDetailDto assest : assestDetailList) {
					Double unEncom = assest.getAssetValue()*assest.getEncumbered()/100;
					encumberedAssetDetails.add(new CreditReportDualInformation(assest.getAssetName()!=null?assest.getAssetName().replaceAll("&", "&amp;"):"" ,jasperReportService.applyFormattor(BigDecimal.valueOf(unEncom/unitDivider),null,null,null)));
				}
			}
			parametersList.put("encumberedAssetDetails", encumberedAssetDetails);

			/////////////////////////////

			List<CreditReportDualInformation> managementDislosures = new ArrayList<>();
			List<DataRequestDocDto> docList = companyInfo.getDataRequestDoc();
			if(docList!=null){
				for(DataRequestDocDto doc : docList) {
					managementDislosures.add( new CreditReportDualInformation(doc.getDocumentCategory()!=null?doc.getDocumentCategory().replaceAll("&", "&amp;"):"", doc.getDocumentDetails()!=null?doc.getDocumentDetails().replaceAll("&", "&amp;"):""));
				}
			}
			parametersList.put("managementDislosures", managementDislosures);
			//////////////////////////

			List<CreditReportLoanDetails> managementDislosuresRemark = new ArrayList<>();
			String politicallyExposedPerson = "-";
			String sanctionOnPromotor = "-";
			String directorResign = "-";
			String criminalRecord = "-";
			String delayedPayment = "-";

			String politicallyExposedPersonComments="N/A";
			String sanctionedAgainstCompanyComments="N/A";
			String independentDirectorComments="N/A";
			String criminalRecordComments="N/A";
			String missedPaymentOfdebtComments="N/A";


			if(companyInfo.getBasicInfo()!=null){
				if(companyInfo.getBasicInfo().getPoliticallyExposedPerson()!=null){
					politicallyExposedPerson = companyInfo.getBasicInfo().getPoliticallyExposedPerson()==1?"Yes":"No";
				}
				if(companyInfo.getBasicInfo().getSanctionedAgainstCompany()!=null){
					sanctionOnPromotor = companyInfo.getBasicInfo().getSanctionedAgainstCompany()==1?"Yes": "No";
				}
				if(companyInfo.getBasicInfo().getIndependentDirector()!=null){
					directorResign = companyInfo.getBasicInfo().getIndependentDirector()==1?"Yes":"No";
				}
				if(companyInfo.getBasicInfo().getCriminalRecord()!=null){
					criminalRecord = companyInfo.getBasicInfo().getCriminalRecord()==1?"Yes": "No";
				}
				if(companyInfo.getBasicInfo().getMissedPaymentOfdebt()!=null){
					delayedPayment = companyInfo.getBasicInfo().getMissedPaymentOfdebt()==1?"Yes":"No";
				}
				/////////////////####
				if(companyInfo.getBasicInfo().getMissedPaymentOfdebt()!=null){
					politicallyExposedPersonComments = companyInfo.getBasicInfo().getPoliticallyExposedPersonComments();
				}
				if(companyInfo.getBasicInfo().getMissedPaymentOfdebt()!=null){
					sanctionedAgainstCompanyComments = companyInfo.getBasicInfo().getSanctionedAgainstCompanyComments();
				}
				if(companyInfo.getBasicInfo().getMissedPaymentOfdebt()!=null){
					independentDirectorComments = companyInfo.getBasicInfo().getIndependentDirectorComments();
				}
				if(companyInfo.getBasicInfo().getMissedPaymentOfdebt()!=null){
					criminalRecordComments = companyInfo.getBasicInfo().getCriminalRecordComments();
				}
				if(companyInfo.getBasicInfo().getMissedPaymentOfdebt()!=null){
					missedPaymentOfdebtComments = companyInfo.getBasicInfo().getMissedPaymentOfdebtComments();
				}
			}
			managementDislosuresRemark.add( new CreditReportLoanDetails("Are any key managerial personnel or promoters Politically Exposed Persons (PEP)?", politicallyExposedPerson, politicallyExposedPersonComments));
			managementDislosuresRemark.add( new CreditReportLoanDetails("Are there any sanctions against the company, key managerial personnel or promoters?",sanctionOnPromotor, sanctionedAgainstCompanyComments));
			managementDislosuresRemark.add( new CreditReportLoanDetails("Have any Independent Directors or Key Managerial Personnel resigned in the past 2 years?", directorResign, independentDirectorComments));
			managementDislosuresRemark.add( new CreditReportLoanDetails("Are there any criminal proceedings, cases or lawsuits against the company, key managerial personnel or promoters?",criminalRecord, criminalRecordComments));
			managementDislosuresRemark.add( new CreditReportLoanDetails("Has the company delayed or missed payments of its debt obligations in the past 5 years?", delayedPayment, missedPaymentOfdebtComments));
			parametersList.put("managementDislosuresRemark", managementDislosuresRemark);
			//////////////////////////

			List<CreditReportLoanDetails> managementExperience = new ArrayList<>();
			List<keyManagementInfoDto> keyMgmtInfoList = companyInfo.getKeyMgmntInfoList();
			if(keyMgmtInfoList!=null){
				for(keyManagementInfoDto keyMgmt : keyMgmtInfoList) {
					managementExperience.add( new CreditReportLoanDetails(keyMgmt.getTitle()+" "+keyMgmt.getName(), keyMgmt.getDesignation(), String.valueOf(keyMgmt.getYearOfExp())+" years"));
				}
			}
			parametersList.put("managementExperience", managementExperience);
			//////////////////////////
			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd MMM-yy");
			String as_on = customFormatter.format(localDateTime);
			parametersList.put("as_on", as_on);
			////////////////////////////////
			List<CreditReportFourCol> ownershipDetails = new ArrayList<>();
			List<OwnershipStructureDto> ownershipList = companyInfo.getOwnershipStructureList();
			if(ownershipList!=null){
				Double totalOwnerShipToday = 0d;
				Double totalOwnerShipPrev = 0d;
				Double totalOwnershipPrevMinus1 = 0d;
				for(OwnershipStructureDto ownership : ownershipList) {
					totalOwnerShipToday = totalOwnerShipToday + ownership.getPerOfShareToday();
					totalOwnerShipPrev = totalOwnerShipPrev + ownership.getPerOfShareLatestFinYear();
					totalOwnershipPrevMinus1 = totalOwnershipPrevMinus1 + ownership.getPerOfSharePrevFinYear();
					ownershipDetails.add( new CreditReportFourCol(ownership.getType()!=null?ownership.getType().replaceAll("&", "&amp;"):"", String.valueOf(ownership.getPerOfShareToday()+"%"), String.valueOf(ownership.getPerOfShareLatestFinYear()+"%"),String.valueOf(ownership.getPerOfSharePrevFinYear()+"%")));

				}
				ownershipDetails.add(new CreditReportFourCol(jasperReportService.createBold("Total"), jasperReportService.createBold(""+totalOwnerShipToday+"%"), jasperReportService.createBold(""+totalOwnerShipPrev+"%"), jasperReportService.createBold(""+totalOwnershipPrevMinus1+"%")));
			}
			parametersList.put("ownershipDetails", ownershipDetails);
			//////////////////////////



			List<CreditReportFourCol> promotorsOwnershipDetails = new ArrayList<>();
			List<PromoterGroupOwnershipStructureDto> promoterOwnershipList = companyInfo.getPromoterGroupOwnershipStructure();
			if(promoterOwnershipList!=null){			
				Double totalPromotorOwnerShipToday = 0d;
				Double totalPromotorOwnerShipPrev = 0d;
				Double totalPromotorOwnershipPrevMinus1 = 0d;
				for(PromoterGroupOwnershipStructureDto promoterOwnership : promoterOwnershipList) {
					totalPromotorOwnerShipToday = totalPromotorOwnerShipToday + promoterOwnership.getPerOfShareToday();
					totalPromotorOwnerShipPrev = totalPromotorOwnerShipPrev + promoterOwnership.getPerOfShareLatestFinYear();
					totalPromotorOwnershipPrevMinus1 = totalPromotorOwnershipPrevMinus1 + promoterOwnership.getPerOfSharePrevFinYear();
					String promotorName = promoterOwnership.getName();
					if(promoterOwnership.getTitle()!=null && !promoterOwnership.getTitle().equals("")){
						promotorName = promoterOwnership.getName() + " ("+promoterOwnership.getTitle()+")";
					}

					promotorsOwnershipDetails.add(new CreditReportFourCol(promotorName , String.valueOf(promoterOwnership.getPerOfShareToday()+"%"), String.valueOf(promoterOwnership.getPerOfShareLatestFinYear()+"%"),String.valueOf(promoterOwnership.getPerOfSharePrevFinYear()+"%")));
				}
				promotorsOwnershipDetails.add(new CreditReportFourCol(jasperReportService.createBold("Total"), jasperReportService.createBold(""+totalPromotorOwnerShipToday+"%"),jasperReportService.createBold( ""+totalPromotorOwnerShipPrev+"%"), jasperReportService.createBold(""+totalPromotorOwnershipPrevMinus1+"%")));
			}
			parametersList.put("promotorsOwnershipDetails", promotorsOwnershipDetails);
			//////////////////////////

			List<List<JasperDynamicModel>> allParameterData = new ArrayList<>();
			List<List<ReportPerformanceData> > chartDataList = new ArrayList<>();
			List<String> chartTitleList = new ArrayList<>();
			List<String> chartSummaryList = new ArrayList<>();
			List<UserModelDTO> userListALL = null;

			List<UserModelDTO> userListBS = new ArrayList<>();
			List<UserModelDTO> userListCF = new ArrayList<>();
			List<UserModelDTO> userListIS = new ArrayList<>();
			List<UserModelDTO> userListFR = new ArrayList<>();

			Future<List<UserModelDTO>> userListFut = executorService.submit(()->{
				try{
					return userService.getUserFinancial(userId, "ALL", "yearly", sdf.parse(companyInfo.getApplicableFromYear()), sdf.parse(companyInfo.getApplicableToYear()), currency);
				}catch(Exception e){
					throw e;
				}
			});
			/*if(userListALL!=null){
				_log.debug(new Gson().toJson(userListALL));
			}*/

			LinkedHashMap<String, List<ReportPerformanceData>> chartDataTempList = new LinkedHashMap<>();
			LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> summaryMap = new LinkedHashMap<>();
			LinkedHashMap<String,List<ReportPerformanceData>> userMap = new LinkedHashMap<>();
			LinkedHashMap<String,List<ReportPerformanceData>> peerCompanyMap1 = new LinkedHashMap<>();
			LinkedHashMap<String,List<ReportPerformanceData>> peerCompanyMap2 = new LinkedHashMap<>();
			LinkedHashMap<String,List<ReportPerformanceData>> peerAvgMap = new LinkedHashMap<>();
			LinkedHashMap<String,Integer> scoreMap = new LinkedHashMap<>();
			LinkedHashMap<String,String> scoreCommentMap = new LinkedHashMap<>();

			List<String> allFieldList = new ArrayList<String>();
			List<String> ratioFieldList = new ArrayList<String>();
			ratioFieldList.addAll(LoanStatic.PERFORMANCE_AND_ACTIVITY_RATIOS);
			ratioFieldList.addAll(LoanStatic.PROFITABILITY_AND_RETURN_RATIOS);
			ratioFieldList.addAll(LoanStatic.LEVERAGE_RATIO);
			ratioFieldList.addAll(LoanStatic.LIQUIDITY_RATIO);
			ratioFieldList.addAll(LoanStatic.DEBT_COVERAGE_RATIO);
			allFieldList.addAll(ratioFieldList);
			allFieldList.addAll(LoanStatic.FINANCIAL_SNAPSHOT);
			allFieldList.add(LoanStatic.CAPEX);



			for (String string : ratioFieldList) {
				chartDataTempList.put(string,new ArrayList<ReportPerformanceData>());
				titleMap.put(string, "");
				summaryMap.put(string, "");
				userMap.put(string, new ArrayList<>());
				peerCompanyMap1.put(string, new ArrayList<>());
				peerCompanyMap2.put(string, new ArrayList<>());
				peerAvgMap.put(string, new ArrayList<>());
				scoreMap.put(string, 0);
			}


			List<UserModelDTO> financialSnapshotListUSer = new ArrayList<>(); 
			List<UserModelDTO> financialSnapshotListIndustry = new ArrayList<>(); 
			List<UserModelDTO> capexUser = new ArrayList<>(); 
			String calculatedPeriod = null;
			
			userListALL = userListFut.get();
			
			if(userListALL!=null){
				for (UserModelDTO userModelDTO : userListALL) {
					if(sdf.format(userModelDTO.getApplicablePeriod()).equals(companyInfo.getApplicableToYear())){
						calculatedPeriod = sdf.format(userModelDTO.getPeriod());
					}

					if(LoanStatic.BALANCE_SHEET.equalsIgnoreCase(userModelDTO.getFinancialType())){
						userListBS.add(userModelDTO);
					}
					if(LoanStatic.CASH_FLOW.equalsIgnoreCase(userModelDTO.getFinancialType())){
						userListCF.add(userModelDTO);
					}
					if(LoanStatic.PROFIT_AND_LOSS.equalsIgnoreCase(userModelDTO.getFinancialType())){
						userListIS.add(userModelDTO);
					}
					if(LoanStatic.FINANCIAL_RATIOS.equalsIgnoreCase(userModelDTO.getFinancialType())){
						userListFR.add(userModelDTO);
					}

					if(LoanStatic.FINANCIAL_SNAPSHOT.contains(userModelDTO.getFieldName())){
						financialSnapshotListUSer.add(userModelDTO);
					}

					if(LoanStatic.CAPEX.equals(userModelDTO.getFieldName())){
						capexUser.add(userModelDTO);
					}

					for (String string : ratioFieldList) {

						if(userModelDTO.getFieldName().equals(string)){
							titleMap.put(string, userModelDTO.getDisplayName()+ " "+ jasperReportService.unitCurrencyAppender(userModelDTO.getCurrency(), userModelDTO.getUnit()));
							if(applicablePeriodList.contains(sdf.format(userModelDTO.getApplicablePeriod()))){
								List<ReportPerformanceData> tmList = chartDataTempList.get(string);
								ReportPerformanceData reportPerformanceData = new ReportPerformanceData();
								reportPerformanceData.setData(userModelDTO.getData());
								reportPerformanceData.setPeriod(sdfChart.format(userModelDTO.getApplicablePeriod()));
								reportPerformanceData.setSeries(companyName);
								List<ReportPerformanceData>userData = userMap.get(string);
								userData.add(reportPerformanceData);
								userMap.put(string, userData);
								tmList.add(reportPerformanceData);
							}
						}
					}
				}
			}

			List<String> calculatedPeriodList = new ArrayList<String>();
			HashMap<String, String> periodMap = new HashMap<>();
			List<String> annexturePeriodList = new ArrayList<>();
			List<String> annextureApplicablePeriodList = new ArrayList<>();
			if(calculatedPeriod!=null){
				calculatedPeriodList = dateUtil.generatePrevApplicablePeriodYear(calculatedPeriod, diff);
				int tP = Integer.parseInt(calculatedPeriod.split("-")[0]);
				annexturePeriodList.add(""+tP);
				annexturePeriodList.add(""+(tP-1));
				annexturePeriodList.add(""+(tP-2));
				annexturePeriodList.add(""+(tP-3));
				annexturePeriodList.add(""+(tP-4));
			}else{
				throw new RuntimeException("Invalid Data");
			}

			parametersList.put("annexturePeriodList", annexturePeriodList);

			for (int i = 0; i < applicablePeriodList.size(); i++) {
				periodMap.put(applicablePeriodList.get(i), calculatedPeriodList.get(i));
			}
			/*/////////////////////////////////////////////
			period map plays an important role in the caluclation if the display is changed.
			as the calculations are based on the applicable period and the display period is the period , The time in the charts are the 
			applicable period , if it is changed , it has to be as per the period map
			 */
			_log.info(periodMap);

			List<String> periodListChart = new ArrayList<>();
			for (String string : calculatedPeriodList) {
				periodListChart.add(sdfChart.format(sdf.parse(string)));
			}

			parametersList.put("periodListChart", periodListChart);

			/////// this code need to be changed as for yearly and qtrly changes


			for (String key : titleMap.keySet()) {
				chartTitleList.add(titleMap.get(key));
			}

			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			SimpleDateFormat sdfMon = new SimpleDateFormat("MMM");
			String year = sdfYear.format(sdf.parse(companyInfo.getApplicableToYear()));
			String month = sdfMon.format(sdf.parse(companyInfo.getApplicableToYear()));

			int tempYear = Integer.parseInt(year);

			annextureApplicablePeriodList.add(""+tempYear);
			annextureApplicablePeriodList.add(""+(tempYear-1));
			annextureApplicablePeriodList.add(""+(tempYear-2));
			annextureApplicablePeriodList.add(""+(tempYear-3));
			annextureApplicablePeriodList.add(""+(tempYear-4));

			UserPeerInfoDTO userPeerLatestCompany = null;
			List<UserPeerInfoDTO> userPeerCompanyList = null;

			Date toPeriodDate = null;
			Date fromPeriodDate = null;
			String toPeriodString = null;
			String fromPeriodString = null;
			Integer finYearEndMonth = null;
			fromPeriodDate = sdf.parse(companyInfo.getApplicableFromYear());
			toPeriodDate = sdf.parse(companyInfo.getApplicableToYear());
			fromPeriodString = companyInfo.getApplicableFromYear();
			toPeriodString = companyInfo.getApplicableToYear();
			if(toPeriodDate != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(toPeriodDate);
				finYearEndMonth = calendar.get(Calendar.MONTH) + 1;
			}
			try{
				//userPeerCompanyList = getPeerCompanyList(userId,toPeriodString,fromPeriodString);
				userPeerCompanyList = getPeerCompanyList(userId,toPeriodString,toPeriodString);
				if(userPeerCompanyList!=null && userPeerCompanyList.size()>0) {
					userPeerLatestCompany = userPeerCompanyList.get(0);
				}
			}catch(Exception e){
				_log.error(e.getMessage(),e);
			}
			List<IndustryFinancialDataDTO> companyData1 = null;
			List<IndustryFinancialDataDTO> companyData2 = null;
			String peerCompany1 = "company1";
			String peerCompany2 = "company2";
			String peerCompanyAvg = "PeerAvg";

			StringBuilder sb = new StringBuilder();
			Iterator<String> itr = allFieldList.iterator();  
			while (itr.hasNext()) {
				sb.append(itr.next()).append(",");
			}
			String params  = sb.substring(0, sb.length() - 1);

			if(userPeerLatestCompany!=null) {
				List<String> userList = Arrays.asList(userPeerLatestCompany.getPeerCompanyId().split("\\s*,\\s*"));
				Integer companyFlag = null;

				if(userList!=null){
					_log.info("Finding the company data");
					for (String tempCompanyId : userList) {
						if(companyData1==null){
							_log.info("Finding peer for company 1 for the compay Id " + tempCompanyId);
						}
						if(companyData1!=null && companyData2==null){
							_log.info("Finding peer for company 2 for the compay Id " + tempCompanyId);
						}
						List<IndustryFinancialDataDTO>	companyData = industryService.getIDCompanyData("yearly", null, tempCompanyId, null, params, fromPeriodDate, toPeriodDate, currency, finYearEndMonth,companyFlag);
						if(companyData==null || companyData.isEmpty()){
							_log.info("The data is blank for " + tempCompanyId + ".....Checking next....blooop bleeep bleeep");
							continue;
						}
						if(companyData!=null && !companyData.isEmpty() && companyData1==null){
							companyData1 = companyData;
						}else{
							if(companyData!=null && !companyData.isEmpty() && companyData1!=null && companyData2==null){
								companyData2 = companyData;
								break;
							}
						}
					}
				}

				/*if(userList.size()>=2) {
					String companyId1 = userList.get(0);
					String companyId2 = userList.get(1);
					//Integer companyFlag = null;
					try {
						companyData1 = industryService.getIDCompanyData("yearly", null, companyId1, null, params, fromPeriodDate, toPeriodDate, currency, finYearEndMonth,companyFlag);
						//This is temporary code because data from industry table come in Billion for converting the data in Million
						companyData1 = convertUnitToMillion(companyData1);
					}catch(Exception e){
						//companyData1= new ArrayList<>();
					}
					try{
						companyData2 = industryService.getIDCompanyData("yearly", null, companyId2, null, params, fromPeriodDate, toPeriodDate , currency, finYearEndMonth,companyFlag);
						//This is temporary code because data from industry table come in Billion for converting the data in Million
						companyData2 = convertUnitToMillion(companyData2);
					}catch (Exception e) {
						//companyData2= new ArrayList<>();
					}
				}
				 */			
			}

			List<IndustryFinancialDataDTO> capexCompany1 = new ArrayList<>(); 
			List<IndustryFinancialDataDTO> capexCompany2 = new ArrayList<>(); 
			List<UserModelDTO> capexPeer = new ArrayList<>(); 

			if(companyData1!=null && companyData1.size()>0) {
				companyData1 = convertUnitToMillion(companyData1);
				peerCompany1 = companyData1.get(0).getCompanyName();
				for (IndustryFinancialDataDTO companyModel1 : companyData1) {

					if(LoanStatic.CAPEX.equals(companyModel1.getFieldName())){
						capexCompany1.add(companyModel1);
					}

					for (String string : ratioFieldList) {
						if(companyModel1.getFieldName().equals(string)){

							boolean testCondition = false;
							String tempPeriod = null;

							/*if(isTempForPeerCompaniesTobedeleted){
								String[] td = sdf.format(companyModel1.getPeriod()).split("-");
								tempPeriod = td[0]+"-12-31";
								testCondition = true;
							}else{
								tempPeriod = sdf.format(companyModel1.getPeriod());
								testCondition = applicablePeriodList.contains(tempPeriod);
							}*/

							tempPeriod = sdf.format(companyModel1.getApplicablePeriod());
							testCondition = applicablePeriodList.contains(tempPeriod);

							if(testCondition){

								List<ReportPerformanceData> tmList = chartDataTempList.get(string);
								ReportPerformanceData reportPerformanceData = new ReportPerformanceData();
								reportPerformanceData.setData(companyModel1.getData());
								String period = sdfChart.format(companyModel1.getApplicablePeriod());
								if(!isTempCode) {
									reportPerformanceData.setPeriod(period);
								}else {
									period = month+"-"+period.split("-")[1];
									reportPerformanceData.setPeriod(period);
								}
								reportPerformanceData.setSeries(peerCompany1);
								List<ReportPerformanceData>userData = peerCompanyMap1.get(string);
								userData.add(reportPerformanceData);
								peerCompanyMap1.put(string, userData);
								tmList.add(reportPerformanceData);
							}
						}
					}


				}
			}

			if(companyData2!=null && companyData2.size()>0) {
				companyData2 = convertUnitToMillion(companyData2);
				peerCompany2 = companyData2.get(0).getCompanyName();
				for (IndustryFinancialDataDTO companyModel2 : companyData2) {

					if(LoanStatic.CAPEX.equals(companyModel2.getFieldName())){
						capexCompany2.add(companyModel2);
					}

					for (String string : ratioFieldList) {
						if(companyModel2.getFieldName().equals(string)){
							boolean testCondition = false;
							String tempPeriod = null;

							/*if(isTempForPeerCompaniesTobedeleted){
								String[] td = sdf.format(companyModel2.getPeriod()).split("-");
								tempPeriod = td[0]+"-12-31";
								testCondition = true;
							}else{
								tempPeriod = sdf.format(companyModel2.getPeriod());
								testCondition = applicablePeriodList.contains(tempPeriod);
							}*/

							tempPeriod = sdf.format(companyModel2.getApplicablePeriod());
							testCondition = applicablePeriodList.contains(tempPeriod);

							if(testCondition){

								List<ReportPerformanceData> tmList = chartDataTempList.get(string);
								ReportPerformanceData reportPerformanceData = new ReportPerformanceData();
								reportPerformanceData.setData(companyModel2.getData());
								//reportPerformanceData.setPeriod(sdfChart.format(companyModel2.getPeriod()));
								String period = sdfChart.format(companyModel2.getApplicablePeriod());
								if(!isTempCode) {
									reportPerformanceData.setPeriod(period);
								}else {
									period = month+"-"+period.split("-")[1];
									reportPerformanceData.setPeriod(period);
								}
								reportPerformanceData.setSeries(peerCompany2);
								List<ReportPerformanceData>userData = peerCompanyMap2.get(string);
								userData.add(reportPerformanceData);
								peerCompanyMap2.put(string, userData);
								tmList.add(reportPerformanceData);
							}
						}
					}


				}
			}
			List<UserModelDTO> peerAvgList = null;

			try{
				peerAvgList = userService.getPeerData(userId, null, "yearly", sdf.parse(companyInfo.getApplicableFromYear()), sdf.parse(companyInfo.getApplicableToYear()),currency, null, null);
				String applicableFromYear = null;
				//check if fromDate and toDate is equal
				if(companyInfo.getApplicableFromYear().equals(companyInfo.getApplicableToYear())){
					//generate previous date
					applicableFromYear = dateUtil.generatePreviousDate(companyInfo.getApplicableToYear(), 1);
					_log.info("generated startDate for peer data "+applicableFromYear);
				}else{
					applicableFromYear = companyInfo.getApplicableFromYear();
				}
				String financialSnapshotParams = LoanStatic.FINANCIAL_SNAPSHOT.stream().collect(Collectors.joining(","));
				financialSnapshotListIndustry = userService.getPeerData(userId, null, "yearly", sdf.parse(applicableFromYear), sdf.parse(companyInfo.getApplicableToYear()),currency, null, financialSnapshotParams);
			}catch(Exception e){
				throw e;
			}

			if(peerAvgList!=null) {
				for (UserModelDTO peerAvgModel : peerAvgList) {

					/*if(LoanStatic.FINANCIAL_SNAPSHOT.contains(peerAvgModel.getFieldName())){
						financialSnapshotListIndustry.add(peerAvgModel);
					}*/


					if(LoanStatic.CAPEX.equals(peerAvgModel.getFieldName())){
						capexPeer.add(peerAvgModel);
					}

					for (String string : ratioFieldList) {
						if(peerAvgModel.getFieldName().equals(string)){
							if(applicablePeriodList.contains(sdf.format(peerAvgModel.getApplicablePeriod()))){

								List<ReportPerformanceData> tmList = chartDataTempList.get(string);
								ReportPerformanceData reportPerformanceData = new ReportPerformanceData();
								reportPerformanceData.setData(peerAvgModel.getData());
								//reportPerformanceData.setPeriod(sdfChart.format(peerAvgModel.getPeriod()));
								String period = sdfChart.format(peerAvgModel.getApplicablePeriod());
								if(!isTempCode) {
									reportPerformanceData.setPeriod(period);
								}else {
									period = month+"-"+period.split("-")[1];
									reportPerformanceData.setPeriod(period);
								}
								reportPerformanceData.setSeries(peerCompanyAvg);
								List<ReportPerformanceData>userData = peerAvgMap.get(string);
								userData.add(reportPerformanceData);
								peerAvgMap.put(string, userData);
								tmList.add(reportPerformanceData);
							}
						}
					}


				}
			}

			List<UserModelDTO> scoreValueList = null;

			try{
				scoreValueList = userService.getScore(userId, "ALL", "yearly", sdf.parse(companyInfo.getApplicableToYear()), sdf.parse(companyInfo.getApplicableToYear()), null, null);
			}catch(Exception e){
				throw e;
			}
			if(scoreValueList!=null) {
				for (UserModelDTO score : scoreValueList) {
					if(companyInfo.getApplicableToYear().equals(sdf.format(score.getApplicablePeriod()))){
						for (String string : ratioFieldList) {
							if(score.getFieldName().equals(string)){
								scoreMap.put(string, score.getData()==null?null:score.getData().intValue());
								break;
							}
						}
					}
				}
			}

			List<UserDataScoringCommentDto> scoreCommentList = null;
			try{
				scoreCommentList = userService.getUserScoreComment(userId,allFieldList ,sdf.parse(companyInfo.getApplicableToYear()));
			}catch(Exception e){
				throw e;
			}

			if(scoreCommentList!=null) {
				for (UserDataScoringCommentDto scoreComment : scoreCommentList) {
					if(companyInfo.getApplicableToYear().equals(sdf.format(scoreComment.getApplicablePeriod()))){
						for (String string : ratioFieldList) {
							if(scoreComment.getFieldName().equals(string)){
								scoreCommentMap.put(string, scoreComment.getComment());
								break;
							}
						}
					}
				}
			}

			LinkedHashMap<String, ReportAnnextureModel> financialMap = getFinancialDataMap(financialSnapshotListUSer, annextureApplicablePeriodList,LoanStatic.FINANCIAL_SNAPSHOT_MODULE);
			LinkedHashMap<String, ReportAnnextureModel> financialMapPeer = getFinancialDataMap(financialSnapshotListIndustry, annextureApplicablePeriodList,LoanStatic.FINANCIAL_SNAPSHOT_MODULE);

			List<CreditReportFinancialSnapshot> financialSnapShotList = new ArrayList<>();

			if(financialMap!=null){
				for (String key  : LoanStatic.FINANCIAL_SNAPSHOT) {
					ReportAnnextureModel userRAM = financialMap.get(key);
					ReportAnnextureModel peerRAM = financialMapPeer.get(key);
					String percentChangePrint = "-";
					String percentChangePrintPeer = "-";
					if(userRAM!=null){
						if(userRAM.getSlot1()==null || userRAM.getSlot1().equals("-") || userRAM.getSlot2()==null || userRAM.getSlot2().equals("-")){
							percentChangePrint = "-";
						}else{
							/*if(Double.valueOf(userRAM.getSlot1())==0d && Double.valueOf(userRAM.getSlot2())==0d){
								percentChangePrint = "-";
							}else if(Double.valueOf(userRAM.getSlot2())==0d){
								percentChangePrint = "-";
							}else{*/
							//Double percentChange = ((Double.valueOf(userRAM.getSlot1())-Double.valueOf(userRAM.getSlot2()))/Double.valueOf(userRAM.getSlot2()))*100;
							Double percentChange = null;
							if(userRAM.getUnit()!=null && (!userRAM.getUnit().contains("%") && !userRAM.getUnit().contains("days"))){
								if(Double.valueOf(userRAM.getSlot1())==0d && Double.valueOf(userRAM.getSlot2())==0d){
									percentChangePrint = "-";
								}else if(Double.valueOf(userRAM.getSlot2())==0d){
									percentChangePrint = "-";
								}else{
									percentChange = percentageChange(Double.valueOf(userRAM.getSlot1()), Double.valueOf(userRAM.getSlot2()));
								}
							}else{

								if(userRAM.getUnit().contains("days")){
									percentChange = Double.valueOf(userRAM.getSlot1()) - Double.valueOf(userRAM.getSlot2());
								}else{
									percentChange = (Double.valueOf(userRAM.getSlot1()) - Double.valueOf(userRAM.getSlot2()))*100;
								}
							}
							if(percentChange!=null){
								if(!userRAM.getUnit().contains("%")){
									percentChangePrint = jasperReportService.customFormattor(percentChange,"%",null,false);
								}else{
									percentChangePrint = jasperReportService.customFormattor(percentChange,"bps",null,false);
								}
							}
							/*}*/
						}

						if(!percentChangePrint.equals("-")){
							if(userRAM.getUnit()!=null){
								if(!userRAM.getUnit().contains("%")){
									percentChangePrint = percentChangePrint+"%";
								}else{
									percentChangePrint = percentChangePrint+" bps";
								}
							}
						}
					}
					if(peerRAM!=null){
						if(peerRAM.getSlot1()==null || peerRAM.getSlot1().equals("-") || peerRAM.getSlot2()==null || peerRAM.getSlot2().equals("-")){
							percentChangePrintPeer = "-";
						}else{
							/*if(Double.valueOf(peerRAM.getSlot1())==0d && Double.valueOf(peerRAM.getSlot2())==0d){
								percentChangePrintPeer = "-";
							}else if(Double.valueOf(peerRAM.getSlot2())==0d){
								percentChangePrint = "-";
							}else{*/
							//Double percentChangePeer = ((Double.valueOf(peerRAM.getSlot1())-Double.valueOf(peerRAM.getSlot2()))/Double.valueOf(peerRAM.getSlot2()))*100;

							Double percentChangePeer = null;
							if(peerRAM.getUnit()!=null && (!peerRAM.getUnit().contains("%") && !peerRAM.getUnit().contains("days"))){
								if(Double.valueOf(peerRAM.getSlot1())==0d && Double.valueOf(peerRAM.getSlot2())==0d){
									percentChangePrintPeer = "-";
								}else if(Double.valueOf(peerRAM.getSlot2())==0d){
									percentChangePrintPeer = "-";
								}else{
									percentChangePeer = percentageChange(Double.valueOf(peerRAM.getSlot1()), Double.valueOf(peerRAM.getSlot2()));
								}
							}else{
								if(peerRAM.getUnit().contains("days")){
									percentChangePeer = Double.valueOf(peerRAM.getSlot1()) - Double.valueOf(peerRAM.getSlot2());
								}else{
									percentChangePeer = (Double.valueOf(peerRAM.getSlot1()) - Double.valueOf(peerRAM.getSlot2()))*100;
								}
							}
							if(!percentChangePeer.equals("-")){
								if(!peerRAM.getUnit().contains("%")){
									percentChangePrintPeer = jasperReportService.customFormattor(percentChangePeer,"%",null,false);
								}else{
									percentChangePrintPeer = jasperReportService.customFormattor(percentChangePeer,"bps",null,false);
								}
							}
							/*}*/
						}
						if(!percentChangePrintPeer.equals("-")){

							if(peerRAM.getUnit()!=null){
								if(!peerRAM.getUnit().contains("%")){
									percentChangePrintPeer = percentChangePrintPeer+"%";
								}else{
									percentChangePrintPeer = percentChangePrintPeer+" bps";
								}
							}
						}
					}
					try{
						/*if(!percentChangePrint.equals("-")){
							percentChangePrint = percentChangePrint+"%";
						}*/
						/*if(!percentChangePrintPeer.equals("-")){
							percentChangePrintPeer = percentChangePrintPeer+"%";
						}*/
						financialSnapShotList.add(new CreditReportFinancialSnapshot(userRAM.getSubMetric()!=null?jasperReportService.createBold(userRAM.getSubMetric()):""+" " + jasperReportService.unitCurrencyAppender(userRAM.getCurrency(), userRAM.getUnit()),jasperReportService.customFormattor(userRAM.getSlot1()==null?null:Double.valueOf(userRAM.getSlot1()),null,null,false),jasperReportService.customFormattor(userRAM.getSlot2()==null?null:Double.valueOf(userRAM.getSlot2()),null,null,false),percentChangePrint,jasperReportService.customFormattor(peerRAM.getSlot1()==null?null:Double.valueOf(peerRAM.getSlot1()),null,null,false),jasperReportService.customFormattor(peerRAM.getSlot2()==null?null:Double.valueOf(peerRAM.getSlot2()),null,null,false),percentChangePrintPeer));
					}catch(Exception e){
						_log.error(e.getMessage());
						throw e;
					}
				}
			}
			parametersList.put("financialSnapshot", financialSnapShotList);
			List<String> scoreList = new ArrayList<>();
			if(chartDataTempList!=null){
				for (String key : chartDataTempList.keySet()) {
					List<ReportPerformanceData> userList = userMap.get(key);
					List<ReportPerformanceData> peerComp1 = peerCompanyMap1.get(key);
					List<ReportPerformanceData> peerComp2 = peerCompanyMap2.get(key);
					List<ReportPerformanceData> peerAvg = peerAvgMap.get(key);

					String title = titleMap.get(key);
					String unitToAppend = "";
					if(title.contains("(x)")){
						unitToAppend = "x";
					}else if(title.contains("(%)")){
						unitToAppend = "%";
					}

					Double userLatestData = null;
					Double peerLatestData = null;

					sortReportElement(userList);
					sortReportElement(peerComp1);
					sortReportElement(peerComp2);
					sortReportElement(peerAvg);

					List<JasperDynamicModel> parameterCompanyData = new LinkedList<>();
					String[] tempString = new String[8];
					tempString[0] = LoanStatic.LOAN_REPORT_IMAGES+"1.jpg";
					tempString[1]= companyName!=null?companyName.replaceAll("&", "&amp;"):"Company";
					tempString[2] = "-";
					tempString[3] = "-";
					tempString[4] = "-";
					tempString[5] = "-";
					tempString[6] = "-";

					String[] tempStringCompany1 = new String[8];
					tempStringCompany1[0] = LoanStatic.LOAN_REPORT_IMAGES+"2.jpg";
					tempStringCompany1[1]= peerCompany1!=null?peerCompany1.replaceAll("&", "&amp;"):"Company1";
					tempStringCompany1[2] = "-";
					tempStringCompany1[3] = "-";
					tempStringCompany1[4] = "-";
					tempStringCompany1[5] = "-";
					tempStringCompany1[6] = "-";

					String[] tempStringCompany2 = new String[8];
					tempStringCompany2[0] = LoanStatic.LOAN_REPORT_IMAGES+"3.jpg";
					tempStringCompany2[1]= peerCompany2!=null?peerCompany2.replaceAll("&", "&amp;"):"Company2";
					tempStringCompany2[2] = "-";
					tempStringCompany2[3] = "-";
					tempStringCompany2[4] = "-";
					tempStringCompany2[5] = "-";
					tempStringCompany2[6] = "-";

					String[] tempStringPeerAvg = new String[8];
					tempStringPeerAvg[0] = LoanStatic.LOAN_REPORT_IMAGES+"4.jpg";
					tempStringPeerAvg[1]= peerCompanyAvg!=null?peerCompanyAvg.replaceAll("&", "&amp;"):"PeerAvg";
					tempStringPeerAvg[2] = "-";
					tempStringPeerAvg[3] = "-";
					tempStringPeerAvg[4] = "-";
					tempStringPeerAvg[5] = "-";
					tempStringPeerAvg[6] = "-";

					Boolean[] pCheck = new Boolean[5];
					Boolean[] pCheckComp1 = new Boolean[5];
					Boolean[] pCheckComp2 = new Boolean[5];
					Boolean[] pCheckPeerAvg = new Boolean[5];
					for (int p=0;p < applicablePeriodList.size();p++) {
						String period = sdfYear.format(sdf.parse(applicablePeriodList.get(p)));
						for (int i = 0; i < userList.size(); i++) {
							if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfChart.parse(userList.get(i).getPeriod())))){
								String data = "-";
								Double tempData = userList.get(i).getData();
								//String unit = userList.get(i).get
								if(p==0){
									userLatestData = tempData;
								}
								if(tempData!=null){

									String value = jasperReportService.applyFormattor(BigDecimal.valueOf(tempData), null, null, null);
									if(!tempData.equals("-")){
										value = value + unitToAppend;
									}
									data = jasperReportService.fontCheck(value);
								}

								tempString[6-p] = data;
								pCheck[p] = true;
								break;
							}
						}
						for (int i = 0; i < peerComp1.size(); i++) {
							if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfChart.parse(peerComp1.get(i).getPeriod())))){
								String data = "-";
								Double tempData = peerComp1.get(i).getData();
								if(tempData!=null){
									String value = jasperReportService.applyFormattor(BigDecimal.valueOf(tempData), null, null, null);
									if(!tempData.equals("-")){
										value = value + unitToAppend;
									}
									data = jasperReportService.fontCheck(value);
								}

								tempStringCompany1[6-p] = data;
								pCheckComp1[p] = true;
								break;
							}
						}
						for (int i = 0; i < peerComp2.size(); i++) {
							if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfChart.parse(peerComp2.get(i).getPeriod())))){
								String data = "-";
								Double tempData = peerComp2.get(i).getData();
								if(tempData!=null){
									String value = jasperReportService.applyFormattor(BigDecimal.valueOf(tempData), null, null, null);
									if(!tempData.equals("-")){
										value = value + unitToAppend;
									}
									data = jasperReportService.fontCheck(value);
								}

								tempStringCompany2[6-p] = data;
								pCheckComp2[p] = true;
								break;
							}
						}

						for (int i = 0; i < peerAvg.size(); i++) {
							if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfChart.parse(peerAvg.get(i).getPeriod())))){
								String data = "-";
								Double tempData = peerAvg.get(i).getData();
								if(p==0){
									peerLatestData = tempData;
								}


								if(tempData!=null){
									String value = jasperReportService.applyFormattor(BigDecimal.valueOf(tempData), null, null, null);
									if(!tempData.equals("-")){
										value = value + unitToAppend;
									}
									data = jasperReportService.fontCheck(value);
								}

								tempStringPeerAvg[6-p] = data;
								pCheckPeerAvg[p] = true;
								break;
							}
						}
					}

					for (int i = 0; i < applicablePeriodList.size(); i++) {
						if(pCheck[i]==null || pCheck[i]==false){
							ReportPerformanceData reportPerformanceData = new ReportPerformanceData(companyName,null,sdfChart.format(sdf.parse(applicablePeriodList.get(i))));
							userList.add(reportPerformanceData);
						}

						if(pCheckComp1[i]==null || pCheckComp1[i]==false){
							ReportPerformanceData reportPerformanceData = new ReportPerformanceData(peerCompany1,null,sdfChart.format(sdf.parse(applicablePeriodList.get(i))));
							peerComp1.add(reportPerformanceData);
						}

						if(pCheckComp2[i]==null || pCheckComp2[i]==false){
							ReportPerformanceData reportPerformanceData = new ReportPerformanceData(peerCompany2,null,sdfChart.format(sdf.parse(applicablePeriodList.get(i))));
							peerComp2.add(reportPerformanceData);
						}

						if(pCheckPeerAvg[i]==null || pCheckPeerAvg[i]==false){
							ReportPerformanceData reportPerformanceData = new ReportPerformanceData(peerCompanyAvg,null,sdfChart.format(sdf.parse(applicablePeriodList.get(i))));
							peerAvg.add(reportPerformanceData);
						}
					}

					parameterCompanyData.add(new JasperDynamicModel(Arrays.asList(tempString)));
					parameterCompanyData.add(new JasperDynamicModel(Arrays.asList(tempStringCompany1)));
					parameterCompanyData.add(new JasperDynamicModel(Arrays.asList(tempStringCompany2)));
					parameterCompanyData.add(new JasperDynamicModel(Arrays.asList(tempStringPeerAvg)));
					allParameterData.add(parameterCompanyData);


					sortReportElement(userList);
					sortReportElement(peerComp1);
					sortReportElement(peerComp2);
					sortReportElement(peerAvg);

					List<ReportPerformanceData> tempList = new ArrayList<>();
					tempList.addAll(userList);
					tempList.addAll(peerComp1);
					tempList.addAll(peerComp2);
					tempList.addAll(peerAvg);

					chartDataList.add(tempList);
					Integer score = scoreMap.get(key);
					if(score==null || score==0){
						scoreList.add(LoanStatic.LOAN_REPORT_IMAGES+"not_available_data_info_image.png");
						//scoreList.add("");
					}else{
						scoreList.add(LoanStatic.LOAN_REPORT_IMAGES+"score"+score+".png");
					}


					String summary = "";
					summary = scoreCommentMap.get(key);
					if(summary==null || summary.equals("")){
						summary = scoreSummary(titleMap.get(key),sdfChart.format(sdf.parse(calculatedPeriodList.get(0))), userLatestData, peerLatestData).replaceAll("&", "&amp;");
					}
					/*if(summary.equalsIgnoreCase("N/A")){
						if(scoreCommentMap.get(key)!=null){
							summary = scoreCommentMap.get(key);
						}
					}*/


					chartSummaryList.add(summary);
				}
			}

			parametersList.put("scoreList", scoreList);
			parametersList.put("chartDataList", chartDataList);
			parametersList.put("chartTitleList", chartTitleList);
			parametersList.put("chartSummaryList", chartSummaryList);
			//////////////////////////////////////////
			parametersList.put("allParameterData", allParameterData);
			//////////////////////////////////////////

			List<ReportPerformanceData> debtSizingChart = new ArrayList<>();

			String financialTypeDebtSizing = CMStatic.DEBT_SIZING;
			List<UserModelDTO>	debtSizingList = userService.getUserDebtSizing(userId, financialTypeDebtSizing, "yearly", sdf.parse(companyInfo.getApplicableFromYear()), sdf.parse(companyInfo.getApplicableToYear()), currency);

			String debtTenor = "";
			String averageInterestRate = "";
			String reFinaciableDebt = "";
			Double borrowing = null;
			Double totalSustainableDebt = null;


			String debtCurrency= null;
			String debtUnit = null;
			
			Double debtScore = null;

			if(debtSizingList!=null){
				for (UserModelDTO userModelDTO : debtSizingList) {

					if(sdf.format(userModelDTO.getApplicablePeriod()).equals(applicablePeriodList.get(0))){
						if(debtTenor.equals("")){
							if(userModelDTO.getFieldName().equals(LoanStatic.DEBT_TENOR)){
								debtTenor = ""+(userModelDTO.getData()==null?"-":jasperReportService.applyFontAndFormattor(userModelDTO.getData(),userModelDTO.getUnit(),null,null));
							}
						}
						if(averageInterestRate.equals("")){
							if(userModelDTO.getFieldName().equals(LoanStatic.AVERAGE_INTEREST_COST)){
								averageInterestRate = ""+(userModelDTO.getData()==null?"-":jasperReportService.applyFontAndFormattor(userModelDTO.getData(),userModelDTO.getUnit(),null,null));

							}
						}
						if(reFinaciableDebt.equals("")){
							if(userModelDTO.getFieldName().equals(LoanStatic.REFINANCIABLE_DEBT)){
								reFinaciableDebt = ""+(userModelDTO.getData()==null?"-":jasperReportService.applyFontAndFormattor(userModelDTO.getData(),userModelDTO.getUnit(),null,null));
							}
						}

						if(borrowing==null){
							if(userModelDTO.getFieldName().equals(LoanStatic.BORROWING)){
								borrowing = userModelDTO.getData();
								if(debtCurrency==null){
									debtCurrency = userModelDTO.getCurrency();
								}
								if(debtUnit==null){
									debtUnit = userModelDTO.getUnit();
								}
							}
						}

						if(totalSustainableDebt==null){
							if(userModelDTO.getFieldName().equals(LoanStatic.TOTAL_SUSTAINABLE_DEBT)){
								totalSustainableDebt = userModelDTO.getData();
								if(debtCurrency==null){
									debtCurrency = userModelDTO.getCurrency();
								}
								if(debtUnit==null){
									debtUnit = userModelDTO.getUnit();
								}
							}
						}
						
						
						if(debtScore==null){
							if(userModelDTO.getFieldName().equals(LoanStatic.DEBT_SIZING_SCORE)){
								debtScore = userModelDTO.getData();
							}
						}
					}
				}
			}
			
			
			scoreMap.put(LoanStatic.DEBT_SIZING_SCORE, debtScore==null?null:debtScore.intValue());

			String borrowingSeries = annexturePeriodList.get(0) + " Borrowing";
			String totalSustainableDebtSeries = "Total Sustainable Debt";
			debtSizingChart.add(new ReportPerformanceData(borrowingSeries,borrowing,companyName));
			debtSizingChart.add(new ReportPerformanceData(totalSustainableDebtSeries,totalSustainableDebt,companyName));
			/*debtSizingChart.add(new ReportPerformanceData(borrowingSeries,15d,"Airtel"));
			debtSizingChart.add(new ReportPerformanceData(totalSustainableDebtSeries,15d,"Airtel"));
			debtSizingChart.add(new ReportPerformanceData(borrowingSeries,15d,"Voda"));
			debtSizingChart.add(new ReportPerformanceData(totalSustainableDebtSeries,5d,"Voda"));*/

			String debtChartTitle = "Debt Sizing Analysis " + jasperReportService.unitCurrencyAppender(debtCurrency, debtUnit) ;

			String debtSummary = "";
			if(totalSustainableDebt!=null && borrowing!=null){
				double difference = Math.abs(totalSustainableDebt - borrowing);
				if(borrowing > totalSustainableDebt){
					debtSummary = "The company's borrowings are more than its sustainable debt capacity by "+debtCurrency + " "+jasperReportService.applyFontAndFormattor(difference,null,null,null)+ " "+ (debtUnit==null?"":debtUnit) +" in "+annexturePeriodList.get(0);			
				}else if(totalSustainableDebt > borrowing){
					debtSummary = "The company's sustainable debt capacity is more than its current borrowings by "+debtCurrency + " " +jasperReportService.applyFontAndFormattor(difference,null,null,null)+ " "+ (debtUnit==null?"":debtUnit) +" in "+annexturePeriodList.get(0);
				}else{
					debtSummary = "The company's sustainable debt is same as its borrowings"+" in "+annexturePeriodList.get(0);;
				}					
			}else{
				debtSummary = "N/A";
			}

			/*if(debtSummary.equalsIgnoreCase("N/A")){
				if(scoreCommentMap.get(LoanStatic.CAPEX)!=null){
					debtSummary = scoreCommentMap.get(LoanStatic.CAPEX);
				}
			}*/

			parametersList.put("debtChartTitle", debtChartTitle);
			parametersList.put("debtSizingChart", debtSizingChart);
			parametersList.put("debtSummary", debtSummary);
			parametersList.put("debtTenor", debtTenor);
			parametersList.put("averageInterestRate", averageInterestRate);
			parametersList.put("reFinaciableDebt", reFinaciableDebt);

			////////////////////////////////////////////////

			String sensitivityChart1Title = "";
			String sensitivityChart2Title = "";

			List<ReportPerformanceData> sensitivityChart1Data = new ArrayList<>();
			List<ReportPerformanceData> sensitivityChart2Data = new ArrayList<>();

			String financialTypeSensitivity = CMStatic.DATA_SENSITIVITY;
			List<UserModelDTO> sensitivityData = null;
			try{
				sensitivityData = userService.getUserSensitivityAnalysis(userId, financialTypeSensitivity, "yearly", sdf.parse(companyInfo.getApplicableFromYear()), sdf.parse(companyInfo.getApplicableToYear()), currency);
			}catch(Exception e){
				throw e;
			}

			String chart2Unit = null;
			String chart2Currency =null;
			if(sensitivityData!=null){
				for (UserModelDTO userModelDTO : sensitivityData) {
					if(sdfYear.format(userModelDTO.getApplicablePeriod()).equals(annextureApplicablePeriodList.get(0)) || sdfYear.format(userModelDTO.getApplicablePeriod()).equals(annextureApplicablePeriodList.get(1)) || sdfYear.format(userModelDTO.getApplicablePeriod()).equals(annextureApplicablePeriodList.get(2))){
						if(LoanStatic.SENSITIVITY_CHART_1.contains(userModelDTO.getFieldName())){
							sensitivityChart1Data.add(new ReportPerformanceData(userModelDTO.getItemName(),userModelDTO.getData(),sdfYear.format(userModelDTO.getApplicablePeriod())));
						}
						if(LoanStatic.SENSITIVITY_CHART_2.contains(userModelDTO.getFieldName())){
							if(chart2Unit==null){
								chart2Unit = userModelDTO.getUnit();
							}
							if(chart2Currency==null){
								chart2Currency = userModelDTO.getCurrency();
							}
							sensitivityChart2Data.add(new ReportPerformanceData(userModelDTO.getItemName(),userModelDTO.getData(),sdfYear.format(userModelDTO.getApplicablePeriod())));
						}
					}
				}
			}

			sensitivityChart1Title = "Interest Rate to DSCR Sensitivity (x)";
			sensitivityChart2Title = "Interest Rate to Cash Flow Sesitivity "+jasperReportService.unitCurrencyAppender(chart2Currency, chart2Unit);

			sortReportElement(sensitivityChart1Data);
			sortReportElement(sensitivityChart2Data);

			parametersList.put("sensitivityChart1Title", sensitivityChart1Title);
			parametersList.put("sensitivityChart2Title", sensitivityChart2Title);
			parametersList.put("sensitivityChart1Data", sensitivityChart1Data);
			parametersList.put("sensitivityChart2Data", sensitivityChart2Data);

			//////////////////////////////////////////

			//String capexChartTitle = "Revenue / Gross Fixed Assets (%)" ;
			List<ReportPerformanceData> capexChartData = new ArrayList<>();
			List<ReportPerformanceData> capexChartDataUser = new ArrayList<>();
			List<ReportPerformanceData> capexChartDataCompany1 = new ArrayList<>();
			List<ReportPerformanceData> capexChartDataCompany2 = new ArrayList<>();
			List<ReportPerformanceData> capexChartDataPeer = new ArrayList<>();

			if(capexUser!=null){
				for (UserModelDTO userModelDTO : capexUser) {
					capexChartDataUser.add(new ReportPerformanceData(companyName,userModelDTO.getData(),sdfYear.format(userModelDTO.getApplicablePeriod())));
				}
			}
			if(capexCompany1!=null){
				for (IndustryFinancialDataDTO userModelDTO : capexCompany1) {
					capexChartDataCompany1.add(new ReportPerformanceData(peerCompany1==null?"Company1":peerCompany1,userModelDTO.getData(),sdfYear.format(userModelDTO.getPeriod())));
				}
			}
			if(capexCompany2!=null){
				for (IndustryFinancialDataDTO userModelDTO : capexCompany2) {
					capexChartDataCompany2.add(new ReportPerformanceData(peerCompany2==null?"Company2":peerCompany2,userModelDTO.getData(),sdfYear.format(userModelDTO.getPeriod())));
				}
			}
			if(capexPeer!=null){
				for (UserModelDTO userModelDTO : capexPeer) {
					capexChartDataPeer.add(new ReportPerformanceData(peerCompanyAvg,userModelDTO.getData(),sdfYear.format(userModelDTO.getApplicablePeriod())));
				}
			}

			Boolean[] pCheckUser = new Boolean[5];
			Boolean[] pCheckCompany1 = new Boolean[5];
			Boolean[] pCheckCompany2 = new Boolean[5];
			Boolean[] pCheckPeer = new Boolean[5];

			for (int p=0;p < applicablePeriodList.size();p++) {
				String period = sdfYear.format(sdf.parse(applicablePeriodList.get(p)));
				for (int i = 0; i < capexChartDataUser.size(); i++) {
					if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfYear.parse(capexChartDataUser.get(i).getPeriod())))){
						pCheckUser[p] = true;
						break;
					}
				}
				for (int i = 0; i < capexChartDataCompany1.size(); i++) {
					if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfYear.parse(capexChartDataCompany1.get(i).getPeriod())))){
						pCheckCompany1[p] = true;
						break;
					}
				}
				for (int i = 0; i < capexChartDataCompany2.size(); i++) {
					if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfYear.parse(capexChartDataCompany2.get(i).getPeriod())))){
						pCheckCompany2[p] = true;
						break;
					}
				}
				for (int i = 0; i < capexChartDataPeer.size(); i++) {
					if(sdft.format(sdfYear.parse(period)).equals(sdft.format(sdfYear.parse(capexChartDataPeer.get(i).getPeriod())))){
						pCheckPeer[p] = true;
						break;
					}
				}
			}

			for (int i = 0; i < applicablePeriodList.size(); i++) {
				if(pCheckUser[i]==null || pCheckUser[i]==false){
					ReportPerformanceData reportPerformanceData = new ReportPerformanceData(companyName,null,sdfYear.format(sdf.parse(applicablePeriodList.get(i))));
					capexChartDataUser.add(reportPerformanceData);
				}
				if(pCheckCompany1[i]==null || pCheckCompany1[i]==false){
					ReportPerformanceData reportPerformanceData = new ReportPerformanceData(peerCompany1==null?"Company1":peerCompany1,null,sdfYear.format(sdf.parse(applicablePeriodList.get(i))));
					capexChartDataCompany1.add(reportPerformanceData);
				}
				if(pCheckCompany2[i]==null || pCheckCompany2[i]==false){
					ReportPerformanceData reportPerformanceData = new ReportPerformanceData(peerCompany2==null?"Company2":peerCompany2,null,sdfYear.format(sdf.parse(applicablePeriodList.get(i))));
					capexChartDataCompany2.add(reportPerformanceData);
				}
				if(pCheckPeer[i]==null || pCheckPeer[i]==false){
					ReportPerformanceData reportPerformanceData = new ReportPerformanceData(peerCompanyAvg,null,sdfYear.format(sdf.parse(applicablePeriodList.get(i))));
					capexChartDataPeer.add(reportPerformanceData);
				}
			}

			capexChartData.addAll(capexChartDataUser);
			capexChartData.addAll(capexChartDataCompany1);
			capexChartData.addAll(capexChartDataCompany2);
			capexChartData.addAll(capexChartDataPeer);

			sortReportElement(capexChartData);

			LinkedHashMap<String, ReportAnnextureModel> tempMap1 = getFinancialDataMap(capexUser, annextureApplicablePeriodList, null);
			LinkedHashMap<String, ReportAnnextureModel> tempMap2 = getFinancialDataMapIndustry(capexCompany1, annextureApplicablePeriodList, null);
			LinkedHashMap<String, ReportAnnextureModel> tempMap3 = getFinancialDataMapIndustry(capexCompany2, annextureApplicablePeriodList, null);
			LinkedHashMap<String, ReportAnnextureModel> tempMap4 = getFinancialDataMap(capexPeer, annextureApplicablePeriodList, null);

			ReportAnnextureModel rm1 = tempMap1.get(LoanStatic.CAPEX);
			ReportAnnextureModel rm2 = tempMap2.get(LoanStatic.CAPEX);
			ReportAnnextureModel rm3 = tempMap3.get(LoanStatic.CAPEX);
			ReportAnnextureModel rm4 = tempMap4.get(LoanStatic.CAPEX);

			List<JasperDynamicModel> capexTableData = new ArrayList<>();
			String capexUnit = "";
			if(rm1==null){
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"1.jpg",companyName.replaceAll("&", "&amp;"),"-","-","-","-","-")));
			}else{
				capexUnit = rm1.getUnit();
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"1.jpg",companyName.replaceAll("&", "&amp;"),jasperReportService.applyFontAndFormattor(rm1.getSlot5()==null?null:Double.valueOf(rm1.getSlot5()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm1.getSlot4()==null?null:Double.valueOf(rm1.getSlot4()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm1.getSlot3()==null?null:Double.valueOf(rm1.getSlot3()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm1.getSlot2()==null?null:Double.valueOf(rm1.getSlot2()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm1.getSlot1()==null?null:Double.valueOf(rm1.getSlot1()),capexUnit,"APPEND",null))));
			}

			if(rm2==null){
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"2.jpg",peerCompany1.replaceAll("&", "&amp;"),"-","-","-","-","-")));
			}else{
				capexUnit = rm1.getUnit();
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"2.jpg",peerCompany1.replaceAll("&", "&amp;"),jasperReportService.applyFontAndFormattor(rm2.getSlot5()==null?null:Double.valueOf(rm2.getSlot5()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm2.getSlot4()==null?null:Double.valueOf(rm2.getSlot4()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm2.getSlot3()==null?null:Double.valueOf(rm2.getSlot3()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm2.getSlot2()==null?null:Double.valueOf(rm2.getSlot2()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm2.getSlot1()==null?null:Double.valueOf(rm2.getSlot1()),capexUnit,"APPEND",null))));
			}

			if(rm3==null){
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"3.jpg",peerCompany2.replaceAll("&", "&amp;"),"-","-","-","-","-")));
			}else{
				capexUnit = rm1.getUnit();
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"3.jpg",peerCompany2.replaceAll("&", "&amp;"),jasperReportService.applyFontAndFormattor(rm3.getSlot5()==null?null:Double.valueOf(rm3.getSlot5()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm3.getSlot4()==null?null:Double.valueOf(rm3.getSlot4()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm3.getSlot3()==null?null:Double.valueOf(rm3.getSlot3()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm3.getSlot2()==null?null:Double.valueOf(rm3.getSlot2()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm3.getSlot1()==null?null:Double.valueOf(rm3.getSlot1()),capexUnit,"APPEND",null))));
			}

			if(rm4==null){
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"4.jpg","Peer Avg","-","-","-","-","-")));
			}else{
				capexUnit = rm1.getUnit();
				capexTableData.add(new JasperDynamicModel(Arrays.asList(LoanStatic.LOAN_REPORT_IMAGES+"4.jpg","Peer Avg",jasperReportService.applyFontAndFormattor(rm4.getSlot5()==null?null:Double.valueOf(rm4.getSlot5()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm4.getSlot4()==null?null:Double.valueOf(rm4.getSlot4()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm4.getSlot3()==null?null:Double.valueOf(rm4.getSlot3()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm4.getSlot2()==null?null:Double.valueOf(rm4.getSlot2()),capexUnit,"APPEND",null),jasperReportService.applyFontAndFormattor(rm4.getSlot1()==null?null:Double.valueOf(rm4.getSlot1()),capexUnit,"APPEND",null))));
			}


			String capexSummary = "" ;
			String capexChartTitle = "Revenue / Gross Fixed Assets ("+capexUnit+")" ;
			if(rm1!=null && rm4!=null){
				capexSummary = scoreCommentMap.get(LoanStatic.CAPEX);
				if(capexSummary==null || capexSummary.equals("")){
					capexSummary = scoreSummary(capexChartTitle, calculatedPeriodList.get(0), rm1.getSlot1()==null?null:Double.valueOf(rm1.getSlot1()), rm4.getSlot1()==null?null:Double.valueOf(rm4.getSlot1())).replaceAll("&", "&amp;");
				}
				/*if(capexSummary.equalsIgnoreCase("N/A")){
					if(scoreCommentMap.get(LoanStatic.CAPEX)!=null){
						capexSummary = scoreCommentMap.get(LoanStatic.CAPEX);
					}
				}
				 */
			}
			parametersList.put("capexChartTitle", capexChartTitle);
			parametersList.put("capexChartData", capexChartData);
			parametersList.put("capexTableData", capexTableData);
			parametersList.put("capexSummary", capexSummary);

			List<ReportAnnextureModel> debtTrapData = new ArrayList<>();
			List<UserModelDTO> financialData = null;
			try{
				financialData = userService.getUserDebtTrap(userId, CMStatic.DTA, "yearly", sdf.parse(companyInfo.getApplicableFromYear()), sdf.parse(companyInfo.getApplicableToYear()), currency);
			}catch(Exception e){
				throw e;
			}

			Double operatingCashFlow = null;
			Double equityInfusion = null;
			Double debtServicing = null;
			if(financialData!=null){
				LinkedHashMap<String, ReportAnnextureModel> myMapDebtTrap = getFinancialDataMap(financialData, annextureApplicablePeriodList,LoanStatic.APPENDIX);
				LinkedHashMap<String, ReportAnnextureModel> myMapDebtTrapOrig = getFinancialDataMap(financialData, annextureApplicablePeriodList,null);
				for (String key  : myMapDebtTrap.keySet()) {
					ReportAnnextureModel ramOrig =	myMapDebtTrapOrig.get(key);
					if(key.equals(LoanStatic.OPERATING_CASH_FLOW)){
						String OpCaFlow = ramOrig.getSlot1();
						if(OpCaFlow!=null){
							operatingCashFlow = ramOrig.getSlot1()!=null?Double.valueOf(OpCaFlow.replaceAll(",","").replace("(", "-").replace(")","")):null;
						}
					}
					if(key.equals(LoanStatic.EQUITY_INFUSION)){
						String eqInfus = ramOrig.getSlot1();
						if(eqInfus!=null){
							equityInfusion = ramOrig.getSlot1()!=null?Double.valueOf(eqInfus.replaceAll(",","").replace("(", "-").replace(")","")):null;
						}
					}
					if(key.equals(LoanStatic.DEBT_SERVICING_AMOUNT)){
						String dtServig = ramOrig.getSlot1();
						if(dtServig!=null){
							debtServicing = ramOrig.getSlot1()!=null?Double.valueOf(dtServig.replaceAll(",","").replace("(", "-").replace(")","")):null;
						}
					}
					debtTrapData.add(myMapDebtTrap.get(key));
				}
			}

			String debtTrapSummary = "N/A";
			_log.info("operatingCashFlow : "+operatingCashFlow+"\t equityInfusion : "+equityInfusion+"\t debtServicing : "+debtServicing);
			if(operatingCashFlow!=null && equityInfusion!=null && debtServicing!=null){
				if((operatingCashFlow + equityInfusion)/debtServicing > 100){
					debtTrapSummary = "The company is not reliant on debt funding to service its debt repayment obligation in "+ annexturePeriodList.get(0) ;
				}else{
					debtTrapSummary = "The company is reliant on debt funding to service its debt repayment obligation in "+ annexturePeriodList.get(0) ;
				}
			}
			parametersList.put("debtTrapData", debtTrapData);
			parametersList.put("debtTrapSummary", debtTrapSummary);

			////////////////////////////////l////////

			int performanceAndActivityratiosScore = calculateTotalScoreOfASection(LoanStatic.PERFORMANCE_AND_ACTIVITY_RATIOS, scoreMap);
			int profitabilityAndReturnRatiosScore = calculateTotalScoreOfASection(LoanStatic.PROFITABILITY_AND_RETURN_RATIOS, scoreMap);
			List<String> tempLeverageList = new ArrayList<>();
			tempLeverageList.addAll(LoanStatic.LEVERAGE_RATIO);
			_log.info(tempLeverageList);
			tempLeverageList.add(LoanStatic.DEBT_SIZING_SCORE);
			_log.info(tempLeverageList);
			
			int leverageRatiosScore = calculateTotalScoreOfASection(tempLeverageList, scoreMap);
			int liquidityRatiosScore = calculateTotalScoreOfASection(LoanStatic.LIQUIDITY_RATIO, scoreMap);
			int debtCoverageRatiosScore = calculateTotalScoreOfASection(LoanStatic.DEBT_COVERAGE_RATIO, scoreMap);
			int comparativeFinancialScoreInt = performanceAndActivityratiosScore+profitabilityAndReturnRatiosScore+leverageRatiosScore+liquidityRatiosScore+debtCoverageRatiosScore;

			String comparativeFinancialScore = getFontColor(comparativeFinancialScoreInt)+jasperReportService.addColor("/300", "#203864");
			String performanceAndActivityratios = getParticularColor(performanceAndActivityratiosScore,80)+jasperReportService.addColor("/80", "#203864");
			String profitabilityAndReturnRatios = getParticularColor(profitabilityAndReturnRatiosScore,70)+jasperReportService.addColor("/70", "#203864");
			String leverageRatios = getParticularColor(leverageRatiosScore,70)+jasperReportService.addColor("/80", "#203864");
			String liquidityRatios = getParticularColor(liquidityRatiosScore,40)+jasperReportService.addColor("/40", "#203864");
			String debtCoverageRatios = getParticularColor(debtCoverageRatiosScore,30)+jasperReportService.addColor("/30", "#203864");

			String comparativeFinancialHealthLevel = getScoreValueAndColor(comparativeFinancialScoreInt);
			parametersList.put("comparativeFinancialHealthLevel", comparativeFinancialHealthLevel);
			parametersList.put("comparativeFinancialScore", comparativeFinancialScore);
			parametersList.put("performanceAndActivityratios", performanceAndActivityratios);
			parametersList.put("profitabilityAndReturnRatios", profitabilityAndReturnRatios);
			parametersList.put("leverageRatios", leverageRatios);
			parametersList.put("liquidityRatios", liquidityRatios);
			parametersList.put("debtCoverageRatios", debtCoverageRatios);

			//////////////////////////////////

			List<ReportAnnextureModel> annextureDataProfitLoss = new ArrayList<>();
			List<ReportAnnextureModel> annextureDataBalanceSheet = new ArrayList<>();
			List<ReportAnnextureModel> annextureDataCashFlow = new ArrayList<>();
			List<ReportAnnextureModel> annextureDataRatios = new ArrayList<>();

			////////////////////////

			sortReportElementDisplayOrder(userListBS);
			sortReportElementDisplayOrder(userListCF);
			sortReportElementDisplayOrder(userListIS);
			sortReportElementDisplayOrder(userListFR);

			String annextureCurrency = userListALL.get(0).getCurrency();
			String annextureUnit = userListALL.get(0).getUnit();

			if(userListBS!=null){
				LinkedHashMap<String, ReportAnnextureModel> myMapBS = getFinancialDataMap(userListBS, annextureApplicablePeriodList,LoanStatic.APPENDIX);
				for (String key  : myMapBS.keySet()) {
					ReportAnnextureModel reportAnnextureModel = myMapBS.get(key);
					if(reportAnnextureModel.getSection()!=null && !reportAnnextureModel.getSection().equals("")){
						ReportAnnextureModel reportAnnextureModelTemp = new ReportAnnextureModel();
						reportAnnextureModelTemp.setSubMetric(jasperReportService.createBold(reportAnnextureModel.getSection()));
						reportAnnextureModelTemp.setSlot1("");
						reportAnnextureModelTemp.setSlot2("");
						reportAnnextureModelTemp.setSlot3("");
						reportAnnextureModelTemp.setSlot4("");
						reportAnnextureModelTemp.setSlot5("");
						reportAnnextureModelTemp.setSlot6("");
						reportAnnextureModelTemp.setUnit("");
						annextureDataBalanceSheet.add(reportAnnextureModelTemp);
					}
					annextureDataBalanceSheet.add(myMapBS.get(key));
				}
			}

			if(userListCF!=null){
				LinkedHashMap<String, ReportAnnextureModel> myMapCF = getFinancialDataMap(userListCF, annextureApplicablePeriodList,LoanStatic.APPENDIX);		
				for (String key  : myMapCF.keySet()) {
					ReportAnnextureModel reportAnnextureModel = myMapCF.get(key);
					if(reportAnnextureModel.getSection()!=null && !reportAnnextureModel.getSection().equals("")){
						ReportAnnextureModel reportAnnextureModelTemp = new ReportAnnextureModel();
						reportAnnextureModelTemp.setSubMetric(jasperReportService.createBold(reportAnnextureModel.getSection()));
						reportAnnextureModelTemp.setSlot1("");
						reportAnnextureModelTemp.setSlot2("");
						reportAnnextureModelTemp.setSlot3("");
						reportAnnextureModelTemp.setSlot4("");
						reportAnnextureModelTemp.setSlot5("");
						reportAnnextureModelTemp.setSlot6("");
						reportAnnextureModelTemp.setUnit("");
						annextureDataCashFlow.add(reportAnnextureModelTemp);
					}
					annextureDataCashFlow.add(myMapCF.get(key));
				}
			}

			if(userListIS!=null){
				LinkedHashMap<String, ReportAnnextureModel> myMapIS = getFinancialDataMap(userListIS, annextureApplicablePeriodList,LoanStatic.APPENDIX);
				for (String key  : myMapIS.keySet()) {
					ReportAnnextureModel reportAnnextureModel = myMapIS.get(key);
					if(reportAnnextureModel.getSection()!=null && !reportAnnextureModel.getSection().equals("")){
						ReportAnnextureModel reportAnnextureModelTemp = new ReportAnnextureModel();
						reportAnnextureModelTemp.setSubMetric(jasperReportService.createBold(reportAnnextureModel.getSection()));
						reportAnnextureModelTemp.setSlot1("");
						reportAnnextureModelTemp.setSlot2("");
						reportAnnextureModelTemp.setSlot3("");
						reportAnnextureModelTemp.setSlot4("");
						reportAnnextureModelTemp.setSlot5("");
						reportAnnextureModelTemp.setSlot6("");
						reportAnnextureModelTemp.setUnit("");
						annextureDataProfitLoss.add(reportAnnextureModelTemp);
					}
					annextureDataProfitLoss.add(myMapIS.get(key));
				}
			}


			if(userListFR!=null){
				LinkedHashMap<String, ReportAnnextureModel> myMapFR = getFinancialDataMap(userListFR, annextureApplicablePeriodList,LoanStatic.APPENDIX);
				for (String key  : myMapFR.keySet()) {
					ReportAnnextureModel reportAnnextureModel = myMapFR.get(key);
					if(reportAnnextureModel.getSection()!=null && !reportAnnextureModel.getSection().equals("")){
						ReportAnnextureModel reportAnnextureModelTemp = new ReportAnnextureModel();
						reportAnnextureModelTemp.setSubMetric(jasperReportService.createBold(reportAnnextureModel.getSection()));
						reportAnnextureModelTemp.setSlot1("");
						reportAnnextureModelTemp.setSlot2("");
						reportAnnextureModelTemp.setSlot3("");
						reportAnnextureModelTemp.setSlot4("");
						reportAnnextureModelTemp.setSlot5("");
						reportAnnextureModelTemp.setSlot6("");
						reportAnnextureModelTemp.setUnit("");
						annextureDataRatios.add(reportAnnextureModelTemp);
					}
					annextureDataRatios.add(myMapFR.get(key));
				}
			}

			_log.info("Ratios ::: " + annextureDataRatios);

			_log.info("The diff is :::: " + diff);
			String annextureType = "Annexture_Table_"+diff+"_data.jasper";
			String chartTableType = "Chart_Table_"+diff+".jasper";
			String ratioTableType = "Ratio_Table_"+diff+".jasper";
			String debtTrapTableType = "Debt_Trap_"+diff+"_Table.jasper";

			parametersList.put("ratioTableType", ratioTableType);
			parametersList.put("chartTableType", chartTableType);
			parametersList.put("debtTrapTableType", debtTrapTableType);
			parametersList.put("annextureType", annextureType);
			parametersList.put("annextureDataProfitLoss", annextureDataProfitLoss);
			parametersList.put("annextureDataBalanceSheet", annextureDataBalanceSheet);
			parametersList.put("annextureDataCashFlow", annextureDataCashFlow);
			parametersList.put("annextureDataRatios", annextureDataRatios);

			parametersList.put("annextureParticulars", "Particulars " + jasperReportService.unitCurrencyAppender(annextureCurrency, annextureUnit));
			parametersList.put("annextureParticularsRatios", "Particulars");

			/////////////////////////////
			List<ReportAnnextureModel> peerComposition = new ArrayList<>();

			Collections.reverse(userPeerCompanyList);
			TreeMap<String,List<String>> peerCompanyInfo = new TreeMap<String,List<String>>();
			Boolean[] check = new Boolean[5];
			int rowSize = 0;
			int maxLength = 0;
			if(userPeerCompanyList!=null){
				for(UserPeerInfoDTO peer : userPeerCompanyList) {
					List<CompanyDTO> companies = capitalMarketService.getCMExchangeCompanies(null,null, null, peer.getPeerCompanyId(), false,null, "PUB");
					if(companies!=null && companies.size()>0) {
						for(CompanyDTO company : companies) {
							String val = company.getName()+"("+company.getCountryCode()+")";
							if(val!=null){
								if(val.length() > maxLength){
									maxLength = val.length();
								}
							}
						}
					}
				}


				_log.info(userPeerCompanyList);

				for(UserPeerInfoDTO peer : userPeerCompanyList) {
					List<CompanyDTO> companies = capitalMarketService.getCMExchangeCompanies(null,null, null, peer.getPeerCompanyId(), false,null, "PUB");
					List<String> companyNameList = new ArrayList<String>();
					if(companies!=null && companies.size()>0) {
						for(CompanyDTO company : companies) {
							int fontsize = 6;
							if(diff <= 3 ){
								if(maxLength >= 51){
									fontsize = 7;
								}else if(maxLength >= 61){
									fontsize = 6;
								}else{
									fontsize = 7;
								}
							}if(diff>=4){
								if(maxLength >= 51){
									fontsize = 5;
								}else if(maxLength >= 61){
									fontsize = 4;
								}else{
									fontsize = 6;
								}
							}

							String val = jasperReportService.fontCheck(company.getName()+"("+company.getCountryCode()+")",fontsize);
							companyNameList.add(val);
						}
						if(companies.size()>rowSize) {
							rowSize = companies.size(); 
						}
					}
					peerCompanyInfo.put(sdfYear.format(peer.getApplicableDate()), companyNameList);
				}
			}

			_log.info(peerCompanyInfo);
			if(peerCompanyInfo!=null){
				for (String key : peerCompanyInfo.keySet()) {
					for (int i=0;i<applicablePeriodList.size();i++) {
						if(key.equals(sdfYear.format(sdf.parse(applicablePeriodList.get(i))))){
							check[i]= true;
						}
					}
				}
			}
			for (int i=0;i<applicablePeriodList.size();i++) {
				if(check!=null && (check[i]==null || check[i]==false)){
					peerCompanyInfo.put(sdfYear.format(sdf.parse(applicablePeriodList.get(i))), new ArrayList<>());
				}
			}


			Set<String> keys = peerCompanyInfo.keySet();
			List<String> keyArray = new ArrayList<String>(keys);

			_log.info(keys);
			_log.info(keyArray);


			//for(int i=0;i<rowSize;i++) {
			ReportAnnextureModel rAMPeerComposition = new ReportAnnextureModel();
			List<String> comList = peerCompanyInfo.get(keyArray.get(keys.size()-1));
			_log.info(comList);
			int j=0;
			for(int p = 0;p<comList.size();p++) {
				//for(int j = 0;j<applicablePeriodList.size();j++) {
				//List<String> comList = peerCompanyInfo.get(keyArray.get(j));
				String data = "-";
				data = comList.get(p);
				/*if(comList!=null) {
						//if(i<=comList.size()){
							data = comList.get(p);
						}else{
							data = "-";
						}
					}*/

				if(j==0 || j==3){
					rAMPeerComposition = new ReportAnnextureModel();
					j=0;
				}
				if(j==0){
					rAMPeerComposition.setSlot1(data);
				}
				if(j==1){
					rAMPeerComposition.setSlot2(data);
				}
				if(j==2){
					rAMPeerComposition.setSlot3(data);
				}
				if(j==2 || p==(comList.size()-1)){
					peerComposition.add(rAMPeerComposition);
				}
				j++;



				/*if(j==3){
						rAMPeerComposition.setSlot4(data);
					}
					if(j==4){
						rAMPeerComposition.setSlot5(data);
					}*/
			}

			//	}

			String peerCompositionType = LoanStatic.LOAN_JASPER_PATH+"Peer_Composition_Data_"+diff+"_table.jasper";
			if("portfolio".equalsIgnoreCase(companyInfo.getRequestType())){
				peerCompositionType = LoanStatic.PORTFOLIO_LOAN_JASPER_PATH+"Peer_Composition_Data_"+diff+"_table.jasper";
			}
			parametersList.put("peerComposition", peerComposition);
			parametersList.put("peerCompositionType", peerCompositionType);
			_log.info(peerCompositionType);
			return parametersList;
		}catch(Exception e){
			_log.error(e.getMessage(),e);
			throw e;
		}

	}

	@SuppressWarnings("unused")
	private void setValueInSlot(int i, String string, ReportAnnextureModel rAMPeerComposition) {
		// TODO Auto-generated method stub

	}

	private List<UserPeerInfoDTO> getPeerCompanyList(String userId, String applicableToDate, String applicableFromoDate) {
		return televisoryUserRepository.getPeerCompanyList(userId,applicableToDate,applicableFromoDate);
	}

	String getFontColor(int score){

		String customVal = "";
		if(score>=240 && score <=300){
			customVal = "<font color='#267451'>" + score + "</font>";
		}else if(score>=180 && score <=239){
			customVal = "<font color='#6ea92d'>" + score + "</font>";
		}else if(score>=120 && score <=179){
			customVal = "<font color='#ffc000'>" + score + "</font>";
		}else if(score>=60 && score <=119){
			customVal = "<font color='#ed7d31'>" + score + "</font>";
		}else if(score>=0 && score <=59){
			customVal = "<font color='#ff0000'>" + score + "</font>";
		}else{
			return ""+score;
		}
		return customVal;
	}


	String getParticularColor(int score,int maxScore){

		String customVal = "";
		double percent = (double)(score*100)/maxScore;

		if(percent>=0 && percent<=20){
			customVal = "<font color='#ff0000'>" + score + "</font>";
		}else if(percent>20 && percent<=40){
			customVal = "<font color='#ed7d31'>" + score + "</font>";
		}else if(percent>40 && percent<=60){
			customVal = "<font color='#ffc000'>" + score + "</font>";
		}else if(percent>60 && percent<=80){
			customVal = "<font color='#6ea92d'>" + score + "</font>";
		}else if(percent>80 && percent<=100){
			customVal = "<font color='#267451'>" + score + "</font>";
		}
		return customVal;
	}



	String getScoreValueAndColor(int score){

		String customVal = "";
		if(score>=240 && score <=300){
			customVal = "<font color='#267451'>" + "Superior".replace("&", "&amp;") + "</font>";
		}else if(score>=180 && score <=239){
			customVal = "<font color='#6ea92d'>" + "Above Average".replace("&", "&amp;") + "</font>";
		}else if(score>=120 && score <=179){
			customVal = "<font color='#ffc000'>" + "Average".replace("&", "&amp;") + "</font>";
		}else if(score>=60 && score <=119){
			customVal = "<font color='#ed7d31'>" + "Below Average".replace("&", "&amp;") + "</font>";
		}else if(score>=0 && score <=59){
			customVal = "<font color='#ff0000'>" + "Inferior".replace("&", "&amp;") + "</font>";
		}
		return customVal;
	}

	String getFontSize(String value){
		int length = value.length();
		String customVal = "";
		if(length>=240 && length <=300){
			customVal = "<font size='6'>" + value.replace("&", "&amp;") + "</font>";
		}else if(length>=180 && length <=239){
			customVal = "<font size='6'>" + value.replace("&", "&amp;") + "</font>";
		}else if(length>=120 && length <=179){
			customVal = "<font size='6'>" + value.replace("&", "&amp;") + "</font>";
		}else if(length>=60 && length <=119){
			customVal = "<font size='6'>" + value.replace("&", "&amp;") + "</font>";
		}else if(length>=0 && length <=59){
			customVal = "<font size='6'>" + value.replace("&", "&amp;") + "</font>";
		}else{
			return ""+length;
		}
		return customVal;
	}




	LinkedHashMap<String, ReportAnnextureModel> getFinancialDataMap(List<UserModelDTO> userList , List<String> annextureApplicablePeriodList,String formattingType){
		LinkedHashMap<String, ReportAnnextureModel> myMap = new LinkedHashMap<>();	

		for (UserModelDTO userModelDTO : userList) {
			myMap.put(userModelDTO.getFieldName(), new ReportAnnextureModel());
		}

		Boolean[] pCheck = new Boolean[5];

		if(userList!=null){
			for (UserModelDTO userModelDTO : userList) {
				ReportAnnextureModel reportAnnextureModel = myMap.get(userModelDTO.getFieldName());
				for (int j = 0; j < annextureApplicablePeriodList.size(); j++) {
					if(reportAnnextureModel.getMetric()==null){
						reportAnnextureModel.setMetric(userModelDTO.getDepthLevel());
					}
					if(reportAnnextureModel.getCurrency()==null){
						reportAnnextureModel.setCurrency(userModelDTO.getCurrency());
					}
					if(reportAnnextureModel.getUnit()==null){
						String curr = null;
						String unit = null;
						if(null!=userModelDTO.getCurrency() && !userModelDTO.getCurrency().equals("") ){
							curr = userModelDTO.getCurrency();
						}
						if(null!=userModelDTO.getUnit() && !userModelDTO.getUnit().equals("") ){
							unit = userModelDTO.getUnit();
						}
						reportAnnextureModel.setUnit(jasperReportService.getUnitAndCurrency(curr, unit));
					}
					if(reportAnnextureModel.getSubMetric()==null){
						if(LoanStatic.FINANCIAL_SNAPSHOT_MODULE.equals(formattingType)){
							int length = (userModelDTO.getDisplayName()+" "+jasperReportService.unitCurrencyAppender(userModelDTO.getCurrency(), userModelDTO.getUnit())).length();
							if(length > 32){
								reportAnnextureModel.setSubMetric(jasperReportService.fontCheck(userModelDTO.getDisplayName()+" "+jasperReportService.unitCurrencyAppender(userModelDTO.getCurrency(), userModelDTO.getUnit()),9));
							}else{
								reportAnnextureModel.setSubMetric(userModelDTO.getDisplayName()+" "+jasperReportService.unitCurrencyAppender(userModelDTO.getCurrency(), userModelDTO.getUnit()));
							}
						}else{
							if(userModelDTO.getDepthLevel()!=null && userModelDTO.getDepthLevel().equals("L0")){
								reportAnnextureModel.setSubMetric(jasperReportService.createBold(userModelDTO.getDisplayName()));
							}else{
								reportAnnextureModel.setSubMetric(userModelDTO.getDisplayName().replaceAll("&", "&amp;"));
							}
						}
					}

					if(reportAnnextureModel.getSection()==null){
						reportAnnextureModel.setSection(userModelDTO.getSection());
					}

					SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
					String tempPeriod = annextureApplicablePeriodList.get(j);
					if(tempPeriod.equals(sdfYear.format(userModelDTO.getApplicablePeriod()))){
						String data = null;
						if(LoanStatic.APPENDIX.equalsIgnoreCase(formattingType)){
							data = "-";
						}
						if(userModelDTO.getData()!=null){
							if(LoanStatic.APPENDIX.equalsIgnoreCase(formattingType)){
								data = jasperReportService.applyFontAndFormattor(userModelDTO.getData(),userModelDTO.getUnit(),null,null);
							}else{
								data = ""+userModelDTO.getData();
							}
						}
						pCheck[j] = true;

						if(j==0){
							reportAnnextureModel.setSlot1(data);
						}
						if(j==1){
							reportAnnextureModel.setSlot2(data);
						}
						if(j==2){
							reportAnnextureModel.setSlot3(data);
						}
						if(j==3){
							reportAnnextureModel.setSlot4(data);
						}
						if(j==4){
							reportAnnextureModel.setSlot5(data);
						}	

					}
				}


				for (int j = 0; j < annextureApplicablePeriodList.size(); j++) {
					if(pCheck[j]==null || pCheck[j]==false){
						String data = null;
						if(LoanStatic.APPENDIX.equalsIgnoreCase(formattingType)){
							data = "-";
						}else{
							data = null;
						}
						if(j==0){
							reportAnnextureModel.setSlot1(data);
						}
						if(j==1){
							reportAnnextureModel.setSlot2(data);
						}
						if(j==2){
							reportAnnextureModel.setSlot3(data);
						}
						if(j==3){
							reportAnnextureModel.setSlot4(data);
						}
						if(j==4){
							reportAnnextureModel.setSlot5(data);
						}
					}
				}
			}
		}
		return myMap;
	}


	LinkedHashMap<String, ReportAnnextureModel> getFinancialDataMapIndustry(List<IndustryFinancialDataDTO> userList , List<String> annextureApplicablePeriodList,String formattingType){
		LinkedHashMap<String, ReportAnnextureModel> myMap = new LinkedHashMap<>();	

		for (IndustryFinancialDataDTO userModelDTO : userList) {
			myMap.put(userModelDTO.getFieldName(), new ReportAnnextureModel());
		}

		Boolean[] pCheck = new Boolean[5];

		if(userList!=null){
			for (IndustryFinancialDataDTO userModelDTO : userList) {
				ReportAnnextureModel reportAnnextureModel = myMap.get(userModelDTO.getFieldName());
				for (int j = 0; j < annextureApplicablePeriodList.size(); j++) {
					if(reportAnnextureModel.getSubMetric()==null){
						//reportAnnextureModel.setSubMetric(userModelDTO.getItemName()!=null?userModelDTO.getItemName().replaceAll("&", "&amp;"):"-");
						if(LoanStatic.FINANCIAL_SNAPSHOT_MODULE.equals(formattingType)){
							reportAnnextureModel.setSubMetric(userModelDTO.getItemName()+" "+jasperReportService.unitCurrencyAppender(userModelDTO.getCurrency(), userModelDTO.getUnit()).replaceAll("&", "&amp;"));
						}else{
							if(userModelDTO.getDepthLevel()!=null && userModelDTO.getDepthLevel().equals("L0")){
								reportAnnextureModel.setSubMetric(jasperReportService.createBold(userModelDTO.getItemName()));
							}else{
								reportAnnextureModel.setSubMetric(userModelDTO.getItemName().replaceAll("&", "&amp;"));
							}
						}
					}
					if(reportAnnextureModel.getMetric()==null){
						reportAnnextureModel.setMetric(userModelDTO.getDepthLevel());
					}
					if(reportAnnextureModel.getCurrency()==null){
						reportAnnextureModel.setCurrency(userModelDTO.getCurrency());
					}
					if(reportAnnextureModel.getUnit()==null){
						String curr = null;
						String unit = null;
						if(null!=userModelDTO.getCurrency() && !userModelDTO.getCurrency().equals("") ){
							curr = userModelDTO.getCurrency();
						}
						if(null!=userModelDTO.getUnit() && !userModelDTO.getUnit().equals("") ){
							unit = userModelDTO.getUnit();
						}
						reportAnnextureModel.setUnit(jasperReportService.getUnitAndCurrency(curr, unit));
					}

					SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
					String tempPeriod = annextureApplicablePeriodList.get(j);
					if(tempPeriod.equals(sdfYear.format(userModelDTO.getPeriod()))){
						String data = null;
						if(LoanStatic.APPENDIX.equalsIgnoreCase(formattingType)){
							data = "-";
						}
						if(userModelDTO.getData()!=null){
							if(LoanStatic.APPENDIX.equalsIgnoreCase(formattingType)){
								data = jasperReportService.applyFontAndFormattor(userModelDTO.getData(),userModelDTO.getUnit(),null,null);
							}else{
								data = ""+userModelDTO.getData();
							}
						}
						pCheck[j] = true;
						if(j==0){
							reportAnnextureModel.setSlot1(data);
						}
						if(j==1){
							reportAnnextureModel.setSlot2(data);
						}
						if(j==2){
							reportAnnextureModel.setSlot3(data);
						}
						if(j==3){
							reportAnnextureModel.setSlot4(data);
						}
						if(j==4){
							reportAnnextureModel.setSlot5(data);
						}	

					}
				}

				for (int j = 0; j < annextureApplicablePeriodList.size(); j++) {
					if(pCheck[j]==null || pCheck[j]==false){
						String data = null;
						if(LoanStatic.APPENDIX.equalsIgnoreCase(formattingType)){
							data = "-";
						}
						if(j==0){
							reportAnnextureModel.setSlot1(data);
						}
						if(j==1){
							reportAnnextureModel.setSlot2(data);
						}
						if(j==2){
							reportAnnextureModel.setSlot3(data);
						}
						if(j==3){
							reportAnnextureModel.setSlot4(data);
						}
						if(j==4){
							reportAnnextureModel.setSlot5(data);
						}
					}
				}
			}
		}
		return myMap;
	}




	public String scoreSummary(String title, String period,Double dataUser , Double dataPeer){
		String summary = "N/A";
		try{
			String higOrLow ="lower";
			if(dataPeer!=null && dataUser!=null){
				Double percent = null;
				String unitToAppend = "%";
				String unit = "%";
				if(dataUser==0d && dataPeer==0d){
					summary = "N/A";
				}else if(dataUser==0d){
					summary = "N/A";
				}else{
					percent = percentageChange(dataUser, dataPeer);
				}

				if(title.contains("(%)")){
					unitToAppend = "bps";
					unit = "bps";
					percent = (dataUser - dataPeer)*100;
				}else if(title.contains("(days)")){
					unitToAppend = "days";
					percent = dataUser - dataPeer;
				}else if(title.contains("(Years)")){
					unitToAppend = "Years";
					percent = dataUser - dataPeer;
				}

				if(unitToAppend.equals("days")){
					unit = "days";
				}
				
				if(unitToAppend.equals("Years")){
					unit = "Years";
				}

				if(percent!=null){
					if(percent < 0){
						higOrLow = "lower";
						summary = "The "+title.replaceAll("&","&amp;") + " of the company was "+higOrLow + " by "+jasperReportService.applyFormattor(Math.abs(percent),unit,null,null)+ " " + unitToAppend +" as compared to the Peers in "+ period;
					}else if(percent>0){
						higOrLow = "higher";
						summary = "The "+title.replaceAll("&","&amp;") + " of the company was "+higOrLow + " by "+jasperReportService.applyFormattor(Math.abs(percent),unit,null,null)+ " " + unitToAppend + " as compared to the Peers in "+ period;
					}else{
						higOrLow = "same";
						summary = "The "+title.replaceAll("&","&amp;") + " of the company was "+higOrLow +" compared to the Peers in "+ period;
					}
				}else{
					summary = "N/A";
				}
			}else{
				summary ="N/A";
			}
		}catch(Exception e){
			_log.error(e.getMessage(),e);
		}
		return summary;
	}


	public Integer calculateTotalScoreOfASection(List<String> metrixList, HashMap<String,Integer> contentList){
		Double totalScore = 0d;
		for (String key : contentList.keySet()) {
			if(metrixList.contains(key)){
				Integer score = contentList.get(key);
				if(score!=null){
					totalScore = totalScore + score;
				}
			}
		}
		return totalScore.intValue();
	}

	public void sortReportElement(List<ReportPerformanceData> rpdList){
		if(rpdList!=null){
			Collections.sort(rpdList,new Comparator<ReportPerformanceData>() {
				@Override
				public int compare(ReportPerformanceData o1, ReportPerformanceData o2) {
					return (int) (o1.getPeriod().compareTo(o2.getPeriod()));
				}
			});
		}
	}

	public void sortReportElementDisplayOrder(List<UserModelDTO> rpdList){
		if(rpdList!=null){
			Collections.sort(rpdList,new Comparator<UserModelDTO>() {
				@Override
				public int compare(UserModelDTO o1, UserModelDTO o2) {
					if(Integer.parseInt(o1.getDisplayOrder()) < Integer.parseInt(o2.getDisplayOrder())){
						return -1;
					}else if(Integer.parseInt(o1.getDisplayOrder()) > Integer.parseInt(o2.getDisplayOrder())){
						return 1;
					}else if(Integer.parseInt(o1.getDisplayOrder()) == Integer.parseInt(o2.getDisplayOrder())){
						return 0;
					}else{
						return 1;
					}
				}
			});
		}
	}


	//This method check if Unit is Billion then convert the data in Million and update the unit in Million
	List<IndustryFinancialDataDTO> convertUnitToMillion(List<IndustryFinancialDataDTO> companyData) {
		if(companyData == null || companyData.size() == 0) {
			return companyData;
		}

		List<IndustryFinancialDataDTO> companyDataUpdated = companyData.stream().map(industryData -> {
			if(industryData.getUnit() != null 
					&& industryData.getUnit().equalsIgnoreCase(CMStatic.UNIT_BILLION) 
					&& industryData.getData() != null){
				industryData.setData(industryData.getData() * 1000);
				industryData.setUnit(CMStatic.UNIT_MILLION);
			}

			return industryData;

		}).collect(Collectors.toList());


		return companyDataUpdated;
	}



	public List<String> getTableOfContent(String pdfFileName , CreditReportInfo companyInfo){

		LinkedHashMap<String, String> contentAndPageMap = new LinkedHashMap<>();
		contentAndPageMap.put("LOAN DETAILS","");
		contentAndPageMap.put("UNENCUMBERED ASSET DETAILS","");
		if(LoanStatic.PORTFOLIO.equalsIgnoreCase(companyInfo.getRequestType())){
			contentAndPageMap.put("1. COMPANY INFORMATION","");
		}else{
			contentAndPageMap.put("KEY BORROWER INFORMATION","");
		}
		contentAndPageMap.put("ENCUMBERED ASSET DETAILS","");
		contentAndPageMap.put("MANAGEMENT DISCLOSURES & UNDERTAKINGS","");
		contentAndPageMap.put("Documents Submitted","");
		contentAndPageMap.put("Management Disclosure","");
		contentAndPageMap.put("Management Experience","");
		contentAndPageMap.put("Ownership Details","");
		contentAndPageMap.put("PROMOTER OWNERSHIP DETAILS","");
		contentAndPageMap.put("COMPARATIVE FINANCIAL SCORE","");
		contentAndPageMap.put("Performance & Activity Ratios","");
		contentAndPageMap.put("Profitability & Return Ratios","");
		contentAndPageMap.put("Leverage Ratios","");
		contentAndPageMap.put("Liquidity Ratios","");
		contentAndPageMap.put("Debt Coverage Ratios","");
		contentAndPageMap.put("SENSITIVITY ANALYSIS","");
		contentAndPageMap.put("RISK INDICATORS","");
		contentAndPageMap.put("PROFIT & LOSS ACCOUNT","");
		try {
			PdfReader pdfReader = new PdfReader(pdfFileName);
			for(int i=1; i<= pdfReader.getNumberOfPages(); i++){
				String textFromPage = PdfTextExtractor.getTextFromPage(pdfReader, i);
				for (String key : contentAndPageMap.keySet()) {
					if(textFromPage.toLowerCase().replaceAll("\\s","").contains(key.toLowerCase().replaceAll("\\s",""))){
						contentAndPageMap.put(key, ""+(i+2));
					}
				}
			}

			LinkedList<String> list = new LinkedList<>();
			for (String key : contentAndPageMap.keySet()) {
				list.add(contentAndPageMap.get(key));
			}
			return list;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>(19);
	}


	public boolean validateInputData(){
		boolean check = false;


		return check;
	}

	Double deFormatString(String value){
		Double tempValue = null;
		if(value!=null && !value.toLowerCase().trim().equals("")){
			value = value.replaceAll(",","");
			value = value.replaceAll("(","-");
			value = value.replaceAll(")","");
			tempValue = Double.valueOf(value);
		}
		return tempValue;
	}

	public Double percentageChange(Double newNumber , Double baseNumber){
		Double percent = null;
		if(baseNumber!=null && newNumber!=null){
			if(newNumber==0d && baseNumber==0d){
				return null;
			}else if(newNumber==0d){
				return null;
			}else{
				if(newNumber > 0 && baseNumber > 0){
					percent = ((newNumber-baseNumber)/baseNumber)*100;
				}else if(newNumber > 0 && baseNumber < 0){
					percent = Math.abs(((newNumber-baseNumber)/baseNumber)*100);
				}else if(newNumber < 0 && baseNumber > 0){
					percent = ((newNumber-baseNumber)/baseNumber)*100;
				}else if(newNumber < 0 && baseNumber < 0){
					if(newNumber > baseNumber){
						percent = Math.abs(((newNumber-baseNumber)/baseNumber)*100);
					}else{
						percent = -(((newNumber-baseNumber)/baseNumber)*100);
					}
				}
			}
		}else{
			return null;
		}
		return percent;
	}



	public List<CreditReportDualInformation> getBorrowerInformation(CreditReportInfo companyInfo){
		List<CreditReportDualInformation> keyBorrowerInformation = new ArrayList<>();
		if(companyInfo.getBasicInfo()!=null){
			keyBorrowerInformation.add(new CreditReportDualInformation("Entity Name" ,companyInfo.getBasicInfo().getCompanyName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Entity Registration No." ,companyInfo.getBasicInfo().getRegistrationNo()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Date of Incorporation " ,companyInfo.getDateOfIncorporation()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Class of Company" ,companyInfo.getBasicInfo().getClassOfCompany()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Industry" ,companyInfo.getIndustryName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Corporate Headquarters" ,companyInfo.getBasicInfo().getHeadquartersAddr()));
			keyBorrowerInformation.add(new CreditReportDualInformation("State" ,companyInfo.getBasicInfo().getStateName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Domicile Country" ,companyInfo.getBasicInfo().getCountryIsoCode()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Contact Person" ,companyInfo.getBasicInfo().getContactPersonName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Contact Email Address" ,companyInfo.getBasicInfo().getContactPersonEmail()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Tax Registration Number" ,companyInfo.getBasicInfo().getTaxRegistrationNumber()));
			//keyBorrowerInformation.add(new CreditReportDualInformation("Indirect Taxation Number" ,companyInfo.getBasicInfo().getTaxRegistrationNumber()));
		}

		return keyBorrowerInformation;
	}


	public List<CreditReportDualInformation> getBorrowerCompanyInformation(CreditReportInfo companyInfo){
		List<CreditReportDualInformation> keyBorrowerInformation = new ArrayList<>();
		if(companyInfo.getBasicInfo()!=null){
			keyBorrowerInformation.add(new CreditReportDualInformation("Company Name" ,companyInfo.getBasicInfo().getCompanyName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Industry" ,companyInfo.getIndustryName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Country" ,companyInfo.getBasicInfo().getCountryIsoCode()));
			keyBorrowerInformation.add(new CreditReportDualInformation("State" ,companyInfo.getBasicInfo().getStateName()));
			keyBorrowerInformation.add(new CreditReportDualInformation("Latest Year" ,companyInfo.getApplicableToYear()));
			String totalCreditExposure = jasperReportService.customFormattor(companyInfo.getBasicInfo().getTotalCreditExposure(),"","",false);
			if(totalCreditExposure!=null && !"-".equals(totalCreditExposure)){
				totalCreditExposure  = totalCreditExposure + " "+jasperReportService.getUnitAndCurrency(companyInfo.getUserCurrency(), companyInfo.getUserUnit());
			}
			keyBorrowerInformation.add(new CreditReportDualInformation("Total Credit Exposure" ,"" + totalCreditExposure));
		}

		return keyBorrowerInformation;
	}
}
