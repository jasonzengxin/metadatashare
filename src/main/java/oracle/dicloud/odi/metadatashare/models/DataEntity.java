package oracle.dicloud.odi.metadatashare.models;

public class DataEntity {

	private String name;
	private DataEntityInfo applicationProperties;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DataEntityInfo getApplicationProperties() {
		return applicationProperties;
	}
	public void setApplicationProperties(DataEntityInfo applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
}
