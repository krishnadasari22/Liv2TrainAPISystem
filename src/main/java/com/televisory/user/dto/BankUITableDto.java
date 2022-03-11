package com.televisory.user.dto;

import java.io.Serializable;

public class BankUITableDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String applicationDate;
	String srn;
	String lsrn;
	String dsrn;
	int companyId;
	String companyName;
	String industry;
	double loanAmount;
	String status;
	String comments;
	String currency;
	String bankName;

	public String getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getSrn() {
		return srn;
	}
	public void setSrn(String srn) {
		this.srn = srn;
	}
	public String getLsrn() {
		return lsrn;
	}
	public void setLsrn(String lsrn) {
		this.lsrn = lsrn;
	}
	public String getDsrn() {
		return dsrn;
	}
	public void setDsrn(String dsrn) {
		this.dsrn = dsrn;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Override
	public String toString() {
		return "BankUITableDto [applicationDate=" + applicationDate + ", srn="
				+ srn + ", lsrn=" + lsrn + ", dsrn=" + dsrn + ", companyId="
				+ companyId + ", companyName=" + companyName + ", industry="
				+ industry + ", loanAmount=" + loanAmount + ", status="
				+ status + ", comments=" + comments + ", currency=" + currency
				+ ", bankName=" + bankName + "]";
	}

}
