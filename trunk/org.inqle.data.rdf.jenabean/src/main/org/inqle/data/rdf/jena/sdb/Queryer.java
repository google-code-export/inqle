package org.inqle.data.rdf.jena.sdb;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.util.Converter;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.rdf.jenabean.ArcSet;
import org.inqle.data.rdf.jenabean.ArcStep;

import thewebsemantic.TypeWrapper;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDB;

/**
 * This class is capable of querying 1 or more models.  
 * @author David Donohue
 * Jul 16, 2007
 * 
 * TODO rename to SDBQueryer, implementing IQueryer interface.
 * TODO add a QueryerFactory, which creates appropriate queryer implementation
 * TODO consider remove any SDB dependence (? might have no effect)
 * @see http://jena.hpl.hp.com/wiki/SDB/JavaAPI
 * 
 */
	public class Queryer {
	
	static Logger log = Logger.getLogger(Queryer.class);
	
	/**
	 * Query an ARQ dataset.  Generally SPARQL queries sent to this method should reference
	 * graphs lest the query be issued against only the default graph,
	 * (which might mean no graph at all is queried).
	 * 
	 * Example query:
	 * SELECT ?uri ?dbType ?dbDriver ?dbUrl ?dbUser ?creationDate
{
GRAPH ?g {
?uri a <http://jena.hpl.hp.com/2005/11/Assembler#Connection> .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbType> ?dbType .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbClass> ?dbDriver .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbURL> ?dbUrl .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbUser> ?dbUser .
?uri <http://inqle.org/ns/1.0/inqle.rdf#creationTime> ?creationDate
} }

	 * @param queryCriteria the QueryCriteria object containing all info about the query
	 * @return an Rdftable object
	 * 
	 * TODO verify that this is doing SDB queries & not slower ARQ
	 */
	public static RdfTable selectRdfTable(QueryCriteria queryCriteria) {
		Query query;
		RdfTable resultTable = new RdfTable();
		resultTable.setQueryCriteria(queryCriteria);
		assert(queryCriteria.getQuery() != null);
		try {
			query = QueryFactory.create(queryCriteria.getQuery());
		} catch (Exception e) {
			resultTable.setError(e);
			log.error("Error parsing SPARQL query:" + queryCriteria.getQuery(), e);
			return resultTable;
		}
		log.debug("Querying w/ SPARQL:\n" + queryCriteria.getQuery());
		Dataset dataset = queryCriteria.getDataset();
		
		//log info about the dataset
		Iterator<?> datasetNamesI = dataset.listNames();
		log.debug("Querying dataset '" + dataset.toString() + "' with names...");
		while (datasetNamesI.hasNext()) {
			log.debug((String)datasetNamesI.next());
		}
		
		Iterator<?> dsNames = dataset.listNames();
		while (dsNames.hasNext()) {
			log.debug("Querying model '" + (String)dsNames.next() + "'.");
		}
		
		QueryExecution qe = QueryExecutionFactory.create(query, dataset) ;
		qe.getContext().set(SDB.unionDefaultGraph, true) ;
		
		//Do the query
		ResultSet resultSet;
		//SDB.getContext().set(SDB.unionDefaultGraph, true);
		try {
			resultSet = qe.execSelect() ;
			log.debug("Got results? " + resultSet.hasNext() + "; Has these vars: " + resultSet.getResultVars());
			//this sets resultSet.hasNext() to false: ResultSetFormatter.out(resultSet) ;
			resultTable = Converter.resultSetToRdfTable(resultSet);
			resultTable.setQueryCriteria(queryCriteria);
			log.debug("Retrieved RdfTable w/ " + resultTable.getResultList().size() + " results");
		} catch (Exception e) {
			log.error("Error performing query " + query, e);
			resultTable.setError(e);
		} finally { 
			qe.close(); 
		}
		
		
		//close the models in the QueryCriteria object
		//queryCriteria.close();
		
		return resultTable;
	}
	
	public static List<String> selectUriList(QueryCriteria queryCriteria) {
		Query query;
		List<String> resultList = new ArrayList<String>();
		try {
			query = QueryFactory.create(queryCriteria.getQuery());
		} catch (Exception e) {
			log.error("Error parsing SPARQL query:" + queryCriteria.getQuery(), e);
			return resultList;
		}
		log.debug("Querying w/ SPARQL:\n" + queryCriteria.getQuery());
		Dataset dataset = queryCriteria.getDataset();
		
		//log info about the dataset
		Iterator<?> datasetNamesI = dataset.listNames();
		log.debug("Querying dataset '" + dataset.toString() + "' with names...");
		while (datasetNamesI.hasNext()) {
			log.debug((String)datasetNamesI.next());
		}
		
		QueryExecution qe = QueryExecutionFactory.create(query, dataset) ;
		qe.getContext().set(SDB.unionDefaultGraph, true) ;
		
		//Do the query
		ResultSet resultSet;
		//SDB.getContext().set(SDB.unionDefaultGraph, true);
		try {
			resultSet = qe.execSelect() ;
			log.debug("Got results? " + resultSet.hasNext() + "; Has these vars: " + resultSet.getResultVars());
			//this sets resultSet.hasNext() to false: ResultSetFormatter.out(resultSet) ;
			resultList = Converter.resultSetToUriList(resultSet);
			log.debug("Retrieved URI List w/ " + resultList.size() + " results");
		} catch (Exception e) {
			log.error("Error performing query " + query, e);
		} finally { 
			qe.close(); 
		}
		
		//close the models in the QueryCriteria object
		//queryCriteria.close();
		
		return resultList;
	}
	
	public static List<String> selectSimpleList(QueryCriteria queryCriteria, String varName) {
		Query query;
		List<String> resultList = new ArrayList<String>();
		try {
			query = QueryFactory.create(queryCriteria.getQuery());
		} catch (Exception e) {
			log.error("Error parsing SPARQL query:" + queryCriteria.getQuery(), e);
			return resultList;
		}
		log.debug("Querying w/ SPARQL:\n" + queryCriteria.getQuery());
		Dataset dataset = queryCriteria.getDataset();
		
		//log info about the dataset
		Iterator<?> datasetNamesI = dataset.listNames();
		log.debug("Querying dataset '" + dataset.toString() + "' with names...");
		while (datasetNamesI.hasNext()) {
			log.debug((String)datasetNamesI.next());
		}
		
		QueryExecution qe = QueryExecutionFactory.create(query, dataset) ;
		qe.getContext().set(SDB.unionDefaultGraph, true) ;
		
		//Do the query
		ResultSet resultSet;
		try {
			resultSet = qe.execSelect() ;
			log.info("Got results? " + resultSet.hasNext() + "; Has these vars: " + resultSet.getResultVars());
			//this sets resultSet.hasNext() to false: ResultSetFormatter.out(resultSet) ;
			resultList = Converter.resultSetToSimpleList(resultSet, varName);
			log.info("Retrieved simple List w/ " + resultList.size() + " results");
		} catch (Exception e) {
			log.error("Error performing query " + query, e);
		} finally { 
			qe.close(); 
		}
		
		return resultList;
	}
	
	/**
	 * Query an SDB or ARQmodel.  SPARQL queries sent to this method should 
	 * not reference graph names.  Rather, the sole model is the default model.
	 * 
	 * Example query:
SELECT ?uri ?dbType ?dbDriver ?dbUrl ?dbUser ?creationDate
{
?uri a <http://jena.hpl.hp.com/2005/11/Assembler#Connection> .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbType> ?dbType .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbClass> ?dbDriver .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbURL> ?dbUrl .
?uri <http://jena.hpl.hp.com/2005/11/Assembler#dbUser> ?dbUser .
?uri <http://inqle.org/ns/1.0/inqle.rdf#creationTime> ?creationDate
}

	 * @param queryCriteria the QueryCriteria object containing all info about
	 * Should contain a single model
	 * @return an Rdftable object
	 * 
	 * @deprecated
	 */
	public static RdfTable selectFromSingleModel(QueryCriteria queryCriteria) {
		Query query;
		try {
			query = QueryFactory.create(queryCriteria.getQuery());
		} catch (Exception e) {
			log.error("Error parsing SPARQL query:" + queryCriteria.getQuery(), e);
			return null;
		}
		log.debug("Querying w/ SPARQL:\n" + queryCriteria.getQuery());
		Model model = queryCriteria.getModel();
		
		QueryExecution qe = QueryExecutionFactory.create(query, model) ;
		RdfTable rdfTable = new RdfTable();
		
		ResultSet resultSet;
		try {
			resultSet = qe.execSelect() ;
			//ResultSetFormatter.out(qryResults) ;
			rdfTable = Converter.resultSetToRdfTable(resultSet);
			log.debug("Retrieved RdfTable w/ " + rdfTable.getResultList().size() + " results");
		} catch (Exception e) {
			log.error("Error performing query " + query, e);
		} finally { 
			qe.close(); 
		}
		rdfTable.setQueryCriteria(queryCriteria);
		
		//close the models in the QueryCriteria object
		//queryCriteria.close();
		
		return rdfTable;
	}
	
	/**
	 * Get list of Jenabean IDs which match the provided class and the 
	 * provided ArcSet
	 * @param clazz
	 * @param arcSet the ArcSet object, containing how to restrict the 
	 * list
	 * @return
	 */
	public static List<String> getJenabeanIds(Class<?> clazz, ArcSet arcSet, QueryCriteria queryCriteria) {
		String sparql = getSparqlJenabeanId(clazz, arcSet);
		queryCriteria.setQuery(sparql);
		List<String> results = selectSimpleList(queryCriteria, "id");
		return results;
	}
	
	/**
	 * Gets SPARQL query which will retrieve the ID of every Jenabean
	 * RDF object which is of the provided class and matches the provided
	 * arcs
	 * @param clazz the provided class
	 * @param arcSet
	 * @return
	 */
	public static String getSparqlJenabeanId(Class<?> clazz, ArcSet arcSet) {
		TypeWrapper classWrapper = TypeWrapper.wrap(clazz);
		//String classBaseUri = classWrapper.inspect();
		String classBaseUri = classWrapper.typeUri();
		String classUri = classBaseUri + clazz.getCanonicalName();
		String idPredicate = classBaseUri + RDF.JENABEAN_ID_ATTRIBUTE;
		String sparql = "PREFIX ja: <" + RDF.JA + ">\n"; 
		sparql += "select ?id {\n";
		sparql += "GRAPH ?g {\n";
		sparql += "?uri a <" + classUri + "> \n";
		sparql += " . ?uri <" + idPredicate + "> ?id \n";
		sparql += getSparqlWhereFromArcs("?uri", arcSet);
		sparql += "} }\n";
		return sparql;
	}
	
	private static String getSparqlWhereFromArcs(String subject, ArcSet arcSet) {
		String sparql = "";
		for (Arc arc: arcSet.getArcs()) {
			sparql += getSparqlWhereFromArc(subject, arc);
		}
		return sparql;
	}

	private static String getSparqlWhereFromArc(String subject, Arc arc) {
		String sparql = "";
		String subjectStr = "";
		String newNode = subject;
		String lastNode = subject;
		for (ArcStep step: arc.getArcSteps()) {
			int stepType = step.getStepType();
			String objectStr = "";
			String predicate = step.getPredicate();
			Object object = step.getObject();
			
			if (object == null) {
				if (stepType == ArcStep.OUTGOING) {
					newNode = "?out_" + UUID.randomUUID().toString();
					objectStr = newNode;
					subjectStr = lastNode;
				} else {//stepType == ArcStep.INCOMING
					newNode = "?in_" + UUID.randomUUID().toString();
					objectStr = lastNode;
					subjectStr = newNode;
					lastNode = newNode;
				}
			} else if (object instanceof URI) {
				subjectStr = lastNode;
				objectStr = "<" + ((URI)object).toString() + ">";
			} else if (object instanceof String) {
				subjectStr = lastNode;
				objectStr = "\"" + object.toString() + "\"";
			} else {
				subjectStr = lastNode;
				objectStr = object.toString();
			}
			sparql += " . " + subjectStr + " <" + predicate + "> " + objectStr;
		}
		return sparql;
	}
	
	/**
	 * Retrieve a Collection of jenabeans which match the provided 
	 * list of ArcSet, from the specified model
	 * @param clazz
	 * @param model
	 * @return
	 */
//	public Collection<?> reconstituteList(Class<?> clazz, ArcSet arcSet, Model model, Persister persister) {
//		RDF2Bean loader = new RDF2Bean(model);
//		Collection<?> objects = loader.load(clazz);
//		//log.debug("Retrieved these Connections:" + connections);
//		return objects;
//	}
	
}
