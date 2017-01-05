package com.dipc.odiintegration.metadatashare.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import com.dipc.odiintegration.metadatashare.models.DataserverInfo;
import com.dipc.odiintegration.metadatashare.services.DataserverService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiResponse;
@Path("/dataservers")
@Api(value = "/dataservers")
public class DataserverResource {

	private DataserverService ds = new DataserverService();
	
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = " all dataservers",
    notes = "Multiple dataservers information can be provided with comma seperated strings",
    response = DataserverInfo.class,
    responseContainer = "List")
	public List<DataserverInfo> getDataservers(){
		return ds.getAllDataservers();
	}
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addDataserver(DataserverInfo dsInfo){
    	ds.createDataServer(dsInfo);
    	return "data server "+ dsInfo.getDataserverName() +" is created";
    }
    
    @PUT
    @Path("/{dataserverName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public String updateDataserver(@PathParam("dataserverName") String dsName,DataserverInfo dsInfo){
    	dsInfo.setDataserverName(dsName);
    	ds.createDataServer(dsInfo);
    	return "data server "+ dsName +" is update";
    }
    
    
    @DELETE 
	@Path("/{dataserverName}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Deletes a dataserver")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid Name supplied"),
            @ApiResponse(code = 404, message = "Datasever not found") })
	public Response deleteDataserver( @ApiParam(value = "Dataserver name to delete", required = true) @PathParam("dataserverName") String dsName) {
		if (ds.deleteDataServer(dsName)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
