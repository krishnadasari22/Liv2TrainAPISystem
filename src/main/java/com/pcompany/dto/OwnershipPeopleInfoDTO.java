package com.pcompany.dto;

public class OwnershipPeopleInfoDTO {

	private Integer id;
	
	private String company;
	
	private String fullName;
	
	private String personId;
	
	private String position;
	
	private String address;
	
	private String email;
	
	private String fax;
	
	private String phone;
	
	private String bio;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	@Override
	public String toString() {
		return "OwnershipPeopleInfoDTO [id=" + id + ", company=" + company
				+ ", fullName=" + fullName + ", personId=" + personId
				+ ", position=" + position + ", address=" + address
				+ ", email=" + email + ", fax=" + fax + ", phone=" + phone
				+ ", bio=" + bio + "]";
	}
}
