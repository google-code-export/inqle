package org.inqle.test.restlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.qs.Constants;
import org.inqle.util.FileReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.object.ObjectConnection;
import org.openrdf.repository.object.ObjectRepository;
import org.openrdf.repository.object.config.ObjectRepositoryFactory;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;



public class TestCreateRepo {

	@BeforeClass
	public static void setup() {
		
	}
	
	private File rdfFile = new File(FileReader.getAbsolutePath("rdf/WildlifeOntology.rdf"));
	private File test1Dir = new File(FileReader.getAbsolutePath("repos/test1"));
	private File test2Dir = new File(FileReader.getAbsolutePath("repos/test2"));
	private static Logger log = Logger.getLogger(TestCreateRepo.class);
	
//	WORKS:
	
	@Test
	public void testCreateRepo() {
		Repository repo = new SailRepository(new MemoryStore(test1Dir));
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
		
		RepositoryConnection repoconn = null;
		try {
			repoconn = repo.getConnection();
			repoconn.clear();
			repoconn.add(rdfFile, Constants.NS, RDFFormat.RDFXML);
			 RepositoryResult<Statement> statements = repoconn.getStatements(null, null, null, true);
			int i = countStatements(statements);
			log.info("Found " + i + " statements");
			 assertEquals(380, i);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				repoconn.close();
				repo.shutDown();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}
	
//	WORKS: 
	
	@Test
	public void testLoadRepo() {
		Repository repo = new SailRepository(new MemoryStore(test1Dir));
		try {
			repo.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
			fail();
		}
		
		RepositoryConnection repoconn = null;
		try {
			repoconn = repo.getConnection();
			 RepositoryResult<Statement> statements = repoconn.getStatements(null, null, null, true);
			int i = countStatements(statements);
			log.info("Found " + i + " statements");
			 assertEquals(380, i);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				repoconn.close();
				repo.shutDown();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}

//	FAILS: @Test
	public void testQuerySparqlEndpoint() {
//		HTTPRepository sparqlEndpoint = new HTTPRepository(Constants.SPARQLENDPOINT_WILDLIFEFINDER, "");
		SPARQLRepository sparqlRepo = new SPARQLRepository(Constants.SPARQLENDPOINT_FACTFORGE);
		try {
			sparqlRepo.initialize();
			log.info("Got SPARQLRepository: initialized? " + sparqlRepo.isInitialized());
		} catch (RepositoryException e1) {
			e1.printStackTrace();
			fail();
		}

		RepositoryConnection conn = null;
		try {
			conn = sparqlRepo.getConnection();
			String sparqlQuery = 
		         " SELECT * WHERE {?X ?P ?Y} LIMIT 100 ";
	//			log.info("Got RepositoryConnection: empty? " + conn.isEmpty());
			RepositoryResult<Namespace> rr = conn.getNamespaces();
			while (rr.hasNext()) {
				log.info("Found ns: " + rr.next());
			}
			  TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
			  TupleQueryResult result = query.evaluate();
	
			  List<String> bindingNames = result.getBindingNames();
			  log.info("Queried and got bindingNames=" + bindingNames);
			  
			  while (result.hasNext()) {
			    log.info(result.next());
			  }
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
		  try {
			conn.close();
			sparqlRepo.shutDown();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testPersistObjects() throws RepositoryException {
		ObjectConnection objCon = null;
		try {
			Document doc1 = new Document("Document #1 Has this Title", "Document #1 has this body, which is somewhat longer...");
			Document doc2 = new Document("Document #2 Has this Title", "Document #2 has this body, which is somewhat longer...");
			assertFalse(doc2.equals(doc1));
			
			// add a Document to the repository
			Repository repo = new SailRepository(new MemoryStore(test2Dir));
			repo.initialize();
			RepositoryConnection repoCon = repo.getConnection();
			repoCon.setNamespace("", Constants.NS);
			ObjectRepositoryFactory orf = new ObjectRepositoryFactory();
			orf.getConfig().addConcept(Document.class, Constants.NS + "Document/"); 
			ObjectRepository objRepo = orf.createRepository(repo);
			objRepo.initialize();
			
			objCon = objRepo.getConnection();
			ValueFactory vf = objCon.getValueFactory();
			URI uri1 = vf.createURI(Constants.NS + "Document/" + URLEncoder.encode(doc1.getTitle(), "UTF-8"));
			URI uri2 = vf.createURI(Constants.NS + "Document/" + URLEncoder.encode(doc2.getTitle(), "UTF-8"));
			objCon.addObject(uri1, doc1);
			objCon.addObject(uri2, doc2);

			// retrieve a Document by id
			Document doc1a = objCon.getObject(Document.class, uri1);
			Document doc2a = objCon.getObject(Document.class, uri2);
			assertFalse(doc2a.equals(doc1a));
			assertTrue(doc1a.equals(doc1));
			assertTrue(doc2a.equals(doc2));
			
			// remove a Document from the repository
			Document doc1b = objCon.getObject(Document.class, uri1);
//			doc1b.setTitle(null);
			objCon.removeDesignation(doc1b, Document.class);
			
			Document doc1c = null;
			try {
				doc1c = objCon.getObject(Document.class, uri1);
			} catch (Exception e) {
				log.error("Expectedly, unable to retrieve doc: " + uri1);
			}
			assertNull(doc1c);
			Document doc2c = null;
			try {
				doc2c = objCon.getObject(Document.class, uri2);
			} catch (Exception e) {
				log.error("UNexpectedly, unable to retrieve doc: " + uri2);
			}
			assertNotNull(doc2c);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			objCon.close();
		}		
	}

	private int countStatements(RepositoryResult<Statement> statements) throws RepositoryException {
		 int i = 0;
		 while(statements.hasNext()) {
			 Statement stmt = statements.next();
			 log.info(stmt);
			 i++;
		 }
		 return i;
	}

	

}
