package com.televisory.bond.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="etd_data_latest")
public class DerivativeLatestData {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "symbol")
	private String identifier;
	
	@Column(name="owncode")
	private String owncode;
	
	@Column(name="aii_composite")
	private String aii_composite;
	
	@Column(name="esignal_id")
	private String esignal_id;
	
	@Column(name="isin")
	private String isin;
	
	@Column(name="bb_root")
	private String bb_root;
	
	@Column(name="as_on_date")
	private Date as_on_date;
	
	@Column(name="bb_root_market_sector")
	private String bb_root_market_sector;
	
	@Column(name="bb_ticker")
	private String bb_ticker;
	
	@Column(name="bb_price_source")
	private String bb_price_source;
	
	@Column(name="bb_market_sector")
	private String bb_market_sector;
	
	@Column(name="figi")
	private String figi;
	
	@Column(name="figi_comp")
	private String figi_comp;
	
	@Column(name="exchange_ticker")
	private String exchange_ticker;
	
	@Column(name="osi_21")
	private String osi_21; 
	
	@Column(name="asset_type")
	private String asset_type ; 
	
	@Column(name="underlying_name")
	private String underlying_name;
	
	@Column(name="contract_description")
	private String contract_description;
	
	@Column(name="underlying_type")
	private String underlying_type; 
	
	@Column(name="underlying_ticker_root")
	private String underlying_ticker_root;
	
	@Column(name="underlying_ticker_contract")
	private String underlying_ticker_contract;
	
	@Column(name="underlying_isin")
	private String underlying_isin;
	
	@Column(name="underlying_sedol")
	private String underlying_sedol; 
	
	@Column(name="exchange_name")
	private String exchange_name; 
	
	@Column(name="country_code")
	private String country_code; 
	
	@Column(name="exchange_code")
	private String exchange_code; 
	
	@Column(name="expiry_date")
	private Date expiry_date; 
	
	@Column(name="maturity")
	private Date maturity; 
	
	@Column(name="call_put")
	private String call_put; 
	
	@Column(name="strike")
	private Double strike; 
	
	@Column(name="option_type")
	private String option_type; 
	
	@Column(name="contract_size_units")
	private String contract_size_units;
	
	@Column(name="contract_size")
	private Double contract_size ; 
	
	@Column(name="multiplier")
	private Integer multiplier;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="option_ex_type_new")
	private String option_ex_type_new; 
	
	@Column(name="tick_size_var")
	private String tick_size_var;
	
	@Column(name="tick_value")
	private Double tick_value; 
	
	@Column(name="listing_mic")
	private String listing_mic; 
	
	@Column(name="sector")
	private String sector; 
	
	@Column(name="subsector")
	private String subsector;
	
	@Column(name="aifmd")
	private String aifmd;
	
	@Column(name="r20")
	private String r20;
	
	@Column(name="omegaid")
	private String omegaid;
	
	@Column(name="analystics_cusip")
	private String analystics_cusip; 
	
	@Column(name="expiration_cycle")
	private String expiration_cycle; 
	
	@Column(name="settlement_type")
	private String settlement_type; 
	
	@Column(name="contract_version")
	private Double contract_version; 
	
	@Column(name="inst_status")
	private String inst_status; 
	
	@Column(name="inst_status_date")
	private Date inst_status_date; 
	
	@Column(name="underlying_cusip")
	private String underlying_cusip; 
	
	@Column(name="rolling_ticker")
	private String rolling_ticker; 
	
	@Column(name="product_root")
	private String product_root;
	
	@Column(name="first_trade_date")
	private Date first_trade_date; 
	
	@Column(name="first_notice_date")
	private Date first_notice_date; 
	
	@Column(name="delivery_start_date")
	private Date delivery_start_date; 
	
	@Column(name="delivery_end_date")
	private Date delivery_end_date; 
	
	@Column(name="bid_latest")
	private Double bid_latest; 
	
	@Column(name="ask_latest")
	private Double ask_latest; 
	
	@Column(name="quote_date_latest")
	private Date quote_date_latest; 
	
	@Column(name="open")
	private Double open; 
	
	@Column(name="high")
	private Double high; 
	
	@Column(name="low")
	private Double low; 
	
	@Column(name="last_trade")
	private Double last_trade; 
	
	@Column(name="last_trade_date")
	private Date last_trade_date; 
	
	@Column(name="latest_settlement")
	private Double latest_settlement;
	
	@Column(name="latest_settlement_1d_percent_change")
	private Double latest_settlement_1d_percent_change;
	
	@Column(name="latest_settlement_1w_percent_change")
	private Double latest_settlement_1w_percent_change;
	
	@Column(name="latest_settlement_1m_percent_change")
	private Double latest_settlement_1m_percent_change;
	
	@Column(name="latest_settlement_date")
	private Date latest_settlement_date; 
	
	@Column(name="total_volume")
	private Double total_volume; 
	
	@Column(name="open_interest")
	private Double open_interest;
	
	public String getOwncode() {
		return owncode;
	}
	public void setOwncode(String owncode) {
		this.owncode = owncode;
	}
	public String getAii_composite() {
		return aii_composite;
	}
	public void setAii_composite(String aii_composite) {
		this.aii_composite = aii_composite;
	}
	public String getEsignal_id() {
		return esignal_id;
	}
	public void setEsignal_id(String esignal_id) {
		this.esignal_id = esignal_id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getBb_root() {
		return bb_root;
	}
	public void setBb_root(String bb_root) {
		this.bb_root = bb_root;
	}
	public String getBb_root_market_sector() {
		return bb_root_market_sector;
	}
	public void setBb_root_market_sector(String bb_root_market_sector) {
		this.bb_root_market_sector = bb_root_market_sector;
	}
	public String getBb_ticker() {
		return bb_ticker;
	}
	public void setBb_ticker(String bb_ticker) {
		this.bb_ticker = bb_ticker;
	}
	public String getBb_price_source() {
		return bb_price_source;
	}
	public void setBb_price_source(String bb_price_source) {
		this.bb_price_source = bb_price_source;
	}
	public String getBb_market_sector() {
		return bb_market_sector;
	}
	public void setBb_market_sector(String bb_market_sector) {
		this.bb_market_sector = bb_market_sector;
	}
	public String getFigi() {
		return figi;
	}
	public void setFigi(String figi) {
		this.figi = figi;
	}
	public String getFigi_comp() {
		return figi_comp;
	}
	public void setFigi_comp(String figi_comp) {
		this.figi_comp = figi_comp;
	}
	public String getExchange_ticker() {
		return exchange_ticker;
	}
	public void setExchange_ticker(String exchange_ticker) {
		this.exchange_ticker = exchange_ticker;
	}
	public String getOsi_21() {
		return osi_21;
	}
	public void setOsi_21(String osi_21) {
		this.osi_21 = osi_21;
	}
	public String getAsset_type() {
		return asset_type;
	}
	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}
	public String getUnderlying_name() {
		return underlying_name;
	}
	public void setUnderlying_name(String underlying_name) {
		this.underlying_name = underlying_name;
	}
	public String getContract_description() {
		return contract_description;
	}
	public void setContract_description(String contract_description) {
		this.contract_description = contract_description;
	}
	public String getUnderlying_type() {
		return underlying_type;
	}
	public void setUnderlying_type(String underlying_type) {
		this.underlying_type = underlying_type;
	}
	public String getUnderlying_ticker_root() {
		return underlying_ticker_root;
	}
	public void setUnderlying_ticker_root(String underlying_ticker_root) {
		this.underlying_ticker_root = underlying_ticker_root;
	}
	public String getUnderlying_ticker_contract() {
		return underlying_ticker_contract;
	}
	public void setUnderlying_ticker_contract(String underlying_ticker_contract) {
		this.underlying_ticker_contract = underlying_ticker_contract;
	}
	public String getUnderlying_isin() {
		return underlying_isin;
	}
	public void setUnderlying_isin(String underlying_isin) {
		this.underlying_isin = underlying_isin;
	}
	public String getUnderlying_sedol() {
		return underlying_sedol;
	}
	public void setUnderlying_sedol(String underlying_sedol) {
		this.underlying_sedol = underlying_sedol;
	}
	public String getExchange_name() {
		return exchange_name;
	}
	public void setExchange_name(String exchange_name) {
		this.exchange_name = exchange_name;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getExchange_code() {
		return exchange_code;
	}
	public void setExchange_code(String exchange_code) {
		this.exchange_code = exchange_code;
	}
	public Date getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	public Date getMaturity() {
		return maturity;
	}
	public void setMaturity(Date maturity) {
		this.maturity = maturity;
	}
	public String getCall_put() {
		return call_put;
	}
	public void setCall_put(String call_put) {
		this.call_put = call_put;
	}
	public Double getStrike() {
		return strike;
	}
	public void setStrike(Double strike) {
		this.strike = strike;
	}
	public String getOption_type() {
		return option_type;
	}
	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}
	public String getContract_size_units() {
		return contract_size_units;
	}
	public void setContract_size_units(String contract_size_units) {
		this.contract_size_units = contract_size_units;
	}
	public Double getContract_size() {
		return contract_size;
	}
	public void setContract_size(Double contract_size) {
		this.contract_size = contract_size;
	}
	public Integer getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(Integer multiplier) {
		this.multiplier = multiplier;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOption_ex_type_new() {
		return option_ex_type_new;
	}
	public void setOption_ex_type_new(String option_ex_type_new) {
		this.option_ex_type_new = option_ex_type_new;
	}
	public String getTick_size_var() {
		return tick_size_var;
	}
	public void setTick_size_var(String tick_size_var) {
		this.tick_size_var = tick_size_var;
	}
	public Double getTick_value() {
		return tick_value;
	}
	public void setTick_value(Double tick_value) {
		this.tick_value = tick_value;
	}
	public String getListing_mic() {
		return listing_mic;
	}
	public void setListing_mic(String listing_mic) {
		this.listing_mic = listing_mic;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getSubsector() {
		return subsector;
	}
	public void setSubsector(String subsector) {
		this.subsector = subsector;
	}
	public String getAifmd() {
		return aifmd;
	}
	public void setAifmd(String aifmd) {
		this.aifmd = aifmd;
	}
	public String getR20() {
		return r20;
	}
	public void setR20(String r20) {
		this.r20 = r20;
	}
	public String getOmegaid() {
		return omegaid;
	}
	public void setOmegaid(String omegaid) {
		this.omegaid = omegaid;
	}
	public String getAnalystics_cusip() {
		return analystics_cusip;
	}
	public void setAnalystics_cusip(String analystics_cusip) {
		this.analystics_cusip = analystics_cusip;
	}
	public String getExpiration_cycle() {
		return expiration_cycle;
	}
	public void setExpiration_cycle(String expiration_cycle) {
		this.expiration_cycle = expiration_cycle;
	}
	public String getSettlement_type() {
		return settlement_type;
	}
	public void setSettlement_type(String settlement_type) {
		this.settlement_type = settlement_type;
	}
	public Double getContract_version() {
		return contract_version;
	}
	public void setContract_version(Double contract_version) {
		this.contract_version = contract_version;
	}
	public String getInst_status() {
		return inst_status;
	}
	public void setInst_status(String inst_status) {
		this.inst_status = inst_status;
	}
	public Date getInst_status_date() {
		return inst_status_date;
	}
	public void setInst_status_date(Date inst_status_date) {
		this.inst_status_date = inst_status_date;
	}
	public String getUnderlying_cusip() {
		return underlying_cusip;
	}
	public void setUnderlying_cusip(String underlying_cusip) {
		this.underlying_cusip = underlying_cusip;
	}
	public String getRolling_ticker() {
		return rolling_ticker;
	}
	public void setRolling_ticker(String rolling_ticker) {
		this.rolling_ticker = rolling_ticker;
	}
	public String getProduct_root() {
		return product_root;
	}
	public void setProduct_root(String product_root) {
		this.product_root = product_root;
	}
	public Date getFirst_trade_date() {
		return first_trade_date;
	}
	public void setFirst_trade_date(Date first_trade_date) {
		this.first_trade_date = first_trade_date;
	}
	public Date getFirst_notice_date() {
		return first_notice_date;
	}
	public void setFirst_notice_date(Date first_notice_date) {
		this.first_notice_date = first_notice_date;
	}
	public Date getDelivery_start_date() {
		return delivery_start_date;
	}
	public void setDelivery_start_date(Date delivery_start_date) {
		this.delivery_start_date = delivery_start_date;
	}
	public Date getDelivery_end_date() {
		return delivery_end_date;
	}
	public void setDelivery_end_date(Date delivery_end_date) {
		this.delivery_end_date = delivery_end_date;
	}
	public Double getBid_latest() {
		return bid_latest;
	}
	public void setBid_latest(Double bid_latest) {
		this.bid_latest = bid_latest;
	}
	public Double getAsk_latest() {
		return ask_latest;
	}
	public void setAsk_latest(Double ask_latest) {
		this.ask_latest = ask_latest;
	}
	public Date getQuote_date_latest() {
		return quote_date_latest;
	}
	public void setQuote_date_latest(Date quote_date_latest) {
		this.quote_date_latest = quote_date_latest;
	}
	public Double getOpen() {
		return open;
	}
	public void setOpen(Double open) {
		this.open = open;
	}
	public Double getHigh() {
		return high;
	}
	public void setHigh(Double high) {
		this.high = high;
	}
	public Double getLow() {
		return low;
	}
	public void setLow(Double low) {
		this.low = low;
	}
	public Double getLast_trade() {
		return last_trade;
	}
	public void setLast_trade(Double last_trade) {
		this.last_trade = last_trade;
	}
	public Date getLast_trade_date() {
		return last_trade_date;
	}
	public void setLast_trade_date(Date last_trade_date) {
		this.last_trade_date = last_trade_date;
	}
	public Double getLatest_settlement() {
		return latest_settlement;
	}
	public void setLatest_settlement(Double latest_settlement) {
		this.latest_settlement = latest_settlement;
	}
	public Double getLatest_settlement_1d_percent_change() {
		return latest_settlement_1d_percent_change;
	}
	public void setLatest_settlement_1d_percent_change(Double latest_settlement_1d_percent_change) {
		this.latest_settlement_1d_percent_change = latest_settlement_1d_percent_change;
	}
	public Double getLatest_settlement_1w_percent_change() {
		return latest_settlement_1w_percent_change;
	}
	public void setLatest_settlement_1w_percent_change(Double latest_settlement_1w_percent_change) {
		this.latest_settlement_1w_percent_change = latest_settlement_1w_percent_change;
	}
	public Double getLatest_settlement_1m_percent_change() {
		return latest_settlement_1m_percent_change;
	}
	public void setLatest_settlement_1m_percent_change(Double latest_settlement_1m_percent_change) {
		this.latest_settlement_1m_percent_change = latest_settlement_1m_percent_change;
	}
	public Date getLatest_settlement_date() {
		return latest_settlement_date;
	}
	public void setLatest_settlement_date(Date latest_settlement_date) {
		this.latest_settlement_date = latest_settlement_date;
	}
	public Double getTotal_volume() {
		return total_volume;
	}
	public void setTotal_volume(Double total_volume) {
		this.total_volume = total_volume;
	}
	public Double getOpen_interest() {
		return open_interest;
	}
	public void setOpen_interest(Double open_interest) {
		this.open_interest = open_interest;
	}
	
	
	public Date getAs_on_date() {
		return as_on_date;
	}
	public void setAs_on_date(Date as_on_date) {
		this.as_on_date = as_on_date;
	}
	@Override
	public String toString() {
		return "DerivativeLatestData [identifier=" + identifier + ", owncode=" + owncode + ", aii_composite="
				+ aii_composite + ", esignal_id=" + esignal_id + ", isin=" + isin + ", bb_root=" + bb_root
				+ ", as_on_date=" + as_on_date + ", bb_root_market_sector=" + bb_root_market_sector + ", bb_ticker="
				+ bb_ticker + ", bb_price_source=" + bb_price_source + ", bb_market_sector=" + bb_market_sector
				+ ", figi=" + figi + ", figi_comp=" + figi_comp + ", exchange_ticker=" + exchange_ticker + ", osi_21="
				+ osi_21 + ", asset_type=" + asset_type + ", underlying_name=" + underlying_name
				+ ", contract_description=" + contract_description + ", underlying_type=" + underlying_type
				+ ", underlying_ticker_root=" + underlying_ticker_root + ", underlying_ticker_contract="
				+ underlying_ticker_contract + ", underlying_isin=" + underlying_isin + ", underlying_sedol="
				+ underlying_sedol + ", exchange_name=" + exchange_name + ", country_code=" + country_code
				+ ", exchange_code=" + exchange_code + ", expiry_date=" + expiry_date + ", maturity=" + maturity
				+ ", call_put=" + call_put + ", strike=" + strike + ", option_type=" + option_type
				+ ", contract_size_units=" + contract_size_units + ", contract_size=" + contract_size + ", multiplier="
				+ multiplier + ", currency=" + currency + ", option_ex_type_new=" + option_ex_type_new
				+ ", tick_size_var=" + tick_size_var + ", tick_value=" + tick_value + ", listing_mic=" + listing_mic
				+ ", sector=" + sector + ", subsector=" + subsector + ", aifmd=" + aifmd + ", r20=" + r20 + ", omegaid="
				+ omegaid + ", analystics_cusip=" + analystics_cusip + ", expiration_cycle=" + expiration_cycle
				+ ", settlement_type=" + settlement_type + ", contract_version=" + contract_version + ", inst_status="
				+ inst_status + ", inst_status_date=" + inst_status_date + ", underlying_cusip=" + underlying_cusip
				+ ", rolling_ticker=" + rolling_ticker + ", product_root=" + product_root + ", first_trade_date="
				+ first_trade_date + ", first_notice_date=" + first_notice_date + ", delivery_start_date="
				+ delivery_start_date + ", delivery_end_date=" + delivery_end_date + ", bid_latest=" + bid_latest
				+ ", ask_latest=" + ask_latest + ", quote_date_latest=" + quote_date_latest + ", open=" + open
				+ ", high=" + high + ", low=" + low + ", last_trade=" + last_trade + ", last_trade_date="
				+ last_trade_date + ", latest_settlement=" + latest_settlement
				+ ", latest_settlement_1d_percent_change=" + latest_settlement_1d_percent_change
				+ ", latest_settlement_1w_percent_change=" + latest_settlement_1w_percent_change
				+ ", latest_settlement_1m_percent_change=" + latest_settlement_1m_percent_change
				+ ", latest_settlement_date=" + latest_settlement_date + ", total_volume=" + total_volume
				+ ", open_interest=" + open_interest + "]";
	}

	
}
