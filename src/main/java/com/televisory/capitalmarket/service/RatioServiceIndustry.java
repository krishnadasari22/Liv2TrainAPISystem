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
public class RatioServiceIndustry {

	//@Autowired
	//CMStockService cmStockService;
	
	Logger _log = Logger.getLogger(RatioServiceIndustry.class);
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public CompanyFinancialMINDTO t_ebit(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double sales9 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		Double cogs_ida10 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cogs");
		Double cogs_xda11 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs_xdep") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cogs_xdep");
		Double da12 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp");
		Double d13 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp");
		Double ai14 = industryRatio.get(companyId+"_"+currentDate+"_ff_amort_intang") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_amort_intang");
		Double ad15 = industryRatio.get(companyId+"_"+currentDate+"_ff_amort_dfd_chrg") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_amort_dfd_chrg");
		
		Double sga17 = industryRatio.get(companyId+"_"+currentDate+"_ff_sga") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sga");
		Double ooe20 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_exp_oth") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_oper_exp_oth");
		
		Double finalDa = null; 
		
		if(cogs_ida10<=0.0 || (cogs_ida10>0.0 && cogs_ida10==cogs_xda11)) {
			finalDa = cogs_xda11+ da12>0?da12:d13+ai14+ad15;
		}else {
			finalDa = cogs_ida10;
		}
		
		Double ff_gross_inc = industryRatio.get(companyId+"_"+currentDate+"_ff_gross_inc");
		if(ff_gross_inc == null)
			keyRatio.setData(industryRatio.get(companyId+"_"+currentDate+"_ff_ebit_oper"));
		else
			keyRatio.setData(sales9 - finalDa - sga17 - ooe20);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_ebitda(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double sales9 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");

		Double cogs_ida10 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cogs");
		Double cogs_xda11 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs_xdep") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_cogs_xdep");
		Double da12 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp");
		Double d13 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp");
		Double ai14 = industryRatio.get(companyId+"_"+currentDate+"_ff_amort_intang") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_amort_intang");
		Double ad15 = industryRatio.get(companyId+"_"+currentDate+"_ff_amort_dfd_chrg") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_amort_dfd_chrg");
		
		Double sga17 = industryRatio.get(companyId+"_"+currentDate+"_ff_sga") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sga");
		Double ooe20 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_exp_oth") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_oper_exp_oth");
		
		Double finalDa = null; 
		
		if(cogs_xda11<=0.0 || (cogs_xda11>0.0 && cogs_xda11==cogs_ida10)) {
			finalDa = cogs_ida10+ da12>0?da12:d13+ai14+ad15;
		}else {
			finalDa = cogs_xda11;
		}
		
		Double ff_gross_inc = industryRatio.get(companyId+"_"+currentDate+"_ff_gross_inc");
		if(ff_gross_inc == null)
			keyRatio.setData(industryRatio.get(companyId+"_"+currentDate+"_ff_ebitda_oper"));
		else
			keyRatio.setData(sales9 - finalDa - sga17 - ooe20);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_working_capital(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr");
		
		Double finalVal = val1-val2;
		keyRatio.setData(finalVal);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_free_cash_flow(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_capex")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_capex");
		
		if(val4== null || val1 == null) {
			keyRatio.setData(null);
		}else {
			Double finVal = val1+val2*(1-val3/val4)-val5;
			keyRatio.setData(finVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_gross_profit_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_gross_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_gross_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_ebitda_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_ebit_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_pbt_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_pat_margin(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_roe(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		
		if(val2==null || val2==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		if(index+1==availPeriods.size()) {
			finalVal = val1/(val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2;
			finalVal = val1/(avg);
		}
		
		keyRatio.setData(finalVal);
		/*keyRatio.setKeyParameter(1);*/
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_roce(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		//Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ebit_oper")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ebit_oper");
		Double val1 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr");
		
		Double finalVal = val2-val3;
		if(finalVal==0.0) {
			keyRatio.setData(finalVal);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			finalVal = val1/(val2-val3);
			keyRatio.setData(finalVal);
		}
		/*keyRatio.setKeyParameter(1);*/
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
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
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
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
		keyRatio.setData(finalVal);
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_debtor_days(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_trade_receivable_turnover(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1 == null || val1 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(365/val1);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_inventory_days(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_inventory_turnover(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1 == null || val1 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(365/val1);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_creditor_days(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_trade_payables_turnover(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		if(val1 == null || val1 == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(365/val1);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cash_conversion_cycle(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = t_debtor_days(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_inventory_days(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val3 = t_creditor_days(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		val1 = val1==null?0.0:val1;
		val2 = val2==null?0.0:val2;
		val3 = val3==null?0.0:val3;
		
		keyRatio.setData(val1 + val2 - val3);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_trade_receivable_turnover(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_receiv_net");
		if(val2==null || val2==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		if(index+1==availPeriods.size()) {
			finalVal = val1/(val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_receiv_net");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2;
			finalVal = val1/(avg);
		}
		keyRatio.setData(finalVal);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_inventory_turnover(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cogs");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_inven");
		if(val2==null || val2==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		if(index+1==availPeriods.size()) {
			finalVal = val1/(val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_inven");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2;
			finalVal = val1/(avg);
		}
		keyRatio.setData(finalVal);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_trade_payables_turnover(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cogs");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_pay_acct");
		if(val2==null || val2==0.0) {
			keyRatio.setData(null);
			return keyRatio;
		}
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			val1 = val1*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			val1 = val1*2;
		}
		if(index+1==availPeriods.size()) {
			finalVal = val1/(val2);
		}else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_pay_acct");
			if(val3==null ) {
				val3=0.0;
			}
			Double avg = (val2+val3)/2;
			finalVal = val1/(avg);
		}
		keyRatio.setData(finalVal);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_working_capital_turnover(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		Double val2 = t_working_capital(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val1 = val1*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val1 = val1*2;
			}
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cogs_sales(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cogs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cogs");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_sga_sales(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sga")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sga");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_current_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_quick_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_inven")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_inven");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr");
		
		Double finalVal = val1-val2;
		if(finalVal==0.0) {
			keyRatio.setData(finalVal);
		}else {
			finalVal = finalVal/(val3);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_liquid_assets_tot_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		
		if(val2==0.0)
			keyRatio.setData(null);
		else 
			keyRatio.setData(val1/val2);

		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_liquid_assets_st_debt(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		
		if(val1==null || (val2==null || val2 == 0.0)) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_liquid_assets_tot_cur_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets_curr");
		
		if(val1==null || val2==null || val2==0.0) {
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
	
	public CompanyFinancialMINDTO t_liquid_assets_adj_cur_liab(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_pay_acct")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_pay_acct");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_pay_tax")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_pay_tax");
		if(val1==0.0) {
			keyRatio.setData(0.0);
		}else {
			Double finalVal = val2+val3+val4;
			if(finalVal==0.0) {
				keyRatio.setData(0.0);
			}else {
				finalVal = val1/(finalVal);
				keyRatio.setData(finalVal);
			}
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_fin_viability(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double ff_sga = industryRatio.get(companyId+"_"+currentDate+"_ff_sga")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sga");
		Double ff_int_exp_net = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double ff_cash_st = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double ff_receiv_net = industryRatio.get(companyId+"_"+currentDate+"_ff_receiv_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_receiv_net");
		Double ff_inven = industryRatio.get(companyId+"_"+currentDate+"_ff_inven")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_inven");
		Double ff_pay_acct = industryRatio.get(companyId+"_"+currentDate+"_ff_pay_acct")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_pay_acct");
		
		Double ff_debt_lt_curr;
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			ff_debt_lt_curr = 0.0;
		} else {
			String prevDate = availPeriods.get(index+1); 
			ff_debt_lt_curr = industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr");
		}
		
		Double val1 = ff_cash_st+0.8*(ff_receiv_net+ff_inven)-ff_pay_acct;
		
		if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
			ff_sga = ff_sga*4;
			ff_int_exp_net = ff_int_exp_net*4;
		}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
			ff_sga = ff_sga*2;
			ff_int_exp_net = ff_int_exp_net*2;
		}
		
		Double val2 = ff_debt_lt_curr+ff_int_exp_net+ff_sga;
		
		if(val1==null || (val2==null || val2 == 0.0)) 
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_liquid_assets_cur_liab(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs_curr");
		
		if(val1==null || (val2==null || val2 == 0.0)) 
			keyRatio.setData(null);
		else
			keyRatio.setData(val1 / val2);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_ebit_debt_ser_amount(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_debt_servicing_amount(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || (val2==null || val2 == 0.0)) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_intr_coverage_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		//Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ebit_oper")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ebit_oper");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_debt_servicing_amount(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double val2 = 0.0;
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(val1);
		} else {
			String prevDate = availPeriods.get(index+1); 
			val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr");
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2/4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2/2;
			}
		}
		
		keyRatio.setData(val1+val2);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_debt_st_gross_debt(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		Double val2 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val2==null || val2 == 0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2)*100;
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	
	public CompanyFinancialMINDTO t_altman_z_score(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		//Section A (1.2*t_working_capital/ff_assets)
		Double t_working_capital = t_working_capital(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double ff_assets = industryRatio.get(companyId+"_"+currentDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		t_working_capital = t_working_capital == null ? 0.0 : t_working_capital;
		Double A = 0.0;
		if(ff_assets != 0.0)
			A = 1.2 * t_working_capital/ff_assets;
		
		//Section B (1.4*(ff_net_inc_basic-ff_div_com_cf)/ff_assets)
		Double ff_net_inc_basic = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		Double ff_div_com_cf = null;
		
		ff_div_com_cf = industryRatio.get(companyId+"_"+currentDate+"_ff_div_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_div_cf");
		
		Double B = 0.0;
		if(ff_assets != 0.0) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				B = 1.4*(ff_net_inc_basic-ff_div_com_cf)*4/ff_assets;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				B = 1.4*(ff_net_inc_basic-ff_div_com_cf)*2/ff_assets;
			} else
				B = 1.4*(ff_net_inc_basic-ff_div_com_cf)/ff_assets;
			
		}
		
		//Section C (3.3*t_ebit/ff_assets)
		Double t_ebit = t_ebit(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		t_ebit = t_ebit == null ? 0.0 : t_ebit;
		Double C = 0.0;
		if(ff_assets != 0.0) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				C = 3.3 * t_ebit*4/ff_assets;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				C = 3.3 * t_ebit*2/ff_assets;
			} else
				C = 3.3 * t_ebit/ff_assets;
		}
		
		//Section D (0.6*t_mcap/ff_assets)
		Double t_mcap = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		t_mcap = t_mcap == null ? 0.0 : t_mcap;
		Double D = 0.0;
		if(ff_assets != 0.0)
			D = 0.6 * t_mcap/ff_assets;
		
		//Section E (0.99*ff_sales/G104)
		Double ff_sales = industryRatio.get(companyId+"_"+currentDate+"_ff_sales")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		Double E = 0.0;
		if(ff_assets != 0.0) {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				E = 0.99 * ff_sales*4/ff_assets;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				E = 0.99 * ff_sales*2/ff_assets;
			} else
				E = 0.99 * ff_sales/ff_assets;
		}
		
		keyRatio.setData(A+B+C+D+E);
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_financial_leverage(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		
		Double finalVal = 0.0;
		int index = availPeriods.indexOf(currentDate);
		
		if(index+1==availPeriods.size()) {
			if(val2==null || val2==0.0) {
				keyRatio.setData(null);
				return keyRatio;
			}
			finalVal = val1/val2;
		} else {
			String prevDate = availPeriods.get(index+1); 
			
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val3==null ) {
				val3=0.0;
			}
			Double avgAssets = (val1+val3)/2;
			
			Double val4 = industryRatio.get(companyId+"_"+prevDate+"_ff_eq_tot");
			if(val4==null ) {
				val4=0.0;
			}
			Double avgeq = (val2+val4)/2;
			
			finalVal = avgAssets/avgeq;
		}
		keyRatio.setData(finalVal);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cash_mcap(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val2 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || val2==null || val2 == 0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2)*100;
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_debt_serv_coverage_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		//Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ebitda_oper")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ebitda_oper");
		Double val1 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_debt_servicing_amount(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || val2==null || val2 == 0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_ebitda_intr_exp(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		//Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_ebitda_oper")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ebitda_oper");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cads_intr_expenses(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_cash_for_debt_serv(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	
	public CompanyFinancialMINDTO t_accr_debt_lt_curr(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp");
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size() || val2 == null) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_debt_lt_curr");
			if(val3==null || val3==0.0) { 
				keyRatio.setData(null);
			}else {
				
				Double finalVal = (val1+val2)/(val3);
				
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					finalVal = finalVal*4;
				}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
					finalVal = finalVal*2;
				}
				
				keyRatio.setData(finalVal);
			}
		}
		
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_accr_debt_lt(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_amort_exp");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_lt");
		
		if(val2==null || val3==null || val3==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = (val1+val2)/(val3);
			
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				finalVal = finalVal*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				finalVal = finalVal*2;
			}
			
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cash_for_debt_serv(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp_cf");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_wkcap_chg");
		
		if(val4==null) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1+val2+val3+val4);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_gross_debt(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt_st");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_debt_lt")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_debt_lt");
		keyRatio.setData(val1+val2);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_gross_debt_ebitda(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_ebitda(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_total_liabs_to_total_assets_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_gross_debt_equity(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);

		Double val1 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_tangible_networth_debt_serv_amount(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = t_debt_servicing_amount(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	
	public CompanyFinancialMINDTO t_total_liabs_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double val2 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(val2);
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_tangible_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_intang")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_intang");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		
		if(val4==null) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val2-val3-val4);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_networth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");

		if(val1==null || val2==null) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1 - val2);
		}
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
				keyRatio.setData((val1 - val2)/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
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
				keyRatio.setData((val1 - val2)/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_total_assets_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");

		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size() || val1==null) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			if(val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			} else
				keyRatio.setData((val1 - val2)/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tot_tangible_assets_growth(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_tangible_networth(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size() || val1==null) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = t_tangible_networth(companyId, industryRatio,closePrice, totalShares, availPeriods, prevDate, prevDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
			if(val2==null || val2==0.0 ) { 
				keyRatio.setData(null);
			}else
				keyRatio.setData((val1 - val2)/val2);
		}
		if(keyRatio.getData()!=null) {
			keyRatio.setData(keyRatio.getData()*100);
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
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_consol_net_inc");
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
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
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
			if((val4 == null || val5 == null))
				devident = null;
			else
				devident = (val4 + val5) / 2;

		}
		if(devident == null || devident == 0.0 || val4==null || val1 == null)
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
	
	public CompanyFinancialMINDTO t_sales_total_assets(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		
		Double devident = val2;
		
		/*int index = availPeriods.indexOf(currentDate);
		if(availPeriods.size() > index + 1) {
			
			String prevDate = availPeriods.get(index + 1);
			Double val5 = industryRatio.get(companyId+"_"+prevDate+"_ff_assets");
			
			if((val2 != null && val5 != null))
				devident = (val2 + val5) / 2;
			if((val2 == null && val5 == null))
				devident = null;
			if(val2 != null)
				devident = val2;
			else
				devident = val5;

		}*/
		if(devident == null || devident == 0.0 || val2==null)
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
	
	
	public CompanyFinancialMINDTO t_sales_working_capital(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		Double devident = t_working_capital(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();	
		
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
	
	public CompanyFinancialMINDTO t_sales_ppe_gross(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		
		Double devident = industryRatio.get(companyId+"_"+currentDate+"_ff_ppe_gross")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_ppe_gross");	
		
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
	
	
	// Vatuation Ratios
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
			keyRatio.setData(val1/totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_cash_eps(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp_cf");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_dfd_tax_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_dfd_tax_cf");
		//Double val4 = (Double) cmStockService.getCompanyTotalSharesByPeriod(companyId,currentDate).get(0).get("total_shares");
		if(totalShares==null || totalShares==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = (val1 + val2 + val3)/(totalShares) * CMStatic.UNIT_FACTOR_MILLION;
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
		
	public CompanyFinancialMINDTO t_dividend_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		
		if(val1==null || totalShares==null || totalShares==0.0) {
			keyRatio.setData(null);
		}else {
			Double finalVal = val1/(totalShares) * CMStatic.UNIT_FACTOR_MILLION;
			keyRatio.setData(finalVal);
		}
		return keyRatio;
	}
		
	public CompanyFinancialMINDTO t_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_eq_tot");
		if(totalShares==null || totalShares==0.0) {
			keyRatio.setData(null);
		}else {
			keyRatio.setData(val1/totalShares * CMStatic.UNIT_FACTOR_MILLION);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_tangible_book_value_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_assets")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_assets");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_intang")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_intang");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_liabs")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_liabs");
		Double finVal = val1-val2-val3;
		if (totalShares == null || totalShares == 0.0)
			keyRatio.setData(null);
		else
			keyRatio.setData(finVal/totalShares * CMStatic.UNIT_FACTOR_MILLION);
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_free_cash_flow_per_share(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_cf");
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_capex")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_capex");
		
		if(val1==null || val4==null) {
			keyRatio.setData(null);
		}else {
			Double finVal = val1+val2*(1-val3/val4)-val5;
			if (totalShares == null || totalShares == 0.0)
				keyRatio.setData(null);
			else
				keyRatio.setData(finVal/totalShares * CMStatic.UNIT_FACTOR_MILLION);
			
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_price_earnings(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(val1==null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1/val2);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_ex_cash_pe(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
		if(val1==null || val2==null ||val3==null) {
			keyRatio.setData(null);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val3 = val3*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val3 = val3*2;
			}
			keyRatio.setData((val1-val2)/val3);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_to_cash_eps(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_cf");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_dep_exp_cf");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_dfd_tax_cf")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_dfd_tax_cf");
		
		Double val5 = (val2 + val3 + val4);
		
		if(val1==null || val5==0.0){
				keyRatio.setData(null);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val5 = val5*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val5 = val5*2;
			}
			keyRatio.setData(val1/val5);
		}
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_to_sales_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_sales");
		if(val1 == null || val2==null || val2==0.0) {
			keyRatio.setData(null);
		}else {
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				val2 = val2*4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				val2 = val2*2;
			}
			keyRatio.setData(val1/val2);
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
			keyRatio.setData(val1/val2);
		}
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_price_to_free_cash_flow(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_cf");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax");
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val6 = industryRatio.get(companyId+"_"+currentDate+"_ff_capex")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_capex");
		
		if(val1==null || val2==null || val5==null) {
			keyRatio.setData(null);
		}else {
			Double finVal = (val2+val3*(1-val4/val5)-val6);
			if(finVal==0.0) {
				keyRatio.setData(null);
			}else {
				if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
					finVal = finVal*4;
				}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
					finVal = finVal*2;
				}
				keyRatio.setData(val1/finVal);
			}
		}
		
		return keyRatio;
	}

	public CompanyFinancialMINDTO t_enterprise_value(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_min_int_accum") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_min_int_accum");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_pfd_stk") == null ? 0.0 : industryRatio.get(companyId+"_"+currentDate+"_ff_pfd_stk");
		Double val4 = t_gross_debt(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		val4 = val4 == null ? 0.0 : val4;
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_cash_st");
		
		if(val1 == null || val5 == null)
			keyRatio.setData(null);
		else {
			keyRatio.setData(val1 + val2 + val3 + val4 - val5);
		}
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
		/*keyRatio.setKeyParameter(1);*/
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
		/*keyRatio.setKeyParameter(1);*/
		return keyRatio;
		
	}
	
	public CompanyFinancialMINDTO t_ev_fcf(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		Double val1 = t_enterprise_value(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		
		Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_oper_cf");
		Double val3 = industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_int_exp_net");
		Double val4 = industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_inc_tax");
		Double val5 = industryRatio.get(companyId+"_"+currentDate+"_ff_ptx_inc");
		Double val6 = industryRatio.get(companyId+"_"+currentDate+"_ff_capex")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_capex");
		
		if(val1 == null || val2==null || val5==null) {
			keyRatio.setData(null);
		}else {
			Double finVal = (val2+val3*(1-val4/val5)-val6);
			if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_QUARTERLY)) {
				finVal *= 4;
			}else if(periodType.equalsIgnoreCase(CMStatic.PERIODICITY_HALF_YEARLY)) {
				finVal *= 2;
			}
			if(finVal != 0.0)
				keyRatio.setData(val1/finVal);
		}
		return keyRatio;
	}
	
	public CompanyFinancialMINDTO t_peg_ratio(String companyId, HashMap<String, Double> industryRatio, Double closePrice, Double totalShares, List<String> availPeriods, String currentDate, String currentApplicableDate, String periodType, int order, String currency, String unit, String latestAnnualDate, BalanceModelDTO ratio, RatioCompanyData companyData) {
		
		CompanyFinancialMINDTO keyRatio = ratioValuesAssignment(companyId, ratio, companyData, currentDate, currentApplicableDate, periodType, order, latestAnnualDate, unit, currency);
		
		Double val1 = t_price_earnings(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();
		//val1 = val1==null?0.0:val1;
		
		int index = availPeriods.indexOf(currentDate);
		if(index+1==availPeriods.size()) {
			keyRatio.setData(null);
		} else {
			String prevDate = availPeriods.get(index+1); 
			Double val2 = industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+currentDate+"_ff_net_inc_basic");
			Double val3 = industryRatio.get(companyId+"_"+prevDate+"_ff_net_inc_basic")==null?0.0:industryRatio.get(companyId+"_"+prevDate+"_ff_net_inc_basic");
			if(val1==null || val2==null || val2==0.0 || val3==null || val3==0.0) { 
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

		if(val1==null || val2 == null || val2 < 0.0 || val1==0.0 || val2==0.0)
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
		Double val1 = industryRatio.get(companyId+"_"+currentDate+"_ff_div_com_cf");
		Double val2 = t_mcap(companyId, industryRatio, closePrice, totalShares, availPeriods, currentDate, currentApplicableDate, periodType, order, currency, unit, latestAnnualDate, ratio, companyData).getData();

		if(val1==null || val2 == null || val2 == 0.0)
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
