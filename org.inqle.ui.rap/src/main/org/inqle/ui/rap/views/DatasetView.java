package org.inqle.ui.rap.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jena.util.SubjectClassLister;
import org.inqle.ui.rap.widgets.ResultSetTable;

/**
 * @author David Donohue
 * May 6, 2008
 */
public class DatasetView extends SparqlView {

	private static final Logger log = Logger.getLogger(DatasetView.class);

	public static final String ID = "org.inqle.ui.rap.views.DatasetView";
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		currentSortColumn = ResultSetTable.URI_VARIABLE;
		currentSortDirection = SparqlView.ASC;
		hideUriColumn = false;
		linkColumn = ResultSetTable.URI_VARIABLE;
		linkUriOnly = true;
	}
	
	//TODO optional fields will render the columns retrieved on any pass thru variable. Must extract columns from the query
	@Override
	public String getSparql() {
		
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
		return SubjectClassLister.getSparqlSelectUncommonClassesTable();
	}
	
	public void widgetSelected(SelectionEvent event) {
		Object source = event.getSource();
		if (source instanceof Link) {
			Link link = (Link)source;
			Object data = link.getData();
			if (data==null) return;
//			log.info(data + " clicked.");
//			ClassView classView = new ClassView();
			
			ClassView classView = (ClassView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ClassView.ID);
			if (classView==null) {
				try {
					classView = (ClassView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ClassView.ID);
				} catch (PartInitException e) {
					log.error("Error showing view: " + ClassView.ID, e);
				}
			}
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(classView);
			classView.setNamedModel(getNamedModel());
			classView.setClassUri(data.toString());
			classView.setTitleText("Things of type: <" + data.toString() + ">");
			log.info("Refreshing Class View with dataset: " + getNamedModel() + " and class URI: " + data.toString());
			classView.refreshView();
//			classView.setFocus();
		}
	}
}

/*
//query for subjects
ResultSetRewindable subjectsRS = SubjectClassLister.queryGetUncommonSubjectsRS(getDataset().getId());
resultSetTable.setResultSet(subjectsRS);
resultSetTable.setSortable(false);
resultSetTable.setLinkColumn(SubjectClassLister.CLASS_URI_VAR);
resultSetTable.renderTable(this);
*/