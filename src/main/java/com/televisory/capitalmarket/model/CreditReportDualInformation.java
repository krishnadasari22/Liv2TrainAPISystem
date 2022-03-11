package com.televisory.capitalmarket.model;

public class CreditReportDualInformation {

	String firstCol;
	String secondCol;

	public CreditReportDualInformation(	String firstCol,String secondCol) {
		this.firstCol = firstCol;
		this.secondCol = secondCol;
	}

	public String getFirstCol() {
		return firstCol;
	}
	public void setFirstCol(String firstCol) {
		this.firstCol = firstCol;
	}
	public String getSecondCol() {
		return secondCol;
	}
	public void setSecondCol(String secondCol) {
		this.secondCol = secondCol;
	}

	@Override
	public String toString() {
		return "CreditReportDualInformation [firstCol=" + firstCol
				+ ", secondCol=" + secondCol + "]";
	}


}
