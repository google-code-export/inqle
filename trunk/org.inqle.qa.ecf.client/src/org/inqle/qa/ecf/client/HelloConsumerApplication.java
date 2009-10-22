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
package org.inqle.qa.ecf.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.examples.remoteservices.hello.IHello;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.RemoteServiceHelper;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.concurrent.future.IFuture;
import org.inqle.qa.common.IHostIdentified;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class HelloConsumerApplication implements IApplication,
		IDistributionConstants, ServiceTrackerCustomizer {

	private static Logger log = Logger.getLogger(HelloConsumerApplication.class);
	public static final String CONSUMER_NAME = "org.inqle.qa.ecf.client";

//	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";

	public static final String ECF_PROTOCOL = "ecf.generic.client";

	private BundleContext bundleContext;
	private ServiceTracker containerManagerServiceTracker;
	private List<ServiceTracker> serviceTrackers = new ArrayList<ServiceTracker>();
//	private String containerType = DEFAULT_CONTAINER_TYPE;

	private final Object appLock = new Object();
	private boolean done = false;

//	private ServiceTracker helloServiceTracker;

	public enum DBServer {
	    SERVER1 ("ecftcp://localhost:3787/server1", "3787", ECF_PROTOCOL),
	    SERVER2 ("ecftcp://localhost:3788/server2", "3788", ECF_PROTOCOL);
	    
	    public String uri;
	    public String port;
	    public String protocol;
		public IHello helloService;
	    
	    DBServer(String uri, String port, String protocol) {
	    	this.uri = uri;
	    	this.port = port;
	    	this.protocol = protocol;
	    };
	};
	
	public enum DBService {
	    SERVICE1 (IHello.class.getName());
	    
	    public String serviceClassName;
	    DBService(String serviceClassName) {
	    	this.serviceClassName = serviceClassName;
	    };
	};
	

	public Object start(IApplicationContext appContext) throws Exception {
		// Set bundle context (for use with service trackers)
		bundleContext = Activator.getContext();
//		processArgs(appContext);

		// Create ECF container. This setup is required so that an ECF provider
		// will
		// be available for handling discovered remote endpoints
//		List<String> serverUriList = new ArrayList<String>();
		for (DBServer dbServer: DBServer.values()) {
			createContainer(dbServer.uri, dbServer.port, dbServer.protocol);
//			serverUriList.add(dbServer.uri);
		}
//		createContainer(serverUriList.toArray(new String[] {}), ECF_PROTOCOL);
		for (DBService dbService: DBService.values()) {
			ServiceTracker serviceTracker = new ServiceTracker(bundleContext,
				createRemoteFilter(dbService.serviceClassName), this);
			serviceTracker.open();
			serviceTrackers.add(serviceTracker);
		}

		waitForDone();

		return IApplication.EXIT_OK;
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
	 * Method called when a REMOTE IHello instance is registered.
	 */
	public Object addingService(ServiceReference reference) {
		Object serviceObject = bundleContext.getService(reference);
		for (String key: reference.getPropertyKeys()) {
			System.out.println(key + "=" + reference.getProperty(key));
		}
		Object objectClass = reference.getProperty("objectClass");
		Class objectClassClass = (Class)objectClass;
		log.info("objectClassClass=" + objectClassClass.getName());
		String hostUri = null;
		if (serviceObject instanceof IHostIdentified) {
			hostUri = ((IHostIdentified)serviceObject).getHostUri();
		}
		if (hostUri==null) {
			log.warn("Added service not an instance of IHostIdentified, so will ignore it.");
			return null;
		}
		DBServer server = getDBServerOfUri(hostUri);
		if (serviceObject instanceof IHello) {
			System.out.println("IHello service proxy being added");
			IHello hello = (IHello) serviceObject;
			// Call it
			String helloMessage = hello.hello(CONSUMER_NAME);
			System.out.println("Called hello using proxy, received: " + helloMessage);
			server.helloService = hello;
		}
		
		

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

	private DBServer getDBServerOfUri(String hostUri) {
		for (DBServer dbServer: DBServer.values()) {
			if (dbServer.uri.equals(hostUri)) return dbServer;
		}
		return null;
	}

	public void modifiedService(ServiceReference reference, Object service) {
	}

	public void removedService(ServiceReference reference, Object service) {
	}

}
