package org.inqle.test.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

public class DDApplication extends Application {

	/**
     * Constructor.
     */
    public DDApplication() {
        setName("Example REST server application");
        setDescription("Example with challenge response authentication");
        setOwner("David Donohue");
        setAuthor("Daddy Delicious");
    }

    /**
     * Creates a root Router to dispatch call to server resources.
     */
    @Override
    public Restlet createInboundRoot() {
    	Router router = new Router(getContext());
        router.attach("/test", DocumentServerResource.class);
        router.attachDefault(UsageServerResource.class);
//        return router;
        
    	ChallengeAuthenticator authenticator = new ChallengeAuthenticator(
    			getContext(), ChallengeScheme.HTTP_BASIC, "My Realm");
		MapVerifier verifier = new MapVerifier();
		verifier.getLocalSecrets().put("dummy", "pwd".toCharArray());
		authenticator.setVerifier(verifier);
		authenticator.setNext(router);
		return authenticator;
    }

}
