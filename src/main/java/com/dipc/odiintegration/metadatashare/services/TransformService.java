package com.dipc.odiintegration.metadatashare.services;

import java.util.ArrayList;
import java.util.List;

import com.dipc.odiintegration.metadatashare.models.Connection;
import com.dipc.odiintegration.metadatashare.models.ConnectionInfo;
import com.dipc.odiintegration.metadatashare.models.odi.DataserverInfo;
import com.dipc.odiintegration.metadatashare.models.odi.PhysicalSchemaInfo;

public class TransformService {

	public DataserverInfo fromConnectionToDataserver(Connection conn) {
		
		DataserverInfo dsInfo = new DataserverInfo();
		ConnectionInfo connInfo = conn.getApplicationProperties();
		dsInfo.setDataserverName(connInfo.getName());
		dsInfo.setDataserverUrl(connInfo.getUrl());
		dsInfo.setDsPwd(connInfo.getPassword());
		dsInfo.setDsUsername(connInfo.getPassword());
		dsInfo.setTechnology(connInfo.getPlateform());
		PhysicalSchemaInfo physicalSchema = new PhysicalSchemaInfo();
		physicalSchema.setPhysicalWorkSchemaName(connInfo.getDefaultSchema().getWorkSchemaName());
		physicalSchema.setPhysicalSchemaName(connInfo.getDefaultSchema().getName());
		List<PhysicalSchemaInfo> physicalSchemaList = new ArrayList<PhysicalSchemaInfo>();
		physicalSchemaList.add(physicalSchema);
		dsInfo.setPhysicalSchemas(physicalSchemaList);
		return dsInfo;
		
	}
}
