package com.pcompany.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
/*@Table(name = "own_v5_own_con_bio")*/
@Table(name = "ff_v3_ff_od_bio")
public class OwnershipPeopleInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="company")
	private String company;
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name = "factset_person_id")
	private String personId;
	
	@Column(name="position")
	private String position;
	
	@Column(name="address")
	private String address;
	
	@Column(name="email")
	private String email;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="bio")
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
		return "OwnershipPeopleInfo [id=" + id + ", company=" + company
				+ ", fullName=" + fullName + ", personId=" + personId
				+ ", position=" + position + ", address=" + address
				+ ", email=" + email + ", fax=" + fax + ", phone=" + phone
				+ ", bio=" + bio + "]";
	}
}
