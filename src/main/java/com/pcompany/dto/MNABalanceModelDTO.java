package com.pcompany.dto;

public class MNABalanceModelDTO {

	private Integer id;
	
	private String display_order;
	
	private String display_level;
	
	private String type;
	
	private String field_name;
	
	private String section;
	
	private String shortName;
	
	private String description;
	
	private String display_name;
	
	private Integer currencyFlag;
	
	private String unit;
	
	private Integer isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(String display_order) {
		this.display_order = display_order;
	}

	public String getDisplay_level() {
		return display_level;
	}

	public void setDisplay_level(String display_level) {
		this.display_level = display_level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public Integer getCurrencyFlag() {
		return currencyFlag;
	}

	public void setCurrencyFlag(Integer currencyFlag) {
		this.currencyFlag = currencyFlag;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "MNABalanceModel [id=" + id + ", display_order=" + display_order + ", display_level=" + display_level
				+ ", type=" + type + ", field_name=" + field_name + ", section=" + section + ", shortName=" + shortName
				+ ", description=" + description + ", display_name=" + display_name + ", currencyFlag=" + currencyFlag
				+ ", unit=" + unit + ", isActive=" + isActive + "]";
	}
	
	
}
