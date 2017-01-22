package oracle.dicloud.odi.metadatashare.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import oracle.dicloud.odi.metadatashare.dao.ContextDao;
import oracle.dicloud.odi.metadatashare.models.ExecConfig;
import oracle.dicloud.odi.metadatashare.models.odi.ContextInfo;

@Path("/exec_configurations")
@Api(value = "/exec_configurations")
public class ExecConfigResource {

	private ContextDao contextService = new ContextDao();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new context according to the new exec_configuration")
	@ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
	public Response addDataStore(ExecConfig execConfig) {
		ContextInfo contextInfo = contextService.addContext(execConfig);
		return Response.ok().entity(contextInfo).build();
	}
}
