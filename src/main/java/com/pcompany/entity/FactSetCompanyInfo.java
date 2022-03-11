package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_info")
public class FactSetCompanyInfo {

	@Id
	@Column(name="company_info_id")
	private Integer companyInfoId;
	
	@Column(name="company_id")
	private String companyId;
	
	@Column(name="ticker_and_domocile_code")
	private String tickerAndDomicileCode;
	
	@Column(name="xml_flag")
	private Integer xmlFlag;
	
	@Column(name="xml_flag_date")
	private Date xmlFlagDate;

	public Integer getCompanyInfoId() {
		return companyInfoId;
	}

	public void setCompanyInfoId(Integer companyInfoId) {
		this.companyInfoId = companyInfoId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTickerAndDomicileCode() {
		return tickerAndDomicileCode;
	}

	public void setTickerAndDomicileCode(String tickerAndDomicileCode) {
		this.tickerAndDomicileCode = tickerAndDomicileCode;
	}

	public Integer getXmlFlag() {
		return xmlFlag;
	}

	public void setXmlFlag(Integer xmlFlag) {
		this.xmlFlag = xmlFlag;
	}

	public Date getXmlFlagDate() {
		return xmlFlagDate;
	}

	public void setXmlFlagDate(Date xmlFlagDate) {
		this.xmlFlagDate = xmlFlagDate;
	}

	@Override
	public String toString() {
		return "FactSetCompanyInfo [companyInfoId=" + companyInfoId + ", companyId=" + companyId
				+ ", tickerAndDomicileCode=" + tickerAndDomicileCode + ", xmlFlag=" + xmlFlag + ", xmlFlagDate="
				+ xmlFlagDate + "]";
	}

}
