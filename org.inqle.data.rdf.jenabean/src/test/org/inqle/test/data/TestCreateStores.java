/**
 * 
 */
package org.inqle.test.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.Dataset;
import org.inqle.data.rdf.jena.ExternalDataset;
import org.inqle.data.rdf.jena.sdb.SDBConnector;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * TODO test persisting serializable members
 * @author David Donohue
 * Dec 10, 2007
 */
public class TestCreateStores {
	
	public static final String DB_NAME = "test_inqle_data";
	public static final String MODEL_NAME = "test_model";
	public static final String TEST_DATAMODEL_ID = "org.inqle.datasets.TestDataset";
	//public static final String TEST_DATAMODEL_URI = RDF.INQLE + TEST_DATAMODEL_ID;
	//public final String URI_DUMMY_1 = RDF.INQLE + "dummy1";
	//public final String URI_DUMMY_2 = RDF.INQLE + "dummy2";
	//public final String HAS_LIST_OF_DUMMY_URIS = RDF.INQLE + this.getClass() + "hasListOfDummyUris";
	
	public static Logger log = Logger.getLogger(TestCreateStores.class);
	
	public TestCreateStores() {	}
	
	@Test
	public void createMetarepositoryModel() {
		
		//create an AppInfo, pointing to this server
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		
		Dataset metarepositoryDataset = appInfo.getMetarepositoryDataset();
		assertNotNull(metarepositoryDataset);
		//assertEquals(repositoryNamedModel.getJavaClass(), Dataset.class.getName());
		
		//create the Persister
		Persister persister = Persister.getInstance(appInfo);
		
		//create the repository model
		log.info("Creating Connection to metarepository of id=" + metarepositoryDataset.getConnectionId());
		//Connection repositoryConnection = persister.getConnection(metarepositoryDataset.getConnectionId());
		Connection repositoryConnection = appInfo.getInternalConnection();
		
		SDBConnector repositoryConnector = new SDBConnector(repositoryConnection);
		//first delete
		repositoryConnector.deleteSDBStore();
		//then create
		repositoryConnector.createSDBStore();
		
		//Model repositoryModel = persister.createDBModel(repositoryConnectionInfo, AppInfo.getRepositoryModelName());
		Model repositoryModel = persister.getMetarepositoryModel();
		
		assertNotNull(repositoryModel);
	}

	@Test
	public void testInsertStatement() {
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		Persister persister = Persister.getInstance(appInfo);
		Model repositoryModel = persister.getMetarepositoryModel();
		log.info("Got repositoryModel");
		//try to add statements
		Resource subj = ResourceFactory.createResource(RDF.UNKNOWN_SUBJECT);
		Property pred = ResourceFactory.createProperty(RDF.TYPE);
		Resource obj = ResourceFactory.createResource(RDF.JAVA_CLASS);
		Statement dummyStatement = ResourceFactory.createStatement(subj, pred, obj);
		log.info("made statement");
		repositoryModel.add(dummyStatement);
		log.info("added repositoryModel");
		RDFNode nullObj = null;
		StmtIterator statements = repositoryModel.listStatements(subj, pred, nullObj);
		log.info("listed statements");
		boolean anyStatements = false;
		while (statements.hasNext()) {
			anyStatements = true;
			Statement stmt = statements.nextStatement();
			log.info("Found statement:" + stmt);
		}
		assertTrue(anyStatements);
		repositoryModel.remove(dummyStatement);
		assertEquals(0, repositoryModel.size());
	}
	
