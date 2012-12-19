package org.inqle.test.restlet;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;

public class DDComponent extends Component {

	/**
     * Launches the mail server component.
     * 
     * @param args
     *            The arguments.
     * @throws Exception
     */

    /**
     * Constructor.
     * 
     * @throws Exception
     */
    public DDComponent() {
        // Set basic properties
        setName("RESTful component");
        setDescription("description here");
        setOwner("DD");
        setAuthor("DD");
    }
    
    public void startServer() throws Exception {
        // Add connectors
//        getClients().add(new Client(Protocol.CLAP));
    	
        Server server = new Server(new Context(), Protocol.HTTP, 8182);
        server.getContext().getParameters().set("tracing", "true");
        getServers().add(server);
        // Configure the default virtual host
//        VirtualHost host = getDefaultHost();
        // host.setHostDomain("www\\.rmep\\.com|www\\.rmep\\.net|www\\.rmep\\.org");
        // host.setServerAddress("1\\.2\\.3\\.10|1\\.2\\.3\\.20");
        // host.setServerPort("80");

        // Attach the application to the default virtual host
        getDefaultHost().attach(new DDApplication());
        this.start();
    }

}
