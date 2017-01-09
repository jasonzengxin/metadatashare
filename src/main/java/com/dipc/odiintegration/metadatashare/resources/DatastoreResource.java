package com.dipc.odiintegration.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dipc.odiintegration.metadatashare.models.DatastoreInfo;
import com.dipc.odiintegration.metadatashare.services.DatastoreService;

import io.swagger.annotations.Api;

@Path("/datastores")
@Api(value = "/datastores")
public class DatastoreResource {

	private DatastoreService ds = new DatastoreService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addDataserver(DatastoreInfo dsInfo) {
		ds.createDataStore(dsInfo);
		return "data server " + dsInfo.getName() + " is created";
	}
	
	@DELETE
	@Path("/{modelName}/{datastoreName}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteDataserver(@PathParam("modelName") String modelName,
			@PathParam("datastoreName") String dsName) {
		if (ds.getDataStoreFinder().findByName(dsName, modelName) == null) {
			return "data store " + dsName + "does not exist";
		}
		ds.deleteDataStore(modelName, dsName);
		return "data store " + dsName + " is deleted";

	}
}
