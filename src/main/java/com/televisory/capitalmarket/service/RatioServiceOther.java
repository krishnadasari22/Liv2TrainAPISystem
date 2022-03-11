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
public class RatioServiceOther {
	
	Logger _log = Logger.getLogger(RatioServiceOther.class);
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public CompanyFinancialMINDTO t_ebit(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_inc");
		if(val2==null)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 + val2);
		
		return keyRatio;
	}
	

	public CompanyFinancialMINDTO t_ebitda(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double ebit = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp");
		
		if(ebit==null)
			keyRatio.setData(null);
		else
		keyRatio.setData(ebit + val1);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_intr_income_tot_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_investment_income_tot_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_invest_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_oth_oper_income_tot_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_inc_oth") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_oper_inc_oth");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_trade_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_trade_inc");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_trust_commiss_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_trust_commiss_inc");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_for_exch_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_for_exch_inc");
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if(val5 == null || val5 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData((val1 + val2 + val3 + val4)/ val5);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cost_to_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_exp_tot");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_loss_prov") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_loan_loss_prov");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if((val2 == null || val2 == 0.0) && (val4 == null || val4 == 0.0))
			keyRatio.setData(null);
		else
			keyRatio.setData((val1 - val2 - val3)/ (val4 - val2));
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_oper_cost_tot_assests(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_exp_tot");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_loss_prov") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_loan_loss_prov");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double devident = val4;
		int index = availPeriods.indexOf(currentDate);
		if(availPeriods.size() > index + 1) {
			String prevDate = availPeriods.get(index + 1);
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if((val4 != null && val5 != null))
				devident = (val4 + val5) / 2;
			if((val4 == null && val5 == null))
				devident = null;
			if(val4 != null)
				devident = val4;
			else
				devident = val5;
		}
		if((devident == null || devident == 0.0))
			keyRatio.setData(null);
		else {
			Double numerator = val1 - val2 - val3;
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				numerator = numerator*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				numerator = numerator*2;
			}
			keyRatio.setData((numerator)/ devident);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_non_intr_income_tot_income(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if(val1 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData((val2 - val1) / val2);
		
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_assets_under_management(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		keyRatio.setData(val1);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_ebitda_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double ebitda = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(ebitda == null || val1 == null || val1 == 0.0 )
			keyRatio.setData(null);
		else
			keyRatio.setData(ebitda / val1);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
		
	}

	public CompanyFinancialMINDTO t_ebit_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double ebit = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(ebit == null || val1 == null || val1 == 0.0 )
			keyRatio.setData(null);
		else
			keyRatio.setData(ebit / val1);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_pbt_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_net_intr_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		
		if(val2==null)
			keyRatio.setData(null);
		else {
			Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
			
			Double devident = val4;
			
			int index = availPeriods.indexOf(currentDate);
			if(availPeriods.size() > index + 1) {
				
				String prevDate = availPeriods.get(index + 1);
				Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
				
				if((val4 != null && val5 != null))
					devident = (val4 + val5) / 2;
				if((val4 == null && val5 == null))
					devident = null;
				if(val4 != null)
					devident = val4;
				else
					devident = val5;
	
			}
			if((devident == null || devident == 0.0))
				keyRatio.setData(null);
			else {
				Double numerator = val1 - val2;
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					numerator = numerator*4;
				}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
					numerator = numerator*2;
				}
				keyRatio.setData((numerator)/ devident);
			}
		}
		
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_pat_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_roa(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		
		Double devident = val4;
		
		int index = availPeriods.indexOf(currentDate);
		if(availPeriods.size() > index + 1) {
			
			String prevDate = availPeriods.get(index + 1);
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			
			if((val4 != null && val5 != null))
				devident = (val4 + val5) / 2;
			if((val4 == null && val5 == null))
				devident = null;
			if(val4 != null)
				devident = val4;
			else
				devident = val5;

		}
		if((devident == null || devident == 0.0))
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData((val1*4)/ devident);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData((val1*2)/ devident);
			}else {
				keyRatio.setData((val1)/ devident);
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_roe(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		
		Double devident = val4;
		
		int index = availPeriods.indexOf(currentDate);
		if(availPeriods.size() > index + 1) {
			
			String prevDate = availPeriods.get(index + 1);
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
			
			if((val4 != null && val5 != null))
				devident = (val4 + val5) / 2;
			if((val4 == null && val5 == null))
				devident = null;
			if(val4 != null)
				devident = val4;
			else
				devident = val5;

		}
		if((devident == null || devident == 0.0))
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData((val1*4)/ devident);
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData((val1*2)/ devident);
			}else {
				keyRatio.setData((val1)/ devident);
			}
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	
	
	public CompanyFinancialMINDTO t_liquid_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_investment_tot_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_invest");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_liquid_assets_tot_debt(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
		
	}
	
	
	
	public CompanyFinancialMINDTO t_ebit_debt_ser_amount(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_debt_servicing_amount(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val1 == null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_intr_coverage_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");

		if(val1 == null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	//t_ebitda_debt_ser_amount
	public CompanyFinancialMINDTO t_debt_ser_coverage(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_debt_servicing_amount(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val1 == null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_ebitda_intr_exp(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");

		if(val1 == null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_cads_intr_expenses(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_cash_for_debt_serv(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");

		if(val1 == null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_gross_debt_ebitda(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(val1 / (val2*4));
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(val1 / (val2*2));
			}else {
				keyRatio.setData(val1 / val2);
			}
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_tot_liab_tot_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_gross_debt_equity(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_gross_debt(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		keyRatio.setData(industryRatio.get(companyId+"_"+currentDate+"_ff_debt"));
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_cash_for_debt_serv(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		
		if(val1==null)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 + val2);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_debt_servicing_amount(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_tot");
		Double val2 = 0.0;
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(val1);
		} else {
			String prevDate = availPeriods.get(index+1); 
			val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr") == null ? 0.0 : industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr");
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2/4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2/2;
			}
		}
		
		keyRatio.setData(val1 + val2);
		return keyRatio;
	}
	
	
	
	public CompanyFinancialMINDTO t_tangible_networth_debt_serv_amount(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = t_debt_servicing_amount(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				keyRatio.setData(val1 / (val2*4));
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				keyRatio.setData(val1 / (val2*2));
			}else {
				keyRatio.setData(val1 / val2);
			}
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_short_term_debt_total_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st") ==  null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets") ==  null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_short_term_debt_liquid_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st") ==  null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st") ==  null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_short_term_debt_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st") ==  null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_total_liabs_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs") ==  null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val2 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_sales_total_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			keyRatio.setData(val1 / val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_intang") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_intang");

		if(val2 == null)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1-val2-val3);
		
		return keyRatio;
	}	
	
	public CompanyFinancialMINDTO t_net_sales_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_sales");
			if(val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(((val1 - val2)/val2)*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_net_income_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");

		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_consol_net_inc");
			if(val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(((val1 - val2)/val2)*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tot_assets_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val1 == null || val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(((val1 - val2)/val2)*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_tot_tangible_assets_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = t_tangible_networth(companyId, industryRatio,closePrice, totalShares, availPeriods, prevDate, prevDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
			if(val1==null || val1==0.0 || val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData(((val1 - val2)/val2)*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_investment_income_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_invest_inc");

		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_invest_inc");
			if(val2==null || val2==0.0 || val1==null  ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData((val1/val2-1)*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_aum_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_loan_gross");

		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_loan_gross");
			if(val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData((val1/val2-1)*100);
		}
		return keyRatio;
	}

	
	public CompanyFinancialMINDTO t_net_profit_tot_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			keyRatio.setData(val1 / val2);
			
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_net_profit_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc");
		Double val2 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			keyRatio.setData(val1 / val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_fixed_assets_turnover_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_ppe_net");
		
		Double devident = val4;
		
		int index = availPeriods.indexOf(currentDate);
		if(availPeriods.size() > index + 1) {
			
			String prevDate = availPeriods.get(index + 1);
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_ppe_net");
			
			/*if((val4 != null && val5 != null))
				devident = (val4 + val5) / 2;
			if((val4 == null && val5 == null))
				devident = null;
			if(val4 != null)
				devident = val4;
			else
				devident = val5;*/
			if(val4 == null || val5 == null)
				devident = null;
			else
				devident = (val4 + val5) / 2;

		}
		if((devident == null || devident == 0.0))
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			} 
			keyRatio.setData((val1)/ devident);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_tot_assets_turnover(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		
		Double devident = val4;
		
		int index = availPeriods.indexOf(currentDate);
		if(availPeriods.size() > index + 1) {
			
			String prevDate = availPeriods.get(index + 1);
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			
			/*if((val4 != null && val5 != null))
				devident = (val4 + val5) / 2;
			if((val4 == null && val5 == null))
				devident = null;
			if(val4 != null)
				devident = val4;
			else
				devident = val5;*/
			if(val4 == null || val5 == null)
				devident = null;
			else
				devident = (val4 + val5) / 2;

		}
		if((devident == null || devident == 0.0))
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			keyRatio.setData((val1)/ devident);
		}
		return keyRatio;
	}
	
	////////////////// Valuation Ratio
	
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
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(totalShares == null || totalShares == 0.0)
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
	
	public CompanyFinancialMINDTO t_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		if(totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_earnings(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		
		if(val1==null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_ex_cash_pe(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");

		if(val1==null || val1 == 0.0 || val2==null || val2 == 0.0 || val3==null || val3 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val3 = val3*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val3 = val3*2;
			}
			keyRatio.setData((val1 - val2) / val3);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_price_to_sales_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_price_to_book_value(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val1 = val1 == null ? 0.0 : val1;
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		
		if(val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_enterprise_value(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_pfd_stk") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_pfd_stk");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_min_int_accum") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_min_int_accum");
		Double val4 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val4 = val4 == null ? 0.0 : val4;
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		
		if(val1 == null || val5 == null)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 + val2 + val3 + val4 - val5);
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_ev_ebitda(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_enterprise_value(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val1 == null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
		
	}

	public CompanyFinancialMINDTO t_ev_ebit(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_enterprise_value(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val1==null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1 / val2);
		}
		return keyRatio;
		
	}

	public CompanyFinancialMINDTO t_ev_sales(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_enterprise_value(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");;

		if(val1==null || val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1 / val2);
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
	
	public CompanyFinancialMINDTO t_dividend_payout_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");

		if(val1 == null || val2 == null || val2 < 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_dividend_yield(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val2 == null || val2 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO ratioValuesAssignment(String companyId, BalanceModelDTO ratio, RatioCompanyData companyData, String currentDate, String currentApplicableDate, String periodType, int order, String latestAnnualDate, String unit, String currency) {
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
