package oracle.dicloud.odi.metadatashare.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConnectionInfo {


	private Schema defaultSchema;
	private String plateform;
	private String username;
	private String password;
	private String url;
	private ConnectionTypeInfo connectionType;


	public Schema getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(Schema defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public String getPlateform() {
		return plateform;
	}

	public void setPlateform(String plateform) {
		this.plateform = plateform;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ConnectionTypeInfo getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(ConnectionTypeInfo connectionType) {
		this.connectionType = connectionType;
	}

}
