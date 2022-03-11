package com.pcompany.dto;

import java.io.Serializable;

import javax.persistence.Column;

public class InsiderTransactionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String factsetEntityId;
	
	private String fsymId;
	
	private String reportedTitle;
	
	private Double tranShares;

	private Double tranValue;
	
	private String entityProperName;
	
	private String effective_date;
	
	private String market;
	
	private Integer sharesOwned;
	
	private Double percentHolding;
	
	private String buySell;
	
	private String currency;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFactsetEntityId() {
		return factsetEntityId;
	}

	public void setFactsetEntityId(String factsetEntityId) {
		this.factsetEntityId = factsetEntityId;
	}

	public String getFsymId() {
		return fsymId;
	}

	public void setFsymId(String fsymId) {
		this.fsymId = fsymId;
	}

	public String getReportedTitle() {
		return reportedTitle;
	}

	public void setReportedTitle(String reportedTitle) {
		this.reportedTitle = reportedTitle;
	}

	public Double getTranShares() {
		return tranShares;
	}

	public void setTranShares(Double tranShares) {
		this.tranShares = tranShares;
	}

	public Double getTranValue() {
		return tranValue;
	}

	public void setTranValue(Double tranValue) {
		this.tranValue = tranValue;
	}

	public String getEntityProperName() {
		return entityProperName;
	}

	public void setEntityProperName(String entityProperName) {
		this.entityProperName = entityProperName;
	}

	public String getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Integer getSharesOwned() {
		return sharesOwned;
	}

	public void setSharesOwned(Integer sharesOwned) {
		this.sharesOwned = sharesOwned;
	}

	public Double getPercentHolding() {
		return percentHolding;
	}

	public void setPercentHolding(Double percentHolding) {
		this.percentHolding = percentHolding;
	}

	public String getBuySell() {
		return buySell;
	}

	public void setBuySell(String buySell) {
		this.buySell = buySell;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "InsiderTransactionDTO [id=" + id + ", factsetEntityId="
				+ factsetEntityId + ", fsymId=" + fsymId + ", reportedTitle="
				+ reportedTitle + ", tranShares=" + tranShares + ", tranValue="
				+ tranValue + ", entityProperName=" + entityProperName
				+ ", effective_date=" + effective_date + ", market=" + market
				+ ", sharesOwned=" + sharesOwned + ", percentHolding="
				+ percentHolding + ", buySell=" + buySell + ", currency="
				+ currency + "]";
	}
	
}
