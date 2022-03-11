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
public class GlobalSearchIndustry implements Serializable{
	
	private static final long serialVersionUID = 4584130738645280746L;

	@Id
	@Column(name="gs_industry_id")
	private Integer globalSearchIndustryId;
	
	@Column(name="industry_id")
	private Integer industryId;
	
	@Column(name="tics_industry_name")
	private String ticsIndustryName;
	
	
	@Column(name="tics_industry_code")
	private String ticsIndustryCode;
	
	@Column(name="tics_industry_desc")
	private String ticsIndustryDesc;
	
	@Column(name="factset_industry")
	private String factsetIndustry;
	

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tics_sector_code")
	private TicsSector ticsSector;
	
	@Column(name="domicile_country_code")
	private String domicileCountryCode;
	
	@Column(name="country_name")
	private String countryName;
	
	@Column(name="country_id")
	private Integer countryId;

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

	public String getFactsetIndustry() {
		return factsetIndustry;
	}

	public void setFactsetIndustry(String factsetIndustry) {
		this.factsetIndustry = factsetIndustry;
	}

	public TicsSector getTicsSector() {
		return ticsSector;
	}

	public void setTicsSector(TicsSector ticsSector) {
		this.ticsSector = ticsSector;
	}

	public String getDomicileCountryCode() {
		return domicileCountryCode;
	}

	public void setDomicileCountryCode(String domicileCountryCode) {
		this.domicileCountryCode = domicileCountryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "GlobalSearchIndustry [globalSearchIndustryId="
				+ globalSearchIndustryId + ", industryId=" + industryId
				+ ", ticsIndustryName=" + ticsIndustryName
				+ ", ticsIndustryCode=" + ticsIndustryCode
				+ ", ticsIndustryDesc=" + ticsIndustryDesc
				+ ", factsetIndustry=" + factsetIndustry + ", ticsSector="
				+ ticsSector + ", domicileCountryCode=" + domicileCountryCode
				+ ", countryName=" + countryName + ", countryId=" + countryId
				+ "]";
	}
}
