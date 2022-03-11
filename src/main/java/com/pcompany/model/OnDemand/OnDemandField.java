package com.pcompany.model.OnDemand;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.FIELD)
public class OnDemandField {

	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="value")
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "OnDemandField [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
		
}
