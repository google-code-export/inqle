package org.inqle.qa.common.activator;

import org.apache.log4j.Logger;
import org.inqle.ecf.client.Servicer;
import org.inqle.qa.common.services.IHello;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class QACommonActivator implements BundleActivator{

	private static BundleContext context;
	private Servicer servicer;
	private static Logger log = Logger.getLogger(QACommonActivator.class);
	public void start(BundleContext ctxt) throws Exception {
		context = ctxt;
		servicer = Servicer.getInstance();
		IHello hello1 = servicer.getServiceObject(IHello.class, "ecftcp://localhost:3787/server1");
		log.info("hello1.hello()" + hello1.hello("QA Client (to ECF server #1)"));
		
		IHello hello2 = servicer.getServiceObject(IHello.class, "ecftcp://localhost:3788/server2");
		log.info("hello2.hello()" + hello2.hello("QA Client (to ECF server #2)"));
		
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
