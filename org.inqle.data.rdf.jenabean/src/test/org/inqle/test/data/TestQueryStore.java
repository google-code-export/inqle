package org.inqle.test.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestQueryStore {

	private static final String SPARQL_NULL = null;
	private static final String SPARQL_BLANK = "";
	private static final String SPARQL_SYNTAX_ERROR = "select {this query has syntax error.}";
	private static final String SPARQL_NO_RESULTS = "select $s { $s $s $s }";
	private static final String PREFIXES = RDF.getPrefixClause("rdf", RDF.RDF)
		+ RDF.getPrefixClause("usgovt", "tag:govshare.info,2005:rdf/usgovt/")
		+ RDF.getPrefixClause("dc", RDF.DC)
		+ RDF.getPrefixClause("geo", RDF.GEO);
	private static final String SPARQL_1_RESULT = PREFIXES + 
		"select $state_name \n" +
		"{ GRAPH ?anyGraph { \n " +
			"$state rdf:type usgovt:State . \n " +
			"$state dc:title $state_name . \n " +
			"$state geo:lat $latitude . \n " +
			"FILTER ($latitude > 61) \n " +
		"} } ";
	private static final String SPARQL_50_RESULTS = PREFIXES + 
	"select ?state \n" +
	//"FROM NAMED <" + TestCreateStores.TEST_DATAMODEL_URI + ">\n" +
	"{ GRAPH ?anyGraph { " +
		"?state rdf:type usgovt:State " +
	"} } ";
	
	private static final String SPARQL_MANY_RESULTS =  
	"SELECT ?s \n" +
	//"FROM NAMED <" + TestCreateStores.TEST_DATAMODEL_URI + ">\n" +
	"{ GRAPH ?anyGraph { " +
		"?s ?p ?o " +
	"} } ";
	
	private String sparql;
	private int resultCount;
	private boolean syntaxError;

	static Logger log = Logger.getLogger(TestQueryStore.class);
	
	public TestQueryStore(String sparql, int resultCount, boolean syntaxError) {
		this.sparql = sparql;
		this.resultCount = resultCount;
		this.syntaxError = syntaxError;
	}
	
	@Parameters
	public static Collection<Object[]> getQueries() {
		return Arrays.asList(new Object[][] {
			{SPARQL_NULL, 0, true },
			{SPARQL_BLANK, 0, true },
			{SPARQL_SYNTAX_ERROR, 0, true },
			{SPARQL_NO_RESULTS, 0, false },
			{SPARQL_MANY_RESULTS, -1, false },
			{SPARQL_50_RESULTS, 50, false },
			{SPARQL_1_RESULT, 1, false },
		});
	}
	
	@Test
	public void queryStore() {
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		Persister persister = Persister.createPersister(appInfo);
		QueryCriteria queryCriteria = new QueryCriteria(persister);
		//NamedModel testDatamodel = (NamedModel)persister.reconstitute(NamedModel.class, TestCreateStores.TEST_DATAMODEL_ID, persister.getRepositoryModel(), true);
		NamedModel testDatamodel = (NamedModel)persister.getNamedModel(TestCreateStores.TEST_DATAMODEL_ID);
		assert(testDatamodel != null);
		queryCriteria.addNamedModel(testDatamodel);
		queryCriteria.setQuery(this.sparql);
		RdfTable resultTable = Queryer.selectRdfTable(queryCriteria);
		log.info("Query Text:" + resultTable.getQuery());
		log.info("Query Error:" + resultTable.getError());
		log.info("Query Count:" + resultTable.countResults());
		assertEquals(this.syntaxError, (resultTable.getError() != null));
		int numResults = resultTable.countResults();
		//resultCount < 0 means many results
		if (this.resultCount < 0) {
			assertTrue(numResults > 0);
		} else {
			assertEquals(this.resultCount, numResults);
		}
	}
}
