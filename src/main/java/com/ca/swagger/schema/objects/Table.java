package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String entity;
	private List<String> primaryKeyColumns = new ArrayList<>();
	private List<Attribute> columns = new ArrayList<>();
	private List<Relationship> parents = new ArrayList<>();
	private List<Key> keys = new ArrayList<>();

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
	
	public void addPrimaryKeyColumns(String columnName) {
		this.primaryKeyColumns.add(columnName);
	}

	public List<String> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
		this.primaryKeyColumns = primaryKeyColumns;
	}

	public List<Key> getKeys() {
		return keys;
	}

	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	
	public void addKey(Key akey){
		keys.add(akey);
	}

	public boolean hasExistingReln(Relationship reln) {
		for(Relationship areln : parents){
			if(areln.getParent_entity().equals(reln.getParent_entity()))
				return true;
		}
		return false;
	}

}
