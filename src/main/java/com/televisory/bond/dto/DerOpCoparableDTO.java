package com.televisory.bond.dto;


import java.sql.Date;

public class DerOpCoparableDTO {

	private String call_symbol;

	private Date call_expiry_date;

	private Double call_latest_settlement;
	
	private Double call_last_trade;
	
	private Double put_last_trade;

	private Double call_total_volume;

	private Double call_open_interest;

	private Double strike;

	private Double put_latest_settlement;

	private Double put_total_volume;

	private Double put_open_interest;

	private Date put_expiry_date;

	private String put_symbol;

	public String getCall_symbol() {
		return call_symbol;
	}

	public void setCall_symbol(String call_symbol) {
		this.call_symbol = call_symbol;
	}

	public Date getCall_expiry_date() {
		return call_expiry_date;
	}

	public void setCall_expiry_date(Date call_expiry_date) {
		this.call_expiry_date = call_expiry_date;
	}

	public Double getCall_latest_settlement() {
		return call_latest_settlement;
	}

	public void setCall_latest_settlement(Double call_latest_settlement) {
		this.call_latest_settlement = call_latest_settlement;
	}

	public Double getCall_total_volume() {
		return call_total_volume;
	}

	public void setCall_total_volume(Double call_total_volume) {
		this.call_total_volume = call_total_volume;
	}

	public Double getCall_open_interest() {
		return call_open_interest;
	}

	public void setCall_open_interest(Double call_open_interest) {
		this.call_open_interest = call_open_interest;
	}

	public Double getStrike() {
		return strike;
	}

	public void setStrike(Double strike) {
		this.strike = strike;
	}

	public Double getPut_latest_settlement() {
		return put_latest_settlement;
	}

	public void setPut_latest_settlement(Double put_latest_settlement) {
		this.put_latest_settlement = put_latest_settlement;
	}

	public Double getPut_total_volume() {
		return put_total_volume;
	}

	public void setPut_total_volume(Double put_total_volume) {
		this.put_total_volume = put_total_volume;
	}

	public Double getPut_open_interest() {
		return put_open_interest;
	}

	public void setPut_open_interest(Double put_open_interest) {
		this.put_open_interest = put_open_interest;
	}

	public Date getPut_expiry_date() {
		return put_expiry_date;
	}

	public void setPut_expiry_date(Date put_expiry_date) {
		this.put_expiry_date = put_expiry_date;
	}

	public String getPut_symbol() {
		return put_symbol;
	}

	public void setPut_symbol(String put_symbol) {
		this.put_symbol = put_symbol;
	}
	
	public Double getCall_last_trade() {
		return call_last_trade;
	}

	public void setCall_last_trade(Double call_last_trade) {
		this.call_last_trade = call_last_trade;
	}

	public Double getPut_last_trade() {
		return put_last_trade;
	}

	public void setPut_last_trade(Double put_last_trade) {
		this.put_last_trade = put_last_trade;
	}

	@Override
	public String toString() {
		return "DerOpCoparableDTO [call_symbol=" + call_symbol + ", call_expiry_date=" + call_expiry_date
				+ ", call_latest_settlement=" + call_latest_settlement + ", call_last_trade=" + call_last_trade
				+ ", put_last_trade=" + put_last_trade + ", call_total_volume=" + call_total_volume
				+ ", call_open_interest=" + call_open_interest + ", strike=" + strike + ", put_latest_settlement="
				+ put_latest_settlement + ", put_total_volume=" + put_total_volume + ", put_open_interest="
				+ put_open_interest + ", put_expiry_date=" + put_expiry_date + ", put_symbol=" + put_symbol + "]";
	}
	
}
