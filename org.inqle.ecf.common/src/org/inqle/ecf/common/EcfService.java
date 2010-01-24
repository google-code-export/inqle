package org.inqle.ecf.common;

import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.extensions.util.IJavaExtension;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jenabean.Site;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;

import thewebsemantic.Namespace;

/**
 * 
 * @author David Donohue
 * represents an ECF server that is contactable by an ECF client
 * This class can be assembled from <ecfServer> extensions (in plugin.xml)
 * and it is a persistable class so it can be stored in a datamodel.
 */
@TargetDatabaseId(InqleInfo.CORE_DATABASE_ID)
@TargetModelName(EcfService.ECF_MODEL_NAME)
@Namespace(RDF.INQLE)
public class EcfService implements IJavaExtension {
	public static final String ECF_MODEL_NAME = "_ECF";
	private String serviceClassName;
	private String serviceInterfaceName;
	private String serverProtocol;
	private String clientProtocol;

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
