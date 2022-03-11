package com.pcompany.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EventsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer eventId;
	private String 	eventType;
	@JsonFormat(pattern="hh:mm a dd/MM/yyyy")
	private Date eventDatetimeUtc;
	private String title;
	private String urlPr;
	private Long reportId;
	private String transcriptCompleted;
	private String webcastLive;
	private String webcastReplay;
	private Long slideId;
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
		return "EventsDTO [eventId=" + eventId + ", eventType=" + eventType
				+ ", eventDatetimeUtc=" + eventDatetimeUtc + ", title=" + title
				+ ", urlPr=" + urlPr + ", reportId=" + reportId
				+ ", transcriptCompleted=" + transcriptCompleted
				+ ", webcastLive=" + webcastLive + ", webcastReplay="
				+ webcastReplay + ", slideId=" + slideId + ", webcastSlides="
				+ webcastSlides + "]";
	}

	
}
