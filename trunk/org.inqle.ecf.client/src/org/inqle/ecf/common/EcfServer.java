package org.inqle.ecf.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
public class EcfServer implements IJavaExtension {
	private String uri;
	private String port;
	private String protocol;
	private Map<String, Object> ecfServiceObjects = new HashMap<String, Object>();
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	@Override
	public void setSpec(IExtensionSpec spec) {
		uri = spec.getAttribute("uri");
		port = spec.getAttribute("port");
		protocol = spec.getAttribute("protocol");
	}
	public Map<String, Object> getServiceObjects() {
		return ecfServiceObjects;
	}
	public void addServiceObject(String serviceClassName, Object serviceObject) {
		ecfServiceObjects.put(serviceClassName, serviceObject);
	}
	public Object getServiceObject(String serviceClassName) {
		return ecfServiceObjects.get(serviceClassName);
	}
}
