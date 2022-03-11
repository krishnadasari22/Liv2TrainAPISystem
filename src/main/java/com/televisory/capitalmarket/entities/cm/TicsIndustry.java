package com.televisory.capitalmarket.entities.cm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "tics_industry")
@Proxy(lazy=false)
public class TicsIndustry implements Serializable{

	private static final long serialVersionUID = 4584130738645280746L;

	@Column(name="industry_id")
	private Integer industryId;
	
	@Column(name="tics_industry_name")
	private String ticsIndustryName;
	
	@Id
	@Column(name="tics_industry_code")
	private String ticsIndustryCode;
	
	@Column(name="tics_industry_desc")
	private String ticsIndustryDesc;
	
	@Column(name="factset_industry")
	private String factsetIndustry;
	

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tics_sector_code")
	private TicsSector ticsSector;

	public Integer getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}

	public String getTicsIndustryName() {
		return ticsIndustryName;
	}

	public void setTicsIndustryName(String ticsIndustryName) {
		this.ticsIndustryName = ticsIndustryName;
	}
	
	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public String getTicsIndustryDesc() {
		return ticsIndustryDesc;
	}

	public void setTicsIndustryDesc(String ticsIndustryDesc) {
		this.ticsIndustryDesc = ticsIndustryDesc;
	}
	
	public TicsSector getTicsSector() {
		return ticsSector;
	}

	public void setTicsSector(TicsSector ticsSector) {
		this.ticsSector = ticsSector;
	}

	public String getFactsetIndustry() {
		return factsetIndustry;
	}

	public void setFactsetIndustry(String factsetIndustry) {
		this.factsetIndustry = factsetIndustry;
	}

	@Override
	public String toString() {
		return "TicsIndustry [industryId=" + industryId + ", ticsIndustryName=" + ticsIndustryName
				+ ", ticsIndustryCode=" + ticsIndustryCode + ", ticsIndustryDesc=" + ticsIndustryDesc
				+ ", factsetIndustry=" + factsetIndustry + ", ticsSector=" + ticsSector + "]";
	}

}
