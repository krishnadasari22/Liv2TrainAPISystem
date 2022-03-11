package com.pcompany.model.OnDemand;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class OnDemandResponse {
	
	@XmlElement
	private List<OnDemandRecord> Record;

	public List<OnDemandRecord> getRecord() {
		return Record;
	}

	public void setRecord(List<OnDemandRecord> record) {
		Record = record;
	}

	@Override
	public String toString() {
		return "OnDemandResponse [Record=" + Record + "]";
	}

}
