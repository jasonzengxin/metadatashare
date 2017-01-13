package com.dipc.odiintegration.metadatashare.models;

import java.util.List;

import com.dipc.odiintegration.metadatashare.models.odi.ColumnInfo;
import com.dipc.odiintegration.metadatashare.models.odi.KeyInfo;

public class DataEntityInfo {
	private String name;
	private Schema schema;
	private List<ColumnInfo> attributes;
	private List<KeyInfo> keys;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public List<ColumnInfo> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<ColumnInfo> attributes) {
		this.attributes = attributes;
	}
	public List<KeyInfo> getKeys() {
		return keys;
	}
	public void setKeys(List<KeyInfo> keys) {
		this.keys = keys;
	}
	
}
