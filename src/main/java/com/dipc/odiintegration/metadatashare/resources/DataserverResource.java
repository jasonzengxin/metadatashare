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
import com.dipc.odiintegration.metadatashare.models.DataserverInfo;
import com.dipc.odiintegration.metadatashare.services.DataserverService;

@Path("/dataservers")
public class DataserverResource {

	private DataserverService ds = new DataserverService();
	
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
	public String deleteDataserver(@PathParam("dataserverName") String dsName) {
		ds.deleteDataServer(dsName);
		return "data server "+ dsName +" is deleted";
		
	}
}
