package oracle.dicloud.odi.metadatashare.models;

public class SchemaInfo {

	private String plateform;
	private Connection defaultConnection;
	private String workSchemaName;
	
	public Connection getDefaultConnection() {
		return defaultConnection;
	}
	public void setDefaultConnection(Connection defaultConnection) {
		this.defaultConnection = defaultConnection;
	}
	public String getWorkSchemaName() {
		return workSchemaName;
	}
	public void setWorkSchemaName(String workSchemaName) {
		this.workSchemaName = workSchemaName;
	}
	public String getPlateform() {
		return plateform;
	}
	public void setPlateform(String plateform) {
		this.plateform = plateform;
	}
	
}
