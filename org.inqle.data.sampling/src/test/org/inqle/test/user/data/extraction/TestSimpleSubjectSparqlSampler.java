/**
 * 
 */
package org.inqle.test.user.data.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.sampling.IDataTable;
import org.inqle.data.sampling.SimpleSubjectSparqlSampler;
import org.inqle.test.data.AppInfoProvider;
import org.junit.Test;

/**
 * @author David Donohue
 * Jan 2, 2008
 */
public class TestSimpleSubjectSparqlSampler {

	static Logger log = Logger.getLogger(TestSimpleSubjectSparqlSampler.class);

	@Test
	public void testRunDESAutomatically() {
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		//Persister persister = Persister.getInstance(appInfo);
		assertEquals(appInfo.getId(), AppInfo.APPINFO_INSTANCE_ID);
		
		SimpleSubjectSparqlSampler sss = new SimpleSubjectSparqlSampler();
		assertNotNull(sss);
		
		//get list of choosable datamodels
		IDataTable resultTable = sss.execute();
		assertNotNull(resultTable);
		//assertNotNull(sss.getAvailableNamedModels());
		assertNotNull(sss.getSelectedNamedModels());
		//Collection<String> availablePredicates = sss.getAvailablePredicates();
		//assertEquals(true, availablePredicates.size() > 5);
//		Collection<Arc> selectedArc = sss.get();
//		assertEquals(true, (selectedPredicates.size() >= SimpleSparqlSampler.MINIMUM_LEARNABLE_PREDICATES && selectedPredicates.size() <= SimpleSparqlSampler.MAXIMUM_LEARNABLE_PREDICATES));
//		assertEquals(selectedPredicates.size() + 1, resultTable.getColumns().size());
//		assertEquals(true, selectedPredicates.size() > 0);
//		assertNotNull(resultTable);
//		log.info(DataTableWriter.dataTableToString(resultTable));
		log.info(JenabeanWriter.toString(sss));
	}
}
