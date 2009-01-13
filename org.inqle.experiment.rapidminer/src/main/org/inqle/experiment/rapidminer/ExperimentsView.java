/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.InternalDataset;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.views.SparqlView;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class ExperimentsView extends SparqlView {

	private static final Logger log = Logger.getLogger(ExperimentsView.class);

	public static final String ID = "org.inqle.experiment.rapidminer.experimentsView";

	@Override
	/**
	 * This view is ready to display upon creation, so refresh (and show) the view
	 */
	public void createPartControl(Composite parent) {
		linkColumn = "URI";
		hideUriColumn = false;
		super.createPartControl(parent);
		refreshView();
	}
	
	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX dc: <" + RDF.DC + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?URI ?Creation_Date ?Subject_Class ?Experiment_Subject ?Experiment_Label ?Experiment_Attributes ?Correlation ?Root_Mean_Squared_Error\n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?URI a ?classUri\n" +
			"  . ?classUri <" + RDF.JAVA_CLASS + "> \"" + ExperimentResult.class.getName() + "\" \n" +
			". ?URI inqle:id ?id \n" +
			". ?URI inqle:creationDate ?Creation_Date \n" +
//			". OPTIONAL { ?URI dc:name ?Name }\n" +
			". OPTIONAL { ?URI inqle:experimentSubjectClass ?Subject_Class } \n" +
			". OPTIONAL { ?URI inqle:experimentSubjectArc ?experimentSubjectArc \n" +
			"  . ?experimentSubjectArc inqle:qnameRepresentation ?Experiment_Subject }\n" +
			". OPTIONAL { ?URI inqle:experimentLabelArc ?experimentLabelArc \n" +
			"  . ?experimentLabelArc inqle:qnameRepresentation ?Experiment_Label }\n" +
			". OPTIONAL { ?URI inqle:experimentAttributeQnameRepresentation ?Experiment_Attributes } \n" +
//			". OPTIONAL { ?URI inqle:experimentAttributeArcs ?experimentAttributeArcs\n" +
//			"  . ?experimentAttributeArcs inqle:qnameRepresentation ?Experiment_Attributes}\n" +
			". OPTIONAL { ?URI inqle:correlation ?Correlation }\n" +
			". OPTIONAL { ?URI inqle:root_mean_squared_error ?Root_Mean_Squared_Error }\n" +
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
