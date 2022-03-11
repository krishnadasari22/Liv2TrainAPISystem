package com.televisory.capitalmarket.dto;

public class ObjectPropertiesDTO {

	private Integer id;
	
	private String name;
	
	private String value;
	
	private String description;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ObjectProperties [id=" + id + ", name=" + name + ", value=" + value + ", description=" + description
				+ "]";
	}

}
