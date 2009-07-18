package org.inqle.http.lookup;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.QueryCriteria;
import org.inqle.data.rdf.jena.QueryCriteriaFactory;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jena.util.DatafileUtil;
import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;

import com.hp.hpl.jena.query.larq.HitLARQ;
import com.hp.hpl.jena.query.larq.IndexLARQ;
import com.hp.hpl.jena.rdf.model.Model;

public class TableMappingsSearcher {

	public static final Logger log = Logger.getLogger(TableMappingsSearcher.class);
	public static final String MAPPING_URI = "Mapping_URI";
	public static final String MAPPING_NAME = "Mapping_Name";
	public static final String MAPPING_DATE = "Mapping_Date";
	public static final String MAPPING_ID = "Mapping_ID";
	public static final String MAPPING_DESCRIPTION = "Mapping_Description";

	public static String lookupMappings(String headerText, int countSearchResults, int offset) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQuery(getSparqlFindMatchingMappings(headerText, countSearchResults, offset));
		queryCriteria.addDatamodel(DataMapping.MAPPING_DATASET_ROLE_ID);
		String matchingMappingsXml = Queryer.selectXml(queryCriteria);
		return matchingMappingsXml;
	}

	private static String getSparqlFindMatchingMappings(String headerText,
			int countSearchResults, int offset) {
		String sparql = " PREFIX inqle: <" + RDF.INQLE + "> \n " + 
			" PREFIX xsd: <" + RDF.XSD + "> \n " + 
			" SELECT ?" + MAPPING_DATE + " ?" + MAPPING_URI + " ?" + MAPPING_ID + " ?" + MAPPING_NAME + " ?" + MAPPING_DESCRIPTION + "\n " +
			" { \n " +
			" GRAPH ?g { \n " +
			" ?Mapping_URI a inqle:TableMapping \n " +
			" . ?Mapping_URI inqle:mappedText \"" + headerText + "\"^^xsd:string \n" +
			" . ?Mapping_URI inqle:id ?Mapping_ID \n " +
			" . ?Mapping_URI inqle:creationDate ?Mapping_Date \n " +
			" . OPTIONAL { ?Mapping_URI <" + RDF.NAME_PREDICATE + "> ?Mapping_Name } \n " +
			" . OPTIONAL { ?Mapping_URI <" + RDF.DESCRIPTION_PREDICATE + "> ?Mapping_Description } \n " +
			"} } ORDER BY DESC(?Mapping_Date) \n" +
			"LIMIT " + countSearchResults + " OFFSET " + offset;
		return sparql;
	}
}
