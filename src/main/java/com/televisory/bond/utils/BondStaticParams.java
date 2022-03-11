package com.televisory.bond.utils;

public interface BondStaticParams {
	
	public static String ALL = "ALL";
	
	public static String BOND = "bond";
	
	public static String CDS = "cds";
	
	public static String DEFAULT_CDS_IDENTIFIER = "04297120EURSeniorCR";
	
	public static String DEFAULT_FI_IDENTIFIER = "US912810FM54";
	
	public static String FIELD_NAME = "price";

	public static Integer CDS_COMPARABLE_LIMIT = 5;
	
	public static String CDS_COMPARABLE_FIELFNAMES = "business_date_time,par_spread_mid,quote_spread_mid,up_front_mid,restructuring_type,seniority";
	
	public static Integer FI_COMPARABLE_LIMIT = 5;
	
	public static String FI_COMPARABLE_FIELFNAMES = "as_of_date,maturity_date,yield_to_maturity,price,modified_duration,industry_lvl_1_desc,maturity_type";
	
	public static Integer ETD_FUTURE_COMPARABLE_LIMIT = 6;
	
	public static Integer ETD_OPTION_COMPARABLE_LIMIT = 5;
	
	public static String DEFAULT_ETD_IDENTIFIER_PROPERTY_NAME = "DEFAULT_ETD_IDENTIFIER";
	
	public static String ETD_DEFAULT_FIELD_NAME = "last_trade";
}
