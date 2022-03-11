package com.privatecompany.dto;

import java.util.Date;

public class PrivateCompanyDTO {
	
	private String id;
	
	private String name;
	
	private String industryCode;
	
	private String industryName;
	
	private String sectorCode;
	
	private String sectorName;
	
	private String countryCode;
	
	private String domicileCountryName;

	private Date date;

	private String yearFounded;
	
	private String website;
	
	private String contactNo;
	
	private String email;

	private String street1;
	
	private String street2;
	
	private String street3;
	
	private String street4;
	
	private String street5;
	
	private String city;
	
	private String state;
	
	private String postalCode;
	
	private String countryName;
	
	private String totalEmp;
	
	private Integer crunchBaseRank;
	
	private String reportingCurrency;
	
	private String detailedDescription;
	
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
	
	public String getIndustryCode() {
		return industryCode;
	}
	
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	
	public String getIndustryName() {
		return industryName;
	}
	
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getDomicileCountryName() {
		return domicileCountryName;
	}
	
	public void setDomicileCountryName(String domicileCountryName) {
		this.domicileCountryName = domicileCountryName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getYearFounded() {
		return yearFounded;
	}
	
	public void setYearFounded(String yearFounded) {
		this.yearFounded = yearFounded;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getContactNo() {
		return contactNo;
	}
	
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getStreet1() {
		return street1;
	}
	
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	
	public String getStreet2() {
		return street2;
	}
	
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	
	public String getStreet3() {
		return street3;
	}
	
	public void setStreet3(String street3) {
		this.street3 = street3;
	}
	
	public String getStreet4() {
		return street4;
	}
	
	public void setStreet4(String street4) {
		this.street4 = street4;
	}
	
	public String getStreet5() {
		return street5;
	}
	
	public void setStreet5(String street5) {
		this.street5 = street5;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getTotalEmp() {
		return totalEmp;
	}
	
	public void setTotalEmp(String totalEmp) {
		this.totalEmp = totalEmp;
	}
	
	public Integer getCrunchBaseRank() {
		return crunchBaseRank;
	}
	
	public void setCrunchBaseRank(Integer crunchBaseRank) {
		this.crunchBaseRank = crunchBaseRank;
	}
	
	public String getReportingCurrency() {
		return reportingCurrency;
	}
	
	public void setReportingCurrency(String reportingCurrency) {
		this.reportingCurrency = reportingCurrency;
	}
	
	public String getSectorCode() {
		return sectorCode;
	}
	
	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}
	
	public String getSectorName() {
		return sectorName;
	}
	
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	
	public String getDetailedDescription() {
		return detailedDescription;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	@Override
	public String toString() {
		return "PrivateCompanyDTO [id=" + id + ", name=" + name + ", industryCode=" + industryCode + ", industryName="
				+ industryName + ", sectorCode=" + sectorCode + ", sectorName=" + sectorName + ", countryCode="
				+ countryCode + ", domicileCountryName=" + domicileCountryName + ", date=" + date + ", yearFounded="
				+ yearFounded + ", website=" + website + ", contactNo=" + contactNo + ", email=" + email + ", street1="
				+ street1 + ", street2=" + street2 + ", street3=" + street3 + ", street4=" + street4 + ", street5="
				+ street5 + ", city=" + city + ", state=" + state + ", postalCode=" + postalCode + ", countryName="
				+ countryName + ", totalEmp=" + totalEmp + ", crunchBaseRank=" + crunchBaseRank + ", reportingCurrency="
				+ reportingCurrency + ", detailedDescription=" + detailedDescription + "]";
	}
}
