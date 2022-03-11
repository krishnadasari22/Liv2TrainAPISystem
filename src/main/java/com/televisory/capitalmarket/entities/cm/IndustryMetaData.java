package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name= "industry_data_af")
public class IndustryMetaData {
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "tics_sector_code")
	private String ticsSectorCode;
	
	@Column(name = "company_id")
	private String CompanyId;
	
	@Column(name = "domicile_country_code")
	private String CountryCode;
	
	@Column(name = "date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date period;
	
	@Column(name = "period_type", insertable = false)
	private String periodType;
	
	@Column(name = "company_count")
	private Integer CompanyCount;
	
	@Column(name = "country_coverage")
	private Integer CountryCoverage;
	
	@Column(name = "fx", insertable = false)
	private Double fx;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTicsSectorCode() {
		return ticsSectorCode;
	}

	public void setTicsSectorCode(String ticsSectorCode) {
		this.ticsSectorCode = ticsSectorCode;
	}

	public String getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	public String getCountryCode() {
		return CountryCode;
	}

	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Integer getCompanyCount() {
		return CompanyCount;
	}

	public void setCompanyCount(Integer companyCount) {
		CompanyCount = companyCount;
	}

	public Integer getCountryCoverage() {
		return CountryCoverage;
	}

	public void setCountryCoverage(Integer countryCoverage) {
		CountryCoverage = countryCoverage;
	}

	public Double getFx() {
		return fx;
	}

	public void setFx(Double fx) {
		this.fx = fx;
	}

	@Override
	public String toString() {
		return "IndustryMetaData [id=" + id + ", ticsSectorCode=" + ticsSectorCode + ", CompanyId=" + CompanyId
				+ ", CountryCode=" + CountryCode + ", period=" + period + ", periodType=" + periodType
				+ ", CompanyCount=" + CompanyCount + ", CountryCoverage=" + CountryCoverage + ", fx=" + fx + "]";
	}
	
	
}
