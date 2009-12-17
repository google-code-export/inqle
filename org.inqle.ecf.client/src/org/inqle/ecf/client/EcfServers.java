package org.inqle.ecf.client;

import java.util.List;

import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.ecf.common.EcfServer;
import org.inqle.ecf.common.EcfService;

public class EcfServers {
	
	public static final String EXTENSION_POINT_ECF_SERVERS = "org.inqle.ecf.extensionPoint.servers";
	public static final String EXTENSION_POINT_ECF_SERVICES = "org.inqle.ecf.extensionPoint.services";
	
	public static List<EcfServer> listEcfServersFromExtensions() {
		return ExtensionFactory.getExtensionObjects(EcfServer.class, EXTENSION_POINT_ECF_SERVERS);
	}
	
	public static List<EcfService> listEcfServicesFromExtensions() {
		return ExtensionFactory.getExtensionObjects(EcfService.class, EXTENSION_POINT_ECF_SERVICES);
	}
}

//public enum InqleServers {
//	SERVER1("ecftcp://localhost:3787/server1", "3787", EcfClientConstants.ECF_PROTOCOL),
//	SERVER2("ecftcp://localhost:3788/server2", "3788", EcfClientConstants.ECF_PROTOCOL);
//    
//    public String uri;
//    public String port;
//    public String protocol;
//	
//    InqleServers(String uri, String port, String protocol) {
//    	this.uri = uri;
//    	this.port = port;
//    	this.protocol = protocol;
//    };
//}