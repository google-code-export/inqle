package org.inqle.test.data;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.load.Loader;
import org.inqle.data.rdf.jenabean.Persister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.hp.hpl.jena.rdf.model.Model;

@RunWith(Parameterized.class)
public class TestLoadData {

	private static final String SAMPLE_DATA_FILE_1 = "org.inqle.data.rdf.jenabean/src/test/data/geo-states.n3";
	//private static final String SAMPLE_DATA_FILE_2 = "org.inqle.data.rdf.jenabean/src/test/data/geo-congressional_districts_110.n3";
	private static final String DEFAULT_URI = RDF.INQLE;
	private String filePath;

	static Logger log = Logger.getLogger(TestLoadData.class);
	
	public TestLoadData(String filePath) {
		this.filePath = filePath;
	}
	
	@Parameters
	public static Collection<Object[]> getQueries() {
		return Arrays.asList(new Object[][] {
			{SAMPLE_DATA_FILE_1 }
			//,{SAMPLE_DATA_FILE_2 }
		});
	}
	
	@Test
	public void load() {
		//get the test model
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		Persister persister = Persister.getInstance(appInfo);
		Model testModel = persister.getModel(TestCreateStores.TEST_DATAMODEL_ID);
		
		//assume the model begins blank
		long origSize = testModel.size();
		
		Loader loader = new Loader(testModel);
		String fullFilepath = AppInfoProvider.APP_HOME + filePath;
		log.info("Loading file " + filePath + "...");
		loader.loadFile(fullFilepath, DEFAULT_URI);
		
		//the model should now contain statements
		assert(testModel.size() > origSize);
		testModel.close();
	}
}
