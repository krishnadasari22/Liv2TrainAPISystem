package com.pcompany.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "evt_v1_ce_events")
public class Events implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="event_id")
	private Integer eventId;
	
	@Column(name = "event_type")
	private String 	eventType;
	
	@Column(name = "event_datetime_utc")
	private Date eventDatetimeUtc;
	
	/*@Column(name = "market_time")
	private String marketTime;*/
	
	@Column(name = "title")
	private String title;

	/*@Column(name = "fiscal_period")
	private String fiscalPeriod;

	@Column(name = "fiscal_year	")
	private Double fiscalYear	;

	@Column(name = "fiscal_year_norm")
	private Double fiscalYearNorm;

	@Column(name = "projected")
	private Integer projected;*/
	
	/*@Column(name = "event_group_id")
	private Integer eventGroupId;*/
	
	@Column(name = "url_pr")
	private String urlPr;

	@Column(name = "report_id")
	private Long reportId;
	
	@Column(name = "transcript_completed")
	private String transcriptCompleted;
	
	@Column(name = "webcast_live")
	private String webcastLive;
	
	@Column(name = "webcast_replay")
	private String webcastReplay;
	
	@Column(name = "slide_id")
	private Long slideId;
	
	@Column(name = "webcast_slides")
	private String webcastSlides;

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Date getEventDatetimeUtc() {
		return eventDatetimeUtc;
	}

	public void setEventDatetimeUtc(Date eventDatetimeUtc) {
		this.eventDatetimeUtc = eventDatetimeUtc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlPr() {
		return urlPr;
	}

	public void setUrlPr(String urlPr) {
		this.urlPr = urlPr;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getTranscriptCompleted() {
		return transcriptCompleted;
	}

	public void setTranscriptCompleted(String transcriptCompleted) {
		this.transcriptCompleted = transcriptCompleted;
	}

	public String getWebcastLive() {
		return webcastLive;
	}

	public void setWebcastLive(String webcastLive) {
		this.webcastLive = webcastLive;
	}

	public String getWebcastReplay() {
		return webcastReplay;
	}

	public void setWebcastReplay(String webcastReplay) {
		this.webcastReplay = webcastReplay;
	}

	public Long getSlideId() {
		return slideId;
	}

	public void setSlideId(Long slideId) {
		this.slideId = slideId;
	}

	public String getWebcastSlides() {
		return webcastSlides;
	}

	public void setWebcastSlides(String webcastSlides) {
		this.webcastSlides = webcastSlides;
	}

	@Override
	public String toString() {
		return "Events [eventId=" + eventId + ", eventType=" + eventType
				+ ", eventDatetimeUtc=" + eventDatetimeUtc + ", title=" + title
				+ ", urlPr=" + urlPr + ", reportId=" + reportId
				+ ", transcriptCompleted=" + transcriptCompleted
				+ ", webcastLive=" + webcastLive + ", webcastReplay="
				+ webcastReplay + ", slideId=" + slideId + ", webcastSlides="
				+ webcastSlides + "]";
	}
	
	

}
