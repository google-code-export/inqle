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


import org.apache.log4j.Logger;
//import org.inqle.ecf.services.IHello;
//import org.inqle.qa.ecf.services.IHello2;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EcfClientActivator implements BundleActivator {

	private static BundleContext context;
	private Servicer servicer;
	private static Logger log = Logger.getLogger(EcfClientActivator.class);
	public void start(BundleContext ctxt) throws Exception {
		context = ctxt;
		servicer = Servicer.getInstance();
		//register services from this bundle
//		servicer.addEcfService(IHello.class.getName());
//		servicer.addEcfService(IHello2.class.getName());
		//test calling service
//		IHello hello1 = servicer.getServiceObject(IHello.class, "ecftcp://localhost:3787/server1");
//		log.info("hello1.hello() to hello1 service object: " + hello1);
//		log.info(hello1.hello("QA Client (to ECF server #1, IHello1 service)"));
//		
//		IHello2 hello2 = servicer.getServiceObject(IHello2.class, "ecftcp://localhost:3787/server1");
//		log.info("hello2.hello()" + hello2.hello("QA Client (to ECF server #1, IHello2 service)"));
		log.info("inqle.home=" + System.getProperty("inqle.home"));
	}

	public void stop(BundleContext context) throws Exception {
		context = null;
		//TODO: needed?
		servicer.removeServices();
	}

	public static BundleContext getContext() {
		return context;
	}

}
