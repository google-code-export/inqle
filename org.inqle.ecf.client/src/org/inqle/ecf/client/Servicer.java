/****************************************************************************
 * Copyright (c) 2009 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.inqle.ecf.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.inqle.ecf.common.EcfService;
import org.inqle.ecf.common.EcfServiceConstants;
import org.inqle.ecf.common.EcfServices;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * This class is responsible for holding references to all servers and services known to this INQLE app.
 * @author David Donohue
 * 2009/12/11
 */
public class Servicer implements IDistributionConstants, ServiceTrackerCustomizer {

	private static Logger log = Logger.getLogger(Servicer.class);
//	public static final String CONSUMER_NAME = "org.inqle.ecf.client";

//	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";

//	public static final String ECF_PROTOCOL = "ecf.generic.client";

	private Map<String, Object> services = new HashMap<String, Object>();
	
	private BundleContext bundleContext;
	private ServiceTracker containerManagerServiceTracker;
	private List<ServiceTracker> serviceTrackers = new ArrayList<ServiceTracker>();
//	private String containerType = DEFAULT_CONTAINER_TYPE;

	private final Object appLock = new Object();
	private boolean done = false;
	private List<EcfServer> ecfServers = new ArrayList<EcfServer>();

	/* *********************************************************************
	 * *** FACTORY METHODS
	 * ********************************************************************* */
	private Servicer() {
		log.info("Servicer() called");
		if (bundleContext == null) {
			log.info("initialize bundleContext");
			bundleContext = EcfClientActivator.getContext();
		}
		resetServersAndServices();
	}
	
	public void resetServersAndServices() {
		removeServices();
		log.info("bundleContext=" + bundleContext);
		try {
			setPermanentEcfServers();
		} catch (Exception e) {
			log.error("Error setting ECF servers from plugin extensions", e);
		}
		try {
			setPermanentEcfServices();
		} catch (InvalidSyntaxException e) {
			log.error("Error setting ECF services from plugin extensions", e);
		}
	}

	/**
	* ServicerHolder is loaded on the first execution of Servicer.getInstance() 
	* or the first access to ServicerHolder, not before.
	*/
	private static class ServicerHolder { 
		private final static Servicer instance = new Servicer();
	}
	 
	public static Servicer getInstance() {
		return ServicerHolder.instance;
	}
	
//	public <T> T getService(Class<T> serviceClass) {
//    	String key = serviceClass.getName();
//    	Object serviceObject = services.get(key);
//    	if (serviceObject == null) return null;
//    	T typedServiceObject = (T)serviceObject;
//    	return typedServiceObject;
//    }
//    
//    public void addService(String serviceClassName, Object serviceObject) {
//    	services.put(serviceClassName, serviceObject);
//    }
//    
//    public void removeService(String serviceClassName) {
//    	services.remove(serviceClassName);
//    }	

	public void setPermanentEcfServers() throws Exception {
		log.info("setPermanentEcfServers()...");
		// Set bundle context (for use with service trackers)
		
//		List<EcfServer> ecfServers = ExtensionFactory.getExtensionObjects(EcfServer.class, EcfServers.EXTENSION_POINT_ECF_SERVERS);
		ecfServers = EcfServers.listEcfServersFromExtensions();
			
		// Create ECF container. This setup is required so that an ECF provider
		// will be available for handling discovered remote endpoints
		for (EcfServer ecfServer: ecfServers) {
			addEcfServer(ecfServer);
		}

//		waitForDone();
	}
	
	private void addEcfServer(EcfServer ecfServer) throws Exception {
		createContainer(ecfServer.getUri(), ecfServer.getPort(), ecfServer.getProtocol());
	}

	public void setPermanentEcfServices() throws InvalidSyntaxException {
//		List<EcfService> ecfServices = ExtensionFactory.getExtensionObjects(EcfService.class, EcfServices.EXTENSION_POINT_ECF_SERVICES);
		List<EcfService> ecfServices = EcfServices.listEcfClientServicesFromExtensions();
		// Create ECF container. This setup is required so that an ECF provider
		// will be available for handling discovered remote endpoints
		for (EcfService ecfService: ecfServices) {
			addEcfService(ecfService);
		}
	}
	
	private void addEcfService(EcfService ecfService) throws InvalidSyntaxException {
		trackServiceType(ecfService.getServiceInterfaceName());
	}

