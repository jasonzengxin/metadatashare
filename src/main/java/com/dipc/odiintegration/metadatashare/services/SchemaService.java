package com.dipc.odiintegration.metadatashare.services;

import java.sql.SQLIntegrityConstraintViolationException;

import com.dipc.odiintegration.metadatashare.OdiReposGlobalHelper;
import com.dipc.odiintegration.metadatashare.models.Schema;

import oracle.odi.core.persistence.IOdiEntityManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
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

	public void createLogicalAndModel(String dipcSchemaname, String technology) {
		String logicalSchemaName = generateLogicSchemaName(dipcSchemaname);
		String modelName = generateModelName(dipcSchemaname);
		b.type("transaction");
		b.type("design");

		// if the logical schema does not exist, create one.
		if (getLogicalSchemaFinder().findByName(logicalSchemaName) == null) {
			b.type("topology");
			b.type("logicalschema", b.p("technology", technology), b.p("name", logicalSchemaName));
			b.end();
			b.end("topology");

		}
		// if the model does not exist, create one.
		if (getModelFinder().findByCode(modelName) == null) {
			b.type("model", b.p("technology", technology), b.p("logicalschema", logicalSchemaName),
					b.p("name", modelName), b.p("code", modelName));
			b.end("model");
		}

		b.end("design");
		b.end("transaction");
	}

	public void addSchema(Schema schema) {
		if(schema.getApplicationProperties().getDefaultConnection() != null){
			OdiDataServer dataserver  = dataserverService.getDataServerFinder().findByName(schema.getApplicationProperties().getDefaultConnection().getName());
			if(dataserver != null){
				OdiPhysicalSchema oraclePhysicalSchema = new OdiPhysicalSchema(dataserver);
				
				// Set additional information on the schema
				oraclePhysicalSchema.setSchemaName(schema.getApplicationProperties().getName());
				oraclePhysicalSchema.setWorkSchemaName(schema.getApplicationProperties().getWorkSchemaName());
				persistEntity(dataserver);
			}
		}
	}

	protected IOdiLogicalSchemaFinder getLogicalSchemaFinder() {
		return (IOdiLogicalSchemaFinder) b.getOdiInstance().getTransactionalEntityManager()
				.getFinder(OdiLogicalSchema.class);
	}

	protected IOdiModelFinder getModelFinder() {
		return (IOdiModelFinder) b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiModel.class);
	}
	private void persistEntity(final IOdiEntity rEntity) 
	{
		TransactionTemplate transaction = new TransactionTemplate(b.getOdiInstance().getTransactionManager());
		try
		{
			transaction.execute(new TransactionCallbackWithoutResult()
			{
				public void doInTransactionWithoutResult(ITransactionStatus pStatus)
				{
					IOdiEntityManager entityManager = b.getOdiInstance().getTransactionalEntityManager();
					entityManager.persist(rEntity);
				}
			});
		}
		
		//	Ignore integrity constraint violations (entity already exists):
		catch(UncategorizedRepositoryAccessException e) {
			if ( ! (e.getCause() instanceof SQLIntegrityConstraintViolationException) ) {
				throw e;
			}
		}
	}

}
