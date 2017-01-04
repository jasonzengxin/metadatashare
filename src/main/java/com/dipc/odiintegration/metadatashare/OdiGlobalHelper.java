package com.dipc.odiintegration.metadatashare;

import oracle.odi.scripting.odibuilder.* ;
import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.security.Authentication;
import oracle.odi.domain.topology.OdiTechnology;
import oracle.odi.scripting.ETLHelper;

public class OdiGlobalHelper {

	public static String masterUrl = "jdbc:oracle:thin:@adc2140010.us.oracle.com:1521:ora112";
	public static String masterDriver = "oracle.jdbc.OracleDriver";
	public static String masterUserName = "ODIREP";
	public static String masterPassword = "ODIREP";
	public static String workRepositoryName = "WORK0";
	public static String odiUser = "SUPERVISOR";
	public static String odiPawd = "sunopsis";
		
	private OdiInstance mOdiInstance = null;
	private JOdiBuilder odiBuilder = null;
	private static OdiGlobalHelper globalHelper =null;
	private OdiGlobalHelper(){}
	public static OdiGlobalHelper getInstance(){
		if(globalHelper ==null){
			globalHelper = new OdiGlobalHelper();
			globalHelper.init();
		}
		return globalHelper;
	}
	public void init(){
		//b = new JOdiBuilder();
		if(mOdiInstance == null){
			connectToOdi(masterUrl, masterDriver, masterUserName, masterPassword, workRepositoryName, odiUser, odiPawd);
			odiBuilder = new JOdiBuilder(mOdiInstance);
		}
		
	}

	private void connectToOdi(String url, String driver, String dbUser, String dbPassword, String workRep,
			String odiUser, String odiPassword) {
//		MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(url, driver, dbUser, dbPassword.toCharArray(),
//				new PoolingAttributes());
//		WorkRepositoryDbInfo workInfo = null;
//		workInfo = new WorkRepositoryDbInfo(workRep, new PoolingAttributes());
//	
//		OdiInstanceConfig cfg = new OdiInstanceConfig(masterInfo, workInfo);
//		mOdiInstance = OdiInstance.createInstance(cfg);
//		
//		Authentication auth = mOdiInstance.getSecurityManager().createAuthentication(odiUser,
//				odiPassword.toCharArray());
//		
//		mOdiInstance.getSecurityManager().setGlobalAuthentication(auth);
		OdiBuilder b  = new OdiBuilder();
		b.reposConnect();
		odiBuilder = b;
	}

	public static class OdiBuilder extends JOdiBuilder {
		
		public void reposConnect() {
			type("repository", p("type", "master"), p("technology", "ORACLE"), p("reposid", "1"),
					p("odiuser", odiUser), p("odipassword",odiPawd),
					p("create", "true"));
			type("jdbcconnection", p("driver", masterDriver), p("url", masterUrl));
			end();
			type("connection", p("username", masterUserName), p("password", masterPassword));
			end();
			end("repository");

			type("repository", p("type", "work"), p("technology", "ORACLE"), p("reposid", "2"),
					p("name", workRepositoryName), p("odiuser", odiUser),
					p("odipassword", odiPawd), p("create", "true"));
			type("jdbcconnection", p("driver", "oracle.jdbc.OracleDriver"), p("url", "jdbc:oracle:thin:@adc2140010.us.oracle.com:1521:ora112"));
			end();
			type("connection", p("username", "ODIREP_WRK"), p("password", "ODIREP_WRK"));
			end();
			end("repository");
		}

	}
	public JOdiBuilder getOdiBuilder(){
		return odiBuilder;
	}

}
