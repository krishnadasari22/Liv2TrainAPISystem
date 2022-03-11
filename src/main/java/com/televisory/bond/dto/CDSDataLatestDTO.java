package com.televisory.bond.dto;

import java.sql.Date;

import org.dozer.Mapping;

public class CDSDataLatestDTO {

	@Mapping("cdsDataDetails.entityName")
	private String entityName;

	@Mapping("cdsDataDetails.currency")
	private String currency;

	@Mapping("cdsDataDetails.tenor")
	private Double tenor;

	@Mapping("cdsDataDetails.seniority")
	private String seniority;

	@Mapping("cdsDataDetails.restructuringType")
	private String restructuringType;

	@Mapping("cdsDataDetails.businessDateTime")
	private Date business_date_time;

	private String identifier;
	
	private Date maturity_date;

	private String ice_cds_code;

	private String bbg_cds_ticker;

	private Integer cma_entity_id;
	
	private String entity_name;

	private String cma_ticker;

	private String contract_standard;

	private String sector;

	private String region;

	private Integer coupon;

	private String market_quoting_convention;

	private String instrument_type;

	private Date default_date;

	private Date auction_date;

	private Double par_spread_bid;

	private Double par_spread_mid;

	private Double par_spread_offer;

	private Double quote_spread_bid;

	private Double quote_spread_mid;

	private Double quote_spread_offer;

	private Double up_front_bid;

	private Double up_front_mid;

	private Double up_front_offer;

	private Double percent_of_par_bid;

	private Double percent_of_par_mid;

	private Double percent_of_par_offer;

	private Double par_spread_pv01_bid;

	private Double par_spread_pv01_mid;

	private Double par_spread_pv01_offer;

	private Double quote_spread_pv01_bid;

	private Double quote_spread_pv01_mid;

	private Double quote_spread_pv01_offer;

	private Double hazard_rate;

	private Double cum_probability_of_default;

	private String cma_short_term_implied_rating;

	private String cma_long_term_implied_rating;

	private String sp_rating;

	private String moodys_rating;

	private String delta;

	private String reference;

	private Integer market_recovery_rate;

	private Integer isda_recovery_rate;

	private String devaluation_factor;

	private String observed_derived_indicator;

	private String derivation_type;

	private String ref_curve_info;

	private Date last_contribution_date_time;

	private Double up_front_mid_1d_percent_change;

	private Double quote_spread_mid_1d_percent_change;

	private Double par_spread_mid_1d_percent_change;

	private Double percent_of_par_mid_1d_percent_change;

	private Double coupon_1d_percent_change;

	private Double quote_spread_pv01_mid_1d_percent_change;

	private Double par_spread_pv01_mid_1d_percent_change;

	private Double cum_probability_of_default_1d_percent_change;

	private Double hazard_rate_1d_percent_change;

	private Double market_recovery_rate_1d_percent_change;
	
	private Double up_front_mid_1w_percent_change;

	private Double quote_spread_mid_1w_percent_change;

	private Double par_spread_mid_1w_percent_change;

	private Double percent_of_par_mid_1w_percent_change;

	private Double coupon_1w_percent_change;

	private Double quote_spread_pv01_mid_1w_percent_change;

	private Double par_spread_pv01_mid_1w_percent_change;

	private Double cum_probability_of_default_1w_percent_change;

	private Double hazard_rate_1w_percent_change;

	private Double market_recovery_rate_1w_percent_change;
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getTenor() {
		return tenor;
	}

	public void setTenor(Double tenor) {
		this.tenor = tenor;
	}

