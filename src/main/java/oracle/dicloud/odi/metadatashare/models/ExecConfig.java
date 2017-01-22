package oracle.dicloud.odi.metadatashare.models;

public class ExecConfig {

	private String name;
	
	
	private ExecConfigInfo applicationProperties;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ExecConfigInfo getApplicationProperties() {
		return applicationProperties;
	}
	public void setApplicationProperties(ExecConfigInfo applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	
}
