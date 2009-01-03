/**
 * 
 */
package org.inqle.experiment.rapidminer;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.InternalDataset;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.views.SparqlView;
import org.inqle.ui.rap.views.SparqlView2;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class ExperimentsView2 extends SparqlView2 {

	private static final Logger log = Logger.getLogger(ExperimentsView2.class);

	public static final String ID = "org.inqle.experiment.rapidminer.experimentsView";

	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX dc: <" + RDF.DC + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?uri ?Creation_Date ?Subject_Class ?Experiment_Subject ?Experiment_Label ?Experiment_Attributes ?Correlation ?Root_Mean_Squared_Error\n" +
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
			". OPTIONAL { ?uri inqle:experimentAttributeQnameRepresentation ?Experiment_Attributes } \n" +
//			". OPTIONAL { ?uri inqle:experimentAttributeArcs ?experimentAttributeArcs\n" +
//			"  . ?experimentAttributeArcs inqle:qnameRepresentation ?Experiment_Attributes}\n" +
			". OPTIONAL { ?uri inqle:correlation ?Correlation }\n" +
			". OPTIONAL { ?uri inqle:root_mean_squared_error ?Root_Mean_Squared_Error }\n" +
			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		return sparql;
	}
	
	@Override
	public NamedModel getNamedModel() {
		Persister persister = Persister.getInstance();
		InternalDataset dataset = persister.getInternalDataset(ExperimentResult.EXPERIMENTS_DATASET);
		return dataset;
	}

}
