package com.televisory.bond.dto;

public class DerivativeBalanceModelDTO {

	private Integer id;
	private String fieldName;
	private String description;
	private String csvFieldName;
	private String unit;
	private Integer currencyFlag;
	private String futureOrder;
	private String futureSection;
	private String optionSection;
	private String optionOrder;
	private String displayName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCsvFieldName() {
		return csvFieldName;
	}

	public void setCsvFieldName(String csvFieldName) {
		this.csvFieldName = csvFieldName;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getCurrencyFlag() {
		return currencyFlag;
	}

	public void setCurrencyFlag(Integer currencyFlag) {
		this.currencyFlag = currencyFlag;
	}

	public String getFutureSection() {
		return futureSection;
	}

	public void setFutureSection(String futureSection) {
		this.futureSection = futureSection;
	}

	public String getFutureOrder() {
		return futureOrder;
	}

	public void setFutureOrder(String futureOrder) {
		this.futureOrder = futureOrder;
	}

	public String getOptionSection() {
		return optionSection;
	}

	public void setOptionSection(String optionSection) {
		this.optionSection = optionSection;
	}

	public String getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(String optionOrder) {
		this.optionOrder = optionOrder;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "DerivativeBalanceModelDTO [id=" + id + ", fieldName=" + fieldName + ", description=" + description
				+ ", csvFieldName=" + csvFieldName + ", unit=" + unit + ", currencyFlag=" + currencyFlag
				+ ", futureOrder=" + futureOrder + ", futureSection=" + futureSection + ", optionSection="
				+ optionSection + ", optionOrder=" + optionOrder + ", displayName=" + displayName + "]";
	}
}
