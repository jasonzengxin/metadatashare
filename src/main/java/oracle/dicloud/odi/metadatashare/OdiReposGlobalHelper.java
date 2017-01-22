package oracle.dicloud.odi.metadatashare;

import oracle.odi.core.OdiInstance;
import oracle.odi.scripting.odibuilder.JOdiBuilder;

public class OdiReposGlobalHelper {

	// public static String masterUrl =
	// "jdbc:oracle:thin:@192.168.56.101:1521:orcl";
	// public static String masterDriver = "oracle.jdbc.OracleDriver";
	// public static String masterUserName = "ODIREP";
	// public static String masterPassword = "odirep";
	// public static String workRepositoryName = "WORKREP1";
	// public static String odiUser = "SUPERVISOR";
	// public static String odiPawd = "welcome1";

	private OdiInstance mOdiInstance = null;
	private JOdiBuilder odiBuilder = null;
	private static OdiReposGlobalHelper globalHelper = null;

	private OdiReposGlobalHelper() {
	}

	public static OdiReposGlobalHelper getInstance() {
		if (globalHelper == null) {
			globalHelper = new OdiReposGlobalHelper();
		}
		return globalHelper;
	}


	// private void connectToOdi(String url, String driver, String dbUser,
	// String dbPassword, String workRep,
	// String odiUser, String odiPassword) {
	// MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(url,
	// driver, dbUser, dbPassword.toCharArray(),
	// new PoolingAttributes());
	// WorkRepositoryDbInfo workInfo = null;
	// workInfo = new WorkRepositoryDbInfo(workRep, new PoolingAttributes());
	// OdiInstanceConfig cfg = new OdiInstanceConfig(masterInfo, workInfo);
	// mOdiInstance = OdiInstance.createInstance(cfg);
	// Authentication auth =
	// mOdiInstance.getSecurityManager().createAuthentication(odiUser,
	// odiPassword.toCharArray());
	// mOdiInstance.getSecurityManager().setGlobalAuthentication(auth);
	//
	//
	// }
	public void connectToOdi(String iMsterJndi, String iWorkRepName,String odiUsername,String odiPassword) {
		if (mOdiInstance == null) {
			mOdiInstance = OdiInstanceProvider.getInstance(iMsterJndi, iWorkRepName,odiUsername,odiPassword);
			odiBuilder = new JOdiBuilder(mOdiInstance);
		}
	}

	public JOdiBuilder getOdiBuilder() {
		return odiBuilder;
	}

}
