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
package org.inqle.ecf.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.inqle.qa.common.services.Hello;
import org.inqle.qa.common.services.IHello;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class ServerApplication implements IApplication,
		IDistributionConstants {
	
//	private static final String DEFAULT_CONTAINER_TYPE = "ecf.r_osgi.peer";
//	public static final String DEFAULT_CONTAINER_ID = null;

	private BundleContext bundleContext;
	private ServiceTracker containerManagerServiceTracker;

	private String containerType;
	private String containerId;
//	private String containerPort;

	private final Object appLock = new Object();
	private boolean done = false;

//	private ServiceRegistration helloRegistration;
	private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();

	public Object start(IApplicationContext appContext) throws Exception {
		bundleContext = Activator.getContext();
		// Process Arguments
		processArgs(appContext);
		// Create Container of desired type
		IContainerManager containerManager = getContainerManagerService();
//		if (containerId == null || "".equals(containerId)) {
//			System.out.println("Container ID not specified");
//			containerManager.getContainerFactory().createContainer(
//					containerType);
//		} else {
//			ID serverId = IDFactory.getDefault().createStringID(containerId);
			containerManager.getContainerFactory().createContainer(
				containerType, new Object[] {containerId});
		
			
		// register all remote services
		ServiceRegistration serviceRegistration = ServiceRegistrar.registerService(
			bundleContext,
			containerId,
			IHello.class.getName(), 
			new Hello(containerId));
			
//			bundleContext.registerService(IHello.class
//				.getName(), new Hello(containerId), props);
		serviceRegistrations.add(serviceRegistration);
		System.out.println("Host: Hello Service Registered on server: " + containerId);

		// wait until stopped
		waitForDone();

		return IApplication.EXIT_OK;
	}

	public void stop() {
		for (ServiceRegistration sr: serviceRegistrations) {
			if (sr != null) {
				sr.unregister();
				sr = null;
			}
		}
		serviceRegistrations = new ArrayList<ServiceRegistration>();
		
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		bundleContext = null;
	}

	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(bundleContext,
					IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

	private void processArgs(IApplicationContext appContext) {
		String[] originalArgs = (String[]) appContext.getArguments().get(
				"application.args");
		if (originalArgs == null)
			return;
		for (int i = 0; i < originalArgs.length; i++) {
			if (originalArgs[i].equals("-containerType")) {
				containerType = originalArgs[i + 1];
				i++;
			} else if (originalArgs[i].equals("-containerId")) {
				containerId = originalArgs[i + 1];
				i++;
			}
//			else if (originalArgs[i].equals("-containerPort")) {
//				containerPort = originalArgs[i + 1];
//				i++;
//			}
		}
	}

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

}
