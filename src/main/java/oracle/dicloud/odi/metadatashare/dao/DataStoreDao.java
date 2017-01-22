package oracle.dicloud.odi.metadatashare.dao;

import oracle.dicloud.odi.metadatashare.OdiReposGlobalHelper;
import oracle.dicloud.odi.metadatashare.models.DataEntity;
import oracle.dicloud.odi.metadatashare.models.odi.ColumnInfo;
import oracle.dicloud.odi.metadatashare.models.odi.DatastoreInfo;
import oracle.dicloud.odi.metadatashare.models.odi.KeyInfo;
import oracle.dicloud.odi.metadatashare.services.TransformService;
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

public class DataStoreDao {
	JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
	private TransformService transform = new TransformService();

	/**
	 * Update an existing dataserver according to a updated connection
	 * 
	 * @param connect
	 */
	public DatastoreInfo updateDataStore(DataEntity dataEntity) {
		DatastoreInfo updatedDataServer = null;
		String modelName = dataEntity.getApplicationProperties().getParentSchema().getName();

		OdiDataStore dataStore = getDataStoreFinder().findByName(dataEntity.getName(), modelName);
		if (dataStore != null) {
			updatedDataServer = updateDataStore(transform.fromDataEntityToDatastore(dataEntity));
		}
		return updatedDataServer;
	}

	public DatastoreInfo updateDataStore(final DatastoreInfo dsInfo) {

		// remove the old dataserver
		new TransactionTemplate(b.getOdiInstance().getTransactionManager()).execute(new ITransactionCallback() {
			public Object doInTransaction(ITransactionStatus pStatus) {
				try {
					OdiDataStore ds = getDataStoreFinder().findByName(dsInfo.getName(), dsInfo.getModelName());
					b.getAdapter().remove(ds);
				} catch (Exception e) {
					return e;
				}
				return null;
			}
		});

		// create dataserver and physical schema
		b.type("transaction");
		b.type("design");

		// create data store.
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
		return dsInfo;
	}

	public DatastoreInfo createDataStore(DataEntity dataEntity) {
		DatastoreInfo newDatastore = null;
		String modelName = transform
				.generateModelName(dataEntity.getApplicationProperties().getParentSchema().getName());

		OdiDataStore dataStore = getDataStoreFinder().findByName(dataEntity.getName(), modelName);
		if (dataStore == null) {
			newDatastore = createDataStore(transform.fromDataEntityToDatastore(dataEntity));
		}
		return newDatastore;
	}

	public DatastoreInfo createDataStore(DatastoreInfo dsInfo) {
		b.type("transaction");
		b.type("design");

		// create data store.
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
		return dsInfo;
	}

	public boolean deleteDataStore(String schemaName, final String datastoreName) {
		final String modelName = transform.generateModelName(schemaName);
		final OdiDataStore ds = getDataStoreFinder().findByName(datastoreName, modelName);
		if(ds == null){
			return false;
		}
		new TransactionTemplate(b.getOdiInstance().getTransactionManager()).execute(new ITransactionCallback() {
			public Object doInTransaction(ITransactionStatus pStatus) {
				try {
					b.getAdapter().remove(ds);
				} catch (Exception e) {
					return e;
				}
				return null;
			}
		});
		return true;
//		// if there is no data store under the model, remove the model as well.
//		if (getDataStoreFinder().findByModel(modelName).size() <= 0) {
//			b.type("transaction");
//			b.type("design");
//			b.type("remove_model", b.p("code", modelName));
//			b.end();
//			b.end("design");
//			b.end("transaction");
//		}
	}

	public IOdiDataStoreFinder getDataStoreFinder() {
		return (IOdiDataStoreFinder) b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiDataStore.class);
	}

	protected IOdiLogicalSchemaFinder getLogicalSchemaFinder() {
		return (IOdiLogicalSchemaFinder) b.getOdiInstance().getTransactionalEntityManager()
				.getFinder(OdiLogicalSchema.class);
	}

	protected IOdiModelFinder getModelFinder() {
		return (IOdiModelFinder) b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiModel.class);
	}
}
