package com.televisory.capitalmarket.entities.cm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "object_properties")
public class ObjectProperties {

	@Id
	@Column(name="object_property_id")
	private Integer id;
	
	@Column(name="object_name")
	private String name;
	
	@Column(name="default_value")
	private String value;
	
	@Column(name="description")
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
