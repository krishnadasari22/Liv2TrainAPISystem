package com.televisory.capitalmarket.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dao.CMFinancialDataRepository;
import com.televisory.capitalmarket.dto.BenchMarkCompanyDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.CompanyLatestFilingInfoDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;
import com.televisory.capitalmarket.util.CMStatic;
import com.televisory.capitalmarket.util.DateUtil;

@Service
public class MobileAppResponseService {
	
	Logger _log = Logger.getLogger(MobileAppResponseService.class);

	@Autowired
	CMFinancialDataRepository factSetRepository;
	
	@Autowired
	CMStockService cmStockService;
	
	@Autowired
	DateUtil dateUtil;

	static final Map<String, String> financialTypeMap;
	
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	static{
		financialTypeMap = new HashMap<String, String>();
		financialTypeMap.put("IS","Income Statement");
		financialTypeMap.put("BS","Balance Sheet");
		financialTypeMap.put("CF","Cash Flow");
		financialTypeMap.put("FR","Financial Ratios");
		financialTypeMap.put("VR","Valuation Ratios");
	}
	
	public JSONObject editbenchMarkCompaniesByCompanyId(String companyId,Integer resultLimit,String currency, String benchmarkCompanyAdd, String benchmarkCompanyRemove, String periodType) {
		
		JSONObject responeJson = new JSONObject();
		JSONArray responseArray = new JSONArray();
		JSONObject headerJson = new JSONObject();
		List<BenchMarkCompanyDTO> benchMarkResponse = null;
		try{
			
			benchMarkResponse =  factSetRepository.editbenchMarkCompaniesByCompanyId(companyId,resultLimit,currency, benchmarkCompanyAdd, benchmarkCompanyRemove, periodType);
			
		}catch(Exception ex){
			_log.error(ex);
			String msg = "";
			Date latestStockdate = null;
			Date latestFinancialDate = null;
			try {
				 latestStockdate = cmStockService.getCompanyLatestStockPrice(companyId,currency).getDate();
				 Optional<Date> finDateOp = factSetRepository.getCompanyLatestFilingInfo(companyId)
					.stream()
					.filter(clf -> CMStatic.PERIODICITY_YEARLY.equalsIgnoreCase(clf.getPeriodType()))
					.map(clf->clf.getPeriod())
					.findFirst();
				 if(finDateOp.isPresent()){
					 latestFinancialDate = finDateOp.get();
				 }
			} catch (Exception e) {
				_log.error(e);
			}
			Date prevDateInYears = dateUtil.prevDateInYears(new Date(), 2);			
			if(latestStockdate !=null && latestStockdate.before(prevDateInYears)){
				msg = "Company delisted in "+new SimpleDateFormat("MMMM yyyy").format(latestStockdate);	
			} else if(latestFinancialDate!=null && latestFinancialDate.before(prevDateInYears)){
				msg = "Latest financial data not available to create benchmark";
			}else if(ex.getCause().getCause()!=null&&ex.getCause().getCause().getMessage().contains(companyId)){
				msg = ex.getCause().getCause().getMessage();
			}else{
				throw ex;
			} 
			responeJson.put("header", headerJson);
			responeJson.put("data", responseArray);
			responeJson.put("msg", msg);
			return responeJson;
		}
		
		Map<String,JSONObject> benchMarkResponseCompanyWise = new LinkedHashMap<String, JSONObject>();
		
		try {
			
			AtomicInteger columnIndex = new AtomicInteger(2);
					
			AtomicReference<String> currenctMetric = new AtomicReference<>();
			
			currenctMetric.set(benchMarkResponse.get(0).getFieldName());
			
			benchMarkResponse.forEach(benchMarkCompanyObject -> {
				
				String unitField="";
				int colNumber;
				
				if(!currenctMetric.get().equals(benchMarkCompanyObject.getFieldName())){
					columnIndex.incrementAndGet();
					currenctMetric.set(benchMarkCompanyObject.getFieldName());
				}

				if(benchMarkCompanyObject.getUnit()!=null && benchMarkCompanyObject.getUnit().equals("%")){
					unitField = "(%)";
				}else if(benchMarkCompanyObject.getUnit()!=null && benchMarkCompanyObject.getUnit().equalsIgnoreCase("million")){
					unitField+="(";
					if(benchMarkCompanyObject.getCurrency()!=null){
						unitField+=benchMarkCompanyObject.getCurrency()+" ";
					}
					unitField+="M)";
				}else if(benchMarkCompanyObject.getUnit()!=null && benchMarkCompanyObject.getUnit().equals("x")) {
					unitField+="(x)";
				}else if(benchMarkCompanyObject.getCurrency()!=null){
					unitField+="("+benchMarkCompanyObject.getCurrency()+")";
				}
				
				
				if(benchMarkResponseCompanyWise.containsKey(benchMarkCompanyObject.getCompanyName())){
						
					/*if(firstCompany.equals(benchMarkCompanyObject.getCompanyName()){
						 benchMarkResponseCompanyWise.get(benchMarkCompanyObject.getCompanyName()).put("columnName"+columnIndex, benchMarkCompanyObject.getShortName()+unitField);
					}*/
							
					/*benchMarkResponseCompanyWise.get(benchMarkCompanyObject.getCompanyName()).put("columnName"+columnIndex.get(), benchMarkCompanyObject.getShortName()+" "+unitField);*/
					headerJson.put("columnName"+columnIndex.get(), benchMarkCompanyObject.getShortName()+" "+unitField);
					benchMarkResponseCompanyWise.get(benchMarkCompanyObject.getCompanyName()).put("columnValue"+columnIndex.get(), benchMarkCompanyObject.getData());
				
					_log.info(columnIndex.get());
					
				}else{
					
					JSONObject benchMarkResponseJson =new JSONObject();
					
					/*if(firstCompany==""){
						firstCompany.replace("",benchMarkCompanyObject.getCompanyName());
					}
					*/
					benchMarkResponseJson.put("companyId", benchMarkCompanyObject.getCompanyId()==null?JSONObject.NULL:benchMarkCompanyObject.getCompanyId());
					benchMarkResponseJson.put("id", benchMarkCompanyObject.getId());
					if(null!=benchMarkCompanyObject.getPeriodType()){
						benchMarkResponseJson.put("periodType", benchMarkCompanyObject.getPeriodType());	
					}else{
						benchMarkResponseJson.put("periodType", "Delisted");
					}
					if(null!=benchMarkCompanyObject.getDate()) {
					benchMarkResponseJson.put("date", dateFormat.format(benchMarkCompanyObject.getDate()));
					}else {
						benchMarkResponseJson.put("date", benchMarkCompanyObject.getDate());
					}
					
					/*benchMarkResponseJson.put("columnName1", "company");*/
					headerJson.put("columnName1", "Company");
					benchMarkResponseJson.put("columnValue1", benchMarkCompanyObject.getCompanyName());
					
					/*benchMarkResponseJson.put("columnName"+columnIndex.get(), benchMarkCompanyObject.getShortName()+" "+unitField);*/
					headerJson.put("columnName"+columnIndex.get(), benchMarkCompanyObject.getShortName()+" "+unitField);
					benchMarkResponseJson.put("columnValue"+columnIndex.get(), benchMarkCompanyObject.getData());
					
					benchMarkResponseCompanyWise.put( benchMarkCompanyObject.getCompanyName(), benchMarkResponseJson);
				}
			});
			
			benchMarkResponseCompanyWise.forEach((key,value) -> {
				responseArray.put(value);
				if(value.has("columnValue2")==false&&value.has("columnValue3")==false&&value.has("columnValue4")==false
						&&value.has("columnValue5")==false&&value.has("columnValue6")==false){
					try {
						Date latestStockdate = cmStockService.getCompanyLatestStockPrice(value.getString("companyId"),currency).getDate();
						Date prevDateInYears = dateUtil.prevDateInYears(new Date(), 2);			
						if(latestStockdate !=null && latestStockdate.before(prevDateInYears)){
							value.put("msg", "Company delisted in "+new SimpleDateFormat("MMMM yyyy").format(latestStockdate));
						}
					} catch (Exception e) {
						_log.error(e);
					}
				}
			});
			/*JSONObject headerJson = new JSONObject();
			if(!responseArray.isEmpty()) {
				JSONObject json = (JSONObject) responseArray.get(0);
				for(int index=1;index<=columnIndex.get();index++) {
					headerJson.put("columnName"+index, json.getString("columnName"+index));	
				}
			}*/
			responeJson.put("header", headerJson);
			responeJson.put("data", responseArray);
			responeJson.put("msg", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responeJson;
	
	}

	/**
	 * 
	 * @param financialData
	 * @return
	 */
	public JSONObject getCompanyFinancial(List<CompanyFinancialMINDTO> financialData) {
		
		JSONObject finalResponseJson =new JSONObject();
	
		Map<String, LinkedHashMap<String,JSONObject>> financialMap = new HashMap<String, LinkedHashMap<String,JSONObject>>();
		Map<String,JSONObject> financialHeaderMap =new HashMap<>();
		Map<Date,Integer> periodPositionMap =new HashMap<>();
		
		try {
			
			AtomicInteger columnIndex = new AtomicInteger(2);
			AtomicReference<Date> currenctMetric = new AtomicReference<>();
			
			if(financialData.size()>0){
				
				currenctMetric.set(financialData.get(0).getPeriod());
				
				periodPositionMap.put(financialData.get(0).getPeriod(),2);
				
				financialData.forEach(financialDataObject -> {
					
					if(currenctMetric.get().compareTo(financialDataObject.getPeriod())!=0 && !periodPositionMap.containsKey(financialDataObject.getPeriod())){
						columnIndex.incrementAndGet();
						currenctMetric.set(financialDataObject.getPeriod());
						periodPositionMap.put(financialDataObject.getPeriod(),columnIndex.get());
					}
					
					String unitField="";
					
					/*if(financialDataObject.getUnit()!=null && !financialDataObject.getUnit().equals("%")){
						unitField+="(";
						if(financialDataObject.getCurrency()!=null){
							unitField+=financialDataObject.getCurrency()+" ";
						}
						unitField+=(financialDataObject.getUnit().equalsIgnoreCase("million")?"M":financialDataObject.getUnit())+")";
					}*/
					
					if(financialDataObject.getUnit()!=null && financialDataObject.getUnit().equals("%")){
						unitField = "(%)";
					}else if(financialDataObject.getUnit()!=null && financialDataObject.getUnit().equalsIgnoreCase("million")){
						unitField+="(";
						if(financialDataObject.getCurrency()!=null){
							unitField+=financialDataObject.getCurrency()+" ";
						}
						unitField+="M)";
					}else if(financialDataObject.getUnit()!=null && financialDataObject.getUnit().equals("x")) {
						unitField+="(x)";
					}else if(financialDataObject.getUnit()!=null && financialDataObject.getUnit().equalsIgnoreCase("days")) {
						unitField+="(days)";
					}else if(financialDataObject.getUnit()!=null && financialDataObject.getUnit().equalsIgnoreCase("Years")) {
						unitField+="(Years)";
					}else if(financialDataObject.getCurrency()!=null){
						unitField+="("+financialDataObject.getCurrency()+")";
					}
					
					Integer currentColumnIndex = periodPositionMap.get(financialDataObject.getPeriod());
					
					if(financialHeaderMap.containsKey(financialDataObject.getFinancialType())){
						financialHeaderMap.get(financialDataObject.getFinancialType()).put("columnName"+currentColumnIndex, financialDataObject.getPeriod());
					}else{
						JSONObject financialHeaderJson =new JSONObject();
						//financialHeaderJson.put("columnName1",financialTypeMap.get(financialDataObject.getFinancialType())+" "+unitField);
						String columnName1 = null;
						if(financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE) || 
								financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE)){
							columnName1 = financialTypeMap.get(financialDataObject.getFinancialType());
							financialHeaderJson.put("columnName1",financialTypeMap.get(financialDataObject.getFinancialType()));	
						}else if(financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_BALANCE_SHEET_CODE) ||
								financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_CASH_FLOW_CODE) ||
								financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_IS_CODE)){
							columnName1 = financialTypeMap.get(financialDataObject.getFinancialType())+" "+unitField;
						}
						financialHeaderJson.put("columnName1",columnName1);
						financialHeaderJson.put("columnName"+currentColumnIndex, financialDataObject.getPeriod());
						financialHeaderMap.put(financialDataObject.getFinancialType(), financialHeaderJson);
					}
	
					if(!financialMap.containsKey(financialDataObject.getFinancialType())){
						LinkedHashMap<String,JSONObject> financialResponseCompanyWise = new LinkedHashMap<String, JSONObject>();
						financialMap.put(financialDataObject.getFinancialType(), financialResponseCompanyWise);
					}
					
					if(financialMap.get(financialDataObject.getFinancialType()).containsKey(financialDataObject.getFieldName())){
						financialMap.get(financialDataObject.getFinancialType()).get(financialDataObject.getFieldName()).put("columnValue"+currentColumnIndex, financialDataObject.getData());
					}else{
						
						JSONObject financialResponseJson =new JSONObject();
							
						financialResponseJson.put("id", financialDataObject.getId());
						financialResponseJson.put("periodType", financialDataObject.getPeriodType());
						financialResponseJson.put("shortName", financialDataObject.getShortName());
						financialResponseJson.put("itemName", financialDataObject.getItemName());
						financialResponseJson.put("displayName", financialDataObject.getDisplayName());
						financialResponseJson.put("displayOrder", financialDataObject.getDisplayOrder());
						financialResponseJson.put("depthLevel", financialDataObject.getDepthLevel());
						financialResponseJson.put("companyId", financialDataObject.getCompanyId());
						financialResponseJson.put("keyParameter", financialDataObject.getKeyParameter());
						financialResponseJson.put("keyParameterOrder", financialDataObject.getKeyParameterOrder());
						financialResponseJson.put("icFlag", financialDataObject.getIcFlag());
						
						String columnValue1 = null;
						if(financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_FINANCIAL_RATIO_CODE) || 
								financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_VALUATION_RATIO_CODE)){
							columnValue1 = financialDataObject.getItemName()+" "+unitField;
						}else if(financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_BALANCE_SHEET_CODE) ||
								financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_CASH_FLOW_CODE) ||
								financialDataObject.getFinancialType().equals(CMStatic.DATA_TYPE_IS_CODE)){
							columnValue1 = financialDataObject.getItemName();
						}
						financialResponseJson.put("columnValue1", columnValue1);
						financialResponseJson.put("columnValue"+currentColumnIndex, financialDataObject.getData());
						financialMap.get(financialDataObject.getFinancialType()).put( financialDataObject.getFieldName(), financialResponseJson);
					}
				});
				
				AtomicInteger columnNumber = new AtomicInteger(1);
				
				financialMap.forEach((key,value) -> {
					
					JSONArray responseDataArray = new JSONArray();
					
					financialMap.get(key).forEach((dataKey,dataVal) -> {
						responseDataArray.put(dataVal);
					});
					
					finalResponseJson.put("header"+columnNumber.get(),financialHeaderMap.get(key));
					finalResponseJson.put("values"+columnNumber.get(),responseDataArray);
					
					columnNumber.incrementAndGet();
				});
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return finalResponseJson;
		
	}
	
}
