package com.televisory.bond.entity;

//@Entity
//@Table(name="etd_balance_model")
public class DerivativeHistoricalTableInfo {
	
	private String identifier;
	
	private Double lastTradedPrice;
	
	private Double open;
	
	private Double high;
	
	private Double low;
	
	private Double settlementPrice;
	
	private Double volume;
	
	private Double turnOver;
	
	private Double openInterest;
	
	private Double changeInOI;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Double getLastTradedPrice() {
		return lastTradedPrice;
	}

	public void setLastTradedPrice(Double lastTradedPrice) {
		this.lastTradedPrice = lastTradedPrice;
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

	public Double getSettlementPrice() {
		return settlementPrice;
	}

	public void setSettlementPrice(Double settlementPrice) {
		this.settlementPrice = settlementPrice;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getTurnOver() {
		return turnOver;
	}

	public void setTurnOver(Double turnOver) {
		this.turnOver = turnOver;
	}

	public Double getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(Double openInterest) {
		this.openInterest = openInterest;
	}

	public Double getChangeInOI() {
		return changeInOI;
	}

	public void setChangeInOI(Double changeInOI) {
		this.changeInOI = changeInOI;
	}

	@Override
	public String toString() {
		return "DerivativeHistoricalTableInfo [identifier=" + identifier + ", lastTradedPrice=" + lastTradedPrice
				+ ", open=" + open + ", high=" + high + ", low=" + low + ", settlementPrice=" + settlementPrice
				+ ", volume=" + volume + ", turnOver=" + turnOver + ", openInterest=" + openInterest + ", changeInOI="
				+ changeInOI + "]";
	}
}
