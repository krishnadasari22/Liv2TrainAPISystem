package com.televisory.capitalmarket.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.BalanceModelDTO;
import com.televisory.capitalmarket.model.RatioCompanyData;
import com.televisory.capitalmarket.util.CMStatic;

@Service
public class RatioServiceInsurance {
		
	Logger _log = Logger.getLogger(RatioServiceInsurance.class);
	
	public CompanyFinancialMINDTO t_premium_earned(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_prem_earn");
		keyRatio.setData(val1);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_assets_under_management(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		keyRatio.setData(val1);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_insurance_liabilities(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ins_liabs_pol");
		keyRatio.setData(val1);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_loss_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_loss_claim_exp");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_prem_earn");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_expense_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_underwriting_exp");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_prem_earn");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_combined_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_expense_ratio(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_loss_ratio(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		val1 = val1==null?0.0:val1/100; // divided by hundred because its already been multiplied while calculating
		val2 = val2==null?0.0:val2/100; // divided by hundred because its already been multiplied while calculating
		
		keyRatio.setData(val1+val2);
		/*keyRatio.setKeyParameter(1);*/
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_investment_yield(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ins_invest_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ins_invest_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_real_gain")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_real_gain");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		
		if(val3==null) {
			keyRatio.setData(null);
		}else {
			int index = availPeriods.indexOf(currentDate);
			Double finalVal = 0.0;
			if(index+1==availPeriods.size()) {
				finalVal = (val1+val2)/(val3);
			}else {
				String prevDate = availPeriods.get(index+1); 
				Double val4 = industryRatio.get(companyId+"_"+prevDate+"_ff_invest");
				if(val4==null || val4==0.0) {
					finalVal = 0.0;
				}else {
					Double avg = (val3+val4)/2;
					finalVal = (val1+val2)/(avg);
				}
			}
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				finalVal = finalVal*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				finalVal = finalVal*2;
			}
			keyRatio.setData(finalVal);
			/*keyRatio.setKeyParameter(1);*/
			if(keyRatio.getData()!=null) {
				keyRatio.setData(keyRatio.getData()*100);
			}
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_return_on_net_worth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_rsrv_noneq")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_rsrv_noneq");
		
		int index = availPeriods.indexOf(currentDate);
		Double finalVal = null;

		if(index+1==availPeriods.size() && val2 != null) {
			finalVal = val1/(val2-val3-val4);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			Double val6 = industryRatio.get(companyId+"_"+prevDate+"_ff_liabs")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_liabs");
			Double val7 = industryRatio.get(companyId+"_"+prevDate+"_ff_rsrv_noneq")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_rsrv_noneq");
			
			if(val5 != null) {
				Double final1 = val2-val3-val4;
				Double final2 = val5-val6-val7;
				Double avg = (final1+final2)/2;
				finalVal = val1/(avg);
			}
		}
		
		if(finalVal != null){
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				finalVal = finalVal*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				finalVal = finalVal*2;
			}
			keyRatio.setData(finalVal*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_fixed_assets_turnover_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_ppe_net");
		
		if(val2==null || val2==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			finalVal = val1/(val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_ppe_net");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2;
			finalVal = val1/(avg);
		}
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			finalVal = finalVal*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			finalVal = finalVal*2;
		}
		keyRatio.setData(finalVal);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_roa(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		
		if(val2==null || val2==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			finalVal = val1/(val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2;
			finalVal = val1/(avg);
		}
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			finalVal = finalVal*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			finalVal = finalVal*2;
		}
		keyRatio.setData(finalVal);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_roe(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			Double fin1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
			
			Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
			
			if(val3==null || val3==0.0) {
				keyRatio.setData(null);
				return keyRatio;
			}
			
			Double finalVal = 0.0;
			int index = availPeriods.indexOf(currentDate);
			if(index+1==availPeriods.size()) {
				finalVal = fin1/(val3);
			}else {
				String prevDate = availPeriods.get(index+1); 
				Double val4 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
				if(val4==null ) {
					val4=0.0;
				}
				Double avg = (val3+val4)/2;
				finalVal = fin1/(avg);
			}
			if(finalVal!=null)
				finalVal = finalVal*4;
			keyRatio.setData(finalVal);
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			Double fin1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc");
			
			Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
			
			if(val3==null || val3==0.0) {
				keyRatio.setData(null);
				return keyRatio;
			}
			
			Double finalVal = 0.0;
			int index = availPeriods.indexOf(currentDate);
			if(index+1==availPeriods.size()) {
				finalVal = fin1/(val3);
			}else {
				String prevDate = availPeriods.get(index+1); 
				Double val4 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
				if(val4==null ) {
					val4=0.0;
				}
				Double avg = (val3+val4)/2;
				finalVal = fin1/(avg);
			}
			if(finalVal!=null)
				finalVal = finalVal*2;
			keyRatio.setData(finalVal);
		}else {
			Double fin1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc");
			
			Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
			
			if(val3==null || val3==0.0) {
				keyRatio.setData(null);
				return keyRatio;
			}
			
			Double finalVal = 0.0;
			int index = availPeriods.indexOf(currentDate);
			if(index+1==availPeriods.size()) {
				finalVal = fin1/(val3);
			}else {
				String prevDate = availPeriods.get(index+1); 
				Double val4 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
				if(val4==null ) {
					val4=0.0;
				}
				Double avg = (val3+val4)/2;
				finalVal = fin1/(avg);
			}
			
			keyRatio.setData(finalVal);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_net_profit_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_pbt_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_ebit_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_inc_bef_int");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_liquid_assets_to_reserves(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_only");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_ins_rsrv");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_insurance_leverage(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_prem_earn")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_prem_earn");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_rsrv_noneq")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_rsrv_noneq");
		
		if(val2==null) {
			keyRatio.setData(null);
		} else {
			Double finalVal = val2-val3-val4;
			if(finalVal==0.0) {
				keyRatio.setData(null);
			}else {
				finalVal = val1/(val2-val3-val4);
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					finalVal = finalVal*4;
				}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
					finalVal = finalVal*2;
				}
				keyRatio.setData(finalVal*100);
			}
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
	
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_intang")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_intang");
		if(val1==null) {
			keyRatio.setData(null);
		} else {
			keyRatio.setData(val1-val2);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_debt_to_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt");
		Double val2 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			if(val1==0.0) {
				keyRatio.setData(0.0);
			}else {
				Double finalVal = val1/(val2);
				keyRatio.setData(finalVal);
			}
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cash_to_investment(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_only");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	
	public CompanyFinancialMINDTO t_insurance_liabilities_to_investment(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_assets_under_management(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_insurance_liabilities(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val2/val1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData());
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_premium_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_prem_earn");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_prem_earn");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(val1/val2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_claims_payout_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_loss_claim_exp");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_loss_claim_exp");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(val1/val2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_growth_in_investment_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ins_invest_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ins_invest_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_real_gain")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_real_gain");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_ins_invest_inc")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_ins_invest_inc");
			Double val4 = industryRatio.get(companyId+"_"+prevDate+"_ff_real_gain")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_real_gain");
			Double sum1 = val1+val2;
			Double sum2 = val3+val4;
			if(sum2==0.0 ) { 
				keyRatio.setData(0.0);
			}else
				keyRatio.setData(sum1/sum2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_growth_in_investment(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_invest");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(val1/val2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_net_profit_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_consol_net_inc");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(val1/val2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_mcap(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		if(closePrice == null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(closePrice*totalShares/CMStatic.UNIT_FACTOR_MILLION);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_earning_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(val1==null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		if(val1==null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tangible_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1 == null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_dividend_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		if(val1 == null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}


	public CompanyFinancialMINDTO t_price_earnings(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		
		if(keyRatio.getData()!=null) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(keyRatio.getData()/4);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(keyRatio.getData()/2);
			}
		}
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_to_book_value(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/val2;
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_cashflow(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_cf");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/val2;
			keyRatio.setData(finalVal);
		}
		
		if(keyRatio.getData()!=null) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(keyRatio.getData()/4);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(keyRatio.getData()/2);
			}
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || (val2==null || val2==0.0)) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		if(keyRatio.getData()!=null) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(keyRatio.getData()/4);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(keyRatio.getData()/2);
			}
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_peg_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = t_price_earnings(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val1 = val1==null?0.0:val1;
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_net_inc_basic");
			if(val2==null || val2==0.0 || val3==null || val3==0.0) { 
				keyRatio.setData(null);
			}else {
				keyRatio.setData( val1 / ((val2-val3)*100/Math.abs(val3)) );
			}
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_enterprise_value(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_pfd_stk")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_pfd_stk");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_min_int_accum")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_min_int_accum");
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_only");
		
		if(val1==null || val1 == 0.0 || val5==null)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1+val2+val3+val4-val5);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_ev_ebit(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_enterprise_value(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_inc_bef_int");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		
		if(keyRatio.getData()!=null) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(keyRatio.getData()/4);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(keyRatio.getData()/2);
			}
		}
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_dividend_payout_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		
		if(val1==null || val2==null || val2<0.0 ) { 
			keyRatio.setData(null);
		}else {
			if(val1==0.0) {
				keyRatio.setData(0.0);
			}else {
				keyRatio.setData(val1/val2);
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_dividend_yield(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1==null || val2==null || val2==0.0 ) { 
			keyRatio.setData(null);
		}else {
			if(val1==0.0) {
				keyRatio.setData(0.0);
			}
			else 
				keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(keyRatio.getData()*4);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(keyRatio.getData()*2);
			}
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	
	public CompanyFinancialMINDTO ratioValuesAssignment(String companyId,BalanceModelDTO ratio, RatioCompanyData companyData, String currentDate, String currentApplicableDate, String periodType, int order, String latestAnnualDate, String unit, String currency) {
		CompanyFinancialMINDTO keyRatio = new CompanyFinancialMINDTO();
		keyRatio.setDepthLevel("1");
		keyRatio.setCompanyId(companyId);
		/*keyRatio.setCompanyName(companyData.getCompanyName());
		keyRatio.setCountryId(companyData.getCountryId());
		keyRatio.setCountryName(companyData.getCountryName());*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try { 
			keyRatio.setPeriod(df.parse(currentDate));
			keyRatio.setApplicablePeriod(df.parse(currentApplicableDate));
			if(latestAnnualDate != null)
				keyRatio.setLatestAnnualDate(df.parse(latestAnnualDate));
		} catch (ParseException e) 
		{e.printStackTrace();}
		keyRatio.setDisplayOrder(String.valueOf(order));
		keyRatio.setPeriodType(periodType);
		keyRatio.setPeriodType(periodType);
		keyRatio.setFieldName(ratio.getFieldName());
		keyRatio.setSection(ratio.getSection());
		keyRatio.setItemName(ratio.getDescription());
		
		if(ratio.getCurrencyFlag() != null && ratio.getCurrencyFlag() == 1)
			keyRatio.setCurrency(currency);
		keyRatio.setUnit(ratio.getUnit());
		
		keyRatio.setShortName(ratio.getShortName());
		keyRatio.setFinancialType(ratio.getFinancialType());
		keyRatio.setKeyParameter(ratio.getKeyParameter());
		keyRatio.setIcFlag(ratio.getIcFlag());
		if(ratio.getKeyParameter()!=null && ratio.getKeyParameter()==1) {
			keyRatio.setKeyParameterOrder(ratio.getKeyParameterOrder());
		}
		return keyRatio;
	}
}
