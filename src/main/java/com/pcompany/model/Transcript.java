package com.pcompany.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="transcript")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transcript {
	
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="product")
	private String product;
	
	@XmlElement
	private TranscriptMeta meta;
	@XmlElement
	private TranscriptBody body;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	public TranscriptMeta getMeta() {
		return meta;
	}
	public void setMeta(TranscriptMeta meta) {
		this.meta = meta;
	}
	public TranscriptBody getBody() {
		return body;
	}
	public void setBody(TranscriptBody body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Transcript [id=" + id + ", product=" + product + ",\n\n meta=" + meta + ",\n\n body=" + body + "]";
	}
	
	
		
}
