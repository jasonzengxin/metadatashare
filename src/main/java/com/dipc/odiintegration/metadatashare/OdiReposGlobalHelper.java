package com.dipc.odiintegration.metadatashare;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.security.Authentication;
import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class OdiReposGlobalHelper {

	public static String masterUrl = "jdbc:oracle:thin:@192.168.56.101:1521:orcl";
	public static String masterDriver = "oracle.jdbc.OracleDriver";
	public static String masterUserName = "ODIREP";
	public static String masterPassword = "odirep";
	public static String workRepositoryName = "WORKREP1";
	public static String odiUser = "SUPERVISOR";
	public static String odiPawd = "welcome1";
		
	private OdiInstance mOdiInstance = null;
	private JOdiBuilder odiBuilder = null;
	private static OdiReposGlobalHelper globalHelper =null;
	private OdiReposGlobalHelper(){}
	public static OdiReposGlobalHelper getInstance(){
		if(globalHelper ==null){
			globalHelper = new OdiReposGlobalHelper();
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
		MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(url, driver, dbUser, dbPassword.toCharArray(),
				new PoolingAttributes());
		WorkRepositoryDbInfo workInfo = null;
		workInfo = new WorkRepositoryDbInfo(workRep, new PoolingAttributes());
		OdiInstanceConfig cfg = new OdiInstanceConfig(masterInfo, workInfo);
		mOdiInstance = OdiInstance.createInstance(cfg);
		Authentication auth = mOdiInstance.getSecurityManager().createAuthentication(odiUser,
				odiPassword.toCharArray());
		mOdiInstance.getSecurityManager().setGlobalAuthentication(auth);
	}
	
	public JOdiBuilder getOdiBuilder(){
		return odiBuilder;
	}

}
