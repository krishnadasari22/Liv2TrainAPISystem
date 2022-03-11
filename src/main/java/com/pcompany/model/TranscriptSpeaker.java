package com.pcompany.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "speaker")
@XmlAccessorType(XmlAccessType.FIELD)
public class TranscriptSpeaker {
	
	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="type")
	private String type;
	
	@XmlElementWrapper(name="plist")
    @XmlElement(name="p")
	private List<String> plist;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getPlist() {
		return plist;
	}

	public void setPlist(List<String> plist) {
		this.plist = plist;
	}

	@Override
	public String toString() {
		return "TranscriptSpeaker [id=" + id + ", type=" + type + ",\n plist=" + plist + "]";
	}
	
}
