package com.televisory.bond.Model;

import java.util.List;
import java.util.Map;

public class BondInitialDetails {

	Map<String, List<Object>> initialDetails;

	public Map<String, List<Object>> getInitialDetails() {
		return initialDetails;
	}

	public void setInitialDetails(Map<String, List<Object>> initialDetails) {
		this.initialDetails = initialDetails;
	}

	@Override
	public String toString() {
		return "BondInitialDetails [initialDetails=" + initialDetails + "]";
	}
}
