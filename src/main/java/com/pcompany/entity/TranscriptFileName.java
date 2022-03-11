package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "evt_v1_ce_reports")
public class TranscriptFileName {
	
	@Id
	@Column(name="report_id")
	private String reportId;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="event_type")
	private String eventType;
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	@Override
	public String toString() {
		return "TranscriptFileName [reportId=" + reportId + ", fileName=" + fileName + ", eventType=" + eventType + "]";
	}
	
}
