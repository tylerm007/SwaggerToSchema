package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String entity;
	private List<Attribute> columns = new ArrayList<Attribute>();
	//private List<Relationship> children = new ArrayList<Relationship>();
	private List<Relationship> parents = new ArrayList<Relationship>();

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		String name = clean(entity);
		this.entity = name;
	}

	/**
	 * Live API Creator includes prefix (demo:customer) which we strip off
	 * @param value
	 * @return
	 */
	private String clean(String value) {
		int idx = value.indexOf(":");
		if(idx > 0){
			return value.substring(idx + 1); 
		}
		return value;
	}

	public List<Attribute> getColumns() {
		return columns;
	}

	public void setColumns(List<Attribute> columns) {
		this.columns = columns;
	}

	public List<Relationship> getParents() {
		return parents;
	}

	public void setParents(List<Relationship> parents) {
		this.parents = parents;
	}

	public void addParents(Relationship reln) {
		this.parents.add(reln);

	}

}
