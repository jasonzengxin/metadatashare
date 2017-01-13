package com.dipc.odiintegration.metadatashare.services;

import java.sql.SQLIntegrityConstraintViolationException;

import com.dipc.odiintegration.metadatashare.OdiReposGlobalHelper;
import com.dipc.odiintegration.metadatashare.models.Schema;
import com.dipc.odiintegration.metadatashare.models.SchemaInfo;

import oracle.odi.core.persistence.IOdiEntityManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionCallbackWithoutResult;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;
import oracle.odi.core.repository.UncategorizedRepositoryAccessException;
import oracle.odi.domain.IOdiEntity;
import oracle.odi.domain.model.OdiModel;
import oracle.odi.domain.model.finder.IOdiModelFinder;
import oracle.odi.domain.topology.OdiDataServer;
import oracle.odi.domain.topology.OdiLogicalSchema;
import oracle.odi.domain.topology.OdiPhysicalSchema;
import oracle.odi.domain.topology.finder.IOdiLogicalSchemaFinder;
import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class SchemaService {
	private static OdiPhysicalSchema updatedSchema = null;
	JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
	private DataserverService dataserverService = new DataserverService();

	private final String logicSchemaPrefix = "LS";
	private final String modelPrefix = "MOD";

	protected String generateLogicSchemaName(String dipcSchemaname) {
		return logicSchemaPrefix + "_" + dipcSchemaname;
	}

	protected String generateModelName(String dipcSchemaname) {
		return modelPrefix + "_" + dipcSchemaname;
	}

	public void createLogicalAndModel(Schema  schema) {
		
		String logicalSchemaName = generateLogicSchemaName(schema.getName());
		String modelName = generateModelName(schema.getName());
		SchemaInfo schemaInfo =  schema.getApplicationProperties();
		
		b.type("transaction");
		b.type("design");

		// if the logical schema does not exist, create one.
		if (getLogicalSchemaFinder().findByName(logicalSchemaName) == null) {
			b.type("topology");
			b.type("logicalschema", b.p("technology", schemaInfo.getPlateform()), b.p("name", logicalSchemaName));
			b.end();
			b.end("topology");

		}
		// if the model does not exist, create one.
		if (getModelFinder().findByCode(modelName) == null) {
			b.type("model", b.p("technology", schemaInfo.getPlateform()), b.p("logicalschema", logicalSchemaName),
					b.p("name", modelName), b.p("code", modelName));
			b.end("model");
		}

		//default EXEC CONFIG in DIPC is corresponding to the "GLOBAL" context.
		b.type("topology");
		b.type("context", b.p("name", "Global"), b.p("code", "GLOBAL"));
		b.type("contextmapping");
		b.type("contextlogicalschema", b.p("technology", schemaInfo.getPlateform()), b.p("name", logicalSchemaName));
		b.end();
		b.type("contextphysicalschema", b.p("technology", schemaInfo.getDefaultConnection().getApplicationProperties().getPlateform()), b.p("dataserver", schemaInfo.getDefaultConnection().getName()),
				b.p("schemaname", schema.getName()));
		b.end();
		b.end("contextmapping");
		b.end("context");
		b.end("topology");
		
		b.end("design");
		b.end("transaction");
	}

	public SchemaInfo addSchema(Schema schema) {
		SchemaInfo schemaInfo =  schema.getApplicationProperties();
	

		//If the default connection is configured, add the physical schema to the dataserver.
		if (schemaInfo.getDefaultConnection() != null) {
			 OdiDataServer dataserver = dataserverService.getDataServerFinder()
					.findByName(schemaInfo.getDefaultConnection().getName());
			
			//If the dataserver already exist, add the physical schema
			if (dataserver != null) {
				OdiPhysicalSchema oraclePhysicalSchema = new OdiPhysicalSchema(dataserver);

				// Set additional information on the schema
				oraclePhysicalSchema.setSchemaName(schema.getName());
				oraclePhysicalSchema.setWorkSchemaName(schema.getApplicationProperties().getWorkSchemaName());
				updatedSchema = oraclePhysicalSchema;
				if(findPhysicalSchemaByName(dataserver,schema.getName() ) == null){
					b.startTransaction();
					b.doInTransaction(new ITransactionCallback() {
						@Override
						public Object doInTransaction(ITransactionStatus pStatus) {
							b.getAdapter().persist(updatedSchema);
							return null;
						}
					});
					b.endTransaction();
				}
			
				//persistEntity(dataserver);
				
			} else {// create dataserver if it's not already exist. schema will be added as well
				dataserverService.createDataServer(schema.getApplicationProperties().getDefaultConnection());
			}
		    // create logical schema and model for this schema
			createLogicalAndModel(schema);
		}

		
		return schemaInfo;
	}

	public void removeSchema(String connectionName, String schemaName) {
		OdiDataServer dataserver = dataserverService.getDataServerFinder().findByName(connectionName);
		
		if(dataserver != null){
			for (OdiPhysicalSchema physicalSchema : dataserver.getPhysicalSchemas()) {
				if (physicalSchema.getSchemaName().equals(schemaName)) {
					dataserver.removePhysicalSchema(physicalSchema);
				}
			}
		}
		
	}

	protected OdiPhysicalSchema findPhysicalSchemaByName(OdiDataServer dataServer, String physicalSchemaName){
		for (OdiPhysicalSchema physicalSchema : dataServer.getPhysicalSchemas()) {
			if (physicalSchema.getSchemaName().equals(physicalSchemaName)) {
				return physicalSchema;
			}
		}
		return null;
	}


	protected IOdiLogicalSchemaFinder getLogicalSchemaFinder() {
		return (IOdiLogicalSchemaFinder) b.getOdiInstance().getTransactionalEntityManager()
				.getFinder(OdiLogicalSchema.class);
	}

	protected IOdiModelFinder getModelFinder() {
		return (IOdiModelFinder) b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiModel.class);
	}

	private void persistEntity(final IOdiEntity rEntity) {
		TransactionTemplate transaction = new TransactionTemplate(b.getOdiInstance().getTransactionManager());
		try {
			transaction.execute(new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(ITransactionStatus pStatus) {
					IOdiEntityManager entityManager = b.getOdiInstance().getTransactionalEntityManager();
					entityManager.persist(rEntity);
				}
			});
		}

		// Ignore integrity constraint violations (entity already exists):
		catch (UncategorizedRepositoryAccessException e) {
			if (!(e.getCause() instanceof SQLIntegrityConstraintViolationException)) {
				throw e;
			}
		}
	}

}
