package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Relationship {

	private String relationship_name;
	private String parent_entity;
	private String child_entity;
	private List<String> parent_column_names = new ArrayList<String>();
	private List<String> child_column_names = new ArrayList<String>();

	public String getRelationship_name() {
		return relationship_name;
	}

	public void setRelationship_name(String relationship_name) {
		this.relationship_name = relationship_name;
	}

	public String getParent_entity() {
		return parent_entity;
	}

	public void setParent_entity(String parent_entity) {
		this.parent_entity = parent_entity;
	}

	public List<String> getParent_column_names() {
		return parent_column_names;
	}

	public void setParent_column_names(List<String> parent_column_names) {
		this.parent_column_names = parent_column_names;
	}

	public List<String> getChild_column_names() {
		return child_column_names;
	}

	public void setChild_column_names(List<String> child_column_names) {
		this.child_column_names = child_column_names;
	}

	public void addChild_column_names(String child_column_names) {
		this.child_column_names.add(child_column_names);
	}

	public void addParent_column_names(String parent_column_names) {
		this.parent_column_names.add(parent_column_names);
	}

	public String getChild_entity() {
		return child_entity;
	}

	public void setChild_entity(String child_entity) {
		this.child_entity = child_entity;
	}

}
