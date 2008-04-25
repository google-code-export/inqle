package org.inqle.test.user.data.extraction;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Run this Junit4 test suite to test all functionality within this bundle.
 * 
 * @author David Donohue
 * Dec 26, 2007
 */
@RunWith(Suite.class)
@SuiteClasses({
	TestSimpleSparqlSampler.class
})
public class TestSamplerSuite extends TestSuite { }
