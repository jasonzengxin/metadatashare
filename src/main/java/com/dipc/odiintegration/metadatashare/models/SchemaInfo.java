package com.dipc.odiintegration.metadatashare.models;

public class SchemaInfo {
	private String name;
	private ConnectionInfo defaultConnection;
	private String workSchemaName;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ConnectionInfo getDefaultConnection() {
		return defaultConnection;
	}
	public void setDefaultConnection(ConnectionInfo defaultConnection) {
		this.defaultConnection = defaultConnection;
	}
	public String getWorkSchemaName() {
		return workSchemaName;
	}
	public void setWorkSchemaName(String workSchemaName) {
		this.workSchemaName = workSchemaName;
	}
	
}
