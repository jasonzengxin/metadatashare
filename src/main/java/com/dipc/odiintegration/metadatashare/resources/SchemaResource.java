package com.dipc.odiintegration.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dipc.odiintegration.metadatashare.models.Connection;
import com.dipc.odiintegration.metadatashare.models.Schema;
import com.dipc.odiintegration.metadatashare.services.DataserverService;
import com.dipc.odiintegration.metadatashare.services.SchemaService;

import io.swagger.annotations.Api;

@Path("/schemas")
@Api(value = "/schemas")
public class SchemaResource {

	private SchemaService schemaService = new SchemaService();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addSchema(Schema schema) {
		schema.getApplicationProperties().setName(schema.getName());
		//schemaService.createDataServer(schema);
		return "schema " + schema.getName() + " is created";
	}
}
