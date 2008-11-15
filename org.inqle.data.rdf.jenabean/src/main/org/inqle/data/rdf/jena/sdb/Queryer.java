package org.inqle.data.rdf.jena.sdb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.RdfTable;
import org.inqle.data.rdf.jena.util.ArcSparqlBuilder;
import org.inqle.data.rdf.jena.util.Converter;
import org.inqle.data.rdf.jenabean.ArcSet;

import thewebsemantic.TypeWrapper;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.query.larq.LARQ;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
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
	
	private static Logger log = Logger.getLogger(Queryer.class);
	
	private static QueryExecution getQueryExecution(QueryCriteria queryCriteria) {
		Query query;
		try {
			query = QueryFactory.create(queryCriteria.getQuery(), RDF.INQLE, Syntax.syntaxARQ);
		} catch (Exception e) {
			log.error("Error parsing SPARQL query:" + queryCriteria.getQuery(), e);
			return null;
		}
		log.debug("Querying w/ SPARQL:\n" + queryCriteria.getQuery());
		
		//log info about the dataset
//		Iterator<?> datasetNamesI = dataset.listNames();
//		log.debug("Querying dataset '" + dataset.toString() + "' with names...");
//		while (datasetNamesI.hasNext()) {
//			log.debug((String)datasetNamesI.next());
//		}
		
//		Iterator<?> dsNames = dataset.listNames();
//		while (dsNames.hasNext()) {
//			log.debug("Querying model '" + (String)dsNames.next() + "'.");
//		}
		QueryExecution qe = null;
//		if (queryCriteria.getSingleModel() != null) {
//			qe = QueryExecutionFactory.create(query, queryCriteria.getSingleModel()) ;
//		} else {
//			qe = QueryExecutionFactory.create(query, queryCriteria.getDataset()) ;
//		}
		
		qe = QueryExecutionFactory.create(query, queryCriteria.getDataset()) ;
		
		if (queryCriteria.getTextIndex() != null) {
			log.trace("setDefaultIndex...");
			LARQ.setDefaultIndex(qe.getContext(), queryCriteria.getTextIndex());
		}
		
		qe.getContext().set(SDB.unionDefaultGraph, true);
		return qe;
	}
	
	/**
	 * Query an ARQ dataset.

	 * @param queryCriteria the QueryCriteria object containing all info about the query
	 * @return an ResultSet object
	 */
	public static String selectXml(QueryCriteria queryCriteria) {
		QueryExecution qe = getQueryExecution(queryCriteria);

		//Do the query
		ResultSet resultSet = null;
		//SDB.getContext().set(SDB.unionDefaultGraph, true);
		String resultXml = null;
		try {
			resultSet = qe.execSelect() ;
			resultXml = ResultSetFormatter.asXMLString(resultSet);
		} catch (Exception e) {
			log.error("Error performing query " + queryCriteria.getQuery(), e);
		} finally {
			if (qe != null) qe.close();
		}
		
		return resultXml;
	}
	
	/**
	 * Query an ARQ dataset.

	 * @param queryCriteria the QueryCriteria object containing all info about the query
	 * @return an ResultSet object
	 */
	public static String selectText(QueryCriteria queryCriteria) {
		QueryExecution qe = getQueryExecution(queryCriteria);

		//Do the query
		ResultSet resultSet = null;
		//SDB.getContext().set(SDB.unionDefaultGraph, true);
		String resultText = null;
		try {
			resultSet = qe.execSelect() ;
			resultText = ResultSetFormatter.asText(resultSet);
		} catch (Exception e) {
			log.error("Error performing query " + queryCriteria.getQuery(), e);
		} finally { 
			if (qe != null) qe.close(); 
		}
		
		return resultText;
	}

	/**
	 * Query a dataset and return results as RDF

	 * @param queryCriteria the QueryCriteria object containing all info about the query
	 * @return an ResultSet object
	 */
	public static Model selectRdf(QueryCriteria queryCriteria) {
		QueryExecution qe = getQueryExecution(queryCriteria);
		
		//Do the query
		ResultSet resultSet = null;
		//SDB.getContext().set(SDB.unionDefaultGraph, true);
		Model resultModel = ModelFactory.createDefaultModel();
		try {
			resultSet = qe.execSelect() ;
			ResultSetFormatter.asRDF(resultModel, resultSet);
		} catch (Exception e) {
			log.error("Error performing query " + queryCriteria.getQuery(), e);
		} finally { 
			if (qe != null) qe.close(); 
		}
		
		return resultModel;
	}
	
	/**
	 * Perform a CONSTRUCT query on a dataset and return results as RDF
	 * e.g. CONSTRUCT * WHERE ( ?x vcard:FN ?name )
	 * @param queryCriteria the QueryCriteria object containing all info about the query
	 * @return an ResultSet object
	 */
	public static Model constructRdf(QueryCriteria queryCriteria) {
		QueryExecution qe = getQueryExecution(queryCriteria);
		
		//Do the query
		Model resultModel = null;
		try {
			resultModel = qe.execConstruct() ;
		} catch (Exception e) {
			log.error("Error performing query " + queryCriteria.getQuery(), e);
		} finally { 
			if (qe != null) qe.close(); 
		}
		
		return resultModel;
	}
	
	/**
	 * Query an ARQ dataset.

	 * @param queryCriteria the QueryCriteria object containing all info about the query
	 * @return an RdfTable object
	 */
	public static RdfTable selectRdfTable(QueryCriteria queryCriteria) {
//		Query query;
		RdfTable resultTable = new RdfTable();
		resultTable.setQueryCriteria(queryCriteria);
//		assert(queryCriteria.getQuery() != null);
//		try {
//			query = QueryFactory.create(queryCriteria.getQuery());
//		} catch (Exception e) {
//			resultTable.setError(e);
//			log.error("Error parsing SPARQL query:" + queryCriteria.getQuery(), e);
//			return resultTable;
//		}
//		log.debug("Querying w/ SPARQL:\n" + queryCriteria.getQuery());
//		Dataset dataset = queryCriteria.getDataset();
//		
//		//log info about the dataset
//		Iterator<?> datasetNamesI = dataset.listNames();
//		log.debug("Querying dataset '" + dataset.toString() + "' with names...");
//		while (datasetNamesI.hasNext()) {
//			log.debug((String)datasetNamesI.next());
//		}
//		
//		Iterator<?> dsNames = dataset.listNames();
//		while (dsNames.hasNext()) {
//			log.debug("Querying model '" + (String)dsNames.next() + "'.");
//		}
//		
//		QueryExecution qe = QueryExecutionFactory.create(query, dataset) ;
//		
//		if (queryCriteria.getTextIndex() != null) {
//			log.trace("setDefaultIndex...");
//			LARQ.setDefaultIndex(qe.getContext(), queryCriteria.getTextIndex());
//		}
//		
//		qe.getContext().set(SDB.unionDefaultGraph, true) ;
//		
//		
		QueryExecution qe = getQueryExecution(queryCriteria);
		
		//Do the query
		ResultSet resultSet;
		//SDB.getContext().set(SDB.unionDefaultGraph, true);
		try {
			resultSet = qe.execSelect() ;
//			log.debug("Got results? " + resultSet.hasNext() + "; Has these vars: " + resultSet.getResultVars());
			//this sets resultSet.hasNext() to false: ResultSetFormatter.out(resultSet) ;
			resultTable = Converter.resultSetToRdfTable(resultSet);
			resultTable.setQueryCriteria(queryCriteria);
			log.debug("Retrieved RdfTable w/ " + resultTable.getResultList().size() + " results");
		} catch (Exception e) {
			log.error("Error performing query " + queryCriteria.getQuery(), e);
			resultTable.setError(e);
		} finally { 
			if (qe != null) qe.close(); 
		}
		
		
		//close the models in the QueryCriteria object
		//queryCriteria.close();
		
		return resultTable;
	}
	
	public static List<String> selectUriList(QueryCriteria queryCriteria) {
		Query query;
		List<String> resultList = new ArrayList<String>();
		QueryExecution qe = getQueryExecution(queryCriteria);
		
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
			log.error("Error performing query " + queryCriteria.getQuery(), e);
		} finally { 
			if (qe != null) qe.close(); 
		}
		
		//close the models in the QueryCriteria object
		//queryCriteria.close();
		
		return resultList;
	}
	
	public static List<String> selectSimpleList(QueryCriteria queryCriteria, String varName) {
		Query query;
		List<String> resultList = new ArrayList<String>();
		QueryExecution qe = getQueryExecution(queryCriteria);
		
		//Do the query
		ResultSet resultSet;
		try {
			resultSet = qe.execSelect() ;
			log.trace("Got results? " + resultSet.hasNext() + "; Has these vars: " + resultSet.getResultVars());
			//this sets resultSet.hasNext() to false: ResultSetFormatter.out(resultSet) ;
			resultList = Converter.resultSetToSimpleList(resultSet, varName);
			log.trace("Retrieved simple List w/ " + resultList.size() + " results");
		} catch (Exception e) {
			log.error("Error performing query " + queryCriteria.getQuery(), e);
		} finally { 
			if (qe != null) qe.close(); 
		}
		
		return resultList;
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
		String sparql = Queryer.getSparqlJenabeanId(clazz, arcSet);
		queryCriteria.setQuery(sparql);
		List<String> results = selectSimpleList(queryCriteria, "id");
		return results;
	}
	
	public static String decorateSparql(String baseSparql, boolean randomize, int offset, int limit) {
		String sparql = "";
		if (randomize) {
			sparql += "PREFIX inqle-fn: <java:org.inqle.data.rdf.jena.fn.> \n";
		}
		sparql += baseSparql;
		if (randomize) {
			sparql += " ORDER BY inqle-fn:Rand() \n";
		}
		sparql += " LIMIT " + limit + " OFFSET " + offset + "\n";
		return sparql;
	}

	/**
	 * Gets SPARQL query which will retrieve the ID of every Jenabean
	 * RDF object which is of the provided class and matches the provided
	 * arcs
	 * @param clazz the provided class
	 * @param arcSet
	 * @return
	 * I don't think this is used
	 */
	@Deprecated
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
		sparql += ArcSparqlBuilder.getSparqlWhereFromArcs("?uri", arcSet);
		sparql += "} }\n";
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
