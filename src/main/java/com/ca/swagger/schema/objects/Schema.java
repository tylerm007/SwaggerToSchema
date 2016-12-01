package com.ca.swagger.schema.objects;

import java.util.ArrayList;
import java.util.List;

public class Schema {

	private List<Table> tables = new ArrayList<>();

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public boolean hasTableName(String entity) {
		for(Table table: this.tables){
			if(table.getEntity().equals(entity)){
				return true;
			}
		}
		return false;
	}
	
}
