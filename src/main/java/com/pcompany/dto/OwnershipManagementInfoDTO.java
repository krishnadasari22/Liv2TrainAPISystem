package com.pcompany.dto;


public class OwnershipManagementInfoDTO {

	private Integer id;
	
	private String management;
	
	private String personId;
	
	private String instrumentType;
	
	private String appointed;
	
	private String otherCompanies;
	
	private String experience;
	
	private Integer inceptionDate;
	
	/*private Integer jobId;
	
	private String jobFunctionCode;*/
	
	private String email;
	
	private String address;

	private String fax;
	
	private String phone;
	
	private String biography;
	
	/*private Integer jId;*/

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

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	@Override
	public String toString() {
		return "OwnershipManagementInfoDTO [id=" + id + ", management="
				+ management + ", personId=" + personId + ", instrumentType="
				+ instrumentType + ", appointed=" + appointed
				+ ", otherCompanies=" + otherCompanies + ", experience="
				+ experience + ", inceptionDate=" + inceptionDate + ", email="
				+ email + ", address=" + address + ", fax=" + fax + ", phone="
				+ phone + ", biography=" + biography + "]";
	}
}
