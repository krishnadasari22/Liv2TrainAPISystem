package com.televisory.capitalmarket.factset.dto;

public class FFEntityProfileDTO {

	private Integer id;
	
	private String entityId;
	
	private String profileType;
	
	private String entityProfile;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getProfileType() {
		return profileType;
	}

	public void setProfileType(String profileType) {
		this.profileType = profileType;
	}

	public String getEntityProfile() {
		return entityProfile;
	}

	public void setEntityProfile(String entityProfile) {
		this.entityProfile = entityProfile;
	}

	@Override
	public String toString() {
		return "FFEntityProfile [id=" + id + ", entityId=" + entityId + ", profileType=" + profileType
				+ ", entityProfile=" + entityProfile + "]";
	}
}
