package org.inqle.test.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Run this Junit4 test suite to test all functionality within this bundle.
 * 
 * This suite tests the following operations:<ul>
 * <li>load AppInfo object from file</li>
 * <li>Create SDB store, registering it in the repositories model</li>
 * <li>Bulk load data into this store</li>
 * <li>Query this store using SPARQL</li>
 * </ul>
 * 
 * TODO load data into 2nd store, query across the 2 stores
 * TODO add performance metrics
 * TODO add profiling, memory usage metrics
 * 
 * @author David Donohue
 * Dec 17, 2007
 */
@RunWith(Suite.class)
@SuiteClasses({
	TestJenabean.class,
	TestCreateAppInfo.class,
	TestCreateStores.class,
	TestLoadData.class,
	TestQueryStore.class
})
public class TestSuite { }
