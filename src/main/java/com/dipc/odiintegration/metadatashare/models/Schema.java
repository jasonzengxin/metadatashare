package com.dipc.odiintegration.metadatashare.models;

public class Schema {
	private String name;
	private SchemaInfo applicationProperties;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SchemaInfo getApplicationProperties() {
		return applicationProperties;
	}
	public void setApplicationProperties(SchemaInfo applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
}
