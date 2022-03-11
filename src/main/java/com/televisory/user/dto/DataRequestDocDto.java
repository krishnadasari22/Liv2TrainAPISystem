package com.televisory.user.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DataRequestDocDto {
	private static final long serialVersionUID = -1829801053603420210L;
	//private Integer companyId;
	private String documentCategory;
	private String documentDetails;
	public String getDocumentCategory() {
		return documentCategory;
	}
	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = null;
		try {
			this.documentCategory = URLDecoder.decode(documentCategory, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public String getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(String documentDetails) {
		
		this.documentDetails = null;
		try {
			this.documentDetails = URLDecoder.decode(documentDetails, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Override
	public String toString() {
		return "DataRequestDocDto [documentCategory=" + documentCategory
				+ ", documentDetails=" + documentDetails + "]";
	}
}
