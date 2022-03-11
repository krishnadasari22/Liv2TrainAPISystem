package com.pcompany.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class TranscriptBody {
	
	@XmlElement
	private List<TranscriptSection> section;

	public List<TranscriptSection> getSection() {
		return section;
	}

	public void setSection(List<TranscriptSection> section) {
		this.section = section;
	}

	@Override
	public String toString() {
		return "TranscriptBody [section=" + section + "]";
	}
	
}
