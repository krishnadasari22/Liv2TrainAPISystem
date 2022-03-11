package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ff_v3_ff_od_people")
public class OwnershipManagementInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="management")
	private String management;
	
	@Column(name = "factset_person_id")
	private String personId;
	
	@Column(name = "designation")
	private String instrumentType;
	
	@Column(name = "appointed")
	private String appointed;
	
	@Column(name = "other_companies")
	private String otherCompanies;
	
	@Column(name = "experience")
	private String experience;
	
	@Column(name = "min_inception_date")
	private Integer inceptionDate;
	
	/*@Column(name = "job_id")
	private Integer jobId;
	
	@Column(name = "job_function_code")
	private String jobFunctionCode;*/
	
	@Column(name = "email")
	private String email;
	
	@Column(name="address")
	private String address;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "biography")
	private String biography;
	
	/*@Column(name = "j_id")
	private Integer jId;*/
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getAppointed() {
		return appointed;
	}

	public void setAppointed(String appointed) {
		this.appointed = appointed;
	}

	public String getOtherCompanies() {
		return otherCompanies;
	}

	public void setOtherCompanies(String otherCompanies) {
		this.otherCompanies = otherCompanies;
	}

	/*public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobFunctionCode() {
		return jobFunctionCode;
	}

	public void setJobFunctionCode(String jobFunctionCode) {
		this.jobFunctionCode = jobFunctionCode;
	}*/

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	/*public Integer getjId() {
		return jId;
	}

	public void setjId(Integer jId) {
		this.jId = jId;
	}*/
	
	public Integer getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Integer inceptionDate) {
		this.inceptionDate = inceptionDate;
	}
	
	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	@Override
	public String toString() {
		return "OwnershipManagementInfo [id=" + id + ", management="
				+ management + ", personId=" + personId + ", instrumentType="
				+ instrumentType + ", appointed=" + appointed
				+ ", otherCompanies=" + otherCompanies + ", experience="
				+ experience + ", inceptionDate=" + inceptionDate + ", email="
				+ email + ", address=" + address + ", fax=" + fax + ", phone="
				+ phone + ", biography=" + biography + "]";
	}
}
