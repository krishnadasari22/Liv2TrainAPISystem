package com.televisory.capitalmarket.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author vinay
 *
 */
public class CompanyMetaData implements Serializable {

	private static final long serialVersionUID = 3989814493178007679L;
	private String companyId;
	private String entityId;
	private String companyName;
	private String properName;
	private String securityType;
	private String companyDescription;
	private String companyDetailedDescription;
	private String companyTicker;
	private String countryCode;
	private Integer countryId;
	private String countryName;
	private String exchangeCode;
	private String exchangeName;
	private String currency;
	
	private Double marketCap;
	private String marketCapCurrency;
	
	private Double marketCapFiling;
	private String marketCapFilingCurrency;

	private Double priceEarnings;
	private String priceEarningsCurrency;
	private String priceEarningsUnit;
	
	private Double dividendYield;
	private String dividendYieldCurrency;
	private String dividendYieldUnit;
	
	private Double bookValue;
	private String bookValueCurrency;
	private String bookValueUnit;
	
	private Double priceBookValue;
	private String priceBookValueCurrency;
	private String priceBookValueUnit;
	
	private Double cashMcap;
	private String cashMcapCurrency;
	private String cashMcapUnit;

	private Double evEbitda;
	private String evEbitdaCurrency;
	private String evEbitdaUnit;

	private Double finViability;
	private String finViabilityCurrency;
	private String finViabilityUnit;
	
	private Double priceIncome;
	private String priceIncomeCurrency;
	private String priceIncomeUnit;
	
	private Double roa;
	private String roaCurrency;
	private String roaUnit;
	
	private Double shareOutStanding;
	private Double shareFreeFloat;

	private Double sharePrice;
	private Double sharePricePercentChange;
	private Date sharePriceDate;
	private String sharePriceCurrency;
	private Double highStockPrice;
	private Double lowStockPrice;
	private Double dailyVolume;
	private Double filingCurrencySharePrice;
	
	private String ff_industry;
	
	private String ticsIndustryCode;
	private String ticsIndustryName;
	
	private String ticsSectorCode;
	private String ticsSectorName;
	
	private Boolean countryEconomyFlag;
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProperName() {
		return properName;
	}

	public void setProperName(String properName) {
		this.properName = properName;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}
	
	public String getCompanyDetailedDescription() {
		return companyDetailedDescription;
	}

