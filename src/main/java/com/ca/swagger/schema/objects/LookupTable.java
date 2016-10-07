package com.ca.swagger.schema.objects;

public class LookupTable {

	private String baseEntity;
	private String refEntity;
	private String columnName;
	private String refArray;

	public LookupTable(String baseEntity, String refEntity, String column) {
		this.baseEntity = baseEntity;
		this.refEntity = refEntity;
		this.columnName = column;
	}

	public String getBaseEntity() {
		return baseEntity;
	}

	public void setBaseEntity(String baseEntity) {
		this.baseEntity = baseEntity;
	}

	public String getRefEntity() {
		return refEntity;
	}

	public void setRefEntity(String refEntity) {
		this.refEntity = refEntity;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getRefArray() {
		return refArray;
	}

	public void setRefArray(String refArray) {
		this.refArray = refArray;
	}

	

}
