package org.inqle.testing;

import java.util.ArrayList;
import java.util.List;

import org.inqle.core.extensions.util.ExtensionFactory;
import org.inqle.core.extensions.util.IExtensionSpec;

public class JUnitTestLister {

	public static List<IJUnitTest> listJUnitTests() {
		List<IJUnitTest> junitTests = new ArrayList<IJUnitTest>();
		
		List<IExtensionSpec> extensionSpecs = 
			ExtensionFactory.getExtensionSpecs(IJUnitTest.ID);
		
		for (IExtensionSpec extensionSpec: extensionSpecs) {
			if (extensionSpec == null) continue;
			IJUnitTest junitTest = JUnitTestFactory.createJUnitTest(extensionSpec);
			junitTests.add(junitTest);
		}
		
		return junitTests;
	}
}