	public void setCompanyDetailedDescription(String companyDetailedDescription) {
		this.companyDetailedDescription = companyDetailedDescription;
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public Double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(Double marketCap) {
		this.marketCap = marketCap;
	}
	
	public String getMarketCapCurrency() {
		return marketCapCurrency;
	}

	public void setMarketCapCurrency(String marketCapCurrency) {
		this.marketCapCurrency = marketCapCurrency;
	}

	public Double getMarketCapFiling() {
		return marketCapFiling;
	}

	public void setMarketCapFiling(Double marketCapFiling) {
		this.marketCapFiling = marketCapFiling;
	}

	public String getMarketCapFilingCurrency() {
		return marketCapFilingCurrency;
	}

	public void setMarketCapFilingCurrency(String marketCapFilingCurrency) {
		this.marketCapFilingCurrency = marketCapFilingCurrency;
	}

	public Double getPriceEarnings() {
		return priceEarnings;
	}

	public void setPriceEarnings(Double priceEarnings) {
		this.priceEarnings = priceEarnings;
	}

	public String getPriceEarningsCurrency() {
		return priceEarningsCurrency;
	}

	public void setPriceEarningsCurrency(String priceEarningsCurrency) {
		this.priceEarningsCurrency = priceEarningsCurrency;
	}

	public String getPriceEarningsUnit() {
		return priceEarningsUnit;
	}

	public void setPriceEarningsUnit(String priceEarningsUnit) {
		this.priceEarningsUnit = priceEarningsUnit;
	}

	public Double getDividendYield() {
		return dividendYield;
	}

	public void setDividendYield(Double dividendYield) {
		this.dividendYield = dividendYield;
	}

	public String getDividendYieldCurrency() {
		return dividendYieldCurrency;
	}

	public void setDividendYieldCurrency(String dividendYieldCurrency) {
		this.dividendYieldCurrency = dividendYieldCurrency;
	}

	public String getDividendYieldUnit() {
		return dividendYieldUnit;
	}

	public void setDividendYieldUnit(String dividendYieldUnit) {
		this.dividendYieldUnit = dividendYieldUnit;
	}

	public Double getBookValue() {
		return bookValue;
	}

	public void setBookValue(Double bookValue) {
		this.bookValue = bookValue;
	}

	public String getBookValueCurrency() {
		return bookValueCurrency;
	}

	public void setBookValueCurrency(String bookValueCurrency) {
		this.bookValueCurrency = bookValueCurrency;
	}

	public String getBookValueUnit() {
		return bookValueUnit;
	}

	public void setBookValueUnit(String bookValueUnit) {
		this.bookValueUnit = bookValueUnit;
	}

	public Double getPriceBookValue() {
		return priceBookValue;
	}

	public void setPriceBookValue(Double priceBookValue) {
		this.priceBookValue = priceBookValue;
	}

	public String getPriceBookValueCurrency() {
		return priceBookValueCurrency;
	}

	public void setPriceBookValueCurrency(String priceBookValueCurrency) {
		this.priceBookValueCurrency = priceBookValueCurrency;
	}

	public String getPriceBookValueUnit() {
		return priceBookValueUnit;
	}

	public void setPriceBookValueUnit(String priceBookValueUnit) {
		this.priceBookValueUnit = priceBookValueUnit;
	}
	
	public Double getCashMcap() {
		return cashMcap;
	}

	public void setCashMcap(Double cashMcap) {
		this.cashMcap = cashMcap;
	}

	public String getCashMcapCurrency() {
		return cashMcapCurrency;
	}

	public void setCashMcapCurrency(String cashMcapCurrency) {
		this.cashMcapCurrency = cashMcapCurrency;
	}

	public String getCashMcapUnit() {
		return cashMcapUnit;
	}

	public void setCashMcapUnit(String cashMcapUnit) {
		this.cashMcapUnit = cashMcapUnit;
	}

	public Double getEvEbitda() {
		return evEbitda;
	}

	public void setEvEbitda(Double evEbitda) {
		this.evEbitda = evEbitda;
	}

	public String getEvEbitdaCurrency() {
		return evEbitdaCurrency;
	}

	public void setEvEbitdaCurrency(String evEbitdaCurrency) {
		this.evEbitdaCurrency = evEbitdaCurrency;
	}

	public String getEvEbitdaUnit() {
		return evEbitdaUnit;
	}

	public void setEvEbitdaUnit(String evEbitdaUnit) {
		this.evEbitdaUnit = evEbitdaUnit;
	}

	public Double getFinViability() {
		return finViability;
	}

	public void setFinViability(Double finViability) {
		this.finViability = finViability;
	}

	public String getFinViabilityCurrency() {
		return finViabilityCurrency;
	}

	public void setFinViabilityCurrency(String finViabilityCurrency) {
		this.finViabilityCurrency = finViabilityCurrency;
	}

	public String getFinViabilityUnit() {
		return finViabilityUnit;
	}

	public void setFinViabilityUnit(String finViabilityUnit) {
		this.finViabilityUnit = finViabilityUnit;
	}

	public Double getPriceIncome() {
		return priceIncome;
	}

	public void setPriceIncome(Double priceIncome) {
		this.priceIncome = priceIncome;
	}

	public String getPriceIncomeCurrency() {
		return priceIncomeCurrency;
	}

	public void setPriceIncomeCurrency(String priceIncomeCurrency) {
		this.priceIncomeCurrency = priceIncomeCurrency;
	}

	public String getPriceIncomeUnit() {
		return priceIncomeUnit;
	}

	public void setPriceIncomeUnit(String priceIncomeUnit) {
		this.priceIncomeUnit = priceIncomeUnit;
	}

	public Double getRoa() {
		return roa;
	}

	public void setRoa(Double roa) {
		this.roa = roa;
	}

	public String getRoaCurrency() {
		return roaCurrency;
	}

	public void setRoaCurrency(String roaCurrency) {
		this.roaCurrency = roaCurrency;
	}

	public String getRoaUnit() {
		return roaUnit;
	}

	public void setRoaUnit(String roaUnit) {
		this.roaUnit = roaUnit;
	}

	public Double getShareOutStanding() {
		return shareOutStanding;
	}

	public void setShareOutStanding(Double shareOutStanding) {
		this.shareOutStanding = shareOutStanding;
	}

	public Double getShareFreeFloat() {
		return shareFreeFloat;
	}

	public void setShareFreeFloat(Double shareFreeFloat) {
		this.shareFreeFloat = shareFreeFloat;
	}

	public Double getSharePrice() {
		return sharePrice;
	}

	public void setSharePrice(Double sharePrice) {
		this.sharePrice = sharePrice;
	}
	
	public Double getSharePricePercentChange() {
		return sharePricePercentChange;
	}

	public void setSharePricePercentChange(Double sharePricePercentChange) {
		this.sharePricePercentChange = sharePricePercentChange;
	}

	public Date getSharePriceDate() {
		return sharePriceDate;
	}

	public void setSharePriceDate(Date sharePriceDate) {
		this.sharePriceDate = sharePriceDate;
	}
	
	public String getSharePriceCurrency() {
		return sharePriceCurrency;
	}

	public void setSharePriceCurrency(String sharePriceCurrency) {
		this.sharePriceCurrency = sharePriceCurrency;
	}

	public Double getHighStockPrice() {
		return highStockPrice;
	}

	public void setHighStockPrice(Double highStockPrice) {
		this.highStockPrice = highStockPrice;
	}

	public Double getLowStockPrice() {
		return lowStockPrice;
	}

	public void setLowStockPrice(Double lowStockPrice) {
		this.lowStockPrice = lowStockPrice;
	}

	public Double getDailyVolume() {
		return dailyVolume;
	}

	public void setDailyVolume(Double dailyVolume) {
		this.dailyVolume = dailyVolume;
	}
	
	public Double getFilingCurrencySharePrice() {
		return filingCurrencySharePrice;
	}

	public void setFilingCurrencySharePrice(Double filingCurrencySharePrice) {
		this.filingCurrencySharePrice = filingCurrencySharePrice;
	}

	public String getFf_industry() {
		return ff_industry;
	}

	public void setFf_industry(String ff_industry) {
		this.ff_industry = ff_industry;
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

	public String getTicsSectorCode() {
		return ticsSectorCode;
	}

	public void setTicsSectorCode(String ticsSectorCode) {
		this.ticsSectorCode = ticsSectorCode;
	}

	public String getTicsSectorName() {
		return ticsSectorName;
	}

	public void setTicsSectorName(String ticsSectorName) {
		this.ticsSectorName = ticsSectorName;
	}

	public Boolean getCountryEconomyFlag() {
		return countryEconomyFlag;
	}

	public void setCountryEconomyFlag(Boolean countryEconomyFlag) {
		this.countryEconomyFlag = countryEconomyFlag;
	}
	
	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "CompanyMetaData [companyId=" + companyId + ", entityId=" + entityId + ", companyName=" + companyName
				+ ", properName=" + properName + ", securityType=" + securityType + ", companyDescription="
				+ companyDescription + ", companyDetailedDescription=" + companyDetailedDescription + ", companyTicker="
				+ companyTicker + ", countryCode=" + countryCode + ", countryId=" + countryId + ", countryName="
				+ countryName + ", exchangeCode=" + exchangeCode + ", exchangeName=" + exchangeName + ", currency="
				+ currency + ", marketCap=" + marketCap + ", marketCapCurrency=" + marketCapCurrency
				+ ", marketCapFiling=" + marketCapFiling + ", marketCapFilingCurrency=" + marketCapFilingCurrency
				+ ", priceEarnings=" + priceEarnings + ", priceEarningsCurrency=" + priceEarningsCurrency
				+ ", priceEarningsUnit=" + priceEarningsUnit + ", dividendYield=" + dividendYield
				+ ", dividendYieldCurrency=" + dividendYieldCurrency + ", dividendYieldUnit=" + dividendYieldUnit
				+ ", bookValue=" + bookValue + ", bookValueCurrency=" + bookValueCurrency + ", bookValueUnit="
				+ bookValueUnit + ", priceBookValue=" + priceBookValue + ", priceBookValueCurrency="
				+ priceBookValueCurrency + ", priceBookValueUnit=" + priceBookValueUnit + ", cashMcap=" + cashMcap
				+ ", cashMcapCurrency=" + cashMcapCurrency + ", cashMcapUnit=" + cashMcapUnit + ", evEbitda=" + evEbitda
				+ ", evEbitdaCurrency=" + evEbitdaCurrency + ", evEbitdaUnit=" + evEbitdaUnit + ", finViability="
				+ finViability + ", finViabilityCurrency=" + finViabilityCurrency + ", finViabilityUnit="
				+ finViabilityUnit + ", priceIncome=" + priceIncome + ", priceIncomeCurrency=" + priceIncomeCurrency
				+ ", priceIncomeUnit=" + priceIncomeUnit + ", roa=" + roa + ", roaCurrency=" + roaCurrency
				+ ", roaUnit=" + roaUnit + ", shareOutStanding=" + shareOutStanding + ", shareFreeFloat="
				+ shareFreeFloat + ", sharePrice=" + sharePrice + ", sharePricePercentChange=" + sharePricePercentChange
				+ ", sharePriceDate=" + sharePriceDate + ", sharePriceCurrency=" + sharePriceCurrency
				+ ", highStockPrice=" + highStockPrice + ", lowStockPrice=" + lowStockPrice + ", dailyVolume="
				+ dailyVolume + ", filingCurrencySharePrice=" + filingCurrencySharePrice + ", ff_industry="
				+ ff_industry + ", ticsIndustryCode=" + ticsIndustryCode + ", ticsIndustryName=" + ticsIndustryName
				+ ", ticsSectorCode=" + ticsSectorCode + ", ticsSectorName=" + ticsSectorName + ", countryEconomyFlag="
				+ countryEconomyFlag + "]";
	}
}
