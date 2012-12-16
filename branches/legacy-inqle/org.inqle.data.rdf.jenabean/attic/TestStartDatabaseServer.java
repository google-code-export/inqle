package org.inqle.test.data;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestStartDatabaseServer {

private static Logger log = Logger.getLogger(TestStartDatabaseServer.class);
	
	@Test
	public void startServer() {
		try {
			AppInfoProvider.startDatabaseServer();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assert(false);
		}
		log.info("Started database server");
	}
	
	@Test
	public void createAppInfo() {
		AppInfoProvider.createAppInfo();
	}
}
