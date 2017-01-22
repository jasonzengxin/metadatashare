package oracle.dicloud.odi.metadatashare.models.odi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhysicalSchemaInfo {
	private String physicalSchemaName;
	private String physicalWorkSchemaName;
	
	
	public String getPhysicalSchemaName() {
		return physicalSchemaName;
	}
	public void setPhysicalSchemaName(String physicalSchemaName) {
		this.physicalSchemaName = physicalSchemaName;
	}
	public String getPhysicalWorkSchemaName() {
		return physicalWorkSchemaName;
	}
	public void setPhysicalWorkSchemaName(String physicalWorkSchemaName) {
		this.physicalWorkSchemaName = physicalWorkSchemaName;
	}
}
