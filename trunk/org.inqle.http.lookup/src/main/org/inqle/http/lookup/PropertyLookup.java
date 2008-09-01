package org.inqle.http.lookup;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.sdb.Queryer;
import org.inqle.data.rdf.jena.util.DatafileUtil;
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
public class PropertyLookup {

	private static final Logger log = Logger.getLogger(PropertyLookup.class);
	public static final Object QUERY_HEADER_URI = "Property_URI";
	public static final Object QUERY_HEADER_LABEL = "Label";
	public static final Object QUERY_HEADER_COMMENT = "Comment";
	/**
	 * Generates SPARQL, to find all properties of all instances of all 
	 * subclasses of inqle:Data, 
	 * which have the provided subjectClassUri as an inqle:subject
	 * That is, this finds properties of data rows having a particular 
	 * inqle:subject.
	 * @param subjectClassUri
	 * @param limit
	 * @param offset
	 * @return the SQPRQL string
	 */
	@Deprecated
	public static String getSparqlFindMappedDataPropertiesAboutSubject(String subjectClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?" + QUERY_HEADER_URI + " ?Column_Header ?" + QUERY_HEADER_LABEL + " ?" + QUERY_HEADER_COMMENT + " \n" +
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
	 * Generates SPARQL, to find all properties that have been mapped to 
	 * the provided OWL class.
	 * That is, this finds properties of DataSubjects.
	 * @param subjectClassUri
	 * @param limit
	 * @param offset
	 * @return the SQPRQL string
	 */
	@Deprecated
	public static String getSparqlFindMappedSubjectProperties(String subjectClassUri, int limit, int offset) {
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
	 * @return the SQPRQL string
	 */
	@Deprecated
	public static String getSparqlFindAllMappedProperties(String subjectClassUri, int limit, int offset) {
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
	 * Generate SPARQL for finding properties of inqle:Data class, which themselves have inqle:subject of
	 * the provided RDF class URI
	 * @param searchTerm
	 * @param subjectClassUri the URI of the subject class, which the 
	 * @param limit
	 * @param offset
	 * @return the SQPRQL string
	 * 
	 * TODO standardize nomenclature: inqle:Data and inqle:Subject, and use "Data" and "Subject" in method names
	 */
	public static String getSparqlFindDataProperties(String subjectClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX pf: <" + RDF.PF + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?Property_URI ?Property_Type ?Label ?Comment \n" +
				"{\n" +
				"GRAPH ?g {\n" +
						"?Property_URI rdfs:subPropertyOf inqle:DataProperty . \n" +
						". ?Property_URI rdfs:subPropertyOf ?Property_Type \n" +
						". ?Property_URI rdfs:domain ?DataSubjectAnonClass \n" +
						". ?DataSubjectAnonClass inqle:subject <" + subjectClassUri + "> \n" +
						". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
						". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
				"} } ORDER BY ASC(?Label) \n" +
				"LIMIT " + limit + " OFFSET " + offset;
			return sparql;
		}
	
	/**
	 * Generate SPARQL for finding properties of inqle:Subject class
	 * @param searchTerm
	 * @param subjectClassUri the URI of the subject class, which the 
	 * @param limit
	 * @param offset
	 * @return the SQPRQL string
	 * 
	 * TODO standardize nomenclature: inqle:Data and inqle:Subject, and use "Data" and "Subject" in method names
	 * TODO add inqle:SubjectProperty and inqle:DataProperty to the RDF class.
	 */
	public static String getSparqlFindSubjectProperties(String subjectClassUri, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT DISTINCT ?Property_URI ?Property_Type ?Label ?Comment \n" +
			"{\n" +
			"GRAPH ?g {\n" +
					"?Property_URI rdfs:subPropertyOf inqle:SubjectProperty \n" +
					". ?Property_URI rdfs:subPropertyOf ?Property_Type \n" +
					". ?Property_URI rdfs:domain <" + subjectClassUri + "> \n" +
					". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
					". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
			"} } ORDER BY ASC(?Label) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
	
	/**
	 * Generate SPARQL for finding properties of any class;
	 * usable for querying native OWL/RDFS vocabularies like SKOS, Geonames, etc.
	 * @param searchTerm
	 * @param subjectClassUri the URI of the subject class, which the 
	 * @param limit
	 * @param offset
	 * @return the SQPRQL string
	 */
	public static String getSparqlFindProperties(String subjectClassUri, int limit, int offset) {
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
			"PREFIX owl: <" + RDF.OWL + ">\n" + 
			"PREFIX pf: <" + RDF.PF + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT DISTINCT ?Property_URI ?Property_Type ?Label ?Comment \n" +
			"{\n" +
			"GRAPH ?g {\n" +
					"?Property_URI rdfs:domain <" + subjectClassUri + "> \n" +
					". OPTIONAL { ?Property_URI rdfs:label ?Label }\n" +
					". OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
			"} } ORDER BY ASC(?Label) \n" +
			"LIMIT " + limit + " OFFSET " + offset;
		return sparql;
	}
	
	/**
	 * Generate SPARQL for finding properties of inqle:Data class, which themselves have inqle:subject of
	 * the provided RDF class URI
	 * @param searchTerm
	 * @param subjectClassUri the URI of the subject class, which the 
	 * @param limit
	 * @param offset
	 * @return the SQPRQL string
	 * 
	 * TODO standardize nomenclature: inqle:Data and inqle:Subject, and use "Data" and "Subject" in method names
	 */
	public static String getSparqlFindDataAndSubjectProperties(String subjectClassUri, int limit, int offset) {
			String sparql = 
				"PREFIX rdf: <" + RDF.RDF + ">\n" + 
				"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
				"PREFIX owl: <" + RDF.OWL + ">\n" + 
				"PREFIX pf: <" + RDF.PF + ">\n" + 
				"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
				"SELECT DISTINCT ?Property_URI ?Property_Type ?Label ?Comment \n" +
				"{ GRAPH ?g {\n" +
						"{ ?Property_URI rdfs:subPropertyOf inqle:DataProperty \n" +
//						". ?Property_URI rdfs:subPropertyOf ?Property_Type \n" +
						"  . LET(?Property_Type := str(inqle:DataProperty)) \n" +
						"  . ?Property_URI rdfs:domain ?DataSubjectAnonClass \n" +
						"  . ?DataSubjectAnonClass inqle:subject <" + subjectClassUri + "> \n" +
					"} UNION {\n" +
						"  ?Property_URI rdfs:subPropertyOf inqle:SubjectProperty \n" +
//						"  . ?Property_URI rdfs:subPropertyOf ?Property_Type \n" +
						"  . LET(?Property_Type := str(inqle:SubjectProperty)) \n" +
						"  . ?Property_URI rdfs:domain <" + subjectClassUri + "> \n" +
						"  . OPTIONAL { ?Property_URI rdfs:label ?Label } \n" +
						"  . OPTIONAL { ?Property_URI rdfs:comment ?Comment } \n" +
				"} } } ORDER BY ASC(?Property_Type) \n" +
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
	 * @return the SQPRQL string
	 */
	public static String lookupDataPropertiesAboutSubject(String subjectClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_PROPERTY_DATASET_ROLE_ID));
		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
		String sparql = getSparqlFindMappedDataPropertiesAboutSubject(subjectClassUri, countSearchResults, offset);
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
	 * @return the SQPRQL string
	 */
	public static String lookupSubjectProperties(String subjectClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.addNamedModel(persister.getInternalDataset(Data.DATA_PROPERTY_DATASET_ROLE_ID));
		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
		String sparql = getSparqlFindMappedSubjectProperties(subjectClassUri, countSearchResults, offset);
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
//		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
//		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
//		String sparql = getSparqlFindAllMappedProperties(subjectClassUri, countSearchResults, offset);
		String sparql = getSparqlFindDataAndSubjectProperties(subjectClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}
	
	/**
	 * Lookup any properties of data tables or of their rows, from any RDFS/OWL vocabulary
	 * @param subjectClassUri the URI of the class 
	 * @param countSearchResults
	 * @param offset
	 * @return
	 */
	public static String lookupPropertiesInSchemaFiles(String subjectClassUri, int countSearchResults, int offset) {
		Persister persister = Persister.getInstance();
		QueryCriteria queryCriteria = new QueryCriteria();
		
		log.info("Get/Create OntModel...");
		queryCriteria.setSingleModel(persister.getSchemaFilesOntModel());
		
//		queryCriteria.addNamedModel(persister.getInternalDataset(DataMapping.MAPPING_DATASET_ROLE_ID));
//		DatafileUtil.addDatafiles(queryCriteria, InqleInfo.getRdfSchemaFilesDirectory());
//		String sparql = getSparqlFindAllMappedProperties(subjectClassUri, countSearchResults, offset);
		String sparql = getSparqlFindProperties(subjectClassUri, countSearchResults, offset);
		log.info("Querying w/ this sparql:\n" + sparql);
		queryCriteria.setQuery(sparql);
		String resultXml = Queryer.selectXml(queryCriteria);
		return resultXml;
	}

}
