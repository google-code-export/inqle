package org.inqle.testing;

import org.apache.log4j.Logger;
import org.inqle.core.extensions.util.IExtensionSpec;
import org.inqle.core.util.InqleInfo;

public class JUnitTestFactory {

	private static Logger log = Logger.getLogger(JUnitTestFactory.class);
	
	public static IJUnitTest createJUnitTest(IExtensionSpec extensionSpec) {
		JUnitTest jUnitTest = new JUnitTest();
		jUnitTest.setName(extensionSpec.getAttribute(InqleInfo.NAME_ATTRIBUTE));
		jUnitTest.setDescription(extensionSpec.getAttribute(InqleInfo.DESCRIPTION_ATTRIBUTE));
		String junitTestClassStr = extensionSpec.getAttribute(IJUnitTest.TEST_CLASS_ATTRIBUTE);
		Class<?> junitTestClass = null;
		try {
			junitTestClass = Class.forName(junitTestClassStr);
		} catch (Exception e) {
			log.error("Unable to add JUnit test plugin named " + extensionSpec.getAttribute(InqleInfo.NAME_ATTRIBUTE), e);
		}
		jUnitTest.setJUnitTestClass(junitTestClass);
		return jUnitTest;
	}
}
