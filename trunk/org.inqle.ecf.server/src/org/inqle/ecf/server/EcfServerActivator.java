package org.inqle.ecf.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.inqle.ecf.common.EcfServerConstants;
import org.inqle.ecf.common.EcfService;
import org.inqle.ecf.common.EcfServices;
import org.inqle.ecf.common.IInqleEcfService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class EcfServerActivator implements BundleActivator {

private static final String BASE_PROPERTY_NAME = "org.inqle.ecf.server.uri.";
	//	private static BundleContext context;
	private ServiceTracker containerManagerServiceTracker;
	private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();
//	private String baseContainerId;
//	private String containerType;
	private List<String> containerTypes = new ArrayList<String>();
//	private String containerPort;
	
	private static Logger log = Logger.getLogger(EcfServerActivator.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
//		baseContainerId = System.getProperty(EcfServerConstants.PROPERTY_SERVER_URI);
//		containerPort = System.getProperty(EcfServerConstants.PROPERTY_SERVER_PORT);
		// register all remote services, creating containers along the way
		log.info("Get ECF Services...");
		List<EcfService> ecfServices = null;
		try {
			ecfServices = EcfServices.listEcfServerServicesFromExtensions();
			log.info("Successfully got list of ECF services");
		} catch (Exception e) {
			log.error("Error getting ECF services", e);
		}
		
		if (ecfServices==null) {
			log.info("Found no ECF services declared for this server.");
			return;
		}
		log.info("Registering " + ecfServices.size() + " services...");
		for (EcfService ecfService: ecfServices) {
//			String containerId = baseContainerId + ":" + containerPort + "/" + ecfService.getProtocol();
			String containerId = getContainerId(ecfService.getServerProtocol());
			if (containerId==null) continue;
			registerContainerIfNotDone(bundleContext, containerId, ecfService.getServerProtocol());
			Class serviceClass;
//			Class serviceInterface;
			Object instance = null;
			try {
				serviceClass = Class.forName(ecfService.getServiceClassName());
				instance = serviceClass.newInstance();
				log.info("EcfServerActivator: registered service: " + ecfService.getServiceClassName() + " with object: " + instance);
			} catch (Exception e) {
				log.error("Unable to instantiate object of class: " + ecfService.getServiceClassName(), e);
				continue;
			}
			
			if (instance instanceof IInqleEcfService) {
				((IInqleEcfService)instance).setServerId(containerId);
			}
			
			ServiceRegistration serviceRegistration = ServiceRegistrar.registerService(
				bundleContext,
				containerId,
				ecfService.getServiceInterfaceName(), 
				instance);
				
			serviceRegistrations.add(serviceRegistration);
			System.out.println("Registered service: " + serviceClass.getName() + " on ECF Server: " + containerId);
		}
	}
	
	private String getContainerId(String protocol) {
		String propertyName = BASE_PROPERTY_NAME + protocol;
		String val = System.getProperty(propertyName);
		if (val==null) {
			log.error("Found no system property: " + propertyName + "\nMust declare this property.");
		}
		return val;
	}

	private void registerContainerIfNotDone(BundleContext bundleContext, String containerId, String containerType) {
		if (containerTypes.contains(containerType)) return;
//		String containerId = baseContainerId + "/" + containerType.replaceAll("\\.", "_");
		
		log.info("Creating container of id: " + containerId + " and type: " + containerType);
		
		try {
			IContainerManager containerManager = getContainerManagerService(bundleContext);
			log.info("Got containerManager");
		    ID targetID = IDFactory.getDefault().createStringID(containerId);
	        containerManager.getContainerFactory().createContainer(containerType, new Object[] {targetID});

			//works for generic but not REST
//			containerManager.getContainerFactory().createContainer(containerType, new Object[] {containerId});
			log.info("Created container");
			containerTypes.add(containerType);
		} catch (Exception e) {
			log.error("Error creating container of id: " + containerId + " and type: " + containerType, e);
			return;
		}
	}

	private IContainerManager getContainerManagerService(BundleContext bundleContext) {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(bundleContext,
					IContainerManager.class.getName(), null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}
	
	public void stop(BundleContext context) throws Exception {
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
		context = null;
	}

}
