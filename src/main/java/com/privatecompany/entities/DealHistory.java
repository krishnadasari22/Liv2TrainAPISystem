package com.privatecompany.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ma_v1_ma_deal_info")
public class DealHistory {

	@Id
	@Column(name = "deal_id")
	private Integer dealId;

	@Column(name = "deal_currency")
	private String dealCurrency;

	@Column(name = "announce_date")
	private Date announceDate;

	@Column(name = "target")
	private String target;

	@Column(name = "role")
	private String role;

	@Column(name = "acquirer")
	private String acquirer;

	@Column(name = "seller")
	private String seller;

	@Column(name = "close_date/status")
	private String closeDateOrStatus;

	@Column(name = "transaction_value")
	private Double transactionValue;

	@Column(name = "deal_type")
	private String dealType;

	@Column(name = "target_industry")
	private String targetIndustry;

	@Column(name = "percent_sought")
	private Double percentSought;

	@Column(name = "term&synopsis")
	private String termAndSynopsis;
	
	@Column(name=" percent_owned")
	private Double  percent_owned;
	
	@Column(name="mop")
	private String mop;
	
	@Column(name="source_funds_desc")
	private String source_funds_desc;
	
	@Column(name="ev")
	private Double ev;
	
	@Column(name="revenue_ltm_before_deal")
	private Double revenue_ltm_before_deal;
	
	@Column(name="ebidta_ltm_before_deal")
	private Double ebidta_ltm_before_deal;
	
	@Column(name="price_share")
	private Double price_share;
	
	@Column(name="stock_price_share")
	private Double stock_price_share;
	
	@Column(name="cash_price_share")
	private Double cash_price_share;
	
	@Column(name="one_day_prem")
	private Double one_day_prem;
	
	@Column(name="ev_rev")
	private Double ev_rev;
	
	@Column(name="ev_ebitda")
	private Double ev_ebitda;
	
	@Column(name="cash_adjusted_deal_value")
	private Double cash_adjusted_deal_value;
	
	@Column(name="entity_country_name")
	private String entity_country_name;
	
	@Column(name="entity_name")
    private String entity_name;
	
	@Column(name="entity_industry_name")
	private String entity_industry_name;

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public String getDealCurrency() {
		return dealCurrency;
	}

	public void setDealCurrency(String dealCurrency) {
		this.dealCurrency = dealCurrency;
	}

	public Date getAnnounceDate() {
		return announceDate;
	}

	public void setAnnounceDate(Date announceDate) {
		this.announceDate = announceDate;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getCloseDateOrStatus() {
		return closeDateOrStatus;
	}

	public void setCloseDateOrStatus(String closeDateOrStatus) {
		this.closeDateOrStatus = closeDateOrStatus;
	}

	public Double getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionValue(Double transactionValue) {
		this.transactionValue = transactionValue;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getTargetIndustry() {
		return targetIndustry;
	}

	public void setTargetIndustry(String targetIndustry) {
		this.targetIndustry = targetIndustry;
	}

	public Double getPercentSought() {
		return percentSought;
	}

	public void setPercentSought(Double percentSought) {
		this.percentSought = percentSought;
	}

	public String getTermAndSynopsis() {
		return termAndSynopsis;
	}

	public void setTermAndSynopsis(String termAndSynopsis) {
		this.termAndSynopsis = termAndSynopsis;
	}
	
	
	public Double getPercent_owned() {
		return percent_owned;
	}

	public void setPercent_owned(Double percent_owned) {
		this.percent_owned = percent_owned;
	}
	
	

	public String getMop() {
		return mop;
	}

	public void setMop(String mop) {
		this.mop = mop;
	}

	public String getSource_funds_desc() {
		return source_funds_desc;
	}

	public void setSource_funds_desc(String source_funds_desc) {
		this.source_funds_desc = source_funds_desc;
	}

	public Double getEv() {
		return ev;
	}

	public void setEv(Double ev) {
		this.ev = ev;
	}

	public Double getRevenue_ltm_before_deal() {
		return revenue_ltm_before_deal;
	}

	public void setRevenue_ltm_before_deal(Double revenue_ltm_before_deal) {
		this.revenue_ltm_before_deal = revenue_ltm_before_deal;
	}

	public Double getEbitda_ltm_before_deal() {
		return ebidta_ltm_before_deal;
	}

	public void setEbitda_ltm_before_deal(Double ebidta_ltm_before_deal) {
		this.ebidta_ltm_before_deal = ebidta_ltm_before_deal;
	}

	public Double getPrice_share() {
		return price_share;
	}

	public void setPrice_share(Double price_share) {
		this.price_share = price_share;
	}

	public Double getStock_price_share() {
		return stock_price_share;
	}

	public void setStock_price_share(Double stock_price_share) {
		this.stock_price_share = stock_price_share;
	}

	public Double getCash_price_share() {
		return cash_price_share;
	}

	public void setCash_price_share(Double cash_price_share) {
		this.cash_price_share = cash_price_share;
	}

	public Double getOne_day_prem() {
		return one_day_prem;
	}

	public void setOne_day_prem(Double one_day_prem) {
		this.one_day_prem = one_day_prem;
	}

	public Double getEv_rev() {
		return ev_rev;
	}

	public void setEv_rev(Double ev_rev) {
		this.ev_rev = ev_rev;
	}

	public Double getEv_ebitda() {
		return ev_ebitda;
	}

	public void setEv_ebitda(Double ev_ebitda) {
		this.ev_ebitda = ev_ebitda;
	}

	public Double getCash_adjusted_deal_value() {
		return cash_adjusted_deal_value;
	}

	public void setCash_adjusted_deal_value(Double cash_adjusted_deal_value) {
		this.cash_adjusted_deal_value = cash_adjusted_deal_value;
	}
	
	

	public Double getEbidta_ltm_before_deal() {
		return ebidta_ltm_before_deal;
	}

	public void setEbidta_ltm_before_deal(Double ebidta_ltm_before_deal) {
		this.ebidta_ltm_before_deal = ebidta_ltm_before_deal;
	}

	public String getEntity_country_name() {
		return entity_country_name;
	}

	public void setEntity_country_name(String entity_country_name) {
		this.entity_country_name = entity_country_name;
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public String getEntity_industry_name() {
		return entity_industry_name;
	}

	public void setEntity_industry_name(String entity_industry_name) {
		this.entity_industry_name = entity_industry_name;
	}

	@Override
	public String toString() {
		return "DealHistory [dealId=" + dealId + ", dealCurrency=" + dealCurrency + ", announceDate=" + announceDate
				+ ", target=" + target + ", role=" + role + ", acquirer=" + acquirer + ", seller=" + seller
				+ ", closeDateOrStatus=" + closeDateOrStatus + ", transactionValue=" + transactionValue + ", dealType="
				+ dealType + ", targetIndustry=" + targetIndustry + ", percentSought=" + percentSought
				+ ", termAndSynopsis=" + termAndSynopsis + "]";
	}

}
