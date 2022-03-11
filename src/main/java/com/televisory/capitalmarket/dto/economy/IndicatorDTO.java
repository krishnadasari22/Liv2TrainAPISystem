package com.televisory.capitalmarket.dto.economy;

import java.io.Serializable;

import org.dozer.Mapping;

public class IndicatorDTO implements Serializable {
	
	private static final long serialVersionUID = -4064987140388309693L;

	@Mapping("id")
	private int indicatorId;
	
	@Mapping("indicatorsType.id")
	private int indicatorTypeId;
	
	@Mapping("indicatorsName")
	private String indicatorsName;
	
	@Mapping("shortName")
	private String shortName;
	
	@Mapping("indicatorsNameCode")
	private String indicatorsNameCode;
	
	@Mapping("displayName")
	private String displayName;
	
	@Mapping("availablePeriods")
	private String availablePeriods;
	
	@Mapping("dataType")
	private String dataType;
	
	public String getIndicatorsName() {
		return indicatorsName;
	}

	public void setIndicatorsName(String indicatorsName) {
		this.indicatorsName = indicatorsName;
	}

	public String getIndicatorsNameCode() {
		return indicatorsNameCode;
	}

	public void setIndicatorsNameCode(String indicatorsNameCode) {
		this.indicatorsNameCode = indicatorsNameCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAvailablePeriods() {
		return availablePeriods;
	}

	public void setAvailablePeriods(String availablePeriods) {
		this.availablePeriods = availablePeriods;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}

	public int getIndicatorTypeId() {
		return indicatorTypeId;
	}

	public void setIndicatorTypeId(int indicatorTypeId) {
		this.indicatorTypeId = indicatorTypeId;
	}

	@Override
	public String toString() {
		return "IndicatorNameDTO [indicatorId=" + indicatorId + ", indicatorTypeId=" + indicatorTypeId
				+ ", indicatorsName=" + indicatorsName + ", shortName=" + shortName + ", indicatorsNameCode="
				+ indicatorsNameCode + ", displayName=" + displayName + ", availablePeriods=" + availablePeriods
				+ ", dataType=" + dataType + "]";
	}
}
