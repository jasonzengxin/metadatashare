package oracle.dicloud.odi.metadatashare.resources;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import oracle.dicloud.odi.metadatashare.Constants;
import oracle.dicloud.odi.metadatashare.OdiReposGlobalHelper;
import oracle.dicloud.odi.metadatashare.dao.DataServerDao;
import oracle.dicloud.odi.metadatashare.models.Connection;
import oracle.dicloud.odi.metadatashare.models.odi.DataserverInfo;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(Constants.CONNECTION_RESOURCE_PATH)
@Api(value = "Convert a connection to dataserver")
public class ConnectionResource {

	private DataServerDao dataserverDao = new DataServerDao();
	private ServletContext context;

	@Context
	public void setServletContext(ServletContext context) {

		this.context = context;
		String iMasterJndi = this.context.getInitParameter("jndiMasterRepos");
		String iWorkRepName = this.context.getInitParameter("workReposName");
		String odiUsername = this.context.getInitParameter("odiUsername");
		String odiPassword = this.context.getInitParameter("odiPassword");
		OdiReposGlobalHelper.getInstance().connectToOdi(iMasterJndi, iWorkRepName,odiUsername,odiPassword);
	}

	@POST
	@ApiOperation(value = "Create a new Dataserver according to the new connection", notes = "Convert Dicloud Connection", response = DataserverInfo.class)
	@ApiResponses(value = { @ApiResponse(code = Constants.CREATED, message = "A dataserver is created"),
			@ApiResponse(code = Constants.INTERNAL_SERVER_ERROR, message = "Unexpected error"),
			@ApiResponse(code = Constants.CONFLICT, message = "Name already existed") })
	public Response addConnection(Connection connection) {
		return dataserverDao.createDataServer(connection);
	}

	@PUT
	@Path("/{connectionName}")
	@ApiOperation(value = "Update an existing dataserver according to the updated connection")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
			@ApiResponse(code = 404, message = "The corresponding dataserver not found"),
			@ApiResponse(code = 405, message = "Validation exception") })
	public Response updateDataserver(@PathParam("connectionName") String connectionName, Connection connection) {

		DataserverInfo updatedDs = dataserverDao.updateDataServer(connection);
		return Response.ok().entity(updatedDs).build();
	}

	@DELETE
	@Path("/{connectionName}")
	@ApiOperation(value = "Delete a dataserver according to a removed connection")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
			@ApiResponse(code = 404, message = "Dataserver not found") })
	public Response deleteDataserver(
			@ApiParam(value = "Dataserver name to delete", required = true) @PathParam("connectionName") String connectionName) {
		if (dataserverDao.deleteDataServer(connectionName)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
