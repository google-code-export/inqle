package org.inqle.http.lookup;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;

/**
 * This class gets 2 kinds of properties:
 *  * properties of a particular OWL class
 *    (e.g. can be used to find all properties a subclasses of inqle:DataSubject)
 *  * properties of a subclass of inqle:Data, which itself has a inqle:subject of a particular native
 *    subject class.
 * @author David Donohue
 * Aug 1, 2008
 */
public class OwlPropertyLookup {

	private static final Logger log = Logger.getLogger(OwlPropertyLookup.class);
	
	/**
	 * Generates SPARQL, to find all properties of all instances of all subclasses of inqle:Data, 
	 * which have the provided subjectClassUri as an inqle:subject
	 * That is, this finds properties of data rows having a particular inqle:subject.
	 * @param subjectClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindDataPropertiesAboutSubject(String subjectClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?Property_URI ?Column_Header ?Label ?Comment \n" +
				//datatype(?value) as ?Data_Type
				//". OPTIONAL { ?URI inqle:mapsValue ?value } \n" +
				"{\n" +
				"GRAPH ?g {\n" +
				"?SubjectMappingURI a inqle:SubjectMapping \n" +
				". ?SubjectMappingUri inqle:subjectClass <" + subjectClassUri + "> \n" +
				". ?SubjectMappingUri inqle:dataMappings ?DataMappingUri \n" +
				". ?DataMappingUri inqle:mapsPredicate ?Property_URI \n" +
				". ?DataMappingUri inqle:mapsHeader ?Column_Header \n" +
				". ?DataMappingUri inqle:mapsSubjectType inqle:Data \n" +
				". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
				". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY ASC(?Label) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}
	
	/**
	 * Generates SPARQL, to find all properties that have been mapped to the provided OWL class.
	 * That is, this finds properties of DataSubjects.
	 * @param subjectClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindSubjectProperties(String subjectClassUri, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT DISTINCT ?Property_URI ?Column_Header ?Label ?Comment \n" +
			//datatype(?value) as ?Data_Type
			//". OPTIONAL { ?URI inqle:mapsValue ?value } \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?SubjectMappingURI a inqle:SubjectMapping \n" +
			". ?SubjectMappingUri inqle:subjectClass <" + subjectClassUri + "> \n" +
			". ?SubjectMappingUri inqle:dataMappings ?DataMappingUri \n" +
			". ?DataMappingUri inqle:mapsPredicate ?Property_URI \n" +
			". ?DataMappingUri inqle:mapsHeader ?Column_Header \n" +
			". ?DataMappingUri inqle:mapsSubjectType inqle:DataSubject \n" +
			". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
			". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
			"} } ORDER BY ASC(?Label) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
		}
	
	
	/**
	 * Generates SPARQL, to find all mapped properties of all subclasses of inqle:Data, which have the
	 * provided subjectClassUri as a inqle:subject.
	 * @param subjectClassUri
	 * @param limit
	 * @param offset
	 * @return
	 */
	public static String getSparqlFindAllProperties(String subjectClassUri, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT DISTINCT ?Property_URI ?Subject_Type ?Column_Header ?Label ?Comment \n" +
			//datatype(?value) as ?Data_Type
			//". OPTIONAL { ?URI inqle:mapsValue ?value } \n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?SubjectMappingURI a inqle:SubjectMapping \n" +
			". ?SubjectMappingUri inqle:subjectClass <" + subjectClassUri + "> \n" +
			". ?SubjectMappingUri inqle:dataMappings ?DataMappingUri \n" +
			". ?DataMappingUri inqle:mapsPredicate ?Property_URI \n" +
			". ?DataMappingUri inqle:mapsHeader ?Column_Header \n" +
			". ?DataMappingUri inqle:mapsSubjectType ?Subject_Type \n" +
			". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
			". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
			"} } ORDER BY ASC(?Label) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
	
	/**
	 * Lookup any properties of either of the following:
	 *  * mapped subject classes, of the subjectClassUri
	 *  * mapped data tables or of their rows, having an inqle:subject of the subjectClassUri
	 * @param subjectClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupDataPropertiesAboutSubject(String subjectClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_PROPERTY_DATASET_ROLE_ID));
		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
		String sparql = getSparqlFindSubjectProperties(subjectClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}
	
	/**
	 * Lookup any properties of mapped subject classes of URI subjectClassUri
	 * @param subjectClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupSubjectProperties(String subjectClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_PROPERTY_DATASET_ROLE_ID));
		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
		String sparql = getSparqlFindAllProperties(subjectClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}
	
	/**
	 * Lookup any properties of data tables or of their rows, having an inqle:subject of the subjectClassUri
	 * @param subjectClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupAllDataProperties(String subjectClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_PROPERTY_DATASET_ROLE_ID));
		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
		String sparql = getSparqlFindAllProperties(subjectClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}

}
