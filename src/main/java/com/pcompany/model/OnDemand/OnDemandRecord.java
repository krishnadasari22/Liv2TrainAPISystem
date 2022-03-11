package com.pcompany.model.OnDemand;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Record")
@XmlAccessorType(XmlAccessType.FIELD)
public class OnDemandRecord {
	
	@XmlAttribute(name="key")
	String key;
	
	@XmlAttribute(name="req_sym")
	String reqSym;
	
	@XmlElementWrapper(name="Fields")
	@XmlElement(name="Field")
	private List<OnDemandField> field;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getReqSym() {
		return reqSym;
	}

	public void setReqSym(String reqSym) {
		this.reqSym = reqSym;
	}

	public List<OnDemandField> getField() {
		return field;
	}

	public void setField(List<OnDemandField> field) {
		this.field = field;
	}

	
	@Override
	public String toString() {
		return "OnDemandRecord [key=" + key + ", reqSym=" + reqSym + ", field=" + field + "]";
	}

}
