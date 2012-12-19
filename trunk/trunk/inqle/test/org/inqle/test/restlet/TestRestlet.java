package org.inqle.test.restlet;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;


public class TestRestlet {

	private static Logger log = Logger.getLogger(TestRestlet.class);
	private static DDComponent ddComponent = new DDComponent();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ddComponent.startServer();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ddComponent.stop();
	}

	@Test
	public void test() throws ResourceException, IOException {
		String path = new File("").getAbsolutePath();
		System.out.println("path=" + path);
		// Outputting the content of a Web page  
		ClientResource unauthenticatedClientResource = new ClientResource("http://localhost:8182/question"); 
		
		try {
			unauthenticatedClientResource.get().write(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Status status = unauthenticatedClientResource.getStatus();
		assertEquals(401, status.getCode());
		
		
		ClientResource authenticatedClientResource = new ClientResource("http://localhost:8182/test"); 
		ChallengeResponse authentication = new ChallengeResponse(
			ChallengeScheme.HTTP_BASIC, "dummy", "pwd");
		authenticatedClientResource.setChallengeResponse(authentication);
		Representation representation = authenticatedClientResource.get();
		Status statusAfterAuthent = authenticatedClientResource.getStatus();
		assertEquals(200, statusAfterAuthent.getCode());
		log.info("Authenticated client retrieved from server: ");
		representation.write(System.out);
		
//		log.info("Store a question...");
//		authenticatedClientResource.put(representation);
		
		log.info("Store another question...");
		Document q2 = new Document();
		q2.setTitle("duty");
		authenticatedClientResource.put(q2);
		
		log.info("get usage message...");
		ClientResource defaultClientResource = new ClientResource("http://localhost:8182/whatever?param7=dummy"); 
		defaultClientResource.accept(MediaType.TEXT_PLAIN);
		defaultClientResource.setChallengeResponse(authentication);
//		assertEquals(UsageServerResource.USAGE_MESSAGE, defaultClientResource.get());
		defaultClientResource.get().write(System.out);
	}

}
