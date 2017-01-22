package oracle.dicloud.odi.metadatashare;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.security.Authentication;

import org.apache.commons.lang.StringUtils;

public class OdiInstanceProvider {

	private static OdiInstance mOdiInstance = null;

	public static OdiInstance getInstance(String iMasterJndi, String iWorkRepName,String odiUsername,String odiPassword) {
		if (mOdiInstance != null) {
			return mOdiInstance;
		}
		WorkRepositoryDbInfo aWorkRepositoryDbInfo = null;
		MasterRepositoryDbInfo aMasterRepositoryDbInfo = new MasterRepositoryDbInfo(iMasterJndi);
		if (StringUtils.isNotBlank(iWorkRepName)) {
			aWorkRepositoryDbInfo = new WorkRepositoryDbInfo(iWorkRepName, null);
		}
		try {
			OdiInstanceConfig odiInstanceConfig = new OdiInstanceConfig(aMasterRepositoryDbInfo, aWorkRepositoryDbInfo);
			OdiInstance odiInstance = OdiInstance.createInstance(odiInstanceConfig);
			Authentication auth = odiInstance.getSecurityManager().createAuthentication(odiUsername,
					odiPassword.toCharArray());
			odiInstance.getSecurityManager().setGlobalAuthentication(auth);
			mOdiInstance = odiInstance;
			return odiInstance;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
