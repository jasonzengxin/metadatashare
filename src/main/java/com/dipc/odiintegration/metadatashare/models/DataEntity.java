package com.dipc.odiintegration.metadatashare.models;

public class DataEntity {
	private String name;
	private ConnectionInfo applicationProperties;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ConnectionInfo getApplicationProperties() {
		return applicationProperties;
	}
	public void setApplicationProperties(ConnectionInfo applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	
}
