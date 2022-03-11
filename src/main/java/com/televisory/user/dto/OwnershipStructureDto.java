package com.televisory.user.dto;

import java.io.Serializable;

public class OwnershipStructureDto implements Serializable{

	private static final long serialVersionUID = -8101803989383613425L;
	
	private String type;
	private double perOfShareToday;
	private double perOfShareLatestFinYear;
	private double perOfSharePrevFinYear;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getPerOfShareToday() {
		return perOfShareToday;
	}
	public void setPerOfShareToday(double perOfShareToday) {
		this.perOfShareToday = perOfShareToday;
	}
	public double getPerOfShareLatestFinYear() {
		return perOfShareLatestFinYear;
	}
	public void setPerOfShareLatestFinYear(double perOfShareLatestFinYear) {
		this.perOfShareLatestFinYear = perOfShareLatestFinYear;
	}
	public double getPerOfSharePrevFinYear() {
		return perOfSharePrevFinYear;
	}
	public void setPerOfSharePrevFinYear(double perOfSharePrevFinYear) {
		this.perOfSharePrevFinYear = perOfSharePrevFinYear;
	}
	@Override
	public String toString() {
		return "OwnershipStructureDto [type=" + type + ", perOfShareToday="
				+ perOfShareToday + ", perOfShareLatestFinYear="
				+ perOfShareLatestFinYear + ", perOfSharePrevFinYear="
				+ perOfSharePrevFinYear + "]";
	}
}
