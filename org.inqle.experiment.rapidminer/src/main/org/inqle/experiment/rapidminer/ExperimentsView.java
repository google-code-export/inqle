/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.ui.rap.views.SparqlView;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class ExperimentsView extends SparqlView {

	private static final Logger log = Logger.getLogger(ExperimentsView.class);

	public static final String ID = "org.inqle.experiment.rapidminer.experimentsView";

	private static final String[] PROPERTY_NAMES = {
		"creationDate",
		"id",
		"experimentSubject",
		"experimentAttributes",
		"experimentLabel",
		"correlation",
		"root_mean_squared_error"
	};

	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX dc: <" + RDF.DC + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?id ?creationDate ?name ?experimentSubject ?experimentAttributes ?experimentLabel ?correlation ?root_mean_squared_error\n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?uri inqle:id ?id\n" +
			". ?uri inqle:creationDate ?creationDate\n" +
			". OPTIONAL { ?uri dc:name ?name }\n" +
			". OPTIONAL { ?uri inqle:experimentSubjectArc ?experimentSubjectArc \n" +
			"  . ?experimentSubjectArc inqle:stringRepresentation ?experimentSubject }\n" +
			". OPTIONAL { ?uri inqle:experimentLabelArc ?experimentLabelArc \n" +
			"  . ?experimentLabelArc inqle:stringRepresentation ?experimentLabel }\n" +
			". OPTIONAL { ?uri inqle:experimentAttributeArcs ?experimentAttributeArcs\n" +
			"  . ?experimentAttributeArcs inqle:stringRepresentation ?experimentAttributes}\n" +
			". OPTIONAL { ?uri inqle:correlation ?correlation }\n" +
			". OPTIONAL { ?uri inqle:root_mean_squared_error ?root_mean_squared_error }\n" +
			". ?uri a ?classUri\n" +
			"  . ?classUri <" + RDF.JAVA_CLASS + "> \"" + ExperimentResult.class.getName() + "\" \n" +
			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		return sparql;
	}

	@Override
	public List<String> getPropertyNames() {
		return Arrays.asList(PROPERTY_NAMES);
	}

	@Override
	public String getDatasetRole() {
		return ExperimentResult.EXPERIMENTS_DATASET;
	}
}
