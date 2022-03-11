package com.televisory.user.dto;

import java.io.Serializable;

public class keyManagementInfoDto implements Serializable {

	private static final long serialVersionUID = 5937117951093267076L;
	
	String title;
	String name;
	String designation;
	Integer yearOfExp;
	
	public keyManagementInfoDto() {
		super();
	}

	public keyManagementInfoDto(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public Integer getYearOfExp() {
		return yearOfExp;
	}
	public void setYearOfExp(Integer yearOfExp) {
		this.yearOfExp = yearOfExp;
	}
	
	@Override
	public String toString() {
		return "keyManagementInfoDto [title=" + title + ", name=" + name
				+ ", designation=" + designation + ", yearOfExp=" + yearOfExp
				+ "]";
	}

}
