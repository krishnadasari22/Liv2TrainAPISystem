package com.pcompany.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "companies")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransciptCompanies {
	
	@XmlElement
	private String company;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "TransciptCompanies [company=" + company + "]";
	}
	
}
