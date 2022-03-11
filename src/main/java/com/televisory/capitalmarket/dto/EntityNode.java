package com.televisory.capitalmarket.dto;

import java.util.HashMap;
import java.util.Map;

public class EntityNode {

	String parentId;
	String entityId;
	String name;
	String level;
	Integer iLevel;
	String country;
	String entityType;
	Boolean hasChild;
	Integer noOfChild;
	Map<String,EntityNode> childNodes;

	public Integer getiLevel() {
		return iLevel;
	}
	public void setiLevel(Integer iLevel) {
		this.iLevel = iLevel;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public Boolean getHasChild() {
		return hasChild;
	}
	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}
	public Integer getNoOfChild() {
		return noOfChild;
	}
	public void setNoOfChild(Integer noOfChild) {
		this.noOfChild = noOfChild;
	}
	public Map<String, EntityNode> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(Map<String, EntityNode> childNodes) {
		this.childNodes = childNodes;
	}
	@Override
	public String toString() {
		return "EntityNode [parentId=" + parentId + ", entityId=" + entityId
				+ ", name=" + name + ", level=" + level + ", iLevel=" + iLevel
				+ ", country=" + country + ", entityType=" + entityType
				+ ", hasChild=" + hasChild + ", noOfChild=" + noOfChild
				+ ", childNodes=" + childNodes + "]";
	}


}