package com.pcompany.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "own_v5_own_insider_trans")
public class InsiderTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name = "factset_entity_id")
	private String factsetEntityId;
	
	@Column(name = "fsym_id")
	private String fsymId;
	
	@Column(name = "reported_title")
	private String reportedTitle;
	
	@Column(name = "tran_shares")
	private Double tranShares;

	@Column(name = "tran_value")
	private Double tranValue;
	
	@Column(name="effective_date")
	private String effective_date;
	
	@Column(name="entity_proper_name",insertable=false)
	private String entityProperName;
	
	@Column(name="market",insertable=false)
	private String market;
	
	@Column(name="shares_owned",insertable=false)
	private Integer sharesOwned;
	
	@Column(name="percent_holding",insertable=false)
	private Double percentHolding;
	
	@Column(name="buy_sell",insertable=false)
	private String buySell;
	
	@Column(name="currency",insertable=false)
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

	public String getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}

	public String getEntityProperName() {
		return entityProperName;
	}

	public void setEntityProperName(String entityProperName) {
		this.entityProperName = entityProperName;
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
		return "InsiderTransaction [id=" + id + ", factsetEntityId="
				+ factsetEntityId + ", fsymId=" + fsymId + ", reportedTitle="
				+ reportedTitle + ", tranShares=" + tranShares + ", tranValue="
				+ tranValue + ", effective_date=" + effective_date
				+ ", entityProperName=" + entityProperName + ", market="
				+ market + ", sharesOwned=" + sharesOwned + ", percentHolding="
				+ percentHolding + ", buySell=" + buySell + ", currency="
				+ currency + "]";
	}

}
