package com.privatecompany.dto;

import java.util.Date;

public class DealHistoryDTO {

	private Integer dealId;

	private String dealCurrency;

	private Date announceDate;

	private String target;

	private String role;

	private String acquirer;

	private String seller;

	private String closeDateOrStatus;

	private Double transactionValue;

	private String dealType;

	private String targetIndustry;

	private Double percentSought;

	private String termAndSynopsis;
	
	private Double percent_owned;
	
	private String mop;
	
	private String source_funds_desc;
	
	private Double ev;
	
    private Double revenue_ltm_before_deal;
	
	private Double ebitda_ltm_before_deal;
	
    private Double price_share;
	
	private Double stock_price_share;
	
	private Double cash_price_share;
	
	private Double one_day_prem;
	
    private Double ev_rev;
	
	private Double ev_ebitda;
	
	private Double cash_adjusted_deal_value;
	
	private String entity_country_name;
	
	private String entity_name;
	
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
		return ebitda_ltm_before_deal;
	}

	public void setEbitda_ltm_before_deal(Double ebitda_ltm_before_deal) {
		this.ebitda_ltm_before_deal = ebitda_ltm_before_deal;
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
		return "DealHistoryDTO [dealId=" + dealId + ", dealCurrency=" + dealCurrency + ", announceDate=" + announceDate
				+ ", target=" + target + ", role=" + role + ", acquirer=" + acquirer + ", seller=" + seller
				+ ", closeDateOrStatus=" + closeDateOrStatus + ", transactionValue=" + transactionValue + ", dealType="
				+ dealType + ", targetIndustry=" + targetIndustry + ", percentSought=" + percentSought
				+ ", termAndSynopsis=" + termAndSynopsis + "]";
	}

}
