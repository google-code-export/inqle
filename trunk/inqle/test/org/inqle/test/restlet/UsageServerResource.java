package org.inqle.test.restlet;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class UsageServerResource extends ServerResource {

	public static final String USAGE_MESSAGE = "Please see here for proper usage...";
	@Get
	public String toString() {
		return USAGE_MESSAGE;
	}
	
	@Put
	public String store(Object rep) {
		return USAGE_MESSAGE;
	}
	
}
