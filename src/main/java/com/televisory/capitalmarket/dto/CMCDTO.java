package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.dozer.Mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author vinay
 *
 */

public class CMCDTO implements Serializable{

	private static final long serialVersionUID = -6003856656417771060L;


	private Integer rowNo;
	
	private String id;

	
	private String factSetEntityId;

	private String name;

	private String properName;

	private String description;

	private String reportingCurrency;

	private String exchangeCode;

	private String exchangeName;

	private String companyTicker;

	private String tickerExchange;
	
	private String ff_industry;

	private String securityId;


	private String entityType;

	private boolean isActive;
	
	private boolean isDublicate;

	public boolean isDublicate() {
		return isDublicate;
	}

	public void setDublicate(boolean isDublicate) {
		this.isDublicate = isDublicate;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
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

	public String getReportingCurrency() {
		return reportingCurrency;
	}

	public void setReportingCurrency(String reportingCurrency) {
		this.reportingCurrency = reportingCurrency;
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

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getCompanyTicker() {
		return companyTicker;
	}

	public void setCompanyTicker(String companyTicker) {
		this.companyTicker = companyTicker;
	}

	public String getTickerExchange() {
		return tickerExchange;
	}

	public void setTickerExchange(String tickerExchange) {
		this.tickerExchange = tickerExchange;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getFf_industry() {
		return ff_industry;
	}

	public void setFf_industry(String ff_industry) {
		this.ff_industry = ff_industry;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}


}
