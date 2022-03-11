package com.televisory.user.dto;

import java.io.Serializable;

public class FacilityDetailDto implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private String facilityName;
	private double loanAMount;
	private String fundLimit;
	private String purpose;
	
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public double getLoanAMount() {
		return loanAMount;
	}
	public void setLoanAMount(double loanAMount) {
		this.loanAMount = loanAMount;
	}
	public String getFundLimit() {
		return fundLimit;
	}
	public void setFundLimit(String fundLimit) {
		this.fundLimit = fundLimit;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	@Override
	public String toString() {
		return "FacilityDetailDto [facilityName=" + facilityName
				+ ", loanAMount=" + loanAMount + ", fundLimit=" + fundLimit
				+ ", purpose=" + purpose + "]";
	}
}
