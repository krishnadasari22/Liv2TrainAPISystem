package com.pcompany.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "meta")
@XmlAccessorType(XmlAccessType.FIELD)
public class TranscriptMeta {
	
	@XmlElement
	private String title;
	@XmlElement
	private String date;
	@XmlElement
	private TransciptCompanies companies;
	
	@XmlElementWrapper(name="participants")
    @XmlElement(name="participant")
	private List<TranscriptParticipant> participants;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public TransciptCompanies getCompanies() {
		return companies;
	}

	public void setCompanies(TransciptCompanies companies) {
		this.companies = companies;
	}

	public List<TranscriptParticipant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<TranscriptParticipant> participants) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "TranscriptMeta [title=" + title + ", date=" + date + ", companies=" + companies + ", \n\t participants="
				+ participants + "]";
	}
}
