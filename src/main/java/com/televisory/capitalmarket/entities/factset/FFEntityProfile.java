package com.televisory.capitalmarket.entities.factset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ent_v1_ent_entity_profiles")
public class FFEntityProfile {

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="factset_entity_id")
	private String entityId;
	
	@Column(name="entity_profile_type")
	private String profileType;
	
	@Column(name="entity_profile")
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
