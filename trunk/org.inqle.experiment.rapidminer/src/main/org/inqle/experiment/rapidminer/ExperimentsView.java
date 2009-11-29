/**
 * 
 */
package org.inqle.experiment.rapidminer;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.IDBConnector;
import org.inqle.data.rdf.jena.IDatabase;
import org.inqle.data.rdf.jena.SystemDatamodel;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.views.SparqlView;
import org.inqle.ui.rap.views.SubjectStatementsView;
import org.inqle.ui.rap.widgets.ResultSetTable.UriValData;

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
		super.createPartControl(parent);
		linkColumn = InqleInfo.URI_VARIABLE;
		linkUriOnly = true;
		hideUriColumn = false;
		setTitleText("Log of Experiments Done");
		refreshView();
	}
	
	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
		String sparql = 
			"PREFIX rdf: <" + RDF.RDF + ">\n" + 
			"PREFIX dc: <" + RDF.DC + ">\n" + 
			"PREFIX inqle: <" + RDF.INQLE + ">\n" + 
			"SELECT ?" + InqleInfo.URI_VARIABLE + " ?Creation_Date ?Subject_Class ?Experiment_Subject ?Experiment_Label ?Experiment_Attributes ?Correlation ?Accuracy\n" +
			"{\n" +
			"GRAPH ?g {\n" +
			"?" + InqleInfo.URI_VARIABLE + " a ?classUri\n" +
			"  . ?classUri <" + RDF.JAVA_CLASS + "> \"" + PerformanceVectorResult.class.getName() + "\" \n" +
			". ?" + InqleInfo.URI_VARIABLE + " inqle:id ?id \n" +
			". ?" + InqleInfo.URI_VARIABLE + " inqle:creationDate ?Creation_Date \n" +
//			". OPTIONAL { ?" + ResultSetTable.URI_VARIABLE + " dc:name ?Name }\n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " inqle:experimentSubjectClass ?Subject_Class } \n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " inqle:experimentSubjectArc ?experimentSubjectArc \n" +
			"  . ?experimentSubjectArc inqle:qnameRepresentation ?Experiment_Subject }\n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " inqle:experimentLabelArc ?experimentLabelArc \n" +
			"  . ?experimentLabelArc inqle:qnameRepresentation ?Experiment_Label }\n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " inqle:experimentAttributeQnameRepresentation ?Experiment_Attributes } \n" +
//			". OPTIONAL { ?" + ResultSetTable.URI_VARIABLE + " inqle:experimentAttributeArcs ?experimentAttributeArcs\n" +
//			"  . ?experimentAttributeArcs inqle:qnameRepresentation ?Experiment_Attributes}\n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " inqle:correlation ?Correlation }\n" +
			". OPTIONAL { ?" + InqleInfo.URI_VARIABLE + " inqle:accuracy ?Accuracy }\n" +
			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		log.info("SPARQL=" + sparql);
		return sparql;
	}
	
	@Override
	public String getDatamodelId() {
//		Persister persister = Persister.getInstance();
//		SystemDatamodel datamodel = persister.getSystemDatamodel(IExperimentResult.EXPERIMENT_RESULTS_DATAMODEL_NAME);
//		return datamodel;
		return Persister.getDatamodelId(IExperimentResult.EXPERIMENT_RESULTS_DB_ID, IDBConnector.SUBDATABASE_SYSTEM, IExperimentResult.EXPERIMENT_RESULTS_DATAMODEL_NAME);
//		return IExperimentResult.EXPERIMENT_RESULTS_DATAMODEL_ID;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
		
		Object source = event.getSource();
		if (source instanceof Link) {
			Link link = (Link)source;
			Object data = link.getData();
			if (data==null) return;
			
			if (data instanceof UriValData) {
				UriValData uriValData = (UriValData) data;
				SubjectStatementsView ssView = (SubjectStatementsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SubjectStatementsView.ID);
				if (ssView==null) {
					try {
						ssView = (SubjectStatementsView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SubjectStatementsView.ID);
					} catch (PartInitException e) {
						log.error("Error showing view: " + SubjectStatementsView.ID, e);
					}
				}
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(ssView);
				ssView.setDatamodelId(datamodelId);
				ssView.setSubjectUri(uriValData.getUriVal());
				ssView.setTitleText("Properties of thing: <" + uriValData.getUriVal() + ">");
				log.info("Refreshing Subject Statements View with datamodel: " + datamodelId + " and instance URI: " + data.toString());
				ssView.refreshView();
//				ssView.setFocus();
				
				return;
			}
		}
		
		super.widgetSelected(event);
	}

}
