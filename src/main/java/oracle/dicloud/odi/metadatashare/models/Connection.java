package oracle.dicloud.odi.metadatashare.models;

public class Connection {

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
