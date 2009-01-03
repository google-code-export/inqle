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
@Deprecated
public class ExperimentsView_orig extends SparqlView {

	private static final Logger log = Logger.getLogger(ExperimentsView_orig.class);

	public static final String ID = "org.inqle.experiment.rapidminer.experimentsView";

	private static final String[] PROPERTY_NAMES = {
		"Creation_Date",
		"id",
		"Subject_Class",
		"Experiment_Subject",
		"Experiment_Attributes",
		"Experiment_Label",
		"Correlation",
		"Root_Mean_Squared_Error"
	};

	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX dc: <" + RDF.DC + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?id ?Creation_Date ?Subject_Class ?Experiment_Subject ?Experiment_Attributes ?Experiment_Label ?Correlation ?Root_Mean_Squared_Error\n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?uri a ?classUri\n" +
			"  . ?classUri <" + RDF.JAVA_CLASS + "> \"" + ExperimentResult.class.getName() + "\" \n" +
			". ?uri inqle:id ?id \n" +
			". ?uri inqle:creationDate ?Creation_Date \n" +
//			". OPTIONAL { ?uri dc:name ?Name }\n" +
			". OPTIONAL { ?uri inqle:experimentSubjectClass ?Subject_Class } \n" +
			". OPTIONAL { ?uri inqle:experimentSubjectArc ?experimentSubjectArc \n" +
			"  . ?experimentSubjectArc inqle:qnameRepresentation ?Experiment_Subject }\n" +
			". OPTIONAL { ?uri inqle:experimentLabelArc ?experimentLabelArc \n" +
			"  . ?experimentLabelArc inqle:qnameRepresentation ?Experiment_Label }\n" +
			". OPTIONAL { ?uri inqle:experimentAttributeArcs ?experimentAttributeArcs\n" +
			"  . ?experimentAttributeArcs inqle:qnameRepresentation ?Experiment_Attributes}\n" +
			". OPTIONAL { ?uri inqle:correlation ?Correlation }\n" +
			". OPTIONAL { ?uri inqle:root_mean_squared_error ?Root_Mean_Squared_Error }\n" +
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
