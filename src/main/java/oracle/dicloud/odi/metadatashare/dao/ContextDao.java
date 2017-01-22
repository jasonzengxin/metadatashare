package oracle.dicloud.odi.metadatashare.dao;

import oracle.dicloud.odi.metadatashare.OdiReposGlobalHelper;
import oracle.dicloud.odi.metadatashare.models.ExecConfig;
import oracle.dicloud.odi.metadatashare.models.odi.ContextInfo;
import oracle.dicloud.odi.metadatashare.services.TransformService;
import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class ContextDao {
	JOdiBuilder b = OdiReposGlobalHelper.getInstance().getOdiBuilder();
	private TransformService transform = new TransformService();

	public ContextInfo addContext(ExecConfig config) {

		ContextInfo contextInfo = transform.fromExecConifgToContext(config);

		b.type("transaction");
		b.type("topology");
		b.type("context", b.p("name", config.getName()), b.p("code", config.getName()));
		b.type("contextmapping");
		b.type("contextlogicalschema", b.p("technology", contextInfo.getTechnology()),
				b.p("name", contextInfo.getLogicalSchemaName()));
		b.end();
		b.type("contextphysicalschema", b.p("technology", contextInfo.getTechnology()),
				b.p("dataserver", contextInfo.getDataServerName()),
				b.p("schemaname", contextInfo.getPhysicalSchemaName()));
		b.end();
		b.end("contextmapping");
		b.end("context");
		b.end("topology");

		b.end("transaction");
		return contextInfo;
	}

}
