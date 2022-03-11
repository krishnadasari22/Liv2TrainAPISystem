package com.pcompany.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "section")
@XmlAccessorType(XmlAccessType.FIELD)
public class TranscriptSection {
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlElement
	private List<TranscriptSpeaker> speaker;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TranscriptSpeaker> getSpeaker() {
		return speaker;
	}

	public void setSpeaker(List<TranscriptSpeaker> speaker) {
		this.speaker = speaker;
	}

	@Override
	public String toString() {
		return "TranscriptSection [name=" + name + ", \n\tspeaker=" + speaker + "]";
	}
	
}
