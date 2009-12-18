package org.inqle.ecf.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.ecf.core.IContainerManager;
import org.inqle.ecf.common.EcfService;
import org.inqle.ecf.common.EcfServices;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class EcfServerActivator implements BundleActivator {

//	private static BundleContext context;
	private ServiceTracker containerManagerServiceTracker;
	private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();
	private String containerId;
	private String containerType;
	
	private static Logger log = Logger.getLogger(EcfServerActivator.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
//		context = ctxt;
		containerId = System.getProperty(EcfServerConstants.ECF_SERVER_CONTAINER_ID_SYSTEM_VARIABLE);
		containerType = System.getProperty(EcfServerConstants.ECF_SERVER_CONTAINER_TYPE_SYSTEM_VARIABLE);
		log.info("Creating container of id: " + containerId + " and type: " + containerType);
		
		try {
			IContainerManager containerManager = getContainerManagerService(bundleContext);
			log.info("Got containerManager");
			containerManager.getContainerFactory().createContainer(containerType, new Object[] {containerId});
			log.info("Created container");
		} catch (Exception e) {
			log.error("Error creating container of id: " + containerId + " and type: " + containerType, e);
			return;
		}
		log.info("Register services...");
		registerServices(bundleContext);
	}

	private void registerServices(BundleContext bundleContext) {
		// register all remote services
		log.info("Get ECF Services...");
		List<EcfService> ecfServices = null;
		try {
			ecfServices = EcfServices.listEcfServicesFromExtensions();
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
			Class serviceClass;
			Class serviceInterface;
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
				
	//			bundleContext.registerService(IHello.class
	//				.getName(), new Hello(containerId), props);
			serviceRegistrations.add(serviceRegistration);
			System.out.println("Registered service: " + serviceClass.getName() + " on ECF Server: " + containerId);
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
