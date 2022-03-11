package com.televisory.user.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BasicInfoDto implements Serializable {

	private static final long serialVersionUID = -1829801053603420210L;
	//private Integer companyId;
	private String ticsIndustryCode;
	private String companyName;
	private String registrationNo;
	private String countryIsoCode;
	private String stateName; 
	private String classOfCompany;
	private Date dateOfIncorporation;
	private String description;
	private String financialYearEndingMonth;
	private String taxRegistrationNumber;
	private double totalCreditExposure;
	
	private String headquartersAddr;
	private String contactPersonName;
	private String contactPersonDesignation;
	private String contactPersonEmail;
	private String contactPersonPhone;
	
	private Integer politicallyExposedPerson;
	private Integer sanctionedAgainstCompany;
	private Integer independentDirector;
	private Integer criminalRecord;
	private Integer missedPaymentOfdebt;
	
	private String politicallyExposedPersonComments;
	private String sanctionedAgainstCompanyComments;
	private String independentDirectorComments;
	private String criminalRecordComments;
	private String missedPaymentOfdebtComments;
	
	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}
	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	public String getCountryIsoCode() {
		return countryIsoCode;
	}
	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getClassOfCompany() {
		return classOfCompany;
	}
	public void setClassOfCompany(String classOfCompany) {
		this.classOfCompany = classOfCompany;
	}
	public Date getDateOfIncorporation() {
		return dateOfIncorporation;
	}
	public void setDateOfIncorporation(Date dateOfIncorporation) {
		this.dateOfIncorporation = dateOfIncorporation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFinancialYearEndingMonth() {
		return financialYearEndingMonth;
	}
	public void setFinancialYearEndingMonth(String financialYearEndingMonth) {
		this.financialYearEndingMonth = financialYearEndingMonth;
	}
	public String getTaxRegistrationNumber() {
		return taxRegistrationNumber;
	}
	public void setTaxRegistrationNumber(String taxRegistrationNumber) {
		this.taxRegistrationNumber = taxRegistrationNumber;
	}
	public String getHeadquartersAddr() {
		return headquartersAddr;
	}
	public void setHeadquartersAddr(String headquartersAddr) {
		this.headquartersAddr = headquartersAddr;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getContactPersonDesignation() {
		return contactPersonDesignation;
	}
	public void setContactPersonDesignation(String contactPersonDesignation) {
		this.contactPersonDesignation = contactPersonDesignation;
	}
	public String getContactPersonEmail() {
		return contactPersonEmail;
	}
	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}
	public String getContactPersonPhone() {
		return contactPersonPhone;
	}
	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
	}
	public Integer getPoliticallyExposedPerson() {
		return politicallyExposedPerson;
	}
	public void setPoliticallyExposedPerson(Integer politicallyExposedPerson) {
		this.politicallyExposedPerson = politicallyExposedPerson;
	}
	public Integer getSanctionedAgainstCompany() {
		return sanctionedAgainstCompany;
	}
	public void setSanctionedAgainstCompany(Integer sanctionedAgainstCompany) {
		this.sanctionedAgainstCompany = sanctionedAgainstCompany;
	}
	public Integer getIndependentDirector() {
		return independentDirector;
	}
	public void setIndependentDirector(Integer independentDirector) {
		this.independentDirector = independentDirector;
	}
	public Integer getCriminalRecord() {
		return criminalRecord;
	}
	public void setCriminalRecord(Integer criminalRecord) {
		this.criminalRecord = criminalRecord;
	}
	public Integer getMissedPaymentOfdebt() {
		return missedPaymentOfdebt;
	}
	public void setMissedPaymentOfdebt(Integer missedPaymentOfdebt) {
		this.missedPaymentOfdebt = missedPaymentOfdebt;
	}
	public String getPoliticallyExposedPersonComments() {
		return politicallyExposedPersonComments;
	}
	public void setPoliticallyExposedPersonComments(
			String politicallyExposedPersonComments) {
		this.politicallyExposedPersonComments = politicallyExposedPersonComments;
	}
	public String getSanctionedAgainstCompanyComments() {
		return sanctionedAgainstCompanyComments;
	}
	public void setSanctionedAgainstCompanyComments(
			String sanctionedAgainstCompanyComments) {
		this.sanctionedAgainstCompanyComments = sanctionedAgainstCompanyComments;
	}
	public String getIndependentDirectorComments() {
		return independentDirectorComments;
	}
	public void setIndependentDirectorComments(String independentDirectorComments) {
		this.independentDirectorComments = independentDirectorComments;
	}
	public String getCriminalRecordComments() {
		return criminalRecordComments;
	}
	public void setCriminalRecordComments(String criminalRecordComments) {
		this.criminalRecordComments = criminalRecordComments;
	}
	public String getMissedPaymentOfdebtComments() {
		return missedPaymentOfdebtComments;
	}
	public void setMissedPaymentOfdebtComments(String missedPaymentOfdebtComments) {
		this.missedPaymentOfdebtComments = missedPaymentOfdebtComments;
	}
	public double getTotalCreditExposure() {
		return totalCreditExposure;
	}
	public void setTotalCreditExposure(double totalCreditExposure) {
		this.totalCreditExposure = totalCreditExposure;
	}
	@Override
	public String toString() {
		return "BasicInfoDto [ticsIndustryCode=" + ticsIndustryCode
				+ ", companyName=" + companyName + ", registrationNo="
				+ registrationNo + ", countryIsoCode=" + countryIsoCode
				+ ", stateName=" + stateName + ", classOfCompany="
				+ classOfCompany + ", dateOfIncorporation="
				+ dateOfIncorporation + ", description=" + description
				+ ", financialYearEndingMonth=" + financialYearEndingMonth
				+ ", taxRegistrationNumber=" + taxRegistrationNumber
				+ ", totalCreditExposure=" + totalCreditExposure
				+ ", headquartersAddr=" + headquartersAddr
				+ ", contactPersonName=" + contactPersonName
				+ ", contactPersonDesignation=" + contactPersonDesignation
				+ ", contactPersonEmail=" + contactPersonEmail
				+ ", contactPersonPhone=" + contactPersonPhone
				+ ", politicallyExposedPerson=" + politicallyExposedPerson
				+ ", sanctionedAgainstCompany=" + sanctionedAgainstCompany
				+ ", independentDirector=" + independentDirector
				+ ", criminalRecord=" + criminalRecord
				+ ", missedPaymentOfdebt=" + missedPaymentOfdebt
				+ ", politicallyExposedPersonComments="
				+ politicallyExposedPersonComments
				+ ", sanctionedAgainstCompanyComments="
				+ sanctionedAgainstCompanyComments
				+ ", independentDirectorComments="
				+ independentDirectorComments + ", criminalRecordComments="
				+ criminalRecordComments + ", missedPaymentOfdebtComments="
				+ missedPaymentOfdebtComments + "]";
	}
	
	
}
