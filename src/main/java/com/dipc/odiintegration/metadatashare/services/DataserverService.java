package com.dipc.odiintegration.metadatashare.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dipc.odiintegration.metadatashare.OdiReposGlobalHelper;
import com.dipc.odiintegration.metadatashare.models.DataserverInfo;
import com.dipc.odiintegration.metadatashare.models.PhysicalSchemaInfo;

import oracle.odi.domain.topology.OdiDataServer;
import oracle.odi.domain.topology.OdiPhysicalSchema;
import oracle.odi.domain.topology.finder.IOdiDataServerFinder;
import oracle.odi.scripting.odibuilder.JOdiBuilder;


public class DataserverService {
	JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
	
	

	public DataserverInfo createDataServer(DataserverInfo dsInfo) {
		b.type("transaction");
		b.type("topology");
		
//		//create logical schema
//		b.type("logicalschema", b.p("technology", "ORACLE"), b.p("name", dsInfo.getLogicalSchemaName()));
//		b.end();
		//create dataserver and physical schema
		b.type("dataserver", b.p("technology", "ORACLE"), b.p("name", dsInfo.getDataserverName()));
		b.type("jdbcconnection", b.p("driver", "oracle.jdbc.driver.OracleDriver"), b.p("url", dsInfo.getDataserverUrl()));
		b.end();
		b.type("connection", b.p("username", dsInfo.getDsUsername()), b.p("password", dsInfo.getDsPwd()));
		b.end();
		try{
		for(PhysicalSchemaInfo ps: dsInfo.getPhysicalSchemas()){
			b.type("physicalschema", b.p("technology", "ORACLE"), b.p("schemaname", ps.getPhysicalSchemaName()),
					b.p("workschemaname", ps.getPhysicalWorkSchemaName()));
			b.end();
		}
		}catch(Exception e){
			b.end("dataserver");
			b.end("topology");
			b.end("transaction");
			return null;
		}
		b.end("dataserver");
		
//		//create context
//		b.type("context", b.p("name", "Global"), b.p("code", "GLOBAL"));
//		b.type("contextmapping");
//		b.type("contextlogicalschema", b.p("technology", "ORACLE"), b.p("name", dsInfo.getLogicalSchemaName()));
//		b.end();
//		b.type("contextphysicalschema", b.p("technology", "ORACLE"), b.p("dataserver", dsInfo.getDataserverName()),
//				b.p("schemaname", dsInfo.getPhysicalSchemaName()));
//		b.end();
//		b.end("contextmapping");
//		b.end("context");
		
		b.end("topology");
		b.end("transaction");
		return dsInfo;
	}
	public boolean deleteDataServer(String dsName) {
		
		if(getDataServerFinder().findByName(dsName) == null){
			return false;
		}
		b.type("transaction");
		b.type("topology");
		b.type("remove_dataserver", b.p("name", dsName));
		b.end();
		b.end("topology");
		b.end("transaction");
		
		return true;
	}
	
	public DataserverInfo updateDataServer(DataserverInfo dsInfo){
		
		
		b.type("transaction");
		b.type("topology");
		b.type("remove_dataserver", b.p("name", dsInfo.getDataserverName()));
		b.end();
		
		
		// create dataserver and physical schema
		b.type("dataserver", b.p("technology", "ORACLE"), b.p("name", dsInfo.getDataserverName()));
		b.type("jdbcconnection", b.p("driver", "oracle.jdbc.driver.OracleDriver"),
				b.p("url", dsInfo.getDataserverUrl()));
		b.end();
		b.type("connection", b.p("username", dsInfo.getDsUsername()), b.p("password", dsInfo.getDsPwd()));
		b.end();

		for (PhysicalSchemaInfo ps : dsInfo.getPhysicalSchemas()) {
			b.type("physicalschema", b.p("technology", "ORACLE"), b.p("schemaname", ps.getPhysicalSchemaName()),
					b.p("workschemaname", ps.getPhysicalWorkSchemaName()));
			b.end();
		}
		b.end("dataserver");
		
		b.end("topology");
		b.end("transaction");
		return dsInfo;
	}
	public List<DataserverInfo> getAllDataservers(){
		List<DataserverInfo> dsList = new ArrayList<DataserverInfo>();
		Collection<OdiDataServer> dsCollection = getDataServerFinder().findAll(true);
		for(OdiDataServer ds: dsCollection){
			DataserverInfo newds = new DataserverInfo();
			newds.setDataserverName(ds.getName());
			newds.setDataserverUrl(ds.getConnectionSettings().getJdbcUrl());
			newds.setDsUsername(ds.getUsername());
			//newds.setDsPwd(ds.getPassword().toString());
			for(OdiPhysicalSchema physicalSchema: ds.getPhysicalSchemas()){
				//newds.setPhysicalSchemaName(physicalSchema.getName());
				//newds.setPhysicalWorkSchemaName(physicalSchema.getWorkSchemaName());
			}
			dsList.add(newds);
	
		}
		return dsList;
	}
	protected IOdiDataServerFinder getDataServerFinder()
	{
		return (IOdiDataServerFinder)b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiDataServer.class);
	}
}
