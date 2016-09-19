package com.ca.swagger.schema.objects;

public class Attribute {

	private String name;
	private String generic_type = "string";
	private String subtype = null;
	private Integer size = null;
	private boolean nullable = true;
	private Boolean autonum = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGeneric_type() {
		return generic_type;
	}

	public void setGeneric_type(String generic_type) {
		this.generic_type = generic_type;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Boolean isAutonum() {
		return autonum;
	}

	public void setAutonum(Boolean autonum) {
		this.autonum = autonum;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public Boolean getAutonum() {
		return autonum;
	}

}
