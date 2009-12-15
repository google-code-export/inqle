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
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.RemoteServiceHelper;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.concurrent.future.IFuture;
import org.inqle.qa.common.services.IHello;
import org.inqle.qa.common.services.QAServiceConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.inqle.ecf.client.EcfClientConstants;
import org.inqle.ecf.client.spec.InqleServer;
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
	
	private BundleContext bundleContext = Activator.getContext();
	private ServiceTracker containerManagerServiceTracker;
	private List<ServiceTracker> serviceTrackers = new ArrayList<ServiceTracker>();
//	private String containerType = DEFAULT_CONTAINER_TYPE;

	private final Object appLock = new Object();
	private boolean done = false;

	/* *********************************************************************
	 * *** FACTORY METHODS
	 * ********************************************************************* */
	private Servicer() {}
	
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
	
	public <T> T getService(Class<T> serviceClass) {
    	String key = serviceClass.getName();
    	Object serviceObject = services.get(key);
    	if (serviceObject == null) return null;
    	T typedServiceObject = (T)serviceObject;
    	return typedServiceObject;
    }
    
    public void addService(String serviceClassName, Object serviceObject) {
    	services.put(serviceClassName, serviceObject);
    }
    
    public void removeService(String serviceClassName) {
    	services.remove(serviceClassName);
    }	
	
	public enum InqleService {
	    SERVICE1 (IHello.class.getName());
	    
	    public String serviceClassName;
	    InqleService(String serviceClassName) {
	    	this.serviceClassName = serviceClassName;
	    };
	};
	

	public Object start(IApplicationContext appContext) throws Exception {
		log.info("ECF Client starting...");
		// Set bundle context (for use with service trackers)
		
//		processArgs(appContext);

		// Create ECF container. This setup is required so that an ECF provider
		// will be available for handling discovered remote endpoints
		for (InqleServer dbServer: InqleServer.values()) {
			createContainer(dbServer.uri, dbServer.port, dbServer.protocol);
		}
		
		for (InqleService service: InqleService.values()) {
			trackServiceType(service.serviceClassName);
		}

		waitForDone();

		return IApplication.EXIT_OK;
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

	public void stop() {
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
		this.bundleContext = null;
	}

	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(bundleContext,
					IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

//	private void processArgs(IApplicationContext appContext) {
//		String[] originalArgs = (String[]) appContext.getArguments().get(
//				"application.args");
//		if (originalArgs == null)
//			return;
//		for (int i = 0; i < originalArgs.length; i++) {
//			if (originalArgs[i].equals("-containerType")) {
//				containerType = originalArgs[i + 1];
//				i++;
//			}
//		}
//	}

	private void waitForDone() {
		// then just wait here
		synchronized (appLock) {
			while (!done) {
				try {
					appLock.wait();
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		}
	}

	/**
	 * Method called when each service instance is registered.
	 */
	public Object addingService(ServiceReference reference) {
		
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
		log.info("Getting service of class:" + serviceClassName);
		
		
		String hostUri = (String)reference.getProperty(QAServiceConstants.PROPERTY_SERVER_URI);
		if (hostUri==null || hostUri.length()==0) {
			log.error("Remote service: " + serviceClassName + " did not declare property: " + QAServiceConstants.PROPERTY_SERVER_URI + ".  Ignoring it.");
			return null;
		}
//		if (serviceObject instanceof IServerIdentified) {
//			log.info("1...");
//			IServerIdentified siObj = (IServerIdentified)serviceObject;
//			log.info("2...");
//			try {
//				hostUri = siObj.getServerId();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			log.info("3...");
//		}
		log.info("Getting server of URI:" + hostUri);
		if (hostUri==null) {
			log.warn("Incoming service is not an instance of IServerIdentified, so will ignore it.");
			return null;
		}
		
		InqleServer server = getQAServerOfUri(hostUri);
		log.info("For server " + server.uri + ", storing service:" + serviceClassName + "...");
		if (serviceObject instanceof IHello) {
			System.out.println("IHello service proxy being added");
			IHello hello = (IHello) serviceObject;
			// Call it
			String helloMessage = hello.hello(CONSUMER_NAME);
			log.info("Called hello using proxy, received: " + helloMessage);
		}
		
		server.addService(serviceClassName, serviceObject);
		
		// Now get remote service reference and use asynchronous
		// remote invocation
		IRemoteService remoteService = (IRemoteService) reference
				.getProperty(REMOTE);
		// This futureExec returns immediately
		IFuture future = RemoteServiceHelper.futureExec(remoteService, "hello",
				new Object[] { CONSUMER_NAME + " future" });

		try {
			// This method blocks until a return
			String futureHelloMessage = (String)future.get();
			System.out.println("Called hello using future, received: " + futureHelloMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceObject;
	}

	private InqleServer getQAServerOfUri(String hostUri) {
		for (InqleServer dbServer: InqleServer.values()) {
			if (dbServer.uri.equals(hostUri)) return dbServer;
		}
		return null;
	}

	public void modifiedService(ServiceReference reference, Object service) {
	}

	public void removedService(ServiceReference reference, Object service) {
	}

}
