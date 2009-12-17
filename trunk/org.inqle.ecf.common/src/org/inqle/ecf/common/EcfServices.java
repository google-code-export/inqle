package org.inqle.ecf.common;

import java.util.List;

import org.inqle.core.extensions.util.ExtensionFactory;

public class EcfServices {
	
	public static final String EXTENSION_POINT_ECF_SERVICES = "org.inqle.ecf.services";
	
	public static List<EcfService> listEcfServicesFromExtensions() {
		return ExtensionFactory.getExtensionObjects(EcfService.class, EXTENSION_POINT_ECF_SERVICES);
	}
}