	public void trackServiceType(String serviceClassName) throws InvalidSyntaxException {
		ServiceTracker serviceTracker = new ServiceTracker(
			bundleContext, 
			createRemoteFilter(serviceClassName), 
			this);
		serviceTracker.open();
		serviceTrackers.add(serviceTracker);
	}

//	private void createContainer(String serverUri, String protocol) throws Exception {
//		// Get container factory
//		IContainerFactory containerFactory = getContainerManagerService()
//				.getContainerFactory();
//		ID serverId = IDFactory.getDefault().createStringID(serverUri);
//		containerFactory.createContainer(protocol, serverId);
//	}

	private void createContainer(String serverUri, String port, String protocol) throws Exception {
	// Get container factory
		IContainerFactory containerFactory = getContainerManagerService()
				.getContainerFactory();
		//	ID serverId = IDFactory.getDefault().createStringID(serverUri);
		containerFactory.createContainer(protocol, new Object[] {serverUri, port});
	}
	
	private Filter createRemoteFilter(String serviceClassName) throws InvalidSyntaxException {
		// This filter looks for IHello instances that have the REMOTE property
		// set (are remote
		// services as per RFC119).
		return bundleContext.createFilter("(&("
				+ org.osgi.framework.Constants.OBJECTCLASS + "="
				+ serviceClassName + ")(" + REMOTE + "=*))");
	}

	public void removeServices() {
		for (ServiceTracker serviceTracker: serviceTrackers) {
			if (serviceTracker != null) {
				serviceTracker.close();
				serviceTracker = null;
			}
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
	}

	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(bundleContext, IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

//	private void waitForDone() {
//		// then just wait here
//		synchronized (appLock) {
//			while (!done) {
//				try {
//					appLock.wait();
//				} catch (InterruptedException e) {
//					// do nothing
//				}
//			}
//		}
//	}

	/**
	 * Method called when each service instance is registered.
	 */
	public Object addingService(ServiceReference reference) {
		log.info("Adding service: " + reference);
		Object serviceObject = bundleContext.getService(reference);
		for (String key: reference.getPropertyKeys()) {
			log.info(key + "=" + reference.getProperty(key));
		}
		
		String serviceClassName = null;
		try {
			String[] objectClasses = (String[])reference.getProperty("objectClass");
			serviceClassName = objectClasses[0];
		} catch (Exception e1) {
			e1.printStackTrace();
		}
//		log.info("Getting service of class:" + serviceClassName);
		
		
		String hostUri = (String)reference.getProperty(EcfServiceConstants.PROPERTY_SERVER_URI);
		if (hostUri==null || hostUri.length()==0) {
			log.error("Remote service: " + serviceClassName + " did not declare property: " + EcfServiceConstants.PROPERTY_SERVER_URI + ".  Ignoring it.");
			return null;
		}

//		log.info("Getting server of URI:" + hostUri);
		if (hostUri==null) {
			log.warn("Incoming service is not an instance of IServerIdentified, so will ignore it.");
			return null;
		}
		
		EcfServer server = getEcfServerOfUri(hostUri);
		log.info("For server " + server.getUri() + ", storing service:" + serviceClassName + "...");
		
		server.addServiceObject(serviceClassName, serviceObject);
		server.addServiceReference(serviceClassName, reference);
		
//		if (serviceObject instanceof IHello) {
//			System.out.println("IHello service proxy being added");
//			IHello hello = (IHello) serviceObject;
//			// Call it
//			String helloMessage = hello.hello("org.inqle.ecf.client");
//			log.info("Called hello using proxy, received: " + helloMessage);
//		}
		
		// Now get remote service reference and use asynchronous
		// remote invocation
//		IRemoteService remoteService = (IRemoteService) reference
//				.getProperty(REMOTE);
//		// This futureExec returns immediately
//		IFuture future = RemoteServiceHelper.futureExec(remoteService, "hello",
//				new Object[] { "org.inqle.ecf.client" + " future" });
//
//		try {
//			// This method blocks until a return
//			String futureHelloMessage = (String)future.get();
//			System.out.println("Called hello using future, received: " + futureHelloMessage);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return serviceObject;
	}

	private EcfServer getEcfServerOfUri(String serverUri) {
		for (EcfServer ecfServer: ecfServers) {
			if (ecfServer.getUri().equals(serverUri)) return ecfServer;
		}
		return null;
	}
	
	public <T> T getServiceObject(Class<T> serviceClass, String serverUri) {
		EcfServer server = getEcfServerOfUri(serverUri);
		return server.getServiceObject(serviceClass);
	}
	
	public ServiceReference getServiceReference(String serviceClassName, String serverUri) {
		EcfServer server = getEcfServerOfUri(serverUri);
		return server.getServiceReference(serviceClassName);
	}

	public void modifiedService(ServiceReference reference, Object service) {
	}

	//TODO: get this working
	public void removedService(ServiceReference reference, Object service) {
	}

}
