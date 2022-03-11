package com.televisory.bond.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CDSMapping {

	static final Map<String, String> cdsMappingDetails = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("Business Date Time", "businessDateTime");
			put("ice_cds_code", "ice_cds_code");
			put("currency", "currency");
		};
	};

	public static List<String> DEFAULT_CDS_COMPARABLE = Arrays.asList("quote_spread_mid","up_front_mid");






}
