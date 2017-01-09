package com.dipc.odiintegration.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dipc.odiintegration.metadatashare.models.Connection;
import com.dipc.odiintegration.metadatashare.services.DataserverService;

import io.swagger.annotations.Api;

@Path("/connections")
@Api(value = "/connections")
public class ConnectionResource {
	
	private DataserverService dataserverService = new DataserverService();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addConnection(Connection connection) {
	    connection.getApplicationProperties().setName(connection.getName());
		dataserverService.createDataServer(connection);
		return "data server " + connection.getName() + " is created";
	}
}
