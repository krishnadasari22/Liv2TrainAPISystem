package com.televisory.bond.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="fi_data_latest")
public class FIDataLatest {
	
	@Column(name = "as_of_date")
	Date as_of_date;
	
	@Id
	@Column(name = "isin_number	")
	String isin_number;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "ticker")
	String ticker;
	
	@Column(name = "current_period_coupon")
	private Double current_period_coupon;
	
	@Column(name = "maturity_date")
	Date maturity_date;

	@Column(name = "maturity_wal")
	private Double maturity_wal;

	@Column(name = "years_to_worst")
	private Double years_to_worst;

	@Column(name = "iso_currency_code")
	private String iso_currency_code;

	@Column(name = "country_name")
	private String country_name;
	
	@Column(name = "iso_country_code")
	private String iso_country_code;

	@Column(name = "industry_lvl_1_desc")
	private String industry_lvl_1_desc;

	@Column(name = "industry_lvl_2_desc")
	private String industry_lvl_2_desc;

	@Column(name = "industry_lvl_3_desc")
	private String industry_lvl_3_desc;

	@Column(name = "industry_lvl_4_desc")
	private String industry_lvl_4_desc;

	@Column(name = "semi_modified_duration")
	private Double modified_duration_sa;
	
	@Column(name = "semi_modified_duration_1w_percent_change")
	private Double semi_modified_duration_1w_percent_change;

	@Column(name = "semi_mod_dur_to_worst")
	private Double semi_mod_dur_to_worst;
	
	@Column(name="semi_mod_dur_to_worst_1w_percent_change")
	private Double semi_mod_dur_to_worst_1w_percent_change;

	@Column(name = "effective_duration")
	private Double effective_duration;
	
	@Column(name = "effective_duration_1w_percent_change")
	private Double effective_duration_1w_percent_change;

	@Column(name = "mod_duration_to_worst")
	private Double mod_duration_to_worst;
	
	@Column(name = "mod_duration_to_worst_1w_percent_change")
	private Double mod_duration_to_worst_1w_percent_change;

	@Column(name = "modified_duration")
	private Double modified_duration;
	
	@Column(name = "modified_duration_1w_percent_change")
	private Double modified_duration_1w_percent_change;

	@Column(name = "macaulay_duration")
	private Double macaulay_duration;
	
	@Column(name = "macaulay_duration_1w_percent_change")
	private Double macaulay_duration_1w_percent_change;

	@Column(name = "effective_convexity")
	private Double effective_convexity;
	
	@Column(name = "effective_convexity_1w_percent_change")
	private Double effective_convexity_1w_percent_change;

	@Column(name = "convexity_to_worst")
	private Double convexity_to_worst;
	
	@Column(name = "convexity_to_worst_1w_percent_change")
	private Double convexity_to_worst_1w_percent_change;
	
	@Column(name = "semi_convexity_to_worst")
	private Double semi_convexity_to_worst;
	
	@Column(name = "semi_convexity_to_worst_1w_percent_change")
	private Double semi_convexity_to_worst_1w_percent_change;

	@Column(name = "semi_convexity")
	private Double semi_convexity;
	
	@Column(name = "semi_convexity_1w_percent_change")
	private Double semi_convexity_1w_percent_change;

	@Column(name = "convexity_1w_percent_change")
	private Double convexity_1w_percent_change;
	
	@Column(name = "convexity")
	private Double convexity;

	@Column(name = "type")
	private String subordination_type;

	@Column(name = "spread_duration")
	private Double spread_duration;
	
	@Column(name = "spread_duration_1w_percent_change")
	private Double spread_duration_1w_percent_change;

	@Column(name = "maturity_type")
	private String maturity_type;

	@Column(name = "asset_swap_spread")
	private Integer asset_swap_spread;
	
	@Column(name = "asset_swap_spread_1w_percent_change")
	private Integer asset_swap_spread_1w_percent_change;

	@Column(name = "oas")
	private Integer oas;
	
	@Column(name = "oas_1w_percent_change")
	private Integer oas_1w_percent_change;

	@Column(name = "effective_yield")
	private Double effective_yield;
	
	@Column(name = "effective_yield_1w_percent_change")
	private Double effective_yield_1w_percent_change;

	@Column(name = "semi_yield_to_worst")
	private Double semi_yield_to_worst;
	
	@Column(name = "semi_yield_to_worst_1w_percent_change")
	private Double semi_yield_to_worst_1w_percent_change;

	@Column(name = "yield_to_worst_conv")
	private Double yield_to_worst_conv;
	
	@Column(name = "yield_to_worst_conv_1w_percent_change")
	private Double yield_to_worst_conv_1w_percent_change;

	@Column(name = "yield_to_maturity")
	private Double yield_to_maturity;
	
	@Column(name = "yield_to_maturity_1w_percent_change")
	private Double yield_to_maturity_1w_percent_change;

	@Column(name = "semi_mod_dur")
	private Double semi_mod_dur;
	
	@Column(name = "semi_mod_dur_1w_percent_change")
	private Double semi_mod_dur_1w_percent_change;

	@Column(name = "yield_to_next_call")
	private Double yield_to_next_call;
	
	@Column(name = "yield_to_next_call_1w_percent_change")
	private Double yield_to_next_call_1w_percent_change;

	@Column(name = "price")
	private Double price;

	@Column(name = "accrued_interest")
	private Double accrued_interest;

	@Column(name = "spread_to_worst")
	private Integer spread_to_worst;
	
	@Column(name = "spread_to_worst_1w_percent_change")
	private Integer spread_to_worst_1w_percent_change;

	@Column(name = "libor_oas")
	private Integer libor_oas;
	
	@Column(name = "libor_oas_1w_percent_change")
	private Integer libor_oas_1w_percent_change;
	
	@Column(name = "index_name")
	private String index_name;
	
	@Column(name = "bond_equiv")
	private Double bond_equiv;
	
	@Column(name="oldest_price_history_date")
	private Date oldest_price_history_date;

	public Date getAs_of_date() {
		return as_of_date;
	}

	public void setAs_of_date(Date as_of_date) {
		this.as_of_date = as_of_date;
	}

	public String getIsin_number() {
		return isin_number;
	}

	public void setIsin_number(String isin_number) {
		this.isin_number = isin_number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public Double getCurrent_period_coupon() {
		return current_period_coupon;
	}

	public void setCurrent_period_coupon(Double current_period_coupon) {
		this.current_period_coupon = current_period_coupon;
	}

	public Date getMaturity_date() {
		return maturity_date;
	}

	public void setMaturity_date(Date maturity_date) {
		this.maturity_date = maturity_date;
	}

	public Double getMaturity_wal() {
		return maturity_wal;
	}

	public void setMaturity_wal(Double maturity_wal) {
		this.maturity_wal = maturity_wal;
	}

	public Double getYears_to_worst() {
		return years_to_worst;
	}

	public void setYears_to_worst(Double years_to_worst) {
		this.years_to_worst = years_to_worst;
	}

	public String getIso_currency_code() {
		return iso_currency_code;
	}

	public void setIso_currency_code(String iso_currency_code) {
		this.iso_currency_code = iso_currency_code;
	}

	public String getIso_country_code() {
		return iso_country_code;
	}

	public void setIso_country_code(String iso_country_code) {
		this.iso_country_code = iso_country_code;
	}

	public String getIndustry_lvl_1_desc() {
		return industry_lvl_1_desc;
	}

	public void setIndustry_lvl_1_desc(String industry_lvl_1_desc) {
		this.industry_lvl_1_desc = industry_lvl_1_desc;
	}

	public String getIndustry_lvl_2_desc() {
		return industry_lvl_2_desc;
	}

	public void setIndustry_lvl_2_desc(String industry_lvl_2_desc) {
		this.industry_lvl_2_desc = industry_lvl_2_desc;
	}

	public String getIndustry_lvl_3_desc() {
		return industry_lvl_3_desc;
	}

	public void setIndustry_lvl_3_desc(String industry_lvl_3_desc) {
		this.industry_lvl_3_desc = industry_lvl_3_desc;
	}

	public String getIndustry_lvl_4_desc() {
		return industry_lvl_4_desc;
	}

	public void setIndustry_lvl_4_desc(String industry_lvl_4_desc) {
		this.industry_lvl_4_desc = industry_lvl_4_desc;
	}

	public Double getModified_duration_sa() {
		return modified_duration_sa;
	}

	public void setModified_duration_sa(Double modified_duration_sa) {
		this.modified_duration_sa = modified_duration_sa;
	}

	public Double getSemi_modified_duration_1w_percent_change() {
		return semi_modified_duration_1w_percent_change;
	}

	public void setSemi_modified_duration_1w_percent_change(Double semi_modified_duration_1w_percent_change) {
		this.semi_modified_duration_1w_percent_change = semi_modified_duration_1w_percent_change;
	}

	public Double getSemi_mod_dur_to_worst() {
		return semi_mod_dur_to_worst;
	}

	public void setSemi_mod_dur_to_worst(Double semi_mod_dur_to_worst) {
		this.semi_mod_dur_to_worst = semi_mod_dur_to_worst;
	}

	public Double getSemi_mod_dur_to_worst_1w_percent_change() {
		return semi_mod_dur_to_worst_1w_percent_change;
	}

	public void setSemi_mod_dur_to_worst_1w_percent_change(Double semi_mod_dur_to_worst_1w_percent_change) {
		this.semi_mod_dur_to_worst_1w_percent_change = semi_mod_dur_to_worst_1w_percent_change;
	}

	public Double getEffective_duration() {
		return effective_duration;
	}

	public void setEffective_duration(Double effective_duration) {
		this.effective_duration = effective_duration;
	}

	public Double getEffective_duration_1w_percent_change() {
		return effective_duration_1w_percent_change;
	}

	public void setEffective_duration_1w_percent_change(Double effective_duration_1w_percent_change) {
		this.effective_duration_1w_percent_change = effective_duration_1w_percent_change;
	}

	public Double getMod_duration_to_worst() {
		return mod_duration_to_worst;
	}

	public void setMod_duration_to_worst(Double mod_duration_to_worst) {
		this.mod_duration_to_worst = mod_duration_to_worst;
	}

	public Double getMod_duration_to_worst_1w_percent_change() {
		return mod_duration_to_worst_1w_percent_change;
	}

	public void setMod_duration_to_worst_1w_percent_change(Double mod_duration_to_worst_1w_percent_change) {
		this.mod_duration_to_worst_1w_percent_change = mod_duration_to_worst_1w_percent_change;
	}

	public Double getModified_duration() {
		return modified_duration;
	}

	public void setModified_duration(Double modified_duration) {
		this.modified_duration = modified_duration;
	}

	public Double getModified_duration_1w_percent_change() {
		return modified_duration_1w_percent_change;
	}

	public void setModified_duration_1w_percent_change(Double modified_duration_1w_percent_change) {
		this.modified_duration_1w_percent_change = modified_duration_1w_percent_change;
	}

	public Double getMacaulay_duration() {
		return macaulay_duration;
	}

	public void setMacaulay_duration(Double macaulay_duration) {
		this.macaulay_duration = macaulay_duration;
	}

	public Double getMacaulay_duration_1w_percent_change() {
		return macaulay_duration_1w_percent_change;
	}

	public void setMacaulay_duration_1w_percent_change(Double macaulay_duration_1w_percent_change) {
		this.macaulay_duration_1w_percent_change = macaulay_duration_1w_percent_change;
	}

	public Double getEffective_convexity() {
		return effective_convexity;
	}

	public void setEffective_convexity(Double effective_convexity) {
		this.effective_convexity = effective_convexity;
	}

	public Double getEffective_convexity_1w_percent_change() {
		return effective_convexity_1w_percent_change;
	}

	public void setEffective_convexity_1w_percent_change(Double effective_convexity_1w_percent_change) {
		this.effective_convexity_1w_percent_change = effective_convexity_1w_percent_change;
	}

	public Double getConvexity_to_worst() {
		return convexity_to_worst;
	}

	public void setConvexity_to_worst(Double convexity_to_worst) {
		this.convexity_to_worst = convexity_to_worst;
	}

	public Double getConvexity_to_worst_1w_percent_change() {
		return convexity_to_worst_1w_percent_change;
	}

	public void setConvexity_to_worst_1w_percent_change(Double convexity_to_worst_1w_percent_change) {
		this.convexity_to_worst_1w_percent_change = convexity_to_worst_1w_percent_change;
	}

	public Double getSemi_convexity_to_worst() {
		return semi_convexity_to_worst;
	}

	public void setSemi_convexity_to_worst(Double semi_convexity_to_worst) {
		this.semi_convexity_to_worst = semi_convexity_to_worst;
	}

	public Double getSemi_convexity_to_worst_1w_percent_change() {
		return semi_convexity_to_worst_1w_percent_change;
	}

	public void setSemi_convexity_to_worst_1w_percent_change(Double semi_convexity_to_worst_1w_percent_change) {
		this.semi_convexity_to_worst_1w_percent_change = semi_convexity_to_worst_1w_percent_change;
	}

	public Double getSemi_convexity() {
		return semi_convexity;
	}

	public void setSemi_convexity(Double semi_convexity) {
		this.semi_convexity = semi_convexity;
	}

	public Double getSemi_convexity_1w_percent_change() {
		return semi_convexity_1w_percent_change;
	}

	public void setSemi_convexity_1w_percent_change(Double semi_convexity_1w_percent_change) {
		this.semi_convexity_1w_percent_change = semi_convexity_1w_percent_change;
	}

	public Double getConvexity_1w_percent_change() {
		return convexity_1w_percent_change;
	}

	public void setConvexity_1w_percent_change(Double convexity_1w_percent_change) {
		this.convexity_1w_percent_change = convexity_1w_percent_change;
	}

	public Double getConvexity() {
		return convexity;
	}

	public void setConvexity(Double convexity) {
		this.convexity = convexity;
	}

	public String getSubordination_type() {
		return subordination_type;
	}

	public void setSubordination_type(String subordination_type) {
		this.subordination_type = subordination_type;
	}

	public Double getSpread_duration() {
		return spread_duration;
	}

	public void setSpread_duration(Double spread_duration) {
		this.spread_duration = spread_duration;
	}

	public Double getSpread_duration_1w_percent_change() {
		return spread_duration_1w_percent_change;
	}

	public void setSpread_duration_1w_percent_change(Double spread_duration_1w_percent_change) {
		this.spread_duration_1w_percent_change = spread_duration_1w_percent_change;
	}

	public String getMaturity_type() {
		return maturity_type;
	}

	public void setMaturity_type(String maturity_type) {
		this.maturity_type = maturity_type;
	}

	public Integer getAsset_swap_spread() {
		return asset_swap_spread;
	}

	public void setAsset_swap_spread(Integer asset_swap_spread) {
		this.asset_swap_spread = asset_swap_spread;
	}

	public Integer getAsset_swap_spread_1w_percent_change() {
		return asset_swap_spread_1w_percent_change;
	}

	public void setAsset_swap_spread_1w_percent_change(Integer asset_swap_spread_1w_percent_change) {
		this.asset_swap_spread_1w_percent_change = asset_swap_spread_1w_percent_change;
	}

	public Integer getOas() {
		return oas;
	}

	public void setOas(Integer oas) {
		this.oas = oas;
	}

	public Integer getOas_1w_percent_change() {
		return oas_1w_percent_change;
	}

	public void setOas_1w_percent_change(Integer oas_1w_percent_change) {
		this.oas_1w_percent_change = oas_1w_percent_change;
	}

	public Double getEffective_yield() {
		return effective_yield;
	}

	public void setEffective_yield(Double effective_yield) {
		this.effective_yield = effective_yield;
	}

	public Double getEffective_yield_1w_percent_change() {
		return effective_yield_1w_percent_change;
	}

	public void setEffective_yield_1w_percent_change(Double effective_yield_1w_percent_change) {
		this.effective_yield_1w_percent_change = effective_yield_1w_percent_change;
	}

	public Double getSemi_yield_to_worst() {
		return semi_yield_to_worst;
	}

	public void setSemi_yield_to_worst(Double semi_yield_to_worst) {
		this.semi_yield_to_worst = semi_yield_to_worst;
	}

	public Double getSemi_yield_to_worst_1w_percent_change() {
		return semi_yield_to_worst_1w_percent_change;
	}

	public void setSemi_yield_to_worst_1w_percent_change(Double semi_yield_to_worst_1w_percent_change) {
		this.semi_yield_to_worst_1w_percent_change = semi_yield_to_worst_1w_percent_change;
	}

	public Double getYield_to_worst_conv() {
		return yield_to_worst_conv;
	}

	public void setYield_to_worst_conv(Double yield_to_worst_conv) {
		this.yield_to_worst_conv = yield_to_worst_conv;
	}

	public Double getYield_to_worst_conv_1w_percent_change() {
		return yield_to_worst_conv_1w_percent_change;
	}

	public void setYield_to_worst_conv_1w_percent_change(Double yield_to_worst_conv_1w_percent_change) {
		this.yield_to_worst_conv_1w_percent_change = yield_to_worst_conv_1w_percent_change;
	}

	public Double getYield_to_maturity() {
		return yield_to_maturity;
	}

	public void setYield_to_maturity(Double yield_to_maturity) {
		this.yield_to_maturity = yield_to_maturity;
	}

	public Double getYield_to_maturity_1w_percent_change() {
		return yield_to_maturity_1w_percent_change;
	}

	public void setYield_to_maturity_1w_percent_change(Double yield_to_maturity_1w_percent_change) {
		this.yield_to_maturity_1w_percent_change = yield_to_maturity_1w_percent_change;
	}

	public Double getSemi_mod_dur() {
		return semi_mod_dur;
	}

	public void setSemi_mod_dur(Double semi_mod_dur) {
		this.semi_mod_dur = semi_mod_dur;
	}

	public Double getSemi_mod_dur_1w_percent_change() {
		return semi_mod_dur_1w_percent_change;
	}

	public void setSemi_mod_dur_1w_percent_change(Double semi_mod_dur_1w_percent_change) {
		this.semi_mod_dur_1w_percent_change = semi_mod_dur_1w_percent_change;
	}

	public Double getYield_to_next_call() {
		return yield_to_next_call;
	}

	public void setYield_to_next_call(Double yield_to_next_call) {
		this.yield_to_next_call = yield_to_next_call;
	}

	public Double getYield_to_next_call_1w_percent_change() {
		return yield_to_next_call_1w_percent_change;
	}

	public void setYield_to_next_call_1w_percent_change(Double yield_to_next_call_1w_percent_change) {
		this.yield_to_next_call_1w_percent_change = yield_to_next_call_1w_percent_change;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getAccrued_interest() {
		return accrued_interest;
	}

	public void setAccrued_interest(Double accrued_interest) {
		this.accrued_interest = accrued_interest;
	}

	public Integer getSpread_to_worst() {
		return spread_to_worst;
	}

	public void setSpread_to_worst(Integer spread_to_worst) {
		this.spread_to_worst = spread_to_worst;
	}

	public Integer getSpread_to_worst_1w_percent_change() {
		return spread_to_worst_1w_percent_change;
	}

	public void setSpread_to_worst_1w_percent_change(Integer spread_to_worst_1w_percent_change) {
		this.spread_to_worst_1w_percent_change = spread_to_worst_1w_percent_change;
	}

	public Integer getLibor_oas() {
		return libor_oas;
	}

	public void setLibor_oas(Integer libor_oas) {
		this.libor_oas = libor_oas;
	}

	public Integer getLibor_oas_1w_percent_change() {
		return libor_oas_1w_percent_change;
	}

	public void setLibor_oas_1w_percent_change(Integer libor_oas_1w_percent_change) {
		this.libor_oas_1w_percent_change = libor_oas_1w_percent_change;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public Double getBond_equiv() {
		return bond_equiv;
	}

	public void setBond_equiv(Double bond_equiv) {
		this.bond_equiv = bond_equiv;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public Date getOldest_price_history_date() {
		return oldest_price_history_date;
	}

	public void setOldest_price_history_date(Date oldest_price_history_date) {
		this.oldest_price_history_date = oldest_price_history_date;
	}

	@Override
	public String toString() {
		return "FIDataLatest [as_of_date=" + as_of_date + ", isin_number=" + isin_number + ", description="
				+ description + ", ticker=" + ticker + ", current_period_coupon=" + current_period_coupon
				+ ", maturity_date=" + maturity_date + ", maturity_wal=" + maturity_wal + ", years_to_worst="
				+ years_to_worst + ", iso_currency_code=" + iso_currency_code + ", country_name=" + country_name
				+ ", iso_country_code=" + iso_country_code + ", industry_lvl_1_desc=" + industry_lvl_1_desc
				+ ", industry_lvl_2_desc=" + industry_lvl_2_desc + ", industry_lvl_3_desc=" + industry_lvl_3_desc
				+ ", industry_lvl_4_desc=" + industry_lvl_4_desc + ", modified_duration_sa=" + modified_duration_sa
				+ ", semi_modified_duration_1w_percent_change=" + semi_modified_duration_1w_percent_change
				+ ", semi_mod_dur_to_worst=" + semi_mod_dur_to_worst + ", semi_mod_dur_to_worst_1w_percent_change="
				+ semi_mod_dur_to_worst_1w_percent_change + ", effective_duration=" + effective_duration
				+ ", effective_duration_1w_percent_change=" + effective_duration_1w_percent_change
				+ ", mod_duration_to_worst=" + mod_duration_to_worst + ", mod_duration_to_worst_1w_percent_change="
				+ mod_duration_to_worst_1w_percent_change + ", modified_duration=" + modified_duration
				+ ", modified_duration_1w_percent_change=" + modified_duration_1w_percent_change
				+ ", macaulay_duration=" + macaulay_duration + ", macaulay_duration_1w_percent_change="
				+ macaulay_duration_1w_percent_change + ", effective_convexity=" + effective_convexity
				+ ", effective_convexity_1w_percent_change=" + effective_convexity_1w_percent_change
				+ ", convexity_to_worst=" + convexity_to_worst + ", convexity_to_worst_1w_percent_change="
				+ convexity_to_worst_1w_percent_change + ", semi_convexity_to_worst=" + semi_convexity_to_worst
				+ ", semi_convexity_to_worst_1w_percent_change=" + semi_convexity_to_worst_1w_percent_change
				+ ", semi_convexity=" + semi_convexity + ", semi_convexity_1w_percent_change="
				+ semi_convexity_1w_percent_change + ", convexity_1w_percent_change=" + convexity_1w_percent_change
				+ ", convexity=" + convexity + ", subordination_type=" + subordination_type + ", spread_duration="
				+ spread_duration + ", spread_duration_1w_percent_change=" + spread_duration_1w_percent_change
				+ ", maturity_type=" + maturity_type + ", asset_swap_spread=" + asset_swap_spread
				+ ", asset_swap_spread_1w_percent_change=" + asset_swap_spread_1w_percent_change + ", oas=" + oas
				+ ", oas_1w_percent_change=" + oas_1w_percent_change + ", effective_yield=" + effective_yield
				+ ", effective_yield_1w_percent_change=" + effective_yield_1w_percent_change + ", semi_yield_to_worst="
				+ semi_yield_to_worst + ", semi_yield_to_worst_1w_percent_change="
				+ semi_yield_to_worst_1w_percent_change + ", yield_to_worst_conv=" + yield_to_worst_conv
				+ ", yield_to_worst_conv_1w_percent_change=" + yield_to_worst_conv_1w_percent_change
				+ ", yield_to_maturity=" + yield_to_maturity + ", yield_to_maturity_1w_percent_change="
				+ yield_to_maturity_1w_percent_change + ", semi_mod_dur=" + semi_mod_dur
				+ ", semi_mod_dur_1w_percent_change=" + semi_mod_dur_1w_percent_change + ", yield_to_next_call="
				+ yield_to_next_call + ", yield_to_next_call_1w_percent_change=" + yield_to_next_call_1w_percent_change
				+ ", price=" + price + ", accrued_interest=" + accrued_interest + ", spread_to_worst=" + spread_to_worst
				+ ", spread_to_worst_1w_percent_change=" + spread_to_worst_1w_percent_change + ", libor_oas="
				+ libor_oas + ", libor_oas_1w_percent_change=" + libor_oas_1w_percent_change + ", index_name="
				+ index_name + ", bond_equiv=" + bond_equiv + "]";
	}
	
	


}
