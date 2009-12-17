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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EcfClientActivator implements BundleActivator {

	private static BundleContext context;
	private Servicer servicer;

	public void start(BundleContext ctxt) throws Exception {
		context = ctxt;
		servicer = Servicer.getInstance();
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
