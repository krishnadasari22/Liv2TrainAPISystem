package com.televisory.utils;

import java.util.Arrays;
import java.util.List;

import com.televisory.capitalmarket.util.CMStatic;

public interface LoanStatic {
	public static final String APPENDIX = "APPENDIX";
	public static final String FINANCIAL_SNAPSHOT_MODULE = "FINANCIAL_SNAPSHOT";

	public static final String BALANCE_SHEET = "BS";
	public static final String CASH_FLOW = "CF";
	public static final String PROFIT_AND_LOSS = "IS";
	public static final String FINANCIAL_RATIOS = "FR";

	public static final String LOAN_JASPER_PATH = "/data/resources/JASPER_TEMPLATES/CREDIT_REPORT/";
	public static final String LOAN_MAIN_FILE_PATH = "/data/resources/JASPER_TEMPLATES/CREDIT_REPORT/Credit_Rating_Report_Main"+CMStatic.JASPER_EXTENSION;
	public static final String FINANCIAL_SNAPSHOT_AND_TOC_MAIN_FILE_PATH = "/data/resources/JASPER_TEMPLATES/CREDIT_REPORT/Toc_Main"+CMStatic.JASPER_EXTENSION;
	public static final String LOAN_REPORT_FILE_PATH = "/data/CREDIT_REPORT_DOWNLOAD/";
	public static final String LOAN_REPORT_IMAGES = "/data/resources/Images/";
	//////////////////////////////////////////////////////
	
	public static final String PORTFOLIO = "portfolio";
	public static final String PORTFOLIO_LOAN_JASPER_PATH = "/data/resources/JASPER_TEMPLATES/PORTFOLIO_CREDIT_REPORT/";
	public static final String PORTFOLIO_LOAN_MAIN_FILE_PATH = "/data/resources/JASPER_TEMPLATES/PORTFOLIO_CREDIT_REPORT/Credit_Rating_Report_Main"+CMStatic.JASPER_EXTENSION;
	public static final String PORTFOLIO_FINANCIAL_SNAPSHOT_AND_TOC_MAIN_FILE_PATH = "/data/resources/JASPER_TEMPLATES/PORTFOLIO_CREDIT_REPORT/Toc_Main"+CMStatic.JASPER_EXTENSION;
	public static final String PORTFOLIO_LOAN_REPORT_FILE_PATH = "/data/PORTFOLIO_CREDIT_REPORT_DOWNLOAD/";
	
	
	
	//////////////////////////////////////////////////////
	

	public final static List<String> FINANCIAL_SNAPSHOT = Arrays.asList("ff_sales","t_ebitda","t_ebitda_margin","ff_net_inc_basic","t_pat_margin","t_gross_debt_equity","t_intr_coverage_ratio");

	public final static String DEBT_TENOR = "debt_tenor";
	public final static String AVERAGE_INTEREST_COST = "t_interest_cost";
	public final static String REFINANCIABLE_DEBT = "refinanceable_debt";
	public final static String BORROWING = "t_gross_debt";
	public final static String TOTAL_SUSTAINABLE_DEBT = "t_debt_size";


	public final static String CAPEX = "t_sales_ppe_gross";
	public final static List<String> SENSITIVITY_CHART_1 = Arrays.asList("t_debt_serv_coverage_ratio_minus","t_debt_serv_coverage_ratio_plus","t_debt_serv_coverage_ratio_actual");
	public final static List<String> SENSITIVITY_CHART_2 = Arrays.asList("ff_oper_cf_minus","ff_oper_cf_actual","ff_oper_cf_plus");

	public final static String EQUITY_INFUSION = "t_stk_chg_cf";
	public final static String DEBT_SERVICING_AMOUNT = "t_debt_servicing_amount";
	public final static String OPERATING_CASH_FLOW = "t_oper_cf"; 


	public final static List<String> PERFORMANCE_AND_ACTIVITY_RATIOS = Arrays.asList("t_net_sales_growth","t_debtor_days","t_inventory_days","t_creditor_days","t_cash_conversion_cycle","t_sales_working_capital","t_sales_total_assets","t_fin_viability");
	public final static List<String> PROFITABILITY_AND_RETURN_RATIOS = Arrays.asList("t_gross_profit_margin","t_ebitda_margin","t_ebit_margin","t_pat_margin","t_roa","t_roce","t_roe");
	public final static List<String> LEVERAGE_RATIO = Arrays.asList("t_networth","t_gross_debt","t_debt_st_gross_debt","t_gross_debt_ebitda","t_gross_debt_equity","t_total_liabs_tangible_networth","t_total_liabs_to_total_assets_ratio");
	public final static List<String> LIQUIDITY_RATIO = Arrays.asList("t_current_ratio","t_quick_ratio","t_liquid_assets_cur_liab","t_intr_coverage_ratio");
	public final static List<String> DEBT_COVERAGE_RATIO = Arrays.asList("t_debt_serv_coverage_ratio","t_accr_debt_lt_curr","t_accr_debt_lt");

	public final static String DEBT_SIZING_SCORE = "t_debt_size_score";
	
}
