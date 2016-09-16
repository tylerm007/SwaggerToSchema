package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.List;

public class Schema {

	private List<Table> tables = new ArrayList<Table>();

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
}
