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

import com.dipc.odiintegration.metadatashare.models.DataEntity;
import com.dipc.odiintegration.metadatashare.models.odi.DatastoreInfo;
import com.dipc.odiintegration.metadatashare.services.DatastoreService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/dataEntities")
@Api(value = "/dataEntities")
public class DataEntityResource {

	private DatastoreService datastoreService = new DatastoreService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new dataStore according to the new dataEntiy")
	@ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
	public Response addDataStore(DataEntity dataEntity) {
		DatastoreInfo newDatastore = datastoreService.createDataStore(dataEntity);
		return Response.ok().entity(newDatastore).build();
	}

	@PUT
	@Path("/{dataEntityName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing dataserver")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Update an existing dataStore according to the updated data entity"),
			@ApiResponse(code = 404, message = "The corresponding datastore not found"),
			@ApiResponse(code = 405, message = "Validation exception") })
	public Response updateDataserver(@PathParam("dataEntityName") String dsName, DataEntity dataEntity) {

		DatastoreInfo updatedDs = datastoreService.updateDataStore(dataEntity);
		return Response.ok().entity(updatedDs).build();
	}

	@DELETE
	@Path("/{dataEntityName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete a datastore according to a removed data entity in DIPC")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
			@ApiResponse(code = 404, message = "Datastore not found") })
	public Response deleteDataStore(
			@ApiParam(value = "Schema name contains the data entity", required = true) @PathParam("schemaName") String schemaName,
			@ApiParam(value = "Datastore name to delete", required = true) @PathParam("dataEntityName") String dataEntityName) {
		if (datastoreService.deleteDataStore(schemaName,dataEntityName)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
