package com.pcompany.dto;

public class TranscriptFileNameDTO {

	private String reportId;
	private String fileName;
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
		return "TranscriptFileNameDTO [reportId=" + reportId + ", fileName=" + fileName + ", eventType=" + eventType + "]";
	}
	
}
