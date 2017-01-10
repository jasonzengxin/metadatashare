package com.dipc.odiintegration.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dipc.odiintegration.metadatashare.models.Schema;
import com.dipc.odiintegration.metadatashare.models.SchemaInfo;
import com.dipc.odiintegration.metadatashare.services.SchemaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/schemas")
@Api(value = "/schemas")
public class SchemaResource {

	private SchemaService schemaService = new SchemaService();
	

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new physical/logical schemas amd Model according to the new schema in DIPC")
	@ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
	public Response addSchema(Schema schema) {
		schema.getApplicationProperties().setName(schema.getName());
		SchemaInfo updatedSchema = schemaService.addSchema(schema);
		return Response.ok().entity(updatedSchema).build();
	}
	
	@DELETE
	@Path("/{connectionName}/{schemaName}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSchema(@PathParam("modelName") String connectionName,@PathParam("schemaName") String schemaName) {

		schemaService.removeSchema(connectionName, schemaName);
		return "schema " + connectionName + " is created";
	}
}
