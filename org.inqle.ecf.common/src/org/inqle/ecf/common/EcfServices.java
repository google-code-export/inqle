package org.inqle.ecf.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.ExtensionFactory;

public class EcfServices {
	
	public static final String EXTENSION_POINT_ECF_SERVICES = "org.inqle.ecf.services";

	private static final String ATTRIBUTE_PRODUCE = "produce";

	private static final String ATTRIBUTE_CONSUME = "consume";
	
	private static Logger log = Logger.getLogger(EcfServices.class);
	
	public static List<EcfService> listEcfClientServicesFromExtensions() {
		log.info("listEcfClientServicesFromExtensions()...");
		return ExtensionFactory.getExtensionObjectsWithValue(EcfService.class, EXTENSION_POINT_ECF_SERVICES, ATTRIBUTE_CONSUME, "true");
	}
	
	public static List<EcfService> listEcfServerServicesFromExtensions() {
		log.info("listEcfServerServicesFromExtensions()...");
		return ExtensionFactory.getExtensionObjectsWithValue(EcfService.class, EXTENSION_POINT_ECF_SERVICES, ATTRIBUTE_PRODUCE, "true");
	}
}