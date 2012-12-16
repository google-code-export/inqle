package org.inqle.ecf.client;

import java.util.HashMap;
import java.util.Map;

import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.extensions.util.IJavaExtension;
import org.inqle.core.util.InqleInfo;
import org.inqle.rdf.RDF;
import org.inqle.rdf.annotations.TargetDatabaseId;
import org.inqle.rdf.annotations.TargetModelName;
import org.osgi.framework.ServiceReference;

import thewebsemantic.Namespace;

/**
 * 
 * @author David Donohue
 * represents an ECF server that is contactable by an ECF client
 * This class can be assembled from <ecfServer> extensions (in plugin.xml)
 * and it is a persistable class so it can be stored in a datamodel.
 */
@TargetDatabaseId(InqleInfo.CORE_DATABASE_ID)
@TargetModelName(RDF.SITE_DATASET_ROLE_ID)
@Namespace(RDF.INQLE)
public class EcfServer implements IJavaExtension {
	private String uri;
	private String port;
	private String protocol;
	private Map<String, Object> ecfServiceObjects = new HashMap<String, Object>();
	private Map<String, ServiceReference> ecfServiceReferences = new HashMap<String, ServiceReference>();
	
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
	public <T> T getServiceObject(Class<T> serviceClass) {
		return (T)ecfServiceObjects.get(serviceClass.getName());
	}
	public void addServiceReference(String serviceClassName, ServiceReference reference) {
		ecfServiceReferences.put(serviceClassName, reference);
	}
	public ServiceReference getServiceReference(String serviceClassName) {
		return ecfServiceReferences.get(serviceClassName);
	}
	
	@Override
	public String toString() {
		String s = "EcfServer: {uri=" + uri + "; port=" + port + "; protocol=" + protocol + "; number of ecfServiceObjects=" + ecfServiceObjects.size() + "}";
		return s;
	}
}
