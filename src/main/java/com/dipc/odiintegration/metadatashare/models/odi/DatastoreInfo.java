package com.dipc.odiintegration.metadatashare.models.odi;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class DatastoreInfo {
	private String technology;
	private String name;
	private String logicalSchemaName;
	private List<ColumnInfo> columnInfoList;
	private List<KeyInfo> keyInfoList;
	private String modelName;
	
	
	
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getLogicalSchemaName() {
		return logicalSchemaName;
	}
	public void setLogicalSchemaName(String logicalSchemaName) {
		this.logicalSchemaName = logicalSchemaName;
	}
	public List<ColumnInfo> getColumnInfoList() {
		return columnInfoList;
	}
	public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
		this.columnInfoList = columnInfoList;
	}
	public List<KeyInfo> getKeyInfoList() {
		return keyInfoList;
	}
	public void setKeyInfoList(List<KeyInfo> keyInfoList) {
		this.keyInfoList = keyInfoList;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
