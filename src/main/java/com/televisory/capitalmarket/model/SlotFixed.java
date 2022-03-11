package com.televisory.capitalmarket.model;

public class SlotFixed {

	private String[] heading;
	private Double[] data; // data[0] will be null 
	private String itemName;
	private String[] unit;
	private String[] currency;
	
	public String[] getHeading() {
		return heading;
	}
	public void setHeading(String[] heading) {
		this.heading = heading;
	}
	public Double[] getData() {
		return data;
	}
	public void setData(Double[] data) {
		this.data = data;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String[] getUnit() {
		return unit;
	}
	public void setUnit(String[] unit) {
		this.unit = unit;
	}
	public String[] getCurrency() {
		return currency;
	}
	public void setCurrency(String[] currency) {
		this.currency = currency;
	}


}
