package com.dipc.odiintegration.metadatashare.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KeyInfo {
private String keyName;
private String keyType;
private String keyColumnName;
public String getKeyName() {
	return keyName;
}
public void setKeyName(String keyName) {
	this.keyName = keyName;
}
public String getKeyType() {
	return keyType;
}
public void setKeyType(String keyType) {
	this.keyType = keyType;
}
public String getKeyColumnName() {
	return keyColumnName;
}
public void setKeyColumnName(String keyColumnName) {
	this.keyColumnName = keyColumnName;
}
}
