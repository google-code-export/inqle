/**
 * 
 */
package org.inqle.test.user.data.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.sampling.DataTable;
import org.inqle.data.sampling.DataTableWriter;
import org.inqle.data.sampling.SimpleSparqlSampler;
import org.inqle.test.data.AppInfoProvider;
import org.junit.Test;

/**
 * @author David Donohue
 * Jan 2, 2008
 */
public class TestSimpleSparqlSampler {

	static Logger log = Logger.getLogger(TestSimpleSparqlSampler.class);

	@Test
	public void testRunDESAutomatically() {
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		Persister persister = Persister.createPersister(appInfo);
		assertEquals(appInfo.getId(), AppInfo.APPINFO_INSTANCE);
		
		SimpleSparqlSampler sss = new SimpleSparqlSampler();
		assertNotNull(sss);
		
		//get list of choosable datamodels
		DataTable resultTable = sss.execute(persister);
		//assertNotNull(sss.getAvailableNamedModels());
		assertNotNull(sss.getSelectedNamedModels());
		//Collection<String> availablePredicates = sss.getAvailablePredicates();
		//assertEquals(true, availablePredicates.size() > 5);
		Collection<String> selectedPredicates = sss.getSelectedPredicates();
		assertEquals(true, (selectedPredicates.size() >= SimpleSparqlSampler.MINIMUM_LEARNABLE_PREDICATES && selectedPredicates.size() <= SimpleSparqlSampler.MAXIMUM_LEARNABLE_PREDICATES));
		assertEquals(selectedPredicates.size() + 1, sss.getDataColumns().length);
		assertEquals(true, selectedPredicates.size() > 0);
		assertNotNull(resultTable);
		log.info(DataTableWriter.dataTableToString(resultTable));
		log.info(JenabeanWriter.toString(sss));
	}
}