	public String getSeniority() {
		return seniority;
	}

	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}

	public String getRestructuringType() {
		return restructuringType;
	}

	public void setRestructuringType(String restructuringType) {
		this.restructuringType = restructuringType;
	}

	public Date getBusiness_date_time() {
		return business_date_time;
	}

	public void setBusiness_date_time(Date business_date_time) {
		this.business_date_time = business_date_time;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getMaturity_date() {
		return maturity_date;
	}

	public void setMaturity_date(Date maturity_date) {
		this.maturity_date = maturity_date;
	}

	public String getIce_cds_code() {
		return ice_cds_code;
	}

	public void setIce_cds_code(String ice_cds_code) {
		this.ice_cds_code = ice_cds_code;
	}

	public String getBbg_cds_ticker() {
		return bbg_cds_ticker;
	}

	public void setBbg_cds_ticker(String bbg_cds_ticker) {
		this.bbg_cds_ticker = bbg_cds_ticker;
	}

	public Integer getCma_entity_id() {
		return cma_entity_id;
	}

	public void setCma_entity_id(Integer cma_entity_id) {
		this.cma_entity_id = cma_entity_id;
	}

	public String getCma_ticker() {
		return cma_ticker;
	}

	public void setCma_ticker(String cma_ticker) {
		this.cma_ticker = cma_ticker;
	}

	public String getContract_standard() {
		return contract_standard;
	}

	public void setContract_standard(String contract_standard) {
		this.contract_standard = contract_standard;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getCoupon() {
		return coupon;
	}

	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}

	public String getMarket_quoting_convention() {
		return market_quoting_convention;
	}

	public void setMarket_quoting_convention(String market_quoting_convention) {
		this.market_quoting_convention = market_quoting_convention;
	}

	public String getInstrument_type() {
		return instrument_type;
	}

	public void setInstrument_type(String instrument_type) {
		this.instrument_type = instrument_type;
	}

	public Date getDefault_date() {
		return default_date;
	}

	public void setDefault_date(Date default_date) {
		this.default_date = default_date;
	}

	public Date getAuction_date() {
		return auction_date;
	}

	public void setAuction_date(Date auction_date) {
		this.auction_date = auction_date;
	}

	public Double getPar_spread_bid() {
		return par_spread_bid;
	}

	public void setPar_spread_bid(Double par_spread_bid) {
		this.par_spread_bid = par_spread_bid;
	}

	public Double getPar_spread_mid() {
		return par_spread_mid;
	}

	public void setPar_spread_mid(Double par_spread_mid) {
		this.par_spread_mid = par_spread_mid;
	}

	public Double getPar_spread_offer() {
		return par_spread_offer;
	}

	public void setPar_spread_offer(Double par_spread_offer) {
		this.par_spread_offer = par_spread_offer;
	}

	public Double getQuote_spread_bid() {
		return quote_spread_bid;
	}

	public void setQuote_spread_bid(Double quote_spread_bid) {
		this.quote_spread_bid = quote_spread_bid;
	}

	public Double getQuote_spread_mid() {
		return quote_spread_mid;
	}

	public void setQuote_spread_mid(Double quote_spread_mid) {
		this.quote_spread_mid = quote_spread_mid;
	}

	public Double getQuote_spread_offer() {
		return quote_spread_offer;
	}

	public void setQuote_spread_offer(Double quote_spread_offer) {
		this.quote_spread_offer = quote_spread_offer;
	}

	public Double getUp_front_bid() {
		return up_front_bid;
	}

	public void setUp_front_bid(Double up_front_bid) {
		this.up_front_bid = up_front_bid;
	}

	public Double getUp_front_mid() {
		return up_front_mid;
	}

	public void setUp_front_mid(Double up_front_mid) {
		this.up_front_mid = up_front_mid;
	}

	public Double getUp_front_offer() {
		return up_front_offer;
	}

	public void setUp_front_offer(Double up_front_offer) {
		this.up_front_offer = up_front_offer;
	}

	public Double getPercent_of_par_bid() {
		return percent_of_par_bid;
	}

	public void setPercent_of_par_bid(Double percent_of_par_bid) {
		this.percent_of_par_bid = percent_of_par_bid;
	}

	public Double getPercent_of_par_mid() {
		return percent_of_par_mid;
	}

	public void setPercent_of_par_mid(Double percent_of_par_mid) {
		this.percent_of_par_mid = percent_of_par_mid;
	}

	public Double getPercent_of_par_offer() {
		return percent_of_par_offer;
	}

	public void setPercent_of_par_offer(Double percent_of_par_offer) {
		this.percent_of_par_offer = percent_of_par_offer;
	}

	public Double getPar_spread_pv01_bid() {
		return par_spread_pv01_bid;
	}

	public void setPar_spread_pv01_bid(Double par_spread_pv01_bid) {
		this.par_spread_pv01_bid = par_spread_pv01_bid;
	}

	public Double getPar_spread_pv01_mid() {
		return par_spread_pv01_mid;
	}

	public void setPar_spread_pv01_mid(Double par_spread_pv01_mid) {
		this.par_spread_pv01_mid = par_spread_pv01_mid;
	}

	public Double getPar_spread_pv01_offer() {
		return par_spread_pv01_offer;
	}

	public void setPar_spread_pv01_offer(Double par_spread_pv01_offer) {
		this.par_spread_pv01_offer = par_spread_pv01_offer;
	}

	public Double getQuote_spread_pv01_bid() {
		return quote_spread_pv01_bid;
	}

	public void setQuote_spread_pv01_bid(Double quote_spread_pv01_bid) {
		this.quote_spread_pv01_bid = quote_spread_pv01_bid;
	}

	public Double getQuote_spread_pv01_mid() {
		return quote_spread_pv01_mid;
	}

	public void setQuote_spread_pv01_mid(Double quote_spread_pv01_mid) {
		this.quote_spread_pv01_mid = quote_spread_pv01_mid;
	}

	public Double getQuote_spread_pv01_offer() {
		return quote_spread_pv01_offer;
	}

	public void setQuote_spread_pv01_offer(Double quote_spread_pv01_offer) {
		this.quote_spread_pv01_offer = quote_spread_pv01_offer;
	}

	public Double getHazard_rate() {
		return hazard_rate;
	}

	public void setHazard_rate(Double hazard_rate) {
		this.hazard_rate = hazard_rate;
	}

	public Double getCum_probability_of_default() {
		return cum_probability_of_default;
	}

	public void setCum_probability_of_default(Double cum_probability_of_default) {
		this.cum_probability_of_default = cum_probability_of_default;
	}

	public String getCma_short_term_implied_rating() {
		return cma_short_term_implied_rating;
	}

	public void setCma_short_term_implied_rating(String cma_short_term_implied_rating) {
		this.cma_short_term_implied_rating = cma_short_term_implied_rating;
	}

	public String getCma_long_term_implied_rating() {
		return cma_long_term_implied_rating;
	}

	public void setCma_long_term_implied_rating(String cma_long_term_implied_rating) {
		this.cma_long_term_implied_rating = cma_long_term_implied_rating;
	}

	public String getSp_rating() {
		return sp_rating;
	}

	public void setSp_rating(String sp_rating) {
		this.sp_rating = sp_rating;
	}

	public String getMoodys_rating() {
		return moodys_rating;
	}

	public void setMoodys_rating(String moodys_rating) {
		this.moodys_rating = moodys_rating;
	}

	public String getDelta() {
		return delta;
	}

	public void setDelta(String delta) {
		this.delta = delta;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getMarket_recovery_rate() {
		return market_recovery_rate;
	}

	public void setMarket_recovery_rate(Integer market_recovery_rate) {
		this.market_recovery_rate = market_recovery_rate;
	}

	public Integer getIsda_recovery_rate() {
		return isda_recovery_rate;
	}

	public void setIsda_recovery_rate(Integer isda_recovery_rate) {
		this.isda_recovery_rate = isda_recovery_rate;
	}

	public String getDevaluation_factor() {
		return devaluation_factor;
	}

	public void setDevaluation_factor(String devaluation_factor) {
		this.devaluation_factor = devaluation_factor;
	}

	public String getObserved_derived_indicator() {
		return observed_derived_indicator;
	}

	public void setObserved_derived_indicator(String observed_derived_indicator) {
		this.observed_derived_indicator = observed_derived_indicator;
	}

	public String getDerivation_type() {
		return derivation_type;
	}

	public void setDerivation_type(String derivation_type) {
		this.derivation_type = derivation_type;
	}

	public String getRef_curve_info() {
		return ref_curve_info;
	}

	public void setRef_curve_info(String ref_curve_info) {
		this.ref_curve_info = ref_curve_info;
	}

	public Date getLast_contribution_date_time() {
		return last_contribution_date_time;
	}

	public void setLast_contribution_date_time(Date last_contribution_date_time) {
		this.last_contribution_date_time = last_contribution_date_time;
	}

	public Double getUp_front_mid_1d_percent_change() {
		return up_front_mid_1d_percent_change;
	}

	public void setUp_front_mid_1d_percent_change(Double up_front_mid_1d_percent_change) {
		this.up_front_mid_1d_percent_change = up_front_mid_1d_percent_change;
	}

	public Double getQuote_spread_mid_1d_percent_change() {
		return quote_spread_mid_1d_percent_change;
	}

	public void setQuote_spread_mid_1d_percent_change(Double quote_spread_mid_1d_percent_change) {
		this.quote_spread_mid_1d_percent_change = quote_spread_mid_1d_percent_change;
	}

	public Double getPar_spread_mid_1d_percent_change() {
		return par_spread_mid_1d_percent_change;
	}

	public void setPar_spread_mid_1d_percent_change(Double par_spread_mid_1d_percent_change) {
		this.par_spread_mid_1d_percent_change = par_spread_mid_1d_percent_change;
	}

	public Double getPercent_of_par_mid_1d_percent_change() {
		return percent_of_par_mid_1d_percent_change;
	}

	public void setPercent_of_par_mid_1d_percent_change(Double percent_of_par_mid_1d_percent_change) {
		this.percent_of_par_mid_1d_percent_change = percent_of_par_mid_1d_percent_change;
	}

	public Double getCoupon_1d_percent_change() {
		return coupon_1d_percent_change;
	}

	public void setCoupon_1d_percent_change(Double coupon_1d_percent_change) {
		this.coupon_1d_percent_change = coupon_1d_percent_change;
	}

	public Double getQuote_spread_pv01_mid_1d_percent_change() {
		return quote_spread_pv01_mid_1d_percent_change;
	}

	public void setQuote_spread_pv01_mid_1d_percent_change(Double quote_spread_pv01_mid_1d_percent_change) {
		this.quote_spread_pv01_mid_1d_percent_change = quote_spread_pv01_mid_1d_percent_change;
	}

	public Double getPar_spread_pv01_mid_1d_percent_change() {
		return par_spread_pv01_mid_1d_percent_change;
	}

	public void setPar_spread_pv01_mid_1d_percent_change(Double par_spread_pv01_mid_1d_percent_change) {
		this.par_spread_pv01_mid_1d_percent_change = par_spread_pv01_mid_1d_percent_change;
	}

	public Double getCum_probability_of_default_1d_percent_change() {
		return cum_probability_of_default_1d_percent_change;
	}

	public void setCum_probability_of_default_1d_percent_change(Double cum_probability_of_default_1d_percent_change) {
		this.cum_probability_of_default_1d_percent_change = cum_probability_of_default_1d_percent_change;
	}

	public Double getHazard_rate_1d_percent_change() {
		return hazard_rate_1d_percent_change;
	}

	public void setHazard_rate_1d_percent_change(Double hazard_rate_1d_percent_change) {
		this.hazard_rate_1d_percent_change = hazard_rate_1d_percent_change;
	}

	public Double getMarket_recovery_rate_1d_percent_change() {
		return market_recovery_rate_1d_percent_change;
	}

	public void setMarket_recovery_rate_1d_percent_change(Double market_recovery_rate_1d_percent_change) {
		this.market_recovery_rate_1d_percent_change = market_recovery_rate_1d_percent_change;
	}

	public Double getUp_front_mid_1w_percent_change() {
		return up_front_mid_1w_percent_change;
	}

	public void setUp_front_mid_1w_percent_change(Double up_front_mid_1w_percent_change) {
		this.up_front_mid_1w_percent_change = up_front_mid_1w_percent_change;
	}

	public Double getQuote_spread_mid_1w_percent_change() {
		return quote_spread_mid_1w_percent_change;
	}

	public void setQuote_spread_mid_1w_percent_change(Double quote_spread_mid_1w_percent_change) {
		this.quote_spread_mid_1w_percent_change = quote_spread_mid_1w_percent_change;
	}

	public Double getPar_spread_mid_1w_percent_change() {
		return par_spread_mid_1w_percent_change;
	}

	public void setPar_spread_mid_1w_percent_change(Double par_spread_mid_1w_percent_change) {
		this.par_spread_mid_1w_percent_change = par_spread_mid_1w_percent_change;
	}

	public Double getPercent_of_par_mid_1w_percent_change() {
		return percent_of_par_mid_1w_percent_change;
	}

	public void setPercent_of_par_mid_1w_percent_change(Double percent_of_par_mid_1w_percent_change) {
		this.percent_of_par_mid_1w_percent_change = percent_of_par_mid_1w_percent_change;
	}

	public Double getCoupon_1w_percent_change() {
		return coupon_1w_percent_change;
	}

	public void setCoupon_1w_percent_change(Double coupon_1w_percent_change) {
		this.coupon_1w_percent_change = coupon_1w_percent_change;
	}

	public Double getQuote_spread_pv01_mid_1w_percent_change() {
		return quote_spread_pv01_mid_1w_percent_change;
	}

	public void setQuote_spread_pv01_mid_1w_percent_change(Double quote_spread_pv01_mid_1w_percent_change) {
		this.quote_spread_pv01_mid_1w_percent_change = quote_spread_pv01_mid_1w_percent_change;
	}

	public Double getPar_spread_pv01_mid_1w_percent_change() {
		return par_spread_pv01_mid_1w_percent_change;
	}

	public void setPar_spread_pv01_mid_1w_percent_change(Double par_spread_pv01_mid_1w_percent_change) {
		this.par_spread_pv01_mid_1w_percent_change = par_spread_pv01_mid_1w_percent_change;
	}

	public Double getCum_probability_of_default_1w_percent_change() {
		return cum_probability_of_default_1w_percent_change;
	}

	public void setCum_probability_of_default_1w_percent_change(Double cum_probability_of_default_1w_percent_change) {
		this.cum_probability_of_default_1w_percent_change = cum_probability_of_default_1w_percent_change;
	}

	public Double getHazard_rate_1w_percent_change() {
		return hazard_rate_1w_percent_change;
	}

	public void setHazard_rate_1w_percent_change(Double hazard_rate_1w_percent_change) {
		this.hazard_rate_1w_percent_change = hazard_rate_1w_percent_change;
	}

	public Double getMarket_recovery_rate_1w_percent_change() {
		return market_recovery_rate_1w_percent_change;
	}

	public void setMarket_recovery_rate_1w_percent_change(Double market_recovery_rate_1w_percent_change) {
		this.market_recovery_rate_1w_percent_change = market_recovery_rate_1w_percent_change;
	}

	@Override
	public String toString() {
		return "CDSDataLatestDTO [entityName=" + entityName + ", currency=" + currency + ", tenor=" + tenor
				+ ", seniority=" + seniority + ", restructuringType=" + restructuringType + ", business_date_time="
				+ business_date_time + ", identifier=" + identifier + ", maturity_date=" + maturity_date
				+ ", ice_cds_code=" + ice_cds_code + ", bbg_cds_ticker=" + bbg_cds_ticker + ", cma_entity_id="
				+ cma_entity_id + ", cma_ticker=" + cma_ticker + ", contract_standard=" + contract_standard
				+ ", sector=" + sector + ", region=" + region + ", coupon=" + coupon + ", market_quoting_convention="
				+ market_quoting_convention + ", instrument_type=" + instrument_type + ", default_date=" + default_date
				+ ", auction_date=" + auction_date + ", par_spread_bid=" + par_spread_bid + ", par_spread_mid="
				+ par_spread_mid + ", par_spread_offer=" + par_spread_offer + ", quote_spread_bid=" + quote_spread_bid
				+ ", quote_spread_mid=" + quote_spread_mid + ", quote_spread_offer=" + quote_spread_offer
				+ ", up_front_bid=" + up_front_bid + ", up_front_mid=" + up_front_mid + ", up_front_offer="
				+ up_front_offer + ", percent_of_par_bid=" + percent_of_par_bid + ", percent_of_par_mid="
				+ percent_of_par_mid + ", percent_of_par_offer=" + percent_of_par_offer + ", par_spread_pv01_bid="
				+ par_spread_pv01_bid + ", par_spread_pv01_mid=" + par_spread_pv01_mid + ", par_spread_pv01_offer="
				+ par_spread_pv01_offer + ", quote_spread_pv01_bid=" + quote_spread_pv01_bid
				+ ", quote_spread_pv01_mid=" + quote_spread_pv01_mid + ", quote_spread_pv01_offer="
				+ quote_spread_pv01_offer + ", hazard_rate=" + hazard_rate + ", cum_probability_of_default="
				+ cum_probability_of_default + ", cma_short_term_implied_rating=" + cma_short_term_implied_rating
				+ ", cma_long_term_implied_rating=" + cma_long_term_implied_rating + ", sp_rating=" + sp_rating
				+ ", moodys_rating=" + moodys_rating + ", delta=" + delta + ", reference=" + reference
				+ ", market_recovery_rate=" + market_recovery_rate + ", isda_recovery_rate=" + isda_recovery_rate
				+ ", devaluation_factor=" + devaluation_factor + ", observed_derived_indicator="
				+ observed_derived_indicator + ", derivation_type=" + derivation_type + ", ref_curve_info="
				+ ref_curve_info + ", last_contribution_date_time=" + last_contribution_date_time
				+ ", up_front_mid_1d_percent_change=" + up_front_mid_1d_percent_change
				+ ", quote_spread_mid_1d_percent_change=" + quote_spread_mid_1d_percent_change
				+ ", par_spread_mid_1d_percent_change=" + par_spread_mid_1d_percent_change
				+ ", percent_of_par_mid_1d_percent_change=" + percent_of_par_mid_1d_percent_change
				+ ", coupon_1d_percent_change=" + coupon_1d_percent_change
				+ ", quote_spread_pv01_mid_1d_percent_change=" + quote_spread_pv01_mid_1d_percent_change
				+ ", par_spread_pv01_mid_1d_percent_change=" + par_spread_pv01_mid_1d_percent_change
				+ ", cum_probability_of_default_1d_percent_change=" + cum_probability_of_default_1d_percent_change
				+ ", hazard_rate_1d_percent_change=" + hazard_rate_1d_percent_change
				+ ", market_recovery_rate_1d_percent_change=" + market_recovery_rate_1d_percent_change
				+ ", up_front_mid_1w_percent_change=" + up_front_mid_1w_percent_change
				+ ", quote_spread_mid_1w_percent_change=" + quote_spread_mid_1w_percent_change
				+ ", par_spread_mid_1w_percent_change=" + par_spread_mid_1w_percent_change
				+ ", percent_of_par_mid_1w_percent_change=" + percent_of_par_mid_1w_percent_change
				+ ", coupon_1w_percent_change=" + coupon_1w_percent_change
				+ ", quote_spread_pv01_mid_1w_percent_change=" + quote_spread_pv01_mid_1w_percent_change
				+ ", par_spread_pv01_mid_1w_percent_change=" + par_spread_pv01_mid_1w_percent_change
				+ ", cum_probability_of_default_1w_percent_change=" + cum_probability_of_default_1w_percent_change
				+ ", hazard_rate_1w_percent_change=" + hazard_rate_1w_percent_change
				+ ", market_recovery_rate_1w_percent_change=" + market_recovery_rate_1w_percent_change + "]";
	}
	
}
