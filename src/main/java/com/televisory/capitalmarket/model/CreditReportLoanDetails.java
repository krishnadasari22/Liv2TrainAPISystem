package com.televisory.capitalmarket.model;

public class CreditReportLoanDetails {

	String facility;
	String loanAmount;
	String fund_nonfund_limit;
	String total_category;
	String total_category_value;

	public CreditReportLoanDetails(String facility,	String loanAmount,	String fund_nonfund_limit) {
		this.facility = facility;
		this.loanAmount = loanAmount;
		this.fund_nonfund_limit = fund_nonfund_limit;
	}

	public CreditReportLoanDetails(String facility,	String loanAmount,	String fund_nonfund_limit, String total_category,String total_category_value) {
		this.facility = facility;
		this.loanAmount = loanAmount;
		this.fund_nonfund_limit = fund_nonfund_limit;
		this.total_category = total_category;
		this.total_category_value = total_category_value;
	}

	public String getTotal_category() {
		return total_category;
	}

	public void setTotal_category(String total_category) {
		this.total_category = total_category;
	}


	public String getTotal_category_value() {
		return total_category_value;
	}

	public void setTotal_category_value(String total_category_value) {
		this.total_category_value = total_category_value;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getFund_nonfund_limit() {
		return fund_nonfund_limit;
	}

	public void setFund_nonfund_limit(String fund_nonfund_limit) {
		this.fund_nonfund_limit = fund_nonfund_limit;
	}

	@Override
	public String toString() {
		return "CreditReportLoanDetails [facility=" + facility
				+ ", loanAmount=" + loanAmount + ", fund_nonfund_limit="
				+ fund_nonfund_limit + "]";
	}


}
