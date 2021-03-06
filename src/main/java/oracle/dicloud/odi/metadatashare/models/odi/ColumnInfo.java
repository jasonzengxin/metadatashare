package oracle.dicloud.odi.metadatashare.models.odi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ColumnInfo {
	private String name;
	private String dataType;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
