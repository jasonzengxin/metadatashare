package com.dipc.odiintegration.metadatashare.services;

import java.util.ArrayList;
import java.util.List;

import com.dipc.odiintegration.metadatashare.models.Connection;
import com.dipc.odiintegration.metadatashare.models.ConnectionInfo;
import com.dipc.odiintegration.metadatashare.models.DataEntity;
import com.dipc.odiintegration.metadatashare.models.DataEntityInfo;
import com.dipc.odiintegration.metadatashare.models.Schema;
import com.dipc.odiintegration.metadatashare.models.odi.DataserverInfo;
import com.dipc.odiintegration.metadatashare.models.odi.DatastoreInfo;
import com.dipc.odiintegration.metadatashare.models.odi.PhysicalSchemaInfo;

public class TransformService {

	
	private final String logicSchemaPrefix = "LS";
	private final String modelPrefix = "MOD";
	/**
	 * Convert a Connection in DIPC side to a Dataserver in ODI side.
	 * 
	 * @param conn
	 * @return
	 */
	public DataserverInfo fromConnectionToDataserver(Connection conn) {

		DataserverInfo dsInfo = new DataserverInfo();
		ConnectionInfo connInfo = conn.getApplicationProperties();
		dsInfo.setDataserverName(conn.getName());
		dsInfo.setDataserverUrl(connInfo.getUrl());
		dsInfo.setDsPwd(connInfo.getPassword());
		dsInfo.setDsUsername(connInfo.getPassword());
		dsInfo.setTechnology(connInfo.getPlateform());
		
		//if the default schema is set for this connection, using the default schema to create the physical schema.
		//It's possible that a dataserver without any physical schema.
		if(connInfo.getDefaultSchema() != null){
			PhysicalSchemaInfo physicalSchema = new PhysicalSchemaInfo();
			physicalSchema.setPhysicalWorkSchemaName(connInfo.getDefaultSchema().getApplicationProperties().getWorkSchemaName());
			physicalSchema.setPhysicalSchemaName(connInfo.getDefaultSchema().getName());
			List<PhysicalSchemaInfo> physicalSchemaList = new ArrayList<PhysicalSchemaInfo>();
			physicalSchemaList.add(physicalSchema);
			dsInfo.setPhysicalSchemas(physicalSchemaList);
		}
		
		return dsInfo;

	}
	/**
	 * Convert a DataEntity in DIPC side to a Datastore in ODI side.
	 * @param dataEntity
	 * @return
	 */
	public DatastoreInfo fromDataEntityToDatastore(DataEntity dataEntity){
		DatastoreInfo dsInfo = new DatastoreInfo();
		DataEntityInfo dataEntityInfo = dataEntity.getApplicationProperties();
		dsInfo.setName(dataEntity.getName());
		dsInfo.setModelName(generateModelName(dataEntityInfo.getParentSchema().getName()));
		dsInfo.setLogicalSchemaName(generateLogicSchemaName(dataEntityInfo.getParentSchema().getName()));
		dsInfo.setTechnology(dataEntityInfo.getTechnology());
		dsInfo.setColumnInfoList(dataEntityInfo.getColumnInfoList());
		dsInfo.setKeyInfoList(dataEntityInfo.getKeyInfoList());
		
		return dsInfo;
	}
	
	/**
	 * Convert a Schema in DIPC to a Physical Schema in ODI.
	 * @param schema
	 * @return
	 */
	public PhysicalSchemaInfo fromSchemaToPhysicalSchema(Schema schema){
		
		PhysicalSchemaInfo phySchemaInfo = new PhysicalSchemaInfo();
		phySchemaInfo.setPhysicalSchemaName(schema.getName());
		phySchemaInfo.setPhysicalWorkSchemaName(schema.getApplicationProperties().getWorkSchemaName());

		return phySchemaInfo;
	}
	
	public String generateLogicSchemaName(String dipcSchemaname) {
		return logicSchemaPrefix + "_" + dipcSchemaname;
	}

	public String generateModelName(String dipcSchemaname) {
		return modelPrefix + "_" + dipcSchemaname;
	}

	 
}
