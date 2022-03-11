package com.privatecompany.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_list")
public class AdvancedSearchCompany {

	@Column(name = "id")
	private Integer rowNo;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_id")
	private String id;
	
	@Column(name = "factset_entity_id")
	private String factSetEntityId;

	@Column(name = "name")
	private String name;

	@Column(name = "proper_name")
	private String properName;

	@Column(name = "description")
	private String description;

	@Column(name = "company_ticker")
	private String companyTicker;

	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "domicile_country_code", referencedColumnName = "country_iso_code_3")
	private CMCountry country;*/
	
	@Column(name = "domicile_country_code")
	private String countryCode;

	/*@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tics_industry_code")
	private TicsIndustry ticsIndustry;*/
	
	
	@Column(name = "tics_industry_code")
	private String ticsIndustryCode;
	
	@Column(name = "industry_name")
	private String ticsIndustryName;
	
	@Column(name = "sector_code")
	private String sectorCode;
	
	@Column(name = "sector_name")
	private String sectorName;
	
	@Column(name = "entity_type")
	private String entityType;
	
	/*@Column(name = "closing_price")
	private Double closingPrice;
	
	@Column(name = "daily_change")
	private Double dailyChange;*/
	
	@Column(name = "revenue")
	private Double revenue;
	
	@Column(name = "debt_equity")
	private Double debtEquity;
	
	@Column(name = "pat")
	private Double pat;
	

	@Column(name = "is_active")
	private Integer isActive;

	/*@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "exchange_code")
	private CMExchangeList exchange;*/
	
	@Column(name = "exchange_name")
	private String exchangeName;
	
	@Column(name = "exchange_code")
	private String exchangeCode;
	
	@Column(name = "country")
	private String countryName;

	@Column(name = "factset_industry")
	private String ff_industry;
	
	@Column(name = "reporting_currency")
	private String reportingCurrency;
	
	@Column(name = "net_worth")
	private Double netWorth;
	
	@Column(name = "latest_ann_update")
	private String latestAnnUpdate;
	
	@Column(name="security_id")
	private String securityId;

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFactSetEntityId() {
		return factSetEntityId;
	}

	public void setFactSetEntityId(String factSetEntityId) {
		this.factSetEntityId = factSetEntityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperName() {
		return properName;
	}

	public void setProperName(String properName) {
		this.properName = properName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompanyTicker() {
		return companyTicker;
	}

	public void setCompanyTicker(String companyTicker) {
		this.companyTicker = companyTicker;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public String getTicsIndustryName() {
		return ticsIndustryName;
	}

	public void setTicsIndustryName(String ticsIndustryName) {
		this.ticsIndustryName = ticsIndustryName;
	}

	public String getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Double getDebtEquity() {
		return debtEquity;
	}

	public void setDebtEquity(Double debtEquity) {
		this.debtEquity = debtEquity;
	}

	public Double getPat() {
		return pat;
	}

	public void setPat(Double pat) {
		this.pat = pat;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getFf_industry() {
		return ff_industry;
	}

	public void setFf_industry(String ff_industry) {
		this.ff_industry = ff_industry;
	}

	public String getReportingCurrency() {
		return reportingCurrency;
	}

	public void setReportingCurrency(String reportingCurrency) {
		this.reportingCurrency = reportingCurrency;
	}

	public Double getNetWorth() {
		return netWorth;
	}

	public void setNetWorth(Double netWorth) {
		this.netWorth = netWorth;
	}

	public String getLatestAnnUpdate() {
		return latestAnnUpdate;
	}

	public void setLatestAnnUpdate(String latestAnnUpdate) {
		this.latestAnnUpdate = latestAnnUpdate;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	@Override
	public String toString() {
		return "AdvancedSearchCompany [rowNo=" + rowNo + ", id=" + id
				+ ", factSetEntityId=" + factSetEntityId + ", name=" + name
				+ ", properName=" + properName + ", description=" + description
				+ ", companyTicker=" + companyTicker + ", countryCode="
				+ countryCode + ", ticsIndustryCode=" + ticsIndustryCode
				+ ", ticsIndustryName=" + ticsIndustryName + ", sectorCode="
				+ sectorCode + ", sectorName=" + sectorName + ", entityType="
				+ entityType + ", revenue=" + revenue + ", debtEquity="
				+ debtEquity + ", pat=" + pat + ", isActive=" + isActive
				+ ", exchangeName=" + exchangeName + ", exchangeCode="
				+ exchangeCode + ", countryName=" + countryName
				+ ", ff_industry=" + ff_industry + ", reportingCurrency="
				+ reportingCurrency + ", netWorth=" + netWorth
				+ ", latestAnnUpdate=" + latestAnnUpdate + ", securityId="
				+ securityId + "]";
	}
	
}
