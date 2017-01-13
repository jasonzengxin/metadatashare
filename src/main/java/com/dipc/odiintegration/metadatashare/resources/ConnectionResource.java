package com.dipc.odiintegration.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dipc.odiintegration.metadatashare.models.Connection;
import com.dipc.odiintegration.metadatashare.models.odi.DataserverInfo;
import com.dipc.odiintegration.metadatashare.services.DataserverService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/connections")
@Api(value = "/connections")
public class ConnectionResource {

	private DataserverService dataserverService = new DataserverService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new Dataserver according to the new connection")
	@ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
	public Response addConnection(Connection connection) {

		DataserverInfo createdDs =dataserverService.createDataServer(connection);
		return Response.ok().entity(createdDs).build();
	}

	@PUT
	@Path("/{connectionName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing dataserver according to the updated connection")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
			@ApiResponse(code = 404, message = "The corresponding dataserver not found"),
			@ApiResponse(code = 405, message = "Validation exception") })
	public Response updateDataserver(@PathParam("connectionName") String connectionName, Connection connection) {

		DataserverInfo updatedDs = dataserverService.updateDataServer(connection);
		return Response.ok().entity(updatedDs).build();
	}

	@DELETE
	@Path("/{connectionName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete a dataserver according to a removed connection")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
			@ApiResponse(code = 404, message = "Datasever not found") })
	public Response deleteDataserver(
			@ApiParam(value = "Dataserver name to delete", required = true) @PathParam("connectionName") String connectionName) {
		if (dataserverService.deleteDataServer(connectionName)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
