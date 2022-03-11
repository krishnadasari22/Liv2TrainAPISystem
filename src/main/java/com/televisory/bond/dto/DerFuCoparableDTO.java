package com.televisory.bond.dto;


import java.sql.Date;

public class DerFuCoparableDTO {

	private String symbol;

	private String displayName;

	private String underlyingName;

	private Date expiryDate;

	private Double latestSettlement;
	
	private Date latestSettlementDate;
	
	private Double lastTrade;
	
	private Date lastTradeDate;

	private Double openInterest;

	private Double changeInOI;
	
	private String currency;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUnderlyingName() {
		return underlyingName;
	}

	public void setUnderlyingName(String underlyingName) {
		this.underlyingName = underlyingName;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getLatestSettlement() {
		return latestSettlement;
	}

	public void setLatestSettlement(Double latestSettlement) {
		this.latestSettlement = latestSettlement;
	}

	public Date getLatestSettlementDate() {
		return latestSettlementDate;
	}

	public void setLatestSettlementDate(Date latestSettlementDate) {
		this.latestSettlementDate = latestSettlementDate;
	}

	public Double getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(Double openInterest) {
		this.openInterest = openInterest;
	}

	public Double getChangeInOI() {
		return changeInOI;
	}

	public void setChangeInOI(Double changeInOI) {
		this.changeInOI = changeInOI;
	}
	
	public Double getLastTrade() {
		return lastTrade;
	}

	public void setLastTrade(Double lastTrade) {
		this.lastTrade = lastTrade;
	}
	
	public Date getLastTradeDate() {
		return lastTradeDate;
	}

	public void setLastTradeDate(Date lastTradeDate) {
		this.lastTradeDate = lastTradeDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "DerFuCoparableDTO [symbol=" + symbol + ", displayName=" + displayName + ", underlyingName="
				+ underlyingName + ", expiryDate=" + expiryDate + ", latestSettlement=" + latestSettlement
				+ ", latestSettlementDate=" + latestSettlementDate + ", lastTrade=" + lastTrade + ", lastTradeDate="
				+ lastTradeDate + ", openInterest=" + openInterest + ", changeInOI=" + changeInOI + "]";
	}
}
