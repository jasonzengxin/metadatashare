package oracle.dicloud.odi.metadatashare.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import oracle.dicloud.odi.metadatashare.OdiReposGlobalHelper;
import oracle.dicloud.odi.metadatashare.models.Connection;
import oracle.dicloud.odi.metadatashare.models.Schema;
import oracle.dicloud.odi.metadatashare.models.SchemaInfo;
import oracle.dicloud.odi.metadatashare.services.TransformService;
import oracle.odi.core.persistence.IOdiEntityManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.ITransactionCallback;
import oracle.odi.core.persistence.transaction.support.TransactionCallbackWithoutResult;
import oracle.odi.core.persistence.transaction.support.TransactionTemplate;
import oracle.odi.core.repository.UncategorizedRepositoryAccessException;
import oracle.odi.domain.IOdiEntity;
import oracle.odi.domain.model.OdiModel;
import oracle.odi.domain.model.finder.IOdiModelFinder;
import oracle.odi.domain.topology.OdiContextualSchemaMapping;
import oracle.odi.domain.topology.OdiDataServer;
import oracle.odi.domain.topology.OdiLogicalSchema;
import oracle.odi.domain.topology.OdiPhysicalSchema;
import oracle.odi.domain.topology.finder.IOdiContextualSchemaMappingFinder;
import oracle.odi.domain.topology.finder.IOdiLogicalSchemaFinder;
import oracle.odi.domain.topology.finder.IOdiPhysicalSchemaFinder;
import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class SchemaDao {
	private static OdiPhysicalSchema updatedSchema = null;
	JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
	private DataServerDao dataserverService = new DataServerDao();
	private TransformService transform = new TransformService();

	private final String globalContextName = "GLOBAL";


	public void createLogicalAndModel(Schema schema) {

		String logicalSchemaName = transform.generateLogicSchemaName(schema.getName());
		String modelName = transform.generateModelName(schema.getName());
		SchemaInfo schemaInfo = schema.getApplicationProperties();

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

		// default EXEC CONFIG in DIPC is corresponding to the "GLOBAL" context.
		b.type("topology");
		b.type("context", b.p("name", "GLOBAL"), b.p("code", "GLOBAL"));
		b.type("contextmapping");
		b.type("contextlogicalschema", b.p("technology", schemaInfo.getPlateform()), b.p("name", logicalSchemaName));
		b.end();
		b.type("contextphysicalschema",
				b.p("technology", schemaInfo.getDefaultConnection().getApplicationProperties().getPlateform()),
				b.p("dataserver", schemaInfo.getDefaultConnection().getName()), b.p("schemaname", schema.getName()));
		b.end();
		b.end("contextmapping");
		b.end("context");
		b.end("topology");

		b.end("design");
		b.end("transaction");
	}

	public SchemaInfo addSchema(Schema schema) {
		SchemaInfo schemaInfo = schema.getApplicationProperties();

		// If the default connection is configured, add the physical schema to
		// the dataserver.
		if (schemaInfo.getDefaultConnection() != null) {
			OdiDataServer dataserver = dataserverService.getDataServerFinder()
					.findByName(schemaInfo.getDefaultConnection().getName());

			// If the dataserver already exist, add the physical schema
			if (dataserver != null) {
				OdiPhysicalSchema oraclePhysicalSchema = new OdiPhysicalSchema(dataserver);

				// Set additional information on the schema
				oraclePhysicalSchema.setSchemaName(schema.getName());
				oraclePhysicalSchema.setWorkSchemaName(schema.getApplicationProperties().getWorkSchemaName());
				updatedSchema = oraclePhysicalSchema;
				if (findPhysicalSchemaByName(dataserver, schema.getName()) == null) {
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

				// persistEntity(dataserver);

			} else {// create dataserver if it's not already exist. schema will
					// be added as well
				dataserverService.createDataServer(schema.getApplicationProperties().getDefaultConnection());
			}
			// create logical schema and model for this schema
			createLogicalAndModel(schema);
		}

		return schemaInfo;
	}

	public void removeSchema(String connectionName, String schemaName) {

		// remove the model and logical schema which is corresponding to this
		// schema
		// b.type("transaction");
		// b.type("design");
		// remove_model",b.p("logicalschema", ""),b.p("name", ""),b.p("code",
		// ""));
		// b.end();
		// b.end("design");
		// b.end("transaction");
		//
		// b.type("transaction");
		// b.type("topology");
		// b.type("remove_logicalschema", b.p("name", ""));
		// b.end();
		// b.end("topology");
		// b.end("transaction");

	}

	protected void removePhysicalSchema(String schemaName) {
		// remove the corresponding to physical schema in ODI
		@SuppressWarnings("unchecked")
		ArrayList<OdiPhysicalSchema> schemas = (ArrayList<OdiPhysicalSchema>) getPhysicalSchemaFinder().findAll();
		for (OdiPhysicalSchema physicalSchema : schemas) {
			if (physicalSchema.getSchemaName().equals(schemaName)) {
				OdiDataServer dataserver = physicalSchema.getDataServer();
				dataserver.removePhysicalSchema(physicalSchema);
				persistEntity(physicalSchema);
			}
		}
	}

	protected void createPhysicalSchema(Schema schema) {

		OdiDataServer dataserver = dataserverService.getDataServerFinder()
				.findByName(schema.getApplicationProperties().getDefaultConnection().getName());

		if (dataserver != null) {
			OdiPhysicalSchema oraclePhysicalSchema = new OdiPhysicalSchema(dataserver);

			// Set additional information on the schema
			oraclePhysicalSchema.setSchemaName(schema.getName());
			oraclePhysicalSchema.setWorkSchemaName(schema.getApplicationProperties().getWorkSchemaName());
			updatedSchema = oraclePhysicalSchema;
			if (findPhysicalSchemaByName(dataserver, schema.getName()) == null) {
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
		}

	}

	public SchemaInfo updateSchema(Schema schema) {
		SchemaInfo schemaInfo = schema.getApplicationProperties();

		// update physical schema in ODI
		Connection defaultConnect = schema.getApplicationProperties().getDefaultConnection();
		removePhysicalSchema(schema.getName());
		createPhysicalSchema(schema);

		// update context
		OdiContextualSchemaMapping contextMapping = getContextualSchemaMappingFinder()
				.findByLogicalSchema(transform.generateLogicSchemaName(schema.getName()), globalContextName);
		OdiDataServer dataServer = contextMapping.getPhysicalSchema().getDataServer();
		// if the default connection is changed, the physical-logical
		// relationship needs to be updated.
		if (!defaultConnect.getName().equals(dataServer.getName())) {
			// if the dataserver and physical Schema existed already
			OdiDataServer dataserver = dataserverService.getDataServerFinder().findByName(defaultConnect.getName());
			if (dataserver != null) {
				OdiPhysicalSchema defaultSchema = dataserver.getDefaultPhysicalSchema();
				if (defaultSchema != null) {
					contextMapping.setPhysicalSchema(defaultSchema);
				}
			}
		}

		return schemaInfo;
	}

	protected OdiPhysicalSchema findPhysicalSchemaByName(OdiDataServer dataServer, String physicalSchemaName) {
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

	protected IOdiContextualSchemaMappingFinder getContextualSchemaMappingFinder() {
		return (IOdiContextualSchemaMappingFinder) b.getOdiInstance().getTransactionalEntityManager()
				.getFinder(OdiContextualSchemaMapping.class);
	}

	protected IOdiPhysicalSchemaFinder getPhysicalSchemaFinder() {
		return (IOdiPhysicalSchemaFinder) b.getOdiInstance().getTransactionalEntityManager()
				.getFinder(OdiPhysicalSchema.class);
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
