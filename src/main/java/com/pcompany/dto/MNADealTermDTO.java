package com.pcompany.dto;

import java.util.Date;

public class MNADealTermDTO {

	private Integer version;
	
	private Date effect_date;
	
	private String target;
	
	private String mop;
	
	private String source_funds_desc;
	
	private Double cash;
	
	private Double stock;
	
	private Double percent_sought;
	
	private Double percent_owned;
	
	private Double transaction_value;
	
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
	
	private String term_change_desc;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getEffect_date() {
		return effect_date;
	}

	public void setEffect_date(Date effect_date) {
		this.effect_date = effect_date;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public Double getPercent_sought() {
		return percent_sought;
	}

	public void setPercent_sought(Double percent_sought) {
		this.percent_sought = percent_sought;
	}

	public Double getPercent_owned() {
		return percent_owned;
	}

	public void setPercent_owned(Double percent_owned) {
		this.percent_owned = percent_owned;
	}

	public Double getTransaction_value() {
		return transaction_value;
	}

	public void setTransaction_value(Double transaction_value) {
		this.transaction_value = transaction_value;
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

	public String getTerm_change_desc() {
		return term_change_desc;
	}

	public void setTerm_change_desc(String term_change_desc) {
		this.term_change_desc = term_change_desc;
	}

	@Override
	public String toString() {
		return "MNADealTerm [version=" + version + ", effect_date=" + effect_date + ", target=" + target + ", mop="
				+ mop + ", source_funds_desc=" + source_funds_desc + ", cash=" + cash + ", stock=" + stock
				+ ", percent_sought=" + percent_sought + ", percent_owned=" + percent_owned + ", transaction_value="
				+ transaction_value + ", ev=" + ev + ", revenue_ltm_before_deal=" + revenue_ltm_before_deal
				+ ", ebitda_ltm_before_deal=" + ebitda_ltm_before_deal + ", price_share=" + price_share
				+ ", stock_price_share=" + stock_price_share + ", cash_price_share=" + cash_price_share
				+ ", one_day_prem=" + one_day_prem + ", ev_rev=" + ev_rev + ", ev_ebitda=" + ev_ebitda
				+ ", cash_adjusted_deal_value=" + cash_adjusted_deal_value + ", term_change_desc=" + term_change_desc
				+ "]";
	}
	
}
