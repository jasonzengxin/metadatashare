package com.dipc.odiintegration.metadatashare.services;

import com.dipc.odiintegration.metadatashare.OdiReposGlobalHelper;
import com.dipc.odiintegration.metadatashare.models.ExecConfig;
import com.dipc.odiintegration.metadatashare.models.odi.ContextInfo;

import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class ContextService {
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
