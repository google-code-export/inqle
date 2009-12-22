package org.inqle.ecf.common;

import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.extensions.util.IJavaExtension;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.Site;
import org.inqle.data.rdf.jenabean.TargetDatabaseId;
import org.inqle.data.rdf.jenabean.TargetModelName;

import thewebsemantic.Namespace;

/**
 * 
 * @author David Donohue
 * represents an ECF server that is contactable by an ECF client
 * This class can be assembled from <ecfServer> extensions (in plugin.xml)
 * and it is a persistable class so it can be stored in a datamodel.
 */
@TargetDatabaseId(Persister.CORE_DATABASE_ID)
@TargetModelName(Site.SITE_DATASET_ROLE_ID)
@Namespace(RDF.INQLE)
public class EcfService implements IJavaExtension {
	private String serviceClassName;
	private String serviceInterfaceName;
	private String serverProtocol;
	private String clientProtocol;

	@Override
	public void setSpec(IExtensionSpec spec) {
		serviceClassName = spec.getAttribute("serviceClassName");
		serviceInterfaceName = spec.getAttribute("serviceInterfaceName");
		serverProtocol = spec.getAttribute("serverProtocol");
		clientProtocol = spec.getAttribute("clientProtocol");
	}

	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}

	public String getServiceClassName() {
		return serviceClassName;
	}

	public void setServiceInterfaceName(String serviceInterfaceName) {
		this.serviceInterfaceName = serviceInterfaceName;
	}

	public String getServiceInterfaceName() {
		return serviceInterfaceName;
	}

	public String getServerProtocol() {
		return serverProtocol;
	}

	public void setServerProtocol(String serverProtocol) {
		this.serverProtocol = serverProtocol;
	}

	public String getClientProtocol() {
		return clientProtocol;
	}

	public void setClientProtocol(String clientProtocol) {
		this.clientProtocol = clientProtocol;
	}
}
