package com.televisory.bond.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="etd_balance_model")
public class DerivativeBalanceModel {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "field_name")
	private String fieldName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "csv_field_name")
	private String csvFieldName;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "currency_flag")
	private Integer currencyFlag;
	
	@Column(name = "future_section")
	private String futureSection;
	
	@Column(name = "future_order")
	private String futureOrder;
	
	@Column(name = "option_section")
	private String optionSection;
	
	@Column(name = "option_order")
	private String optionOrder;
	
	@Column(name = "display_name")
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
		return "DerivativeBalanceModel [id=" + id + ", fieldName=" + fieldName + ", description=" + description
				+ ", csvFieldName=" + csvFieldName + ", unit=" + unit + ", currencyFlag=" + currencyFlag
				+ ", futureSection=" + futureSection + ", futureOrder=" + futureOrder + ", optionSection="
				+ optionSection + ", optionOrder=" + optionOrder + ", displayName=" + displayName + "]";
	}
}
