package oracle.dicloud.odi.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import oracle.dicloud.odi.metadatashare.dao.SchemaDao;
import oracle.dicloud.odi.metadatashare.models.Schema;
import oracle.dicloud.odi.metadatashare.models.SchemaInfo;

@Path("/schemas")
@Api(value = "/schemas")
public class SchemaResource {

	private SchemaDao schemaService = new SchemaDao();
	

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new physical/logical schemas and Model according to the new schema in DIPC")
	@ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
	public Response addSchema(Schema schema) {

		SchemaInfo updatedSchema = schemaService.addSchema(schema);
		return Response.ok().entity(updatedSchema).build();
	}
	
	@PUT
	@Path("/{schemaName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing physical/logical schema and context according to the updated schema in DIPC")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Update an existing dataStore according to the updated data entity"),
			@ApiResponse(code = 404, message = "The corresponding datastore not found"),
			@ApiResponse(code = 405, message = "Validation exception") })
	public Response updateSchema(@PathParam("schemaName") String dsName, Schema schema) {
		
		SchemaInfo updatedDs = schemaService.updateSchema(schema);
		return Response.ok().entity(updatedDs).build();
	} 
	
	@DELETE
	@Path("/{connectionName}/{schemaName}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete the existing physical/logical schema and context according to a removed schema in DIPC")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
			@ApiResponse(code = 404, message = "Physical schema is not found") })
	public String deleteSchema(@PathParam("connectionName") String connectionName,@PathParam("schemaName") String schemaName) {

		schemaService.removeSchema(connectionName, schemaName);
		return "schema " + connectionName + " is created";
	}
}
