package com.dipc.odiintegration.metadatashare.services;

import com.dipc.odiintegration.metadatashare.OdiReposGlobalHelper;
import com.dipc.odiintegration.metadatashare.models.odi.ColumnInfo;
import com.dipc.odiintegration.metadatashare.models.odi.DatastoreInfo;
import com.dipc.odiintegration.metadatashare.models.odi.KeyInfo;

import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;
import oracle.odi.domain.model.OdiDataStore;
import oracle.odi.domain.model.OdiModel;
import oracle.odi.domain.model.finder.IOdiDataStoreFinder;
import oracle.odi.domain.model.finder.IOdiModelFinder;
import oracle.odi.domain.topology.OdiLogicalSchema;
import oracle.odi.domain.topology.finder.IOdiLogicalSchemaFinder;
import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class DatastoreService {
	JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
	
	public void deleteDataStore(final String modelName, final String datastoreName) {
		
		
		new TransactionTemplate(b.getOdiInstance().getTransactionManager()).execute(new ITransactionCallback() {
			public Object doInTransaction(ITransactionStatus pStatus) {
				try {
					OdiDataStore ds = getDataStoreFinder().findByName(datastoreName, modelName);
					
					b.getAdapter().remove(ds);
				} catch (Exception e) {
					return e;
				}
				return null;
			}
		});
		//if there is no data store under the model, remove the model as well.
		if (getDataStoreFinder().findByModel(modelName).size() <= 0) {
			b.type("transaction");
			b.type("design");
			b.type("remove_model", b.p("code", modelName));
			b.end();
			b.end("design");
			b.end("transaction");
		}
	}
	
	public void createDataStore(DatastoreInfo dsInfo) {
		b.type("transaction");
		b.type("design");
		
	    //create data store.    
		b.type("model", b.p("technology", dsInfo.getTechnology()), b.p("logicalschema", dsInfo.getLogicalSchemaName()),
				b.p("name", dsInfo.getModelName()), b.p("code", dsInfo.getModelName()));
		b.type("datastore", b.p("name", dsInfo.getName()), b.p("defaultalias", dsInfo.getName() + "_A"));
		for (ColumnInfo dsInfoLine : dsInfo.getColumnInfoList()) {
			b.type("column", b.p("name", dsInfoLine.getName()), b.p("datatype", dsInfoLine.getDataType()),
					b.p("description", dsInfoLine.getDescription()));
			b.end();
		}
		if (dsInfo.getKeyInfoList() != null) {
			for (KeyInfo keyInfoLine : dsInfo.getKeyInfoList()) {
				b.type("key", b.p("name", keyInfoLine.getKeyName()), b.p("type", "PRIMARY_KEY"), b.p("active", "true"),
						b.p("flowcheckenabled", "true"), b.p("indatabase", "false"),
						b.p("staticcheckenabled", "false"));
				b.type("keycolumn", b.p("name", keyInfoLine.getKeyColumnName()));
				b.end();
				b.end("key");
			}

		}
		b.end("datastore");
		b.end("model");
		b.end("design");
		b.end("transaction");
		
	}
	public IOdiDataStoreFinder getDataStoreFinder()
	{
		return (IOdiDataStoreFinder)b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiDataStore.class);
	}
	protected IOdiLogicalSchemaFinder getLogicalSchemaFinder()
	{
		return (IOdiLogicalSchemaFinder)b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiLogicalSchema.class);
	}
	protected IOdiModelFinder getModelFinder()
	{
		return (IOdiModelFinder)b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiModel.class);
	}
}
