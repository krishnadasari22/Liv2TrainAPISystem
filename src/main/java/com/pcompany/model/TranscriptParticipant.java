package com.pcompany.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement(name = "participants")
@XmlAccessorType(XmlAccessType.FIELD)
public class TranscriptParticipant {
	
	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="affiliation")
	private String affiliation;
	
	@XmlAttribute(name="affiliation_entity")
	private String affiliationEntity;
	
	@XmlAttribute(name="title")
	private String title;
	
	@XmlAttribute(name="entity")
	private String entity;
	
	@XmlValue
	private String name;
	
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
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getAffiliationEntity() {
		return affiliationEntity;
	}
	public void setAffiliationEntity(String affiliationEntity) {
		this.affiliationEntity = affiliationEntity;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "TranscriptParticipant [id=" + id + ", type=" + type + ", affiliation=" + affiliation
				+ ", affiliationEntity=" + affiliationEntity + ", title=" + title + ", entity=" + entity + ", name="
				+ name + "]";
	}
}
