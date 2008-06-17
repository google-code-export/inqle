package org.inqle.test.data;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestStopDatabaseServer {

private static Logger log = Logger.getLogger(TestStopDatabaseServer.class);
	
	@Test
	public void stopServer() {
		AppInfoProvider.stopDatabaseServer();
		log.info("Stopped database server");
	}
}
