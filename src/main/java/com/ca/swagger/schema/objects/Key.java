package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.List;

public class Key {

	private String name = null;
	private String  type = "primary";
	private List<String> columns = new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public void addColumn(String name){
		columns.add(name);
	}
	
}
