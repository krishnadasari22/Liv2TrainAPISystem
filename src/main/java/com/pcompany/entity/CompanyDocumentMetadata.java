package com.pcompany.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_document_metadata")
public class CompanyDocumentMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="comapny_documents_metadata_id")
	private Integer comapnyDocumentsMetadataId;
	
	@Column(name = "ticker_country_code")
	private String tickerCountryCode;
	
	@Column(name = "period")
	private Date period;
	
	@Column(name = "key")
	private String key;
	
	@Column(name = "req_sym")
	private String reqSym;

	@Column(name = "headline")
	private String headline;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "search_ids")
	private String searchIds;
	
	@Column(name = "all_ids")
	private String allIds;
	
	@Column(name = "story_date")
	private Date storyDate;
	
	@Column(name = "story_time")
	private Date storyTime;
	
	@Column(name = "count")
	private Integer count;
	
	@Column(name = "categories")
	private String categories;
	
	@Column(name = "link1")
	private String link1;
	
	@Column(name = "industries")
	private String industries;
	
	@Column(name = "sa_categories")
	private String saCategories;
	
	@Column(name = "security_info")
	private String securityInfo;
	
	@Column(name = "transcript_expected")
	private String transcriptExpected;

	@Column(name = "report_expected")
	private String reportExpected;
	
	@Column(name = "filing_size")
	private String filingSize;
	
	@Column(name = "form_type")
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
}
