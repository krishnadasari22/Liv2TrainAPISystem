package com.privatecompany.dto;


public class EntityStructureDTO {

	private String entityId;
	private String entity;
	private String entityType;
	private String entityTypeDesc;
	private String entityCountryName;
	private String parentEntityId;
	private String parentEntity;
	private String parentEntityType;
	private String ultimateParentEntityId;
	private String ultimateParentEntity;
	private String ultimateParentEntityType;
	private String ultimateParentEntityTypeDesc;
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

	public String getEntityCountryName() {
		return entityCountryName;
	}

	public void setEntityCountryName(String entityCountryName) {
		this.entityCountryName = entityCountryName;
	}

	public String getUltimateParentEntityTypeDesc() {
		return ultimateParentEntityTypeDesc;
	}

	public void setUltimateParentEntityTypeDesc(String ultimateParentEntityTypeDesc) {
		this.ultimateParentEntityTypeDesc = ultimateParentEntityTypeDesc;
	}

	public String getUltimateParentCountryName() {
		return ultimateParentCountryName;
	}

	public void setUltimateParentCountryName(String ultimateParentCountryName) {
		this.ultimateParentCountryName = ultimateParentCountryName;
	}

	@Override
	public String toString() {
		return "EntityStructureDTO [entityId=" + entityId + ", entity="
				+ entity + ", entityType=" + entityType + ", entityTypeDesc="
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
