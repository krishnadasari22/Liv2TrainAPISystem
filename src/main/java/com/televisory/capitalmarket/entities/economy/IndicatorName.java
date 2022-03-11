package com.televisory.capitalmarket.entities.economy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "indicator_name")
public class IndicatorName {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "indicator_name_id", nullable = false, unique = true)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="indicator_type_id")
	private IndicatorType indicatorsType;
	
	@Column(name="indicator_name")
	private String indicatorsName;
	
	@Column(name="short_name")
	private String shortName;
	
	@Column(name="indicator_name_code")
	private String indicatorsNameCode;
	
	@Column(name="display_name")
	private String displayName;
	
	@Column(name="available_periods")
	private String availablePeriods;
	
	@Column(name="data_type")
	private String dataType;
	
	@Column(name="chart_flag")
	private int chartFlag;
	
	@Column(name = "is_active")
	private int isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_at")
	private Date lastModifiedAt;

	@Column(name = "modified_by")
	private String lastModifiedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IndicatorType getIndicatorsType() {
		return indicatorsType;
	}

	public void setIndicatorsType(IndicatorType indicatorsType) {
		this.indicatorsType = indicatorsType;
	}

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

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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
	
	public int getChartFlag() {
		return chartFlag;
	}

	public void setChartFlag(int chartFlag) {
		this.chartFlag = chartFlag;
	}

	@Override
	public String toString() {
		return "IndicatorName [id=" + id + ", indicatorsType=" + indicatorsType + ", indicatorsName=" + indicatorsName
				+ ", shortName=" + shortName + ", indicatorsNameCode=" + indicatorsNameCode + ", displayName="
				+ displayName + ", availablePeriods=" + availablePeriods + ", dataType=" + dataType + ", chartFlag="
				+ chartFlag + ", isActive=" + isActive + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", lastModifiedAt=" + lastModifiedAt + ", lastModifiedBy=" + lastModifiedBy + "]";
	}
}
