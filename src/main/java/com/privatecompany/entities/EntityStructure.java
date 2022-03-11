package com.privatecompany.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "entity_structure")
public class EntityStructure {

	@Id
	@Column(name = "entity_id")
	private String entityId;
	
	@Column(name = "entity")
	private String entity;

	@Column(name = "entity_type")
	private String entityType;
	
	@Column(name = "entity_type_desc")
	private String entityTypeDesc;
	
	@Column(name= "entity_country_name")
	private String entityCountryName;
	
	@Column(name = "parent_entity_id")
	private String parentEntityId;

	@Column(name = "parent_entity")
	private String parentEntity;

	@Column(name = "parent_entity_type")
	private String parentEntityType;
	
	@Column(name = "ultimate_parent_entity_id")
	private String ultimateParentEntityId;
	
	@Column(name = "ultimate_parent_entity")
	private String ultimateParentEntity;
	
	@Column(name = "ultimate_parent_entity_type")
	private String ultimateParentEntityType;
	
	@Column(name = "ultimate_parent_entity_type_desc")
	private String ultimateParentEntityTypeDesc;
	
	@Column(name= "ultimate_parent_country_name")
	private String ultimateParentCountryName;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getParentEntityId() {
		return parentEntityId;
	}

	public void setParentEntityId(String parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	public String getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}

	public String getParentEntityType() {
		return parentEntityType;
	}

	public void setParentEntityType(String parentEntityType) {
		this.parentEntityType = parentEntityType;
	}

	public String getUltimateParentEntityId() {
		return ultimateParentEntityId;
	}

	public void setUltimateParentEntityId(String ultimateParentEntityId) {
		this.ultimateParentEntityId = ultimateParentEntityId;
	}

	public String getUltimateParentEntity() {
		return ultimateParentEntity;
	}

	public void setUltimateParentEntity(String ultimateParentEntity) {
		this.ultimateParentEntity = ultimateParentEntity;
	}

	public String getUltimateParentEntityType() {
		return ultimateParentEntityType;
	}

	public void setUltimateParentEntityType(String ultimateParentEntityType) {
		this.ultimateParentEntityType = ultimateParentEntityType;
	}

	public String getEntityTypeDesc() {
		return entityTypeDesc;
	}

	public void setEntityTypeDesc(String entityTypeDesc) {
		this.entityTypeDesc = entityTypeDesc;
	}

	public String getUltimateParentEntityTypeDesc() {
		return ultimateParentEntityTypeDesc;
	}

	public void setUltimateParentEntityTypeDesc(String ultimateParentEntityTypeDesc) {
		this.ultimateParentEntityTypeDesc = ultimateParentEntityTypeDesc;
	}

	public String getEntityCountryName() {
		return entityCountryName;
	}

	public void setEntityCountryName(String entityCountryName) {
		this.entityCountryName = entityCountryName;
	}

	public String getUltimateParentCountryName() {
		return ultimateParentCountryName;
	}

	public void setUltimateParentCountryName(String ultimateParentCountryName) {
		this.ultimateParentCountryName = ultimateParentCountryName;
	}

	@Override
	public String toString() {
		return "EntityStructure [entityId=" + entityId + ", entity=" + entity
				+ ", entityType=" + entityType + ", entityTypeDesc="
				+ entityTypeDesc + ", entityCountryName=" + entityCountryName
				+ ", parentEntityId=" + parentEntityId + ", parentEntity="
				+ parentEntity + ", parentEntityType=" + parentEntityType
				+ ", ultimateParentEntityId=" + ultimateParentEntityId
				+ ", ultimateParentEntity=" + ultimateParentEntity
				+ ", ultimateParentEntityType=" + ultimateParentEntityType
				+ ", ultimateParentEntityTypeDesc="
				+ ultimateParentEntityTypeDesc + ", ultimateParentCountryName="
				+ ultimateParentCountryName + "]";
	}
	
	
}
