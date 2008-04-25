package org.inqle.test.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.junit.Test;

public class TestCreateAppInfo {
	
	@Test
	public void getAppInfoPath() {
		String appInfoPath = Persister.getAppInfoFilePath();
		assertEquals(appInfoPath, System.getProperty(InqleInfo.INQLE_HOME) + Persister.FILENAME_APPINFO);
	}
	
	@Test
	public void testGetAppInfo() {
		AppInfoProvider.createAppInfo();
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		assertNotNull(appInfo);
		assertEquals(appInfo.getId(), AppInfo.APPINFO_INSTANCE);
	}
}
