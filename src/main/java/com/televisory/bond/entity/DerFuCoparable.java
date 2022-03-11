package com.televisory.bond.entity;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="etd_data_latest")
public class DerFuCoparable {

	@Id
	@Column(name = "symbol")
	private String symbol;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "underlying_name")
	private String underlyingName;

	@Column(name = "expiry_date")
	private Date expiryDate;

	@Column(name = "latest_settlement")
	private Double latestSettlement;
	
	@Column(name = "latest_settlement_date")
	private Date latestSettlementDate;
	
	@Column(name = "last_trade")
	private Double lastTrade;
	
	@Column(name = "last_trade_date")
	private Date lastTradeDate;


	@Column(name = "open_interest")
	private Double openInterest;

	@Column(name = "change_in_oi")
	private Double changeInOI;
	
	@Column(name = "currency")
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
		return "DerFuCoparable [symbol=" + symbol + ", displayName=" + displayName + ", underlyingName="
				+ underlyingName + ", expiryDate=" + expiryDate + ", latestSettlement=" + latestSettlement
				+ ", latestSettlementDate=" + latestSettlementDate + ", lastTrade=" + lastTrade + ", lastTradeDate="
				+ lastTradeDate + ", openInterest=" + openInterest + ", changeInOI=" + changeInOI + "]";
	}
}
