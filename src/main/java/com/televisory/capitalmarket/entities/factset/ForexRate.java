package com.televisory.capitalmarket.entities.factset;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

	@Entity
	@Table(name = "ref_v2_fx_rates_usd")
	public class ForexRate {
	
	@EmbeddedId
	ForexKey forexKey;
	
	@Column(name = "exch_rate_usd")
	private Double exchRateUsd;
	
	@Column(name = "exch_rate_per_usd")
	private Double exchRatePerUsd;

	public ForexRate() {
	
	}

	public ForexRate(ForexKey forexKey, Double exchRateUsd, Double exchRatePerUsd) {
		this.forexKey = forexKey;
		this.exchRateUsd = exchRateUsd;
		this.exchRatePerUsd = exchRatePerUsd;
	}

	public Double getExchRateUsd() {
		return exchRateUsd;
	}

	public void setExchRateUsd(Double exchRateUsd) {
		this.exchRateUsd = exchRateUsd;
	}

	public Double getExchRatePerUsd() {
		return exchRatePerUsd;
	}

	public void setExchRatePerUsd(Double exchRatePerUsd) {
		this.exchRatePerUsd = exchRatePerUsd;
	}

	public ForexKey getForexKey() {
		return forexKey;
	}

	public void setForexKey(ForexKey forexKey) {
		this.forexKey = forexKey;
	}
}
