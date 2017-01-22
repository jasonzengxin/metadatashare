package com.dipc.odiintegration.metadatashare.models;

public class ExecConfigInfo {
	

	String connectionName;
	String schemaName;
	String plateform;

	
	public String getConnectionName() {
		return connectionName;
	}
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public String getPlateform() {
		return plateform;
	}
	public void setPlateform(String technology) {
		this.plateform = technology;
	}
	
}
