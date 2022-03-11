package com.televisory.bond.entity;

import java.util.Date;


public class DerivativeIdentifierWithProp {
	
	private String identifier;
	private Date expiryDate;
	private Double strike;
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Double getStrike() {
		return strike;
	}
	public void setStrike(Double strike) {
		this.strike = strike;
	}
	
	@Override
	public String toString() {
		return "DerivativeIdentifierWithProp [identifier=" + identifier + ", expiryDate=" + expiryDate + "]";
	}
}
