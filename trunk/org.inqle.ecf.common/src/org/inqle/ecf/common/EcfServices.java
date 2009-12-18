package org.inqle.ecf.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;

public class EcfServices {
	
	public static final String EXTENSION_POINT_ECF_SERVICES = "org.inqle.ecf.services";
	private static Logger log = Logger.getLogger(EcfServices.class);
	
	public static List<EcfService> listEcfServicesFromExtensions() {
		log.info("listEcfServicesFromExtensions()...");
		return ExtensionFactory.getExtensionObjects(EcfService.class, EXTENSION_POINT_ECF_SERVICES);
	}
}