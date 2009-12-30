package org.inqle.qa.common.activator;

import org.apache.log4j.Logger;
import org.inqle.ecf.client.Servicer;
import org.inqle.ecf.services.IHello;
import org.inqle.qa.ecf.services.IHello2;
import org.inqle.qa.services.IQAObjectService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
//import org.inqle.rdf.object.services.IRdfObjectService;

public class QAClientActivator implements BundleActivator{

	private static BundleContext context;
	private Servicer servicer;
	private static Logger log = Logger.getLogger(QAClientActivator.class);
	public void start(BundleContext ctxt) throws Exception {
		context = ctxt;
		servicer = Servicer.getInstance();
		//register services from this bundle
//		servicer.addEcfService(IHello.class.getName());
		
		//test calling service
		IHello hello1 = servicer.getServiceObject(IHello.class, "ecftcp://localhost:3787/server1");
		log.info("hello1.hello() to hello1 service object: " + hello1);
		log.info(hello1.hello("QA Client (to ECF server #1, IHello1 service)"));

		IHello2 hello2 = servicer.getServiceObject(IHello2.class, "ecftcp://localhost:3787/server1");
		log.info("hello2.hello()" + hello2.hello("QA Client (to ECF server #1, IHello2 service)"));
		
		IQAObjectService qaObjectService = servicer.getServiceObject(
				IQAObjectService.class, 
				"ecftcp://localhost:3787/server1");
	}

	public void stop(BundleContext context) throws Exception {
		context = null;
		//TODO: needed?
//		servicer.removeServices();
	}

	public static BundleContext getContext() {
		return context;
	}
}
