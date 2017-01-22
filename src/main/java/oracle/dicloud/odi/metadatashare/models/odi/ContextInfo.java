package oracle.dicloud.odi.metadatashare.models.odi;

public class ContextInfo {
	
	
	String loggicalSchemaName;
	String physicalSchemaName;
	String dataServerName;
	String technology;
	
	
	public String getLoggicalSchemaName() {
		return loggicalSchemaName;
	}
	public void setLoggicalSchemaName(String loggicalSchemaName) {
		this.loggicalSchemaName = loggicalSchemaName;
	}
	public String getDataServerName() {
		return dataServerName;
	}
	public void setDataServerName(String dataServerName) {
		this.dataServerName = dataServerName;
	}
	
	public String getLogicalSchemaName() {
		return loggicalSchemaName;
	}
	public void setLogicalSchemaName(String connectionName) {
		this.loggicalSchemaName = connectionName;
	}
	public String getPhysicalSchemaName() {
		return physicalSchemaName;
	}
	public void setPhysicalSchemaName(String schemaName) {
		this.physicalSchemaName = schemaName;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	
	
}
