package com.televisory.capitalmarket.model;

public class ReportAnnextureModel {

	String metric;
	String subMetric;
	String slot1;
	String slot2;
	String slot3;
	String slot4;
	String slot5;
	String slot6;
	String currency;
	String unit;
	String section;



	public String getSlot6() {
		return slot6;
	}
	public void setSlot6(String slot6) {
		this.slot6 = slot6;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public String getSubMetric() {
		return subMetric;
	}
	public void setSubMetric(String subMetric) {
		this.subMetric = subMetric;
	}
	public String getSlot1() {
		return slot1;
	}
	public void setSlot1(String slot1) {
		this.slot1 = slot1;
	}
	public String getSlot2() {
		return slot2;
	}
	public void setSlot2(String slot2) {
		this.slot2 = slot2;
	}
	public String getSlot3() {
		return slot3;
	}
	public void setSlot3(String slot3) {
		this.slot3 = slot3;
	}
	public String getSlot4() {
		return slot4;
	}
	public void setSlot4(String slot4) {
		this.slot4 = slot4;
	}
	public String getSlot5() {
		return slot5;
	}
	public void setSlot5(String slot5) {
		this.slot5 = slot5;
	}
	@Override
	public String toString() {
		return "ReportAnnextureModel [metric=" + metric + ", subMetric="
				+ subMetric + ", slot1=" + slot1 + ", slot2=" + slot2
				+ ", slot3=" + slot3 + ", slot4=" + slot4 + ", slot5=" + slot5
				+ ", slot6=" + slot6 + ", currency=" + currency + ", unit="
				+ unit + ", section=" + section + "]";
	}
}
