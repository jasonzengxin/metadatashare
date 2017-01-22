package oracle.dicloud.odi.metadatashare.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import oracle.dicloud.odi.metadatashare.ErrorMessageWrapper;
import oracle.dicloud.odi.metadatashare.NameConflictException;
import oracle.dicloud.odi.metadatashare.OdiReposGlobalHelper;
import oracle.dicloud.odi.metadatashare.models.Connection;
import oracle.dicloud.odi.metadatashare.models.odi.DataserverInfo;
import oracle.dicloud.odi.metadatashare.models.odi.PhysicalSchemaInfo;
import oracle.dicloud.odi.metadatashare.services.TransformService;
import oracle.odi.domain.topology.OdiDataServer;
import oracle.odi.domain.topology.OdiPhysicalSchema;
import oracle.odi.domain.topology.finder.IOdiDataServerFinder;
import oracle.odi.scripting.odibuilder.JOdiBuilder;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DataServerDao {

	private final static Logger logger = Logger.getLogger(DataServerDao.class);
	
	private TransformService transform = new TransformService();

	/**
	 * Create a new dataserver according to a new connection
	 * 
	 * @param connect
	 */
	public Response createDataServer(Connection connect) {
		DataserverInfo newDataserver = null;
		OdiDataServer dataServer = getDataServerFinder().findByName(connect.getName());
		if (dataServer != null) {
			logger.warn("A data server with the same name " + connect.getName() + " has already existed");
			return Response.status(Response.Status.CONFLICT).entity(new NameConflictException(connect.getName()))
					.build();
		} else {
			try {
				newDataserver = createDataServer(transform.fromConnectionToDataserver(connect));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(new ErrorMessageWrapper(e.getMessage())).build();
			}
		}
		return Response.ok(newDataserver, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Update an existing dataserver according to a updated connection
	 * 
	 * @param connect
	 */
	public DataserverInfo updateDataServer(Connection connect) {
		DataserverInfo updatedDataServer = null;
		OdiDataServer dataServer = getDataServerFinder().findByName(connect.getName());
		if (dataServer != null) {
			updatedDataServer = updateDataServer(transform.fromConnectionToDataserver(connect));
		}
		return updatedDataServer;
	}

	public boolean deleteDataServer(String connectionName) {
		JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
		String dataserverName = connectionName;
		if (getDataServerFinder().findByName(dataserverName) == null) {
			return false;
		}
		b.type("transaction");
		b.type("topology");
		b.type("remove_dataserver", b.p("name", dataserverName));
		b.end();
		b.end("topology");
		b.end("transaction");
		return true;
	}

	public DataserverInfo createDataServer(DataserverInfo dsInfo) {
		JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
		b.type("transaction");
		b.type("topology");

		// create dataserver and physical schema
		b.type("dataserver", b.p("technology", dsInfo.getTechnology()), b.p("name", dsInfo.getDataserverName()));
		b.type("jdbcconnection", b.p("driver", "oracle.jdbc.driver.OracleDriver"),
				b.p("url", dsInfo.getDataserverUrl()));
		b.end();
		b.type("connection", b.p("username", dsInfo.getDsUsername()), b.p("password", dsInfo.getDsPwd()));
		b.end();
		try {
			for (PhysicalSchemaInfo ps : dsInfo.getPhysicalSchemas()) {
				b.type("physicalschema", b.p("technology", "ORACLE"), b.p("schemaname", ps.getPhysicalSchemaName()),
						b.p("workschemaname", ps.getPhysicalWorkSchemaName()));
				b.end();
			}
		} catch (Exception e) {
			b.end("dataserver");
			b.end("topology");
			b.end("transaction");
			return null;
		}
		b.end("dataserver");

		// //create context
		// b.type("context", b.p("name", "Global"), b.p("code", "GLOBAL"));
		// b.type("contextmapping");
		// b.type("contextlogicalschema", b.p("technology", "ORACLE"),
		// b.p("name", dsInfo.getLogicalSchemaName()));
		// b.end();
		// b.type("contextphysicalschema", b.p("technology", "ORACLE"),
		// b.p("dataserver", dsInfo.getDataserverName()),
		// b.p("schemaname", dsInfo.getPhysicalSchemaName()));
		// b.end();
		// b.end("contextmapping");
		// b.end("context");

		b.end("topology");
		b.end("transaction");
		return dsInfo;
	}

	public DataserverInfo updateDataServer(DataserverInfo dsInfo) {
		JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
		// remove the old dataserver
		b.type("transaction");
		b.type("topology");
		b.type("remove_dataserver", b.p("name", dsInfo.getDataserverName()));
		b.end();
		b.end("topology");
		b.end("transaction");

		// create dataserver and physical schema
		b.type("transaction");
		b.type("topology");
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

	public List<DataserverInfo> getAllDataservers() {
		List<DataserverInfo> dsList = new ArrayList<DataserverInfo>();
		Collection<OdiDataServer> dsCollection = getDataServerFinder().findAll(true);
		for (OdiDataServer ds : dsCollection) {
			DataserverInfo newds = new DataserverInfo();
			newds.setDataserverName(ds.getName());
			newds.setDataserverUrl(ds.getConnectionSettings().getJdbcUrl());
			newds.setDsUsername(ds.getUsername());
			// newds.setDsPwd(ds.getPassword().toString());
			List<PhysicalSchemaInfo> schemaList = new ArrayList<PhysicalSchemaInfo>();
			for (OdiPhysicalSchema physicalSchema : ds.getPhysicalSchemas()) {
				PhysicalSchemaInfo schemaInfo = new PhysicalSchemaInfo();
				schemaInfo.setPhysicalSchemaName(physicalSchema.getName());
				schemaInfo.setPhysicalWorkSchemaName(physicalSchema.getWorkSchemaName());
				schemaList.add(schemaInfo);
				// newds.setPhysicalSchemaName(physicalSchema.getName());
				// newds.setPhysicalWorkSchemaName(physicalSchema.getWorkSchemaName());
			}
			newds.setPhysicalSchemas(schemaList);
			dsList.add(newds);

		}
		return dsList;
	}

	protected IOdiDataServerFinder getDataServerFinder() {
		JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
		return (IOdiDataServerFinder) b.getOdiInstance().getTransactionalEntityManager().getFinder(OdiDataServer.class);
	}
}