	@Test
	public void createTestConnection() {
		AppInfo appInfo = AppInfoProvider.getAppInfo();
		Connection metarepositoryConnection = appInfo.getInternalConnection();
		Persister persister = Persister.getInstance(appInfo);
		Connection dataConnection = new Connection();
		dataConnection.setDbClass(metarepositoryConnection.getDbClass());
		dataConnection.setDbType(metarepositoryConnection.getDbType());
		dataConnection.setDbUser(metarepositoryConnection.getDbUser());
		dataConnection.setDbPassword(metarepositoryConnection.getDbPassword());
		String dbUrl = metarepositoryConnection.getDbURL().substring(0, metarepositoryConnection.getDbURL().lastIndexOf(":") + 1);
		dbUrl += DB_NAME;
		dataConnection.setDbURL(dbUrl);
		
		//log.info("We have now created a new Conection spec for test data:\n" + JenabeanWriter.toString(testConnectionInfo));
		//clear the SDB store
		//boolean ableToDelete = persister.clearDBConnection(testConnectionInfo);
		//assertEquals(ableToDelete, false);
		
		//try to load the Connection from the blank database
		//Connection testConnectionInfo2 = (Connection)Persister.reconstitute(Connection.class, testConnectionInfo.getId(), repositoryModel, true);
		//assertNull(testConnectionInfo2);
		
		//add the new test datamodel connection info
		log.info("Try to create new DB Connection...");
		int status = persister.createNewDBConnection(dataConnection);
		//assertEquals(SDBConnector.STORE_CREATED, status);
		//status = persister.createNewDBConnection(testConnectionInfo);
		assertEquals(SDBConnector.STORE_IS_BLANK, status);
		
		//try to load the Connection from the database
		log.info("Try to create DB Connection 3...");
		Connection testConnectionInfo3 = (Connection)persister.reconstitute(Connection.class, dataConnection.getId(), true);
		//log.info(PersistableWriter.persistableToString(testConnectionInfo3));
		assertNotNull(testConnectionInfo3);
		
		log.info("create a Dataset object...");
		ExternalDataset testNamedModel = new ExternalDataset();
		testNamedModel.setId(TEST_DATAMODEL_ID);
//		testNamedModel.setModelName(MODEL_NAME);
//		testNamedModel.setConnection(testConnectionInfo);
//		testNamedModel.setId(MODEL_NAME);
		testNamedModel.setConnectionId(dataConnection.getId());
		persister.createDBModel(testNamedModel);
		//persister.persist(testNamedModel, repositoryModel, true);
		log.info("retrieve the Dataset from the metarepository...");
		Dataset reconstitutedTestNamedModel = (Dataset)persister.getNamedModel(TEST_DATAMODEL_ID);
		assertNotNull(reconstitutedTestNamedModel);
		String origNamedModelString = JenabeanWriter.toString(testNamedModel);
		String reconstNamedModelString = JenabeanWriter.toString(reconstitutedTestNamedModel);
		assertEquals(origNamedModelString, reconstNamedModelString);
		
		log.info("get the associated model...");
		Model testModel = persister.getModel(testNamedModel);
		assertNotNull(testModel);
		//Store AppInfo object in this new datamodel
		//try adding a Serializable to the AppInfo object, to test persisting of serializable fields
		//List<String> testUriList = new ArrayList<String>();
		//testUriList.add(URI_DUMMY_1);
		//testUriList.add(URI_DUMMY_2);
		//appInfo.addAttribute(HAS_LIST_OF_DUMMY_URIS, testUriList);
		log.info("persist into this model the AppInfo object...");
		persister.persist(appInfo, testModel, true);
		
		//load this new AppInfo object
		log.info("reconstitute from this model appInfo2...");
		AppInfo appInfo2 = (AppInfo)Persister.reconstitute(AppInfo.class, appInfo.getId(), testModel, true);
		//appInfo3 = AppInfo object, without the IPersistable attributes
		log.info("reconstitute from this model appInfo3...");
		AppInfo appInfo3 = (AppInfo)Persister.reconstitute(AppInfo.class, appInfo.getId(), testModel, false);
		
		/*
		 * appInfo (loaded from config file) and appInfo2 (reconstituted from test store)
		 * and appInfo3 (same as 2 except reconstituted without Persistable attributes) 
		 * should have same URI, String, and Calendar attributes.
		 */
		String strAppInfo1 = JenabeanWriter.toString(appInfo);
		String strAppInfo2 = JenabeanWriter.toString(appInfo2);
		String strAppInfo3 = JenabeanWriter.toString(appInfo3);
		log.info("\n\nAttributes for appInfo #1 (original, loaded from file) =======================");
		log.info(strAppInfo1);
		log.info("\n\nAttributes for appInfo #2 (new, reconstituted from new test DB) =======================");
		log.info(strAppInfo2);
		log.info("\n\nAttributes for appInfo #3 (same as #2 except IPersistable attributes excluded) =======================");
		log.info(strAppInfo3);
		assertEquals(strAppInfo1, strAppInfo2);
		//assertFalse(strAppInfo1.equals(strAppInfo3));

		//TODO test persister.remove() method
		log.info("BEFORE remove(): testModel.size()=" + testModel.size());
		Persister.remove(appInfo, testModel);
		log.info("AFTER remove(): testModel.size()=" + testModel.size());
		//assertEquals(0, testModel.size());
		
		//clear data from the test model
		testModel.removeAll();
		assertEquals(0, testModel.size());
		
		//close the Persister
		//persister.close();
	}
}
