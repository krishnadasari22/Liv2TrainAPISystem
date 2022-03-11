package com.televisory.user.dto;

import java.io.Serializable;

public class LoanBankDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int bankId;

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	@Override
	public String toString() {
		return "LoanBankDto [bankId=" + bankId + "]";
	}
	
	

}
