package org.inqle.test.http.lookup;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.http.lookup.OwlSubclassLookup;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexBuilderModel;
import com.hp.hpl.jena.query.larq.IndexBuilderSubject;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class TestLookupSubclasses {

	private static final Logger log = Logger.getLogger(TestLookupSubclasses.class);
	private static final String SEARCH_TERM = "city";
	private static final String SCHEMA_FILES_DIRECTORY = "C:/workspace/InqleCentralServerFeature/dist_root/assets/rdf/schemas";
	private static final String MINIMUM_SCORE_THRESHOLD = "0.01";

	@Test
	public void querySchemaFilesForSubclasses() {
		QueryCriteria queryCriteria = new QueryCriteria();
		//add any internal RDF schemas
//		addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
		log.info("Get/Create index of Model...");
		IndexLARQ textIndex =  getSchemaFilesSubjectIndex();
		Iterator<?> searchResultI = textIndex.search(SEARCH_TERM);
		log.info("Searched SchemaFiles index for '" + SEARCH_TERM + "'...");
		while(searchResultI.hasNext()) {
			HitLARQ hit = (HitLARQ)searchResultI.next();
			log.info("Found result: " + hit.getNode() + "; score=" + hit.getScore());
		}
		if (textIndex != null) {
			queryCriteria.setTextIndex(textIndex);
		}
		
		log.info("Get/Create OntModel...");
		OntModel schemaModel = getOntModel(SCHEMA_FILES_DIRECTORY);
		schemaModel.setStrictMode(true);
		queryCriteria.setSingleModel(schemaModel);
		
//		String sparql = OwlSubclassLookup.getSparqlSearchRdfSubclasses(SEARCH_TERM, null, 10, 0);
		String sparql = getSparqlSearchRdfSubclasses(SEARCH_TERM, null, 10, 0);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String matchingClassesText = Queryer.selectText(queryCriteria);
		log.info("matchingClassesXml=" + matchingClassesText);
	}

	private IndexLARQ getSchemaFilesSubjectIndex() {
		Model schemaFilesModel = getModel(SCHEMA_FILES_DIRECTORY);
		IndexBuilderModel larqBuilder = new IndexBuilderSubject();
		log.info("Persister.getSchemaFilesSubjectIndex(): indexing model of " + schemaFilesModel.size() + " statements...");
		larqBuilder.indexStatements(schemaFilesModel.listStatements());
		log.info("...done");
		IndexLARQ schemaFilesSubjectIndex = larqBuilder.getIndex();
		return schemaFilesSubjectIndex;
	}
	
	private static void addModel(Model model, String folderPath) {
		File folder = new File(folderPath);
		for (File file: folder.listFiles()) {
			if (file.isDirectory()) {
				addModel(model, file.getAbsolutePath());
			} else {
				log.info("Getting model: " + file.getAbsolutePath());
				try {
					Model newModel = FileManager.get().loadModel(file.getAbsolutePath());
					model.add(newModel);
				} catch (RuntimeException e) {
					log.info("Error reading RDF file:" + file.getAbsolutePath());
				}
			}
		}
	}
	
	public static OntModel getOntModel(String folderPath) {
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
		addModel(ontModel, folderPath);
		return ontModel;
	}
	
	/**
	 * Gets an Model, containing all data files contained in the specified 
	 * folder path or in any subfolders.
	 * @param folderPath
	 * @return
	 */
	public static Model getModel(String folderPath) {
		Model model = ModelFactory.createDefaultModel();
		addModel(model, folderPath);
		return model;
	}
	
	public static String getSparqlSearchRdfSubclasses(String searchTerm, String owlClassUri, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"PREFIX skos: <" + RDF.SKOS + ">\n" +
			"SELECT DISTINCT ?URI ?Label ?Comment \n" +
//			"{ GRAPH ?g {\n" +
			"WHERE {\n" +
//			"?URI rdf:type skos:ConceptScheme \n";
//			"?URI rdfs:subPropertyOf rdfs:label \n";
			"(?URI ?Score) pf:textMatch ( '" + searchTerm + "' " + MINIMUM_SCORE_THRESHOLD + " ) \n" +
		". FILTER ( isURI(?URI) ) \n";	
		if (owlClassUri != null) {
				sparql += ". ?URI rdfs:subClassOf <" + owlClassUri + "> \n";
			} else {
//				sparql += ". ?URI rdf:type rdfs:Class \n";
				sparql += ". OPTIONAL {?URI rdfs:subPropertyOf ?superProperty }\n" +
//				". ?type a rdf:Property }\n" +
				". FILTER ( ! bound(?superProperty) ) \n";
//				sparql += ". ?someInstance a ?URI \n";
			}
			
		sparql += ". OPTIONAL { ?URI rdfs:label ?Label }\n" +
//		sparql += ". OPTIONAL { ?URI skos:prefLabel ?Label }\n" +
//		sparql += ". OPTIONAL { ?URI ?labelProperty ?Label \n" +
//				"    . ?labelProperty rdfs:subPropertyOf rdfs:label }\n" +
//		sparql += ". ?URI rdfs:label ?Label \n" +
			". OPTIONAL { ?URI rdfs:comment ?Comment } \n" +
			". OPTIONAL { ?URI skos:definition ?Comment } \n" +
//			"} } ORDER BY DESC(?Score) \n" +
			"}";
//			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
}
