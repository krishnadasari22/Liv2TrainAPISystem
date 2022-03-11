package com.televisory.capitalmarket.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMStatic {

	public final static String ENTITY_TYPE_PUBLIC = "PUBLIC";
	public final static String ENTITY_TYPE_PRIVATE = "PRIVATE";

	public final static String FACTSET_STOCK_DEFAULT_START_DATE = "1970-12-31";

	public final static String EXCHANGE_DEFAULT_START_DATE = "2011-03-31";
	public final static String START_DATE = "Start Date";
	public final static String END_DATE = "End Date";
	public final static String PERIODICITY = "Periodicity";
	public final static String DATA_TYPE = "Data Type";
	public final static String COUNTRY = "Country";
	public final static String CURRENCY = "Currency";
	public final static String UNIT = "Unit";
	public final static String DATE = "Date";
	public final static String HIGH = "HIGH";
	public final static String LOW = "LOW";
	public final static String CLOSE = "CLOSE";
	public final static String OPEN = "OPEN";
	public final static String COUNTRIES = "Country";
	public final static String PARAMETERS = "Parameters";
	public final static Integer SEARCH_DEFAULT_COUNT = 5;

	public final static String SHEET_STOCK_PRICE_INDEX = "Index Movement";
	public final static String SHEET_STOCK_PRICE_COMPANY = "Company Share Price";
	public final static String SHEET_FINANCIAL = "Financials";
	public final static String SHEET_MACRO_ECONOMIC_METRIC = "Macro Economic-Metric";
	public final static String SHEET_MACRO_ECONOMIC_COUNTRY = "Macro Economic-Country";
	public final static String SHEET_COMPANY_BETA = "Company Beta";
	public final static String SHEET_COMPANY_EVENTS = "Events-And-Filings-Data";
	public final static String SHEET_INSIDER_TRANSACTION = "Insider Transactions";

	public final static String SHEET_MNA_DEAL_TERM_SYNOPSIS = "Deal Terms & Synopsis";

	public final static String TYPE_COMPANY = "company";
	public final static String TYPE_INDEX = "index";
	public final static String TYPE_ECONOMY_INDICATOR = "economyIndicator";
	public final static String TYPE_ECONOMY_COUNTRY = "economyCountry";

	public final static String ECONOMY_GDP = "GDP";
	public final static String ECONOMY_CPI = "CPI";
	public final static String ECONOMY_WPI = "WPI";
	public final static String ECONOMY_IMPORT = "IMPORT";
	public final static String ECONOMY_EXPORT = "EXPORT";
	public final static String ECONOMY_POLICY_REPO_RATE = "POLICY REPO RATE";
	public final static String ECONOMY_REVERSE_REPO_RATE = "REVERSE REPO RATE";
	public final static String ECONOMY_FX = "FX";
	public final static String ECONOMY_FOREX_RESERVE = "FOREX RESERVES";
	public final static String ECONOMY_IIP = "IIP";
	public final static String ECONOMY_CREDIT_RATING = "SOVEREIGN RATING";
	public final static String ECONOMY_COUNTRY_DEFAULT_SPREAD = "DEFAULT SPREAD";
	public final static String ECONOMY_COUNTRY_RISK_PREMIUM = "COUNTRY RISK PREMIUM";
	public final static String ECONOMY_EQUITY_RISK_PREMIUM = "EQUITY RISK PREMIUM";
	public final static String ECONOMY_CALL_MONEY = "CALL MONEY";

	public final static String DATA_TYPE_STOCK_PRICE = "stockPrice";
	public final static String DATA_TYPE_BETA = "beta";
	public final static String DATA_TYPE_BALANCE_SHEET = "balanceSheet";
	public final static String DATA_TYPE_PNL = "PNL";
	public final static String DATA_TYPE_CASH_FLOW = "cashFlow";
	public final static String COMMODITY = "Commodity";
	public final static String SHEET_COMMODITY_PRICE = "Commodity PRICE";
	public final static String SHEET_COMMODITY_DATA_DOWNLOAD = "Commodity";

	public final static String DATA_TYPE_BALANCE_SHEET_CODE = "BS";
	public final static String DATA_TYPE_IS_CODE = "IS";
	public final static String DATA_TYPE_CASH_FLOW_CODE = "CF";
	public final static String DATA_TYPE_VALUATION_RATIO_CODE = "VR";
	public final static String DATA_TYPE_FINANCIAL_RATIO_CODE = "FR";
	public final static String DATA_TYPE_FINANCIAL_ALL_CODE = "ALL";
	public final static String DATA_TYPE_FINANCIAL_ALL_RATIOS_CODE = "ALL-RATIOS";

	public final static String PERIODICITY_DAILY = "DAILY";
	public final static String PERIODICITY_WEEKLY = "WEEKLY";
	public final static String PERIODICITY_MONTHLY = "MONTHLY";
	public final static String PERIODICITY_QUARTERLY = "QUARTERLY";
	public final static String PERIODICITY_HALF_YEARLY = "SEMIANN";
	public final static String PERIODICITY_YEARLY = "YEARLY";

	public final static Boolean KEEP_CM_REPORT = true;
	public final static String QUARTER_END = "Quarter Ended";
	public final static String YEAR_END = "Year Ended";
	public final static String DAILY = "Daily";
	public final static String WEEKLY = "Weekly";
	public final static String MONTHLY = "Monthly";
	public final static String HALF_YEAR_END = "Half Year Ended";

	public final static String NA = "-";
	public final static String SPECIAL_CHAR_REGEX = "[(.\\[\\]\\(\\)/)?]";
	public final static String COMPANY_REMOVE_SPECIAL_CHAR_REGEX = "[?]";
	public final static String NO_DATA_FOUND = "NO DATA FOUND";
	public final static Integer SHEET_MAX_ROW = 2000;
	public final static Integer SHEET_MAX_COLUMN = 250;
	public final static Integer SHEET_ADD_ROW = 200;
	public final static String BALANCE_SHEET = "Balance Sheet";
	public final static String INCOME_STATEMENT = "Income Statement";
	public final static String CASH_FLOW = "Cash Flow";
	public final static String SHARE_PRICE_TEXT = "Share Price";
	public final static String NUMERIC_REGEX = "[0-9]";
	public final static String INDEX_MOVEMENT_TEXT = "INDEX Movement";
	public final static String USD_TEXT = "Vs USD";
	public final static String BETA = "Beta";
	public final static String YEARS = "Years";

	public final static String VALUATION_RATIO = "Valuation Ratios";
	public final static String FINANCIAL_RATIO = "Financial Ratios";

	public final static String RATIO_PE_FINANCIAL_FIELD = "ff_net_inc_basic";
	public final static String RATIO_DY_FINANCIAL_FIELD = "ff_div_com_cf";
	public final static String RATIO_BV_FINANCIAL_FIELD = "ff_eq_tot";
	public final static String RATIO_CASH_MCAP_FINANCIAL_FIELD = "ff_cash_st";

	/*
	 * public final static Map<String,String> KEY_FINANCIALS_BANK =
	 * Stream.of(new String[][]{ {"ff_int_inc", "Interest Income"},
	 * {"ff_int_exp_tot", "Interest Expense"}, {"ff_oper_inc",
	 * "Operating Income"}, {"ff_loan_net", "Loan Book"}, {"ff_deps",
	 * "Total Deposits"}, {"ff_eq_tot", "Networth"}})
	 * .collect(Collectors.toMap(data -> data[0], data -> data[1]));
	 * 
	 * 
	 * 
	 * public final static Map<String,String> KEY_FINANCIALS_OTHERS =
	 * Stream.of(new String[][]{ {"ff_sales", "Revenue"}, {"ff_exp_tot",
	 * "Total Expense"}, {"ff_net_inc", "Net Income"}, {"ff_assets",
	 * "Total Assets"}, {"ff_debt", "Total Deposits"}, {"ff_eq_tot",
	 * "Networth"}}) .collect(Collectors.toMap(data -> data[0], data ->
	 * data[1]));
	 * 
	 * 
	 * public final static Map<String,String> KEY_FINANCIALS_INSURANCE =
	 * Stream.of(new String[][]{ {"ff_sales", "Total Income"}, {"ff_prem_earn",
	 * "Premium Income"}, {"ff_loss_claim_rsrv", "Losses, Claims & Reserves"},
	 * {"ff_sga_oth_exp", "SG&A Expense"}, {"ff_invest", "Total Investments"},
	 * {"ff_eq_tot", "Networth"}}) .collect(Collectors.toMap(data -> data[0],
	 * data -> data[1]));
	 * 
	 * 
	 * 
	 * public final static Map<String,String> KEY_FINANCIALS_INDUSTRY
	 * =Stream.of(new String[][]{ {"ff_sales", "Revenue"}, {"ff_cogs", "COGS"},
	 * {"ff_ebitda_oper", "EBITDA"}, {"ff_net_inc", "PAT"}, {"ff_oper_cf",
	 * "CFO"}, {"ff_eq_tot", "Networth"}}) .collect(Collectors.toMap(data ->
	 * data[0], data -> data[1]));
	 */
	public final static String INDUSTRY_INSURANCE = "Insurance";
	public final static String INDUSTRY_BANK = "Bank";
	public final static String INDUSTRY_IND = "Industrial";
	public final static String INDUSTRY_OTHER = "Other Financial";

	public final static String PEER_IGNORE = "Industry Average";

	public static final String DOUBLE_STAR = "**";

	public static final String TRIPLE_STAR = "***";

	public static final String DOUBLE_STAR_MSG = "** Delisted Company";

	public static final String TRIPLE_STAR_MSG = "*** Data not available for the selected period";

	/*
	 * public final static String INDUSTRY_INSURANCE_CODE="INS"; public final
	 * static String INDUSTRY_BANK_CODE="BANK"; public final static String
	 * INDUSTRY_IND_CODE="IND"; public final static String
	 * INDUSTRY_OTHER_CODE="OTH";
	 */

	/*
	 * public final static String
	 * INDUSTRY_TYPE_KEY_INDUSTRY_CODE="industry_code"; public final static
	 * String INDUSTRY_TYPE_KEY_INDUSTRY_NAME="industry_name";
	 */

	/*
	 * public final static List<String> REPORT_INDUSTRY_RATIOS_PROFITABILITY =
	 * Arrays.asList("Gross Profit Margin","EBITDA Margin","PAT Margin"
	 * ,"Return on Capital Employed"); public final static List<String>
	 * REPORT_INDUSTRY_RATIOS_LIQUIDITY =
	 * Arrays.asList("Debtor Days","Inventory Days","Creditor Days"
	 * ,"Cash Conversion Cycle","Current Ratio"); public final static
	 * List<String> REPORT_INDUSTRY_RATIOS_RETURN =
	 * Arrays.asList("Return on Assets","Return on Equity"); public final static
	 * List<String> REPORT_INDUSTRY_RATIOS_LEVERAGE =
	 * Arrays.asList("Interest Coverage Ratio","Gross Debt/EBITDA"
	 * ,"Gross Debt/Equity Ratio"); public final static List<String>
	 * REPORT_INDUSTRY_RATIOS_VALUATIONS =
	 * Arrays.asList("Price to Earnings Ratio","Price to Sales Ratio"
	 * ,"Price to Book Value","Price to Free Cash Flow", "PEG Ratio",
	 * "Dividend Yield","Dividend Payout Ratio","EV/EBITDA","EV/Sales");
	 * 
	 * public final static List<String> REPORT_BANK_RATIOS_PROFITABILITY =
	 * Arrays.asList("Net Interest Margin","Cost to Income"
	 * ,"Risk Adjusted Margin", "PBT Margin" ); public final static List<String>
	 * REPORT_BANK_RATIOS_LIQUIDITY =
	 * Arrays.asList("Liquid Asset Ratio","Loans/Deposits"); public final static
	 * List<String> REPORT_BANK_RATIOS_RETURN =
	 * Arrays.asList("Return on Assets", "Return on Equity"); public final
	 * static List<String> REPORT_BANK_RATIOS_RISK_MANAGEMENT =
	 * Arrays.asList("Tangible Networth",
	 * "Loan Provision/Total Assets","Debt to Assets"); public final static
	 * List<String> REPORT_BANK_RATIOS_VALUATIONS =
	 * Arrays.asList("Price to Earnings Ratio","Price to Income"
	 * ,"Price to Cash Flow","Price to Book Value","PEG Ratio","Dividend Yield"
	 * ,"Dividend Payout Ratio");
	 * 
	 * public final static List<String> REPORT_INSURANCE_RATIOS_PROFITABILITY =
	 * Arrays.asList("Loss Ratio","Combined Ratio","Investment Yield"
	 * ,"PBT Margin"); public final static List<String>
	 * REPORT_INSURANCE_RATIOS_LIQUIDITY =
	 * Arrays.asList("Liquid Assets to Reserves","Insurance Leverage"); public
	 * final static List<String> REPORT_INSURANCE_RATIOS_RETURN =
	 * Arrays.asList("Return on Assets","Return on Equity"); public final static
	 * List<String> REPORT_INSURANCE_RATIOS_SOLVENCY =
	 * Arrays.asList("Tangible Networth","Asset Under Management"
	 * ,"Insurance Liabilities"); public final static List<String>
	 * REPORT_INSURANCE_RATIOS_VALUATIONS =
	 * Arrays.asList("Price to Earnings Ratio","Price to Income"
	 * ,"Price to Book Value","Price to Cash Flow","PEG Ratio","Dividend Yield"
	 * ,"EV/EBIT");
	 * 
	 * public final static List<String> REPORT_OTHERS_RATIOS_PROFITABILITY =
	 * Arrays.asList("Net Interest Margin","Cost/Income","PBT Margin"
	 * ,"PAT Margin"); public final static List<String>
	 * REPORT_OTHERS_RATIOS_LIQUIDITY =
	 * Arrays.asList("Liquid Assets Ratio","Investments/Total Assets"); public
	 * final static List<String> REPORT_OTHERS_RATIOS_RETURN =
	 * Arrays.asList("Return on Average Assets","Return on Equity"); public
	 * final static List<String> REPORT_OTHERS_RATIOS_SOLVENCY =
	 * Arrays.asList("Tangible Net Worth","Total Liabilities/TNW"); //public
	 * final static List<String> REPORT_OTHERS_RATIOS_VALUATIONS =
	 * Arrays.asList("Price to Earnings Ratio","Price to Sales Ratio"
	 * ,"Price to Book Value","Price to Earnings Ratio","Dividend Yield"
	 * ,"EV/EBIT","Total Debt"); public final static List<String>
	 * REPORT_OTHERS_RATIOS_VALUATIONS =
	 * Arrays.asList("Price to Earnings Ratio","Price to Sales Ratio"
	 * ,"Price to Book Value","Price to Earnings Ratio","Dividend Yield"
	 * ,"Total Debt");
	 * 
	 * public final static List<String> REPORT_INDUSTRY_FINANCIALS =
	 * Arrays.asList("Sales/Revenue","EBITDA",
	 * "Net Income","Earnings per Share"); public final static List<String>
	 * REPORT_BANK_FINANCIALS =
	 * Arrays.asList("Interest Income","Non-Interest Income","Pretax Income"
	 * ,"Net Income","Total Interest Expense - Banks","Earnings per Share");
	 * public final static List<String> REPORT_INSURANCE_FINANCIALS =
	 * Arrays.asList("Sales/Revenue","Premiums Earned"
	 * ,"Losses, Claims & Reserves","Pretax Income","Net Income"
	 * ,"Earnings per Share"); public final static List<String>
	 * REPORT_OTHERS_FINANCIALS = Arrays.asList("Sales/Revenue"
	 * ,"Total Expense","Pretax Income"
	 * ,"Net Income","Total Investments - Banks","Earnings per Share");
	 * 
	 * public final static String REPORT_PERFORMANCE_PARAMETER_INDUSTRY_REVENUE
	 * = "Sales/Revenue"; public final static String
	 * REPORT_PERFORMANCE_PARAMETER_INDUSTRY_EBITDA = "EBITDA Margin";
	 * 
	 * public final static String REPORT_PERFORMANCE_PARAMETER_BANK_REVENUE =
	 * "Interest Income"; public final static String
	 * REPORT_PERFORMANCE_PARAMETER_BANK_EBITDA = "Net Interest Margin";
	 * 
	 * public final static String REPORT_PERFORMANCE_PARAMETER_INSURANCE_REVENUE
	 * = "Sales/Revenue"; public final static String
	 * REPORT_PERFORMANCE_PARAMETER_INSURANCE_EBITDA = "Combined Ratio";
	 * 
	 * public final static String REPORT_PERFORMANCE_PARAMETER_OTHERS_REVENUE =
	 * "Sales/Revenue"; public final static String
	 * REPORT_PERFORMANCE_PARAMETER_OTHERS_EBITDA = "PBT Margin";
	 */

	public final static List<String> REPORT_INDUSTRY_RATIOS_PROFITABILITY = Arrays.asList("t_gross_profit_margin",
			"t_ebitda_margin", "t_pat_margin", "t_roce");
	public final static List<String> REPORT_INDUSTRY_RATIOS_LIQUIDITY = Arrays.asList("t_debtor_days",
			"t_inventory_days", "t_creditor_days", "t_cash_conversion_cycle", "t_current_ratio");
	public final static List<String> REPORT_INDUSTRY_RATIOS_RETURN = Arrays.asList("t_roa", "t_roe");
	public final static List<String> REPORT_INDUSTRY_RATIOS_LEVERAGE = Arrays.asList("t_intr_coverage_ratio",
			"t_gross_debt_ebitda", "t_gross_debt_equity");
	public final static List<String> REPORT_INDUSTRY_RATIOS_VALUATIONS = Arrays.asList("t_price_earnings",
			"t_price_to_sales_ratio", "t_price_to_book_value", "t_price_to_free_cash_flow", "t_peg_ratio",
			"t_dividend_yield", "t_dividend_payout_ratio", "t_ev_ebitda", "t_ev_sales");

	public final static List<String> REPORT_BANK_RATIOS_PROFITABILITY = Arrays.asList("t_net_intr_margin",
			"t_cost_to_income", "t_risk_adjusted_margin", "t_pbt_margin");
	public final static List<String> REPORT_BANK_RATIOS_LIQUIDITY = Arrays.asList("t_liquid_assets", "t_loan_deposits");
	public final static List<String> REPORT_BANK_RATIOS_RETURN = Arrays.asList("t_roa", "t_roe");
	public final static List<String> REPORT_BANK_RATIOS_RISK_MANAGEMENT = Arrays.asList("t_tangible_networth",
			"t_loan_provision_tot_assest", "t_debt_assests");
	public final static List<String> REPORT_BANK_RATIOS_VALUATIONS = Arrays.asList("t_price_earnings", "t_price_income",
			"t_price_cashflow", "t_price_to_book_value", "t_peg_ratio", "t_dividend_yield", "t_dividend_payout_ratio");

	public final static List<String> REPORT_INSURANCE_RATIOS_PROFITABILITY = Arrays.asList("t_loss_ratio",
			"t_combined_ratio", "t_investment_yield", "t_pbt_margin");
	public final static List<String> REPORT_INSURANCE_RATIOS_LIQUIDITY = Arrays.asList("t_liquid_assets_to_reserves",
			"t_insurance_leverage");
	public final static List<String> REPORT_INSURANCE_RATIOS_RETURN = Arrays.asList("t_roa", "t_roe");
	public final static List<String> REPORT_INSURANCE_RATIOS_SOLVENCY = Arrays.asList("t_tangible_networth",
			"t_assets_under_management", "t_insurance_liabilities");
	public final static List<String> REPORT_INSURANCE_RATIOS_VALUATIONS = Arrays.asList("t_price_earnings",
			"t_price_income", "t_price_to_book_value", "t_price_cashflow", "t_peg_ratio", "t_dividend_yield",
			"t_ev_ebit");

	public final static List<String> REPORT_OTHERS_RATIOS_PROFITABILITY = Arrays.asList("t_net_intr_margin",
			"t_cost_to_income", "t_pbt_margin", "t_pat_margin");
	public final static List<String> REPORT_OTHERS_RATIOS_LIQUIDITY = Arrays.asList("t_liquid_assets",
			"t_investment_tot_assets");
	public final static List<String> REPORT_OTHERS_RATIOS_RETURN = Arrays.asList("t_roa", "t_roe");
	public final static List<String> REPORT_OTHERS_RATIOS_SOLVENCY = Arrays.asList("t_tangible_networth",
			"t_total_liabs_tangible_networth");
	// public final static List<String> REPORT_OTHERS_RATIOS_VALUATIONS =
	// Arrays.asList("Price to Earnings Ratio","Price to Sales Ratio","Price to
	// Book Value","Price to Earnings Ratio","Dividend Yield","EV/EBIT","Total
	// Debt");
	public final static List<String> REPORT_OTHERS_RATIOS_VALUATIONS = Arrays.asList("t_price_earnings",
			"t_price_to_sales_ratio", "t_price_to_book_value", "t_dividend_yield", "ff_debt");

	public final static List<String> REPORT_INDUSTRY_FINANCIALS = Arrays.asList("ff_sales", "t_ebitda", "ff_net_inc",
			"ff_eps_basic");
	public final static List<String> REPORT_BANK_FINANCIALS = Arrays.asList("ff_int_inc", "ff_non_int_inc",
			"ff_int_exp_tot", "ff_ptx_inc", "ff_net_inc", "ff_eps_basic");
	public final static List<String> REPORT_INSURANCE_FINANCIALS = Arrays.asList("ff_sales", "ff_prem_earn",
			"ff_loss_claim_rsrv", "ff_ptx_inc", "ff_net_inc", "ff_eps_basic");
	public final static List<String> REPORT_OTHERS_FINANCIALS = Arrays.asList("ff_sales", "ff_exp_tot", "ff_ptx_inc",
			"ff_net_inc", "t_aum_growth", "ff_eps_basic");

	public final static String REPORT_PERFORMANCE_PARAMETER_INDUSTRY_REVENUE = "ff_sales";
	public final static String REPORT_PERFORMANCE_PARAMETER_INDUSTRY_EBITDA = "t_ebitda_margin";
	public final static String REPORT_PERFORMANCE_PARAMETER_INDUSTRY_NET_PROFIT = "ff_net_inc";
	public final static String REPORT_PERFORMANCE_PARAMETER_INDUSTRY_ROE = "t_roe";

	public final static String REPORT_PERFORMANCE_PARAMETER_BANK_REVENUE = "ff_int_inc";
	public final static String REPORT_PERFORMANCE_PARAMETER_BANK_EBITDA = "t_net_intr_margin";
	public final static String REPORT_PERFORMANCE_PARAMETER_BANK_LOAN_BOOK = "t_loan_book_size";
	public final static String REPORT_PERFORMANCE_PARAMETER_BANK_ROA = "t_roa";

	public final static String REPORT_PERFORMANCE_PARAMETER_INSURANCE_REVENUE = "ff_sales";
	public final static String REPORT_PERFORMANCE_PARAMETER_INSURANCE_EBITDA = "t_combined_ratio";
	public final static String REPORT_PERFORMANCE_PARAMETER_INSURANCE_AUM = "ff_invest";
	public final static String REPORT_PERFORMANCE_PARAMETER_INSURANCE_ROA = "t_roa";

	public final static String REPORT_PERFORMANCE_PARAMETER_OTHERS_REVENUE = "ff_sales";
	public final static String REPORT_PERFORMANCE_PARAMETER_OTHERS_EBITDA = "t_pbt_margin";
	public final static String REPORT_PERFORMANCE_PARAMETER_OTHERS_AUM = "ff_invest";
	public final static String REPORT_PERFORMANCE_PARAMETER_OTHERS_ROA = "t_roa";

	public final static String RATIO_PROFITABILITY = "Profitability";
	public final static String RATIO_LIQUIDITY = "Liquidity";
	public final static String RATIO_LEVERAGE = "Leverage";
	public final static String RATIO_RETURN = "Return";
	public final static String RATIO_VALUATION = "Valuation";
	public final static String RATIO_RISK_MGMT = "Risk Management";
	public final static String RATIO_SOLVENCY = "Solvency";

	public final static int UNIT_FACTOR_HUNDRED = 100;
	public final static int UNIT_FACTOR_THOUSAND = 1000;
	public final static int UNIT_FACTOR_MILLION = 1000000;
	public final static int UNIT_FACTOR_BILLION = 1000000000;

	public final static String UNIT_THOUSAND = "THOUSAND";
	public final static String UNIT_MILLION = "MILLION";
	public final static String UNIT_BILLION = "BILLION";

	public final static String oneYear = "1 year";
	public final static String threeYear = "3 year";
	public final static String fiveYear = "5 year";

	public final static String JASPER_MAIN_FILE = "Company_Profile.jasper";
	public final static String COMPANY_PROFILE_FILE_NAME = "Company_Profile";
	public final static String INDUSTRY_MONTITOR_FILE_NAME = "Banking_Industry_Main";
	public final static String JASPER_EXTENSION = ".jasper";

	public final static List<String> dataTypeList = Arrays.asList(DATA_TYPE_PNL, DATA_TYPE_BALANCE_SHEET,
			DATA_TYPE_CASH_FLOW, DATA_TYPE_FINANCIAL_RATIO_CODE, DATA_TYPE_VALUATION_RATIO_CODE);

	// RATIOS FOR INDUSTRY MONITOR

	public final static String REPORT_MONITOR_FUNDAMENTAL = "fundamental";
	public final static String REPORT_MONITOR_VALUATION = "valuation";
	public final static String REPORT_MONITOR_ECONOMY = "economy";
	public final static String REPORT_MONITOR_TOPPERS = "topper";

	public final static List<String> REPORT_INDUSTRY_FUNDAMENTAL_MONITORS = Arrays.asList("ff_sales", "ff_gross_inc",
			"t_gross_profit_margin", "t_ebitda", "t_ebitda_margin", "ff_net_inc", "t_net_profit_margin", "ff_eps_basic",
			"t_roe", "t_roce", "t_roa", "t_debtor_days", "t_inventory_days", "t_creditor_days",
			"t_cash_conversion_cycle", "t_current_ratio", "t_quick_ratio", "t_sales_total_assets",
			"t_intr_coverage_ratio", "t_debt_serv_coverage_ratio", "t_gross_debt_equity", "t_gross_debt_ebitda");
	public final static List<String> REPORT_BANK_FUNDAMENTAL_MONITORS = Arrays.asList("ff_int_inc", "ff_int_exp_tot",
			"ff_int_inc_net", "t_net_intr_margin", "ff_non_int_inc", "ff_loan_loss_prov", "ff_ptx_inc", "t_pbt_margin",
			"ff_net_inc", "t_pat_margin", "t_cost_to_income", "t_fee_income_tot_income", "t_non_intr_income_tot_income",
			"t_deposit_growth", "ff_loan_net", "t_growth_loan_book", "t_liquid_assets", "t_loan_deposits",
			"t_gearing_ratio", "t_risk_adjusted_margin", "t_roe", "t_roa");
	public final static List<String> REPORT_INSURANCE_FUNDAMENTAL_MONITORS = Arrays.asList("ff_sales", "ff_prem_earn",
			"t_premium_growth", "ff_loss_claim_rsrv", "t_claims_payout_growth", "ff_sga", "ff_ptx_inc", "t_pbt_margin",
			"t_net_profit_margin", "ff_net_inc", "t_loss_ratio", "t_expense_ratio", "t_combined_ratio",
			"t_investment_yield", "ff_invest", "t_growth_in_investment", "t_liquid_assets_to_reserves",
			"t_insurance_leverage", "t_roa", "t_roe", "", "");
	public final static List<String> REPORT_OTHERS_FUNDAMENTAL_MONITORS = Arrays.asList("ff_sales", "ff_int_inc",
			"ff_invest_inc", "t_oth_oper_income", "t_intr_income_tot_income", "t_cost_to_income", "t_net_intr_margin",
			"ff_ptx_inc", "t_pbt_margin", "ff_net_inc", "t_pat_margin", "t_roe", "t_roa", "ff_invest", "t_aum_growth",
			"t_eq_tot", "t_total_liabs_tangible_networth", "t_gross_debt_equity", "t_intr_coverage_ratio",
			"t_liquid_assets", "t_debt_st_liquid_assets", "t_debt_st_tangible_networth");

	public final static List<String> REPORT_INDUSTRY_VALUATION_MONITORS = Arrays.asList("t_mcap", "t_price_earnings",
			"t_price_to_sales_ratio", "t_price_to_book_value", "t_price_to_free_cash_flow", "t_ev_ebitda",
			"t_peg_ratio", "t_dividend_payout_ratio");
	public final static List<String> REPORT_BANK_VALUATION_MONITORS = Arrays.asList("t_mcap", "ff_eps_basic",
			"t_price_earnings", "t_price_to_book_value", "t_price_income", "t_price_cashflow", "t_peg_ratio",
			"t_dividend_yield");
	public final static List<String> REPORT_INSURANCE_VALUATION_MONITORS = Arrays.asList("t_mcap", "ff_eps_basic",
			"t_price_earnings", "t_price_to_book_value", "t_price_income", "t_price_cashflow", "t_peg_ratio",
			"t_dividend_yield");
	public final static List<String> REPORT_OTHERS_VALUATION_MONITORS = Arrays.asList("t_mcap", "ff_eps_basic",
			"t_price_earnings", "t_price_to_book_value", "t_price_income", "t_price_cashflow", "t_peg_ratio",
			"t_dividend_yield");

	public final static List<String> REPORT_INDUSTRY_TOPPERS_LIST = Arrays.asList("t_mcap", "ff_sales",
			"t_ebitda_margin", "t_net_profit_margin", "t_roe", "t_roce", "t_cash_conversion_cycle",
			"t_gross_debt_ebitda");/*
									 * replaced ff_oper_cf to
									 * t_gross_debt_ebitda
									 */
	public final static List<String> REPORT_BANK_TOPPERS_LIST = Arrays.asList("t_mcap", "ff_sales", "t_net_intr_margin",
			"ff_ptx_inc", "t_deposit_growth", "ff_loan_net", "t_roa", "t_roe");
	public final static List<String> REPORT_INSURANCE_TOPPERS_LIST = Arrays.asList("t_mcap", "ff_prem_earn",
			"t_combined_ratio", "t_pbt_margin", "t_premium_growth", "t_growth_in_investment", "t_roa", "t_roe");
	public final static List<String> REPORT_OTHERS_TOPPERS_LIST = Arrays.asList("t_mcap", "ff_sales",
			"t_cost_to_income", "t_pbt_margin", "t_net_income_growth", "ff_invest", "t_roa", "t_roe");

	public final static List<String> REPORT_INDUSTRY_REGIONAL_GROWTH_MONITOR = Arrays.asList("ff_sales",
			"t_ebitda_margin", "t_net_profit_margin", "ff_eps_basic", "t_roe", "t_mcap", "t_price_earnings",
			"t_ev_ebitda", "t_price_to_book_value", "t_dividend_payout_ratio");
	public final static List<String> REPORT_BANK_REGIONAL_GROWTH_MONITOR = Arrays.asList("ff_loan_net", "ff_int_inc",
			"t_net_intr_margin", "t_pbt_margin", "ff_eps_basic", "t_roa", "t_mcap", "t_price_earnings",
			"t_price_to_book_value", "t_dividend_yield");
	public final static List<String> REPORT_INSURANCE_REGIONAL_GROWTH_MONITOR = Arrays.asList("ff_invest",
			"ff_prem_earn", "t_combined_ratio", "t_pbt_margin", "ff_eps_basic", "t_mcap", "t_roa", "t_price_earnings",
			"t_price_to_book_value", "t_dividend_yield");
	public final static List<String> REPORT_OTHERS_REGIONAL_GROWTH_MONITOR = Arrays.asList("ff_invest", "ff_sales",
			"t_cost_to_income", "t_pbt_margin", "ff_eps_basic", "t_roe", "t_mcap", "t_price_earnings",
			"t_price_to_book_value", "t_dividend_yield");

	/* Toppers list ascending order */
	public final static List<String> REPORT_TOPPERS_LIST_ORDER_ASC = Arrays.asList("t_cash_conversion_cycle",
			"t_gross_debt_ebitda", "t_combined_ratio", "t_cost_to_income");
	/*
	 * Ascending order will be followed first.Thereafter Negative numbers will
	 * be in descending order.
	 */
	public final static List<String> REPORT_TOPPERS_LIST_ORDER_ASC_AND_DESC_NEGATIVE_NUMBER = Arrays
			.asList("t_gross_debt_ebitda");

	// public final static List<Integer> REPORT_ECONOMY_MONITORS =
	// Arrays.asList(18,24,-1,1,22,32); //-1 for FX
	public final static List<Integer> REPORT_ECONOMY_MONITORS = Arrays.asList(16, 24, -1, 1, 22, 32); // -1
																										// for
																										// FX

	// gross domestic product ,nominal
	// consumer price index
	// fx
	// Central Bank Policy Rate
	// Exports of goods and services
	// Total Reserves
	//// the below mentioned REPORT_ECONOMY_MONITORS_TRADING_ECONOMICS is added
	// for the trading economic values
	public final static String EXPORTS = "Exports";
	public final static String GDP = "Gdp Constant Prices";
	public final static String GDP_DISPLAY_NAME = "GDP Constant Prices";
	public final static String FER = "Foreign Exchange Reserves";
	public final static String INTR_RATE = "Interest Rate";
	public final static String CPI = "Consumer Price Index CPI";

	public final static String MONTHLY_FREQUENCY = "monthly";
	public final static String YEARLY_FREQUENCY = "yearly";
	public final static String QUARTERLY_FREQUENCY = "quarterly";
	public final static String WEEKLY_FREQUENCY = "weekly";

	public final static String ANNUAL_REPORTED = "Annual";
	public final static String QUARTER_REPORTED = "Quarter";
	public final static String MONTHLY_REPORTED = "Month";
	public final static String WEEKLY_REPORTED = "Week";

	public final static String FREQUENCY = "frequency";

	public final static List<String> REPORT_ECONOMY_MONITORS_TRADING_ECONOMICS = Arrays.asList(GDP, CPI, "fx",
			INTR_RATE, EXPORTS, FER);

	public final static List<String> REPORT_IM_META_DATA_FIELDS = Arrays.asList("company_count", "t_mcap", "ff_sales",
			"country_coverage");
	public final static String REPORT_IM_FIELD_COMP_COUNT = "company_count";
	public final static String REPORT_IM_FIELD_MARKET_CAP = "t_mcap";
	public final static String REPORT_IM_FIELD_REVENUE = "ff_sales";
	public final static String REPORT_IM_FIELD_COUNTRY_COUNT = "country_coverage";

	public final static Integer REPORT_TOPPERS_COUNT = 5;

	public final static String CHART = "Chart";
	public final static String CHART_TITLE = "ChartTitle";
	public final static String TTM = "TTM";
	public final static String QOQ_YOY = "QoqYoy";

	public final static String QUADRANT_FIRST = "1st";
	public final static String QUADRANT_SECOND = "2nd";
	public final static String QUADRANT_THIRD = "3rd";
	public final static String QUADRANT_FOURTH = "4th";

	public final static String IMAGE_LOGO = "televisory-logo-blue.png";
	public final static String IMAGE_UP_TREND = "up_image.png";
	public final static String IMAGE_DOWN_TREND = "down_image.png";
	public final static String IMAGE_NO_TREND = "no_image.png";
	public final static String IMAGE_NO_TREND_INFO = "no_trend_info_image.png";

	public final static String CURRENCY_CODE_USD = "USD";
	public final static String CURRENCY_CODE_EURO = "EUR";
	public final static String RATE = "Rate";

	public final static String STARTDATE = "STARTDATE";
	public final static String ENDDATE = "ENDDATE";

	public final static List<String> curIgnoreList = Arrays.asList("debtor days", "inventory days", "creditor days");
	public final static String SCOPE = "World";
	public final static String SHAREHOLDING = "Shareholding";
	public final static String FREE_FLOAT = "Free Float";
	public final static String CLOSELY_HELD = "Closely Held";

	public final static int TOPPER_ROW_COUNT = 5;

	public final static String DAYS = "Days";

	public final static String REPORT_CALC_TYPE_FIN = "Financial";
	public final static String REPORT_CALC_TYPE_RATIO = "Ratio";

	public final static Integer REPORT_TEMPLATE_TYPE_QUARTER = 0;
	public final static Integer REPORT_TEMPLATE_TYPE_YEAR = 1;
	public final static Integer REPORT_TEMPLATE_TYPE_BOTH = 2;

	public final static String REPORT_PREFIX_YEAR_FY = "FY-";

	public final static String CASH_CONVERSION_FIELD_NAME = "t_cash_conversion_cycle";

	public final static String GROSS_DEBT_EBITDA_FIELD_NAME = "t_gross_debt_ebitda";

	public final static String PUBLIC_FINANCIAL_ALL = "IS,BS,CF";

	public final static String FINANCIAL_ALL = "IS,BS,CF,FR,VR";

	public final static String PRIVATE_FINANCIAL_ALL = "IS,BS,CF,FR";

	public final static int ROW_SHEET_GAP = 5;

	public static final int FINANCIAL_DATA_STARTING_COLUMN = 2;

	public static final int FINANCIAL_DATA_STARTING_ROW = 12;

	public static final int FINANCIAL_DATA_HEADER_SPACE = 2;

	public static int FINANCIAL_KEY_COL_NUM = 1;

	public static int MAX_FINANCIL_SHEET_NAME_LENGTH = 26;

	public static String DEBT_SIZING = "DS";

	public static String DATA_SENSITIVITY = "TS";

	public static String DTA = "DTA";

	public final static List<String> DEBT_HEADER = Arrays.asList("Instrument Type", "Instrument Description",
			"Issue Date", "Maturity Date", "Coupon Type", "Coupon Index", "Coupon Rate (%)", "Coupon Spread (bps)",
			"Issue Currency", "Issue Amount (Million)", "Outstanding Amount (Million)");

	public final static List<String> DEBT_BORROWING_HEADER = Arrays.asList("Instrument Type",
			/*
			 * "Issue Currency", "Total Limit as per Credit Agreement",
			 */
			"Amount Issued(Million)"/*
									 * ,
									 * "Available Limit as per Credit Agreement"
									 */);

	public final static List<String> DEBT_CAPITAL_HEADER = Arrays.asList("Collateral Type", "Collateral Details");

	public final static List<String> OWNERSHIP_MANAGEMENT_HEADER = Arrays.asList("Name", "Designation", "Exp.",
			"Appointed", "Contact Email", "Contact Phone", "Biography", "Other Related Companies");

	public final static List<String> ANALYST_RECOMMEDATION_HEADER = Arrays.asList("Date", "Buy", "Overweight",
			"Neutral", "Underweight", "Sell");

	public final static List<String> OPERATIONAL_DATA_HEADER = Arrays.asList("Name");

	public final static String MNA_SYNOPSIS_HEADER = "Synopsis";

	public final static String MNA_DEAL_ADVISOR_HEADER_PRE = "Deal Advisor";
	public final static List<String> MNA_DEAL_ADVISOR_HEADER = Arrays.asList("Advisor Name", "Advisor Role",
			"Client Name", "Client Role", "Deal Status", "Fee (CURRENCY Mn)", "Comment");

	public final static String MNA_DEAL_TERM_HEADER_PRE = "Deal Terms";
	/*****************************************************
	 * Factset Environment
	 *********************************************************/
	/*
	 * public final static String
	 * FACTSET_ONDEMAND_SERVICE_HOST="datadirect-beta.factset.com"; public final
	 * static String FACTSET_ONDEMAND_AUTH_CHECK_PATH="/services/auth-test";
	 * public final static String FACTSET_ONDEMAND_FETCH_TOKEN_URL=
	 * "https://auth-beta.factset.com/fetchotpv1";
	 * 
	 * public final static String FACTSET_ONDEMAND_RESPONSE_LINK_ID = "7011";
	 */
	public final static List<String> FACTSET_FILING_EXCLUDED_FORM_TYPE = Arrays.asList("13FCONP", "13FCONP/A",
			// "144",
			"40-202A", "40-202A/A", "40-203", "40-203/A", "AFDB", "CERTBATS",
			// "CERTNYS",
			"EBRD", "FOCUSN", "FOCUSN/A", "G-405N", "IFC", "NO ACT", "QRTLYRPT", "QRTLYRPT/A", "REG-NR", "REG-NR/A",
			"REGDEX", "SE", "SL"
	// "TTW",
	// "X-17A-5"
	);

	public final static String FACTSET_ENVIRONMENT_BETA = "BETA";
	public final static String FACTSET_ENVIRONMENT_PROD = "PROD";

	public final static String FACTSET_ACCESS_LEVEL_DOD = "DOD";
	public final static String FACTSET_ACCESS_LEVEL_SDF = "SDF";
	/***************************************************************************************************************************************/

	public final static List<String> EVENTS_HEADER = Arrays.asList("Time & Header", "Event");

	public final static List<String> INSIDE_TRANSACTION = Arrays.asList("Shareholder", "Date", "Buy/Sell",
			"No. of Shares", "Value (CURRENCY)", "Market", "Shares Owned", "% Holding");

	public final static List<String> COMPANY_FILINGS_HEADER = Arrays.asList("Date of Filling", "Document Filling",
			"Document Details");

	public static final Map<String, Integer> DESIGNATION;
	static {
		DESIGNATION = new HashMap<String, Integer>();
		DESIGNATION.put("ANA", 8);
		DESIGNATION.put("ANF", 14);
		DESIGNATION.put("AUD", 15);
		DESIGNATION.put("BOH", 16);
		DESIGNATION.put("ADM", 17);
		DESIGNATION.put("BRD", 7);
		DESIGNATION.put("CEO", 2);
		DESIGNATION.put("CHM", 1);
		DESIGNATION.put("CIO", 9);
		DESIGNATION.put("CIZ", 10);
		DESIGNATION.put("CMP", 11);
		DESIGNATION.put("CNS", 34);
		DESIGNATION.put("COO", 4);
		DESIGNATION.put("CTO", 6);
		DESIGNATION.put("DFI", 5);
		DESIGNATION.put("DIF", 35);
		DESIGNATION.put("DIR", 32);
		DESIGNATION.put("DRD", 36);
		DESIGNATION.put("ECO", 31);
		DESIGNATION.put("EDH", 37);
		DESIGNATION.put("ETD", 38);
		DESIGNATION.put("FDH", 33);
		DESIGNATION.put("FOU", 50);
		DESIGNATION.put("GRD", 18);
		DESIGNATION.put("HOG", 13);
		DESIGNATION.put("HOS", 19);
		DESIGNATION.put("HRO", 12);
		DESIGNATION.put("ICM", 48);
		DESIGNATION.put("IND", 20);
		DESIGNATION.put("INS", 21);
		DESIGNATION.put("INV", 22);
		DESIGNATION.put("IRC", 45);
		DESIGNATION.put("IRO", 46);
		DESIGNATION.put("LAW", 47);
		DESIGNATION.put("MBA", 23);
		DESIGNATION.put("MOG", 24);
		DESIGNATION.put("PEA", 25);
		DESIGNATION.put("PEI", 44);
		DESIGNATION.put("PME", 49);
		DESIGNATION.put("PMF", 27);
		DESIGNATION.put("PRN", 26);
		DESIGNATION.put("PSD", 3);
		DESIGNATION.put("PVF", 43);
		DESIGNATION.put("SAM", 30);
		DESIGNATION.put("SEC", 42);
		DESIGNATION.put("SHA", 28);
		DESIGNATION.put("STR", 29);
		DESIGNATION.put("TRE", 39);
		DESIGNATION.put("TRX", 40);
		DESIGNATION.put("UND", 41);
	}

	public final static List<String> PRICE_METRICS_HEADER = Arrays.asList("Date", "Stock Price (Closing) (USD)",
			"Stock Volume");

	// Product Line
	public final static String FACTSET_ENTITY_PROFILE_PRD = "PRD";
	// Inst Prof (Inv Appr)
	public final static String FACTSET_ENTITY_PROFILE_PR2 = "PR2";
	// Inst Prof (Overv)
	public final static String FACTSET_ENTITY_PROFILE_PRO = "PRO";

	// Deal history sheet name
	public final static String DEAL_HISTORY_SHEET_NAME = "Deals History";

	// Deal history sheet name
	public final static String PEVC_SUMMARY_GLOBAL_SHEET_NAME = "VCPE Funding Summary - Global";

	// Deal history sheet name
	public final static String PEVC_SUMMARY_COUNTRY_SHEET_NAME = "VCPE Funding Summary - Country";

	public final static List<String> DEAL_HISTORY_HEADER = Arrays.asList("Announce\n Date", "Target", "Role",
			"Acquirer", "Seller", "Close Date/Status", "Transaction Value \n (CURRENCY Million )", "Deal Type",
			"Target Industry", "% Sought", "% Owned", "Mode of Payment", "Source Funds",
			"Target EV\n (CURRENCY Million)", "Target LTM Revenue \n (CURRENCY Million)",
			"Target LTM EBITDA\n (CURRENCY Million)", "Offered Price per Share \n (Total)(CURRENCY)",
			"Offered Price per Share\n (Stock) (CURRENCY)", "Offered Price per Share\n (Cash) (CURRENCY)",
			"1 Day Prior Share\n Price Premium (%) ", "Ev To Sales \n (x)", "EV to EBITDA \n (x)",
			"Cash Adjusted Deal Value \n (CURRENCY Million)");

	public final static List<Integer> DEAL_HISTORY_HEADER_COLUMN_WIDTH_INDEX = Arrays.asList(1, 6, 8, 10, 11, 12, 13);
	
	public final static List<String>PEVC_FINANCIAL_TYPE=Arrays.asList("PE","VC","OT","PE,VC","All");
	
	public final static List<String>PEVC_FUNDING_HEADER=Arrays.asList("Serial Number","Investor Name","Fund Name","Investor Type",
			"Investment %","Status","Exit Date");
	
	public static final String FR_UNIT_CODE = "fr_unit";
	public static final String VR_UNIT_CODE = "vr_unit";
	
}
