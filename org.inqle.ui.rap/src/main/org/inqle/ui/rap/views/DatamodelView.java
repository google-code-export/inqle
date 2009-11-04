package org.inqle.ui.rap.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Queryer;
import org.inqle.data.rdf.jena.util.SubjectClassLister;
import org.inqle.ui.rap.widgets.ResultSetTable;
import org.inqle.ui.rap.widgets.ResultSetTable.UriValData;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class DatamodelView extends SparqlView {

	private static final Logger log = Logger.getLogger(DatamodelView.class);

	public static final String ID = "org.inqle.ui.rap.views.DatamodelView";
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		currentSortColumn = ResultSetTable.URI_VARIABLE;
		currentSortDirection = SparqlView.ASC;
		hideUriColumn = false;
		linkColumn = SubjectClassLister.CLASS_URI_VAR;
		linkUriOnly = true;
	}
	
	@Override
	public String getSparql() {
		String s = "PREFIX rdfs: <" + RDF.RDFS + "> \n" +
		"SELECT DISTINCT " +
		"?" + SubjectClassLister.CLASS_URI_VAR + " ?Name ?Description { GRAPH ?anyGraph { \n " +
		"?subject a ?" + SubjectClassLister.CLASS_URI_VAR + " ." +
		"OPTIONAL { ?" + SubjectClassLister.CLASS_URI_VAR + " rdfs:label ?Name} . \n" +
		"OPTIONAL { ?" + SubjectClassLister.CLASS_URI_VAR + " rdfs:comment ?Description} . \n" +
		 	Queryer.getSparqlClauseFilterCommonClasses("?" + SubjectClassLister.CLASS_URI_VAR) +
		"\n} } \n" +
		" ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n" +
		" LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
		log.info("SPARQL lookup subjects in datamodel:" + s);
		return s;
//		String sparql = 
//			"PREFIX rdfs: <" + RDF.RDFS + ">\n" + 
//			"SELECT ?" + ResultSetTable.URI_VARIABLE + " ?Name ?Description \n" +
//			"{\n" +
//			"GRAPH ?g {\n" +
//			"?" + ResultSetTable.URI_VARIABLE + " a <" + getClassUri() + "> \n" +
//			". OPTIONAL { ?" + ResultSetTable.URI_VARIABLE + " rdfs:label ?Name } \n" +
//			". OPTIONAL { ?" + ResultSetTable.URI_VARIABLE + " rdfs:comment ?Description }\n" +
//			"\n} } ORDER BY " + getCurrentSortDirection() + "(?" + getCurrentSortColumn() + ") \n";
//		sparql +=  "LIMIT " + String.valueOf(getRecordCount()) + " OFFSET " + String.valueOf(getOffset());
//		return sparql;
//		String sparql = SubjectClassLister.getSparqlSelectUncommonClassesTable();
		
//		return sparql;
	}
	
	public void widgetSelected(SelectionEvent event) {
		Object source = event.getSource();
		if (source instanceof Link) {
			Link link = (Link)source;
			Object data = link.getData();
			if (data==null) return;
			
			if (data instanceof UriValData) {
				ClassView classView = (ClassView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ClassView.ID);
				if (classView==null) {
					try {
						classView = (ClassView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ClassView.ID);
					} catch (PartInitException e) {
						log.error("Error showing view: " + ClassView.ID, e);
					}
				}
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(classView);
				classView.setDatamodelId(datamodelId);
				classView.setClassUri(data.toString());
				classView.setTitleText("Things of type: <" + data.toString() + ">");
				log.info("Refreshing Class View with datamodel: " + datamodelId + " and class URI: " + data.toString());
				classView.refreshView();
			}
		}
		
		super.widgetSelected(event);
	}
}