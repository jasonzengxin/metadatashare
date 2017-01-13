package com.dipc.odiintegration.metadatashare.models.odi;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataserverInfo {
	
	
	//private String logicalSchemaName;
	private String technology;
	private String dataserverName;
	private String dataserverUrl;
	private String dsUsername;

	private String dsPwd;
	private List<PhysicalSchemaInfo> physicalSchemas;
	public DataserverInfo(){}

	public DataserverInfo(String dataserverName){
		this.dataserverName = dataserverName;
	}

	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	
	public String getDataserverName() {
		return dataserverName;
	}
	public void setDataserverName(String dataserverName) {
		this.dataserverName = dataserverName;
	}
	public String getDataserverUrl() {
		return dataserverUrl;
	}
	public void setDataserverUrl(String dataserverUrl) {
		this.dataserverUrl = dataserverUrl;
	}
	public String getDsUsername() {
		return dsUsername;
	}
	public void setDsUsername(String dsUsername) {
		this.dsUsername = dsUsername;
	}
	public String getDsPwd() {
		return dsPwd;
	}
	public void setDsPwd(String dsPwd) {
		this.dsPwd = dsPwd;
	}

	public List<PhysicalSchemaInfo> getPhysicalSchemas() {
		return physicalSchemas;
	}

	public void setPhysicalSchemas(List<PhysicalSchemaInfo> physicalSchemas) {
		this.physicalSchemas = physicalSchemas;
	}


}
