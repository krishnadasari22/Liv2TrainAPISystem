package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="pe_v1_pe_securities")
public class PevcIssueType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	
	@Column(name="issue_type_code")
	private String issueTypeCode;
	
	
	@Column(name="issue_type_desc")
	private String issueTypeDesc;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIssueTypeCode() {
		return issueTypeCode;
	}


	public void setIssueTypeCode(String issueTypeCode) {
		this.issueTypeCode = issueTypeCode;
	}


	public String getIssueTypeDesc() {
		return issueTypeDesc;
	}


	public void setIssueTypeDesc(String issueTypeDesc) {
		this.issueTypeDesc = issueTypeDesc;
	}
	
	
	

}
