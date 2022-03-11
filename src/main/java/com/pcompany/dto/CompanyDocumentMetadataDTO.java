package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={"link1"})
public class CompanyDocumentMetadataDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer comapnyDocumentsMetadataId;
	private String tickerCountryCode;
	private Date period;
	private String key;
	private String reqSym;
	private String headline;
	private String source;
	private String searchIds;
	private String allIds;
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date storyDate;
	@JsonFormat(pattern="hh:mm a")
	private Date storyTime;
	private Integer count;
	private String categories;
	private String link1;
	private String industries;
	private String saCategories;
	private String securityInfo;
	private String transcriptExpected;
	private String reportExpected;
	private String filingSize;
	private String formType;
	public Integer getComapnyDocumentsMetadataId() {
		return comapnyDocumentsMetadataId;
	}
	public void setComapnyDocumentsMetadataId(Integer comapnyDocumentsMetadataId) {
		this.comapnyDocumentsMetadataId = comapnyDocumentsMetadataId;
	}
	public String getTickerCountryCode() {
		return tickerCountryCode;
	}
	public void setTickerCountryCode(String tickerCountryCode) {
		this.tickerCountryCode = tickerCountryCode;
	}
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getReqSym() {
		return reqSym;
	}
	public void setReqSym(String reqSym) {
		this.reqSym = reqSym;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSearchIds() {
		return searchIds;
	}
	public void setSearchIds(String searchIds) {
		this.searchIds = searchIds;
	}
	public String getAllIds() {
		return allIds;
	}
	public void setAllIds(String allIds) {
		this.allIds = allIds;
	}
	public Date getStoryDate() {
		return storyDate;
	}
	public void setStoryDate(Date storyDate) {
		this.storyDate = storyDate;
	}
	public Date getStoryTime() {
		return storyTime;
	}
	public void setStoryTime(Date storyTime) {
		this.storyTime = storyTime;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getLink1() {
		return link1;
	}
	public void setLink1(String link1) {
		this.link1 = link1;
	}
	public String getIndustries() {
		return industries;
	}
	public void setIndustries(String industries) {
		this.industries = industries;
	}
	public String getSaCategories() {
		return saCategories;
	}
	public void setSaCategories(String saCategories) {
		this.saCategories = saCategories;
	}
	public String getSecurityInfo() {
		return securityInfo;
	}
	public void setSecurityInfo(String securityInfo) {
		this.securityInfo = securityInfo;
	}
	public String getTranscriptExpected() {
		return transcriptExpected;
	}
	public void setTranscriptExpected(String transcriptExpected) {
		this.transcriptExpected = transcriptExpected;
	}
	public String getReportExpected() {
		return reportExpected;
	}
	public void setReportExpected(String reportExpected) {
		this.reportExpected = reportExpected;
	}
	public String getFilingSize() {
		return filingSize;
	}
	public void setFilingSize(String filingSize) {
		this.filingSize = filingSize;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	@Override
	public String toString() {
		return "CompanyDocumentMetadataDTO [comapnyDocumentsMetadataId="
				+ comapnyDocumentsMetadataId + ", tickerCountryCode="
				+ tickerCountryCode + ", period=" + period + ", key=" + key
				+ ", reqSym=" + reqSym + ", headline=" + headline + ", source="
				+ source + ", searchIds=" + searchIds + ", allIds=" + allIds
				+ ", storyDate=" + storyDate + ", storyTime=" + storyTime
				+ ", count=" + count + ", categories=" + categories
				+ ", link1=" + link1 + ", industries=" + industries
				+ ", saCategories=" + saCategories + ", securityInfo="
				+ securityInfo + ", transcriptExpected=" + transcriptExpected
				+ ", reportExpected=" + reportExpected + ", filingSize="
				+ filingSize + ", formType=" + formType + "]";
	}
	
}
