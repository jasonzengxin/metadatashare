package com.dipc.odiintegration.metadatashare.models;

import java.util.List;

import com.dipc.odiintegration.metadatashare.models.odi.ColumnInfo;
import com.dipc.odiintegration.metadatashare.models.odi.KeyInfo;

public class DataEntityInfo {

	
	private String technology;
	private Schema parentSchema;
	private List<ColumnInfo> columnInfoList;
	private List<KeyInfo> keyInfoList;
	
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public Schema getParentSchema() {
		return parentSchema;
	}
	public void setParentSchema(Schema parentSchema) {
		this.parentSchema = parentSchema;
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

}
