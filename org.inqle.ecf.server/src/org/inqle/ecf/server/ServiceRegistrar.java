package org.inqle.ecf.server;

import java.util.Properties;

import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.inqle.ecf.common.EcfServiceConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Create a service registration for a remote OSGi service for INLQE QA application.
 * Adds a property to the props, which identifies the URI of the server, so that the client can identify
 * which server a service reference belongs to.
 * @author David Donohue
 * 2009/10/29
 *
 */
public class ServiceRegistrar {

	public static ServiceRegistration registerService(
			BundleContext bundleContext, 
			String containerId,
			String serviceName, 
			Object serviceObject) {
		Properties props = new Properties();
		// add OSGi service property indicating this
		props.put(IDistributionConstants.REMOTE_INTERFACES, IDistributionConstants.REMOTE_INTERFACES_WILDCARD);
		props.put(EcfServiceConstants.PROPERTY_SERVER_URI, containerId);
		
		ServiceRegistration serviceRegistration = bundleContext.registerService(
			serviceName, serviceObject, props);
		return serviceRegistration;
	}

}
