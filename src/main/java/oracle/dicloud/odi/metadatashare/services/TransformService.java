package oracle.dicloud.odi.metadatashare.services;

import java.util.ArrayList;
import java.util.List;

import oracle.dicloud.odi.metadatashare.models.Connection;
import oracle.dicloud.odi.metadatashare.models.ConnectionInfo;
import oracle.dicloud.odi.metadatashare.models.DataEntity;
import oracle.dicloud.odi.metadatashare.models.DataEntityInfo;
import oracle.dicloud.odi.metadatashare.models.ExecConfig;
import oracle.dicloud.odi.metadatashare.models.ExecConfigInfo;
import oracle.dicloud.odi.metadatashare.models.Schema;
import oracle.dicloud.odi.metadatashare.models.odi.ContextInfo;
import oracle.dicloud.odi.metadatashare.models.odi.DataserverInfo;
import oracle.dicloud.odi.metadatashare.models.odi.DatastoreInfo;
import oracle.dicloud.odi.metadatashare.models.odi.PhysicalSchemaInfo;

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
	
	public ContextInfo fromExecConifgToContext(ExecConfig execConfig){
		
		ContextInfo contextInfo = new ContextInfo();
		contextInfo.setLogicalSchemaName(generateLogicSchemaName(execConfig.getApplicationProperties().getSchemaName()));
		contextInfo.setPhysicalSchemaName(execConfig.getApplicationProperties().getSchemaName());
		contextInfo.setTechnology(execConfig.getApplicationProperties().getPlateform());
        contextInfo.setDataServerName(execConfig.getApplicationProperties().getConnectionName());
		return contextInfo;
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
