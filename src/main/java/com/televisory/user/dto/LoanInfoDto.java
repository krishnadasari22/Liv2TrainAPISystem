package com.televisory.user.dto;

import java.io.Serializable;
import java.util.Arrays;

public class LoanInfoDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String loanCurrency;
	private double loanAmount;
	public String getLoanCurrency() {
		return loanCurrency;
	}
	public void setLoanCurrency(String loanCurrency) {
		this.loanCurrency = loanCurrency;
	}
	public double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	@Override
	public String toString() {
		return "LoanInfoDto [loanCurrency=" + loanCurrency + ", loanAmount="
				+ loanAmount + "]";
	}
}
