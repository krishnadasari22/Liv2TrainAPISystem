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
public class RatioServiceBank {
	
	Logger _log = Logger.getLogger(RatioServiceBank.class);
	
	public CompanyFinancialMINDTO t_loan_book_size(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		keyRatio.setData(industryRatio.get(companyId+"_"+currentDate+"_ff_loan_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_loan_net"));
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_net_intr_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		if(val1==null || val2==null) {
			keyRatio.setData(null);
		}else {			
			keyRatio.setData(val1-val2);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_total_deposit(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		keyRatio.setData(industryRatio.get(companyId+"_"+currentDate+"_ff_deps")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_deps"));
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_net_intr_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		int index = availPeriods.indexOf(currentDate);
		CompanyFinancialMINDTO preKeyRatio = t_net_intr_income(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData);
		if(preKeyRatio==null || preKeyRatio.getData()==null ) {
			keyRatio.setData(null);
			return keyRatio; 
		}else if(preKeyRatio.getData()==0.0 ){
			keyRatio.setData(0.0);
			return keyRatio; 
		}
		if(index+1==availPeriods.size()) {
			Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
			if(val1==null || val1==0.0 || preKeyRatio.getData()==null ) {
				keyRatio.setData(null);
			}else {
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					keyRatio.setData((preKeyRatio.getData()*4)/val1);
				}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
					keyRatio.setData((preKeyRatio.getData()*2)/val1);
				}else {
					keyRatio.setData((preKeyRatio.getData())/val1);
				}
			}
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
				
			if(val1==null || val1==0.0 || preKeyRatio.getData()==null  || val2==null || val2==0.0 ) {
				keyRatio.setData(null);
			}else {
				
				Double avg = (val1+val2)/2;
				
				if(avg==0.0){
					keyRatio.setData(null);
				}else{
					if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
						keyRatio.setData((preKeyRatio.getData()*4)/avg);
					}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
						keyRatio.setData((preKeyRatio.getData()*2)/avg);
					}else {
						keyRatio.setData((preKeyRatio.getData())/avg);
					}
				}	
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_cost_to_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_exp");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		
		if(val1==null || val2==null || val2==0.0 || val3==null || val3==0.0 || val4==null || val4==0.0 || (val2-val3+val4)==0.0) {
			keyRatio.setData(null);
		} else {
			Double finalVal = val1/(val2-val3+val4);
			keyRatio.setData(finalVal*100);
		}
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_oper_cost_tot_assests(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		int index = availPeriods.indexOf(currentDate);
		Double val = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_exp");

		if(index+1==availPeriods.size()) {
			Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
			if(val1==null || val1==0.0 || val==null || val==0.0) {
				keyRatio.setData(null);
			}else {
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					keyRatio.setData((val*4)/val1);
				}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
					keyRatio.setData((val*2)/val1);
				}else {
					keyRatio.setData((val)/val1);
				}
			}
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val1==null || val1==0.0 || val==null || val==0.0 || val2==null || val2==0.0) {
				keyRatio.setData(null);
			}else {
				
				Double avg = (val1+val2)/2;
				
				if(avg==0.0){
					keyRatio.setData(null);
				}else{
					if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
						keyRatio.setData((val*4)/avg);
					}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
						keyRatio.setData((val*2)/avg);
					}else {
						keyRatio.setData((val)/avg);
					}
				}
				
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_non_intr_income_tot_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		if(val1==null || val1==0.0 || val2==null || val2==0.0 ||(val1+val2)==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val1+val2);
			keyRatio.setData(finalVal);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_emp_cost_tot_exp(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_labor_exp");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_exp");
		if(val1==null || val1==0.0 || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_fee_income_tot_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {

		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val = industryRatio.get(companyId+"_"+currentDate+"_ff_trust_commiss_inc");
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");

		if(val1==null || val1==0.0 || val2==null || val2==0.0 || val==null || val==0.0 || (val1+val2)==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val/(val1+val2);
			keyRatio.setData(finalVal*100);
		}
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_fixed_assets_turnover_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");

		if(val2 == null) {
			keyRatio.setData(null);
		} else {
			Double val3 = (val1+val2);
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val3 = val3*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val3 = val3*2;
			}
			int index = availPeriods.indexOf(currentDate);
	
			Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_ppe_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ppe_net");
			if(index+1==availPeriods.size()) {
				if(val4==0.0)
					keyRatio.setData(null);
				else
					keyRatio.setData(val3/val4);
			} else {
				String prevDate = availPeriods.get(index+1); 
				Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_ppe_net")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_ppe_net");
	
				if( val4==0.0 || val5==0.0 ) {
					keyRatio.setData(null);
				}else {
					Double avg = (val4+val5)/2;
					keyRatio.setData((val3)/avg);
				}
			}
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_pbt_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");

		if(val1==null || val3==null || (val2+val3)==0.0) {
			keyRatio.setData(null);
		} 
		else {
			Double finalVal = val1/(val2+val3);
			keyRatio.setData(finalVal);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_pat_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");

		Double val4 = val2+val3;
		if(val4==0.0)
			keyRatio.setData(null);
		else {
			Double finalVal = val1/val4;
			keyRatio.setData(finalVal);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_risk_adjusted_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		CompanyFinancialMINDTO preKeyRatio = t_net_intr_income(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData);
		Double val1 = preKeyRatio.getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_loss_prov");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val1==null || val1==0.0 || val2==null || val2==0.0 || val3==null || val3==0.0 || val4==null || val4==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData((val1 + val2 - val3)/(val4));
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val5==null || val5==0.0) {
				keyRatio.setData(null);
			}else {
				Double avg = (val4+val5)/2; 
				
				if(avg==0.0){
					keyRatio.setData(null);
				}else{
					keyRatio.setData((val1 + val2 - val3)/(avg));
				}		
			}
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

	public CompanyFinancialMINDTO t_roa(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			if(val2==0.0)
				keyRatio.setData(null);
			else
				keyRatio.setData(val1/val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			
			Double avg = (val2+val3)/2; 
				
			if(avg==0.0){
				keyRatio.setData(null);
			}else{
				keyRatio.setData((val1)/(avg));
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_roe(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			if(val2==0.0)
				keyRatio.setData(null);
			else
				keyRatio.setData(val1/val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
			
			Double avg = (val2+val3)/2; 
				
			if(avg==0.0){
				keyRatio.setData(null);
			}else{
				keyRatio.setData((val1)/(avg));
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_yield_advances(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_net");
		
		if(val1==null || val2==null) {
			keyRatio.setData(null);
		} else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			int index = availPeriods.indexOf(currentDate);
			if(index+1==availPeriods.size()) {
				if(val2==0.0)
					keyRatio.setData(null);
				else
					keyRatio.setData(val1/val2);
			}else {
				String prevDate = availPeriods.get(index+1); 
				Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_loan_net")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_loan_net");
				
				Double avg = (val2+val3)/2; 
					
				if(avg==0.0){
					keyRatio.setData(null);
				}else{
					keyRatio.setData((val1)/(avg));
				}
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_average_cost_deposits(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_deps");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_deps");
		if(val1==null || val1==0.0 || val2==null || val2==0.0 ) {
			keyRatio.setData(null);
			return keyRatio;
		}
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData((val1)/(val2));
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_deps");
			if(val3==null || val3==0.0) {
				keyRatio.setData(null);
			}else {
				Double avg = (val2+val3)/2; 
				
				if(avg==0.0){
					keyRatio.setData(null);
				}else{
					keyRatio.setData((val1)/(avg));
				}
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_intr_spread(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		CompanyFinancialMINDTO fin1 = t_yield_advances(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData);
		CompanyFinancialMINDTO fin2 = t_average_cost_deposits(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData);
		if(fin1.getData()==null || fin1.getData()==0.0 || fin2.getData()==null || fin2.getData()==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(fin1.getData()/100-fin2.getData()/100);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_liquid_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_due_fr_bk");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		if(val1==null || val1==0.0 || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_loan_deposits(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_net");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_deps");
		if(val1==null || val1==0.0 || val2==null || val2==0.0) {
			keyRatio.setData(null);
		} else {
			keyRatio.setData(val1/val2);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_investment_deposit(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_deps");
		if(val1==null || val1==0.0 || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_intang");
		if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
			keyRatio.setData(null);
		}else {
			Double finalVal = val1-val2;
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tier_1_capital(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
	
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double finalVal = industryRatio.get(companyId+"_"+currentDate+"_ff_tier1_cap")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_tier1_cap");
		keyRatio.setData(finalVal);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tier_2_capital(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double finalVal = industryRatio.get(companyId+"_"+currentDate+"_ff_tier2_cap")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_tier2_cap");
		keyRatio.setData(finalVal);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_loan_provision_tot_assest(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_loss_prov");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
			keyRatio.setData(null);
			return keyRatio;
		}
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData((val1)/(val2));
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2; 
			
			if(avg==0.0 || avg==null){
				keyRatio.setData(null);
			}else{
				keyRatio.setData((val1)/(avg));
			}
			//}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_gearing_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt");
		CompanyFinancialMINDTO fin2 = t_tangible_networth(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData);
		Double val2 = fin2.getData();
		if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
			keyRatio.setData(null);
		}else
			keyRatio.setData(val1/val2);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_debt_assests(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
			keyRatio.setData(null);
		}else
			keyRatio.setData(val1/val2);
		
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_deposit_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_deps");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_deps");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ||(val2-1)==0.0) { 
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

	public CompanyFinancialMINDTO t_growth_loan_book(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_net");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_loan_net");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ||(val2-1)==0.0) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(val1/val2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_growth_nii(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_net_intr_income(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = t_net_intr_income(companyId, industryRatio,closePrice, totalShares, availPeriods, prevDate, prevDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ||(val2-1)==0) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(val1/val2-1);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_growth_non_intr_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {

		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
			return keyRatio;
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_non_int_inc");
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ||(val2-1)==0.0) { 
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
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		if(closePrice == null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(closePrice*totalShares/CMStatic.UNIT_FACTOR_MILLION);
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_earning_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");//==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(totalShares == null || totalShares == 0.0 || val1==null)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
	
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		if(totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1/totalShares * CMStatic.UNIT_FACTOR_MILLION);
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tangible_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
	
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double finVal = t_tangible_networth(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(finVal == null || totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(finVal/totalShares * CMStatic.UNIT_FACTOR_MILLION);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_dividend_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		if(totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1/totalShares * CMStatic.UNIT_FACTOR_MILLION);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_price_earnings(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(val1 ==null || val2==null || val2==0.0) {
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

	public CompanyFinancialMINDTO t_price_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_non_int_inc");
		
		if(val1==null || val2==null) {
			keyRatio.setData(null);
		}else {
			Double fin1 = val2+val4;
			if(fin1==0.0) {
				keyRatio.setData(null);
			}else {
				Double finalVal = val1/(fin1);
				keyRatio.setData(finalVal);
			}
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
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
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
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
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

	public CompanyFinancialMINDTO t_peg_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = t_price_earnings(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

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

	public CompanyFinancialMINDTO t_dividend_payout_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		
		if(val1==null || val2==null || val2 < 0.0 ) { 
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_dividend_yield(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId,ratio,companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = t_mcap(companyId, industryRatio,closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1==null || val2==null || val2==0.0 ) { 
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/val2);
		}
		
		if(keyRatio.getData()!=null) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(keyRatio.getData()*4);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(keyRatio.getData()*2);
			}
		}
		if(keyRatio.getData()!=null) {
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
